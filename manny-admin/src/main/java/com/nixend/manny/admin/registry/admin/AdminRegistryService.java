package com.nixend.manny.admin.registry.admin;

import com.nixend.manny.admin.registry.RegistryService;
import com.nixend.manny.admin.utils.Constants;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author panyox
 */
public class AdminRegistryService implements RegistryService {

    private String address;

    private CuratorFramework zkClient;

    private static String root = "/manny";

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public void init() {
        if (address == null) {
            throw new IllegalStateException("server address is null, cannot init");
        }
        CuratorFrameworkFactory.Builder zkClientBuilder = CuratorFrameworkFactory.builder().
                connectString(address).
                retryPolicy(new ExponentialBackoffRetry(1000, 3));
        zkClient = zkClientBuilder.build();
        zkClient.start();
    }

    @Override
    public String setConfig(String key, String value) {
        return setConfig(null, key, value);
    }

    @Override
    public String getConfig(String key) {
        return getConfig(null, key);
    }

    @Override
    public boolean deleteConfig(String key) {
        return deleteConfig(null, key);
    }

    @Override
    public String setConfig(String group, String key, String value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("key or value cannot be null");
        }
        String path = getNodePath(key, group);
        try {
            if (zkClient.checkExists().forPath(path) == null) {
                zkClient.create().creatingParentsIfNeeded().forPath(path);
            }
            zkClient.setData().forPath(path, value.getBytes());
            return value;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public String getConfig(String group, String key) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }
        String path = getNodePath(key, group);

        try {
            if (zkClient.checkExists().forPath(path) == null) {
                return null;
            }
            return new String(zkClient.getData().forPath(path));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean deleteConfig(String group, String key) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }
        String path = getNodePath(key, group);
        try {
            zkClient.delete().forPath(path);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public String getPath(String key) {
        return getNodePath(key, null);
    }

    @Override
    public String getPath(String group, String key) {
        return getNodePath(key, group);
    }

    private String getNodePath(String path, String group) {
        if (path == null) {
            throw new IllegalArgumentException("path cannot be null");
        }
        return toRootDir(group) + path;
    }

    private String toRootDir(String group) {
        if (group != null) {
            if (!group.startsWith(Constants.PATH_SEPARATOR)) {
                root = Constants.PATH_SEPARATOR + group;
            } else {
                root = group;
            }
        }
        if (root.equals(Constants.PATH_SEPARATOR)) {
            return root;
        }
        return root + Constants.PATH_SEPARATOR;
    }
}
