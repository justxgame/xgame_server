package com.xgame.service.load.balance.service;

import com.xgame.service.load.balance.db.dao.ServerDao;
import com.xgame.service.load.balance.db.dto.ServerDto;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class ServerService {
    @Inject
    ServerDao serverDao;
    public ServerDto getServerInfoById(Integer serverId){return serverDao.getServerInfoById(serverId);}
    public List<ServerDto> getActiveServer(String ver,String plaform){return serverDao.getActiveServer(ver,plaform);}
}
