package com.xgame.order.consumer;

import com.xgame.order.consumer.biz.FuelCardRechargeBiz;
import com.xgame.order.consumer.biz.PhoneMiRechargeBiz;
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
        System.out.println(" Timer start");
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
            if (rewardOrderLogDtos!=null&&!rewardOrderLogDtos.isEmpty()){
                logger.info("[ConsumerTimerTask] get order from db.size "+rewardOrderLogDtos.size());
                List<RewardOrderLogDto> phoneDtos = getPhoneRechargeDto(rewardOrderLogDtos);
                List<RewardOrderLogDto> fuelCardDtos = getFuelCardRechargeDto(rewardOrderLogDtos);
                List<RewardOrderLogDto> phoneMidtos = getPhoneMiRechargeDto(rewardOrderLogDtos);

                FuelCardRechargeBiz fuelCardRechargeBiz = new FuelCardRechargeBiz(sqlSession,rewardBoxDtos);
                PhoneReChargeBiz phoneReChargeBiz = new PhoneReChargeBiz(sqlSession,rewardBoxDtos);
                PhoneMiRechargeBiz phoneMiRechargeBiz = new PhoneMiRechargeBiz(sqlSession, rewardBoxDtos);
                List<RewardOrderInfoDto> fuelOrderInfoDtos=null;
                List<RewardOrderInfoDto> phoneOrderInfoDtos=null;
                List<RewardOrderInfoDto> phoneMiInfoDtos = null;
                if (fuelCardDtos!=null&&!fuelCardDtos.isEmpty()){
                    fuelOrderInfoDtos = fuelCardRechargeBiz.getProcessedResult(fuelCardDtos);
                }
                if (phoneDtos!=null&&!phoneDtos.isEmpty()){
                    phoneOrderInfoDtos = phoneReChargeBiz.getProcessedResult(phoneDtos);
                }
                if(phoneMidtos!=null&&!phoneMidtos.isEmpty()){
                    phoneMiInfoDtos = phoneMiRechargeBiz.getProcessedResult(phoneMidtos);
                }



                RewardOrderInfoDao rewardOrderInfoDao = sqlSession.getMapper(RewardOrderInfoDao.class);
                if (null!=fuelOrderInfoDtos&&!fuelOrderInfoDtos.isEmpty()){
                    rewardOrderInfoDao.saveObjects(fuelOrderInfoDtos);
                }
                if(null!=rewardOrderInfoDao&&!phoneOrderInfoDtos.isEmpty()){
                    rewardOrderInfoDao.saveObjects(phoneOrderInfoDtos);
                }
                if(null!=phoneMiInfoDtos&&!phoneOrderInfoDtos.isEmpty()){
                    rewardOrderInfoDao.saveObjects(phoneMidtos);
                }
            }


        }catch (Exception e){
            logger.error("get data from db error"+e);
        }



    }
    private  List<RewardOrderLogDto> getPhoneRechargeDto( List<RewardOrderLogDto> rewardOrderLogDtos) {
        List<RewardOrderLogDto> dtos = new ArrayList<>();
        for (RewardOrderLogDto dto : rewardOrderLogDtos) {
            if (dto.getItem_type() == 100) {
                dtos.add(dto);
            }
        }
        return dtos;
    }

    private  List<RewardOrderLogDto> getFuelCardRechargeDto( List<RewardOrderLogDto> rewardOrderLogDtos) {
        List<RewardOrderLogDto> dtos = new ArrayList<>();
        for (RewardOrderLogDto dto : rewardOrderLogDtos) {
            if (dto.getItem_type() == 200) {
                dtos.add(dto);
            }
        }
        return dtos;
    }
    private  List<RewardOrderLogDto> getPhoneMiRechargeDto( List<RewardOrderLogDto> rewardOrderLogDtos) {
        List<RewardOrderLogDto> dtos = new ArrayList<>();
        for (RewardOrderLogDto dto : rewardOrderLogDtos) {
            if (dto.getItem_type() == 300) {
                dtos.add(dto);
            }
        }
        return dtos;
    }


}


