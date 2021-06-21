package com.nixend.manny.core;

import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author panyox
 */
public interface RouterHandler {

    Mono<ServerResponse> handle(ServerWebExchange exchange);
    
}
