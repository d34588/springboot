package com.example.springboot.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * 异步方法@Async
 *
 * https://www.cnblogs.com/wlandwl/p/async.html
 * 一、基于@Async的name名称：TaskPoolConfig.java
 *
 * 二、基于项目默认的线程池：实现接口AsyncConfigurer 或 继承AsyncConfigurerSupport（最终也是实现接口AsyncConfigurer）
 * 设置默认线程池defaultExecutor，默认是空的，当重新实现接口AsyncConfigurer的getAsyncExecutor()时，可以设置项目默认的线程池。
 * 通过查看Spring源码关于@Async的默认调用规则，会优先查询源码中实现AsyncConfigurer这个接口的类，实现这个接口的类为AsyncConfigurerSupport。
 * 但默认配置的线程池和异步处理方法均为空，所以，无论是继承或者重新实现接口，都需指定一个线程池。且重新实现 public Executor getAsyncExecutor()方法。
 *
 * 三、获取系统默认线程池：
 * 1、由于AsyncConfigurer的默认线程池在源码中为空，Spring通过beanFactory.getBean(TaskExecutor.class)先查看是否有线程池，
 * 2、未配置时，通过beanFactory.getBean(DEFAULT_TASK_EXECUTOR_BEAN_NAME, Executor.class)，又查询是否存在默认名称为TaskExecutor的线程池。
 * 所以可在项目中，定义名称为TaskExecutor的bean生成一个默认线程池。也可不指定线程池的名称，申明一个线程池，本身底层是基于TaskExecutor.class便可。
 * 3、最后都没有找到项目中设置的默认线程池时，采用spring 默认的线程池SimpleAsyncTaskExecutor
 */
@Service
public class AsyncTest1Service {
    private static final Logger logger = LoggerFactory.getLogger(AsyncTest1Service.class);

    /**
     * 带参数的异步调用 异步方法可以传入参数
     * 对于返回值是void，若需要抛出异常，需手动new一个异常抛出。异常会被AsyncUncaughtExceptionHandler处理掉
     *
     * @param name
     * @throws InterruptedException
     */
    @Async
    public void voidMethod(String name) throws InterruptedException {
        String s = "Hello," + name + "!";
        logger.info("voidMethod:{}", s);
        //模拟耗时操作，5秒
        Thread.sleep(5000);

        // throw new IllegalArgumentException(s);
    }

    /**
     * 异常调用返回Future
     * 对于返回值是Future，不会被AsyncUncaughtExceptionHandler处理，需要我们在方法中捕获异常并处理
     * 或者在调用方在调用Futrue.get时捕获异常进行处理
     *
     * @param name
     * @param millis
     * @return
     * @throws InterruptedException
     */
    @Async
    public CompletableFuture<String> asyncMethod(String name, int millis) throws InterruptedException {
        String s = "Hello," + name + "!";
        logger.info(s);

        if ("liao3".equals(name)) {
            Thread.sleep(millis); // 模拟耗时操作，5秒
            // throw new IllegalArgumentException(s);
        }

        return CompletableFuture.completedFuture(s + " success.");
    }

}
