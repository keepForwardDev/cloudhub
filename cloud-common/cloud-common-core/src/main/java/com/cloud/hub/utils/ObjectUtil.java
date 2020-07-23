package com.cloud.hub.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @Author: jaxMine
 * @Date: 2019/12/31 16:53
 */
public class ObjectUtil {


    public static Long toLon(Object obj) {
        if (obj == null) return 0l;
        if (obj == null) return null;
        if (obj instanceof Long) {
            return (Long) obj;
        } else if (obj instanceof BigInteger) {
            return ((BigInteger) obj).longValue();
        } else if (obj instanceof String) {
            return Long.valueOf(obj.toString());
        } else if (obj instanceof BigDecimal) {
            return Long.valueOf(obj.toString());
        }
        return 0l;
    }

    public static Float toFlo(Object obj) {
        if (obj == null) return 0f;
        if (obj instanceof Float) {
            return (Float) obj;
        } else if (obj instanceof BigInteger) {
            return ((BigInteger) obj).floatValue();
        }
        return 0f;
    }

    public static Double toDou(Object obj) {
        if (obj == null) return 0d;
        if (obj instanceof Double) {
            return (Double) obj;
        } else if (obj instanceof BigInteger) {
            return ((BigInteger) obj).doubleValue();
        } else if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).doubleValue();
        }
        return 0d;
    }

    public static String toStr(Object obj) {
        if (obj == null) return "";
        if (obj instanceof String) {
            return (String) obj;
        }
        return "";
    }

    public static Integer toInt(Object obj) {
        if (obj == null) return 0;
        if (obj instanceof Integer) {
            return (Integer) obj;
        } else if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).intValue();
        } else if (obj instanceof BigInteger) {
            return ((BigInteger) obj).intValue();
        }
        return 0;
    }

    public static Date toDate(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Date) {
            return (Date) obj;
        }
        return null;
    }


}
