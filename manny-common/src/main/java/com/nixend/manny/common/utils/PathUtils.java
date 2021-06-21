package com.nixend.manny.common.utils;

import com.nixend.manny.common.constant.Constants;
import com.nixend.manny.common.model.PathInfo;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author panyox
 */
public class PathUtils {

    /**
     * orders
     * v1/order/detail/89iu/ko
     *
     * @param path
     * @return
     */
    public static PathInfo parse(String path) {
        if (!StringUtils.hasLength(path)) {
            return null;
        }
        if (path.startsWith(Constants.SLASH)) {
            path = path.substring(1);
        }
        if (path.endsWith(Constants.SLASH)) {
            path = path.substring(0, path.length() - 1);
        }
        String[] slices = path.split(Constants.SLASH);
        if (slices.length <= 2) {
            return null;
        } else {
            PathInfo pathInfo = new PathInfo();
            Pattern pattern = Pattern.compile(Constants.VERSION_REGX);
            Matcher matcher = pattern.matcher(slices[0]);
            String version = matcher.find() ? slices[0] : Constants.DEFAULT_VERSION;
            pathInfo.setServiceId(String.join(Constants.SLASH, version, slices[1]));
            String methodId = Arrays.stream(slices).skip(2).collect(Collectors.joining(Constants.SLASH));
            pathInfo.setMethodId(methodId);
            return pathInfo;
        }
    }

    public static boolean checkVersion(String v) {
        Pattern pattern = Pattern.compile(Constants.VERSION_REGX);
        Matcher matcher = pattern.matcher(v);
        return matcher.find();
    }

    public static String clearPath(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        str = str.trim();
        if (str.startsWith(Constants.SLASH)) {
            str = str.substring(1);
        }
        if (str.endsWith(Constants.SLASH)) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
