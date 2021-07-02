package com.nixend.manny.registry.zookeeper;

import com.alibaba.fastjson.JSON;
import com.nixend.manny.common.constant.Constants;
import com.nixend.manny.common.model.RouteData;
import com.nixend.manny.common.model.ServiceData;
import com.nixend.manny.registry.api.RegistryService;
import com.nixend.manny.remoting.zookeeper.CuratorZookeeperClient;
import lombok.extern.slf4j.Slf4j;

/**
 * @author panyox
 */
@Slf4j
public class ZookeeperRegistryService implements RegistryService {

    private final CuratorZookeeperClient client;

    public ZookeeperRegistryService(final CuratorZookeeperClient client) {
        this.client = client;
        initNode();
    }

    private void initNode() {
        if (!client.checkExists(Constants.DUBBO_SERVICE_PATH)) {
            client.createPersistent(Constants.DUBBO_SERVICE_PATH);
        }
        if (!client.checkExists(Constants.DUBBO_PROVIDERS_PATH)) {
            client.createPersistent(Constants.DUBBO_PROVIDERS_PATH);
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
    public void unRegisterRoute(RouteData route) {
        try {
            String path = buildPath(route);
            client.delete(path);
            log.info("zookeeper remove route success! route: {}", route);
        } catch (Exception var) {
            log.error("zookeeper remove error {} ", var.getMessage());
        }
    }

    /**
     * /manny/dubbo/providers/com...UserService
     *
     * @param service
     */
    @Override
    public void registerProvider(ServiceData service) {
        try {
            String path = buildProviderPath(service);
            client.createPersistent(path, JSON.toJSONString(service));
            log.info("zookeeper register provider success! {}", service);
        } catch (Exception var) {
            log.error("zookeeper register error {} ", var.getMessage());
        }
    }

    @Override
    public void unRegisterProvider(ServiceData service) {
        try {
            String path = buildProviderPath(service);
            client.delete(path);
            log.info("zookeeper delete provider success! {}", service);
        } catch (Exception var) {
            log.error("zookeeper delete error {} ", var.getMessage());
        }
    }

    /**
     * eg. /manny/com.test.service.UserService/routers/findUser
     *
     * @param routeData
     * @return
     */
    private String buildPath(RouteData routeData) {
        return String.join(Constants.SLASH, Constants.DUBBO_SERVICE_PATH, routeData.getService().getName(), routeData.getMethod().getName());
    }

    private String buildProviderPath(ServiceData serviceData) {
        return String.join(Constants.SLASH, Constants.DUBBO_PROVIDERS_PATH, serviceData.getName());
    }
}
