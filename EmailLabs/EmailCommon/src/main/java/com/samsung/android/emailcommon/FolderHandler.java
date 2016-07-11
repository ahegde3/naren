package com.samsung.android.emailcommon;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

public class FolderHandler {
	final static public String AUTHORITY = "com.samsung.android.emailsync.folderprovider";
	final static public Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
	final static public String VALUE_FOLDER_NAME = "folerName";
	final static public String VALUE_PARENT_ID = "parentId";
	final static public String VALUE_MAILBOX_ID = "mailboxId";
	final static public String VALUE_OPS = "operation";
	
	final static public int OPS_TYPE_RENAME = 1;
	final static public int OPS_TYPE_MOVE = 2;

	static public long createFolder(Context context, long accountId, long parentMailboxId, String mame) {
		ContentValues cv = new ContentValues();
		cv.put(VALUE_PARENT_ID, parentMailboxId);
		cv.put(VALUE_FOLDER_NAME, mame);
		Uri cmdUri = ContentUris.withAppendedId(CONTENT_URI, accountId);
		try {
    		Uri uri = context.getContentResolver().insert(cmdUri, cv);
    		if (uri != null) {
    			return Long.parseLong(uri.getPathSegments().get(1));
    		}
		} catch (IllegalArgumentException e){
		    e.printStackTrace();
		}
		
		return -1;
	}
	
	static public int deleteFolder(Context context, long mailboxId) {
		return context.getContentResolver().delete(CONTENT_URI, null, new String [] { String.valueOf(mailboxId)});
	}
	
	static public int renameFolder(Context context, long mailboxId, String folderName) {
		ContentValues cv = new ContentValues();
		cv.put(VALUE_OPS, OPS_TYPE_RENAME);
		cv.put(VALUE_FOLDER_NAME, folderName);
		cv.put(VALUE_MAILBOX_ID, mailboxId);
		return context.getContentResolver().update(CONTENT_URI, cv, null, null);
	}
	
	static public int moveFolder(Context context, long mailboxId, long targetMailboxId) {
		ContentValues cv = new ContentValues();
		cv.put(VALUE_OPS, OPS_TYPE_MOVE);
		cv.put(VALUE_PARENT_ID, targetMailboxId);
		cv.put(VALUE_MAILBOX_ID, mailboxId);
		return context.getContentResolver().update(CONTENT_URI, cv, null, null);
	}
}
