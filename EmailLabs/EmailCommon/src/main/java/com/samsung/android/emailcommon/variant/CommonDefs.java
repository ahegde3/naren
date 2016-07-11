
package com.samsung.android.emailcommon.variant;

import com.samsung.android.emailcommon.provider.AccountValues;

import android.os.Build;
import android.os.Environment;

public interface CommonDefs {
    
    public static final String STORE_SCHEME_IMAP = "imap";

    public static final String STORE_SCHEME_SYNCIMAP = "snc";

    public static final String STORE_SCHEME_POP3 = "pop3";

    public static final String STORE_SCHEME_EAS = "eas";

    public static final String STORE_SCHEME_LOCAL = "local";

    public static final String STORE_SECURITY_SSL = "+ssl";

    public static final String STORE_SECURITY_TLS = "+tls";

    public static final String STORE_SECURITY_TRUST_CERTIFICATES = "+trustallcerts";
  
    
    public static final String OPTIONS_USERNAME = "username";

    public static final String OPTIONS_PASSWORD = "password";

    public static final String OPTIONS_CONTACTS_SYNC_ENABLED = "contacts";

    public static final String OPTIONS_CALENDAR_SYNC_ENABLED = "calendar";

    public static final String OPTIONS_EMAIL_SYNC_ENABLED = "email";

    public static final String OPTIONS_TASKS_SYNC_ENABLED = "tasks";

    public static final String OPTIONS_NOTES_SYNC_ENABLED = "notes";
    
    
    public static final boolean ACCOUNT_RECOUNCILE_ENABLE = false;

    // NOTE
    public static final long SNC_QRESYNC_SLEEP_TIME = (15 * 60); // 15 mins in sec

    // NOTE This is done for Sync and Connect account
    public static final int SNC_INITIAL_MAX_DOWNLOAD_MSG_COUNT = 100;

    /**
     * In order to balance the load, the client has to download the messaegs
     * from the server in batches.
     */
    public static final int MAX_MESSAGE_FETCH_FROM_SERVER = 25;

    /*
     * Everytime, when the user refreshes the mailbox, only the latest messages
     * upto this value will be updated. Not all!
     */
    public static final int DEFAULT_REFRESH_MESSAGE_COUNT = 100;

    public static final boolean SNC_CREATE_MAILBOX_IF_NOT_EXISTS = false;
    /**
     * If for any reasons, if the server returns the delta of size > 150,
     * then it is better to reset the QRESYNC mode rather than trying to
     * pull all the changes.
     */
    public static final int MAX_MESSAGE_FETCH_IN_QRESYNC = 150; // 150 messages

    public static final String EXCHANGE_GAL_AUTHORITY = "com.samsung.android.exchange.directory.provider";

    public static boolean SEND_SNC_SMS = false;
    
    // SyncConnector Porting
    /**
     * All useful/funny/urWishList things that you want to have during Dev!
     * shall be under this flag!
     */

    public static final String CLIENT_VERSION = "EAS-1.2";

    // change@easPhoneUI.jongyun.kim START
    // Version (X0X...)
    // ex) 10612 == Version 1.612
    public static final String VERSION = "101"; // EAS Version == 1.1

    // public static final String PLATFORM_VERSION= "202"; // OS Platform
    // Version ==2.2
    // jykim for UserAgent
    public static final String PLATFORM_VERSION = Build.VERSION.RELEASE.replace(".", "0"); // OS
                                                                                           // Platform
                                                                                           // Version

    // change@easPhoneUI.jongyun.kim END
    public static final String ACCOUNT_MAILBOX_PREFIX = "__eas";

    // Define our default protocol version as 2.5 (Exchange 2003)
    public static final String SUPPORTED_PROTOCOL_EX2003 = "2.5";
    public static final double SUPPORTED_PROTOCOL_EX2003_DOUBLE = 2.5;
    public static final String SUPPORTED_PROTOCOL_EX2007 = "12.0";
    public static final double SUPPORTED_PROTOCOL_EX2007_DOUBLE = 12.0;
    // change@pguha: protocol 12.1 start
    public static final String SUPPORTED_PROTOCOL_EX2007_EXT = "12.1";
    public static final double SUPPORTED_PROTOCOL_EX2007_EXT_DOUBLE = 12.1;
    // change@pguha: protocol 12.1 end
    // change@wtl.pkijowski 14.0
    public static final String SUPPORTED_PROTOCOL_EX2010 = "14.0";
    public static final double SUPPORTED_PROTOCOL_EX2010_DOUBLE = 14.0;
    // change@wtl.pkijowski 14.0
    public static final String SUPPORTED_PROTOCOL_EX2010_EXT = "14.1";
    public static final double SUPPORTED_PROTOCOL_EX2010_EXT_DOUBLE = 14.1;
    public static final String DEFAULT_PROTOCOL_VERSION = SUPPORTED_PROTOCOL_EX2003;
    public static final String ACCOUNT_MAILBOX = "__eas";
    
