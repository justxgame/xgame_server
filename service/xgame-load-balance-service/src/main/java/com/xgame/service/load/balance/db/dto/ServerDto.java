package com.xgame.service.load.balance.db.dto;

public class ServerDto {
    private String server_ip;
    private Integer port;

    public String getServer_ip() {
        return server_ip;
    }

    public void setServer_ip(String server_ip) {
        this.server_ip = server_ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
