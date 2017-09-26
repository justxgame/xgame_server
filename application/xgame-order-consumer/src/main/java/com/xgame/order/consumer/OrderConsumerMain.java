package com.xgame.order.consumer;

import com.google.common.util.concurrent.Runnables;
import com.google.common.util.concurrent.Service;
import com.xgame.order.consumer.business.OrderBusinessProcessor;
import com.xgame.order.consumer.db.dao.RewardOrderLogMappingDao;
import com.xgame.order.consumer.db.dto.RewardOrderLogMappingDto;
import com.xgame.service.common.type.OrderLogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class OrderConsumerMain {

    private static Logger logger = LoggerFactory.getLogger(OrderConsumerMain.class.getName());

    public static void main(String[] args) {
        //每十秒获取db数据执行
        // timer.schedule(new OrderConsumerTimerTask(sqlSession), 0, 10 * 1000);
        OrderConsumerTask orderConsumerTask = new OrderConsumerTask();
        new Thread(orderConsumerTask).start();
    }


}


class OrderConsumerTask implements Runnable {

    OrderBusinessProcessor orderBusinessProcessor = new OrderBusinessProcessor();
    private static Logger logger = LoggerFactory.getLogger(TimerTask.class.getName());

    @Override
    public void run() {

        logger.info("[OrderConsumerTask] task starting");

        while (true) {
            try {
                // 获取所有未处理的订单
                List<RewardOrderLogMappingDto> rewardOrderLogMappingDtos = ServiceContextFactory.getRewardOrderLogMappingDao().getAllOrdersLogByOrderType(OrderLogType.NoConsumer.getValue());
                if (null == rewardOrderLogMappingDtos || 0 == rewardOrderLogMappingDtos.size()) {
                    logger.info("[OrderConsumerTask] no orders to consumer");
                }
                logger.info("[OrderConsumerTask] get order from db.size = " + rewardOrderLogMappingDtos.size());

                // 处理每一条订单
                for (RewardOrderLogMappingDto rewardOrderLogMappingDto : rewardOrderLogMappingDtos) {
                    orderBusinessProcessor.processOrder(rewardOrderLogMappingDto);
                }
            } catch (Exception e) {
                logger.error("[OrderConsumerTask] processor error", e);
            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}