    public static final String EXCHANGE_CONFIGURATION_USE_ALTERNATE_STRINGS = "com.samsung.android.email.ui.EXCHANGE_CONFIGURATION_USE_ALTERNATE_STRINGS";

    // change@wtl.jpshu recipient_information_cache begin
    // TODO hard code the max to 30
    public static final String MAX_ITEMS = "30";

    
    public enum EmailDataSize {
        // Add receive option for CTC : China CDMA CTC function : hm.je_20120206 _ start
        HEADERS_ONLY(0, "0"), HALF_KB(1, "1"), ONE_KB(2, "2"), TWO_KB(3, "3"), FIVE_KB(4, "4"), TEN_KB(
                5, "5"), TWENTY_KB(6, "6"), FIFTY_KB(7, "7"), HUNDRED_KB(8, "8"), ALL(9, "9"), ALL_WITH_ATTACHMENT(10, "10"),
        // Add receive option for CTC : China CDMA CTC function : hm.je_20120206 _ end
        AUTOMATIC(11, "11");

        private final byte mValue;
        private final String mText;

  
        EmailDataSize(byte value, String text) {
            mValue = value;
            mText = text;
        }

        EmailDataSize(int value, String text) {
            mValue = (byte) value;
            mText = text;
        }

        public byte getAsByte() {
            return mValue;
        }

        public int getAsInt() {
            return mValue;
        }

        public String getAsString() {
            return mText;
        }

        // These constants are based on EAS 2.5 enum
        public int toEas2_5Value() {
            return mValue;
        }

        public String toEas2_5Text() {
            return mText;
        }

        // map to actual size in bytes
        public int toEas12Value() {
            switch (mValue) {
                case 0:
                    // 0 bytes for headers only
                    return 0x0;
                case 1:
                    // 512 bytes
                    return 0x200;
                case 2:
                    // 1KB
                    return 0x400;
                case 3:
                    // 2KB
                    return 0x800;
                case 4:
                    // 5KB
                    return 0x1400;
                case 5:
                    // 10KB
                    return 0x2800;
                case 6:
                    // 20KB
                    return 0x5000;
                case 7:
                    // 50KB
                    return 0xC800;
                case 8:
                    // 100KB
                    return 0x19000;
                case 9:
                // Add receive option for CTC : China CDMA CTC function : hm.je_20120206 _ start
                case 10:
                // Add receive option for CTC : China CDMA CTC function : hm.je_20120206 _ start
                case 11:
                default:
                    // use max case
                    break;
            }

            // Although eliminating the TruncationSize element is supposed to
            // indicate "All"
            // Using the maximum number of bytes supported should be fine. No
            // one is going to have
            // an email > 2.0 GB in size.
            //
            // 2,147,483,647 bytes is the max: aka 0x7FFFFFFF, aka max positive
            // value for signed int
            return 0x7FFFFFFF;
        }

        public String toEas12Text() {
            switch (mValue) {
                case 0:
                    // 0 bytes for headers only
                    return "0";
                case 1:
                    // 512 bytes
                    return "512";
                case 2:
                    // 1KB
                    return "1024";
                case 3:
                    // 2KB
                    return "2048";
                case 4:
                    // 5KB
                    return "5120";
                case 5:
                    // 10KB
                    return "10240";
                case 6:
                    // 20KB
                    return "20480";
                case 7:
                    // 50KB
                    return "51200";
                case 8:
                    // 100KB
                    return "102400";
                case 9:
                // Add receive option for CTC : China CDMA CTC function : hm.je_20120206 _ start
                case 10:
                // Add receive option for CTC : China CDMA CTC function : hm.je_20120206 _ start
                case 11:
                default:
                    // use max case
                    break;
            }

            // Although eliminating the TruncationSize element is supposed to
            // indicate "All"
            // Using the maximum number of bytes supported should be fine. No
            // one is going to have
            // an email > 2.0 GB in size.
            //
            // 2,147,483,647 bytes is the max: aka 0x7FFFFFFF, aka max positive
            // value for signed int
            return "2147483647";
        }

