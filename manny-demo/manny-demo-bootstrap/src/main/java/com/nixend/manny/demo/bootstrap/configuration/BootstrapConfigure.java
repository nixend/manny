package com.nixend.manny.demo.bootstrap.configuration;

import com.nixend.manny.plugin.auth.AuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author panyox
 */
@Configuration
public class BootstrapConfigure {

    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter().excludePaths("/open/*", "/login");
    }
}
