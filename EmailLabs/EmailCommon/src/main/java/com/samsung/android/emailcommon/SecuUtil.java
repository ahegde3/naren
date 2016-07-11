package com.samsung.android.emailcommon;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import org.apache.commons.io.IOUtils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.sec.enterprise.content.SecContentProviderURI;
import android.text.TextUtils;

import com.samsung.android.emailcommon.mail.Address;
import com.samsung.android.emailcommon.provider.EmailContent;
import com.samsung.android.emailcommon.provider.EmailContent.Attachment;
import com.samsung.android.emailcommon.provider.EmailContent.Body;
import com.samsung.android.emailcommon.provider.EmailContent.Message;
import com.samsung.android.emailcommon.security.PublicKey;
import com.samsung.android.emailcommon.security.PublicKey.PublicKeys;
import com.samsung.android.emailcommon.service.ProxyArgs;
import com.samsung.android.emailcommon.utility.EmailLog;
import com.samsung.android.emailcommon.utility.Log;
import com.samsung.android.emailksproxy.EmailKeystoreManager;
import com.sec.enterprise.knox.EnterpriseKnoxManager;
import com.sec.enterprise.knox.ccm.ClientCertificateManager;

public class SecuUtil implements ProxyArgs {

    final static private String TAG = "SecuUtil";
    private static final String UCM_ALIAS_SCHEME = "ucmkeychain";

    Context mContext;
    public SecuUtil(Context context) {
        mContext = context;
    }

    public static class DecryptInfo {
    	public long originalId;
        public long decryptedId;
    	public String msg;
    	public int code;
    	public Message handledMsg;
    }

    public static DecryptInfo decryptSMIME(Context context, Message message) {

        Bundle b = new Bundle();
        b.putString(ProxyArgs.ARG_ENC_TYPE, "smime");
        b.putLong(ProxyArgs.ARG_MESSAGE_ID, message.mId);

        Bundle args = EmailSyncProviderUtils.emailSyncProviderQuery(context, b,
                EmailSyncProviderUtils.SYNC_INDEX_DECRYPTSMIME);

        DecryptInfo info = new DecryptInfo();
        info.code = args.getInt(ProxyArgs.ARG_STATUS_CODE);
        info.originalId = args.getLong(ProxyArgs.ARG_MESSAGE_ID);

        info.msg = args.getString(ProxyArgs.ARG_TEXT);
        info.decryptedId = args.getLong(ProxyArgs.ARG_MESSAGE_ID_DRAFT);
        info.handledMsg = getMessage(context, message, info.decryptedId);
        info.handledMsg.mOpaqueSigned = args.getBoolean(ProxyArgs.ARG_OPAQUE_SIGNED);
        return info;
    }

    public static DecryptInfo decryptPGP(Context context, Message message, long keyId, String password) {

        Bundle b = new Bundle();
        b.putString(ProxyArgs.ARG_ENC_TYPE, "pgp");
        b.putLong(ProxyArgs.ARG_MESSAGE_ID, message.mId);
        b.putLong(ProxyArgs.ARG_PGP_KEY, keyId);
        b.putString(ProxyArgs.ARG_PASSWORD, password);

        Bundle args = EmailSyncProviderUtils.emailSyncProviderQuery(context, b,
                EmailSyncProviderUtils.SYNC_INDEX_DECRYPTPGP);

        DecryptInfo info = new DecryptInfo();
        info.code = args.getInt(ProxyArgs.ARG_STATUS_CODE);
        info.originalId = args.getLong(ProxyArgs.ARG_MESSAGE_ID);

        info.msg = args.getString(ProxyArgs.ARG_TEXT);
        info.handledMsg = getMessage(context, message, args.getLong(ProxyArgs.ARG_MESSAGE_ID_DRAFT));


        return info;
    }

    public static class VerifyInfo {
    	public long originalId;
    	public String msg;
    	public int code;
    	public Message handledMsg;
    }

    public static VerifyInfo verifySMIME(Context context, Message message) {

        Bundle b = new Bundle();
        b.putLong(ProxyArgs.ARG_MESSAGE_ID, message.mId);
        b.putString(ProxyArgs.ARG_ENC_TYPE, "smime");

        Bundle args = EmailSyncProviderUtils.emailSyncProviderQuery(context, b,
                EmailSyncProviderUtils.SYNC_INDEX_VERIFYMESSAGESMIME);

        VerifyInfo info = new VerifyInfo();
        info.code = args.getInt(ProxyArgs.ARG_STATUS_CODE);
        info.originalId = args.getLong(ProxyArgs.ARG_MESSAGE_ID);
        info.msg = args.getString(ProxyArgs.ARG_TEXT);
        info.handledMsg = getMessage(context, message, args.getLong(ProxyArgs.ARG_MESSAGE_ID_DRAFT));

        return info;
    }

