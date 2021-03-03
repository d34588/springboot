package com.example.springboot.demo.controller;

import com.example.springboot.demo.service.AsyncTest2Service;
import com.example.springboot.demo.service.AsyncTest1Service;
import com.example.springboot.demo.utils.JedisPoolUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class HelloController {
    private static  final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private AsyncTest1Service asyncTestService;

    @Autowired
    private AsyncTest2Service asyncTest2Service;


    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() throws InterruptedException, ExecutionException {
        // 有返回值异步
        CompletableFuture<String> result1 = asyncTestService.asyncMethod("liao1", 1000);
        CompletableFuture<String> result2 = asyncTestService.asyncMethod("liao2", 1000);
        CompletableFuture<String> result3 = asyncTestService.asyncMethod("liao3", 5000);
        try {
            // 在设置时间类未返回结果，会直接抛出异常TimeoutException，messages为null
            // logger.info(result3.get(3, TimeUnit.SECONDS));

            // join等待所有调用的异步方法都执行完，才继续往下执行
            CompletableFuture.allOf(result1, result2, result3).join();
            logger.info(result1.get());
            logger.info(result2.get());
            logger.info(result3.get());
        } catch (Exception e) {
            logger.error("CompletableFuture get error", e);
        }


        // 没返回值异步
        for (int i = 1; i <= 10; i++) {
            asyncTestService.voidMethod("liao" + i);
        }

        // 没返回值异步
        for (int i = 1; i <= 11; i++) {
            asyncTest2Service.asyncMethod("qing" + i);
        }
        return "Hello Spring Boot.";
    }

    @RequestMapping(value = "/statisProperty", method = RequestMethod.GET)
    public String statisProperty() {
        logger.info("{}={}", JedisPoolUtils.redisIP, JedisPoolUtils.redisPort);
        return "OK";
    }
}
