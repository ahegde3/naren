
package com.samsung.android.emailcommon;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.provider.Settings;

import com.samsung.android.emailcommon.provider.EmailContent;
import com.samsung.android.emailcommon.provider.EmailContent.Account;
import com.samsung.android.emailcommon.provider.EmailContent.AccountColumns;
import com.samsung.android.emailcommon.provider.EmailContent.HostAuth;
import com.samsung.android.emailcommon.provider.ImapConstants;
import com.samsung.android.emailcommon.system.CarrierValues;
import com.samsung.android.emailcommon.utility.EmailLog;
import com.samsung.android.emailcommon.utility.SecFeatureWrapper;
import com.samsung.android.emailcommon.utility.Utility;
import com.samsung.android.emailcommon.utility.ViewFileLogger;
import com.samsung.android.emailcommon.variant.CommonDefs;
import com.samsung.android.emailcommon.variant.DPMWraper;
import com.samsung.android.feature.FloatingFeature;
import com.sec.android.app.CscFeature;
import com.sec.android.app.CscFeatureTagCommon;
import com.sec.android.app.CscFeatureTagContact;
import com.sec.android.app.CscFeatureTagEmail;
import com.sec.android.app.CscFeatureTagFramework;
import com.sec.android.emergencymode.EmergencyManager;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class EmailFeature {
    private static final String EDGE_STRIPE_FEATURE_LEVEL = "SEC_FLOATING_FEATURE_COMMON_CONFIG_EDGE_STRIPE";
    private static final String PEOPLE_EDGE_NOTIFICATION = "com.sec.feature.people_edge_notification";

    private static final String TAG = "EmailFeature";

    public static final String BUILD_CARRIER = CarrierValues.PRODUCT_NAME;

    public static boolean mHtmlSanitizerAtView = true;

    public static boolean isHtmlSanitizerAtView() {

        return mHtmlSanitizerAtView;
    }

    /**
     * @param cscFeatureKey It is defined in CscFeatureTagEmail
     * @return true or false. CscFeatureTagEmail.Bool_NoTag is default.
     */
    public static boolean getEnableStatus(String cscFeatureKey) {
        return getEnableStatus(cscFeatureKey, false);
    }

    /**
     * @param cscFeatureKey It is defined in CscFeatureTagEmail
     * @param enableLog     If this is false, we don't display log lines
     * @return true or false. CscFeatureTagEmail.Bool_NoTag is default.
     */
    public static boolean getEnableStatus(String cscFeatureKey, boolean enableLog) {
        boolean ret = CscFeatureTagEmail.Bool_NoTag;
        try {
            ret = CscFeature.getInstance().getEnableStatus(cscFeatureKey);
            if (enableLog)
                EmailLog.d(TAG, cscFeatureKey + ": " + ret);
        } catch (Exception e) {
            if (enableLog)
                EmailLog.d(TAG, cscFeatureKey + ": exception occurred");
        }
        return ret;
    }

    /**
     * @param cscFeatureKey It is defined in CscFeatureTagEmail
     * @return String or empty(not null). CscFeatureTagEmail.Str_NoTag is
     * default.
     */
    public static String getString(String cscFeatureKey) {
        return getString(cscFeatureKey, false);
    }

    /**
     * @param cscFeatureKey It is defined in CscFeatureTagEmail
     * @param enableLog     If this is false, we don't display log lines
     * @return String or empty(not null). CscFeatureTagEmail.Str_NoTag is
     * default.
     */
    public static String getString(String cscFeatureKey, boolean enableLog) {
        String ret = CscFeatureTagEmail.Str_NoTag;
        try {
            ret = CscFeature.getInstance().getString(cscFeatureKey);
            if (enableLog)
                EmailLog.d(TAG, cscFeatureKey + ": " + ret);
        } catch (Exception e) {
            if (enableLog)
                EmailLog.d(TAG, cscFeatureKey + ": exception occurred");
        }
        return ret;
    }

    /**
     * @param cscFeatureKey It is defined in CscFeatureTagEmail
     * @return int or 0. CscFeatureTagEmail.Int_NoTag is default.
     */
    public static int getInteger(String cscFeatureKey) {
        return getInteger(cscFeatureKey, false);
    }

    /**
     * @param cscFeatureKey It is defined in CscFeatureTagEmail
     * @param enableLog     If this is false, we don't display log lines
     * @return int or 0. CscFeatureTagEmail.Int_NoTag is default.
     */
    public static int getInteger(String cscFeatureKey, boolean enableLog) {
        int ret = CscFeatureTagEmail.Int_NoTag;
        try {
            ret = CscFeature.getInstance().getInteger(cscFeatureKey);
            if (enableLog)
                EmailLog.d(TAG, cscFeatureKey + ": " + ret);
        } catch (Exception e) {
            if (enableLog)
                EmailLog.d(TAG, cscFeatureKey + ": exception occurred");
        }
        return ret;
    }

    public enum DOWNLOADPROGRESS {
        BASE, HALF, NINETY, TWICE
    }

    public static boolean IsUseRetrievesize() {
        return true;
    }

    public static boolean IsAmoledDisplay() {
        return FloatingFeature.getInstance().getEnableStatus(
                "SEC_FLOATING_FEATURE_LCD_SUPPORT_AMOLED_DISPLAY");
    }

    public static boolean hideStatusBar() {
//        String hideStatusBar = FloatingFeature.getInstance().getString(
//                "SEC_FLOATING_FEATURE_COMMON_CONFIG_HIDE_STATUS_BAR");
//        return !TextUtils.isEmpty(hideStatusBar) && "LAND".equals(hideStatusBar);
        return true;
    }

    public static boolean IsUseNewDownloadProgress() {
        return true;
    }

    public static DOWNLOADPROGRESS GetDownloadProgress() {
        return DOWNLOADPROGRESS.NINETY;
    }

    public static int GetDownloadPercent() {
        if (GetDownloadProgress() == DOWNLOADPROGRESS.HALF) {
            return 50;
        } else if (GetDownloadProgress() == DOWNLOADPROGRESS.NINETY) {
            return 90;
        }

        return 100;
    }

    public static boolean IsUseMessageWebViewSizeChange() {
        return true;
    }

    public static boolean IsUseDownloadCancel() {
        return true;
    }

    public static boolean IsUseBodyRefreshTestMode() {
        return false;
    }

    static boolean isUseTranslate = false;

    public static void CheckTranslate(Context context) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
                "com.sec.android.app.translator.TRANSLATE"), 0);

        if (activities.size() == 0) {
            // disabled
            isUseTranslate = false;
        } else {
            // enabled
            isUseTranslate = true;
        }
    }

    public static boolean showAutofitOptionMenu = true;
    public static boolean isUseAutofit = true;

    public static boolean IsUseAutofit() {
        return isUseAutofit;
    }

    public static boolean IsUseSaveFromService() {
        return !(isSaveAsAttachments() || isDefaultFolderToSaveAttachments());
    }

    public static boolean IsUsePager() {
        return true;
    }

    public static boolean IsUseWordWrappingByScript() {
        return false && !IsUseAutofit();
    }

    public static boolean IsUsePatterMatchByJavaScript() {
        return false;
    }

    public static boolean IsUseCreateWebview() {
        return false; //IsSplitMode() ? !IsUseAutofit() : false;
    }

    public static boolean IsUseTextAutoSizing() {
        return false;
    }

    public static boolean IsUseChangeFontSizeByJavaScript() {
        return true;
    }

    public static boolean IsUseContentReady() {
        return false;
    }

    public static boolean IsUsePatternMatchingAtSync() {
        return false;
    }

    public static final int IN_RENDERMESSAGE_TASK = 0;
    public static final int IN_WEBVIEW_CLICK = 1;
    public static final int IN_UPDATEHEADER = 2;
    public static final int IN_CONTENT_READY = 3;
    public static final int PATTERNMATCH_TIMEOUT = 300; // ms

    public static int getPatternMethod() {
        int ret = IN_CONTENT_READY;

        if (IsUsePatternMatchingAtSync()) {
            ret = IN_WEBVIEW_CLICK;
        }
        return ret;
    }

    public static boolean IsUseSecondPatternMatching() {
        return false;
    }

    public static final int UPDATE_NONE = -1;
    public static final int RELOAD_DATA = 0;
    public static final int UPDATE_BODY = 1;
    public static final int UPDATE_HEAD_BODY = 2;
    private static int updateStatus = getPatternMethod() == IN_RENDERMESSAGE_TASK ? UPDATE_NONE
            : UPDATE_BODY;

    public static int getPatternUpdateMethod() {
        return updateStatus;
    }

    public static void setPatternUpdateMethod(int update) {
        updateStatus = update;
    }

    public static final int CACHE_HTML_NONE = -1;
    public static final int CACHE_HTML_ALL = 0;
    public static final int CACHE_HTML_PATTERN = 1;

    public static int getCacheHtmlMethod() {
        return CACHE_HTML_NONE;
    }

    public static boolean IsUseFixedViewport() {
        return false;
    }

    public static boolean IsUseContactPhoto() {
        return false;
    }

    public static boolean IsUseFirstDrawToHeader() {
        return true;
    }

    public static boolean IsUseAllAddPOPAttachmnet() {
        return true;
    }

    public static boolean IsUseEmbeddedImageDisable() {
        return true;
    }

    /**
     * @return if it is false, layout of attachment is show only when load
     * completed.
     */
    public static boolean IsUseAttLayoutAlwaysShow() {
        return true;
    }

    public static final int ALPHA_ANIMATION = 0;

    public static int getDetailViewAnimationType() {
        return ALPHA_ANIMATION;
    }

    public static boolean IsUseToolbar() {
        return true; //Utility.isTabletModel() || Utility.isFonbletModel() || Utility.isNoteModel();
    }

    public static boolean IsUseThreePane() {
        return true; //Utility.isFonbletModel() || Utility.isNoteModel() || EmailFeature.isSupportDualScreenMode();
    }

    public static boolean IsUseAdvancedUIFromNote5() {
        return true;
    }

    public static boolean IsUseAdvancedUIInDownload() {
        return true;
    }

    public static boolean IsSupportSimpleSetup() {
        // boolean support = false;
        //
        // if (CarrierValues.IS_DEVICE_NOBLE || CarrierValues.IS_DEVICE_ZEN
        // || (CarrierValues.IS_DEVICE_A8 && Utility.isMainlandChinaModel())
        // || CarrierValues.IS_DEVICE_TABS2) {
        // support = true;
        // }
        return true;
    }

    public static boolean IsSupportChinaSetup() {
        boolean support = false;

        if (Utility.isMainlandChinaModel()) {
            support = true;
        }

        return support;
    }

    public static boolean IsSupportResetSettings() {
        boolean support = true;

        return support;
    }

    public static boolean IsUseRefreshCursor() {
        return true;
    }

    // Current off because LO Style issue(P150514-01095)
    public static boolean IsUseRemoveHeight() {
        return EmailFeature.IsUseAutofit() && false;
    }

    // Remove the width=100% !important in body sytle
    public static boolean IsUseRemoveWidthImportantInBodyStyle() {
        return EmailFeature.IsUseAutofit();
    }

    public static boolean IsUseDownloadBoosterWithLoadmore() {
        return false;// CarrierValues.IS_CARRIER_CHINA &&
        // CarrierValues.IS_DEVICE_A8;
    }

    // PLM style - BACKGROUND: url(cid:04A24XEV4D4T@namo.co.kr)
    // P150602-06906
    public static boolean IsUseCheckURL() {
        return true;
    }

    // Performance issue.
    // P150528-03366, P150530-00792
    public static boolean IsUseOpenOneMessage() {
        return true;
    }

    static boolean sIsSelectionMode = false;

    public static boolean IsUseSelectionModeOpen() {
        return IsSplitMode(); // CarrierValues.IS_DEVICE_TABS2;
    }

    public static boolean IsSelectionMode() {
        return sIsSelectionMode;
    }

    public static void setSelectionMode(boolean isSelectionMode) {
        sIsSelectionMode = isSelectionMode;
    }

    static boolean sIsSplitMode = false;

    public static boolean IsSplitMode() {
        return sIsSplitMode;
    }

    public static void setSplitMode(boolean isSpllitMode) {
        sIsSplitMode = isSpllitMode;
    }

    public static boolean IsUseOnlyWebviewAnimation() {
        return false;// Utility.isNoteModel();
    }

    public static boolean IsUsePagerClear() {
        return false; // !IsUseAdvancedUIFromNote5();
    }

    public static boolean IsUseLowerCaseForMSOffice() {
        return true;
    }

    public static boolean isUseChangeIRMConcept() {
        return true;
    }

    public static boolean IsFullViewInSplitMode() {
        return false;
    }

    public static boolean IsFullAnimationInThreadOpen() {
        return false;
    }

    public static boolean IsUseSpamInPopupMenu() {
        return false;
    }

    public static boolean IsUseNewInlineImageUpdateScript() {
        return true;
    }

    public static final String CONDENSED = "condensed";

    public static boolean IsLargeScale(Context context) {
        if (context == null) {
            return false;
        }

        // 1(560): object scaling small, 0(640): object scaling large
        //return Settings.System.getInt(context.getContentResolver(), Settings.System.CONDENSED, 0) != 0;
        return Settings.System.getInt(context.getContentResolver(), CONDENSED, 0) == 0;
    }

    public static boolean IsUseSmoothScrollToHeader() {
        return false;
    }

    public static boolean DEBUG_TOUCHEVENT = false;
    public static boolean DEBUG_OPEN_TIME_CHECK = false;
    public static boolean DEBUG_MESSAGE_OPEN_TIME_CHECK = false;
    public static boolean DEBUG_ATTACHMENT_DOWNLOAD_TIME_CHECK = false;
    public static boolean DEBUG_EML_OPEN_TIME_CHECK = false;
    public static boolean DEBUG_SAVE_HTML = false;
    public static boolean DEBUG_WEBVIEW = false;
    public static boolean DEBUG_WEBVIEW_JAVASCRIPT = false;
    public static boolean DEBUG_WEBVIEW_JAVASCRIPT_DETAIL = false;
    public static boolean DEBUG_VIEW_SAVE_LOG_FILE = false;
    public static boolean DEBUG_VIEW_LOADMORE_TIME = false;
    public static boolean DEBUG_VIEW_LOADMORE_EAS_TIME = false;
    public static boolean DEBUG_VIEW_REFRESH_BODY_MENU = false;
    public static boolean DEBUG_NEWPAGER = false;
    public static boolean DEBUG_WEBVIEW_INTERFACE = false;
    public static boolean DEBUG_WEBVIEW_INTERFACE_DETAIL = false;
    public static boolean DEBUG_WEBVIEW_SIZE = false;
    public static boolean DEBUG_CHECK_INLINE = false;

    /*
     * private static long startTime, prevTime; private static long
     * startDownTime, prevDownTime; private static long startOpenTime,
     * prevOpenTime;
     */

    public static void debugStartTime(String tag, String log) {
        if (tag.startsWith("DEBUG_MESSAGE_OPEN_TIME_CHECK")
                || tag.startsWith("DEBUG_ATTACHMENT_DOWNLOAD_TIME_CHECK")
                || tag.startsWith("DEBUG_VIEW_LOADMORE_TIME")
                || tag.startsWith("DEBUG_VIEW_LOADMORE_EAS_TIME")) {
            Logging.startDownTime = Logging.prevDownTime = Logging.startOpenTime = Logging.prevOpenTime = System.currentTimeMillis();
            android.util.Log.i(tag, log + " => debugStartTime()");
            if (DEBUG_VIEW_SAVE_LOG_FILE)
                ViewFileLogger.log(tag, log + " => debugStartTime()");

        } else {
            Logging.startTime = Logging.prevTime = System.currentTimeMillis();
            android.util.Log.d("DEBUG_TIME_CHECK", log + " => debugStartTime()");
            if (DEBUG_VIEW_SAVE_LOG_FILE)
                ViewFileLogger.log("DEBUG_TIME_CHECK", log + " => debugStartTime()");
        }
    }

    public static void debugTime(String tag, String log) {
        long curTime = System.currentTimeMillis();

        if (tag.startsWith("DEBUG_MESSAGE_OPEN_TIME_CHECK")) {
            long debugTime = curTime - Logging.prevOpenTime;
            double totalTime = (double) (curTime - Logging.startOpenTime) / 1000.0;
            Logging.prevOpenTime = curTime;
            android.util.Log.d(tag, log + " => debugTime(" + debugTime + "), totalTime("
                    + totalTime + ")");
            if (DEBUG_VIEW_SAVE_LOG_FILE)
                ViewFileLogger.log(tag, log + " => debugTime(" + debugTime + "), totalTime("
                        + totalTime + ")");
        } else if (tag.startsWith("DEBUG_ATTACHMENT_DOWNLOAD_TIME_CHECK")) {
            long debugTime = curTime - Logging.prevDownTime;
            double totalTime = (double) (curTime - Logging.startDownTime) / 1000.0;
            Logging.prevDownTime = curTime;
            android.util.Log.d(tag, log + " => debugTime(" + debugTime + "), totalTime("
                    + totalTime + ")");
            if (DEBUG_VIEW_SAVE_LOG_FILE)
                ViewFileLogger.log(tag, log + " => debugTime(" + debugTime + "), totalTime("
                        + totalTime + ")");
        } else if (tag.startsWith("DEBUG_EML_OPEN_TIME_CHECK")
                || tag.startsWith("DEBUG_VIEW_LOADMORE_TIME")
                || tag.startsWith("DEBUG_VIEW_LOADMORE_EAS_TIME")) {
            long debugTime = curTime - Logging.prevTime;
            double totalTime = (double) (curTime - Logging.startTime) / 1000.0;
            Logging.prevTime = curTime;
            android.util.Log.d(tag, log + " => debugTime(" + debugTime + "), totalTime("
                    + totalTime + ")");
            if (DEBUG_VIEW_SAVE_LOG_FILE)
                ViewFileLogger.log(tag, log + " => debugTime(" + debugTime + "), totalTime("
                        + totalTime + ")");
        } else {
            long debugTime = curTime - Logging.prevTime;
            double totalTime = (double) (curTime - Logging.startTime) / 1000.0;
            Logging.prevTime = curTime;
            android.util.Log.d("DEBUG_TIME_CHECK", log + " => debugTime(" + debugTime
                    + "), totalTime(" + totalTime + ")");
        }
    }

    private static boolean hasPackage(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);
            // ApplicationInfo appInfo = pi.applicationInfo;
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static boolean isSetupChinaCTC189PredefinedAccount() {
        return "CTC".equals(getString(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_ADDISPACCOUNT));
    }

    public static boolean isSetupChinaCMCC139PredefinedAccount() {
        if (CarrierValues.IS_CARRIER_CHM)
            return true;
        else
            return false;
    }

    public static boolean isReceiveOptionCTC() {
        return getEnableStatus(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_ENABLEDETAILRECOPTION);
    }

    public static boolean isEasRemoveSyncInterval12Hours() {
        return getEnableStatus(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_EASDISABLEOPTIONSYNCINTERVAL);
    }

    // public static boolean isSupportYahooProvider() {
    // return
    // getEnableStatus(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_SUPPORT_YAHOOPROVIDER);
    // }

    public static String isKoreaIspAccountsetup() {
        String result = getString(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_ADDISPACCOUNT);
        if (Utility.isTabletModel()) {
            return null;
        } else {
            return (result.length() == 0 || "CTC".equals(result)) ? null : result;
        }
    }

    public static String isKorea() {
        String result = getString(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_ADDISPACCOUNT);
        return (result.length() == 0 || "CTC".equals(result)) ? null : result;
    }

    // public static int getEasDefaultSyncInterval() {
    // return
    // getInteger(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_EASDEFAULTSYNCINTERVAL);
    // }
    //
    // public static int getDefaultSyncInterval() {
    // return
    // getInteger(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_DEFAULTSYNCINTERVAL);
    // }

    public static boolean isSupportDmOption() {
        return getEnableStatus(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_ENABLEACCOUNTGENERATIONFROMDM);
    }

    /*
     * public static boolean isAnotherResolutionDisplay() { return
     * getEnableStatus
     * (CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_ANOTHERRESOLUTIONDISPLAY); }
     */
    public static int getEasSyncServiceCommandTimeoutValue() {
        return getInteger(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_EASSYNCSERVICECOMMANDTIMEOUTVALUE);
    }

    public static int getEasSyncServiceConnectionTimeoutValue() {
        return getInteger(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_EASSYNCSERVICECONNECTIONTIMEOUTVALUE);
    }

    public static boolean isEasNotUseProxy() {
        return getEnableStatus(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_EASDONOTUSEPROXY);
    }

    public static boolean isConfirmBeforeSendIfSubjectEmpty() {
        return getEnableStatus(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_ENABLEPROMPTWHENSENDINGEMPTYSUBJECT);
    }

    public static boolean isNotiAttachmentDownAtForward() {
        return getEnableStatus(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_ENABLENOTIWHENFWDWITHUNDOWNLOADEDCONTENTS);
    }

    public static boolean isChangeFilenameUnsupportedCharExists() {
        return getEnableStatus(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_REPLACEUNSUPPORTEDCHARWITHUNDERBARINATTACHMENT);
    }

    public static boolean isDefaultFolderToSaveAttachments() {
        return CscFeature.getInstance().getEnableStatus(
                CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_SETDOWNLOADFOLDERNAMEBYMIMETYPE);
    }

    public static boolean isSaveAsAttachments() {
        // save as function for Chinese CTC requirement
        //return CscFeature.getInstance().getEnableStatus(
        //        CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_ENABLESAVEASATTACHMENT);
        // sjpado.seo, disabled because there is not related requirement any longer
        return false;
    }

    public static boolean isUseEmptyServerValue() {
        return getEnableStatus(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_DISABLEAUTODOMAINGENERATIONDURINGMANUALSETUP);
    }

    public static boolean isNotiPopupWhenChangeSyncSchedule() {
        return getEnableStatus(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_ENABLEPOPUPWHENCHANGINGSYNCSCHEDULE);
    }

    public static boolean isBidiEnabled() {
        return getEnableStatus(CscFeatureTagFramework.TAG_CSCFEATURE_FRAMEWORK_ENABLEBIDIRECTION);
    }

    public static boolean isSncEnabled(Context context) {
        // For KNOX, disable the S&C feature, since the token will get
        // invalidated,
        // if getPIN is called twice for the same MDN
        if (Utility.isInContainer(context)) {
            return false;
        }
        return getEnableStatus(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_ENABLESYNCANDCONNECT);
    }

    public static boolean isUseDefaultSmtpPort25() {
        if (CarrierValues.IS_CARRIER_CHN || CarrierValues.IS_CARRIER_CHU
                || CarrierValues.IS_CARRIER_CHM || CarrierValues.IS_CARRIER_TGY) {
            return true;
        }
        if (getInteger(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_USEFIXEDSMTPPORTAS) == 25) {
            return true;
        }
        return false;
    }

    public static boolean isEnableUIDisplayMirroring() {
        return false; // getEnableStatus(CscFeatureTagCommon.TAG_CSCFEATURE_COMMON_ENABLEUIDISPLAYMIRRORING);
    }

    public static boolean isAlignmentForHebrew() {
        return getEnableStatus(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_ALIGNMENTFORRTL);
    }

    public static boolean isActivateAccountWithPredefinedIdPwd() {
        return getEnableStatus(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_ACTIVATEACCOUNTWITHPREDEFINEDIDPWD);
    }

    public static boolean is_CHAMELEON() {
        return getEnableStatus(CscFeatureTagCommon.TAG_CSCFEATURE_COMMON_USECHAMELEON);
    }

    public static boolean is_ReplaceNameGalaxy() {
        return getEnableStatus("CscFeature_Common_ReplaceSecBrandAsGalaxy");
    }

    public static String getAccountHintDomain() {
        return getString(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_REPLACEHINTDOMAINAS);
    }

    public static boolean isReplaceNotiIconCTC() {
        return "189".equals(getString(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_REPLACENOTIICON4));
    }

    public static boolean isUseDefaultBrowserOnly4UrlLink() {
        return getEnableStatus(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_USEDEFAULTBROWSERONLY4URLLINK);
    }

    public static boolean isEnableLocalSymbolTable() {
        String tag = getString(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_ENABLELOCALSYMBOLTABLE);
        return (tag.length() > 0 && tag.equalsIgnoreCase("docomo"));
    }

    public static boolean isEnablePromptToSelectApp4EmailAddress() {
        return getEnableStatus(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_ENABLEPROMPTTOSELECTAPP4EMAILADDRESS);
    }

    /**
     * Feature flag for displaying on device help for Email app. When this flag
     * is set, user's will see a help icon during exchange account setup
     * Clicking on the help icon will display a popup help message related to
     * the adjacent field
     *
     * @return true: if Csc flag to show help is enabled in feature.xml file
     * false: if Csc flag to show help is disabled in feature.xml file
     */
    public static boolean showEmailHelp() {
        return getEnableStatus(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_ENABLEONDEVICEHELP);
    }

    public static String getFontAttributeDisabled() {
        return getString(CscFeatureTagEmail.TAG_CSCFEATURE_EMAIL_DISABLEFONTATTRIBUTEDURINGCOMPOSING);
    }

    public static String getContactReplacePackageName() {
        return getString(CscFeatureTagContact.TAG_CSCFEATURE_CONTACT_REPLACEPACKAGEAS);
    }

    public static boolean isContactReplacePackage() {
        return getContactReplacePackageName().length() > 0;
    }

    public static boolean isEnableEpsonMobilePrint() {
        return "Epson"
                .equals(getString(CscFeatureTagCommon.TAG_CSCFEATURE_COMMON_ADDEXTMOBILEPRINT));
    }

    public static boolean isVibration4NotiDuringCall() {
        return getEnableStatus(CscFeatureTagCommon.TAG_CSCFEATURE_COMMON_VIBRATION4NOTIDURINGCALL);
    }

    // sjpado.seo start, UX for Chinese premium folder model is applided about
    // focus issue_20120620
    public static boolean isChinesePremiumFolder(Context context) {
        try {
            Configuration config = context.getResources().getConfiguration();
            if ((config.keyboard == Configuration.KEYBOARD_12KEY)
                    && (config.navigation == Configuration.NAVIGATION_DPAD)) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    // sjpado.seo end

    // Apply UX guide - Remove action bar title if there button exist when
    // portrait mode.
    public static boolean isRemoveTitle4Button() {
        return true;
    }

    public static boolean is5FontModel() {
        return true;
    }

    public static boolean isPhyAddressSupport() {
        return true;
    }

    public static final String FEATURE_SPEN_USP = "com.sec.feature.spen_usp";

    public static boolean isDrawingModeSupport(Context context) {
        if (CarrierValues.IS_DEVICE_H || CarrierValues.IS_DEVICE_T0 || CarrierValues.IS_DEVICE_T || CarrierValues.IS_DEVICE_NOBLE)
            return true;
        else
            return hasFeature(context, FEATURE_SPEN_USP);
    }

    public static final String FEATURE_MULTI_WINDOW = "com.sec.feature.multiwindow";

    public static boolean isMultiWindowSupport(Context context) {
        return hasFeature(context, FEATURE_MULTI_WINDOW);
    }

    public static boolean isMultiWindows() {
        return "1".equals(android.os.SystemProperties.get("sys.multiwindow.running"));
    }

    /**
     * Feature in the package manager
     */
    public static boolean isSupportHoveringUI(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_HOVERING_UI);
    }

    public static boolean isSupportFingerAirView() {
        return FloatingFeature.getInstance().getEnableStatus(
                "SEC_FLOATING_FEATURE_COMMON_GESTURE_WITH_FINGERHOVER");
    }

    public static boolean isSupportAirGesture() {
        return FloatingFeature.getInstance().getEnableStatus(
                "SEC_FLOATING_FEATURE_COMMON_GESTURE_WITH_IRSENSOR");
    }

    public static boolean isSupportFlashNotification() {
        return FloatingFeature.getInstance().getEnableStatus(
                "SEC_FLOATING_FEATURE_COMMON_SUPPORT_FLASH_NOTIFICATION");
    }

    public static boolean isSupportDualScreenMode() {
        return FloatingFeature.getInstance().getEnableStatus(
                "SEC_FLOATING_FEATURE_COMMON_SUPPORT_DUAL_DISPLAY");
    }

    private static boolean hasFeature(Context context, String feature) {
        return context.getPackageManager().hasSystemFeature(feature);
    }

    public static boolean isPGPEnabled() {
        return true;

    }

    public static boolean isDisableCheckFrequencyItemUnder15() {
        // if (CarrierValues.IS_CARRIER_ATT ||
        // CarrierValues.IS_CARRIER_WHOLE_SPRINT)
        // return true;
        //
        // return false;
        return true;
    }

    public static boolean isRTLLanguage() {
        String currentLanguage = Locale.getDefault().getLanguage();
        return (("ar".equals(currentLanguage)) || ("iw".equals(currentLanguage))
                || ("he".equals(currentLanguage)) || ("ur".equals(currentLanguage)) || ("fa"
                .equals(currentLanguage)));
    }

    public static boolean showOutboxErrorReasonPhrase() {
        return (CarrierValues.IS_CARRIER_VZW);
    }

    public static boolean showLegacySpecialErrorReasonPhrase() {
        return (CarrierValues.IS_CARRIER_VZW);
    }

    public static boolean isAttExceptionDevice() {
        if (CarrierValues.IS_CARRIER_ATT == true && "d2uc".equals(BUILD_CARRIER) == false
                && "jactivelteuc".equals(BUILD_CARRIER) == false
                && "jflteuc".equals(BUILD_CARRIER) == false
                && "hlteuc".equals(BUILD_CARRIER) == false
                && "klteuc".equals(BUILD_CARRIER) == false) {
            return false;
        } else {
            return true;
        }
    }

    // changes@siso.atul parallel attachment download start
    public static boolean isUseParallelAttachmentDownload() {
        return true;
    }

    // changes@siso.atul parallel attachment download end

    private static final String EASY_MODE_SWITCH = "easy_mode_switch";
    private static final String EASY_MODE_EMAIL = "easy_mode_email";

    /*    private static String DEVICE_NAME = null;

        public static String whatIsDeviceModel() {
            if (DEVICE_NAME == null) {
                EmailLog.d(TAG, "ro.product.model = " + SystemProperties.get("ro.product.model"));
                EmailLog.d(TAG, "ro.product.name = " + SystemProperties.get("ro.product.name"));
                EmailLog.d(TAG, "ro.product.device = " + SystemProperties.get("ro.product.device"));
                DEVICE_NAME = SystemProperties.get("ro.product.device");
            }
            return DEVICE_NAME;
        }
    */
    public static boolean isEasyHomeMode(Context context) {
        if (context == null)
            return false;

/*        String deviceName = whatIsDeviceModel();

        if (deviceName != null) {
            if ((deviceName.startsWith("d2") || deviceName.startsWith("t0"))
                    || (isKoreaIspAccountsetup() != null && (deviceName.startsWith("m0") || deviceName
                            .startsWith("c1"))))
                return false;

            if ("m0".equals(deviceName) || "m3".equals(deviceName))
                return false;
        }
*/
        int isDeviceEasyMode = Settings.System.getInt(context.getContentResolver(),
                EASY_MODE_SWITCH, 1);
        int isAppEasyMode = Settings.System
                .getInt(context.getContentResolver(), EASY_MODE_EMAIL, 1);

        if (isDeviceEasyMode == 0 && isAppEasyMode == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isSamsungAccount() {
        return false;
    }

    public static boolean isSMTPPipeliningEnabled() {
        return true;
    }

    public static boolean isIMAPPushEnabled() {
        /**
         * Enable the IMAP Push feature for every device since KK.
         */
        return true;
    }

    /**
     * IMAP Smart Sync feature (CONDSTORE/MODSEQ)
     *
     * @return
     */
    public static boolean isIMAPSmartSyncEnabled() {
        return true;
    }

    /**
     * APOP support for POP accounts, RFC 1939
     *
     * @return
     */
    public static boolean isPOPSecureLoginEnabled() {
        return true;
    }

    public static boolean firstCharIsRTL(String str) {
        if (str == null) {
            return false;
        }
        if (str.length() == 0) {
            return false;
        }
        int testChar = str.codePointAt(0);
        // if condition check for arabic
        if ((testChar >= 0x0621 && testChar <= 0x065F)
                || (testChar >= 0x066E && testChar <= 0x06D3)
                || (testChar >= 0xFB50 && testChar <= 0xFDFF)
                || (testChar >= 0xFE70 && testChar <= 0xFEFC)
                || (testChar == 0x061B || testChar == 0x061F)) {
            return true;// Arabic Unicode Character Range
        } else if ((testChar >= 0x0590 && testChar <= 0x05FF)) {
            return true;// Hebrew Unicode Character Range
        }
        return false;
    }

    // for SDL
    // public static boolean isMagazineEnabed(Context context) {
    // return
    // (SecProductFeature_LAUNCHER.SEC_PRODUCT_FEATURE_LAUNCHER_ENABLE_MAGAZINE_LINK
    // && !Utility
    // .isInContainer(context));
    // }

    public static boolean isSupportHelpMenuAtEasyMode() {
        if (CarrierValues.IS_DEVICE_H || "goldenlte_usa_vzw".equals(BUILD_CARRIER)
                || "serranolte_usa_vzw".equals(BUILD_CARRIER))
            return true;
        else
            return false;
    }

    /**
     * "DIGEST-MD5" based authentication for Legacy accounts. RFC 2831
     *
     * @return
     */
    public static boolean isMD5AuthenticationEnabled() {
        return true;
    }

    /**
     * IMAP Smart Forward feature (GENURLAUTH/BURL) RFC 4467 and RFC 4468
     *
     * @return
     */
    public static boolean isIMAPSmartForwardEnabled() {
        return true;
    }

    /**
     * IMAP QRESYNC feature (QRESYNC) RFC 5162
     *
     * @return
     */
    public static boolean isIMAPQresyncEnabled() {
        return true;
    }

    private static final String CONTEXTUAL_CONTENTS_EMAIL_ENABLED = "contextual_contents_email_enabled";
    private static final String CONTEXTUAL_CONTENTS_ENABLED = "contextual_contents_enabled";

    public static boolean IsContextualSearh(Context context) {
        // get Contextual Search setting value
        if (context == null) {
            return false;
        }
        // get Contextual Search setting value
        int IsContextualEmailSetting = Settings.System.getInt(context.getContentResolver(),
                CONTEXTUAL_CONTENTS_EMAIL_ENABLED, 0);
        int IsContextaulDeviceSetting = Settings.System.getInt(context.getContentResolver(),
                CONTEXTUAL_CONTENTS_ENABLED, 0);
        if ((IsContextaulDeviceSetting == 1) && (IsContextualEmailSetting == 1)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * As per Oct 2013 Email Verizon requirement, new feature is added to
     * download attachments via Wi-Fi only
     *
     * @return true if Download via Wifi is supported otherwise flase
     */
    public static boolean isDownloadOnlyViaWifiEnabled() {
        if (CarrierValues.IS_CARRIER_VZW) {
            return true;
        }
        return false;
    }

    /**
     * As per Oct 2013 Email Verizon requirement, new feature is added not to
     * show the delete confirmation dialog when user set the option in settings.
     * Supported only for K and future devices
     *
     * @return
     */
    public static boolean isEmailDeleteConfirmFeatureEnabled() {
        if (CarrierValues.IS_CARRIER_NA || CarrierValues.IS_CARRIER_JPN) {
            return true;
        }
        return false;
    }

    /**
     * Portal Branding feature. This feature shows account provider icon as
     * notification icon when new mail is received.
     *
     * @return
     */
    public static boolean isPortalBrandingEnabled() {
        return false;
    }

    /**
     * new feature is added not to show the menu dialog at view when user click
     * the text.
     *
     * @return
     */

    public static boolean isPatternMatchingDialogEnabled() {
        if (CarrierValues.IS_CARRIER_NA) {
            return false;
        }
        return false;
    }

    public static boolean isChinaModel() {
        if (CarrierValues.IS_CARRIER_CHINA) {
            return true;
        }
        return false;
    }

    /**
     * Legacy Auto Retry feature This feature attempts to retry sending Legacy
     * email stuck in Outbox due to Temporary server error.
     *
     * @return
     */
    public static boolean isLegacyAutoRetryEnabled() {
        return true;
    }

    public static boolean isEnableEmergencyMode(Context context) {
        if (context == null) {
            EmailLog.d(TAG, "context is null in isEnableEmergencyMode");
            return false;
        }
        boolean isEmergencyOrUPSM = EmergencyManager.isEmergencyMode(context);
        return isEmergencyOrUPSM;
    }

    public static boolean isEnableEmptyViewPopup() {
        return SecFeatureWrapper.isNACarrier();
    }

    /**
     * For Verizon, two kinds of message sync settings are implemented 1.
     * synchronize the messages based on days such as 1 week, 2 weeks, 1 month
     * or 1 month or 100 mails this is primarily implemented to satisfy the sync
     * and connect requirements. So, all the devices with Sync&Connect are
     * implemented this way. NOTE: P4 and SCamera still has this settings,
     * though S&C is disabled, to maintain consistency. so, the visible limit of
     * the mailbox should never be altered just like that. 2. synchronize the
     * messages based on count such as 25, 50, and so on. here, the visible
     * limit is controlled based on numbers and not based on days. so, even if
     * the user has only 25 in the last one year, it still shows all 25
     * messages. this is the global concept of Samsung. NOTE: From KitKat
     * onwards days based feature has been enabled for all devices, some of the
     * IMAP server does not support this concept, hence for those IMAP servers
     * still we perform IMAP Count based sync. For POP accounts we still perform
     * count based sync.
     *
     * @return
     */
    public static boolean isRecentMessageSettingsEnabled(Context context,
                                                         EmailContent.Account account) {
        // POP account does not support days based sync

        if (AccountCache.isPop3(context, account.mId)) {
            return true;
        }

        /**
         * Some IMAP server does not support days based sync feature based on
         * server support it will be decided. mCalendarSyncLookback -> field is
         * used to store this feature status for Legacy accounts
         */
        if (account.getImapDaysBasedSync() == EmailContent.Account.IMAP_COUNT_BASED_SYNC) {
            return true;
        }
        return false;
    }

    /**
     * isIMAPPushSupported - Check IMAP push is supported by checking the
     * CAPABILITIES response. Always first preference will be given to IMAP
     * store variable, since support can be dynamically added or removed. If the
     * store value is null, depend on Email DB value.
     *
     * @param account
     * @return
     */
    public static boolean isIMAPPushSupported(Context context, EmailContent.Account account) {
        EmailLog.e(TAG, "isIMAPPushSupported");
        if (!EmailFeature.isIMAPPushEnabled()) {
            EmailLog.e(TAG, "IMAP Idle support feature is not enabled");
            return false;
        }

        return isIdleSupported(context, account);
    }

    public static boolean isIdleSupported(Context context, EmailContent.Account account) {
        EmailLog.e(TAG, "isIdleSupported");
        if (!EmailFeature.isIMAPPushEnabled()) {
            EmailLog.e(TAG, "IMAP Idle support feature is not enabled");
            return false;
        }
        return (isIMAPCapabilitySupported(context, account, ImapConstants.IDLE));
    }

    public static boolean isIMAPMoveSupported(Context context, EmailContent.Account account) {
        EmailLog.e(TAG, "isIMAPMoveSupported");
        return (isIMAPCapabilitySupported(context, account, ImapConstants.MOVE));
    }

    public static boolean isIMAPQresyncSupported(Context context, EmailContent.Account account) {
        EmailLog.e(TAG, "isIMAPQresyncSupported");
        if (!EmailFeature.isIMAPQresyncEnabled()) {
            EmailLog.e(TAG, "IMAP QRESYNC support feature is not enabled");
            return false;
        }
        return (isIMAPCapabilitySupported(context, account, ImapConstants.QRESYNC));
    }

    /**
     * isIMAPCapabilitySupported - Check given capability is supported by
     * checking the CAPABILITIES response. Always first preference will be given
     * to IMAP store variable, since support can be dynamically added or
     * removed. If the store value is null, depend on Email DB value.
     *
     * @param account
     * @return
     */
    private static boolean isIMAPCapabilitySupported(Context context, EmailContent.Account account,
                                                     String capability) {
        EmailLog.e(TAG, "isIMAPCapabilitySupported");
        if (account == null) {
            EmailLog.e(TAG, "Account is null");
            return false;
        }
        // Store remoteStore = null;
        String serverCap = "";
        HostAuth hostauth = account.mHostAuthRecv;
        if (hostauth == null) {
            hostauth = HostAuth.restoreHostAuthWithId(context, account.mHostAuthKeyRecv);
        }
        if (hostauth == null) {
            EmailLog.e(TAG, "No host auth to check protocol and capabilities");
            return false;
        }
        String protocol = hostauth.mProtocol;
        if (!CommonDefs.STORE_SCHEME_IMAP.equals(protocol)) {
            EmailLog.e(TAG, "Non IMAP account");
            return false;
        }
        // try {
        // New_email Context context = Email.getEmailContext();
        // remoteStore = Store.getInstance(account.getStoreUri(context),
        // context, null);
        // serverCap = remoteStore.getCapabilities();
        // if (serverCap == null) {
        serverCap = hostauth.mCapabilities;
        // }
        // }
        // catch (MessagingException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        if (serverCap == null) {
            EmailLog.e(TAG, "Capabilities are null");
            return false;
        }

        HashSet<String> capSet = new HashSet<String>();
        parseCapabilities(capSet, serverCap);
        return capSet.contains(capability);
    }

    /**
     * isSMTPCapabilitySupported - Check given capability is supported by
     * checking the EHLO response. Always first preference will be given to SMTP
     * sender variable, since support can be dynamically added or removed. If
     * the sender value is null, depend on Email DB value.
     *
     * @param account
     * @return
     */

    private static boolean isSMTPCapabilitySupported(Context context, EmailContent.Account account,
                                                     String capability) {
        EmailLog.e(TAG, "isSMTPCapabilitySupported");
        if (account == null) {
            EmailLog.e(TAG, "Account is null");
            return false;
        }
        // Sender smtpSender = null;
        String serverCap = "";
        HostAuth hostauth = account.mHostAuthSend;
        if (hostauth == null) {
            hostauth = HostAuth.restoreHostAuthWithId(context, account.mHostAuthKeySend);
        }
        if (hostauth == null) {
            EmailLog.e(TAG, "No host auth to check protocol and capabilities");
            return false;
        }
        // try {
        // Context context = Email.getEmailContext();
        // smtpSender = Sender.getInstance(context,
        // account.getSenderUri(context));
        // serverCap = smtpSender.getSenderCapabilities();
        // if (serverCap == null) {
        serverCap = hostauth.mCapabilities;
        // }
        // } catch (MessagingException e) {
        // e.printStackTrace();
        // }
        if (serverCap == null) {
            EmailLog.e(TAG, "Capabilities are null");
            return false;
        }
        return serverCap.contains(capability);
    }

    public static boolean isIMAPSmartForwardSupported(Context context, EmailContent.Account account) {
        if (!EmailFeature.isIMAPSmartForwardEnabled()) {
            EmailLog.e(TAG, "IMAP Smart Forward feature is not enabled");
            return false;
        }
        /**
         * Check for both IMAP and SMTP server capabilities for IMAP Lemonade
         * Smart Forward support
         */
        return (isIMAPCapabilitySupported(context, account, ImapConstants.URLAUTH) && isSMTPCapabilitySupported(
                context, account, ImapConstants.BURL));
    }

    public static void setIMAPForwardedFlagVerified(Context context, EmailContent.Account account) {
        EmailLog.d(TAG, "setIMAPForwardedFlagVerified");
        if (!AccountCache.isImap(context, account.mId)) {
            EmailLog.e(TAG, "IMAP Forwarded flag is supported only for IMAP");
            return;
        }
        account.mFlags |= Account.FLAGS_IMAP_FORWARD_FLAG_VERIFIED;
        account.mFlags &= ~Account.FLAGS_IMAP_FORWARD_FLAG_INIT;
        account.mFlags &= ~Account.FLAGS_IMAP_FORWARD_FLAG_ENABLED;
        account.mFlags &= ~Account.FLAGS_IMAP_FORWARD_FLAG_DISABLED;
        ContentValues cv = new ContentValues();
        cv.put(AccountColumns.FLAGS, account.mFlags);
        Uri uri = ContentUris.withAppendedId(EmailContent.Account.CONTENT_URI, account.mId);
        context.getContentResolver().update(uri, cv, null, null);
    }

    public static void disableIMAPForwardedFlagFeature(Context context, EmailContent.Account account) {
        EmailLog.d(TAG, "disableIMAPForwardedFlagFeature");
        if (!AccountCache.isImap(context, account.mId)) {
            EmailLog.e(TAG, "IMAP Forwarded flag is supported only for IMAP");
            return;
        }
        account.mFlags &= ~Account.FLAGS_IMAP_FORWARD_FLAG_INIT;
        account.mFlags &= ~Account.FLAGS_IMAP_FORWARD_FLAG_ENABLED;
        account.mFlags &= ~Account.FLAGS_IMAP_FORWARD_FLAG_VERIFIED;
        account.mFlags |= Account.FLAGS_IMAP_FORWARD_FLAG_DISABLED;
        ContentValues cv = new ContentValues();
        cv.put(AccountColumns.FLAGS, account.mFlags);
        Uri uri = ContentUris.withAppendedId(EmailContent.Account.CONTENT_URI, account.mId);
        context.getContentResolver().update(uri, cv, null, null);
    }

    public static boolean isIMAPForwadedFlagVerificationDone(EmailContent.Account account) {
        return ((account.mFlags & Account.FLAGS_IMAP_FORWARD_FLAG_VERIFIED) != 0 || (account.mFlags & Account.FLAGS_IMAP_FORWARD_FLAG_DISABLED) != 0);
    }

    public static boolean isIMAPForwadedFlagSupported(Context context, EmailContent.Account account) {
        EmailLog.d(TAG, "isIMAPForwadedFlagSupported");
        if (!AccountCache.isImap(context, account.mId)) {
            EmailLog.e(TAG, "IMAP Forwarded flag is supported only for IMAP");
            return false;
        }
        return ((account.mFlags & Account.FLAGS_IMAP_FORWARD_FLAG_ENABLED) != 0 || (account.mFlags & Account.FLAGS_IMAP_FORWARD_FLAG_VERIFIED) != 0);
    }

    public static void parseCapabilities(HashSet<String> capSet, String capabilities) {
        try {
            if (capabilities.contains("[") && capabilities.contains("]")) {
                capabilities = capabilities.substring(capabilities.indexOf("[") + 1,
                        (capabilities.indexOf("]")));
            }
        } catch (Exception e) {
            EmailLog.e(Logging.LOG_TAG, "parseCapabilities - Exception while parsing");
        }
        String[] resultList = capabilities.split(",");
        if (resultList == null) {
            EmailLog.e(Logging.LOG_TAG, "IMAP server capabilities is null/empty");
            return;
        }
        try {
            for (int i = 0; i < resultList.length; i++) {
                capSet.add(resultList[i]);
            }
        } catch (Exception e) {
            EmailLog.e(Logging.LOG_TAG, "IMAP Capability parsing failed", e.getMessage());
        }
        return;
    }

    public static boolean isGoogleOAuthAccountSetupEnabled() {
        return true;
    }

    public static boolean isGoogleOAuth2Enabled() {
        return !Utility.isMainlandChinaModel();
    }

    /**
     * is8bitMIMEFeatureEnabled - RFC 6152 Enable 8BitMIME support.
     *
     * @return
     */
    public static boolean is8bitMIMEFeatureEnabled() {
        return false;
    }

    /**
     * IMAP Drafts Sync support - rfc4469 CATENATE
     *
     * @return
     */
    public static boolean isIMAPDraftSyncEnabled() {
        return true;
    }

    public static boolean isTutorialHelperDownloadable() {
        return "downloadable".equals(FloatingFeature.getInstance().getString(
                "SEC_FLOATING_FEATURE_HELP_HUB_APK_TYPE", "preload"));
    }

    /**
     * IMAP UTF8ACCEPT Support - RFC6855 (VZW requirement)
     *
     * @return
     */
    public static boolean isIMAPUTF8Enabled() {
        return false;
    }

    public static boolean isSupportEdgeStripe(Context context) {

        try {
            boolean enable = false;
            PackageManager pm = context.getPackageManager();
            enable = pm.hasSystemFeature(PEOPLE_EDGE_NOTIFICATION);

            if (enable)
                return enable;

            int featureLevel = FloatingFeature.getInstance().getInteger(EDGE_STRIPE_FEATURE_LEVEL);

            // Not support if featureLevel is -1
            if (featureLevel == -1) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean useForceRefresh() {
        if (CarrierValues.IS_CARRIER_VZW) {
            return false;
        }
        return true;
    }

    // public static boolean isSupportSortByMenu() {
    // if (CarrierValues.IS_CARRIER_NA)
    // return true;
    //
    // return false;
    // }

    public static boolean blockGalSearch() {
        if (CarrierValues.IS_CARRIER_VZW) {
            return true;
        }
        return false;
    }

    public static boolean isSupportTopLineInfoEnabled() {
        if (CarrierValues.IS_CARRIER_ATT) {
            return true;
        }
        return false;
    }

    public static boolean isSupportHideTickBoxes() {
        if (CarrierValues.IS_CARRIER_ATT) {
            return true;
        }
        return false;
    }

    public static boolean doRefreshForStaleMailbox() {
        if (showRefreshOnOpenSetting()) {
            return false;
        }
        if (CarrierValues.IS_CARRIER_VZW) {
            return true;
        }
        return false;
    }

    /**
     * IMAP Compress Extension support - rfc4978 Feature enabled only for Noble
     * device
     *
     * @return
     */
    public static boolean isIMAPCompressEnabled() {
        return false;// IsUseAdvancedUIFromNote5();
    }

    /**
     * isIMAPCompressSupported - Check IMAP Compress is supported by checking
     * the CAPABILITIES response.
     *
     * @param account
     * @return
     */
    public static boolean isIMAPCompressSupported(Context context, EmailContent.Account account) {
        EmailLog.d(TAG, "isIMAPCompressSupported");
        if (!EmailFeature.isIMAPCompressEnabled()) {
            EmailLog.e(TAG, "IMAP Compress support feature is not enabled");
            return false;
        }
        return (isIMAPCapabilitySupported(context, account, ImapConstants.COMPRESS_DEFALTE));
    }

    public static boolean isSupportPen(Context context) {
        if (context != null
                && Settings.System.getInt(context.getContentResolver(),
                Settings.System.PEN_HOVERING, 0) == 1)
            return true;
        return false;
    }

    public static boolean isSupportPenHover(Context context) {
        if (context != null
                && Settings.System.getInt(context.getContentResolver(),
                Settings.System.PEN_HOVERING, 0) == 1
                && Settings.System.getInt(context.getContentResolver(),
                Settings.System.PEN_HOVERING_INFORMATION_PREVIEW, 0) == 1)
            return true;
        return false;
    }

    public static boolean isIMAPMultiSelect() {
        return IsUseAdvancedUIFromNote5();
    }

    public static boolean useMimeForEas() {
        return false;
    }

    public static boolean isShowButtonBackground(Context context) {
        return (Settings.System.getInt(context.getContentResolver(), "show_button_background", 0) > 0);
    }

    public static boolean isSupportNotesSync(Context context) {
        // Notes Sync support only in KNOX Mode.
        if (Utility.isKnoxMode(context)) {
            try {
                PackageManager pm = context.getPackageManager();
                pm.getPackageInfo("com.sec.knox.knoxlauncher", PackageManager.GET_ACTIVITIES);
                EmailLog.e(TAG, "isNotesSyncFeatureEnabled - True");
                return true;
            } catch (PackageManager.NameNotFoundException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * As per Feb 2015 Email Verizon requirement, new feature is added to
     * provide user with settings to enable/disable refresh mailbox when opened.
     * Supported from Noble and future devices TODO:: Remove IS_DEVICE_NOBLE
     * condition when Tab S2 is branched out
     */
    public static boolean showRefreshOnOpenSetting() {
        if (CarrierValues.IS_CARRIER_VZW && IsUseAdvancedUIFromNote5()) {
            return true;
        }
        return false;
    }

    public static boolean isUsedLoadMoreAtBottom() {
        return false;
    }

    /**
     * As per Email Verizon requirement, provision should be given to store the
     * email attachments in the external SD card storage. It should have two settings
     * <p/>
     * 1. Phone
     * 2. SD card
     * <p/>
     * By default it should be phone storage. Below method controls this verizon
     * requirement for SD card supported device
     */
    public static boolean showExternalSDCardStorageSetting(Context context) {
        if (Utility.isKnoxMode(context)) {
            EmailLog.e(TAG, "In KNOX mode storing attachment in external SD card is not supported");
            return false;
        }
        if (CarrierValues.IS_CARRIER_VZW) {
            return true;
        }
        return false;
    }

    /**
     * This method controls the SD card storage feature based on
     * <p/>
     * 1. Availability of the feature
     * 2. Availability of the SD Card
     * 3. Based on Exchange/MDM policy
     */
    public static boolean externalSDCardStorageSettingEnabled(Context context) {
        if (false == showExternalSDCardStorageSetting(context)) {
            EmailLog.e(TAG, "External SDCard feature is not enabled");
            return false;
        }
        if (false == Utility.isExternSDCardMounted(context)) {
            EmailLog.e(TAG, "External SDCard is not mounted");
            return false;
        }
        if (false == DPMWraper.getInstance(context).getAllowStorageCard(null)) {
            EmailLog.e(TAG, "Exchange policy not allowed to store in external SDCard");
            return false;
        }
        return true;
    }

    /**
     * Whether the untrusted certificate feature is enabled or not
     *
     * @param context
     * @return
     */
    public static boolean isUntrustedCertificateFeatureEnabled(Context context) {
        Preferences preferences = Preferences.getPreferences(context);
        boolean result = preferences.getEnableUntrustedCertificateFeature();

        EmailLog.d(TAG, "Untrusted certificate feature is enabled? " + result);
        return result;
    }
}
