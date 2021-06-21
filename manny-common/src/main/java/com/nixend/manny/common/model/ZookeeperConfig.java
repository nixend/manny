package com.nixend.manny.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author panyox
 */
@Data
public class ZookeeperConfig implements Serializable {
    private String address;
    private Integer sessionTimeout;
    private Integer connectionTimeout;
}
