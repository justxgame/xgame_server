package com.xgame.service.manager.rest.model.kpi;

import java.util.List;

/**
 * Created by william on 2017/9/11.
 */
public class KpiNavModel {

    private String navName;
    private List<KpiMetaModel> kpiMetaModelList;

    public List<KpiMetaModel> getKpiMetaModelList() {
        return kpiMetaModelList;
    }

    public void setKpiMetaModelList(List<KpiMetaModel> kpiMetaModelList) {
        this.kpiMetaModelList = kpiMetaModelList;
    }

    public String getNavName() {
        return navName;
    }

    public void setNavName(String navName) {
        this.navName = navName;
    }
}

