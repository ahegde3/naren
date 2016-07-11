/* Copyright (C) 2011 The Android Open Source Project
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

package com.samsung.android.emailcommon.service;

import com.samsung.android.emailcommon.SecurityPolicyDefs;
import com.samsung.android.emailcommon.provider.AccountValues;
import com.samsung.android.emailcommon.provider.EmailContent;
import com.samsung.android.emailcommon.provider.EmailContent.Account;
import com.samsung.android.emailcommon.provider.EmailContent.AccountColumns;
import com.samsung.android.emailcommon.provider.EmailContent.Policies;
import com.samsung.android.emailcommon.provider.EmailContent.PoliciesColumns;
import com.samsung.android.emailcommon.utility.EmailLog;
import com.samsung.android.emailcommon.utility.SyncScheduleData;
import com.samsung.android.emailcommon.variant.CommonDefs;
import com.samsung.android.emailcommon.variant.DPMWraper;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

//change@wtl.akulik end
/**
 * Class for tracking policies and reading/writing into accounts
 */
public class PolicySet implements Parcelable {

    private static final String TAG = "SecurityPolicy";

    public static final int ALLOW_BLUETOOTH_MODE_VALUE_DISABLE = 0;

    public static final int ALLOW_BLUETOOTH_MODE_VALUE_HANDSFREE_ONLY = 1;

    public static final int ALLOW_BLUETOOTH_MODE_VALUE_ALLOW = 2;

    // Security (provisioning) flags
    // bits 0..4: password length (0=no password required)
    private static final int PASSWORD_LENGTH_MASK = 31;

    private static final int PASSWORD_LENGTH_SHIFT = 0;

    public static final int PASSWORD_LENGTH_MAX = 30;

    // bits 5..8: password mode
    private static final int PASSWORD_MODE_SHIFT = 5;

    private static final int PASSWORD_MODE_MASK = 15 << PASSWORD_MODE_SHIFT;

    public static final int PASSWORD_MODE_NONE = 0 << PASSWORD_MODE_SHIFT;

    public static final int PASSWORD_MODE_SIMPLE = 1 << PASSWORD_MODE_SHIFT;

    public static final int PASSWORD_MODE_STRONG = 2 << PASSWORD_MODE_SHIFT;

    public static final int PASSWORD_MODE_COMPLEX = 3 << PASSWORD_MODE_SHIFT;

    // bits 9..13: password failures -> wipe device (0=disabled)
    private static final int PASSWORD_MAX_FAILS_SHIFT = 9;

    private static final int PASSWORD_MAX_FAILS_MASK = 31 << PASSWORD_MAX_FAILS_SHIFT;

    public static final int PASSWORD_MAX_FAILS_MAX = 31;

    // bits 14..24: seconds to screen lock (0=not required)
    private static final int SCREEN_LOCK_TIME_SHIFT = 14;

    private static final int SCREEN_LOCK_TIME_MASK = 2047 << SCREEN_LOCK_TIME_SHIFT;

    public static final int SCREEN_LOCK_TIME_MAX = 2047;

    // bit 25: remote wipe capability required
    private static final int REQUIRE_REMOTE_WIPE = 1 << 25;

    // bit 26..35: password expiration (days; 0=not required)
    private static final int PASSWORD_EXPIRATION_SHIFT = 26;

    private static final long PASSWORD_EXPIRATION_MASK = 1023L << PASSWORD_EXPIRATION_SHIFT;

    public static final int PASSWORD_EXPIRATION_MAX = 1023;

    // bit 36..43: password history (length; 0=not required)
    private static final int PASSWORD_HISTORY_SHIFT = 36;

    private static final long PASSWORD_HISTORY_MASK = 255L << PASSWORD_HISTORY_SHIFT;

    public static final int PASSWORD_HISTORY_MAX = 255;

    // bit 44..48: min complex characters (0=not required)
    private static final int PASSWORD_COMPLEX_CHARS_SHIFT = 44;

    private static final long PASSWORD_COMPLEX_CHARS_MASK = 31L << PASSWORD_COMPLEX_CHARS_SHIFT;

    public static final int PASSWORD_COMPLEX_CHARS_MAX = 31;

    // bit 49: requires device encryption
    private static final long REQUIRE_ENCRYPTION = 1L << 49;

    /* Convert days to mSec (used for password expiration) */
    private static final long DAYS_TO_MSEC = 24 * 60 * 60 * 1000;

    private static final Object sPolicyLock = new Object();
    
    /*
     * Small offset (2 minutes) added to policy expiration to make user testing
     * easier.
     */
//    private static final long EXPIRATION_OFFSET_MSEC = 2 * 60 * 1000;

    /* package */public int mMinPasswordLength;

    /* package */public int mPasswordMode;

    /* package */public int mMaxPasswordFails;

    /* package */public int mMaxScreenLockTime;

    /* package */public boolean mRequireRemoteWipe;

    /* package */public int mPasswordExpirationDays;

    /* package */public int mPasswordHistory;

    /* package */public int mPasswordComplexChars;

    /* package */public boolean mRequireEncryption;

    public boolean mDeviceEncryptionEnabled;

    // change@wtl.akulik start IT Policy 12.0
    public boolean mPasswordRecoverable;

    public boolean mAttachmentsEnabled;

    public boolean mSimplePasswordEnabled;
    public int mMaxAttachmentSize;

    // change@wtl.akulik end
    // change@pguha: IT Policy 12.1 start
    public boolean mAllowStorageCard;

    public boolean mAllowCamera;

    public boolean mAllowWifi;

    public boolean mAllowTextMessaging;

    public boolean mAllowPOPIMAPEmail;

    public boolean mAllowHTMLEmail;

    public boolean mAllowBrowser;

    public boolean mAllowInternetSharing;

    public boolean mRequireManualSyncWhenRoaming;

    public int mAllowBluetoothMode;

    public int mMaxCalendarAgeFilter;

    public int mMaxEmailAgeFilter;

    public int mMaxEmailBodyTruncationSize;

    public int mMaxEmailHtmlBodyTruncationSize;

    public boolean mRequireSignedSMIMEMessages;

    public boolean mRequireEncryptedSMIMEMessages;

    // change@wtl.akulik SMIME policies update start
    public int mRequireSignedSMIMEAlgorithm;

    public int mRequireEncryptionSMIMEAlgorithm;

    public int mAllowSMIMEEncryptionAlgorithmNegotiation;

    // change@wtl.akulik SMIME policies update end
    public boolean mAllowSMIMESoftCerts;

    public boolean mAllowDesktopSync;

    public boolean mAllowIrDA;

