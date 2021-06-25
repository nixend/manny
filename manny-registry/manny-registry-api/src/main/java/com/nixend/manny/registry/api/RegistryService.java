package com.nixend.manny.registry.api;

import com.nixend.manny.common.model.RouteData;

/**
 * @author panyox
 */
public interface RegistryService {
    
    void registerRoute(RouteData route);

    void unregisterRoute(RouteData route);

}
