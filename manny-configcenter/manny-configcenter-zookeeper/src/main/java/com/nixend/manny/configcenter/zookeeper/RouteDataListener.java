package com.nixend.manny.configcenter.zookeeper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.nixend.manny.common.model.RouteData;
import com.nixend.manny.configcenter.api.ConfigListener;
import com.nixend.manny.configcenter.api.DataEvent;
import com.nixend.manny.remoting.zookeeper.DataListener;
import com.nixend.manny.remoting.zookeeper.EventType;
import lombok.extern.slf4j.Slf4j;

/**
 * @author panyox
 */
@Slf4j
public class RouteDataListener implements DataListener {

    private ConfigListener notify;

    public RouteDataListener() {
    }

    public RouteDataListener(final ConfigListener notify) {
        this.notify = notify;
    }

    public void setListener(final ConfigListener notify) {
        this.notify = notify;
    }

    @Override
    public void dataChanged(String path, Object data, EventType eventType) {
        try {
            RouteData routeData = JSON.parseObject((String) data, RouteData.class);
            switch (eventType) {
                case NodeCreated:
                    notify.dataChanged(routeData, DataEvent.CREATED);
                    break;
                case NodeDataChanged:
                    notify.dataChanged(routeData, DataEvent.UPDATE);
                    break;
                case NodeDeleted:
                    notify.dataChanged(routeData, DataEvent.DELETE);
                    break;
            }
        } catch (JSONException ex) {
            log.error("parse route data error: {}", ex.getMessage());
        }
    }
}
