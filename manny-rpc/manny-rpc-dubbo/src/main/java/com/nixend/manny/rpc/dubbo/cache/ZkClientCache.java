package com.nixend.manny.rpc.dubbo.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;
import com.nixend.manny.common.exception.MannyException;
import com.nixend.manny.remoting.zookeeper.CuratorZookeeperClient;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * @author panyox
 */
@Slf4j
public class ZkClientCache {

    private static volatile ZkClientCache instance;

    private final int maxCount = 10000;

    private final LoadingCache<String, CuratorZookeeperClient> cache = CacheBuilder.newBuilder()
            .maximumWeight(maxCount)
            .weigher((Weigher<String, CuratorZookeeperClient>) (string, zookeeperClient) -> getSize())
            .build((new CacheLoader<String, CuratorZookeeperClient>() {
                @Override
                public CuratorZookeeperClient load(final String key) {
                    return new CuratorZookeeperClient();
                }
            }));

    private int getSize() {
        return (int) cache.size();
    }

    public static ZkClientCache getInstance() {
        if (instance == null) {
            synchronized (ZkClientCache.class) {
                if (instance == null) {
                    instance = new ZkClientCache();
                }
            }
        }
        return instance;
    }

    public CuratorZookeeperClient get(final String id) {
        try {
            return cache.get(id);
        } catch (ExecutionException e) {
            throw new MannyException(e.getCause());
        }
    }

    public CuratorZookeeperClient initClient(String address) {
        try {
            CuratorZookeeperClient zkClient = get(address);
            if (zkClient.getClient() == null) {
                zkClient.init(address);
                cache.put(address, zkClient);
            }
            return zkClient;
        } catch (Exception ex) {
            log.error("init zk client error: {}", ex.getMessage());
            return null;
        }
    }
}
