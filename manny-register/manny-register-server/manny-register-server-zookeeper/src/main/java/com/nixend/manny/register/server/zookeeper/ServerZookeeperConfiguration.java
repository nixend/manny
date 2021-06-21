package com.nixend.manny.register.server.zookeeper;

import com.nixend.manny.common.constant.Constants;
import com.nixend.manny.common.model.ZookeeperConfig;
import com.nixend.manny.register.server.api.NotifyListener;
import com.nixend.manny.remoting.zookeeper.CuratorZookeeperClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author panyox
 */
@Configuration
public class ServerZookeeperConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "manny.sync.zookeeper")
    public ZookeeperConfig zookeeperConfig() {
        return new ZookeeperConfig();
    }

    @Bean
    public CuratorZookeeperClient curatorZookeeperClient(final ZookeeperConfig zookeeperConfig) {
        return new CuratorZookeeperClient(zookeeperConfig);
    }

    @Bean
    public ZookeeperDiscoveryService discoveryService(final CuratorZookeeperClient client, final ObjectProvider<NotifyListener> notifyListener) {
        ZookeeperDiscoveryService discoveryService = new ZookeeperDiscoveryService(client);
        discoveryService.subscribe(Constants.DUBBO_SERVICE_PATH, notifyListener.getIfAvailable());
        return discoveryService;
    }
}
