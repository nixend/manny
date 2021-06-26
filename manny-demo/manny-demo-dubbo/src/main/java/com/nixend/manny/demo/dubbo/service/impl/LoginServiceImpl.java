package com.nixend.manny.demo.dubbo.service.impl;

import com.nixend.manny.common.exception.MannyCodeException;
import com.nixend.manny.common.utils.JwtUtils;
import com.nixend.manny.core.annotation.PostRoute;
import com.nixend.manny.core.annotation.RequestRoute;
import com.nixend.manny.demo.dubbo.model.User;
import com.nixend.manny.demo.dubbo.service.LoginService;

/**
 * @author panyox
 */
@RequestRoute(value = "login", retry = 1, timeout = 10)
public class LoginServiceImpl implements LoginService {

    @Override
    @PostRoute("/")
    public String login(String name, String password) {
        if (name.equals("test") && password.equals("123")) {
            return JwtUtils.createToken("100");
        } else {
            throw new MannyCodeException(10001, "Incorrect username or password");
        }
    }

    @Override
    @PostRoute("/signup")
    public User signup(User user) {
        return user;
    }
}
