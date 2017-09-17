package test;

import com.xgame.service.game.receive.util.CommonUtils;
import org.junit.Test;

public class CommonTest {
    @Test
    public void testOrderId(){
        String id =CommonUtils.getOrderId(1);
        System.out.println(id);
        System.out.println(CommonUtils.getFormatDateByNow());
    }
}