    public static VerifyInfo verifyPGP(Context context, Message message) {

        Bundle b = new Bundle();
        b.putLong(ProxyArgs.ARG_MESSAGE_ID, message.mId);
        b.putString(ProxyArgs.ARG_ENC_TYPE, "pgp");

        Bundle args = EmailSyncProviderUtils.emailSyncProviderQuery(context, b,
                EmailSyncProviderUtils.SYNC_INDEX_VERIFYMESSAGEPGP);

        VerifyInfo info = new VerifyInfo();
        info.code = args.getInt(ProxyArgs.ARG_STATUS_CODE);
        info.originalId = args.getLong(ProxyArgs.ARG_MESSAGE_ID);
        info.msg = args.getString(ProxyArgs.ARG_TEXT);
        info.handledMsg = getMessage(context, message, args.getLong(ProxyArgs.ARG_MESSAGE_ID_DRAFT));
        return info;
    }

    public static Vector<Long> fetchPGPKeys(Context context, Message message) {
        Bundle b = new Bundle();
        b.putLong(ProxyArgs.ARG_MESSAGE_ID, message.mId);

        Bundle ret = EmailSyncProviderUtils.emailSyncProviderQuery(context, b,
                EmailSyncProviderUtils.SYNC_INDEX_FETCHPGPKEYS);

        long[] keys = ret.getLongArray(ProxyArgs.ARG_PGP_KEY);
        Vector<Long> vKey = new Vector<Long>();
        for (long key : keys) {
            vKey.add(key);
        }

        return vKey;
    }

    public static Vector<Long> getValidSigningKeyIds(Context context, String emailId) {
        String [] args = new String[1];
        args[0] = emailId;
        Uri uri = EmailSyncProviderUtils.buildUri(EmailSyncProviderUtils.SYNC_GET_SIGNKEY_PATH);
        Bundle ret = EmailSyncProviderUtils.emailSyncProviderQuery(context, uri,
        		args);

        if(ret == null) {
        	return null;
        }

        long[] keys = ret.getLongArray(ProxyArgs.ARG_PGP_KEY_ID_ARRAY);
        Vector<Long> vKey = new Vector<Long>();
        for (long key : keys) {
            vKey.add(key);
        }

        return vKey;
    }

    public static Bundle saveToFile(Context context, Message message, String fileName) {

        Bundle b = new Bundle();
        b.putLong(ProxyArgs.ARG_MESSAGE_ID, message.mId);
        b.putString(ProxyArgs.ARG_TEXT, fileName);

        return EmailSyncProviderUtils.emailSyncProviderQuery(context, b,
                EmailSyncProviderUtils.SYNC_INDEX_SAVETOFILE);
    }

    public static File getTempAttachmentFile(Context ctx, long attId) throws FileNotFoundException {
        Attachment _att = Attachment.restoreAttachmentWithId(ctx, attId);
        return getTempAttachmentFile(ctx, _att);
    }

