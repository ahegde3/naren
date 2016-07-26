package com.samsung.android.email.labs.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by nsbisht on 6/3/16.
 */
public class Ulitily {

    /**
     * This takes a date object and returns
     * a well formatted String.
     * If the date is todays, then it returns the time.
     * Otherwise this returns the date
     * @param date
     * @return
     */
    public static String getTime(Date date) {

        Calendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        Date todayDate = c.getTime();
        if(date.after(todayDate)) {
            // this is today, lets just return the time
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
            String str = sdf.format(date);
            return str;
        } else {
            // This is not today
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d");
            String str = sdf.format(date);
            return str;
        }
    }
}
