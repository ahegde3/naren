
package com.samsung.android.emailcommon.variant;

import com.samsung.android.emailcommon.utility.Log;

import android.app.admin.DeviceAdminInfo;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.dirEncryption.DirEncryptionManager;
import android.dirEncryption.SDCardEncryptionPolicies;

import java.util.List;

public class DPMWraper {

    final public static String ACTION_START_ENCRYPTION = "android.app.action.START_ENCRYPTION";

    final public static int PASSWORD_QUALITY_NUMERIC = DevicePolicyManager.PASSWORD_QUALITY_NUMERIC;
    final public static int PASSWORD_QUALITY_ALPHANUMERIC = DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC;
    final public static int PASSWORD_QUALITY_UNSPECIFIED = DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED;
    final public static int PASSWORD_QUALITY_COMPLEX = DevicePolicyManager.PASSWORD_QUALITY_COMPLEX;
// for SDL    
//    final public static int PASSWORD_QUALITY_SMARTCARDNUMERIC = DevicePolicyManager.PASSWORD_QUALITY_SMARTCARDNUMERIC;//WTL SMARTCARD LOCKSCREEN

    final public static String ACTION_ADD_DEVICE_ADMIN = DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN;
    final public static String EXTRA_DEVICE_ADMIN = DevicePolicyManager.EXTRA_DEVICE_ADMIN;
    final public static String EXTRA_ADD_EXPLANATION = DevicePolicyManager.EXTRA_ADD_EXPLANATION;
    final public static String ACTION_SET_NEW_PASSWORD = DevicePolicyManager.ACTION_SET_NEW_PASSWORD;

    final public static int USES_POLICY_EXPIRE_PASSWORD = DeviceAdminInfo.USES_POLICY_EXPIRE_PASSWORD;
    final public static int USES_ENCRYPTED_STORAGE = DeviceAdminInfo.USES_ENCRYPTED_STORAGE;

    final public static int ENCRYPTION_STATUS_ACTIVE = DevicePolicyManager.ENCRYPTION_STATUS_ACTIVE;
    final public static int ENCRYPTION_STATUS_UNSUPPORTED = DevicePolicyManager.ENCRYPTION_STATUS_UNSUPPORTED;

    private static final String TAG = "DPMWraper";

    private DevicePolicyManager mDPM = null;
    private DirEncryptionManager mDEM = null;
    private static DPMWraper sInstance = null;

    DPMWraper(Context context) {
        Log.d(TAG, "DPMWraper  calls getApplicationContext()!!.It returns  " + context.getApplicationContext());
        if(context.getApplicationContext() != null)
            mDPM = (DevicePolicyManager) context.getApplicationContext().getSystemService(
                    Context.DEVICE_POLICY_SERVICE);
        mDEM = new DirEncryptionManager(context);

    }

