package com.nixend.manny.configcenter.zookeeper;

import com.nixend.manny.configcenter.api.notify.TagRouteNotify;
import com.nixend.manny.configcenter.api.subscriber.TagRouteSubscriber;
import com.nixend.manny.configcenter.zookeeper.listener.TagRouteDataListener;
import com.nixend.manny.remoting.zookeeper.CuratorZookeeperClient;
import com.nixend.manny.remoting.zookeeper.DataListener;

/**
 * @author panyox
 */
public class ZookeeperTagRouteSubscriber implements TagRouteSubscriber {

    private CuratorZookeeperClient client;

    public ZookeeperTagRouteSubscriber(final CuratorZookeeperClient client) {
        this.client = client;
    }

    @Override
    public void subscribe(String path, TagRouteNotify notify) {
        DataListener listener = new TagRouteDataListener(notify);
        client.addDataListener(path, listener);
    }

    @Override
    public void unsubscribe(String path) {
        //
    }
}
