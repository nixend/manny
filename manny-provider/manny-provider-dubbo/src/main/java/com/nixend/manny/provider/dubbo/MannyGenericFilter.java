package com.nixend.manny.provider.dubbo;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.nixend.manny.common.exception.MannyCodeException;
import com.nixend.manny.common.exception.MannyException;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.beanutil.JavaBeanAccessor;
import org.apache.dubbo.common.beanutil.JavaBeanDescriptor;
import org.apache.dubbo.common.beanutil.JavaBeanSerializeUtil;
import org.apache.dubbo.common.config.Configuration;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.io.UnsafeByteArrayInputStream;
import org.apache.dubbo.common.io.UnsafeByteArrayOutputStream;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.common.serialize.Serialization;
import org.apache.dubbo.common.utils.PojoUtils;
import org.apache.dubbo.common.utils.ReflectUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.apache.dubbo.rpc.service.GenericException;
import org.apache.dubbo.rpc.service.GenericService;
import org.apache.dubbo.rpc.support.ProtocolUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.stream.IntStream;

@Activate(
        group = {"provider"},
        order = -21000
)
public class MannyGenericFilter implements Filter, Filter.Listener {
    private static final Logger logger = LoggerFactory.getLogger(MannyGenericFilter.class);

    public MannyGenericFilter() {
    }

    public Result invoke(Invoker<?> invoker, Invocation inv) throws RpcException {
        if ((inv.getMethodName().equals("$invoke") || inv.getMethodName().equals("$invokeAsync")) && inv.getArguments() != null && inv.getArguments().length == 3 && !GenericService.class.isAssignableFrom(invoker.getInterface())) {
            String name = ((String) inv.getArguments()[0]).trim();
            String[] types = (String[]) inv.getArguments()[1];
            Object[] args = (Object[]) inv.getArguments()[2];

            try {
                Method method = ReflectUtils.findMethodByMethodSignature(invoker.getInterface(), name, types);
                Class<?>[] params = method.getParameterTypes();
                if (args == null) {
                    args = new Object[params.length];
                }

                if (types == null) {
                    types = new String[params.length];
                }

                if (args.length != types.length) {
                    throw new RpcException("GenericFilter#invoke args.length != types.length, please check your params");
                } else {
                    String generic = inv.getAttachment("generic");
                    if (StringUtils.isBlank(generic)) {
                        generic = RpcContext.getContext().getAttachment("generic");
                    }

                    if (!StringUtils.isEmpty(generic) && !ProtocolUtils.isDefaultGenericSerialization(generic) && !ProtocolUtils.isGenericReturnRawResult(generic)) {
                        if (ProtocolUtils.isGsonGenericSerialization(generic)) {
                            args = this.getGsonGenericArgs(args, method.getGenericParameterTypes());
                        } else if (ProtocolUtils.isJavaGenericSerialization(generic)) {
                            Configuration configuration = ApplicationModel.getEnvironment().getConfiguration();
                            if (!configuration.getBoolean("dubbo.security.serialize.generic.native-java-enable", false)) {
                                String notice = "Trigger the safety barrier! Native Java Serializer is not allowed by default.This means currently maybe being attacking by others. If you are sure this is a mistake, please set `dubbo.security.serialize.generic.native-java-enable` enable in configuration! Before doing so, please make sure you have configure JEP290 to prevent serialization attack.";
                                logger.error(notice);
                                throw new RpcException(new IllegalStateException(notice));
                            }

                            for (int i = 0; i < args.length; ++i) {
                                if (byte[].class != args[i].getClass()) {
                                    throw new RpcException("Generic serialization [nativejava] only support message type " + byte[].class + " and your message type is " + args[i].getClass());
                                }

                                try {
                                    UnsafeByteArrayInputStream is = new UnsafeByteArrayInputStream((byte[]) args[i]);

                                    try {
                                        args[i] = ((Serialization) ExtensionLoader.getExtensionLoader(Serialization.class).getExtension("nativejava")).deserialize((URL) null, is).readObject();
                                    } catch (Throwable var18) {
                                        try {
                                            is.close();
                                        } catch (Throwable var14) {
                                            var18.addSuppressed(var14);
                                        }

                                        throw var18;
                                    }

                                    is.close();
                                } catch (Exception var19) {
                                    throw new RpcException("Deserialize argument [" + (i + 1) + "] failed.", var19);
                                }
                            }
                        } else if (ProtocolUtils.isBeanGenericSerialization(generic)) {
                            for (int i = 0; i < args.length; ++i) {
                                if (!(args[i] instanceof JavaBeanDescriptor)) {
                                    throw new RpcException("Generic serialization [bean] only support message type " + JavaBeanDescriptor.class.getName() + " and your message type is " + args[i].getClass().getName());
                                }

                                args[i] = JavaBeanSerializeUtil.deserialize((JavaBeanDescriptor) args[i]);
                            }
                        } else if (ProtocolUtils.isProtobufGenericSerialization(generic)) {
                            if (args.length != 1 || !(args[0] instanceof String)) {
                                throw new RpcException("Generic serialization [protobuf-json] only support one " + String.class.getName() + " argument and your message size is " + args.length + " and type is" + args[0].getClass().getName());
                            }

                            try {
                                UnsafeByteArrayInputStream is = new UnsafeByteArrayInputStream(((String) args[0]).getBytes());

                                try {
                                    args[0] = ((Serialization) ExtensionLoader.getExtensionLoader(Serialization.class).getExtension("protobuf-json")).deserialize((URL) null, is).readObject(method.getParameterTypes()[0]);
                                } catch (Throwable var16) {
                                    try {
                                        is.close();
                                    } catch (Throwable var15) {
                                        var16.addSuppressed(var15);
                                    }

                                    throw var16;
                                }

                                is.close();
                            } catch (Exception var17) {
                                throw new RpcException("Deserialize argument failed.", var17);
                            }
                        }
                    } else {
                        try {
                            args = PojoUtils.realize(args, params, method.getGenericParameterTypes());
                        } catch (IllegalArgumentException var20) {
                            throw new RpcException(var20);
                        }
                    }

                    RpcInvocation rpcInvocation = new RpcInvocation(method, invoker.getInterface().getName(), invoker.getUrl().getProtocolServiceKey(), args, inv.getObjectAttachments(), inv.getAttributes());
                    rpcInvocation.setInvoker(inv.getInvoker());
                    rpcInvocation.setTargetServiceUniqueName(inv.getTargetServiceUniqueName());
                    return invoker.invoke(rpcInvocation);
                }
            } catch (ClassNotFoundException | NoSuchMethodException var21) {
                throw new RpcException(var21.getMessage(), var21);
            }
        } else {
            return invoker.invoke(inv);
        }
    }

