/*
 * Copyright (C) 2008-2009 Marc Blank
 * Licensed to The Android Open Source Project.
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

package com.samsung.android.emailcommon.utility;

/**
 * The wbxml tags for EAS are all defined here. The static final int's, of the
 * form <page>_<tag> = <constant> are used in parsing incoming responses from
 * the server (i.e. EasParser and its subclasses). The array of String arrays is
 * used to construct server requests with EasSerializer. One thing we might do
 * eventually is to "precompile" these requests, in part, although they should
 * be fairly fast to begin with (each tag requires one HashMap lookup, and there
 * aren't all that many of them in a given command).
 */
public class Tags {
    // Wbxml page definitions for EAS
    public static final int AIRSYNC = 0x00;

    public static final int CONTACTS = 0x01;

    public static final int EMAIL = 0x02;

    public static final int CALENDAR = 0x04;

    public static final int MOVE = 0x05;

    public static final int GIE = 0x06;

    public static final int FOLDER = 0x07;

    public static final int MREQ = 0x08;

    public static final int TASK = 0x09;

    // change@wtl.kirill.k ResolveRecipients start
    public static final int RESOLVERECIPIENTS = 0x0A;

    // change@wtl.kirill.k ResolveRecipients end
    public static final int CONTACTS2 = 0x0C;

    public static final int PING = 0x0D;

    public static final int PROVISION = 0x0E;

    public static final int SEARCH = 0x0F;

    public static final int GAL = 0x10;

    public static final int BASE = 0x11;

    // change@wtl.akulik start IT Policy 12.0
    public static final int SETTINGS = 0x12;

    // chnage@wtl.jpshu document search
    public static final int DOCLIB = 0x13;
    // change@wtl.dtuttle start
    // dr_25: Google hasn't fully defined all the Eas WBXML tags yet.
    public static final int ITEMOPERATIONS = 0x14;

    // change@wtl.dtuttle end

    // change@wtl.lohith SmartReply/SmartForward 14.0 feature
    // start...ComposeMail code
    public static final int COMPOSE_MAIL = 0x15;

    // change@wtl.lohith SmartReply/SmartForward 14.0 feature end
    // change@wtl.pkijowski 14.0
    public static final int EMAIL2 = 0x16;

    // change@wtl.pkijowski 14.0

    // change @ siso Mahsky, Notes Sync 14.0
    public static final int NOTES = 0x17;

    // change @ siso Mahsky, Notes Sync 14.0
    // change @ siso spoorti, IRM 14.1 start
    public static final int IRM = 0x18;
    // change @ siso spoorti, IRM 14.1 end

    // change@wtl.jrabina ValidateCert command start
    public static final int VALIDATE_CERT = 0x0B;

    // change@wtl.jrabina ValidateCert command end

    // Shift applied to page numbers to generate tag
    public static final int PAGE_SHIFT = 6;

    public static final int PAGE_MASK = 0x3F; // 6 bits

    public static final int SYNC_PAGE = AIRSYNC << PAGE_SHIFT;// change@wtl.pkijowski
    // search

    public static final int SYNC_SYNC = SYNC_PAGE + 5;

    public static final int SYNC_RESPONSES = SYNC_PAGE + 6;

    public static final int SYNC_ADD = SYNC_PAGE + 7;

    public static final int SYNC_CHANGE = SYNC_PAGE + 8;

    public static final int SYNC_DELETE = SYNC_PAGE + 9;

    public static final int SYNC_FETCH = SYNC_PAGE + 0xA;

    public static final int SYNC_SYNC_KEY = SYNC_PAGE + 0xB;

    public static final int SYNC_CLIENT_ID = SYNC_PAGE + 0xC;

    public static final int SYNC_SERVER_ID = SYNC_PAGE + 0xD;

    public static final int SYNC_STATUS = SYNC_PAGE + 0xE;

    public static final int SYNC_COLLECTION = SYNC_PAGE + 0xF;

    public static final int SYNC_CLASS = SYNC_PAGE + 0x10;

    public static final int SYNC_VERSION = SYNC_PAGE + 0x11;

    public static final int SYNC_COLLECTION_ID = SYNC_PAGE + 0x12;

    public static final int SYNC_GET_CHANGES = SYNC_PAGE + 0x13;

    public static final int SYNC_MORE_AVAILABLE = SYNC_PAGE + 0x14;

    public static final int SYNC_WINDOW_SIZE = SYNC_PAGE + 0x15;

    public static final int SYNC_COMMANDS = SYNC_PAGE + 0x16;

    public static final int SYNC_OPTIONS = SYNC_PAGE + 0x17;

    public static final int SYNC_FILTER_TYPE = SYNC_PAGE + 0x18;

    public static final int SYNC_TRUNCATION = SYNC_PAGE + 0x19;

    public static final int SYNC_RTF_TRUNCATION = SYNC_PAGE + 0x1A;

    public static final int SYNC_CONFLICT = SYNC_PAGE + 0x1B;

    public static final int SYNC_COLLECTIONS = SYNC_PAGE + 0x1C;

    public static final int SYNC_APPLICATION_DATA = SYNC_PAGE + 0x1D;

    public static final int SYNC_DELETES_AS_MOVES = SYNC_PAGE + 0x1E;

    public static final int SYNC_NOTIFY_GUID = SYNC_PAGE + 0x1F;

    public static final int SYNC_SUPPORTED = SYNC_PAGE + 0x20;

    public static final int SYNC_SOFT_DELETE = SYNC_PAGE + 0x21;

    public static final int SYNC_MIME_SUPPORT = SYNC_PAGE + 0x22;

    public static final int SYNC_MIME_TRUNCATION = SYNC_PAGE + 0x23;

    public static final int SYNC_WAIT = SYNC_PAGE + 0x24;

    public static final int SYNC_LIMIT = SYNC_PAGE + 0x25;

    public static final int SYNC_PARTIAL = SYNC_PAGE + 0x26;

    // change@wtl.pkijowski 14.0
    public static final int SYNC_CONVERSATION_MODE = SYNC_PAGE + 0x27;

    public static final int SYNC_MAX_ITEMS = SYNC_PAGE + 0x28;

    public static final int SYNC_HEARTBEAT_INTERVAL = SYNC_PAGE + 0x29;

    // change@wtl.pkijowski 14.0
    public static final int GIE_PAGE = GIE << PAGE_SHIFT;

    public static final int GIE_GET_ITEM_ESTIMATE = GIE_PAGE + 5;

    public static final int GIE_VERSION = GIE_PAGE + 6;

    public static final int GIE_COLLECTIONS = GIE_PAGE + 7;

    public static final int GIE_COLLECTION = GIE_PAGE + 8;

    public static final int GIE_CLASS = GIE_PAGE + 9;

    public static final int GIE_COLLECTION_ID = GIE_PAGE + 0xA;

    public static final int GIE_DATE_TIME = GIE_PAGE + 0xB;

    public static final int GIE_ESTIMATE = GIE_PAGE + 0xC;

    public static final int GIE_RESPONSE = GIE_PAGE + 0xD;

    public static final int GIE_STATUS = GIE_PAGE + 0xE;

    public static final int CONTACTS_PAGE = CONTACTS << PAGE_SHIFT;

    public static final int CONTACTS_ANNIVERSARY = CONTACTS_PAGE + 5;

    public static final int CONTACTS_ASSISTANT_NAME = CONTACTS_PAGE + 6;

    public static final int CONTACTS_ASSISTANT_TELEPHONE_NUMBER = CONTACTS_PAGE + 7;

    public static final int CONTACTS_BIRTHDAY = CONTACTS_PAGE + 8;

    public static final int CONTACTS_BODY = CONTACTS_PAGE + 9;

    public static final int CONTACTS_BODY_SIZE = CONTACTS_PAGE + 0xA;

    public static final int CONTACTS_BODY_TRUNCATED = CONTACTS_PAGE + 0xB;

    public static final int CONTACTS_BUSINESS2_TELEPHONE_NUMBER = CONTACTS_PAGE + 0xC;

    public static final int CONTACTS_BUSINESS_ADDRESS_CITY = CONTACTS_PAGE + 0xD;

    public static final int CONTACTS_BUSINESS_ADDRESS_COUNTRY = CONTACTS_PAGE + 0xE;

    public static final int CONTACTS_BUSINESS_ADDRESS_POSTAL_CODE = CONTACTS_PAGE + 0xF;

    public static final int CONTACTS_BUSINESS_ADDRESS_STATE = CONTACTS_PAGE + 0x10;

    public static final int CONTACTS_BUSINESS_ADDRESS_STREET = CONTACTS_PAGE + 0x11;

    public static final int CONTACTS_BUSINESS_FAX_NUMBER = CONTACTS_PAGE + 0x12;

    public static final int CONTACTS_BUSINESS_TELEPHONE_NUMBER = CONTACTS_PAGE + 0x13;

    public static final int CONTACTS_CAR_TELEPHONE_NUMBER = CONTACTS_PAGE + 0x14;

    public static final int CONTACTS_CATEGORIES = CONTACTS_PAGE + 0x15;

    public static final int CONTACTS_CATEGORY = CONTACTS_PAGE + 0x16;

    public static final int CONTACTS_CHILDREN = CONTACTS_PAGE + 0x17;

    public static final int CONTACTS_CHILD = CONTACTS_PAGE + 0x18;

    public static final int CONTACTS_COMPANY_NAME = CONTACTS_PAGE + 0x19;

    public static final int CONTACTS_DEPARTMENT = CONTACTS_PAGE + 0x1A;

    public static final int CONTACTS_EMAIL1_ADDRESS = CONTACTS_PAGE + 0x1B;

    public static final int CONTACTS_EMAIL2_ADDRESS = CONTACTS_PAGE + 0x1C;

    public static final int CONTACTS_EMAIL3_ADDRESS = CONTACTS_PAGE + 0x1D;

    public static final int CONTACTS_FILE_AS = CONTACTS_PAGE + 0x1E;

