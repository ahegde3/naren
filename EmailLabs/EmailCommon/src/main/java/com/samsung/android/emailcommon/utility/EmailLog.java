
package com.samsung.android.emailcommon.utility;

import com.samsung.android.emailcommon.EmailFeature;
import com.samsung.android.emailcommon.IntentConst;
import com.samsung.android.emailcommon.Preferences;

import android.content.Context;
import android.content.Intent;
import android.os.DropBoxManager;


public class EmailLog {

    public static final String LOG_TAG = "Email";

    /**
     * If this is enabled there will be additional logging information sent to
     * Log.d, including protocol dumps. This should only be used for logs that
     * are useful for debbuging user problems, not for internal/development
     * logs. This can be enabled by typing "debug" in the AccountFolderList
     * activity. Changing the value to 'true' here will likely have no effect at
     * all! TODO: rename this to sUserDebug, and rename LOGD below to DEBUG.
     */
    /**
     * Enable/disable printing stack trace.
     */
    public static boolean DEBUG_PRINT_STACKTRACE = false; // adapter_porting

    /**
     * If true, logging regarding activity/fragment lifecycle will be enabled.
     * Do not check in as "true".
     */
    public static boolean DEBUG_LIFECYCLE = false;

    /**
     * Stops the execution
     */
    public static final boolean DEBUG_ASSERT = true;

    /**
     * dumps the stack trace!
     */
    // public static final boolean DEBUG_SHOW_STACKTRACE =
    // EmailLog.LOGD_PRINT_STACKTRACE; // FIXME: move this here
    public static final boolean DEBUG_SHOW_STACKTRACE = true; // adapter_porting

    public static boolean LOG_STATS = false;
    public static final String TAG_STATS = "STATS";

    /**
     * Enable to print the POP mail body
     */
    public static boolean DEBUG_POP3_LOG_RAW_STREAM = false;

    /**
     * Enable to read/write logging for Legacy mail transport
     */
    public static boolean DEBUG_LEGACY_TRANSPORT = true;

    /**
     * Privacy. TODO refactor to DEBUG_SENSITIVE
     */
    public static final boolean DEBUG_LOG_PRIVACY = false;  /* NOTE Do not commit as true */
    public static String LOG_PRIVACY = "PRIVACY_LOGS_OMITTED";
    
    public static boolean DEBUG_LOG_DBOPS = false;

    public static final String ACCOUNT_SETUP_LEGACY = "AccountSetupLegacy";
    public static final String ACCOUNT_SETUP_EAS = "AccountSetupEAS";
    public static final String ACCOUNT_SETUP_OZ = "AccountSetupOz";
    public static final String ACCOUNT_SETUP_UTILS = "AccountSetupUtils";
    public static final String ACCOUNT_SETTINGS_LEGACY = "AccountSettingsLegacy";
    public static final String ACCOUNT_SETTINGS_OZ = "AccountSettingsOz";
    public static final String ACCOUNT_SETTINGS_EAS = "AccountSettingsEas";
    public static final String ACCOUNT_SETTINGS_UTILS = "AccountSettingsUtils";
    public static final String ACCOUNT_SETTINGS_BASE = "AccountSettingsBase";
    public static final String ACCOUNT_FOLDER_LIST = "AccountFolderList";
    public static final String GAL_ADDRESS_ADAPTER = "GalEmailAddressAdapter";
    public static final String IMAP_MESSAGE = "ImapMessage";
    public static final String MESSAGE_COMPOSE = "MessageCompose";
    
    public static boolean DEBUG_DEV = false;

    // The following two are for user logging (the second providing more detail)
    public static boolean DEBUG_MODE = false; // DO NOT CHECK IN WITH THIS SET TO  TRUE
    public static boolean DEBUG = false; // DO NOT CHECK IN WITH THIS SET TO  TRUE
    public static boolean USER_LOG = false; // DO NOT CHECK IN WITH THIS SET TO TRUE
    public static boolean PARSER_LOG = false; // DO NOT CHECK IN WITH THIS SET TO TRUE
    public static boolean FILE_LOG = false; // DO NOT CHECK IN WITH THIS SET TO TRUE
    public static boolean TIME_CHECK_LOG = false; // DO NOT CHECK IN WITH THIS SET TO TRUE
    public static boolean VIEW_FILE_LOG = false;// DO NOT CHECK IN WITH THIS SET TO TRUE
    public static boolean REFRESH_BODY_TEST_ENABLE = false;
    public static boolean DEBUG_IMAP_TRAFFIC_STATS = false;
    
