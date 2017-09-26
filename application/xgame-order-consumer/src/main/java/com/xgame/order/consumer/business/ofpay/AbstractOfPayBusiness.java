package com.xgame.order.consumer.business.ofpay;


import com.xgame.order.consumer.business.BaseBusiness;
import com.xgame.order.consumer.conf.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * Created by william on 2017/9/26.
 */
public abstract class AbstractOfPayBusiness extends BaseBusiness {

    private final static String ofdate = "yyyyMMddHHmmss";
    private final static FastDateFormat ofDateFormat = FastDateFormat.getInstance(ofdate);

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

}