    public static final int CONTACTS_FIRST_NAME = CONTACTS_PAGE + 0x1F;

    public static final int CONTACTS_HOME2_TELEPHONE_NUMBER = CONTACTS_PAGE + 0x20;

    public static final int CONTACTS_HOME_ADDRESS_CITY = CONTACTS_PAGE + 0x21;

    public static final int CONTACTS_HOME_ADDRESS_COUNTRY = CONTACTS_PAGE + 0x22;

    public static final int CONTACTS_HOME_ADDRESS_POSTAL_CODE = CONTACTS_PAGE + 0x23;

    public static final int CONTACTS_HOME_ADDRESS_STATE = CONTACTS_PAGE + 0x24;

    public static final int CONTACTS_HOME_ADDRESS_STREET = CONTACTS_PAGE + 0x25;

    public static final int CONTACTS_HOME_FAX_NUMBER = CONTACTS_PAGE + 0x26;

    public static final int CONTACTS_HOME_TELEPHONE_NUMBER = CONTACTS_PAGE + 0x27;

    public static final int CONTACTS_JOB_TITLE = CONTACTS_PAGE + 0x28;

    public static final int CONTACTS_LAST_NAME = CONTACTS_PAGE + 0x29;

    public static final int CONTACTS_MIDDLE_NAME = CONTACTS_PAGE + 0x2A;

    public static final int CONTACTS_MOBILE_TELEPHONE_NUMBER = CONTACTS_PAGE + 0x2B;

    public static final int CONTACTS_OFFICE_LOCATION = CONTACTS_PAGE + 0x2C;

    public static final int CONTACTS_OTHER_ADDRESS_CITY = CONTACTS_PAGE + 0x2D;

    public static final int CONTACTS_OTHER_ADDRESS_COUNTRY = CONTACTS_PAGE + 0x2E;

    public static final int CONTACTS_OTHER_ADDRESS_POSTAL_CODE = CONTACTS_PAGE + 0x2F;

    public static final int CONTACTS_OTHER_ADDRESS_STATE = CONTACTS_PAGE + 0x30;

    public static final int CONTACTS_OTHER_ADDRESS_STREET = CONTACTS_PAGE + 0x31;

    public static final int CONTACTS_PAGER_NUMBER = CONTACTS_PAGE + 0x32;

    public static final int CONTACTS_RADIO_TELEPHONE_NUMBER = CONTACTS_PAGE + 0x33;

    public static final int CONTACTS_SPOUSE = CONTACTS_PAGE + 0x34;

    public static final int CONTACTS_SUFFIX = CONTACTS_PAGE + 0x35;

    public static final int CONTACTS_TITLE = CONTACTS_PAGE + 0x36;

    public static final int CONTACTS_WEBPAGE = CONTACTS_PAGE + 0x37;

    public static final int CONTACTS_YOMI_COMPANY_NAME = CONTACTS_PAGE + 0x38;

    public static final int CONTACTS_YOMI_FIRST_NAME = CONTACTS_PAGE + 0x39;

    public static final int CONTACTS_YOMI_LAST_NAME = CONTACTS_PAGE + 0x3A;

    public static final int CONTACTS_COMPRESSED_RTF = CONTACTS_PAGE + 0x3B;

    public static final int CONTACTS_PICTURE = CONTACTS_PAGE + 0x3C;

    // change@wtl.jpshu recipient_information_cache begin
    public static final int CONTACTS_ALIAS = CONTACTS_PAGE + 0x3D;

    public static final int CONTACTS_WEIGHTEDRANK = CONTACTS_PAGE + 0x3E;

    // change@wtl.jpshu recipient_information_cache end

    public static final int CALENDAR_PAGE = CALENDAR << PAGE_SHIFT;

    public static final int CALENDAR_TIME_ZONE = CALENDAR_PAGE + 5;

    public static final int CALENDAR_ALL_DAY_EVENT = CALENDAR_PAGE + 6;

    public static final int CALENDAR_ATTENDEES = CALENDAR_PAGE + 7;

    public static final int CALENDAR_ATTENDEE = CALENDAR_PAGE + 8;

    public static final int CALENDAR_ATTENDEE_EMAIL = CALENDAR_PAGE + 9;

    public static final int CALENDAR_ATTENDEE_NAME = CALENDAR_PAGE + 0xA;

    public static final int CALENDAR_BODY = CALENDAR_PAGE + 0xB;

    public static final int CALENDAR_BODY_TRUNCATED = CALENDAR_PAGE + 0xC;

    public static final int CALENDAR_BUSY_STATUS = CALENDAR_PAGE + 0xD;

    public static final int CALENDAR_CATEGORIES = CALENDAR_PAGE + 0xE;

    public static final int CALENDAR_CATEGORY = CALENDAR_PAGE + 0xF;

    public static final int CALENDAR_COMPRESSED_RTF = CALENDAR_PAGE + 0x10;

    public static final int CALENDAR_DTSTAMP = CALENDAR_PAGE + 0x11;

    public static final int CALENDAR_END_TIME = CALENDAR_PAGE + 0x12;

    public static final int CALENDAR_EXCEPTION = CALENDAR_PAGE + 0x13;

    public static final int CALENDAR_EXCEPTIONS = CALENDAR_PAGE + 0x14;

    public static final int CALENDAR_EXCEPTION_IS_DELETED = CALENDAR_PAGE + 0x15;

    public static final int CALENDAR_EXCEPTION_START_TIME = CALENDAR_PAGE + 0x16;

    public static final int CALENDAR_LOCATION = CALENDAR_PAGE + 0x17;

    public static final int CALENDAR_MEETING_STATUS = CALENDAR_PAGE + 0x18;

    public static final int CALENDAR_ORGANIZER_EMAIL = CALENDAR_PAGE + 0x19;

    public static final int CALENDAR_ORGANIZER_NAME = CALENDAR_PAGE + 0x1A;

    public static final int CALENDAR_RECURRENCE = CALENDAR_PAGE + 0x1B;

    public static final int CALENDAR_RECURRENCE_TYPE = CALENDAR_PAGE + 0x1C;

    public static final int CALENDAR_RECURRENCE_UNTIL = CALENDAR_PAGE + 0x1D;

    public static final int CALENDAR_RECURRENCE_OCCURRENCES = CALENDAR_PAGE + 0x1E;

    public static final int CALENDAR_RECURRENCE_INTERVAL = CALENDAR_PAGE + 0x1F;

    public static final int CALENDAR_RECURRENCE_DAYOFWEEK = CALENDAR_PAGE + 0x20;

    public static final int CALENDAR_RECURRENCE_DAYOFMONTH = CALENDAR_PAGE + 0x21;

    public static final int CALENDAR_RECURRENCE_WEEKOFMONTH = CALENDAR_PAGE + 0x22;

    public static final int CALENDAR_RECURRENCE_MONTHOFYEAR = CALENDAR_PAGE + 0x23;

    public static final int CALENDAR_REMINDER_MINS_BEFORE = CALENDAR_PAGE + 0x24;

    public static final int CALENDAR_SENSITIVITY = CALENDAR_PAGE + 0x25;

    public static final int CALENDAR_SUBJECT = CALENDAR_PAGE + 0x26;

    public static final int CALENDAR_START_TIME = CALENDAR_PAGE + 0x27;

    public static final int CALENDAR_UID = CALENDAR_PAGE + 0x28;

    public static final int CALENDAR_ATTENDEE_STATUS = CALENDAR_PAGE + 0x29;

    public static final int CALENDAR_ATTENDEE_TYPE = CALENDAR_PAGE + 0x2A;

    public static final int CALENDAR_ATTACHMENT = CALENDAR_PAGE + 0x2B;

    public static final int CALENDAR_ATTACHMENTS = CALENDAR_PAGE + 0x2C;

    public static final int CALENDAR_ATT_NAME = CALENDAR_PAGE + 0x2D;

    public static final int CALENDAR_ATT_SIZE = CALENDAR_PAGE + 0x2E;

    public static final int CALENDAR_ATT_OID = CALENDAR_PAGE + 0x2F;

    public static final int CALENDAR_ATT_METHOD = CALENDAR_PAGE + 0x30;

    public static final int CALENDAR_ATT_REMOVED = CALENDAR_PAGE + 0x31;

    public static final int CALENDAR_DISPLAY_NAME = CALENDAR_PAGE + 0x32;

    public static final int CALENDAR_DISALLOW_NEWTIME_PROPOSAL = CALENDAR_PAGE + 0x33;

    public static final int CALENDAR_RESPONSE_REQUESTED = CALENDAR_PAGE + 0x34;

    public static final int CALENDAR_APPOINTMENT_REPLY_TIME = CALENDAR_PAGE + 0x35;

    public static final int CALENDAR_RESPONSE_TYPE = CALENDAR_PAGE + 0x36;

    public static final int CALENDAR_CALENDAR_TYPE = CALENDAR_PAGE + 0x37;

    public static final int CALENDAR_IS_LEAP_MONTH = CALENDAR_PAGE + 0x38;

    public static final int FOLDER_PAGE = FOLDER << PAGE_SHIFT;

    public static final int FOLDER_FOLDERS = FOLDER_PAGE + 5;

    public static final int FOLDER_FOLDER = FOLDER_PAGE + 6;

    public static final int FOLDER_DISPLAY_NAME = FOLDER_PAGE + 7;

    public static final int FOLDER_SERVER_ID = FOLDER_PAGE + 8;

    public static final int FOLDER_PARENT_ID = FOLDER_PAGE + 9;

    public static final int FOLDER_TYPE = FOLDER_PAGE + 0xA;

    public static final int FOLDER_RESPONSE = FOLDER_PAGE + 0xB;

    public static final int FOLDER_STATUS = FOLDER_PAGE + 0xC;

    public static final int FOLDER_CONTENT_CLASS = FOLDER_PAGE + 0xD;

    public static final int FOLDER_CHANGES = FOLDER_PAGE + 0xE;

