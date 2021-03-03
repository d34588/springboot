package com.example.springboot.demo.service;

import java.util.concurrent.Callable;

public class ThreadPoolTestService implements Callable<String> {
    private String name;

    public ThreadPoolTestService(String name) {
        this.name = name;
    }

    @Override
    public String call() throws InterruptedException {
        System.out.println("name:" + name);
        if ("5".equals(name) || "8".equals(name)) {
            Thread.sleep(3000);
            // Thread.currentThread().wait(3000);
        } else {
            Thread.sleep(2500);
        }
        System.out.println(name + "==");
        return name + "_success";
    }


}
