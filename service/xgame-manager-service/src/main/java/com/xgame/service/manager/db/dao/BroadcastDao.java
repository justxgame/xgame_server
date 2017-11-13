package com.xgame.service.manager.db.dao;

import com.xgame.service.manager.db.dto.BroadCastDto;
import com.xgame.service.manager.db.dto.BroadCastRegularDto;
import com.xgame.service.manager.db.dto.PushDto;

import java.util.List;

public interface BroadcastDao extends BaseDao {

    List<BroadCastDto> getAllBroadCast(Integer type);
    List<BroadCastRegularDto> getRegularTasksByType(Integer type);
    List<BroadCastRegularDto> getAllRegularTasks();
    void saveRegularTask (BroadCastRegularDto dto);
    void updateRegularTaskById (BroadCastRegularDto dto);
    void deleteRegularTaskById  (Long transection);
    void savePush(PushDto dto);
}
