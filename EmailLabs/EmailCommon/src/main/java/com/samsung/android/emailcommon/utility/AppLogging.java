
package com.samsung.android.emailcommon.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.samsung.android.emailcommon.EmailPackage;
import com.samsung.android.emailcommon.Preferences;
import com.samsung.android.emailcommon.provider.AccountValues;
import com.samsung.android.emailcommon.provider.EmailContent;
import com.samsung.android.emailcommon.provider.EmailContent.Account;
import com.samsung.android.emailcommon.provider.EmailContent.BlackListColumns;
import com.samsung.android.emailcommon.provider.EmailContent.HostAuth;
import com.samsung.android.emailcommon.provider.EmailContent.Mailbox;
import com.samsung.android.emailcommon.system.CarrierValues;
import com.samsung.android.emailcommon.variant.CommonDefs;
import com.samsung.android.feature.FloatingFeature;

public class AppLogging {
    private static final String TAG = "AppLogging";

    public static final String APPLOGGING_APP_ID = EmailPackage.PKG_PROVIDER;

    public final static String USE_APP_FEATURE_SURVEY = "com.samsung.android.providers.context.log.action.USE_APP_FEATURE_SURVEY";
    //public final static String USE_MULTI_APP_FEATURE_SURVEY = "com.samsung.android.providers.context.log.action.USE_MULTI_APP_FEATURE_SURVEY";
    public final static String REPORT_MULTI_APP_STATUS_SURVEY = "com.samsung.android.providers.context.log.action.REPORT_MULTI_APP_STATUS_SURVEY";

    public static final String APPLOGGING_FEATURE_ADD_ACCOUNT = "ADDA";
    public static final String APPLOGGING_FEATURE_ADD_EASACCOUNT = "ADDE";
    public static final String APPLOGGING_FEATURE_STARRED = "STAR";
    public static final String APPLOGGING_FEATURE_SNAPVIEW = "SNVW";
    public static final String APPLOGGING_FEATURE_ADD_PRIORITY_SENDERS = "APRS";
    public static final String APPLOGGING_FEATURE_SENDERS_MENU = "AFSM";
    public static final String APPLOGGING_FEATURE_GOTO_NEXT_EMAIL = "GONE";
    public static final String APFPLOGGING_FEATURE_SAVE_AS_GROUP = "SAGP";
    public static final String APPLOGGING_FEATURE_COMPOSER_SWIPEDONW_LIST = "COSD";
    public static final String APPLOGGING_FEATURE_PROFILE_PICTURE = "PRPI";
    public static final String APPLOGGING_FEATURE_MARKAS_READ_UNREAD = "MARU";
    public static final String APPLOGGING_FEATURE_FAB_COMPOSER = "FABC";
    public static final String APPLOGGING_FEATURE_FAB_DRAFT = "FABD";
    public static final String APPLOGGING_FEATURE_LIST_SEARCH = "SEAR";

    public static final String APPLOGGING_FEATURE_SETTING_DISPLAY_AUTO_FIT_CONTENT = "DAFC";
    public static final String APPLOGGING_FEATURE_LIST_VIEWAS = "VIAS";
    public static final String APPLOGGING_FEATURE_LIST_TOP_LINE_INFO = "TLIN";
    public static final String APPLOGGING_FEATURE_EMAIL_NOTIFICATION = "EMNO";
    public static final String APPLOGGING_FEATURE_ACCOUNT_SPAM = "SPAM";
    public static final String APPLOGGING_FEATURE_SYNC_ACCOUNT = "SYAC";
    public static final String APPLOGGING_FEATURE_VIEW_SAVE_EMAIL = "VSEF";
    public static final String APPLOGGING_FEATURE_VIEW_SAVE_ATTACHMENT = "VSAF";
    public static final String APPLOGGING_FEATURE_VIEW_ATTACHMENT = "VVAF";
    public static final String APPLOGGING_FEATURE_VIEW_PRINT_EMAL = "VPEF";
    public static final String APPLOGGING_FEATURE_VIEW_ADD_EVENT = "VAEF";
    public static final String APPLOGGING_FEATURE_EAS_LICENSE = "EASL";
    public static final String APPLOGGING_FEATURE_VIEW_AIR_BROWSER_SCROLL = "VAIR";
    public static final String APPLOGGING_FEATURE_VIEW_SMART_SCROLL = "VSMA";

    public static final String APPLOGGING_FEATURE_COMPOSER_SPEN_ATTACHMENT_HOVERING = "CSAH";
    public static final String APPLOGGING_FEATURE_COMPOSER_SPEN_INSERT_IMAGE_HOVERING = "CSII";
    public static final String APPLOGGING_FEATURE_COMPOSER_SPEN_INSERT_RECIPIENT_HOVERING = "CSIH";

    public static final String APPLOGGING_FEATURE_SETTINGS = "SETS";
    public static final String APPLOGGING_FEATURE_SYNC_SETTINGS = "SYNC";
    public static final String APPLOGGING_FEATURE_ACCOUNT_SETTINGS = "ACCS";
    public static final String APPLOGGING_FEATURE_SECURITY_SETTINGS = "SECS";
    public static final String APPLOGGING_FEATURE_SERVER_SETTINGS = "SERS";
    public static final String APPLOGGING_FEATURE_SNAP_REPLY = "SNRE";
    public static final String APPLOGGING_FEATURE_SNAP_REMIND = "SNRM";
    public static final String APPLOGGING_FEATURE_SNAP_DELETE = "SNDE";

    public static final String APPLOGGING_FEATURE__VIEW_REMIND = "REMI";
    public static final String APPLOGGING_FEATURE_QUICK_RESPONSE_TEXTFIELD = "QRTF";
    public static final String APPLOGGING_FEATURE_UNREAD_FOLDER = "UNFO";
    public static final String APPLOGGING_FEATURE_REFRESH = "REFE";
    public static final String APPLOGGING_FEATURE_DELETE_PRIORITYSENDER = "DPRS";

    public static final String APPLOGGING_FEATURE_STATUS_ACCOUNT_NUMBER = "STAC";
    public static final String APPLOGGING_FEATURE_STATUS_ACCOUNT_NUMBER_E = "STAN";
    public static final String APPLOGGING_FEATURE_STATUS_ACCOUNT_GOOGLE_NUMBER = "STAG";

