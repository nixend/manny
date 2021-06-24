package com.nixend.manny.server.dubbo;

import com.nixend.manny.common.model.MethodParam;
import com.nixend.manny.common.utils.ParamUtils;
import com.nixend.manny.core.ParamResolveService;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author panyox
 */
public class DubboParamResolveService implements ParamResolveService {

    @Override
    public Pair<String[], Object[]> buildParameter(String body, MethodParam methodParam) {
        return ParamUtils.buildParameters(body, methodParam);
    }

    @Override
    public Pair<String[], Object[]> buildParameter(String body, MethodParam methodParam, Object authInfo) {
        return ParamUtils.buildParameters(body, methodParam, authInfo);
    }

}
