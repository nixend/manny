package com.nixend.manny.server.dubbo;

import com.nixend.manny.common.constant.Constants;
import com.nixend.manny.common.model.RouteData;
import com.nixend.manny.common.utils.JsonUtils;
import com.nixend.manny.core.ParamResolveService;
import com.nixend.manny.core.RouterHandler;
import com.nixend.manny.core.exception.MannyException;
import com.nixend.manny.core.response.ResponseBuilder;
import com.nixend.manny.core.response.ResponseCode;
import com.nixend.manny.server.dubbo.cache.ReferenceCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author panyox
 */
@Slf4j
public class DubboRouterHandler implements RouterHandler {

    private ParamResolveService paramResolveService;

    private ResponseBuilder responseBuilder;

    public DubboRouterHandler(final ParamResolveService paramResolveService, final ResponseBuilder responseBuilder) {
        this.paramResolveService = paramResolveService;
        this.responseBuilder = responseBuilder;
    }

    @Override
    public Mono<ServerResponse> handle(ServerWebExchange exchange) {
        String params = exchange.getAttribute(Constants.ROUTER_PARAMS);
        RouteData routeData = exchange.getAttribute(Constants.ROUTE_DATA);
        Object authInfo = exchange.getAttribute(Constants.AUTH_INFO);
        Pair<String[], Object[]> pair;
        log.info("authInfo: {}", authInfo);
        if (Objects.nonNull(authInfo)) {
            pair = paramResolveService.buildParameter(params, routeData.getMethod().getParameters(), authInfo);
        } else {
            pair = paramResolveService.buildParameter(params, routeData.getMethod().getParameters());
        }
        log.info("params: {} left: {} right: {}", params, pair.getLeft(), pair.getRight());
        return Mono.defer(() -> {
            Object result;
            try {
                Object res = doInvoke(pair, routeData);
                result = responseBuilder.success(res);
                result = JsonUtils.removeClass(result);
            } catch (Exception ex) {
                log.error("invoke error: {}", ex.getMessage());
                result = responseBuilder.error(new MannyException(ResponseCode.INVOKE_ERROR));
            }
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(result));
        });
    }

    private Object doInvoke(final Pair<String[], Object[]> pair, final RouteData routeData) {
        ReferenceConfig<GenericService> reference = ReferenceCache.getInstance().get(routeData.getService().getName());
        if (Objects.isNull(reference) || StringUtils.isEmpty(reference.getInterface())) {
            reference = ReferenceCache.getInstance().initRef(routeData);
        }
        GenericService service = ReferenceConfigCache.getCache().get(reference);
        return service.$invoke(routeData.getMethod().getName(), pair.getLeft(), pair.getRight());
    }
}
