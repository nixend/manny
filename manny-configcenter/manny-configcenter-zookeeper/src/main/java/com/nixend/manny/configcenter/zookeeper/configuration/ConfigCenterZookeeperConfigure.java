package com.nixend.manny.configcenter.zookeeper.configuration;

import com.nixend.manny.common.constant.Constants;
import com.nixend.manny.common.model.ZookeeperConfig;
import com.nixend.manny.configcenter.api.notify.RouteNotify;
import com.nixend.manny.configcenter.api.notify.TagRouteNotify;
import com.nixend.manny.configcenter.api.subscriber.RouteSubscriber;
import com.nixend.manny.configcenter.api.subscriber.TagRouteSubscriber;
import com.nixend.manny.configcenter.zookeeper.ZookeeperRouteSubscriber;
import com.nixend.manny.configcenter.zookeeper.ZookeeperTagRouteSubscriber;
import com.nixend.manny.remoting.zookeeper.CuratorZookeeperClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author panyox
 */
@Configuration
public class ConfigCenterZookeeperConfigure {

    @Bean
    @ConfigurationProperties(prefix = "manny.config.zookeeper")
    public ZookeeperConfig zookeeperConfig() {
        return new ZookeeperConfig();
    }

    @Bean
    public CuratorZookeeperClient curatorZookeeperClient(final ZookeeperConfig zookeeperConfig) {
        return new CuratorZookeeperClient(zookeeperConfig);
    }

    @Bean
    public RouteSubscriber routeSubscriber(final CuratorZookeeperClient client, final ObjectProvider<RouteNotify> notify) {
        RouteSubscriber subscriber = new ZookeeperRouteSubscriber(client);
        subscriber.subscribe(Constants.DUBBO_SERVICE_PATH, notify.getIfAvailable());
        return subscriber;
    }

    @Bean
    public TagRouteSubscriber tagRouteSubscriber(final CuratorZookeeperClient client, final ObjectProvider<TagRouteNotify> notify) {
        TagRouteSubscriber subscriber = new ZookeeperTagRouteSubscriber(client);
        subscriber.subscribe(Constants.DUBBO_TAG_PATH, notify.getIfAvailable());
        return subscriber;
    }

}
