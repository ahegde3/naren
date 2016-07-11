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

package com.samsung.android.emailcommon.mail;

//upgrade to HC3.1 import com.samsung.android.email.ui.R;

import com.samsung.android.emailcommon.utility.EmailLog;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * This exception is used for most types of failures that occur during server
 * interactions. Data passed through this exception should be considered
 * non-localized. Any strings should either be internal-only (for debugging) or
 * server-generated. TO DO: Does it make sense to further collapse
 * AuthenticationFailedException and CertificateValidationException and any
 * others into this?
 */
public class MessagingException extends Exception {

//    private final String[] mExceptionString = {
//            /**
//             * NOTE: If the below ordering is changed, the string array needs to
//             * be chagned. This is a temp work until R.string is defined TODO
//             * update R.values.strings.xml file
//             */
//            " NO_ERROR ", " UNSPECIFIED_EXCEPTION ", " IOERROR ", " TLS_REQUIRED ",
//            " AUTH_REQUIRED ", " GENERAL_SECURITY ", /*
//                                                      * R.string.
//                                                      * account_setup_failed_security
//                                                      */
//            " AUTHENTICATION_FAILED ", " DUPLICATE_ACCOUNT ",
//            " SECURITY_POLICIES_REQUIRED ",
//            " SECURITY_POLICIES_UNSUPPORTED ",
//            " PROTOCOL_VERSION_UNSUPPORTED ",
//            " GAL_EXCEPTION ",
//            " OOO_EXCEPTION ",
//            " SECURITY_POLICIES_POP3IMAP_DISABLED ",
//            " EMAIL_SYNC_DISABLED ",           //MNO_LAB : d1.jain : Added this to the non-localised string array
//            " STRANGE ",
//            " STRANGE ",
//            " STRANGE ",
//            " STRANGE ",
//            " STRANGE ",
//            " LEGACY_SERVER_NORESPONSE ",
//            " STRANGE ",
//            " STRANGE ",
//            " SECURITY_POLICY_ATTACHMENT_MAX_SIZE ",
//            " SECURITY_POLICY_ATTACHMENT_DISABLED ",
//            " ATTACHMENT_DOWNLOAD_FAILED ",
//            " UNSUPPORTED ",
//            " LOADMORE_EXCEPTION ",
//            " SUCCESS ",
//            " IN_PROGRESS ",
//            " ACCOUNT_MOVE_SUCCESS ",
//            " MESSAGE_NOT_FOUND ",
//            " ATTACHMENT_NOT_FOUND ",
//            " FOLDER_NOT_DELETED ",
//            " FOLDER_NOT_RENAMED ",
//            " FOLDER_NOT_CREATED ",
//            " REMOTE_EXCEPTION ",
//            " LOGIN_FAILED ",
//            " SECURITY_FAILURE ",
//            " ACCOUNT_UNINITIALIZED ",
//            " CONNECTION_ERROR ",
//            " ERROR_NO_PROTOCOL_SUPPORT ", // R.string.oof_no_protocol_support
//            " ERROR_SERVER_CONNECT ", " ERROR_GAL_NULL_STRING ", " ERROR_GAL_RESPONSE_PARSE ",
//            " ERROR_GAL_INVALID_RANGE ", " ERROR_ITEM ", " ERROR_OOO_SET_ERROR ",
//            " ERROR_OOO_GET_ERROR ", " ERROR_OOO_RESPONSE_PARSE ", " ERROR_SERVER_ERROR ",
//            " ERROR_SYSTEM_FOLDER ", " ERROR_FOLDER_NOT_EXIST ", " ERROR_FOLDER_EXISTS ",
//            " ERROR_FOLDER_PARENT_NOT_FOUND ", " ERROR_EMPTYTRASH_RESPONSE_PARSE ",
//            " ERROR_EMPTYTRASH_FAILURE ", " ERROR_FETCH_RESPONSE_PARSE ", " ERROR_FETCH_FAILURE ",
//            " ERROR_FETCH_OUTOFMEMORY ", " ERROR_EMPTYTRASH_TIMEOUT ", " ERROR_FETCH_NULLMSG ",
//            " EMPTYTRASH_EXCEPTION ", " ERROR_INVALID_PARAM ", " ERROR_OOO_NOT_SUPPORTED ",
//            " ERROR_CONNECTING_SERVICE ", " FILE_IOERROR ", " ERROR_FETCHING_MSG_BODY ",
//            " ERROR_SERVICE_RESPONSE_NULL ", " ERROR_PROCESS_SERVICE_RESPONSE_SUCCESS ",
//            " ERROR_SERVICE_RESPONSE_FAILURE ", " ERROR_MAX_RETRY_ATTEMPTS_REACHED ",
//            " ERROR_NOT_FOUND_IN_DB ", " ERROR_LOADING_BODY ", " ERROR_VIEW_ATTACHMENT ",
//            " ERROR_SERVER_ERROR_15MIN_RESTRICTION", " CERTIFICATE_VALIDATION_ERROR ",
//            " AUTODISCOVER_AUTHENTICATION_FAILED ", " AUTODISCOVER_AUTHENTICATION_RESULT ",
//            " AUTHENTICATION_FAILED_OR_SERVER_ERROR ", " LICENSE_FAILURE ",
//            " PROGRESS_CHECK_INCOMIMNG ", " PROGRESS_CHECK_OUTGOING ", " CHECK_SETTINGS_OK ",
//            " CHECK_SETTINGS_ERROR ", " CANCEL_CHECK_SETTINGS_SUCCESS ",
//            " CANCEL_CHECK_SETTINGS_ERROR ", " ERROR_CONNECTION_READ_TIMEOUT ",
//            " ERROR_SERVER_ERROR_HTTP_5XX", " ERROR_SERVER_ERROR_INSUFFICIENT_STORAGE",
//            " ERROR_SERVER_NOT_FOUND", " ERROR_SERVER_NOT_RESPONDING ",
//            " ERROR_NETWORK_NOT_AVAILABLE ", " ERROR_DELETE_ACCOUNT_FAILURE",
//            " ERROR_NETWORK_TRANSIENT_ERROR", " ERROR_NO_NETWORK",
//            " ERROR_FOLDER_UPDATE_NOT_ALLOWED", " PROGRESS_CHECK_LICENSE_SERVER",
//            " ERROR_SEND_MSG_UNLOADED_ATTACHMENT", " MAX_DEVICE_PARTNERSHIP_EXCEPTION",
//            " ERROR_CERTIFICATE_NOT_AVAILABLE", " DEVICE_BLOCKED_EXCEPTION"
//    };

