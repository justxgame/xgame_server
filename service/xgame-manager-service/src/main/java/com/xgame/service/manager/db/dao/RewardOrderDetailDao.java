package com.xgame.service.manager.db.dao;

import com.xgame.service.manager.db.dto.RewardOrderDetailDto;

import java.util.List;
import java.util.Map;

public interface RewardOrderDetailDao extends BaseDao{

    List<RewardOrderDetailDto> getAllByQuery(Map query);

    List<RewardOrderDetailDto> getAllStatus(Object o);

    List<RewardOrderDetailDto> getAllType(Object o);

    void updateRecallOrder(Object o);
}
