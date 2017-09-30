package com.xgame.service.manager.service;

import com.xgame.service.manager.db.dao.BroadcastDao;
import com.xgame.service.manager.db.dao.KpiDao;
import com.xgame.service.manager.db.dto.BroadCastDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KpiService {
    @Autowired
    KpiDao  kpiDao;

    public KpiDao getKpiDao() {
        return kpiDao;
    }
}
