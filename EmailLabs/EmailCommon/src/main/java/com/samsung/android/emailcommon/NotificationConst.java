package com.samsung.android.emailcommon;

public interface NotificationConst {
    public static final int NOTIFICATION_ID_BASE_NEW_MESSAGES            = 0x10000000;
    public static final int NOTIFICATION_ID_BASE_LOGIN_WARNING           = 0x20000000;
    public static final int NOTIFICATION_ID_BASE_SENDING_MESSAGE_FAIL    = 0x30000000;
    
    public static final int NOTIFICATION_ID_SENDINGNOTI_MESSAGE            = 0x31000000;

    // SVL Start: sshah, S&C Account Notifications.
    public static final int NOTIFICATION_ID_SNC_ACCOUNT_UPDATION         = 0x40000000;
    public static final int NOTIFICATION_ID_SNC_PASSWORD_UPDATION        = 0x50000000;
    public static final int NOTIFICATION_ID_CANCEL_SENDING_MESSAGE       = 0x60000000; //change@siso.gaurav cancel send
    public static final int NOTIFICATION_ID_KEYSTORE_UPGRADE             = 0x70000000;
    //Account configuration status notification
    public static final int NOTIFICATION_ID_ACCOUNT_CONFIGURATION_STATUS = 0x80000000;
    public static final int NOTIFICATION_ID_SECURITY_NEEDED              = 0x90000000;
    public static final int NOTIFICATION_ID_EXCHANGE_CALENDAR_ADDED      = 0xA0000000;
    public static final int NOTIFICATION_ID_ATTACHMENT_WARNING           = 0xB0000000;
    public static final int NOTIFICATION_ID_PASSWORD_EXPIRING            = 0xC0000000;
    public static final int NOTIFICATION_ID_PASSWORD_EXPIRED             = 0xD0000000;
    public static final int NOTIFICATION_ID_MDM_DATA_RECEIVED            = 0xE0000000;

    public static final int NOTIFICATION_ID_MDMCERTS_RECEIVED            = 0xF0000000;
    public static final int NOTIFICATION_ID_RUNTIME_PERMISSION_BACKGROUND_CONTACT     = 0xF1000000;
    public static final int NOTIFICATION_ID_RUNTIME_PERMISSION_BACKGROUND_CALENDAR     = 0xF2000000;
    public static final int NOTIFICATION_ID_RUNTIME_PERMISSION_BACKGROUND_SMS     = 0xF3000000;
    public static final int NOTIFICATION_ID_UNTRUSTED_CERTIFICATE        = 0xF4000000;
    
    public static final String NOTI_EXTRA_ACTION = "Action";
    public static final String NOTI_EXTRA_ACCOUNT_ID = "AccountID";
    public static final String NOTI_EXTRA_ACCOUNT_ADDRESS = "AccountAddress";
    public static final String NOTI_EXTRA_MESSAGE_ID = "MessageID";
    public static final String NOTI_EXTRA_TITLE = "Title";
    public static final String NOTI_EXTRA_SENDER = "Sender";
    public static final String EXTRA_VOICE_REPLY = "extra_voice_reply";
}
