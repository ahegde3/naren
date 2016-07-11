package com.samsung.android.emailcommon;

import com.samsung.android.emailcommon.service.ProxyArgs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;

public class EmailSyncProviderUtils {

    final static public String AUTHORITY = "com.samsung.android.emailsync.provider";
    public final static Uri SYNC_BASE_URI = new Uri.Builder().scheme("content").authority(AUTHORITY).build();

    public final static String SYNC_SYNCHELLO = "synchello";

    public final static String SYNC_CHECKING_URI = "checkinguri";
    public final static String SYNC_CHECKINGSENDER_URI = "checkingsenderuri";
    public final static String SYNC_CHECKINGAUTODISCOVER_URI = "checkingautodiscoveruri";

    public final static String SYNC_DECRYPTSMIME_URI = "decryptsmimeuri";
    public final static String SYNC_DECRYPTPGP_URI = "decryptpgpuri";

    public final static String SYNC_VERIFYMESSAGESMIME_URI = "verifymessagesmimeuri";
    public final static String SYNC_VERIFYMESSAGEPGP_URI = "verifymessagepgpuri";
    public final static String SYNC_FETCHPGPKEYS_URI = "fetchpgpkeys";
    public final static String SYNC_SAVETOFILE_URI = "savetofile";
    public final static String SYNC_IS_EXTRACTABLE_PATH = "isExtractable";
    public final static String SYNC_IS_SIGNINGKEY_PATH = "isSigningKey";
    public final static String SYNC_IS_VALIDITY_PATH = "isValidKey";
    public final static String SYNC_GET_SIGNKEY_PATH = "getSignKey";
    public final static String SYNC_SAVEPASS_PATH = "savePass";
    
    public final static String SYNC_EAS_ACTIVATION = "easActivation";

    public final static String SYNC_DELETEACCOUNTPIMDATA_URI = "deleteaccountpimdata";
    public final static String SYNC_HOSTCHANGED_URI = "hostchanged";
    public final static String SYNC_UPLOADDRAFTMESSAGE_URI = "uploadDraftMessage";
    public final static String SYNC_SENDRECOVERYPASSWORD_URI = "sendRecoveryPassword";
    public final static String SYNC_MOVECONVERSATIONALWAYS_URI = "moveConversationAlways";
    public final static String SYNC_READYTOSEND_URI = "readyToSend";

    public final static String SYNC_CANCELLOADATTACHMENT_URI = "cancelLoadAttachment";
    public final static String SYNC_CANCELAUTODISCOVER_URI = "cancelAutoDiscover";
    public final static String SYNC_SSLCERTVALIDATIONFINISHED_URI = "sslCertValidationFinished";
    public final static String SYNC_STARTPUSH_URI = "startPush";
    public final static String SYNC_STOPPUSH_URI = "stopPush";

    public final static String SYNC_DELETEACCOUNT_URI = "deleteAccount";

    public final static String SYNC_IS_CREDENTIAL_ACCOUNT = "isCredentialAccount";
    public final static String SYNC_CHECK_RECIPIENT_CERT = "checkRecipientCert";
    public final static String SYNC_GET_RECIPIENT_CERT_CNT = "getRecipientCert";

    public final static String SYNC_GET_EMAILADDR_BY_ALIAS = "getEmailAddrByAlias";
    public static final String SYNC_CHECK_CERT_ALIAS = "checkCertAliasExistance";

    public static final String SYNC_REMOVE_CERT = "removeCertByAlias";
    public static final String SYNC_IMPORT_CERT = "importCertByPem";
    
    public static final String SYNC_IMPORT_NEW_CERT = "importNewCertByPem";
    public static final String SYNC_GET_ALL_ALIAS = "getAllAliases";
    public static final String SYNC_DISCONNECT = "disconnect";

    public static final String SYNC_UPDATE_NETOWRK_INFO = "updateNetworkInfo";

