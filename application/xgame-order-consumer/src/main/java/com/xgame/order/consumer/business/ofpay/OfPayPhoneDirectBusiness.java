package com.xgame.order.consumer.business.ofpay;

import com.xgame.order.consumer.conf.Configuration;
import com.xgame.order.consumer.db.dto.RewardOrderInfoDto;
import com.xgame.order.consumer.db.dto.RewardOrderLogMappingDto;
import com.xgame.service.common.type.OrderInfoType;
import com.xgame.service.common.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Created by william on 2017/9/26.
 */
public class OfPayPhoneDirectBusiness extends AbstractOfPayBusiness {

    private static Logger logger = LoggerFactory.getLogger(OfPayPhoneDirectBusiness.class.getName());

    private final String request_url = ofpay_base_url + "/onlineorder.do";
    private final String callback_url = Configuration.getInstance().getConfig().getString("ofpay.phone.direct.recall.url");

    @Override
    public void processorOrder(RewardOrderLogMappingDto rewardOrderLogMappingDto) throws Throwable {
        requireNonNull(rewardOrderLogMappingDto.getOrder_id(), "order id is null");
        String orderId = rewardOrderLogMappingDto.getOrder_id();
        logger.info("[OfPayPhoneDirectBusiness] start to process order , orderid=" + orderId);

        // 保存初始状态
        requireNonNull(rewardOrderLogMappingDto.getItem_count(), "item count is null");
        RewardOrderInfoDto rewardOrderInfoDto = parsOrderLog2OrderInfo(rewardOrderLogMappingDto);
        rewardOrderInfoDto.setOrder_status(OrderInfoType.INIT.getValue());
        rewardOrderInfoDto.setIndate(new Date());
        rewardOrderInfoDao.saveObject(rewardOrderInfoDto);

        //更新 order info表 to consumer
        rewardOrderLogMappingDao.updateOrderToConsumer(rewardOrderLogMappingDto.getOrder_id());
        String exceptionMessage = "";
        String message = "";
        String res = "";
        // 循环 count
        for (int i = 0; i < rewardOrderLogMappingDto.getItem_count(); i++) {
            try {
                // 订单面额
                Integer cardnum = requireNonNull(rewardOrderLogMappingDto.getPrice());
                // 生成子订单
                String sporder_id = generateSubOrder(rewardOrderLogMappingDto.getOrder_id(), String.valueOf(i));
                String cardid = "140101";
                String sporder_time = getOFDateByNow();
                String game_userid = requireNonNull(rewardOrderLogMappingDto.getPhone());
                // 签名串  包体=userid+userpws+cardid+cardnum+sporder_id+sporder_time+ game_userid
                String md5_str = CommonUtil.hashingMD5(userid, userpws, cardid, String.valueOf(cardnum), sporder_id, sporder_time, game_userid, keyStr);
                String ret_url = callback_url;

                // 调用接口
                HttpPost httpPost = new HttpPost(request_url);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("userpws", userpws));
                params.add(new BasicNameValuePair("cardid", cardid)); //快冲
                params.add(new BasicNameValuePair("cardnum", String.valueOf(cardnum)));
                params.add(new BasicNameValuePair("sporder_id", sporder_id));
                params.add(new BasicNameValuePair("sporder_time", sporder_time));
                params.add(new BasicNameValuePair("game_userid", game_userid));
                params.add(new BasicNameValuePair("md5_str", md5_str));
                params.add(new BasicNameValuePair("ret_url", ret_url));
                params.add(new BasicNameValuePair("version", version));
                httpPost.setEntity(new UrlEncodedFormEntity(params));
                CloseableHttpResponse response = httpclient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                res = EntityUtils.toString(entity);
                message = res;
                logger.info("[OfPayPhoneDirectBusiness] res=" + res);

                // 解析返回值
                OrderInfo orderInfo = getOrderInfo(res);
                requireNonNull(orderInfo, "orderInfo is empty");
                if (isRechagering(requireNonNull(orderInfo.getGame_state()))) {
                    message = message + String.format("第 %s 充值中 game_status=%s", i, orderInfo.getGame_state());
                } else {
                    exceptionMessage = exceptionMessage + String.format("第 %s 充值失败 , return = %s", i, res);
                }
            } catch (Throwable t) {
                exceptionMessage = exceptionMessage + ExceptionUtils.getMessage(t);
            }
        }

        // save to db
        if(StringUtils.isEmpty(exceptionMessage)){
            rewardOrderInfoDto.setOrder_status(OrderInfoType.CHARGING.getValue());
        }else{
            rewardOrderInfoDto.setOrder_status(OrderInfoType.FAILURE.getValue());
        }
        rewardOrderInfoDto.setMessage(message);
        rewardOrderInfoDto.setOrder_exception(exceptionMessage);
        rewardOrderInfoDao.updateObjectById(rewardOrderInfoDto);
        logger.info("[OfPayPhoneDirectBusiness] processor order over  , orderid=" + orderId);
    }

    /**
     * 接口调用是否充值中
     *
     * @param game_state
     * @return
     */
    protected boolean isRechagering(String game_state) {

        if (StringUtils.isEmpty(game_state)) {
            return false;
        }

        if (game_state.equals("1") || game_state.equals("0")) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws IOException, JAXBException {
//        HttpPost httpPost = new HttpPost("http://apitest.ofpay.com/onlineorder.do");
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("userid", "A08566"));
//        params.add(new BasicNameValuePair("userpws", "4c625b7861a92c7971cd2029c2fd3c4a"));
//        params.add(new BasicNameValuePair("cardid", "140101"));
//        params.add(new BasicNameValuePair("cardnum", "50"));
//        params.add(new BasicNameValuePair("sporder_id", "test001234567"));
//        params.add(new BasicNameValuePair("sporder_time", "20160817140214"));
//        params.add(new BasicNameValuePair("game_userid", "15996271050"));
//        params.add(new BasicNameValuePair("md5_str", "B662CA03452882C761BC75585BA2483F"));
//        params.add(new BasicNameValuePair("version", "6.0"));
//        httpPost.setEntity(new UrlEncodedFormEntity(params));
//
//        CloseableHttpResponse response = httpclient.execute(httpPost);
//        HttpEntity entity = response.getEntity();
//        String res = EntityUtils.toString(entity);
//        System.out.println(res);
//        OrderInfo orderInfo = getOrderInfo(res);
//        System.out.println(response);

        //  String x = CommonUtil.hashingMD5("A08566", "4c625b7861a92c7971cd2029c2fd3c4a", "140101", String.valueOf(50),"test001234567", "20160817140214","15996271050","OFCARD");
        // System.out.println(x);
    }

}
