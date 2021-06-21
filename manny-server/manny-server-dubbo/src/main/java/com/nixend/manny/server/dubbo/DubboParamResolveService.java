package com.nixend.manny.server.dubbo;

import com.nixend.manny.common.utils.ParamUtils;
import com.nixend.manny.core.ParamResolveService;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

/**
 * @author panyox
 */
public class DubboParamResolveService implements ParamResolveService {

    @Override
    public Pair<String[], Object[]> buildParameter(String body, Map<String, String> parameterTypes) {
        return ParamUtils.buildParameters(body, parameterTypes);
    }
}
