package com.nixend.manny.common.exception;

import com.nixend.manny.common.enums.ResponseCode;

/**
 * @author panyox
 */
public class MannyCodeException extends RuntimeException {

    private int code;

    public MannyCodeException(int code) {
        this.code = code;
    }

    public MannyCodeException(int code, String message) {
        super(message);
        this.code = code;
    }

    public MannyCodeException(ResponseCode responseCode) {
        super(responseCode.getMsg());
        this.code = responseCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
