import com.xgame.service.common.conf.ConfigHolder;

import com.xgame.service.common.type.PhoneType;

import javax.xml.bind.JAXBException;
import java.io.UnsupportedEncodingException;

public class Test {
    public static void main(String[] args) throws JAXBException, UnsupportedEncodingException {
//        String str = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
//                "\t<orderinfo>\n" +
//                "  <err_msg /> \n" +
//                "<retcode>1</retcode >\n" +
//                "  <orderid>S0703280004</orderid >\t\t\t\n" +
//                "  <cardid>360101</cardid>\t\t\t\t\t\t\n" +
//                "  <cardnum>1</cardnum>\t\t\t\t\t\t\n" +
//                "  <ordercash>1</ordercash>\t\t\t\t\t\t\n" +
//                "  <cardname>欧飞1分钱支付体验卡</cardname>\t\n" +
//                "  <sporder_id>123</sporder_id> \t\t\t\t\n" +
//                "\t<cards>\n" +
//                "\t<card>\n" +
//                "  <cardno>you</cardno> \t\t\t\t\t\n" +
//                "  <cardpws>success</cardpws>\t\t\t\n" +
//                "  <expiretime>4000-12-31</expiretime>\t \n" +
//                "  </card>\n" +
//                "\t<card>\n" +
//                "  <cardno>ele</cardno> \t\t\t\t\t\n" +
//                "  <cardpws>success</cardpws>\t\t\t\n" +
//                "  <expiretime>4000-12-31</expiretime>\t \n" +
//                "  </card>\n" +
//                "  </cards>\n" +
//                "  </orderinfo>\n";
//
//        OrderInfo orderInfo = ConfigHolder.getOrderInfo(str);
//        System.out.println(orderInfo.getRetcode());
//        System.out.println(orderInfo.getCards().getCard().get(0).getCardno());
//        System.out.println(orderInfo.getCards().getCard().get(1).getCardno());

        String str = "移动";
        PhoneType phoneType = PhoneType.fromString(str);
        System.out.println(phoneType);
    }
}
