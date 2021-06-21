package com.nixend.manny.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author panyox
 */
@Data
public class PathInfo implements Serializable {
    private String serviceId;
    private String methodId;
}
