package com.nixend.manny.core.response;

import com.nixend.manny.core.exception.MannyException;

/**
 * @author panyox
 */
public class DefaultResponseBuilder implements ResponseBuilder<DefaultResponseEntity> {

    @Override
    public DefaultResponseEntity success(Object data) {
        return DefaultResponseEntity.success(data);
    }

    @Override
    public DefaultResponseEntity error(Throwable throwable) {
        if (throwable instanceof MannyException) {
            MannyException exception = (MannyException) throwable;
            return DefaultResponseEntity.error(exception.getCode(), exception.getMessage());
        }
        return DefaultResponseEntity.error(throwable.getMessage());
    }
}