        public static EmailDataSize parse(byte value) {
            switch (value) {
                case 0:
                    // headers only
                    return HEADERS_ONLY;
                case 1:
                    // half kb
                    return HALF_KB;
                case 2:
                    // 1KB
                    return ONE_KB;
                case 3:
                    // 2KB
                    return TWO_KB;
                case 4:
                    // 5KB
                    return FIVE_KB;
                case 5:
                    // 10KB
                    return TEN_KB;
                case 6:
                    // 20KB
                    return TWENTY_KB;
                case 7:
                    // 50KB
                    return FIFTY_KB;
                case 8:
                    // 100KB
                    return HUNDRED_KB;
                // Add receive option for CTC : China CDMA CTC function : hm.je_20120206 _ start
                case 10:
                    return ALL_WITH_ATTACHMENT;
                // Add receive option for CTC : China CDMA CTC function : hm.je_20120206 _ start
                case 11:
                    return AUTOMATIC;
                case 9:
                default:
                    // use max case
                    break;
            }

            // ALL
            return ALL;
        }

        public static EmailDataSize parse(int value) {
            return parse((byte) value);
        }

        public static EmailDataSize parse(String parseMe) {
            byte b = -1;
            try {
                b = Byte.parseByte(parseMe);
            } catch (NumberFormatException nfe) {
                // in case this is text
                return null;
            }
            return parse(b);
        }

        public static byte parseToByte(int size) {
            if (size == 0)
                return AccountValues.SyncSize.EMAIL_RETRIEVE_SIZE_INDEX_NO_LIMIT; // No limit

            if (size > 0 && size < 512)
                return AccountValues.SyncSize.EMAIL_RETRIEVE_SIZE_INDEX_HEADER_ONLY;
            else if (size >= 512 && size < 1024) {
                return AccountValues.SyncSize.EMAIL_RETRIEVE_SIZE_INDEX_HALF_KB;
            } else if (size >= 1024 && size < 2048) {
                return AccountValues.SyncSize.EMAIL_RETRIEVE_SIZE_INDEX_ONE_KB;
            } else if (size >= 2048 && size < 5120) {
                return AccountValues.SyncSize.EMAIL_RETRIEVE_SIZE_INDEX_TWO_KB;
            } else if (size >= 5120 && size < 10240) {
                return AccountValues.SyncSize.EMAIL_RETRIEVE_SIZE_INDEX_FIVE_KB;
            } else if (size >= 10240 && size < 20480) {
                return AccountValues.SyncSize.EMAIL_RETRIEVE_SIZE_INDEX_TEN_KB;
            } else if (size >= 20480 && size < 51200) {
                return AccountValues.SyncSize.EMAIL_RETRIEVE_SIZE_INDEX_TWENTY_KB;
            } else if (size >= 51200 && size < 102400) {
                return AccountValues.SyncSize.EMAIL_RETRIEVE_SIZE_INDEX_FIFTY_KB;
            } else {
                return AccountValues.SyncSize.EMAIL_RETRIEVE_SIZE_INDEX_HUNDRED_KB;
            }
        }
        
        public static byte parseToByte03(int size){
            if (size == 0)
                return AccountValues.SyncSize.EMAIL_RETRIEVE_SIZE_INDEX_NO_LIMIT; // No limit
            
            if (size > 0 && size < 4096) {
                return AccountValues.SyncSize.EMAIL_RETRIEVE_SIZE_INDEX_HEADER_ONLY;
            } else if (size >= 4096 && size < 5120) {
                return AccountValues.SyncSize.EMAIL_RETRIEVE_SIZE_INDEX_FOUR_KB_03;
            } else if (size >= 5120 && size < 7168) {
                return AccountValues.SyncSize.EMAIL_RETRIEVE_SIZE_INDEX_FIVE_KB_03;
            } else if (size >= 7168 && size < 10240) {
                return AccountValues.SyncSize.EMAIL_RETRIEVE_SIZE_INDEX_SEVEN_KB_03;
            } else if (size >= 10240 && size < 20480) {
                return AccountValues.SyncSize.EMAIL_RETRIEVE_SIZE_INDEX_TEN_KB_03;
            } else if (size >= 20480 && size < 51200) {
                return AccountValues.SyncSize.EMAIL_RETRIEVE_SIZE_INDEX_TWENTY_KB_03;
            } else if (size >= 51200 && size < 102400) {
                return AccountValues.SyncSize.EMAIL_RETRIEVE_SIZE_INDEX_FIFTY_KB_03;
            } else {
                return AccountValues.SyncSize.EMAIL_RETRIEVE_SIZE_INDEX_HUNDRED_KB_03;
            }
        }
    }