    public static final String APPLOGGING_FEATURE_STATUS_PRIORITYSENDERS = "STPC";
    public static final String APPLOGGING_FEATURE_STATUS_PRIORITY_NOTIFICATION = "STPN";
    public static final String APPLOGGING_FEATURE_STATUS_AUTOFIT = "STAF";
    public static final String APPLOGGING_FEATURE_STATUS_VIEWAS = "STVA";
    public static final String APPLOGGING_FEATURE_STATUS_SHOW_IMAGE = "STSI";
    public static final String APPLOGGING_FEATURE_VIEW_STARRED = "AFVS";
    public static final String APPLOGGING_FEATURE_STATUS_PEAK_SCHEDULE = "STPS";
    public static final String APPLOGGING_FEATURE_STATUS_COMPOSER_ENABLE_RTBAR = "STRT";
    public static final String APPLOGGING_FEATURE_STATUS_SPLIT_VIEW_MODE = "STVM";
    public static final String APPLOGGING_FEATURE_STATUS_PRIORITY_SENDER_DISPLAY = "STPD";


    public static final String APPLOGGING_FEATURE_STATUS_ACCOUNT_EMAIL_SERVICE_DOMAIN = "SASD";

    public static final String APPLOGGING_FEATURE_STATUS_SYNC_SCHEDULE = "STSC";
    public static final String APPLOGGING_FEATURE_STATUS_SYNC_WHILE_ROAMING = "STWR";
    public static final String APPLOGGING_FEATURE_STATUS_SET_PEAK_SCHEDULE = "STPK";
    public static final String APPLOGGING_FEATURE_STATUS_SET_PEROID_SYNC_EMAIL = "STPE";
    public static final String APPLOGGING_FEATURE_STATUS_LIMIT_SIZE = "STLR";
    public static final String APPLOGGING_FEATURE_STATUS_SIZE_ROAMING = "STSR";
    public static final String APPLOGGING_FEATURE_STATUS_SIGNATURE = "STST";
    public static final String APPLOGGING_FEATURE_STATUS_AUTOATTACHMENT = "STAT";

    public static final String APPLOGGING_FEATURE_ACCOUNT_SETUP_ACCOUNT_TYPE_SELECT = "ASSA";
    public static final String APPLOGGING_FEATURE_ACCOUNT_SETUP_MANUAL_SETUP = "ASBT";
    public static final String APPLOGGING_FEATURE_ACCOUNT_SETUP_ACCOUNT_TYPE = "ACTY";
    public static final String APPLOGGING_FEATURE_ACCOUNT_SETUP_EDIT = "AEDT";

    public static final String APPLOGGING_FEATURE_STATUS_REFRESH_FOLDERS = "STRF";
    public static final String APPLOGGING_FEATURE_STATUS_CONFIRM_DELETION = "STCD";


    //public static final String APPLOGGING_FEATURE_ACCOUNT_EMAIL_SERVICE_DOMAIN = "ACSD";

    public static final String APPLOGGING_FEATURE_LIST_PENHOVER_ACTIONBUTTON = "PHAB";
    public static final String APPLOGGING_FEATURE_LIST_FILTERBY = "FBOP";

    public static final String APPLOGGING_FEATURE_GOTO_PREV_EMAIL = "GTPE";
    public static final String APPLOGGING_FEATURE_REGISTER_SPAM = "RASP";
    public static final String APPLOGGING_FEATURE_REMOVE_SPAM = "RMSP";
    public static final String APPLOGGING_FEATURE_STATUS_RICH_TEXT = "STRT";
    public static final String APPLOGGING_FEATURE_VIEW_COMPOSE_NEWEMAIL = "VCNE";
    public static final String APPLOGGING_FEATURE_SEARCH_ON_SERVER = "STSS";

    public static final String APPLOGGING_FEATURE_REMINDER_EDIT = "EDRM";
    public static final String APPLOGGING_FEATURE_REMINDER_VIEW = "EDRV";
    public static final String APPLOGGING_FEATURE_FOLDER_SHOWHIDE = "FOSH";
    public static final String APPLOGGING_FEATURE_REMINDER_OFF = "DIRE";
    public static final String APPLOGGING_FEATURE_SELECTALL = "SEAL";

    public static final String APPLOGGING_FEATURE_COMPOSER_SAVING_TEMPORARILY = "TESA";

    public static final String APPLOGGING_FEATURE_COMPOSER_ATTACH = "COAT";
    public static final String APPLOGGING_FEATURE_COMPOSER_INSERT = "COIS";
    public static final String APPLOGGING_FEATURE_COMPOSER_RICHTEXT = "CORT";
    public static final String APPLOGGING_FEATURE_COMPOSER_SAVE_IN_DRAFTS = "COSD";
    public static final String APPLOGGING_FEATURE_COMPOSER_INCLUDE_PERVIOUS_RECIPIENTS = "CIPR";
    public static final String APPLOGGING_FEATURE_COMPOSER_INCLUDE_PERVIOUS_MESSAGES = "CIPM";
    public static final String APPLOGGING_FEATURE_COMPOSER_SEND_PRIORITY = "CSPR";
    public static final String APPLOGGING_FEATURE_COMPOSER_VIEW_CONTACT = "CVCT";

    public static final String APPLOGGING_FEATURE_PARSING_CALLNUMBER = "PSCN";
    
    public static final String APPLOGGING_FEATURE_SORT_BY = "SOBY";
    
    public static final String APPLOGGING_FEATURE_FOLDER_SHOWHIDE_STATE = "FSHS";
    
    public static final String APPLOGGING_FEATURE_FLICK_TO_LEFT_OR_RIGHT = "FTLR";
    
    public static final String APPLOGGING_FEATURE_LIST_SEARCH_WITH_FILTER = "LSWF";
    
    //deleted from ZERO
    // public static final String APPLOGGING_FEATURE_ACCOUNT_WELCOME = "WELC";  // deleted
    //  public static final String APPLOGGING_FEATURE_ACCOUNT_PRIORITY_SENDER = "PRIO";   //<-  APRS
    public static final String APPLOGGING_FEATURE_ACCOUNT_SYNCSCHEDULE = "SYNS";
    public static final String APPLOGGING_FEATURE_ACCOUNT_RINGTONE = "RING";  //deleted

    public static final String APPLOGGING_FEATURE_SETTING_PRIORITY_SENDER_DEFAULT_FOLDER = "PSDF";  // deleted
    public static final String APPLOGGING_FEATURE_SETTING_DISPLAY_MESSAGE_PREVIEW_LINE = "DMPL";  //deleted
    public static final String APPLOGGING_FEATURE_SETTING_DISPLAY_TITLE_LINE_IN_LIST = "DTLL";  //deleted
    public static final String APPLOGGING_FEATURE_SETTING_DISPLAY_HIDE_TICKBOXES = "DHTV";
    public static final String APPLOGGING_FEATURE_SETTING_DEFAULT_DISPLAY = "DEDP"; //deleted
    public static final String APPLOGGING_FEATURE_SETTING_DELAY_EMAIL_SENDING = "DESD"; //deleted

