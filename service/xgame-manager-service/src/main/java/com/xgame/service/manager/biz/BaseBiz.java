package com.xgame.service.manager.biz;

import com.alibaba.fastjson.JSONObject;
import com.xgame.service.common.type.TimeType;
import com.xgame.service.manager.ServiceContextFactory;
import com.xgame.service.manager.db.dao.BroadcastDao;
import com.xgame.service.manager.db.dao.ServerStatusDao;
import com.xgame.service.manager.db.dto.BroadCastRegularDto;
import com.xgame.service.manager.db.dto.ServerStatusDto;
import com.xgame.service.manager.rest.model.broadcast.BroadCastSendModel;
import com.xgame.service.manager.rest.model.response.ServerRes;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public abstract class BaseBiz {
    private static Logger logger = LoggerFactory.getLogger(BaseBiz.class.getName());
    protected BroadcastDao broadcastDao = ServiceContextFactory.getBroadCastDao();
    protected ServerStatusDao serverStatusDao = ServiceContextFactory.getServerStatusDao();
    protected static final String HTTP_PREFIX="http://";
    protected static CloseableHttpClient httpclient;
    {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(5 * 1000)
                .setConnectionRequestTimeout(5 * 1000)
                .setSocketTimeout(5 * 1000).build();
        httpclient =
                HttpClientBuilder.create().setDefaultRequestConfig(config).build();

    }

    abstract void broadCastTaskProcess(BroadCastRegularDto dto);
    /**
     * 实际发送
     * @param dto
     * @param message
     * @throws IOException
     */
    public void sendBroadcast(ServerStatusDto dto, String message) throws IOException {

        String sendUrl = HTTP_PREFIX+dto.getUrl()+"/broadcast";
        HttpPost post = new HttpPost(sendUrl);
        BroadCastSendModel sendModel = new BroadCastSendModel();
        sendModel.setServer_id(dto.getServer_id());

        sendModel.setMsg(message);
        String jsonStr = JSONObject.toJSONString(sendModel);
        StringEntity reqEntity = new StringEntity(jsonStr, Charset.forName("UTF-8"));
        reqEntity.setContentEncoding("UTF-8");
        reqEntity.setContentType("application/json");

        post.setEntity(reqEntity);
        CloseableHttpResponse response =null;
        HttpEntity entity=null;
        try {
            response = httpclient.execute(post);

            entity = response.getEntity();
            String res = EntityUtils.toString(entity, "UTF-8");
            ServerRes serverRes = JSONObject.parseObject(res,ServerRes.class);
            if (serverRes.getCode()!=0){

                logger.error("[BroadcastResources]send broadcast error by game server get error");
            }


        }finally {
            try {
                if (response!=null){
                    response.close();
                }
                EntityUtils.consume(entity);

            } catch (IOException e) {
                logger.error("[BroadcastResources] send broad cast error");
            }
        }
    }
    protected ServerStatusDto getDtoById(String serverId, List<ServerStatusDto> dtos){
        for(ServerStatusDto dto:dtos){
            if (dto.getServer_id().equals(Integer.valueOf(serverId))){
                return dto;
            }
        }
        return null;
    }

    /**
     * 计算 task 下次开始时间 毫秒级别
     * @param freq_unit
     * @param freq_val
     * @param time
     * @return
     */
    protected Long calculateNextDate(String freq_unit,Integer freq_val,Long time){
        TimeType timeType = TimeType.fromString(freq_unit);
        switch (timeType){
            case HOUR: return 60*60*1000*freq_val+time;
            case MINUTE:return 60*1000*freq_val+time;
            case SECOND:return 1*1000*freq_val+time;
            default:return time;
        }
    }
}
