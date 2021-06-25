package com.nixend.manny.rpc.dubbo.configration;

import com.nixend.manny.configcenter.api.ConfigListener;
import com.nixend.manny.core.ParamResolveService;
import com.nixend.manny.core.RouterDispatcherHandler;
import com.nixend.manny.core.exception.DefaultGlobalExceptionHandler;
import com.nixend.manny.core.filter.DataFilter;
import com.nixend.manny.core.response.DefaultResponseBuilder;
import com.nixend.manny.core.response.ResponseBuilder;
import com.nixend.manny.rpc.dubbo.DubboParamResolveService;
import com.nixend.manny.rpc.dubbo.DubboRouterHandler;
import com.nixend.manny.rpc.dubbo.DubboRouterHandlerMapping;
import com.nixend.manny.rpc.dubbo.listener.DubboRouteConfigListener;
import org.springframework.beans.factory.ObjectProvider;
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
public class DubboRpcConfiguration {

    @Bean
    public RouterDispatcherHandler dispatcherHandler() {
        return new RouterDispatcherHandler();
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
    @ConditionalOnMissingBean(value = ResponseBuilder.class, search = SearchStrategy.ALL)
    public ResponseBuilder responseBuilder() {
        return new DefaultResponseBuilder();
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
    public DubboRouterHandler routerHandler(final ParamResolveService resolveService, final ObjectProvider<ResponseBuilder> responseBuilder) {
        return new DubboRouterHandler(resolveService, responseBuilder.getIfAvailable());
    }

    @Bean
    public DubboRouterHandlerMapping dubboRouterHandlerMapping(final DubboRouterHandler routerHandler) {
        return new DubboRouterHandlerMapping(routerHandler);
    }

    @Bean
    public ConfigListener configListener() {
        return new DubboRouteConfigListener();
    }
}
