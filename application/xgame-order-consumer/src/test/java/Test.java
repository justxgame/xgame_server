import com.xgame.order.consumer.biz.BaseBiz;
import com.xgame.service.common.conf.ConfigHolder;
import com.xgame.service.common.conf.OrderInfo;
import com.xgame.service.common.util.CommonUtil;

import javax.xml.bind.JAXBException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

public class Test {
    public static void main(String[] args) throws ParseException, JAXBException, UnsupportedEncodingException {
        String str = "2017-09-10 16:05:43";
        System.out.println(CommonUtil.getNormalDate(str));

//        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><orderinfo><msg>充值中</msg><reqcode>0</reqcode></orderinfo>";
//        OrderInfo orderInfo= ConfigHolder.getPhoneChargeXml(xml);
//        System.out.println(orderInfo.getReqcode());
//
//        String recalXml ="<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
//                "<orderinfo>\n" +
//                "<msg>充值中</msg>\n" +
//                "<reqcode>0</reqcode>\n" +
//                "<retcode>0</retcode >\n" +
//                "<orderId> SP交易流水号</orderId> \n" +
//                "</orderinfo>";
//        OrderInfo orderInfo2= ConfigHolder.getPhoneChargeXml(recalXml);
//        System.out.println(orderInfo2.getReqcode());
//        System.out.println(orderInfo2.getRetcode());
//        System.out.println(orderInfo2.getOrderId());
//        System.out.println(orderInfo2.getMsg());
    }


}
