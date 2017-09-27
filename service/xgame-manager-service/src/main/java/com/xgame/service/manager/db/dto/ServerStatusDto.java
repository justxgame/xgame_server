package com.xgame.service.manager.db.dto;



public class ServerStatusDto {

    private Integer server_id;

    private String ip;

    private Integer port;

    private Integer gm_port;

    private Integer status;

    private String url;
    private String indate;
    private String server_name;


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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIndate() {
        return indate;
    }

    public void setIndate(String indate) {
        this.indate = indate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getServer_name() {
        return server_name;
    }

    public void setServer_name(String server_name) {
        this.server_name = server_name;
    }


    @Override
    public String toString(){
        return "ServerStatusDto{" +
                "server_id='" + server_id + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", gm_port=" + gm_port +
                ", status=" + status +
                ", url=" + url +
                ", server_name=" + server_name +

                '}';
    }
}
