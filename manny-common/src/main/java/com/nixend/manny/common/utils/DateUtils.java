package com.nixend.manny.common.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @author panyox
 */
public class DateUtils {

    public static Date afterHours(int hour) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, hour);
        return cal.getTime();
    }
}
