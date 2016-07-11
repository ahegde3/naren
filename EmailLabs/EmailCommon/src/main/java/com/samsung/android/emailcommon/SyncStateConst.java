package com.samsung.android.emailcommon;

import android.net.Uri;

public interface SyncStateConst {
	public static final String TYPE = "type";
	public static final String ID = "id";
	public static final String STARTED = "started";
	public static final String PROGRESS = "progress";
	public static final String MESSAGING_EXCEPTION = "messaging_exception";
	final static public String MESSAGESYNC = "messagesync";
	final static public String SENDINGSYNC = "sendingsync";
	final static public String FOLDERSYNC = "foldersync"; 
	final static public String BADSYNC = "badsync"; 
	final static public String LOADMORESYNC = "loadmoresync";
	final static public int TYPE_MESSAGESYNC = 1;
	final static public int TYPE_SENDINGSYNC = 2;
	final static public int TYPE_FOLDERSYNC = 3;
	final static public int TYPE_BADSYNC = 4;
	final static public int TYPE_LOADMORESYNC = 5;
	public static final Uri CONTENT_URI = Uri.parse("content://com.samsung.android.email.syncstate.provider");
}
