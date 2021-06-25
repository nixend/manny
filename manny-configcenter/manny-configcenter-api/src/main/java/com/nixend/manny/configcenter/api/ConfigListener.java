package com.nixend.manny.configcenter.api;

import com.nixend.manny.common.model.RouteData;

/**
 * @author panyox
 */
public interface ConfigListener {

    void dataChanged(RouteData routeData, DataEvent event);
    
}
