package com.nixend.manny.core;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

/**
 * @author panyox
 */
public interface ParamResolveService {

    Pair<String[], Object[]> buildParameter(String body, Map<String, String> parameterTypes);

}
