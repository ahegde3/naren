package com.samsung.android.emailcommon;

import com.samsung.android.emailcommon.provider.EmailContent.AccountColumns;
import com.samsung.android.emailcommon.service.PolicySet;

public class SecurityPolicyDefs {
    /** Possible policies */
    public static final String POLICY_REMOTE_WIPE_REQUIRED = "RemoteWipe";

    public static final String POLICY_PASSWORD_MODE = "PasswordMode";
    public static final String POLICY_PASSWORD_RECOVERY_ENABLED = "PasswordRecoveryEnabled";
    public static final String POLICY_MAX_DEVICE_PASSWORD_FAILED_ATTEMPTS = "MaxDevicePasswordFailedAttempts";
    public static final String POLICY_DEVICE_PASSWORD_EXPIRATION = "DevicePasswordExpiration";
    public static final String POLICY_DEVICE_PASSWORD_HISTORY = "DevicePasswordHistory";
    public static final String POLICY_MIN_DEVICE_PASSWORD_LENGTH = "MinDevicePasswordLength";
    public static final String POLICY_MAX_INACTIVITY_TIME = "MaxInactivityTime";
    public static final String POLICY_REQUIRE_DEVICE_ENCRYPTION = "RequireDeviceEncryption";
    public static final String POLICY_DEVICE_ENCRYPTION_ENABLED = "DeviceEncryptionEnabled";
    public static final String POLICY_ATTACHMENTS_ENABLED = "AttachmentsEnabled";
    public static final String POLICY_SIMPLE_PASSWORD_ENABLED = "AllowSimpleDevicePassword";
    public static final String POLICY_MAX_ATTACHMENT_SIZE = "MaxAttachmentSize";
    public static final String POLICY_ALLOW_STORAGE_CARD = "AllowStorageCard";
    public static final String POLICY_ALLOW_CAMERA = "AllowCamera";
    public static final String POLICY_ALLOW_WIFI = "AllowWifi";
    public static final String POLICY_ALLOW_TEXT_MESSAGING = "AllowTextMessaging";
    public static final String POLICY_ALLOW_POPIMAP_EMAIL = "AllowPOPIMAPEmail";
    public static final String POLICY_ALLOW_HTML_EMAIL = "AllowHTMLEmail";
    public static final String POLICY_ALLOW_BROWSER = "AllowBrowser";
    public static final String POLICY_ALLOW_INTERNET_SHARING = "AllowInternetSharing";
    public static final String POLICY_REQUIRE_MANUALSYNC_ROAMING = "RequireManualSyncWhenRoaming";
    public static final String POLICY_ALLOW_BLUETOOTH = "AllowBluetoothMode";
    public static final String POLICY_MIN_PASSWORD_COMPLEX_CHARS = "MinPasswordComplexCharacters";
    public static final String POLICY_MAX_CALENDAR_AGE_FILTER = "MaxCalendarAgeFilter";
    public static final String POLICY_MAX_EMAIL_AGE_FILTER = "MaxEmailAgeFilter";
    public static final String POLICY_MAX_EMAIL_BODY_TRUNC_SIZE = "MaxEmailBodyTruncationSize";
    public static final String POLICY_MAX_EMAILHTML_BODY_TRUNC_SIZE = "MaxEmailHtmlBodyTruncationSize";
    public static final String POLICY_REQUIRE_SIGNED_SMIME_MSGS = "RequireSignedSMIMEMessages";
    public static final String POLICY_REQUIRE_ENCRYPTED_SMIME_MSGS = "RequireEncryptedSMIMEMessages";
    public static final String POLICY_REQUIRE_SIGNED_SMIME_ALGORITHM = "RequireSignedSMIMEAlgorithm";
    public static final String POLICY_REQUIRE_ENCRYPTION_SMIME_ALGORITHM = "RequireEncryptionSMIMEAlgorithm";
    public static final String POLICY_ALLOW_SMIME_ENCRYPTION_ALGO_NEGOTIATION = "AllowSMIMEEncryptionAlgorithmNegotiation";
    public static final String POLICY_ALLOW_SMIME_SOFT_CERTS = "AllowSMIMESoftCerts";
    public static final String POLICY_ALLOW_DESKTOP_SYNC = "AllowDesktopSync";
    public static final String POLICY_ALLOW_IRDA = "AllowIrDA";
    public static final String POLICY_ALLOW_APP_THIRD_PARTY = "AllowAppThirdParty";
    public static final String POLICY_BLOCK_APP_INROM = "BlockAppInRom";
    public static final String POLICY_ALLOW_UNSIGNED_APPS = "AllowUnsignedApplications";
    public static final String POLICY_ALLOW_UNSIGNED_INSTALLATION_PACKAGES = "AllowUnsignedInstallationPackages";

    public static final int ALLOW_BLUETOOTH_MODE_VALUE_DISABLE = 0;
    public static final int ALLOW_BLUETOOTH_MODE_VALUE_HANDSFREE_ONLY = 1;
    public static final int ALLOW_BLUETOOTH_MODE_VALUE_ALLOW = 2;

    public static final PolicySet NO_POLICY_SET = new PolicySet(0,
            PolicySet.PASSWORD_MODE_NONE, 0, 0, false, false, 0, 0, true, 0, true, true, true,
            true, true, true, true, true, false, ALLOW_BLUETOOTH_MODE_VALUE_ALLOW, 0, 0, 0, 0, 0,
            false, false,
            -1, -1, -1,
            true, true, true, false, false, true, "", "", true, true); 

    /**
     * Projection of the password related policies
     */
    public static final String[] PASSWORD_POLICIES = new String[] {
            POLICY_PASSWORD_MODE,
            POLICY_PASSWORD_RECOVERY_ENABLED, POLICY_MAX_DEVICE_PASSWORD_FAILED_ATTEMPTS,
            POLICY_SIMPLE_PASSWORD_ENABLED, POLICY_DEVICE_PASSWORD_EXPIRATION,
            POLICY_DEVICE_PASSWORD_HISTORY, POLICY_MIN_DEVICE_PASSWORD_LENGTH
            , POLICY_MIN_PASSWORD_COMPLEX_CHARS
    };

    /**
     * This projection on Account is for scanning/reading
     */
    public static final String[] ACCOUNT_SECURITY_PROJECTION = new String[] {
            AccountColumns.ID, AccountColumns.SECURITY_SYNC_KEY
    };

    public static final int ACCOUNT_SECURITY_COLUMN_ID = 0;
    public static final int ACCOUNT_SECURITY_COLUMN_FLAGS = 1;

    public static final int DEVICE_ADMIN_MESSAGE_ENABLED = 1;
    public static final int DEVICE_ADMIN_MESSAGE_DISABLED = 2;
    public static final int DEVICE_ADMIN_MESSAGE_PASSWORD_CHANGED = 3;
    public static final int DEVICE_ADMIN_MESSAGE_PASSWORD_EXPIRING = 4;

    public final static int INACTIVE_NEED_ACTIVATION = 1;
    public final static int INACTIVE_NEED_CONFIGURATION = 2;
    public final static int INACTIVE_NEED_PASSWORD = 4;
    public final static int INACTIVE_NEED_ENCRYPTION = 8;
    public final static int INACTIVE_NEED_EXT_SD_ENCRYPTION = 16;    
}
