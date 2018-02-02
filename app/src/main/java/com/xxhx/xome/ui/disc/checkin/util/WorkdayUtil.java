package com.xxhx.xome.ui.disc.checkin.util;

import com.xxhx.xome.ui.disc.checkin.data.DayType;
import java.util.Calendar;

/**
 * Created by xxhx on 2017/8/2.
 */

public class WorkdayUtil {

    public static boolean isWorkday(long timeInMillis) {
        DayType dayType = getDayType(timeInMillis);
        if (dayType == DayType.Workday) {
            return true;
        } else if (dayType == DayType.Normalday) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timeInMillis);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) return true;
        }
        return false;
    }

    public static DayType getDayType(long timeInMillis) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        switch (year) {
            case 2017:
                return getDayTypeOf2017(month, dayOfMonth);
            case 2018:
                return getDayTypeOf2018(month, dayOfMonth);
        }

        return DayType.Normalday;
    }

    private static DayType getDayTypeOf2018(int month, int dayOfMonth) {
        switch (month) {
            case Calendar.JANUARY:
                if(dayOfMonth == 1) {
                    return DayType.Freeday;
                }
                break;
            case Calendar.FEBRUARY:
                if(dayOfMonth == 11 || dayOfMonth == 24) {
                    return DayType.Workday;
                }
                else if(dayOfMonth >= 15 && dayOfMonth <= 21) {
                    return DayType.Freeday;
                }
                break;
            case Calendar.APRIL:
                if(dayOfMonth == 8 || dayOfMonth ==28) {
                    return DayType.Workday;
                }
                else if((dayOfMonth >= 5 && dayOfMonth <= 7) || dayOfMonth == 29 || dayOfMonth == 30) {
                    return DayType.Freeday;
                }
                break;
            case Calendar.MAY:
                if(dayOfMonth == 1) {
                    return DayType.Freeday;
                }
                break;
            case Calendar.JUNE:
                if(dayOfMonth >= 16 && dayOfMonth <= 18) {
                    return DayType.Freeday;
                }
                break;
            case Calendar.SEPTEMBER:
                if(dayOfMonth == 29 || dayOfMonth == 30) {
                    return DayType.Workday;
                }
                else if (dayOfMonth >= 22 && dayOfMonth <= 24) {
                    return DayType.Freeday;
                }
                break;
            case Calendar.OCTOBER:
                if(dayOfMonth >= 1 && dayOfMonth <= 7) {
                    return DayType.Freeday;
                }
                break;
        }
        return DayType.Normalday;
    }

    private static DayType getDayTypeOf2017(int month, int dayOfMonth) {
        switch (month) {
            case Calendar.JANUARY:
                if(dayOfMonth == 22) {
                    return DayType.Workday;
                }
                else if(dayOfMonth == 1 || dayOfMonth == 2 || (dayOfMonth >= 27 && dayOfMonth <= 31)) {
                    return DayType.Freeday;
                }
                break;
            case Calendar.FEBRUARY:
                if(dayOfMonth == 4) {
                    return DayType.Workday;
                }
                else if(dayOfMonth == 1 || dayOfMonth == 2) {
                    return DayType.Freeday;
                }
                break;
            case Calendar.APRIL:
                if(dayOfMonth == 1) {
                    return DayType.Workday;
                }
                else if((dayOfMonth >= 2 && dayOfMonth <= 4) || dayOfMonth == 29 || dayOfMonth == 30) {
                    return DayType.Freeday;
                }
                break;
            case Calendar.MAY:
                if(dayOfMonth == 27) {
                    return DayType.Workday;
                }
                else if(dayOfMonth == 1 || (dayOfMonth >= 28 && dayOfMonth <= 30)) {
                    return DayType.Freeday;
                }
                break;
            case Calendar.SEPTEMBER:
                if(dayOfMonth == 30) {
                    return DayType.Workday;
                }
                break;
            case Calendar.OCTOBER:
                if(dayOfMonth >= 1 && dayOfMonth <= 8) {
                    return DayType.Freeday;
                }
                break;
        }
        return DayType.Normalday;
    }
}
