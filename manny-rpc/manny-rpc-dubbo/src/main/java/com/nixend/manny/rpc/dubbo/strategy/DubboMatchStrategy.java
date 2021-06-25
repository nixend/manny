package com.nixend.manny.rpc.dubbo.strategy;

import com.nixend.manny.common.enums.RpcType;
import com.nixend.manny.common.model.MethodData;
import com.nixend.manny.common.model.PathInfo;
import com.nixend.manny.common.model.RouteData;
import com.nixend.manny.common.model.ServiceData;
import com.nixend.manny.common.utils.PathUtils;
import com.nixend.manny.core.match.MatchStrategy;
import com.nixend.manny.core.route.RouteMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author panyox
 */
@Slf4j
public class DubboMatchStrategy implements MatchStrategy {

    private static volatile DubboMatchStrategy instance;

    public static DubboMatchStrategy getInstance() {
        if (instance == null) {
            synchronized (DubboMatchStrategy.class) {
                if (instance == null) {
                    instance = new DubboMatchStrategy();
                }
            }
        }
        return instance;
    }

    @Override
    public Mono<RouteData> matchRoute(ServerWebExchange exchange) {
        RouteMapping routeMapping = RouteMapping.getInstance();
        ServerHttpRequest request = exchange.getRequest();
        PathInfo pathInfo = PathUtils.parse(request.getPath().value());
        if (pathInfo == null) {
            return Mono.empty();
        }
        List<ServiceData> services = routeMapping.getService(RpcType.DUBBO.getName());
        ServiceData serviceData = services.stream().filter(service -> service.getId().equals(pathInfo.getServiceId())).findFirst().orElse(null);
        if (serviceData == null) {
            log.warn("service not matching, pathInfo: {}", pathInfo);
            return Mono.empty();
        }
        List<MethodData> methods = routeMapping.getMethods(serviceData.getId());
        MethodData methodData = methods.stream().filter(method -> method.getId().equals(pathInfo.getMethodId())).findFirst().orElse(null);
        if (methodData == null) {
            log.warn("method not matching, pathInfo: {}", pathInfo);
            return Mono.empty();
        }
        HttpMethod httpMethod = request.getMethod();
        if (!httpMethod.matches(methodData.getHttpMethod())) {
            log.warn("method httpMethod not matching, pathInfo: {}", pathInfo);
            return Mono.empty();
        }
        RouteData routeData = RouteData.builder()
                .service(serviceData)
                .method(methodData)
                .build();
        return Mono.just(routeData);
    }
}
