package com.nixend.manny.core.exception;

import com.nixend.manny.core.response.ResponseCode;

/**
 * @author panyox
 */
public class MannyException extends RuntimeException {

    private static final long serialVersionUID = -20023284059L;

    private int code;

    public MannyException(Throwable e) {
        super(e);
    }

    public MannyException(int code, String message) {
        super(message);
        this.code = code;
    }

    public MannyException(String message) {
        super(message);
    }

    public MannyException(ResponseCode responseCode) {
        super(responseCode.getMsg());
        this.code = responseCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
