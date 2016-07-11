
package com.samsung.android.emailcommon.provider;

import com.samsung.android.emailcommon.crypto.AESEncryptionUtil;
import com.samsung.android.emailcommon.utility.Log;

import android.content.Context;
import android.database.Cursor;

import java.io.Serializable;

public class LDAPSettings implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private static final String TAG = "LDAPSettings";
    private String ldapUsername;
    private String ldapPassword;
    private int ldapPort;
    private String ldapHost;
    private boolean isSSL;
    private boolean isAnonymous;
    private String DN;
    private int iCertificatePath;
    private int trustAll;
    
    public static final int MSG_ERROR_NONE = 0;
    public static final int MSG_REGISTER_CLIENT = 1;
    
    public static final int MSG_UNREGISTER_CLIENT = 1002;
    public static final int MSG_LDAP_SAVE_SETTINGS_REQ = 1003;
    public static final int MSG_LDAP_CONNECTION_REQ = 1004;
    public static final int MSG_LDAP_SEARCH_ATTR_REQ = 1005;
    public static final int MSG_LDAP_SEARCH_CONTACT_REQ = 1006;
    public static final int MSG_LDAP_DISCONNECT_REQ = 1007;
    public static final int MSG_LDAP_GET_DN_REQ = 1008;
    public static final int MSG_LDAP_ADD_ENTRY_REQ = 1009;
    public static final int MSG_LDAP_ADD_VALUE_REQ = 1010;
    public static final int MSG_LDAP_REPLACE_VALUE_REQ = 1011;
    public static final int MSG_LDAP_DELETE_VALUE_REQ = 1012;
    public static final int MSG_LDAP_DELETE_ENTRY_REQ = 1013;
    public static final int MSG_LDAP_GET_REQUIRED_LIST_REQ = 1014;
    public static final int MSG_LDAP_CONTINUE_CONNECT_REQ = 1015;
    public static final int MSG_LDAP_SEARCH_MORE_REQ = 1016;

    public static final int MSG_LDAP_SAVE_SETTINGS_RESP = 1103;
    public static final int MSG_LDAP_CONNECTION_RESP = 1104;
    public static final int MSG_LDAP_SEARCH_ATTR_RESP = 1105;
    public static final int MSG_LDAP_SEARCH_CONTACT_RESP = 1106;
    public static final int MSG_LDAP_DISCONNECT_RESP = 1107;
    public static final int MSG_LDAP_GET_DN_RESP = 1108;
    public static final int MSG_LDAP_ADD_ENTRY_RESP = 1109;
    public static final int MSG_LDAP_ADD_VALUE_RESP = 1110;
    public static final int MSG_LDAP_REPLACE_VALUE_RESP = 1111;
    public static final int MSG_LDAP_DELETE_VALUE_RESP = 1112;
    public static final int MSG_LDAP_DELETE_ENTRY_RESP = 1113;
    public static final int MSG_LDAP_GET_REQUIRED_LIST_RESP = 1114;
    public static final int MSG_LDAP_SEARCH_MORE_RESP = 1115;

    public static final int MSG_SUCCESS = 1201;
    public static final int MSG_FAILED = 1202;
    public static final int CONNECTION_SUCCESS = 1203;
    public static final int CONNECTION_FAILED = 1204;
    public static final int GETDN_SUCCESS = 1205;
    public static final int GETDN_FAILED = 1206;
    public static final int MSG_SEARCH_SUCCESS = 1207;
    public static final int MSG_SEARCH_FAILED = 1208;
    public static final int MSG_CERTIFICATE_EXCEPTION = 1209;
    public static final int MSG_CERTIFICATE_NOT_YET_VALID = 1210;
    public static final int MSG_CERTIFICATE_EXPIRED = 1211;
    public static final int MSG_SSL_EXCEPTION = 1212;

    public String getDN() {
        return DN;
    }

    public void setDN(String dN) {
        DN = dN;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public String getLdapUsername() {
        return ldapUsername;
    }

    public void setLdapUsername(String ldapUsername) {
        this.ldapUsername = ldapUsername;
    }

    public String getLdapPassword() {
        return ldapPassword;
    }

    public void setLdapPassword(String ldapPassword) {
        this.ldapPassword = ldapPassword;
    }

    public int getLdapPort() {
        return ldapPort;
    }

    public void setLdapPort(int ldapPort) {
        this.ldapPort = ldapPort;
    }

    public String getLdapHost() {
        return ldapHost;
    }

    public void setLdapHost(String ldapHost) {
        this.ldapHost = ldapHost;
    }

    public boolean isSSL() {
        return isSSL;
    }

    public void setSSL(boolean isSSL) {
        this.isSSL = isSSL;
    }

    public int GetCertificatePath() {
        return iCertificatePath;
    }

    public void setCertificatePath(int iCertificate) {
        iCertificatePath = iCertificate;
    }
    public void  setTrustAll(int trustAll){
        this.trustAll =trustAll;
    }
    public int getTrustAll(){
        return trustAll;
    }
    @SuppressWarnings("unused")	public boolean isExist(Context mContext) {
        boolean result = false;
        Cursor c = null;
        if (mContext == null) {
            Log.logd(TAG, "isExist : mContext is null");
            return result;
        }
        String[] PROJECTION = {
                EmailContent.LDAPAccountColumns.SERVER, EmailContent.LDAPAccountColumns.PORT,
                EmailContent.LDAPAccountColumns.USERNAME, EmailContent.LDAPAccountColumns.PASSWORD,
                EmailContent.LDAPAccountColumns.SSL
        };
        try {
            c = mContext.getContentResolver().query(EmailContent.LDAPAccountColumns.CONTENT_URI,
                    PROJECTION, null, null, null);
            if (c != null && c.getCount() > 0) {                c.moveToFirst();                do {
                    if (this.getLdapHost().equals(c.getString(0))                            && this.getLdapPort() == c.getInt(1)

                            && this.getLdapUsername().equals(c.getString(2))
                            && this.getLdapPassword().equals(                                    AESEncryptionUtil.AESDecryption(c.getString(3)) == null ? ""
                                            : AESEncryptionUtil.AESDecryption(c.getString(3)))

                            && (this.isSSL() ? 1 : 0) == c.getInt(4)) {
                        result = true;                    }                } while (c.moveToNext());
            }        } finally {            if (c != null && !c.isClosed())                c.close();
        }
        Log.logd(TAG, "isExist : returns = " + result);
        return result;
    }
}
