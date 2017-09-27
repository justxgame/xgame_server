package com.xgame.order.consumer.business.pmi;

import com.xgame.order.consumer.business.BaseBusiness;
import com.xgame.order.consumer.business.ofpay.OrderInfo;
import org.apache.http.NameValuePair;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public abstract class AbstractPmiBusiness extends BaseBusiness {
    /**
     * 返回字订单号 ,  generateSubOrder_0
     *
     * @param orderId
     * @param subNum
     * @return
     */
    protected String generateSubOrder(String orderId, String subNum) {
        return orderId + "_" + subNum;
    }

    protected String getParameterStr(List<NameValuePair> params) {
        if (null == params) {
            return "";
        }
        String paramsStr = "";
        for (NameValuePair pair : params) {
            if (null != pair) {
                paramsStr = paramsStr +","+ pair.toString();
            }
        }
        return paramsStr;
    }
    public static PmiOrderInfo getOrderInfo(String res) throws JAXBException, UnsupportedEncodingException {
        JAXBContext jaxbContext = JAXBContext.newInstance(PmiOrderInfo.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        InputStream stream = new ByteArrayInputStream(res.getBytes(StandardCharsets.UTF_8.name()));
        return (PmiOrderInfo) jaxbUnmarshaller.unmarshal(stream);
    }
}
