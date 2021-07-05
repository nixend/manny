package com.nixend.manny.core;

import com.nixend.manny.common.enums.ResponseCode;
import com.nixend.manny.common.exception.MannyCodeException;
import com.nixend.manny.core.response.ResponseBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author panyox
 */
public class RouterDispatcherHandler implements HandlerFunction<ServerResponse>, ApplicationContextAware {

    private List<RouterHandlerMapping> handlerMappings;

    private ResponseBuilder responseBuilder;

    public RouterDispatcherHandler(final ResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.initStrategies(applicationContext);
    }

    protected void initStrategies(ApplicationContext context) {
        Map<String, RouterHandlerMapping> mappingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, RouterHandlerMapping.class, true, false);
        ArrayList<RouterHandlerMapping> mappings = new ArrayList(mappingBeans.values());
        this.handlerMappings = Collections.unmodifiableList(mappings);
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        ServerWebExchange exchange = request.exchange();
        return Flux.fromIterable(handlerMappings)
                .concatMap((mapping) -> mapping.getHandler(exchange))
                .next()
                .flatMap((handler) -> handler.handle(exchange))
                .switchIfEmpty(noResult())
                .flatMap(result -> okJson(responseBuilder.success(result)));
    }

    private Mono<ServerResponse> okJson(Object result) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(result));
    }

    private <R> Mono<R> noResult() {
        return Mono.defer(() -> {
            Exception ex = new MannyCodeException(ResponseCode.DUBBO_EMPTY);
            return Mono.error(ex);
        });
    }
}
