package com.nixend.manny.demo.dubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * @author panyox
 */
@SpringBootApplication
@ImportResource({"classpath:dubbo/dubbo-config.xml"})
public class DubboApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboApplication.class);
    }
}
