package com.nixend.manny.common.enums;

/**
 * @author panyox
 */
public enum RpcType {

    DUBBO("dubbo"),
    SPRING_CLOUD("spring_cloud"),
    HTTP("http");

    private String name;

    RpcType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
