package com.xgame.service.load.balance;

import com.xgame.service.load.balance.service.ServerService;
import com.xgame.service.load.balance.service.UserService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * manually maintain spring bean & service
 */
public class ServiceContextFactory {

    private static ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-mybatis.xml");

    public static ServerService serverService;
    public static UserService userService;

    static {

        serverService = (ServerService) applicationContext.getBean("serverService");
        userService = (UserService) applicationContext.getBean("userService");
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(String name) throws BeansException {
        return (T) applicationContext.getBean(name);
    }

}