package com.nixend.manny.core.response;

/**
 * @author panyox
 */
public enum ResponseCode {

    INNER_ERROR(500, "Inner Server Error"),
    SERVICE_NOT_FOUND(100, "Service not found"),
    METHOD_NOT_FOUND(101, "Method not found"),
    INVOKE_ERROR(102, "Invoke error"),
    ROUTE_NOT_MATCH(103, "Route not matching, check out your url");

    private int code;
    private String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
