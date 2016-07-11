package com.samsung.android.emailcommon;

import java.util.UUID;

public class BlackList {

	private String mUuid;
	private String mBlackListName;
	private String mBlackListEmailAddress;
	private long mBlackListAccountKey;
	private long mLastAccessedTimeStamp;
	private int mIsDomain;
	
	private static final String BLACK_LIST_UUIDS = "blackListUuids";
	
	private static final String KEY_BLACK_LIST_NAME = ".blackListName";
	private static final String KEY_BLACK_LIST_EMAIL_ADDRESS = ".blackListEmailAddress";
	private static final String KEY_BLACK_LIST_ACCOUNT_KEY = ".blackListAccountKey";
	private static final String KEY_LAST_ACCESSED_TIME_STEMP = ".lastAccessedTimeStamp";
	private static final String KEY_IS_DOMAIN = ".isDomain";
	
	public BlackList() {
		mUuid = UUID.randomUUID().toString();
		mBlackListName = null;
		mBlackListEmailAddress = null;
		mBlackListAccountKey = -1;
		mLastAccessedTimeStamp = -1;
		mIsDomain = -1;
	}
	
    public BlackList(Preferences preferences, String uuid) {

        this.mUuid = uuid;

        refresh(preferences);

    }
	
	public void setUuid(String Uuid) {
        this.mUuid = Uuid;
    }
	
	public String gettUuid() {
        return mUuid;
    }
	
	public void setBlackListName (String blackListName) {
    	this.mBlackListName = blackListName;
    }
    
    public String getBlackListName () {
    	return mBlackListName;
    }
    
    public void setBlackListEmailAddress(String blackListEmailAddress) {
    	this.mBlackListEmailAddress = blackListEmailAddress;
    }
    
    public String getBlackListEmailAddress () {
    	return mBlackListEmailAddress;
    }
    
    public void setBlackListAccountKey(long blackListAccountKey) {
    	this.mBlackListAccountKey = blackListAccountKey;
    }
    
    public long getBlackListAccountKey () {
    	return mBlackListAccountKey;
    }
    
    public void setLastAccessedTimeStamp(long lastAccessedTimeStamp) {
    	this.mLastAccessedTimeStamp = lastAccessedTimeStamp;
    }
    
    public long getLastAccessedTimeStamp () {
    	return mLastAccessedTimeStamp;
    }
    
    public void setIsDomain(int isDomain) {
    	this.mIsDomain = isDomain;
    }
    
    public int getIsDomain () {
    	return mIsDomain;
    }
    
    public void save(Preferences preferences) {
		
		if (!preferences.getStringAccountInformation(BLACK_LIST_UUIDS, "").contains(mUuid)) {
			
			StringBuffer blackListUuids = new StringBuffer(preferences.getStringAccountInformation(
					BLACK_LIST_UUIDS, ""));// sec.email tom.jung For RSAR
			blackListUuids.append((blackListUuids.length() != 0 ? "," : "")).append(mUuid);
            preferences.putAccountInformation(BLACK_LIST_UUIDS, blackListUuids.toString());
		}
		
		preferences.putAccountInformation(mUuid + KEY_BLACK_LIST_NAME, mBlackListName);
		preferences.putAccountInformation(mUuid + KEY_BLACK_LIST_EMAIL_ADDRESS, mBlackListEmailAddress);
		preferences.putAccountInformation(mUuid + KEY_BLACK_LIST_ACCOUNT_KEY, mBlackListAccountKey);
		preferences.putAccountInformation(mUuid + KEY_LAST_ACCESSED_TIME_STEMP, mLastAccessedTimeStamp);
		preferences.putAccountInformation(mUuid + KEY_IS_DOMAIN, mIsDomain);
	}
    
	public void delete(Preferences preferences) {
		
		String[] uuids = preferences.getStringAccountInformation(BLACK_LIST_UUIDS, "").split(",");
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = uuids.length; i < length; i++) {
            if (!uuids[i].equals(mUuid)) {
                if (sb.length() > 0) {
                    sb.append(',');
                }
                sb.append(uuids[i]);
            }
        }
        
        String blackListUuids = sb.toString();
        preferences.putAccountInformation(BLACK_LIST_UUIDS, blackListUuids);
        
        preferences.removeAccountInformations(mUuid + KEY_BLACK_LIST_NAME);
        preferences.removeAccountInformations(mUuid + KEY_BLACK_LIST_EMAIL_ADDRESS);
        preferences.removeAccountInformations(mUuid + KEY_BLACK_LIST_ACCOUNT_KEY);
        preferences.removeAccountInformations(mUuid + KEY_LAST_ACCESSED_TIME_STEMP);
        preferences.removeAccountInformations(mUuid + KEY_IS_DOMAIN);
        
	}
    
	private void refresh(Preferences preferences) {
		
		mBlackListName = preferences.getStringAccountInformation(mUuid + KEY_BLACK_LIST_NAME, null);
		mBlackListEmailAddress = preferences.getStringAccountInformation(mUuid + KEY_BLACK_LIST_EMAIL_ADDRESS, null);
		mBlackListAccountKey = preferences.getLongAccountInformation(mUuid + KEY_BLACK_LIST_ACCOUNT_KEY, (long)0);
		mLastAccessedTimeStamp = preferences.getLongAccountInformation(mUuid + KEY_LAST_ACCESSED_TIME_STEMP, (long)0);
		mIsDomain = preferences.getIntAccountInformation(mUuid + KEY_IS_DOMAIN, -1);
	}
}
