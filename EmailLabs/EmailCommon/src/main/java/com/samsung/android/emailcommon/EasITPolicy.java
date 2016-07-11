package com.samsung.android.emailcommon;

import com.samsung.android.emailcommon.provider.EmailContent;
import com.samsung.android.emailcommon.service.PolicySet;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class EasITPolicy {
	
//	final static private String TAG = "EmailPolicy";
	
	final static public String AUTHORITY = EmailContent.AUTHORITY + ".policy";
	final static public Uri URI = Uri.parse("content://" + AUTHORITY);
	
	final static public String IS_ACTIVE_ADMIN = "isActiveAdmin";
	final static public String IS_ACTIVE = "isActive";
	final static public String IS_POLICY_SET_ACTIVE = "isPolicySetActive";
	final static public String COMPONENET_NAME = "componentName";
	final static public String SET_ACCOUNT_HOLD_FLAG = "setAccountHoldFlag";
    final static public String SET_PASSWORD_EXPIRED_FLAG = "setPasswordExpiredFlag";
    final static public String SET_ACCOUNT_SECURITY_HOLD = "setAccountSecurityHold";
	final static public String INACTIVE_REASON = "inActiveReason";
	final static public String SET_ACTIVE_POLICIES ="setActivePolicies";
	final static public String ACCOUNT_POLICY = "accountPolicy";
	final static public String REDUCE_POLICIES = "reducePolcies";
	final static public String UPDATE_POLICIES = "updatePolcies";
	final static public String REMOTE_WIPE = "remoteWipe";
	
	final static public String ACCOUNT_ID = "account_id";
	final static public String NEW_STATE = "new_state";
	
	private static EasITPolicy _inst = null;
	
	protected Context mContext = null;
	
	public static EasITPolicy getInstance(Context context) {
		if (_inst == null) {
			_inst = new EasITPolicy(context);
		}
		return _inst;
	}
	
	protected EasITPolicy(Context context) {
		mContext = context.getApplicationContext();
	}
	
	static public void release() {
		if (_inst == null)
			return;
		_inst.mContext = null;
		_inst = null;
	}
	public boolean isAdminActive() {
		boolean isActive = false;
		QueryUtil qu = QueryUtil.createInstance(mContext.getContentResolver());
		Uri uri = URI.buildUpon().appendEncodedPath(IS_ACTIVE_ADMIN).build();
        if (uri != null) {
            Cursor c = qu.query(uri, null, null);
            if (c != null && !c.isClosed()) {
                c.moveToFirst();
                isActive = c.getInt(0) == 1;
                c.close();
            }
        }
		return isActive;
	}
	
	public ComponentName getComponentName() {
	    ComponentName name = null;
		
		QueryUtil qu = QueryUtil.createInstance(mContext.getContentResolver());
		Uri uri = URI.buildUpon().appendEncodedPath(COMPONENET_NAME).build();
        if (uri != null) {
            Cursor c = qu.query(uri, null, null);
            if (c != null && !c.isClosed()) {
                c.moveToFirst();
                String pkgName = c.getString(0);
                String clsName = c.getString(1);
                name = new ComponentName(pkgName, clsName);
                c.close();
            }
        }
		return name;
	}
	
	public boolean isActive(){
	    boolean isActive = true;
	    QueryUtil qu = QueryUtil.createInstance(mContext.getContentResolver());
	    Uri uri = URI.buildUpon().appendEncodedPath(IS_ACTIVE).build();
        if (uri != null) {
            Cursor c = qu.query(uri, null, null);
            if (c != null && !c.isClosed()) {
                c.moveToFirst();
                isActive = c.getInt(0) == 1;
                c.close();
            }
        }
	    return isActive;
	}
	
	public boolean isActive(PolicySet policies){
	    int isActive = 0;
        // quick check for the "empty set" of no policies
        if ((SecurityPolicyDefs.NO_POLICY_SET).equals(policies)) {
           return true;
        }
	    ContentValues cv = new ContentValues();
	    
	    cv.put(SecurityPolicyDefs.POLICY_PASSWORD_MODE, policies.mPasswordMode);
	    cv.put(SecurityPolicyDefs.POLICY_MIN_DEVICE_PASSWORD_LENGTH, policies.mMinPasswordLength);
	    cv.put(SecurityPolicyDefs.POLICY_SIMPLE_PASSWORD_ENABLED, policies.mSimplePasswordEnabled);
	    cv.put(SecurityPolicyDefs.POLICY_MIN_PASSWORD_COMPLEX_CHARS, policies.mPasswordComplexChars);
	    cv.put(SecurityPolicyDefs.POLICY_PASSWORD_RECOVERY_ENABLED, policies.mPasswordRecoverable);
	    cv.put(SecurityPolicyDefs.POLICY_DEVICE_PASSWORD_EXPIRATION, policies.mPasswordExpirationDays);
	    cv.put(SecurityPolicyDefs.POLICY_DEVICE_PASSWORD_HISTORY, policies.mPasswordHistory);
	    cv.put(SecurityPolicyDefs.POLICY_MAX_DEVICE_PASSWORD_FAILED_ATTEMPTS, policies.mMaxPasswordFails);
	    cv.put(SecurityPolicyDefs.POLICY_MAX_INACTIVITY_TIME, policies.mMaxScreenLockTime);
	    cv.put(SecurityPolicyDefs.POLICY_REQUIRE_DEVICE_ENCRYPTION, policies.mRequireEncryption);
        cv.put(SecurityPolicyDefs.POLICY_DEVICE_ENCRYPTION_ENABLED, policies.mDeviceEncryptionEnabled);
	    cv.put(SecurityPolicyDefs.POLICY_ALLOW_CAMERA, policies.mAllowCamera);
	    cv.put(SecurityPolicyDefs.POLICY_ALLOW_WIFI, policies.mAllowWifi);
	    cv.put(SecurityPolicyDefs.POLICY_ALLOW_TEXT_MESSAGING, policies.mAllowTextMessaging);
	    cv.put(SecurityPolicyDefs.POLICY_ALLOW_POPIMAP_EMAIL, policies.mAllowPOPIMAPEmail);
	    cv.put(SecurityPolicyDefs.POLICY_ALLOW_BROWSER, policies.mAllowBrowser);
	    cv.put(SecurityPolicyDefs.POLICY_ALLOW_STORAGE_CARD, policies.mAllowStorageCard);
	    cv.put(SecurityPolicyDefs.POLICY_ALLOW_INTERNET_SHARING, policies.mAllowInternetSharing);
	    cv.put(SecurityPolicyDefs.POLICY_ALLOW_BLUETOOTH, policies.mAllowBluetoothMode);
	    cv.put(SecurityPolicyDefs.POLICY_ALLOW_DESKTOP_SYNC, policies.mAllowDesktopSync);
	    cv.put(SecurityPolicyDefs.POLICY_ALLOW_IRDA, policies.mAllowIrDA);
	    cv.put(SecurityPolicyDefs.POLICY_ALLOW_UNSIGNED_APPS, policies.mAllowUnsignedApp);
	    cv.put(SecurityPolicyDefs.POLICY_ALLOW_UNSIGNED_INSTALLATION_PACKAGES, policies.mAllowUnsignedInstallationPkg);
	    
        Uri uri = URI.buildUpon().appendEncodedPath(IS_POLICY_SET_ACTIVE).build();
        if (uri != null) {
            isActive = mContext.getContentResolver().update(uri, cv, null, null);

        }
        return isActive==0?true:false;
	}
	
	public void setAccountHoldFlag(long accountId, boolean newValue){
	    ContentValues cv = new ContentValues();
	    cv.put(ACCOUNT_ID, accountId);
	    cv.put(NEW_STATE, newValue);
	    Uri uri = URI.buildUpon().appendEncodedPath(SET_ACCOUNT_HOLD_FLAG).build();
        if (uri != null) {
            mContext.getContentResolver().update(uri, cv, null, null);
        }
	}

    public void setAccountPasswordExpiredFlag(long accountId, boolean newValue) {
        ContentValues cv = new ContentValues();
        cv.put(ACCOUNT_ID, accountId);
        cv.put(NEW_STATE, newValue);
        Uri uri = URI.buildUpon().appendEncodedPath(SET_PASSWORD_EXPIRED_FLAG).build();
        if (uri != null) {
            mContext.getContentResolver().update(uri, cv, null, null);
        }
    }

    public void setAccountSecurityHold(long accountId) {
        ContentValues cv = new ContentValues();
        cv.put(ACCOUNT_ID, accountId);
        Uri uri = URI.buildUpon().appendEncodedPath(SET_ACCOUNT_SECURITY_HOLD).build();
        if (uri != null) {
            mContext.getContentResolver().update(uri, cv, null, null);
        }
    }
	
	public int getInactiveReasons(){
	    int reason = 0;
	    QueryUtil qu = QueryUtil.createInstance(mContext.getContentResolver());
        Uri uri = URI.buildUpon().appendEncodedPath(INACTIVE_REASON).build();
        if (uri != null) {
            Cursor c = qu.query(uri, null, null);
            if (c != null && !c.isClosed()) {
                c.moveToFirst();
                reason = c.getInt(0);
                c.close();
            }
        }
	    return reason;
	}
	
	public void setActivePolicies(){
	    Uri uri = URI.buildUpon().appendEncodedPath(SET_ACTIVE_POLICIES).build();
	    ContentValues emptyCV = new ContentValues();
        if (uri != null) {
            mContext.getContentResolver().update(uri, emptyCV, null, null);
        }
	}
	
	public PolicySet getAccountPolicy(long accountId){
	    PolicySet accPolicy = SecurityPolicyDefs.NO_POLICY_SET;
	    QueryUtil qu = QueryUtil.createInstance(mContext.getContentResolver());
        Uri uri = URI.buildUpon().appendEncodedPath(ACCOUNT_POLICY).build();
        if (uri != null) {
            Cursor c = qu.query(uri, null, Long.toString(accountId));
            if (c != null && !c.isClosed()) {
                c.moveToFirst();
                // Account Policies
                boolean attachmentsEnabled = c.getInt(0) == 1 ? true : false;
                int maxAttachmentSize = c.getInt(1);
                boolean allowHTMLEmail = c.getInt(2) == 1 ? true : false;
                boolean requireManualSyncWhenRoaming = c.getInt(3) == 1 ? true : false;
                int maxCalendarAgeFilter = c.getInt(4);
                int maxEmailAgeFilter = c.getInt(5);
                int maxEmailBodyTruncationSize = c.getInt(6);
                int maxEmailHtmlBodyTruncationSize = c.getInt(7);
                boolean requireSignedSMIMEMessages = c.getInt(8) == 1 ? true : false;
                boolean requireEncryptedSMIMEMessages = c.getInt(9) == 1 ? true : false;
                int requireSignedSMIMEAlgorithm = c.getInt(10);
                int requireEncryptionSMIMEAlgorithm = c.getInt(11);
                int allowSMIMEEncryptionAlgorithmNegotiation = c.getInt(12);
                boolean allowSMIMESoftCerts = c.getInt(13) == 1 ? true : false;

                // Device Policies
                int minPasswordLength = c.getInt(14);
                int passwordMode = c.getInt(15);
                int maxPasswordFails = c.getInt(16);
                int passwordExpirationDays = c.getInt(17);
                int passwordHistory = c.getInt(18);
                int passwordComplexChars = c.getInt(19);
                boolean simplePasswordEnabled = c.getInt(20) == 1 ? true : false;
                int maxScreenLockTime = c.getInt(21);
                boolean passwordRecoverable = c.getInt(22) == 1 ? true : false;
                boolean allowStorageCard = c.getInt(23) == 1 ? true : false;
                boolean allowCamera = c.getInt(24) == 1 ? true : false;
                boolean allowWifi = c.getInt(25) == 1 ? true : false;
                boolean allowTextMessaging = c.getInt(26) == 1 ? true : false;
                boolean allowPOPIMAPEmail = c.getInt(27) == 1 ? true : false;
                boolean allowBrowser = c.getInt(28) == 1 ? true : false;
                boolean allowInternetSharing = c.getInt(29) == 1 ? true : false;
                int allowBluetoothMode = c.getInt(30);
                boolean allowDesktopSync = c.getInt(31) == 1 ? true : false;
                boolean allowIrDA = c.getInt(32) == 1 ? true : false;
                boolean encryptionRequired = c.getInt(33) == 1 ? true : false;
                boolean deviceEncryptionEnabled = c.getInt(34) == 1 ? true : false;
                String mAppBlockListInRom = c.getString(35);
                String mAppAllowListThirdParty = c.getString(36);
                boolean allowUnsignedApp = c.getInt(37) == 1 ? true : false;
                boolean allowUnsignedInstallationPkg = c.getInt(38) == 1 ? true : false;

                accPolicy = new PolicySet(minPasswordLength, passwordMode, maxPasswordFails,
                        maxScreenLockTime, true, passwordRecoverable, passwordExpirationDays,
                        passwordHistory, attachmentsEnabled, maxAttachmentSize, allowStorageCard,
                        allowCamera, allowWifi, allowTextMessaging, allowPOPIMAPEmail,
                        allowHTMLEmail, allowBrowser, allowInternetSharing,
                        requireManualSyncWhenRoaming, allowBluetoothMode, passwordComplexChars,
                        maxCalendarAgeFilter, maxEmailAgeFilter, maxEmailBodyTruncationSize,
                        maxEmailHtmlBodyTruncationSize, requireSignedSMIMEMessages,
                        requireEncryptedSMIMEMessages, requireSignedSMIMEAlgorithm,
                        requireEncryptionSMIMEAlgorithm, allowSMIMEEncryptionAlgorithmNegotiation,
                        allowSMIMESoftCerts, allowDesktopSync, allowIrDA, encryptionRequired,
                        deviceEncryptionEnabled, simplePasswordEnabled, mAppAllowListThirdParty,
                        mAppBlockListInRom, allowUnsignedApp, allowUnsignedInstallationPkg);
                c.close();
            }
        }
	    return accPolicy;
	}
	
	public void updatePolicies(){
	    Uri uri = URI.buildUpon().appendEncodedPath(UPDATE_POLICIES).build();
        if (uri != null) {
            ContentValues emptyCV = new ContentValues();
            mContext.getContentResolver().update(uri, emptyCV, null, null);
        }
	}
	
	public void reducePolicies(){
	    Uri uri = URI.buildUpon().appendEncodedPath(REDUCE_POLICIES).build();
        if (uri != null) {
            ContentValues emptyCV = new ContentValues();
            mContext.getContentResolver().update(uri, emptyCV, null, null);
        }
	}
	
	public void remoteWipe(){
		Uri uri = URI.buildUpon().appendEncodedPath(REMOTE_WIPE).build();
        if (uri != null) {
            ContentValues emptyCV = new ContentValues();
            mContext.getContentResolver().update(uri, emptyCV, null, null);
        }
	}
}
