package com.nixend.manny.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nixend.manny.common.constant.Constants;
import com.nixend.manny.common.model.MethodParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author panyox
 */
public class ParamUtils {

    public static Pair<String[], Object[]> buildSingleParameter(final String body, final String parameterTypes) {
        JSONObject bodyMap = JSON.parseObject(body);
        return new ImmutablePair<>(new String[]{parameterTypes}, new Object[]{bodyMap});
    }
    
    /**
     * {"name":"java.lang.String","id":"java.lang.Integer"}
     * {"order":"com.test.model.Order"}
     *
     * @param body
     * @param methodParam
     * @return
     */
    public static Pair<String[], Object[]> buildParameters(final String body, final MethodParam methodParam) {
        if (Objects.isNull(body) || Objects.isNull(methodParam)) {
            return new ImmutablePair<>(new String[]{}, new Object[]{});
        }
        String[] paramNames = StringUtils.split(methodParam.getNames(), Constants.MARK);
        String[] paramTypes = StringUtils.split(methodParam.getTypes(), Constants.MARK);
        if (paramTypes.length == 1 && !isBaseType(paramTypes[0])) {
            return buildSingleParameter(body, paramTypes[0]);
        }
        JSONObject bodyMap = JSON.parseObject(body);
        List<Object> list = new LinkedList<>();
        for (String key : paramNames) {
            list.add(bodyMap.getOrDefault(key, null));
        }
        Object[] objects = list.toArray();
        return new ImmutablePair<>(paramTypes, objects);
    }

    private static boolean isBaseType(final String paramType) {
        return paramType.startsWith("java") || paramType.startsWith("[Ljava");
    }
}
