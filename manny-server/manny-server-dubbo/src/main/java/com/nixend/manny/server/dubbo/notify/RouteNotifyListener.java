package com.nixend.manny.server.dubbo.notify;

import com.nixend.manny.common.model.RouteData;
import com.nixend.manny.core.route.RouteMapping;
import com.nixend.manny.register.server.api.Event;
import com.nixend.manny.register.server.api.NotifyListener;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author panyox
 */
@Slf4j
public class RouteNotifyListener implements NotifyListener {

    @Override
    public void routeChanged(RouteData routeData, Event event) {
        if (Objects.nonNull(routeData) && Objects.nonNull(event)) {
            switch (event) {
                case CREATED:
                case UPDATE:
                    doRegister(routeData);
                    break;
                case DELETE:
                    doDelete(routeData);
                    break;
            }
        }
    }

    private void doRegister(RouteData route) {
        log.info("update route: {}", route);
        RouteMapping routeMapping = RouteMapping.getInstance();
        routeMapping.registerRoute(route);
    }

    private void doDelete(RouteData route) {
        log.info("delete route: {}", route);
        RouteMapping routeMapping = RouteMapping.getInstance();
        routeMapping.registerMethod(route.getMethod());
    }
}
