package com.nixend.manny.admin.config;

import com.nixend.manny.admin.registry.RegistryService;
import com.nixend.manny.admin.registry.admin.AdminRegistryService;
import com.nixend.manny.admin.registry.dubbo.DubboRegistryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author panyox
 */
@Configuration
public class ConfigCenter {

    @Value("${manny.admin.registry}")
    private String adminRegistry;

    @Value("${manny.dubbo.registry}")
    private String dubboRegistry;

    @Bean
    public RegistryService dubboRegistryService() {
        RegistryService registryService = new DubboRegistryService();
        registryService.setAddress(dubboRegistry);
        registryService.init();
        return registryService;
    }

    @Bean
    public RegistryService adminRegistryService() {
        RegistryService registryService = new AdminRegistryService();
        registryService.setAddress(adminRegistry);
        registryService.init();
        return registryService;
    }
}