    public static final long serialVersionUID = -1;

    public static final int NO_ERROR = -1;

    /** Any exception that does not specify a specific issue */
    public static final int UNSPECIFIED_EXCEPTION = 0;

    /** Connection or IO errors */
    public static final int IOERROR = 1;

    /** The configuration requested TLS but the server did not support it. */
    public static final int TLS_REQUIRED = 2;

    /** Authentication is required but the server did not support it. */
    public static final int AUTH_REQUIRED = 3;

    /** General security failures */
    public static final int GENERAL_SECURITY = 4;

    /** Authentication failed */
    public static final int AUTHENTICATION_FAILED = 5;

    /** Attempt to create duplicate account */
    public static final int DUPLICATE_ACCOUNT = 6;

    /** Required security policies reported - advisory only */
    public static final int SECURITY_POLICIES_REQUIRED = 7;

    /** Required security policies not supported */
    public static final int SECURITY_POLICIES_UNSUPPORTED = 8;

    /** The protocol (or protocol version) isn't supported */
    public static final int PROTOCOL_VERSION_UNSUPPORTED = 9;

    // change@wtl.kSingh - isolating feature exception mappings - starts
    /** Gal Search exception */
    public static final int GAL_EXCEPTION = 10;

    /** out of office exception */
    public static final int OOO_EXCEPTION = 11;

    // change@wtl.kSingh - isolating feature exception mappings - ends
    /** Exceptions for DPM AllowPOP3IMAP device polcy */
    public static final int SECURITY_POLICIES_POP3IMAP_DISABLED = 12;

