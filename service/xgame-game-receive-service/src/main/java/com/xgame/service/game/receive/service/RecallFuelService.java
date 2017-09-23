package com.xgame.service.game.receive.service;

import com.xgame.service.game.receive.db.dao.RecallFuelDao;
import com.xgame.service.game.receive.db.dto.RecallPhoneDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecallFuelService {
    @Autowired
    private RecallFuelDao fuelDao;
    public void saveFuelDto(RecallPhoneDto dto){
        fuelDao.saveObject(dto);

    }
}
