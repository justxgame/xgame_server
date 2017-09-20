package com.xgame.order.consumer;

import com.xgame.order.consumer.biz.FuelCardRechargeBiz;
import com.xgame.order.consumer.biz.PhoneReChargeBiz;
import com.xgame.order.consumer.db.dao.RewardBoxDao;
import com.xgame.order.consumer.db.dao.RewardOrderInfoDao;
import com.xgame.order.consumer.db.dao.RewardOrderLogDao;
import com.xgame.order.consumer.db.dto.RewardBoxDto;
import com.xgame.order.consumer.db.dto.RewardOrderInfoDto;
import com.xgame.order.consumer.db.dto.RewardOrderLogDto;
import com.xgame.order.consumer.util.MybatisSqlSessionFactory;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class OrderConsumer {
    private static Logger logger = LoggerFactory.getLogger(OrderConsumer.class.getName());
    public static void main(String[] args) {
        SqlSession sqlSession = MybatisSqlSessionFactory.getSqlSessionAutoCommit();
        Timer timer = new Timer("conmer timer");
        //每十秒获取db数据执行
        timer.schedule(new ConsumerTimerTask(sqlSession),0,10*1000);

    }


}

class ConsumerTimerTask extends TimerTask{
    private SqlSession sqlSession;
    public ConsumerTimerTask(SqlSession sqlSession){
        this.sqlSession = sqlSession;
    }
    private static Logger logger = LoggerFactory.getLogger(TimerTask.class.getName());
    @Override
    public void run() {
        try {
            RewardBoxDao rewardBoxDao = sqlSession.getMapper(RewardBoxDao.class);
            List<RewardBoxDto> rewardBoxDtos = rewardBoxDao.getAll();

            RewardOrderLogDao rewardOrderLogDao = sqlSession.getMapper(RewardOrderLogDao.class);
            List<RewardOrderLogDto> rewardOrderLogDtos = rewardOrderLogDao.getAll();
            if (rewardOrderLogDtos==null||rewardOrderLogDtos.isEmpty()){
                logger.info("[ConsumerTimerTask] get data from db empty");
            }
            List<RewardOrderLogDto> phoneDtos = getPhoneRechargeDto(rewardOrderLogDtos);
            List<RewardOrderLogDto> fuelCardDtos = getFuelCardRechargeDto(rewardOrderLogDtos);

            FuelCardRechargeBiz fuelCardRechargeBiz = new FuelCardRechargeBiz(sqlSession,rewardBoxDtos);
            PhoneReChargeBiz phoneReChargeBiz = new PhoneReChargeBiz(sqlSession,rewardBoxDtos);

            List<RewardOrderInfoDto> fuelOrderInfoDtos = fuelCardRechargeBiz.getProcessedResult(fuelCardDtos);
            List<RewardOrderInfoDto> phoneOrderInfoDtos = phoneReChargeBiz.getProcessedResult(phoneDtos);

            RewardOrderInfoDao rewardOrderInfoDao = sqlSession.getMapper(RewardOrderInfoDao.class);
            if (!fuelOrderInfoDtos.isEmpty()){
                rewardOrderInfoDao.saveObjects(fuelOrderInfoDtos);
            }
            if(!phoneOrderInfoDtos.isEmpty()){
                rewardOrderInfoDao.saveObjects(phoneOrderInfoDtos);
            }

        }catch (Exception e){
            logger.error("get data from db error");
        }



    }
    private  List<RewardOrderLogDto> getPhoneRechargeDto( List<RewardOrderLogDto> rewardOrderLogDtos) {
        List<RewardOrderLogDto> dtos = new ArrayList<>();
        for (RewardOrderLogDto dto : rewardOrderLogDtos) {
            if (dto.getOrder_type() == 100) {
                dtos.add(dto);
            }
        }
        return dtos;
    }

    private  List<RewardOrderLogDto> getFuelCardRechargeDto( List<RewardOrderLogDto> rewardOrderLogDtos) {
        List<RewardOrderLogDto> dtos = new ArrayList<>();
        for (RewardOrderLogDto dto : rewardOrderLogDtos) {
            if (dto.getOrder_type() == 200) {
                dtos.add(dto);
            }
        }
        return dtos;
    }


}


