package com.nixend.manny.common.constant;

/**
 * @author panyox
 */
public interface Constants {
    String SLASH = "/";
    String DASH = "-";
    String DOT = ".";
    String MARK = ",";
    String AT = "@";

    String ROUTER_PARAMS = "routerParams";
    String SERVICE_DATA = "serviceData";
    String METHOD_DATA = "methodData";
    String ROUTE_DATA = "routeData";
    String DEFAULT_VERSION = "v1";
    String VERSION_REGX = "^v\\d*\\.?\\d*$";
    String AUTH_INFO = "authInfo";
    String EMPTY_JSON = "{}";
    String DEFAULT_INDEX = "index";
    String DUBBO_TAG = "dubbo.tag";
    String DUBBO_TAG_NAME = "DubboTag";

    String DUBBO_SERVICE_PATH = "/manny/dubbo/services";
    String DUBBO_PROVIDERS_PATH = "/manny/dubbo/providers";
    String DUBBO_TAG_PATH = "/manny/dubbo/tags";
}
