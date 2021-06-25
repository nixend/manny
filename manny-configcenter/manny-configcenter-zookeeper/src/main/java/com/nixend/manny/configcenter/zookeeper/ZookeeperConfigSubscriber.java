package com.nixend.manny.configcenter.zookeeper;

import com.nixend.manny.configcenter.api.ConfigListener;
import com.nixend.manny.configcenter.api.ConfigSubscriber;
import com.nixend.manny.remoting.zookeeper.CuratorZookeeperClient;
import com.nixend.manny.remoting.zookeeper.DataListener;

/**
 * @author panyox
 */
public class ZookeeperConfigSubscriber implements ConfigSubscriber {

    private CuratorZookeeperClient client;

    public ZookeeperConfigSubscriber(final CuratorZookeeperClient client) {
        this.client = client;
    }

    @Override
    public void subscribe(String path, ConfigListener notify) {
        DataListener dataListener = new RouteDataListener(notify);
        client.addDataListener(path, dataListener);
    }

    @Override
    public void unsubscribe(String path) {
        //client.removeDataListener(path, null);
    }
}
