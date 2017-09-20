package com.xgame.service.game.receive.rest.model.server;

import com.alibaba.fastjson.annotation.JSONField;

public class ServerStatusReportModel {
    @JSONField(name = "Server_id")
    private Integer server_id;
    @JSONField(name = "IP")
    private String ip;
    @JSONField(name = "Port")
    private Integer port;
    @JSONField(name = "Gm_port")
    private Integer gm_port;
    @JSONField(name = "Status")
    private Boolean status;

    public Integer getServer_id() {
        return server_id;
    }

    public void setServer_id(Integer server_id) {
        this.server_id = server_id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getGm_port() {
        return gm_port;
    }

    public void setGm_port(Integer gm_port) {
        this.gm_port = gm_port;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString(){
        return "ServerStatusReportModel[server_id=" + server_id + " ip=" + ip + " port=" + port
                + " gm_port=" + gm_port + "status="+status+"]";
    }
}
