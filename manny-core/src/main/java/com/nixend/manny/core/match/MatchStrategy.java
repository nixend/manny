package com.nixend.manny.core.match;

import com.nixend.manny.common.model.RouteData;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author panyox
 */
public interface MatchStrategy {

    Mono<RouteData> matchRoute(ServerWebExchange exchange);

}
