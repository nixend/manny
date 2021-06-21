package com.nixend.manny.server.dubbo.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;
import com.nixend.manny.common.model.RouteData;
import com.nixend.manny.core.exception.MannyException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

/**
 * @author panyox
 */
@Slf4j
public class ReferenceCache {

    private ApplicationConfig applicationConfig;

    private ConcurrentMap<String, RegistryConfig> registryConfigs = new ConcurrentHashMap<>();

    private final int maxCount = 10000;

    private final Integer DEFAULT_TIMEOUT = 5;

    private static volatile ReferenceCache instance;

    private final LoadingCache<String, ReferenceConfig<GenericService>> cache = CacheBuilder.newBuilder()
            .maximumWeight(maxCount)
            .weigher((Weigher<String, ReferenceConfig<GenericService>>) (string, referenceConfig) -> getSize())
            .build((new CacheLoader<String, ReferenceConfig<GenericService>>() {
                @Override
                public ReferenceConfig<GenericService> load(final String key) {
                    return new ReferenceConfig<>();
                }
            }));

    private int getSize() {
        return (int) cache.size();
    }

    public static ReferenceCache getInstance() {
        if (instance == null) {
            synchronized (ReferenceCache.class) {
                if (instance == null) {
                    instance = new ReferenceCache();
                }
            }
        }
        return instance;
    }

    public <T> ReferenceConfig<T> get(final String id) {
        try {
            return (ReferenceConfig<T>) cache.get(id);
        } catch (ExecutionException e) {
            throw new MannyException(e.getCause());
        }
    }

    public ReferenceCache() {
        if (applicationConfig == null) {
            applicationConfig = new ApplicationConfig("manny_proxy");
        }
    }

    /**
     * name com.test.service.UserService
     *
     * @param routeData
     * @return
     */
    public ReferenceConfig<GenericService> initRef(final RouteData routeData) {
        try {
            ReferenceConfig<GenericService> reference = cache.get(routeData.getService().getName());
            if (StringUtils.isNoneBlank(reference.getInterface())) {
                return reference;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return initReference(routeData);
    }

    private RegistryConfig getRegistryConfig(String address) {
        return registryConfigs.computeIfAbsent(address, k -> addRegistryConfig(address));
    }

    private RegistryConfig addRegistryConfig(String address) {
        return new RegistryConfig(address);
    }

    private ReferenceConfig<GenericService> initReference(final RouteData routeData) {

        ApplicationModel.getConfigManager().setApplication(applicationConfig);
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setInterface(routeData.getService().getName());
        reference.setGeneric("true");
        reference.setRegistry(getRegistryConfig(routeData.getService().getRegistryAddress()));
        reference.setProtocol("dubbo");
        reference.setTimeout(DEFAULT_TIMEOUT);
        try {
            Object obj = reference.get();
            if (obj != null) {
                log.info("init dubbo reference success {}", routeData);
                cache.put(routeData.getService().getName(), reference);
            }
        } catch (Exception var1) {
            var1.printStackTrace();
            log.error("init apache dubbo reference got error: {}", var1.getMessage());
        }
        return reference;
    }
}
