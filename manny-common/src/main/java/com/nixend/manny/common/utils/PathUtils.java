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
     * order/create
     * v1/order/create
     * v1/order /index
     * <p>
     * v1/login/index   ->  v1/login/index
     * v1/login         ->  v1/login/index
     * login            ->  v1/login/index
     *
     * @param path
     * @return
     */
    public static PathInfo parse(String path) {
        if (!StringUtils.hasLength(path)) {
            return null;
        }
        path = clearPath(path);
        String[] pathList = path.split(Constants.SLASH);
        int pathLen = pathList.length;
        if (pathLen == 0) {
            return null;
        }

        Pattern pattern = Pattern.compile(Constants.VERSION_REGX);
        Matcher matcher = pattern.matcher(pathList[0]);
        boolean hasVersion = matcher.find();

        if (pathLen == 1) {
            if (hasVersion) {
                return null;
            }
            PathInfo pathInfo = new PathInfo();
            String version = Constants.DEFAULT_VERSION;
            pathInfo.setServiceId(String.join(Constants.SLASH, version, pathList[0]));
            pathInfo.setMethodId(Constants.DEFAULT_INDEX);
            return pathInfo;
        } else if (pathLen == 2) {
            PathInfo pathInfo = new PathInfo();
            if (hasVersion) {
                String serviceId = String.join(Constants.SLASH, pathList[0], pathList[1]);
                pathInfo.setServiceId(serviceId);
                pathInfo.setMethodId(Constants.DEFAULT_INDEX);
            } else {
                String serviceId = String.join(Constants.SLASH, Constants.DEFAULT_VERSION, pathList[0]);
                pathInfo.setServiceId(serviceId);
                pathInfo.setMethodId(pathList[1]);
            }
            return pathInfo;
        } else {
            PathInfo pathInfo = new PathInfo();
            if (hasVersion) {
                String version = pathList[0];
                pathInfo.setServiceId(String.join(Constants.SLASH, version, pathList[1]));
                String methodId = Arrays.stream(pathList).skip(2).collect(Collectors.joining(Constants.SLASH));
                pathInfo.setMethodId(methodId);
            } else {
                String version = Constants.DEFAULT_VERSION;
                pathInfo.setServiceId(String.join(Constants.SLASH, version, pathList[0]));
                String methodId = Arrays.stream(pathList).skip(1).collect(Collectors.joining(Constants.SLASH));
                pathInfo.setMethodId(methodId);
            }
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

    public static boolean isPattern(String path) {
        if (path == null) {
            return false;
        }
        return (path.indexOf('*') != -1 || path.indexOf('?') != -1);
    }

    /**
     * @param pattern
     * @param path
     * @return
     */
    public static boolean match(String pattern, String path) {
        if (!StringUtils.hasLength(path)) {
            return false;
        }
        int index = pattern.indexOf("*");
        if (index == 0) {
            return true;
        }
        if (index > path.length()) {
            return false;
        }
        index = index - 1;
        String sub = path.substring(0, index);
        return sub.equals(pattern.substring(0, index));
    }
}
