package com.nixend.manny.configcenter.zookeeper.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.nixend.manny.common.model.WeightTagRoute;
import com.nixend.manny.configcenter.api.DataEvent;
import com.nixend.manny.configcenter.api.notify.TagRouteNotify;
import com.nixend.manny.remoting.zookeeper.DataListener;
import com.nixend.manny.remoting.zookeeper.EventType;

/**
 * @author panyox
 */
public class TagRouteDataListener implements DataListener {

    private TagRouteNotify notify;

    public TagRouteDataListener(final TagRouteNotify notify) {
        this.notify = notify;
    }

    @Override
    public void dataChanged(String path, Object data, EventType eventType) {
        try {
            WeightTagRoute tagRoute = JSON.parseObject((String) data, WeightTagRoute.class);
            switch (eventType) {
                case NodeCreated:
                    notify.onSubscribe(tagRoute, DataEvent.CREATED);
                    break;
                case NodeDataChanged:
                    notify.onSubscribe(tagRoute, DataEvent.UPDATE);
                    break;
                case NodeDeleted:
                    notify.onSubscribe(tagRoute, DataEvent.DELETE);
                    break;
            }
        } catch (JSONException ex) {
            System.out.println("parse route data error " + ex.getMessage());
        }
    }
}