    // Standard debugging
    public static final int DEBUG_BIT = 1;
    // Verbose (parser) logging
    public static final int DEBUG_VERBOSE_BIT = 2;
    // File (SD card) logging
    public static final int DEBUG_FILE_BIT = 4;
    // Enable strict mode
    public static final int DEBUG_ENABLE_STRICT_MODE = 8;
    
    public static final int DEBUG_TIME_CHECK_LOG_BIT = 16;
    public static final int DEBUG_VIEW_FILE_BIT = 32;
    public static final int DEBUG_REFRESH_BODY_ENABLE = 64;
    
    /**
     * Flag to control the logging of mail headers
     */
    public static boolean DEBUG_MIME = false;
    
    public static boolean LOGD = false;
    
    public static boolean WAIT_DEBUG = false; // DO NOT CHECK IN WITH THIS SET TO TRUE
    
    /**
     * Flag, whether to send the SNC SMS to self
     */
    public static boolean SEND_SNC_SMS = false;
    
    static public boolean mLeakDebug = false;
    
    static String packageName;
    
    public static long startTime = 0, prevTime = 0;
    public static long startDownTime = 0, prevDownTime = 0;
    public static long startOpenTime = 0, prevOpenTime = 0;
    
    
    public static long dbLoadingTime;
    public static long viewLoadingTime;
    public static long totalLoadingTime;
    
    
    // huijae.lee not used
    // private static String[] enabledUsers ={
    // ACCOUNT_SETUP_LEGACY,
    // ACCOUNT_SETUP_EAS,
    // ACCOUNT_SETUP_OZ,
    // ACCOUNT_SETUP_UTILS,
    // ACCOUNT_SETTINGS_LEGACY,
    // ACCOUNT_SETTINGS_OZ,
    // ACCOUNT_SETTINGS_EAS,
    // ACCOUNT_SETTINGS_UTILS,
    // ACCOUNT_SETTINGS_BASE,
    // ACCOUNT_FOLDER_LIST,
    // GAL_ADDRESS_ADAPTER,
    // IMAP_MESSAGE,
    // MESSAGE_COMPOSE
    // };

    public static void v(String tag, String message) {
        android.util.secutil.Log.v(tag, getCallerClassName() + message);
        if (FILE_LOG) {
            FileLogger.log(tag, getCallerClassName() + message);
        }


    }

    public static void v(Object caller, String tag, String message) {

    	android.util.secutil.Log.v(tag, "[" + caller.getClass().getSimpleName() + "] " + message);
        if (FILE_LOG) {
            FileLogger.log(tag, "[" + caller.getClass().getSimpleName() + "] " + message);
        }

    }

    public static void d(String tag, String message) {

    	android.util.secutil.Log.d(tag, getCallerClassName() + message);
        if (FILE_LOG) {
            FileLogger.log(tag, getCallerClassName() + message);
        }

        if (VIEW_FILE_LOG) {
            if (tag.contains("VIEW_EAS")) {
                ViewFileLogger.log(tag, message);                
            }
        }

    }

    public static void d(Object caller, String tag, String message) {

    	android.util.secutil.Log.d(tag, "[" + caller.getClass().getSimpleName() + "] " + message);
        if (FILE_LOG) {
            FileLogger.log(tag, "[" + caller.getClass().getSimpleName() + "] " + message);
        }

        if (VIEW_FILE_LOG) {
            if (tag.contains("VIEW_EAS")) {
                ViewFileLogger.log(tag, message);                
            }
        }

    }

    public static void i(String tag, String message) {

    	android.util.secutil.Log.i(tag, getCallerClassName() + message);
        if (FILE_LOG) {
            FileLogger.log(tag, getCallerClassName() + message);
        }

    }

    public static void i(Object caller, String tag, String message) {

    	android.util.secutil.Log.i(tag, "[" + caller.getClass().getSimpleName() + "] " + message);
        if (FILE_LOG) {
            FileLogger.log(tag, "[" + caller.getClass().getSimpleName() + "] " + message);
        }

    }

    public static void w(String tag, String message) {

    	android.util.secutil.Log.w(tag, getCallerClassName() + message);
        if (FILE_LOG) {
            FileLogger.log(tag, getCallerClassName() + message);
        }

    }

    public static void w(Object caller, String tag, String message) {

    	android.util.secutil.Log.w(tag, "[" + caller.getClass().getSimpleName() + "] " + message);
        if (FILE_LOG) {
            FileLogger.log(tag, "[" + caller.getClass().getSimpleName() + "] " + message);
        }

    }

