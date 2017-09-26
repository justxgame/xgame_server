package com.xgame.order.consumer.business.ofpay;


import com.alibaba.fastjson.JSONObject;
import com.xgame.order.consumer.business.BaseBusiness;
import com.xgame.order.consumer.conf.Configuration;
import com.xgame.order.consumer.rest.model.ExchangeResultModel;
import com.xgame.service.common.type.PhoneType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Created by william on 2017/9/26.
 */
public abstract class AbstractOfPayBusiness extends BaseBusiness {
    private final static String ofdate = "yyyyMMddHHmmss";
    private final static FastDateFormat ofDateFormat = FastDateFormat.getInstance(ofdate);
    protected final static int SUCCESSCODE =1;
    protected final static int ERRORCODE=0;
    protected final static String HTTP_PREFIX= "http://";

    // base url
    protected final String ofpay_base_url = Configuration.getInstance().getConfig().getString("ofpay.base.url");

    // base property
    protected final String userid = Configuration.getInstance().getConfig().getString("ofpay.userid");
    protected final String userpws = Configuration.getInstance().getConfig().getString("ofpay.userpw");
    protected final String keyStr = Configuration.getInstance().getConfig().getString("ofpay.keystr");
    protected final String version = "6.0";


    protected static OrderInfo getOrderInfo(String res) throws JAXBException, UnsupportedEncodingException {
        JAXBContext jaxbContext = JAXBContext.newInstance(OrderInfo.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        InputStream stream = new ByteArrayInputStream(res.getBytes(StandardCharsets.UTF_8.name()));
        return (OrderInfo) jaxbUnmarshaller.unmarshal(stream);
    }

    /**
     * 返回字订单号 ,  generateSubOrder_0
     * @param orderId
     * @param subNum
     * @return
     */
    protected String generateSubOrder(String orderId, String subNum) {
        return orderId + "_" + subNum;
    }

    protected static String getOFDateByNow() {
        return ofDateFormat.format(System.currentTimeMillis());
    }

    public static void gameCallBack(String url, ExchangeResultModel model) throws IOException {

        HttpPost post = new HttpPost(url);
        String jsonStr = JSONObject.toJSONString(model);
        StringEntity reqEntity = new StringEntity(jsonStr, Charset.forName("UTF-8"));
        reqEntity.setContentEncoding("UTF-8");
        reqEntity.setContentType("application/json");

        post.setEntity(reqEntity);
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        try {
            response = httpclient.execute(post);
            entity = response.getEntity();
            String res = EntityUtils.toString(entity, "UTF-8");


        }  finally {

            response.close();
            EntityUtils.consume(entity);
        }

    }

    protected PhoneType getPhoneType(String phone,String url,CloseableHttpClient httpclient) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("mobilenum", phone));

        httpPost.setEntity(new UrlEncodedFormEntity(params));
        CloseableHttpResponse response = httpclient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String res = EntityUtils.toString(entity);
        requireNonNull(res, "get phone location error");
        String[] mobinfo = res.split("\\|");
        String location = mobinfo[2];
        return PhoneType.fromString(location);


    }

    protected String getCardIdByPhoneType(String cardId,PhoneType phoneType){
        String[] cardArr = cardId.split("\\|");
        //移动|联通|电信-- 移动||电信 --|联通|电信
        if (cardArr.length==3){
            switch (phoneType){
                case MOBILE:
                    return cardArr[0];
                case UNICOM:
                    return cardArr[1];
                case TELECOM:
                    return cardArr[2];
                default:return "";
            }
        }else {
            //移动|联通|  |联通| cardarr length=2
            switch (phoneType){
                case MOBILE:
                    return cardArr[0];
                case UNICOM:
                    return cardArr[1];
                case TELECOM:
                    return "";
                default:return "";
            }
        }

    }
}
