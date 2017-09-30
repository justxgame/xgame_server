package com.xgame.service.manager.db.dao;

import com.xgame.service.manager.rest.model.kpi.*;

import java.util.List;

/**
 * Created by william on 2017/9/30.
 */
public interface KpiDao extends BaseDao {

    List<ActiveModel> getActive();

    List<NewActiveModel> getNewActive();

    List<PayModel> getPay();

    List<PayModelNumber> getPayNumber();

    List<RetentionModel> getRetention();

}