    public static final int FOLDER_ADD = FOLDER_PAGE + 0xF;

    public static final int FOLDER_DELETE = FOLDER_PAGE + 0x10;

    public static final int FOLDER_UPDATE = FOLDER_PAGE + 0x11;

    public static final int FOLDER_SYNC_KEY = FOLDER_PAGE + 0x12;

    public static final int FOLDER_FOLDER_CREATE = FOLDER_PAGE + 0x13;

    public static final int FOLDER_FOLDER_DELETE = FOLDER_PAGE + 0x14;

    public static final int FOLDER_FOLDER_UPDATE = FOLDER_PAGE + 0x15;

    public static final int FOLDER_FOLDER_SYNC = FOLDER_PAGE + 0x16;

    public static final int FOLDER_COUNT = FOLDER_PAGE + 0x17;

    public static final int FOLDER_VERSION = FOLDER_PAGE + 0x18;

    public static final int MREQ_PAGE = MREQ << PAGE_SHIFT;

    public static final int MREQ_CAL_ID = MREQ_PAGE + 5;

    public static final int MREQ_COLLECTION_ID = MREQ_PAGE + 6;

    public static final int MREQ_MEETING_RESPONSE = MREQ_PAGE + 7;

    public static final int MREQ_REQ_ID = MREQ_PAGE + 8;

    public static final int MREQ_INSTANCE_ID = MREQ_PAGE + 0xE;

    public static final int MREQ_REQUEST = MREQ_PAGE + 9;

    public static final int MREQ_RESULT = MREQ_PAGE + 0xA;

    public static final int MREQ_STATUS = MREQ_PAGE + 0xB;

    public static final int MREQ_USER_RESPONSE = MREQ_PAGE + 0xC;

    public static final int MREQ_VERSION = MREQ_PAGE + 0xD;

    public static final int EMAIL_PAGE = EMAIL << PAGE_SHIFT;

    public static final int EMAIL_ATTACHMENT = EMAIL_PAGE + 5;

    public static final int EMAIL_ATTACHMENTS = EMAIL_PAGE + 6;

    public static final int EMAIL_ATT_NAME = EMAIL_PAGE + 7;

    public static final int EMAIL_ATT_SIZE = EMAIL_PAGE + 8;

    public static final int EMAIL_ATT0ID = EMAIL_PAGE + 9;

    public static final int EMAIL_ATT_METHOD = EMAIL_PAGE + 0xA;

    public static final int EMAIL_ATT_REMOVED = EMAIL_PAGE + 0xB;

    public static final int EMAIL_BODY = EMAIL_PAGE + 0xC;

    public static final int EMAIL_BODY_SIZE = EMAIL_PAGE + 0xD;

    public static final int EMAIL_BODY_TRUNCATED = EMAIL_PAGE + 0xE;

    public static final int EMAIL_DATE_RECEIVED = EMAIL_PAGE + 0xF;

    public static final int EMAIL_DISPLAY_NAME = EMAIL_PAGE + 0x10;

    public static final int EMAIL_DISPLAY_TO = EMAIL_PAGE + 0x11;

    public static final int EMAIL_IMPORTANCE = EMAIL_PAGE + 0x12;

    public static final int EMAIL_MESSAGE_CLASS = EMAIL_PAGE + 0x13;

    public static final int EMAIL_SUBJECT = EMAIL_PAGE + 0x14;

    public static final int EMAIL_READ = EMAIL_PAGE + 0x15;

    public static final int EMAIL_TO = EMAIL_PAGE + 0x16;

    public static final int EMAIL_CC = EMAIL_PAGE + 0x17;

    public static final int EMAIL_FROM = EMAIL_PAGE + 0x18;

    public static final int EMAIL_REPLY_TO = EMAIL_PAGE + 0x19;

    public static final int EMAIL_ALL_DAY_EVENT = EMAIL_PAGE + 0x1A;

    public static final int EMAIL_CATEGORIES = EMAIL_PAGE + 0x1B;

    public static final int EMAIL_CATEGORY = EMAIL_PAGE + 0x1C;

    public static final int EMAIL_DTSTAMP = EMAIL_PAGE + 0x1D;

    public static final int EMAIL_END_TIME = EMAIL_PAGE + 0x1E;

    public static final int EMAIL_INSTANCE_TYPE = EMAIL_PAGE + 0x1F;

    public static final int EMAIL_INTD_BUSY_STATUS = EMAIL_PAGE + 0x20;

    public static final int EMAIL_LOCATION = EMAIL_PAGE + 0x21;

    public static final int EMAIL_MEETING_REQUEST = EMAIL_PAGE + 0x22;

    public static final int EMAIL_ORGANIZER = EMAIL_PAGE + 0x23;

    public static final int EMAIL_RECURRENCE_ID = EMAIL_PAGE + 0x24;

    public static final int EMAIL_REMINDER = EMAIL_PAGE + 0x25;

    public static final int EMAIL_RESPONSE_REQUESTED = EMAIL_PAGE + 0x26;

    public static final int EMAIL_RECURRENCES = EMAIL_PAGE + 0x27;

    public static final int EMAIL_RECURRENCE = EMAIL_PAGE + 0x28;

    public static final int EMAIL_RECURRENCE_TYPE = EMAIL_PAGE + 0x29;

    public static final int EMAIL_RECURRENCE_UNTIL = EMAIL_PAGE + 0x2A;

    public static final int EMAIL_RECURRENCE_OCCURRENCES = EMAIL_PAGE + 0x2B;

    public static final int EMAIL_RECURRENCE_INTERVAL = EMAIL_PAGE + 0x2C;

    public static final int EMAIL_RECURRENCE_DAYOFWEEK = EMAIL_PAGE + 0x2D;

    public static final int EMAIL_RECURRENCE_DAYOFMONTH = EMAIL_PAGE + 0x2E;

    public static final int EMAIL_RECURRENCE_WEEKOFMONTH = EMAIL_PAGE + 0x2F;

    public static final int EMAIL_RECURRENCE_MONTHOFYEAR = EMAIL_PAGE + 0x30;

    public static final int EMAIL_START_TIME = EMAIL_PAGE + 0x31;

    public static final int EMAIL_SENSITIVITY = EMAIL_PAGE + 0x32;

    public static final int EMAIL_TIME_ZONE = EMAIL_PAGE + 0x33;

    public static final int EMAIL_GLOBAL_OBJID = EMAIL_PAGE + 0x34;

    public static final int EMAIL_THREAD_TOPIC = EMAIL_PAGE + 0x35;

    public static final int EMAIL_MIME_DATA = EMAIL_PAGE + 0x36;

    public static final int EMAIL_MIME_TRUNCATED = EMAIL_PAGE + 0x37;

    public static final int EMAIL_MIME_SIZE = EMAIL_PAGE + 0x38;

    public static final int EMAIL_INTERNET_CPID = EMAIL_PAGE + 0x39;

    public static final int EMAIL_FLAG = EMAIL_PAGE + 0x3A;

    public static final int EMAIL_FLAG_STATUS = EMAIL_PAGE + 0x3B;

    public static final int EMAIL_CONTENT_CLASS = EMAIL_PAGE + 0x3C;

    public static final int EMAIL_FLAG_TYPE = EMAIL_PAGE + 0x3D;

    public static final int EMAIL_COMPLETE_TIME = EMAIL_PAGE + 0x3E;
    
    //  change@SISO for Propose new time start
    public static final int EMAIL_DISALLOW_NEWTIME_PROPOSAL = EMAIL_PAGE + 0x3F;
    //  change@SISO for Propose new time end

    public static final int TASK_PAGE = TASK << PAGE_SHIFT;

    public static final int TASK_BODY = TASK_PAGE + 5;

    public static final int TASK_BODY_SIZE = TASK_PAGE + 6;

    public static final int TASK_BODY_TRUNCATED = TASK_PAGE + 7;

    public static final int TASK_CATEGORIES = TASK_PAGE + 8;

    public static final int TASK_CATEGORY = TASK_PAGE + 9;

    public static final int TASK_COMPLETE = TASK_PAGE + 0xA;

    public static final int TASK_DATE_COMPLETED = TASK_PAGE + 0xB;

    public static final int TASK_DUE_DATE = TASK_PAGE + 0xC;

    public static final int TASK_UTC_DUE_DATE = TASK_PAGE + 0xD;

    public static final int TASK_IMPORTANCE = TASK_PAGE + 0xE;

    public static final int TASK_RECURRENCE = TASK_PAGE + 0xF;

    public static final int TASK_RECURRENCE_TYPE = TASK_PAGE + 0x10;

    public static final int TASK_RECURRENCE_START = TASK_PAGE + 0x11;

    public static final int TASK_RECURRENCE_UNTIL = TASK_PAGE + 0x12;

    public static final int TASK_RECURRENCE_OCCURRENCES = TASK_PAGE + 0x13;

    public static final int TASK_RECURRENCE_INTERVAL = TASK_PAGE + 0x14;

    public static final int TASK_RECURRENCE_DAY_OF_MONTH = TASK_PAGE + 0x15;

    public static final int TASK_RECURRENCE_DAY_OF_WEEK = TASK_PAGE + 0x16;

    public static final int TASK_RECURRENCE_WEEK_OF_MONTH = TASK_PAGE + 0x17;

    public static final int TASK_RECURRENCE_MONTH_OF_YEAR = TASK_PAGE + 0x18;

    public static final int TASK_RECURRENCE_REGENERATE = TASK_PAGE + 0x19;

    public static final int TASK_RECURRENCE_DEAD_OCCUR = TASK_PAGE + 0x1A;

    public static final int TASK_REMINDER_SET = TASK_PAGE + 0x1B;

    public static final int TASK_REMINDER_TIME = TASK_PAGE + 0x1C;

    public static final int TASK_SENSITIVITY = TASK_PAGE + 0x1D;

    public static final int TASK_START_DATE = TASK_PAGE + 0x1E;

