package com.nixend.manny.registry.api;

import com.nixend.manny.common.model.RouteData;
import com.nixend.manny.common.model.ServiceData;

/**
 * @author panyox
 */
public interface RegistryService {

    void registerRoute(RouteData route);

    void unRegisterRoute(RouteData route);

    void registerProvider(ServiceData service);

    void unRegisterProvider(ServiceData service);
}