    public static final int FOLDER_STATUS_OK = 1;
    public static final int SERVER_ERROR = 6;
    public static final int FOLDER_STATUS_INVALID_KEY = 9;
    // change@siso.cmouli Provisioning start
    public static final int STATUS_CODE_DEVICE_NOT_FULLY_PROVISIONABLE = 139;
    public static final int STATUS_CODE_REMOTE_WIPE_REQUESTED = 140;
    public static final int STATUS_CODE_LEGACY_DEVICE_ON_STRICT_POLICY = 141;
    public static final int STATUS_CODE_DEVICE_NOT_PROVISIONED = 142;
    public static final int STATUS_CODE_POLICY_REFRESH = 143;
    public static final int STATUS_CODE_INVALID_POLICY_KEY = 144;
    // change@siso.cmouli Provisioning end

    public static final int EXCHANGE_ERROR_NOTIFICATION = 0x10;
    // change@siso.mahsky Allow/Block/Access start
    public static final int STATUS_CODE_MAIL_SUBMISSION_FAILED_WHILE_DEVICE_QUARANTINED = 120;
    public static final int STATUS_CODE_DEVICE_BLOCKED = 129;

    // change@siso.mahsky Allow/Block/Access End

    public static final int STATUS_CODE_TOO_MANY_PARTNERSHIPS = 177;
   

    public static final int SD_ENCRYPTION_NO_REQUIRED = 0;
    public static final int SD_ENCRYPTION_POLICY_REQUIRED = 1;
    public static final int SD_ENCRYPTION_REQUIRED = 2;

    public static final boolean EAS_LOCAL_DB_OPERATION = false; 
 // == attachment file's mime type define: yonghee2.lee 20100127 samsung

    // public static final String ACCEPTABLE_ATTACHMENT_SEND_SEC_TYPES =
    // "image/*;audio/*;video/*;text/x-vcard;text/x-vCalendar;text/x-vnote";

    public static final String ACCEPTABLE_ATTACHMENT_SEND_SEC_TYPES = "*/*";

    /**
     * Specifies how many messages will be shown in a folder by default. This
     * number is set on each new folder and can be incremented with
     * "Load more messages..." by the VISIBLE_LIMIT_INCREMENT
     */
    public static final int VISIBLE_LIMIT_DEFAULT = 25;

    /**
     * Number of additional messages to load when a user selects
     * "Load more messages..."
     */
    public static final int VISIBLE_LIMIT_INCREMENT = 25;

    public static final int[] VISIBLE_LIMIT_ARRAY = {
            25, 50, 75, 100, 200, 1000
    };

    /**
     * This is used to force stacked UI to return to the "welcome" screen any
     * time we change the accounts list (e.g. deleting accounts in the Account
     * Manager preferences.)
     */
//    public static final String LOG_PROVIDER_CONTENT_URI = "content://logs/email";

    public static final int EMAIL_LOG_INCOMING_TYPE = 1;

    public static final int EMAIL_LOG_OUTGOING_TYPE = 2;
    
    public static final boolean ALWAYS_ROAMING = false;
    
    public static final String EXTRA_DEVICE_POLICY_ADMIN = "message_code";
    
    public static final String ACTION_BROADCAST = "broadcast_receiver";
    
    public static final String ACTION_DEVICE_POLICY_ADMIN = "com.samsung.android.email.ui.devicepolicy";
    
    public static final String tempDirectory = Environment.getExternalStorageDirectory()
            + "/.EmailTempImage";
    public static final String SAVE_SIGNATURE_TMPSAMMFILE_PATH = "signature_handWriting_"; //add code temporary
    public static final String REMOVE_PRIVATE_KEYSTORE_FILE = "removePrivateKeystoreFile";

}
