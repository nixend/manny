package com.nixend.manny.server.dubbo.configuration;

import com.nixend.manny.core.ParamResolveService;
import com.nixend.manny.core.response.ResponseBuilder;
import com.nixend.manny.server.dubbo.DubboParamResolveService;
import com.nixend.manny.server.dubbo.DubboRouterHandler;
import com.nixend.manny.server.dubbo.DubboRouterHandlerMapping;
import com.nixend.manny.server.dubbo.notify.RouteNotifyListener;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author panyox
 */
@Configuration
public class DubboServerConfiguration {

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
    public RouteNotifyListener notifyListener() {
        return new RouteNotifyListener();
    }
}
