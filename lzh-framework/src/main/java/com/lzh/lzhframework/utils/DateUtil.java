package com.lzh.lzhframework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static com.lzh.lzhframework.constants.SysConstants.DATE_FORMAT;
import static com.lzh.lzhframework.constants.SysConstants.DATE_TIME_FORMAT;

/**
 * @author luzhiheng
 * @date 2023-11-22
 */
public class DateUtil {

    public static String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        return sdf.format(date);
    }

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }

    public static Date parseDate(String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            return sdf.parse(datetime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date parseDateTime(String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        try {
            return sdf.parse(datetime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
