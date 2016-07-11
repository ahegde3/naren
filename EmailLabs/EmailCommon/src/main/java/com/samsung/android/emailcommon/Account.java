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

package com.samsung.android.emailcommon;

import android.content.Context;
import android.net.Uri;

import com.samsung.android.emailcommon.crypto.AESEncryptionUtil;
import com.samsung.android.emailcommon.provider.AccountValues;
import com.samsung.android.emailcommon.service.PolicySet;
import com.samsung.android.emailcommon.system.CarrierValues;
import com.samsung.android.emailcommon.utility.SecFeatureWrapper;
import com.samsung.android.emailcommon.utility.Utility;

import java.util.UUID;

/**
 * Account stores all of the settings for a single account defined by the user.
 * It is able to save and delete itself given a Preferences to work with. Each
 * account is defined by a UUID.
 */
public class Account {

    // transient values - do not serialize
    // huijae.lee not used private transient Preferences mPreferences;

    // serialized values
    public String mUuid;

    public String mStoreUri;

    public String mLocalStoreUri;

    public String mSenderUri;

    public String mDescription;

    public String mName;

    public String mPasswd;

    public String mSendAddr;

    public int mSendPort;

    public String mEmail;

    public int mAutomaticCheckIntervalMinutes;

    public long mLastAutomaticCheckTime;

    public boolean mNotifyNewMail;

    public String mDraftsFolderName;

    public String mSentFolderName;

    public String mTrashFolderName;

    public String mOutboxFolderName;

    public int mAccountNumber;

    public boolean mVibrate; // true: Always vibrate. false: Only when
                      // mVibrateWhenSilent.

    public boolean mVibrateWhenSilent; // true: Vibrate even if !mVibrate. False:
                                // Require mVibrate.

    public String mRingtoneUri;

    public int mSyncWindow;

    public int mBackupFlags; // for account backups only

    public String mProtocolVersion; // for account backups only

    public long mSecurityFlags; // for account backups only
    public int mSendSecurityFlags;

    public String mSecurityAuth;

    public boolean mAddSignature;

    public String mSignature; // for account backups only

    public String mCapabilities;
    public String mSenderCapabilities;

    // ==========================================================
    // Seven �߰�(2010.07.20)
    // ==========================================================
    public long mAccountKey;

    public long mSevenAccountKey;

    public int mTypeMsg;

    public int mFlagSyncContact;

    public int mFlagSyncCalendar;

    public int mFlagSyncNotes; //NotesSync

    public long mTimeLimit;

    public long mSizeLimit;

    // ==========================================================

    /**
     * <pre>
     * 0 Never 
     * 1 After 7 days 
     * 2 When I delete from inbox
     * </pre>
     */
    public int mDeletePolicy;

    // SVL - Start - j.sb
    public String mSyncScheduleData;
    public int mMessageFormat;
    public int mAccountType;
    // SVL - End - j.sb
    // MDM intent parameter for EAS Setup >>>>>>
    public int mSyncLookback;
    public int mCalendarSyncLookback;
    public int mEmailSize;
    public int mRoamingEmailSize;
    public boolean mIsDefault;
    public String mUpdateAccount;
    public String mServerName;
    // MDM intent parameter for EAS Setup <<<<<<
    /**
     * All new fields should have named keys
     */

    public int mRecentMessages;

    private static final String KEY_SYNC_WINDOW = ".syncWindow";

    private static final String KEY_BACKUP_FLAGS = ".backupFlags";

    private static final String KEY_PROTOCOL_VERSION = ".protocolVersion";

    private static final String KEY_SECURITY_FLAGS = ".securityFlags";
    private static final String KEY_SEND_SECURITY_FLAGS = ".sendSecurityFlags";
    private static final String KEY_SECURITY_AUTH = ".securityAuth";

    private static final String KEY_SIGNATURE = ".signature";

    private static final String KEY_VIBRATE_WHEN_SILENT = ".vibrateWhenSilent";

    private static final String KEY_CAPABILITITIES = ".capabilities";
    private static final String KEY_SENDER_CAPABILITITIES = ".senderCapabilities";

    // SVL - Start - J.sb
    private static final String KEY_SYNC_SCHEDULE_DATA = ".syncScheduleData";
    private static final String KEY_MESSAGE_FORMAT = ".messageFormat";
    private static final String KEY_ACCOUNT_TYPE = ".accountType";
    public static final int CHECK_INTERVAL_NEVER = -1;
    public static final int CHECK_INTERVAL_PUSH = -2;
    // SVL - End - J.sb
    
    private static final String KEY_ACCOUNT_FLAGS = ".accountFlagsbkp";
    private static final String KEY_FORWARD_WITH_FILES = ".flagForwardWithFiles";
    private static final String KEY_SHOW_IMAGES = ".flagShowImages";
    private static final String KEY_AUTO_DOWNLOAD = ".autoDownload";    
    private String KEY_SMIME_OWN_ENCRYPT_CERT_ALIAS  = ".smimeOwnCertificateAlias";
    private String KEY_SMIME_OWN_SIGN_CERT_ALIAS = ".smimeOwnSignCertAlias";
    private String KEY_SMIME_ENCRYPT_ALL  = ".mimeEncryptAll";
    private String KEY_SMIME_SIGN_ALL  = ".smimeSignAll";
    private String KEY_SMIME_SIGN_ALGORITHM  = ".smimeSignAlgorithm";
    private String KEY_SMIME_ENCRYPT_ALGORITHM  = ".smimeEncryptionAlgorithm";
    private String KEY_AUTO_RETRY  = ".autoRetryTimes";
    private String KEY_CONFLICT_FLAGS  = ".conflictFlags";
    private String KEY_CONVERSATION_MODE = ".conversationMode";
    private String KEY_TEXT_PREVIEW = ".textPreviewSize";
    private static final String KEY_DOWNLOAD_ON_SCROLL = ".flagDownloadOnScroll";
    private static final String KEY_IS_SIGNATURE_EDITED = ".flagIsSignatureEdited";
    private static final String KEY_SPAM_FOLDER_KEY = ".flagIsSpamfolderkey";
    private static final String KEY_DEFAULT_EMAIL_SIZE = ".flagIsDefaultEmailSize";

    private static final String KEY_RECENT_MESSAGES = ".recentMessages";

    int mFlagsBkp;
    boolean mForwardWithFiles;
    boolean mShowImages;
    boolean mAutoDownload;
    String mSmimeOwnEncryptCertAlias;
    String mSmimeOwnSignCertAlias;
    boolean mSmimeEncryptAll;
    boolean mSmimeSignAll;
    int mSmimeSignAlgorithm;
    int mSmimeEncryptionAlgorithm;
    int mAutoRetryTimes;
    int mConflictFlags;
    int mConversationMode;
    int mTextPreviewSize;
    boolean mDownloadOnScroll;
    private boolean mIsSignatureEdited;
    private long mSpamfolderkey;
    boolean mIsDefaultEmailSize;
    
