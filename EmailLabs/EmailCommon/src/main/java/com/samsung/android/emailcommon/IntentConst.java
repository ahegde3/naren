
package com.samsung.android.emailcommon;

public interface IntentConst {
    public static final String EXTRA_ACCOUNT_ID = "ACCOUNT_ID";
    public static final String EXTRA_MAILBOX_ID = "MAILBOX_ID";
    public static final String EXTRA_MESSAGE_ID = "MESSAGE_ID";
    public static final String EXTRA_DECRYPTED_MESSAGE_ID = "DECRYPTED_MESSAGE_ID";
    public static final String EXTRA_THREAD_ID = "THREAD_ID";
    public static final String EXTRA_REMINDER_ID = "REMINDER_ID";
    public static final String EXTRA_PHONE_NUMBER = "PHONE_NUMBER";
    public static final String EXTRA_TIMESTAMP = "TIMESTAMP";
    public static final String EXTRA_OPEN_BY_SELF = "OPEN_BY_SELF";
    public static final String EXTRA_OPEN_VIEW = "OPEN_VIEW";
    public static final String EXTRA_DEVICE_POLICY_ADMIN = "message_code";
    public static final String EXTRA_FAB_CENTER_X = "FAB_CENTER_X";
    public static final String EXTRA_FAB_CENTER_Y = "FAB_CENTER_Y";
    public static final String EXTRA_LAUNCHED_FIRST_TIME = "LAUNCHED_FIRST_TIME";
    public static final String EXTRA_LAUNCHED_VIEW_FIRST_TIME = "LAUNCHED_VIEW_FIRST_TIME";
    public static final String EXTRA_CURRENT_SCREEN = "CURRENT_SCREEN";
    public static final String EXTRA_CLIP_DATA = "CLIP_DATA";
    public static final String EXTRA_ACCOUNT_MAIL_SERVICE = "com.samsung.android.email.intent.extra.ACCOUNT";
    public static final String EXTRA_NOTI_ID = "NOTIFICATION_ID";

    public static final String ACTION_FROM_WIDGET = "action_from_widget";
    public static final String ACTION_FROM_LAUNCHER = "action_from_launcher";
    public static final String ACTION_VIEW_NEWTASK = "action_view_newtask";
    public static final String ACTION_BROADCAST = "broadcast_receiver";
    public static final String ACTION_DEVICE_POLICY_ADMIN = "com.samsung.android.email.ui.devicepolicy";
    public static final String INTENT_ATTACHMENTDOWNLOADSERVICE_SUCESS_TOAST = "com.samsung.android.email.ui.intent.attachmentdownloadservice.sucess";
    public static final String INTENT_ATTACHMENTDOWNLOADSERVICE_FAIL_TOAST = "com.samsung.android.email.ui.intent.attachmentdownloadservice.fail";
    public static final String INTENT_DOWNLOAD_MANAGER_FAIL_TOAST = "com.samsung.android.email.ui.intent.downloadmanager.fail";
    public static final String EXTRA_FILE_PATH = "com.samsung.android.email.ui.intent.extra.FILE_PATH";
    public static final String EXTRA_FILE_NAME = "com.samsung.android.email.ui.intent.extra.FILE_NAME";
    public static final String ACTION_LIST_REFRESH = "com.samsung.android.email.ui.intent.list.refresh";

    public static final String AUTO_DISCOVER_BUNDLE_ERROR_CODE = "autodiscover_error_code";
    public static final String VALIDATE_BUNDLE_RESULT_CODE = "validate_result_code";
    public static final String VALIDATE_BUNDLE_POLICY_SET = "validate_policy_set";
    public static final String VALIDATE_BUNDLE_ERROR_MESSAGE = "validate_error_message";
    public static final String VALIDATE_BUNDLE_PROTOCOL_VERSION = "validate_protocol_version";
    public static final String VALIDATE_BUNDLE_LEGACY_CAPABILITIES = "legacy_capabilities";

