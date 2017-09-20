package com.xgame.service.manager.db.dao;

import java.util.List;

public interface RewardOrderDetailDao<T> extends BaseDao{

    List<T> getAllByQuery(Object o);

}
