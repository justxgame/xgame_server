package com.xgame.service.common.util;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {
    private final static String dsPatterns = "yyyy-MM-dd HH:mm:ss";
    private final static String PHONE_DATEFORMAt = "yyyyMMdd";
    private final static FastDateFormat dsDateFormat = FastDateFormat.getInstance(dsPatterns);
    private final static FastDateFormat norMalDateFormat = FastDateFormat.getInstance(PHONE_DATEFORMAt);


    public static String hashingMD5(String str) {
        if (null == str) {
            return null;
        }
        return Hashing.md5().hashString(str, Charsets.UTF_8).toString().toUpperCase();
    }


    public static String hashingMD5(String... args) {
        String str = "";
        for (String arg : args) {
            str=str+arg;
        }
        return Hashing.md5().hashString(str, Charsets.UTF_8).toString().toUpperCase();
    }
    public static String hashingMD5Lower(String... args) {
        String str = "";
        for (String arg : args) {
            str=str+arg;
        }
        return Hashing.md5().hashString(str, Charsets.UTF_8).toString();
    }


    public static String getDsFromUnixTimestamp(long unixTimestamp) {
        return dsDateFormat.format(unixTimestamp);
    }

    public static String getFormatDateByNow() {
        return dsDateFormat.format(System.currentTimeMillis());
    }

    public static String getNormalDate(String datestr) throws ParseException {
        return norMalDateFormat.format(dsDateFormat.parse(datestr));
    }
    public static String getNormalDateByTime(){
        return norMalDateFormat.format(System.currentTimeMillis());
    }


    public static String getOrderId(int bizId) {

        String orderId = bizId + (System.currentTimeMillis() + "").substring(1) +
                (System.nanoTime() + "").substring(7, 10);

        return orderId;
    }

    public static long parseStr2Time(String date) {
        long time = 0;
        try {
            time = dsDateFormat.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static Integer parseBoolean2Int(boolean b) {
        if (true == b) {
            return 1;
        }
        return 0;
    }
    /**
     * unicode转中文
     * @param str
     * @return
     * @author yutao
     * @date 2017年1月24日上午10:33:25
     */
    public static String unicodeToString(String str) {

        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");

        Matcher matcher = pattern.matcher(str);

        char ch;

        while (matcher.find()) {

            ch = (char) Integer.parseInt(matcher.group(2), 16);

            str = str.replace(matcher.group(1), ch+"" );

        }

        return str;

    }
}
