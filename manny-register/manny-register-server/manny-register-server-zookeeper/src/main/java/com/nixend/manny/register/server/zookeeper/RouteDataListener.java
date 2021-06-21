package com.nixend.manny.register.server.zookeeper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.nixend.manny.common.model.RouteData;
import com.nixend.manny.register.server.api.Event;
import com.nixend.manny.register.server.api.NotifyListener;
import com.nixend.manny.remoting.zookeeper.DataListener;
import com.nixend.manny.remoting.zookeeper.EventType;
import lombok.extern.slf4j.Slf4j;

/**
 * @author panyox
 */
@Slf4j
public class RouteDataListener implements DataListener {

    private NotifyListener listener;

    public RouteDataListener() {

    }

    public RouteDataListener(final NotifyListener listener) {
        this.listener = listener;
    }

    public void setListener(final NotifyListener listener) {
        this.listener = listener;
    }

    @Override
    public void dataChanged(String path, Object data, EventType eventType) {
        //log.info("dataChanged path: {} data: {}", path, data);
        try {
            RouteData routeData = JSON.parseObject((String) data, RouteData.class);
            switch (eventType) {
                case NodeCreated:
                    listener.routeChanged(routeData, Event.CREATED);
                    break;
                case NodeDataChanged:
                    listener.routeChanged(routeData, Event.UPDATE);
                    break;
                case NodeDeleted:
                    listener.routeChanged(routeData, Event.DELETE);
                    break;
            }
        } catch (JSONException ex) {
            log.error("parse route data error: {}", ex.getMessage());
        }
    }
}
