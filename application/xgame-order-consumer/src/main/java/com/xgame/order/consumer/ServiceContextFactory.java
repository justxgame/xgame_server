package com.xgame.order.consumer;


import com.xgame.order.consumer.business.pmi.AbstractPmiBusiness;
import com.xgame.order.consumer.db.dao.RewardOrderInfoDao;
import com.xgame.order.consumer.db.dao.RewardOrderLogMappingDao;
import com.xgame.order.consumer.db.dao.SubOrderInfoDao;
import com.xgame.order.consumer.service.OrderService;
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

    private static OrderService orderService;
    private static  RewardOrderLogMappingDao rewardOrderLogMappingDao;
    private static RewardOrderInfoDao rewardOrderInfoDao;
    private static SubOrderInfoDao subOrderInfoDao;

    static {
        orderService =  (OrderService) applicationContext.getBean("orderService");
        rewardOrderLogMappingDao = orderService.getRewardOrderLogMappingDao();
        rewardOrderInfoDao  = orderService.getRewardOrderInfoDao();
        subOrderInfoDao = orderService.getSubOrderInfoDao();
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(String name) throws BeansException {
        return (T) applicationContext.getBean(name);
    }


    public static RewardOrderLogMappingDao getRewardOrderLogMappingDao() {
        return rewardOrderLogMappingDao;
    }

    public static RewardOrderInfoDao getRewardOrderInfoDao() {
        return rewardOrderInfoDao;
    }

    public static SubOrderInfoDao getSubOrderInfoDao() {
        return subOrderInfoDao;
    }
}