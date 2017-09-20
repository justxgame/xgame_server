package com.xgame.service.manager.service;

import com.xgame.service.manager.db.dao.RewardBoxDao;
import com.xgame.service.manager.db.dto.RewardBoxDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RewardBoxService {
    @Autowired
    RewardBoxDao rewardBoxDao;

    public List<RewardBoxDto> getAll(){
        return rewardBoxDao.getAll();
    }

}
