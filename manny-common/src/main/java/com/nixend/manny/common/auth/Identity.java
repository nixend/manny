package com.nixend.manny.common.auth;

import java.io.Serializable;

/**
 * @author panyox
 */
public interface Identity extends Serializable {

    Object getIdentity();

}