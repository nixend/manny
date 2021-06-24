package com.nixend.manny.core.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nixend.manny.common.auth.AuthInfo;
import com.nixend.manny.common.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.filter.OrderedWebFilter;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * @author panyox
 */
@Slf4j
public class DataFilter implements OrderedWebFilter {

    private final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        MediaType mediaType = request.getHeaders().getContentType();
        ServerRequest serverRequest = ServerRequest.create(exchange, messageReaders);
        if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)) {
            return body(exchange, chain, serverRequest);
        } else if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)) {
            return form(exchange, chain, serverRequest);
        } else {
            return query(exchange, chain, serverRequest);
        }
    }

    private Mono<Void> body(ServerWebExchange exchange, WebFilterChain chain, ServerRequest request) {
        return request.bodyToMono(String.class)
                .switchIfEmpty(Mono.defer(() -> Mono.just(Constants.EMPTY_JSON)))
                .flatMap(body -> {
                    AuthInfo authInfo = exchange.getAttribute(Constants.AUTH_INFO);
                    if (authInfo == null) {
                        exchange.getAttributes().put(Constants.ROUTER_PARAMS, body);
                    } else {
                        String bodyContent;
                        if (body.equals(Constants.EMPTY_JSON)) {
                            bodyContent = JSON.toJSONString(authInfo);
                        } else {
                            JSONObject obj = JSON.parseObject(body);
                            obj.put(Constants.LOGIN_ID, authInfo.getIdentity());
                            bodyContent = JSON.toJSONString(obj);
                        }
                        exchange.getAttributes().put(Constants.ROUTER_PARAMS, bodyContent);
                    }
                    return chain.filter(exchange);
                });
    }

    private Mono<Void> form(ServerWebExchange exchange, WebFilterChain chain, ServerRequest request) {
        return request.formData()
                .switchIfEmpty(Mono.defer(() -> Mono.just(new LinkedMultiValueMap<>())))
                .flatMap(map -> {
                    Map<String, String> paramMap = map.toSingleValueMap();
                    AuthInfo authInfo = exchange.getAttribute(Constants.AUTH_INFO);
                    if (authInfo != null) {
                        paramMap.put(Constants.LOGIN_ID, (String) authInfo.getIdentity());
                    }
                    exchange.getAttributes().put(Constants.ROUTER_PARAMS, JSON.toJSONString(paramMap));
                    return chain.filter(exchange);
                });
    }

    private Mono<Void> query(ServerWebExchange exchange, WebFilterChain chain, ServerRequest request) {
        Map<String, String> map = request.queryParams().toSingleValueMap();
        AuthInfo authInfo = exchange.getAttribute(Constants.AUTH_INFO);
        if (authInfo != null) {
            map.put(Constants.LOGIN_ID, (String) authInfo.getIdentity());
        }
        exchange.getAttributes().put(Constants.ROUTER_PARAMS, JSON.toJSONString(map));
        return chain.filter(exchange);
    }

}