    public static final String ACTION_BACKUP_RESTORE_REQ_EMAIL = "com.sec.android.email.service.BROADCAST_DETECT";
    public static final String ACTION_BACKUP_TO_EMAIL_LOCATION = "com.samsung.android.email.intent.action.BACKUP_TO_EMAIL_LOCATION";
    public static final String ACTION_RESTORE_TO_EMAIL_DB = "com.samsung.android.email.intent.action.RESTORE_TO_EMAIL_DB";
//    public static final String BACKUP_OR_RESTORE = "backup_Or_Restore";
    public static final String BACKUP_TO_EMAIL_LOCATION_FINISH = "com.samsung.android.email.service.BACKUP_TO_EMAIL_LOCATION_FINISH";
    public static final String BACKUP_PATH = "backup_Path";

    // Zero Kies Backup & Restore start
    public static final String ACTION_REQUEST_BACKUP_EMAIL = "com.samsung.android.intent.action.REQUEST_BACKUP_EMAIL";
    public static final String ACTION_RESPONSE_BACKUP_EMAIL = "com.samsung.android.intent.action.RESPONSE_BACKUP_EMAIL";
    public static final String ACTION_REQUEST_RESTORE_EMAIL = "com.samsung.android.intent.action.REQUEST_RESTORE_EMAIL";
    public static final String ACTION_RESPONSE_RESTORE_EMAIL = "com.samsung.android.intent.action.RESPONSE_RESTORE_EMAIL";
    public static final String SAVE_PATH = "SAVE_PATH";
    public static final String SESSION_KEY = "SESSION_KEY";
    public static final String SOURCE = "SOURCE";
    // Zero Kies Backup & Restore end

    // Hero SmartSwitch
    public static final String EXPORT_SESSION_TIME = "EXPORT_SESSION_TIME";
    public static final String SECURITY_LEVEL = "SECURITY_LEVEL";
    
    // Zero sCloud BNR start
    public static final String ACTION_BACKUP_TO_EMAIL_LOCATION_SCLOUD = "com.samsung.android.email.intent.action.BACKUP_TO_EMAIL_LOCATION_SCLOUD";
    public static final String BACKUP_TO_EMAIL_LOCATION_FINISH_SCLOUD = "com.samsung.android.email.service.BACKUP_TO_EMAIL_LOCATION_FINISH_SCLOUD";
    public static final String TEST_START_BACKUP_FOR_SCLOUD = "test.start.backup.for.scloud";
    public static final String ACTION_RESTORE_TO_EMAIL_DB_SCLOUD = "com.samsung.android.email.intent.action.RESTORE_TO_EMAIL_DB_SCLOUD";
    // Zero sCloud BNR end

    public static final String LOG_VIEW_ACTION = "com.samsung.android.email.ui.LogProvider";
    public static final String CARD_VIEW_ACTION = "com.samsung.android.sdk.personalassistant";

    public static final String EXCHANGE_INTENT = "com.samsung.android.emailsync.EXCHANGE_INTENT";
    public static final String IMAP_INTENT = "com.samsung.android.emailsync.IMAP_INTENT";
    public static final String POP3_INTENT = "com.samsung.android.emailsync.POP_INTENT";
    public static final String ACCOUNT_INTENT = "com.samsung.android.email.ui.ACCOUNT_INTENT";
    public static final String DECRYPT_INTENT = "com.samsung.android.emailsync.DECRYPT_INTENT";
    public static final String PGP_KEY_INTENT = "com.samsung.android.emailsync.PGP_KEY_INTENT";
    public static final String CERTIFICATE_INTENT = "com.samsung.android.emailsync.CERTIFICATE_INTENT";

    public static final String HELLO = "com.samsung.android.email.HELLO";

    public static final String ACTION_CLEAR_NOTIFICATION = "com.samsung.android.email.intent.action.CLEAR_NOTIFICATION";
    public static final String EXTRA_IS_LAUNCHED = "isLaunched";

