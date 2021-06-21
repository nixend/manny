package com.nixend.manny.core.route;

import com.google.common.collect.Lists;
import com.nixend.manny.common.model.MethodData;
import com.nixend.manny.common.model.RouteData;
import com.nixend.manny.common.model.ServiceData;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author panyox
 */
public class RouteMapping {

    /**
     * Rpc services [rpc]=>[][]
     */
    private static final ConcurrentMap<String, List<ServiceData>> serviceMap = new ConcurrentHashMap<>();

    /**
     * Service methods [serviceId]=>[][]
     */
    private static final ConcurrentMap<String, List<MethodData>> methodMap = new ConcurrentHashMap<>();

    private static volatile RouteMapping instance;

    public static RouteMapping getInstance() {
        if (instance == null) {
            synchronized (RouteMapping.class) {
                if (instance == null) {
                    instance = new RouteMapping();
                }
            }
        }
        return instance;
    }

    public RouteMapping() {

    }

    public List<ServiceData> getService(String rpcType) {
        return serviceMap.get(rpcType);
    }

    public void registerService(ServiceData data) {
        String rpcType = data.getRpcType();
        if (!StringUtils.hasLength(rpcType)) {
            return;
        }
        if (serviceMap.containsKey(rpcType)) {
            List<ServiceData> services = serviceMap.get(rpcType);
            services.removeIf(s -> s.getId().equals(data.getId()));
            services.add(data);
            serviceMap.put(rpcType, services);
        } else {
            serviceMap.put(rpcType, Lists.newArrayList(data));
        }
    }

    public void removeService(ServiceData data) {
        List<ServiceData> services = serviceMap.get(data.getRpcType());
        if (services == null) {
            return;
        }
        services.removeIf(s -> s.getId().equals(data.getId()));
    }

    public List<MethodData> getMethods(String serviceId) {
        return methodMap.get(serviceId);
    }

    public void registerMethod(MethodData data) {
        String serviceId = data.getServiceId();
        if (methodMap.containsKey(serviceId)) {
            List<MethodData> methods = methodMap.get(serviceId);
            methods.removeIf(m -> m.getId().equals(data.getId()));
            methods.add(data);
            methodMap.put(serviceId, methods);
        } else {
            methodMap.put(serviceId, Lists.newArrayList(data));
        }
    }

    public void removeMethod(MethodData data) {
        Optional.ofNullable(data).ifPresent(d -> {
            List<MethodData> dataList = methodMap.get(d.getServiceId());
            Optional.ofNullable(dataList).ifPresent(list -> list.removeIf(m -> m.getId().equals(d.getId())));
        });
    }

    public void registerRoute(RouteData routeData) {
        if (Objects.nonNull(routeData)) {
            if (Objects.nonNull(routeData.getService())) {
                registerService(routeData.getService());
            }
            if (Objects.nonNull(routeData.getMethod())) {
                registerMethod(routeData.getMethod());
            }
        }
    }
}
