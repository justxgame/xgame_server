package com.xgame.service.load.balance.service;

import com.xgame.service.load.balance.db.dao.ServerDao;
import com.xgame.service.load.balance.db.dto.ServerDto;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class ServerService {
    @Inject
    ServerDao serverDao;
    public ServerDto getServerInfoById(Integer serverId){return serverDao.getServerInfoById(serverId);}
}
