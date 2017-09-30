package com.xgame.service.manager;

import com.xgame.service.manager.db.dao.KpiDao;
import com.xgame.service.manager.service.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * manually maintain spring bean & service
 */
public class ServiceContextFactory {

    private static ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-mybatis.xml");
    public static TokenService tokenService;
    public static UserService userService;
    public static RewardOrderDetailService rewardOrderDetailService;
    public static RewardBoxService rewardBoxService;
    public static ServerStatusService serverStatusService;
    public static BroadcastService broadcastService;
    public static KpiService kpiService;
    public static KpiDao kpiDao;

    static {
        tokenService = (TokenService) applicationContext.getBean("tokenService");
        userService = (UserService) applicationContext.getBean("userService");
        rewardOrderDetailService = (RewardOrderDetailService) applicationContext.getBean("rewardOrderDetailService");
        rewardBoxService = (RewardBoxService) applicationContext.getBean("rewardBoxService");
        serverStatusService = (ServerStatusService) applicationContext.getBean("serverStatusService");
        broadcastService = (BroadcastService) applicationContext.getBean("broadcastService");
        kpiService = (KpiService) applicationContext.getBean("kpiService");
        kpiDao = kpiService.getKpiDao();
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(String name) throws BeansException {
        return (T) applicationContext.getBean(name);
    }

}