package com.nixend.manny.demo.client.service.impl;

import com.nixend.manny.core.annotation.GetRoute;
import com.nixend.manny.core.annotation.PostRoute;
import com.nixend.manny.core.annotation.RequestRoute;
import com.nixend.manny.demo.client.service.UserService;

/**
 * @author panyox
 */
@RequestRoute(value = "/user")
public class UserServiceImpl implements UserService {

    @Override
    @GetRoute("/hello")
    public String hello() {
        return "hello world";
    }

    @Override
    @PostRoute("/sayHello")
    public String sayHello(String name) {
        return "Hello, " + name;
    }
}