    public static final String APPLOGGING_FEATURE_LIST_FOLDER = "FOLD";  //deleted
    public static final String APPLOGGING_FEATURE_LIST_SHOW_ALL_FOLDERS = "SAFL";  //deleted

    public static final String APPLOGGING_FEATURE_COMPOSER_NEW = "SEND";   //deleted
    public static final String APPLOGGING_FEATURE_COMPOSER_REPLY = "SEND";  //deleted
    public static final String APPLOGGING_FEATURE_COMPOSER_FORWARD = "SEND";  //deleted    
    public static final String APPLOGGING_FEATURE_VIEW_VIEW = "VIEW";  //deleted

    public static final String APPLOGGING_FEATURE_VIEW_THREAD = "AFVT";
    public static final String APPLOGGING_FEATURE_VIEW_DELETE = "AFVD";
    public static final String APPLOGGING_FEATURE_VIEW_FORWARD = "AFVF";
    public static final String APPLOGGING_FEATURE_VIEW_REPLY = "AFVR";
    public static final String APPLOGGING_FEATURE_VIEW_REPLYALL = "AFVA";
    public static final String APPLOGGING_FEATURE_VIEW_DETAIL = "AFVE";
    public static final String APPLOGGING_FEATURE_ENTER_MAILBOX = "ENMA";
    public static final String APPLOGGING_FEATURE_FOLDER_IN_MAILBOX = "FIMA";
    public static final String APPLOGGING_FEATURE_DELETE_IN_EDITMODE = "DIEM";
    public static final String APPLOGGING_FEATURE_CONTINUE_SEARCH_ON_SERVER = "CSOS";
    public static final String APPLOGGING_FEATURE_ENTER_EDIT_MODE = "EEMO";
    public static final String APPLOGGING_FEATURE_ENTER_EMAIL_SETTING = "ENES";
    public static final String APPLOGGING_FEATURE_UNDO_ON_LIST_FLICK = "UOLF";
    public static final String APPLOGGING_FEATURE_COMPOSE_EVENT_INVITATION = "COEI";
    public static final String APPLOGGING_FEATURE_SEARCH_FOLDER_IN_SERVER = "SFIS";
    public static final String APPLOGGING_FEATURE_MOVE_EMAIL = "MOEM";
    public static final String APPLOGGING_FEATURE_CREATE_FOLDER = "CRFO";

    //deleted from ZERO

    public static final String READ = "READ";
    public static final String UNREAD = "Unread";
    public static final String CHECK = "check";
    public static final String UNCHECK = "uncheck";
    public static final String ON = "On";
    public static final String OFF = "Off";
    public static final String NONE = "NONE";
    public static final String LINE_1 = "1_LINE";
    public static final String LINES_2 = "2_LINES";
    public static final String LINES_3 = "3_LINES";
    public static final String SENDER = "SENDER";
    public static final String NEXT = "NEXT";
    public static final String PREVIOUS = "PREVIOUS";
    public static final String LIST = "List";
    public static final String EDIT_MODE = "Edit mode";

    public static final String EXTRA = "extra";
    public static final String VIEWER = "Viewer";
    public static final String SNAPVIEW = "SnapView";
    public static final String BUTTON = "Button";
    public static final String SWIPE = "Swipe";
    public static final String STANDARDVIEW = "Standard view";
    public static final String CONVERSATIONVIEW = "Conversation view";
    public static final String SUBJECTINFO = "Subject";
    public static final String SENDERINFO = "Sender";
    public static final String SETTINSYNC = "Setting sync";
    public static final String MAILBOXSNYC = "MAILBOX sync";
    public static final String SETSYNCSCHEDULE = "Set sync schedule";
    public static final String WHILEROAMING = "While roaming";
    public static final String PEAKSCHEDULEOFF = "Peak schedule OFF";
    public static final String SETPEAKSCHEDULE = "Set peak schedule";
    public static final String PRIODTOSYNCEMAIL = "Period to sync Email";
    public static final String LIMITRETRIEVALSIZE = "Limit retrieval size";
    public static final String ACCOUNTNAME = "Account name";
    public static final String YOURNAME = "Your name";
    public static final String PASSWORD = "Password";
    public static final String CCBCC = "Always Cc/Bcc myself";
    public static final String SIGNATURE = "Signature";
    public static final String SHOWIMAGESON = "Show images ON";
    public static final String AUTODOWNLOADATTACHMENTS = "Auto download attachments";
    public static final String INCOMINGSETTINGS = "Incoming settings";
    public static final String OUTGOINGSETTINGS = "Outcoming settings";
    public static final String EXCHANGESETTINGS = "Exchange settings";
    public static final String REPLY = "Reply";
    public static final String REPLY_ALL = "Reply All";
    public static final String FORWARD = "Forward";
    public static final String SETREMINDER = "Set Reminder";
    public static final String DELETE = "Delete";
    public static final String DETAILS = "Details";
    public static final String MOVE = "Move";
    public static final String STARREDFLAGGED = "Starred and flagged";
    public static final String DATE_MOST_RECENT = "Date_most recent";
    public static final String DATE_OLDEST = "Date_oldest";
    public static final String SENDER_A_TO_Z = "Sender_A to Z";
    public static final String SENDER_Z_TO_A = "Sender_Z to A";
    public static final String READ_UNREAD = "Read_Unread";
    public static final String STARRED = "Starred";
    public static final String ATTACHMENTS = "Attachments";
    public static final String PRIORITY = "Priority";
    public static final String REMINDER = "Reminder";
    public static final String SEARCHTODAY = "Today";
    public static final String SEARCH1DAY = "Last 24 hours";
    public static final String SEARCH1WEEK = "Last 7 days";
    public static final String SEARCH6MONTH = "Last 6 months";
    public static final String SEARCH1YEAR = "Last 12 months";
    public static final String SEARCH_CUSTOM = "Customize";

    public static final String SETUP_MANUAL = "Manual setup";
    public static final String SETUP_NEXT = "Next";
    public static final String SETUP_ADD_NEW_ACCOUNT = "Add new account";
    public static final String SETUP_SELECT_EXISTING_ACCOUNT = "Select existing account";

    public static final String SETUP_EXISTING_ACCOUNT = "Existing account";

    public static final String SETUP_INCOMING = "Incoming server settings";
    public static final String SETUP_OUTGOING = "Outgoing server settings";
    public static final String SETUP_SYNC_OPTION = "Sync options";
    public static final String SETUP_EDITNAMES = "Edit names";

    public static final String POP3 = "POP3";
    public static final String IMAP = "IMAP";
    public static final String EAS = "EAS";

