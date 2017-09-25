package com.xgame.service.common.util;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;

public class CommonUtil {
    private final static String dsPatterns = "yyyy-MM-dd HH:mm:ss";
    private final static String PHONE_DATEFORMAt = "yyyyMMdd";
    private final static String ofdate = "yyyyMMddHHmmss";
    private final static FastDateFormat dsDateFormat = FastDateFormat.getInstance(dsPatterns);
    private final static FastDateFormat norMalDateFormat = FastDateFormat.getInstance(PHONE_DATEFORMAt);
    private final static FastDateFormat ofDateFormat = FastDateFormat.getInstance(ofdate);




    public static String hashingMD5(String str) {
        if (null == str) {
            return null;
        }
        return Hashing.md5().hashString(str, Charsets.UTF_8).toString();
    }

    public static String getDsFromUnixTimestamp(long unixTimestamp) {
        return dsDateFormat.format(unixTimestamp);
    }
    public static String getFormatDateByNow(){
        return dsDateFormat.format(System.currentTimeMillis());
    }

    public static String getNormalDate(String datestr) throws ParseException {
        return norMalDateFormat.format(dsDateFormat.parse(datestr));
    }


    public static String getOrderId(int bizId) {

        String orderId = bizId+ (System.currentTimeMillis()+"").substring(1)+
                (System.nanoTime()+"").substring(7,10);

        return orderId;
    }
    public static long parseStr2Time(String date){
        long time =0;
        try {
            time= dsDateFormat.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static Integer parseBoolean2Int(boolean b){
        if (true==b){
            return 1;
        }
        return 0;
    }
    public static String getOFDateByNow(){
        return ofDateFormat.format(System.currentTimeMillis());
    }

}
