package com.nixend.manny.core.configuration;

import com.nixend.manny.core.RouterDispatcherHandler;
import com.nixend.manny.core.exception.GlobalErrorWebExceptionHandler;
import com.nixend.manny.core.filter.DataFilter;
import com.nixend.manny.core.response.DefaultResponseBuilder;
import com.nixend.manny.core.response.ResponseBuilder;
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
public class MannyConfiguration {

    @Bean
    public RouterDispatcherHandler dispatcherHandler() {
        return new RouterDispatcherHandler();
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunction(final RouterDispatcherHandler handler) {
        System.out.println("fuck");
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
    public GlobalErrorWebExceptionHandler globalErrorWebExceptionHandler(final ResponseBuilder responseBuilder) {
        return new GlobalErrorWebExceptionHandler(responseBuilder);
    }
}
