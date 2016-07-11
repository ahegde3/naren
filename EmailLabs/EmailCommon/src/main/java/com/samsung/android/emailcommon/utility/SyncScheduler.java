//change@wtl.pkijowski SyncSchedule begin

package com.samsung.android.emailcommon.utility;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class SyncScheduler {

    public static Pair<Boolean, Long> getIsPeakAndNextAlarm(SyncScheduleData syncScheduleData) {
        Calendar cal =  Calendar.getInstance();
        boolean isPeak = false;
        // current time
        Date date = new Date();
        long now = date.getTime();
        

        long nextAlarm = 0;
        int peakStartMinute = syncScheduleData.getStartMinute();
        int offPeakStartMinute = syncScheduleData.getEndMinute();
        // get peak time in milliseconds for today
        long peakTime = getMinuteInMillis(date, peakStartMinute);
        // get off peak time in milliseconds for today
        long offPeakTime = getMinuteInMillis(date, offPeakStartMinute);
        
        Date peakdate = new Date(peakTime);
        Date offpeakdate = new Date(offPeakTime);
        
        long millisToPeakTimeStart = peakTime - now;
        long millisToPeakTimeEnd = offPeakTime - now;
        long millisToNextAlarm = 0;
        boolean isAfterPeak = date.after(peakdate);
        boolean isBeforeOffPeak = date.before(offpeakdate);

        // get midnight in milliseconds
        // zero hour of tomorrow morning
        ArrayList<Integer> peakDaysArray = getPeakDays(syncScheduleData);

        cal.setTime(date);
        boolean isPeakDay = true;
        if (Arrays.binarySearch(peakDaysArray.toArray(),
                cal.get(java.util.Calendar.DAY_OF_WEEK)) < 0) {
            isPeakDay = false;
        }

        cal.add(java.util.Calendar.DATE, -1);
        boolean isYesterdayPeakDay = true;
        if (Arrays.binarySearch(peakDaysArray.toArray(),
                cal.get(java.util.Calendar.DAY_OF_WEEK)) < 0) {
            isYesterdayPeakDay = false;
        }

        // go through all remaining settings and determine isPeak and nextAlarm
        if (millisToPeakTimeStart == millisToPeakTimeEnd) {
            // Equal start and stop times could mean either never or always
            // peak. Assume always.
            if (isBeforeOffPeak) {
                // Before the transition point means we are in peak if yesterday
                // was a peak day.
                if (isYesterdayPeakDay) {
                    isPeak = true;
                }

                // The next transition point is today's start/end time.
                millisToNextAlarm = millisToPeakTimeEnd;
            } else {
                // After the transition point means we are in peak if today is a
                // peak day.
                if (isPeakDay) {
                    isPeak = true;
                }

                // The next transition point is tomorrow's start/end time.
                millisToNextAlarm = millisToPeakTimeStart + SyncScheduleUtils.MILLISECONDS_PER_DAY;
            }
        } else if (isAfterPeak) {
            // The current time is on or after the start of peak time.

            // The current peak period ends tomorrow if the start time is
            // greater than the end time.
            if (millisToPeakTimeEnd < millisToPeakTimeStart) {
                millisToPeakTimeEnd += SyncScheduleUtils.MILLISECONDS_PER_DAY;
            }

            if (isBeforeOffPeak) {
                // We are in peak if today is a peak day.
                if (isPeakDay) {
                    isPeak = true;
                }

                // The next transition point is the end of peak time.
                millisToNextAlarm = millisToPeakTimeEnd;
            } else {
                // We are in off-peak. The next transition point is tomorrow's
                // start of peak time.
                millisToNextAlarm = millisToPeakTimeStart + SyncScheduleUtils.MILLISECONDS_PER_DAY;
            }
        } else if ((isBeforeOffPeak) && (millisToPeakTimeEnd < millisToPeakTimeStart)) {
            // The current time is before the end of peak time, which precedes
            // the start of peak time.

            // We are in peak if yesterday was a peak day.
            if (isYesterdayPeakDay) {
                isPeak = true;
            }

            // The next transition point is the end of peak time.
            millisToNextAlarm = millisToPeakTimeEnd;
        } else {
            // The current time is off-peak, either preceding the start of a
            // peak period that begins
            // and ends on the same day or between the end of a peak period that
            // began the day before
            // and the start of a peak period beginning today.
            millisToNextAlarm = millisToPeakTimeStart;
        }

        nextAlarm = millisToNextAlarm;
        
        if(!syncScheduleData.getIsPeakScheduleOn())
            isPeak = false;

        return new Pair<Boolean, Long>(isPeak, nextAlarm);
    }

    private static ArrayList<Integer> getPeakDays(SyncScheduleData syncSchedulerData) {
        // get number of peak days
        int peakDays = syncSchedulerData.getPeakDay();
        int mask = 0x01;
        int numberOfPeakDays = 0;
        for (int i = 0; i < SyncScheduleUtils.NUMBER_OF_DAYS; i++) {
            if ((peakDays & mask) != 0) {
                numberOfPeakDays++;
            }
            mask = mask << 1;
        }

        // populate peakDaysArray with Calendar enumeration values
        ArrayList<Integer> peakDaysArray = new ArrayList<Integer>(numberOfPeakDays);
        mask = 0x01;
        int j = 0;
        for (int i = 0; i < SyncScheduleUtils.NUMBER_OF_DAYS; i++) {
            if ((peakDays & mask) != 0) {
                peakDaysArray.add(j++, java.util.Calendar.SUNDAY + i);
            }
            mask = mask << 1;
        }
        return peakDaysArray;
    }

    private static long getMinuteInMillis(Date date, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hr = minute / SyncScheduleUtils.NUMBER_OF_MINUTES_IN_HOUR;
        int min = minute % SyncScheduleUtils.NUMBER_OF_MINUTES_IN_HOUR;
        cal.set(java.util.Calendar.HOUR_OF_DAY, hr);
        cal.set(java.util.Calendar.MINUTE, min);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    private SyncScheduler() {
        // do not initialize this class
    }
}
// change@wtl.pkijowski SyncSchedule end
