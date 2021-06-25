package com.nixend.manny.demo.dubbo.service;

import com.nixend.manny.demo.dubbo.model.User;

import java.util.List;

/**
 * @author panyox
 */
public interface UserService {

    String hello();

    String sayHello(String name);

    User info(Integer userId);

    List<User> userList();

    User create(Integer id, String name);
}
