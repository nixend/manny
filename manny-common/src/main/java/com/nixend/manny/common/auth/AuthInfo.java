package com.nixend.manny.common.auth;

/**
 * @author panyox
 */
public class AuthInfo implements Identity {

    private Object loginId;

    public AuthInfo(Object loginId) {
        this.loginId = loginId;
    }

    @Override
    public Object getIdentity() {
        return loginId;
    }
}
