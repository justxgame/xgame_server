package com.xgame.service.common.type;


import java.util.HashMap;
import java.util.Map;

public enum PhoneType {

    MOBILE("移动"),  // 移动
    UNICOM("联通"), // 联通
    TELECOM("电信");// 电信


    private String name;//定义自定义的变量

     PhoneType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    private static final Map<String, PhoneType> stringToEnum = new HashMap<>();
    static {

        for(PhoneType blah : values()) {
            stringToEnum.put(blah.toString(), blah);
        }
    }


    public static PhoneType fromString(String symbol) {
        return stringToEnum.get(symbol);
    }

    @Override
    public String toString() {
        return name;
    }
}
