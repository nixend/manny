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
public class RouteData implements Serializable {
    private ServiceData service;
    private MethodData method;
}
