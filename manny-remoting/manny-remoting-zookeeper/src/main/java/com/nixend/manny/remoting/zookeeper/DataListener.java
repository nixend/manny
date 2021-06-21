package com.nixend.manny.remoting.zookeeper;

/**
 * @author panyox
 */
public interface DataListener {

    void dataChanged(String path, Object data, EventType eventType);

}
