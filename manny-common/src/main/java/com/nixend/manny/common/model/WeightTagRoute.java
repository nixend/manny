package com.nixend.manny.common.model;

import lombok.Data;

import java.util.List;

/**
 * @author panyox
 */
@Data
public class WeightTagRoute {
    private String application;
    private boolean enable;
    private List<WeightTag> tags;
}