    public static void e(String tag, String message) {
        // if(isLoggable(tag, Log.ERROR)) {
    	android.util.secutil.Log.e(tag, getCallerClassName() + message);
        // }
        if (FILE_LOG) {
            FileLogger.log(tag, getCallerClassName() + message);
        }

    }

    public static void e(String tag, String message, Throwable ex) {
        // if(isLoggable(tag, Log.ERROR)) {
    	android.util.secutil.Log.e(tag, getCallerClassName() + message, ex);
        // }
        if (FILE_LOG) {
            FileLogger.log(tag, getCallerClassName() + message);
            FileLogger.log(ex);
        }

    }

    public static void e(Object caller, String tag, String message) {
        // if(isLoggable(tag, Log.ERROR)) {
    	android.util.secutil.Log.e(tag, "[" + caller.getClass().getSimpleName() + "] " + message);
        // }
        if (FILE_LOG) {
            FileLogger.log(tag, "[" + caller.getClass().getSimpleName() + "] " + message);
        }

    }

    public static void e(Object caller, String tag, String message, Throwable ex) {
        // if(isLoggable(tag, Log.ERROR)) {
    	android.util.secutil.Log.e(tag, "[" + caller.getClass().getSimpleName() + "] " + message, ex);
        // }
        if (FILE_LOG) {
            FileLogger.log(tag, "[" + caller.getClass().getSimpleName() + "] " + message);
        }

    }

    public static void log(String message) {
    	android.util.secutil.Log.d(LOG_TAG, message);
    }
    
    static public void loge(String tag, String str) {
    	android.util.secutil.Log.e(LOG_TAG, tag + "\t" + str);
    }

    static public void loge(String tag, String str, Throwable tr) {
    	android.util.secutil.Log.e(LOG_TAG, tag + "\t" + str, tr);
    }

    static public void logd(String tag, String str) {
    	android.util.secutil.Log.d(LOG_TAG, tag + "\t" + str);
    }

    static public void logv(String tag, String str) {
    	android.util.secutil.Log.v(LOG_TAG, tag + "\t" + str);
    }

    static public void logs(String tag, String str) {
    	android.util.secutil.Log.v(LOG_TAG, "SOCKET " + tag + "\t" + str);
    }

    static public void logv2(String tag, String str) {
    	android.util.secutil.Log.v(tag, str);
    }
        
    static public void logv2(String tag, String format, Object... args) {
    	android.util.secutil.Log.v(tag, String.format(format, args));
    }

    static public void logd2(String tag, String str) {
    	android.util.secutil.Log.d(tag, str);
    }
        
    static public void logd2(String tag, String format, Object... args) {
    	android.util.secutil.Log.d(tag, String.format(format, args));
    }

    static public void logi2(String tag, String str) {
    	android.util.secutil.Log.i(tag, str);
    }
    
    static public void logi2(String tag, String format, Object... args) {
    	android.util.secutil.Log.i(tag, String.format(format, args));
    }
    
    static public void logw2(String tag, String str) {
    	android.util.secutil.Log.w(tag, str);
    }
        
    static public void logw2(String tag, String format, Object... args) {
    	android.util.secutil.Log.w(tag, String.format(format, args));
    }

    static public void loge2(String tag, String str) {
    	android.util.secutil.Log.e(tag, str);
    }
        
    static public void loge2(String tag, String format, Object... args) {
    	android.util.secutil.Log.e(tag, String.format(format, args));
    }
    
    static public void loge2(String tag, Throwable tr, String format, Object... args) {
        Log.e(tag, String.format(format, args), tr);
    }
    
    public static void stats(String message) {
        if (LOG_STATS)
            d(TAG_STATS, message);
    }

    private static void dumpStackTrace(String TAG, Exception e) {
        if (e == null)
            return;

        if (DEBUG_SHOW_STACKTRACE) {
            e.printStackTrace();
        } else {
            {
                EmailLog.d(TAG, e.toString());
            }
        }
        if (FILE_LOG) {
            FileLogger.log(e);
        }

    }

    public static void assertException(String TAG, Exception e, String msg) {
        if (null != msg) {
        	android.util.secutil.Log.e(TAG, msg);
        }

        assertException(TAG, e);
    }

    public static void assertException(String TAG, Exception e) {
        dumpStackTrace(TAG, e);

        if (DEBUG_ASSERT) {
            assert (false);
        }
    }

    public static void dumpException(String TAG, Exception e, String msg) {
        if (null != msg) {
        	android.util.secutil.Log.e(TAG, msg);
        }

        dumpException(TAG, e);
    }

    public static void dumpException(String TAG, Exception e) {
        dumpStackTrace(TAG, e);
    }

