package com.nixend.manny.configcenter.api;

/**
 * @author panyox
 */
public interface ConfigSubscriber {

    void subscribe(String path, ConfigListener listener);

    void unsubscribe(String path);

}
