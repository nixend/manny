package com.nixend.manny.core.handler;

import com.nixend.manny.common.constant.Constants;
import com.nixend.manny.common.enums.ResponseCode;
import com.nixend.manny.common.exception.MannyCodeException;
import com.nixend.manny.common.exception.MannyException;
import com.nixend.manny.core.RouterHandler;
import com.nixend.manny.core.RouterHandlerMapping;
import com.nixend.manny.core.match.MatchStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author panyox
 */
@Slf4j
public abstract class AbstractRouterHandlerMapping implements RouterHandlerMapping {

    @Override
    public Mono<RouterHandler> getHandler(ServerWebExchange exchange) {
        MatchStrategy matchStrategy = getMatchStrategy();
        if (matchStrategy == null) {
            return strategyNotFound();
        } else {
            return matchStrategy.matchRoute(exchange).switchIfEmpty(routeNotFound())
                    .flatMap(route -> {
                        exchange.getAttributes().put(Constants.ROUTE_DATA, route);
                        return getHandlerInternal(exchange);
                    });
        }
    }

    private <R> Mono<R> strategyNotFound() {
        return Mono.defer(() -> {
            Exception ex = new MannyException("Inner Error");
            return Mono.error(ex);
        });
    }

    private <R> Mono<R> routeNotFound() {
        return Mono.defer(() -> {
            Exception ex = new MannyCodeException(ResponseCode.ROUTE_NOT_MATCH);
            return Mono.error(ex);
        });
    }

    protected abstract Mono<RouterHandler> getHandlerInternal(ServerWebExchange var1);

    protected abstract MatchStrategy getMatchStrategy();
}