    private PolicySet mPolicySet;

    // MDM intent parameter for EAS Setup >>>>>>
    private static final String KEY_EAS_LOOKBACK = ".lookback";
    private static final String KEY_EAS_RETRIVALSIZE = ".retrival";
    private static final String KEY_EAS_RETRIVALSIZE_ROAMING = ".retrival_roaming";
    private static final String KEY_EAS_CALENDARLOOKBACK = ".callookback";
    private static final String KEY_EAS_SYNCCONTACTS = ".synccontacts";
    private static final String KEY_EAS_SYNCCALENDAR = ".synccalendar";
    private static final String KEY_EAS_SYNCNOTES = ".syncnotes";
    private static final String DEFAULT_ACCOUNT = ".defaultaccount";
    private static final String UPDATE_ACCOUNT = ".updateaccount";
    private static final String SERVER_NAME = ".servername";
    // MDM intent parameter for EAS Setup <<<<<<

    public Account(Context context) {
        // TODO Change local store path to something readable / recognizable
        mUuid = UUID.randomUUID().toString();
        mLocalStoreUri = "local://localhost/" + context.getDatabasePath(mUuid + ".db");
        mAutomaticCheckIntervalMinutes = -1;
        mAccountNumber = -1;
        mNotifyNewMail = true;
        mVibrate = false;
        mVibrateWhenSilent = false;
        mRingtoneUri = CarrierValues.DEFAULT_RINGTONEURI;
        mSyncWindow = AccountValues.SyncWindow.SYNC_WINDOW_USER; // IMAP & POP3
        mBackupFlags = 0;
        mProtocolVersion = null;
        mSecurityFlags = 0;
        mSendSecurityFlags = 0;
        mSecurityAuth = null;
        mAddSignature = true;
        mSignature = null;
        // SVL - Start - j.sb
        mSyncScheduleData = null;
        mMessageFormat = 0;
        mAccountType = 0xFF; // invalid
        // SVL - End - j.sb
        // MDM intent parameter for EAS Setup >>>>>>
        mSyncLookback = 0;
        mCalendarSyncLookback = 0;
        mEmailSize = 0;
        mFlagSyncContact = 0;
        mFlagSyncCalendar = 0;
        mFlagSyncNotes = 0;//NotesSync
        mIsDefault = false;
        mUpdateAccount = null;
        // MDM intent parameter for EAS Setup <<<<<<
        mPolicySet = null;

        mDownloadOnScroll = true;
        mIsSignatureEdited = true;
        mSpamfolderkey = 0;
        mIsDefaultEmailSize = true;

        mCapabilities = null;
        mSenderCapabilities = null;

        mRecentMessages = 0;
    }

    public Account(Preferences preferences, String uuid) {
        this.mUuid = uuid;
        refresh(preferences);
    }

