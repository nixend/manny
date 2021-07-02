package com.nixend.manny.configcenter.api.subscriber;

import com.nixend.manny.configcenter.api.notify.RouteNotify;

/**
 * @author panyox
 */
public interface RouteSubscriber {

    void subscribe(String path, RouteNotify notify);

    void unsubscribe(String path);
    
}
