package com.nixend.manny.core.response;

import com.nixend.manny.common.exception.MannyCodeException;
import com.nixend.manny.common.exception.MannyException;

/**
 * @author panyox
 */
public class DefaultResponseBuilder implements ResponseBuilder<DefaultResponseEntity> {

    @Override
    public DefaultResponseEntity success(Object data) {
        return DefaultResponseEntity.success(data);
    }

    @Override
    public DefaultResponseEntity error(Throwable ex) {
        if (ex instanceof MannyException) {
            MannyException exception = (MannyException) ex;
            return DefaultResponseEntity.error(exception.getMessage());
        } else if (ex instanceof MannyCodeException) {
            MannyCodeException exception = (MannyCodeException) ex;
            return DefaultResponseEntity.error(exception.getCode(), exception.getMessage());
        }
        return DefaultResponseEntity.error(500, "Inner Error");
    }
}