    /** Exceptions for sync disabled */
    public static final int EMAIL_SYNC_DISABLED = 13; // adapter_porting

    // jh5997.hyun - 2011.03.02 - Add Exception for EAS Activation ERROR - START
    public static final int ACTIVATION_ERROR = 16;
    public static final int ACTIVATION_ERROR_NO_DEVICE_ID = 18;

    public static final int SECURITY_POLICIES_POP_IMAP_UNSUPPORTED = 14;

    /** The server refused access */
    public static final int ACCESS_DENIED = 15;
    
    // ++yuseung9.kim, 2011.08.03, Add max attachment size restriction
    // noti(50MB)
    public static final int EXCEEDED_MESSAGE_SIZE_LIMITS_ERROR = 17;
    // --yuseung9.kim, 2011.08.03, Add max attachment size restriction
    // noti(50MB)

    // jh5997.hyun - 2011.03.02 - Add Exception for EAS Activation ERROR - END
    
    public static final int AUTODISCOVER_TRY_MANUAL = 19;

    public static final int LEGACY_SERVER_NORESPONSE = 20;
    
    // change@wtl.dtuttle empty_trash start
    // et_41: Adding exception to indicate an unsupported action.
    // Chose to use the 3rd byte since Google is using the first byte
    // with [0,6] and the fourth byte with the -1.
    // DEPRECATED ... remove it in a future iteration.
    /* Exceptions for DPM Attachment policy */
    /** FIXME this value directly depends on View's value. Dont cahnge */
    public static final int SECURITY_POLICY_ATTACHMENT_MAX_SIZE = 21;

    public static final int SECURITY_POLICY_ATTACHMENT_DISABLED = 22;

    public static final int ATTACHMENT_DOWNLOAD_FAILED = 23;

    /** Server does not support the request action */
    public static final int UNSUPPORTED = 24; // unused!

    // change@wtl.dtuttle end

    // change@wtl.dtuttle feature exception mapping start

    public static final int LOADMORE_EXCEPTION = 25;

    // change@wtl.dtuttle end

    /** Merged from MessagingException.java **/

    public static final int SUCCESS = 26;

    public static final int IN_PROGRESS = 27;

    public static final int ACCOUNT_MOVE_SUCCESS = 28; // gunheng.lee, 100923

    public static final int MESSAGE_NOT_FOUND = 29;

    public static final int ATTACHMENT_NOT_FOUND = 30;

    public static final int FOLDER_NOT_DELETED = 31;

    public static final int FOLDER_NOT_UPDATED = 32;

    public static final int FOLDER_NOT_CREATED = 33;

    public static final int REMOTE_EXCEPTION = 34;

    public static final int LOGIN_FAILED = 35;

    public static final int SECURITY_FAILURE = 36;

    public static final int ACCOUNT_UNINITIALIZED = 37;

    // Maybe we should automatically retry these?
    public static final int CONNECTION_ERROR = 38;

    // change@wtl.kSingh adding custom error codes for Gal Search and
    // future enhancements.
    // public static final int CUSTOM_ERROR_BASE = 39;
    public static final int ERROR_NO_PROTOCOL_SUPPORT = 39;

    public static final int ERROR_SERVER_CONNECT = 40;

    public static final int ERROR_GAL_NULL_STRING = 41;

    public static final int ERROR_GAL_RESPONSE_PARSE = 42;

    public static final int ERROR_GAL_INVALID_RANGE = 43;

    // change@wtl.jrabina Move Message
    public static final int ERROR_ITEM = 44;

    // change @wtl.kSingh added for Out of office - starts
    public static final int ERROR_OOO_SET_ERROR = 45;

    public static final int ERROR_OOO_GET_ERROR = 46;

    public static final int ERROR_OOO_RESPONSE_PARSE = 47;

    // change @wtl.kSingh added for Out of office - ends

    // change@wtl.jrabina Folder Commands start
    public static final int ERROR_SERVER_ERROR = 48;

    public static final int ERROR_SYSTEM_FOLDER = 49;

