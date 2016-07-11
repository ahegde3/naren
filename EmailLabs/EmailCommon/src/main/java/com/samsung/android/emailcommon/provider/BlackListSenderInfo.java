/******************************************************************************************
  Samsung 
  File Name 	: BlackListSenderInfo.java
  Description 	: This class contains the blacklist senders info this is used along with   
  				  the CBlackListModule to insert, delete and retrieve the senders list
  Author(s)	 	: Kumar Atul (kumar.atul@samsung.com)
   				  Ashok Das (ashok.das10@samsung.com)
   				  Naresh Korapati (naresh.kora@samsung.com)

 *******************************************************************************************/

package com.samsung.android.emailcommon.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;

import com.samsung.android.emailcommon.provider.EmailContent.BlackList;
import com.samsung.android.emailcommon.utility.EmailLog;

public class BlackListSenderInfo {

	private String mUserName;
	private String mEmailAddress;
	private long mAccountId;
	private long mMsgId;
	private long mLastAccessedTime;
	private int mIsDomain = 0;
	private Context mContext;
	private int id;
	// private static final String BLACKLIST_ID = BlackList.EMAIL_ADDRESS
	// + "=? AND " + BlackList.ACCOUNT_KEY + "=?";
    private static final String BLACKLIST_ID = BlackList.EMAIL_ADDRESS + "=? COLLATE NOCASE";

	private static final String TAG = "BlackListInfo";

	public BlackListSenderInfo() {

	}

	public BlackListSenderInfo(Context _context) {
		super();
		this.mContext = _context;
	}

	public BlackListSenderInfo(Context _context, String emailAddress,
			long accountId, long messageId) {
		super();
		this.mContext = _context;
		this.mEmailAddress = emailAddress;
		this.mAccountId = accountId;
		this.mMsgId = messageId;
	}

	public BlackListSenderInfo(Context _context, String userName,
			String emailAddress, long accountId, long messageId, int isDomain) {
		super();
		this.mContext = _context;
		this.mUserName = userName;
		this.mEmailAddress = emailAddress;
		this.mAccountId = accountId;
		this.mMsgId = messageId;
		this.mLastAccessedTime = System.currentTimeMillis();
		this.mIsDomain = isDomain;
	}

	public BlackListSenderInfo(Context _context, String userName,
			String emailAddress, long accountId, long messageId, int isDomain,
			int id) {
		super();
		this.mContext = _context;
		this.mUserName = userName;
		this.mEmailAddress = emailAddress;
		this.mAccountId = accountId;
		this.mMsgId = messageId;
		this.mLastAccessedTime = System.currentTimeMillis();
		this.mIsDomain = isDomain;
		this.id = id;
	}

	/**
	 * Checks whether the sender is present in the Blacklist table
	 * 
	 * @return true if present else false
	 */
	public boolean isPresentInBlackListTable() {
		ContentResolver resolver = mContext.getContentResolver();
		BlackListSenderInfo item = this;
		Boolean isPresent = false;
        if(item.mEmailAddress == null) {
			return isPresent;
        }
		String[] args;
		args = new String[] { item.mEmailAddress };
		Cursor getItemCursor = resolver.query(BlackList.CONTENT_URI,
				BlackList.BLACKLIST_EMAIL_PROJECTION, BLACKLIST_ID, args,
				null);
		if (getItemCursor != null) {
			try {
				if (getItemCursor.moveToFirst()) {
					if (getItemCursor.getString(0).equalsIgnoreCase(
							item.mEmailAddress)) {
						isPresent = true;
					}
				}
			} finally {
				if (getItemCursor != null && getItemCursor.isClosed() != true)
					getItemCursor.close();
			}
		}
		return isPresent;
	}