    public static final int TASK_UTC_START_DATE = TASK_PAGE + 0x1F;

    public static final int TASK_SUBJECT = TASK_PAGE + 0x20;

    public static final int COMPRESSED_RTF = TASK_PAGE + 0x21;

    public static final int ORDINAL_DATE = TASK_PAGE + 0x22;

    public static final int SUBORDINAL_DATE = TASK_PAGE + 0x23;

    // change@wtl.kirill.k ResolveRecipients start
    public static final int RESOLVERECIPIENTS_PAGE = RESOLVERECIPIENTS << PAGE_SHIFT;

    public static final int RESOLVERECIPIENTS_RESOLVERECIPIENTS = RESOLVERECIPIENTS_PAGE + 5;

    public static final int RESOLVERECIPIENTS_RESPONSE = RESOLVERECIPIENTS_PAGE + 6;

    public static final int RESOLVERECIPIENTS_STATUS = RESOLVERECIPIENTS_PAGE + 7;

    public static final int RESOLVERECIPIENTS_TYPE = RESOLVERECIPIENTS_PAGE + 8;

    public static final int RESOLVERECIPIENTS_RECIPIENT = RESOLVERECIPIENTS_PAGE + 9;

    public static final int RESOLVERECIPIENTS_DISPLAY_NAME = RESOLVERECIPIENTS_PAGE + 0xA;

    public static final int RESOLVERECIPIENTS_EMAIL_ADDRESS = RESOLVERECIPIENTS_PAGE + 0xB;

    public static final int RESOLVERECIPIENTS_CERTIFICATES = RESOLVERECIPIENTS_PAGE + 0xC;

    public static final int RESOLVERECIPIENTS_CERTIFICATE = RESOLVERECIPIENTS_PAGE + 0xD;

    public static final int RESOLVERECIPIENTS_MINI_CERTIFICATE = RESOLVERECIPIENTS_PAGE + 0xE;

    public static final int RESOLVERECIPIENTS_OPTIONS = RESOLVERECIPIENTS_PAGE + 0xF;

    public static final int RESOLVERECIPIENTS_TO = RESOLVERECIPIENTS_PAGE + 0x10;

    public static final int RESOLVERECIPIENTS_CERTIFICATE_RETRIEVAL = RESOLVERECIPIENTS_PAGE + 0x11;

    public static final int RESOLVERECIPIENTS_RECIPIENT_COUNT = RESOLVERECIPIENTS_PAGE + 0x12;

    public static final int RESOLVERECIPIENTS_MAX_CERTIFICATES = RESOLVERECIPIENTS_PAGE + 0x13;

    public static final int RESOLVERECIPIENTS_MAX_AMBIGOUS_RECIPIENTS = RESOLVERECIPIENTS_PAGE + 0x14;

    public static final int RESOLVERECIPIENTS_CERTIFICATE_COUNT = RESOLVERECIPIENTS_PAGE + 0x15;

    public static final int RESOLVERECIPIENTS_AVAILABILITY = RESOLVERECIPIENTS_PAGE + 0x16;

    public static final int RESOLVERECIPIENTS_START_TIME = RESOLVERECIPIENTS_PAGE + 0x17;

    public static final int RESOLVERECIPIENTS_END_TIME = RESOLVERECIPIENTS_PAGE + 0x18;

    public static final int RESOLVERECIPIENTS_MERGED_FREE_BUSY = RESOLVERECIPIENTS_PAGE + 0x19;

    // change@wtl.kirill.k ResolveRecipients end

    public static final int MOVE_PAGE = MOVE << PAGE_SHIFT;

    public static final int MOVE_MOVE_ITEMS = MOVE_PAGE + 5;

    public static final int MOVE_MOVE = MOVE_PAGE + 6;

    public static final int MOVE_SRCMSGID = MOVE_PAGE + 7;

    public static final int MOVE_SRCFLDID = MOVE_PAGE + 8;

    public static final int MOVE_DSTFLDID = MOVE_PAGE + 9;

    public static final int MOVE_RESPONSE = MOVE_PAGE + 0xA;

    public static final int MOVE_STATUS = MOVE_PAGE + 0xB;

    public static final int MOVE_DSTMSGID = MOVE_PAGE + 0xC;

    public static final int CONTACTS2_PAGE = CONTACTS2 << PAGE_SHIFT;

    public static final int CONTACTS2_CUSTOMER_ID = CONTACTS2_PAGE + 5;

    public static final int CONTACTS2_GOVERNMENT_ID = CONTACTS2_PAGE + 6;

    public static final int CONTACTS2_IM_ADDRESS = CONTACTS2_PAGE + 7;

    public static final int CONTACTS2_IM_ADDRESS_2 = CONTACTS2_PAGE + 8;

    public static final int CONTACTS2_IM_ADDRESS_3 = CONTACTS2_PAGE + 9;

    public static final int CONTACTS2_MANAGER_NAME = CONTACTS2_PAGE + 0xA;

    public static final int CONTACTS2_COMPANY_MAIN_PHONE = CONTACTS2_PAGE + 0xB;

    public static final int CONTACTS2_ACCOUNT_NAME = CONTACTS2_PAGE + 0xC;

    public static final int CONTACTS2_NICKNAME = CONTACTS2_PAGE + 0xD;

    public static final int CONTACTS2_MMS = CONTACTS2_PAGE + 0xE;

    public static final int PING_PAGE = PING << PAGE_SHIFT;

    public static final int PING_PING = PING_PAGE + 5;

    public static final int PING_AUTD_STATE = PING_PAGE + 6;

    public static final int PING_STATUS = PING_PAGE + 7;

    public static final int PING_HEARTBEAT_INTERVAL = PING_PAGE + 8;

    public static final int PING_FOLDERS = PING_PAGE + 9;

    public static final int PING_FOLDER = PING_PAGE + 0xA;

    public static final int PING_ID = PING_PAGE + 0xB;

    public static final int PING_CLASS = PING_PAGE + 0xC;

    public static final int PING_MAX_FOLDERS = PING_PAGE + 0xD;

    public static final int SEARCH_PAGE = SEARCH << PAGE_SHIFT;

    public static final int SEARCH_SEARCH = SEARCH_PAGE + 5;

    public static final int SEARCH_STORES = SEARCH_PAGE + 6;

    public static final int SEARCH_STORE = SEARCH_PAGE + 7;

    public static final int SEARCH_NAME = SEARCH_PAGE + 8;

    public static final int SEARCH_QUERY = SEARCH_PAGE + 9;

    public static final int SEARCH_OPTIONS = SEARCH_PAGE + 0xA;

    public static final int SEARCH_RANGE = SEARCH_PAGE + 0xB;

    public static final int SEARCH_STATUS = SEARCH_PAGE + 0xC;

    public static final int SEARCH_RESPONSE = SEARCH_PAGE + 0xD;

    public static final int SEARCH_RESULT = SEARCH_PAGE + 0xE;

    public static final int SEARCH_PROPERTIES = SEARCH_PAGE + 0xF;

    public static final int SEARCH_TOTAL = SEARCH_PAGE + 0x10;

    public static final int SEARCH_EQUAL_TO = SEARCH_PAGE + 0x11;

    public static final int SEARCH_VALUE = SEARCH_PAGE + 0x12;

    public static final int SEARCH_AND = SEARCH_PAGE + 0x13;

    public static final int SEARCH_OR = SEARCH_PAGE + 0x14;

    public static final int SEARCH_FREE_TEXT = SEARCH_PAGE + 0x15;

    public static final int SEARCH_SUBSTRING_OP = SEARCH_PAGE + 0x16;

    public static final int SEARCH_DEEP_TRAVERSAL = SEARCH_PAGE + 0x17;

    public static final int SEARCH_LONG_ID = SEARCH_PAGE + 0x18;

    public static final int SEARCH_REBUILD_RESULTS = SEARCH_PAGE + 0x19;

    public static final int SEARCH_LESS_THAN = SEARCH_PAGE + 0x1A;

    public static final int SEARCH_GREATER_THAN = SEARCH_PAGE + 0x1B;

    public static final int SEARCH_SCHEMA = SEARCH_PAGE + 0x1C;

    public static final int SEARCH_SUPPORTED = SEARCH_PAGE + 0x1D;

    // change@siso.vpdj start
    public static final int SEARCH_USERNAME = SEARCH_PAGE + 0x1E;
    public static final int SEARCH_PASSWORD = SEARCH_PAGE + 0x1F;
    public static final int SEARCH_CONVERSATIONID = SEARCH_PAGE + 0x20;
    // change@siso.vpdj end

    // change@siso.vinay Gal photo start
    public static final int SEARCH_PICTURE = SEARCH_PAGE + 0x21;
    public static final int SEARCH_MAXSIZE = SEARCH_PAGE + 0x22;
    public static final int SEARCH_MAXPICTURES = SEARCH_PAGE + 0x23;
    // change@siso.vinay Gal photo end

// change@wtl.jpshu document search begin
    public static final int DOCLIB_PAGE = DOCLIB << PAGE_SHIFT;
    public static final int DOCLIB_LINKID = DOCLIB_PAGE + 5;
    public static final int DOCLIB_DISPLAY_NAME = DOCLIB_PAGE + 6;
    public static final int DOCLIB_ISFOLDER = DOCLIB_PAGE + 7;
    public static final int DOCLIB_CREATION_DATE = DOCLIB_PAGE + 8;
    public static final int DOCLIB_LAST_MODIFIED_DATE = DOCLIB_PAGE + 9;
    public static final int DOCLIB_ISHIDDEN = DOCLIB_PAGE + 0xA;
    public static final int DOCLIB_CONTENT_LENGTH = DOCLIB_PAGE + 0xB;
    public static final int DOCLIB_CONTENT_TYPE = DOCLIB_PAGE + 0xC;
// change@wtl.jpshu document search end    
    public static final int GAL_PAGE = GAL << PAGE_SHIFT;

