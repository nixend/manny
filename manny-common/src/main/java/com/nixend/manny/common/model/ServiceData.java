package com.nixend.manny.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author panyox
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceData implements Serializable {
    private String id;
    private String name;
    private String path;
    private String version;
    private String rpcType;
    private String registryAddress;
    private Integer retry;
    private Integer timeout;
    private String application;
    private String address;
}
