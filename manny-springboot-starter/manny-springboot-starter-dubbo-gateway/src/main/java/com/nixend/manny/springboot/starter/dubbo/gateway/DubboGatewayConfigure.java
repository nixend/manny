package com.nixend.manny.springboot.starter.dubbo.gateway;

import com.nixend.manny.core.ParamResolveService;
import com.nixend.manny.core.RouterDispatcherHandler;
import com.nixend.manny.core.exception.DefaultGlobalExceptionHandler;
import com.nixend.manny.core.filter.DataFilter;
import com.nixend.manny.core.response.DefaultResponseBuilder;
import com.nixend.manny.core.response.ResponseBuilder;
import com.nixend.manny.rpc.dubbo.DubboParamResolveService;
import com.nixend.manny.rpc.dubbo.DubboRouterHandler;
import com.nixend.manny.rpc.dubbo.DubboRouterHandlerMapping;
import com.nixend.manny.rpc.dubbo.listener.DubboRouteNotify;
import com.nixend.manny.rpc.dubbo.listener.DubboTagRouteNotify;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author panyox
 */
@Configuration
public class DubboGatewayConfigure {

    @Bean
    @ConditionalOnMissingBean(value = ResponseBuilder.class, search = SearchStrategy.ALL)
    public ResponseBuilder responseBuilder() {
        return new DefaultResponseBuilder();
    }


    @Bean
    public RouterDispatcherHandler dispatcherHandler(final ResponseBuilder responseBuilder) {
        return new RouterDispatcherHandler(responseBuilder);
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunction(final RouterDispatcherHandler handler) {
        return RouterFunctions.route(RequestPredicates.all(), handler::handle);
    }

    @Bean
    public DataFilter dataFilter() {
        return new DataFilter();
    }

    @Bean
    public DefaultGlobalExceptionHandler globalErrorWebExceptionHandler(final ResponseBuilder responseBuilder) {
        return new DefaultGlobalExceptionHandler(responseBuilder);
    }

    @Bean
    public ParamResolveService paramResolveService() {
        return new DubboParamResolveService();
    }

    @Bean
    public DubboRouterHandler routerHandler(final ParamResolveService resolveService) {
        return new DubboRouterHandler(resolveService);
    }

    @Bean
    public DubboRouterHandlerMapping dubboRouterHandlerMapping(final DubboRouterHandler routerHandler) {
        return new DubboRouterHandlerMapping(routerHandler);
    }

    @Bean
    public DubboRouteNotify routeNotify() {
        return new DubboRouteNotify();
    }

    @Bean
    public DubboTagRouteNotify tagRouteNotify() {
        return new DubboTagRouteNotify();
    }
}