    public static final int GAL_DISPLAY_NAME = GAL_PAGE + 5;

    public static final int GAL_PHONE = GAL_PAGE + 6;

    public static final int GAL_OFFICE = GAL_PAGE + 7;

    public static final int GAL_TITLE = GAL_PAGE + 8;

    public static final int GAL_COMPANY = GAL_PAGE + 9;

    public static final int GAL_ALIAS = GAL_PAGE + 0xA;

    public static final int GAL_FIRST_NAME = GAL_PAGE + 0xB;

    public static final int GAL_LAST_NAME = GAL_PAGE + 0xC;

    public static final int GAL_HOME_PHONE = GAL_PAGE + 0xD;

    public static final int GAL_MOBILE_PHONE = GAL_PAGE + 0xE;

    public static final int GAL_EMAIL_ADDRESS = GAL_PAGE + 0xF;

    // change@siso.vinay Gal photo start
    public static final int GAL_PICTURE = GAL_PAGE + 0x10;
    public static final int GAL_STATUS = GAL_PAGE + 0x11;
    public static final int GAL_PICTURE_DATA = GAL_PAGE + 0x12;
    // change@siso.vinay Gal photo end

    public static final int PROVISION_PAGE = PROVISION << PAGE_SHIFT;

    // EAS 2.5
    public static final int PROVISION_PROVISION = PROVISION_PAGE + 5;

    public static final int PROVISION_POLICIES = PROVISION_PAGE + 6;

    public static final int PROVISION_POLICY = PROVISION_PAGE + 7;

    public static final int PROVISION_POLICY_TYPE = PROVISION_PAGE + 8;

    public static final int PROVISION_POLICY_KEY = PROVISION_PAGE + 9;

    public static final int PROVISION_DATA = PROVISION_PAGE + 0xA;

    public static final int PROVISION_STATUS = PROVISION_PAGE + 0xB;

    public static final int PROVISION_REMOTE_WIPE = PROVISION_PAGE + 0xC;

    // EAS 12.0
    public static final int PROVISION_EAS_PROVISION_DOC = PROVISION_PAGE + 0xD;

    public static final int PROVISION_DEVICE_PASSWORD_ENABLED = PROVISION_PAGE + 0xE;

    public static final int PROVISION_ALPHA_DEVICE_PASSWORD_ENABLED = PROVISION_PAGE + 0xF;

    public static final int PROVISION_DEVICE_ENCRYPTION_ENABLED = PROVISION_PAGE + 0x10;

    public static final int PROVISION_PASSWORD_RECOVERY_ENABLED = PROVISION_PAGE + 0x11;

    public static final int PROVISION_ATTACHMENTS_ENABLED = PROVISION_PAGE + 0x13;

    public static final int PROVISION_MIN_DEVICE_PASSWORD_LENGTH = PROVISION_PAGE + 0x14;

    public static final int PROVISION_MAX_INACTIVITY_TIME_DEVICE_LOCK = PROVISION_PAGE + 0x15;

    public static final int PROVISION_MAX_DEVICE_PASSWORD_FAILED_ATTEMPTS = PROVISION_PAGE + 0x16;

    public static final int PROVISION_MAX_ATTACHMENT_SIZE = PROVISION_PAGE + 0x17;

    public static final int PROVISION_ALLOW_SIMPLE_DEVICE_PASSWORD = PROVISION_PAGE + 0x18;

    public static final int PROVISION_DEVICE_PASSWORD_EXPIRATION = PROVISION_PAGE + 0x19;

    public static final int PROVISION_DEVICE_PASSWORD_HISTORY = PROVISION_PAGE + 0x1A;

    public static final int PROVISION_MAX_SUPPORTED_TAG = PROVISION_DEVICE_PASSWORD_HISTORY;

    // EAS 12.1
    public static final int PROVISION_ALLOW_STORAGE_CARD = PROVISION_PAGE + 0x1B;

    public static final int PROVISION_ALLOW_CAMERA = PROVISION_PAGE + 0x1C;

    public static final int PROVISION_REQUIRE_DEVICE_ENCRYPTION = PROVISION_PAGE + 0x1D;

    public static final int PROVISION_ALLOW_UNSIGNED_APPLICATIONS = PROVISION_PAGE + 0x1E;

    public static final int PROVISION_ALLOW_UNSIGNED_INSTALLATION_PACKAGES = PROVISION_PAGE + 0x1F;

    public static final int PROVISION_MIN_DEVICE_PASSWORD_COMPLEX_CHARS = PROVISION_PAGE + 0x20;

    public static final int PROVISION_ALLOW_WIFI = PROVISION_PAGE + 0x21;

    public static final int PROVISION_ALLOW_TEXT_MESSAGING = PROVISION_PAGE + 0x22;

    public static final int PROVISION_ALLOW_POP_IMAP_EMAIL = PROVISION_PAGE + 0x23;

    public static final int PROVISION_ALLOW_BLUETOOTH = PROVISION_PAGE + 0x24;

    public static final int PROVISION_ALLOW_IRDA = PROVISION_PAGE + 0x25;

    public static final int PROVISION_REQUIRE_MANUAL_SYNC_WHEN_ROAMING = PROVISION_PAGE + 0x26;

    public static final int PROVISION_ALLOW_DESKTOP_SYNC = PROVISION_PAGE + 0x27;

    public static final int PROVISION_MAX_CALENDAR_AGE_FILTER = PROVISION_PAGE + 0x28;

    public static final int PROVISION_ALLOW_HTML_EMAIL = PROVISION_PAGE + 0x29;

    public static final int PROVISION_MAX_EMAIL_AGE_FILTER = PROVISION_PAGE + 0x2A;

    public static final int PROVISION_MAX_EMAIL_BODY_TRUNCATION_SIZE = PROVISION_PAGE + 0x2B;

    public static final int PROVISION_MAX_EMAIL_HTML_BODY_TRUNCATION_SIZE = PROVISION_PAGE + 0x2C;

    public static final int PROVISION_REQUIRE_SIGNED_SMIME_MESSAGES = PROVISION_PAGE + 0x2D;

    public static final int PROVISION_REQUIRE_ENCRYPTED_SMIME_MESSAGES = PROVISION_PAGE + 0x2E;

    public static final int PROVISION_REQUIRE_SIGNED_SMIME_ALGORITHM = PROVISION_PAGE + 0x2F;

    public static final int PROVISION_REQUIRE_ENCRYPTION_SMIME_ALGORITHM = PROVISION_PAGE + 0x30;

    public static final int PROVISION_ALLOW_SMIME_ENCRYPTION_NEGOTIATION = PROVISION_PAGE + 0x31;

    public static final int PROVISION_ALLOW_SMIME_SOFT_CERTS = PROVISION_PAGE + 0x32;

    public static final int PROVISION_ALLOW_BROWSER = PROVISION_PAGE + 0x33;

    public static final int PROVISION_ALLOW_CONSUMER_EMAIL = PROVISION_PAGE + 0x34;

    public static final int PROVISION_ALLOW_REMOTE_DESKTOP = PROVISION_PAGE + 0x35;

    public static final int PROVISION_ALLOW_INTERNET_SHARING = PROVISION_PAGE + 0x36;

    public static final int PROVISION_UNAPPROVED_IN_ROM_APPLICATION_LIST = PROVISION_PAGE + 0x37;

    public static final int PROVISION_APPLICATION_NAME = PROVISION_PAGE + 0x38;

    public static final int PROVISION_APPROVED_APPLICATION_LIST = PROVISION_PAGE + 0x39;

    public static final int PROVISION_HASH = PROVISION_PAGE + 0x3A;

    public static final int BASE_PAGE = BASE << PAGE_SHIFT;

    public static final int BASE_BODY_PREFERENCE = BASE_PAGE + 5;

    public static final int BASE_TYPE = BASE_PAGE + 6;

    public static final int BASE_TRUNCATION_SIZE = BASE_PAGE + 7;

    public static final int BASE_ALL_OR_NONE = BASE_PAGE + 8;

    public static final int BASE_RESERVED = BASE_PAGE + 9;

    public static final int BASE_BODY = BASE_PAGE + 0xA;

    public static final int BASE_DATA = BASE_PAGE + 0xB;

    public static final int BASE_ESTIMATED_DATA_SIZE = BASE_PAGE + 0xC;

    public static final int BASE_TRUNCATED = BASE_PAGE + 0xD;

    public static final int BASE_ATTACHMENTS = BASE_PAGE + 0xE;

    public static final int BASE_ATTACHMENT = BASE_PAGE + 0xF;

    public static final int BASE_DISPLAY_NAME = BASE_PAGE + 0x10;

    public static final int BASE_FILE_REFERENCE = BASE_PAGE + 0x11;

    public static final int BASE_METHOD = BASE_PAGE + 0x12;

    public static final int BASE_CONTENT_ID = BASE_PAGE + 0x13;

    public static final int BASE_CONTENT_LOCATION = BASE_PAGE + 0x14;

    public static final int BASE_IS_INLINE = BASE_PAGE + 0x15;

    public static final int BASE_NATIVE_BODY_TYPE = BASE_PAGE + 0x16;

    public static final int BASE_CONTENT_TYPE = BASE_PAGE + 0x17;

    // change@siso.vpdj Conversation Text preview start
    public static final int BASE_PREVIEW = BASE_PAGE + 0x18;
  //MessageDiff change @siso.gaurav start
    public static final int BASE_BODY_PART_PREFERENCE = BASE_PAGE + 0x19;
    public static final int BASE_BODY_PART = BASE_PAGE + 0x1A;
    public static final int BASE_BODY_PART_STATUS = BASE_PAGE + 0x1B;
  //MessageDiff change @siso.gaurav end

    // change@siso.vpdj Conversation Text preview end
    // out of office change@wtl.ksingh
    public static final int SETTINGS_PAGE = SETTINGS << PAGE_SHIFT;

    public static final int SETTINGS_SETTINGS = SETTINGS_PAGE + 0x5;

