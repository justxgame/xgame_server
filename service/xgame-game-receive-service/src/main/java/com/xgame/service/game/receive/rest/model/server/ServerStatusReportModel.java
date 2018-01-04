package com.xgame.service.game.receive.rest.model.server;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerStatusReportModel {
    @JsonProperty("Server_id")
    private Integer server_id;
    @JsonProperty("IP")
    private String ip;
    @JsonProperty("Port")
    private Integer port;
    @JsonProperty("Gm_port")
    private Integer gm_port;
    @JsonProperty("Status")
    private Boolean status;

    @JsonProperty("Version")
    private String version;

    @JsonProperty("PlatForm")
    private String platform;

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ServerStatusReportModel{" +
                "server_id=" + server_id +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", gm_port=" + gm_port +
                ", status=" + status +
                ", version='" + version + '\'' +
                ", platform='" + platform + '\'' +
                '}';
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
