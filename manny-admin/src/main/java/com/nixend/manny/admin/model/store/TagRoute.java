package com.nixend.manny.admin.model.store;

import lombok.Data;

import java.util.List;

/**
 * @author panyox
 */
@Data
public class TagRoute {
    private int priority;
    private boolean enabled;
    private boolean force;
    private boolean runtime;
    private String key;
    private List<Tag> tags;
}
