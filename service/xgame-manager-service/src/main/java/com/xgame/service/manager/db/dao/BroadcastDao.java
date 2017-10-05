package com.xgame.service.manager.db.dao;

import com.xgame.service.manager.db.dto.BroadCastRegularDto;

import java.util.List;

public interface BroadcastDao extends BaseDao {

    List<BroadCastRegularDto> getRegularTasks();
    void saveRegularTask (BroadCastRegularDto dto);
    void updateRegularTaskById (BroadCastRegularDto dto);
    void deleteRegularTaskById  (Long transection);
}
