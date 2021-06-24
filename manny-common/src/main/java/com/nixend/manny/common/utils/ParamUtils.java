package com.nixend.manny.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nixend.manny.common.constant.Constants;
import com.nixend.manny.common.enums.ParamAnnotation;
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
     * order@ID-userId@PA
     *
     * @param body
     * @param paramName
     * @param paramType
     * @param authInfo
     * @return
     */
    public static Pair<String[], Object[]> buildSingleParameter(final String body, final String paramName, final String paramType, final Object authInfo) {
        Object val = null;
        if (hasIdentityAnnotation(paramName)) {
            String ide = getIdentityAnnotation(paramName);
            int i = ide.indexOf(Constants.DASH);
            if (i > 0) {
                JSONObject bodyMap = JSON.parseObject(body);
                String key = ide.substring(i + 1);
                bodyMap.put(key, authInfo);
                val = bodyMap;
            } else {
                val = authInfo;
            }
        } else {
            val = JSON.parseObject(body);
        }
        return new ImmutablePair<>(new String[]{paramType}, new Object[]{val});
    }

    /**
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

    public static Pair<String[], Object[]> buildParameters(final String body, final MethodParam methodParam, final Object authInfo) {
        if (Objects.isNull(body) || Objects.isNull(methodParam)) {
            return new ImmutablePair<>(new String[]{}, new Object[]{});
        }
        String[] paramNames = StringUtils.split(methodParam.getNames(), Constants.MARK);
        String[] paramTypes = StringUtils.split(methodParam.getTypes(), Constants.MARK);
        if (paramTypes.length == 1 && !isBaseType(paramTypes[0])) {
            return buildSingleParameter(body, paramNames[0], paramTypes[0], authInfo);
        }
        JSONObject bodyMap = JSON.parseObject(body);
        List<Object> list = new LinkedList<>();
        for (String name : paramNames) {
            if (hasIdentityAnnotation(name)) {
                String ide = getIdentityAnnotation(name);
                int i = ide.indexOf(Constants.DASH);
                if (i > 0) {
                    String key = ide.substring(i + 1);
                    String paramName = getParamName(name);
                    JSONObject param = bodyMap.getJSONObject(paramName);
                    if (param != null) {
                        param.put(key, authInfo);
                    }
                    list.add(param);
                } else {
                    list.add(authInfo);
                }
            } else {
                list.add(bodyMap.getOrDefault(name, null));
            }
        }
        Object[] objects = list.toArray();
        return new ImmutablePair<>(paramTypes, objects);
    }

    private static String getParamName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        String[] names = name.split(Constants.AT);
        if (names.length > 0) {
            return names[0];
        }
        return null;
    }

    private static String getIdentityAnnotation(String name) {
        String[] names = name.split(Constants.AT);
        String key = null;
        for (String n : names) {
            if (n.startsWith(ParamAnnotation.IDENTITY.getName())) {
                key = n;
                break;
            }
        }
        return key;
    }

    private static boolean hasIdentityAnnotation(String paramName) {
        return paramName.contains(Constants.AT + ParamAnnotation.IDENTITY.getName());
    }

    private static boolean isBaseType(final String paramType) {
        return paramType.startsWith("java") || paramType.startsWith("[Ljava");
    }
}