    public static final int ERROR_FOLDER_NOT_EXIST = 50;

    public static final int ERROR_FOLDER_EXISTS = 51;

    public static final int ERROR_FOLDER_PARENT_NOT_FOUND = 52;

    // change@wtl.jrabina Folder Commands end
    // change@wtl.dtuttle empty_trash/download_remainder start
    // error codes: use 3rd byte
    public static final int ERROR_EMPTYTRASH_RESPONSE_PARSE = 53;

    public static final int ERROR_EMPTYTRASH_FAILURE = 54;

    public static final int ERROR_FETCH_RESPONSE_PARSE = 55;

    public static final int ERROR_FETCH_FAILURE = 56;

    // change@wtl.dtuttle end
    // change@wtl.dtuttle ASYNC00000868 start
    public static final int ERROR_FETCH_OUTOFMEMORY = 57;

    // change@wtl.dtuttle end
    // change@wtl.dtuttle ASYNC00000837 start
    public static final int ERROR_EMPTYTRASH_TIMEOUT = 58;

    // change@wtl.dtuttle end
    // change@wtl.dtuttle CoverityPrevent: null msg start
    public static final int ERROR_FETCH_NULLMSG = 59;

    // change@wtl.dtuttle end
    public static final int EMPTYTRASH_EXCEPTION = 60;

    public static final int ERROR_INVALID_PARAM = 61;

    public static final int ERROR_OOO_NOT_SUPPORTED = 62;

    public static final int ERROR_CONNECTING_SERVICE = 63; // rather,
                                                           // ERROR_POSTING_REQ?

    public static final int FILE_IOERROR = 64;

    public static final int ERROR_FETCHING_MSG_BODY = 65;

    public static final int ERROR_SERVICE_RESPONSE_NULL = 66;

    public static final int ERROR_PROCESS_SERVICE_RESPONSE_SUCCESS = 67;

    public static final int ERROR_SERVICE_RESPONSE_FAILURE = 68;

    public static final int ERROR_MAX_RETRY_ATTEMPTS_REACHED = 69;

    public static final int ERROR_NOT_FOUND_IN_DB = 70;

    public static final int ERROR_LOADING_BODY = 71;

    public static final int ERROR_VIEW_ATTACHMENT = 72;

    /**
     * Special error code to handle POP3/Hotmail account
     */
    public static final int ERROR_SERVER_ERROR_15MIN_RESTRICTION = 73;

    // New Error codes in HoneyComb
    /** An SSL certificate couldn't be validated */
    public static final int CERTIFICATE_VALIDATION_ERROR = 74;

    /** Authentication failed during autodiscover */
    public static final int AUTODISCOVER_AUTHENTICATION_FAILED = 75;

    /** Autodiscover completed with a result (non-error) */
    public static final int AUTODISCOVER_AUTHENTICATION_RESULT = 76;

    /** Ambiguous failure; server error or bad credentials */
    public static final int AUTHENTICATION_FAILED_OR_SERVER_ERROR = 77;

    /** Licensing Failure: Its only for EAS Accounts */
    public static final int LICENSE_FAILURE = 78;

    /** progress check incoming settings */
    public static final int PROGRESS_CHECK_INCOMIMNG = 79;

    /** progress check outgoing settings */
    public static final int PROGRESS_CHECK_OUTGOING = 80;

    /** Success check Settings */
    public static final int SUCCESS_CHECK_SETTINGS = 81;

    /** check settings error */
    public static final int ERROR_CHECK_SETTINGS = 82;

    /** cancel check settings success */
    public static final int SUCCESS_CANCEL_CHECK_SETTINGS = 83;

    /** cancel check settings error */
    public static final int ERROR_CANCEL_CHECK_SETTINGS = 84;

    /** socket read timeout */
    public static final int ERROR_CONNECTION_READ_TIMEOUT = 85;

    /** EAS HTTP Server 5XX Error */
    public static final int ERROR_SERVER_ERROR_HTTP_5XX = 86;

    /** IO Exception sub class errors */
    public static final int ERROR_SERVER_NOT_FOUND = 88;

