package com.xgame.service.manager.service;

import com.xgame.service.manager.db.dao.RewardOrderDetailDao;
import com.xgame.service.manager.db.dto.RewardOrderDetailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RewardOrderDetailService {
    @Autowired
    public RewardOrderDetailDao rewardOrderDetailDao;
    public List<RewardOrderDetailDto> getAllByQuery (Map query){

        return rewardOrderDetailDao.getAllByQuery(query);
    }
    public List<RewardOrderDetailDto> getAllStatus(Map query){
        return rewardOrderDetailDao.getAllStatus(query);
    }

    public List<RewardOrderDetailDto> getAll(){
        return rewardOrderDetailDao.getAll();
    }
    public List<RewardOrderDetailDto> getAllType(Map query){
        return rewardOrderDetailDao.getAllType(query);
    }
    public void updateRecalorder(String orderid){
        rewardOrderDetailDao.updateRecallOrder(orderid);

    }


}
