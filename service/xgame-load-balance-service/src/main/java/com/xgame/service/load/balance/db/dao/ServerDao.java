package com.xgame.service.load.balance.db.dao;

import com.xgame.service.load.balance.db.dto.ServerDto;

public interface ServerDao extends BaseDao {

    public ServerDto getServerInfoById(Integer serverId);
}
