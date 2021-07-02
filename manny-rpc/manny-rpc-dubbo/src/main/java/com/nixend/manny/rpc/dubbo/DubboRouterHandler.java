package com.nixend.manny.rpc.dubbo;

import com.nixend.manny.common.constant.Constants;
import com.nixend.manny.common.exception.MannyCodeException;
import com.nixend.manny.common.exception.MannyException;
import com.nixend.manny.common.model.DubboTag;
import com.nixend.manny.common.model.RouteData;
import com.nixend.manny.core.ParamResolveService;
import com.nixend.manny.core.RouterHandler;
import com.nixend.manny.rpc.dubbo.cache.ReferenceCache;
import com.nixend.manny.rpc.dubbo.strategy.TagStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.service.GenericException;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @author panyox
 */
@Slf4j
public class DubboRouterHandler implements RouterHandler {

    private ParamResolveService paramResolveService;

    public DubboRouterHandler(final ParamResolveService paramResolveService) {
        this.paramResolveService = paramResolveService;
    }

    @Override
    public Mono<Object> handle(ServerWebExchange exchange) {
        String params = exchange.getAttribute(Constants.ROUTER_PARAMS);
        RouteData routeData = exchange.getAttribute(Constants.ROUTE_DATA);
        Object authInfo = exchange.getAttribute(Constants.AUTH_INFO);
        Pair<String[], Object[]> pair;
        if (Objects.nonNull(authInfo)) {
            pair = paramResolveService.buildParameter(params, routeData.getMethod().getParameters(), authInfo);
        } else {
            pair = paramResolveService.buildParameter(params, routeData.getMethod().getParameters());
        }
        String tag = exchange.getRequest().getHeaders().getFirst(Constants.DUBBO_TAG_NAME);
        if (!StringUtils.isEmpty(tag)) {
            log.info("use spec tag: {}", tag);
            RpcContext.getContext().setAttachment(Constants.DUBBO_TAG, tag);
        } else {
            DubboTag bestTag = TagStrategy.getInstance().matchBestTag(routeData.getService().getApplication());
            log.info("use best tag: {}", bestTag);
            if (null != bestTag) {
                RpcContext.getContext().setAttachment(Constants.DUBBO_TAG, bestTag.getName());
            }
        }
        log.info(" params: {} left: {} right: {}", params, pair.getLeft(), pair.getRight());
        CompletableFuture<Object> future = doInvoke(pair, routeData);
        return Mono.fromFuture(future.thenApply(res -> {
            //do something
            return res;
        })).onErrorMap(exception -> {
            Throwable rpcException;
            if (exception instanceof MannyException || exception instanceof MannyCodeException) {
                rpcException = exception;
            } else if (exception instanceof GenericException) {
                String msg = ((GenericException) exception).getExceptionMessage();
                rpcException = new MannyException(msg);
            } else {
                rpcException = (Throwable) exception;
            }
            return rpcException;
        });
    }

    private CompletableFuture<Object> doInvoke(final Pair<String[], Object[]> pair, final RouteData routeData) {
        ReferenceConfig<GenericService> reference = ReferenceCache.getInstance().get(routeData.getService().getName());
        if (Objects.isNull(reference) || StringUtils.isEmpty(reference.getInterface())) {
            reference = ReferenceCache.getInstance().initRef(routeData);
        }
        GenericService service = ReferenceConfigCache.getCache().get(reference);
        return service.$invokeAsync(routeData.getMethod().getName(), pair.getLeft(), pair.getRight());
    }

}
