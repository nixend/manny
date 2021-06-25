package com.nixend.manny.configcenter.zookeeper.configuration;

import com.nixend.manny.common.constant.Constants;
import com.nixend.manny.common.model.ZookeeperConfig;
import com.nixend.manny.configcenter.api.ConfigListener;
import com.nixend.manny.configcenter.api.ConfigSubscriber;
import com.nixend.manny.configcenter.zookeeper.ZookeeperConfigSubscriber;
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
    public ConfigSubscriber configSubscriber(final CuratorZookeeperClient client, final ObjectProvider<ConfigListener> listener) {
        ConfigSubscriber subscriber = new ZookeeperConfigSubscriber(client);
        subscriber.subscribe(Constants.DUBBO_SERVICE_PATH, listener.getIfAvailable());
        return subscriber;
    }
}
