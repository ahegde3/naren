
package com.samsung.android.emailcommon.utility.calendar;

import java.util.ArrayList;


//
import com.samsung.android.emailcommon.mail.MeetingInfo;
import com.samsung.android.emailcommon.mail.PackedString;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.TimeFormatException;

public class RecurrenceUtilities {

    private ArrayList<RecurrenceInstance> instance = null;
    private static final long MINIMUM_EXPANSION_SPAN = 2L * 31 * 24 * 60 * 60 * 1000;
    long lastEnd = -1;
    public ArrayList<RecurrenceInstance> getRecurrenceInstances(long start, long end, String timezone, String meetingInfo) {
        PackedString pack =  new PackedString(meetingInfo);
        String AllDay = pack.get(MeetingInfo.MEETING_IS_ALLDAY);
        boolean allday = false;
        if(AllDay!= null && AllDay.equalsIgnoreCase("1"))
            allday = true;
        String rule = pack.get(MeetingInfo.MEETING_RRULE);
        lastEnd = start + MINIMUM_EXPANSION_SPAN;
        instance = new ArrayList<RecurrenceInstance>();
        performInstanceExpansion (start,end,timezone,allday,rule);
        return instance;
    }

    private void performInstanceExpansion(long dtstartMillis, long dtendMillis, String eventTimezone, boolean allDay,String rule) {
        RecurrenceProcessor rp = new RecurrenceProcessor();
        Duration duration = new Duration();
        Time eventTime = new Time();
        try {
            if (allDay || TextUtils.isEmpty(eventTimezone)) {
                eventTimezone = Time.TIMEZONE_UTC;
            }
            String durationStr = null;
            String rruleStr = rule;
            String rdateStr = null;//entries.getString(rdateColumn);
            String exruleStr = null;//entries.getString(exruleColumn);
            String exdateStr = null;//entries.getString(exdateColumn);
            RecurrenceSet recur = null;
            try {
                recur = new RecurrenceSet(rruleStr, rdateStr, exruleStr, exdateStr);
            } catch (EventRecurrence.InvalidFormatException e) {
                return;
            }
            if (null != recur && recur.hasRecurrence()) {
                eventTime.timezone = eventTimezone;
                eventTime.set(dtstartMillis);
                eventTime.allDay = allDay;
                if (durationStr == null) {
                    if (allDay) {
                        // set to one day.
                        duration.sign = 1;
                        duration.weeks = 0;
                        duration.days = 1;
                        duration.hours = 0;
                        duration.minutes = 0;
                        duration.seconds = 0;
                        durationStr = "+P1D";
                    } else {
                        // compute the duration from dtend, if we can.
                        // otherwise, use 0s.
                        duration.sign = 1;
                        duration.weeks = 0;
                        duration.days = 0;
                        duration.hours = 0;
                        duration.minutes = 0;
                        if (!(dtendMillis == -1)) {
                            duration.seconds = (int) ((dtendMillis - dtstartMillis) / 1000);
                            durationStr = "+P" + duration.seconds + "S";
                        } else {
                            duration.seconds = 0;
                            durationStr = "+P0S";
                        }
                    }
                }
                long[] dates = null;
                try {
                    dates = rp.expand(eventTime, recur, dtstartMillis, lastEnd);
                } catch (DateException e) {
                    e.printStackTrace();
                }
                if (dates != null) {
                    long durationMillis = duration.getMillis();
                    for (long date : dates) {
                        instance.add(new RecurrenceInstance(date, date + durationMillis));
                    }
                }
            }
        }catch (TimeFormatException e) {
        }
    }
}