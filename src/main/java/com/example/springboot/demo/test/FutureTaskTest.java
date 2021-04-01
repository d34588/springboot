package com.example.springboot.demo.test;

import com.example.springboot.demo.config.ThreadPoolExecutorConfigurer;
import com.example.springboot.demo.service.ThreadPoolTestService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTaskTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<FutureTask<String>> list = new ArrayList<FutureTask<String>>();

        //模拟10个多线程（Callable）请求，异步获取结果
        for (int i = 0; i < 10; i++) {
            ThreadPoolTestService service = new ThreadPoolTestService(i + "");
            FutureTask<String> future = new FutureTask<String>(service);
            ThreadPoolExecutorConfigurer.getInstance().submit(future);
            list.add(future);
        }

        // 循环获取结果
        for (FutureTask<String> future : list) {
            // 判断是否结束（完成/取消/异常）返回true
            if (future.isDone()) {
                try {
                    // get获取结果，若无结果会阻塞至异步计算完成
                    System.out.println("result:" + future.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // 如果没有完成，休眠2秒，并取消这个任务，后面继续获取其他结果
                Thread.sleep(2000);
                future.cancel(true);
                System.out.println("cancel:");
            }
        }

        System.out.println("^^^^");
        Thread.sleep(10);

//        for (int i = 0; i < 10; i++) {
//            ThreadPoolTestService service = new ThreadPoolTestService(i+"");
//            Future<String> future = ThreadPoolExecutorConfigurer.getInstance().submit(service);
//            future.cancel(true);
//            System.out.println(future.get());
//        }
    }
}
