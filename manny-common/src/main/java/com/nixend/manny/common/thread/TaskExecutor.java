package com.nixend.manny.common.thread;

import java.util.concurrent.*;

/**
 * @author panyox
 */
public class TaskExecutor {

    private int corePoolSize = 1;

    private int maxPoolSize = 100;

    private int keepAliveSeconds = 60;

    private int queueCapacity = 10000;

    private ThreadPoolExecutor threadPoolExecutor;

    private static volatile TaskExecutor instance;

    public static TaskExecutor getInstance() {
        if (instance == null) {
            synchronized (TaskExecutor.class) {
                if (instance == null) {
                    instance = new TaskExecutor();
                }
            }
        }
        return instance;
    }

    public TaskExecutor() {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(queueCapacity);
        this.threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveSeconds, TimeUnit.SECONDS, workQueue);
    }

    public void execute(Runnable task) {
        try {
            this.threadPoolExecutor.execute(task);
        } catch (RejectedExecutionException var1) {
            System.out.println(var1);
        }
    }
}
