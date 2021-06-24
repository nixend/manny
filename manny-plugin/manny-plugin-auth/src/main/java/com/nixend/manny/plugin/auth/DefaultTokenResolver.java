package com.nixend.manny.plugin.auth;

import com.nixend.manny.common.utils.JwtUtils;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

/**
 * @author panyox
 */
public class DefaultTokenResolver implements TokenResolver {

    private String tokenName;

    private TokenFrom tokenFrom;

    public DefaultTokenResolver() {
        this.tokenFrom = TokenFrom.HEADER;
        this.tokenName = "Authorization";
    }

    public DefaultTokenResolver(String tokenName) {
        this.tokenName = tokenName;
        this.tokenFrom = TokenFrom.HEADER;
    }

    public DefaultTokenResolver(TokenFrom tokenFrom) {
        this.tokenFrom = tokenFrom;
        this.tokenName = "Authorization";
    }

    public DefaultTokenResolver(String tokenName, TokenFrom tokenFrom) {
        this.tokenName = tokenName;
        this.tokenFrom = tokenFrom;
    }

    @Override
    public Mono<String> getToken(ServerHttpRequest request) {
        String token = null;
        switch (tokenFrom) {
            case HEADER:
                token = request.getHeaders().getFirst(tokenName);
                break;
            case COOKIE:
                HttpCookie cookie = request.getCookies().getFirst(tokenName);
                if (cookie != null) {
                    token = cookie.getValue();
                }
                break;
            case BODY:
                token = request.getQueryParams().getFirst(tokenName);
                break;
        }
        return Mono.justOrEmpty(token);
    }

    @Override
    public Mono<Object> getAuthInfo(String token) {
        String id = JwtUtils.parseToken(token);
        return Mono.justOrEmpty(id);
    }
}