	/**
	 * Inserts the sender into the blacklist table
	 * 
	 * @return true if added successfully
	 */
	public boolean insertIntoBlackListTable() {
		ContentResolver resolver = mContext.getContentResolver();
		BlackListSenderInfo item = this;
		boolean result = false;
		try {
			ContentValues values = new ContentValues();
			values.put(BlackList.USER_NAME, item.mUserName);
			values.put(BlackList.EMAIL_ADDRESS, item.mEmailAddress);
			values.put(BlackList.ACCOUNT_KEY, item.mAccountId);
			values.put(BlackList.LAST_ACCESSED_TIME_STAMP,
					System.currentTimeMillis());
			values.put(BlackList.IS_DOMAIN, item.mIsDomain);
			resolver.insert(BlackList.CONTENT_URI, values);
			result = true;
		} catch (SQLiteConstraintException e) {
			EmailLog.d(TAG, "Caught SQLiteConstraintException");
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/**
	 * Update the sender info in blacklist table
	 * 
	 * @return true if updated successfully
	 */
	public boolean updateEmailaddressDomaininfoInBlackListTable() {
		ContentResolver resolver = mContext.getContentResolver();
		BlackListSenderInfo item = this;
		final String Selection = "_id=?";
		boolean result = false;
		try {
			ContentValues values = new ContentValues();
			values.put(BlackList.USER_NAME, mUserName);
			values.put(BlackList.EMAIL_ADDRESS, item.mEmailAddress);
			values.put(BlackList.LAST_ACCESSED_TIME_STAMP,
					System.currentTimeMillis());
			values.put(BlackList.IS_DOMAIN, item.mIsDomain);
			resolver.update(BlackList.CONTENT_URI, values, Selection,
					new String[] { String.valueOf(item.id) });
			result = true;
		} catch (SQLiteConstraintException e) {
			EmailLog.d(TAG, "Caught SQLiteConstraintException");
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/**
	 * delete the blacklist sender from the blacklist table
	 * 
	 * @param record
	 *            id of the sender in the table
	 * @return true if deleted successfully
	 */
	public boolean deleteFromBlackListTableWithId(String selectedId) {
		ContentResolver resolver = mContext.getContentResolver();

		String[] args;
		args = new String[] { selectedId };
		resolver.delete(BlackList.CONTENT_URI, BlackList.RECORD_ID + "=?", args);

		return true;
	}

	/**
	 * deletes the blacklist entries for the account
	 * 
	 * @param accountId
	 * @return
	 */
	boolean deleteFromBlackListTableWithAccountId(long acctId) {
		ContentResolver resolver = mContext.getContentResolver();
		String[] args = new String[] { Long.toString(acctId) };

		resolver.delete(BlackList.CONTENT_URI, BlackList.ACCOUNT_KEY + "=?",
				args);

		return true;
	}

	
	/**
	 * delete the blacklist sender from the blacklist table
	 * 
	 * @param 
	 * @return true if deleted successfully
	 */
	boolean deleteFromBlackListTableWithEmailAddress() {
		ContentResolver resolver = mContext.getContentResolver();

		BlackListSenderInfo item = this;
		String[] args = new String[] { item.mEmailAddress };
		resolver.delete(BlackList.CONTENT_URI, BlackList.EMAIL_ADDRESS
				+ "=? COLLATE NOCASE", args);
		return true;
	}

	long getAccountId() {
		return mAccountId;
	}

	public long getMsgId() {
		return mMsgId;
	}

	public String getEmailAddress() {
		return mEmailAddress;
	}

    public ContentValues toContentValues(){
        BlackListSenderInfo item = this;
        ContentValues values = new ContentValues();
        values.put(BlackList.USER_NAME, item.mUserName);
        values.put(BlackList.EMAIL_ADDRESS, item.mEmailAddress);
        values.put(BlackList.ACCOUNT_KEY, item.mAccountId);
        values.put(BlackList.LAST_ACCESSED_TIME_STAMP, System.currentTimeMillis());
        values.put(BlackList.IS_DOMAIN, item.mIsDomain);
        return values;
    }
}
