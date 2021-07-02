package com.nixend.manny.configcenter.api.subscriber;

import com.nixend.manny.configcenter.api.notify.TagRouteNotify;

/**
 * @author panyox
 */
public interface TagRouteSubscriber {

    void subscribe(String path, TagRouteNotify notify);

    void unsubscribe(String path);

}
