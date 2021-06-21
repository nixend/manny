package com.nixend.manny.register.client.zookeeper;

import com.nixend.manny.common.model.ZookeeperConfig;
import com.nixend.manny.remoting.zookeeper.CuratorZookeeperClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author panyox
 */
@Configuration
public class ClientZookeeperConfiguration {

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
    public ZookeeperRegisterService registerService(final CuratorZookeeperClient client) {
        return new ZookeeperRegisterService(client);
    }
}
