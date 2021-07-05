package com.nixend.manny.plugin.auth;

import com.nixend.manny.common.constant.Constants;
import com.nixend.manny.common.exception.TokenExpiredException;
import com.nixend.manny.common.exception.UnAuthException;
import com.nixend.manny.common.utils.PathUtils;
import org.springframework.boot.web.reactive.filter.OrderedWebFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author panyox
 */
public class AuthFilter implements OrderedWebFilter {

    private TokenResolver tokenResolver;

    private List<String> allowPaths;

    public AuthFilter() {
        this.allowPaths = new ArrayList<>();
        this.tokenResolver = new DefaultTokenResolver();
    }

    public AuthFilter(TokenResolver tokenResolver) {
        this.allowPaths = new ArrayList<>();
        this.tokenResolver = tokenResolver;
    }

    public AuthFilter(List<String> allowPaths, TokenResolver tokenResolver) {
        this.allowPaths = allowPaths;
        this.tokenResolver = tokenResolver;
    }

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        if (isAllowPath(path)) {
            return chain.filter(exchange);
        } else {
            return tokenResolver.getToken(request)
                    .switchIfEmpty(unauthorized())
                    .flatMap(token -> tokenResolver.getAuthInfo(token))
                    .switchIfEmpty(tokenExpired())
                    .flatMap(authInfo -> {
                        exchange.getAttributes().put(Constants.AUTH_INFO, authInfo);
                        return chain.filter(exchange);
                    });
        }
    }

    private boolean isAllowPath(String path) {
        int allowSize = allowPaths.size();
        int i = 0;
        boolean allow = false;
        while (i < allowSize) {
            if (PathUtils.isPattern(allowPaths.get(i))) {
                if (PathUtils.match(allowPaths.get(i), path)) {
                    allow = true;
                    break;
                }
            } else {
                if (path.equals(allowPaths.get(i))) {
                    allow = true;
                    break;
                }
            }
            i++;
        }
        return allow;
    }

    private <R> Mono<R> unauthorized() {
        return Mono.defer(() -> {
            Exception ex = new UnAuthException("Unauthorized");
            return Mono.error(ex);
        });
    }

    private <R> Mono<R> tokenExpired() {
        return Mono.defer(() -> {
            Exception ex = new TokenExpiredException("Token expired");
            return Mono.error(ex);
        });
    }

    public AuthFilter tokenResolver(TokenResolver tokenResolver) {
        this.tokenResolver = tokenResolver;
        return this;
    }

    public AuthFilter excludePaths(String... paths) {
        allowPaths.addAll(Arrays.asList(paths));
        return this;
    }
}
