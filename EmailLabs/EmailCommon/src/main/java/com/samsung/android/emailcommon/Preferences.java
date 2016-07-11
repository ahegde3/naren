/*
 * Copyright (C) 2008 The Android Open Source Project
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

package com.samsung.android.emailcommon;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.samsung.android.emailcommon.provider.EmailContent;
import com.samsung.android.emailcommon.system.AccountSetupCustomer;
import com.samsung.android.emailcommon.system.CarrierValues;
import com.samsung.android.emailcommon.utility.Log;
import com.samsung.android.emailcommon.utility.SecFeatureWrapper;
import com.samsung.android.emailcommon.utility.Utility;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

public class Preferences implements CarrierValues {

    private static String TAG = "Preferences";
    public static final String delim = "!#_";
    public static final String EMPTY_STRING = "EMPTY_STRING";
    public static final Uri CONTENT_URI = Uri
            .parse("content://com.samsung.android.email.preference.provider");

    final static public String VALUE_TYPE_BOOLEAN = "boolean";
    final static public String VALUE_TYPE_INTEGER = "integer";
    final static public String VALUE_TYPE_LONG = "long";
    final static public String VALUE_TYPE_STRING = "string";
    final static public String VALUE_TYPE_SIGNATURE = "signature";

    private static final String ACCOUNT_UUIDS = "accountUuids";

    private static final String VIP_UUIDS = "vipUuids";

    private static final String BLACK_LIST_UUIDS = "blackListUuids";

    // private static final String DEFAULT_ACCOUNT_UUID = "defaultAccountUuid";

    private static final String CARRIER_ACCOUNT_UUID = "carrierAccountUuid";

    private static final String DEBUG_BITS = "DebugBits";

    private static final String UI_ACCOUNT_CHANGED = "UiAccountChanged";

    private static final String ENABLE_DEBUG_LOGGING = "enableDebugLogging";

    private static final String ENABLE_SENSITIVE_LOGGING = "enableSensitiveLogging";

    private static final String ENABLE_EXCHANGE_LOGGING = "enableExchangeLogging";

    private static final String ENABLE_EXCHANGE_FILE_LOGGING = "enableExchangeFileLogging";

    private static final String ENABLE_WEBVIEW_SIZE_LOGGING = "enableWebviewSizeLogging";

    private static final String ENABLE_DOWNLOAD_SPEED_LOGGING = "enableDownloadSpeedLogging";

    private static final String ENABLE_VIEW_ENTRY_SPEED_LOGGING = "enableViewEntrySpeedLogging";

    private static final String ENABLE_EML_ENTRY_SPEED_LOGGING = "enableEMLEntrySpeedLogging";

    private static final String ENABLE_SAVE_HTML_LOGGING = "enableSaveHtmlLogging";

    private static final String ENABLE_WEBVIEW_INTERFACE_LOGGING = "enableWebviewInterfaceLogging";

    private static final String ENABLE_WEBVIEW_INTERFACE_DETATIL_LOGGING = "enableWebviewInterfaceDetailLogging";

    private static final String ENABLE_LOADMORE_TIME_LOGGING = "enableLoadmoreTimeLogging";

    private static final String ENABLE_EAS_LOADMORE_TIME_LOGGING = "enableEASLoadmoreTimeLogging";

    private static final String ENABLE_SAVE_VIEWLOG_FILE_LOGGING = "enableWebviewSave_View_Log_File_Logging";

    private static final String ENABLE_REFRESH_BODY_MENU = "enableRefreshBodyMenu";

    private static final String INHIBIT_GRAPHICS_ACCELERATION = "inhibitGraphicsAcceleration";

    private static final String FORCE_ONE_MINUTE_REFRESH = "forceOneMinuteRefresh";

    private static final String ENABLE_STRICT_MODE = "enableStrictMode";

    private static final String ENABLE_MAIL_HEADERS_LOGGING = "enableMailHeaderLogging";

    private static final String DEVICE_UID = "deviceUID";

    private static final String ONE_TIME_INITIALIZATION_PROGRESS = "oneTimeInitializationProgress";

    private static final String AUTO_ADVANCE_DIRECTION = "autoAdvance";

    //    private static final String TEXT_ZOOM = "textZoom";

    private static final String ENABLE_USE_SYSTEM_FONT = "useSystemFont";

    private static final String PREVIEW_LINE = "previewline";

    private static final String PREVIEW_LINE_EASYMODE = "previewline_easymode";

    private static final String ENABLE_EMAIL_SIMPLE_FEATURE = "emailsimplefeature";

    private static final String ENABLE_EMAIL_PLUS_SIMPLE_FEATURE = "emailplussimplefeature";

    private static final String ENABLE_LOCAL_TIME_LOGGING = "enableLocalTimeLogging";

    private static final String ENABLE_SMS_FORWARDING = "enableSmsForwarding";

    private static final String ENABLE_PANIC_ERROR_NOTIFIER = "enablePanicErrorNotifier";

    private static final String DISABLE_IMAP_SMART_SYNC = "disableImapSmartSync";

    private static final String ENABLE_IMAP_DRAFTS_SYNC = "enableImapDraftsSync";

    // sh2209.cho : Split mode
    private static final String SPLIT_MODE = "splitmode";

    // private static final String RECENT_MESSAGES = "recentmessages";

    // private static final String SHOW_IMAGE = "showimage";

    public static final String TOP_LINE_INFO_MODE = "toplinemode";

    private static final String DELETE_EMAIL_CONFIRM = "delete_email_confirm";

    private static final String AUTO_FIT = "auto_fit";

    private static final String VIEW_AUTOIMAGE = "view_autoimage";

    private static final String HIDE_CHECKBOXES = "hide_checkboxes";

    public static final String PREPERENCE_SAMSUNG_PERMISSION_STATE = "SamsungPermission_State";

    // sh2209.cho : Add VIP Contact
    private static final String MASTER_NOTIFICATION = "master_notification";
    private static final String VIP_DEFAULT_FOLDER = "vip_defaulte_folder";
    private static final String VIP_NOTIFICATION = "vip_notification";
    private static final String VIP_RINGTONE = "vip_ringtone";
    private static final String VIP_VIBRATE = "vip_vibrate";

    private static final String MAILBOX_ID = "mailboxId";

    private static final String ACCOUNT_ID = "accountId";
    private static final String ACCOUNT_ID2 = "accountId2";


    private static final String SELECTED_PRIORITY = "selected_priority_address";

    private static final String BACKGROUND_ATTACHMENTS = "backgroundAttachments";

    private static final String ACTIVATION_LICENSE = "activationLicense"; // SAMSUNG
    // ACTIVATION
    // -
    // reda.dehy

    // Motion tutorial by EJ
    private static final String MOTION_TUTORIAL_DOUBLE_TAP = "MotionTutorialDoubleTap";

    private static final String QUICK_RESPONSE_OPTION = "quickResponseOption";

    private static final String ONLINE_RECIPIENT_SEARCH = "online_recipient_search";

    // change@siso.gaurav cancel send start
    private static final String CANCEL_SENDING_MESSAGE = "cancel_sending_message";

    private static final String CANCEL_SENDING_MESSAGE_PERIOD = "cancel_sending_message_period";
    // change@siso.gaurav cancel send end

    private static final String DEFAULT_IMAGE_SIZE = "default_image_size";

    private static final String DEFAULT_RICH_TOOLBAR_DISPLAY_STATE = "default_rich_toolbar_display_state";

    private static final String EMAIL_COMPOSE_TIPS_SHOWING_STATE = "email_compose_tips_showing_state";

    private static final String EMAIL_MESSAGE_LIST_FIRST_SHOW_STATE = "email_message_list_first_show_state";    

    private static final String LAST_FOCUSED_EDITABLE_VIEW_NAME = "last_focused_editable_view_name";


    public static final String PREFERENCES_TEMP_MESSAGE_ID = "prefer_temp_message_id";

    public static final String PREFERENCES_TEMP_FWD_RECIPIENTS_CHECK = "prefer_temp_fwd_recipients_check";

    public static final String PREFERENCES_TEMP_FWD_TO_RECIPIENTS = "prefer_temp_fwd_to_recipients";
    public static final String PREFERENCES_TEMP_FWD_CC_RECIPIENTS = "prefer_temp_fwd_cc_recipients";

    public static final String PREFERENCES_TEMP_ACCOUNT_ID = "prefer_temp_account_id";

    public static final String PREFERENCES_EXTRA_FONT_SIZE = "extra_font_size";

    public static final String PREFERENCES_THREAD_VIEW_MODE = "prefer_view_mode";

    public static final String PREFERENCES_SORT_TYPE = "prefer_sort_type";

    public static final String PREFERENCES_RUN_FIRST = "prefer_run_first";
    
    public static final String PREFERENCES_LEFT_AND_MIDDLE_SPLIT_POINT = "left_and_middle_split";
    
    public static final String PREFERENCES_MIDDLE_AND_RIGHT_SPLIT_POINT = "middle_and_right_split";
    
    public static final String PREFERENCES_MIDDLE_AND_RIGHT_CONV_SPLIT_POINT = "middle_and_right_conv_split";
    
    public static final String PREFERENCES_RIGHT_CONV_AND_RIGHT_SPLIT_POINT = "right_and_right_conv_split";

    public static final String PREFERENCES_HIDE_ACCOUNT = "prefer_hide_account : ";

    public static final String PREFERENCES_CONTEXTUAL_CONTENTS = "contextual_contents";

    //    public static final String NOTI_PREF = "NotificationPreferences";

    //    public static final String NOTI_FAIL = "SendingFailNotification";

    public static final String PREFERENCES_DEBUG_IGNORE_DATA_CONNECTION = "debug_ignore_data_connection";

    public static final String PREFERENCES_DATA_ALLOW_PERMISSION = "DATAallowPermission"; // JMK
    // china
    // data
    // allow
    // permission

    // Information Security Authentication of China NAL Test start
    public static final String PREFERENCES_LOCATION_ALLOW_PERMISSION = "LocationAllowPermission";
    // Information Security Authentication of China NAL Test end

    //siso@abhirao change for MDM CBA cert installation starts
    public static final String PREFERENCE_MDM_CBA_CERT = "mdm_cba_cert";

    public static final String CBA_PASSWORD_TAG = "password";

    public static final String CBA_CERT_FILE_TAG = "file";
    public static final String PREFERENCE_MDM_SMIME = "mdm_smime_cert";

    public static final String PREFERENCE_MDM_SMIME_ENCRYPT = "mdm_smime_cert_encrypt";

    public static final String PREFERENCE_MDM_SMIME_SIGN = "mdm_smime_cert_sign";

    public static final String CERT_PATH = "file_path";

    public static final String CERT_PASSWORD = "password";

    public static final String CERT_RESULT_ID = "result_id";

    public static final String PREFERENCE_MDM_PUSHED_SMIME_ACCOUNT = "mdm_smime_accounts";
    //SRIB@abhilasha.hv change for MDM SMIME cert installation ends

    private static final String REFRESH_ON_OPEN = "refresh_on_open";

    /* ++ Cross App Feature ++ */
    private static final String PREFERENCE_ATTACH_SHEET_TAB_POSITION = "attach_sheet_tab_position";
    /* -- Cross App Feature -- */

    /* Black list function */
    private static final String PREFERENCE_DROP_DOWN_BLACK_LIST = "drop_down_black_list";
    /* Black list function */

    public static final String PREFERENCES_AUTO_SYNC_TIPS_CLOSED = "auto_sync_tips_closed";

    private static final String ATTACHMENT_STORAGE = "attachment_storage";

    private static final String SDCARD_STORAGE_STATUS = "sdcard_storage_status";

    private static final String UNTRUSTED_CERTIFICATE_FEATURE = "untrusted_certificate_feature";

    private static final String LAST_SYNC_TIME = "last_sync_time";

    private static final String UNREAD_COUNT = "unread_count";

    // OAuth related fields
    private static final String OAUTH_REFRESH_TOKEN = ".refresh_token";
    private static final String OAUTH_ACCESS_TOKEN = ".access_token";
    private static final String OAUTH_PROVIDER_ID = ".oauth_provider_id";
    private static final String OAUTH_TOKEN_EXPIRATION = ".token_expiration_time";


    // The following constants are used as offsets into TEXT_ZOOM_ARRAY (below)
    public static final int TEXT_ZOOM_TINY = 0;

    public static final int TEXT_ZOOM_SMALL = 1;

    public static final int TEXT_ZOOM_NORMAL = 2;

    public static final int TEXT_ZOOM_LARGE = 3;

    public static final int TEXT_ZOOM_HUGE = 4;

    public static final int TEXT_ZOOM_TINY_IN_APP = 5;

    public static final int TEXT_ZOOM_SMALL_IN_APP = 6;

    public static final int TEXT_ZOOM_NORMAL_IN_APP = 7;

    public static final int TEXT_ZOOM_LARGE_IN_APP = 8;

    public static final int TEXT_ZOOM_HUGE_IN_APP = 9;

    public static final int TEXT_ZOOM_TINY_H = 0;

    public static final int TEXT_ZOOM_TINY2_H = 1;

    public static final int TEXT_ZOOM_SMALL_H = 2;

    public static final int TEXT_ZOOM_SMALL2_H = 3;

    public static final int TEXT_ZOOM_NORMAL_H = 4;

    public static final int TEXT_ZOOM_LARGE_H = 5;

    public static final int TEXT_ZOOM_HUGE_H = 6;

    public static final int TEXT_ZOOM_TINY_IN_APP_H = 7;

    public static final int TEXT_ZOOM_TINY2_IN_APP_H = 8;

    public static final int TEXT_ZOOM_SMALL_IN_APP_H = 9;

    public static final int TEXT_ZOOM_SMALL2_IN_APP_H = 10;

    public static final int TEXT_ZOOM_NORMAL_IN_APP_H = 11;

    public static final int TEXT_ZOOM_LARGE_IN_APP_H = 12;

    public static final int TEXT_ZOOM_HUGE_IN_APP_H = 13;

    public static final int TEXT_ZOOM_TABLET_DEFAULT = TEXT_ZOOM_NORMAL;

    public static final boolean ENABLE_USE_SYSTEM_FONT_DEFAULT = false;

    // The following constants are used as offsets into PREVIEW_LINE_ARRAY
    // (below)
    public static final int PREVIEW_LINE_NONE = 0;

    public static final int PREVIEW_LINE_1 = 1;

    public static final int PREVIEW_LINE_2 = 2;

    public static final int PREVIEW_LINE_3 = 3;

    // "none" will be the default
    public static final int PREVIEW_LINE_DEFAULT = SecFeatureWrapper.getProductModel().contains(
            "9082") ? PREVIEW_LINE_NONE
                    : (SecFeatureWrapper.getCarrierId() == SecFeatureWrapper.CARRIER_USC ? PREVIEW_LINE_3
                            : PREVIEW_LINE_1);

    public static final int TOP_LINE_SUBJECT = 0;

    public static final int TOP_LINE_SENDER = 1;

    public static final int TOP_LINE_DEFAULT = CarrierValues.IS_CARRIER_ATT ? TOP_LINE_SUBJECT
            : TOP_LINE_SENDER;

    public static final boolean DELETE_EMAIL_CONFIRM_DEFAULT = EmailFeature
            .isEmailDeleteConfirmFeatureEnabled() ? true : false;

    public static final boolean ONLINE_RECIPIENT_SEARCH_DEFAULT = true;

    public static final boolean AUTO_FIT_DEFAULT = true;

    public static final long ACCOUNT_ID_DEFAULT = -1;

    public static final long MAILBOX_ID_DEFAULT = -1;


    public static final boolean TEMP_CHECK_FWD_RECIPIENTS_DEFAULT = false;

    public static final long TEMP_MESSAGE_ID_DEFAULT = -1;

    public static final String SELECTED_PRIORITY_DEFAULT = "";

    public static final long CANCEL_SENDING_MESSAGE_DEFAULT_PERIOD = 3000;

    private static final int DEFAULT_IMAGE_SIZE_DEFAULT = 4;

    private static final boolean DEFAULT_RICH_TOOLBAR_DISPLAY_STATE_DEFAULT = true;

    private static final String LAST_FOCUSED_EDITABLE_VIEW_NAME_DEFAULT = "";

    private static final boolean DEFAULT_CONTEXTUAL_CONTENTS_STATE_DEFAULT = false;

    private static final int DEFAULT_SORT_TYPE = 0;

    private static final int DEFAULT_VIEW_MODE = 0;

    private static final int DEFAULT_ATTACHMENT_STORAGE = 0;

    private static final String SHOW_WIFI_ONLY_DIALOG_FOR_ATTACTMANTS = "show_wifi_only_dialog_for_attachments";
    // Moved from AppPreferences

    public static final String KEY_PREF_ACCOUNT_SELECTED = "account_selected";

    final static private String PRIORITY_SENDER_GUIDE = "priority_sender_guide";

    /**
     * The maximum message size that we'll consider to be "small". A small
     * message is downloaded in full immediately instead of in pieces. Anything
     * over this size will be downloaded in pieces with attachments being left
     * off completely and downloaded on demand. 25k for a "small" message was
     * picked by educated trial and error.
     * http://answers.google.com/answers/threadview?id=312463 claims that the
     * average size of an email is 59k, which I feel is too large for our blind
     * download. The following tests were performed on a download of 25 random
     * messages.
     *
     * <pre>
     * 5k - 61 seconds,
     * 25k - 51 seconds,
     * 55k - 53 seconds,
     * </pre>
     *
     * So 25k gives good performance and a reasonable data footprint. Sounds
     * good to me. For HTML pages, 25K does not contain any body. Hence, changed
     * to 50K
     */
    // 60K is as per Sync and Connect!
    public static final int MAX_SMALL_MESSAGE_SIZE = (60 * 1024);

    // Even if the user presses, loadMore, the maximum size of a message that
    // shall be
    // fetched is 1MB
    public static final int MAX_FETCH_MESSAGE_SIZE = (1024 * 1024);

    // NOTE This is done for Sync and Connect account
    public static final int SNC_INITIAL_MAX_DOWNLOAD_MSG_COUNT = 100;

    /*
     * Everytime, when the user refreshes the mailbox, only the latest messages
     * upto this value will be updated. Not all!
     */
    public static final int DEFAULT_REFRESH_MESSAGE_COUNT = 100;

    /**
     * In order to balance the load, the client has to download the messaegs
     * from the server in batches.
     */
    public static final int MAX_MESSAGE_FETCH_FROM_SERVER = 25;

    // public static final boolean ENABLE_PUSH_FOR_LEGACY = false;
    // For SNC, getTags() API changes frequently. Hence, this flag decides
    // whether
    // to use getTags() API and its responses or only the default headers!
    // The impact is that the underlying the IMAP protocol should now change
    // the query and should update only Inbox for all stuffs!
    public static boolean ENABLE_SNC_GET_TAGS = false;
    /*
     * Sometimes, a message is received with tagId, for which no mailbox exists.
     * getTags() API might not have returned that folder. In that case, a
     * mailbox of this name shall be created dynamically or msg has to be
     * dropped!
     */
    // public static final boolean SNC_CREATE_MAILBOX_IF_NOT_EXISTS = false;
    /*
     * Close the connection, once it comes out!
     */
    public static final boolean SNC_REUSE_CONNECTION = false;

    public static boolean ENABLE_SNC_SMART_FORWARD = true;

