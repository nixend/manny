package com.nixend.manny.configcenter.api.notify;

import com.nixend.manny.common.model.WeightTagRoute;
import com.nixend.manny.configcenter.api.DataEvent;

/**
 * @author panyox
 */
public interface TagRouteNotify {

    void onSubscribe(WeightTagRoute tagRoute, DataEvent dataEvent);

}
