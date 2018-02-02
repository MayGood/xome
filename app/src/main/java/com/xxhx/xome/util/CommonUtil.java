package com.xxhx.xome.util;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by xxhx on 2017/4/12.
 */

public class CommonUtil {
    public static long getRelativeDays(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Calendar calendarToday = Calendar.getInstance();
        calendarToday.set(Calendar.HOUR_OF_DAY, 0);
        calendarToday.set(Calendar.MINUTE, 0);
        calendarToday.set(Calendar.SECOND, 0);
        calendarToday.set(Calendar.MILLISECOND, 0);
        long relativeTimeInSecs = (calendarToday.getTimeInMillis() / 1000) - (calendar.getTimeInMillis() / 1000);
        return relativeTimeInSecs / (60 * 60 * 24);
    }

    //TODO:逻辑确认
    public static long getRelativeDays(long time, long now) {
        int defaultOffset = TimeZone.getDefault().getRawOffset();
        long day = (time + defaultOffset) / (24 * 3600 * 1000);
        long today = (now + defaultOffset) / (24 * 3600 * 1000);
        return (day - today);
    }
}