    public final static int SYNC_INDEX_SYNCHELLO = 0;
    public final static int SYNC_INDEX_CHECKING = SYNC_INDEX_SYNCHELLO +1;
    public final static int SYNC_INDEX_CHECKINGSENDER = SYNC_INDEX_CHECKING +1;
    public final static int SYNC_INDEX_CHECKINGAUTODISCOVER = SYNC_INDEX_CHECKINGSENDER + 1;
    public final static int SYNC_INDEX_DECRYPTSMIME = SYNC_INDEX_CHECKINGAUTODISCOVER + 1;
    public final static int SYNC_INDEX_DECRYPTPGP = SYNC_INDEX_DECRYPTSMIME + 1;
    public final static int SYNC_INDEX_VERIFYMESSAGESMIME = SYNC_INDEX_DECRYPTPGP + 1;
    public final static int SYNC_INDEX_VERIFYMESSAGEPGP = SYNC_INDEX_VERIFYMESSAGESMIME + 1;
    public final static int SYNC_INDEX_FETCHPGPKEYS = SYNC_INDEX_VERIFYMESSAGEPGP + 1;
    public final static int SYNC_INDEX_SAVETOFILE = SYNC_INDEX_FETCHPGPKEYS + 1;
    public final static int SYNC_INDEX_IS_EXTRATABLE = SYNC_INDEX_SAVETOFILE + 1;
    public final static int SYNC_INDEX_IS_SIGNINGKEY = SYNC_INDEX_IS_EXTRATABLE + 1;
    public final static int SYNC_INDEX_IS_VALIDKEY = SYNC_INDEX_IS_SIGNINGKEY + 1;
    public final static int SYNC_INDEX_GET_SIGNKEY = SYNC_INDEX_IS_VALIDKEY + 1;
    public final static int SYNC_INDEX_SAVE_PASS = SYNC_INDEX_GET_SIGNKEY + 1;


    public final static int SYNC_INDEX_DELETEACCOUNTPIMDATA = SYNC_INDEX_SAVE_PASS + 1;
    public final static int SYNC_INDEX_HOSTCHANGED = SYNC_INDEX_DELETEACCOUNTPIMDATA + 1;
    public final static int SYNC_INDEX_UPLOADDRAFTMESSAGE = SYNC_INDEX_HOSTCHANGED + 1;
    public final static int SYNC_INDEX_SENDRECOVERYPASSWORD = SYNC_INDEX_UPLOADDRAFTMESSAGE + 1;
    public final static int SYNC_INDEX_MOVECONVERSATIONALWAYS = SYNC_INDEX_SENDRECOVERYPASSWORD + 1;
    public final static int SYNC_INDEX_READYTOSEND = SYNC_INDEX_MOVECONVERSATIONALWAYS + 1;

    public final static int SYNC_INDEX_CANCELLOADATTACHMENT = SYNC_INDEX_READYTOSEND + 1;
    public final static int SYNC_INDEX_CANCELAUTODISCOVER = SYNC_INDEX_CANCELLOADATTACHMENT + 1;
    public final static int SYNC_INDEX_SSLCERTVALIDATIONFINISHED = SYNC_INDEX_CANCELAUTODISCOVER + 1;
    public final static int SYNC_INDEX_STARTPUSH = SYNC_INDEX_SSLCERTVALIDATIONFINISHED + 1;
    public final static int SYNC_INDEX_STOPPUSH = SYNC_INDEX_STARTPUSH + 1;

    public final static int SYNC_INDEX_DELETEACCOUNT = SYNC_INDEX_STOPPUSH + 1;

    public final static int SYNC_INDEX_IS_CREDENTIAL_ACCOUNT = SYNC_INDEX_DELETEACCOUNT + 1;
    public final static int SYNC_INDEX_CHECK_RECIPIENT_CERT = SYNC_INDEX_IS_CREDENTIAL_ACCOUNT + 1;
    public final static int SYNC_INDEX_GET_RECIPIENT_CERT_CNT = SYNC_INDEX_CHECK_RECIPIENT_CERT + 1;

    public final static int SYNC_INDEX_GET_EMAILADDR_BY_ALIAS = SYNC_INDEX_GET_RECIPIENT_CERT_CNT + 1;
    public final static int SYNC_INDEX_CHECK_CERT_ALIAS = SYNC_INDEX_GET_EMAILADDR_BY_ALIAS + 1;

