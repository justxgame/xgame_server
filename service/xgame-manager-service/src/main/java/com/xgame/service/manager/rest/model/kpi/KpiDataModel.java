package com.xgame.service.manager.rest.model.kpi;

import java.util.List;

/**
 * Created by william on 2017/9/11.
 */
public class KpiDataModel {
    private List<String> columns;
    private List<Object> data;

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }
}
