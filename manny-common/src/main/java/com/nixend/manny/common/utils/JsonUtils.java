package com.nixend.manny.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

import java.util.HashMap;

/**
 * @author panyox
 */
public class JsonUtils {
    
    private static final SimplePropertyPreFilter CLASS_NAME_PRE_FILTER = new SimplePropertyPreFilter(HashMap.class);

    static {
        CLASS_NAME_PRE_FILTER.getExcludes().add("class");
    }

    public static Object removeClass(Object obj) {
        String json = JSON.toJSONString(obj, CLASS_NAME_PRE_FILTER, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty);
        return JSON.parse(json);
    }
}
