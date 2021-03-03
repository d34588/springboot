package com.example.springboot.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncTest2Service {
    private static final Logger logger = LoggerFactory.getLogger(AsyncTest2Service.class);

    /**
     * 带参数的异步调用 异步方法可以传入参数
     *
     * @param name
     * @throws InterruptedException
     */
    @Async("new_task")
    public void asyncMethod(String name) throws InterruptedException {
        String s = "Hello," + name + "!";
        logger.info("asyncMethod:{}", s);
        //模拟耗时操作，5秒
        Thread.sleep(5000);
    }

}
