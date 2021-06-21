package com.nixend.manny.client.dubbo.configuration;

import com.nixend.manny.client.dubbo.DubboServiceBeanListener;
import com.nixend.manny.register.client.api.RegisterService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author panyox
 */
@Configuration
public class DubboClientConfiguration {

    @Bean
    public DubboServiceBeanListener dubboServiceBeanListener(final ObjectProvider<RegisterService> registerService) {
        return new DubboServiceBeanListener(registerService.getIfAvailable());
    }
}
