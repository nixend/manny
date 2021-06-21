package com.nixend.manny.register.server.api;

/**
 * @author panyox
 */
public interface DiscoveryService {

    void subscribe(String path, NotifyListener listener);

    void unsubscribe(String path);

}
