package com.example.springboot.demo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * 静态属性和方法获取yml配制三种方法
 */
@Component
public class JedisPoolUtils {
    /**
     * 声明静态属性-IP
     */
    public static String redisIP = "127.0.0.1";
    /**
     * 声明静态属性-Port
     */
    public static Integer redisPort = 6379;

    /**
     * 方法1:
     * 先把 yml 值读取到普通属性中再转到静态属性值时面
     * PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器调用一次; PostConstruct在构造函数之后执行,init()方法之前执行。PreDestroy（）方法在destroy()方法执行执行之后执行
     *
     * 注意此类使用了 @Component 属性注解了说明是需要在启动类 Application 启动的时候加载的，所以我们本地写一个方法调用 util 的时候是获取不到 name的。
     */
    // @PostConstruct
    public void transValues() {
        System.out.println("方法1");
        redisIP = this.redisip;
        redisPort = this.redisport;
    }

    /**
     * 方法2:
     * 采用静态代码块配置静态变量：通过静态变量记录各种属性，在工程的其他方可以很方便的引用，
     * 可以根据配置文件里面配置的属性重新设置静态属性，如果没有配置文件则使用代码里面的默认值。

    static {
        System.out.println("方法2");
        redisIP = (String) getCommonYml("redis.redisip");
    }
     */

    /**
     * 方法3:
     * 给静态属性添加set方法，并通过Value 注解注入yml中配置的值
     */
    @Value("${redis.redisip}")
    public void setRedisIP(String redisIP) {
        System.out.println("方法3");
        this.redisIP = redisIP;
    }

    public static Object getCommonYml(Object key){
        Resource resource = new ClassPathResource("/application.yml");
        Properties properties = null;
        try {
            YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
            yamlFactory.setResources(resource);
            properties =  yamlFactory.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return properties.get(key);
    }

    /**
     * 通过 Value 注解获取 yml 中配置的值
     */
    @Value("${redis.redisip}")
    private String redisip;
    /**
     * 通过 Value 注解获取 yml 中配置的值
     */
    @Value("${redis.redisport}")
    private Integer redisport;

}