    private static String getCallerClassName() {
        //StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        // stack[0] = VmStack.getThreadStackTrace()
        // stack[1] = Thread.getStackTrace()
        // stack[2] = ImLog.getCallerClassName()
        // stack[3] = ImLog.v/d/i/w/e()
        // stack[4] = <actual caller class>
        //String callerClassName = stack[4].getFileName().substring(0,
        //        stack[4].getFileName().length() - 5 /* .java */);
        //return "[" + callerClassName + "] ";
        return "";
    }

    /** FIXME needs to be extended for all modules */
    // huijae.lee not used
    // private static boolean isLogEnabled(String tagName){
    // for(String temp:enabledUsers){
    // if(temp.equals(tagName))
    // return true;
    // }
    // return false;
    // }

    public static boolean isLoggable(String tag, int logLevel) {
        try {
            return (android.util.secutil.Log.isLoggable(tag, logLevel));
        } catch (Exception e) {
            // do nothing
            e.printStackTrace();
        }
        return false;
    }

    public static void w(String a, String b, Throwable e) {
    	android.util.secutil.Log.w(a, b, e);
    }

    public static void w(String a, Throwable e) {
    	android.util.secutil.Log.w(a, e);
    }

    public static void v(String a, String b, Throwable e) {
    	android.util.secutil.Log.v(a, b, e);
    }

    public static void d(String a, String b, Throwable e) {
    	android.util.secutil.Log.d(a, b, e);
    }

    public static String getStackTraceString(Throwable e) {
        return android.util.secutil.Log.getStackTraceString(e);
    }

    public static void i(String a, String b, Throwable e) {
    	android.util.secutil.Log.i(a, b, e);
    }

    public static void println(String a) {
    	android.util.secutil.Log.d("EmailLog", a);
    }
    
    /*
     * In order to store the critical events of Email persistently, 
     * DropBoxManager is used.
     * NOTE: Never dump too many easy-to-predict events here!
     */
    private static DropBoxManager mDbmInstance;
    
    public synchronized static void initLog(Context context) {
        if (mDbmInstance == null) {
        	android.util.secutil.Log.d("EmailLog", "dropboxManager instanciated");
            mDbmInstance = (DropBoxManager) context.getSystemService(Context.DROPBOX_SERVICE);
        }
    }
    
    public static void logToDropBox(String tag, String msg) {
        if (mDbmInstance != null) {
            mDbmInstance.addText(tag, msg);
        }
    }
    
    /**
     * Load enabled debug flags from the preferences and update the EAS debug
     * flag.
     */

    /**
     * Load enabled debug flags from the preferences and update the EAS debug
     * flag.
     */
    
    public static void updateLoggingFlags(Context context, Preferences prefs) {
        
        packageName = context.getPackageName();
        
        
        int debugLogging = prefs.getEnableDebugLogging() ? DEBUG_BIT : 0;
        int verboseLogging = prefs.getEnableExchangeLogging() ? DEBUG_VERBOSE_BIT
                : 0;
        int fileLogging = prefs.getEnableExchangeFileLogging() ? DEBUG_FILE_BIT
                : 0;

        int timeLogging = prefs.getEnableEASLoadmoreTimeLogging() ? DEBUG_TIME_CHECK_LOG_BIT
                : 0;
        int viewFileLogging = prefs.getEnableSaveViewLogLogging() ? DEBUG_VIEW_FILE_BIT
                : 0;
        int enableStrictMode = prefs.getEnableStrictMode() ? DEBUG_ENABLE_STRICT_MODE
                : 0;

        int enableRefreshBodyForTest = prefs.getEnableRefreshBodyMenu() ? DEBUG_REFRESH_BODY_ENABLE
                : 0;

        int debugBits = debugLogging | verboseLogging | fileLogging | enableStrictMode
                | timeLogging | viewFileLogging | enableRefreshBodyForTest;

        EmailLog.setUserDebug(debugBits, context);
        
        // save debugBits in prefs
        prefs.setDebugBits(debugBits);
        // send broadcast changed debugBits
        sendBroadcastIntent(context, IntentConst.ACTION_CHANGE_LOGGING, IntentConst.EXTRA_DEBUG_BITS, debugBits);
    }

