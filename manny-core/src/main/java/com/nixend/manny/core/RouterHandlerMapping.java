package com.nixend.manny.core;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author panyox
 */
public interface RouterHandlerMapping {

    Mono<RouterHandler> getHandler(ServerWebExchange var1);
    
}