    // change@pguha: IT Policy 12.1 end
    //EAS IT Policy --start
    public String mAllowAppList="";
    public String mBlockAppList="";
    public boolean mAllowUnsignedApp= true;
    public boolean mAllowUnsignedInstallationPkg = true;
    
    public String mSecuritySyncKey;
    
    public String getAllowAppListThirdParty() {
    	return mAllowAppList.intern();
    }
    
    public String getBlockAppListInRom() {
    	return mBlockAppList.intern();
    }
    
    public boolean getAllowUnsignedApp() {
    	return mAllowUnsignedApp;	
    }
    
    public boolean getAllowUnsignedInstallationPkg() {
    	return mAllowUnsignedInstallationPkg ;
    }
 
   //EAS IT Policy --end

    public int getMinPasswordLengthForTest() {
        return mMinPasswordLength;
    }

    public int getPasswordModeForTest() {
        return mPasswordMode;
    }

    public int getMaxPasswordFailsForTest() {
        return mMaxPasswordFails;
    }

    public int getMaxScreenLockTimeForTest() {
        return mMaxScreenLockTime;
    }

    public boolean isRequireRemoteWipeForTest() {
        return mRequireRemoteWipe;
    }

    public boolean isRequireEncryptionForTest() {
        return mRequireEncryption;
    }

    /**
     * Create from raw values.
     * 
     * @param minPasswordLength (0=not enforced)
     * @param passwordMode
     * @param maxPasswordFails (0=not enforced)
     * @param maxScreenLockTime in seconds (0=not enforced)
     * @param requireRemoteWipe
     * @param passwordExpirationDays in days (0=not enforced)
     * @param passwordHistory (0=not enforced)
     * @param passwordComplexChars (0=not enforced)
     * @throws IllegalArgumentException for illegal arguments.
     */
    public PolicySet(int minPasswordLength, int passwordMode, int maxPasswordFails,
            int maxScreenLockTime, boolean requireRemoteWipe, boolean passwordRecoverable,
            int passwordExpirationDays, int passwordHistory, boolean attachmentsEnabled,
            int maxAttachmentSize, boolean allowStorageCard, boolean allowCamera,
            boolean allowWifi, boolean allowTextMessaging, boolean allowPOPIMAPEmail,
            boolean allowHTMLEmail, boolean allowBrowser, boolean allowInternetSharing,
            boolean requireManualSyncWhenRoaming, int allowBluetoothMode,
            int passwordComplexChars,
            int maxCalendarAgeFilter,
            int maxEmailAgeFilter,
            int maxEmailBodyTruncationSize,
            int maxEmailHtmlBodyTruncationSize,
            // change@wtl.akulik SMIME policies update start
            boolean requireSignedSMIMEMessages, boolean requireEncryptedSMIMEMessages,
            int requireSignedSMIMEAlgorithm, int requireEncryptionSMIMEAlgorithm,
            int allowSMIMEEncryptionAlgorithmNegotiation, boolean allowSMIMESoftCerts,
            boolean allowDesktopSync, boolean allowIrDA, boolean requireEncryption,
            boolean deviceEncryptionEnabled, boolean simplePasswordEnabled, //EAS IT Policy --start
			String allowList, String blockList,
			boolean allowUnsignedApp, boolean allowUnsignedInstallationPkg)//EAS IT Policy --end
            throws IllegalArgumentException {
        // If we're not enforcing passwords, make sure we clean up related
        // values, since EAS
        // can send non-zero values for any or all of these
        if (passwordMode == PASSWORD_MODE_NONE) {
            maxPasswordFails = 0;
            maxScreenLockTime = 0;
            minPasswordLength = 0;
            passwordComplexChars = 0;
            passwordHistory = 0;
            passwordExpirationDays = 0;
        } else {
            if ((passwordMode != PASSWORD_MODE_SIMPLE) && (passwordMode != PASSWORD_MODE_STRONG)
                    && (passwordMode != PASSWORD_MODE_COMPLEX)) {
                throw new IllegalArgumentException("password mode");
            }
            // If we're only requiring a simple password, set complex chars
            // to zero; note
            // that EAS can erroneously send non-zero values in this case
            if (passwordMode == PASSWORD_MODE_SIMPLE) {
                passwordComplexChars = 0;
            }
            // The next four values have hard limits which cannot be
            // supported if exceeded.
            if (minPasswordLength > PASSWORD_LENGTH_MAX) {
                throw new IllegalArgumentException("password length");
            }
            if (passwordExpirationDays > PASSWORD_EXPIRATION_MAX) {
                throw new IllegalArgumentException("password expiration");
            }
            if (passwordHistory > PASSWORD_HISTORY_MAX) {
                throw new IllegalArgumentException("password history");
            }
            if (passwordComplexChars > PASSWORD_COMPLEX_CHARS_MAX) {
                throw new IllegalArgumentException("complex chars");
            }
            // This value can be reduced (which actually increases security)
            // if necessary
            if (maxPasswordFails > PASSWORD_MAX_FAILS_MAX) {
                maxPasswordFails = PASSWORD_MAX_FAILS_MAX;
            }
            // This value can be reduced (which actually increases security)
            // if necessary
            if (maxScreenLockTime > SCREEN_LOCK_TIME_MAX) {
                maxScreenLockTime = SCREEN_LOCK_TIME_MAX;
            }
        }
        mMinPasswordLength = minPasswordLength;
        
        // jh5997.hyun - 2013.10.23 - START
        // As per MS official response, complex character is number of groups(Upper, Lower, Number, Symbol)
        // For example, if complex character is 4 LockScreen PW must include at least one uppercase, lowercase, number, symbol
        // Also, as per GED concept, if PW type is COMPLEX, PW must include at least one symbol.
        // case #1 - Letter(upper or lower) + Num 
        // case #2 - Letter(upper or lower) + Num       : These 2 are basic type of PASSWORD. so, nothing to do.
        // case #3 - Letter(upper or lower) + Num + Sym : So, EAS has to set COMPLEX for PW type in DPM.
        // case #4 - Upper + Lower + Num + Sym          : So, EAS has to set COMPLEX for PW type in DPM. And, set minimum number for Upper/Lower in DPM 
        if (passwordMode == PASSWORD_MODE_STRONG && passwordComplexChars > 2)
            mPasswordMode = PASSWORD_MODE_COMPLEX;
        else
            mPasswordMode = passwordMode;
        // jh5997.hyun - 2013.10.23 - END
        
        mMaxPasswordFails = maxPasswordFails;
        mMaxScreenLockTime = maxScreenLockTime;
        mRequireRemoteWipe = requireRemoteWipe;
        mPasswordExpirationDays = passwordExpirationDays;
        mPasswordHistory = passwordHistory;
        mPasswordComplexChars = passwordComplexChars;
        mRequireEncryption = requireEncryption;
        mDeviceEncryptionEnabled = deviceEncryptionEnabled;
        // jh5997.hyun - 2012.01.05 - No need to change PW quality due to ODE
        // if (mRequireEncryption || mDeviceEncryptionEnabled) {
        // // jh5997.hyun - change password quality to adjust ODE certificate.
        // if (mMinPasswordLength < 6)
        // mMinPasswordLength = 6;
        // if (mPasswordMode < PASSWORD_MODE_STRONG) {
        // mPasswordMode = PASSWORD_MODE_STRONG;
        // mPasswordComplexChars = 1;
        // }
        // }
        // change@wtl.akulik start IT Policy 12.0
        mPasswordRecoverable = passwordRecoverable;

        mAttachmentsEnabled = attachmentsEnabled;

        mSimplePasswordEnabled = simplePasswordEnabled;

        mMaxAttachmentSize = maxAttachmentSize;
        // change@wtl.akulik end

        // change@pguha: IT Policy 12.1 start
        mAllowStorageCard = allowStorageCard;
        mAllowCamera = allowCamera;
        mAllowWifi = allowWifi;
        mAllowTextMessaging = allowTextMessaging;
        mAllowPOPIMAPEmail = allowPOPIMAPEmail;
        mAllowHTMLEmail = allowHTMLEmail;
        mAllowBrowser = allowBrowser;
        mAllowInternetSharing = allowInternetSharing;
        mRequireManualSyncWhenRoaming = requireManualSyncWhenRoaming;
        mAllowBluetoothMode = allowBluetoothMode;

        mMaxCalendarAgeFilter = maxCalendarAgeFilter;
        mMaxEmailAgeFilter = maxEmailAgeFilter;
        mMaxEmailBodyTruncationSize = maxEmailBodyTruncationSize;
        mMaxEmailHtmlBodyTruncationSize = maxEmailHtmlBodyTruncationSize;
        mRequireSignedSMIMEMessages = requireSignedSMIMEMessages;
        mRequireEncryptedSMIMEMessages = requireEncryptedSMIMEMessages;
        mRequireSignedSMIMEAlgorithm = requireSignedSMIMEAlgorithm;
        mRequireEncryptionSMIMEAlgorithm = requireEncryptionSMIMEAlgorithm;
        mAllowSMIMEEncryptionAlgorithmNegotiation = allowSMIMEEncryptionAlgorithmNegotiation;
        mAllowSMIMESoftCerts = allowSMIMESoftCerts;
        mAllowDesktopSync = allowDesktopSync;
        mAllowIrDA = allowIrDA;
        // change@pguha: IT Policy 12.1 end
        //EAS IT Policy --start
        mAllowAppList = allowList;
        mBlockAppList = blockList;
        mAllowUnsignedApp = allowUnsignedApp;
        mAllowUnsignedInstallationPkg = allowUnsignedInstallationPkg;
        //EAS IT Policy --end
    }

