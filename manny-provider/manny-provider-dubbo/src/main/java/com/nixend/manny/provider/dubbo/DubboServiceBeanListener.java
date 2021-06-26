package com.nixend.manny.provider.dubbo;

import com.nixend.manny.common.constant.Constants;
import com.nixend.manny.common.enums.RpcType;
import com.nixend.manny.common.model.MethodData;
import com.nixend.manny.common.model.MethodParam;
import com.nixend.manny.common.model.RouteData;
import com.nixend.manny.common.model.ServiceData;
import com.nixend.manny.common.thread.TaskExecutor;
import com.nixend.manny.common.utils.PathUtils;
import com.nixend.manny.common.utils.StringUtils;
import com.nixend.manny.core.annotation.RequestRoute;
import com.nixend.manny.registry.api.RegistryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.ServiceBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author panyox
 */
@Slf4j
public class DubboServiceBeanListener implements ApplicationListener<ContextRefreshedEvent> {

    private RegistryService registryService;

    private final AtomicBoolean registered = new AtomicBoolean(false);

    public DubboServiceBeanListener(RegistryService registryService) {
        this.registryService = registryService;
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
            throw new IllegalArgumentException("RequestRoute value can not empty in Service: " + serviceBean.getInterface());
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
                .retry(requestRoute.retry())
                .timeout(requestRoute.timeout())
                .build();
        Method[] methods = ReflectionUtils.getUniqueDeclaredMethods(clazz);
        for (Method method : methods) {
            if (AnnotatedElementUtils.hasAnnotation(method, RequestRoute.class)) {
                RequestRoute route = AnnotatedElementUtils.getMergedAnnotation(method, RequestRoute.class);
                if (Objects.nonNull(route)) {
                    String clearedPath = PathUtils.clearPath(route.value());
                    String path = clearedPath.length() == 0 ? Constants.DEFAULT_INDEX : clearedPath;
                    MethodParam parameters = MethodParam.parse(method);
                    MethodData methodData = MethodData.builder()
                            .id(path)
                            .serviceId(serviceId)
                            .name(method.getName())
                            .path(path)
                            .parameters(parameters)
                            .httpMethod(route.method().name())
                            .build();
                    RouteData routeData = RouteData.builder()
                            .service(serviceData)
                            .method(methodData)
                            .build();
                    registryService.registerRoute(routeData);
                }
            }
        }
    }
}