    public static final String ACTION_BOOT_COMPLETED = "com.samsung.android.email.intent.action.BOOT_COMPLETED";

    public static final String ACTION_BOOT_COMPLETED_EMAIL_UI = "com.samsung.android.email.intent.action.BOOT_COMPLETED_EMAIL_UI";

	public static final String ACTION_LOCALE_CHANGED = "android.intent.action.LOCALE_CHANGED";

    public static final String ACTION_DEVICE_STORAGE_LOW = "com.samsung.android.email.intent.action.DEVICE_STORAGE_LOW";

    public static final String ACTION_DEVICE_STORAGE_OK = "com.samsung.android.email.intent.action.DEVICE_STORAGE_OK";

	public static final String ACTION_LOGIN_ACCOUNTS_CHANGED = "com.samsung.android.email.intent.action.LOGIN_ACCOUNTS_CHANGED";

	public static final String ACTION_REPLY_BACKGROUND = "com.samsung.android.email.intent.action.REPLY_BACKGROUND";
	
	public static final String ACTION_SEND_BACKGROUND = "com.samsung.android.email.intent.action.SEND_BACKGROUND";

    public static final String ACTION_REPLY_BACKGROUND_SENT = "com.samsung.android.email.intent.action.REPLY_BACKGROUND_SENT";
    
    public static final String ACTION_SEND_BACKGROUND_SENT = "com.samsung.android.email.intent.action.SEND_BACKGROUND_SENT";
    
    public static final String ACTION_REPLY_BACKGROUND_SENT_INTERNAL = "com.samsung.android.email.intent.action.REPLY_BACKGROUND_SENT_INTERNAL";
    
    public static final String ACTION_SEND_BACKGROUND_SENT_INTERNAL = "com.samsung.android.email.intent.action.SEND_BACKGROUND_SENT_INTERNAL";

	public static final String ACTION_QUICK_REPLY_BACKGROURND = "com.samsung.android.email.intent.action.QUICK_REPLY_BACKGROUND";

	public static final String ACTION_REPLY_FROM_OTHER_DEVICE = "com.samsung.android.email.intent.action.REPLY_FROM_OTHER_DEVICE";

	public static final String ACTION_COMPOSE_FROM_OTHER_DEVICE = "com.samsung.android.email.intent.action.COMPOSE_FROM_OTHER_DEVICE";

	public static final String ACTION_CHANGE_LOGGING = "com.samsung.android.email.intent.action.CHANGE_LOGGING";
	
	public static final String ACTION_THEME_CHANGE = "com.samsung.android.email.intent.action.THEME_APPLY";
	
	public static final String THEME_CENTER_CHANGE = "com.samsung.android.theme.themecenter.THEME_APPLY";
	
    public static final String PEOPLE_STRIPE_ON_START = "com.samsung.android.service.peoplestripe.ON_START";
    
    public static final String ACTION_PEOPLE_STRIPE_ON_START = "com.samsung.android.email.intent.action.peoplestripe.ON_START";
    
    public static final String EXTRA_RESCHEDULE = "com.samsung.android.email.intent.extra.RESCHEDULE";

    public static final String EXTRA_DEBUG_BITS = "com.samsung.android.email.intent.extra.DEBUG_BITS";

    public static final String ACTION_VIEWMODE_CHANGED = "com.samsung.android.email.intent.action.VIEWMODE_CHANGED";
    public static final String EXTRA_VIEWMODE = "view_mode";
    
    public static final String ACTION_TOPLINEINFO_CHANGED = "com.samsung.android.email.intent.action.TOPLINEINFO_CHANGED";
    public static final String EXTRA_TOPLINEINFO = "topline_info";

