package com.nixend.manny.core.exception;

import com.alibaba.fastjson.JSON;
import com.nixend.manny.core.response.ResponseBuilder;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author panyox
 */
@Order(-2)
public class DefaultGlobalExceptionHandler implements ErrorWebExceptionHandler {

    private ResponseBuilder responseBuilder;

    public DefaultGlobalExceptionHandler(final ResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable exception) {
        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        ServerHttpResponse response = exchange.getResponse();
        //throwable.printStackTrace();
        exception.printStackTrace();
        Object result = responseBuilder.error(exception);
        String res = JSON.toJSONString(result);
        DataBuffer dataBuffer = bufferFactory.wrap(res.getBytes());
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.just(dataBuffer));
    }
}