    public static final String REMINDER_1HOUR = "0.In 1 hour";
    public static final String REMINDER_6HOUR = "1.In 6 hours";
    public static final String REMINDER_12HOUR = "2.In 12 hours";
    public static final String REMINDER_1DAY = "3.In 1 day";
    public static final String REMINDER_1WEEK = "4.In 1 week";

    public static final String PARSING_CALL = "Call";
    public static final String PARSING_SEND = "Send message";
    public static final String PARSING_ADDCONTACT = "Add to Contacts";
    public static final String PARSING_COPY = "Copy text";

    public static final String THREAD_VIEW = "Thread";
    public static final String MARK_UNREAD = "Mark as unread";
    public static final String SAVE_EMAIL= "Save email as file";
    public static final String EDIT_REMINDER= "Edit reminder";
    
    public static final String ALL = "All";
    public static final String FROM = "From";
    public static final String TO = "To";
    public static final String SUBJECT = "Subject";
    public static final String DRAFTS = "Drafts";
    public static final String PRIORITY_SENDERS = "Priority senders";
    public static final String REMINDERS = "Reminders";
    public static final String SAVED_EMAILS = "Saved emails";
    public static final String SENT = "Sent";
    public static final String SPAM = "Spam";
    public static final String CUSTOM_FOLDERS = "Custom folders";
    public static final String LIST_LONG_PRESS = "List Long press";
    public static final String EDIT_BUTTON = "Edit Button";
    public static final String ENTER_SETTINGS_IN_INBOX = "Enter Settings in Inbox";
    public static final String ENTER_SETTINGS_IN_MAILBOX = "Enter Settings in Mailbox";
    public static final String SET_PRIORITY = "Add to priority senders";
    public static final String REMOVE_PRIORITY = "Remove from VIPs";
    public static final String ADD_SPAM = "Register as spam";
    public static final String VIEW_CONTACT = "View contact";
    public static final String VIEW_ATTACHMENT = "Open Attachment";
    public static final String SAVE_ATTACHMENT = "Save Attachment";


    public static final String REMOVESPAMFROMLIST = "Remove from spam on List";
    public static final String REMOVESPAMFROMVIEWER = "Remove from spam on viewer";
    public static final String REMOVESPAMFROMSETTINGS = "Remove from spam on settings";
    public static final String PRIORITYSENDER = "priority sender";
    public static final String PRIORITYSENDERMORE = "More than 6 priority sender";

    public static final String MOVE_IN_EDIT_MODE = "Move in edit mode";
    public static final String MOVE_IN_VIEWER = "Move in Viewer";

    public static final long NoValueData = -1;

    private static FloatingFeature sFloatingFeatureInstance = null;
    private static boolean enableFloatingFeature = false;

    public static void insertLog(Context context, String appId, String feature) {
        insertLog(context, appId, feature, null, NoValueData);
    }

    public static void insertLog(Context context, String appId, String feature, String extra) {
        insertLog(context, appId, feature, extra, NoValueData);
    }

    public static boolean isEnableSurveyMode() {

        if (sFloatingFeatureInstance == null) {
            sFloatingFeatureInstance = FloatingFeature.getInstance();
            enableFloatingFeature = sFloatingFeatureInstance.getEnableStatus("SEC_FLOATING_FEATURE_CONTEXTSERVICE_ENABLE_SURVEY_MODE");
        }
        return enableFloatingFeature;
    }

    public static void insertLog(Context context, String appId, String feature, String extra, String value) {
        if (isEnableSurveyMode()) {
            try {
                // writes a log to ContentProvider
                ContentValues cv = new ContentValues();
                cv.put("app_id", appId);
                cv.put("feature", feature);
                if (extra != null) cv.put("extra", extra);
                if (value != null) cv.put("value", value);

                Intent broadcastIntent = new Intent();

                broadcastIntent.setAction(USE_APP_FEATURE_SURVEY);
                broadcastIntent.putExtra("data", cv);

                broadcastIntent.setPackage("com.samsung.android.providers.context");

                Log.d(TAG, "Logging   " + appId + " " + feature + "  " + extra);
                context.sendBroadcast(broadcastIntent);
            } catch (Exception ex) {
                Log.e(TAG, "Error while using insertLog");
                Log.e(TAG, ex.toString());
                ex.printStackTrace();
            }
        }
    }

    // Main Code to insert Log 
    static void insertLog(Context context, String appId, String feature, String extra, long value) {
        if (isEnableSurveyMode()) {

            try {
                // writes a log to ContentProvider
                ContentValues cv = new ContentValues();
                cv.put("app_id", appId);
                cv.put("feature", feature);
                if (extra != null) cv.put("extra", extra);
                if (value != NoValueData) cv.put("value", value);

                Intent broadcastIntent = new Intent();

                broadcastIntent.setAction(USE_APP_FEATURE_SURVEY);
                broadcastIntent.putExtra("data", cv);

                broadcastIntent.setPackage("com.samsung.android.providers.context");

                Log.d(TAG, "Logging   " + appId + " " + feature + "  " + extra);
                context.sendBroadcast(broadcastIntent);
            } catch (Exception ex) {
                Log.e(TAG, "Error while using insertLog");
                Log.e(TAG, ex.toString());
                ex.printStackTrace();
            }
        }
    }

    public static void insertMultipleStatusLog(Context context, String appId, ArrayList<String> features, ArrayList<String> extras, ArrayList<String> values) {
        if (isEnableSurveyMode()) {
            try {
                ContentValues[] cvs = new ContentValues[features.size()];

                for (int i = 0; i < features.size(); i++) {
                    cvs[i] = new ContentValues();
                    cvs[i].put("app_id", appId);
                    cvs[i].put("feature", features.get(i));
                    cvs[i].put("extra", extras.get(i));
                    cvs[i].put("value", values.get(i));
                }

                Intent broadcastIntent = new Intent();


                broadcastIntent.setAction(REPORT_MULTI_APP_STATUS_SURVEY);
                broadcastIntent.putExtra("data", cvs);

                broadcastIntent.setPackage("com.samsung.android.providers.context");
                Log.d(TAG, "Logging   " + appId + " " + features + "  " + extras + " " + values);
                context.sendBroadcast(broadcastIntent);
            } catch (Exception ex) {
                Log.e(TAG, "Error while using insertLog");
                Log.e(TAG, ex.toString());
                ex.printStackTrace();
            }
        }
    }

