import com.xgame.order.consumer.biz.BaseBiz;
import com.xgame.service.common.util.CommonUtil;

import java.text.ParseException;

public class Test {
    public static void main(String[] args) throws ParseException {
        String str = "2017-09-10 16:05:43";
        System.out.println(CommonUtil.getNormalDate(str));

    }

}
