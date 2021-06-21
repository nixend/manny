package com.nixend.manny.register.server.api;

import com.nixend.manny.common.model.RouteData;

/**
 * @author panyox
 */
public interface NotifyListener {

    void routeChanged(RouteData routeData, Event event);

}