    public static DPMWraper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DPMWraper(context);
        }
        return sInstance;
    }
    
    static public void release() {
    	if (sInstance == null)
    		return;
    	sInstance.mDEM = null;
    	sInstance.mDPM = null;
    	sInstance = null;
    }
    public boolean isDPMExist() {
        if (mDPM == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean getAllowPOPIMAPEmail(ComponentName cp) {
        return mDPM.getAllowPOPIMAPEmail(cp);
    }

    public boolean getAllowIrDA(ComponentName cp) {
        return mDPM.getAllowIrDA(cp);
    }

    public boolean isAdminActive(ComponentName cp) {
        return mDPM.isAdminActive(cp);
    }

    public List<ComponentName> getActiveAdmins() {
        return mDPM.getActiveAdmins();
    }

    public int getPasswordMinimumLength(ComponentName cp) {
        return mDPM.getPasswordMinimumLength(cp);
    }

    public int getPasswordQuality(ComponentName cp) {
        return mDPM.getPasswordQuality(cp);
    }

    public boolean isActivePasswordSufficient() {
        return mDPM.isActivePasswordSufficient();
    }

    public long getMaximumTimeToLock(ComponentName cp) {
        return mDPM.getMaximumTimeToLock(cp);
    }

    public int getMaximumFailedPasswordsForWipe(ComponentName cp) {
        return mDPM.getMaximumFailedPasswordsForWipe(cp);
    }

    public boolean getPasswordRecoverable(ComponentName cp) {
        return mDPM.getPasswordRecoverable(cp);
    }
    
    public boolean checkPassword(ComponentName cp, String password){
        return mDPM.checkPassword(cp, password);
    }

    public boolean getCameraDisabled(ComponentName cp) {
        return mDPM.getCameraDisabled(cp);
    }

    public boolean getAllowTextMessaging(ComponentName cp) {
        return mDPM.getAllowTextMessaging(cp);
    }

    public boolean getAllowWifi(ComponentName cp) {
        return mDPM.getAllowWifi(cp);
    }

    public boolean getAllowBrowser(ComponentName cp) {
        return mDPM.getAllowBrowser(cp);
    }

    public boolean getAllowStorageCard(ComponentName cp) {
        return mDPM.getAllowStorageCard(cp);
    }

    public boolean getAllowInternetSharing(ComponentName cp) {
        return mDPM.getAllowInternetSharing(cp);
    }

    public int getAllowBluetoothMode(ComponentName cp) {
        return mDPM.getAllowBluetoothMode(cp);
    }

    public boolean getAllowDesktopSync(ComponentName cp) {
        return mDPM.getAllowDesktopSync(cp);
    }

    public boolean getRequireStorageCardEncryption(ComponentName cp) {
        return mDPM.getRequireStorageCardEncryption(cp);
    }

    public void removeActiveAdmin(ComponentName cp) {
        mDPM.removeActiveAdmin(cp);
    }

    public void setPasswordQuality(ComponentName cp, int value) {
        mDPM.setPasswordQuality(cp, value);
    }

    public void setPasswordMinimumLength(ComponentName cp, int value) {
        mDPM.setPasswordMinimumLength(cp, value);
    }

    public void setMaximumTimeToLock(ComponentName cp, int value) {
        mDPM.setMaximumTimeToLock(cp, value);
    }

    public void setMaximumFailedPasswordsForWipe(ComponentName cp, int value) {
        mDPM.setMaximumFailedPasswordsForWipe(cp, value);
    }

    public void setPasswordRecoverable(ComponentName cp, boolean value) {
        mDPM.setPasswordRecoverable(cp, value);
    }

    public void setAllowStorageCard(ComponentName cp, boolean value) {
        mDPM.setAllowStorageCard(cp, value);
    }

    public void setCameraDisabled(ComponentName cp, boolean value) {
        mDPM.setCameraDisabled(cp, value);
    }

    public void setAllowWifi(ComponentName cp, boolean value) {
        mDPM.setAllowWifi(cp, value);
    }

    public void setAllowTextMessaging(ComponentName cp, boolean value) {
        mDPM.setAllowTextMessaging(cp, value);
    }

    public void setAllowPOPIMAPEmail(ComponentName cp, boolean value) {
        mDPM.setAllowPOPIMAPEmail(cp, value);
    }

    public void setAllowIrDA(ComponentName cp, boolean value) {
        mDPM.setAllowIrDA(cp, value);
    }

    public void setAllowBrowser(ComponentName cp, boolean value) {
        mDPM.setAllowBrowser(cp, value);
    }

    public void setAllowInternetSharing(ComponentName cp, boolean value) {
        mDPM.setAllowInternetSharing(cp, value);
    }

    public void setAllowBluetoothMode(ComponentName cp, int value) {
        mDPM.setAllowBluetoothMode(cp, value);
    }

    public void setAllowDesktopSync(ComponentName cp, boolean value) {
        mDPM.setAllowDesktopSync(cp, value);
    }

    public void setRecoveryPasswordState(ComponentName cp, boolean value) {
        mDPM.setRecoveryPasswordState(cp, value);
    }
    
    //EAS IT Policy --start
    public void setAllowAppListThirdParty(ComponentName cp, String value) {
           mDPM.setAllowAppListThirdParty(cp, value);
    }
    
    public void setBlockListInRom(ComponentName cp, String value) {
              mDPM.setBlockListInRom(cp, value);
    }
    
    public void setAllowUnsignedApp(ComponentName cp , boolean flags) {
    	mDPM.setAllowUnsignedApp(cp, flags);
    }
    
    public void setAllowUnsignedInstallationPkg(ComponentName cp , boolean flags) {
    	mDPM.setAllowUnsignedInstallationPkg(cp, flags);
    }
    
    public void setDisableKeyguardFeatures(ComponentName cp, int value){
        mDPM.setKeyguardDisabledFeatures(cp, value);
    }
    
    public int getDisableKeyguardFeatures(ComponentName cp){
        return mDPM.getKeyguardDisabledFeatures(cp);
    }
    //EAS IT Policy --end


    public void wipeData(int value) {
        mDPM.wipeData(value);
    }

    public boolean getRequireDeviceEncryption(ComponentName cp) {
        return mDPM.getStorageEncryption(cp);
    }

    public void setPasswordExpirationTimeout(ComponentName cp, long value) {
        mDPM.setPasswordExpirationTimeout(cp, value);
    }

    public void setPasswordHistoryLength(ComponentName cp, int value) {
        mDPM.setPasswordHistoryLength(cp, value);
    }

    public void setPasswordMinimumNonLetter(ComponentName cp, int value) {
        mDPM.setPasswordMinimumNonLetter(cp, value);
    }

    public void setStorageEncryption(ComponentName cp, boolean value) {
        mDPM.setStorageEncryption(cp, value);
    }

    public void setExternalSDEncryption(ComponentName cp, boolean value) {
        mDPM.setRequireStorageCardEncryption(cp, value);
    }

    public void setSimplePasswordEnabled(ComponentName cp, boolean value) {
        mDPM.setSimplePasswordEnabled(cp, value);

    }

    public boolean hasGrantedPolicy(ComponentName cp, int value) {
        return mDPM.hasGrantedPolicy(cp, value);
    }

    public int getPasswordHistoryLength(ComponentName cp) {
        return mDPM.getPasswordHistoryLength(cp);
    }

    public long getPasswordExpirationTimeout(ComponentName cp) {
        return mDPM.getPasswordExpirationTimeout(cp);
    }

    public long getPasswordExpiration(ComponentName cp) {
        return mDPM.getPasswordExpiration(cp);
    }

    public int getPasswordMinimumNonLetter(ComponentName cp) {
        return mDPM.getPasswordMinimumNonLetter(cp);
    }

    public int getStorageEncryptionStatus() {
        return mDPM.getStorageEncryptionStatus();
    }

    public boolean getSimplePasswordEnabled(ComponentName cp) {
        return mDPM.getSimplePasswordEnabled(cp);

    }

    //EAS IT Policy --start    
    public String getAllowAppListThirdParty(ComponentName cp) {
        return mDPM.getAllowAppListThirdParty(cp);
    }
    
    public String getBlockListInRom(ComponentName cp) {
        return mDPM.getBlockListInRom(cp);
    }
	  
    public boolean getAllowUnsignedApp(ComponentName cp ) {      	
        return mDPM.getAllowUnsignedApp(cp);      	
    }
	  
    public boolean getAllowUnsignedInstallationPkg(ComponentName cp) {      	
        return mDPM.getAllowUnsignedInstallationPkg(cp);      	
    }
    //EAS IT Policy --end
     
    public boolean isRecoveryPasswordEnabled() {
        return mDPM.getPasswordRecoverable(null);        
    }

    public void setPasswordMinimumUpperCase(ComponentName cp, int value) {
        mDPM.setPasswordMinimumUpperCase(cp, value);        
    }

    public void setPasswordMinimumLowerCase(ComponentName cp, int value) {
        mDPM.setPasswordMinimumLowerCase(cp, value);
    }
    
    public boolean isDeviceEncrypted() {
        return (mDPM.getStorageEncryptionStatus() == DevicePolicyManager.ENCRYPTION_STATUS_ACTIVE)
                || (mDPM.getStorageEncryptionStatus() == DevicePolicyManager.ENCRYPTION_STATUS_ACTIVE_DEFAULT_KEY);
    }
    
    public int checkSDStatus() {
        int status = CommonDefs.SD_ENCRYPTION_NO_REQUIRED;

        SDCardEncryptionPolicies savedPolicy = mDEM.getSDCardEncryptionPrefs();

        if(savedPolicy == null || savedPolicy.mEnc == DirEncryptionManager.DECRYPT)
        {
            status = CommonDefs.SD_ENCRYPTION_POLICY_REQUIRED;
        }
        else if(savedPolicy.mEnc == DirEncryptionManager.ENCRYPT)
        {
            if (!easPolicyApplied(savedPolicy))
                status = CommonDefs.SD_ENCRYPTION_POLICY_REQUIRED;
            else
                status = CommonDefs.SD_ENCRYPTION_NO_REQUIRED;
        }
        Log.d(TAG, "checkSDStatus returns : " + status);
        return status;
    }
    
    public boolean easPolicyApplied(SDCardEncryptionPolicies savedPolicy)
    {
        if(savedPolicy.mEnc == DirEncryptionManager.ENCRYPT 
                && savedPolicy.mFullEnc == DirEncryptionManager.ENCRYPT_FULL_ON
                && savedPolicy.mExcludeMedia == DirEncryptionManager.EXCL_MEDIA_OFF)
            return true;
        else
            return false;
    }

    public boolean isEncryptionSupported() {
        return mDPM.getStorageEncryptionStatus() != DevicePolicyManager.ENCRYPTION_STATUS_UNSUPPORTED;
    }
    
    public void notifyChanges(ComponentName mAdminName, boolean isNotify){
        mDPM.notifyChanges(mAdminName, isNotify);
    }
    
    public String getRecoveryPassword(){
        return mDPM.getRecoveryPassword();
    }
    
    public void removeRecoveryPasswords() {
        mDPM.removeRecoveryPasswords();        
    }
    
    public int getPasswordMinimumUpperCase(ComponentName mAdminName){
        return mDPM.getPasswordMinimumUpperCase(mAdminName);

    }
}
