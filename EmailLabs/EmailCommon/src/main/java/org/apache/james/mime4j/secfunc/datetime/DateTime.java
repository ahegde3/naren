/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.mime4j.secfunc.datetime;

import org.apache.james.mime4j.Log;
import org.apache.james.mime4j.LogFactory;
import org.apache.james.mime4j.secfunc.datetime.parser.DateTimeParser;
import org.apache.james.mime4j.secfunc.datetime.parser.ParseException;
import org.apache.james.mime4j.secfunc.datetime.parser.TokenMgrError;

import java.io.StringReader;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateTime {
    private static Log log = LogFactory.getLog(/*DateTime.class*/);
    private final Date date;
    private final int year;
    private final int month;
    private final int day;
    private final int hour;
    private final int minute;
    private final int second;
    private final int timeZone;

    public DateTime(String yearString, int month, int day, int hour, int minute, int second,
            int timeZone) {
        this.year = convertToYear(yearString);
        this.date = convertToDate(year, month, day, hour, minute, second, timeZone);
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.timeZone = timeZone;
    }

    private int convertToYear(String yearString) {
        int year1 = Integer.parseInt(yearString);
        switch (yearString.length()) {
            case 1:
            case 2:
                if (year1 >= 0 && year1 < 50)
                    return 2000 + year1;
                else
                    return 1900 + year1;
            case 3:
                return 1900 + year1;
            default:
                return year1;
        }
    }

    public static Date convertToDate(int year, int month, int day, int hour, int minute,
            int second, int timeZone) {
        Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT+0"));
        c.set(year, month - 1, day, hour, minute, second);
        c.set(Calendar.MILLISECOND, 0);

        if (timeZone != Integer.MIN_VALUE) {
            int minutes = ((timeZone / 100) * 60) + timeZone % 100;
            c.add(Calendar.MINUTE, -1 * minutes);
        }

        return c.getTime();
    }

    public Date getDate() {
        return date;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public int getTimeZone() {
        return timeZone;
    }

    public void print() {
        log.info(getYear() + " " + getMonth() + " " + getDay() + "; " + getHour() + " "
                + getMinute() + " " + getSecond() + " " + getTimeZone());
    }

    public static DateTime parse(String dateString) throws ParseException {
        try {
            return new DateTimeParser(new StringReader(dateString)).parseAll();
        } catch (TokenMgrError err) {
            throw new ParseException(err.getMessage());
        }
    }
}
