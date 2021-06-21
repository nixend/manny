package com.nixend.manny.remoting.zookeeper;

import java.util.List;

/**
 * @author panyox
 */
public interface ZookeeperClient {

    void create(String path, boolean ephemeral);

    void delete(String path);

    List<String> getChildren(String path);

    void addDataListener(String path, DataListener listener);

    void removeDataListener(String path, DataListener listener);

    boolean isConnected();

    void close();

    void create(String path, String content, boolean ephemeral);

    String getContent(String path);

    boolean checkExists(String path);
}
