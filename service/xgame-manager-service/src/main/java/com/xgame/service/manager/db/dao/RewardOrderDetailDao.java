package com.xgame.service.manager.db.dao;

import java.util.List;

public interface RewardOrderDetailDao<T> extends BaseDao{

    List<T> getAllByQuery(Object o);

    List<T> getAllStatus(Object o);

    List<T> getAllType(Object o);

    void updateRecallOrder(Object o);
}