	public static void insertMultipleStatusLog(Context cxt) {

        final Context context = cxt;

        if( Utility.numOfAccount(cxt) == 0)
            return ;

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isEnableSurveyMode()) {

                    try {
                        ArrayList<String> features = new ArrayList<String>();
                        ArrayList<String> extras = new ArrayList<String>();
                        ArrayList<String> values = new ArrayList<String>();

                        /////////////  num of account        /////////////
                        int numAccount = Utility.numOfAccount(context);
                        features.add(0, APPLOGGING_FEATURE_STATUS_ACCOUNT_NUMBER);
                        extras.add(0, null);
                        values.add(0, Integer.toString(numAccount * 1000));

                        ///////////// numVip     /////////////
                        int numVip = getVipCount(context);
                        features.add(1, APPLOGGING_FEATURE_STATUS_PRIORITYSENDERS);
                        if(numVip >= 6)
                            extras.add(1, PRIORITYSENDERMORE);
                        else
                            extras.add(1, Integer.toString(numVip) + " " + PRIORITYSENDER);
                        values.add(1, null);

                        /////////////  autofit     /////////////
                        Preferences preference = Preferences.getPreferences(context);
                        boolean autofit = preference.getAutoFit();
                        features.add(2, APPLOGGING_FEATURE_STATUS_AUTOFIT);
                        extras.add(2, null);
                        if (autofit == true)
                            values.add(2, "1000 : ON");
                        else
                            values.add(2, "0 : OFF");


                        /////////////  viewas        /////////////
                        int viewas = preference.getViewMode();
                        features.add(3, APPLOGGING_FEATURE_STATUS_VIEWAS);
                        if (viewas == 0)
                            extras.add(3, STANDARDVIEW);
                        else
                            extras.add(3, CONVERSATIONVIEW);
                        values.add(3, null);


                        /////////////  show image    /////////////
                        features.add(4, APPLOGGING_FEATURE_STATUS_SHOW_IMAGE);
                        extras.add(4, null);
                        int numShowimage = getShowimage(context);
                        if (numShowimage > 0)
                            values.add(4, numShowimage + " : ON");
                        else
                            values.add(4, "0 : OFF");

                        /////////////  peak schedule      /////////////
                        features.add(5, APPLOGGING_FEATURE_STATUS_PEAK_SCHEDULE);
                        extras.add(5, null);
                        int numSync = getSyncSchedule(context);
                        if (numSync > 0)
                            values.add(5, numSync + " : ON");
                        else
                            values.add(5, "0 : OFF");

                        ///////////// numVip Notification     /////////////
                        Preferences pref = Preferences.getPreferences(context);
                        features.add(6, APPLOGGING_FEATURE_STATUS_PRIORITY_NOTIFICATION);
                        extras.add(6, null);
                        if (pref.getVIPNotification() == true)
                            values.add(6, "1000");
                        else
                            values.add(6, "0");

                        ///////////// SPAM     /////////////
                        int numBlackList = getNumBlacklistList(context);
                        features.add(7, APPLOGGING_FEATURE_ACCOUNT_SPAM);
                        extras.add(7, null);
                        if (numBlackList > 0)
                            values.add(7, "1000 : YES");
                        else
                            values.add(7, "0 : NO");


                        /////////////  composer rich tool bar enabled or disabled      /////////////
                        features.add(8, APPLOGGING_FEATURE_STATUS_COMPOSER_ENABLE_RTBAR);
                        extras.add(8, null);
                        boolean isToolbarDisabled = preference.getRichToolbarDisplayState();
                        if (isToolbarDisabled)
                            values.add(8, "0 : OFF");
                        else
                            values.add(8, "1000 : ON");

                        /////////////  Split view mode      /////////////
                        features.add(9, APPLOGGING_FEATURE_STATUS_SPLIT_VIEW_MODE);
                        extras.add(9, null);
                        boolean split = preference.getSplitMode();
                        if (split)
                            values.add(9, "1000");
                        else
                            values.add(9, "0");


                        /////////////  num of account        /////////////
                        int numofAccount = Utility.numOfAccount(context);
                        features.add(10, APPLOGGING_FEATURE_STATUS_ACCOUNT_NUMBER_E);
                        String accountStr = "";
                        if (numofAccount >= 5)
                            accountStr = "More than " + Integer.toString(numofAccount) + " accounts";
                        else if(numofAccount >= 2)
                            accountStr = Integer.toString(numofAccount) + " accounts";
                        else
                            accountStr = Integer.toString(numofAccount) + " account";
                        extras.add(10, accountStr);
                        values.add(10, null);


                        /////////////  num of Google account        /////////////
                        int numGoogleAccount = googleAccount(context);

                        features.add(11, APPLOGGING_FEATURE_STATUS_ACCOUNT_GOOGLE_NUMBER);
                        String googleStr = "";
                        if (numGoogleAccount >= 5)
                            googleStr = "More than " + Integer.toString(numGoogleAccount) + " accounts";
                        else if(numofAccount >= 2)
                            googleStr = Integer.toString(numGoogleAccount) + " accounts";
                        else
                            googleStr = Integer.toString(numGoogleAccount) + " account";
                        extras.add(11, googleStr);
                        values.add(11, null);

                        /////////////  Refresh folders On/Off       /////////////
                        boolean refreshOnOpen = preference.isRefreshOnOpen();
                        features.add(12, APPLOGGING_FEATURE_STATUS_REFRESH_FOLDERS);
                        extras.add(12, null);
                        if (refreshOnOpen)
                            values.add(12, "1000 : ON");
                        else
                            values.add(12, "0 : OFF");

                        /////////////  Confirm delections       /////////////
                        boolean confirmDeletion = preference.getDeleteEmailConfirm();
                        features.add(13, APPLOGGING_FEATURE_STATUS_CONFIRM_DELETION);
                        extras.add(13, null);
                        if (confirmDeletion)
                            values.add(13, "1000 : ON");
                        else
                            values.add(13, "0 : OFF");


                        //////////////////////////////////////////////////////////////////////////////////////////////
                        insertMultipleStatusLog(context, APPLOGGING_APP_ID, features, extras, values);
                    } catch (Exception ex) {
                        Log.e(TAG, "Error while using insertLog");
                        Log.e(TAG, ex.toString());
                        ex.printStackTrace();
                    }
                }
            }
        }).start();

        insertMultipleStatusLog_Account(context);
        insertMultipleStatusLog_Domain(context);
    }


    public static void insertMultipleStatusLog_Domain(Context cxt) {

        final Context context = cxt;
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (isEnableSurveyMode()) {

                    try {
                        ArrayList<String> features = new ArrayList<String>();
                        ArrayList<String> extras = new ArrayList<String>();
                        ArrayList<String> values = new ArrayList<String>();

                        /////////////  Service domain       /////////////
                        HashMap<String, Integer> domainList = new HashMap<String, Integer>();
                        domainList = serviceDomainAccount(context);

                        int i = 0;
                        for (Map.Entry<String, Integer> domain : domainList.entrySet()) {
                            StringBuffer domainbuf = new StringBuffer(domain.getKey());
                            features.add(i, APPLOGGING_FEATURE_STATUS_ACCOUNT_EMAIL_SERVICE_DOMAIN);
                            extras.add(i, domainbuf.append(" : ").append(domain.getValue()).toString());
                            values.add(i, null);
                            i++;
                        }
                        //////////////////////////////////////////////////////////////////////////////////////////////
                        insertMultipleStatusLog(context, APPLOGGING_APP_ID, features, extras, values);
                    } catch (Exception ex) {
                        Log.e(TAG, "Error while using insertLog");
                        Log.e(TAG, ex.toString());
                        ex.printStackTrace();
                    }
                }

            }
        }).start();
    }


    public static void insertMultipleStatusLog_Account(Context cxt) {

        final Context context = cxt;
        final long accId = EmailContent.Account.getDefaultAccountId(context);

        new Thread(new Runnable() {
            @Override
            public void run() {

                if (isEnableSurveyMode()) {

                    try {
                        ArrayList<String> features = new ArrayList<String>();
                        ArrayList<String> extras = new ArrayList<String>();
                        ArrayList<String> values = new ArrayList<String>();

                        Account account = Account.restoreAccountWithId(context, accId);

                        if (account == null)
                            return;

                        /////////////  Sync Schedule    /////////////
                        SyncScheduleData syncScheduleData = account
                                .getSyncScheduleData();

                        int syncSchedule = syncScheduleData.getOffPeakSchedule();
                        String SS = null;

                        if (syncSchedule == Mailbox.CHECK_INTERVAL_PUSH)
                            SS = "Auto";
                        else if (syncSchedule == Mailbox.CHECK_INTERVAL_NEVER)
                            SS = "Manual";
                        else if (syncSchedule == Mailbox.CHECK_INTERVAL_15_MINS)
                            SS = "Every 15 min";
                        else if (syncSchedule == 30)
                            SS = "Every 30 min";
                        else if (syncSchedule == Mailbox.CHECK_INTERVAL_1HOUR)
                            SS = "Every hour";
                        else if (syncSchedule == 120)
                            SS = "Every 2 hours";
                        else if (syncSchedule == 240)
                            SS = "Every 4 hours";
                        else if (syncSchedule == 720)
                            SS = "Every 12 hours";
                        else if (syncSchedule == 1440)
                            SS = "Once a day";
                        else
                            SS = "";

                        features.add(0, APPLOGGING_FEATURE_STATUS_SYNC_SCHEDULE);
                        extras.add(0, SS);
                        values.add(0, null);
                        //////////////////////////////////////////////////////////////////////////////////////////////

                        /////////////  Sync data while roaming  /////////////
                        int syncwhileRoaming = syncScheduleData.getRoamingSchedule();
                        SS = null;
                        if (syncwhileRoaming == Account.CHECK_ROAMING_MANUAL)
                            SS = "Manual";
                        else
                            SS = "Use above settings";

                        features.add(1, APPLOGGING_FEATURE_STATUS_SYNC_WHILE_ROAMING);
                        extras.add(1, SS);
                        values.add(1, null);
                        //////////////////////////////////////////////////////////////////////////////////////////////     


                        /////////////  Sync data peak   /////////////
                        int syncpeak = syncScheduleData.getPeakSchedule();
                        SS = null;
                        if (syncpeak == Mailbox.CHECK_INTERVAL_PUSH)
                            SS = "Auto";
                        else if (syncpeak == Mailbox.CHECK_INTERVAL_NEVER)
                            SS = "Manual";
                        else if (syncpeak == Mailbox.CHECK_INTERVAL_15_MINS)
                            SS = "Every 15 min";
                        else if (syncpeak == 30)
                            SS = "Every 30 min";
                        else if (syncpeak == Mailbox.CHECK_INTERVAL_1HOUR)
                            SS = "Every hour";
                        else if (syncpeak == 120)
                            SS = "Every 2 hours";
                        else if (syncpeak == 240)
                            SS = "Every 4 hours";
                        else if (syncpeak == 720)
                            SS = "Every 12 hours";
                        else if (syncpeak == 1440)
                            SS = "Once a day";
                        else {
                            if (syncpeak >= 60)
                                SS = "Every " + Integer.toString(syncpeak / 60) + " hours";
                            else
                                SS = "";
                        }
                        features.add(2, APPLOGGING_FEATURE_STATUS_SET_PEAK_SCHEDULE);
                        extras.add(2, SS);
                        values.add(2, null);
                        //////////////////////////////////////////////////////////////////////////////////////////////     


                        /////////////  Sync period to sync  /////////////
                        int syncperiod = account.getSyncLookback();
                        SS = null;
                        if (syncperiod == AccountValues.SyncWindow.SYNC_WINDOW_ALL)
                            SS = "All";
                        else if (syncperiod == AccountValues.SyncWindow.SYNC_WINDOW_1_DAY)
                            SS = "1 day";
                        else if (syncperiod == AccountValues.SyncWindow.SYNC_WINDOW_3_DAYS)
                            SS = "3 days";
                        else if (syncperiod == AccountValues.SyncWindow.SYNC_WINDOW_1_WEEK)
                            SS = "1 week";
                        else if (syncperiod == AccountValues.SyncWindow.SYNC_WINDOW_2_WEEKS)
                            SS = "2 weeks";
                        else if (syncperiod == AccountValues.SyncWindow.SYNC_WINDOW_1_MONTH)
                            SS = "1 month";
                        else
                            SS = "";

                        features.add(3, APPLOGGING_FEATURE_STATUS_SET_PEROID_SYNC_EMAIL);
                        extras.add(3, SS);
                        values.add(3, null);
                        //////////////////////////////////////////////////////////////////////////////////////////////     

                        /////////////  limit retrieval size   /////////////
                        int retrievalSize;
                        SS = null;
                        account.mHostAuthRecv = HostAuth.restoreHostAuthWithId(context, account.mHostAuthKeyRecv);

                        if (account.mHostAuthRecv != null && "eas".equals(account.mHostAuthRecv.mProtocol)) {
                            retrievalSize = account.getEmailSize();
                            if (CommonDefs.SUPPORTED_PROTOCOL_EX2003.equals(account.mProtocolVersion)) {
                                if (retrievalSize == -1)
                                    SS = "Use above settings";
                                else if (retrievalSize == 0)
                                    SS = "Headers only";
                                else if (retrievalSize == 1)
                                    SS = "4 KB";
                                else if (retrievalSize == 2)
                                    SS = "5 KB";
                                else if (retrievalSize == 3)
                                    SS = "7 KB";
                                else if (retrievalSize == 4)
                                    SS = "10 KB";
                                else if (retrievalSize == 5)
                                    SS = "20 KB";
                                else if (retrievalSize == 6)
                                    SS = "50 KB";
                                else if (retrievalSize == 7)
                                    SS = "100 KB";
                                else if (retrievalSize == 8)
                                    SS = "No limit";
                                else
                                    SS = "";
                            } else {
                                if (retrievalSize == -1)
                                    SS = "Use above settings";
                                else if (retrievalSize == 0)
                                    SS = "Headers only";
                                else if (retrievalSize == 1)
                                    SS = "0.5 KB";
                                else if (retrievalSize == 2)
                                    SS = "1 KB";
                                else if (retrievalSize == 3)
                                    SS = "2 KB";
                                else if (retrievalSize == 4)
                                    SS = "5 KB";
                                else if (retrievalSize == 5)
                                    SS = "10 KB";
                                else if (retrievalSize == 6)
                                    SS = "20 KB";
                                else if (retrievalSize == 7)
                                    SS = "50 KB";
                                else if (retrievalSize == 8)
                                    SS = "100 KB";
                                else if (retrievalSize == 9)
                                    SS = "No limit";
                                else
                                    SS = "";
                            }
                        } else {
                            retrievalSize = account.getEmailRetrieveSize();
                            if (retrievalSize == -1)
                                SS = "Use above settings";
                            else if (retrievalSize == 0)
                                SS = "Headers only";
                            else if (retrievalSize == AccountValues.SyncSize.MESSAGE_SIZE_2_KB)
                                SS = "2 KB";
                            else if (retrievalSize == 5120)
                                SS = "5 KB";
                            else if (retrievalSize == 10240)
                                SS = "10 KB";
                            else if (retrievalSize == AccountValues.SyncSize.MESSAGE_SIZE_20_KB)
                                SS = "20 KB";
                            else if (retrievalSize == AccountValues.SyncSize.EMAIL_SIZE_51200)
                                SS = "50 KB";
                            else if (retrievalSize == AccountValues.SyncSize.EMAIL_SIZE_102400)
                                SS = "100 KB";
                            else if (retrievalSize == 307200)
                                SS = "300 KB";
                            else if (retrievalSize == 1048576)
                                SS = "1 MB";
                            else if (retrievalSize == 1)
                                SS = "No limit";
                            else if (retrievalSize == 2)
                                SS = "No limit (including attachments)";
                            else
                                SS = "";
                        }


                        features.add(4, APPLOGGING_FEATURE_STATUS_LIMIT_SIZE);
                        extras.add(4, SS);
                        values.add(4, null);
                        //////////////////////////////////////////////////////////////////////////////////////////////     

                        ///////////// roaming limit retrieval size   /////////////
                        int roamingRetrievalSize;
                        SS = null;
                        if (account.mHostAuthRecv != null && "eas".equals(account.mHostAuthRecv.mProtocol)) {
                            roamingRetrievalSize = account.getRoamingEmailSize();
                            if (CommonDefs.SUPPORTED_PROTOCOL_EX2003.equals(account.mProtocolVersion)) {
                                if (roamingRetrievalSize == -1)
                                    SS = "Use above settings";
                                else if (roamingRetrievalSize == 0)
                                    SS = "Headers only";
                                else if (roamingRetrievalSize == 1)
                                    SS = "4 KB";
                                else if (roamingRetrievalSize == 2)
                                    SS = "5 KB";
                                else if (roamingRetrievalSize == 3)
                                    SS = "7 KB";
                                else if (roamingRetrievalSize == 4)
                                    SS = "10 KB";
                                else if (roamingRetrievalSize == 5)
                                    SS = "20 KB";
                                else if (roamingRetrievalSize == 6)
                                    SS = "50 KB";
                                else if (roamingRetrievalSize == 7)
                                    SS = "100 KB";
                                else if (roamingRetrievalSize == 8)
                                    SS = "No limit";
                                else
                                    SS = "";
                            } else {
                                if (roamingRetrievalSize == -1)
                                    SS = "Use above settings";
                                else if (roamingRetrievalSize == 0)
                                    SS = "Headers only";
                                else if (roamingRetrievalSize == 1)
                                    SS = "0.5 KB";
                                else if (roamingRetrievalSize == 2)
                                    SS = "1 KB";
                                else if (roamingRetrievalSize == 3)
                                    SS = "2 KB";
                                else if (roamingRetrievalSize == 4)
                                    SS = "5 KB";
                                else if (roamingRetrievalSize == 5)
                                    SS = "10 KB";
                                else if (roamingRetrievalSize == 6)
                                    SS = "20 KB";
                                else if (roamingRetrievalSize == 7)
                                    SS = "50 KB";
                                else if (roamingRetrievalSize == 8)
                                    SS = "100 KB";
                                else if (roamingRetrievalSize == 9)
                                    SS = "No limit";
                                else
                                    SS = "";
                            }
                        } else {
                            roamingRetrievalSize = account.getEmailRoamingRetrieveSize();
                            if (roamingRetrievalSize == -1)
                                SS = "Use above settings";
                            else if (roamingRetrievalSize == 0)
                                SS = "Headers only";
                            else if (roamingRetrievalSize == AccountValues.SyncSize.MESSAGE_SIZE_2_KB)
                                SS = "2 KB";
                            else if (roamingRetrievalSize == 5120)
                                SS = "5 KB";
                            else if (roamingRetrievalSize == 10240)
                                SS = "10 KB";
                            else if (roamingRetrievalSize == AccountValues.SyncSize.MESSAGE_SIZE_20_KB)
                                SS = "20 KB";
                            else if (roamingRetrievalSize == AccountValues.SyncSize.EMAIL_SIZE_51200)
                                SS = "50 KB";
                            else if (roamingRetrievalSize == AccountValues.SyncSize.EMAIL_SIZE_102400)
                                SS = "100 KB";
                            else if (roamingRetrievalSize == 307200)
                                SS = "300 KB";
                            else if (roamingRetrievalSize == 1048576)
                                SS = "1 MB";
                            else if (roamingRetrievalSize == 1)
                                SS = "No limit";
                            else if (roamingRetrievalSize == 2)
                                SS = "No limit (including attachments)";
                            else
                                SS = "";
                        }

                        features.add(5, APPLOGGING_FEATURE_STATUS_SIZE_ROAMING);
                        extras.add(5, SS);
                        values.add(5, null);

                        //////////////////////////////////////////////////////////////////////////////////////////////

                        /////////////   Signature   /////////////
                        boolean signatureOn = 0 != (account.getFlags() & Account.FLAGS_ADD_SIGNATURE);
                        features.add(6, APPLOGGING_FEATURE_STATUS_SIGNATURE);
                        extras.add(6, null);
                        if (signatureOn)
                            values.add(6, "1000");
                        else
                            values.add(6, "0");
                        //////////////////////////////////////////////////////////////////////////////////////////////

                        /////////////  auto attachment  /////////////
                        boolean autoDownload = account.getAutoDownload();
                        features.add(7, APPLOGGING_FEATURE_STATUS_AUTOATTACHMENT);
                        extras.add(7, null);
                        if (autoDownload)
                            values.add(7, "1000");
                        else
                            values.add(7, "0");
                        //////////////////////////////////////////////////////////////////////////////////////////////

                        insertMultipleStatusLog(context, APPLOGGING_APP_ID, features, extras, values);
                    } catch (Exception ex) {
                        Log.e(TAG, "Error while using insertLog");
                        Log.e(TAG, ex.toString());
                        ex.printStackTrace();
                    }
                }

            }
        }).start();
    }

    static public int getVipCount(Context context) {
        Cursor c = Utility.getVipListCursor(context);
        if (c == null) {
            return 0;
        }

        int prioryCount = c.getCount();
        if (!c.isClosed())
            c.close();
        return prioryCount;
    }

    static public int getShowimage(Context context) {

        int accNum = 0;
        int imageNum = 0;

        Cursor c = context.getContentResolver().query(EmailContent.Account.CONTENT_URI,
                EmailContent.Account.ID_PROJECTION, "emailAddress!='snc@snc.snc'", null, null);

        try {
            if (c == null) {
                return 0;
            }

            accNum = c.getCount();

            if (accNum == 0) {
                if(CarrierValues.DEFAULT_SHOWIMAGE)
                    return 1000;
                else
                    return 0;
            }

            while (c.moveToNext()) {
                long accId = c.getLong(EmailContent.Account.CONTENT_ID_COLUMN);
                Account account = Account.restoreAccountWithId(context, accId);
                if (account != null && account.getShowImage() == true)
                    imageNum++;
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error while using insertLog");
            Log.e(TAG, ex.toString());
            ex.printStackTrace();
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }

        return 1000 * imageNum / accNum;

    }


    static public int getSyncSchedule(Context context) {

        int accNum = 0;
        int syncNum = 0;

        Cursor c = context.getContentResolver().query(EmailContent.Account.CONTENT_URI,
                EmailContent.Account.ID_PROJECTION, "emailAddress!='snc@snc.snc'", null, null);

        try {
            if (c == null) {
                return 1000;
            }

            accNum = c.getCount();

            if (accNum == 0)
                return 1000;

            while (c.moveToNext()) {
                long accId = c.getLong(EmailContent.Account.CONTENT_ID_COLUMN);
                Account account = Account.restoreAccountWithId(context, accId);
                if(account != null) {
                    SyncScheduleData syncScheduleData;
                    syncScheduleData = account.getSyncScheduleData();

                    if (syncScheduleData.getIsPeakScheduleOn() == true)
                        syncNum++;
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error while using insertLog");
            Log.e(TAG, ex.toString());
            ex.printStackTrace();
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }

        return 1000 * syncNum / accNum;

    }

    private static int getNumBlacklistList(Context context) {

        int num = 0;
        final String[] BLACKLIST_PROJECTION = new String[]{
                EmailContent.RECORD_ID, BlackListColumns.USER_NAME, BlackListColumns.EMAIL_ADDRESS,
                BlackListColumns.IS_DOMAIN
        };

        Cursor c = context.getContentResolver().query(EmailContent.BlackList.CONTENT_URI,
                BLACKLIST_PROJECTION, null, null, BlackListColumns.USER_NAME);
        try {
            if (c != null && c.getCount() > 0)
                num = c.getCount();
            else
                num = 0;
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }

        return num;
    }


    private static int googleAccount(Context context) {
        String GOOGLE_DOMAIN_NAMES = "(?i)^(gmail.com|google.com|android.com|googlemail.com)";
        int googleNum = 0;
        Cursor c = null;
        try {
            if (context != null) {
                c = context.getContentResolver().query(EmailContent.Account.CONTENT_URI,
                        new String[]{EmailContent.Account.EMAIL_ADDRESS}, "emailAddress!='snc@snc.snc'", null, null);
                if (c == null)
                    return 0;
                if (c.getCount() == 0) {
                    if (c != null && !c.isClosed()) {
                        c.close();
                    }
                    return 0;
                }
                while (c.moveToNext()) {
                    String emailAddress = c.getString(0);
                    if (emailAddress != null) {
                        String email = emailAddress.trim();
                        if (email != null && !email.isEmpty()) {
                            String[] emailParts = email.split("@");
                            if (emailParts != null && emailParts.length > 1) {
                                String domain = emailParts[1];
                                if (!"".equalsIgnoreCase(domain) && domain.matches(GOOGLE_DOMAIN_NAMES)) {
                                    googleNum++;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error while using insertLog");
            Log.e(TAG, ex.toString());
            ex.printStackTrace();
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }
        return googleNum;
    }

    private static HashMap<String, Integer> serviceDomainAccount(Context context) {

        ArrayList<String> serviceDomain = new ArrayList<String>();
        HashSet<String> serviceD = new HashSet<String>();
        HashMap<String, Integer> domainList = new HashMap<String, Integer>();
        Cursor c = null;
        try {
            if (context != null) {
                c = context.getContentResolver().query(EmailContent.Account.CONTENT_URI,
                        new String[]{EmailContent.Account.EMAIL_ADDRESS}, "emailAddress!='snc@snc.snc'", null, null);
                if (c == null)
                    return domainList;
                if (c.getCount() == 0) {
                    if (c != null && !c.isClosed()) {
                        c.close();
                    }
                    return domainList;
                }

                while (c.moveToNext()) {
                    String emailAddress = c.getString(0);
                    if (emailAddress != null) {
                        String email = emailAddress.trim();
                        if (email != null && !email.isEmpty()) {
                            String[] emailParts = email.split("@");
                            if (emailParts != null && emailParts.length > 1) {
                                String domain = emailParts[1];
                                serviceDomain.add(domain);
                                serviceD.add(domain);
                            }
                        }
                    }
                }

                for (String domain : serviceD) {
                    int num = 0;
                    for (int i = 0; i < serviceDomain.size(); i++) {
                        if (domain.equalsIgnoreCase(serviceDomain.get(i)))
                            num++;
                    }
                    domainList.put(domain, num);
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error while using insertLog");
            Log.e(TAG, ex.toString());
            ex.printStackTrace();
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }

        return domainList;
    }

    public static String serviceDomain(String emailAddress) {
        String domain = "";
        try {
            if (emailAddress != null) {
                String email = emailAddress.trim();
                if (email != null && !email.isEmpty()) {
                    String[] emailParts = email.split("@");
                    if (emailParts != null && emailParts.length > 1) {
                        domain = emailParts[1];
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return domain;
    }


}

