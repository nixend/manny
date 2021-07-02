package com.nixend.manny.remoting.zookeeper;

import com.nixend.manny.common.model.ZookeeperConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author panyox
 */
public class CuratorZookeeperClient implements ZookeeperClient {

    protected static final Logger logger = LoggerFactory.getLogger(CuratorZookeeperClient.class);

    static final Charset CHARSET = StandardCharsets.UTF_8;
    private CuratorFramework client = null;
    private static Map<String, TreeCache> treeCacheMap = new ConcurrentHashMap<>();

    protected static int DEFAULT_CONNECTION_TIMEOUT_MS = 10 * 1000;
    protected static int DEFAULT_SESSION_TIMEOUT_MS = 60 * 1000;

    public CuratorZookeeperClient() {

    }

    public CuratorZookeeperClient(ZookeeperConfig zookeeperConfig) {
        try {
            int conTimeout = Objects.isNull(zookeeperConfig.getConnectionTimeout()) ? DEFAULT_CONNECTION_TIMEOUT_MS : zookeeperConfig.getConnectionTimeout();
            int sesTimeout = Objects.isNull(zookeeperConfig.getSessionTimeout()) ? DEFAULT_SESSION_TIMEOUT_MS : zookeeperConfig.getSessionTimeout();
            CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                    .connectString(zookeeperConfig.getAddress())
                    .retryPolicy(new RetryNTimes(1, 1000))
                    .connectionTimeoutMs(conTimeout)
                    .sessionTimeoutMs(sesTimeout);
            client = builder.build();
            client.start();
            boolean connected = client.blockUntilConnected(DEFAULT_CONNECTION_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            if (!connected) {
                throw new IllegalStateException("zookeeper not connected");
            }

        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public CuratorZookeeperClient(String address) {
        try {
            CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                    .connectString(address)
                    .retryPolicy(new RetryNTimes(1, 1000))
                    .connectionTimeoutMs(DEFAULT_CONNECTION_TIMEOUT_MS)
                    .sessionTimeoutMs(DEFAULT_SESSION_TIMEOUT_MS);
            client = builder.build();
            client.start();
            boolean connected = client.blockUntilConnected(DEFAULT_CONNECTION_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            if (!connected) {
                throw new IllegalStateException("zookeeper not connected");
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public void init(String address) {
        try {
            CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                    .connectString(address)
                    .retryPolicy(new RetryNTimes(1, 1000))
                    .connectionTimeoutMs(DEFAULT_CONNECTION_TIMEOUT_MS)
                    .sessionTimeoutMs(DEFAULT_SESSION_TIMEOUT_MS);
            client = builder.build();
            client.start();
            boolean connected = client.blockUntilConnected(DEFAULT_CONNECTION_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            if (!connected) {
                throw new IllegalStateException("zookeeper not connected");
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public CuratorFramework getClient() {
        return client;
    }

    @Override
    public void create(String path, boolean ephemeral) {
        if (ephemeral) {
            createEphemeral(path);
        } else {
            createPersistent(path);
        }
    }

    public void createPersistent(String path) {
        try {
            client.create().creatingParentsIfNeeded().forPath(path);
        } catch (KeeperException.NodeExistsException e) {
            logger.warn("ZNode " + path + " already exists.", e);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public void createEphemeral(String path) {
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
        } catch (KeeperException.NodeExistsException e) {
            logger.warn("ZNode " + path + " already exists, since we will only try to recreate a node on a session expiration" +
                    ", this duplication might be caused by a delete delay from the zk server, which means the old expired session" +
                    " may still holds this ZNode and the server just hasn't got time to do the deletion. In this case, " +
                    "we can just try to delete and create again.", e);
            delete(path);
            createEphemeral(path);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public void createPersistent(String path, String data) {
        byte[] dataBytes = data.getBytes(CHARSET);
        try {
            client.create().creatingParentsIfNeeded().forPath(path, dataBytes);
        } catch (KeeperException.NodeExistsException e) {
            try {
                client.setData().forPath(path, dataBytes);
            } catch (Exception e1) {
                throw new IllegalStateException(e.getMessage(), e1);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public void createEphemeral(String path, String data) {
        byte[] dataBytes = data.getBytes(CHARSET);
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, dataBytes);
        } catch (KeeperException.NodeExistsException e) {
            logger.warn("ZNode " + path + " already exists, since we will only try to recreate a node on a session expiration" +
                    ", this duplication might be caused by a delete delay from the zk server, which means the old expired session" +
                    " may still holds this ZNode and the server just hasn't got time to do the deletion. In this case, " +
                    "we can just try to delete and create again.", e);
            delete(path);
            createEphemeral(path, data);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(String path) {
        try {
            client.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (KeeperException.NoNodeException ignored) {
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public List<String> getChildren(String path) {
        try {
            return client.getChildren().forPath(path);
        } catch (KeeperException.NoNodeException e) {
            return null;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void addDataListener(String path, DataListener listener) {
        TreeCacheListenerImpl cacheListener = createTargetDataListener(path, listener);
        addTargetDataListener(path, cacheListener);
    }

    public TreeCacheListenerImpl createTargetDataListener(String path, DataListener listener) {
        return new TreeCacheListenerImpl(client, listener, path);
    }

    public void addTargetDataListener(String path, TreeCacheListenerImpl treeCacheListener) {
        try {
            TreeCache treeCache = new TreeCache(client, path);
            if (treeCacheMap.putIfAbsent(path, treeCache) != null) {
                return;
            }
            treeCache.getListenable().addListener(treeCacheListener);
            treeCache.start();
        } catch (Exception e) {
            throw new IllegalStateException("Add nodeCache listener for path:" + path, e);
        }
    }

    @Override
    public void removeDataListener(String path, DataListener listener) {

    }

    protected void removeTargetDataListener(String path, TreeCacheListenerImpl treeCacheListener) {
        TreeCache nodeCache = treeCacheMap.get(path);
        if (nodeCache != null) {
            nodeCache.getListenable().removeListener(treeCacheListener);
        }
        treeCacheListener.dataListener = null;
    }

    @Override
    public boolean isConnected() {
        return client.getZookeeperClient().isConnected();
    }

    @Override
    public void close() {
        client.close();
    }

    @Override
    public void create(String path, String content, boolean ephemeral) {
        if (ephemeral) {
            createEphemeral(path, content);
        } else {
            createPersistent(path, content);
        }
    }

    @Override
    public String getContent(String path) {
        return null;
    }

    @Override
    public boolean checkExists(String path) {
        try {
            if (client.checkExists().forPath(path) != null) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static class TreeCacheListenerImpl implements TreeCacheListener {

        private CuratorFramework client;

        private volatile DataListener dataListener;

        private String path;

        protected TreeCacheListenerImpl() {
        }

        public TreeCacheListenerImpl(CuratorFramework client, DataListener dataListener, String path) {
            this.client = client;
            this.dataListener = dataListener;
            this.path = path;
        }

        @Override
        public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
            ChildData data = event.getData();
            switch (event.getType()) {
                case NODE_ADDED:
                    dataListener.dataChanged(data.getPath(), new String(data.getData(), CHARSET), EventType.NodeCreated);
                    break;
                case NODE_UPDATED:
                    dataListener.dataChanged(data.getPath(), new String(data.getData(), CHARSET), EventType.NodeDataChanged);
                    break;
                case NODE_REMOVED:
                    dataListener.dataChanged(data.getPath(), new String(data.getData(), CHARSET), EventType.NodeDeleted);
                    break;
            }
        }
    }
}
