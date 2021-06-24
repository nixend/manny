package com.nixend.manny.common.enums;

/**
 * @author panyox
 */
public enum ParamAnnotation {

    IDENTITY("ID"),
    PATH_VARIABLE("PATH"),
    REQUIRED("REQ");

    private String name;

    public String getName() {
        return name;
    }

    ParamAnnotation(String name) {
        this.name = name;
    }
}
