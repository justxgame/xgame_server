package com.xgame.order.consumer.business.pmi;

import com.xgame.order.consumer.business.ofpay.OrderInfo;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class Util {
    public static PmiOrderInfo getOrderInfo(String res) throws JAXBException, UnsupportedEncodingException {
        JAXBContext jaxbContext = JAXBContext.newInstance(OrderInfo.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        InputStream stream = new ByteArrayInputStream(res.getBytes(StandardCharsets.UTF_8.name()));
        return (PmiOrderInfo) jaxbUnmarshaller.unmarshal(stream);
    }
}