    public static final String ACTION_CLEAR_AND_SAVEDRAFT_NOTIFICATION = "com.samsung.android.email.intent.action.CLEAR_AND_SAVEDRAFT_NOTIFICATION";
    public static final String EXTRA_PASSWORD_EXPIRED = "com.samsung.android.email.ui.account_password_expired";
    public static final String EXTRA_EMAIL_ADDRESS = "com.samsung.android.email.ui.email_address";
    public static final String ACTION_CLEAR_FAILNOTIFICATION = "com.samsung.android.email.intent.action.CLEAR_FAILNOTIFICATION";
   // public static final String ACTION_SEND_RESERVATION_EMAIL = "com.samsung.android.email.Intent.ACTION_SEND_RESERVATION_EMAIL";
    public static final String ACTION_ETC = "com.samsung.android.email.intent.action.ACTION_ETC";

    public static final String EXTRA_RETURN = "com.samsung.android.email.ui.extra.return";
    public static final String EXTRA_MESSAGELIST = "com.samsung.android.email.ui.extra.messagelist";

    public static final String COMPOSE_EXTRA_REPLYALL = "ReplyAll";
    public static final String COMPOSE_EXTRA_FORWARD_RECIPIENTS = "AddRecipients";
    public static final String COMPOSE_EXTRA_ADD_ATTACHMENTS = "attachment";
    public static final String COMPOSE_EXTRA_MEETING = "Meeting";

    public static final String ACTION_REPLY = "com.samsung.android.email.composer.intent.action.REPLY";
    public static final String ACTION_REPLY_SMS = "com.samsung.android.email.composer.intent.action.REPLY_SMS";
    public static final String ACTION_REPLY_ALL = "com.samsung.android.email.composer.intent.action.REPLY_ALL";
    public static final String ACTION_FORWARD = "com.samsung.android.email.composer.intent.action.FORWARD";
    public static final String ACTION_QUICK_REPLY = "com.samsung.android.email.composer.intent.action.QUICK_REPLY";
    public static final String ACTION_EDIT_DRAFT = "com.samsung.android.email.composer.intent.action.EDIT_DRAFT";
    public static final String ACTION_EDIT_TEMP = "com.samsung.android.email.composer.intent.action.EDIT_TEMP";

    public static final String ACTION_MEETING_RESPONSE = "com.samsung.android.email.intent.action.MEETING_RESPONSE";
    public static final String ACTION_CALENDAR_MEETING_RESPONSE = "com.samsung.android.email.intent.action.CALENDAR_MEETING_RESPONSE";
    public static final String ACTION_PROPOSE_NEW_TIME = "com.samsung.android.email.intent.action.PROPOSE_NEW_TIME";
    public static final String ACTION_CALENDAR_PROPOSE_NEW_TIME = "com.samsung.android.email.intent.action.CALENDAR_PROPOSE_NEW_TIME";
    public static final String ACTION_CALENDAR_MEETING_FORWARD = "com.samsung.android.email.intent.action.CALENDAR_MEETING_FORWARD";

    public static final String ACTION_CALENDAR_MEETING_FORWARD_BK = "com.samsung.android.email.intent.action.CALENDAR_MEETING_FORWARD";
    public static final String ACTION_CALENDAR_MEETING_RESPONSE_BK = "com.samsung.android.email.intent.action.CALENDAR_MEETING_RESPONSE";

    public static final String ACTION_EMAIL_DOC = "com.samsung.android.email.composer.intent.action.EMAIL_DOC";
    public static final String ACTION_SEND_PENDING_MAIL = "com.samsung.android.email.intent.action.MAIL_SERVICE_SEND_PENDING";
	//for knox launcher
    public static final String ACTION_VIEW_FROM_KNOX_LAUNCHER = "com.samsung.android.email.ui.ACTION_VIEW_FROM_KNOX_LAUNCHER";

    // change@SISO for Edit Response options start
    public static final String EXTRA_MEETING_RESPONSE = "meeting_response";
    // change@SISO for Edit Response options end

    public static final String EXTRA_ADDITIONAL_MESSAGE_FACTOR = "com.samsung.android.email.MESSAGE_FACTOR";

    public static final String ACTION_START_SYNC = "com.samsung.android.email.START_SYNC";
    public static final String ACTION_START_DOWNLOAD_ATTACHMENT = "com.samsung.android.email.START_DOWNLOAD_ATTACHMENT";

