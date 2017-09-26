package com.xgame.order.consumer;

import com.xgame.order.consumer.business.OrderBusinessProcessor;
import com.xgame.order.consumer.conf.Configuration;
import com.xgame.order.consumer.db.dto.RewardOrderLogMappingDto;
import com.xgame.service.common.type.OrderLogType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.TimerTask;


public class OrderConsumerMain {

    private static Logger logger = LoggerFactory.getLogger(OrderConsumerMain.class.getName());

    public static void main(String[] args) {
        //每十秒获取db数据执行
        // timer.schedule(new OrderConsumerTimerTask(sqlSession), 0, 10 * 1000);
        OrderConsumerTask orderConsumerTask = new OrderConsumerTask();
        new Thread(orderConsumerTask).start();

        try {
            System.out.println("start web server ...");
            final String appDir = new OrderConsumerMain().getWebAppsPath();
            final Server server = new Server(Configuration.getInstance().getConfig().getInt("xgame.order.service.port", 8888));
            WebAppContext context = new WebAppContext();
            context.setContextPath("/");
            context.setBaseResource(Resource.newResource(appDir));
            server.setHandler(context);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("webapp not found in CLASSPATH.");
            System.exit(2);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("start metastore service error.");
            System.exit(1);
        }
    }

    private String getWebAppsPath() throws IOException {
        URL url = getClass().getClassLoader().getResource("webapps");
        if (url == null)
            throw new IOException("webapp not found in CLASSPATH");
        return url.toString();
    }
}


class OrderConsumerTask implements Runnable {

    OrderBusinessProcessor orderBusinessProcessor = new OrderBusinessProcessor();
    private static Logger logger = LoggerFactory.getLogger(OrderConsumerTask.class.getName());

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


