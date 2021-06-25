package com.nixend.manny.registry.zookeeper;

import com.alibaba.fastjson.JSON;
import com.nixend.manny.common.constant.Constants;
import com.nixend.manny.common.model.RouteData;
import com.nixend.manny.registry.api.RegistryService;
import com.nixend.manny.remoting.zookeeper.CuratorZookeeperClient;
import lombok.extern.slf4j.Slf4j;

/**
 * @author panyox
 */
@Slf4j
public class ZookeeperRegistryService implements RegistryService {

    private CuratorZookeeperClient client;

    public ZookeeperRegistryService(final CuratorZookeeperClient client) {
        this.client = client;
        initNode();
    }

    private void initNode() {
        if (!client.checkExists(Constants.DUBBO_SERVICE_PATH)) {
            client.createPersistent(Constants.DUBBO_SERVICE_PATH);
        }
    }

    @Override
    public void registerRoute(RouteData route) {
        try {
            String path = buildPath(route);
            client.createPersistent(path, JSON.toJSONString(route));
            log.info("zookeeper register route success! routeData: {}", route);
        } catch (Exception var) {
            log.error("zookeeper register error {} ", var.getMessage());
        }
    }

    @Override
    public void unregisterRoute(RouteData route) {
        try {
            String path = buildPath(route);
            client.delete(path);
            log.info("zookeeper remove route success! route: {}", route);
        } catch (Exception var) {
            log.error("zookeeper remove error {} ", var.getMessage());
        }
    }

    /**
     * eg. /manny/dubbo/service/com.test.service.UserService/findUser
     *
     * @param routeData
     * @return
     */
    private String buildPath(RouteData routeData) {
        return String.join(Constants.SLASH, Constants.DUBBO_SERVICE_PATH, routeData.getService().getName(), routeData.getMethod().getName());
    }
}