    public static final String ACTION_COMMIT_SETTINGS = "com.samsung.android.email.intent.action.COMMIT_SETTINGS";
    public static final String ACTION_DELETE_ACCOUNT = "com.samsung.android.email.intent.action.DELETE_ACCOUNT";

    public static final String PREFERENCE_HTML_SIGNATURE = "account_html_signature";
    public static final String SETTING_SIGNATURE_IS_ON = "account_html_signature_is_on";

    public static final String HTML_SIGNATURE_PARTITION = "HTML_SIGNATURE";

    public static final String ACTION_RESTART_ON_RESUME = "com.samsung.android.email.intent.action.RESTART_ON_RESUME";
    public static final String EXTRA_VALUE = "com.samsung.android.email.intent.extra.VALUE";

    /**
     * If the intent is sent from the email app itself, it should have this
     * boolean extra.
     */
    public static final String EXTRA_FROM_WITHIN_APP = "from_within_app";
    public static final String EXTRA_FROM_WITHIN_SETUP_NO_ACCOUNT = "from_setup_no_account";

    public static final String EXTRA_EVENT_ID = "event_id";
    public static final String EXTRA_ACCOUNT_EMAIL = "account_email";
    // public static final String EXTRA_MESSAGE_ID = "message_id";
    public static final String EXTRA_EACH_SETTINGS_ACCOUNT_ID = "each_settings_ACCOUNT_ID";
    public static final String EXTRA_EACH_SETTINGS_ADDRESS = "each_settings_ADDRESS";

    public static final String CALLER_ID = "CALLER_ID";
    public static final int CALLER_ID_LIST = 0;
    public static final int CALLER_ID_VIEW = 1;

    public static String ACTION_RESCHEDULE = "com.samsung.android.email.intent.action.MAIL_SERVICE_RESCHEDULE";

    public static String ACTION_EMAIL_CREATED = "android.intent.action.EMAIL_ACCOUNT_CREATED_INTENT";

    public static int SELECT_MESSAGE_NORMAL = 1;
    public static int SELECT_MESSAGE_OLDER = 2;
    public static int SELECT_MESSAGE_NEWER = 3;
    public static int SELECT_MESSAGE_ON_PAGER = 4;
    public static int SELECT_MESSAGE_ON_WIDGET = 5;
    public static int SELECT_MESSAGE_ON_INDICATORBAR = 6;

    public static final String ACCOUNT_CONFIGURED = "com.samsung.android.email.activity.setup.snc.ACCOUNT_CONFIGURED";
    public static String ACTION_CREATE_ACCOUNT = "com.samsung.android.email.ui.CREATE_ACCOUNT";
    public static String ACTION_CREATE_SELECTACCOUNT = "com.samsung.android.email.ui.CREATE_SELECTACCOUNT";
    public static String ACTION_ACCOUNT_PRESETUP = "com.samsung.android.email.ui.ACCOUNT_PRESETUP";
    public static String ACTION_ACCOUNT_SETUP_SNC_BASICS = "com.samsung.android.email.activity.setup.snc.ACCOUNT_SETUP_SNC_BASICS";
    public static String ACTION_ACCOUNT_SELECT_VZW = "com.samsung.android.email.ui.SELECT_ACCOUNT_VZW";
    public static String ACTION_ENTER_EACH_ACCOUNT_SETTINGS_MORE = "com.samsung.android.email.ui.activity.setup.ACCOUNT_SETTINGS_MORE";
    public static String ACTION_OPEN_CUSTOM_VZW_WEBVIEW = "com.samsung.android.email.commonutil.VZW_WEBVIEW";
    public static String ACTION_CREATE_ACCOUNT_EAS = "com.samsung.android.email.ui.CREATE_ACCOUNT_EAS";
    public static String ACTION_CREATE_ACCOUNT_NAUTA = "com.samsung.android.email.ui.CREATE_ACCOUNT_NAUTA";
    public static String INTERACTIVE_ACCOUNT_SETUP = "com.samsung.android.email.help.ACCOUNT_SETUP";
    public static String EXTRA_CREATE_ACCOUNT_EMAIL = "EMAIL";
    public static String EXTRA_CREATE_ACCOUNT_USER = "USER";
    public static String EXTRA_CREATE_ACCOUNT_INCOMING = "INCOMING";
    public static String EXTRA_CREATE_ACCOUNT_OUTGOING = "OUTGOING";


