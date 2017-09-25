package com.xgame.order.consumer.biz;

import com.alibaba.fastjson.JSONObject;
import com.xgame.order.consumer.db.dao.RewardOrderInfoDao;
import com.xgame.order.consumer.db.dao.RewardOrderLogDao;
import com.xgame.order.consumer.db.dto.RewardBoxDto;
import com.xgame.order.consumer.db.dto.RewardOrderInfoDto;
import com.xgame.order.consumer.db.dto.RewardOrderLogDto;
import com.xgame.order.consumer.rest.model.ExchangeResultModel;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class BaseBiz {
    protected static CloseableHttpClient httpclient;
    {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(20 * 1000)
                .setConnectionRequestTimeout(20 * 1000)
                .setSocketTimeout(20 * 1000).build();
        httpclient =
                HttpClientBuilder.create().setDefaultRequestConfig(config).build();

    }

    protected String getRewardInfo(int itemId, List<RewardBoxDto> rewardBoxDtos){
        for (RewardBoxDto dto:rewardBoxDtos){
            if (itemId==dto.getId()){
                return dto.getMemo();
            }
        }
        return null;
    }

    /**
     * 获取 10元话费的 10
     * @param rewardInfo
     * @return
     */
    protected String getChargeFee(String rewardInfo){
        int position = rewardInfo.lastIndexOf("0");
        return rewardInfo.substring(0, position+1);
    }

    protected RewardOrderInfoDto parsOrderLog2OrderInfo(RewardOrderLogDto dto,int status,String exception){
        RewardOrderInfoDto orderInfoDto = new RewardOrderInfoDto();
        orderInfoDto.setServerId(dto.getServer_id());
        orderInfoDto.setUid(dto.getUid());
        orderInfoDto.setIsReorder(dto.getIs_reorder());
        orderInfoDto.setItemCount(dto.getItem_count());
        orderInfoDto.setItemId(dto.getItem_id());
        orderInfoDto.setItemType(dto.getItem_type());
        orderInfoDto.setIndate(dto.getIndate());
        orderInfoDto.setOrderId(dto.getOrder_id());
        orderInfoDto.setOrderStatus(status);
        orderInfoDto.setReqId(dto.getOrder_id());
        orderInfoDto.setOrderException(exception);
        orderInfoDto.setId(Integer.valueOf(dto.getId()));
        orderInfoDto.setPhone(dto.getPhone());
        return orderInfoDto;
    }
    protected int postResultCode(CloseableHttpClient httpclient, String url, ExchangeResultModel model){
        HttpPost post = new HttpPost(url);
        String jsonStr = JSONObject.toJSONString(model);
        StringEntity reqEntity = new StringEntity(jsonStr, Charset.forName("UTF-8"));
        reqEntity.setContentEncoding("UTF-8");
        reqEntity.setContentType("application/json");

        post.setEntity(reqEntity);
        CloseableHttpResponse response =null;
        HttpEntity entity=null;
        int code =0;
        try {
            response = httpclient.execute(post);
            entity= response.getEntity();
            String res = EntityUtils.toString(entity, "UTF-8");
            System.out.println(res);
        } catch (IOException e) {
            System.out.println("post game server error"+e.getMessage());
            code=-1;
        }finally {
            try {

                response.close();
                EntityUtils.consume(entity);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return code;
    }


}
