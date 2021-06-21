package com.nixend.manny.register.client.api;

import com.nixend.manny.common.model.RouteData;

/**
 * @author panyox
 */
public interface RegisterService {

    void registerRoute(RouteData route);

    void unregisterRoute(RouteData route);

}