    public static final int ERROR_SERVER_NOT_RESPONDING = 89;

    public static final int ERROR_NETWORK_NOT_AVAILABLE = 90;

    /** Delete Account Failure Status */
    public static final int ERROR_DELETE_ACCOUNT_FAILURE = 91;

    public static final int ERROR_NETWORK_TRANSIENT_ERROR = 92;

    public static final int ERROR_NO_NETWORK = 93;

    /*
     * Folder update status 2, Folder exist or specified folder is a special
     * folder
     */
    public static final int ERROR_FOLDER_UPDATE_NOT_ALLOWED = 94;

    /** check License Server Settings */
    public static final int PROGRESS_CHECK_LICENSE_SERVER = 95;
    public static final int ERROR_SEND_MSG_UNLOADED_ATTACHMENT = 96;
    public static final int MAX_DEVICE_PARTNERSHIP_EXCEPTION = 97;
    public static final int ERROR_CERTIFICATE_NOT_AVAILABLE = 98;
    public static final int DEVICE_ACCESS_EXCEPTION_BASE = 0x00040000;

    public static final int DEVICE_BLOCKED_EXCEPTION = DEVICE_ACCESS_EXCEPTION_BASE + 1;

    public static final int DEVICE_QURANTINED_EXCEPTION = DEVICE_ACCESS_EXCEPTION_BASE + 2;

    // msc, hanklee, 2010.08.25
    /** Z7 REMOTE EXCEPTION ERROR */
    public static final int REMOTE_EXCEPTION_ERROR_FOR_Z7 = 0x00050000;

    public static final int ERROR_MAX = 0x00100000;

    // 2011.08.02 wsseo : Remove max attachment size restriction (10MB) - start
    // wsseo add : for mapping Exception from
    // EmailServiceStatus.ERROR_LOAD_ATTACHMENT_FILE_TOO_LARGE
    // change@siso.kanchan IRM 14.1 support start
    public static final int IRM_EXCEPTION_BASE = 0x00050000;

    public static final int IRM_EXCEPTION_FEATURE_DISABLED = 0x00050000 + 1;

    public static final int IRM_EXCEPTION_TRANSIENT_ERROR = 0x00050000 + 2;

    public static final int IRM_EXCEPTION_PERMANENT_ERROR = 0x00050000 + 3;

    public static final int IRM_EXCEPTION_INVALID_TEPLATEID = 0x00050000 + 4;

    public static final int IRM_EXCEPTION_OPERATION_NOT_PERMITTED = 0x00050000 + 5;

    public static final int IRM_EXCEPTION_REMOVE_FAIL = 0x00050000 + 6;
    // change@siso.kanchan IRM 14.1 support end
    public static final int ERROR_LOAD_ATTACHMENT_FILE_TOO_LARGE = 99;
    // 2011.08.02 wsseo : Remove max attachment size restriction (10MB) - end

    public static final int ATTACHMENT_DOWNLOAD_CANCEL = 100;

//    public static final int FOLDER_CMD_LIST_ERROR = 101;
//
//    public static final int FOLDER_CMD_RESULT_CREATE_ERROR = 102;
//
//    public static final int FOLDER_CMD_RESULT_DELETE_ERROR = FOLDER_CMD_RESULT_CREATE_ERROR + 1;
//
//    public static final int FOLDER_CMD_RESULT_UPDATE_ERROR = FOLDER_CMD_RESULT_CREATE_ERROR + 2;
//
//    public static final int FOLDER_CMD_RESULT_CONNECTION_ERROR = FOLDER_CMD_RESULT_CREATE_ERROR + 3;
//
//    public static final int FOLDER_CMD_RESULT_ALREADY_EXIST_ERROR = FOLDER_CMD_RESULT_CREATE_ERROR + 4;
//
//    public static final int FOLDER_CMD_RESULT_FOLDER_NOT_EXIST_ERROR = FOLDER_CMD_RESULT_CREATE_ERROR + 5;
//
//    public static final int FOLDER_CMD_RESULT_FOLDER_NAME_INVALID_ERROR = FOLDER_CMD_RESULT_CREATE_ERROR + 6;
//
//    public static final int FOLDER_CMD_RESULT_PERMISSION_ERROR = FOLDER_CMD_RESULT_CREATE_ERROR + 7;
//
//    public static final int FOLDER_CMD_RESULT_MAX = FOLDER_CMD_RESULT_CREATE_ERROR + 8;
    public static final int PARTNER_TOO_MANY = 111; // 0104 KSJ 177 error

