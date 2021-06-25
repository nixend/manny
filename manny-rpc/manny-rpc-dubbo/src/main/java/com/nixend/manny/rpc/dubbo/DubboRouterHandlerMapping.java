package com.nixend.manny.rpc.dubbo;

import com.nixend.manny.core.RouterHandler;
import com.nixend.manny.core.handler.AbstractRouterHandlerMapping;
import com.nixend.manny.core.match.MatchStrategy;
import com.nixend.manny.rpc.dubbo.strategy.DubboMatchStrategy;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author panyox
 */
public class DubboRouterHandlerMapping extends AbstractRouterHandlerMapping {

    private DubboRouterHandler routerHandler;

    public DubboRouterHandlerMapping(final DubboRouterHandler routerHandler) {
        this.routerHandler = routerHandler;
    }

    @Override
    protected MatchStrategy getMatchStrategy() {
        return DubboMatchStrategy.getInstance();
    }

    @Override
    protected Mono<RouterHandler> getHandlerInternal(ServerWebExchange exchange) {
        return Mono.justOrEmpty(routerHandler);
    }
}
