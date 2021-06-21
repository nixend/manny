package com.nixend.manny.client.dubbo;

import com.nixend.manny.common.constant.Constants;
import com.nixend.manny.common.enums.RpcType;
import com.nixend.manny.common.model.MethodData;
import com.nixend.manny.common.model.RouteData;
import com.nixend.manny.common.model.ServiceData;
import com.nixend.manny.common.thread.TaskExecutor;
import com.nixend.manny.common.utils.PathUtils;
import com.nixend.manny.common.utils.StringUtils;
import com.nixend.manny.core.annotation.RequestRoute;
import com.nixend.manny.register.client.api.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.ServiceBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author panyox
 */
@Slf4j
public class DubboServiceBeanListener implements ApplicationListener<ContextRefreshedEvent> {

    private RegisterService registerService;

    private final AtomicBoolean registered = new AtomicBoolean(false);

    public DubboServiceBeanListener(RegisterService registerService) {
        this.registerService = registerService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent context) {
        if (!registered.compareAndSet(false, true)) {
            return;
        }
        Map<String, ServiceBean> serviceBean = context.getApplicationContext().getBeansOfType(ServiceBean.class);
        for (Map.Entry<String, ServiceBean> entry : serviceBean.entrySet()) {
            TaskExecutor.getInstance().execute(() -> processServiceBean(entry.getValue()));
        }
    }

    private void processServiceBean(final ServiceBean serviceBean) {
        Class<?> clazz = serviceBean.getRef().getClass();
        RequestRoute requestRoute = clazz.getAnnotation(RequestRoute.class);
        log.info("class: {} rr: {}", clazz, requestRoute);
        if (requestRoute == null) {
            return;
        }
        String servicePath = requestRoute.value();
        if (!StringUtils.hasLength(servicePath)) {
            throw new IllegalArgumentException("RequestRoute value is null in Service: " + serviceBean.getInterface());
        }
        String version = requestRoute.version();
        if (!PathUtils.checkVersion(version)) {
            throw new IllegalArgumentException("RequestRoute version is invalid in Service: " + serviceBean.getInterface());
        }
        RegistryConfig registryConfig = serviceBean.getRegistry();
        String serviceId = servicePath.startsWith(Constants.SLASH) ? String.format("%s%s", version, servicePath) : String.format("%s/%s", version, servicePath);
        ServiceData serviceData = ServiceData.builder()
                .id(serviceId)
                .version(version)
                .path(servicePath)
                .name(serviceBean.getInterface())
                .rpcType(RpcType.DUBBO.getName())
                .registryAddress(registryConfig.getAddress())
                .build();
        Method[] methods = ReflectionUtils.getUniqueDeclaredMethods(clazz);
        for (Method method : methods) {
            if (AnnotatedElementUtils.hasAnnotation(method, RequestRoute.class)) {
                RequestRoute route = AnnotatedElementUtils.getMergedAnnotation(method, RequestRoute.class);
                if (Objects.nonNull(route)) {
                    if (!StringUtils.hasLength(route.value())) {
                        throw new IllegalArgumentException("Method path should not empty in Service: " + serviceBean.getInterface() + " method: " + method.getName());
                    }
                    if (Constants.SLASH.equals(route.value())) {
                        throw new IllegalArgumentException("Method path is invalid in Service: " + serviceBean.getInterface() + " method: " + method.getName());
                    }
                    String path = PathUtils.clearPath(route.value());
                    LinkedHashMap<String, String> parameters = resolveMethodParameters(method);
                    MethodData methodData = MethodData.builder()
                            .id(path)
                            .serviceId(serviceId)
                            .name(method.getName())
                            .path(path)
                            .httpMethod(route.method().name())
                            .parameters(parameters)
                            .build();
                    RouteData routeData = RouteData.builder()
                            .service(serviceData)
                            .method(methodData)
                            .build();
                    registerService.registerRoute(routeData);
                }
            }
        }
    }

    private LinkedHashMap<String, String> resolveMethodParameters(Method method) {
        int paramCount = method.getParameterCount();
        Parameter[] parameters = method.getParameters();
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        for (int i = 0; i < paramCount; i++) {
            Parameter parameter = parameters[i];
            params.put(parameter.getName(), parameter.getType().getName());
            log.info("name: {} type: {}", parameter.getName(), parameter.getType().getName());
        }
        log.info("p: {}", params);
        return params;
    }
}