    public static final int SIM_ABSENT_ERROR = 112;
    
    public static final int MOVEITEM_EXCEPTION_MOVE_FAIL = 113;
    
    public static final int FOLDER_NAME_ERROR = 114;

    // change@siso.kanchan, SSL handling in autodiscover.start
    public static final int AUTODISCOVER_USER_CANCEL = 116;
    // change@siso.kanchan, SSL handling in autodiscover.end
    public static final int NOT_ENOUGH_MEMORY_TO_SYNC = 117;

    // Error to be used when EmailFeature.isDownloadOnlyViaWifiEnabled is
    // enabled and for attachments that are marked to be downloaded only via Wi-Fi
    public static final int ERROR_WIFI_NOT_CONNECTED = 118;
   
    public static final int LOAD_ATTACHMENT_CANCEL = 119;
    
    public static final int SQL_IO_ERROR = 120;
    
    public static final int ERROR_ITEMOPERATION_RESPONSE_PARSER = 121;
    
    public static final int ERROR_MOVE_OUTOFMEMORY = 122;
    
    public static final int CREDENTIALS_REQUIRED = 123;

    public static final int LOADMORE_CANCEL = 124;
    
    public static final int ERROR_CAC_ERROR = 125;
    
    public static final int CONNECTION_ERROR_PROGRESS = 126;
    
    public static final int ERROR_SEARCH_END_RANGE = 152;
    public static final int ERROR_SEARCH_BAD_CONNECTION_ID = 153;
    public static final int ERROR_SEARCH_CPLX_QUERY = 155;

    public static final int EXCEEDED_MESSAGE_DAILY_SIZE_LIMITS_ERROR = 156;

    public static final int SSL_TLS_CERTIFICATE_VALIDATION_ERROR = 157;
    
    public static final int IRM_DEFAULT_CODE = -1;

    protected int mExceptionType;
    
    public static final String STR_ATTACHMENT_DOWNLOAD_CANCEL = "ATTACHMENT_DOWNLOAD_CANCEL";

    public MessagingException(String message) {
        super(message);
        mExceptionType = UNSPECIFIED_EXCEPTION;
    }

    public MessagingException(String message, Throwable throwable) {
        super(message, throwable);
        mExceptionType = UNSPECIFIED_EXCEPTION;
    }

    public MessagingException(int exceptionType, String message, Throwable throwable) {
        super(message, throwable);
        mExceptionType = exceptionType;
        mExceptionData = null;
    }

    // Exception type-specific data
    protected Object mExceptionData;

    /**
     * Constructs a MessagingException with an exceptionType, a message, and
     * data
     * 
     * @param exceptionType The exception type to set for this exception.
     * @param message the message for the exception (or null)
     * @param data exception-type specific data for the exception (or null)
     */
    public MessagingException(int exceptionType, String message, Object data) {
        super(message);
        mExceptionType = exceptionType;
        mExceptionData = data;
    }

    /**
     * Constructs a MessagingException with an exceptionType and a null message.
     * 
     * @param exceptionType The exception type to set for this exception.
     */
    public MessagingException(int exceptionType) {
        super();
        mExceptionType = exceptionType;
    }

    /**
     * Constructs a MessagingException with an exceptionType and a message.
     * 
     * @param exceptionType The exception type to set for this exception.
     */
    public MessagingException(int exceptionType, String message) {
        super(message);
        mExceptionType = exceptionType;
    }

    // change@wtl.kSingh - isolating feature exception mappings - starts
    public MessagingException(int exceptionType, int exceptionMessage) {
        super(String.valueOf(exceptionMessage));
        mExceptionType = exceptionType;
    }