    public static final int SETTINGS_STATUS = SETTINGS_PAGE + 0x6;

    public static final int SETTINGS_GET = SETTINGS_PAGE + 0x7;

    public static final int SETTINGS_SET = SETTINGS_PAGE + 0x8;

    public static final int SETTINGS_OOO = SETTINGS_PAGE + 0x9;

    public static final int SETTINGS_OOO_STATE = SETTINGS_PAGE + 0xA;

    public static final int SETTINGS_START_TIME = SETTINGS_PAGE + 0xB;

    public static final int SETTINGS_END_TIME = SETTINGS_PAGE + 0xC;

    public static final int SETTINGS_OOO_MESSAGE = SETTINGS_PAGE + 0xD;

    public static final int SETTINGS_APPLIES_TO_INTERNAL = SETTINGS_PAGE + 0xE;

    public static final int SETTINGS_APPLIES_TO_EXT_KNOWN = SETTINGS_PAGE + 0xF;

    public static final int SETTINGS_APPLIES_TO_EXT_UNKNOWN = SETTINGS_PAGE + 0x10;

    public static final int SETTINGS_ENABLED = SETTINGS_PAGE + 0x11;

    public static final int SETTINGS_REPLY_MESSAGE = SETTINGS_PAGE + 0x12;

    public static final int SETTINGS_BODY_TYPE = SETTINGS_PAGE + 0x13;

    public static final int SETTINGS_DEVICE_PASSWORD = SETTINGS_PAGE + 0x14;

    public static final int SETTINGS_PASSWORD = SETTINGS_PAGE + 0x15;

    public static final int SETTINGS_DEVICE_INFORMATION = SETTINGS_PAGE + 0x16;

    public static final int SETTINGS_MODEL = SETTINGS_PAGE + 0x17;

    public static final int SETTINGS_IMEI = SETTINGS_PAGE + 0x18;

    public static final int SETTINGS_FRIENDLY_NAME = SETTINGS_PAGE + 0x19;

    public static final int SETTINGS_OS = SETTINGS_PAGE + 0x1A;

    public static final int SETTINGS_OS_LANGUAGE = SETTINGS_PAGE + 0x1B;

    public static final int SETTINGS_PHONE_NUMBER = SETTINGS_PAGE + 0x1C;

    public static final int SETTINGS_USER_INFORMATION = SETTINGS_PAGE + 0x1D;

    public static final int SETTINGS_EMAIL_ADDRESS = SETTINGS_PAGE + 0x1E;

    public static final int SETTINGS_SMTP_ADDRESS = SETTINGS_PAGE + 0x1F;

    // change@wtl.kSingh ends
    // change@siso.cmouli Setting-DeviceInformation start
    // public static final int SETTINGS_IMEI = SETTINGS_PAGE + 0x18;
    public static final int SETTINGS_USER_AGENT = SETTINGS_PAGE + 0x20;

    public static final int SETTINGS_ENABLE_OUTBOUND_SMS = SETTINGS_PAGE + 0x21;

    public static final int SETTINGS_MOBILE_OPERATOR = SETTINGS_PAGE + 0x22;

    // change@siso.cmouli Setting-DeviceInformation end
    // change@siso.Spoorti IRM support starts
    public static final int SETTINGS_IRM_RIGHT_MANAGEMENT_INFORMATION = SETTINGS_PAGE + 0x2B;
    // change@siso.Spoorti IRM support ends

    // change@wtl.dtuttle start
    // dr_26: ItemOperations child elements. Got this information from cupcake's
    // frameworks/base/activesyncfwk/ActiveSync/dataviz/rs/WBXML/wbxml_tables.cpp
    public static final int ITEMOPERATIONS_PAGE = ITEMOPERATIONS << PAGE_SHIFT;

    public static final int ITEMOPERATIONS_ITEMOPERATIONS = ITEMOPERATIONS_PAGE + 0x5;

    public static final int ITEMOPERATIONS_FETCH = ITEMOPERATIONS_PAGE + 0x6;

    public static final int ITEMOPERATIONS_STORE = ITEMOPERATIONS_PAGE + 0x7;

    public static final int ITEMOPERATIONS_OPTIONS = ITEMOPERATIONS_PAGE + 0x8;

    public static final int ITEMOPERATIONS_RANGE = ITEMOPERATIONS_PAGE + 0x9;

    public static final int ITEMOPERATIONS_TOTAL = ITEMOPERATIONS_PAGE + 0xA;

    public static final int ITEMOPERATIONS_PROPERTIES = ITEMOPERATIONS_PAGE + 0xB;

    public static final int ITEMOPERATIONS_DATA = ITEMOPERATIONS_PAGE + 0xC;

    public static final int ITEMOPERATIONS_STATUS = ITEMOPERATIONS_PAGE + 0xD;

    public static final int ITEMOPERATIONS_RESPONSE = ITEMOPERATIONS_PAGE + 0xE;

    public static final int ITEMOPERATIONS_VERSION = ITEMOPERATIONS_PAGE + 0xF;

    public static final int ITEMOPERATIONS_SCHEMA = ITEMOPERATIONS_PAGE + 0x10;

    public static final int ITEMOPERATIONS_PART = ITEMOPERATIONS_PAGE + 0x11;

    public static final int ITEMOPERATIONS_EMPTYFOLDERCONTENTS = ITEMOPERATIONS_PAGE + 0x12;

    public static final int ITEMOPERATIONS_DELETESUBFOLDERS = ITEMOPERATIONS_PAGE + 0x13;

    // change@wtl.kirill.k EAS 14.0 support in ItemOperations start
    public static final int ITEMOPERATIONS_USERNAME = ITEMOPERATIONS_PAGE + 0x14;

    public static final int ITEMOPERATIONS_PASSWORD = ITEMOPERATIONS_PAGE + 0x15;

    public static final int ITEMOPERATIONS_MOVE = ITEMOPERATIONS_PAGE + 0x16;

    public static final int ITEMOPERATIONS_DSTFLDID = ITEMOPERATIONS_PAGE + 0x17;

    public static final int ITEMOPERATIONS_CONVERSATIONID = ITEMOPERATIONS_PAGE + 0x18;

    public static final int ITEMOPERATIONS_MOVEALWAYS = ITEMOPERATIONS_PAGE + 0x19;

    // change@wtl.kirill.k EAS 14.0 support in ItemOperations end
    // change@wtl.dtuttle end
    // change@wtl.lohith SmartReply/SmartForward 14.0 feature start..compose
    // mail codes
    public static final int COMPOSE_MAIL_PAGE = COMPOSE_MAIL << PAGE_SHIFT;

    public static final int COMPOSE_MAIL_SEND_MAIL = COMPOSE_MAIL_PAGE + 0x05;

    public static final int COMPOSE_MAIL_SMART_FORWARD = COMPOSE_MAIL_PAGE + 0x06;

    public static final int COMPOSE_MAIL_SMART_REPLY = COMPOSE_MAIL_PAGE + 0x07;

    public static final int COMPOSE_MAIL_SAVE_IN_SENT_ITEMS = COMPOSE_MAIL_PAGE + 0x08;

    public static final int COMPOSE_MAIL_REPLACE_MIME = COMPOSE_MAIL_PAGE + 0x09;

    public static final int COMPOSE_MAIL_TYPE = COMPOSE_MAIL_PAGE + 0x0A;

    public static final int COMPOSE_MAIL_SOURCE = COMPOSE_MAIL_PAGE + 0x0B;

    public static final int COMPOSE_MAIL_FOLDER_ID = COMPOSE_MAIL_PAGE + 0x0C;

    public static final int COMPOSE_MAIL_ITEM_ID = COMPOSE_MAIL_PAGE + 0x0D;

    public static final int COMPOSE_MAIL_LONG_ID = COMPOSE_MAIL_PAGE + 0x0E;

    public static final int COMPOSE_MAIL_INSTANCE_ID = COMPOSE_MAIL_PAGE + 0x0F;

    public static final int COMPOSE_MAIL_MIME = COMPOSE_MAIL_PAGE + 0x10;

    public static final int COMPOSE_MAIL_CLIENT_ID = COMPOSE_MAIL_PAGE + 0x11;

    public static final int COMPOSE_MAIL_STATUS = COMPOSE_MAIL_PAGE + 0x12;

    // change@wtl.lohith SmartReply/SmartForward 14.0 feature end

    // change@wtl.pkijowski 14.0
    public static final int EMAIL2_PAGE = EMAIL2 << PAGE_SHIFT;

    public static final int EMAIL2_UM_CALLER_ID = EMAIL2_PAGE + 5;

    public static final int EMAIL2_UM_USER_NOTES = EMAIL2_PAGE + 6;

    public static final int EMAIL2_UM_ATT_DURATION = EMAIL2_PAGE + 7;

    public static final int EMAIL2_UM_ATT_ORDER = EMAIL2_PAGE + 8;

    public static final int EMAIL2_CONVERSATION_ID = EMAIL2_PAGE + 9;

    public static final int EMAIL2_CONVERSATION_INDEX = EMAIL2_PAGE + 0xA;

    public static final int EMAIL2_LAST_VERB_EXECUTED = EMAIL2_PAGE + 0xB;

    public static final int EMAIL2_LAST_VERB_EXECUTION_TIME = EMAIL2_PAGE + 0xC;

    public static final int EMAIL2_RECEIVED_AS_BCC = EMAIL2_PAGE + 0xD;

    public static final int EMAIL2_SENDER = EMAIL2_PAGE + 0xE;

    public static final int EMAIL2_CALENDAR_TYPE = EMAIL2_PAGE + 0xF;

    public static final int EMAIL2_IS_LEAP_MONTH = EMAIL2_PAGE + 0x10;

    // change@wtl.pkijowski 14.0

    // change @ siso, Mahsky, Notes Sync, 14.0
    public static final int NOTES_PAGE = NOTES << PAGE_SHIFT;

    public static final int NOTES_SUBJECT = NOTES_PAGE + 0X5;

