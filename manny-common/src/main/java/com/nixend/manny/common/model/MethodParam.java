package com.nixend.manny.common.model;

import com.nixend.manny.common.constant.Constants;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author panyox
 */
@Data
public class MethodParam implements Serializable {
    private String names;
    private String types;

    public MethodParam() {

    }

    public MethodParam(String names, String types) {
        this.names = names;
        this.types = types;
    }

    public static MethodParam empty() {
        return new MethodParam("", "");
    }

    public static MethodParam parse(Method method) {
        int paramCount = method.getParameterCount();
        if (paramCount == 0) {
            return empty();
        }
        List<String> names = new LinkedList<>();
        List<String> types = new LinkedList<>();
        Parameter[] parameters = method.getParameters();
        Arrays.stream(parameters).forEach(parameter -> {
            names.add(parameter.getName());
            types.add(parameter.getType().getName());
        });
        MethodParam methodParam = new MethodParam();
        methodParam.setNames(String.join(Constants.MARK, names));
        methodParam.setTypes(String.join(Constants.MARK, types));
        return methodParam;
    }
}
