package com.cloud.hub.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public final static String time_pattern = "yyyy-MM-dd HH:mm:ss";
    public final static String PATTERN_YEAR = "yyyy";
    public final static String PATTERN_MONTH = "yyyy-MM";
    public final static String PATTERN_DATE = "yyyy-MM-dd";

    /**
     * 格式化日期
     */
    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    public static String formatDate(Date date) {
        return formatDate(date, PATTERN_DATE);
    }

    public static String getSimpleHisStr(Date date) {
        if (date == null) return "";
        String str = "";
        Date now = new Date();
        long dis = now.getTime() - date.getTime();
        if (dis < 0) {
            return "";
        }
        long minu = dis / 60000;

        if (minu < 0) return str;
        if (minu == 0) return "刚刚";

        if (minu < 60) {
            str = minu + "分钟前";
        } else {
            long hour = minu / 60;
            if (hour < 24) {
                str = hour + "小时前";
            } else {
                long day = hour / 24;
                if (day < 30) {
                    str = day + "天前";
                } else {
                    long month = day / 30;
                    if (month < 12) {
                        str = month + "月前";
                    } else {
                        long year = month / 12;
                        str = year + "年前";
                    }
                }
            }

        }
        return str;
    }

    public static Date stringToDate(String dateString) {
        try {
            String[] splitString = null;
            if (dateString.contains(".")) {
                splitString = StringUtils.split(dateString, ".");
            } else if (dateString.contains("-")) {
                splitString = StringUtils.split(dateString, "-");
            }else if (dateString.contains("/")) {
                splitString = StringUtils.split(dateString, "/");
            } else {
                splitString = new String[] { dateString };
            }

            Calendar cal = Calendar.getInstance();
            int year = 1;
            int month = 0;
            int date = 1;
            if (splitString.length > 0) {
                year = Integer.parseInt(splitString[0]);
            }
            if (splitString.length > 1) {
                String mString=splitString[1];
                month = Integer.parseInt(splitString[1]);
                if (month != 0) {
                    month--;
                }
            }
            if (splitString.length > 2) {
                date = Integer.parseInt(splitString[2]);
            }
            cal.set(year, month, date);
            return cal.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化日期
     *
     * @param dateString
     * @param simpleDateFormat
     * @return
     */
    public static Date getDate(String dateString, SimpleDateFormat simpleDateFormat) {

        Date date = null;

        try {
            if(!StringUtils.isEmpty(dateString)){
                date = simpleDateFormat.parse(dateString);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
