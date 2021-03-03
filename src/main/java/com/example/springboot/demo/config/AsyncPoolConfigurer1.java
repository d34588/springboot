package com.example.springboot.demo.config;

import org.springframework.aop.interceptor.AsyncExecutionAspectSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义异步线程池
 *
 * 自定义线程池，可对系统中线程池更加细粒度的控制，方便调整线程池大小配置，线程执行异常控制和处理。
 * 在设置系统自定义线程池代替默认线程池时，虽可通过多种模式设置，但替换默认线程池最终产生的线程池有且只能设置一个
 * （不能同时设置多个类继承AsyncConfigurerSupport或多个类实现接口AsyncConfigurer）。
 *
 * 自定义线程池有如下模式：
 * 1、重新实现接口AsyncConfigurer：AsyncPoolConfigurer2.java
 * 2、继承AsyncConfigurerSupport：AsyncPoolConfigurer3.java
 * 3、配置由自定义的TaskExecutor替代内置的任务执行器：@Bean
 *
 * Async默认异步配置使用的是SimpleAsyncTaskExecutor，该线程池默认来一个任务创建一个线程，若系统中不断的创建线程，最终会导致系统占用内存过高，引发OutOfMemoryError错误。
 * 针对线程创建问题，SimpleAsyncTaskExecutor提供了限流机制，通过concurrencyLimit属性来控制开关，
 * 当concurrencyLimit>=0时开启限流机制，默认关闭限流机制即concurrencyLimit=-1，当关闭情况下，会不断创建新的线程来处理任务。
 * 基于默认配置，SimpleAsyncTaskExecutor并不是严格意义的线程池，达不到线程复用的功能。
 */
@Configuration
public class AsyncPoolConfigurer1 {
    /**
     * 配置spring异步任务线程池，
     * 1、如果不配置Spring会自动创建SimpleAsyncTaskExecutor异步线程池，并使用它来执行异步方法。
     * 2、@Bean的name不配置时，默认为：taskExecutor；如果要指定多个线程池需要指定name
     *
     * @return 异步线程池
     */
    //@Bean
    @Bean(name = AsyncExecutionAspectSupport.DEFAULT_TASK_EXECUTOR_BEAN_NAME)
    public Executor taskExecutor() {
        /*
        Spring 已经实现的异步线程池
        1. SimpleAsyncTaskExecutor：不是真的线程池，这个类不重用线程，默认每次调用都会创建一个新的线程。
        2. SyncTaskExecutor：这个类没有实现异步调用，只是一个同步操作。只适用于不需要多线程的地方。
        3. ConcurrentTaskExecutor：Executor的适配类，不推荐使用。如果ThreadPoolTaskExecutor不满足要求时，才用考虑使用这个类。
        4. SimpleThreadPoolTaskExecutor：是Quartz的SimpleThreadPool的类。线程池同时被quartz和非quartz使用，才需要使用此类。
        5. ThreadPoolTaskExecutor ：最常使用，推荐。 其实质是对java.util.concurrent.ThreadPoolExecutor的包装。
         */
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程池大小
        executor.setCorePoolSize(5);
        // 最大线程数
        executor.setMaxPoolSize(10);
        // 队列容量：先从核心线程池执行任务， 如果不够把任务放到缓存，还不够就创建最大线程，最后有空闲线程才执行缓存队列任务
        executor.setQueueCapacity(1);
        // 活跃时间
        executor.setKeepAliveSeconds(60);
        // 线程名字前缀
        executor.setThreadNamePrefix("mtaskExecutor-");
        // 拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 初始化
        executor.initialize();

        return executor;
    }

    /**
     * 多个线程池
     *  @Async注解，使用系统默认或者自定义的线程池（代替默认线程池）。
     *  可在项目中设置多个线程池，在异步调用时，指明需要调用的线程池名称，如@Async("new_task")。
     *
     * @return 异步线程池
     */
    @Bean(name = "new_task")
    public Executor newTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程池大小
        executor.setCorePoolSize(1);
        // 最大线程数
        executor.setMaxPoolSize(10);
        // 队列容量
        executor.setQueueCapacity(0);
        // 活跃时间
        executor.setKeepAliveSeconds(60);
        // 线程名字前缀
        executor.setThreadNamePrefix("newAsyncTask-");

        /**
         * 当线程池的任务缓存队列已满并且线程池中的线程数目达到maximumPoolSize，如果还有任务到来就会采取任务拒绝策略，
         * （实际在运行任务 大于 MaxPoolSize+QueueCapacity）
         * 通常有以下四种策略：
         * ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。
         * ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
         * ThreadPoolExecutor.DiscardOldestPolicy：当有新任务添加到线程池被拒绝时，线程池会丢弃阻塞队列中末尾的任务，然后将被拒绝的任务添加到末尾（重复此过程）
         * ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务（相当于被拒绝的任务，在主线程运行）
         */
        // 拒绝策略，默认：ThreadPoolExecutor.AbortPolicy
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());

        // 等待任务在关机时完成--表明等待所有线程执行完
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 等待任务在关机时的等待时间 （默认为0，此时立即停止），并没等待xx秒后强制停止
        executor.setAwaitTerminationSeconds(20);

        // 初始化
        executor.initialize();

        return executor;
    }
}