    public static final int NOTES_MESSAGE_CLASS = NOTES_PAGE + 0X6;

    public static final int NOTES_LAST_MODIFIED_DATE = NOTES_PAGE + 0X7;

    public static final int NOTES_CATEGORIES = NOTES_PAGE + 0X8;

    public static final int NOTES_CATEGORY = NOTES_PAGE + 0X9;

    // change @ siso, Mahsky, Notes Sync, 14.0

    // change@wtl.jrabina ValidateCert command start
    public static final int VALIDATE_CERT_PAGE = VALIDATE_CERT << PAGE_SHIFT;

    public static final int VALIDATE_CERT_VALIDATE_CERT = VALIDATE_CERT_PAGE + 5;

    public static final int VALIDATE_CERT_CERTIFICATES = VALIDATE_CERT_PAGE + 6;

    public static final int VALIDATE_CERT_CERTIFICATE = VALIDATE_CERT_PAGE + 7;

    public static final int VALIDATE_CERT_CERTIFICATE_CHAIN = VALIDATE_CERT_PAGE + 8;

    public static final int VALIDATE_CERT_CHECKCRL = VALIDATE_CERT_PAGE + 9;

    public static final int VALIDATE_CERT_STATUS = VALIDATE_CERT_PAGE + 0xA;

    // change@wtl.jrabina ValidateCert command end
    // change@siso.spoorti IRM Support start 14.1
    public static final int IRM_PAGE = IRM << PAGE_SHIFT;
    public static final int RIGHTS_MANAGEMENT_SUPPORT = IRM_PAGE + 0x5;
    public static final int RIGHTS_MANAGEMENT_TEMPLATES = IRM_PAGE + 0x6;
    public static final int RIGHTS_MANAGEMENT_TEMPLATE = IRM_PAGE + 0x7;
    public static final int RIGHTS_MANAGEMENT_LICENSE = IRM_PAGE + 0x8;
    public static final int IRM_EDIT_ALLOWED = IRM_PAGE + 0x9;
    public static final int IRM_REPLY_ALLOWED = IRM_PAGE + 0x0A;
    public static final int IRM_REPLY_ALL_ALLOWED = IRM_PAGE + 0x0B;
    public static final int IRM_FORWARD_ALLOWED = IRM_PAGE + 0x0C;
    public static final int IRM_MODIFY_RECIPIENTS_ALLOWED = IRM_PAGE + 0x0D;
    public static final int IRM_EXTRACT_ALLOWED = IRM_PAGE + 0x0E;
    public static final int IRM_PRINT_ALLOWED = IRM_PAGE + 0x0F;
    public static final int IRM_EXPORT_ALLOWED = IRM_PAGE + 0x10;
    public static final int IRM_PROGRAMMATIC_ACCESS_ALLOWED = IRM_PAGE + 0x11;
    public static final int IRM_OWNER = IRM_PAGE + 0x12;
    public static final int IRM_CONTENT_EXPIRY_DATE = IRM_PAGE + 0x13;
    public static final int IRM_TEMPLATE_ID = IRM_PAGE + 0x14;
    public static final int IRM_TEMPLATE_NAME = IRM_PAGE + 0x15;
    public static final int IRM_TEMPLATE_DESCRIPTION = IRM_PAGE + 0x16;
    public static final int IRM_CONTENT_OWNER = IRM_PAGE + 0x17;
    public static final int IRM_REMOVE_RIGHTS_MANAGEMENT_DISTRIBUTION = IRM_PAGE + 0x18;
    // change@siso.spoorti IRM Support end