    public final static int SYNC_INDEX_REMOVE_CERT = SYNC_INDEX_CHECK_CERT_ALIAS + 1;
    public final static int SYNC_INDEX_IMPORT_CERT = SYNC_INDEX_REMOVE_CERT + 1;
    public final static int SYNC_INDEX_GET_ALL_ALIAS = SYNC_INDEX_IMPORT_CERT + 1;
    
    public final static int SYNC_INDEX_IMPORT_NEW_CERT = SYNC_INDEX_GET_ALL_ALIAS + 1;
    public final static int SYNC_INDEX_DISCONNECT = SYNC_INDEX_IMPORT_NEW_CERT + 1;
    
    public final static int SYNC_INDEX_EAS_ACTIVATION = SYNC_INDEX_DISCONNECT + 1;

    public final static int SYNC_INDEX_UPDATE_NETWORK_INFO = SYNC_INDEX_EAS_ACTIVATION + 1;

    final static public String TYPE_SMIME = "smime";
    final static public String TYPE_PGP = "pgp";


    public static Uri buildUri (String path) {
    	return new Uri.Builder().scheme("content").authority(AUTHORITY).appendEncodedPath(path).build();
    }

    public static int emailSyncProviderDisconnect(Context context, Bundle bundle) {
    	return context.getContentResolver().delete(disconnect(bundle), null, null);
    }
    public static Bundle emailSyncProviderQuery(Context context , Bundle bundle, int num) {

        Cursor cur = null;
        Bundle bundleR = null;

        try {
            cur = context.getContentResolver().query(getQuery(bundle, num),
                    null, null, null, null);
            if(cur != null) {
                bundleR = cur.getExtras();
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            if (cur != null)
                cur.close();
        }

        return bundleR;
    }

    public static Bundle emailSyncProviderQuery(Context context , Uri uri, String [] values) {

        Cursor cur = null;
        Bundle bundleR = null;

        try {
            cur = context.getContentResolver().query(uri,
                    null, null, values, null);
            if(cur != null) {
                bundleR = cur.getExtras();
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            if (cur != null)
                cur.close();
        }
        return bundleR;
    }

    public static int emailSyncProviderUpdate(Context context, Uri uri, ContentValues cv) {
        try {
            return context.getContentResolver().update(uri, cv, null, null);
        } catch (SecurityException se) {
            se.printStackTrace();
            return -1;
        }
    }

    private static Uri getQuery(Bundle bundle, int num) {

        Uri UriR = null;
        switch(num) {
            case EmailSyncProviderUtils.SYNC_INDEX_SYNCHELLO :{
                UriR =  syncHello();
                break;
            }
            case EmailSyncProviderUtils.SYNC_INDEX_CHECKING :{
                UriR =  checkingUri(bundle);
                break;
            }
            case EmailSyncProviderUtils.SYNC_INDEX_CHECKINGSENDER :{
                UriR = checkingSenderUri(bundle);
                break;
            }
            case EmailSyncProviderUtils.SYNC_INDEX_CHECKINGAUTODISCOVER :{
                UriR = checkingAutodiscover(bundle);
                break;
            }
            case EmailSyncProviderUtils.SYNC_INDEX_DECRYPTSMIME :{
                UriR = decryptsmime(bundle);
                break;
            }
            case EmailSyncProviderUtils.SYNC_INDEX_DECRYPTPGP :{
                UriR = decryptpgp(bundle);
                break;
            }
            case EmailSyncProviderUtils.SYNC_INDEX_VERIFYMESSAGESMIME :{
                UriR = verifymessagesmime(bundle);
                break;
            }
            case EmailSyncProviderUtils.SYNC_INDEX_VERIFYMESSAGEPGP :{
                UriR = verifymessagepgp(bundle);
                break;
            }
            case EmailSyncProviderUtils.SYNC_INDEX_FETCHPGPKEYS :{
                UriR = fetchpgpkeys(bundle);
                break;
            }
            case EmailSyncProviderUtils.SYNC_INDEX_SAVETOFILE :{
                UriR = savetofile(bundle);
                break;
            }
            case EmailSyncProviderUtils.SYNC_INDEX_DELETEACCOUNTPIMDATA :{
                UriR = deleteaccountpimdata(bundle);
                break;
            }
            case EmailSyncProviderUtils.SYNC_INDEX_HOSTCHANGED :{
                UriR = hostchanged(bundle);
                break;
            }
            case EmailSyncProviderUtils.SYNC_INDEX_UPLOADDRAFTMESSAGE :{
                UriR = uploaddraftmessage(bundle);
                break;
            }
            case EmailSyncProviderUtils.SYNC_INDEX_SENDRECOVERYPASSWORD :{
                UriR = sendRecoveryPassword();
                break;
            }
            case EmailSyncProviderUtils.SYNC_INDEX_MOVECONVERSATIONALWAYS :{
                UriR = moveConversationAlways(bundle);
                break;
            }
            case EmailSyncProviderUtils.SYNC_INDEX_READYTOSEND :{
                UriR = readyToSend();
                break;
            }
            case EmailSyncProviderUtils.SYNC_INDEX_CANCELLOADATTACHMENT :{
                UriR = cancelLoadAttachment(bundle);
                break;
            }
            case EmailSyncProviderUtils.SYNC_INDEX_CANCELAUTODISCOVER :{
                UriR = cancelAutoDiscover(bundle);
                break;
            }
            case EmailSyncProviderUtils.SYNC_INDEX_SSLCERTVALIDATIONFINISHED :{
                UriR = sslCertValidationFinished(bundle);
                break;
            }
            case EmailSyncProviderUtils.SYNC_INDEX_STARTPUSH :{
                UriR = startPush();
                break;
            }
            case EmailSyncProviderUtils.SYNC_INDEX_STOPPUSH :{
                UriR = stopPush();
                break;
            }
            case EmailSyncProviderUtils.SYNC_INDEX_DELETEACCOUNT :{
                UriR = deleteAccount(bundle);
                break;
            }
            case EmailSyncProviderUtils.SYNC_INDEX_UPDATE_NETWORK_INFO: {
                UriR = updateNetworkInfo();
                break;
            }
            default :
                break;
        }
        return UriR;
    }

    private static Uri syncHello() {
        return EmailSyncProviderUtils.SYNC_BASE_URI.buildUpon()
                .appendPath(EmailSyncProviderUtils.SYNC_SYNCHELLO)
                .build();
    }
    
    private static Uri checkingUri(Bundle bundle) {
        String tmpURI = bundle.getString(ProxyArgs.ARG_URL);
        return EmailSyncProviderUtils.SYNC_BASE_URI.buildUpon()
                .appendPath(EmailSyncProviderUtils.SYNC_CHECKING_URI)
                .appendPath(tmpURI)
                .build();
    }

    private static Uri checkingSenderUri(Bundle bundle) {
        String senderURI = bundle.getString(ProxyArgs.ARG_URL);
        return EmailSyncProviderUtils.SYNC_BASE_URI.buildUpon()
                .appendPath(EmailSyncProviderUtils.SYNC_CHECKINGSENDER_URI)
                .appendPath(senderURI)
                .build();
    }

    private static Uri checkingAutodiscover(Bundle bundle) {

        String Uri = bundle.getString(ProxyArgs.ARG_URL);
        String userName = bundle.getString(ProxyArgs.ARG_USERNAME);
        String password = bundle.getString(ProxyArgs.ARG_PASSWORD);
        String domain = bundle.getString(ProxyArgs.ARG_DOMAIN);
        Boolean bTrustCert = bundle.getBoolean(ProxyArgs.ARG_TRUST_CERTS);

        return EmailSyncProviderUtils.SYNC_BASE_URI.buildUpon()
                .appendPath(EmailSyncProviderUtils.SYNC_CHECKINGAUTODISCOVER_URI)
                .appendPath(Uri)
                .appendPath(userName)
                .appendPath(password)
                .appendPath(domain)
                .appendPath(Boolean.toString(bTrustCert))
                .build();
    }

    private static Uri decryptsmime(Bundle bundle) {

        String enc_type = bundle.getString(ProxyArgs.ARG_ENC_TYPE);
        long messageId = bundle.getLong(ProxyArgs.ARG_MESSAGE_ID);

        return EmailSyncProviderUtils.SYNC_BASE_URI.buildUpon()
                .appendPath(EmailSyncProviderUtils.SYNC_DECRYPTSMIME_URI)
                .appendPath(enc_type)
                .appendPath(Long.toString(messageId))
                .build();
    }


    private static Uri decryptpgp(Bundle bundle) {

        String enc_type = bundle.getString(ProxyArgs.ARG_ENC_TYPE);
        long messageId = bundle.getLong(ProxyArgs.ARG_MESSAGE_ID);
        long keyId = bundle.getLong(ProxyArgs.ARG_PGP_KEY);
        String password = bundle.getString(ProxyArgs.ARG_PASSWORD);

        return EmailSyncProviderUtils.SYNC_BASE_URI.buildUpon()
                .appendPath(EmailSyncProviderUtils.SYNC_DECRYPTPGP_URI)
                .appendPath(enc_type)
                .appendPath(Long.toString(messageId))
                .appendPath(Long.toString(keyId))
                .appendPath(password)
                .build();
    }


    private static Uri verifymessagesmime(Bundle bundle) {

        String enc_type = bundle.getString(ProxyArgs.ARG_ENC_TYPE);
        long messageId = bundle.getLong(ProxyArgs.ARG_MESSAGE_ID);

        return EmailSyncProviderUtils.SYNC_BASE_URI.buildUpon()
                .appendPath(EmailSyncProviderUtils.SYNC_VERIFYMESSAGESMIME_URI)
                .appendPath(enc_type)
                .appendPath(Long.toString(messageId))
                .build();
    }


    private static Uri verifymessagepgp(Bundle bundle) {

        String enc_type = bundle.getString(ProxyArgs.ARG_ENC_TYPE);
        long messageId = bundle.getLong(ProxyArgs.ARG_MESSAGE_ID);

        return EmailSyncProviderUtils.SYNC_BASE_URI.buildUpon()
                .appendPath(EmailSyncProviderUtils.SYNC_VERIFYMESSAGEPGP_URI)
                .appendPath(enc_type)
                .appendPath(Long.toString(messageId))
                .build();
    }
    private static Uri fetchpgpkeys(Bundle bundle) {

        long messageId = bundle.getLong(ProxyArgs.ARG_MESSAGE_ID);

        return EmailSyncProviderUtils.SYNC_BASE_URI.buildUpon()
                .appendPath(EmailSyncProviderUtils.SYNC_FETCHPGPKEYS_URI)
                .appendPath(Long.toString(messageId))
                .build();
    }
    private static Uri savetofile(Bundle bundle) {
        String fileName = bundle.getString(ProxyArgs.ARG_TEXT);
        long messageId = bundle.getLong(ProxyArgs.ARG_MESSAGE_ID);

        return EmailSyncProviderUtils.SYNC_BASE_URI.buildUpon()
                .appendPath(EmailSyncProviderUtils.SYNC_SAVETOFILE_URI)
                .appendPath(fileName)
                .appendPath(Long.toString(messageId))
                .build();
    }
    private static Uri deleteaccountpimdata(Bundle bundle) {
        long accountId = bundle.getLong(ProxyArgs.ARG_ACCOUNT_ID);
        return EmailSyncProviderUtils.SYNC_BASE_URI.buildUpon()
                .appendPath(EmailSyncProviderUtils.SYNC_DELETEACCOUNTPIMDATA_URI)
                .appendPath(Long.toString(accountId))
                .build();
    }
    private static Uri hostchanged(Bundle bundle) {
        long accountId = bundle.getLong(ProxyArgs.ARG_ACCOUNT_ID);
        return EmailSyncProviderUtils.SYNC_BASE_URI.buildUpon()
                .appendPath(EmailSyncProviderUtils.SYNC_HOSTCHANGED_URI)
                .appendPath(Long.toString(accountId))
                .build();
    }
    private static Uri uploaddraftmessage(Bundle bundle) {
        long accountId = bundle.getLong(ProxyArgs.ARG_ACCOUNT_ID);
        return EmailSyncProviderUtils.SYNC_BASE_URI.buildUpon()
                .appendPath(EmailSyncProviderUtils.SYNC_UPLOADDRAFTMESSAGE_URI)
                .appendPath(Long.toString(accountId))
                .build();
    }
    private static Uri sendRecoveryPassword() {
        return EmailSyncProviderUtils.SYNC_BASE_URI.buildUpon()
                .appendPath(EmailSyncProviderUtils.SYNC_SENDRECOVERYPASSWORD_URI)
                .build();
    }

    private static Uri moveConversationAlways(Bundle bundle) {
        long mailboxId = bundle.getLong(ProxyArgs.ARG_MAILBOX_ID);
        long toMailboxId = bundle.getLong(ProxyArgs.ARG_MAILBOX_ID_TO);
        byte[] conversationId = bundle.getByteArray(ProxyArgs.ARG_CONV_ID);
        int ignore = bundle.getInt(ProxyArgs.ARG_IGNORE);

        String conversationId_Str=null;
        if(conversationId != null)
            Base64.encodeToString(conversationId, Base64.DEFAULT);

        return EmailSyncProviderUtils.SYNC_BASE_URI.buildUpon()
                .appendPath(EmailSyncProviderUtils.SYNC_MOVECONVERSATIONALWAYS_URI)
                .appendPath(Long.toString(mailboxId))
                .appendPath(Long.toString(toMailboxId))
                .appendPath(conversationId_Str)
                .appendPath(Integer.toString(ignore))
                .build();
    }

    private static Uri readyToSend() {
        return EmailSyncProviderUtils.SYNC_BASE_URI.buildUpon()
                .appendPath(EmailSyncProviderUtils.SYNC_READYTOSEND_URI)
                .build();
    }

    private static Uri cancelLoadAttachment(Bundle bundle) {
        long attachmentId = bundle.getLong(ProxyArgs.ARG_ATTACHMENT_ID);
        return EmailSyncProviderUtils.SYNC_BASE_URI.buildUpon()
                .appendPath(EmailSyncProviderUtils.SYNC_CANCELLOADATTACHMENT_URI)
                .appendPath(Long.toString(attachmentId))
                .build();
    }

    private static Uri cancelAutoDiscover(Bundle bundle) {
        String username = bundle.getString(ProxyArgs.ARG_USERNAME);
        return EmailSyncProviderUtils.SYNC_BASE_URI.buildUpon()
                .appendPath(EmailSyncProviderUtils.SYNC_CANCELAUTODISCOVER_URI)
                .appendPath(username)
                .build();
    }

    private static Uri sslCertValidationFinished(Bundle bundle) {
        String username = bundle.getString(ProxyArgs.ARG_USERNAME);
        String url = bundle.getString(ProxyArgs.ARG_URL);
        int message = bundle.getInt(ProxyArgs.ARG_RESULT);
        return EmailSyncProviderUtils.SYNC_BASE_URI.buildUpon()
                .appendPath(EmailSyncProviderUtils.SYNC_SSLCERTVALIDATIONFINISHED_URI)
                .appendPath(username)
                .appendPath(url)
                .appendPath(Integer.toString(message))
                .build();
    }

    private static Uri startPush() {
        return EmailSyncProviderUtils.SYNC_BASE_URI.buildUpon()
                .appendPath(EmailSyncProviderUtils.SYNC_STARTPUSH_URI)
                .build();
    }
    private static Uri stopPush() {
        return EmailSyncProviderUtils.SYNC_BASE_URI.buildUpon()
                .appendPath(EmailSyncProviderUtils.SYNC_STOPPUSH_URI)
                .build();
    }

    private static Uri deleteAccount(Bundle bundle) {
        long accountId = bundle.getLong(ProxyArgs.ARG_ACCOUNT_ID);
        return EmailSyncProviderUtils.SYNC_BASE_URI.buildUpon()
                .appendPath(EmailSyncProviderUtils.SYNC_DELETEACCOUNT_URI)
                .appendPath(Long.toString(accountId))
                .build();
    }
    
    private static Uri disconnect(Bundle bundle) {
    	long accountId = bundle.getLong(ProxyArgs.ARG_ACCOUNT_ID);
        return EmailSyncProviderUtils.SYNC_BASE_URI.buildUpon()
        		.appendEncodedPath(EmailSyncProviderUtils.SYNC_DISCONNECT)
                .appendPath(Long.toString(accountId))
                .build();
    }

    private static Uri updateNetworkInfo() {
        return EmailSyncProviderUtils.SYNC_BASE_URI.buildUpon()
                .appendPath(EmailSyncProviderUtils.SYNC_UPDATE_NETOWRK_INFO)
                .build();
    }
}