    public static File getTempAttachmentFile(Context ctx, Attachment att)
            throws FileNotFoundException {

        File _f = null;


        if (att == null) return _f;

        try {
            _f = File.createTempFile("att_" + att.mId + "_", "");
            try(InputStream in = ctx.getContentResolver().openInputStream(Uri.parse(att.mContentUri));
                    OutputStream out = new FileOutputStream(_f)){
                IOUtils.copy(in, out);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return _f;
    }

    public static String getHexKeyID(long keyId) {
        String hexKeyId = Long.toHexString(keyId);

        if (null != hexKeyId) {
            String subStr = hexKeyId.substring(hexKeyId.length() - 8);
            return subStr.toUpperCase();
        }

        return null;
    }

    public static boolean isExtractableToPrivateKey(Context context, long keyId, String pass) {
    	Uri uri = EmailSyncProviderUtils.buildUri(EmailSyncProviderUtils.SYNC_IS_EXTRACTABLE_PATH);
    	String [] args = new String[2];
    	args[0] = String.valueOf(keyId);
    	args[1] = pass;
    	return EmailSyncProviderUtils.emailSyncProviderQuery(context, uri, args) != null;
    }

    public static boolean isSigningKey(Context context, long keyId) {
    	Uri uri = EmailSyncProviderUtils.buildUri(EmailSyncProviderUtils.SYNC_IS_SIGNINGKEY_PATH);
    	String [] args = new String[1];
    	args[0] = String.valueOf(keyId);
    	return EmailSyncProviderUtils.emailSyncProviderQuery(context, uri, args) != null;
    }

  /*  public static int getKeyValidityWithKeyId(Context context, long keyId, boolean isPublic) {
    	Uri uri = EmailSyncProviderUtils.buildUri(EmailSyncProviderUtils.SYNC_IS_VALIDITY_PATH);
    	String [] args = new String[2];
    	args[0] = String.valueOf(keyId);
    	args[1] = String.valueOf(isPublic);
    	Bundle b = EmailSyncProviderUtils.emailSyncProviderQuery(context, uri, args);
    	return b.getInt(ProxyArgs.ARG_PGP_KEY_VALID);
    }*/
    public static int savePassPharsebyKeyId(Context context, String pass, long keyId, String storePass) {
    	Uri uri = EmailSyncProviderUtils.buildUri(EmailSyncProviderUtils.SYNC_SAVEPASS_PATH);
    	ContentValues cv = new ContentValues();
    	cv.put(ProxyArgs.ARG_PASSWORD, pass);
    	cv.put(ProxyArgs.ARG_PGP_KEY_ID, keyId);
    	cv.put(ProxyArgs.ARG_KEY_STORE_PASS, storePass);
    	return EmailSyncProviderUtils.emailSyncProviderUpdate(context, uri, cv);
    }

    public static final Uri PUBLIC_KEY_URI = PublicKey.PublicKeys.CONTENT_URI;

    public static long getDefaultPGPKeyIdByEmailId(Context context, String emailId) {
        long defaultKeyId = -1;
        Cursor cursor = null;
        String[] projection = {
            PublicKeys.KEY_ID
        };
        String selection = PublicKeys.EMAIL_ID + "= ? AND " + PublicKeys.IS_DEFAULT_KEY + "='1'";

        try {
            cursor = context.getContentResolver().query(PUBLIC_KEY_URI, projection, selection,
                    new String[] {
                        emailId
                    }, null);

            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        if (cursor.getCount() > 0) {
                            // int col_ind =
                            // cursor.getColumnIndex(PublicKeys.KEY_ID);
                            defaultKeyId = cursor.getLong(0);
                        } else {
                            defaultKeyId = -1;
                        }
                    } else {
                        EmailLog.e(TAG, "Cursor is empty.");
                    }
                } catch (NullPointerException ne) { // Memory Full Exception
                    ne.printStackTrace();
                } finally {
                    if (cursor != null)
                        cursor.close();
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            EmailLog.e(TAG, "RuntimeException in getDefaultKeyIdByEmailId", e);
            return -1;
        }

        return defaultKeyId;
    }

    private static Message getMessage(Context context, Message origMessage, long messageId) {
        Message dmsg = Message.restoreMessageWithId(context, messageId);

        if (messageId == -1 || dmsg == null) {
            return origMessage;
        }

        dmsg.mAccountKey = origMessage.mAccountKey;
        dmsg.mMailboxKey = origMessage.mMailboxKey;
        dmsg.mId = origMessage.mId;
        dmsg.mProcessed = !(origMessage.mSigned && !origMessage.mEncrypted);
        dmsg.mOpaqueSigned = origMessage.mOpaqueSigned;
        Body body = Body.restoreBodyWithMessageId(context, messageId);

        if (body != null) {
            dmsg.mHtml = body.mHtmlContent;
            dmsg.mText = body.mTextContent;
        }

        Attachment attachments[] = Attachment.restoreAttachmentsWithMessageId(context, messageId);

        dmsg.mAttachments = new ArrayList<Attachment>();

        for (Attachment att : attachments) {
            dmsg.mAttachments.add(att);
        }
        return dmsg;
    }


    public static boolean isCredentialAccount(Context context, String address) {
        if (TextUtils.isEmpty(address))
            return false;
        Uri uri = EmailSyncProviderUtils.buildUri(EmailSyncProviderUtils.SYNC_IS_CREDENTIAL_ACCOUNT);
        String [] args = new String[1];
        args[0] = address;
        return EmailSyncProviderUtils.emailSyncProviderQuery(context, uri, args) != null;
    }

    public static void readyToSend(Context context, String address) {
        if (context == null || TextUtils.isEmpty(address))
            return;

        Bundle b = new Bundle();
        EmailSyncProviderUtils.emailSyncProviderQuery(context, b,
                EmailSyncProviderUtils.SYNC_INDEX_READYTOSEND);
    }

    public static final int RESOLVE_RECIPIENT_ERROR_BY_POLICY = -2;

    public static final int RESOLVE_RECIPIENT_ERROR_BY_EXCEPTION = -1;

    public static final int CHECK_VALIDITY_RESULT_SUCCESS = 0;

    public static final int CHECK_VALIDITY_RESULT_FAILED = 1;

    public static int getCertificateCnt(Context context, long accountId, Address[] addresses) {

        EmailLog.d(TAG, "getCertificateCnt start");

        Uri uri = EmailSyncProviderUtils
                .buildUri(EmailSyncProviderUtils.SYNC_GET_RECIPIENT_CERT_CNT);
        String[] args = new String[addresses.length + 1];

        EmailLog.d(TAG, "getCertificateCnt accountId = " + accountId);

        args[0] = Long.toString(accountId);

        for (int i = 0; i < addresses.length; i++) {
            if (addresses[i] == null) {
                args[i + 1] = null;
            }
            args[i + 1] = addresses[i].getAddress();
            EmailLog.d(TAG, "getCertificateCnt addr[" + i + "] = " + args[i + 1]);
        }

        Bundle ret = EmailSyncProviderUtils.emailSyncProviderQuery(context, uri, args);
        int retCnt = 0;

        if (ret != null)
            retCnt = ret.getInt(ProxyArgs.ARG_CERT_KEY_CNT);

        EmailLog.e(TAG, "getCertificateCnt retCnt = " + retCnt);
        return retCnt;
    }

    public static HashMap<String, String> getValidateMessage(Context context, long accountId,
            Address[] addresses) {

        EmailLog.d(TAG, "getValidateMessage start");
        HashMap<String, String> noValidCertListWithErrorMsg = new HashMap<String, String>();

        Uri uri = EmailSyncProviderUtils.buildUri(EmailSyncProviderUtils.SYNC_CHECK_RECIPIENT_CERT);
        String[] args = new String[addresses.length + 1];

        EmailLog.d(TAG, "getValidateMessage accountId = " + accountId);

        args[0] = Long.toString(accountId);

        for (int i = 0; i < addresses.length; i++) {
            if (addresses[i] == null) {
                args[i + 1] = null;
            }
            args[i + 1] = addresses[i].getAddress();
            EmailLog.d(TAG, "getValidateMessage addr[" + i + "] = " + args[i + 1]);
        }

        Bundle ret = EmailSyncProviderUtils.emailSyncProviderQuery(context, uri, args);

        ArrayList<String> addr = null;
        ArrayList<String> msg = null;
        int result = CHECK_VALIDITY_RESULT_FAILED;
        if (ret != null) {
            addr = ret.getStringArrayList(ProxyArgs.ARG_CERT_KEY_ADDR);
            msg = ret.getStringArrayList(ProxyArgs.ARG_CERT_KEY_MSG);
            result = ret.getInt(ProxyArgs.ARG_CERT_KEY_STAT);
        }

        EmailLog.e(TAG,
                "getValidateMessage got status as = "
                        + ((result == 0) ? "CHECK_VALIDITY_RESULT_SUCCESS"
                                : "CHECK_VALIDITY_RESULT_FAILED"));

        if (result == CHECK_VALIDITY_RESULT_SUCCESS) {
            return new HashMap<String, String>();
        }

        if (addr == null || msg == null) {
            EmailLog.e(TAG, "getValidateMessage returns null");
            return null;
        }

        if (addr.size() != msg.size()) {
            EmailLog.e(TAG, "getValidateMessage returns null");
            return null;
        }

        for (int i = 0; i < addr.size(); i++) {
            noValidCertListWithErrorMsg.put(addr.get(i), msg.get(i));
        }

        EmailLog.e(TAG, "getValidateMessage ret == CHECK_VALIDITY_RESULT_FAILED with error messages");
        return noValidCertListWithErrorMsg;
    }

    public static String getMailAddrByAlias(Context context, String alias) {

        EmailLog.d(TAG, "getMailAddrByAlias start");

        Uri uri = EmailSyncProviderUtils
                .buildUri(EmailSyncProviderUtils.SYNC_GET_EMAILADDR_BY_ALIAS);

        String [] args = new String[1];
        args[0] = alias;
        Bundle ret = EmailSyncProviderUtils.emailSyncProviderQuery(context, uri, args);
        String addr = ret.getString(ProxyArgs.ARG_CERT_MAIL_ADDR);

        return addr;
    }

    public static boolean[] checkCertificatesExist(Context context, String[] aliases) {

        EmailLog.e(TAG, "checkCertificatesExist start");
        boolean retVal[] = null;

        Uri uri = EmailSyncProviderUtils
                .buildUri(EmailSyncProviderUtils.SYNC_CHECK_CERT_ALIAS);

        Bundle ret = EmailSyncProviderUtils.emailSyncProviderQuery(context, uri, aliases);

        if (ret != null) {
            retVal = ret.getBooleanArray(ProxyArgs.ARG_CHECK_ALIAS_RESULT);
        }
        return retVal;
    }

    public static int removeCertificates(Context context, String[] aliases) {

        EmailLog.e(TAG, "checkCertificatesExist start");

        Uri uri = EmailSyncProviderUtils
                .buildUri(EmailSyncProviderUtils.SYNC_REMOVE_CERT);

        Bundle ret = EmailSyncProviderUtils.emailSyncProviderQuery(context, uri, aliases);

        int retVal = ret.getInt(ProxyArgs.ARG_CERT_REMOVED_CNT);
        return retVal;
    }


    public static Bundle importCertificate(Context context, String filePath, String certPw) {

        EmailLog.e(TAG, "importCertificate start");
        String[] args = {filePath, certPw};

        Uri uri = EmailSyncProviderUtils.buildUri(EmailSyncProviderUtils.SYNC_IMPORT_NEW_CERT);

        return EmailSyncProviderUtils.emailSyncProviderQuery(context, uri, args);
    }


    public static Bundle importCertificate(Context context, byte[] data, String certPw,
            String oldAlias, String newAlias) {

        EmailLog.e(TAG, "importCertificate start");
        String encodedData = new String(android.util.Base64.encode(data,
                android.util.Base64.NO_WRAP));
        String[] args = {
                encodedData, certPw, oldAlias, newAlias
        };

        Uri uri = EmailSyncProviderUtils.buildUri(EmailSyncProviderUtils.SYNC_IMPORT_CERT);
        //String retVal = ret.getString(ProxyArgs.ARG_CERT_IMPORTED_ALIAS);
        return EmailSyncProviderUtils.emailSyncProviderQuery(context, uri, args);
    }

    public static Bundle importCertificate(Context context, String path, String certPw,
            String oldAlias, String newAlias) {

        EmailLog.d(TAG, "importCertificate start with path = " + path);

        if (path.equals("emailPrivateKeyStore")) {
            String[] args = {
                    null, certPw, oldAlias, newAlias
            };

            Uri uri = EmailSyncProviderUtils.buildUri(EmailSyncProviderUtils.SYNC_IMPORT_CERT);
            // String retVal = ret.getString(ProxyArgs.ARG_CERT_IMPORTED_ALIAS);
            return EmailSyncProviderUtils.emailSyncProviderQuery(context, uri, args);
        }

        byte[] byteData = null;
        FileInputStream input = null;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            input = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (input == null)
            return null;

        try {
            try {
                IOUtils.copy(input, output);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(output);
            }
            byteData = output.toByteArray();
        } finally {
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(input);
        }

        return importCertificate(context, byteData, certPw, oldAlias, newAlias);
    }

    public static boolean isEmailKeystoreExists(Context context) {
        InputStream is = null;
        KeyStore keyStore;
        try {
            keyStore = KeyStore.getInstance("PKCS12");
            is = context.getContentResolver().openInputStream(EmailContent.CONTENT_URI);

            String pw = Device.getDeviceId(context);
            if (pw == null) {
                Log.d(TAG, " error while getting deviceId in isEmailKeystoreExists()");
                return false;
            }

            keyStore.load(is, pw.toCharArray());
        } catch (Exception e) {
            Log.d(TAG, " error while loading certificate");
            Log.dumpException(TAG, e);
            return false;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
            }
        }
        try {
            Enumeration<String> e = keyStore.aliases();
            if (!e.hasMoreElements()) {
                Log.d(TAG, "Empty Keystore!!!");
                return false;
            }
            while (e.hasMoreElements()) {
                Log.d(TAG, "alias: " + e.nextElement());
            }
        } catch (Exception e) {
        }
        return true;
    }

