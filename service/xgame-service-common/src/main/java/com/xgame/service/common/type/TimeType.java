package com.xgame.service.common.type;

import java.util.HashMap;
import java.util.Map;

public enum TimeType {
    HOUR("H"),
    MINUTE("m"),
    SECOND("s");

    private String name;//定义自定义的变量

    TimeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    private static final Map<String, TimeType> stringToEnum = new HashMap<>();
    static {

        for(TimeType blah : values()) {
            stringToEnum.put(blah.toString(), blah);
        }
    }


    public static TimeType fromString(String symbol) {
        return stringToEnum.get(symbol);
    }

    @Override
    public String toString() {
        return name;
    }
}
