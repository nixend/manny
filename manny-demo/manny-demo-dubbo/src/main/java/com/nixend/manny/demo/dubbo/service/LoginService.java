package com.nixend.manny.demo.dubbo.service;

import com.nixend.manny.demo.dubbo.model.User;

/**
 * @author panyox
 */
public interface LoginService {

    String login(String name, String password);

    User signup(User user);

}
