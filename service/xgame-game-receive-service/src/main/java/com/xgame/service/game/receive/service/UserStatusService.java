package com.xgame.service.game.receive.service;

import com.xgame.service.game.receive.db.dao.UserStatusDao;
import com.xgame.service.game.receive.db.dto.UserStatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserStatusService {
    @Autowired
    private UserStatusDao userStatusDao;

    public void saveObjet(UserStatusDto dto){
        userStatusDao.saveObject(dto);
    }
}
