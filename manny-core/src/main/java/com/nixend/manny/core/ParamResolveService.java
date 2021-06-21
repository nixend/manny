package com.nixend.manny.core;

import com.nixend.manny.common.model.MethodParam;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author panyox
 */
public interface ParamResolveService {

    Pair<String[], Object[]> buildParameter(String body, MethodParam methodParam);

}
