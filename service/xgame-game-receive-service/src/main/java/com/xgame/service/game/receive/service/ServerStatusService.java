package com.xgame.service.game.receive.service;

import com.xgame.service.game.receive.db.dao.ServerStatusDao;
import com.xgame.service.game.receive.db.dto.ServerStatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServerStatusService {
    @Autowired
    private ServerStatusDao serverStatusDao;
    public void saveObject(ServerStatusDto dto){

        serverStatusDao.saveObject(dto);
    }
}
