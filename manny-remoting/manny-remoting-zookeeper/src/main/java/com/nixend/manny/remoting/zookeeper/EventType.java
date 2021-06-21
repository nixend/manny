package com.nixend.manny.remoting.zookeeper;

/**
 * @author panyox
 */
public enum EventType {

    NodeCreated(1),
    NodeDeleted(2),
    NodeDataChanged(3);

    private final int code;

    public int getCode() {
        return this.code;
    }

    EventType(int code) {
        this.code = code;
    }
}
