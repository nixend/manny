package com.nixend.manny.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @author panyox
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MethodData implements Serializable {
    private String id;
    private String serviceId;
    private String name;
    private String path;
    private String httpMethod;
    private Map<String, String> parameters;
}
