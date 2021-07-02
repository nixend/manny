package com.nixend.manny.configcenter.api.notify;

import com.nixend.manny.common.model.RouteData;
import com.nixend.manny.configcenter.api.DataEvent;

/**
 * @author panyox
 */
public interface RouteNotify {

    void onSubscribe(RouteData data, DataEvent dataEvent);

}
