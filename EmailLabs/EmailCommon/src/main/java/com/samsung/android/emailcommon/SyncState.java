package com.samsung.android.emailcommon;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.samsung.android.emailcommon.mail.MessagingException;
import com.samsung.android.emailcommon.provider.EmailContent.Mailbox;
import com.samsung.android.emailcommon.provider.EmailContent.Account;
import com.samsung.android.emailcommon.utility.Log;
import com.samsung.android.emailcommon.utility.Log2;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;

@SuppressLint("UseSparseArrays")
public class SyncState implements SyncStateConst{
	
	static protected SyncState _inst = null;
	
	

	static public String TAG = "SyncState";

	
	public static final int SENDING_START = 0;
	public static final int SENDING_OPEN = 10;
	public static final int SENDING_BODY = 30;
	public static final int SENDING_ATTACHMENT = 100;
	public static final int SENDING_FINISH = 100;
	public static final int SENDING_FAIL = -1;
	
	
	abstract static public class Callback {
		String mTag;
		public Callback(String tag) {
			mTag = tag;
		}
		abstract public void onNotifyChanged(int type, long accountId, long mailboxId, long messageId, boolean started, int progress, int exception);
	}
	private static ContentResolver mContentResolver = null;
	
	
	private QueryUtil _qu = null;
	private EventObserver mEventObserver = null;
	private Context mContext = null;
	
	static private HashSet<Callback> sCallbacks = new HashSet<Callback>();
	
	protected SyncState(Context context) {
		mContext = context.getApplicationContext();
		mContentResolver = mContext.getContentResolver();
		_qu = QueryUtil.createInstance(mContentResolver);
	}
	
	static public SyncState createInstance(Context context) {

		if (_inst == null) {
			SyncState ss = new SyncState(context);
			_inst = ss;
			
		}
		
		return (SyncState) _inst;
	}
	
	static public void release() {
		Log.d(TAG, "check release");
		int count = sCallbacks.size();
		Log.d(TAG, "Remained listener : " + count);
		synchronized(sCallbacks) {
			for (Callback c : sCallbacks) {
				Log.d(TAG, "\tTAG : " + c.mTag);
			}
		}
		
	}
	public void addCallback(Callback callback) {
		if (callback != null) {
			if (_inst.mEventObserver == null) {
				_inst.mEventObserver = new EventObserver(mContext);
				try {
					SyncState.mContentResolver.registerContentObserver(CONTENT_URI, true, _inst.mEventObserver);
				} catch (Exception e) {
					Log.dumpException(TAG, e);
				}
			}
			synchronized (sCallbacks) {
				sCallbacks.add(callback);
			}
		}
	}
	public void removeCallback(Callback callback) {
		synchronized (sCallbacks) {
			sCallbacks.remove(callback);
		}
		if (sCallbacks.size() == 0) {
			try {
				mContentResolver.unregisterContentObserver(mEventObserver);
			} catch (Exception e) {
				Log.dumpException(TAG, e);
			}
			mEventObserver = null;
		}
	}

	public void updateMailboxSyncState(long mailboxId, boolean started) {
		updateMailboxSyncState(mailboxId, started, MessagingException.NO_ERROR);
	}
	
	public void updateMailboxSyncState(long mailboxId, boolean started, int exceptionType) {
	    Log.d(TAG, "updateMailboxSyncState : " + started + " mailboxId : " + mailboxId);
	    ContentValues cv = new ContentValues();
	    cv.put(TYPE, MESSAGESYNC);
	    cv.put(ID, mailboxId);
	    cv.put(STARTED, started);
	    cv.put(PROGRESS, 0);
	    cv.put(MESSAGING_EXCEPTION, exceptionType);
	    try{
	        mContentResolver.update(CONTENT_URI, cv, null, null);
	    }catch (IllegalArgumentException e) {
	    }
	}

	public void updateLoadMoreState(long mailboxId, boolean started) {

	    Log.d(TAG, "updateLoadMoreState : " + started + " mailboxId : " + mailboxId);
	    ContentValues cv = new ContentValues();
	    cv.put(TYPE, LOADMORESYNC);
	    cv.put(ID, mailboxId);
	    cv.put(STARTED, started);
	    try{
	        mContentResolver.update(CONTENT_URI, cv, null, null);
	    }catch (IllegalArgumentException e) {
	    }
	}

	public void updateAccountSendingState(long messageId, boolean started, int progress) {

	    Log.d(TAG, "updateAccountSendingState started : " + started + " messageId : " + messageId + " progress  " + progress);
	    ContentValues cv = new ContentValues();
	    cv.put(TYPE, SENDINGSYNC);
	    cv.put(ID, messageId);
	    cv.put(STARTED, started);
	    cv.put(PROGRESS, progress);
	    try{
	        mContentResolver.update(CONTENT_URI, cv, null, null);
	    }catch (IllegalArgumentException e) {
	    }
	}

	public void updateAccountFolderSyncStates(long accountId, boolean started) {
	    ContentValues cv = new ContentValues();
	    cv.put(TYPE, FOLDERSYNC);
	    cv.put(ID, accountId);
	    cv.put(STARTED, started);
	    try{
	        mContentResolver.update(CONTENT_URI, cv, null, null);
	    }catch (IllegalArgumentException e) {
	    }
	}

