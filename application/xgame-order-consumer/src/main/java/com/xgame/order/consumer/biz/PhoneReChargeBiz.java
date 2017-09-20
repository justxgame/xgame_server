package com.xgame.order.consumer.biz;

import com.xgame.order.consumer.conf.Configuration;
import com.xgame.order.consumer.db.dao.RewardOrderLogDao;
import com.xgame.order.consumer.db.dao.RewardUserDao;
import com.xgame.order.consumer.db.dto.RewardBoxDto;
import com.xgame.order.consumer.db.dto.RewardOrderInfoDto;
import com.xgame.order.consumer.db.dto.RewardOrderLogDto;
import com.xgame.service.common.util.CommonUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.*;

public class PhoneReChargeBiz extends BaseBiz {
    private SqlSession sqlSession;
    private List<RewardBoxDto> rewardBoxDtos;
    private static Logger logger = LoggerFactory.getLogger(PhoneReChargeBiz.class.getName());
    public PhoneReChargeBiz(SqlSession sqlSession,List<RewardBoxDto> rewardBoxDtos){
        this.sqlSession = sqlSession;
        this.rewardBoxDtos= rewardBoxDtos;
    }
    public List<RewardOrderInfoDto> getProcessedResult(List<RewardOrderLogDto> rewardOrderLogDtos) throws URISyntaxException {
        if (rewardOrderLogDtos.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        List<RewardOrderInfoDto> result = new ArrayList<>();
        String url = Configuration.getInstance().getConfig().getString("phone.charge.url");
        String channelNo = Configuration.getInstance().getConfig().getString("phone.charge.channelNo");
        String userId = Configuration.getInstance().getConfig().getString("phone.charge.userId");
        String userPw = Configuration.getInstance().getConfig().getString("phone.charge.userPw");
        String recalUrl = Configuration.getInstance().getConfig().getString("phone.charge.recall.url");
        CloseableHttpClient httpclient = HttpClients.createDefault();
        RewardOrderLogDao rewardOrderLogDao = sqlSession.getMapper(RewardOrderLogDao.class);
        String md5Pw = CommonUtil.hashingMD5(userPw);
        RewardUserDao userDao = sqlSession.getMapper(RewardUserDao.class);
        for (RewardOrderLogDto dto:rewardOrderLogDtos){
            String rewardInfo = getRewardInfo(dto.getItem_id(), rewardBoxDtos);
            if (null==rewardInfo){
                logger.error("can't get item memo. wrong item id");
                continue;
            }
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("server_id", dto.getServer_id());
            queryMap.put("uid", dto.getUid());
            String phone = userDao.getObjectByID(queryMap);
            if (phone==null||phone.isEmpty()){
                logger.error("can't get phone by uid:"+dto.getUid()+" server_id"+dto.getServer_id());
                continue;
            }
            String feeValue = getChargeFee(rewardInfo);


            HttpGet request = new HttpGet(url);

            String isLarge = getIsLarge(feeValue);

            String version = "1.0";
            String date= null;
            try {
                date = CommonUtil.getNormalDate(dto.getIndate());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String orderId = dto.getOrder_id();

            List<NameValuePair> nameValuePairs = new ArrayList<>();
            NameValuePair channelPair = new BasicNameValuePair("channelNo", channelNo);
            NameValuePair phonePair = new BasicNameValuePair("phone", phone);
            NameValuePair feeValuePair = new BasicNameValuePair("tariffeValue", feeValue);
            NameValuePair isLargePair = new BasicNameValuePair("isLarge", isLarge);
            NameValuePair orderIdPair = new BasicNameValuePair("orderId", orderId);
            NameValuePair versionPair = new BasicNameValuePair("version", version);
            String key = getKey(channelNo,md5Pw,phone,feeValue,isLarge,orderId,date,version,userId);
            NameValuePair md5Pair = new BasicNameValuePair("md5Str", CommonUtil.hashingMD5(key));
            NameValuePair datePair = new BasicNameValuePair("txnDate", date);
            NameValuePair retUrlPair = new BasicNameValuePair("retUrl", recalUrl);
            nameValuePairs.add(channelPair);
            nameValuePairs.add(phonePair);
            nameValuePairs.add(feeValuePair);
            nameValuePairs.add(isLargePair);
            nameValuePairs.add(orderIdPair);
            nameValuePairs.add(versionPair);
            nameValuePairs.add(md5Pair);
            nameValuePairs.add(datePair);
            nameValuePairs.add(retUrlPair);
            URI uri = new URIBuilder(request.getURI()).addParameters(nameValuePairs).build();
            CloseableHttpResponse response = null;
            request.setURI(uri);
            try {
                response = httpclient.execute(request);
                HttpEntity entity = response.getEntity();
                System.out.println("phone call result:" +EntityUtils.toString(entity));
                EntityUtils.consume(entity);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            rewardOrderLogDao.updateObjectById(dto);
            RewardOrderInfoDto orderInfoDto = parsOrderLog2OrderInfo(dto, "", "");
            result.add(orderInfoDto);

        }

        return result;
    }

    private String getKey(String channel,String pwMd5,String phone,String feeValue,String isLarge,String orderId,String date,
                          String version,String userId){
        StringBuilder stringBuilder = new StringBuilder();
        String md5 = stringBuilder.append(channel).append(pwMd5).append(phone).append(feeValue).append(isLarge)
                .append(orderId).append(date).append(version).append(userId).toString();
        return md5;
    }


    private String getIsLarge(String chargeValue){
        if (Integer.valueOf(chargeValue)<100){
            return "1";
        }
        return "0";

    }



}