    static public String[][] pages = {
            { // 0x00 AirSync
                    "Sync", "Responses", "Add", "Change", "Delete", "Fetch", "SyncKey", "ClientId",
                    "ServerId", "Status", "Collection", "Class", "Version", "CollectionId",
                    "GetChanges", "MoreAvailable", "WindowSize", "Commands", "Options",
                    "FilterType", "Truncation", "RTFTruncation", "Conflict", "Collections",
                    "ApplicationData", "DeletesAsMoves", "NotifyGUID", "Supported", "SoftDelete",
                    "MIMESupport", "MIMETruncation", "Wait", "Limit", "Partial",
                    // change@wtl.pkijowski 14.0
                    "ConversationMode", "MaxItems", "HeartbeatInterval"
            // change@wtl.pkijowski 14.0
            },
            {
                    // 0x01 Contacts
                    "Anniversary", "AssistantName", "AssistantTelephoneNumber", "Birthday",
                    "ContactsBody", "ContactsBodySize", "ContactsBodyTruncated",
                    "Business2TelephoneNumber", "BusinessAddressCity", "BusinessAddressCountry",
                    "BusinessAddressPostalCode", "BusinessAddressState", "BusinessAddressStreet",
                    "BusinessFaxNumber", "BusinessTelephoneNumber", "CarTelephoneNumber",
                    "ContactsCategories", "ContactsCategory", "Children", "Child", "CompanyName",
                    "Department", "Email1Address", "Email2Address", "Email3Address", "FileAs",
                    "FirstName", "Home2TelephoneNumber", "HomeAddressCity", "HomeAddressCountry",
                    "HomeAddressPostalCode", "HomeAddressState", "HomeAddressStreet",
                    "HomeFaxNumber", "HomeTelephoneNumber", "JobTitle", "LastName", "MiddleName",
                    "MobileTelephoneNumber", "OfficeLocation", "OtherAddressCity",
                    "OtherAddressCountry", "OtherAddressPostalCode", "OtherAddressState",
                    "OtherAddressStreet", "PagerNumber", "RadioTelephoneNumber", "Spouse",
                    "Suffix", "Title", "Webpage", "YomiCompanyName", "YomiFirstName",
                    "YomiLastName", "CompressedRTF", "Picture",
                    // change@wtl.jpshu recipient_information_cache begin
                    "Alias", "WeightedRank"
            // change@wtl.jpshu recipient_information_cache end
            },
            {
                    // 0x02 Email
                    "Attachment", "Attachments", "AttName", "AttSize", "Add0Id", "AttMethod",
                    "AttRemoved", "Body", "BodySize", "BodyTruncated", "DateReceived",
                    "DisplayName", "DisplayTo", "Importance", "MessageClass", "Subject", "Read",
                    "To", "CC", "From", "ReplyTo", "AllDayEvent", "Categories", "Category",
                    "DTStamp", "EndTime", "InstanceType", "IntDBusyStatus", "Location",
                    "MeetingRequest", "Organizer", "RecurrenceId", "Reminder", "ResponseRequested",
                    "Recurrences", "Recurence", "Recurrence_Type", "Recurrence_Until",
                    "Recurrence_Occurrences", "Recurrence_Interval", "Recurrence_DayOfWeek",
                    "Recurrence_DayOfMonth", "Recurrence_WeekOfMonth", "Recurrence_MonthOfYear",
                    "StartTime", "Sensitivity", "TimeZone", "GlobalObjId", "ThreadTopic",
                    "MIMEData", "MIMETruncated", "MIMESize", "InternetCPID", "Flag", "FlagStatus",
                    "EmailContentClass", "FlagType", "CompleteTime", "DisallowNewTimeProposal"
            },
            {
                    // change@pguha start
                    // 0x03 AirNotify
                    "Notify", "Notification", "version", "lifeTime", "deviceInfo", "enable",
                    "folder", "serverId", "deviceAddress", "validCarrierProfiles",
                    "carrierProfile", "status", "responses", "devices", "device", "id", "expiry",
                    "notifyGUID", "devideFriendlyName" // change@pguha: Add
            // missing tags
            // change@pguha end
            },
            {
                    // 0x04 Calendar
                    "CalTimeZone", "CalAllDayEvent", "CalAttendees", "CalAttendee",
                    "CalAttendee_Email", "CalAttendee_Name", "CalBody", "CalBodyTruncated",
                    "CalBusyStatus", "CalCategories", "CalCategory", "CalCompressed_RTF",
                    "CalDTStamp", "CalEndTime", "CalExeption", "CalExceptions",
                    "CalException_IsDeleted", "CalException_StartTime", "CalLocation",
                    "CalMeetingStatus", "CalOrganizer_Email", "CalOrganizer_Name", "CalRecurrence",
                    "CalRecurrence_Type", "CalRecurrence_Until", "CalRecurrence_Occurrences",
                    "CalRecurrence_Interval", "CalRecurrence_DayOfWeek",
                    "CalRecurrence_DayOfMonth", "CalRecurrence_WeekOfMonth",
                    "CalRecurrence_MonthOfYear", "CalReminder_MinsBefore", "CalSensitivity",
                    "CalSubject", "CalStartTime", "CalUID", "CalAttendee_Status",
                    "CalAttendee_Type", "CalAttachment", "CalAttachments", "CalAttName",
                    "CalAttSize", "CalAttOid", "CalAttMethod", "CalAttRemoved", "CalDisplayName",
                    "CalDisallowNewTimeProposal", "CalResponseRequested",
                    "CalAppointmentReplyTime", "CalResponseType", "CalCalendarType",
                    "CalIsLeapMonth", "FirstDayOfWeek", "OnlineMeetingInternalLink",
                    "OnlineMeetingExternalLink"
            },
            {
                    // 0x05 Move
                    "MoveItems", "Move", "SrcMsgId", "SrcFldId", "DstFldId", "MoveResponse",
                    "MoveStatus", "DstMsgId"
            },
            {
                    // 0x06 ItemEstimate
                    "GetItemEstimate", "Version", "Collection", "Collection", "Class",
                    "CollectionId", "DateTime", "Estimate", "Response", "Status"
            },
            {
                    // 0x07 FolderHierarchy
                    "Folders", "Folder", "FolderDisplayName", "FolderServerId", "FolderParentId",
                    "Type", "FolderResponse", "FolderStatus", "FolderContentClass", "Changes",
                    "FolderAdd", "FolderDelete", "FolderUpdate", "FolderSyncKey",
                    "FolderFolderCreate", "FolderFolderDelete", "FolderFolderUpdate", "FolderSync",
                    "Count", "FolderVersion"
            },
            {
                    // 0x08 MeetingResponse
                    "CalId", "CollectionId", "MeetingResponse", "ReqId", "Request", "Result",
                    "Status", "UserResponse", "Version"
            },
            {
                    // 0x09 Tasks
                    "Body", "BodySize", "BodyTruncated", "Categories", "Category", "Complete",
                    "DateCompleted", "DueDate", "UTCDueDate", "Importance", "Recurrence",
                    "RecurrenceType", "RecurrenceStart", "RecurrenceUntil",
                    "RecurrenceOccurrences", "RecurrenceInterval", "RecurrenceDOM",
                    "RecurrenceDOW", "RecurrenceWOM", "RecurrenceMOY", "RecurrenceRegenerate",
                    "RecurrenceDeadOccur", "ReminderSet", "ReminderTime", "Sensitivity",
                    "StartDate", "UTCStartDate", "Subject", "CompressedRTF", "OrdinalDate",
                    "SubordinalDate", "CalendarType", "IsLeapMonth", "FirstDayOfWeek"
            },
            {
                    // change@pguha start
                    // 0x0A ResolveRecipients
                    "resolveRecipients", "response", "status", "type", "recipient", "displayName",
                    "emailAddress", "certificates", "certificate", "miniCertificate", "options",
                    "to", "certificateRetrieval", "recipientCount", "maxCertificates",
                    "maxAmbigiousRecipients", "certificateCount",
                    // change@wtl.kirill.k ResolveRecipients start
                    "Availability", "StartTime", "EndTime", "MergedFreeBusy", "Picture", "MaxSize",
                    "Data", "MaxPictures"
            // change@wtl.kirill.k ResolveRecipients end
            // change@pguha end
            },
            {
                    // change@pguha start
                    // 0x0B ValidateCert
                    "validateCert", "certificates", "certificate", "certificateChain", "checkCRL",
                    "status"
            // change@pguha end
            },
            {
                    // 0x0C Contacts2
                    "CustomerId", "GovernmentId", "IMAddress", "IMAddress2", "IMAddress3",
                    "ManagerName", "CompanyMainPhone", "AccountName", "NickName", "MMS"
            },
            {
                    // 0x0D Ping
                    "Ping", "AutdState", "PingStatus", "HeartbeatInterval", "PingFolders",
                    "PingFolder", "PingId", "PingClass", "MaxFolders"
            },
            {
                    // 0x0E Provision
                    "Provision", "Policies", "Policy", "PolicyType", "PolicyKey", "Data", "Status",
                    "RemoteWipe", "EASProvidionDoc", "DevicePasswordEnabled",
                    "AlphanumericDevicePasswordRequired", "DeviceEncryptionEnabled",
                    "PasswordRecoveryEnabled", "-unused-", "AttachmentsEnabled",
                    "MinDevicePasswordLength", "MaxInactivityTimeDeviceLock",
                    "MaxDevicePasswordFailedAttempts", "MaxAttachmentSize",
                    "AllowSimpleDevicePassword", "DevicePasswordExpiration",
                    "DevicePasswordHistory", "AllowStorageCard", "AllowCamera",
                    "RequireDeviceEncryption", "AllowUnsignedApplications",
                    "AllowUnsignedInstallationPackages", "MinDevicePasswordComplexCharacters",
                    "AllowWiFi", "AllowTextMessaging", "AllowPOPIMAPEmail", "AllowBluetooth",
                    "AllowIrDA", "RequireManualSyncWhenRoaming", "AllowDesktopSync",
                    "MaxCalendarAgeFilder", "AllowHTMLEmail", "MaxEmailAgeFilter",
                    "MaxEmailBodyTruncationSize", "MaxEmailHTMLBodyTruncationSize",
                    "RequireSignedSMIMEMessages", "RequireEncryptedSMIMEMessages",
                    "RequireSignedSMIMEAlgorithm", "RequireEncryptionSMIMEAlgorithm",
                    "AllowSMIMEEncryptionAlgorithmNegotiation", "AllowSMIMESoftCerts",
                    "AllowBrowser", "AllowConsumerEmail", "AllowRemoteDesktop",
                    "AllowInternetSharing", "UnapprovedInROMApplicationList", "ApplicationName",
                    "ApprovedApplicationList", "Hash"
            },
            {
                    // 0x0F Search
                    "Search", "Stores", "Store", "Name", "Query", "Options", "Range", "Status",
                    "Response", "Result", "Properties", "Total", "EqualTo", "Value", "And", "Or",
                    "FreeText", "SubstringOp", "DeepTraversal", "LongId", "RebuildResults",
                    "LessThan", "GreateerThan", "Schema", "Supported"
                    // change@siso.vpdj start
                    , "UserName", "Password", "ConversationId", "Picture", "MaxSize", "MAxPictures"
            // change@siso.vpdj start
            },
            {
                    // 0x10 Gal
                    "GalDisplayName", "GalPhone", "GalOffice", "GalTitle", "GalCompany",
                    "GalAlias", "GalFirstName", "GalLastName", "GalHomePhone", "GalMobilePhone",
                    "GalEmailAddress", "Picture", "Status", "Data"
            },
            {
                    // 0x11 AirSyncBase
                    "BodyPreference", "BodyPreferenceType", "BodyPreferenceTruncationSize",
                    "AllOrNone", "--unused--", "BaseBody", "BaseData", "BaseEstimatedDataSize",
                    "BaseTruncated", "BaseAttachments", "BaseAttachment", "BaseDisplayName",
                    "FileReference", "BaseMethod", "BaseContentId", "BaseContentLocation",
                    "BaseIsInline", "BaseNativeBodyType", "BaseContentType"
                    // change@siso.vpdj Conversation Text preview start
                    , "BasePreview", "BodyPartPreference", "BodyPart", "Status"
            // change@siso.vpdj Conversation Text preview end
            },
            {
                    // 0x12 Settings
                    // change@wtl.akulik start IT Policy 12.0
                    "Settings",
                    "Status",
                    "Get",
                    "Set", // change@pguha: Add missing tags
                    "oof", "oofState", "startTime", "endTime", "oofMessage", "appliedToInternal",
                    "appliesToExtKnown", "appliesToExtUnknown", "enabled", "replyMessage",
                    "bodyType", "devicePassword",
                    "password",
                    "deviceInformation",
                    "model",
                    "IMEI",
                    "friendlyName",
                    "OS",
                    "OSLanguage",
                    "phoneNumber",
                    "userInformation",
                    "emailAddress",
                    "SMTPAddress"
                    // change@wtl.akulik end
                    // change@wtl.kSingh - send deviceInformation During initial
                    // Sync
                    , "UserAgent", "outBoundSms", "networkOperator", "PrimarySmtpAddress",
                    "Accounts", "Account", "AccountId", "AccountName", "UserDisplayName",
                    "SendDisabled",
                    "ihsManagementInformation"
                    // chnage@siso.Spoorti IRM 14.1 starts
                    , "primarySmtpAddress", "Accounts", "Account", "AccountId", "AccountName",
                    "UserdisplayName", "sendDisabled", "", "rightsManagementsInformation"
            // chnage@siso.Spoorti IRM 14.1 ends
            },
            {
                    // change@wtl.ksingh - bug - DocumentLibrary does not work
                    // when logging is enabled - starts
                    // 0x13 DocumentLibrary
                    "linkId", "displayName", "isFolder", "creationDate", "lastModifiedDate",
                    "ishidden", "contentLength", "contentType"
            // change@wtl.ksingh - bug - DocumentLibrary does not work when
            // logging is enabled - ends
            },
            {
                    // 0x14 ItemOperations
                    // change@pguha start
                    "ItemOperations", "Fetch", "Store", "Options", "Range", "Total", "Properties",
                    "Data", "Status", "Response", "Version", "Schema", "Part",
                    "EmptyFolderContents", "DeleteSubFolders",
                    // change@siso.vpdj start
                    "UserName", "Password", "Move", "DstFldId", "ConversationId", "MoveAlways"
            // change@siso.vpdj end
            // change@pguha end
            },
            // change@wtl.pkijowski 14.0
            // change@wtl.lohith SmartReply/SmartForward 14.0 feature
            // start...ComposeMail code page contents
            {
                    "SendMail", "SmartForward", "SmartReply", "SaveInSentItems", "ReplaceMime",
                    "Type", "Source", "FolderId", "ItemId", "LongId", "InstanceId", "MIME",
                    "ClientId", "Status", "AccountId"
            },
            // change@wtl.lohith SmartReply/SmartForward 14.0 feature end
            {
                    // 0x16 Email2
                    "UmCallerID", "UmUserNotes", "UmAttDuration", "UmAttOrder", "ConversationId",
                    "ConversationIndex", "LastVerbExecuted", "LastVerbExecutionTime",
                    "ReveivedAsBcc", "Sender", "CalendarType", "IsLeapMonth", "AccountId",
                    "FirstDayOfWeek", "MeetingMessageType"
            },

            // change @ siso , Mahsky, Notes Sync 14.0
            {
                    // 0x17 Notes
                    "Subject", "MessageClass", "LastModifiedDate", "Categories", "Category"

            },
            // change @ siso , Mahsky, Notes Sync 14.0
            {
                    "RightsManagementSupport", "RightsManagementTemplates",
                    "RightsManagementTemplate", "RightsManagementLicense", "EditAllowed",
                    "ReplyAllowed", "ReplyAllAllowed", "ForwardAllowed", "ModifyRecipientsAllowed",
                    "ExtractAllowed", "PrintAllowed", "ExportAllowed", "ProgrammaticAccessAllowed",
                    "RMOwner", "ContentExpiryDate", "TemplateID", "TemplateName",
                    "TemplateDescription", "ContentOwner", "RemoveRightsManagementDistribution"
            },
    };
}
