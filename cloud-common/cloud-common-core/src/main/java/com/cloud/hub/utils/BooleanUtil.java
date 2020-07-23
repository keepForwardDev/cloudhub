package com.cloud.hub.utils;

public class BooleanUtil {


    public static final String YES_CN = "是";

    public static final String NO_CN = "否";

    public static boolean numberToBoolean(Number obj) {
        if (obj==null) {
            return false;
        }
        return obj.intValue()==1;
    }

    public static String booleanString(boolean flag) {
        return flag? YES_CN: NO_CN;
    }

    public static boolean stringToBoolean(String value) {
        if (YES_CN.equals(value)) {
            return true;
        } else if (Boolean.TRUE.toString().equals(value)) {
            return true;
        } else if ("1".equals(value)) {
            return true;
        }
        return false;
    }

}
