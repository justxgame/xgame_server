package com.xgame.service.game.receive.util;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.UUID;

public class CommonUtils {
    private final static String dsPatterns = "yyyy-MM-dd HH:mm:ss";
    private final static FastDateFormat dsDateFormat = FastDateFormat.getInstance(dsPatterns);



    public static String hashingMD5(String str) {
        if (null == str) {
            return null;
        }
        return Hashing.md5().hashString(str, Charsets.UTF_8).toString();
    }

    public static String getDsFromUnixTimestamp(long unixTimestamp) {
        return dsDateFormat.format(unixTimestamp * 1000);
    }
    public static String getFormatDateByNow(){
        return dsDateFormat.format(System.currentTimeMillis());
    }


    public static String getOrderId(int bizId) {

        String orderId = bizId+ (System.currentTimeMillis()+"").substring(1)+
                (System.nanoTime()+"").substring(7,10);

        return orderId;
    }
}