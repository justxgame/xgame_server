package com.xgame.service.game.receive.service;

import com.xgame.service.game.receive.db.dao.RecallPhoneDao;
import com.xgame.service.game.receive.db.dto.RecallPhoneDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecallPhoneService {

    @Autowired
    private RecallPhoneDao phoneDao;
    public void savePhoneReall(RecallPhoneDto dto){
        phoneDao.saveObject(dto);
    }
}
