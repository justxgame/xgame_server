package com.xgame.service.manager.rest.model.kpi;

/**
 * Created by william on 2017/9/11.
 */
public class KpiMetaModel {
    private Integer kpiId;
    private String kpiName;

    public Integer getKpiId() {
        return kpiId;
    }

    public void setKpiId(Integer kpiId) {
        this.kpiId = kpiId;
    }

    public String getKpiName() {
        return kpiName;
    }

    public void setKpiName(String kpiName) {
        this.kpiName = kpiName;
    }

    public KpiMetaModel() {
    }

    public KpiMetaModel(Integer kpiId, String kpiName) {
        this.kpiId = kpiId;
        this.kpiName = kpiName;
    }
}
