package com.nixend.manny.register.client.zookeeper;

import com.alibaba.fastjson.JSON;
import com.nixend.manny.common.constant.Constants;
import com.nixend.manny.common.model.RouteData;
import com.nixend.manny.register.client.api.RegisterService;
import com.nixend.manny.remoting.zookeeper.CuratorZookeeperClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author panyox
 */
public class ZookeeperRegisterService implements RegisterService {

    protected static final Logger logger = LoggerFactory.getLogger(ZookeeperRegisterService.class);

    private CuratorZookeeperClient client;

    public ZookeeperRegisterService(final CuratorZookeeperClient client) {
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
            logger.info("zookeeper register route success! routeData: {}", route);
        } catch (Exception var) {
            logger.error("zookeeper register error {} ", var.getMessage());
        }
    }

    @Override
    public void unregisterRoute(RouteData route) {
        try {
            String path = buildPath(route);
            client.delete(path);
            logger.info("zookeeper remove route success! route: {}", route);
        } catch (Exception var) {
            logger.error("zookeeper remove error {} ", var.getMessage());
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