	public void updateBadSyncStates(long accountId, boolean started) {
	    ContentValues cv = new ContentValues();
	    cv.put(TYPE, BADSYNC);
	    cv.put(ID, accountId);
	    cv.put(STARTED, started);
	    try{
	        mContentResolver.update(CONTENT_URI, cv, null, null);
	    }catch (IllegalArgumentException e) {
	    }
	}

	public Set<Long> mailboxesOfRefreshingMessage () {
		Cursor c = null;
			
		HashSet<Long> rets = new HashSet<Long> ();
		try {
			c = _qu.query(CONTENT_URI, new String[] {MESSAGESYNC}, null);
			while (c != null && c.moveToNext()) {
				rets.add(c.getLong(0));
			}
		} finally {
			if (c != null && !c.isClosed())
				c.close();
		}
		
		return rets;
	}
	
	public Set<Long> accountOfSending() {
		Cursor c = null;
		
		HashSet<Long> rets = new HashSet<Long> ();
		try {
			c = _qu.query(CONTENT_URI, new String[] {SENDINGSYNC}, null);
			while (c != null && c.moveToNext()) {
				rets.add(c.getLong(0));
			}
		} finally {
			if (c != null && !c.isClosed())
				c.close();
		}
		
		return rets;
	}
	
	public Set<Long> accountOfLoadmore() {
		Cursor c = null;
		
		HashSet<Long> rets = new HashSet<Long> ();
		try {
			c = _qu.query(CONTENT_URI, new String[] {LOADMORESYNC}, null);
			while (c != null && c.moveToNext()) {
				rets.add(c.getLong(0));
			}
		} finally {
			if (c != null && !c.isClosed())
				c.close();
		}
		
		return rets;
	}
	
	public Set<Long> accountOfRefreshing() {
		Cursor c = null;
		
		HashSet<Long> rets = new HashSet<Long> ();
		try {
			c = _qu.query(CONTENT_URI, new String[] {FOLDERSYNC}, null);
			while (c != null && c.moveToNext()) {
				rets.add(c.getLong(0));
			}
		} finally {
			if (c != null && !c.isClosed())
				c.close();
		}
		
		return rets;
	}
	
	public Set<Long> accountOfBadSync() {
		Cursor c = null;
		
		HashSet<Long> rets = new HashSet<Long> ();
		try {
			c = _qu.query(CONTENT_URI, new String[] {BADSYNC}, null);
			while (c != null && c.moveToNext()) {
				rets.add(c.getLong(0));
			}
		} finally {
			if (c != null && !c.isClosed())
				c.close();
		}
		
		return rets;
	}
	
	@Override
	protected void finalize() throws Throwable {
		synchronized (sCallbacks) {
			if (sCallbacks.size() > 0)
				Log2.w(TAG, "remained observer");
			for (Callback c : sCallbacks) {
				Log2.w(TAG, c.mTag + ", ");
			}
		}
		super.finalize();
	}
	
	static private class EventObserver extends ContentObserver {
		Context mContext;
		public EventObserver(Context context) {
			super(null);
			mContext = context;
		}
		
		@Override
		public void onChange(boolean selfChange, Uri uri) {
			synchronized(sCallbacks) {
				List<String> paths = uri.getPathSegments();
				String type = paths.get(0);
				long id = Long.parseLong(paths.get(1));
				boolean isStarted = (Integer.parseInt(paths.get(2)) == 1);
				int idType = 0, progress = 0;
				long accountId, mailboxId, messageId=0;
				int exception = MessagingException.NO_ERROR;
				if (MESSAGESYNC.equals(type)) {
					idType = TYPE_MESSAGESYNC;
					Mailbox m = Mailbox.restoreMailboxWithId(mContext, id);
					mailboxId= id;
					accountId = (m == null ? -1 : m.mAccountKey);
					progress = Integer.parseInt(paths.get(3));
					exception = Integer.parseInt(paths.get(4));
				} else if (SENDINGSYNC.equals(type)) {
					idType = TYPE_SENDINGSYNC;
					mailboxId = Mailbox.NO_MAILBOX;
					messageId = id;
					accountId = Account.getAccountIdForMessageId(mContext, messageId);
					progress = Integer.parseInt(paths.get(3));
				} else if (FOLDERSYNC.equals(type)) {
					idType = TYPE_FOLDERSYNC;
					mailboxId = Mailbox.NO_MAILBOX;
					accountId = id;
				} else if (BADSYNC.equals(type)) {
					idType = TYPE_BADSYNC;
					mailboxId = Mailbox.NO_MAILBOX;
					accountId = id;
				} else if(LOADMORESYNC.equals(type)){
					idType = TYPE_LOADMORESYNC;
					Mailbox m = Mailbox.restoreMailboxWithId(mContext, id);
					mailboxId= id;
					accountId = (m == null ? -1 : m.mAccountKey);
					
				}else{
					accountId = -1;
					mailboxId = Mailbox.NO_MAILBOX;
				}
				for (Callback c : sCallbacks) {
					if (c != null) {
						c.onNotifyChanged(idType, accountId, mailboxId, messageId, isStarted, progress, exception);
					}
				}
			}
		}
		
		
	}
}
