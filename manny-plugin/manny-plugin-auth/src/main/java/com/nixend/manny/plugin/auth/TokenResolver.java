package com.nixend.manny.plugin.auth;

import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

/**
 * @author panyox
 */
public interface TokenResolver {

    Mono<String> getToken(ServerHttpRequest request);

    Mono<Object> getAuthInfo(String token);

}