    /**
     * Create from values encoded in an account
     * 
     * @param account
     */
    public PolicySet(Context context, Account account) {
        // change@wtl.akulik start IT Policy 12.0
        // this(account.mSecurityFlags);

        if (account == null || context == null) {
        	EmailLog.e("SecurityPolicy", "PolicySet(): account is null or context is null");
            return;
        }

        if (context.getContentResolver() == null)
            return;

        Cursor cursor = null;
            
        synchronized (sPolicyLock) {
            cursor = Policies.getPoliciesWithAccountId(context, account.mId);
        }

        while (cursor != null && cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(PoliciesColumns.NAME));
            String type = cursor.getString(cursor.getColumnIndex(PoliciesColumns.TYPE));
            String value = cursor.getString(cursor.getColumnIndex(PoliciesColumns.VALUE));

            if (type.equals("Integer")) {
                Integer val = 0;
                try {
                    val = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    continue;
                }
                if (name.equals(SecurityPolicyDefs.POLICY_PASSWORD_MODE)) {
                    mPasswordMode = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_MAX_DEVICE_PASSWORD_FAILED_ATTEMPTS)) {
                    mMaxPasswordFails = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_MIN_DEVICE_PASSWORD_LENGTH)) {
                    mMinPasswordLength = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_MAX_INACTIVITY_TIME)) {
                    mMaxScreenLockTime = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_MAX_ATTACHMENT_SIZE)) {
                    mMaxAttachmentSize = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_DEVICE_PASSWORD_EXPIRATION)) {
                    mPasswordExpirationDays = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_DEVICE_PASSWORD_HISTORY)) {
                    mPasswordHistory = val;
                }

                // change@pguha: IT Policy 12.1 start
                else if (name.equals(SecurityPolicyDefs.POLICY_ALLOW_BLUETOOTH)) {
                    mAllowBluetoothMode = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_MIN_PASSWORD_COMPLEX_CHARS)) {
                    mPasswordComplexChars = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_MAX_CALENDAR_AGE_FILTER)) {
                    mMaxCalendarAgeFilter = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_MAX_EMAIL_AGE_FILTER)) {
                    mMaxEmailAgeFilter = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_MAX_EMAIL_BODY_TRUNC_SIZE)) {
                    mMaxEmailBodyTruncationSize = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_MAX_EMAILHTML_BODY_TRUNC_SIZE)) {
                    mMaxEmailHtmlBodyTruncationSize = val;
                }
                // change@wtl.akulik SMIME policies update start
                else if (name.equals(SecurityPolicyDefs.POLICY_REQUIRE_SIGNED_SMIME_ALGORITHM)) {
                    mRequireSignedSMIMEAlgorithm = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_REQUIRE_ENCRYPTION_SMIME_ALGORITHM)) {
                    mRequireEncryptionSMIMEAlgorithm = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_ALLOW_SMIME_ENCRYPTION_ALGO_NEGOTIATION)) {
                    mAllowSMIMEEncryptionAlgorithmNegotiation = val;
                }
                // change@wtl.akulik SMIME policies update end
                // change@pguha: IT Policy 12.1 end
            } else if (type.equals("Boolean")) {
                Boolean val = Boolean.parseBoolean(value);
                if (name.equals(SecurityPolicyDefs.POLICY_PASSWORD_RECOVERY_ENABLED)) {
                    mPasswordRecoverable = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_REQUIRE_DEVICE_ENCRYPTION)) {
                    mRequireEncryption = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_ATTACHMENTS_ENABLED)) {
                    mAttachmentsEnabled = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_REMOTE_WIPE_REQUIRED)) {
                    mRequireRemoteWipe = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_SIMPLE_PASSWORD_ENABLED)) {

                    mSimplePasswordEnabled = val;

                }
                // change@pguha: IT Policy 12.1 start
                else if (name.equals(SecurityPolicyDefs.POLICY_ALLOW_STORAGE_CARD)) {
                    mAllowStorageCard = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_ALLOW_CAMERA)) {
                    mAllowCamera = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_ALLOW_WIFI)) {
                    mAllowWifi = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_ALLOW_TEXT_MESSAGING)) {
                    mAllowTextMessaging = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_ALLOW_POPIMAP_EMAIL)) {
                    mAllowPOPIMAPEmail = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_ALLOW_HTML_EMAIL)) {
                    mAllowHTMLEmail = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_ALLOW_BROWSER)) {
                    mAllowBrowser = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_ALLOW_INTERNET_SHARING)) {
                    mAllowInternetSharing = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_REQUIRE_MANUALSYNC_ROAMING)) {
                    mRequireManualSyncWhenRoaming = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_REQUIRE_SIGNED_SMIME_MSGS)) {
                    mRequireSignedSMIMEMessages = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_REQUIRE_ENCRYPTED_SMIME_MSGS)) {
                    mRequireEncryptedSMIMEMessages = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_ALLOW_SMIME_SOFT_CERTS)) {
                    mAllowSMIMESoftCerts = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_ALLOW_DESKTOP_SYNC)) {
                    mAllowDesktopSync = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_ALLOW_IRDA)) {
                    mAllowIrDA = val;
                }
				//EAS IT Policy --start
                else if (name.equals(SecurityPolicyDefs.POLICY_ALLOW_UNSIGNED_APPS)) {
                    mAllowUnsignedApp = val;
                }
                else if (name.equals(SecurityPolicyDefs.POLICY_ALLOW_UNSIGNED_INSTALLATION_PACKAGES)) {
                    mAllowUnsignedInstallationPkg = val;
                }
				//EAS IT Policy --end
                // change@pguha: IT Policy 12.1 end
            }
			//EAS IT Policy --start
            else if(type.equals("String"))
            {
            	String val = value;
            	
               if (name.equals(SecurityPolicyDefs.POLICY_ALLOW_APP_THIRD_PARTY)) {
                    mAllowAppList = val;
                } else if (name.equals(SecurityPolicyDefs.POLICY_BLOCK_APP_INROM)) {
                    mBlockAppList= val;
                }
                
            }
        //EAS IT Policy --end
        }

        if(cursor != null && !cursor.isClosed())
        	cursor.close();
    }
    // change@wtl.akulik end

    /**
     * Create from values encoded in an account flags int
     */
    public PolicySet(long flags) {
        mMinPasswordLength = (int) ((flags & PASSWORD_LENGTH_MASK) >> PASSWORD_LENGTH_SHIFT);
        mPasswordMode = (int) (flags & PASSWORD_MODE_MASK);
        mMaxPasswordFails = (int) ((flags & PASSWORD_MAX_FAILS_MASK) >> PASSWORD_MAX_FAILS_SHIFT);
        mMaxScreenLockTime = (int) ((flags & SCREEN_LOCK_TIME_MASK) >> SCREEN_LOCK_TIME_SHIFT);
        mRequireRemoteWipe = 0 != (flags & REQUIRE_REMOTE_WIPE);
        mPasswordExpirationDays = (int) ((flags & PASSWORD_EXPIRATION_MASK) >> PASSWORD_EXPIRATION_SHIFT);
        mPasswordHistory = (int) ((flags & PASSWORD_HISTORY_MASK) >> PASSWORD_HISTORY_SHIFT);
        mPasswordComplexChars = (int) ((flags & PASSWORD_COMPLEX_CHARS_MASK) >> PASSWORD_COMPLEX_CHARS_SHIFT);
        mRequireEncryption = 0 != (flags & REQUIRE_ENCRYPTION);
    }

    /**
     * Helper to map our internal encoding to DevicePolicyManager password
     * modes.
     */
    public int getDPManagerPasswordQuality() {
        switch (mPasswordMode) {
            case PASSWORD_MODE_SIMPLE:
                return DPMWraper.PASSWORD_QUALITY_NUMERIC;
            case PASSWORD_MODE_STRONG:
                return DPMWraper.PASSWORD_QUALITY_ALPHANUMERIC;
                // change@pguha: Normal password quality not set
                // TODO - James
                // case PASSWORD_MODE_REQUIRED:
                // return DevicePolicyManager.PASSWORD_QUALITY_SOMETHING;
            case PASSWORD_MODE_COMPLEX:

                return DPMWraper.PASSWORD_QUALITY_COMPLEX;
            default:
                return DPMWraper.PASSWORD_QUALITY_UNSPECIFIED;
        }
    }

    /**
     * Helper to map expiration times to the millisecond values used by
     * DevicePolicyManager.
     */
    public long getDPManagerPasswordExpirationTimeout() {
        long result = mPasswordExpirationDays * DAYS_TO_MSEC;
        return result;
    }

    /**
     * Record flags (and a sync key for the flags) into an Account Note: the
     * hash code is defined as the encoding used in Account
     * 
     * @param account to write the values mSecurityFlags and mSecuritySyncKey
     * @param syncKey the value to write into the account's mSecuritySyncKey
     * @param update if true, also writes the account back to the provider
     *            (updating only the fields changed by this API)
     * @param context a context for writing to the provider
     * @return true if the actual policies changed, false if no change (note,
     *         sync key does not affect this)
     */
    public boolean writeAccount(Account account, String syncKey, boolean update, Context context) {

        // change@pguha: IT Policy 12.0 start
        PolicySet oldPolicy = new PolicySet(context, account);
        boolean dirty = !this.equals((Object)oldPolicy);
        // change@pguha: IT Policy 12.0 end

        // change@wtl.akulik start IT Policy 12.0
        // account.mSecurityFlags = newFlags;
        account.mSecuritySyncKey = syncKey;
        if (update) {
            // change@pguha: IT Policy 12.1 start Apply Policy
            // Update the Account syncLookBack and EmailSize based on IT
            // Policy requirements
            int restrictedSize;
            boolean changedValues = false;
            int currentSize = CommonDefs.EmailDataSize.parse(account.mEmailSize).toEas12Value(); // value
            int currentSize_Roaming = CommonDefs.EmailDataSize.parse(account.mRoamingEmailSize).toEas12Value(); // value
            // in
            // Bytes

            // change@wtl.shatekar Applying maxEmailHtmlBodyTruncationSize
            // START
            // restrictedSize = mMaxEmailBodyTruncationSize; // this value
            // is in x KB
            int plainTextSize = mMaxEmailBodyTruncationSize;
            EmailLog.d(TAG, "plainTextSize = " + plainTextSize);
            int htmlSize = mMaxEmailHtmlBodyTruncationSize / 1024; // convert
                                                                   // this
                                                                   // value
                                                                   // from x
                                                                   // B to x
                                                                   // KB
            EmailLog.d(TAG, "htmlSize = " + htmlSize);
            boolean isHtmlAllowed = mAllowHTMLEmail;
            EmailLog.d(TAG, "isHtmlAllowed = " + isHtmlAllowed);
            if ((isHtmlAllowed) && (htmlSize > 0)) // is html allowed?
                restrictedSize = htmlSize;
            else
                // HTML Content Disabled or htmlBodyTruncationSize contains
                // invalid value. So use plainTextSize instead.
                restrictedSize = plainTextSize;
            // change@wtl.shatekar Applying maxEmailHtmlBodyTruncationSize
            // END

            if (restrictedSize == 0)
                restrictedSize = Integer.MAX_VALUE;
            
            if (restrictedSize < currentSize) {
            	EmailLog.e("SecurityPolicy", "Exchange IT Policy has restricted SyncSize, CurrentSize="
                        + currentSize + " Bytes, restrictedSize=" + restrictedSize + " Bytes");
                if (account.mEmailSize > 0)
                    account.mEmailSize = CommonDefs.EmailDataSize.parseToByte(restrictedSize);
                changedValues = true;
            }
            
            if (restrictedSize < currentSize_Roaming) {
            	EmailLog.e("SecurityPolicy", "Exchange IT Policy has restricted SyncSize, currentSize_Roaming="
                        + currentSize + " Bytes, restrictedSize=" + restrictedSize + " Bytes");
                if (account.mRoamingEmailSize > 0)
                    account.mRoamingEmailSize = CommonDefs.EmailDataSize.parseToByte(restrictedSize);
                changedValues = true;
            }

            int restrictedWindow = 6;
            restrictedWindow = mMaxEmailAgeFilter;
            if (restrictedWindow <= 0)
                restrictedWindow = 6;

            if (restrictedWindow < account.mSyncLookback) {
            	EmailLog.e("AccountSettings", "Exchange IT Policy has restricted SyncLookback, current:"
                        + account.mSyncLookback + ", restricted=" + restrictedWindow);
                account.mSyncLookback = restrictedWindow;
                changedValues = true;
            }

            // change@wtl.shatekar maxCalendarAge START
            restrictedWindow = mMaxCalendarAgeFilter;
            if (restrictedWindow > 0
                    && (account.mCalendarSyncLookback == Account.CALENDAR_SYNC_WINDOW_ALL || restrictedWindow < account.mCalendarSyncLookback)) {
            	EmailLog.e("AccountSettings",
                        "Exchange IT Policy has restricted CalendarSyncLookback, current:"
                                + account.mCalendarSyncLookback + ", restricted="
                                + restrictedWindow);
                account.mCalendarSyncLookback = restrictedWindow;
                changedValues = true;
            }
            // change@wtl.shatekar maxCalendarAge END

            ContentValues cv = new ContentValues();
            
            if (mPasswordRecoverable) {
                account.mFlags |= Account.FLAGS_RECOVERY_PASSWORD_FAILED;
                cv.put(AccountColumns.FLAGS, account.mFlags);
            }

            if (mRequireManualSyncWhenRoaming) {
            	SyncScheduleData ssd = account.getSyncScheduleData();
            	ssd.setRoamingSchedule(AccountValues.SyncTime.CHECK_ROAMING_MANUAL);
                cv.put(AccountColumns.ROAMING_SCHEDULE, AccountValues.SyncTime.CHECK_ROAMING_MANUAL);
            }

			// change@pguha: IT Policy 12.1 end Apply Policy
            if (account.isSaved()) {
                
                // cv.put(AccountColumns.SECURITY_FLAGS,
                // account.mSecurityFlags);
                cv.put(AccountColumns.SECURITY_SYNC_KEY, account.mSecuritySyncKey);
                // change@pguha: IT Policy 12.1 start Apply Policy
                // Update the Account syncLookBack and EmailSize based on IT
                // Policy requirements
                if (changedValues) {
                    // dr_67: pack email size field, upper 4 bits of first
                    // byte
                    cv.put(AccountColumns.IS_DEFAULT, account.mIsDefault);
                    cv.put(AccountColumns.EMAIL_SIZE, account.mEmailSize);
                    cv.put(AccountColumns.ROAMING_EMAIL_SIZE, account.mRoamingEmailSize);
                    cv.put(AccountColumns.SYNC_LOOKBACK, account.mSyncLookback);
                    // change@wtl.shatekar maxCalendarAge START
                    cv.put(AccountColumns.CALENDAR_SYNC_LOOKBACK, account.mCalendarSyncLookback);
                    // change@wtl.shatekar maxCalendarAge END
                }
                // change@pguha: IT Policy 12.1 end Apply Policy                
                account.update(context, cv);
            } else {
                account.save(context);
            }

            // PoliciesMultiplexer pm = new
            // PoliciesMultiplexer(SecurityPolicy.getContext());
            // pm.storePolicyRules(account.mId);
            storePolicyRules(context, account.mId);
        }
        // change@wtl.akulik end
        return dirty;
    }

    // change@wtl.akulik start IT Policy 12.0
    /**
     * Adds the received policies to the given account
     * 
     * @param accountId id of the account for which policies will be stored
     */
    public void storePolicyRules(Context context, long accountId) {
        synchronized (PolicySet.class) {
            EmailLog.d(TAG, "storePolicyRules() - accountId = " + accountId);
            HashMap<String, Object> values = new HashMap<String, Object>();
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            populateHash(values);
            
            synchronized (sPolicyLock) {
                // ContentResolver contentResolver = context.getContentResolver();
                // delete all policies for the given account
                removePolicyRules(context, accountId);
                EmailLog.d(TAG, " storePolicyRules: removed policies for accountId:" + accountId);
                for (Entry<String, Object> entry : values.entrySet()) {
                    if (entry.getValue() != null) {
                        Policies policy = new Policies();
                        policy.mAccountId = accountId;
                        policy.mName = entry.getKey();
                        policy.mValue = entry.getValue().toString();
                        if (EmailLog.USER_LOG)
                            EmailLog.d("SecurityPolicy", "policy Name:" + policy.mName + ", policyValue:"
                                    + policy.mValue);
                        // TODO: Protect
                        policy.mType = entry.getValue().getClass().getSimpleName();
                    	ContentProviderOperation.Builder opsBuilder 
                            = ContentProviderOperation.newInsert(Policies.CONTENT_URI).withValues(policy.toContentValues());
                    	ops.add(opsBuilder.build());
                        //contentResolver.insert(Policies.CONTENT_URI, policy.toContentValues());
                    }
                }
            }
            try {
				context.getContentResolver().applyBatch(EmailContent.AUTHORITY, ops);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OperationApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    /**
     * Removes policies from the given account
     */
    public void removePolicyRules(Context context, long accountId) {
        context.getContentResolver().delete(Policies.CONTENT_URI,
                PoliciesColumns.ACCOUNT_ID + "=?", new String[] {
                    String.valueOf(accountId)
                });
    }

    /**
     * Puts the values from the set into the hash map.
     * 
     * @param values hash map to be filled with policy set values
     */
    private void populateHash(HashMap<String, Object> values) {
        values.put(SecurityPolicyDefs.POLICY_PASSWORD_MODE, mPasswordMode);

        if (mPasswordMode != PASSWORD_MODE_NONE) {
            values.put(SecurityPolicyDefs.POLICY_PASSWORD_RECOVERY_ENABLED, mPasswordRecoverable);
            values.put(SecurityPolicyDefs.POLICY_MAX_DEVICE_PASSWORD_FAILED_ATTEMPTS, mMaxPasswordFails);
            values.put(SecurityPolicyDefs.POLICY_DEVICE_PASSWORD_EXPIRATION, mPasswordExpirationDays);
            values.put(SecurityPolicyDefs.POLICY_DEVICE_PASSWORD_HISTORY, mPasswordHistory);
            // change@pguha: IT Policy 12.1
            values.put(SecurityPolicyDefs.POLICY_MIN_PASSWORD_COMPLEX_CHARS, mPasswordComplexChars);
            values.put(SecurityPolicyDefs.POLICY_MIN_DEVICE_PASSWORD_LENGTH, mMinPasswordLength);
            values.put(SecurityPolicyDefs.POLICY_REQUIRE_DEVICE_ENCRYPTION, mRequireEncryption);
        }

        values.put(SecurityPolicyDefs.POLICY_MAX_ATTACHMENT_SIZE, mMaxAttachmentSize);
        values.put(SecurityPolicyDefs.POLICY_ATTACHMENTS_ENABLED, mAttachmentsEnabled);

        values.put(SecurityPolicyDefs.POLICY_SIMPLE_PASSWORD_ENABLED, mSimplePasswordEnabled);
        values.put(SecurityPolicyDefs.POLICY_DEVICE_ENCRYPTION_ENABLED, mDeviceEncryptionEnabled);
        int inactivityTime = mMaxScreenLockTime;
        // according to the documentation - if time is greater or equal to
        // 9999 treat is as 0
        values.put(SecurityPolicyDefs.POLICY_MAX_INACTIVITY_TIME, inactivityTime >= 9999 ? 0 : inactivityTime);
        values.put(SecurityPolicyDefs.POLICY_REMOTE_WIPE_REQUIRED, mRequireRemoteWipe);
        // change@pguha: IT Policy 12.1 start
        values.put(SecurityPolicyDefs.POLICY_ALLOW_STORAGE_CARD, mAllowStorageCard);
        values.put(SecurityPolicyDefs.POLICY_ALLOW_CAMERA, mAllowCamera);
        values.put(SecurityPolicyDefs.POLICY_ALLOW_WIFI, mAllowWifi);
        values.put(SecurityPolicyDefs.POLICY_ALLOW_TEXT_MESSAGING, mAllowTextMessaging);
        values.put(SecurityPolicyDefs.POLICY_ALLOW_POPIMAP_EMAIL, mAllowPOPIMAPEmail);
        values.put(SecurityPolicyDefs.POLICY_ALLOW_HTML_EMAIL, mAllowHTMLEmail);
        values.put(SecurityPolicyDefs.POLICY_ALLOW_BROWSER, mAllowBrowser);
        values.put(SecurityPolicyDefs.POLICY_ALLOW_INTERNET_SHARING, mAllowInternetSharing);
        values.put(SecurityPolicyDefs.POLICY_REQUIRE_MANUALSYNC_ROAMING, mRequireManualSyncWhenRoaming);
        values.put(
                SecurityPolicyDefs.POLICY_ALLOW_BLUETOOTH,
                (mAllowBluetoothMode < ALLOW_BLUETOOTH_MODE_VALUE_DISABLE || mAllowBluetoothMode > ALLOW_BLUETOOTH_MODE_VALUE_ALLOW) ? ALLOW_BLUETOOTH_MODE_VALUE_ALLOW
                        : mAllowBluetoothMode);
        values.put(SecurityPolicyDefs.POLICY_MAX_CALENDAR_AGE_FILTER,
                (mMaxCalendarAgeFilter < 0 || mMaxCalendarAgeFilter > 7) ? 0
                        : mMaxCalendarAgeFilter);
        values.put(SecurityPolicyDefs.POLICY_MAX_EMAIL_AGE_FILTER,
                (mMaxEmailAgeFilter < 0 || mMaxEmailAgeFilter > 7) ? 0 : mMaxEmailAgeFilter);
        values.put(SecurityPolicyDefs.POLICY_MAX_EMAIL_BODY_TRUNC_SIZE, (mMaxEmailBodyTruncationSize < 0) ? 0
                : mMaxEmailBodyTruncationSize);
        values.put(SecurityPolicyDefs.POLICY_MAX_EMAILHTML_BODY_TRUNC_SIZE, (mMaxEmailHtmlBodyTruncationSize < 0) ? 0
                : mMaxEmailHtmlBodyTruncationSize);
        values.put(SecurityPolicyDefs.POLICY_REQUIRE_SIGNED_SMIME_MSGS, mRequireSignedSMIMEMessages);
        values.put(SecurityPolicyDefs.POLICY_REQUIRE_ENCRYPTED_SMIME_MSGS, mRequireEncryptedSMIMEMessages);
        values.put(SecurityPolicyDefs.POLICY_REQUIRE_SIGNED_SMIME_ALGORITHM, mRequireSignedSMIMEAlgorithm);
        values.put(SecurityPolicyDefs.POLICY_REQUIRE_ENCRYPTION_SMIME_ALGORITHM, mRequireEncryptionSMIMEAlgorithm);
        values.put(SecurityPolicyDefs.POLICY_ALLOW_SMIME_ENCRYPTION_ALGO_NEGOTIATION,
                mAllowSMIMEEncryptionAlgorithmNegotiation);
        values.put(SecurityPolicyDefs.POLICY_ALLOW_SMIME_SOFT_CERTS, mAllowSMIMESoftCerts);
        values.put(SecurityPolicyDefs.POLICY_ALLOW_DESKTOP_SYNC, mAllowDesktopSync);
        values.put(SecurityPolicyDefs.POLICY_ALLOW_IRDA, mAllowIrDA);
        // change@pguha: IT Policy 12.1 end
        //EAS IT Policy --start        
        values.put(SecurityPolicyDefs.POLICY_ALLOW_APP_THIRD_PARTY, mAllowAppList);
        values.put(SecurityPolicyDefs.POLICY_BLOCK_APP_INROM, mBlockAppList);
        values.put(SecurityPolicyDefs.POLICY_ALLOW_UNSIGNED_APPS, mAllowUnsignedApp);
        values.put(SecurityPolicyDefs.POLICY_ALLOW_UNSIGNED_INSTALLATION_PACKAGES, mAllowUnsignedInstallationPkg);
       //EAS IT Policy --end
    }

    // change@wtl.akulik end
    /**
     * Supports Parcelable
     */
    public int describeContents() {
        return 0;
    }

    /**
     * Supports Parcelable
     */
    public static final Parcelable.Creator<PolicySet> CREATOR = new Parcelable.Creator<PolicySet>() {
        public PolicySet createFromParcel(Parcel in) {
            return new PolicySet(in);
        }

        public PolicySet[] newArray(int size) {
            return new PolicySet[size];
        }
    };

    /**
     * Supports Parcelable
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mMinPasswordLength);
        dest.writeInt(mPasswordMode);
        dest.writeInt(mMaxPasswordFails);
        dest.writeInt(mMaxScreenLockTime);
        dest.writeInt(mRequireRemoteWipe ? 1 : 0);
        dest.writeInt(mPasswordExpirationDays);
        dest.writeInt(mPasswordHistory);
        dest.writeInt(mPasswordComplexChars);
        dest.writeInt(mPasswordRecoverable ? 1 : 0);
        dest.writeInt(mRequireEncryption ? 1 : 0);
        dest.writeInt(mDeviceEncryptionEnabled ? 1 : 0);

        dest.writeInt(mSimplePasswordEnabled ? 1 : 0);

        // below item will be treated as Account specific policy
        dest.writeInt(mAttachmentsEnabled ? 1 : 0);
        dest.writeInt(mMaxAttachmentSize);
        dest.writeInt(mMaxEmailBodyTruncationSize);
        dest.writeInt(mMaxEmailHtmlBodyTruncationSize);
        dest.writeInt(mAllowHTMLEmail ? 1 : 0);
        dest.writeInt(mMaxCalendarAgeFilter);
        dest.writeInt(mMaxEmailAgeFilter);
        dest.writeInt(mRequireManualSyncWhenRoaming ? 1 : 0);
        dest.writeInt(mRequireSignedSMIMEMessages ? 1 : 0);
        dest.writeInt(mRequireEncryptedSMIMEMessages ? 1 : 0);
        dest.writeInt(mRequireSignedSMIMEAlgorithm);
        dest.writeInt(mRequireEncryptionSMIMEAlgorithm);
        dest.writeInt(mAllowSMIMEEncryptionAlgorithmNegotiation);

        // below item will be treated as Device policy
        dest.writeInt(mAllowStorageCard ? 1 : 0);
        dest.writeInt(mAllowCamera ? 1 : 0);
        dest.writeInt(mAllowWifi ? 1 : 0);
        dest.writeInt(mAllowTextMessaging ? 1 : 0);
        dest.writeInt(mAllowPOPIMAPEmail ? 1 : 0);
        dest.writeInt(mAllowBrowser ? 1 : 0);
        dest.writeInt(mAllowInternetSharing ? 1 : 0);
        dest.writeInt(mAllowSMIMESoftCerts ? 1 : 0);
        dest.writeInt(mAllowDesktopSync ? 1 : 0);
        dest.writeInt(mAllowIrDA ? 1 : 0);
        dest.writeInt(mAllowBluetoothMode);
        //EAS IT Policy --start
        dest.writeString(mAllowAppList);
        dest.writeString(mBlockAppList);
        dest.writeInt(mAllowUnsignedApp ? 1 : 0);
        dest.writeInt(mAllowUnsignedInstallationPkg ? 1 : 0);
        //EAS IT Policy --end
        
        dest.writeString(mSecuritySyncKey);
    }

    /**
     * Supports Parcelable
     */
    public PolicySet(Parcel in) {
        mMinPasswordLength = in.readInt();
        mPasswordMode = in.readInt();
        mMaxPasswordFails = in.readInt();
        mMaxScreenLockTime = in.readInt();
        mRequireRemoteWipe = in.readInt() == 1;
        mPasswordExpirationDays = in.readInt();
        mPasswordHistory = in.readInt();
        mPasswordComplexChars = in.readInt();
        mPasswordRecoverable = in.readInt() == 1;
        mRequireEncryption = in.readInt() == 1;
        mDeviceEncryptionEnabled = in.readInt() == 1;
        mSimplePasswordEnabled = in.readInt() == 1;
        // below item will be treated as Account specific policy
        mAttachmentsEnabled = in.readInt() == 1;
        mMaxAttachmentSize = in.readInt();
        mMaxEmailBodyTruncationSize = in.readInt();
        mMaxEmailHtmlBodyTruncationSize = in.readInt();
        mAllowHTMLEmail = in.readInt() == 1;
        mMaxCalendarAgeFilter = in.readInt();
        mMaxEmailAgeFilter = in.readInt();
        mRequireManualSyncWhenRoaming = in.readInt() == 1;
        mRequireSignedSMIMEMessages = in.readInt() == 1;
        mRequireEncryptedSMIMEMessages = in.readInt() == 1;
        mRequireSignedSMIMEAlgorithm = in.readInt();
        mRequireEncryptionSMIMEAlgorithm = in.readInt();
        mAllowSMIMEEncryptionAlgorithmNegotiation = in.readInt();

        // below item will be treated as Device policy
        mAllowStorageCard = in.readInt() == 1;
        mAllowCamera = in.readInt() == 1;
        mAllowWifi = in.readInt() == 1;
        mAllowTextMessaging = in.readInt() == 1;
        mAllowPOPIMAPEmail = in.readInt() == 1;
        mAllowBrowser = in.readInt() == 1;
        mAllowInternetSharing = in.readInt() == 1;
        mAllowSMIMESoftCerts = in.readInt() == 1;
        mAllowDesktopSync = in.readInt() == 1;
        mAllowIrDA = in.readInt() == 1;
        mAllowBluetoothMode = in.readInt();
        //EAS IT Policy --start
        mAllowAppList = in.readString();
        mBlockAppList = in.readString();
        mAllowUnsignedApp = in.readInt() == 1 ;
        mAllowUnsignedInstallationPkg = in.readInt() == 1 ;
        //EAS IT Policy --end
        
        mSecuritySyncKey = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }    public boolean equals(PolicySet policies) {        if(policies == null )            return false;        if(policies == this)             return true;        return policies.mAllowBrowser == mAllowBrowser            && policies.mAllowCamera == mAllowCamera            && policies.mAllowDesktopSync == mAllowDesktopSync            && policies.mAllowHTMLEmail == mAllowHTMLEmail            && policies.mAllowInternetSharing == mAllowInternetSharing            && policies.mAllowIrDA == mAllowIrDA            && policies.mAllowPOPIMAPEmail == mAllowPOPIMAPEmail            && policies.mAllowSMIMESoftCerts == mAllowSMIMESoftCerts            && policies.mMinPasswordLength == mMinPasswordLength            && policies.mPasswordMode == mPasswordMode            && policies.mMaxPasswordFails == mMaxPasswordFails            && policies.mMaxScreenLockTime == mMaxScreenLockTime            && policies.mRequireRemoteWipe == mRequireRemoteWipe            && policies.mPasswordExpirationDays == mPasswordExpirationDays            && policies.mPasswordHistory == mPasswordHistory            && policies.mPasswordComplexChars == mPasswordComplexChars            && policies.mRequireEncryption == mRequireEncryption            && policies.mDeviceEncryptionEnabled == mDeviceEncryptionEnabled            && policies.mPasswordRecoverable == mPasswordRecoverable            && policies.mAttachmentsEnabled == mAttachmentsEnabled            && policies.mMaxAttachmentSize == mMaxAttachmentSize            && policies.mAllowStorageCard == mAllowStorageCard            && policies.mAllowWifi == mAllowWifi            && policies.mAllowTextMessaging == mAllowTextMessaging            && policies.mRequireManualSyncWhenRoaming == mRequireManualSyncWhenRoaming            && policies.mAllowBluetoothMode == mAllowBluetoothMode            && policies.mMaxCalendarAgeFilter == mMaxCalendarAgeFilter            && policies.mMaxEmailAgeFilter == mMaxEmailAgeFilter            && policies.mMaxEmailBodyTruncationSize == mMaxEmailBodyTruncationSize            && policies.mMaxEmailHtmlBodyTruncationSize == mMaxEmailHtmlBodyTruncationSize            && policies.mRequireSignedSMIMEMessages == mRequireSignedSMIMEMessages            && policies.mRequireEncryptedSMIMEMessages == mRequireEncryptedSMIMEMessages            && policies.mAllowSMIMEEncryptionAlgorithmNegotiation == mAllowSMIMEEncryptionAlgorithmNegotiation            && policies.mAllowSMIMESoftCerts == mAllowSMIMESoftCerts            && policies.mAllowDesktopSync == mAllowDesktopSync            && policies.mAllowUnsignedApp == mAllowUnsignedApp            && policies.mAllowUnsignedInstallationPkg == mAllowUnsignedInstallationPkg            && policies.mAllowAppList.equals(mAllowAppList)            && policies.mBlockAppList.equals(mBlockAppList)            && policies.mSimplePasswordEnabled == mSimplePasswordEnabled;    }
    @Override
    public int hashCode() {
        long code = getSecurityCode();
        return (int) code;
    }

    public long getSecurityCode() {
        long flags = 0;
        flags = (long) mMinPasswordLength << PASSWORD_LENGTH_SHIFT;
        flags |= mPasswordMode;
        flags |= (long) mMaxPasswordFails << PASSWORD_MAX_FAILS_SHIFT;
        flags |= (long) mMaxScreenLockTime << SCREEN_LOCK_TIME_SHIFT;
        if (mRequireRemoteWipe)
            flags |= REQUIRE_REMOTE_WIPE;
        flags |= (long) mPasswordHistory << PASSWORD_HISTORY_SHIFT;
        flags |= (long) mPasswordExpirationDays << PASSWORD_EXPIRATION_SHIFT;
        flags |= (long) mPasswordComplexChars << PASSWORD_COMPLEX_CHARS_SHIFT;
        if (mRequireEncryption)
            flags |= REQUIRE_ENCRYPTION;
        return flags;
    }

    @Override
    public String toString() {
        return "{ " + "pw-len-min=" + mMinPasswordLength + ", pw-mode=" + mPasswordMode
                + ", pw-fails-max=" + mMaxPasswordFails + ", screenlock-max=" + mMaxScreenLockTime
                + ", remote-wipe-req=" + mRequireRemoteWipe + ", pw-expiration="
                + mPasswordExpirationDays + ", pw-history=" + mPasswordHistory
                + ", pw-complex-chars=" + mPasswordComplexChars + ", recoveryEnabled="
                + mPasswordRecoverable + ", require-encryption=" + mRequireEncryption
                + ", require-SD-encryption=" + mDeviceEncryptionEnabled + ", attachmentsEnabled="
                + mAttachmentsEnabled + ", maxAttachmentsSize=" + mMaxAttachmentSize
                + ", allowHTML=" + mAllowHTMLEmail + ", requireManualSyncWhenRoaming="
                + mRequireManualSyncWhenRoaming + ", maxCalendarAgeFilter=" + mMaxCalendarAgeFilter
                + ", maxEmailAgeFilter=" + mMaxEmailAgeFilter + ", maxEmailBodyTruncationSize="
                + mMaxEmailBodyTruncationSize + ", maxEmailHtmlBodyTruncationSize= "
                + mMaxEmailHtmlBodyTruncationSize + ", requireSignMessage= "
                + mRequireSignedSMIMEMessages + ", requireEncryptMessage= "
                + mRequireEncryptedSMIMEMessages + ", allowEncryptionNegotiation= "
                + mAllowSMIMEEncryptionAlgorithmNegotiation + ", signAlgorithm= "
                + mRequireSignedSMIMEAlgorithm + ", encryptAlgorithm= "
                + mRequireEncryptionSMIMEAlgorithm + ", allowCamera= " + mAllowCamera
                + ", allowSDcard= " + mAllowStorageCard + ", allowWiFi= " + mAllowWifi
                + ", allowSMS= " + mAllowTextMessaging + ", allowPopImap= " + mAllowPOPIMAPEmail
                + ", allowIrDA= " + mAllowIrDA + ", allowBrowser= " + mAllowBrowser
                + ", allowInternetSharing= " + mAllowInternetSharing + ", allowBT= "
                + mAllowBluetoothMode + ", allowKIES= " + mAllowDesktopSync
                + ", allowSMIMEsoftCert=" + mAllowSMIMESoftCerts
			    + ", mAllowAppList= " + mAllowAppList + ", mBlockAppList= " 
				+ mBlockAppList + ", mAllowUnsignedApp= " + mAllowUnsignedApp 
				+ ", mAllowUnsignedInstallationPkg= " + mAllowUnsignedInstallationPkg + "}";//EAS IT Policy 
    }
    
    public String getSecuritySyncKey() {
		return mSecuritySyncKey;
	}
}
