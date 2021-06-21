package com.nixend.manny.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author panyox
 */
@Data
public class DubboRegisterConfig implements Serializable {
    private String type;
    private String host;

    public String getAddress() {
        return String.format("%s://%s", type, host);
    }
}
