package com.samsung.android.emailcommon;

import java.util.UUID;

public class Vip {

	private String mUuid;
	private int mContactId;
	private int mEmailId;
	private String mEmailAddress;
	private String mDisplayName;
	private int mSenderOrder;
	
	private static final String VIP_UUIDS = "vipUuids";
	
	private static final String KEY_CONTACT_ID = ".contactId";
	private static final String KEY_EMAIL_ID = ".emailId";
	private static final String KEY_EMAIL_ADDRESS = ".emailAddress";
	private static final String KEY_DISPLAY_NAME = ".displayName";
	private static final String KEY_SENDER_ORDER = ".senderOrder";
	
	public Vip() {
		mUuid = UUID.randomUUID().toString();
		mContactId = -1;
		mEmailId = -1;
		mEmailAddress = null;
		mDisplayName = null;
		mSenderOrder = -1;
	}
	
    public Vip(Preferences preferences, String uuid) {

        this.mUuid = uuid;

        refresh(preferences);

    }
	
	public void setUuid(String Uuid) {
        this.mUuid = Uuid;
    }
	
	public String gettUuid() {
        return mUuid;
    }
	
	public void setContactId(int contactId) {
        this.mContactId = contactId;
    }
	
	public int getContactId() {
        return mContactId;
    }
	
	public void setEmailId(int emailId) {
        this.mEmailId = emailId;
    }
	
	public int getEmailId() {
        return mEmailId;
    }
	
	public void setEmailAddress(String emailAddress) {
        this.mEmailAddress = emailAddress;
    }
	
	public String getEmailAddress() {
        return mEmailAddress;
    }
	
	public void setDisplayName(String displayName) {
        this.mDisplayName = displayName;
    }
	
	public String getDisplayName() {
        return mDisplayName;
    }
	
	public void setSenderOrder(int senderOrder) {
        this.mSenderOrder = senderOrder;
    }
	
	public int getSenderOrder() {
        return mSenderOrder;
    }
	
	public void save(Preferences preferences) {
		
		if (!preferences.getStringAccountInformation(VIP_UUIDS, "").contains(mUuid)) {
			
			StringBuffer vipUuids = new StringBuffer(preferences.getStringAccountInformation(
					VIP_UUIDS, ""));// sec.email tom.jung For RSAR
			vipUuids.append((vipUuids.length() != 0 ? "," : "")).append(mUuid);
            preferences.putAccountInformation(VIP_UUIDS, vipUuids.toString());
		}
		
		preferences.putAccountInformation(mUuid + KEY_CONTACT_ID, mContactId);
		preferences.putAccountInformation(mUuid + KEY_EMAIL_ID, mEmailId);
		preferences.putAccountInformation(mUuid + KEY_EMAIL_ADDRESS, mEmailAddress);
		preferences.putAccountInformation(mUuid + KEY_DISPLAY_NAME, mDisplayName);
		preferences.putAccountInformation(mUuid + KEY_SENDER_ORDER, mSenderOrder);
	}
	
	public void delete(Preferences preferences) {
		
		String[] uuids = preferences.getStringAccountInformation(VIP_UUIDS, "").split(",");
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = uuids.length; i < length; i++) {
            if (!uuids[i].equals(mUuid)) {
                if (sb.length() > 0) {
                    sb.append(',');
                }
                sb.append(uuids[i]);
            }
        }
        
        String vipUuids = sb.toString();
        preferences.putAccountInformation(VIP_UUIDS, vipUuids);
        
        preferences.removeAccountInformations(mUuid + KEY_CONTACT_ID);
        preferences.removeAccountInformations(mUuid + KEY_EMAIL_ID);
        preferences.removeAccountInformations(mUuid + KEY_EMAIL_ADDRESS);
        preferences.removeAccountInformations(mUuid + KEY_DISPLAY_NAME);
        preferences.removeAccountInformations(mUuid + KEY_SENDER_ORDER);
        
	}
	
	private void refresh(Preferences preferences) {
		
		mContactId = preferences.getIntAccountInformation(mUuid + KEY_CONTACT_ID, -1);
		mEmailId = preferences.getIntAccountInformation(mUuid + KEY_EMAIL_ID, -1);
		mEmailAddress = preferences.getStringAccountInformation(mUuid + KEY_EMAIL_ADDRESS, null);
		mDisplayName = preferences.getStringAccountInformation(mUuid + KEY_DISPLAY_NAME, null);
		mSenderOrder = preferences.getIntAccountInformation(mUuid + KEY_SENDER_ORDER, -1);
	}
}
