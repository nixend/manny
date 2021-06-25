package com.nixend.manny.demo.dubbo.service.impl;

import com.nixend.manny.common.annotation.Identity;
import com.nixend.manny.core.annotation.GetRoute;
import com.nixend.manny.core.annotation.PostRoute;
import com.nixend.manny.core.annotation.RequestRoute;
import com.nixend.manny.demo.dubbo.model.User;
import com.nixend.manny.demo.dubbo.service.UserService;

import java.util.Arrays;
import java.util.List;

/**
 * @author panyox
 */
@RequestRoute("user")
public class UserServiceImpl implements UserService {

    @Override
    @GetRoute("hello")
    public String hello() {
        return "Hi, i'm Manny, nice to meet you!";
    }

    @Override
    @GetRoute("sayHello")
    public String sayHello(String name) {
        return "Hello, " + name;
    }

    @Override
    @GetRoute("info")
    public User info(@Identity Integer userId) {
        return new User(userId, "Manny");
    }

    @Override
    @GetRoute("list")
    public List<User> userList() {
        return Arrays.asList(new User(1, "Manny"), new User(2, "Diego"), new User(3, "Sid"));
    }

    @Override
    @PostRoute("create")
    public User create(Integer id, String name) {
        return new User(id, name);
    }
}