    public static void setUserDebug(int state, Context context) {
        // DEBUG takes precedence and is never true in a user build
        if (!DEBUG_MODE) {
            DEBUG =  (state & DEBUG_BIT) != 0;
            USER_LOG = (state & DEBUG_BIT) != 0;
            LOGD = (state & DEBUG_BIT) != 0;
            DEBUG_LIFECYCLE = LOGD;
            PARSER_LOG = (state & DEBUG_VERBOSE_BIT) != 0;
            FILE_LOG = (state & DEBUG_FILE_BIT) != 0 &&
                    EmailRuntimePermission.hasPermissions(context, EmailRuntimePermission.PERMISSION_STORAGE);
            if (FILE_LOG || PARSER_LOG) {
                USER_LOG = true;
                LOGD = true;
            }

            if (!FILE_LOG) {
                FileLogger.close();
            }
            
            EmailLog.d(packageName, "Logging: " + (USER_LOG ? "User " : "")
                    + (PARSER_LOG ? "Parser " : "") + (FILE_LOG ? "File" : ""));
        }
        if ((DEBUG_TIME_CHECK_LOG_BIT & state) != 0) {
            TIME_CHECK_LOG = true;
        }

        if ((DEBUG_VIEW_FILE_BIT & state) != 0) {
            VIEW_FILE_LOG = true;
        }

        if ((DEBUG_REFRESH_BODY_ENABLE & state) != 0) {
            REFRESH_BODY_TEST_ENABLE = true;
        }

        if(context != null) {
            Preferences prefs = Preferences.getPreferences(context);
            if(prefs != null) {
                EmailFeature.DEBUG_MESSAGE_OPEN_TIME_CHECK = EmailFeature.DEBUG_MESSAGE_OPEN_TIME_CHECK
                        || prefs.getEnableViewEntrySpeedLogging();
                EmailFeature.DEBUG_ATTACHMENT_DOWNLOAD_TIME_CHECK = EmailFeature.DEBUG_ATTACHMENT_DOWNLOAD_TIME_CHECK
                        || prefs.getEnableDownloadSpeedLogging();
                EmailFeature.DEBUG_EML_OPEN_TIME_CHECK = EmailFeature.DEBUG_EML_OPEN_TIME_CHECK
                        || prefs.getEnableEMLFileEntrySpeedLogging();
                EmailFeature.DEBUG_WEBVIEW = EmailFeature.DEBUG_WEBVIEW
                        || prefs.getEnableWebSizeLogging();
                EmailFeature.DEBUG_SAVE_HTML = EmailFeature.DEBUG_SAVE_HTML
                        || prefs.getEnableSaveHtmlLogging();
                EmailFeature.DEBUG_WEBVIEW_JAVASCRIPT = EmailFeature.DEBUG_WEBVIEW_JAVASCRIPT
                        || prefs.getEnableWebviewInterfaceLogging();
                EmailFeature.DEBUG_WEBVIEW_JAVASCRIPT_DETAIL = EmailFeature.DEBUG_WEBVIEW_JAVASCRIPT_DETAIL
                        || prefs.getEnableWebviewInterfaceDetailLogging();
                EmailFeature.DEBUG_VIEW_LOADMORE_TIME = EmailFeature.DEBUG_VIEW_LOADMORE_TIME
                        || prefs.getEnableLoadmoreTimeLogging();
                EmailFeature.DEBUG_VIEW_LOADMORE_EAS_TIME = EmailFeature.DEBUG_VIEW_LOADMORE_EAS_TIME
                        || prefs.getEnableEASLoadmoreTimeLogging();
                EmailFeature.DEBUG_VIEW_SAVE_LOG_FILE = EmailFeature.DEBUG_VIEW_SAVE_LOG_FILE
                        || prefs.getEnableSaveViewLogLogging();
                SEND_SNC_SMS = SEND_SNC_SMS || prefs.getSmsForwardingStatus();

                EmailFeature.DEBUG_VIEW_REFRESH_BODY_MENU = EmailFeature.DEBUG_VIEW_REFRESH_BODY_MENU
                        || prefs.getEnableRefreshBodyMenu();

                EmailLog.VIEW_FILE_LOG = EmailFeature.DEBUG_VIEW_SAVE_LOG_FILE;
                EmailLog.REFRESH_BODY_TEST_ENABLE = EmailFeature.DEBUG_VIEW_REFRESH_BODY_MENU;
            }
        }
    }
    
    public static void setUserDebug(Context context) {
        
        packageName = context.getPackageName();
        Preferences prefs = Preferences.getPreferences(context);
        int debugBits = prefs.getDebugBits();
        setUserDebug(debugBits, context);
    }
    
    
    private static void sendBroadcastIntent(Context context, String action, String extra, int value) {
        Intent i = new Intent();
        i.setAction(action);
        if(extra != null) {
            i.putExtra(extra, value);
        }
        context.sendBroadcast(i);
    }
}
