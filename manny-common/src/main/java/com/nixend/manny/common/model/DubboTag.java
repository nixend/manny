package com.nixend.manny.common.model;

import lombok.Data;

/**
 * @author panyox
 */
@Data
public class DubboTag {
    private String application;
    private String name;
    private Integer weight;
    private Integer currentWeight;
}
