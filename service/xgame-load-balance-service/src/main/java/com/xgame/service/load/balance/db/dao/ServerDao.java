package com.xgame.service.load.balance.db.dao;

import com.xgame.service.load.balance.db.dto.ServerDto;

import java.util.List;

public interface ServerDao extends BaseDao {

    public ServerDto getServerInfoById(Integer serverId);
    public List<ServerDto> getActiveServer();
}
