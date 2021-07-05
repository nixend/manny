package com.nixend.manny.springboot.starter.dubbo.provider;

import com.nixend.manny.provider.dubbo.DubboServiceBeanListener;
import com.nixend.manny.registry.api.RegistryService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author panyox
 */
@Configuration
public class DubboProviderConfigure {

    @Bean
    public DubboServiceBeanListener dubboServiceBeanListener(final ObjectProvider<RegistryService> registryService) {
        return new DubboServiceBeanListener(registryService.getIfAvailable());
    }
}