    /**
     * Refresh the account from the stored settings.
     */
    private void refresh(Preferences preferences) {// sec.email tom.jung
                                                   // 20100908 For SM's review.
        mStoreUri = Utility.base64Decode(preferences.getStringAccountInformation(mUuid
                + ".storeUri", null));
        mLocalStoreUri = preferences.getStringAccountInformation(mUuid + ".localStoreUri", null);

        String senderText = preferences.getStringAccountInformation(mUuid + ".senderUri", null);
        if (senderText == null) {
            // Preference ".senderUri" was called ".transportUri" in earlier
            // versions, so we'll
            // do a simple upgrade here when necessary.
            senderText = preferences.getStringAccountInformation(mUuid + ".transportUri", null);
        }
        mSenderUri = Utility.base64Decode(senderText);

        mDescription = preferences.getStringAccountInformation(mUuid + ".description", null);
        mName = preferences.getStringAccountInformation(mUuid + ".name", mName);
        mPasswd = AESEncryptionUtil.AESDecryption(preferences.getStringAccountInformation(mUuid + ".passwd", mPasswd));
        mSendAddr = preferences.getStringAccountInformation(mUuid + ".sendaddr", mSendAddr);
        mSendPort = preferences.getIntAccountInformation(mUuid + ".sendport", mSendPort);
        mEmail = preferences.getStringAccountInformation(mUuid + ".email", mEmail);
        mAutomaticCheckIntervalMinutes = preferences.getIntAccountInformation(mUuid
                + ".automaticCheckIntervalMinutes", -1);
        mLastAutomaticCheckTime = preferences.getLongAccountInformation(mUuid
                + ".lastAutomaticCheckTime", (long)0);
        mNotifyNewMail = preferences.getBooleanAccountInformation(mUuid + ".notifyNewMail", false);

        // delete policy was incorrectly set on earlier versions, so we'll
        // upgrade it here.
        // rule: if IMAP account and policy = 0 ("never"), change policy to 2
        // ("on delete")
        mDeletePolicy = preferences.getIntAccountInformation(mUuid + ".deletePolicy", 0);
        if (mDeletePolicy == AccountRefs.DELETE_POLICY_NEVER && mStoreUri != null
                && mStoreUri.startsWith("imap")) {
            mDeletePolicy = AccountRefs.DELETE_POLICY_ON_DELETE;
        }

        mDraftsFolderName = preferences.getStringAccountInformation(mUuid + ".draftsFolderName",
                "Drafts");
        mSentFolderName = preferences.getStringAccountInformation(mUuid + ".sentFolderName",
                "Sent");
        mTrashFolderName = preferences.getStringAccountInformation(mUuid + ".trashFolderName",
                "Trash");
        mOutboxFolderName = preferences.getStringAccountInformation(mUuid + ".outboxFolderName",
                "Outbox");
        mAccountNumber = preferences.getIntAccountInformation(mUuid + ".accountNumber", 0);
        mVibrate = preferences.getBooleanAccountInformation(mUuid + ".vibrate", false);
        mVibrateWhenSilent = preferences.getBooleanAccountInformation(mUuid
                + KEY_VIBRATE_WHEN_SILENT, false);
        String defaultRingtone = CarrierValues.DEFAULT_RINGTONEURI;
        mRingtoneUri = preferences.getStringAccountInformation(mUuid + ".ringtone", defaultRingtone );
        mSyncWindow = preferences.getIntAccountInformation(mUuid + KEY_SYNC_WINDOW,
                AccountValues.SyncWindow.SYNC_WINDOW_USER);

        mBackupFlags = preferences.getIntAccountInformation(mUuid + KEY_BACKUP_FLAGS, 0);
        mProtocolVersion = preferences.getStringAccountInformation(mUuid + KEY_PROTOCOL_VERSION,
                null);
        // Wrap this in a try/catch, as this preference was formerly saved as an
        // int (the value no
        // longer fits in an int, and is now stored as a long)
        try {
            mSecurityFlags = preferences.getLongAccountInformation(mUuid + KEY_SECURITY_FLAGS, (long)0);
        } catch (ClassCastException e) {
            mSecurityFlags = preferences.getIntAccountInformation(mUuid + KEY_SECURITY_FLAGS, 0);
        }
        mSendSecurityFlags = preferences.getIntAccountInformation(mUuid + KEY_SEND_SECURITY_FLAGS,
                0);
        mSecurityAuth = preferences.getStringAccountInformation(mUuid + KEY_SECURITY_AUTH, null);

        mAddSignature = preferences.getBooleanAccountInformation(mUuid + ".addsignature", false);
        mSignature = preferences.getStringAccountInformation(mUuid + KEY_SIGNATURE, null);

        mCapabilities = preferences.getStringAccountInformation(mUuid + KEY_CAPABILITITIES, null);
        mSenderCapabilities = preferences.getStringAccountInformation(mUuid + KEY_SENDER_CAPABILITITIES, null);
        // SVL - Start - J.sb
        mSyncScheduleData = preferences.getStringAccountInformation(
                mUuid + KEY_SYNC_SCHEDULE_DATA, null);
        mMessageFormat = preferences.getIntAccountInformation(mUuid + KEY_MESSAGE_FORMAT, 0);
        mAccountType = preferences.getIntAccountInformation(mUuid + KEY_ACCOUNT_TYPE, 0);
        // SVL - End - J.sb

        mFlagsBkp = preferences.getIntAccountInformation(mUuid + KEY_ACCOUNT_FLAGS, 0);
        mForwardWithFiles = true;
        mShowImages = preferences.getBooleanAccountInformation(mUuid + KEY_SHOW_IMAGES, 
                SecFeatureWrapper.getCarrierId()==SecFeatureWrapper.CARRIER_USC ? true : false);
        mAutoDownload = preferences.getBooleanAccountInformation(mUuid + KEY_AUTO_DOWNLOAD, false);
        mSmimeOwnEncryptCertAlias = preferences.getStringAccountInformation(mUuid + KEY_SMIME_OWN_ENCRYPT_CERT_ALIAS, null);
        mSmimeOwnSignCertAlias = preferences.getStringAccountInformation(mUuid + KEY_SMIME_OWN_SIGN_CERT_ALIAS, null);
        mSmimeEncryptAll = preferences.getBooleanAccountInformation(mUuid + KEY_SMIME_ENCRYPT_ALL, false);
        mSmimeSignAll = preferences.getBooleanAccountInformation(mUuid + KEY_SMIME_SIGN_ALL, false);
        mSmimeSignAlgorithm = preferences.getIntAccountInformation(mUuid + KEY_SMIME_SIGN_ALGORITHM, 0);
        mSmimeEncryptionAlgorithm = preferences.getIntAccountInformation(mUuid + KEY_SMIME_ENCRYPT_ALGORITHM, 0);
        mAutoRetryTimes = preferences.getIntAccountInformation(mUuid + KEY_AUTO_RETRY, 0);
        mConflictFlags = preferences.getIntAccountInformation(mUuid + KEY_CONFLICT_FLAGS, 0);
        mConversationMode = preferences.getIntAccountInformation(mUuid + KEY_CONVERSATION_MODE , 0);
        mTextPreviewSize = preferences.getIntAccountInformation(mUuid + KEY_TEXT_PREVIEW , 0);

        // MDM intent parameter for EAS Setup >>>>>>       
        mSyncLookback = preferences.getIntAccountInformation(mUuid + KEY_EAS_LOOKBACK, -1);
        mCalendarSyncLookback = preferences.getIntAccountInformation(mUuid + KEY_EAS_CALENDARLOOKBACK, -1);
        mEmailSize = preferences.getIntAccountInformation(mUuid + KEY_EAS_RETRIVALSIZE, -1);
        mRoamingEmailSize = preferences.getIntAccountInformation(mUuid + KEY_EAS_RETRIVALSIZE_ROAMING, -1);
        mIsDefault = preferences.getBooleanAccountInformation(mUuid + DEFAULT_ACCOUNT, false);
        mUpdateAccount = preferences.getStringAccountInformation(mUuid + UPDATE_ACCOUNT, null);
        if (mUpdateAccount != null) {
            mFlagSyncContact = preferences.getIntAccountInformation(mUuid + KEY_EAS_SYNCCONTACTS, 0);
            mFlagSyncCalendar = preferences.getIntAccountInformation(mUuid + KEY_EAS_SYNCCALENDAR, 0);
            mFlagSyncNotes = preferences.getIntAccountInformation(mUuid + KEY_EAS_SYNCNOTES, 0); //NotesSync
            mServerName = preferences.getStringAccountInformation(mUuid + SERVER_NAME, null);
        }
        // MDM intent parameter for EAS Setup <<<<<<
        mDownloadOnScroll = preferences.getBooleanAccountInformation(mUuid + KEY_DOWNLOAD_ON_SCROLL, true);
        mIsSignatureEdited = preferences.getBooleanAccountInformation(mUuid + KEY_IS_SIGNATURE_EDITED, true);
        mSpamfolderkey = preferences.getLongAccountInformation(mUuid + KEY_SPAM_FOLDER_KEY, (long) 0);
        mIsDefaultEmailSize = preferences.getBooleanAccountInformation(mUuid + KEY_DEFAULT_EMAIL_SIZE, true);

        mRecentMessages = preferences.getIntAccountInformation(mUuid + KEY_RECENT_MESSAGES, 0);
    }

    public int getMessageFormat() {
        return mMessageFormat;
    }

    public void setMessageFormat(int messageFormat) {
        this.mMessageFormat = messageFormat;
    }

    public String getUuid() {
        return mUuid;
    }

    public String getStoreUri() {
        return mStoreUri;
    }

    public void setStoreUri(String storeUri) {
        this.mStoreUri = storeUri;
    }

    public String getSenderUri() {
        return mSenderUri;
    }

