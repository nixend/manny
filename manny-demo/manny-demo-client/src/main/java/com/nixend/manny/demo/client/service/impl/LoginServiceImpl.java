package com.nixend.manny.demo.client.service.impl;

import com.nixend.manny.common.utils.JwtUtils;
import com.nixend.manny.core.annotation.PostRoute;
import com.nixend.manny.core.annotation.RequestRoute;
import com.nixend.manny.demo.client.service.LoginService;

/**
 * @author panyox
 */
@RequestRoute("/login")
public class LoginServiceImpl implements LoginService {

    @Override
    @PostRoute("/")
    public String login(String userName, String password) {
        if ("test".equals(userName) && password.equals("123")) {
            return JwtUtils.createToken("100");
        }
        return null;
    }
}
