package com.nixend.manny.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
@Slf4j
public class RouterDispatcherHandler implements HandlerFunction<ServerResponse>, ApplicationContextAware {

    private List<RouterHandlerMapping> handlerMappings;

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
                .flatMap((handler) -> handler.handle(exchange));
    }

}