    private Object[] getGsonGenericArgs(final Object[] args, Type[] types) {
        Gson gson = new Gson();
        return IntStream.range(0, args.length).mapToObj((i) -> {
            String str = args[i].toString();
            Type type = TypeToken.get(types[i]).getType();

            try {
                return gson.fromJson(str, type);
            } catch (JsonSyntaxException var7) {
                throw new RpcException(String.format("Generic serialization [%s] Json syntax exception thrown when parsing (message:%s type:%s) error:%s", "gson", str, type.toString(), var7.getMessage()));
            }
        }).toArray();
    }

    public void onResponse(Result appResponse, Invoker<?> invoker, Invocation inv) {
        if ((inv.getMethodName().equals("$invoke") || inv.getMethodName().equals("$invokeAsync")) && inv.getArguments() != null && inv.getArguments().length == 3 && !GenericService.class.isAssignableFrom(invoker.getInterface())) {
            String generic = inv.getAttachment("generic");
            if (StringUtils.isBlank(generic)) {
                generic = RpcContext.getContext().getAttachment("generic");
            }

            if (appResponse.hasException()) {
                Throwable appException = appResponse.getException();
                
                if (!(appException instanceof MannyException) && !(appException instanceof MannyCodeException)) {
                    appException = new GenericException((Throwable) appException);
                }
                appResponse.setException((Throwable) appException);
            }

            UnsafeByteArrayOutputStream os;
            if (ProtocolUtils.isJavaGenericSerialization(generic)) {
                try {
                    os = new UnsafeByteArrayOutputStream(512);
                    ((Serialization) ExtensionLoader.getExtensionLoader(Serialization.class).getExtension("nativejava")).serialize((URL) null, os).writeObject(appResponse.getValue());
                    appResponse.setValue(os.toByteArray());
                } catch (IOException var8) {
                    throw new RpcException("Generic serialization [nativejava] serialize result failed.", var8);
                }
            } else if (ProtocolUtils.isBeanGenericSerialization(generic)) {
                appResponse.setValue(JavaBeanSerializeUtil.serialize(appResponse.getValue(), JavaBeanAccessor.METHOD));
            } else if (ProtocolUtils.isProtobufGenericSerialization(generic)) {
                try {
                    os = new UnsafeByteArrayOutputStream(512);
                    ((Serialization) ExtensionLoader.getExtensionLoader(Serialization.class).getExtension("protobuf-json")).serialize((URL) null, os).writeObject(appResponse.getValue());
                    appResponse.setValue(os.toString());
                } catch (IOException var7) {
                    throw new RpcException("Generic serialization [protobuf-json] serialize result failed.", var7);
                }
            } else {
                if (ProtocolUtils.isGenericReturnRawResult(generic)) {
                    return;
                }

                appResponse.setValue(PojoUtils.generalize(appResponse.getValue()));
            }
        }

    }

    public void onError(Throwable t, Invoker<?> invoker, Invocation invocation) {
    }
}
