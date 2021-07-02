package com.nixend.manny.admin.model.dto;

import lombok.Data;

/**
 * @author panyox
 */
@Data
public class TagDTO {
    private String name;
    private Integer weight;
    private String[] addresses;

    public TagDTO(String name, Integer weight, String[] addresses) {
        this.name = name;
        this.weight = weight;
        this.addresses = addresses;
    }
}
