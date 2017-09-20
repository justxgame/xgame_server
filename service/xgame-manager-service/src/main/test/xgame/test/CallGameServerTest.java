package xgame.test;

import com.alibaba.fastjson.JSONObject;
import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.manager.rest.model.server.ServerUpdateModel;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;

public class CallGameServerTest {
    @Test
    public void testCallServer(){
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(5 * 1000)
                .setConnectionRequestTimeout(5 * 1000)
                .setSocketTimeout(5 * 1000).build();
       CloseableHttpClient httpclient =
                HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        String sendUrl = "http://106.75.30.116:10159";
        int actionId=5;
        WrapResponseModel responseModel = new WrapResponseModel();
        ServerUpdateModel updateModel = new ServerUpdateModel();
        updateModel.setService("1000");
        String jsonStr = JSONObject.toJSONString(updateModel);
        HttpPost post =null;
        if (4==actionId){

            sendUrl = sendUrl+"/server_start";
            post = new HttpPost(sendUrl);

        }else if(5==actionId){

            sendUrl = sendUrl+"/server_stop";
            post = new HttpPost(sendUrl);

        }else if(6==actionId){

            sendUrl = sendUrl+"/server_restart";
            post = new HttpPost(sendUrl);
        }
        StringEntity reqEntity = new StringEntity(jsonStr, Charset.forName("UTF-8"));
        reqEntity.setContentEncoding("UTF-8");
        reqEntity.setContentType("application/json");

        post.setEntity(reqEntity);
        CloseableHttpResponse response =null;
        try {
            response = httpclient.execute(post);
            HttpEntity entity = response.getEntity();
            String res = EntityUtils.toString(entity, "UTF-8");


        }catch (Throwable t){


            responseModel.setMessage(ExceptionUtils.getStackTrace(t));
        }finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
