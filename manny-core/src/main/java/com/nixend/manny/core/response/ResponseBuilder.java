package com.nixend.manny.core.response;

/**
 * @author panyox
 */
public interface ResponseBuilder<T> {

    T success(Object data);

    T error(Throwable throwable);
}
