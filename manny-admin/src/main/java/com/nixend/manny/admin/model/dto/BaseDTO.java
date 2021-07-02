package com.nixend.manny.admin.model.dto;

import lombok.Data;

/**
 * @author panyox
 */
@Data
public abstract class BaseDTO {
    private String id;
    private String application;
    private String service;
    private String serviceVersion;
    private String serviceGroup;
    private String registryAddress;
}
