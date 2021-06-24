package com.nixend.manny.demo.client.service.impl;

import com.nixend.manny.core.annotation.GetRoute;
import com.nixend.manny.core.annotation.PostRoute;
import com.nixend.manny.core.annotation.RequestRoute;
import com.nixend.manny.demo.client.model.User;
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

    @Override
    @PostRoute("/create")
    public User create(User user) {
        return user;
    }

    @Override
    @GetRoute("/findByIdAndName")
    public User findByIdAndName(Integer id, String name) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        return user;
    }

    @Override
    @PostRoute("/createById")
    public User createById(User user, Integer userId) {
        user.setId(userId);
        return user;
    }

    @Override
    @GetRoute("/info")
    public User info(Integer loginId) {
        User user = new User();
        user.setId(loginId);
        return user;
    }
}
