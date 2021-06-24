package com.nixend.manny.demo.client.service;

import com.nixend.manny.demo.client.model.User;

/**
 * @author panyox
 */
public interface UserService {

    String hello();

    String sayHello(String name);

    User create(User user);

    User findByIdAndName(Integer id, String name);

    User createById(User user);

    User create2(User user, String name);

    User info(Integer loginId);

    User info1(Integer loginId, String name);
}