    public static final String ACTION_START_GENERAL_SETTINGS = "com.samsung.android.email.ui.activity.setup.GENERAL_SETTINGS";
    public static final String EXTRA_ENABLE_DEBUG = "AccountSettingsXL.enable_debug";
    public static final String EXTRA_LOGIN_WARNING_FOR_ACCOUNT = "AccountSettingsXL.for_account";
    public static final String EXTRA_TITLE = "AccountSettings.title";

    public static final String ACTION_HELP_CREATE_FILTERING = "com.samsung.android.email.ui.help.MANAGE_EMAIL_FILTER";
    public static final String ACTION_EXTERNAL_ACCOUNT_SETTINGSXL ="com.samsung.android.email.ui.activity.setup.AccountSettingsXL.intent.action.ACTION_EXTERNAL_ACCOUNT_SETTINGS";
    public static final String ACTION_EXTERNAL_ACCOUNT_SETTINGS = "com.samsung.android.email.ui.activity.setup.AccountSettings.intent.action.ACTION_EXTERNAL_ACCOUNT_SETTINGS";
    public static final String ACTION_ACCOUNT_MANAGER_ENTRY = "com.samsung.android.email.ui.activity.setup.ACCOUNT_MANAGER_ENTRY";
    public static final String ACTION_ENTER_EACH_ACCOUNT_SETTINGS = "com.samsung.android.email.ui.activity.setup.ACCOUNT_SETTINGS";
    public static final String ACTION_ENTER_PRIORITY_SENDER_SETTINGS = "com.samsung.android.email.ui.activity.setup.PRIORITY_SENDER_SETTINGS";
    public static final String ACTION_ENTER_DELETE_ACCOUNT = "com.samsung.android.email.ui.activity.setup.DELETE_ACCOUNT";
    public static final String ACTION_ENTER_SELECT_DEFAULT_ACCOUNT = "com.samsung.android.email.ui.activity.setup.SELECT_DEFAULT_ACCOUNT";

    public static final int GENERAL_PREFERENCE_FROM_EMAIL = 0;
    public static final int GENERAL_PREFERENCE_FROM_SETTING = 1;
    public static final String EXTRA_DELETE_OPTION = "AccountSettingsXL.for_deleteoption";

    public static final String EXTRA_ACCOUNT_MANAGER_ACCOUNT = "account";

    
    public static String EXTRA_TASK_ID = "task_id";
    public static final String EXTRA_SERVER_ERROR = "com.samsung.android.email.ui.MessageListXL.server_error";

    public static String BLACKLIST_PROCESS_EMAIL = "intent.blacklist.action.PROCESS_EMAIL";

    final public static String ACTION_RESCHEDULE_REMIND = "com.samsung.android.email.RescheduleReminder";
    final public static String ACTION_REMOVE_REMIND = "com.samsung.android.email.RemoveReminder";
    final public static String ACTION_CONFIRM_REMIND = "com.samsung.android.email.ConfirmReminder";
    final public static String ACTION_TRIGGER_REMIND = "com.samsung.android.email.TriggerReminder";
    final public static String ACTION_DISMISS_REMIND = "com.samsung.android.email.DismissReminder";
    final public static String ACTION_REPLY_REMIND = "com.samsung.android.email.ReplyReminder";

