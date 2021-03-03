package com.example.springboot.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

/**
 * 自定义异步线程池
 *
 * 重新实现接口AsyncConfigurer：
 */
//@Configuration
public class AsyncPoolConfigurer2 implements AsyncConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(AsyncPoolConfigurer2.class);

    @Override
    public Executor getAsyncExecutor() {
        return executor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        // 无返回值异步方法，异常处理
        return new SpringAsyncExceptionHandler();
    }

    public ThreadPoolTaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("impAsyncTask-");
        executor.initialize();
        return executor;
    }

    class SpringAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
            // 打印异常信息
            logger.error("Exception occurs in async method", throwable);
        }
    }

}