    public void setSenderUri(String senderUri) {
        this.mSenderUri = senderUri;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getPasswd() {
        return mPasswd;
    }

    public void setPasswd(String passwd) {
        this.mPasswd = passwd;
    }

    public String getSendAddr() {
        return mSendAddr;
    }

    public void setSendAddr(String sendAddr) {
        this.mSendAddr = sendAddr;
    }

    public int getSendPort() {
        return mSendPort;
    }

    public void setSendPort(int sendPort) {
        this.mSendPort = sendPort;
    }

    public String getSecurityAuth() {
        return mSecurityAuth;
    }

    public void setSecurityAuth(String flags) {
        this.mSecurityAuth = flags;
    }

    public long getSecurityFlags() {
        return mSecurityFlags;
    }

    public void setSecurityFlags(long flags) {
        this.mSecurityFlags = flags;
    }

    public long getSendSecurityFlags() {
        return mSendSecurityFlags;
    }

    public void setSendSecurityFlags(int flags) {
        this.mSendSecurityFlags = flags;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public boolean isVibrate() {
        return mVibrate;
    }

    public void setVibrate(boolean vibrate) {
        mVibrate = vibrate;
    }

    public boolean isVibrateWhenSilent() {
        return mVibrateWhenSilent;
    }

    public void setVibrateWhenSilent(boolean vibrateWhenSilent) {
        mVibrateWhenSilent = vibrateWhenSilent;
    }

    public String getRingtone() {
        return mRingtoneUri;
    }

    public void setRingtone(String ringtoneUri) {
        mRingtoneUri = ringtoneUri;
    }

    // ============================================================
    // Seven �߰�(2010.07.20)
    // ============================================================
    public void setAccountKey(long accountKey) {
        this.mAccountKey = accountKey;
    }

    public long getAccountKey() {
        return this.mAccountKey;
    }

//    public void setSevenAccountKey(long sevenAccountKey) {
//        this.mSevenAccountKey = sevenAccountKey;
//    }
//
//    public long getSevenAccountKey() {
//        return this.mSevenAccountKey;
//    }

    public void setFlagSyncContact(int flagSyncContact) {
        this.mFlagSyncContact = flagSyncContact;
    }

    public int getFlagSyncContact() {
        return this.mFlagSyncContact;
    }

    public void setFlagSyncCalendar(int flagSyncCalendar) {
        this.mFlagSyncCalendar = flagSyncCalendar;
    }

    public int getFlagSyncCalendar() {
        return this.mFlagSyncCalendar;
    }

    public void setFlagSyncNotes(int flagSyncNotes) {
        this.mFlagSyncNotes = flagSyncNotes;
    }

    public int getFlagSyncNotes() {
        return this.mFlagSyncNotes;
    }

//    public void setTypeMsg(int typeMsg) {
//        this.mTypeMsg = typeMsg;
//    }
//
//    public int getTypeMsg() {
//        return this.mTypeMsg;
//    }

//    public void setTimeLimit(long timeLimit) {
//        this.mTimeLimit = timeLimit;
//    }
//
//    public long getTimeLimit() {
//        return this.mTimeLimit;
//    }
//
//    public void setSizeLimit(long sizeLimit) {
//        this.mSizeLimit = sizeLimit;
//    }
//
//    public long getSizeLimit() {
//        return this.mSizeLimit;
//    }

    // ============================================================

    public void delete(Preferences preferences) {
        String[] uuids = preferences.getStringAccountInformation("accountUuids", "").split(",");
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = uuids.length; i < length; i++) {
            if (!uuids[i].equals(mUuid)) {
                if (sb.length() > 0) {
                    sb.append(',');
                }
                sb.append(uuids[i]);
            }
        }
        
        String accountUuids = sb.toString();
        preferences.putAccountInformation("accountUuids", accountUuids);
        
        preferences.removeAccountInformations(mUuid + ".storeUri");
        preferences.removeAccountInformations(mUuid + ".localStoreUri");
        preferences.removeAccountInformations(mUuid + ".senderUri");
        preferences.removeAccountInformations(mUuid + ".description");
        preferences.removeAccountInformations(mUuid + ".name");
        preferences.removeAccountInformations(mUuid + ".email");
        preferences.removeAccountInformations(mUuid + ".automaticCheckIntervalMinutes");
        preferences.removeAccountInformations(mUuid + ".lastAutomaticCheckTime");
        preferences.removeAccountInformations(mUuid + ".notifyNewMail");
        preferences.removeAccountInformations(mUuid + ".deletePolicy");
        preferences.removeAccountInformations(mUuid + ".draftsFolderName");
        preferences.removeAccountInformations(mUuid + ".sentFolderName");
        preferences.removeAccountInformations(mUuid + ".trashFolderName");
        preferences.removeAccountInformations(mUuid + ".outboxFolderName");
        preferences.removeAccountInformations(mUuid + ".accountNumber");
        preferences.removeAccountInformations(mUuid + ".vibrate");
        preferences.removeAccountInformations(mUuid + KEY_VIBRATE_WHEN_SILENT);
        preferences.removeAccountInformations(mUuid + ".ringtone");
        preferences.removeAccountInformations(mUuid + KEY_SYNC_WINDOW);
        preferences.removeAccountInformations(mUuid + KEY_BACKUP_FLAGS);
        preferences.removeAccountInformations(mUuid + KEY_PROTOCOL_VERSION);
        preferences.removeAccountInformations(mUuid + KEY_SECURITY_FLAGS);
        preferences.removeAccountInformations(mUuid + KEY_SEND_SECURITY_FLAGS);
        preferences.removeAccountInformations(mUuid + ".addsignature");
        preferences.removeAccountInformations(mUuid + KEY_SIGNATURE);

        // SVL - Start - J.sb
        preferences.removeAccountInformations(mUuid + KEY_SYNC_SCHEDULE_DATA);
        preferences.removeAccountInformations(mUuid + KEY_MESSAGE_FORMAT);
        // SVL - End - J.sb
        preferences.removeAccountInformations(mUuid + KEY_EAS_LOOKBACK);
        preferences.removeAccountInformations(mUuid + ".defaultaccount");
        preferences.removeAccountInformations(mUuid + KEY_EAS_CALENDARLOOKBACK);
        preferences.removeAccountInformations(mUuid + KEY_EAS_RETRIVALSIZE);
        preferences.removeAccountInformations(mUuid + KEY_EAS_RETRIVALSIZE_ROAMING);
        // MDM intent parameter for EAS Setup >>>>>>
        if (mUpdateAccount != null) {
            preferences.removeAccountInformations(mUuid + KEY_EAS_SYNCCONTACTS);
            preferences.removeAccountInformations(mUuid + KEY_EAS_SYNCCALENDAR);
            preferences.removeAccountInformations(mUuid + KEY_EAS_SYNCNOTES);
            preferences.removeAccountInformations(mUuid + ".updateaccount");
            preferences.removeAccountInformations(mUuid + ".servername");
        }
        // MDM intent parameter for EAS Setup <<<<<<
        // also delete any deprecated fields
        preferences.removeAccountInformations(mUuid + ".transportUri");

        preferences.removeAccountInformations(mUuid + KEY_ACCOUNT_FLAGS);
        preferences.removeAccountInformations(mUuid + KEY_FORWARD_WITH_FILES);
        preferences.removeAccountInformations(mUuid + KEY_SHOW_IMAGES);
        preferences.removeAccountInformations(mUuid + KEY_AUTO_DOWNLOAD);
        preferences.removeAccountInformations(mUuid + KEY_SMIME_OWN_ENCRYPT_CERT_ALIAS);
        preferences.removeAccountInformations(mUuid + KEY_SMIME_OWN_SIGN_CERT_ALIAS);
        preferences.removeAccountInformations(mUuid + KEY_SMIME_ENCRYPT_ALL);
        preferences.removeAccountInformations(mUuid + KEY_SMIME_SIGN_ALL);
        preferences.removeAccountInformations(mUuid + KEY_SMIME_SIGN_ALGORITHM);
        preferences.removeAccountInformations(mUuid + KEY_SMIME_ENCRYPT_ALGORITHM);
        preferences.removeAccountInformations(mUuid + KEY_AUTO_RETRY);
        preferences.removeAccountInformations(mUuid + KEY_CONFLICT_FLAGS);
        preferences.removeAccountInformations(mUuid + KEY_CONVERSATION_MODE);
        preferences.removeAccountInformations(mUuid + KEY_TEXT_PREVIEW);
        preferences.removeAccountInformations(mUuid + ".sendport");
        preferences.removeAccountInformations(mUuid + KEY_DEFAULT_EMAIL_SIZE);
        preferences.removeAccountInformations(mUuid + ".passwd");
        preferences.removeAccountInformations(mUuid + ".sendaddr", mSendAddr);
        preferences.removeAccountInformations(mUuid + KEY_IS_SIGNATURE_EDITED);
        preferences.removeAccountInformations(mUuid + KEY_SENDER_CAPABILITITIES);
        preferences.removeAccountInformations(mUuid + KEY_SECURITY_AUTH);
        preferences.removeAccountInformations(mUuid + KEY_ACCOUNT_TYPE);
        preferences.removeAccountInformations(mUuid + KEY_DOWNLOAD_ON_SCROLL);
        preferences.removeAccountInformations(mUuid + KEY_SPAM_FOLDER_KEY);
        preferences.removeAccountInformations(mUuid + KEY_CAPABILITITIES);
    }

    public void save(Preferences preferences) {
        if (!preferences.getStringAccountInformation("accountUuids", "").contains(mUuid)) {
            /*
             * When the account is first created we assign it a unique account
             * number. The account number will be unique to that account for the
             * lifetime of the account. So, we get all the existing account
             * numbers, sort them ascending, loop through the list and check if
             * the number is greater than 1 + the previous number. If so we use
             * the previous number + 1 as the account number. This refills gaps.
             * mAccountNumber starts as -1 on a newly created account. It must
             * be -1 for this algorithm to work. I bet there is a much smarter
             * way to do this. Anyone like to suggest it?
             */
        	/* separate_email_ui_from_mom */
        	/*
            Account[] accounts = preferences.getAccounts();
            int[] accountNumbers = new int[accounts.length];
            for (int i = 0; i < accounts.length; i++) {
                accountNumbers[i] = accounts[i].getAccountNumber();
            }
            Arrays.sort(accountNumbers);
            for (int accountNumber : accountNumbers) {
                if (accountNumber > mAccountNumber + 1) {
                    break;
                }
                mAccountNumber = accountNumber;
            }
            mAccountNumber++;
            */

            // String accountUuids =
            // preferences.getStringAccountInformation("accountUuids", "");
            StringBuffer accountUuids = new StringBuffer(preferences.getStringAccountInformation(
                    "accountUuids", ""));// sec.email tom.jung For RSAR
            accountUuids.append((accountUuids.length() != 0 ? "," : "")).append(mUuid);
            preferences.putAccountInformation("accountUuids", accountUuids.toString());
        }

        preferences.putAccountInformation(mUuid + ".storeUri", Utility.base64Encode(mStoreUri));
        preferences.putAccountInformation(mUuid + ".localStoreUri", mLocalStoreUri);
        preferences.putAccountInformation(mUuid + ".senderUri", Utility.base64Encode(mSenderUri));
        preferences.putAccountInformation(mUuid + ".description", mDescription);
        preferences.putAccountInformation(mUuid + ".name", mName);
        preferences.putAccountInformation(mUuid + ".passwd",  AESEncryptionUtil.AESEncryption(mPasswd));
        preferences.putAccountInformation(mUuid + ".sendaddr", mSendAddr);
        preferences.putAccountInformation(mUuid + ".sendport", mSendPort);
        preferences.putAccountInformation(mUuid + ".email", mEmail);
        preferences.putAccountInformation(mUuid + ".automaticCheckIntervalMinutes", mAutomaticCheckIntervalMinutes);
        preferences.putAccountInformation(mUuid + ".lastAutomaticCheckTime", mLastAutomaticCheckTime);
        preferences.putAccountInformation(mUuid + ".notifyNewMail", mNotifyNewMail);
        preferences.putAccountInformation(mUuid + ".deletePolicy", mDeletePolicy);
        preferences.putAccountInformation(mUuid + ".draftsFolderName", mDraftsFolderName);
        preferences.putAccountInformation(mUuid + ".sentFolderName", mSentFolderName);
        preferences.putAccountInformation(mUuid + ".trashFolderName", mTrashFolderName);
        preferences.putAccountInformation(mUuid + ".outboxFolderName", mOutboxFolderName);
        preferences.putAccountInformation(mUuid + ".accountNumber", mAccountNumber);
        preferences.putAccountInformation(mUuid + ".vibrate", mVibrate);
        preferences.putAccountInformation(mUuid + KEY_VIBRATE_WHEN_SILENT, mVibrateWhenSilent);
        preferences.putAccountInformation(mUuid + ".ringtone", mRingtoneUri);
        preferences.putAccountInformation(mUuid + KEY_SYNC_WINDOW, mSyncWindow);
        preferences.putAccountInformation(mUuid + KEY_BACKUP_FLAGS, mBackupFlags);
        preferences.putAccountInformation(mUuid + KEY_PROTOCOL_VERSION, mProtocolVersion);
        preferences.putAccountInformation(mUuid + KEY_SECURITY_FLAGS, mSecurityFlags);
        preferences.putAccountInformation(mUuid + KEY_SEND_SECURITY_FLAGS, mSendSecurityFlags);
        preferences.putAccountInformation(mUuid + KEY_SECURITY_AUTH, mSecurityAuth);
        preferences.putAccountInformation(mUuid + ".addsignature", mAddSignature);
        preferences.putAccountInformation(mUuid + KEY_SIGNATURE, mSignature);

        preferences.putAccountInformation(mUuid + KEY_CAPABILITITIES, mCapabilities);
        preferences.putAccountInformation(mUuid + KEY_SENDER_CAPABILITITIES, mSenderCapabilities);

        // SVL - Start - J.sb
        preferences.putAccountInformation(mUuid + KEY_SYNC_SCHEDULE_DATA, mSyncScheduleData);
        preferences.putAccountInformation(mUuid + KEY_MESSAGE_FORMAT, mMessageFormat);
        preferences.putAccountInformation(mUuid + KEY_ACCOUNT_TYPE, mAccountType);
        // SVL - End - J.sb
        
        preferences.putAccountInformation(mUuid + KEY_ACCOUNT_FLAGS, mFlagsBkp);
        preferences.putAccountInformation(mUuid + KEY_FORWARD_WITH_FILES, mForwardWithFiles);
        preferences.putAccountInformation(mUuid + KEY_SHOW_IMAGES, mShowImages);
        preferences.putAccountInformation(mUuid + KEY_AUTO_DOWNLOAD, mAutoDownload);
        preferences.putAccountInformation(mUuid + KEY_SMIME_OWN_ENCRYPT_CERT_ALIAS, mSmimeOwnEncryptCertAlias);
        preferences.putAccountInformation(mUuid + KEY_SMIME_OWN_SIGN_CERT_ALIAS, mSmimeOwnSignCertAlias);
        preferences.putAccountInformation(mUuid + KEY_SMIME_ENCRYPT_ALL, mSmimeEncryptAll);
        preferences.putAccountInformation(mUuid + KEY_SMIME_SIGN_ALL, mSmimeSignAll);
        preferences.putAccountInformation(mUuid + KEY_SMIME_SIGN_ALGORITHM, mSmimeSignAlgorithm);
        preferences.putAccountInformation(mUuid + KEY_SMIME_ENCRYPT_ALGORITHM, mSmimeEncryptionAlgorithm);
        preferences.putAccountInformation(mUuid + KEY_AUTO_RETRY, mAutoRetryTimes);
        preferences.putAccountInformation(mUuid + KEY_CONFLICT_FLAGS, mConflictFlags);
        preferences.putAccountInformation(mUuid + KEY_CONVERSATION_MODE, mConversationMode);
        preferences.putAccountInformation(mUuid + KEY_TEXT_PREVIEW, mTextPreviewSize);
        
        // MDM intent parameter for EAS Setup >>>>>>    
        preferences.putAccountInformation(mUuid + KEY_EAS_LOOKBACK, mSyncLookback);
        preferences.putAccountInformation(mUuid + KEY_EAS_RETRIVALSIZE, mEmailSize);
        preferences.putAccountInformation(mUuid + KEY_EAS_RETRIVALSIZE_ROAMING, mRoamingEmailSize);
        preferences.putAccountInformation(mUuid + KEY_EAS_CALENDARLOOKBACK, mCalendarSyncLookback);
        preferences.putAccountInformation(mUuid + DEFAULT_ACCOUNT, mIsDefault);
        if (mUpdateAccount != null) {
        	preferences.putAccountInformation(mUuid + KEY_EAS_SYNCCONTACTS, mFlagSyncContact);
        	preferences.putAccountInformation(mUuid + KEY_EAS_SYNCCALENDAR, mFlagSyncCalendar);
        	preferences.putAccountInformation(mUuid + KEY_EAS_SYNCNOTES, mFlagSyncNotes);
        	preferences.putAccountInformation(mUuid + UPDATE_ACCOUNT, mUpdateAccount);
        	preferences.putAccountInformation(mUuid + SERVER_NAME, mServerName);
            // MDM intent parameter for EAS Setup <<<<<<
        }

        // The following fields are *not* written because they need to be more
        // fine-grained
        // and not risk rewriting with old data.
        // putAccountInformation(mUuid + PREF_TAG_STORE_PERSISTENT,
        // mStorePersistent);

        // also delete any deprecated fields
        preferences.removeAccountInformations(mUuid + ".transportUri");
        
        preferences.putAccountInformation(mUuid + KEY_DOWNLOAD_ON_SCROLL, mDownloadOnScroll);
        preferences.putAccountInformation(mUuid + KEY_IS_SIGNATURE_EDITED, mIsSignatureEdited);
        preferences.putAccountInformation(mUuid + KEY_SPAM_FOLDER_KEY, mSpamfolderkey);
        preferences.putAccountInformation(mUuid + KEY_DEFAULT_EMAIL_SIZE, mIsDefaultEmailSize);

        preferences.putAccountInformation(mUuid + KEY_RECENT_MESSAGES, mRecentMessages);
        
        /*
        SharedPreferences.Editor editor = preferences.mSharedPreferences.edit();

        editor.putString(mUuid + ".storeUri", Utility.base64Encode(mStoreUri));
        editor.putString(mUuid + ".localStoreUri", mLocalStoreUri);
        editor.putString(mUuid + ".senderUri", Utility.base64Encode(mSenderUri));
        editor.putString(mUuid + ".description", mDescription);
        editor.putString(mUuid + ".name", mName);
        editor.putString(mUuid + ".passwd",  AESEncryptionUtil.AESEncryption(mPasswd));
        editor.putString(mUuid + ".sendaddr", mSendAddr);
        editor.putInt(mUuid + ".sendport", mSendPort);
        editor.putString(mUuid + ".email", mEmail);
        editor.putInt(mUuid + ".automaticCheckIntervalMinutes", mAutomaticCheckIntervalMinutes);
        editor.putLong(mUuid + ".lastAutomaticCheckTime", mLastAutomaticCheckTime);
        editor.putBoolean(mUuid + ".notifyNewMail", mNotifyNewMail);
        editor.putInt(mUuid + ".deletePolicy", mDeletePolicy);
        editor.putString(mUuid + ".draftsFolderName", mDraftsFolderName);
        editor.putString(mUuid + ".sentFolderName", mSentFolderName);
        editor.putString(mUuid + ".trashFolderName", mTrashFolderName);
        editor.putString(mUuid + ".outboxFolderName", mOutboxFolderName);
        editor.putInt(mUuid + ".accountNumber", mAccountNumber);
        editor.putBoolean(mUuid + ".vibrate", mVibrate);
        editor.putBoolean(mUuid + KEY_VIBRATE_WHEN_SILENT, mVibrateWhenSilent);
        editor.putString(mUuid + ".ringtone", mRingtoneUri);
        editor.putInt(mUuid + KEY_SYNC_WINDOW, mSyncWindow);
        editor.putInt(mUuid + KEY_BACKUP_FLAGS, mBackupFlags);
        editor.putString(mUuid + KEY_PROTOCOL_VERSION, mProtocolVersion);
        editor.putLong(mUuid + KEY_SECURITY_FLAGS, mSecurityFlags);
        editor.putInt(mUuid + KEY_SEND_SECURITY_FLAGS, mSendSecurityFlags);
        editor.putString(mUuid + KEY_SECURITY_AUTH, mSecurityAuth);
        editor.putBoolean(mUuid + ".addsignature", mAddSignature);
        editor.putString(mUuid + KEY_SIGNATURE, mSignature);

        // SVL - Start - J.sb
        editor.putString(mUuid + KEY_SYNC_SCHEDULE_DATA, mSyncScheduleData);
        editor.putInt(mUuid + KEY_MESSAGE_FORMAT, mMessageFormat);
        editor.putInt(mUuid + KEY_ACCOUNT_TYPE, mAccountType);
        // SVL - End - J.sb
        
        editor.putInt(mUuid + KEY_ACCOUNT_FLAGS, mFlagsBkp);
        editor.putBoolean(mUuid + KEY_FORWARD_WITH_FILES, mForwardWithFiles);
        editor.putBoolean(mUuid + KEY_SHOW_IMAGES, mShowImages);
        editor.putBoolean(mUuid + KEY_AUTO_DOWNLOAD, mAutoDownload);
        editor.putString(mUuid + KEY_SMIME_OWN_ENCRYPT_CERT_ALIAS, mSmimeOwnEncryptCertAlias);
        editor.putString(mUuid + KEY_SMIME_OWN_SIGN_CERT_ALIAS, mSmimeOwnSignCertAlias);
        editor.putBoolean(mUuid + KEY_SMIME_ENCRYPT_ALL, mSmimeEncryptAll);
        editor.putBoolean(mUuid + KEY_SMIME_SIGN_ALL, mSmimeSignAll);
        editor.putInt(mUuid + KEY_SMIME_SIGN_ALGORITHM, mSmimeSignAlgorithm);
        editor.putInt(mUuid + KEY_SMIME_ENCRYPT_ALGORITHM, mSmimeEncryptionAlgorithm);
        editor.putInt(mUuid + KEY_AUTO_RETRY, mAutoRetryTimes);
        editor.putInt(mUuid + KEY_CONFLICT_FLAGS, mConflictFlags);
        editor.putInt(mUuid + KEY_CONVERSATION_MODE, mConversationMode);
        editor.putInt(mUuid + KEY_TEXT_PREVIEW, mTextPreviewSize);
        
        // MDM intent parameter for EAS Setup >>>>>>    
        editor.putInt(mUuid + KEY_EAS_LOOKBACK, mSyncLookback);
        editor.putInt(mUuid + KEY_EAS_RETRIVALSIZE, mEmailSize);
        editor.putInt(mUuid + KEY_EAS_RETRIVALSIZE_ROAMING, mRoamingEmailSize);
        editor.putInt(mUuid + KEY_EAS_CALENDARLOOKBACK, mCalendarSyncLookback);
        editor.putBoolean(mUuid + DEFAULT_ACCOUNT, mIsDefault);
        if (mUpdateAccount != null) {
            editor.putInt(mUuid + KEY_EAS_SYNCCONTACTS, mFlagSyncContact);
            editor.putInt(mUuid + KEY_EAS_SYNCCALENDAR, mFlagSyncCalendar);
            editor.putString(mUuid + UPDATE_ACCOUNT, mUpdateAccount);
            editor.putString(mUuid + SERVER_NAME, mServerName);
            // MDM intent parameter for EAS Setup <<<<<<
        }

        // The following fields are *not* written because they need to be more
        // fine-grained
        // and not risk rewriting with old data.
        // editor.putString(mUuid + PREF_TAG_STORE_PERSISTENT,
        // mStorePersistent);

        // also delete any deprecated fields
        editor.remove(mUuid + ".transportUri");
        
        editor.putBoolean(mUuid + KEY_DOWNLOAD_ON_SCROLL, mDownloadOnScroll);
        editor.putBoolean(mUuid + KEY_IS_SIGNATURE_EDITED, mIsSignatureEdited);
        editor.putLong(mUuid + KEY_SPAM_FOLDER_KEY, mSpamfolderkey);
        editor.putBoolean(mUuid + KEY_DEFAULT_EMAIL_SIZE, mIsDefaultEmailSize);

        editor.apply();
        */
    }

    @Override
    public String toString() {
        return mDescription;
    }

    public Uri getContentUri() {
        return Uri.parse("content://accounts/" + getUuid());
    }

    public String getLocalStoreUri() {
        return mLocalStoreUri;
    }

    public void setLocalStoreUri(String localStoreUri) {
        this.mLocalStoreUri = localStoreUri;
    }

    /**
     * Returns -1 for never.
     */
    public int getAutomaticCheckIntervalMinutes() {
        return mAutomaticCheckIntervalMinutes;
    }

    /**
     * @param automaticCheckIntervalMinutes or -1 for never.
     */
    public void setAutomaticCheckIntervalMinutes(int automaticCheckIntervalMinutes) {
        this.mAutomaticCheckIntervalMinutes = automaticCheckIntervalMinutes;
    }

    public long getLastAutomaticCheckTime() {
        return mLastAutomaticCheckTime;
    }

    public void setLastAutomaticCheckTime(long lastAutomaticCheckTime) {
        this.mLastAutomaticCheckTime = lastAutomaticCheckTime;
    }

    public boolean isNotifyNewMail() {
        return mNotifyNewMail;
    }

    public void setNotifyNewMail(boolean notifyNewMail) {
        this.mNotifyNewMail = notifyNewMail;
    }

    public boolean isAddSignature() {
        return mAddSignature;
    }

    public void setAddSignature(boolean addsignature) {
        mAddSignature = addsignature;
    }

    public int getDeletePolicy() {
        return mDeletePolicy;
    }

    public void setDeletePolicy(int deletePolicy) {
        this.mDeletePolicy = deletePolicy;
    }

    public String getDraftsFolderName() {
        return mDraftsFolderName;
    }

    public void setDraftsFolderName(String draftsFolderName) {
        mDraftsFolderName = draftsFolderName;
    }

    public String getSentFolderName() {
        return mSentFolderName;
    }

    public void setSentFolderName(String sentFolderName) {
        mSentFolderName = sentFolderName;
    }

    public String getTrashFolderName() {
        return mTrashFolderName;
    }

    public void setTrashFolderName(String trashFolderName) {
        mTrashFolderName = trashFolderName;
    }

    public String getOutboxFolderName() {
        return mOutboxFolderName;
    }

    public void setOutboxFolderName(String outboxFolderName) {
        mOutboxFolderName = outboxFolderName;
    }

    public int getAccountNumber() {
        return mAccountNumber;
    }

    public int getSyncWindow() {
        return mSyncWindow;
    }

    public void setSyncWindow(int window) {
        mSyncWindow = window;
    }

    public int getBackupFlags() {
        return mBackupFlags;
    }

    public void setBackupFlags(int flags) {
        mBackupFlags = flags;
    }

    // SVL Start - J.sb
    public void setSyncScheduleData(String syncScheduleData) {
        mSyncScheduleData = syncScheduleData;
    }

    public String getSyncScheduleData() {
        return mSyncScheduleData;
    }

    // SVL End - J.sb
    // MDM intent parameter for EAS Setup >>>>>>
    public void setSyncLookbackData(int syncLookback) {
        mSyncLookback = syncLookback;
    }
    
    public int getSyncLookbackData() {
        return mSyncLookback;
    }

    public void setCalendarSyncLookbackData(int syncLookback) {
        mCalendarSyncLookback = syncLookback;
    }

    public int getCalendarSyncLookbackData() {
        return mCalendarSyncLookback;
    }

    public void setEmailSize(int emailSize) {
        //TODO : Should to add restricted conditions
         mEmailSize = emailSize;
    }
    public byte getEmailSize() {
        return (byte)mEmailSize;
    }   
    public int getEmailIntSize() {
        return mEmailSize;
    }   
    // MDM intent parameter for EAS Setup <<<<<<
    
    public void setRoamingEmailSize(int emailSize) {
         mRoamingEmailSize = emailSize;
    }
    public byte getRoamingEmailSize() {
        return (byte)mRoamingEmailSize;
    }   
    
    public int getRoamingEmailIntSize() {
        return mRoamingEmailSize;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Account) {
            return ((Account) o).mUuid.equals(mUuid);
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }
    // MDM intent parameter for EAS Setup >>>>>>
    public void setDefault(Boolean isDefault) {
        mIsDefault = isDefault;
    }
    public Boolean getDefault() {
        return mIsDefault;
    }
    public void setUpdateAccount(String UpdateAccount) {
        mUpdateAccount = UpdateAccount;
    }
    public String getUpdateAccount() {
        return mUpdateAccount;
    }
    public void setSignature(String signature) {
        mSignature = signature;
    }
    public String getSignature() {
        return mSignature;
    }
    public void setServerName(String servername) {
        mServerName = servername;
    }
    public String getServerName() {
        return mServerName;
    }
    // MDM intent parameter for EAS Setup <<<<<<
    
    
    public void setAccountFlags(int flags) {
        mFlagsBkp = flags;
    }

    public int getAccountFlags() {
        return mFlagsBkp;
    }
    
    public void setForwardWithFiles(boolean forwardWithFiles) {
        mForwardWithFiles = forwardWithFiles;
    }

    public boolean getForwardWithFiles() {
        return mForwardWithFiles;
    }
    
    public void setShowImage(boolean showImages) {
        mShowImages = showImages;
    }

    public boolean getShowImages() {
        return mShowImages;
    }
    
    public void setAutoDownload(boolean autoDownload) {
        mAutoDownload = autoDownload;
    }

    public boolean getAutoDownload() {
        return mAutoDownload;
    }
    
    public String getSmimeOwnCertificate() {
        return mSmimeOwnEncryptCertAlias;
    }
    
    public void setSmimeOwnCertificate(String alias) {
        mSmimeOwnEncryptCertAlias = alias;
    }
        
    public String getSmimeOwnSignCertificate() {
        return mSmimeOwnSignCertAlias;
    }
    
    public void setSmimeOwnSignCertificate(String alias) {
        mSmimeOwnSignCertAlias = alias;
    }
    
    public boolean getSmimeEncryptAll() {
        return mSmimeEncryptAll;
    }
    
    public void setSmimeEncryptAll(boolean value) {
        mSmimeEncryptAll = value;
    }

    public boolean getSmimeSignAll() {
        return mSmimeSignAll;
    }
    
    public void setSmimeSignAll(boolean value) {
        mSmimeSignAll = value;
    }

    public int getSmimeSignAlgorithm() {
        return mSmimeSignAlgorithm;
    }
    
    public void setSmimeSignAlgorithm(int signAlgorithm) {
        mSmimeSignAlgorithm = signAlgorithm;
    }

    public int getSmimeEncryptionAlgorithm() {
        return mSmimeEncryptionAlgorithm;
    }
    
    public void setSmimeEncryptionAlgorithm(int encryptionAlgorithm) {
        mSmimeEncryptionAlgorithm = encryptionAlgorithm;
    }
    
    public int getAutoRetryTimes() {
        return mAutoRetryTimes;
    }
    public void setAutoRetryTimes(int retryTimes) {
        mAutoRetryTimes = retryTimes;
    }
    
    public int getConflictresolution() {
        return mConflictFlags;
    }
    
    public void setConflictresolution(int conflict) {
        mConflictFlags = conflict;
    }
    
    public int getConversationMode() {
        return mConversationMode;
    }
    
    public void setConversationMode(int conv_mode) {
        mConversationMode = conv_mode;
    }
    
    public int getTextPreviewSize() {
        return mTextPreviewSize;
    }
    
    public void setTextPreviewSize(int textPreview) {
        mTextPreviewSize = textPreview;
    }

    public void setPolicySet(PolicySet policySet) {
        mPolicySet = policySet;
    }

    public PolicySet getPolicySet() {
        return mPolicySet;
    }

    public void setProtocolVersion(String protocolVersion) {
        mProtocolVersion = protocolVersion;
    }

    public String getProtocolVersion() {
        return mProtocolVersion;
    }
    
    public void setDownloadOnScroll(boolean downloadOnScroll) {
        mDownloadOnScroll = downloadOnScroll;
    }

    public boolean getDownloadOnScroll() {
        return mDownloadOnScroll;
    }

    public void setSignatureEdited(boolean iSignatureEdited) {
        mIsSignatureEdited = iSignatureEdited;
    }

    public boolean isSignatureEdited() {
        return mIsSignatureEdited;
    }
    
    public void setSpamFolderKey(long Spamfolderkey) {
        mSpamfolderkey = Spamfolderkey;
    }

    public long getSpamFolderKey() {
        return mSpamfolderkey;
    }

    public void setIsDefaultEmailSize(boolean isDefaultEmailSize) {
        mIsDefaultEmailSize = isDefaultEmailSize;
    }

    public boolean getIsDefaultEmailSize() {
        return mIsDefaultEmailSize;
    }
    
    public void setAccountType(int accountType) {
        mAccountType = accountType;
    }

    public String getCapabilities() {
        return mCapabilities;
    }

    public void setCapabilities(String mCapabilities) {
        this.mCapabilities = mCapabilities;
    }

    public String getSenderCapabilities() {
        return mSenderCapabilities;
    }

    public void setSenderCapabilities(String mSenderCapabilities) {
        this.mSenderCapabilities = mSenderCapabilities;
    }

    public int getRecentMessages() {
        return mRecentMessages;
    }

    public void setRecentMessages(int recentMessages) {
        this.mRecentMessages = recentMessages;
    }
}
