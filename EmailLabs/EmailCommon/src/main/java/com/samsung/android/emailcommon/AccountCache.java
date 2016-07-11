package com.samsung.android.emailcommon;


import android.content.Context;

import com.samsung.android.emailcommon.esp.Provider;
import com.samsung.android.emailcommon.provider.EmailContent.Account;
import com.samsung.android.emailcommon.provider.EmailContent.HostAuth;
import com.samsung.android.emailcommon.utility.AttachmentUtilities;
import com.samsung.android.emailcommon.utility.EmailLog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class AccountCache {
	final static public int ACCOUNT_TYPE_NONE = 0;
	final static public int ACCOUNT_TYPE_POP3 = 1;
	final static public int ACCOUNT_TYPE_IMAP = 2;
	final static public int ACCOUNT_TYPE_EAS = 3;

	final static public String PROTOCOL_EAS = "eas";
	final static public String PROTOCOL_IMAP = "imap";
	final static public String PROTOCOL_POP3 = "pop3";

	static String TAG = "AccountCache";
	
	static private ConcurrentHashMap<Long, AccountInfo> sAccountType = new ConcurrentHashMap<Long, AccountInfo>();
    private static boolean mIsClearflag = false;
    private static List<Long> mDeleteIds = new ArrayList<Long>();
	
    static public class AccountInfo {
        int accountType;
        double protocolVer;
        long maxAttachmentUploadLimit;
        long mHostAuthKeySend;
        long mHostAuthKeyRecv;

        private AccountInfo(int type, double ver, long maxAttachmentUploadLimit,
                long senderHosAuthtKey, long recvHostAuthKey) {
            accountType = type;
            protocolVer = ver;
            mHostAuthKeySend = senderHosAuthtKey;
            mHostAuthKeyRecv = recvHostAuthKey;
            this.maxAttachmentUploadLimit = maxAttachmentUploadLimit;
        }

        private AccountInfo(int type, long maxAttachmentUploadLimit, long senderHosAuthtKey, long recvHostAuthKey) {

            this(type, 0.0, maxAttachmentUploadLimit, senderHosAuthtKey, recvHostAuthKey);
        }

    }
	
    static public boolean isExchange(Context context, long accountId) {
        validateAccount(context, accountId);
        AccountInfo ai = sAccountType.get(accountId);
        if (ai != null) {
            return (ai.accountType == ACCOUNT_TYPE_EAS);
        }
        return false;
    }

    static public boolean isImap(Context context, long accountId) {
        validateAccount(context, accountId);
        AccountInfo ai = sAccountType.get(accountId);
        if (ai != null) {
            return (ai.accountType == ACCOUNT_TYPE_IMAP);
        }
        return false;
    }

    static public boolean isPop3(Context context, long accountId) {
        validateAccount(context, accountId);
        AccountInfo ai = sAccountType.get(accountId);
        if (ai != null) {
            return (ai.accountType == ACCOUNT_TYPE_POP3);
        }
        return false;
    }

    static public boolean isLegacy(Context context, long accountId) {
        validateAccount(context, accountId);
        AccountInfo ai = sAccountType.get(accountId);
        if (ai != null) {
            return (ai.accountType == ACCOUNT_TYPE_POP3 || ai.accountType == ACCOUNT_TYPE_IMAP);
        }
        return false;
    }

    static public int getType(Context context, long accountId) {
        validateAccount(context, accountId);
        AccountInfo ai = sAccountType.get(accountId);
        if (ai != null) {
            return ai.accountType;
        }
        return ACCOUNT_TYPE_NONE;
    }
	
    static public double getVersion(Context context, long accountId) {
        validateAccount(context, accountId);
        AccountInfo ai = sAccountType.get(accountId);
        if (ai != null) {
            return ai.protocolVer;
        }
        return 0.0;
    }

    static public String getTransportString(Context context, long accountId) {
        validateAccount(context, accountId);
        AccountInfo ai = sAccountType.get(accountId);
        if (ai != null) {
            int accountType = ai.accountType;
            if (accountType == ACCOUNT_TYPE_POP3)
                return PROTOCOL_POP3;
            if (accountType == ACCOUNT_TYPE_IMAP)
                return PROTOCOL_IMAP;
            if (accountType == ACCOUNT_TYPE_EAS)
                return PROTOCOL_EAS;
        }
        return "";
    }
	
	static public void validateAccount(Context context, long accountId) {
	    validateAccount(context, accountId, -1);
	}
	
	static  public void validateAccount(Context context, long accountId, long mMaxAttachmentUploadLimit) {
	    
	    if(mDeleteIds != null && mDeleteIds.size() > 0 && sAccountType != null){
	        for(long mId : mDeleteIds){
	            if(sAccountType.containsKey(mId)){
	                sAccountType.remove(mId);
	            }
	        }
	        mDeleteIds.clear();
	    }
	    
	    if(mIsClearflag && sAccountType != null){
	        sAccountType.clear();
	        mIsClearflag = false;
	    }
	    
//	    if (sAccountType == null) {
//	    	sAccountType = new ConcurrentHashMap<Long, AccountInfo>();
//	    }
	    
		if (sAccountType != null && sAccountType.containsKey(accountId) && mMaxAttachmentUploadLimit == -1) {
			return;
		}
		if (Account.isVirtualAccount(accountId))
			return;
		Account account = Account.restoreAccountWithId(context, accountId);
		if (account != null) {
		    HostAuth ha = HostAuth.restoreHostAuthWithId(context, account.mHostAuthKeyRecv);
		    if (ha != null && sAccountType != null) {
		        if (PROTOCOL_EAS.equals(ha.mProtocol)) {
		            double ver = 0.0;
		            if (account.mProtocolVersion != null) {
		                ver = Double.parseDouble(account.mProtocolVersion);
		            }
		            sAccountType.put(accountId, new AccountInfo(ACCOUNT_TYPE_EAS, ver, mMaxAttachmentUploadLimit,account.mHostAuthKeySend, account.mHostAuthKeyRecv));
		        } else if (PROTOCOL_IMAP.equals(ha.mProtocol)) {
		            sAccountType.put(accountId, new AccountInfo(ACCOUNT_TYPE_IMAP, mMaxAttachmentUploadLimit,account.mHostAuthKeySend, account.mHostAuthKeyRecv));
		        } else if (PROTOCOL_POP3.equals(ha.mProtocol)) {
		            sAccountType.put(accountId, new AccountInfo(ACCOUNT_TYPE_POP3, mMaxAttachmentUploadLimit,account.mHostAuthKeySend, account.mHostAuthKeyRecv));
		        }
		    }
		}
	}

	static public int getSchemeType(String scheme) {
	    if(scheme.startsWith(PROTOCOL_POP3))
	        return ACCOUNT_TYPE_POP3;
	    else if(scheme.startsWith(PROTOCOL_IMAP))
	        return ACCOUNT_TYPE_IMAP;
	    else if(scheme.startsWith(PROTOCOL_EAS))
	        return ACCOUNT_TYPE_EAS;
	    else
	        return -1;
	}


	public static int getProviderFromAddress(String serverAddress) {
	    EmailLog.d(TAG, "getProviderFromAddress");
	    if (serverAddress.contains("yahoo.")) {
	        return Provider.YAHOO;
	    } else if (serverAddress.contains("gmail.") || serverAddress.contains("google.")) {
	        return Provider.GMAIL;
	    } else if (serverAddress.contains("hotmail.") || serverAddress.contains("msn.")
	            || serverAddress.contains("live.") || serverAddress.contains("outlook.")) {
	        return Provider.HOTMAIL;
	    } else if (serverAddress.contains("aol.") || serverAddress.contains("aim.")) {
	        return Provider.AOL;
	    } else if (serverAddress.contains("verizon.")) {
	        return Provider.VERIZON;
	    } else {
	        return Provider.OTHER;
	    }
	}

    public static long getMaxAttachmentUploadLimit(Context context, long accountId) {
        return getMaxAttachmentUploadLimit(context, accountId, false);
    }

    public static long getMaxAttachmentUploadLimit(Context context, long accountId, boolean serverValue) {
        EmailLog.d(TAG, "getMaxAttachmentUploadLimit " + "[accId - " + accountId + "]");
        int size;
        long maxAttachmentUploadLimit = -1;
        size = AttachmentUtilities.getCarrierSpecificMaxAttachmentUploadSize();
        if (size != 0) {
            return size;
        }
        if (accountId < 1 || accountId == Account.ACCOUNT_ID_COMBINED_VIEW
                || accountId == Account.ACCOUNT_ID_FILTER_VIEW
                || accountId == Account.ACCOUNT_ID_OTHERS
                || accountId == Account.ACCOUNT_ID_PRIORITY_SENDER_VIEW
                || accountId == Account.ACCOUNT_ID_RECENTLY_READ_VIEW
                || accountId == Account.ACCOUNT_ID_SINGLE_VIEW) {
            EmailLog.e(TAG, "FATAL: Account not available, cannot find accout for accountId: "
                    + accountId);
            return 0;
        }
        AccountInfo aInfo = null;
        if (sAccountType.containsKey(accountId)) {
            aInfo = sAccountType.get(accountId);
            if (aInfo != null)
                maxAttachmentUploadLimit = aInfo.maxAttachmentUploadLimit;
        }

        if (maxAttachmentUploadLimit == -1) {
            long hostKey = -1;
            if (aInfo != null) {
                hostKey = aInfo.mHostAuthKeySend;
            } else {
                Account account = Account.restoreAccountWithId(context, accountId);
                if (account != null) {
                    hostKey = account.mHostAuthKeySend;
                }
            }
            HostAuth hostAuth = HostAuth.restoreHostAuthWithSenderKey(context, hostKey);
            if (hostAuth != null && hostAuth.mCapabilities != null
                    && hostAuth.mCapabilities.contains("SIZE")) {
                maxAttachmentUploadLimit = AttachmentUtilities
                        .parseSizeAttributeOfCapabilities(hostAuth.mCapabilities);
                setMaxAttachmentUploadLimit(context, accountId, (int) maxAttachmentUploadLimit);
            } else {
                maxAttachmentUploadLimit = AttachmentUtilities.MAX_ATTACHMENT_UPLOAD_SIZE;
            }
        }

        if (isLegacy(context, accountId)) {
            /*
             * SANITY CHECK :: if the limit exceeds 50MB, restrict it to 50MB.
             * Better to have some threshold value in order to handle the
             * misbehaving Server. For e.g., the server upper threshold value in
             * order to handle the misbehaving Server. For e.g., the server
             * "lavabit" sends more than 1GB as the limit which is impossible!
             * If server sends less than 10 MB, strict to 10MB as lower
             * threshold limit.
             */

            Account account = Account.restoreAccountWithId(context, accountId);
            if (account != null
                    && getProviderFromAddress(account.mEmailAddress) == Provider.HOTMAIL) {
                return (AttachmentUtilities.ATTACHMENT_SIZE_1MB * 25 - AttachmentUtilities.ATTACHMENT_SIZE_1MB);
            }
            long rawAttSize = AttachmentUtilities.getRawAttachmentSize(maxAttachmentUploadLimit, serverValue);
            if (rawAttSize > AttachmentUtilities.MAX_THRESHOLD_ATTACHMENT_UPLOAD_SIZE) {
                return AttachmentUtilities.MAX_THRESHOLD_ATTACHMENT_UPLOAD_SIZE;
            } else if (rawAttSize < AttachmentUtilities.MIN_THRESHOLD_ATTACHMENT_UPLOAD_SIZE) {
                return AttachmentUtilities.MIN_THRESHOLD_ATTACHMENT_UPLOAD_SIZE;
            }

            // In case if parsing capabilities fails or capabilities is null
            // return flat 50 MB
            if (maxAttachmentUploadLimit == AttachmentUtilities.MAX_ATTACHMENT_UPLOAD_SIZE) {
                return maxAttachmentUploadLimit;
            }

            // 1MB is reserved for the body of the mail
            return (serverValue ? rawAttSize : rawAttSize - AttachmentUtilities.ATTACHMENT_SIZE_1MB);
        }
        else if (isExchange(context, accountId)) {
            Account account = Account.restoreAccountWithId(context, accountId);

            if (account != null
                    && getProviderFromAddress(account.mEmailAddress) == Provider.HOTMAIL) {
                return AttachmentUtilities
                        .getRawAttachmentSize(AttachmentUtilities.ATTACHMENT_SIZE_1MB * 30 , serverValue);
            }
        }

        return maxAttachmentUploadLimit;
    }


    public static void setMaxAttachmentUploadLimit(Context context, long accountId, int serverLimit) {
        if (accountId < 1) {
            EmailLog.e(TAG, "FATAL: Invalid Account Id");
            return;
        }
        EmailLog.d(TAG, "setMaxAttachmentUploadLimit " + "[accId - " + accountId
                + " serverlimit - " + serverLimit + "]");
        validateAccount(context, accountId, serverLimit);
    }
	
	public static ConcurrentHashMap<Long, AccountInfo> getAccountType(){
	    if(sAccountType != null){
	        return sAccountType;
	    }
	    return null;
	}

    public static void clearAccountType() {
        if(sAccountType != null){
            mIsClearflag  = true;
        }
    }

    public static void removeAccountType(long mId) {
        if(sAccountType != null){
            mDeleteIds .add(mId);
        }
    }

}
