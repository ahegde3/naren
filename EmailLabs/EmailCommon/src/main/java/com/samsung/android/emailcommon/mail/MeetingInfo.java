/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.samsung.android.emailcommon.mail;

public class MeetingInfo {
    // Predefined tags; others can be added
    public static final String MEETING_DTSTAMP = "DTSTAMP";

    public static final String MEETING_UID = "UID";

    public static final String MEETING_ORGANIZER_EMAIL = "ORGMAIL";

    public static final String MEETING_DTSTART = "DTSTART";

    public static final String MEETING_DTEND = "DTEND";

    public static final String MEETING_TITLE = "TITLE";

    public static final String MEETING_LOCATION = "LOC";

    public static final String MEETING_RESPONSE_REQUESTED = "RESPONSE";
    // change@SISO for Propose new time start
    public static final String MEETING_DISALLOW_NEWTIME_PROPOSAL = "PROPOSE_NEW_TIME";
    public static final String MEETING_PROPOSED_LOCATION = "LOC";
    
    public static final String MEETING_PROPOSED_STRAT_TIME = "PROPOSED_START_TIME";
    
    public static final String MEETING_PROPOSED_END_TIME = "PROPOSED_END_TIME";
    
    public static final String MEETING_RESPONSE = "MEETING_RESPONSE";
    public static final String MEETING_RESPONSE_VCALTYPE = "MEETING_RESPONSE_VCAL";
    public static final String MEETING_DURATION = "DURATION";
    // change@SISO.chakra for Propose new time end
    
    //change@siso.madhu.dumpa calendar meeting forward start
    public static final String MEETING_FORWARD = "MEETING_FORWARD";
    
    public static final String MEETING_SERVER_ID = "SERVER_ID";
    
    public static final String MEETING_IS_ALLDAY = "IS_ALLDAY";
    
    public static final String MEETING_EVENT_ID = "EVENT_ID";
    //change@siso.madhu.dumpa calendar meeting forward end

    // taebong.ha@"Remove event from Calendar" is not working when exception event is cancelled.
    public static final String MEETING_RECURRENCE_ID = "RECURRENCE_ID";
    // taebong.ha@"Remove event from Calendar" is not working when exception event is cancelled.

    //change@siso added for  meeting event type
    public static final String MEETING_INSTANCE_TYPE = "INSTANCE_TYPE";
    
    public static final String MEETING_RRULE = "RRULE";
    public static final String MEETING_SENSITIVITY = "SENSITIVITY";
}
