package com.nixend.manny.demo.client.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author panyox
 */
@Data
public class User implements Serializable {

    private Integer id;

    private String name;
    
}
