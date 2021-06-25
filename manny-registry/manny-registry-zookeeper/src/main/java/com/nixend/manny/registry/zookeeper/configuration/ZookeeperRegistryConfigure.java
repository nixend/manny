package com.nixend.manny.registry.zookeeper.configuration;

import com.nixend.manny.common.model.ZookeeperConfig;
import com.nixend.manny.registry.api.RegistryService;
import com.nixend.manny.registry.zookeeper.ZookeeperRegistryService;
import com.nixend.manny.remoting.zookeeper.CuratorZookeeperClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author panyox
 */
@Configuration
public class ZookeeperRegistryConfigure {

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
    public RegistryService registryService(final CuratorZookeeperClient client) {
        return new ZookeeperRegistryService(client);
    }
}
