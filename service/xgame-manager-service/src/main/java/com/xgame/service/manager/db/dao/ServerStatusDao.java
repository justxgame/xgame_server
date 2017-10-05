package com.xgame.service.manager.db.dao;

import com.xgame.service.manager.db.dto.ServerStatusDto;

import java.util.List;

public interface ServerStatusDao extends BaseDao {
    List<ServerStatusDto> getAllActive();
}
