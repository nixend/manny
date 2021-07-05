package com.nixend.manny.configcenter.zookeeper.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.nixend.manny.common.model.RouteData;
import com.nixend.manny.configcenter.api.DataEvent;
import com.nixend.manny.configcenter.api.notify.RouteNotify;
import com.nixend.manny.remoting.zookeeper.DataListener;
import com.nixend.manny.remoting.zookeeper.EventType;
import lombok.extern.slf4j.Slf4j;

/**
 * @author panyox
 */
@Slf4j
public class RouteDataListener implements DataListener {

    private RouteNotify notify;

    public RouteDataListener() {
    }

    public RouteDataListener(final RouteNotify notify) {
        this.notify = notify;
    }

    public void setListener(final RouteNotify notify) {
        this.notify = notify;
    }

    @Override
    public void dataChanged(String path, Object data, EventType eventType) {
        try {
            String json = (String) data;
            if (json.startsWith("{")) {
                RouteData routeData = JSON.parseObject((String) data, RouteData.class);
                switch (eventType) {
                    case NodeCreated:
                        notify.onSubscribe(routeData, DataEvent.CREATED);
                        break;
                    case NodeDataChanged:
                        notify.onSubscribe(routeData, DataEvent.UPDATE);
                        break;
                    case NodeDeleted:
                        notify.onSubscribe(routeData, DataEvent.DELETE);
                        break;
                }
            }
        } catch (JSONException ex) {
            log.error("parse route data error: {}", ex.getMessage());
        }
    }
}
