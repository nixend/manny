package com.nixend.manny.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

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
     * @param parameterTypes
     * @return
     */
    public static Pair<String[], Object[]> buildParameters(final String body, final Map<String, String> parameterTypes) {
        if (Objects.isNull(body) || Objects.isNull(parameterTypes)) {
            return new ImmutablePair<>(new String[]{}, new Object[]{});
        }
        List<String> paramNameList = new ArrayList<>(parameterTypes.keySet());
        List<String> paramTypeList = new ArrayList<>(parameterTypes.values());
        if (paramTypeList.size() == 1 && !isBaseType(paramTypeList.get(0))) {
            return buildSingleParameter(body, paramTypeList.get(0));
        }
        JSONObject bodyMap = JSON.parseObject(body);
        List<Object> list = new LinkedList<>();
        for (String key : paramNameList) {
            list.add(bodyMap.getOrDefault(key, null));
        }
        String[] paramTypes = paramTypeList.toArray(new String[0]);
        Object[] objects = list.toArray();
        return new ImmutablePair<>(paramTypes, objects);
    }

    private static boolean isBaseType(final String paramType) {
        return paramType.startsWith("java") || paramType.startsWith("[Ljava");
    }
}
