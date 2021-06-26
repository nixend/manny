package com.nixend.manny.common.enums;

/**
 * @author panyox
 */
public enum ResponseCode {

    INNER_ERROR(500, "Inner Server Error"),
    SERVICE_NOT_FOUND(100, "Service not found"),
    METHOD_NOT_FOUND(101, "Method not found"),
    DUBBO_EMPTY(102, "Dubbo has no result"),
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
