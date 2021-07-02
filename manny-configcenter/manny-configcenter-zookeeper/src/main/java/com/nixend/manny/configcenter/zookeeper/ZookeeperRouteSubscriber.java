package com.nixend.manny.configcenter.zookeeper;

import com.nixend.manny.configcenter.api.notify.RouteNotify;
import com.nixend.manny.configcenter.api.subscriber.RouteSubscriber;
import com.nixend.manny.configcenter.zookeeper.listener.RouteDataListener;
import com.nixend.manny.remoting.zookeeper.CuratorZookeeperClient;
import com.nixend.manny.remoting.zookeeper.DataListener;

/**
 * @author panyox
 */
public class ZookeeperRouteSubscriber implements RouteSubscriber {

    private CuratorZookeeperClient client;

    public ZookeeperRouteSubscriber(final CuratorZookeeperClient client) {
        this.client = client;
    }

    @Override
    public void subscribe(String path, RouteNotify notify) {
        DataListener dataListener = new RouteDataListener(notify);
        client.addDataListener(path, dataListener);
    }

    @Override
    public void unsubscribe(String path) {
        //client.removeDataListener(path, null);
    }
}
