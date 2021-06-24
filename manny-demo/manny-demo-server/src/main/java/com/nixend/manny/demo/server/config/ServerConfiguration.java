package com.nixend.manny.demo.server.config;

import com.nixend.manny.plugin.auth.AuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author panyox
 */
@Configuration
public class ServerConfiguration {

    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter().excludePaths("/open/*", "/login", "/v1/login", "/v1/login/index");
    }
}
