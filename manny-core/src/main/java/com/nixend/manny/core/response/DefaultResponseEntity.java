package com.nixend.manny.core.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author panyox
 */
@Data
public class DefaultResponseEntity implements Serializable {
    private Integer code;
    private String msg;
    private Object data;

    public DefaultResponseEntity(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private static DefaultResponseEntity build(Integer code, String msg, Object data) {
        return new DefaultResponseEntity(code, msg, data);
    }

    public static DefaultResponseEntity success() {
        return build(0, "success", null);
    }

    public static DefaultResponseEntity success(Integer code) {
        return build(code, "success", null);
    }

    public static DefaultResponseEntity success(Object data) {
        return build(0, "success", data);
    }

    public static DefaultResponseEntity success(Integer code, Object data) {
        return build(code, "success", data);
    }

    public static DefaultResponseEntity error(String msg) {
        return build(500, msg, null);
    }

    public static DefaultResponseEntity error(Integer code, String msg) {
        return build(code, msg, null);
    }

    public static DefaultResponseEntity error(Integer code, String msg, Object data) {
        return build(code, msg, data);
    }
}
