package com.example.springboot.demo.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorConfigurer {
    private static ThreadPoolExecutor pool = null;

    public static ThreadPoolExecutor getInstance() {
        if (pool == null) {
            pool = new ThreadPoolExecutor(5, 10, 0, TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(5),new ThreadPoolExecutor.AbortPolicy());
        }
        return pool;
    }

}