    final public static String SHOW_SSL_DIALOG_BROADCAST = "com.samsung.android.email.intent.action.SSLCertValidationAction";
    final public static String CLOSE_SSL_DIALOG_BROADCAST = "com.samsung.android.email.intent.action.Close_SSLCertValidationAction";

    public final static String EXTRA_LOCK_PASSWORD_EXPIRING = "EXPIRING";
    public final static String EXTRA_LOCK_PASSWORD_EXPIRED = "EXPIRED";
    public static final String EXTRA_SHOW_DIALOG = "show_dialog";
    
    public static String GET_DEVICEID = "com.samsung.android.email.GET_DEVICEID";
    
    public static String MDM_INSTALL_CERT ="com.samsung.android.email.INSTALL_CERTIFICATE";
    public static String MDM_SIGNATURE_UPDATED ="com.samsung.android.email.SIGNATURE_UPDATED";
    public static String MDM_ACCOUNT_DELETED ="com.samsung.android.email.ACCOUNT_DELETED";
    public static String MDM_RENAME_CERT ="com.samsung.android.email.RENAME_CERTIFICATE";
    public static String MDM_ENABLE_COMPOSE ="com.samsung.android.email.ENABLE_MESSAGECOMPOSE";
    
    
    final public static String EXTRA_RESET_TASK = "message.reset.task";
    
    public static final String ACTION_RESULT_GET_GOOGLE_AUTH_TOKEN = "com.samsung.android.email.intent.action.getGoogleAuthToken";
    
    public static final String EXTRA_ENCRYPT_ALIAS = "encryptAlias";

    public static final String EXTRA_SIGN_ALIAS = "signAlias";

    public static final String ACTION_INSTALL_MDM_CERTIFICATES = "com.samsung.android.email.mdm.cert.install";
    
    public static final String ACTION_FONT_SIZE_CHANGED = "com.samsung.settings.FONT_SIZE_CHANGED";
    
    public static final String ACTION_NOTISETUP = "com.samsung.android.email.setup.NOTISETUP";
    
    public static final String EXTRA_LIST_VIEW_EXPANDED = "list_view_expanded";
    public static String EXTRA_OPAQUE_SIGN_FLAG = "IS_OPAQUE_FLAG";
    
    public static String EXTRA_OPAQUE_SIGN_BODY = "OPAQUE_SIGN_BODY";
    
    public static String EXTRA_OPAQUE_SIGN_ATTACHMENTS = "OPAQUE_SIGN_ATTACHMENTS";
    public static String EXTRA_ACCOUNT_SETUP_EMAIL = "ACCOUNT_SETUP_EMAIL";

    public static String ACTION_APPLICATION_DETAILS_SETTINGS = "android.settings.APPLICATION_DETAILS_SETTINGS";
    public static String ACTION_BEFORE_APPLICATION_DETAILS_SETTINGS = "com.samsung.android.email.intent.action.BEFORE_APPLICATION_DETAILS_SETTINGS";

    public static final String SERVICE_META_DATA = "android.content.SyncAdapter";

    public static final String EXTRA_TYPE = "type";

    public static final String EXTRA_UNTRUSTED_CERTIFICATE_HOLD = "UNTRUSTED_CERTIFICATE_HOLD";

    public static final String BOOL_ACCOUNT_EXISTS = "com.samsung.android.email.extra.ACCOUNT_EXISTS";

    // Grace Help
    public static final String ACTION_HELP_SETUP_EMAIL_ACCOUNTS = "com.samsung.android.email.provider.action.SETUP_EMAIL_ACCOUNTS";
    public static final String ACTION_HELP_READING_EMAILS = "com.samsung.android.email.provider.action.READING_EMAILS";
    public static final String ACTION_HELP_COMPOSING_EMAILS = "com.samsung.android.email.provider.action.COMPOSING_EMAILS";
    public static final String ACTION_HELP_MANAGING_EMAILS = "com.samsung.android.email.provider.action.MANAGING_EMAILS";

    final public static String ACTION_DELETE_MESSAGE = "com.samsung.android.email.DeleteMessage";
}