    // change@wtl.kSingh - isolating feature exception mappings - ends

    /**
     * Return the exception type. Will be OTHER_EXCEPTION if not explicitly set.
     * 
     * @return Returns the exception type.
     */
    public int getExceptionType() {
        return mExceptionType;
    }
    
    public void setExceptionType(int exceptionType) {
    	mExceptionType = exceptionType;
    }

    // adapter_porting
    public String getExceptionString() {
        // FIXME add boundary check. else face crash!
        String errorString = null;
        // SVL Start : J.sb
        // If MessagingException String is empty return null to the caller
        errorString = (null != getMessage()) ? getMessage() : null;
        // SVl End : J.sb
        // ((mExceptionType >= NO_ERROR && mExceptionType <= ERROR_MAX) ?
        // mExceptionString[mExceptionType+1] : "NEW_EXCEPTION!");
        return errorString;
    }

    public static int decodeIOException(IOException ioe) {
        boolean isTransientError = false;

        // EmailLog.dumpException("decodeIOException", ioe);

        if (ioe != null) {
            String msg = ioe.getMessage();

            if (ioe instanceof SocketTimeoutException) {
                return ERROR_SERVER_NOT_RESPONDING;
            } else if (ioe instanceof UnknownHostException) {
                return ERROR_SERVER_NOT_FOUND;
            } else if (ioe instanceof SocketException) {
                // FIXME: java.net.SocketException: Broken pipe is not network
                // not available

                if (msg != null) {
                    if (msg.contains("Broken pipe")) {
                        isTransientError = true;
                    }
                } else {
                    return ERROR_NO_NETWORK;
                }
            } else if (ioe instanceof IOException) {
                // TODO: need better logic

                if (msg != null) {
                    if (msg.contains("Connection already shutdown") || msg.contains("Broken pipe")) {
                        isTransientError = true;
                    } else if (msg.contains(STR_ATTACHMENT_DOWNLOAD_CANCEL)) {
                    	/* ATTACHMENT_DOWNLOAD_CANCEL event is trigged as IOException 
                    	 * from IMAP Store
                    	 */
                    	return UNSPECIFIED_EXCEPTION;
                    }
                }
            }
        }
        if (isTransientError == true) {
            return ERROR_NETWORK_TRANSIENT_ERROR;
        }
        return IOERROR;
    }

    public static boolean isError(int errorCode) {

        switch (errorCode) {
        	case MessagingException.ERROR_NETWORK_TRANSIENT_ERROR:
        		EmailLog.d("TAG", "Transient network error ignored");
            case MessagingException.NO_ERROR:
            case MessagingException.IN_PROGRESS:
            case MessagingException.SUCCESS:
                return false;
        }

        return true;
    }
    
    public boolean isIoError() {
    	switch (mExceptionType) {
    	case IOERROR:
    	case ERROR_NO_NETWORK:
    	case ERROR_SERVER_NOT_FOUND:
    	case ERROR_SERVER_NOT_RESPONDING:
    		return true;
    	}
    	
    	return false;
    }
    

    /**
     * Return the exception data. Will be null if not explicitly set.
     * 
     * @return Returns the exception data.
     */
    public Object getExceptionData() {
        return mExceptionData;
    }
  //MNO-SVL_B2B START: m.hanifa    
    @Override
    public String toString() {
    	return "mExceptionType=" + mExceptionType + " msg=" + super.toString();
    }
  //MNO-SVL_B2B END: m.hanifa
    
    public boolean isTempServerError(String msg) {
        boolean ret = false;
        if (msg != null && (msg.toLowerCase().contains("too many simultaneous connections") ||
                msg.toLowerCase().contains("service is not available"))) {
            ret = true;
        }
        return ret;
    }
    
    public boolean isAccountBlocked(String msg) {
        boolean ret = false;
        if( msg == null )
            return ret;
        if (msg.toLowerCase().contains("account is blocked. login to your account via a web browser to verify your identity"))
            ret = true;
        return ret;
    }
}