    public static Bundle getAliases(Context context) {
        EmailLog.e(TAG, "getAliases start");
        Uri uri = EmailSyncProviderUtils.buildUri(EmailSyncProviderUtils.SYNC_GET_ALL_ALIAS);
        return EmailSyncProviderUtils.emailSyncProviderQuery(context, uri, null);
    }

    public static boolean isCCMEnabled(Context context) {
        ClientCertificateManager clientCertMgr = EnterpriseKnoxManager.getInstance()
                .getClientCertificateManagerPolicy(context);
        if (clientCertMgr != null && clientCertMgr.getCCMVersion() != null) {
            return clientCertMgr.isCCMPolicyEnabledForPackage(EmailPackage.PKG_PROVIDER)
                    /*|| clientCertMgr.isCCMPolicyEnabledForPackage(EmailPackage.PKG_SYNC)*/;
        }
        return false;
    }

    //SRIB@abhilasha.hv change for MDM SMIME cert installation starts
    public static boolean checkCertificatesForInstall(Context context) {
        Intent intent = new Intent();
        intent.setAction(IntentConst.ACTION_INSTALL_MDM_CERTIFICATES);
        if (containsMDMPushedCertificates(context)) {
            try{
                ((Activity)context).startActivity(intent);
            }catch (ActivityNotFoundException ex) {
                ex.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public static boolean containsMDMPushedCertificates(Context context){
        Preferences p = Preferences.getPreferences(context);
        boolean certUpdate = (p.getMDMSmimeCertsAcc()!=null && !p.getMDMSmimeCertsAcc().equals("") && p.getMDMSmimeCertsAcc().split(",").length>0) && !SecuUtil.isCCMEnabled(context);
        return certUpdate;
    }

    public static int getKeystoreStatus(Context context) {
        Uri parsedUri = Uri.parse(SecContentProviderURI.SECURITY_URI);

        Cursor c = null;
        int status = -1;
        try {
            c = context.getContentResolver().query(parsedUri,
                    null, SecContentProviderURI.SECURITYPOLICY_GETCREDENTIALSTORAGESTATUS, null, null);


            if (c != null) {
                if (c.moveToFirst()) {
                    status = c.getInt(c.getColumnIndex(SecContentProviderURI.SECURITYPOLICY_GETCREDENTIALSTORAGESTATUS));

                    c.close();
                } else {

                    Log.d(TAG, "credentials cursor is empty");
                }
            } else {

                Log.d(TAG, "credentials cursor is null");
            }
        } catch (SecurityException e){
            Log.dumpException(TAG, e);
        }

        finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }

        if (status == -1) {
            Log.d(TAG, "MDM did not return valid status. Get keystore status from EmailKeystoreManager");
            try {
                EmailKeystoreManager remoteServiceKeystore = EmailKeystoreManager.getService();
                if (remoteServiceKeystore != null) {
                    status = remoteServiceKeystore.getKeystoreStatus();
                    Log.d(TAG, "EmailKeystoreManager.getKeystoreStatus() returned: " + status);
                }
            } catch (Exception e) {
                Log.dumpException(TAG, e);
            }
        }

        Log.d(TAG, "credentials storage status: " + status);
        return status;
    }
    //SRIB@abhilasha.hv change for MDM SMIME cert installation ends

    public static boolean isUcmAlias(String alias) {
        boolean result = false;

        if (alias != null) {
            try {
                URI uri = new URI(alias);
                if (SecuUtil.UCM_ALIAS_SCHEME.equalsIgnoreCase(uri.getScheme())) {
                    Log.d(Logging.LOG_TAG, "isUcmAlias : UCM alias");
                    result = true;
                }
            } catch (URISyntaxException e) {
                Log.d(Logging.LOG_TAG, "isUcmAlias, URISyntaxException : Not a UCM alias");
            }
        }

        return result;
    }

    public static String getAliasNameFromUri(String alias) {
        String result = alias;

        if (alias != null) {
            try {
                URI uri = new URI(alias);
                String path = uri.getPath();
                if (path != null) {
                    String pathSegments[] = path.split("/");
                    if (pathSegments != null && pathSegments.length > 0) {
                        result = pathSegments[pathSegments.length - 1]; // Get last segment
                        Log.d(TAG, "getAliasNameFromUri, extracted name : " + result);
                    }
                }
            } catch (URISyntaxException e) {
                Log.d(TAG, "getAliasNameFromUri : URISyntaxException, Not UCM alias");
            }
        } else {
            result = "";
        }

        return result;
    }
}