//    public final String ID_TAG = "_ID";
//
//    public final String MSGID_TAG = "_MessageId";
//
//    public final String COUNT_TAG = "_Count";
//
//    public final String MSGCOUNT_TAG = "_MessageCount";
//
//    public final String STATE_TAG = "_State";
//
//    public final String SENDFAIL_TAG = "_SendFail";
//
//    public final String SFCOUNT_TAG = "_SendFailCount";
//
//    public final String SFTITLE_TAG = "_SendFailTitle";
//
//    public static final String NOTI_ACCOUNT_ID = "AccountID";
//
//    public static final String NOTI_MESSAGE_ID = "MessageID";

    // 130709, yh215.kim, save current and preset pen setting start
    public static final String SPEN_CURRENT_SPENSETTING = "current_spen";
    // public static final String SPEN_PRESET_COUNT = "spen_preset_count";
    public static final String SPEN_PRESET_ = "preset_";
    public static final String SPEN_NOTHING = "nothing";
    // 130709, yh215.kim, save current and preset pen setting end

    //    private static Preferences sPreferences;

    /*
     * When cache enabled, UI has better performance due to less bounding. but
     * cannot get new value after edited. So disable cache after complete
     * initializing.
     */
    
    private static Preferences sPrefs;
    
    private Map<String, String> mValues;

    private ContentResolver mResolver;
    public static class AccountNoti {
        public long accountId;

        public long lastestMsgId;

        public int messageCount;

        public int unseenMessageCount;
    }

    public static class SendFailNoti {
        public long accountId;

        public long[] messageId;

        public String[] title;
    }

    public static class MDMCBACertPref{
        public String uuid;
        public String password;
        public String fileName;
    }

    public static class MDMSMIMECertPref{

        public long accId;
        public String password;
        public String filePath;
        public long resultID;
    }

	private class preferenceObserver extends ContentObserver {
		public preferenceObserver() {
			super(null);
		}
		
		@Override
		public void onChange(boolean selfChange, Uri uri) {
         	fillValues(true);						
		}
	}
    
    private Preferences(Context context) {
        mResolver = context.getContentResolver();
        if(mResolver != null)
            mResolver.registerContentObserver(CONTENT_URI, true, new preferenceObserver());
    }

    /**
     * TODO need to think about what happens if this gets GCed along with the
     * Activity that initialized it. Do we lose ability to read Preferences in
     * further Activities? Maybe this should be stored in the Application
     * context.
     */
    public static synchronized Preferences getPreferences(Context context) {
        if(sPrefs == null) {
            sPrefs = new Preferences(context);
        	sPrefs.fillValues(true);
        }
        return sPrefs;
    }
    public static synchronized Preferences getNewPreferences(Context context) {
        sPrefs = new Preferences(context);
        sPrefs.fillValues(true);
        return sPrefs;
    }
    static private class ValueBuilder {
        ContentValues cv = new ContentValues();

        public void putBoolean(String key, boolean val) {
            String newKey = key + delim + VALUE_TYPE_BOOLEAN;
            cv.put(newKey, val);
        }

        public void putInt(String key, int val) {
            String newKey = key + delim + VALUE_TYPE_INTEGER;
            cv.put(newKey, val);
        }

        public void putLong(String key, long val) {
            String newKey = key + delim + VALUE_TYPE_LONG;
            cv.put(newKey, val);
        }

        public void putString(String key, String val) {
            String newKey = key;
            newKey = newKey + delim + VALUE_TYPE_STRING;
            cv.put(newKey, val);
        }

        public ContentValues get() {
            return cv;
        }

    }

    public void putValue(String key, boolean value) {
        try {
            ValueBuilder val = new ValueBuilder();
            val.putBoolean(key, value);
            mResolver.update(CONTENT_URI, val.get(), null, null);
            mValues.put(key, String.valueOf(value));
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "Preferences IllegalArgumentException " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void putValue(String key, int value) {
        try {
            ValueBuilder val = new ValueBuilder();
            val.putInt(key, value);
            mResolver.update(CONTENT_URI, val.get(), null, null);
            mValues.put(key, String.valueOf(value));
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "Preferences IllegalArgumentException " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void putValue(String key, long value) {
        try {
            ValueBuilder val = new ValueBuilder();
            val.putLong(key, value);
            mResolver.update(CONTENT_URI, val.get(), null, null);
            mValues.put(key, String.valueOf(value));
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "Preferences IllegalArgumentException " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void putValue(String key, String value) {
        try {
            ValueBuilder val = new ValueBuilder();
            val.putString(key, value);
            mResolver.update(CONTENT_URI, val.get(), null, null);
            mValues.put(key, String.valueOf(value));
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "Preferences IllegalArgumentException " + e.getMessage());
            e.printStackTrace();
        }
    }

    //    private void putValues(ValueBuilder vb) {
    //    	ContentValues cv = vb.get();
    //        mResolver.update(CONTENT_URI, cv, null, null);
    //        for (Entry<String, Object> e : cv.valueSet()) {
    //        	mValues.put(e.getKey(), String.valueOf(e.getValue()));
    //        }
    //    }

    private void removeValues(String... keys) {
        try {
            if (mResolver != null) {
                mResolver.delete(CONTENT_URI, null, keys);
                for (String k : keys) {
                    mValues.remove(k);
                }
            }
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "Preferences IllegalArgumentException " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Map<String, String> getAllValues() {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            Cursor c = null;
            QueryUtil qu = QueryUtil.createInstance(mResolver);
            try {
                c = qu.query(CONTENT_URI, new String[] {
                        "all"
                }, null);
                if(c != null){
                    while (c.moveToNext()) {
                        map.put(c.getString(0), c.getString(1));
                    }
                }
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "Preferences IllegalArgumentException " + e.getMessage());
            e.printStackTrace();
        }
        return map;
    }

    private void fillValues(boolean forceUpdate) {

        if (mValues != null && !forceUpdate)
            return;

        fillValues();
    }
    
    private synchronized void fillValues() {

        HashMap<String, String> values = new HashMap<String, String>();
        try {
            Log.d("Preferences", "gather prefs");
            Cursor c = null;
            try {
                c = mResolver.query(CONTENT_URI, new String[] {
                        "all"
                }, null, null, null);
                while (null != c && c.moveToNext()) {

                    values.put(c.getString(0), c.getString(1));
                }
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "Preferences IllegalArgumentException " + e.getMessage());
            e.printStackTrace();
        }
        mValues = values;
    }

    private boolean getValueBoolean(String key, boolean defVal) {
        boolean retVal = defVal;
        //        if (sCacheEnabled) {
        fillValues(false);
        Map<String, String> values = mValues;
        if (values.containsKey(key)) {
            retVal = Boolean.parseBoolean(values.get(key));
        }

        //        } else {
        //            String proj = key + delim + VALUE_TYPE_BOOLEAN + delim + defVal;
        //            Cursor c = null;
        //            QueryUtil qu = QueryUtil.createInstance(mResolver);
        //
        //            try {
        //                c = qu.query(CONTENT_URI, new String[] {
        //                    proj
        //                }, null);
        //                if (c != null && c.moveToFirst()) {
        //                    retVal = (c.getInt(0) == 1 ? true : false);
        //                }
        //            } catch (Exception ex) {
        //                ex.printStackTrace();
        //            } finally {
        //                if (c != null && !c.isClosed())
        //                    c.close();
        //            }
        //        }
        return retVal;
    }

    private int getValueInt(String key, int defVal) {
        int retVal = defVal;
        //        if (sCacheEnabled) {
        fillValues(false);
        Map<String, String> values = mValues;
        if (values.containsKey(key)) {
            retVal = Integer.parseInt(values.get(key));
        }
        //        } else {
        //            String proj = key + delim + VALUE_TYPE_INTEGER + delim + defVal;
        //            Cursor c = null;
        //            QueryUtil qu = QueryUtil.createInstance(mResolver);
        //
        //            try {
        //                c = qu.query(CONTENT_URI, new String[] {
        //                    proj
        //                }, null);
        //                if (c != null && c.moveToFirst()) {
        //                    retVal = c.getInt(0);
        //                }
        //            } finally {
        //                if (c != null && !c.isClosed())
        //                    c.close();
        //            }
        //        }
        return retVal;
    }

    private long getValueLong(String key, long defVal) {
        long retVal = defVal;
        //        if (sCacheEnabled) {
        fillValues(false);
        Map<String, String> values = mValues;
        if (values.containsKey(key)) {
            retVal = Long.parseLong(values.get(key));
        }
        //        } else {
        //            String proj = key + delim + VALUE_TYPE_LONG + delim + defVal;
        //            Cursor c = null;
        //            QueryUtil qu = QueryUtil.createInstance(mResolver);
        //
        //            try {
        //                c = qu.query(CONTENT_URI, new String[] {
        //                    proj
        //                }, null);
        //                if (c.moveToFirst()) {
        //                    retVal = c.getLong(0);
        //                }
        //            } finally {
        //                if (c != null && !c.isClosed())
        //                    c.close();
        //            }
        //        }
        return retVal;
    }

    private String getValueString(String key, String defVal) {
        String retVal = defVal;
        //        if (sCacheEnabled) {
        fillValues(false);
        Map<String, String> values = mValues;
        if (values.containsKey(key)) {
            retVal = values.get(key);
        }
        //        } else {
        //
        //            String proj = key + delim + VALUE_TYPE_STRING;
        //            if (defVal != null) {
        //                if (defVal.length() == 0) {
        //                    proj = proj + delim + EMPTY_STRING;
        //                } else {
        //                    proj = proj + delim + defVal;
        //                }
        //            }
        //            Cursor c = null;
        //            QueryUtil qu = QueryUtil.createInstance(mResolver);
        //
        //            try {
        //                c = qu.query(CONTENT_URI, new String[] {
        //                    proj
        //                }, null);
        //                if (c.moveToFirst()) {
        //                    retVal = c.getString(0);
        //                }
        //            } finally {
        //                if (c != null && !c.isClosed())
        //                    c.close();
        //            }
        //        }
        return retVal;
    }

    public void setDataAllowPermission(boolean value) {
        putValue(PREFERENCES_DATA_ALLOW_PERMISSION, value);
    }

    public boolean getDataAllowPermission() {
        return getValueBoolean(PREFERENCES_DATA_ALLOW_PERMISSION, false);
    }

    // Information Security Authentication of China NAL Test start
    public void setLocationAllowPermission(boolean value) {
        putValue(PREFERENCES_LOCATION_ALLOW_PERMISSION, value);
    }

    public boolean getLocationAllowPermission() {
        return getValueBoolean(PREFERENCES_LOCATION_ALLOW_PERMISSION, false);
    }

    // Information Security Authentication of China NAL Test start

    /* separate_email_ui_from_mom */
    /*
     * public void setDefaultAccount(Account account) {
     * putValue(DEFAULT_ACCOUNT_UUID, account.getUuid()); }
     */

    public void setUiAccountChanged(boolean value) {
        putValue(UI_ACCOUNT_CHANGED, value);
    }

    public boolean getUiAccountChanged() {
        return getValueBoolean(UI_ACCOUNT_CHANGED, false);
    }

    public void setDebugBits(int value) {
        putValue(DEBUG_BITS, value);
    }

    public int getDebugBits() {
        return getValueInt(DEBUG_BITS, 0);
    }

    public void setEnableDebugLogging(int value) {
        putValue(ENABLE_DEBUG_LOGGING, value);
    }

    public void setCarrierAccountId(String Uuid) {
        synchronized (Preferences.class) {
            putValue(CARRIER_ACCOUNT_UUID, Uuid);
        }
    }

    public String getCarrierAccountId() {
        return getValueString(CARRIER_ACCOUNT_UUID, null);
    }

    public Account getCarrierAccount() {
        synchronized (Preferences.class) {
            String carrierAccountId = getCarrierAccountId();
            Log.d(">>> Preference", "fixed account >> " + carrierAccountId);

            Account ac = null;
            if (carrierAccountId == null) {
                return ac;
            }

            Account[] accounts = getAccounts();
            if (carrierAccountId != null) {
                for (Account account : accounts) {
                    if (account.getUuid().equals(carrierAccountId)) {
                        ac = account;
                        break;
                    }
                }
            }
            return ac;
        }
    }

    public void setEnableWebSizeLogging(boolean value) {
        putValue(ENABLE_WEBVIEW_SIZE_LOGGING, value);
    }

    public void setEnableDownloadSpeedLogging(boolean value) {
        putValue(ENABLE_DOWNLOAD_SPEED_LOGGING, value);
    }

    public void setEnableViewEntrySpeedLogging(boolean value) {
        putValue(ENABLE_VIEW_ENTRY_SPEED_LOGGING, value);
    }

    public void setEnableEMLFileEntrySpeedLogging(boolean value) {
        putValue(ENABLE_EML_ENTRY_SPEED_LOGGING, value);
    }

    public void setEnableSaveHtmlLogging(boolean value) {
        putValue(ENABLE_SAVE_HTML_LOGGING, value);
    }

    public void setEnableWebviewInterfaceLogging(boolean value) {
        putValue(ENABLE_WEBVIEW_INTERFACE_LOGGING, value);
    }

    public void setEnableWebviewInterfaceDetailLogging(boolean value) {
        putValue(ENABLE_WEBVIEW_INTERFACE_DETATIL_LOGGING, value);
    }

    public void setEnableLoadmoreTimeLogging(boolean value) {
        putValue(ENABLE_LOADMORE_TIME_LOGGING, value);
    }

    public void setEnableEASLoadmoreTimeLogging(boolean value) {
        putValue(ENABLE_EAS_LOADMORE_TIME_LOGGING, value);
    }

    public void setEnableRefreshBodyMenu(boolean value) {
        putValue(ENABLE_REFRESH_BODY_MENU, value);
    }

    public void setEnableSaveViewLogLogging(boolean value) {
        putValue(ENABLE_SAVE_VIEWLOG_FILE_LOGGING, value);
    }
    public void setEnableEmailSimpleFeature(boolean value) {
        putValue(ENABLE_EMAIL_SIMPLE_FEATURE, value);
    }
    public boolean getEnableEmailSimpleFeature() {
        return getValueBoolean(ENABLE_EMAIL_SIMPLE_FEATURE, false);
    }

    public void setEnableEmailPlusSimpleFeature(boolean value) {
        putValue(ENABLE_EMAIL_PLUS_SIMPLE_FEATURE, value);
    }
    public boolean getEnableEmailPlusSimpleFeature() {
        return getValueBoolean(ENABLE_EMAIL_PLUS_SIMPLE_FEATURE, false);
    }


    public void setEnableLocalTimeLogging(boolean value) {
        putValue(ENABLE_LOCAL_TIME_LOGGING, value);
    }

    public boolean getEnableLocalTimeLogging() {
        return getValueBoolean(ENABLE_LOCAL_TIME_LOGGING, false);
    }

    public void setEnablePanicErrorNotifier(boolean value) {
        putValue(ENABLE_PANIC_ERROR_NOTIFIER, value);
    }

    public boolean getEnablePanicErrorNotifier() {
        return getValueBoolean(ENABLE_PANIC_ERROR_NOTIFIER, false);
    }

    public void setDisableIMAPSmartSync(boolean value) {
        putValue(DISABLE_IMAP_SMART_SYNC, value);
    }

    public boolean getDisableIMAPSmartSync() {
        return getValueBoolean(DISABLE_IMAP_SMART_SYNC, false);
    }

    public void setEnableUntrustedCertificateFeature(boolean value) {
        putValue(UNTRUSTED_CERTIFICATE_FEATURE, value);
    }

    public boolean getEnableUntrustedCertificateFeature() {
        return getValueBoolean(UNTRUSTED_CERTIFICATE_FEATURE, true);
    }

    public void setEnableIMAPDraftsSync(boolean value) {
        putValue(ENABLE_IMAP_DRAFTS_SYNC, value);
    }

    public boolean getEnableIMAPDraftsSync() {
        /* Default value to be returned is true */
        return getValueBoolean(ENABLE_IMAP_DRAFTS_SYNC, true);
    }

    public boolean getEnableDebugLogging() {

        if (getValueInt(ENABLE_DEBUG_LOGGING, 0) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getEnableWebSizeLogging() {
        return getValueBoolean(ENABLE_WEBVIEW_SIZE_LOGGING, false);
    }

    public boolean getEnableDownloadSpeedLogging() {
        return getValueBoolean(ENABLE_DOWNLOAD_SPEED_LOGGING, false);
    }

    public boolean getEnableViewEntrySpeedLogging() {
        return getValueBoolean(ENABLE_VIEW_ENTRY_SPEED_LOGGING, false);
    }

    public boolean getEnableEMLFileEntrySpeedLogging() {
        return getValueBoolean(ENABLE_EML_ENTRY_SPEED_LOGGING, false);
    }

    public boolean getEnableSaveHtmlLogging() {
        return getValueBoolean(ENABLE_SAVE_HTML_LOGGING, false);
    }

    public boolean getEnableWebviewInterfaceLogging() {
        return getValueBoolean(ENABLE_WEBVIEW_INTERFACE_LOGGING, false);
    }

    public boolean getEnableWebviewInterfaceDetailLogging() {
        return getValueBoolean(ENABLE_WEBVIEW_INTERFACE_DETATIL_LOGGING, false);
    }

    public boolean getEnableLoadmoreTimeLogging() {
        return getValueBoolean(ENABLE_LOADMORE_TIME_LOGGING, false);
    }

    public boolean getEnableEASLoadmoreTimeLogging() {
        return getValueBoolean(ENABLE_EAS_LOADMORE_TIME_LOGGING, false);
    }

    public boolean getEnableRefreshBodyMenu() {
        return getValueBoolean(ENABLE_REFRESH_BODY_MENU, false);
    }

    public boolean getEnableSaveViewLogLogging() {
        return getValueBoolean(ENABLE_SAVE_VIEWLOG_FILE_LOGGING, false);
    }

    public void setPositionOfQuickResponseOption(int value) {
        putValue(QUICK_RESPONSE_OPTION, value);
    }

    public int getPositionOfQuickResponseOption() {
        return getValueInt(QUICK_RESPONSE_OPTION, 0);
    }

    public void setEnableSensitiveLogging(boolean value) {
        putValue(ENABLE_SENSITIVE_LOGGING, value);
    }

    public boolean getEnableSensitiveLogging() {
        return getValueBoolean(ENABLE_SENSITIVE_LOGGING, false);
    }

    public void setEnableExchangeLogging(int value) {
        putValue(ENABLE_EXCHANGE_LOGGING, value);
    }

    public boolean getEnableExchangeLogging() {

        if (getValueInt(ENABLE_EXCHANGE_LOGGING, 0) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void setEnableExchangeFileLogging(int value) {
        putValue(ENABLE_EXCHANGE_FILE_LOGGING, value);
    }

    public boolean getEnableExchangeFileLogging() {

        if (getValueInt(ENABLE_EXCHANGE_FILE_LOGGING, 0) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void setInhibitGraphicsAcceleration(boolean value) {
        putValue(INHIBIT_GRAPHICS_ACCELERATION, value);
    }

    public boolean getInhibitGraphicsAcceleration() {
        return getValueBoolean(INHIBIT_GRAPHICS_ACCELERATION, false);
    }

    public void setForceOneMinuteRefresh(boolean value) {
        putValue(FORCE_ONE_MINUTE_REFRESH, value);
    }

    public boolean getForceOneMinuteRefresh() {
        return getValueBoolean(FORCE_ONE_MINUTE_REFRESH, false);
    }

    public void setEnableStrictMode(boolean value) {
        putValue(ENABLE_STRICT_MODE, value);
    }

    public boolean getEnableStrictMode() {
        return getValueBoolean(ENABLE_STRICT_MODE, false);
    }

    public void setEnableMailHeaderLogging(boolean value) {
        putValue(ENABLE_MAIL_HEADERS_LOGGING, value);
    }

    public boolean getEnableMailHeaderLogging() {
        return getValueBoolean(ENABLE_MAIL_HEADERS_LOGGING, false);
    }

    /**
     * Generate a new "device UID". This is local to Email app only, to prevent
     * possibility of correlation with any other user activities in any other
     * apps.
     *
     * @return a persistent, unique ID
     */
    public synchronized String getDeviceUID() {
        String result = getValueString(DEVICE_UID, null);
        if (result == null) {
            result = UUID.randomUUID().toString();
            putValue(DEVICE_UID, result);
        }
        return result;
    }

    public int getOneTimeInitializationProgress() {
        return getValueInt(ONE_TIME_INITIALIZATION_PROGRESS, 0);
    }

    public void setOneTimeInitializationProgress(int progress) {
        putValue(ONE_TIME_INITIALIZATION_PROGRESS, progress);
    }

    public boolean getHideCheckboxes() {
        return getValueBoolean(HIDE_CHECKBOXES, HIDE_CHECKBOXES_DEFAULT);
    }

    public void setHideCheckboxes(boolean hideCheckbox) {
        putValue(HIDE_CHECKBOXES, hideCheckbox);
    }

    public boolean getAutoFit() {
        return getValueBoolean(AUTO_FIT, AUTO_FIT_DEFAULT);
    }

    public boolean getSmsForwardingStatus() {
        return getValueBoolean(ENABLE_SMS_FORWARDING, false);
    }

    public void setSmsForwardingStatus(boolean forwardSms) {
        putValue(ENABLE_SMS_FORWARDING, forwardSms);
    }

    public void setAutoFit(boolean autofit) {
        putValue(AUTO_FIT, autofit);
    }

    public boolean getViewAutoImage() {
        return getValueBoolean(VIEW_AUTOIMAGE, VIEW_AUTOIMAGE_DEFAULT);
    }

    public void setViewAutoImage(boolean viewautoimage) {
        putValue(VIEW_AUTOIMAGE, viewautoimage);
    }


    public static String getCSCTagValueforEmail(String tagName) {
        String Field_value = "NONE";
        AccountSetupCustomer customer = AccountSetupCustomer.getInstance();

        if (customer != null) {
            String Tag_value = customer.getCSCTagValueforEmail(tagName);
            if (!TextUtils.isEmpty(Tag_value))
                Field_value = Tag_value.trim();
        }

        return Field_value;
    }

    private static int get_AutoAdvanceDirectionfromCSC(int rcv_value) {
        String rcv_Field_value = getCSCTagValueforEmail("AutoAdvance");

        int default_value = 2; // List
        if (rcv_value >= 0 && rcv_value <= 2)
            default_value = rcv_value;

        if (rcv_Field_value != null) {
            if (rcv_Field_value.equalsIgnoreCase("NONE"))
                return default_value;

            if (rcv_Field_value.equalsIgnoreCase("Next"))
                default_value = 0;
            else if (rcv_Field_value.equalsIgnoreCase("Previous"))
                default_value = 1;
            else if (rcv_Field_value.equalsIgnoreCase("List"))
                default_value = 2;
        }

        return default_value;
    }

    public static boolean get_DeleteEmailConfirmfromCSC(boolean rcv_value) {
        String rcv_Field_value = getCSCTagValueforEmail("ConfirmDelete");

        boolean default_value = rcv_value;

        if (rcv_Field_value != null) {
            if (rcv_Field_value.equalsIgnoreCase("NONE"))
                return default_value;

            if (rcv_Field_value.equalsIgnoreCase("OFF"))
                default_value = false;
            else if (rcv_Field_value.equalsIgnoreCase("ON"))
                default_value = true;
        }

        return default_value;
    }

    public int getAutoAdvanceDirection() {
        return getValueInt(AUTO_ADVANCE_DIRECTION,
                get_AutoAdvanceDirectionfromCSC(AUTO_ADVANCE_DEFAULT));
    }

    public void setAutoAdvanceDirection(int direction) {
        putValue(AUTO_ADVANCE_DIRECTION, direction);
    }

    //    public void setTextZoom(int zoom) {
    //        putValue(TEXT_ZOOM, zoom);
    //    }

    public boolean getEnableUseSystemFont() {
        return getValueBoolean(ENABLE_USE_SYSTEM_FONT, ENABLE_USE_SYSTEM_FONT_DEFAULT);
    }

    public void setEnableUseSystemFont(boolean use) {
        putValue(ENABLE_USE_SYSTEM_FONT, use);
    }

    // Added preview line in preference start 20110422 johnny
    public int getPreViewLine() {
        return getValueInt(PREVIEW_LINE, PREVIEW_LINE_DEFAULT);
    }

    public int getPreViewLineEasyMode() {
        return getValueInt(PREVIEW_LINE_EASYMODE, PREVIEW_LINE_NONE);
    }

    public void setPreViewLine(int previewline) {
        putValue(PREVIEW_LINE, previewline);
    }

    public void setPreViewLineEasyMode(int previewline) {
        putValue(PREVIEW_LINE_EASYMODE, previewline);
    }

    // Added preview line in preference end 20110422 johnny

    // sh2209.cho : Split mode

    public boolean getSplitMode() {
        return getValueBoolean(SPLIT_MODE, Utility.isNoteModel() || Utility.isTabletModel());
    }

    public void setSplitMode(boolean direction) {
        putValue(SPLIT_MODE, direction);
        EmailFeature.setSplitMode(direction);
    }

    public boolean getVIPDefaultfolder() {
        //        return getValueBoolean(VIP_DEFAULT_FOLDER, false);
        return false;
    }

    public void setVIPDefaultfolder(boolean direction) {
        putValue(VIP_DEFAULT_FOLDER, direction);
    }

    public boolean getMasterNotification() {
        return getValueBoolean(MASTER_NOTIFICATION, true);
    }

    public void setMasterNotification(boolean direction) {
        putValue(MASTER_NOTIFICATION, direction);
    }

    // sh2209.cho : Add VIP Contacts
    public boolean getVIPNotification() {
        return getValueBoolean(VIP_NOTIFICATION, true);
    }

    public void setVIPNotification(boolean direction) {
        putValue(VIP_NOTIFICATION, direction);
    }

    public String getVIPRingtone() {
        return getValueString(VIP_RINGTONE, CarrierValues.DEFAULT_RINGTONEURI);
    }

    public void setVIPRingtone(String direction) {
        putValue(VIP_RINGTONE, direction);
    }

    public boolean getVIPVibrate() {
        return getValueBoolean(VIP_VIBRATE, false);
    }

    public void setVIPVibrate(boolean direction) {
        putValue(VIP_VIBRATE, direction);
    }

    /*
     * public int getRecentMessages() { return getValueInt(RECENT_MESSAGES, 0);
     * } public void setRecentMessages(int resentmessages) {
     * putValue(RECENT_MESSAGES, resentmessages); } public boolean
     * getShowImage() { return getValueBoolean(SHOW_IMAGE, true); } public void
     * setShowImage(boolean direction) { putValue(SHOW_IMAGE, direction); }
     */

    public int getTopLineInfoMode() {
        return getValueInt(TOP_LINE_INFO_MODE, TOP_LINE_DEFAULT);
    }

    public void setTopLineInfoMode(int toplinemode) {
        putValue(TOP_LINE_INFO_MODE, toplinemode);
    }

    public boolean getDeleteEmailConfirm() {
        return getValueBoolean(DELETE_EMAIL_CONFIRM,
                get_DeleteEmailConfirmfromCSC(DELETE_EMAIL_CONFIRM_DEFAULT));
    }

    public void setDeleteEmailConfirm(boolean deleteemailconfirm) {
        putValue(DELETE_EMAIL_CONFIRM, deleteemailconfirm);
    }

    public boolean getOnlineRecipientSearch() {
        return getValueBoolean(ONLINE_RECIPIENT_SEARCH, ONLINE_RECIPIENT_SEARCH_DEFAULT);
    }

    public void setOnlineRecipientSearch(boolean useOnlineRecipientSearch) {
        putValue(ONLINE_RECIPIENT_SEARCH, useOnlineRecipientSearch);
    }

    // jk0112.lee, to restore list state (accountId and mailboxId)
    public long getMailboxId() {
        return getValueLong(MAILBOX_ID, MAILBOX_ID_DEFAULT);
    }

    public void setMailboxId(long mailboxId) {
        putValue(MAILBOX_ID, mailboxId);
    }

    public long getAccountId() {
        return getValueLong(ACCOUNT_ID, ACCOUNT_ID_DEFAULT);
    }

    public String[] getAccountUuids() {
        String accountUuids = getValueString(ACCOUNT_UUIDS, null);

        if (accountUuids == null || accountUuids.length() == 0) {
            return new String[] {};
        }
        return accountUuids.split(",");
    }

    public String[] getVipUuids() {

        String vipUuids = getValueString(VIP_UUIDS, null);

        if (vipUuids == null || vipUuids.length() == 0) {

            return new String[] {};

        }

        return vipUuids.split(",");

    }

    public String[] getBlackListUuids() {

        String blackListUuids = getValueString(BLACK_LIST_UUIDS, null);

        if (blackListUuids == null || blackListUuids.length() == 0) {

            return new String[] {};

        }

        return blackListUuids.split(",");

    }

    /**
     * Returns an array of the accounts on the system. If no accounts are
     * registered the method returns an empty array.
     */
    public Account[] getAccounts() {
        String[] accountUuids = getAccountUuids();
        Account[] accounts = new Account[accountUuids.length];
        for (int i = 0, length = accountUuids.length; i < length; i++) {
            accounts[i] = new Account(this, accountUuids[i]);
        }
        return accounts;
    }

    public void setAccountId(long accountId) {
        putValue(ACCOUNT_ID, accountId);
        if (!EmailContent.Account.isVirtualAccount(accountId)) {
            putValue(ACCOUNT_ID2, accountId);
        }
    }

    public long getAccountId2() {
        return getValueLong(ACCOUNT_ID2, -1);
    }

    public String getSelectedPriority() {
        return getValueString(SELECTED_PRIORITY, SELECTED_PRIORITY_DEFAULT);
    }

    public void setSelectedPriority(String address) {
        putValue(SELECTED_PRIORITY, address);
    }

    public boolean getBackgroundAttachments() {
        return getValueBoolean(BACKGROUND_ATTACHMENTS, false);
    }

    public void setBackgroundAttachments(boolean allowed) {
        putValue(BACKGROUND_ATTACHMENTS, allowed);
    }

    // SAMSUNG ACTIVATION - reda.dehy - start
    public void setActivationLicense(String value) {
        putValue(ACTIVATION_LICENSE, value);
    }

    public String getActivationLicense() {
        return getValueString(ACTIVATION_LICENSE, null);
    }

    // SAMSUNG ACTIVATION - reda.dehy - end

    // Motion tutorial by EJ
    public boolean getMotionTutorialShowAgain() {
        return getValueBoolean(MOTION_TUTORIAL_DOUBLE_TAP, false);
    }

    public void setgetMotionTutorialShowAgain(boolean value) {
        putValue(MOTION_TUTORIAL_DOUBLE_TAP, value);
    }

    // change@siso.gaurav cancel send start
    public boolean getCancelSendingMessage() {
        return getValueBoolean(CANCEL_SENDING_MESSAGE, CANCEL_SENDING_MESSAGE_DEFAULT);
    }

    public void setCancelSendingMessage(boolean isChecked) {
        putValue(CANCEL_SENDING_MESSAGE, isChecked);
    }

    public long getCancelSendingMessagePeriod() {
        return getValueLong(CANCEL_SENDING_MESSAGE_PERIOD, CANCEL_SENDING_MESSAGE_DEFAULT_PERIOD);
    }

    public void setCancelSendingMessagePeriod(long value) {
        putValue(CANCEL_SENDING_MESSAGE_PERIOD, value);
    }

    // change@siso.gaurav cancel send end

    public int getDefaultImageSize() {
        return getValueInt(DEFAULT_IMAGE_SIZE, DEFAULT_IMAGE_SIZE_DEFAULT);
    }

    public void setDefaultImageSize(int defaultImageSize) {
        putValue(DEFAULT_IMAGE_SIZE, defaultImageSize);
    }

    public int getViewMode() {
        return getValueInt(PREFERENCES_THREAD_VIEW_MODE, DEFAULT_VIEW_MODE);
    }

    public void setViewMode(int viewMode) {
        putValue(PREFERENCES_THREAD_VIEW_MODE, viewMode);
    }

    public int getSortType() {
        return getValueInt(PREFERENCES_SORT_TYPE, DEFAULT_SORT_TYPE);
    }

    public void setSortType(int sortType) {
        putValue(PREFERENCES_SORT_TYPE, sortType);
    }

    public int getLeftAndMiddleSplitPoint() {
    	return getValueInt(PREFERENCES_LEFT_AND_MIDDLE_SPLIT_POINT, -1);
    }
    
    public int getMiddleAndRightSplitPoint() {
    	return getValueInt(PREFERENCES_MIDDLE_AND_RIGHT_SPLIT_POINT, -1);
    }
    
    public int getMiddleAndRightConvSplitPoint() {
    	return getValueInt(PREFERENCES_MIDDLE_AND_RIGHT_CONV_SPLIT_POINT, -1);
    }
    
    public int getRightAndRightConvSplitPoint() {
    	return getValueInt(PREFERENCES_RIGHT_CONV_AND_RIGHT_SPLIT_POINT, -1);
    }
    
    public void setLeftAndMiddleSplitPoint(int point) {
    	putValue(PREFERENCES_LEFT_AND_MIDDLE_SPLIT_POINT, point);
    }
    
    public void setMiddleAndRightSplitPoint(int point) {
    	putValue(PREFERENCES_MIDDLE_AND_RIGHT_SPLIT_POINT, point);
    }
    
    public void setMiddleAndRightConvSplitPoint(int point) {
    	putValue(PREFERENCES_MIDDLE_AND_RIGHT_CONV_SPLIT_POINT, point);
    }
    
    public void setRightAndRightConvSplitPoint(int point) {
    	putValue(PREFERENCES_RIGHT_CONV_AND_RIGHT_SPLIT_POINT, point);
    }

    public boolean getEmailRunState() {
        return getValueBoolean(PREFERENCES_RUN_FIRST, true);
    }

    public void setEmailRunState(boolean emailRunState) {
        putValue(PREFERENCES_RUN_FIRST, emailRunState);
    }

    //    public AccountNoti getNotificationPref(long accountId) {
    //        AccountNoti noti = new AccountNoti();
    //        noti.accountId = getValueLong(NOTI_PREF + accountId + ID_TAG, 0);
    //        noti.lastestMsgId = getValueLong(NOTI_PREF + accountId + MSGID_TAG, 0);
    //        noti.messageCount = getValueInt(NOTI_PREF + accountId + COUNT_TAG, 0);
    //        noti.unseenMessageCount = getValueInt(NOTI_PREF + accountId + MSGCOUNT_TAG, 0);
    //        return noti;
    //    }
    //
    //    public SendFailNoti getFailNotiPref(long accountId) {
    //        SendFailNoti sendNoti = new SendFailNoti();
    //        int i = 0;
    //        Map<String, String> keys = getAllValues();
    //        sendNoti.messageId = new long[keys.size()];
    //        sendNoti.title = new String[keys.size()];
    //        for (Map.Entry<String, ?> entry : keys.entrySet()) {
    //            String keyName = entry.getKey();
    //            if (!keyName.contains(NOTI_FAIL)) {
    //                continue;
    //            }
    //            String keyAccount = keyName
    //                    .substring(NOTI_FAIL.length() + NOTI_MESSAGE_ID.length() + 1);
    //            String keyAccountID[] = keyAccount.split("_");
    //
    //            String keyMessage = keyName.substring(NOTI_FAIL.length() + NOTI_MESSAGE_ID.length() + 1
    //                    + keyAccountID[0].length() + 1);
    //
    //            sendNoti.messageId[i] = Long.valueOf(keyMessage);
    //            sendNoti.title[i] = (String) entry.getValue();
    //            i++;
    //        }
    //        return sendNoti;
    //    }
    //
    //    public void setNotificationPref(long accountId, long lastestMsgId, int unseenMessageCount,
    //            int mCount) {
    //        ValueBuilder vb = new ValueBuilder();
    //        vb.putLong(NOTI_PREF + accountId + ID_TAG, accountId);
    //        vb.putLong(NOTI_PREF + accountId + MSGID_TAG, lastestMsgId);
    //        vb.putInt(NOTI_PREF + accountId + COUNT_TAG, mCount);
    //        vb.putInt(NOTI_PREF + accountId + MSGCOUNT_TAG, unseenMessageCount);
    //        putValues(vb);
    //    }
    //
    //    public void setFailNotiPref(long accountId, long messageId, String title) {
    //        putValue(NOTI_FAIL + NOTI_MESSAGE_ID + ":" + accountId + "_" + messageId, title);
    //    }
    //
    //    public void removeNotificationPref(long accountId) {
    //        removeValues(NOTI_PREF + accountId + ID_TAG, NOTI_PREF + accountId + MSGID_TAG, NOTI_PREF
    //                + accountId + COUNT_TAG, NOTI_PREF + accountId + MSGCOUNT_TAG);
    //
    //    }
    //
    //    public void removeFailNotiPref(long accountId, long messageId) {
    //        removeValues(NOTI_FAIL + NOTI_MESSAGE_ID + ":" + accountId + "_" + messageId);
    //    }
    //
    //    public void removeNotificationAccountPref(long accountId) {
    //        Map<String, String> keys = getAllValues();
    //        for (Map.Entry<String, ?> entry : keys.entrySet()) {
    //            String keyName = entry.getKey();
    //            if (!keyName.contains(NOTI_FAIL)) {
    //                continue;
    //            }
    //            String keyAccount = keyName
    //                    .substring(NOTI_FAIL.length() + NOTI_MESSAGE_ID.length() + 1);
    //            String keyAccountID[] = keyAccount.split("_");
    //
    //            String keyMessage = keyName.substring(NOTI_FAIL.length() + NOTI_MESSAGE_ID.length() + 1
    //                    + keyAccountID[0].length() + 1);
    //            removeFailNotiPref(accountId, Long.valueOf(keyMessage));
    //        }
    //    }
    public void setTempMessageId(long mid) {

        putValue(PREFERENCES_TEMP_MESSAGE_ID, mid);

    }

    public boolean getCheckFwdRecipients(long messageId) {

        return getValueBoolean(messageId + "/" + PREFERENCES_TEMP_FWD_RECIPIENTS_CHECK, TEMP_CHECK_FWD_RECIPIENTS_DEFAULT);

    }

    public void setCheckFwdRecipients(long messageId, boolean check) {

        putValue(messageId + "/" + PREFERENCES_TEMP_FWD_RECIPIENTS_CHECK, check);

    }

    public String[] getStringArrayFwdRecipients(long messageId) {


        return new String[]{getValueString(messageId + "/" + PREFERENCES_TEMP_FWD_TO_RECIPIENTS, null),
                getValueString(messageId + "/" + PREFERENCES_TEMP_FWD_CC_RECIPIENTS, null)};

    }

    public void setStringFwdRecipients(long messageId, String to, String cc) {

        putValue(messageId + "/" + PREFERENCES_TEMP_FWD_TO_RECIPIENTS, to);
        putValue(messageId + "/" + PREFERENCES_TEMP_FWD_CC_RECIPIENTS, cc);

    }

    public long getTempMessageId() {

        return getValueLong(PREFERENCES_TEMP_MESSAGE_ID, TEMP_MESSAGE_ID_DEFAULT);

    }

    public void setTempAccountId(long mid) {

        putValue(PREFERENCES_TEMP_ACCOUNT_ID, mid);

    }

    public long getTempAccountId() {

        return getValueLong(PREFERENCES_TEMP_ACCOUNT_ID, TEMP_MESSAGE_ID_DEFAULT);

    }

    public void setExtraFontSize(int size){
        putValue(PREFERENCES_EXTRA_FONT_SIZE, size);
    }

    public int getExtraFontSize(){
        return getValueInt(PREFERENCES_EXTRA_FONT_SIZE, 0);
    }

    public void setHidePosition(long accountId, boolean hide) {
        putValue(PREFERENCES_HIDE_ACCOUNT + accountId, hide);
    }

    public void setHidePosition(Set<Long> accountIds, boolean hide) {
        Iterator<Long> iter = accountIds.iterator();
        while (iter.hasNext()) {
            setHidePosition(iter.next(), hide);
        }
    }

    public boolean isHiddenPosition(long accountId) {
        return getValueBoolean(PREFERENCES_HIDE_ACCOUNT + accountId, false);
    }

    public void removeHiddenAccountIdPref(long accountId) {
        removeValues(PREFERENCES_HIDE_ACCOUNT + accountId);
    }


    //    public void cleanAccountInfoFromPref(long accountId) {
    //        removeNotificationAccountPref(accountId);
    //        removeNotificationPref(accountId);
    //        removeHiddenAccountIdPref(accountId);
    //    }

    public void save() {
    }

    public void dump() {
        if (Log.LOGD) {
            for (Entry<String, String> e : getAllValues().entrySet()) {
                Log.v(Logging.LOG_TAG, e.getKey() + " = " + e.getValue());
            }
        }
    }

    // 130709, yh215.kim, save current and preset pen setting start
    public String getCurrentSpen() {
        return getValueString(SPEN_CURRENT_SPENSETTING, SPEN_NOTHING);
    }

    // public int getSpenPresetCount() {
    // return getValueInt(SPEN_PRESET_COUNT, 0);
    // }

    // public String getAPreset(int index) {
    // return getValueString(SPEN_PRESET_ + index, "nothing");
    // }

    public void setCurrentSpenSettingPenInfo(String name, int color, float size) {
        putValue("current_spen", name + ";" + color + ";" + size);
    }

    // public void setSpenSettingPenInfoList(List<SpenSettingPenInfo>
    // spenSettingPenInfoList) {
    // SharedPreferences.Editor editor = mSharedPreferences.edit();
    // int size = spenSettingPenInfoList.size();
    // editor.putInt(SPEN_PRESET_COUNT, size);
    // SpenSettingPenInfo aInfo;
    // for (int i = 0 ; i < size ; ++i) {
    // aInfo = spenSettingPenInfoList.get(i);
    // editor.putString(SPEN_PRESET_ + i, aInfo.name + ";" + aInfo.color + ";" +
    // aInfo.size);
    // }
    // editor;
    //
    // }
    // 130709, yh215.kim, save current and preset pen setting end

    public boolean getRichToolbarDisplayState() {
        return getValueBoolean(DEFAULT_RICH_TOOLBAR_DISPLAY_STATE,
                DEFAULT_RICH_TOOLBAR_DISPLAY_STATE_DEFAULT);
    }

    public void setRichToolbarDisplayState(boolean state) {
        putValue(DEFAULT_RICH_TOOLBAR_DISPLAY_STATE, state);
    }

    public boolean getIsTipsShowing() {
        return getValueBoolean(EMAIL_COMPOSE_TIPS_SHOWING_STATE, true);
    }

    public void setIsTipsShowing(boolean isShown) {
        putValue(EMAIL_COMPOSE_TIPS_SHOWING_STATE, isShown);
    }

    public String getLastFocusedEditableView() {

        return getValueString(LAST_FOCUSED_EDITABLE_VIEW_NAME,
                LAST_FOCUSED_EDITABLE_VIEW_NAME_DEFAULT);
    }

    public void setLastFocusedEditableView(String state) {

        putValue(LAST_FOCUSED_EDITABLE_VIEW_NAME, state);

    }
    public boolean getContextualContentsState() {
        return getValueBoolean(PREFERENCES_CONTEXTUAL_CONTENTS,
                DEFAULT_CONTEXTUAL_CONTENTS_STATE_DEFAULT);
    }

    public void setContextualContentsState(boolean state) {
        putValue(PREFERENCES_CONTEXTUAL_CONTENTS, state);
    }

    public boolean getIgnoreDataConnection() {
        return getValueBoolean(PREFERENCES_DEBUG_IGNORE_DATA_CONNECTION, false);
    }

    public void setIgnoreDataConnection(boolean state) {
        putValue(PREFERENCES_DEBUG_IGNORE_DATA_CONNECTION, state);
    }

    public boolean getShowWifiDialogForAttachments() {
        return getValueBoolean(SHOW_WIFI_ONLY_DIALOG_FOR_ATTACTMANTS, true);
    }

    public void setShowWifiDialogForAttachments(boolean show) {
        putValue(SHOW_WIFI_ONLY_DIALOG_FOR_ATTACTMANTS, show);
    }

    // For com.samsung.android.email.ui.Account
    public void putAccountInformation(String keyWithUuid, String value) {
        putValue(keyWithUuid, value);
    }

    public void putAccountInformation(String keyWithUuid, Long value) {
        putValue(keyWithUuid, value);
    }

    public void putAccountInformation(String keyWithUuid, Boolean value) {
        putValue(keyWithUuid, value);
    }

    public void putAccountInformation(String keyWithUuid, int value) {
        putValue(keyWithUuid, value);
    }

    public String getStringAccountInformation(String keyWithUuid, String defValue) {
        return getValueString(keyWithUuid, defValue);
    }

    public Long getLongAccountInformation(String keyWithUuid, Long defValue) {
        return getValueLong(keyWithUuid, defValue);
    }

    public Boolean getBooleanAccountInformation(String keyWithUuid, Boolean defValue) {
        return getValueBoolean(keyWithUuid, defValue);
    }

    public int getIntAccountInformation(String keyWithUuid, int defValue) {
        return getValueInt(keyWithUuid, defValue);
    }

    public void removeAccountInformations(String... keys) {
        removeValues(keys);
    }

    //    public int getTextZoom(boolean isPhone, boolean isEasyMode) {
    //        if (isPhone) {
    //            if (isEasyMode) {
    //                if (EmailFeature.isSevenFontDevice()) {
    //                    return getValueInt(TEXT_ZOOM, TEXT_ZOOM_NORMAL_IN_APP_H);
    //                }
    //                return getValueInt(TEXT_ZOOM, TEXT_ZOOM_NORMAL_IN_APP);
    //            } else {
    //                return getValueInt(TEXT_ZOOM, TEXT_ZOOM_PHONE_DEFAULT);
    //            }
    //        } else
    //            return getValueInt(TEXT_ZOOM, TEXT_ZOOM_TABLET_DEFAULT);
    //    }

    public void setSignature(long accountId, String signature) {
        try {
            String key = String.valueOf(accountId) + delim + VALUE_TYPE_SIGNATURE;
            ContentValues cv = new ContentValues();
            cv.put(key, signature);
            mResolver.update(CONTENT_URI, cv, null, null);
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "Preferences IllegalArgumentException " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getSignature(long accountId) {
        String signature = "";
        try {
            String proj = String.valueOf(accountId) + delim + VALUE_TYPE_SIGNATURE + delim + "dummy";
            Cursor c = null;
            QueryUtil qu = QueryUtil.createInstance(mResolver);
            //        boolean isEdited = false;
            try {
                c = qu.query(CONTENT_URI, new String[] {proj}, null);
                if (c != null && c.moveToFirst()) {
                    signature = c.getString(0);
                    //                isEdited = (c.getInt(1) == 1);
                }
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "Preferences IllegalArgumentException " + e.getMessage());
            e.printStackTrace();
        }
        return signature;
    }

    public boolean getBoolean(String key, boolean defVal) {
        return getValueBoolean(key, defVal);
    }

    public void putBoolean(String key, boolean Val) {
        putValue(key, (boolean) Val);
    }

    public void setDisabledPrioritySenderGuide(boolean disabled) {
        putValue(PRIORITY_SENDER_GUIDE, disabled);
    }

    public boolean isDisabledPrioritySenderGuide() {
        return getBoolean(PRIORITY_SENDER_GUIDE, false);
    }

    public boolean getIsShowMessageListTips(){
        return getValueBoolean(EMAIL_MESSAGE_LIST_FIRST_SHOW_STATE, true);

    }

    public void setIsShowMessageListTips(boolean isFirst){
        putValue(EMAIL_MESSAGE_LIST_FIRST_SHOW_STATE, isFirst);
    }
    //SRIB@abhilasha.hv change for MDM SMIME cert installation starts
    public void setMDMSmimeCertificate(long accountID, String certificatePath, String password, long result_id){
        putValue(PREFERENCE_MDM_SMIME + accountID, accountID);
        putValue(PREFERENCE_MDM_SMIME + accountID + CERT_PATH, certificatePath);
        putValue(PREFERENCE_MDM_SMIME + accountID + CERT_PASSWORD, password);
        putValue(PREFERENCE_MDM_SMIME + accountID + CERT_RESULT_ID, result_id);
    }
    public void setMDMSmimeEncryptCertificate(long accountID, String certificatePath, String password, long result_id){

        putValue(PREFERENCE_MDM_SMIME_ENCRYPT + accountID, accountID);
        putValue(PREFERENCE_MDM_SMIME_ENCRYPT + accountID + CERT_PATH, certificatePath);
        putValue(PREFERENCE_MDM_SMIME_ENCRYPT + accountID + CERT_PASSWORD, password);
        putValue(PREFERENCE_MDM_SMIME_ENCRYPT + accountID + CERT_RESULT_ID, result_id);
    }
    public void setMDMSmimeSignCertificate(long accountID, String certificatePath, String password, long result_id){
        putValue(PREFERENCE_MDM_SMIME_SIGN + accountID, accountID);
        putValue(PREFERENCE_MDM_SMIME_SIGN + accountID + CERT_PATH, certificatePath);
        putValue(PREFERENCE_MDM_SMIME_SIGN + accountID + CERT_PASSWORD, password);
        putValue(PREFERENCE_MDM_SMIME_SIGN + accountID + CERT_RESULT_ID, result_id);
    }

    public MDMSMIMECertPref getMDMSMIMECertPref(long accId){
        MDMSMIMECertPref certInfo = new MDMSMIMECertPref();
        certInfo.accId = getValueLong(PREFERENCE_MDM_SMIME + accId, -1);
        certInfo.filePath = getValueString(PREFERENCE_MDM_SMIME + accId + CERT_PATH, null);
        certInfo.password = getValueString(PREFERENCE_MDM_SMIME + accId + CERT_PASSWORD, null);
        certInfo.resultID = getValueLong(PREFERENCE_MDM_SMIME + accId + CERT_RESULT_ID, -1);
        if(certInfo.accId == -1 || certInfo.password == null || certInfo.filePath == null){
            return null;
        }
        return certInfo;
    }

    public void clearMDMSMIMECertPref(long accId){
        removeValues(PREFERENCE_MDM_SMIME + accId);
        removeValues(PREFERENCE_MDM_SMIME + accId + CBA_PASSWORD_TAG);
        removeValues(PREFERENCE_MDM_SMIME + accId + CERT_PASSWORD);
        removeValues(PREFERENCE_MDM_SMIME + accId + CERT_RESULT_ID);
    }
    public MDMSMIMECertPref getMDMSMIMEEncryptCertPref(long accId){
        MDMSMIMECertPref certInfo = new MDMSMIMECertPref();
        certInfo.accId = getValueLong(PREFERENCE_MDM_SMIME_ENCRYPT + accId, -1);
        certInfo.filePath = getValueString(PREFERENCE_MDM_SMIME_ENCRYPT + accId + CERT_PATH, null);
        certInfo.password = getValueString(PREFERENCE_MDM_SMIME_ENCRYPT + accId + CERT_PASSWORD, null);
        certInfo.resultID = getValueLong(PREFERENCE_MDM_SMIME_ENCRYPT + accId + CERT_RESULT_ID, -1);

        if(certInfo.accId == -1 || certInfo.password == null || certInfo.filePath == null){
            return null;
        }
        return certInfo;
    }

    public void clearMDMSMIMEEncryptCertPref(long accId){

        removeValues(PREFERENCE_MDM_SMIME_ENCRYPT + accId);
        removeValues(PREFERENCE_MDM_SMIME_ENCRYPT + accId + CERT_PATH);
        removeValues(PREFERENCE_MDM_SMIME_ENCRYPT + accId + CERT_PASSWORD);
        removeValues(PREFERENCE_MDM_SMIME_ENCRYPT + accId + CERT_RESULT_ID);
    }

    public MDMSMIMECertPref getMDMSMIMESignCertPref(long accId){
        MDMSMIMECertPref certInfo = new MDMSMIMECertPref();
        certInfo.accId = getValueLong(PREFERENCE_MDM_SMIME_SIGN + accId, -1);
        certInfo.filePath = getValueString(PREFERENCE_MDM_SMIME_SIGN + accId + CERT_PATH, null);
        certInfo.password = getValueString(PREFERENCE_MDM_SMIME_SIGN + accId + CERT_PASSWORD, null);
        certInfo.resultID = getValueLong(PREFERENCE_MDM_SMIME_SIGN + accId + CERT_RESULT_ID, -1);
        if(certInfo.accId == -1 || certInfo.password == null || certInfo.filePath == null){
            return null;
        }
        return certInfo;
    }

    public void clearMDMSMIMESignCertPref(long accId){
        removeValues(PREFERENCE_MDM_SMIME_SIGN + accId);
        removeValues(PREFERENCE_MDM_SMIME_SIGN + accId + CERT_PATH);
        removeValues(PREFERENCE_MDM_SMIME_SIGN + accId + CERT_PASSWORD);
        removeValues(PREFERENCE_MDM_SMIME_SIGN + accId + CERT_RESULT_ID);
    }

    public void setMDMSmimeCertsAcc(long accID){
        String accIds = getValueString(PREFERENCE_MDM_PUSHED_SMIME_ACCOUNT , null);
        if(accIds == null || accIds.isEmpty()){
            putValue(PREFERENCE_MDM_PUSHED_SMIME_ACCOUNT, String.valueOf(accID));
        }
        else if(accIds != null && !accIds.contains(String.valueOf(accID))){
            putValue(PREFERENCE_MDM_PUSHED_SMIME_ACCOUNT, accIds + ","+String.valueOf(accID));
        }
    }
    public String getMDMSmimeCertsAcc(){
        return getValueString(PREFERENCE_MDM_PUSHED_SMIME_ACCOUNT , null);
    }
    public void removeAccountID(long accID){
        StringBuffer sb = new StringBuffer();
        String accounts = getValueString(PREFERENCE_MDM_PUSHED_SMIME_ACCOUNT , null);
        if(accounts == null){
            Log.d("Preferences", "Nothing in PREFERENCE_MDM_PUSHED_SMIME_ACCOUNT!!");
            return;
        }
        String[] acccountArr = accounts.split(",");
        int count = 0;
        for(String acc: acccountArr){
            count++;
            if(String.valueOf(accID).equals(acc)){
                continue;
            }else{
                sb.append(acc);
                if(count != acccountArr.length){
                    sb.append(",");
                }
            }
        }
        if(sb.toString() !=null && sb.toString().length() == 0){
            removeValues(PREFERENCE_MDM_PUSHED_SMIME_ACCOUNT);
        }else{
            putValue(PREFERENCE_MDM_PUSHED_SMIME_ACCOUNT, sb.toString());
        }
    }
    /**
     * Get an account object by Uri, or return null if no account exists TODO:
     * Merge hardcoded strings with the same strings in Account.java
     */
    public Account getAccountByContentUri(Uri uri) {
        if (!"content".equals(uri.getScheme()) || !"accounts".equals(uri.getAuthority())) {
            return null;
        }
        String uuid = uri.getPath().substring(1);
        if (uuid == null) {
            return null;
        }
        String accountUuids = getValueString(ACCOUNT_UUIDS, null);
        if (accountUuids == null || accountUuids.length() == 0) {
            return null;
        }
        String[] uuids = accountUuids.split(",");
        for (int i = 0, length = uuids.length; i < length; i++) {
            if (uuid.equals(uuids[i])) {
                return new Account(this, uuid);
            }
        }
        return null;
    }

    //siso@abhirao change for MDM CBA cert installation starts
    public void setMDMCBACertPref(String uuid, String certPass, String tempFileName){
        putValue(PREFERENCE_MDM_CBA_CERT + uuid, uuid);
        putValue(PREFERENCE_MDM_CBA_CERT + uuid + CBA_PASSWORD_TAG, certPass);
        putValue(PREFERENCE_MDM_CBA_CERT + uuid + CBA_CERT_FILE_TAG, tempFileName);
    }

    public MDMCBACertPref getMDMCBACertPref(String uuid){
        MDMCBACertPref certInfo = new MDMCBACertPref();
        certInfo.uuid = getValueString(PREFERENCE_MDM_CBA_CERT + uuid, null);
        certInfo.password = getValueString(PREFERENCE_MDM_CBA_CERT + uuid + CBA_PASSWORD_TAG, null);
        certInfo.fileName = getValueString(PREFERENCE_MDM_CBA_CERT + uuid + CBA_CERT_FILE_TAG, null);
        if(certInfo.uuid == null || certInfo.password == null || certInfo.fileName == null){
            return null;
        }
        return certInfo;
    }

    public void clearMDMCertPref(String uuid){

        removeValues(PREFERENCE_MDM_CBA_CERT + uuid);
        removeValues(PREFERENCE_MDM_CBA_CERT + uuid + CBA_PASSWORD_TAG);
        removeValues(PREFERENCE_MDM_CBA_CERT + uuid + CBA_CERT_FILE_TAG);
    }
    //siso@abhirao change for MDM CBA cert installation ends

    public boolean isRefreshOnOpen() {
        return getValueBoolean(REFRESH_ON_OPEN, true);
    }

    public void setRefreshOnOpen(boolean flag) {
        putValue(REFRESH_ON_OPEN, flag);
    }

    /* ++ Cross App Feature ++ */
    public void setAttachSheetTabPositionPref(int position) {
        putValue(PREFERENCE_ATTACH_SHEET_TAB_POSITION, position);
    }

    public int getAttachSheetTabPositionPref() {
        return getValueInt(PREFERENCE_ATTACH_SHEET_TAB_POSITION, 1);
    }
    /* -- Cross App Feature -- */

    /* Black list function */
    public void setStringBlackList(String value) {
        putValue(PREFERENCE_DROP_DOWN_BLACK_LIST, value);
    }

    public String getStringBlackList() {
        String temp = getValueString(PREFERENCE_DROP_DOWN_BLACK_LIST, null);
        return temp;
    }

    public void setAutoSyncTipsClosed(boolean close) {
        putValue(PREFERENCES_AUTO_SYNC_TIPS_CLOSED, close);
    }

    public boolean isAutoSyncTipsClosed() {
        boolean temp = getValueBoolean(PREFERENCES_AUTO_SYNC_TIPS_CLOSED, false);
        return temp;
    }


    public void removeBlackList() {
        removeValues(PREFERENCE_DROP_DOWN_BLACK_LIST);
    }
    /* Black list function */


    public int getAttachmentStorage() {
        return getValueInt(ATTACHMENT_STORAGE, DEFAULT_ATTACHMENT_STORAGE);
    }

    public void setAttachmentStorage(int attachmentStorage) {
        putValue(ATTACHMENT_STORAGE, attachmentStorage);
    }

    public int getSDCardStorageStatus() {
        return getValueInt(SDCARD_STORAGE_STATUS, -1);
    }

    public void setSDCardStorageStatus(int attachmentStorage) {
        putValue(SDCARD_STORAGE_STATUS, attachmentStorage);
    }

    public void setLastSyncTime(long time) {
        putValue(LAST_SYNC_TIME, time);
    }

    public long getLastSyncTime() {
        return getValueLong(LAST_SYNC_TIME, 0);
    }

    public void setUnreadCount(int unreadCount) {
        putValue(UNREAD_COUNT, unreadCount);
    }

    public int getUnreadCount() {
        return getValueInt(UNREAD_COUNT, -1);
    }
    //OAuth attributes backup and restore

    public void setOauthAccessToken(Account account, String accessToken) {
        putValue(account.mUuid + OAUTH_ACCESS_TOKEN, accessToken);
    }

    public void setOauthRefreshToken(Account account, String refreshToken) {
        putValue(account.mUuid + OAUTH_REFRESH_TOKEN, refreshToken);
    }
    public void setOauthProviderId(Account account, String providerId) {
        putValue(account.mUuid + OAUTH_PROVIDER_ID, providerId);
    }

    public void setOauthTokenExpiration(Account account, long tokenExpiration) {
        putValue(account.mUuid + OAUTH_TOKEN_EXPIRATION, tokenExpiration);
    }

    public String getOauthAccessToken(Account account) {
        return getValueString(account.mUuid + OAUTH_ACCESS_TOKEN, null);
    }

    public String getOauthRefreshToken(Account account) {
        return getValueString(account.mUuid + OAUTH_REFRESH_TOKEN, null);
    }

    public String getOauthProviderId(Account account) {
        return getValueString(account.mUuid + OAUTH_PROVIDER_ID, null);
    }

    public long getOauthTokenExpiration(Account account) {
        return getValueLong(account.mUuid + OAUTH_TOKEN_EXPIRATION, 0);
    }
}
