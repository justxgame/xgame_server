package com.xgame.service.manager;


import com.xgame.service.common.util.CommonUtil;
import com.xgame.service.manager.biz.BroadCastRegularBizProcessor;
import com.xgame.service.manager.biz.PushRegularBizProcessor;
import com.xgame.service.manager.db.dto.BroadCastRegularDto;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by william on 2017/9/9.
 */
public class XGameManagerMainShell {

    public static void main(String[] args) {
        BroadCastRegularTask task = new BroadCastRegularTask();
        new Thread(task).start();
        try {
            System.out.println("start web server ...");
            final String appDir = new XGameManagerMainShell().getWebAppsPath();
            final Server server = new Server(ServiceConfiguration.getInstance().getConfig().getInt("xgame.manager.service.port", 9100));
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

class BroadCastRegularTask implements Runnable{
    BroadCastRegularBizProcessor broadCastProcessor = new BroadCastRegularBizProcessor();
    PushRegularBizProcessor pushRegularBizProcessor = new PushRegularBizProcessor();
    private  Logger logger = LoggerFactory.getLogger(BroadCastRegularTask.class.getName());
    private static final Integer BROADCAST =1;
    private static final Integer PUSH =2;
    @Override
    public void run() {
        logger.info("[BroadCastRegularTask] start ");
        while (true){
            try {
                List<BroadCastRegularDto> dtos = ServiceContextFactory.getBroadCastDao().getAllRegularTasks();
                if (null==dtos||0==dtos.size()){
                    logger.info("[BroadCastRegularTask] get empty task");
                    continue;
                }
               // logger.info("[BroadCastRegularTask] get task size is "+dtos.size());

                List<BroadCastRegularDto> processTask = getNeedProcessTask(dtos);
                if (null==processTask||0==processTask.size()){
                    //logger.info("[BroadCastRegularTask] get empty process task");
                    continue;
                }
                logger.info("[BroadCastRegularTask] get need process task size is "+processTask.size());
                for (BroadCastRegularDto dto:processTask){

                    if (BROADCAST==dto.getType()){
                        broadCastProcessor.broadCastTaskProcess(dto);
                    }else if(PUSH==dto.getType()){
                        pushRegularBizProcessor.broadCastTaskProcess(dto);
                    }


                }
                logger.info("[BroadCastRegularTask]  task process finished");
            }catch (Throwable t){
                logger.error("[BroadCastRegularTask] process task error");
            }

            try {
                Thread.sleep(10*1000);
            }catch (Throwable t){
                logger.error("[BroadCastRegularTask] process task error");
            }
        }
    }

    /**
     * 比较当前时间和 next_send_date, 获取 task
     * 程序漏掉的会根据时间马上补发
     * @param dtos
     * @return
     */
    private  List<BroadCastRegularDto> getNeedProcessTask( List<BroadCastRegularDto> dtos){
        List<BroadCastRegularDto> result = new ArrayList<>();
        Long now = System.currentTimeMillis();
        for (BroadCastRegularDto dto:dtos){
            Long nextTime = CommonUtil.parseStr2Time(dto.getNext_send_date());
            if (isNextTime(now,nextTime)){
                result.add(dto);
            }

        }
        return result;
    }

    /**
     * 所有 next_time 小于当前时间的都算
     * @param now
     * @param nextTime
     * @return
     */
    private boolean isNextTime(Long now,Long nextTime){
        if (nextTime<now){
            return true;
        }
        return false;
    }
}
