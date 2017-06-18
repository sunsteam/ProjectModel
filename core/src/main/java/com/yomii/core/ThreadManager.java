package com.yomii.core;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 内置一个线程池的线程管理类
 *
 * Created by Yomii on 2016/3/9.
 */
public class ThreadManager {

    private static ThreadPoolExecutor threadPoolExecutor;

    public static ThreadPoolExecutor getPool(){
        if (threadPoolExecutor == null) {
            synchronized (ThreadManager.class) {
                if (threadPoolExecutor == null) {
                    int coreSize = Runtime.getRuntime().availableProcessors();
                    threadPoolExecutor = new ThreadPoolExecutor(
                            //保持CPU核心*2+1
                            coreSize + 1,
                            //最大
                            coreSize * 2 + 1,
                            //存活时间
                            1,
                            //单位
                            TimeUnit.SECONDS,
                            //无限队列
                            new LinkedBlockingQueue<Runnable>(),
                            Executors.defaultThreadFactory(),
                            //抛弃新任务
                            new ThreadPoolExecutor.AbortPolicy());
                }
            }
        }
        return threadPoolExecutor;
    }

    public static void execute(Runnable runnable){
        getPool().execute(runnable);
    }

    public static void cancel(Runnable runnable){
        if (runnable!=null && getPool().isShutdown()) {
            getPool().getQueue().remove(runnable);
        }
    }
}
