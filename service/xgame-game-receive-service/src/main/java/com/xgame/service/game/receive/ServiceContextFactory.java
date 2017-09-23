package com.xgame.service.game.receive;

import com.xgame.service.game.receive.service.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * manually maintain spring bean & service
 *
 */
public class ServiceContextFactory {

    private static ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-mybatis.xml");
    public static RewardOrderService rewardOrderService;
    public static UserStatusService userStatusService;
    public static UserAddressService userAddressService;
    public static ServerStatusService serverStatusService;
    public static RecallFuelService recallFuelService;
    public static RecallPhoneService recallPhoneService;
//    public static AdxConfigManagerService adxConfigManagerService;
//    public static AppConfigService appConfigService;
//    public static ChannelConfigService channelConfigService;
//    public static CustomerConfigService customerConfigService;
//    public static SystemConfigService systemConfigService;
//    public static AdvConfigService advConfigService;
//    public static AdvStatisticService advStatisticService;
//    public static TokenService tokenService;
//    public static UserService userService;

    static {
//        adxConfigManagerService = (AdxConfigManagerService) applicationContext.getBean("adxConfigManagerService");
//        appConfigService = (AppConfigService) applicationContext.getBean("appConfigService");
//        channelConfigService = (ChannelConfigService) applicationContext.getBean("channelConfigService");
//        customerConfigService  = (CustomerConfigService) applicationContext.getBean("customerConfigService");
//        systemConfigService  = (SystemConfigService) applicationContext.getBean("systemConfigService");
//        advConfigService  = (AdvConfigService) applicationContext.getBean("advConfigService");
//        advStatisticService  = (AdvStatisticService) applicationContext.getBean("advStatisticService");
//        tokenService = (TokenService) applicationContext.getBean("tokenService");
//        userService = (UserService) applicationContext.getBean("userService");
        rewardOrderService = (RewardOrderService) applicationContext.getBean("rewardOrderService");
        userAddressService = (UserAddressService) applicationContext.getBean("userAddressService");
        userStatusService = (UserStatusService) applicationContext.getBean("userStatusService");
        serverStatusService = (ServerStatusService) applicationContext.getBean("serverStatusService");
        recallFuelService = (RecallFuelService) applicationContext.getBean("recallFuelService");
        recallPhoneService = (RecallPhoneService) applicationContext.getBean("recallPhoneService");
    }
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(String name) throws BeansException {
        return (T) applicationContext.getBean(name);
    }

}