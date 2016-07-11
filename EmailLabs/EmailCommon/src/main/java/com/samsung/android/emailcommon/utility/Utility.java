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

package com.samsung.android.emailcommon.utility;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.drm.DrmInfo;
import android.drm.DrmInfoRequest;
import android.drm.DrmManagerClient;
import android.drm.DrmStore.DrmFileType;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersonaManager;
import android.os.StatFs;
import android.os.StrictMode;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.sec.enterprise.content.SecContentProviderURI;
import android.sec.enterprise.email.EnterpriseEmailAccount;
import android.sec.enterprise.email.EnterpriseEmailContentProviderURI;
import android.sec.enterprise.email.EnterpriseExchangeAccount;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.view.accessibility.AccessibilityManager;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.android.emailcommon.AccountCache;
import com.samsung.android.emailcommon.AccountManagerTypes;
import com.samsung.android.emailcommon.EmailFeature;
import com.samsung.android.emailcommon.EmailPackage;
import com.samsung.android.emailcommon.Logging;
import com.samsung.android.emailcommon.Preferences;
import com.samsung.android.emailcommon.mail.MeetingInfo;
import com.samsung.android.emailcommon.mail.PackedString;
import com.samsung.android.emailcommon.provider.AccountValues;
import com.samsung.android.emailcommon.provider.EmailContent;
import com.samsung.android.emailcommon.provider.EmailContent.Account;
import com.samsung.android.emailcommon.provider.EmailContent.AccountColumns;
import com.samsung.android.emailcommon.provider.EmailContent.Attachment;
import com.samsung.android.emailcommon.provider.EmailContent.AttachmentColumns;
import com.samsung.android.emailcommon.provider.EmailContent.FavoriteContact;
import com.samsung.android.emailcommon.provider.EmailContent.FavoriteContactColumns;
import com.samsung.android.emailcommon.provider.EmailContent.HostAuth;
import com.samsung.android.emailcommon.provider.EmailContent.HostAuthColumns;
import com.samsung.android.emailcommon.provider.EmailContent.Mailbox;
import com.samsung.android.emailcommon.provider.EmailContent.MailboxColumns;
import com.samsung.android.emailcommon.provider.EmailContent.Message;
import com.samsung.android.emailcommon.provider.EmailContentUtils;
import com.samsung.android.emailcommon.provider.FilterItem;
import com.samsung.android.emailcommon.system.AccountSetupbyCSC;
import com.samsung.android.emailcommon.system.CarrierValues;
import com.samsung.android.emailcommon.variant.CommonDefs;
import com.samsung.android.feature.FloatingFeature;
import com.samsung.android.telephony.MultiSimManager;
import com.sec.enterprise.knox.sdp.exception.SdpCompromisedException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Utility {
    private static final String TAG = "Utility";
    private static final String NO_NAME = "No name";
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Charset ASCII = Charset.forName("US-ASCII");
    public static final String[] EMPTY_STRINGS = new String[0];
    public static final Long[] EMPTY_LONGS = new Long[0];
    public static final String defaultPostmanURI = "/system/media/audio/notifications/S_Postman.ogg";
    public static final String defaultLetterURI = "/system/media/audio/notifications/Letter.ogg";
    private static final String DEFAULT_NOTIFICATION_SOUND = "notification_sound";

    public static int EXTRA_FONT_SIZE = 0;

    public static float FONT_TABLE1[] = new float[]{
            0.84f, 0.84f, 0.9f, 1.0f, 1.16f, 1.27f, 1.37f, 1.51f, 1.58f, 1.69f, 1.79f, 1.9f
    };

    public static int getExtraFontSize(int size) {
        if (EXTRA_FONT_SIZE >= 8 && EXTRA_FONT_SIZE <= 11)
            return (int) (size * FONT_TABLE1[EXTRA_FONT_SIZE] / 1.51f);
        return size;
    }

    // "GMT" + "+" or "-" + 4 digits
    private static final Pattern DATE_CLEANUP_PATTERN_WRONG_TIMEZONE = Pattern
            .compile("GMT([-+]\\d{4})$");

    public static final int[] SEND_OUTBOX_DURATION_MINUTES = new int[]{
            0, 1, 3, 5, 10, 30, 60, 180, 360, 720, 1440
    };

    private static final int SECONDS = 1000;
    private static final int MINUTES = 60 * SECONDS;

    // KNOX_EAS_START Single
    // App -> Agent
    public static final String SSO_ADD_ACCOUNT = "com.sec.knox.ssoagent.add.REQUEST";
    public static final String SSO_MOD_ACCOUNT = "com.sec.knox.ssoagent.mod.REQUEST";
    public static final String SSO_ADD_RESULT_FROM_APP = "com.sec.knox.ssoagent.add.RESULT_FROM_APP";
    public static final String SSO_MOD_RESULT_FROM_APP = "com.sec.knox.ssoagent.mod.RESULT_FROM_APP";
    public static final String SSO_DEL_ACCOUNT = "com.sec.knox.ssoagent.del.ACCOUNT_DELETED";

    // Agent -> App
    public static final String SSO_ADD_ACCOUNT_INFO = "com.sec.knox.ssoagent.add.ACCOUNT_DETAIL";
    public static final String SSO_MOD_ACCOUNT_INFO = "com.sec.knox.ssoagent.mod.NEW_PASSWORD";
    public static final String SSO_ADD_RESULT_FROM_AGENT = "com.sec.knox.ssoagent.add.RESULT_FROM_AGENT";
    public static final String SSO_MOD_RESULT_FROM_AGENT = "com.sec.knox.ssoagent.mod.RESULT_FROM_AGENT";
    public static final String SSO_DEL_SYNCHRONIZE_DELETION = "com.sec.knox.ssoagent.del.SYNCHRONIZE_DELETION";
    public static final String SSO_EAS_DEBUG = "com.sec.knox.ssoagent.EAS_DEBUG";

    // App -> App
    public static final String SSO_ADD_ACCOUNT_COMPLETED = "com.samsung.android.email.ui.sso.add.ACCOUNT_COMPLETED";
    public static final String SSO_ADD_ACCOUNT_FAILED = "com.samsung.android.email.ui.sso.add.ACCOUNT_FAILED";
    public static final String SSO_MOD_ACCOUNT_FAILED = "com.samsung.android.email.ui.sso.mod.ACCOUNT_FAILED";
    public static final String SAMSUNGSINGLE = "samsungsingle";
    private static final String KNOX_PACKAGE_PREFIX = "sec_container_";
    public static final String SSOAGENT_PACKAGE_PREFIX = "sec_container_1.com.sec.knox.ssoagent";
    public static final String MOBILEDESK_PACKAGE_PREFIX = "sec_container_1.com.sds.mobiledesk";
    public static final String COM_SEC_KNOX_SSOAGENT_USE_KNOX = "com.sec.knox.ssoagent.USE_KNOX";
    public static final String SDP_STATE_CHANGED_INTENT = "com.sec.sdp.SDP_STATE_CHANGED";
    private final static Uri MYSINGLE_CONTENT_URI = Uri
            .parse("content://com.sds.mobiledesk.provider.mobiledeskextends/knoxContent");
    private final static String KNOXCONTENT_KEY_TYPE = "mobiledeskType";
    private final static String A_TYPE = "a";
    private final static String APPLICATION_TYPE = "application.type";
    private final static String EAS = "eas";
    public final static String EMAIL = "email";
    private final static String EMAILADDRESS = "user.email";
    public final static String FLOW_MODE = "flow_mode";
    // KNOX_EAS_END Single

    // change@siso to find out the type of meeting start
    public static final int SINGLE_MEETING_EVENT = 0;
    public static final int MASTER_RECURRENCE_MEETING_EVENT = 1;
    public static final int SINGLE_INSTANCE_OF_RECURRENCE_MEETING_EVENT = 2;
    public static final int EXCEPTION_OF_RECURRENCE_MEETING_EVENT = 3;

    // change@siso to find out the type of meeting end
    public final static String NO_DEVICE_ID = "NoDeviceID";
    private static final String OMA_PLUGIN_MIME = "application/vnd.oma.drm.content";
    public static final int TYPE_DRMTYPE_NONE = DrmFileType.DRM2_TYPE_UNDEFINE;
    public static final int TYPE_DRMTYPE_SD = DrmFileType.DRM2_TYPE_SD;
    public static final int TYPE_DRMTYPE_SSD = DrmFileType.DRM2_TYPE_SSD;
    public static Intent mStartWithIntent = null;
    public static final String CSCDATA_FILENAME = "CSCDATA_EmailEAS";
    public static final boolean IS_DUAL_SIM_MODEL = MultiSimManager.getSimSlotCount() == 2;
    public static final String[] KEY_CSC_FIELD = {
            // customer.xml
            "AutoAdvance", "ConfirmDelete", "AutoResendTime", "PollTime", "UseSSL",
            "AcceptAllSSLCert", "SyncCalendar", "SyncContacts", "RoamingSetting", "Signature",
            "PeakDuration", "OffPeakDuration", "PeakDayStart", "PeakDayEnd", "PeakTimeStart",
            "PeakTimeEnd", "PeriodEmail", "SizeEmail", "PeriodCalendar", "SyncTasks", "SyncSms",
            "MessageTone"
    };

    public static final String[] KEY_CSC = {
            // customer
            "Settings.Messages.Email.AutoAdvance", "Settings.Messages.Email.ConfirmDelete",
            "Settings.Messages.Email.EmailSending.AutoResendTime",
            "Settings.Messages.Email.EmailReceiving.PollTime", "Settings.ActiveSync.UseSSL",
            "Settings.ActiveSync.AcceptAllSSLCert", "Settings.ActiveSync.SyncCalendar",
            "Settings.ActiveSync.SyncContacts", "Settings.ActiveSync.RoamingSetting",
            "Settings.ActiveSync.Signature", "Settings.ActiveSync.SyncSchedule.PeakDuration",
            "Settings.ActiveSync.SyncSchedule.OffPeakDuration",
            "Settings.ActiveSync.SyncSchedule.PeakDayStart",
            "Settings.ActiveSync.SyncSchedule.PeakDayEnd",
            "Settings.ActiveSync.SyncSchedule.PeakTimeStart",
            "Settings.ActiveSync.SyncSchedule.PeakTimeEnd",
            "Settings.ActiveSync.EasEmail.PeriodEmail", "Settings.ActiveSync.EasEmail.SizeEmail",
            "Settings.ActiveSync.EasCalendar.PeriodCalendar", "Settings.ActiveSync.SyncTasks",
            "Settings.ActiveSync.SyncSms", "Settings.Sound.MessageTone"
    };

    public static String[] FILTER_PROJECTION = new String[]{
            EmailContent.FilterListColumns.ID, EmailContent.FilterListColumns.CONTACT_ID,
            EmailContent.FilterListColumns.EMAIL_ID, EmailContent.FilterListColumns.EMAIL_ADDRESS,
            EmailContent.FilterListColumns.DISPLAY_NAME,
            EmailContent.FilterListColumns.SUBJECT_NAME,
            EmailContent.FilterListColumns.FOLDER_NAME,
    };

    public static String[] VIP_PROJECTION = new String[]{
            EmailContent.VIPListColumns.ID, EmailContent.VIPListColumns.CONTACT_ID,
            EmailContent.VIPListColumns.EMAIL_ID, EmailContent.VIPListColumns.EMAIL_ADDRESS,
            EmailContent.VIPListColumns.DISPLAY_NAME, EmailContent.VIPListColumns.SENDER_ORDER
    };

    public static boolean isTaskSyncable() {
        if (!FloatingFeature.getInstance().getEnableStatus("SEC_FLOATING_FEATURE_CALENDAR_SUPPORT_TASK", true))
            return false;
        return true;
    }

    public static final String SAVE_SIGNATURE_TMPSAMMFILE_PATH = "signature_handWriting_";
    public static final int DEFAULT_ROAMING_PREFERENCE_SPRINT = 1;

    public static boolean sEnableBroser = false;
    public static boolean sEnableCall = false;
    public static boolean sEnableMessage = false;
    public static boolean sEnableCalendar = false;
    public static boolean sEnableContact = false;
    public static boolean sEnableMap = false;

    private static boolean sAfwMode = false;
    // START DLP
    public static final String CONTENT = "content://";
    public static final String AUTHORITY = "com.sec.knox.provider";
    public static String DLPPOLICY_IS_ALLOWEDTO_SHARE = "isAllowedToShare";
    public static String DLPPOLICY_IS_DLP_ACTIVATED = "isDLPActivated";
    private static String DLPPOLICY = "DlpPolicy";
    private static String DLP_POLICY_URI = CONTENT + AUTHORITY + "/" + DLPPOLICY;
    // STOP DLP

    // P140121-09647 --> Split view mode is missing under email settings_T0VZW
    // Model
    public static boolean isNoteModel() {
        if (/*
             * (currentActivity.getBaseContext().getPackageManager().
             * hasSystemFeature("com.sec.feature.spen_usp")) ||
             */CarrierValues.IS_DEVICE_H || CarrierValues.IS_DEVICE_MELIUS
                || CarrierValues.IS_DEVICE_CRATER || CarrierValues.IS_DEVICE_FLTE
                || CarrierValues.IS_DEVICE_MS01 || CarrierValues.IS_DEVICE_H3GDUOSCTC
                || CarrierValues.IS_DEVICE_T0 || CarrierValues.IS_DEVICE_NOBLE
                || CarrierValues.IS_DEVICE_A8 || CarrierValues.IS_DEVICE_A8_JP
                || CarrierValues.IS_DEVICE_ZEN || CarrierValues.IS_DEVICE_A9
                || CarrierValues.IS_DEVICE_HERO2 || CarrierValues.IS_DEVICE_HERO2_JP
                || CarrierValues.IS_DEVICE_J7 || CarrierValues.IS_DEVICE_A7
                || CarrierValues.IS_DEVICE_GRACE)
            return true;
        else
            return false;
    }

    public static boolean isTabletModel() {
        String deviceType = CarrierValues.BUILD_CHARACTERISTICS;
        if (deviceType != null && deviceType.contains("tablet"))
            return true;
        return false;
    }

    public static boolean isTabletModelForVzw() {
        return (isTabletModel() && CarrierValues.IS_CARRIER_VZW);
    }

    public static boolean isSmallScreenTabletModel() {
        if (CarrierValues.IS_DEVICE_TABE || CarrierValues.IS_DEVICE_TABEX)
            return true;
        if (CarrierValues.IS_DEVICE_DEGAS)
            return true;
        return false;
    }

    public static boolean isFonbletModel() {
        if (CarrierValues.IS_DEVICE_MEGA2)
            return true;
        if (CarrierValues.IS_DEVICE_A8 || CarrierValues.IS_DEVICE_A9)
            return true;
        if (CarrierValues.IS_DEVICE_ZEN)
            return true;
        return false;
    }

    public static boolean isUpgradeModel() {
        if (CarrierValues.IS_DEVICE_KONA | CarrierValues.IS_DEVICE_T0
                | CarrierValues.IS_DEVICE_GOLDEN)
            return false;
        else
            return true;
    }

    public static boolean isCreateGroup() {
        if (CarrierValues.IS_DEVICE_YPLATFORM)
            return false;
        return true;
    }

    public static boolean isMainlandChinaModel() {

        return CarrierValues.CHINA_MODEL;
    }

    public final static String readInputStream(InputStream in, String encoding) throws IOException {
        InputStreamReader reader = new InputStreamReader(in, encoding);
        StringBuffer sb = new StringBuffer();
        int count;
        char[] buf = new char[512];
        while ((count = reader.read(buf)) != -1) {
            sb.append(buf, 0, count);
        }
        return sb.toString();
    }

    public final static boolean arrayContains(Object[] a, Object o) {
        for (int i = 0, count = a.length; i < count; i++) {
            if (a[i].equals(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a concatenated string containing the output of every Object's
     * toString() method, each separated by the given separator character.
     */

    public static String combine(Object[] parts, char separator) {
        if (parts == null) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < parts.length; i++) {
            sb.append(parts[i].toString());
            if (i < parts.length - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    public static String base64Decode(String encoded) {
        if (encoded == null) {
            return null;
        }
        byte[] decoded = Base64.decode(encoded, Base64.DEFAULT);
        return new String(decoded);
    }

    public static String base64Encode(String s) {
        if (s == null) {
            return s;
        }
        return Base64.encodeToString(s.getBytes(), Base64.NO_WRAP);
    }

    public static boolean isTextViewNotEmpty(TextView view) {
        if (view != null) {
            return !TextUtils.isEmpty(view.getText());
        }
        return false;
    }

    public static boolean isPortFieldValid(TextView view) {
        CharSequence chars = view.getText();
        if (TextUtils.isEmpty(chars))
            return false;
        Integer port;
        // In theory, we can't get an illegal value here, since the field is
        // monitored for valid
        // numeric input. But this might be used elsewhere without such a check.
        try {
            port = Integer.parseInt(chars.toString());
        } catch (NumberFormatException e) {
            return false;
        }
        return port > 0 && port < 65536;
    }

    /**
     * Validate a hostname name field. Because we just use the {@link URI} class
     * for validation, it'll accept some invalid host names, but it works well
     * enough...
     */
    public static boolean isServerNameValid(TextView view) {
        return isServerNameValid(view.getText().toString());
    }

    public static boolean isServerNameValid(String serverName) {
        serverName = serverName.trim();
        if (TextUtils.isEmpty(serverName)) {
            return false;
        }

        try {
            new URI("http", null, serverName, -1, null, // path
                    null, // query
                    null);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    /**
     * Ensures that the given string starts and ends with the double quote
     * character. The string is not modified in any way except to add the double
     * quote character to start and end if it's not already there. TODO: Rename
     * this, because "quoteString()" can mean so many different things. sample
     * -> "sample" "sample" -> "sample" ""sample"" -> "sample"
     * "sample"" -> "sample" sa"mp"le -> "sa"mp"le" "sa"mp"le" -> "sa"mp"le"
     * (empty string) -> "" " -> ""
     */
    public static String quoteString(String s) {
        if (s == null) {
            return null;
        }
        if (!s.matches("^\".*\"$")) {
            return "\"" + s + "\"";
        } else {
            return s;
        }
    }

    /**
     * Apply quoting rules per IMAP RFC, quoted = DQUOTE *QUOTED-CHAR DQUOTE
     * QUOTED-CHAR = <any TEXT-CHAR except quoted-specials> / "\"
     * quoted-specials quoted-specials = DQUOTE / "\" This is used primarily for
     * IMAP login, but might be useful elsewhere. NOTE: Not very efficient - you
     * may wish to preflight this, or perhaps it should check for trouble chars
     * before calling the replace functions.
     *
     * @param s The string to be quoted. @return A copy of the string, having
     *          undergone quoting as described above
     */
    public static String imapQuoted(String s) {
        // First, quote any backslashes by replacing \ with \\
        // regex Pattern: \\ (Java string const = \\\\)
        // Substitute: \\\\ (Java string const = \\\\\\\\)
        String result = s.replaceAll("\\\\", "\\\\\\\\");
        // Then, quote any double-quotes by replacing " with \"
        // regex Pattern: " (Java string const = \")
        // Substitute: \\" (Java string const = \\\\\")
        result = result.replaceAll("\"", "\\\\\"");
        // return string with quotes around it
        return "\"" + result + "\"";
    }

    /**
     * A fast version of URLDecoder.decode() that works only with UTF-8 and does
     * only two allocations. This version is around 3x as fast as the standard
     * one and I'm using it hundreds of times in places that slow down the UI,
     * so it helps.
     */
    public static String fastUrlDecode(String s) {
        try {
            byte[] bytes = s.getBytes("UTF-8");
            byte ch;
            int length = 0;
            for (int i = 0, count = bytes.length; i < count; i++) {
                ch = bytes[i];
                if (ch == '%') {
                    int h = (bytes[i + 1] - '0');
                    int l = (bytes[i + 2] - '0');
                    if (h > 9) {
                        h -= 7;
                    }
                    if (l > 9) {
                        l -= 7;
                    }

                    bytes[length] = (byte) ((h << 4) | l);
                    i += 2;
                } else if (ch == '+') {
                    bytes[length] = ' ';
                } else {
                    bytes[length] = bytes[i];
                }
                length++;
            }
            return new String(bytes, 0, length, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            return null;
        }
    }

    private final static String HOSTAUTH_WHERE_CREDENTIALS = HostAuthColumns.ADDRESS + " like ?"
            + " and " + HostAuthColumns.LOGIN + " like ?" + " and " + HostAuthColumns.PROTOCOL
            + " not like \"smtp\"";
    private final static String ACCOUNT_WHERE_HOSTAUTH = AccountColumns.HOST_AUTH_KEY_RECV + "=?";
    private final static String[] EMAILADDRESS_ACCOUNTID_PROJECTION = new String[]{
            Account.RECORD_ID, Account.ADDRESS, Account.DISPLAYNAME, Account.HOST_AUTH_KEY_RECV
    };

    /**
     * Look for an existing account with the same username & server
     *
     * @param context a system context @param allowAccountId this account Id
     *                will not trigger (when editing an existing account) @param
     *                hostName the server's address @param userLogin the user's
     *                login string @result null = no matching account found. Account
     *                = matching account
     */
    public static boolean findExistingAccountEmail(Context context, String email, long hostAuthId) {
        ContentResolver resolver = context.getContentResolver();
        Cursor c = resolver.query(Account.CONTENT_URI, EMAILADDRESS_ACCOUNTID_PROJECTION,
                Account.HOST_AUTH_KEY_RECV + "=" + hostAuthId, null, null);
        try {
            while (c.moveToNext()) {
                String emailAddress = c.getString(Account.EMAILADDRESS_PROJECTION_COLUMN);
                if (emailAddress.equalsIgnoreCase(email)) {
                    // EmailLog.d(TAG, "Duplicate Entry Found in the DB");
                    return true;
                }
            }
        } finally {
            c.close();
        }
        return false;
    }

    // starov.ilya: Check for accounts with specified email address and protocol

    /**
     * Look for an existing account with the same email address and protocol
     *
     * @param context - a system context @param email - email address of account @param
     *                protocol - account protocol ("eas", "imap", "pop3") @result
     *                null = no matching account found. Account = matching account
     */
    public static Account findExistingAccountEmailProtocol(Context context, String email,
                                                           String protocol) {
        if (context == null || email == null || protocol == null)
            return null;
        ContentResolver resolver = context.getContentResolver();
        // Query all accounts with specified email address
        String[] selectionArgs = new String[1];
        selectionArgs[0] = email;
        Cursor c = resolver.query(Account.CONTENT_URI, new String[]{
                Account.RECORD_ID, Account.HOST_AUTH_KEY_RECV
        }, Account.EMAIL_ADDRESS + "=?" + " COLLATE NOCASE", selectionArgs, null);
        if (c != null) {
            try {
                HostAuth ha = null;
                while (c.moveToNext()) {
                    // Check protocol for each account. We can have only one
                    // account with same email address and protocol
                    long accountId = c.getLong(0);
                    long hostAuthId = c.getLong(1);
                    ha = HostAuth.restoreHostAuthWithId(context, hostAuthId);
                    if (ha != null && protocol.equalsIgnoreCase(ha.mProtocol)) {
                        Log.d(TAG, "Account with email address: " + email + " and protocol: "
                                + protocol + " already exists in DB");
                        return Account.restoreAccountWithId(context, accountId);
                    }
                }
            } finally {
                c.close();
            }
        }
        return null;
    }

    // End of starov.ilya: Check for accounts with specified email address and protocol
    public static Account findExistingAccount(Context context, long allowAccountId,
                                              String hostName, String userLogin) {
        return findExistingAccount(context, allowAccountId, hostName, userLogin, null);
    }

    public static Account findExistingAccount(Context context, long allowAccountId,
                                              String hostName, String userLogin, String email) {
        String username = null;
        if (userLogin != null) {
            String[] emailParts = userLogin.split("@");
            if (emailParts != null && emailParts.length > 0) {
                username = emailParts[0].trim();
            }
        }
        // BTBTeam_Android_HQ_Email_IlyaStarovoytov : 20120503 : Prevent
        // creating same EAS account twice (P120509-0180)
        if (username != null) {
            String[] userNameParts = username.split("\\\\");
            if (userNameParts != null && userNameParts.length > 1)
                username = userNameParts[1].trim();
        }

        // End of BTBTeam_Android_HQ_Email_IlyaStarovoytov : 20120503 : Prevent
        // creating same EAS account twice (P120509-0180)
        ContentResolver resolver = context.getContentResolver();
        Cursor c = null;

        try {
            c = resolver.query(HostAuth.CONTENT_URI, new String[]{
                    HostAuth.ID, HostAuth.LOGIN
            }, HOSTAUTH_WHERE_CREDENTIALS, new String[]{
                    hostName, '%' + username + '%'
                    // BTBTeam_Android_HQ_Email_IlyaStarovoytov : 20120503 : Include
                    // accounts with non-empty domain name like this:
                    // google/p1234test@gmail.com
            }, null);

            if (c != null) {
                while (c.moveToNext()) {
                    long hostAuthId = c.getLong(HostAuth.ID_PROJECTION_COLUMN);
                    String Login = c.getString(1);
                    String[] emailPartsDB = Login.split("@");
                    String usernameDB = emailPartsDB[0].trim();
                    // BTBTeam_Android_HQ_Email_IlyaStarovoytov : 20120503 : Check
                    // accounts with non-empty domain name like this:
                    // google/p1234test@gmail.com
                    if (usernameDB != null) {
                        String[] userNameDbParts = usernameDB.split("\\\\");
                        if (userNameDbParts != null && userNameDbParts.length > 1)
                            usernameDB = userNameDbParts[1].trim();
                    }

                    // End of BTBTeam_Android_HQ_Email_IlyaStarovoytov : 20120503 :
                    // Check accounts with non-empty domain name like this:
                    // google/p1234test@gmail.com
                    if (username != null && !username.equalsIgnoreCase(usernameDB)) { // check
                        // p1234test@gmail.com
                        // / p1234test ->
                        // host id / host
                        // address
                        continue;
                    }

                    if (null != email && false == findExistingAccountEmail(context, email, hostAuthId)) { // check
                        continue;
                    }

                    // Find account with matching hostauthrecv key, and return it
                    Cursor c2 = null;
                    try {
                        c2 = resolver.query(Account.CONTENT_URI, Account.ID_PROJECTION,
                                ACCOUNT_WHERE_HOSTAUTH, new String[]{
                                        Long.toString(hostAuthId)
                                }, null);

                        if (c2 != null) {
                            while (c2.moveToNext()) {
                                long accountId = c2.getLong(Account.ID_PROJECTION_COLUMN);
                                if (accountId != allowAccountId) {
                                    Account account = Account.restoreAccountWithId(context, accountId);
                                    if (account != null) {
                                        return account;
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                    } finally {
                        try {
                            if (c2 != null)
                                c2.close();
                        } catch (Exception e) {
                        }
                    }
                }

                // starov.ilya: If account was not found until this point, try last
                // filter: search for account by email address and protocol only
                // We should not allow to create accounts with same email address
                // and protocol, but with different host name.
                // For example, sectest374@zionex.co.kr can be setup using EAS host
                // name zionex.co.kr or mail2.zionex.co.kr
                if (email != null) {
                    // don't allow user to create the same account with different
                    // protocol
                    if (findDuplicateAccount(context, email) != null) {
                        Log.d(TAG, "Duplicate Account !!!!");
                        Account account = Account.restoreAccountWithEmailAddress(context, email);
                        if (account != null && account.mId != allowAccountId) {
                            return account;
                        }
                    }
                }
            }
        } catch (Exception e) {
        } finally {
            try {
                if (c != null)
                    c.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * Generate a random message-id header for locally-generated messages.
     */
    public static String generateMessageId() {
        StringBuffer sb = new StringBuffer();
        sb.append("<");
        for (int i = 0; i < 24; i++) {
            sb.append(Integer.toString((int) (Math.random() * 35), 36));
        }
        sb.append(".");
        sb.append(Long.toString(System.currentTimeMillis()));
        sb.append("@email.android.com>");
        return sb.toString();
    }

    /**
     * Generate a time in milliseconds from a date string that represents a
     * date/time in GMT
     *
     * @param date string in format 20090211T180303Z (rfc2445, iCalendar). @return
     *             the time in milliseconds (since Jan 1, 1970)
     */
    public static long parseDateTimeToMillis(String date) {
        GregorianCalendar cal = parseDateTimeToCalendar(date);
        return cal.getTimeInMillis();
    }

    /**
     * Generate a GregorianCalendar from a date string that represents a
     * date/time in GMT
     *
     * @param date string in format 20090211T180303Z (rfc2445, iCalendar). @return
     *             the GregorianCalendar
     */
    public static GregorianCalendar parseDateTimeToCalendar(String date) {
        GregorianCalendar cal = new GregorianCalendar(Integer.parseInt(date.substring(0, 4)),
                Integer.parseInt(date.substring(4, 6)) - 1, Integer.parseInt(date.substring(6, 8)),
                Integer.parseInt(date.substring(9, 11)), Integer.parseInt(date.substring(11, 13)),
                Integer.parseInt(date.substring(13, 15)));
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        return cal;
    }

    /**
     * Generate a time in milliseconds from an email date string that represents
     * a date/time in GMT
     *
     * @param date string in format 2010-02-23T16:00:00.000Z (ISO 8601, rfc3339)
     *             or 20110613T020000Z @return the time in milliseconds (since
     *             Jan 1, 1970)
     */
    public static long parseEmailDateTimeToMillis(String date) {
        long retval = 0;
        if (date != null) {
            Log.v("Email Input Date format", date);
            GregorianCalendar cal = null;
            if (date.length() > 16) {
                cal = new GregorianCalendar(Integer.parseInt(date.substring(0, 4)),
                        Integer.parseInt(date.substring(5, 7)) - 1, Integer.parseInt(date
                        .substring(8, 10)), Integer.parseInt(date.substring(11, 13)),
                        Integer.parseInt(date.substring(14, 16)), Integer.parseInt(date.substring(
                        17, 19)));
                cal.setTimeZone(TimeZone.getTimeZone("GMT"));
            } else {
                cal = new GregorianCalendar(Integer.parseInt(date.substring(0, 4)),
                        Integer.parseInt(date.substring(4, 6)) - 1, Integer.parseInt(date
                        .substring(6, 8)), Integer.parseInt(date.substring(9, 11)),
                        Integer.parseInt(date.substring(11, 13)), Integer.parseInt(date.substring(
                        13, 15)));
            }
            retval = cal.getTimeInMillis();
            if (retval < 0) {
                Log.e("Email Input Date format", "bad transform : " + retval);
            }
        }
        return retval;
    }

    public static String convertEmailDateTimeToCalendarDateTime(String date) {
        // Format for email date strings is 2010-02-23T16:00:00.000Z
        // Format for calendar date strings is 20100223T160000Z
        return date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 13)
                + date.substring(14, 16) + date.substring(17, 19) + 'Z';
    }

    // change@siso to get the parentUID of exception event
    public static String getParentEventUID(String exceptionUID) {
        // Parent UID- 040000008200E00074C5B7101A82E0080 0000000
        // E6F325D8CC8DCE01000000000000000010000000953DE9A9BD764F4BB446960337D59288
        // ExceptionUID -040000008200E00074C5B7101A82E0080 7DD0802
        // E6F325D8CC8DCE01000000000000000010000000953DE9A9BD764F4BB446960337D59288
        if (exceptionUID.contains("{"))// in case of event from 2003 server UUID
            // of both parent event and exception
            // event always same
            return exceptionUID;
        int leftEnd = 33;
        if (exceptionUID.length() < leftEnd)
            return null;
        StringBuffer parentEventUidBuf = new StringBuffer();
        parentEventUidBuf.append(exceptionUID.substring(0, leftEnd));
        parentEventUidBuf.append("0000000");
        if (parentEventUidBuf.length() > exceptionUID.length())
            return null;
        parentEventUidBuf.append(exceptionUID.substring(parentEventUidBuf.length(),
                exceptionUID.length()));
        return parentEventUidBuf.length() == exceptionUID.length() ? parentEventUidBuf.toString()
                : null;
    }

    // change@siso Get the calendar event Id(single/recurrence/exception) start

    /**
     * delete the calendar event in calendar DB by using info of message object.
     *
     * @param context @param message @return
     */
    public static long getCalendarEventId(Context context, String info, long accountKey) {
        PackedString meetingInfo = new PackedString(info);
        String eventUID = meetingInfo.get(MeetingInfo.MEETING_UID);
        long originalInstanceTime = -1;
        String displayName = null;
        boolean isExceptionEvent = false;
        Long calendarEventId = null;
        int type = 0;
        String isResponseVCalType = meetingInfo.get(MeetingInfo.MEETING_RESPONSE_VCALTYPE);
        if (null != meetingInfo.get(MeetingInfo.MEETING_RECURRENCE_ID)) {
            Log.d(TAG, "MEETING_RECURRENCE_ID found in MeetingInfo of message ");
            originalInstanceTime = Utility.parseEmailDateTimeToMillis(meetingInfo
                    .get(MeetingInfo.MEETING_RECURRENCE_ID));
        }

        if (null != meetingInfo.get(MeetingInfo.MEETING_INSTANCE_TYPE)) {
            type = Integer.parseInt(meetingInfo.get(MeetingInfo.MEETING_INSTANCE_TYPE));
            switch (type) {
                case Utility.SINGLE_INSTANCE_OF_RECURRENCE_MEETING_EVENT:
                case Utility.EXCEPTION_OF_RECURRENCE_MEETING_EVENT:
                    if (isResponseVCalType.equalsIgnoreCase("0")) {
                        eventUID = Utility.getParentEventUID(eventUID);
                    }
                    Log.d(TAG, "Calculated UUID for exception event by logic is:" + eventUID);
                    isExceptionEvent = true;
            }
        }

        // P131214-00747 fix ANR if eventUID is null start
        if (eventUID == null) {
            return -1;
        }

        // /P131214-00747 fix end
        Cursor eventCursor = context.getContentResolver().query(Events.CONTENT_URI, new String[]{
                Events.CALENDAR_ID, Events._ID, Events.TITLE, Events.EVENT_LOCATION, Events.EVENT_TIMEZONE, Events.ALL_DAY,
                Events.AVAILABILITY, Events.ORGANIZER, Events._SYNC_ID
        }, Events.SYNC_DATA2 + "=?", new String[]{
                eventUID
        }, null);

        try {
            if (eventCursor != null && eventCursor.getCount() == 1) {
                if (eventCursor.moveToFirst()) {
                    calendarEventId = getEventIdFromEventCursor(context, eventCursor,
                            isExceptionEvent, originalInstanceTime);
                    if (calendarEventId == null && type == Utility.SINGLE_INSTANCE_OF_RECURRENCE_MEETING_EVENT) {
                        Account account = Account.restoreAccountWithId(context, accountKey);
                        if (account != null) {
                            String accountName = account.mEmailAddress;
                            Uri uri = Events.CONTENT_URI.buildUpon().appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                                    .appendQueryParameter(Calendars.ACCOUNT_NAME, accountName)
                                    .appendQueryParameter(Calendars.ACCOUNT_TYPE, AccountManagerTypes.TYPE_EXCHANGE)
                                    .build();
                            calendarEventId = insertEventToCalendarDB(context, uri, eventCursor, meetingInfo);
                        }
                    }
                }
            } else if (eventCursor != null && eventCursor.getCount() > 1) {
                // taebong.ha@Retrieve DisplayName from Account table
                // with Message.mAccountKey
                Log.d(TAG, "Two UUID are found in calendar Events DB");
                Cursor accountCursor = context.getContentResolver().query(Account.CONTENT_URI,
                        new String[]{
                                EmailContent.AccountColumns.DISPLAY_NAME
                        }, EmailContent.AccountColumns.ID + "=?", new String[]{
                                Long.toString(accountKey)
                        }, null);
                try {
                    if (accountCursor != null && accountCursor.moveToFirst()) {
                        displayName = accountCursor.getString(0);
                    }
                } finally {
                    if (accountCursor != null)
                        accountCursor.close();
                }
                eventCursor.moveToFirst();
                do {
                    String calendarId = eventCursor.getString(0);
                    Cursor calendarsCursor = context.getContentResolver().query(
                            Calendars.CONTENT_URI, new String[]{
                                    Calendars.ACCOUNT_NAME
                            }, Calendars._ID + "=?", new String[]{
                                    calendarId
                            }, null);

                    try {
                        if (calendarsCursor != null && calendarsCursor.moveToFirst()) {
                            if (displayName != null
                                    && displayName.equals(calendarsCursor.getString(0))) {
                                calendarEventId = getEventIdFromEventCursor(context, eventCursor,
                                        isExceptionEvent, originalInstanceTime);
                                if (calendarEventId == null && type == Utility.SINGLE_INSTANCE_OF_RECURRENCE_MEETING_EVENT) {
                                    Account account = Account.restoreAccountWithId(context, accountKey);
                                    if (account != null) {
                                        String accountName = account.mEmailAddress;
                                        Uri uri = Events.CONTENT_URI.buildUpon().appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                                                .appendQueryParameter(Calendars.ACCOUNT_NAME, accountName)
                                                .appendQueryParameter(Calendars.ACCOUNT_TYPE, AccountManagerTypes.TYPE_EXCHANGE)
                                                .build();
                                        calendarEventId = insertEventToCalendarDB(context, uri, eventCursor, meetingInfo);
                                    }
                                }
                            }
                        }
                    } finally {
                        if (calendarsCursor != null)
                            calendarsCursor.close();
                    }
                } while (eventCursor.moveToNext());
            }
        } finally {
            if (eventCursor != null)
                eventCursor.close();
        }
        return calendarEventId == null ? -1 : calendarEventId;
    }

    /**
     * Get the calendar event id by using the events cursor.
     *
     * @param context @param eventCursor @param isExceptionEvent @param
     *                originalInstanceTime @return
     */
    private static Long getEventIdFromEventCursor(Context context, Cursor eventCursor,
                                                  boolean isExceptionEvent, long originalInstanceTime) {
        Cursor exEventCursor = null;
        try {
            long originalId = eventCursor.getLong(eventCursor.getColumnIndex(Events._ID));
            if (isExceptionEvent) {
                Log.d(TAG, "Calendar meeting event is recurrence exception event");
                exEventCursor = context.getContentResolver().query(Events.CONTENT_URI,
                        new String[]{
                                Events._ID
                        }, Events.ORIGINAL_ID + "=? AND " + Events.ORIGINAL_INSTANCE_TIME + "= ?",
                        new String[]{
                                String.valueOf(originalId), Long.toString(originalInstanceTime)
                        }, null);

                if (exEventCursor != null && exEventCursor.moveToFirst()) {
                    Log.d(TAG, "Recurrence exception event found in Calendar DB");
                    return exEventCursor.getLong(exEventCursor.getColumnIndex(Events._ID));
                }
            } else {
                Log.d(TAG, "Calendar meeting event is single/recurrence meeting event");
                return originalId;
            }
        } finally {
            if (exEventCursor != null && !exEventCursor.isClosed())
                exEventCursor.close();
        }
        return null;
    }

    private static Long insertEventToCalendarDB(Context context, Uri uri, Cursor cursor, PackedString meetingInfo) {
        Long id = null;
        Uri uriInsert;
        long dtStart = parseEmailDateTimeToMillis(meetingInfo.get(MeetingInfo.MEETING_DTSTART));
        long dtEnd = parseEmailDateTimeToMillis(meetingInfo.get(MeetingInfo.MEETING_DTEND));
        ContentValues cv = new ContentValues();
        cv.put(Events.CALENDAR_ID, cursor.getInt(0));
        cv.put(Events.ORIGINAL_ID, cursor.getInt(1));
        cv.put(Events.TITLE, "Canceled: " + cursor.getString(2));
        cv.put(Events.EVENT_LOCATION, cursor.getString(3));
        cv.put(Events.EVENT_TIMEZONE, cursor.getString(4));
        cv.put(Events.ALL_DAY, cursor.getInt(5));
        cv.put(Events.AVAILABILITY, cursor.getInt(6));
        cv.put(Events.ORGANIZER, cursor.getString(7));
        cv.put(Events._SYNC_ID, cursor.getString(8) + '_'
                + convertEmailDateTimeToCalendarDateTime(meetingInfo.get(MeetingInfo.MEETING_DTSTART)));
        cv.put(Events.ORIGINAL_SYNC_ID, cursor.getString(8));
        cv.put(Events.DTSTART, dtStart);
        cv.put(Events.DTEND, dtEnd);
        cv.put(Events.ORIGINAL_INSTANCE_TIME, dtStart);
        cv.put(Events.ORIGINAL_ALL_DAY, cursor.getInt(5));
        cv.put(Events.LAST_DATE, dtEnd);
        uriInsert = context.getContentResolver().insert(uri, cv);
        id = Long.valueOf(uriInsert.getLastPathSegment());
        return id;
    }

    // change@siso Get the calendar event Id(single/recurrence/exception) start
    private static byte[] encode(Charset charset, String s) {
        if (s == null) {
            return null;
        }

        final ByteBuffer buffer = charset.encode(CharBuffer.wrap(s));
        final byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        return bytes;
    }

    private static String decode(Charset charset, byte[] b) {
        if (b == null) {
            return null;
        }
        final CharBuffer cb = charset.decode(ByteBuffer.wrap(b));
        return new String(cb.array(), 0, cb.length());
    }

    /**
     * Converts a String to UTF-8
     */
    public static byte[] toUtf8(String s) {
        return encode(UTF_8, s);
    }

    /**
     * Builds a String from UTF-8 bytes
     */
    public static String fromUtf8(byte[] b) {
        return decode(UTF_8, b);
    }

    /**
     * Converts a String to ASCII bytes
     */
    public static byte[] toAscii(String s) {
        return encode(ASCII, s);
    }

    /**
     * Builds a String from ASCII bytes
     */
    public static String fromAscii(byte[] b) {
        return decode(ASCII, b);
    }

    /**
     * @return true if the input is the first (or only) byte in a UTF-8
     * character
     */
    public static boolean isFirstUtf8Byte(byte b) {
        // If the top 2 bits is '10', it's not a first byte.
        return (b & 0xc0) != 0x80;
    }

    public static String byteToHex(int b) {
        return byteToHex(new StringBuilder(), b).toString();
    }

    public static StringBuilder byteToHex(StringBuilder sb, int b) {
        b &= 0xFF;
        sb.append("0123456789ABCDEF".charAt(b >> 4));
        sb.append("0123456789ABCDEF".charAt(b & 0xF));
        return sb;
    }

    public static String replaceBareLfWithCrlf(String str) {
        return str.replace("\r", "").replace("\n", "\r\n");
    }

    /**
     * Cancel an {@link AsyncTask}. If it's already running, it'll be
     * interrupted.
     */
    public static void cancelTaskInterrupt(AsyncTask<?, ?, ?> task) {
        cancelTask(task, true);
    }

    /**
     * Cancel an {@link AsyncTask}.
     *
     * @param mayInterruptIfRunning <tt>true</tt> if the thread executing this
     *                              task should be interrupted; otherwise, in-progress tasks are
     *                              allowed to complete.
     */
    public static void cancelTask(AsyncTask<?, ?, ?> task, boolean mayInterruptIfRunning) {
        if (task != null) {
            if (task.getStatus() != AsyncTask.Status.FINISHED) {
                task.cancel(mayInterruptIfRunning);
            }
            task = null;
        }
    }

    public static String getSmallHashForDeviceId(String value) {
        // add userId to deviceId for multi-user concept
        int userId = UserHandle.myUserId();
        Log.d(TAG, "myUserId : " + userId);
        if (userId != 0) {
            // add userId only for multi-user
            value = value + userId;
        }

        final MessageDigest sha;

        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException impossible) {
            return null;
        }

        byte[] bValue = Utility.toUtf8(value);
        if (bValue != null) {
            sha.update(bValue);
        }
        return getHexString(sha.digest());
    }

    public static String getHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < 6; i++)
            // for 12 digits string
            result = result.concat(String.format("%02x", b[i] & 0xff));
        String returnString = result.toString();
        return returnString.toUpperCase();
    }

    public static String getSmallHash(final String value) {
        final MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException impossible) {
            return null;
        }

        sha.update(Utility.toUtf8(value));
        final int hash = getSmallHashFromSha1(sha.digest());
        return Integer.toString(hash);
    }

    /**
     * @return a non-negative integer generated from 20 byte SHA-1 hash.
     */
    /* package for testing */
    static int getSmallHashFromSha1(byte[] sha1) {
        final int offset = sha1[19] & 0xf; // SHA1 is 20 bytes.
        return ((sha1[offset] & 0x7f) << 24) | ((sha1[offset + 1] & 0xff) << 16)
                | ((sha1[offset + 2] & 0xff) << 8) | ((sha1[offset + 3] & 0xff));
    }

    /**
     * Try to make a date MIME(RFC 2822/5322)-compliant. It fixes: -
     * "Thu, 10 Dec 09 15:08:08 GMT-0700" to "Thu, 10 Dec 09 15:08:08 -0700" (4
     * digit zone value can't be preceded by "GMT") We got a report saying eBay
     * sends a date in this format
     */
    public static String cleanUpMimeDate(String date) {
        if (TextUtils.isEmpty(date)) {
            return date;
        }
        date = DATE_CLEANUP_PATTERN_WRONG_TIMEZONE.matcher(date).replaceFirst("$1");
        return date;
    }

    public static ByteArrayInputStream streamFromAsciiString(String ascii) {
        if (ascii != null)
            return new ByteArrayInputStream(toAscii(ascii));
        else
            return null;
    }

    /**
     * A thread safe way to show a Toast. This method uses
     * {@link Activity#runOnUiThread}, so it can be called on any thread.
     *
     * @param activity Parent activity. @param resId Resource ID of the message
     *                 string.
     */
    public static void showToast(Activity activity, int resId) {
        showToast(activity, activity.getResources().getString(resId));
    }

    /**
     * A thread safe way to show a Toast. This method uses
     * {@link Activity#runOnUiThread}, so it can be called on any thread.
     *
     * @param activity Parent activity. @param message Message to show.
     */
    public static void showToast(final Activity activity, final String message) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Run {@code r} on a worker thread, returning the AsyncTask
     *
     * @return the AsyncTask; this is primarily for use by unit tests, which
     * require the result of the task
     */
    public static AsyncTask<Void, Void, Void> runAsync(final Runnable r) {
        return new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                r.run();
                return null;
            }
        }.execute();
    }

    /**
     * Interface used in {@link #createUniqueFile} instead of
     * {@link File#createNewFile()} to make it testable.
     */
    /* package */interface NewFileCreator {
        public static final NewFileCreator DEFAULT = new NewFileCreator() {
            @Override
            public boolean createNewFile(File f) throws IOException {
                return f.createNewFile();
            }
        };

        public boolean createNewFile(File f) throws IOException;
    }

    public static String substringByByteSizeUtf8(String source, int targetSize) throws IOException {
        if (source == null || TextUtils.isEmpty(source)) {
            return "";
        }
        int sourceSize = source.getBytes(UTF_8).length;
        if (source.equals("") || sourceSize <= targetSize) {
            return source;
        }
        int i = 0;
        String a = source;
        String temp = a.substring(0, 1);
        StringBuffer result = new StringBuffer("");
        while (i < targetSize) {
            if (temp.charAt(0) <= 0x00007F) {
                i++;
            } else if (temp.charAt(0) <= 0x0007FF) {
                i += 2;
            } else if (temp.charAt(0) <= 0x00FFFF) {
                i += 3;
            } else {
                i += 4;
            }
            if (i > targetSize) {
                break;
            }
            if (a == null || a.length() == 0) {
                break;
            }
            result.append(temp);
            a = a.substring(1);
            if (a.length() == 1) {
                temp = a;
            } else if (a.length() > 1) {
                temp = a.substring(0, 1);
            }
        }
        return result.toString();
    }

    /**
     * Creates a new empty file with a unique name in the given directory by
     * appending a hyphen and a number to the given filename.
     *
     * @return a new File object, or null if one could not be created
     */
    public static File createUniqueFile(File directory, String filename) throws IOException {
        return createUniqueFileInternal(NewFileCreator.DEFAULT, directory, filename);
    }

    /* package */
    static File createUniqueFileInternal(NewFileCreator nfc, File directory,
                                         String filename) throws IOException {
        File file = null;
        StringBuffer fileNameBuffer = new StringBuffer("");
        String format = "", name = "", extension = "";
        try {
            // change@siso.deepak Smime Encrypted Email Fix
            filename = filename.replace("/", "");
            int index = filename.lastIndexOf('.');
            if (index != -1) {
                name = filename.substring(0, index);
                if (name != null)
                    name.trim();
                else if (TextUtils.isEmpty(name))
                    name = NO_NAME;
                extension = filename.substring(index);
            }

            if (filename.getBytes(UTF_8).length > 200) {
                filename = substringByByteSizeUtf8(name, 200 - extension.getBytes(UTF_8).length);
                Log.d(Logging.LOG_TAG, "filename = " + filename);
                filename = fileNameBuffer.append(filename).append(extension).toString();
                Log.d(Logging.LOG_TAG, "filename = " + filename);
                Log.d(Logging.LOG_TAG, "filename Size : " + filename.getBytes(UTF_8).length);
            }

            filename.trim();
            if (TextUtils.isEmpty(filename))
                filename = NO_NAME;

            file = new File(directory, filename);

            if (nfc.createNewFile(file)) {
                return file;
            }
        } catch (NullPointerException e) {
            Log.e(Logging.LOG_TAG, "createUniqueFileInternal - NullPointerException=" + e);
            Log.e(Logging.LOG_TAG, "FileName [" + filename + "]\n");
            IOException ioe = new IOException();
            throw ioe;
        }

        // Get the extension of the file, if any.
        int index = filename.lastIndexOf('.');
        if (index != -1) {
            name = filename.substring(0, index);
            if (name != null)
                name.trim();
            if (TextUtils.isEmpty(name))
                name = NO_NAME;
            extension = filename.substring(index);
        }

        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            String addNum = "(" + String.valueOf(i) + ")";
            if (index != -1) {
                format = name + addNum + extension;
            } else {
                format = filename + addNum;
            }

            file = new File(directory, format);
            if (nfc.createNewFile(file)) {
                return file;
            }
        }
        return null;
    }

    /**
     * A class used to restore ListView state (e.g. scroll position) when
     * changing adapter.
     */
    public static class ListStateSaver implements Parcelable {
        private Parcelable mState;

        private ListStateSaver(Parcel p) {
            mState = p.readParcelable(getClass().getClassLoader());
        }

        public ListStateSaver(AbsListView lv) {
            try {
                mState = lv.onSaveInstanceState();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void restore(AbsListView lv) {
            try {
                lv.onRestoreInstanceState(mState);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(mState, flags);
        }

        public static final Parcelable.Creator<ListStateSaver> CREATOR = new Parcelable.Creator<ListStateSaver>() {
            public ListStateSaver createFromParcel(Parcel in) {
                return new ListStateSaver(in);
            }

            public ListStateSaver[] newArray(int size) {
                return new ListStateSaver[size];
            }
        };
    }

    public static boolean attachmentExists(Context context, Attachment attachment) {
        if (attachment == null) {
            android.util.Log.i(TAG, "called attachmentExists, attachment is null");
            return false;
        } else if (attachment.mContentBytes != null) {
            return true;
        } else if (TextUtils.isEmpty(attachment.mContentUri)) {
            android.util.Log.i(TAG, "called attachmentExists, attachment ID : " + attachment.mId + " mContentUri is null");
            return false;
        }

        try {
            Uri fileUri = Uri.parse(attachment.mContentUri);
            try {
                InputStream inStream = context.getContentResolver().openInputStream(fileUri);
                try {
                    inStream.close();
                } catch (IOException e) {
                    // Nothing to be done if can't close the stream
                }
                return true;
            } catch (FileNotFoundException e) {
                return false;
            }
        } catch (RuntimeException re) {
            Log.w(Logging.LOG_TAG, "attachmentExists RuntimeException=" + re);
            return false;
        }
    }

    public static boolean attachmentExistsAndHasRealData(Context context, Attachment attachment) {
        if (attachment == null) {
            android.util.Log.i(TAG, "called attachmentExists, attachment is null");
            return false;
        } else if (attachment.mContentBytes != null) {
            return true;
        } else if (TextUtils.isEmpty(attachment.mContentUri)) {
            android.util.Log.i(TAG, "called attachmentExistsAndHasRealData, attachment ID : " + attachment.mId + " mContentUri is null");
            return false;
        }
        try {
            Uri fileUri = Uri.parse(attachment.mContentUri);
            try {
                InputStream inStream = context.getContentResolver().openInputStream(fileUri);
                try {
                    int bytes = inStream.available();
                    if (bytes <= 0) {
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    try {
                        inStream.close();
                    } catch (IOException e) {
                        // Nothing to be done if can't close the stream
                    }
                }
                return true;
            } catch (FileNotFoundException e) {
                return false;
            }
        } catch (RuntimeException re) {
            Log.w(Logging.LOG_TAG, "attachmentExists RuntimeException=" + re);
            return false;
        }
    }

    /**
     * Check whether the message with a given id has unloaded attachments. If
     * the message is a forwarded message, we look instead at the messages's
     * source for the attachments. If the message or forward source can't be
     * found, we return false
     *
     * @param context the caller's context @param messageId the id of the
     *                message @return whether or not the message has unloaded
     *                attachments
     */
    public static boolean hasUnloadedAttachments(Context context, long messageId) {
        Message msg = Message.restoreMessageWithId(context, messageId);
        if (msg == null) {
            Log.d(Logging.LOG_TAG, "hasUnloadedAttachments. messageId is " + messageId
                    + " but msg is null");
            return false;
        }
        Attachment[] atts = Attachment.restoreAttachmentsWithMessageId(context, messageId);
        if (atts == null) {
            Log.d(Logging.LOG_TAG, "hasUnloadedAttachments. Attachment[] atts got null");
            return false;
        }

        for (Attachment att : atts) {
            if (!attachmentExists(context, att)) {
                // If the attachment doesn't exist and isn't marked for
                // download, we're in trouble
                // since the outbound message will be stuck indefinitely in the
                // Outbox. Instead,
                // we'll just delete the attachment and continue; this is far
                // better than the
                // alternative. In theory, this situation shouldn't be possible.
                if ((att.mFlags & (Attachment.FLAG_DOWNLOAD_FORWARD | Attachment.FLAG_DOWNLOAD_USER_REQUEST)) == 0) {
                    Log.d(Logging.LOG_TAG, "Unloaded attachment isn't marked for download: "
                            + att.mFileName + ", #" + att.mId);
                    // if((att.mFlags & Attachment.FLAG_SMART_FORWARD) == 0)
                    // {
                    // Log.d(Logging.LOG_TAG,
                    // "This attachment is not for smartsend");
                    Attachment.delete(context, Attachment.CONTENT_URI, att.mId);
                    // }
                    // else
                    // {
                    // //this attachment does not need to be downloaded.
                    // continue;
                    // }
                } else if (att.mContentUri != null) {
                    // In this case, the attachment file is gone from the cache;
                    // let's clear the
                    // contentUri; this should be a very unusual case
                    ContentValues cv = new ContentValues();
                    cv.putNull(AttachmentColumns.CONTENT_URI);
                    Attachment.update(context, Attachment.CONTENT_URI, att.mId, cv);
                } else if (att.mContentUri == null) {
                    try {
                        Attachment sourceAtt = Attachment
                                .restoreAttachmentWithMessageIdAndLocation(context,
                                        att.mMessageKey, att.mLocation);
                        if (sourceAtt != null
                                && (sourceAtt.mFlags & Attachment.FLAG_DOWNLOAD_COMPLETE) != 0) {
                            continue;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
            } else {
                if (!attachmentExistsAndHasRealData(context, att)) {
                    try {
                        Attachment sourceAtt = Attachment
                                .restoreAttachmentWithMessageIdAndLocation(context,
                                        att.mMessageKey, att.mLocation);
                        if (sourceAtt != null
                                && (sourceAtt.mFlags & Attachment.FLAG_DOWNLOAD_USER_REQUEST) != 0) {
                            return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    public static long remainedTimeUntilNextSendDuration(int retrySendTimes, long timestamp,
                                                         long currentmilliTime) {
        Log.d(TAG, "remainedTimeUntilNextSendDuration(): retrySendTimes :" + retrySendTimes);
        Log.d(TAG, "remainedTimeUntilNextSendDuration(): timestamp " + timestamp);
        Log.d(TAG, "remainedTimeUntilNextSendDuration(): currentmilliTime " + currentmilliTime);
        long currentMinuteTime = (currentmilliTime - (currentmilliTime % MINUTES)) / MINUTES;
        Log.d(TAG, "remainedTimeUntilNextSendDuration(): currentMinuteTime " + currentMinuteTime);
        // long remainedTime = timestamp - (currentMinuteTime * MINUTES);
        long remainedTime = timestamp - currentmilliTime;
        if (retrySendTimes <= 0 || timestamp <= 0 || remainedTime < (30 * SECONDS)) {
            Log.d(TAG, "remainedTimeUntilNextSendDuration(): sendDurationExceeded return 0.");
            return 0;
        }
        Log.d(TAG,
                "remainedTimeUntilNextSendDuration(): sendDurationExceeded return remainedTime : "
                        + remainedTime);
        return remainedTime;
    }

    public static long nextTimeAfterNextSendDuration(int retrySendTimes, long currentmilliTime) {
        Log.d(TAG, "nextTimeAfterNextSendDuration() : retrySendTimes :" + retrySendTimes);
        Log.d(TAG, "nextTimeAfterNextSendDuration() : currentmilliTime " + currentmilliTime);
        // long currentMinuteTime = (currentmilliTime - (currentmilliTime %
        // MINUTES)) / MINUTES;
        Log.d(TAG, "nextTimeAfterNextSendDuration() : currentmilliTime " + currentmilliTime);
        final int durationTableSize = SEND_OUTBOX_DURATION_MINUTES.length;
        int durationIndex = 0;
        if (retrySendTimes >= durationTableSize) {
            durationIndex = durationTableSize - 1;
        } else {
            durationIndex = retrySendTimes;
        }
        Log.d(TAG, "nextTimeAfterNextSendDuration() : durationIndex " + durationIndex);
        long nextTime = currentmilliTime + SEND_OUTBOX_DURATION_MINUTES[durationIndex] * MINUTES;
        Log.d(TAG, "nextTimeAfterNextSendDuration() : nextTime : " + nextTime);
        return nextTime;
    }

    public static boolean isExternalStorageMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * Class that supports running any operation for each account.
     */
    public abstract static class ForEachAccount extends AsyncTask<Void, Void, Long[]> {
        private final Context mContext;

        public ForEachAccount(Context context) {
            mContext = context;
        }

        @Override
        protected final Long[] doInBackground(Void... params) {
            ArrayList<Long> ids = new ArrayList<Long>();
            Cursor c = mContext.getContentResolver().query(EmailContent.Account.CONTENT_URI,
                    EmailContent.Account.ID_PROJECTION, null, null, null);
            try {
                while (c.moveToNext()) {
                    ids.add(c.getLong(EmailContent.Account.ID_PROJECTION_COLUMN));
                }
            } finally {
                c.close();
            }
            return ids.toArray(EMPTY_LONGS);
        }

        @Override
        protected final void onPostExecute(Long[] ids) {
            if (ids != null && !isCancelled()) {
                for (long id : ids) {
                    performAction(id);
                }
            }
            onFinished();
        }

        /**
         * This method will be called for each account.
         */
        protected abstract void performAction(long accountId);

        /**
         * Called when the iteration is finished.
         */

        protected void onFinished() {
        }
    }

    public static long[] toPrimitiveLongArray(Collection<Long> collection) {
        final int size = collection.size();
        final long[] ret = new long[size];
        // Collection doesn't have get(i). (Iterable doesn't have size())
        int i = 0;
        for (Long value : collection) {
            ret[i++] = value;
        }
        return ret;
    }

    /**
     * Workaround for the {@link ListView#smoothScrollToPosition} randomly
     * scroll the view bug if it's called right after
     * {@link ListView#setAdapter}.
     */

    public static void listViewSmoothScrollToPosition(final Activity activity,
                                                      final ListView listView, final int position) {
        // Workarond: delay-call smoothScrollToPosition()
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (activity.isFinishing()) {
                    return; // Activity being destroyed
                }
                listView.smoothScrollToPosition(position);
            }
        });
    }

    private static final String[] ATTACHMENT_META_NAME_PROJECTION = {
            OpenableColumns.DISPLAY_NAME
    };

    private static final int ATTACHMENT_META_NAME_COLUMN_DISPLAY_NAME = 0;

    /**
     * @return Filename of a content of {@code contentUri}. If the provider
     * doesn't provide the filename, returns the last path segment of
     * the URI.
     */

    public static String getContentFileName(Context context, Uri contentUri) {
        String name = EmailContentUtils.getFirstRowString(context, contentUri,
                ATTACHMENT_META_NAME_PROJECTION, null, null, null,
                ATTACHMENT_META_NAME_COLUMN_DISPLAY_NAME);
        if (name == null) {
            name = contentUri.getLastPathSegment();
        }
        return name;
    }

    /**
     * Append a bold span to a {@link SpannableStringBuilder}.
     */
    public static SpannableStringBuilder appendBold(SpannableStringBuilder ssb, String text) {
        if (!TextUtils.isEmpty(text)) {
            SpannableString ss = new SpannableString(text);
            ss.setSpan(new StyleSpan(Typeface.BOLD), 0, ss.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.append(ss);
        }
        return ssb;
    }

    /**
     * Cursor wrapper that remembers where it was closed. Use {@link #get} to
     * create a wrapped cursor. USe {@link #getTraceIfAvailable} to get the
     * stack trace. Use {@link #log} to log if/where it was closed.
     */
    public static class CloseTraceCursorWrapper extends CursorWrapper {
        private static final boolean TRACE_ENABLED = false;
        private Exception mTrace;

        private CloseTraceCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        @Override
        public void close() {
            mTrace = new Exception("STACK TRACE");
            super.close();
        }

        public static Exception getTraceIfAvailable(Cursor c) {
            if (c instanceof CloseTraceCursorWrapper) {
                return ((CloseTraceCursorWrapper) c).mTrace;
            } else {
                return null;
            }
        }

        public static void log(Cursor c) {
            if (c == null) {
                return;
            }

            if (c.isClosed()) {
                Log.w(Logging.LOG_TAG, "Cursor was closed here: Cursor=" + c,
                        getTraceIfAvailable(c));
            } else {
                Log.w(Logging.LOG_TAG, "Cursor not closed.  Cursor=" + c);
            }
        }

        @SuppressWarnings("resource")
        public static Cursor get(Cursor original) {
            return TRACE_ENABLED ? new CloseTraceCursorWrapper(original) : original;
        }

        /* package */
        static CloseTraceCursorWrapper alwaysCreateForTest(Cursor original) {
            return new CloseTraceCursorWrapper(original);
        }
    }

    /**
     * Create an {@link Intent} to launch an activity as the main entry point.
     * Existing activities will all be closed.
     */
    public static Intent createRestartAppIntent(Context context, Class<? extends Activity> clazz) {
        Intent i = new Intent(context, clazz);
        // i.setAction("android.intent.action.SHROTCUT");
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        long nSingTopFlag = 1;
        i.putExtra("IntentSingTop", nSingTopFlag);
        return i;
    }

    public static Intent createRestartOtherAppIntent(Context context, String pkgName) {
        Intent i = null;
        i = context.getPackageManager().getLaunchIntentForPackage(pkgName);
        // i.setAction(Intent.ACTION_MAIN);
        // i.addCategory(Intent.CATEGORY_LAUNCHER);
        if (i != null) {
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            long nSingTopFlag = 1;
            i.putExtra("IntentSingTop", nSingTopFlag);
        }
        return i;
    }

    public static Intent createRestartAppIntentFromWidget(Context context,
                                                          Class<? extends Activity> clazz) {
        Intent i = new Intent(context, clazz);
        // i.setAction("android.intent.action.SHROTCUT");
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    public static Intent createRestartAppIntentFromService(Context context,
                                                           Class<? extends Activity> clazz) {
        Intent i = new Intent(context, clazz);
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    /**
     * Legacy URI parser. Used in one of three different scenarios: 1. Backup /
     * Restore of account 2. Parsing template from provider.xml 3. Forcefully
     * creating URI for test
     */
    public static void setHostAuthFromString(HostAuth auth, String uriString)
            throws URISyntaxException {
        URI uri = new URI(uriString);
        String path = uri.getPath();
        String domain = null;
        if (path != null && path.length() > 0) {
            domain = path.substring(1);
        }
        auth.mDomain = domain;
        auth.setLogin(uri.getUserInfo());
        auth.setConnection(uri.getScheme(), uri.getHost(), uri.getPort());
    }

    /**
     * Test that the given strings are equal in a null-pointer safe fashion.
     */
    public static boolean areStringsEqual(String s1, String s2) {
        return (s1 != null && s1.equals(s2)) || (s1 == null && s2 == null);
    }

    public static void enableStrictMode(boolean enabled) {
        StrictMode.setThreadPolicy(enabled ? new StrictMode.ThreadPolicy.Builder().detectAll()
                .build() : StrictMode.ThreadPolicy.LAX);
        StrictMode.setVmPolicy(enabled ? new StrictMode.VmPolicy.Builder().detectAll().build()
                : StrictMode.VmPolicy.LAX);

    }

    // adapter porting Begin

    /**
     * @param accountId the account id @param mailboxType the mailbox type (e.g.
     *                  EmailContent.Mailbox.TYPE_TRASH) @return the id of the
     *                  mailbox. The mailbox is created if not existing. Returns
     *                  Mailbox.NO_MAILBOX if the accountId or mailboxType are
     *                  negative. Does not validate the input in other ways (e.g. does
     *                  not verify the existence of account).
     */
    public synchronized static long findOrCreateMailboxOfType(Context context, long accountId, int mailboxType,
                                                              String displayName) {
        if (accountId < 0 || mailboxType < 0) {
            return Mailbox.NO_MAILBOX;
        }
        long mailboxId = Mailbox.findMailboxOfType(context, accountId, mailboxType);
        return mailboxId == Mailbox.NO_MAILBOX ? createMailbox(context, accountId, mailboxType,
                displayName) : mailboxId;
    }

    /**
     * Create a mailbox given the account and mailboxType. TODO: Does this need
     * to be signaled explicitly to the sync engines?
     */
    static long createMailbox(Context context, long accountId, int mailboxType, String name) {
        if (accountId < 0 || mailboxType < 0) {
            String mes = "Invalid arguments " + accountId + ' ' + mailboxType;
            throw new RuntimeException(mes);
        }
        Mailbox box = new Mailbox();
        box.mAccountKey = accountId;
        box.mType = mailboxType;
        box.mSyncInterval = AccountValues.SyncTime.CHECK_INTERVAL_NEVER;
        box.mFlagVisible = true;
        box.mDisplayName = name;
        box.save(context);
        return box.mId;
    }

    public static String findDuplicateAccount(Context context, String email) {
        ContentResolver resolver = context.getContentResolver();
        Cursor c = resolver.query(Account.CONTENT_URI, Account.EMAILADDRESS_ACCOUNTID_PROJECTION,
                null, null, null);
        try {
            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    String emailAddress = c.getString(Account.EMAILADDRESS_PROJECTION_COLUMN);
                    if (emailAddress != null && emailAddress.equalsIgnoreCase(email)) {
                        // EmailLog.d(TAG, "Duplicate Entry Found in the DB");
                        String displayName = c.getString(Account.DISPLAYNAME_PROJECTION_COLUMN);
                        return ((displayName != null) ? (displayName) : (email));
                    }
                }
            }
        } finally {
            if (c != null && !c.isClosed())
                c.close();
        }
        return null;
    }

    /**
     * Look for an existing account with the same username & server
     *
     * @param context a system context @param allowAccountId this account Id
     *                will not trigger (when editing an existing account) @param
     *                hostName the server @param userLogin the user login string @result
     *                null = no dupes found. non-null = dupe account's display name
     */
    public static String findDuplicateAccount(Context context, long allowAccountId,
                                              String hostName, String userLogin) {
        ContentResolver resolver = context.getContentResolver();
        Cursor c = null;

        try {
            c = resolver.query(HostAuth.CONTENT_URI, HostAuth.ID_PROJECTION,
                    HOSTAUTH_WHERE_CREDENTIALS, new String[]{
                            hostName, userLogin
                    }, null);

            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    long hostAuthId = c.getLong(HostAuth.ID_PROJECTION_COLUMN);
                    // Find account with matching hostauthrecv key, and return
                    // its
                    // display name
                    Cursor c2 = null;
                    try {
                        c2 = resolver.query(Account.CONTENT_URI, Account.ID_PROJECTION,
                                ACCOUNT_WHERE_HOSTAUTH, new String[]{
                                        Long.toString(hostAuthId)
                                }, null);

                        if (c2 != null) {
                            while (c2.moveToNext()) {
                                long accountId = c2.getLong(Account.ID_PROJECTION_COLUMN);
                                if (accountId != allowAccountId) {
                                    Account account = Account.restoreAccountWithId(context, accountId);
                                    if (account != null) {
                                        return account.mDisplayName;
                                    }
                                }
                            }
                        }
                    } finally {
                        if (c2 != null && !c2.isClosed())
                            c2.close();
                    }
                }
            }
        } finally {
            if (c != null && !c.isClosed())
                c.close();
        }
        return null;
    }

    /**
     * Lightweight utility api to convert the syncLookback value to date
     */
    public static String convertSyncIntervalToDate(final int syncInterval) {
        if (syncInterval == EmailContent.Account.SYNC_WINDOW_ALL_MAILS) {
            Log.v(TAG, "Sync \"all\" emails");
            return "all";
        }

        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.US);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd", Locale.US);
        String[] date = {
                "0", "0", "0"
        };
        String frmtDate = sdf.format(cal.getTime());
        date = frmtDate.split(new String("-"));
        Log.v(TAG, "Today date : " + date[2] + "-" + date[1] + "-" + date[0]
                + " and syncInterval - " + syncInterval);
        // Subtract syncInterval(days) from the calendar
        cal.add(Calendar.DATE, -syncInterval);
        frmtDate = sdf.format(cal.getTime());
        date = frmtDate.split(new String("-"));
        String syncDate = date[2] + "-" + date[1] + "-" + date[0];
        Log.v(TAG, "Sync date be " + syncDate);
        return syncDate;
    }

    // adapter porting End

    /**
     * Converts Timestamp in long to Hour:Min:Sec:Microsec format
     *
     * @param timestamp
     * @return
     */
    public static String convertTimeStampToDate(final long timestamp) {
        String timeString = TimeUnit.MILLISECONDS.toDays(timestamp)
                + ":" + TimeUnit.MILLISECONDS.toHours(timestamp)
                + ":" + TimeUnit.MILLISECONDS.toMinutes(timestamp)
                + ":" + TimeUnit.MILLISECONDS.toSeconds(timestamp)
                + " [Days:Hr:Min:Sec]";
        return timeString;
    }

    /**
     * Builds an "in" expression for SQLite. e.g. "ID" + 1,2,3 ->
     * "ID in (1,2,3)". If {@code values} is empty or null, it returns an empty
     * string.
     */
    public static String buildInSelection(String columnName, Collection<? extends Number> values) {
        if ((values == null) || (values.size() == 0)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(columnName);
        sb.append(" in (");
        String sep = "";
        for (Number n : values) {
            sb.append(sep);
            sb.append(n.toString());
            sep = ",";
        }
        sb.append(')');
        return sb.toString();
    }

    // change@wtl.jpshu document search begin
    public static int byteArrayToInt(byte[] b) {
        return byteArrayToInt(b, 0);
    }

    public static int byteArrayToInt(byte[] b, int offset) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = i * 8;
            value += (b[i + offset] & 0x000000FF) << shift;
        }
        return value;
    }

    // change@wtl.jpshu document search end
    static public String CallStack() {
        StringBuffer stacktrace = new StringBuffer();
        StackTraceElement[] stacks = new Exception().getStackTrace();
        int maxDepth = 20;
        if (stacks.length < maxDepth)
            maxDepth = stacks.length;
        for (int i = 1; i < maxDepth; i++) {
            stacktrace.append(stacks[i].toString() + " ");
        }
        return stacktrace.toString();
    }

    public static boolean isNonPhone(Context context) {
        TelephonyManager mgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return mgr.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE;
    }

    public static boolean isWifiOnlyModel() {
        return "wifi-only".equalsIgnoreCase(CarrierValues.RO_CARRIER);
    }

    public static boolean isSupportSMS(Context context) {

        String messagePkgName = FloatingFeature.getInstance().getString("SEC_FLOATING_FEATURE_MESSAGE_CONFIG_PACKAGE_NAME", "com.android.mms");
        Log.d(TAG, "messagePkgName : " + messagePkgName);
        try {
            context.getPackageManager().getPackageInfo(messagePkgName, 0);
        } catch (NameNotFoundException e) {
            Log.d(TAG, "notSupportSMS");
            return false;
        }
        return true;
    }

    public static boolean notSupportSBrowser(Context context) {
        try {
            if (context != null) {
                context.getPackageManager().getPackageInfo("com.sec.android.app.sbrowser", 0);
            }
        } catch (NameNotFoundException e) {
            Log.d(TAG, "notSupportSBrowser");
            return true;
        } catch (NullPointerException npe) {
            Log.d(TAG, "NPE is occured");
            return true;
        }
        return false;
    }

    public static boolean notSupportMobilePrint(Context context) {
        try {
            context.getPackageManager().getPackageInfo("com.sec.android.app.mobileprint",
                    PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            Log.d(TAG, "notSupportMobilePrint");
            return true;
        } catch (NullPointerException npe) {
            Log.d(TAG, "NPE is occured");
            return true;
        }
        return false;
    }

    private static Handler sMainThreadHandler;

    /**
     * @return a {@link Handler} tied to the main thread.
     */
    public static Handler getMainThreadHandler() {
        if (sMainThreadHandler == null) {
            // No need to synchronize -- it's okay to create an extra Handler,
            // which will be used
            // only once and then thrown away.
            sMainThreadHandler = new Handler(Looper.getMainLooper());
        }
        return sMainThreadHandler;
    }

    public static String makeVaildRegularExp(String regularExpression) {
        String[] spChars = {
                "\\", "*", "+", "$", "|", "(", ")", "{", "}", "^", "[", "]", "\"", "?", "."
        };
        String ret = regularExpression;
        for (String invalidChar : spChars) {
            ret = ret.replace(invalidChar, "\\" + invalidChar);
        }
        return ret;
    }

    public static boolean isDuplicateFilterName(Context context, String filtername) {
        Cursor c = context.getContentResolver().query(EmailContent.FilterListColumns.CONTENT_URI,
                new String[]{
                        EmailContent.FilterListColumns.FOLDER_NAME
                }, EmailContent.FilterListColumns.FOLDER_NAME + "= ?" + " COLLATE NOCASE",
                new String[]{
                        filtername
                }, null);
        if (c == null) {
            Log.d("Utility", "isDuplicate : Cursor is null.");
            return false;
        }
        boolean res;
        if (c.getCount() > 0)
            res = true;
        else
            res = false;
        if (c != null) {
            c.close();
            c = null;
        }
        return res;
    }

    // Check filter exist or not, if editItem is not null, will igonre that row
    // in database.
    public static boolean isDuplicateFilterName(Context context, String filtername,
                                                FilterItem editItem) {
        String selection;
        String[] selectionArgs;
        if (editItem == null) {
            selection = EmailContent.FilterListColumns.FOLDER_NAME + "=?" + " COLLATE NOCASE";
            selectionArgs = new String[]{
                    filtername
            };
        } else {
            selection = EmailContent.FilterListColumns.FOLDER_NAME + "=? AND "
                    + EmailContent.FilterListColumns.ID + "<>?" + " COLLATE NOCASE";
            selectionArgs = new String[]{
                    filtername, String.valueOf(editItem.id)
            };
        }

        Cursor c = context.getContentResolver().query(EmailContent.FilterListColumns.CONTENT_URI,
                new String[]{
                        EmailContent.FilterListColumns.FOLDER_NAME
                }, selection, selectionArgs, null);
        if (c == null) {
            Log.d("Utility", "isDuplicate : Cursor is null.");
            return false;
        }
        boolean res;
        if (c.getCount() > 0)
            res = true;
        else
            res = false;
        if (c != null) {
            c.close();
            c = null;
        }
        return res;
    }

    public static boolean isDuplicateFilterSubjectName(Context context, String subject,
                                                       FilterItem editItem) {
        String selection;
        String[] selectionArgs;
        if (editItem == null) {
            selection = EmailContent.FilterListColumns.SUBJECT_NAME + "=?" + " COLLATE NOCASE";
            selectionArgs = new String[]{
                    subject
            };
        } else {
            selection = EmailContent.FilterListColumns.SUBJECT_NAME + "=? AND "
                    + EmailContent.FilterListColumns.ID + "<>?" + " COLLATE NOCASE";
            selectionArgs = new String[]{
                    subject, String.valueOf(editItem.id)
            };
        }

        Cursor c = context.getContentResolver().query(EmailContent.FilterListColumns.CONTENT_URI,
                new String[]{
                        EmailContent.FilterListColumns.SUBJECT_NAME
                }, selection, selectionArgs, null);
        if (c == null) {
            Log.d("Utility", "isDuplicate : Cursor is null.");
            return false;
        }
        boolean res;
        if (c.getCount() > 0)
            res = true;
        else
            res = false;
        if (c != null) {
            c.close();
            c = null;
        }
        return res;
    }

    public static boolean isDuplicateFilterAddress(Context context, String address,
                                                   FilterItem editItem) {
        String selection;
        String[] selectionArgs;
        if (editItem == null) {
            selection = EmailContent.FilterListColumns.EMAIL_ADDRESS + "=?" + " COLLATE NOCASE";
            selectionArgs = new String[]{
                    address
            };
        } else {
            selection = EmailContent.FilterListColumns.EMAIL_ADDRESS + "=? AND "
                    + EmailContent.FilterListColumns.ID + "<>?" + " COLLATE NOCASE";
            selectionArgs = new String[]{
                    address, String.valueOf(editItem.id)
            };
        }

        Cursor c = context.getContentResolver().query(EmailContent.FilterListColumns.CONTENT_URI,
                new String[]{
                        EmailContent.FilterListColumns.EMAIL_ADDRESS
                }, selection, selectionArgs, null);
        if (c == null) {
            Log.d("Utility", "isDuplicate : Cursor is null.");
            return false;
        }
        boolean res;
        if (c.getCount() > 0)
            res = true;
        else
            res = false;
        if (c != null) {
            c.close();
            c = null;
        }
        return res;
    }

    public static boolean addFilterList(Context context, String address, String subject,
                                        String folder) {
        Uri uri = Uri.parse("content://com.android.contacts/data/emails");
        final String[] PROJECTION = new String[]{
                "_id", "contact_id", "display_name",
        };
        Cursor con = context.getContentResolver().query(uri, PROJECTION,
                android.provider.ContactsContract.CommonDataKinds.Email.DATA + "= ?", new String[]{
                        String.valueOf(address)
                }, "creation_time desc");
        if (con == null) {
            Log.d("Utility", "addContact : 2 - Cursor is null.");
            return false;
        }

        int res_cnt = con.getCount();
        FilterItem item = new FilterItem();
        if (res_cnt > 0) {
            con.moveToFirst();
            item.address_id = con.getInt(0);
            item.contact_id = con.getInt(1);
            item.contact_name = con.getString(2);
        }

        if (con != null) {
            con.close();
            con = null;
        }

        item.address = address;
        item.subject_name = subject;
        item.folder_name = folder;

        ContentValues cv = new ContentValues();
        cv.put(EmailContent.FilterListColumns.CONTACT_ID, item.contact_id);
        cv.put(EmailContent.FilterListColumns.EMAIL_ID, item.address_id);
        cv.put(EmailContent.FilterListColumns.EMAIL_ADDRESS, item.address);
        cv.put(EmailContent.FilterListColumns.DISPLAY_NAME, item.contact_name);
        cv.put(EmailContent.FilterListColumns.SUBJECT_NAME, item.subject_name);
        cv.put(EmailContent.FilterListColumns.FOLDER_NAME, item.folder_name);
        context.getContentResolver().insert(EmailContent.FilterListColumns.CONTENT_URI, cv);
        return true;
    }

    // if editItem is null, will insert a new row to DB. Otherwise will update
    // the exist row.
    public static boolean addOrUpdateFilterList(Context context, String address, String subject,
                                                String folder, FilterItem editItem) {
        Uri uri = Uri.parse("content://com.android.contacts/data/emails");
        final String[] PROJECTION = new String[]{
                "_id", "contact_id", "display_name",
        };

        Cursor con = context.getContentResolver().query(uri, PROJECTION,
                android.provider.ContactsContract.CommonDataKinds.Email.DATA + "= ?", new String[]{
                        String.valueOf(address)
                }, "creation_time desc");

        if (con == null) {
            Log.d("Utility", "addContact : 2 - Cursor is null.");
            return false;
        }

        int res_cnt = con.getCount();
        FilterItem item = new FilterItem();

        if (res_cnt > 0) {
            con.moveToFirst();
            item.address_id = con.getInt(0);
            item.contact_id = con.getInt(1);
            item.contact_name = con.getString(2);
        }

        if (con != null) {
            con.close();
            con = null;
        }

        item.address = address;
        item.subject_name = subject;
        item.folder_name = folder;

        ContentValues cv = new ContentValues();

        cv.put(EmailContent.FilterListColumns.CONTACT_ID, item.contact_id);
        cv.put(EmailContent.FilterListColumns.EMAIL_ID, item.address_id);
        cv.put(EmailContent.FilterListColumns.EMAIL_ADDRESS, item.address);
        cv.put(EmailContent.FilterListColumns.DISPLAY_NAME, item.contact_name);
        cv.put(EmailContent.FilterListColumns.SUBJECT_NAME, item.subject_name);
        cv.put(EmailContent.FilterListColumns.FOLDER_NAME, item.folder_name);

        if (editItem == null)
            context.getContentResolver().insert(EmailContent.FilterListColumns.CONTENT_URI, cv);
        else
            context.getContentResolver().update(EmailContent.FilterListColumns.CONTENT_URI, cv,
                    EmailContent.FilterListColumns.ID + "=" + editItem.id, null);

        return true;
    }

    public static boolean addOrUpdateFilterList(Context context, String addressList,
                                                String subject, String folder, int updateId) {
        ContentValues cv = new ContentValues();
        cv.put(EmailContent.FilterListColumns.EMAIL_ADDRESS, addressList);
        cv.put(EmailContent.FilterListColumns.SUBJECT_NAME, subject);
        cv.put(EmailContent.FilterListColumns.FOLDER_NAME, folder);

        if (updateId == -1)
            context.getContentResolver().insert(EmailContent.FilterListColumns.CONTENT_URI, cv);
        else
            context.getContentResolver().update(EmailContent.FilterListColumns.CONTENT_URI, cv,
                    EmailContent.FilterListColumns.ID + "=" + updateId, null);
        return true;
    }

    static public int getFilterCount(Context context) {
        Cursor c = getFilterListCursor(context);
        int count = 0;
        if (c == null) {
            Log.e("Utility", "getFilterListaddress : Cursor is null.");
            return 0;
        }
        count = c.getCount();
        c.close();
        return count;
    }

    static public String[] getFilterListaddress(Context context) {
        Cursor c = null;
        String[] filterlist = null;
        try {
            c = getFilterListCursor(context);
            if (c == null) {
                Log.e("Utility", "getFilterListaddress : Cursor is null.");
                return null;
            }

            if (c.getCount() == 0) {
                if (!c.isClosed()) {
                    c.close();
                }
                return null;
            }

            filterlist = new String[c.getCount()];
            int i = 0;
            c.moveToFirst();
            do {
                filterlist[i] = new String();
                filterlist[i] = c.getString(3);
                i++;
            } while (c.moveToNext());
        } finally {
            try {
                if (c != null)
                    c.close();
            } catch (Exception e) {
            }
        }
        return filterlist;
    }

    static public String[] getFilterListFolderName(Context context) {
        Cursor c = null;
        String[] filterlist = null;
        try {
            c = getFilterListCursor(context);
            if (c == null) {
                Log.e("Utility", "getFilterListFolderName : Cursor is null.");
                return null;
            }

            if (c.getCount() == 0) {
                if (!c.isClosed()) {
                    c.close();
                }
                return null;
            }

            filterlist = new String[c.getCount()];
            int i = 0;
            c.moveToFirst();
            do {
                filterlist[i] = new String();
                filterlist[i] = c.getString(6);
                i++;
            } while (c.moveToNext());
        } finally {
            try {
                if (c != null)
                    c.close();
            } catch (Exception e) {
            }
        }
        return filterlist;
    }

    static public String getDisplayNameForFilter(Context context, long id) {
        Cursor c = null;
        String name = null;
        try {
            c = getFilterListCursor(context);
            long position = Mailbox.QUERY_ALL_FILTER - id;
            if (c == null) {
                Log.e("Utility", "getDisplayNameForFilter : Cursor is null.");
                return null;
            }
            if (c.getCount() == 0 || c.getCount() <= position) {
                return null;
            }
            c.moveToPosition((int) position);
            name = c.getString(6);
        } finally {
            try {
                if (c != null)
                    c.close();
            } catch (Exception e) {
            }
        }
        return name;
    }

    static public String getFilterSubject(Context context, long id) {
        Cursor c = null;
        String subject = null;
        try {
            c = getFilterListCursor(context);
            long position = Mailbox.QUERY_ALL_FILTER - id;
            if (c == null) {
                Log.e("Utility", "getFilterSubject : Cursor is null.");
                return null;
            }

            if (c.getCount() == 0 || c.getCount() <= position) {
                return null;
            }

            c.moveToPosition((int) position);
            subject = c.getString(5);
        } finally {
            try {
                if (c != null)
                    c.close();
            } catch (Exception e) {
            }
        }
        return subject;
    }

    static public String getFilterAddress(Context context, long id) {
        Cursor c = null;
        String address = null;
        try {
            c = getFilterListCursor(context);
            long position = Mailbox.QUERY_ALL_FILTER - id;
            if (c == null) {
                Log.e("Utility", "getFilterAddress : Cursor is null.");
                return null;
            }

            if (c.getCount() == 0 || c.getCount() <= position) {
                return null;
            }
            c.moveToPosition((int) position);
            address = c.getString(3);
        } finally {
            try {
                if (c != null)
                    c.close();
            } catch (Exception e) {
            }
        }
        return address;
    }

    private static Cursor getFilterListCursor(Context context) {
        Cursor c = context.getContentResolver().query(EmailContent.FilterListColumns.CONTENT_URI,
                FILTER_PROJECTION, null, null, null/*
                                                    * EmailContent.FilterListColumns
                                                    * .EMAIL_ADDRESS +" asc"
                                                    */);
        if (c == null) {
            Log.e("Utility", "getFilterListCursor : Cursor is null.");
            return null;
        }
        return c;
    }

    public static List<FilterItem> getFilterListFromEmail(Context context) {
        Cursor c = getFilterListCursor(context);
        List<FilterItem> res_filters = new ArrayList<FilterItem>();
        if (c == null) {
            Log.e("Utility", "getFilterListFromEmail : Cursor is null.");
            return res_filters;
        }
        FilterItem[] filters = new FilterItem[c.getCount()];
        c.moveToFirst();
        try {
            for (int i = 0; i < c.getCount(); i++) {
                filters[i] = new FilterItem();
                filters[i].id = c.getInt(0);
                filters[i].contact_id = c.getInt(1);
                filters[i].address_id = c.getInt(2);
                filters[i].address = c.getString(3);
                filters[i].contact_name = c.getString(4);
                filters[i].subject_name = c.getString(5);
                filters[i].folder_name = c.getString(6);
                if (filters[i].contact_id == -1) {
                    Uri uri = Uri.parse("content://com.android.contacts/data/emails");
                    final String[] PROJECTION = new String[]{
                            "_id", "contact_id", "display_name"
                    };
                    Cursor con = null;
                    try {
                        con = context.getContentResolver().query(
                                uri,
                                PROJECTION,
                                android.provider.ContactsContract.CommonDataKinds.Email.DATA
                                        + "= ?", new String[]{
                                        String.valueOf(filters[i].address)
                                }, "creation_time desc");
                        if (con == null) {
                            Log.d("Utility", "getListFromEmail : 2 - Cursor is null.");
                            return null;
                        } else {
                            boolean isChanged = false;
                            if (con.moveToFirst()) {
                                if (filters[i].address_id != con.getInt(0)
                                        || filters[i].contact_id != con.getInt(1)
                                        || !filters[i].contact_name.equals(con.getString(2))) {
                                    isChanged = true;
                                    filters[i].address_id = con.getInt(0);
                                    filters[i].contact_id = con.getInt(1);
                                    filters[i].contact_name = con.getString(2);
                                }
                            }
                            if (isChanged) {
                                ContentValues cv = new ContentValues();
                                cv.put(EmailContent.FilterListColumns.CONTACT_ID,
                                        filters[i].contact_id);
                                cv.put(EmailContent.FilterListColumns.EMAIL_ID,
                                        filters[i].address_id);
                                cv.put(EmailContent.FilterListColumns.EMAIL_ADDRESS,
                                        filters[i].address);
                                cv.put(EmailContent.FilterListColumns.DISPLAY_NAME,
                                        filters[i].contact_name);
                                cv.put(EmailContent.FilterListColumns.SUBJECT_NAME,
                                        filters[i].subject_name);
                                cv.put(EmailContent.FilterListColumns.FOLDER_NAME,
                                        filters[i].folder_name);
                                Uri update_uri = Uri
                                        .parse(EmailContent.FilterListColumns.CONTENT_URI
                                                .toString() + "/" + String.valueOf(filters[i].id));
                                context.getContentResolver().update(update_uri, cv, null, null);
                            }
                        }
                    } catch (Exception e) {
                        // Exception log
                        e.printStackTrace();
                    } finally {
                        if (con != null) {
                            con.close();
                            con = null;
                        }
                    }
                }
                c.moveToNext();
            }
        } catch (Exception e) {
            // Exception log
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
                c = null;
            }
        }
        for (int i = 0; i < filters.length; ++i) {
            res_filters.add(filters[i]);
        }
        return res_filters;
    }

/*    static public int getRecentlyOpenTime(Context context, Long messageId) {
        String[] opentime_content = new String[] {
            EmailContent.MessageColumns.OPEN_TIME
        };
        Cursor c = context.getContentResolver().query(EmailContent.Message.CONTENT_URI,
                opentime_content, EmailContent.MessageColumns.ID + "=?", new String[] {
                    Long.toString(messageId)
                }, null);
        try {
            if (c != null) {
                return c.moveToFirst() ? c.getInt(0) : null;
            }
        } finally {
            if (c != null)
                c.close();
        }
        return (Integer) null;
    }*/

    public static String getMD5EncodedString(String str) {
        MessageDigest md;
        byte[] encodedString = null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            md = MessageDigest.getInstance("MD5");
            encodedString = md.digest(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return toHex(encodedString);
    }

    private static char[] digits = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    /**
     * Convert a byte array to a string of hex digits representing the bytes.
     */
    private static String toHex(byte[] bytes) {
        char[] result = new char[bytes.length * 2];
        for (int index = 0, i = 0; index < bytes.length; index++) {
            int temp = bytes[index] & 0xFF;
            result[i++] = digits[temp >> 4];
            result[i++] = digits[temp & 0xF];
        }
        return new String(result);
    }

    public static String getStringWithLimit(String source, int max) {
        String ret = null;
        if (source != null) {
            if (source.length() > max) {
                ret = source.substring(0, max);
            } else {
                ret = source;
            }
        }
        return ret;
    }

    @SuppressWarnings("unused")
    public static boolean copyFile(String source, String target) {
        if (source == null || "".equals(source) == true || target == null
                || "".equals(target) == true)
            return false;
        long size = 0;


        boolean result = true;
        try (FileInputStream inputStream = new FileInputStream(source);
             FileOutputStream outputStream = new FileOutputStream(target);
             FileChannel fcIn = inputStream.getChannel();
             FileChannel fcOut = outputStream.getChannel()) {

            try {
                size = fcIn.size();
                if (fcIn.transferTo(0, size, fcOut) == 0) {
                    Log.d(TAG, " fcIn.transferTo 0 byte");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return result;
    }

    public static final Pattern IP_ADDRESS = Pattern
            .compile("((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                    + "|[1-9][0-9]|[0-9]))");
    public static final Pattern DOMAIN_NAME = Pattern
            .compile("^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,320}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,320}$" + "|"
                    + IP_ADDRESS);
    public static final Pattern EMAIL_ADDRESS = Pattern
            .compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,320}" + "\\@"
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,320}" + "(" + "\\."
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,320}" + ")+");

    public static String removeReturnChar(String in) {
        if (in == null) {
            return null;
        }
        return in.replace("\n", "");
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null)
            return false;
        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            return true;
        else
            return false;
    }

    // KNOX_EAS_START Single
    public static boolean isInstalledApp(Context context, String packageName) {
        try {
            List<ApplicationInfo> appList = context.getPackageManager().getInstalledApplications(
                    PackageManager.GET_META_DATA);
            for (ApplicationInfo appInfo : appList) {
                if (appInfo.packageName.equals(packageName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isSamsungSingleuser() {
        return false;
    }

    public static boolean isSamsungAccount(Context context, long accountId) {
        boolean retVal = false;
        if (isSamsungSingleuser()) {
            Cursor c = null;
            try {
                c = context.getContentResolver().query(EmailContent.Account.CONTENT_URI,
                        new String[]{
                                Account.COMPANY_NAME
                        }, Account.RECORD_ID + "=" + accountId, null, null);

                if (c != null) {
                    while (c.moveToNext()) {
                        String companyname = c.getString(0);
                        if (SAMSUNGSINGLE.equalsIgnoreCase(companyname)) {
                            Log.d(Logging.LOG_TAG, "Single isSamsungAccount true");
                            retVal = true;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                Log.d(Logging.LOG_TAG, "Single isSamsungAccount false");
            } finally {
                try {
                    if (c != null)
                        c.close();
                } catch (Exception e) {
                }
            }
        }
        // Log.d(Logging.LOG_TAG, "Single isSamsungAccount false");
        return retVal;
    }

    public static void sendReportToAgent(String action, Context context, String emailaddress) {
        Log.d(Logging.LOG_TAG, "SingleReceiver sendReportToAgent >>>>> " + action);
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(APPLICATION_TYPE, EAS);
        intent.putExtra(EMAILADDRESS, emailaddress);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        context.sendBroadcast(intent, COM_SEC_KNOX_SSOAGENT_USE_KNOX);
    }

    public static void sendAddReportToAgent(String action, Context context, String emailaddress,
                                            String flow_mode) {
        Log.d(Logging.LOG_TAG, "SingleReceiver sendReportToAgent >>>>> " + action);
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(APPLICATION_TYPE, EAS);
        intent.putExtra(FLOW_MODE, flow_mode);
        intent.putExtra(EMAILADDRESS, emailaddress);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        context.sendBroadcast(intent, COM_SEC_KNOX_SSOAGENT_USE_KNOX);
    }

    public static boolean isSamsungSingleuserType(Context context) {
        if (isSamsungSingleuser()) {
            Cursor c = null;
            try {
                c = context.getContentResolver().query(MYSINGLE_CONTENT_URI, new String[]{
                        KNOXCONTENT_KEY_TYPE
                }, null, null, null);
                String mobiledeskType = null;
                {
                    if (c != null && !c.isClosed()) {
                        if (c.moveToFirst()) {
                            mobiledeskType = c.getString(0);
                        }
                    }
                }
                if (A_TYPE.equalsIgnoreCase(mobiledeskType)) {
                    Log.d(Logging.LOG_TAG, "isSamsungSingleuserType A");
                    return true;
                }
            } catch (Exception e) {
                Log.d(Logging.LOG_TAG, "isSamsungSingleuserType Exception Null");
            } finally {
                if (c != null && !c.isClosed()) {
                    c.close();
                }
            }
        }
        Log.d(Logging.LOG_TAG, "isSamsungSingleuserType Null");
        return false;
    }

    public static int numOfAccount(Context context) {
        Cursor c = null;
        int numAccount = 0;
        try {
            c = context.getContentResolver().query(EmailContent.Account.CONTENT_URI,
                    EmailContent.Account.ID_PROJECTION, "emailAddress!='snc@snc.snc'", null,
                    null);

            if (c != null)
                numAccount = c.getCount();
        } catch (Exception e) {
            Log.v(TAG, e.toString());
        } finally {
            if (c != null && !c.isClosed())
                c.close();
        }
        return numAccount;
    }

    public static boolean isContainSamsungSingle(Context context) {
        boolean retVal = false;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(EmailContent.Account.CONTENT_URI,
                    new String[]{
                            AccountColumns.ID
                    }, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    if (Utility.isSamsungAccount(context, cursor.getInt(0)) == true) {
                        if (cursor != null && !cursor.isClosed()) {
                            cursor.close();
                        }
                        Log.d(Logging.LOG_TAG, "isContainSamsungSingle true");
                        retVal = true;
                    }
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            Log.d(Logging.LOG_TAG, "isContainSamsungSingle Exception Null");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return retVal;
    }

    // KNOX_EAS_END Single
    public enum VerifyStatus {
        Initialized, OnCreate, OnResume, Executed,
    }

    static private class VerifLogModule {
        VerifyStatus mCurrent = VerifyStatus.Initialized;

        private void printLog(VerifyStatus status) {
            if (mCurrent != status) {
                String outString = "";
                switch (status) {
                    default:
                    case OnCreate:
                        outString = "onCreate";
                        break;
                    case OnResume:
                        outString = "onResume";
                        break;
                    case Executed:
                        outString = "Executed";
                        break;
                }
                EmailLog.i("VerificationLog", outString);
                // Log.i("VerificationLog", outString);
                mCurrent = status;
            }
        }
    }

    static private android.util.SparseArray<VerifLogModule> sVerifySet = new android.util.SparseArray<VerifLogModule>();

    public static void verificationLog(int identifier, VerifyStatus st) {
        VerifLogModule vm = sVerifySet.get(identifier);
        if (vm == null) {
            vm = new VerifLogModule();
            sVerifySet.put(identifier, vm);
        }
        vm.printLog(st);
    }

    public static void removeVerificationLog(int identifier) {
        sVerifySet.remove(identifier);
    }

    /**
     * This method checks if the given account is a Hotmail/live/msn account
     *
     * @return true - If it is a Hotmail/live/msn account false -Other than
     * Hotmail/live/msn account
     */

    public static boolean isHotmailAccount(Context context, Account account) {
        if (context != null && account != null) {
            if (account.mHostAuthRecv == null) {
                account.mHostAuthRecv = EmailContent.HostAuth.restoreHostAuthWithId(context,
                        account.mHostAuthKeyRecv);
            }

            if ((account.mHostAuthRecv != null && account.mHostAuthRecv.mAddress != null && (account.mHostAuthRecv.mAddress
                    .contains("@hotmail.") || account.mHostAuthRecv.mAddress.contains("@outlook.")))
                    || account.mEmailAddress != null
                    && (account.mEmailAddress.contains("@hotmail.") || account.mEmailAddress
                    .contains("@outlook."))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isHotmailAccount(String domain) {
        if (domain != null) {
            if (domain.contains("hotmail.") || domain.contains("outlook."))
                return true;
        }
        return false;
    }


    public static int getMaximumVisibleLimit(Account account) {
        if (account != null && account.mHostAuthRecv != null) {
            if ("imap".equals(account.mHostAuthRecv.mProtocol)
                    && (account.mEmailAddress.contains("@hotmail.") || account.mEmailAddress
                    .contains("@outlook."))) {
                // Change maximum value to previous one because hotmail Imap
                // server
                // does not allow it (hotmail.co.uk)
                return EmailContent.Message.MAXIUM_VISIBLE_LIMIT_PREV;
            }
        }
        return EmailContent.Message.MAXIUM_VISIBLE_LIMIT;
    }

    public static Boolean isPostmanExist(Context context) {
        String uriString = "content://media/internal/audio/media";
        Uri uri = null;
        uri = Uri.parse(uriString);
        Cursor cursor = null;
        try {
            cursor = context.getApplicationContext().getContentResolver().query(uri, new String[]{
                    MediaStore.Audio.Media._ID
            }, MediaStore.Audio.Media.DATA + "= '" + defaultPostmanURI + "'", null, null);
            if ((cursor != null) && (cursor.getCount() > 0)) {
                Log.w(TAG, " S_Postman is exist in the DB");
                cursor.moveToFirst();
                int j = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                Log.d(TAG, "The id is:" + j);
                uri = Uri.parse(uriString + "/" + Integer.toString(j));
                Log.e(TAG, "The uri is:" + uri.toString());
                return true;
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return false;
    }

    public static Uri getEmailRingtoneUri(Context context, String path) {
        if (path == null || path.isEmpty())
            return null;

        if (path.equals(defaultLetterURI) && isPostmanExist(context))
            path = defaultPostmanURI;
        String uriString = "content://media/internal/audio/media";
        Uri uri = null;
        uri = Uri.parse(uriString);
        Log.i(TAG, "windstar The path is:" + path);
        Log.i(TAG, "windstar The uri is:" + uri.toString());

        if (path.startsWith("content://")) {
            Cursor cursor = null;
            boolean res = false;
            try {
                cursor = context.getContentResolver().query(Uri.parse(path), new String[]{
                        MediaStore.Audio.Media._ID
                }, null, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor == null) {
                    res = false;
                } else {
                    if (cursor.getCount() <= 0) {
                        if (Uri.parse(path).toString().contains(DEFAULT_NOTIFICATION_SOUND)
                                && RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION) == null)
                            res = true;
                        else
                            res = false;
                    } else
                        res = true;
                }
                if (cursor != null && !cursor.isClosed())
                    cursor.close();
            }
            if (!res)
                path = CarrierValues.DEFAULT_RINGTONEURI;
            else
                return Uri.parse(path);
        }

        // Added code to close the cursor, prevention of
        // CursorWindowAllocationException
        Cursor cursor = null;
        try {
            cursor = context.getApplicationContext().getContentResolver().query(uri, new String[]{
                    MediaStore.Audio.Media._ID
            }, MediaStore.Audio.Media.DATA + "= '" + path + "'", null, null);
            if ((cursor != null) && (cursor.getCount() > 0)) {
                Log.w(TAG, " The path is exist in the DB");
                cursor.moveToFirst();
                int j = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                Log.d(TAG, "The id is:" + j);
                uri = Uri.parse(uriString + "/" + Integer.toString(j));
                Log.e(TAG, "The uri is:" + uri.toString());
                // return uri;
            } else {
                uri = Uri.parse(CarrierValues.DEFAULT_RINGTONEURI);
            }
            // return null;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return uri;
    }

    public static boolean ringtoneCheckExternal(Context context) {

        Preferences sPreferences;
        sPreferences = Preferences.getPreferences(context);
        String vipRingtone = sPreferences.getVIPRingtone();
        if (vipRingtone != null && vipRingtone.toString().contains(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString()))
            return true;
        String[] ACCOUNT_PROJECTION = new String[]{
                AccountColumns.DISPLAY_NAME, AccountColumns.EMAIL_ADDRESS, AccountColumns.RINGTONE_URI,
        };
        Cursor c = context.getContentResolver().query(EmailContent.Account.CONTENT_URI,
                ACCOUNT_PROJECTION, "emailAddress!='snc@snc.snc'", null,
                null);

        try {
            while (c != null && c.moveToNext()) {
                int count = c.getCount();
                if (count == 0) {
                    return false;
                }
                String ringtone = c.getString(c.getColumnIndex(AccountColumns.RINGTONE_URI));

                if (ringtone != null && ringtone.toString().contains(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString())) {
                    return true;
                }
            }
        } catch (Exception e) {
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return false;

    }


    public static boolean isInContainer(Context ctx) {
        boolean result = false;
        if (ctx == null)
            Log.d(TAG, "isInContainer - ctx is null");
        else {
            Bundle bundle = PersonaManager.getKnoxInfoForApp(ctx);
            if (bundle == null) {
                Log.d(TAG, "PersonaManager.getKnoxInfoForApp() returned null");
            } else {
                String knoxVersion = bundle.getString("version");
                if (knoxVersion == null) {
                    Log.d(TAG, "KNOX version is null");
                } else {
                    Log.d(TAG, "KNOX version is " + knoxVersion);
                    if ("1.0".equals(knoxVersion)) {
                        // Knox 1.0 case
                        if (ctx.getPackageName() == null) {
                            Log.d(TAG, "isInContainer - PKG name is null");
                        } else if (ctx.getPackageName().startsWith(KNOX_PACKAGE_PREFIX)) {
                            // Inside KNOX
                            result = true;
                        } else {
                            // Outside KNOX
                        }
                    } else if ("2.0".equals(knoxVersion)) {
                        // Knox 2.0 case
                        if ("true".equals(bundle.getString("isKnoxMode"))) {
                            // Inside KNOX
                            result = true;
                        } else {
                            // Outside KNOX
                        }
                    } else {
                        // Unknown KNOX version
                        Log.d(TAG, "isInContainer - unknown KNOX version");
                    }
                }
            }
        }
        Log.d(TAG, "isInContainer return " + result);
        return result;
    }

//    public static boolean isInAFW(Context context){
//        Boolean result = false;
//        Class<?> userManagerClass = UserManager.class;
//        
//        Method[] method = userManagerClass.getDeclaredMethods();
//        for(Method call : method){
//            if("get".equals(call.getName())){
//                try {
//                    UserManager manager = (UserManager)call.invoke(null, new Object[]{context});
//                    Class<?> klass = manager.getClass();
//                    Method  m =  klass.getDeclaredMethod("isManagedProfile",null);
//                    result = (Boolean)m.invoke(manager, null);
//                    
//                } catch (NoSuchMethodException e){} 
//                catch (IllegalAccessException e) {} 
//                catch (IllegalArgumentException e) {} 
//                catch (InvocationTargetException e) {}
//                break;
//            }
//        }
//        return result;
//    }

    public static void setAfwMode(Context context) {
        if (context != null && UserHandle.myUserId() >= 10 && UserHandle.myUserId() < 100) {
            try {
                UserManager um = (UserManager) context
                        .getSystemService(Context.USER_SERVICE);
                Class cls = Class.forName("android.os.UserManager");

                Method isManagedProfileMethod = cls.getDeclaredMethod("isManagedProfile");
                Boolean isManagedProfile = (Boolean) isManagedProfileMethod.invoke(um);
                Log.d(TAG, "setAfwMode : UserManager.isManagedProfile() : " + isManagedProfile);
                sAfwMode = isManagedProfile;
            } catch (Exception e) {
                Log.dumpException(TAG, e);
            }
        }
        Log.d(TAG, "setAfwMode :  " + sAfwMode);
    }

    public static boolean isInAFWMode() {
        return sAfwMode;
    }

    public static boolean isInSecureFolder() {
        return false;//PersonaManager.isSecureFolderId(UserHandle.myUserId());
    }

    // KNOX_EAS_START
    public static boolean isKnoxMode(Context context) {
        if (context != null && isInContainer(context)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getContainerName(Context context) {
        String containerName = null;
        if (context != null) {
            containerName = PersonaManager.getPersonaName(context, UserHandle.myUserId());
        }
        return containerName;
    }

    public static boolean isKioskContainer(Context ctx) {
        return PersonaManager.isKioskModeEnabled(ctx);
    }
    // KNOX_EAS_END

//    public static String calculateTimeFormat(long timestamp, DateFormat dateFormat,
//            DateFormat timeFormat) {
//        Date sDate = new Date(timestamp);
//        String dateStr;
//        if (DateUtils.isToday(timestamp)) {
//            if (timeFormat == null) {
//                dateStr = "";
//            } else {
//                dateStr = timeFormat.format(sDate);
//            }
//        } else {
//            if (dateFormat == null) {
//                dateStr = "";
//            } else {
//                dateStr = Utility.removeYearFromDate(timestamp, dateFormat.format(sDate));
//            }
//        }
//        return dateStr;
//    }

    static private Date sDate1 = new Date();

    public static String getFormatDateShort(long timeStamp) {
        Calendar calNow = Calendar.getInstance();
        Calendar calCal = Calendar.getInstance();
        calCal.setTimeInMillis(timeStamp);

        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        if (calNow.get(Calendar.YEAR) == calCal.get(Calendar.YEAR)) {

            //building date format
            String pattern = df.toLocalizedPattern();
            pattern = pattern.replaceAll("\\W?[Yy]+\\W?", "");
            char ch = pattern.charAt(0);
            if (ch != 'D' && ch != 'd' && ch != 'M') {
                pattern = pattern.substring(1);
            }
            ch = pattern.charAt(pattern.length() - 1);

            if (ch != 'D' && ch != 'd' && ch != 'M') {
                pattern = pattern.substring(0, pattern.length() - 1);
            }
            df.applyPattern(pattern);
        }
        sDate1.setTime(timeStamp);
        return df.format(sDate1);
    }

    public static String removeYearFromDate(long timestamp, String dateStr) {

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR), Calendar.JANUARY, 1,
                0, 0, 0);
        long thisYearMilli = (cal.getTimeInMillis() / 1000) * 1000;
        if (timestamp >= thisYearMilli && dateStr != null) {
            int delimiterSize = -1;
            int start = -1;
            int delimiterInt = dateStr.charAt(2);
            if ((delimiterInt >= 48 && delimiterInt <= 57) // for Arabia Digit
                    // (0 ~ 9)
                    || (delimiterInt >= 1632 && delimiterInt <= 1639) // for
                    // Arabic
                    // Digit
                    || (delimiterInt >= 1776 && delimiterInt <= 1785)) { // for
                // Extended
                // Arabic-Indic
                // Digit
                int nextDelimiterInt = dateStr.charAt(5);
                start = 4;
                if ((nextDelimiterInt >= 48 && nextDelimiterInt <= 57)
                        || (nextDelimiterInt >= 1632 && nextDelimiterInt <= 1639)
                        || (nextDelimiterInt >= 1776 && nextDelimiterInt <= 1785)) {
                    delimiterSize = 1;
                } else {
                    delimiterSize = 2;
                }
                dateStr = dateStr.substring(start + delimiterSize, dateStr.length());
            } else {
                int nextDelimiterInt = dateStr.charAt(3);
                start = 0;
                if ((nextDelimiterInt >= 48 && nextDelimiterInt <= 57)
                        || (nextDelimiterInt >= 1632 && nextDelimiterInt <= 1639)
                        || (nextDelimiterInt >= 1776 && nextDelimiterInt <= 1785)) {
                    delimiterSize = 1;
                } else {
                    delimiterSize = 2;
                }
                dateStr = dateStr.substring(start, dateStr.length() - 4 - delimiterSize);
            }
        }
        return dateStr;
    }

    public static String getDateStringForTTS(Context context, Date date) {
        java.text.DateFormat dateFormat = null;
        dateFormat = android.text.format.DateFormat.getDateFormat(context);
        return dateFormat.format(date);
    }


    final static char LIST_DELIMITER_EMAIL = '\1';
    final static char LIST_DELIMITER_PERSONAL = '\2';

    private static String getRecipientAddress(String addressList) {
        StringBuffer addressListBuff = new StringBuffer();
        if (addressList != null) {
            String[] toAddress = addressList.split(String.valueOf(LIST_DELIMITER_EMAIL));
            for (String address : toAddress) {
                String[] splitAddress = address.split(String.valueOf(LIST_DELIMITER_PERSONAL));
                if (splitAddress.length > 0) {
                    if (splitAddress[0].contains("@")) {
                        addressListBuff.append(splitAddress[0]).append(";");
                    }
                }
            }
        }
        return addressListBuff.toString();
    }

    public static boolean isExistEasAccount(Context context) {
        if (context == null)
            return false;
        try {
            ContentResolver resolver = context.getContentResolver();
            Cursor c = resolver.query(HostAuth.CONTENT_URI, HostAuth.ID_PROJECTION,
                    HostAuthColumns.PROTOCOL + "=" + "\"eas\"", null, null);

            if (c == null)
                return false;

            boolean isEasExist = false;
            try {
                if (c.getCount() > 0)
                    isEasExist = true;
            } finally {
                c.close();
            }
            return isEasExist;
        } catch (SecurityException se) {
            return false;
        }
    }

    public static boolean isExistGlobalEasAccountFromAccManager(Context context) {
        android.accounts.Account[] acts = AccountManager.get(context).getAccounts();

        for (android.accounts.Account act : acts) {
            String accType = act.type;
            if (!accType.equals(AccountManagerTypes.TYPE_EXCHANGE)) {
                if (accType.equals(AccountManagerTypes.TYPE_EAS_MAILWISE)
                        || accType.equals(AccountManagerTypes.TYPE_EAS_NINE)
                        || accType.equals(AccountManagerTypes.TYPE_EAS_AQUA)
                        || accType.equals(AccountManagerTypes.TYPE_EAS_TOUCHDOWN)
                        || accType.equals(AccountManagerTypes.TYPE_EAS_CLOUDMAGIC)
                        || accType.contains("eas") || accType.contains("exchange")) {

                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isExistLDAPAccount(Context context) {
        if (context == null)
            return false;
        String[] PROJECTION = {
                EmailContent.LDAPAccountColumns.ID
        };
        ContentResolver resolver = context.getContentResolver();
        Cursor c = resolver.query(EmailContent.LDAPAccountColumns.CONTENT_URI, PROJECTION, null,
                null, null);
        if (c == null)
            return false;
        try {
            if (c.getCount() > 0)
                return true;
            else
                return false;
        } finally {
            c.close();
        }
    }

    public static Uri getEmailPolicy() {
        return Uri.parse(SecContentProviderURI.EMAIL_URI);
    }

    public static Uri getEmailAccountPolicy() {
        return Uri.parse(SecContentProviderURI.EMAILACCOUNT_URI);
    }

    public static Uri getExchangePolicy() {
        return Uri.parse(SecContentProviderURI.EXCHANGEACCOUNT_URI);
    }

    public static Uri getCertificatePolicy() {
        return Uri.parse(SecContentProviderURI.CERTIFICATE_URI);
    }

    public static Uri getDeviceAccountPolicy() {
        return Uri.parse(SecContentProviderURI.DEVICEACCOUNT_URI);
    }

    public static Uri getDLPPolicy() {
        return Uri.parse(DLP_POLICY_URI);
    }

    public static Boolean getBooleanFromSecContentProvider(Context mContext, Uri uri,
                                                           String[] selectionArgs, String policy, boolean defValue) {
        boolean rtn = defValue;
        Cursor cr = null;
        if (mContext != null) {
            cr = mContext.getContentResolver().query(uri, null, policy, selectionArgs, null);
        }
        if (cr != null) {
            try {
                cr.moveToFirst();
                rtn = Boolean.parseBoolean(cr.getString(cr.getColumnIndex(policy)));
                return rtn;
            } finally {
                cr.close();
            }
        }
        return rtn;
    }

    public static int getIntFromSecContentProvider(Context mContext, Uri uri,
                                                   String[] selectionArgs, String policy, int defValue) {
        int rtn = defValue;
        Cursor cr = mContext.getContentResolver().query(uri, null, policy, selectionArgs, null);

        if (cr != null) {
            try {
                cr.moveToFirst();
                rtn = Integer.parseInt(cr.getString(cr.getColumnIndex(policy)));
                return rtn;
            } finally {
                cr.close();
            }
        }
        return rtn;
    }

    public static String getStringFromSecContentProvider(Context mContext, Uri uri,
                                                         String[] selectionArgs, String policy, String defValue) {
        String rtn = defValue;
        Cursor cr = mContext.getContentResolver().query(uri, null, policy, selectionArgs, null);

        if (cr != null) {
            try {
                cr.moveToFirst();
                rtn = cr.getString(cr.getColumnIndex(policy));
                return rtn;
            } finally {
                cr.close();
            }
        }
        return rtn;
    }

    public static EnterpriseExchangeAccount getEASAccountFromSecContentProvider(Context mContext,
                                                                                Uri uri, String[] selectionArgs, String policy, Object defValue) {
        EnterpriseExchangeAccount rtn = (EnterpriseExchangeAccount) defValue;
        Bundle bundle = null;
        Cursor cr = mContext.getContentResolver().query(uri, null, policy, selectionArgs, null);

        if (cr != null) {
            try {
                bundle = cr.getExtras();
                if (bundle != null) {
                    rtn = bundle
                            .getParcelable(EnterpriseEmailContentProviderURI.KEY_GET_EAS_ACCOUNT);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cr.close();
            }
        }
        return rtn;
    }

    public static EnterpriseEmailAccount getEmailAccountFromSecContentProvider(Context mContext,
                                                                               Uri uri, String[] selectionArgs, String policy, Object defValue) {
        EnterpriseEmailAccount rtn = (EnterpriseEmailAccount) defValue;
        Bundle bundle = null;
        Cursor cr = mContext.getContentResolver().query(uri, null, policy, selectionArgs, null);

        if (cr != null) {
            try {
                bundle = cr.getExtras();
                if (bundle != null) {
                    rtn = (EnterpriseEmailAccount) bundle
                            .getParcelable(EnterpriseEmailContentProviderURI.KEY_GET_EMAIL_ACCOUNT);
                }
                return rtn;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cr.close();
            }
        }
        return rtn;
    }

    public static long getLongFromSecContentProvider(Context mContext, Uri uri,
                                                     String[] selectionArgs, String policy, long defValue) {
        long rtn = defValue;
        Cursor cr = mContext.getContentResolver().query(uri, null, policy, selectionArgs, null);

        if (cr != null) {
            try {
                cr.moveToFirst();
                rtn = Long.parseLong(cr.getString(cr.getColumnIndex(policy)));
                return rtn;
            } finally {
                cr.close();
            }
        }
        return rtn;
    }

    public static void SetFavoriteContactRejected(Context context, long id) {

        if (context == null)

            return;

        ContentValues cv = new ContentValues();

        cv.put(FavoriteContactColumns.STATE, FavoriteContact.STATE_REJECTED);

        Uri update_uri = Uri.parse(EmailContent.FavoriteContact.CONTENT_URI.toString() + "/"

                + String.valueOf(id));

        context.getContentResolver().update(update_uri, cv, null, null);

    }

    protected static long syncTime, sendHttpTime, commitTime, parseTime;

    private static long mLastClickTime = 0;
    private static int sLastId = 0;

    public static final long MIN_CLICK_INTERVAL = 999;
    public static final long MIN_CLICK_INTERVAL_ANI = 500;

    final public static String DEBUG_MESSAGE_EXCHANGE_SYNCTIME = "DEBUG_MESSAGE_EXCHANGE_SYNCTIME";
    final public static String DEBUG_MESSAGE_EXCHANGE_SEND_HTTP = "DEBUG_MESSAGE_EXCHANGE_SEND_HTTP";
    final public static String DEBUG_MESSAGE_EXCHANGE_COMMIT = "DEBUG_MESSAGE_EXCHANGE_COMMIT";
    final public static String DEBUG_MESSAGE_EXCHANGE_PARSE = "DEBUG_MESSAGE_EXCHANGE_PARSE";

    public static void debugTime(String tag, String tag2, String log) {
        long curTime = System.currentTimeMillis();
        if (tag2.startsWith("DEBUG_MESSAGE_EXCHANGE_SYNCTIME")) {
            long debugTime = curTime - syncTime;
            double totalTime = (double) (curTime - syncTime) / 1000.0;
            syncTime = curTime;
            Log.d(tag, log + " => debugTime(" + debugTime + "), totalTime(" + totalTime + ")");
        } else if (tag2.startsWith("DEBUG_MESSAGE_EXCHANGE_SEND_HTTP")) {
            long debugTime = curTime - sendHttpTime;
            double totalTime = (double) (curTime - sendHttpTime) / 1000.0;
            sendHttpTime = curTime;
            Log.d(tag, log + " => debugTime(" + debugTime + "), totalTime(" + totalTime + ")");
        } else if (tag2.startsWith("DEBUG_MESSAGE_EXCHANGE_COMMIT")) {
            long debugTime = curTime - commitTime;
            double totalTime = (double) (curTime - commitTime) / 1000.0;
            commitTime = curTime;
            Log.d(tag, log + " => debugTime(" + debugTime + "), totalTime(" + totalTime + ")");
        } else if (tag2.startsWith("DEBUG_MESSAGE_EXCHANGE_PARSE")) {
            long debugTime = curTime - parseTime;
            double totalTime = (double) (curTime - parseTime) / 1000.0;
            parseTime = curTime;
            Log.d(tag, log + " => debugTime(" + debugTime + "), totalTime(" + totalTime + ")");
        }
    }

    static public boolean isMicrosoftAccount(String mailAddress) {
        if (("hotmail".equals(mailAddress)) || ("msn".equals(mailAddress))
                || ("live".equals(mailAddress)) || ("outlook".equals(mailAddress))) {
            return true;
        }
        return false;
    }

    public static boolean hasEmailAccount(Context context) {
        Cursor c = null;
        try {
            c = context.getContentResolver().query(EmailContent.Account.CONTENT_URI,
                    EmailContent.Account.ID_PROJECTION, null, null, null);
            if (c == null) {
                return false;
            }
            boolean enable = c.getCount() > 0;
            if (enable) {
                Log.d(TAG, "Email has account.");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return false;
    }

    public static void killMyProcess() {
        Log.d(TAG, "KillMyProcess : Killed");
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static void killProcess(Context context, String nameOfProcess) {
        try {
            Log.d(TAG, "KillMyProcess : Killed");
            ActivityManager manager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> listOfProcesses = manager
                    .getRunningAppProcesses();
            if (listOfProcesses == null)
                return;

            ActivityManager.RunningAppProcessInfo currentProcess = null;
            for (ActivityManager.RunningAppProcessInfo process : listOfProcesses) {
                // Current process
                if (process.pid == android.os.Process.myPid()) {
                    currentProcess = process;
                    continue;
                }
                if (process.processName.contains(nameOfProcess)) {
                    killProcess(manager, process);
                }
            }

            if (currentProcess != null) {
                killProcess(manager, currentProcess);
            }
        } catch (Exception e) {
        }
    }

    private static void killProcess(ActivityManager manager, ActivityManager.RunningAppProcessInfo process) {
        if (manager != null && process != null) {
            Log.d(TAG, "Kill process: " + process.processName + " : " + process.pid);
            android.os.Process.killProcess(process.pid);
            android.os.Process.sendSignal(process.pid, android.os.Process.SIGNAL_KILL);
            manager.killBackgroundProcesses(process.processName);
        }
    }

    public static boolean runningTask(Context context) {
        boolean found = false;
        try {
            ActivityManager am = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(Integer.MAX_VALUE);
            ComponentName componentInfo = null;

            for (RunningTaskInfo runningTaskInfo : taskInfo) {
                if (runningTaskInfo.topActivity.getPackageName().contains(EmailPackage.PKG_BASE)) { // context.getPackageName()
                    componentInfo = runningTaskInfo.topActivity;
                    Log.d(TAG, "runningTask packageName : " + componentInfo.getPackageName()
                            + "className : " + componentInfo.getClassName());
                    found = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return found;
    }

    static public String callStack(Exception e) {
        StringBuffer stacktrace = new StringBuffer();
        StackTraceElement[] stacks = e.getStackTrace();
        int maxDepth = 20;
        if (stacks.length < maxDepth)
            maxDepth = stacks.length;
        for (int i = 1; i < maxDepth; i++) {
            stacktrace.append(stacks[i].toString() + "<-");
        }
        return stacktrace.toString();
    }

    public static int getUnreadTotalCount(Context context) {
        try {
            ContentValues values = new ContentValues();
            values.put(MailboxColumns.UNREAD_COUNT, 0);
            String where = MailboxColumns.TYPE + "=" + Mailbox.TYPE_INBOX + " AND "
                    + MailboxColumns.UNREAD_COUNT + "<0";
            context.getContentResolver().update(Mailbox.CONTENT_URI, values, where, null);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        int unreadMessageCount = Message.getAllUnreadCount(context);
        if (unreadMessageCount < 0) {
            unreadMessageCount = 0;
        }
        return unreadMessageCount;
    }

    static public boolean isIgnoreDrmCheck(String filename) {
        boolean ret = false;
        if (filename == null) {
            return false;
        }
        if (filename.toLowerCase().endsWith(".vnt")) {
            ret = true;
        }
        return ret;
    }

    public static boolean IsDRMFile(Context mcontext, Uri uri) {
        String filepath = null;
        if (uri == null)
            return false;
        if ("file".equals(uri.getScheme())) {
            filepath = uri.getPath();
        } else {
            filepath = getFilePath(mcontext, uri);
            if (filepath == null)
                return false;
        }
        return IsDRMFile(mcontext, filepath);
    }

    public static boolean IsDRMFile(Context mcontext, String filepath) {
        DrmInfoRequest drmrequest = new DrmInfoRequest(DrmInfoRequest.TYPE_GET_DRMFILE_INFO,
                OMA_PLUGIN_MIME);
        drmrequest.put("drm_path", filepath);
        DrmManagerClient mDrmClient = new DrmManagerClient(mcontext);

        DrmInfo drminfo = mDrmClient.acquireDrmInfo(drmrequest);
        int drmtype = TYPE_DRMTYPE_NONE;
        if (drminfo != null) {
            String type = (String) drminfo.get("type");
            if (type != null) {
                drmtype = Integer.parseInt(type);
                Log.d(TAG, "DrmType : " + drmtype);
            } else {
                Log.d(TAG, "DrmType is null!");
            }
        } else {
            Log.d(TAG, "DrmInfo is null!");
        }
        if (mDrmClient != null) {
            try {
                mDrmClient.release();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            mDrmClient = null;
        }
        drmrequest = null;
        if ((TYPE_DRMTYPE_NONE == drmtype) || (TYPE_DRMTYPE_SD == drmtype)
                || (TYPE_DRMTYPE_SSD == drmtype))
            return false;
        else
            return true;
    }

    private static String getFilePath(Context mcontext, Uri uri) {
        if (uri == null || uri.toString().length() == 0 || uri.getScheme() == null)
            return null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            String mPath = uri.getPath();
            if (EmailLog.DEBUG) {
                Log.e(Logging.LOG_TAG, "getFilePath file path = " + mPath);
            }
            return mPath;
        } else if ((uri.getScheme().equals(ContentResolver.SCHEME_CONTENT) && uri.toString()
                .contains("picasa") == false)
                && (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT) && uri.toString()
                .contains("sns") == false)) {
            String mPath = null;
            /*Cursor c = SqliteWrapper.query(mcontext, mcontext.getContentResolver(), uri, null,
                    null, null, null);*/
            Cursor c = mcontext.getContentResolver().query(uri, null,
                    null, null, null);
            if (c == null) {
                if (EmailLog.DEBUG) {
                    Log.e(Logging.LOG_TAG, "Query on " + uri + " returns null result.");
                }
                return null;
            }
            try {
                if ((c.getCount() != 1) || !c.moveToFirst()) {
                    if (EmailLog.DEBUG) {
                        Log.d(Logging.LOG_TAG, "Query on " + uri + " returns 0 or multiple rows.");
                    }
                    return null;
                }
                String filePath;
                filePath = c.getString(c.getColumnIndexOrThrow(Images.Media.DATA));
                mPath = filePath;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                c.close();
            }
            if (EmailLog.DEBUG) {
                Log.e(Logging.LOG_TAG, "getFilePath file path = " + mPath);
            }
            return mPath;
        } else {
            if (EmailLog.DEBUG) {
                Log.e(Logging.LOG_TAG,
                        "getFilePath URI path is not file scheme - " + uri.getScheme());
            }
        }
        return null;
    }

    public static int getAccountColorIndex(long accountId, int colors) {
        return (int) ((accountId - 1) % colors);
    }

    static public boolean hasEnoughSpace() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getAbsolutePath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        long totalBlocks = stat.getBlockCountLong();
        long totalSize = blockSize * totalBlocks;
        long freeSize = blockSize * availableBlocks;
        long memLowThreshold = totalSize / 25;
        long memMinThreshold = (long) (100 * 1024L * 1024L);

        if (totalSize < (1.5 * 1024L * 1024L * 1024L)) {
            freeSize = freeSize - (60 * 1024 * 1024);
            if (freeSize < 0) {
                freeSize = 0;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("blockSize = ").append(blockSize).append(" total blocks = ").append(totalBlocks);
        sb.append(" available blocks = ").append(availableBlocks);
        sb.append(" totalSize = ").append(totalSize).append(" freeSize = ").append(freeSize);
        sb.append(" ThreshHold = ").append(memLowThreshold);
        sb.append(" MinThreshHold = ").append(memMinThreshold);
        Log.d(TAG, "memory check \n " + sb.toString());

        return /*freeSize > memLowThreshold && */freeSize > memMinThreshold;

    }

    public static boolean checkSyncSettings(Context context, Account a, boolean forceSync) {
        if (!ContentResolver.getMasterSyncAutomatically() && !forceSync)
            return false;
        if (a == null || TextUtils.isEmpty(a.mEmailAddress))
            return false;
        String type = (AccountCache.isExchange(context, a.mId) ? AccountManagerTypes.TYPE_EXCHANGE
                : AccountManagerTypes.TYPE_POP_IMAP);
        if (CarrierValues.IS_CARRIER_CUE && a.mEmailAddress.contains(EmailFeature.getAccountHintDomain())) {//NAUTA
            type = AccountManagerTypes.TYPE_NAUTA;
        }
        android.accounts.Account acc = null;
        try {
            acc = new android.accounts.Account(a.mEmailAddress, type);
        } catch (Exception e) {
            Log.dumpException(TAG, e);
        }
        if (acc == null)
            return false;
        return ContentResolver.getSyncAutomatically(acc, EmailContent.AUTHORITY);
    }

    public static void sendResponseToMDM(Context context, String action, String userId,
                                         String serviceType, String serverName, String receiveHost, int status, long accountId) {
        Log.d(TAG, "sendResponseToMDM: start");
        Log.d(TAG, "action: " + action);
        Log.d(TAG, "user_id: " + userId);
        Log.d(TAG, "service: " + serviceType);
        Log.d(TAG, "server_name: " + serverName);
        Log.d(TAG, "receive_host: " + receiveHost);
        Log.d(TAG, "status: " + status);
        Log.d(TAG, "account_id: " + accountId);
        Log.d(TAG, "user_handle_id: " + UserHandle.myUserId());
        Intent responseIntent = new Intent(action);
        responseIntent.putExtra("user_id", userId);
        responseIntent.putExtra("service", serviceType);
        if ("eas".equals(serviceType) || "ldap".equals(serviceType))
            responseIntent.putExtra("server_name", serverName);
        else
            responseIntent.putExtra("receive_host", receiveHost);
        responseIntent.putExtra("status", status);
        responseIntent.putExtra("account_id", accountId);
        responseIntent.putExtra("user_handle_id", UserHandle.myUserId());
        context.sendBroadcast(responseIntent, android.Manifest.permission.MDM_EMAIL);
    }

    public static int getGoogleAccountFromAccountManager(Context ctx) {
        int result = 0;
        String GOOGLE_TYPE = "com.google";
        AccountManager am = AccountManager.get(ctx);
        android.accounts.Account[] accounts = am.getAccountsByType(GOOGLE_TYPE);
        if (accounts != null) {
            EmailLog.d(TAG, "Number of accounts in AccountManager DB: " + accounts.length);
        } else {
            EmailLog.d(TAG, "account is null");
            return result;
        }

        for (android.accounts.Account acc : accounts) {
            String accountName = acc.name;
            String accountType = acc.type;
            EmailLog.d(TAG, "acc name" + accountName);
            EmailLog.d(TAG, "acc type " + accountType);

            String duplicateAccountName = Utility.findDuplicateAccount(ctx, accountName);
            if (duplicateAccountName != null)
                continue;
            result += 1;

        }
        return result;

    }


    public static boolean isSdpEnabled() {
        return SdpHelper.isSdpEnabled();
    }

    public static void setSdpEnabled(Context context) {
        // Set AFW status first
        setAfwMode(context);
        if (isInAFWMode() || context != null && isInContainer(context)) {
            long token = Binder.clearCallingIdentity();
            SdpHelper.setSdpEnabled(context);
            Binder.restoreCallingIdentity(token);
        }
        Log.d(TAG, "setSdpEnabled : " + SdpHelper.isSdpEnabled());
    }

    public static boolean isSdpActive() {
        return SdpHelper.isSdpActive();
    }

    public static void registerSensitiveFileWithSdpIfNecessary(String fullFileName) {
        long token = Binder.clearCallingIdentity();
        try {
            if (isSdpEnabled() && !TextUtils.isEmpty(fullFileName)) {
                File file = new File(fullFileName);
                if (file != null && file.exists())
                    SdpHelper.FILE().setSensitive(file);
            }
        } catch (SdpCompromisedException sce) {
            Log.e(TAG, "registerSensitiveFileWithSdpIfNecessary :: Initialize Sdp first... is sdp enabled? " + SdpHelper.isSdpEnabled());
            Log.dumpException(TAG, sce);
        } finally {
            Binder.restoreCallingIdentity(token);
        }
    }

    public static File getAttachmentsSaveDirForAfw() {
        File downloadsDir = null;

        if (Utility.isInAFWMode()) {
            try {
                File afwDir = SdpHelper.FILE().getManagedProfileKnoxDir();
                if (afwDir != null && afwDir.exists()) {
                    downloadsDir = new File(afwDir.getAbsolutePath() + "/" + Environment.DIRECTORY_DOWNLOADS);
                }
            } catch (SdpCompromisedException e) {
                Log.dumpException(TAG, e);
            }

        }

        // Use default dir, if SDP disabled and we could not get AFW dir
        if (downloadsDir == null) {
            downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        }

        if (downloadsDir != null)
            Log.d(TAG, "getAttachmentsSaveDirForAfw() returns : " + downloadsDir.getAbsolutePath());
        return downloadsDir;
    }

    public static String getAttachmentsSavePathForAfw() {
        String afwDirPath = null;

        if (Utility.isInAFWMode()) {
            try {
                File afwDir = SdpHelper.FILE().getManagedProfileKnoxDir();
                if (afwDir != null && afwDir.exists()) {
                    afwDirPath = afwDir.getAbsolutePath();
                }
            } catch (SdpCompromisedException e) {
                Log.dumpException(TAG, e);
            }

        }

        // Use default dir, if SDP disabled and we could not get AFW path
        if (afwDirPath == null) {
            afwDirPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        }

        if (afwDirPath != null)
            Log.d(TAG, "getAttachmentsSavePathForAfw() returns : " + afwDirPath);
        return afwDirPath;
    }


    public static void onSdpStateChanged(Intent intent, SQLiteDatabase dbHandle, String dbAlias) {
        long token = Binder.clearCallingIdentity();
        try {
            SdpHelper.onSdpStateChanged(intent, dbHandle, dbAlias);
        } catch (SdpCompromisedException sce) {
            Log.e(TAG, "onSdpStateChanged :: Initialize Sdp first... is sdp enabled? " + SdpHelper.isSdpEnabled());
            Log.dumpException(TAG, sce);
        } finally {
            Binder.restoreCallingIdentity(token);
        }
    }

    public static void setSdpState(final SQLiteDatabase dbHandle, final String dbAlias) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SdpHelper.setSdpState(dbHandle, dbAlias);
                } catch (SdpCompromisedException sce) {
                    Log.e(TAG, "setSdpState :: Initialize Sdp first... is sdp enabled? " + SdpHelper.isSdpEnabled());
                    Log.dumpException(TAG, sce);
                }
            }
        }).start();
    }

    public static File getSdpCacheDir(Context context) {
        File cacheDir = null;

        try {
            if (SdpHelper.isSdpEnabled() && SdpHelper.FILE() != null) {
                cacheDir = SdpHelper.FILE().getCacheDir();
            }
        } catch (SdpCompromisedException e) {
            Log.dumpException(TAG, e);
        } catch (Exception e) {
            Log.dumpException(TAG, e);
        }

        if (cacheDir == null && context != null) {
            cacheDir = context.getCacheDir();
        }

        Log.d(TAG, "getSdpCacheDir returns : " + (cacheDir == null ? "null" : cacheDir.getAbsolutePath()));

        return cacheDir;
    }


    public static boolean isClickValid(int hash) {
        if (sLastId != hash) {
            sLastId = hash;
            return true;
        }
        return isClickValid();
    }

    public static void clearHashForClickValid() {
        sLastId = 0;
    }

    public static boolean isClickValid() {
        long newClickTime = SystemClock.elapsedRealtime();
        if (newClickTime - mLastClickTime < MIN_CLICK_INTERVAL) {
            Log.d(TAG, "isClickValid() invalid click - newClickTime = " + newClickTime + ", mLastClickTime = " + mLastClickTime);
            Log.d(TAG, "isClickValid() invalid click - time diff = " + (newClickTime - mLastClickTime));
            return false;
        } else {
            mLastClickTime = newClickTime;
            return true;
        }
    }

    public static boolean isClickValidForAnimation() {
        long newClickTime = SystemClock.elapsedRealtime();
        if (newClickTime - mLastClickTime < MIN_CLICK_INTERVAL_ANI) {
            return false;
        } else {
            mLastClickTime = newClickTime;
            return true;
        }
    }

    /**
     * Determines whether to refresh a Drafts folder. If server id is empty, we
     * deduce that its a local folder
     *
     * @param mailbox @return
     */

    public static boolean isServerDraftsFolder(Context cntx, Mailbox mailbox) {
        if (isDraftsSyncEnabled(cntx)) {
            if (mailbox != null && !TextUtils.isEmpty(mailbox.mServerId)
                    && isDraftsSyncEnabled(cntx, mailbox.mAccountKey)
                    && mailbox.mType == Mailbox.TYPE_DRAFTS) {
                return true;
            }
        }
        return false;
    }

    public static boolean isServerDraftsFolder(Context cntx, long mailboxId) {
        if (isDraftsSyncEnabled(cntx)) {
            Mailbox mailbox = Mailbox.restoreMailboxWithId(cntx, mailboxId);
            return isServerDraftsFolder(cntx, mailbox);
        }
        return false;
    }

    /**
     * Checks whether this account has a Server side Drafts folder.
     *
     * @param cntx @param accountId @return
     */

    public static boolean isServerDraftsFolderAvailable(Context cntx, long accountId) {
        if (isDraftsSyncEnabled(cntx)) {
            Mailbox mailbox = Mailbox.restoreMailboxOfType(cntx, accountId, Mailbox.TYPE_DRAFTS);
            return isServerDraftsFolder(cntx, mailbox);
        }
        return false;
    }

    /**
     * Determines whether Drafts Sync is enabled for this account
     *
     * @param accountId @return
     */
    public static boolean isDraftsSyncEnabled(Context cntx, long accountId) {
        return (isDraftsSyncEnabled(cntx) && AccountCache.isImap(cntx, accountId));
    }

    /**
     * IMAP-Drafts-Sync Determines if this feature is enabled.
     *
     * @param cntx @return
     */
    public static boolean isDraftsSyncEnabled(Context cntx) {
        return EmailFeature.isIMAPDraftSyncEnabled()
                && Preferences.getPreferences(cntx).getEnableIMAPDraftsSync();
    }

    /**
     * Determines whether the given string is a genuine server Id This is
     * applicable only for cases where server Id is a numeric value > 0
     *
     * @param serverId @return
     */
    public static boolean isGenuineServerId(String serverId) {
        if (!TextUtils.isEmpty(serverId)) {
            try {
                long serveId = Long.parseLong(serverId);
                if (serveId > 0) {
                    return true;
                }
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Makes a deep copy of a given source Message object
     *
     * @param sourceMsg
     */
    public static Message makeDeepCopy(Message sourceMsg) {
        Message newMessage = new Message();
        // Copying some important attributes.
        newMessage.mId = EmailContent.NOT_SAVED;
        newMessage.mAccountKey = sourceMsg.mAccountKey;
        newMessage.mMailboxKey = sourceMsg.mMailboxKey;
        newMessage.mMailboxType = sourceMsg.mMailboxType;
        newMessage.mMessageId = sourceMsg.mMessageId;
        newMessage.mServerId = sourceMsg.mServerId;
        newMessage.mDisplayName = sourceMsg.mDisplayName;
        newMessage.mFlagLoaded = EmailContent.Message.FLAG_LOADED_UNLOADED; // to
        // make
        // it
        // hidden
        return newMessage;
    }

    /**
     * This will deletes the mail permanently. i.e. it will be removed from
     * trash too
     *
     * @param messageId
     * @param accountId
     */
    public static void deleteMessagePermanently(final Context context, final long messageId,
                                                final long accountId, final long mailboxId) {
        EmailLog.d(TAG, "deleteMessagePermanently() is called : " + accountId + " " + messageId
                + " " + mailboxId);
        if (messageId < 1 || accountId < 1 || mailboxId < 1) {
            EmailLog.e(TAG, "deleteMessagePermanently() invalid input " + accountId + " "
                    + messageId + " " + mailboxId);
            return;
        }
        Utility.runAsync(new Runnable() {
            public void run() {
                AttachmentUtilities.deleteAllAttachmentFiles(context, accountId, messageId);
                BodyUtilites.deleteAllMessageBodyFilesUri(context, accountId, messageId);
                ContentResolver resolver = context.getContentResolver();

                Uri uri = ContentUris.withAppendedId(EmailContent.Message.SYNCED_CONTENT_URI,
                        messageId);
                resolver.delete(uri, null, null);
                // tmp_johnny
                // mLegacyController.processPendingActions(accountId);
            }
        });
    }

    static public Double getProtocolVersionDouble(String version) {
        if (CommonDefs.SUPPORTED_PROTOCOL_EX2003.equals(version)) {
            return CommonDefs.SUPPORTED_PROTOCOL_EX2003_DOUBLE;
        } else if (CommonDefs.SUPPORTED_PROTOCOL_EX2007.equals(version)) {
            return CommonDefs.SUPPORTED_PROTOCOL_EX2007_DOUBLE;
        } else if (CommonDefs.SUPPORTED_PROTOCOL_EX2007_EXT.equals(version)) {
            return CommonDefs.SUPPORTED_PROTOCOL_EX2007_EXT_DOUBLE;
        } else if (CommonDefs.SUPPORTED_PROTOCOL_EX2010.equals(version)) {
            return CommonDefs.SUPPORTED_PROTOCOL_EX2010_DOUBLE;
        } else if (CommonDefs.SUPPORTED_PROTOCOL_EX2010_EXT.equals(version)) {
            return CommonDefs.SUPPORTED_PROTOCOL_EX2010_EXT_DOUBLE;
        }
        throw new IllegalArgumentException("illegal protocol version");

    }

    public static boolean isRoaming(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        boolean isRoaming = false;
        // Ignore roaming status if WiFi is connected
        if (isWifiConnected(context)) {
            EmailLog.d(TAG, "isRoaming(): WiFi is connected.");
        } else {
            // Add dual standby model : check roaming state // hm.je
            if (IS_DUAL_SIM_MODEL) {
                int currentActiveNetwork = Settings.System.getInt(context.getContentResolver(),
                        "CURRENT_NETWORK", 0);
                if (currentActiveNetwork == 0) {
                    EmailLog.d(TAG, "ProgressTracker : current network is 0");
                    // isRoaming =
                    // tm.isNetworkRoaming(MultiSimManager.getSubId(0)[0]);
                    try {
                        isRoaming = MultiSimManager
                                .isNetworkRoaming((int) MultiSimManager.getSubscriptionId(0)[0]);
                    } catch (Exception e) {
                        Log.e(TAG, "Exception occur" + e.getMessage());
                    }
                } else {
                    EmailLog.d(TAG, "ProgressTracker : current network is 1");
                    int[] sub = MultiSimManager.getSubscriptionId(1);
                    if (sub != null && sub.length > 0) {
                        // isRoaming = tm.isNetworkRoaming(sub[0]);
                        try {
                            isRoaming = MultiSimManager.isNetworkRoaming((int) sub[0]);
                        } catch (Exception e) {
                            Log.e(TAG, "exception occur :" + e.getMessage());
                        }
                    }
                }
            } else {
                isRoaming = tm.isNetworkRoaming();
            }
            EmailLog.d(TAG, "isRoaming = " + isRoaming);
        }

        if (CommonDefs.ALWAYS_ROAMING)
            isRoaming = true;

        return isRoaming;
    }

    public static int getRoamingPreference(Context context) {
        AccountSetupbyCSC mAccountSetupbyCSCCameleon = new AccountSetupbyCSC(
                context.getApplicationContext(), true);
        if (mAccountSetupbyCSCCameleon.is_cameleonDATA() == true) {
            return (mAccountSetupbyCSCCameleon.isRoamPrefMenuDisplayed());
        }
        return DEFAULT_ROAMING_PREFERENCE_SPRINT;
    }

    public static Cursor getVipListCursor(Context context) {

        EmailContent.checkNative();
        Cursor c = null;

        try {
            c = context.getContentResolver().query(EmailContent.VIPListColumns.CONTENT_URI,
                    VIP_PROJECTION, null, null, EmailContent.VIPListColumns.EMAIL_ADDRESS + " asc");

            if (c == null) {
                Log.e(TAG, "getVipListCursor : Cursor is null.");
                return null;
            }

            if (c.getCount() == 0) {
                c.close();
                c = null;
            }
        } catch (Exception e) {
            Log.e(TAG, "getVipListCursor error :" + e.getMessage());
            if (c != null && !c.isClosed()) {
                c.close();
                c = null;
            }
        }
        return c;
    }

    public static void version(Context context) {
        String version;

        try {
            if (context != null) {
                PackageInfo i = context.getPackageManager().getPackageInfo(
                        context.getPackageName(), 0);

                version = i.versionName;
                Log.e(TAG, "VERSION  " + context.getPackageName() + " : " + version);
            }
        } catch (NameNotFoundException e) {
        }
    }

    private static String convertPlainTextToHtml(String text) {
        EmailLog.d("abcdef", "plain text");

        // Convert the plain text to HTML
        StringBuffer sb = new StringBuffer();
        String alignmentforHebrew = "";
        if (EmailFeature.isAlignmentForHebrew() == true) {
            if (text != null) {
                for (int i = 0; i < text.length(); i++) {
                    if ((text.charAt(i) >= '\u0590' && text.charAt(i) <= '\u05ff')
                            || (text.charAt(i) >= '\u0600' && text.charAt(i) <= '\u06ff')) {
                        alignmentforHebrew = " dir=\"rtl\"";
                        break;
                    }
                }
            }
        }

        if (EmailFeature.IsUseAutofit()) {
            sb.append("<html" + alignmentforHebrew + "> <body> <div>");
        } else {
            sb.append("<html" + alignmentforHebrew
                    + "> <body> <div style=\"word-break:keep-all;\">");
        }

        if (text != null) {
            text = EmailHtmlUtil.escapeCharacterToDisplay(text);
            sb.append(text);
        }
        sb.append("</div></body></html>");
        try {
            text = sb.toString();
        } catch (OutOfMemoryError oe) {
            Log.d(TAG, "patternMatching OutOfMemoryError");
        }

        return text;
    }

    public static String patternMatching(Context context, EmailContent.Message msg,
                                         String htmlData, boolean isTextOlny) {
        try {
            ViewPatternMatching mPMManager = new ViewPatternMatching();
            String text = htmlData;

            if (isTextOlny) {
                text = convertPlainTextToHtml(text);
            }
            if (EmailFeature.isPhyAddressSupport())
                mPMManager.pmInit(context, ViewPatternMatching.PATTERNMATCHING_OPTION_ALL,
                        msg.mTimeStamp);
            else
                mPMManager.pmInit(context, ViewPatternMatching.PATTERNMATCHING_OPTION_DEFAULT,
                        msg.mTimeStamp);
            if (CarrierValues.IS_CARRIER_TMB) {
                mPMManager.pmSetEnableOption(
                        ViewPatternMatching.PATTERNMATCHING_ENABLE_CALENDAR_KEYWORD, false);
            }

            mPMManager.pmSettimeout(EmailFeature.PATTERNMATCH_TIMEOUT);

            msg.mHtml = mPMManager.pmDataMatching(text, 1, 0);
        } catch (Exception e) {
        }
        return msg.mHtml;
    }

    /**
     * Return TalkBack enabled status
     *
     * @return : True - On , False - off
     */
    public static boolean isTalkBackEnabled(Context context) {
        boolean talkbackEnabled = false;
        String accesibilityService = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        if (accesibilityService != null) {
            // com.google.android.marvin.talkback
            // com.samsung.android.app.talkback
            talkbackEnabled = accesibilityService.matches("(?i).*TalkBackService.*");
        }

        return talkbackEnabled;
    }


    public static boolean isEmailNotificationsEnabled(Context context, long accountId) {
        Boolean bNotificationEnable = true;

        if (context != null && accountId > 0) {
            String[] selectionArgs = {String.valueOf(accountId)};
            bNotificationEnable = Utility.getBooleanFromSecContentProvider(context, Utility.getEmailPolicy(), selectionArgs, SecContentProviderURI.EMAILPOLICY_EMAILNOTIFICATIONS_METHOD, true);
        }

        return bNotificationEnable;
    }

    public static boolean isTTS(Context context, AccessibilityManager am) {
        if (am == null)
            am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);

        boolean isAccessibilityEnabled = am.isEnabled();
        boolean isExploreByTouchEnabled = am.isTouchExplorationEnabled();
        Log.d(TAG, "isAccessibilityEnabled : " + isAccessibilityEnabled + " isExploreByTouchEnabled : " + isExploreByTouchEnabled);
        if (isAccessibilityEnabled && isExploreByTouchEnabled)
            return true;
        else
            return false;
    }

    public static final int EPOCH_JULIAN_DAY = 2440588;

    public static int getJulianDay(long millis, long gmtoff) {
        long offsetMillis = gmtoff * 1000;
        long julianDay = (millis + offsetMillis) / DateUtils.DAY_IN_MILLIS;
        return (int) julianDay + EPOCH_JULIAN_DAY;
    }

    public static long getJulianMilli() {
        GregorianCalendar cal = new GregorianCalendar();
        long milli2 = cal.getTimeInMillis();

        long offset = cal.get(Calendar.ZONE_OFFSET);

        long jul = getJulianDay(milli2, offset);
        long sel = getJulianDay(System.currentTimeMillis(), offset);

        if (jul == sel) {
            cal.add(Calendar.HOUR_OF_DAY, 1);
        } else {
            cal.set(Calendar.HOUR_OF_DAY, 8);
        }
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTimeInMillis();
    }

    public static void checkEnablePackage(Activity activity) {

        Intent intent = null;
        intent = new Intent(Intent.ACTION_DIAL);
        boolean isOnlyWifiDevice = false;
        if (activity != null) {
            isOnlyWifiDevice = DataConnectionUtil.getInstance(activity).IsWifiAndDataModel();
        }
        if (activity != null && !isOnlyWifiDevice) {
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                sEnableCall = true;
            } else {
                sEnableCall = false;
            }
        }
        intent = null;
        intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("sms", "", null));
        if (activity != null && !isOnlyWifiDevice) {
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                sEnableMessage = true;
            } else {
                sEnableMessage = false;
            }
        }
        intent = null;
        intent = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT, Uri.fromParts("tel",
                "", null));
        if (activity != null) {
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                sEnableContact = true;
            } else {
                sEnableContact = false;
            }
        }

        intent = null;
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("calendar:"));
        intent.setAction(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");

        if (activity != null) {
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                sEnableCalendar = true;
            } else {
                sEnableCalendar = false;
            }
        }
        intent = null;
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http:"));
        if (activity != null) {
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                sEnableBroser = true;
            } else {
                sEnableBroser = false;
            }
        }
        intent = null;
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"));

        if (activity != null) {
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                sEnableMap = true;
            } else {
                sEnableMap = false;
            }
        }
    }


    public static boolean isExternSDCardMounted(Context context) {
        Log.d(TAG, "isExternSDCardMounted");
        String externalVolume = getExternalStorageDirectorySd(context);
        String state = externalVolume != null ? ((StorageManager) context
                .getSystemService(Context.STORAGE_SERVICE)).getVolumeState(externalVolume) : null;
        if (state != null && state.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            Log.d(TAG, "isExternSDCardMounted ret=true");
            return true;
        }
        return false;
    }

    public static String getExternalStorageDirectorySd(Context context) {
        StorageVolume externalVolume = null;
        StorageManager storageMgr = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        StorageVolume[] storageVolumes = storageMgr.getVolumeList();
        int length = storageVolumes.length;
        for (int i = 0; i < length; i++) {
            if (storageVolumes[i].isRemovable() && storageVolumes[i].getSubSystem().equals("sd")) {
                externalVolume = storageVolumes[i];
            }
        }
        if (null == externalVolume) {
            return null;
        }
        Log.d(TAG, "getExternalStorageDirectorySd externalVolume=" + externalVolume.getPath());
        return externalVolume.getPath();
    }

    public static void deleteTempMessageLocalOnly(final Context context, final long messageId, final long accountId) {
        if (context != null && messageId > 0 && accountId > 0) {
            Utility.runAsync(new Runnable() {
                public void run() {
                    try {
                        AttachmentUtilities.deleteAllAttachmentFiles(context, accountId, messageId);
                        BodyUtilites.deleteAllMessageBodyFilesUri(context, accountId, messageId);
                        ContentResolver resolver = context.getContentResolver();
                        if (resolver != null) {
                            Uri uri = ContentUris.withAppendedId(EmailContent.Message.CONTENT_URI, messageId);
                            resolver.delete(uri, null, null);
                        }
                    } catch (Exception e) {
                        Log.dumpException(TAG, e);
                    }
                }
            });
        }
    }

    public static boolean isDataCapable(Context context) {
        boolean ret = true;
        ConnectivityManager cm = null;

        if (context == null) {
            Log.d(TAG, "(isDataCapable)null context");
            return ret;
        }

        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) {
            Log.d(TAG, "(isDataCapable)null ConnectivityManager");
            return ret;
        }
        //ret = (cm.isNetworkSupported(ConnectivityManager.TYPE_MOBILE) == true);
        NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkInfo != null) {
            ret = networkInfo.isAvailable();
        }
        Log.d(TAG, "(isDataCapable)ret = " + ret);
        return ret;
    }
}
