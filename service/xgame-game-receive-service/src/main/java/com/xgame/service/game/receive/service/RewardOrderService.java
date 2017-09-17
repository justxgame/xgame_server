package com.xgame.service.game.receive.service;

import com.xgame.service.game.receive.db.dao.RewardOrderDao;
import com.xgame.service.game.receive.db.dto.RewardOrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RewardOrderService {
    @Autowired
    private RewardOrderDao rewardOrderDao;

    public void saveObject(RewardOrderDto rewardOrderDto){ rewardOrderDao.saveObject(rewardOrderDto);}
}
