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
    
}
