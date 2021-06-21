package com.nixend.manny.register.server.zookeeper;

import com.nixend.manny.register.server.api.DiscoveryService;
import com.nixend.manny.register.server.api.NotifyListener;
import com.nixend.manny.remoting.zookeeper.CuratorZookeeperClient;
import com.nixend.manny.remoting.zookeeper.DataListener;

/**
 * @author panyox
 */
public class ZookeeperDiscoveryService implements DiscoveryService {

    private CuratorZookeeperClient client;

    public ZookeeperDiscoveryService(final CuratorZookeeperClient client) {
        this.client = client;
    }

    @Override
    public void subscribe(String path, NotifyListener listener) {
        DataListener dataListener = new RouteDataListener(listener);
        client.addDataListener(path, dataListener);
    }

    @Override
    public void unsubscribe(String path) {
        //client.removeDataListener(path, null);
    }
}
