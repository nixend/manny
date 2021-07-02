package com.nixend.manny.admin.registry;

/**
 * @author panyox
 */
public interface RegistryService {

    void init();

    void setAddress(String address);

    String getAddress();

    String setConfig(String key, String value);

    String getConfig(String key);

    boolean deleteConfig(String key);

    String setConfig(String group, String key, String value);

    String getConfig(String group, String key);

    boolean deleteConfig(String group, String key);

    String getPath(String key);

    String getPath(String group, String key);
}
