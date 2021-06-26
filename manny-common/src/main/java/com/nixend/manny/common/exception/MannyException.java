package com.nixend.manny.common.exception;

/**
 * @author panyox
 */
public class MannyException extends RuntimeException {

    public MannyException(String message) {
        super(message);
    }

    public MannyException(Throwable e) {
        super(e);
    }
}
