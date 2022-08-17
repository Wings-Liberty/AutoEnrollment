package org.cloud.utils;

import java.util.concurrent.*;

public class ThreadPoolUtil {

    public static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors() * 8,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100), // 开太大就 OOM 了
                Executors.defaultThreadFactory()
                );


    public static ThreadPoolExecutor get(){
        return threadPool;
    }
}
