package com.nixend.manny.admin.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author panyox
 */
@Data
public class TagRouteDTO extends BaseDTO {
    private int priority;
    private boolean enabled;
    private boolean force;
    private boolean runtime;
    private List<TagDTO> tags;
}
