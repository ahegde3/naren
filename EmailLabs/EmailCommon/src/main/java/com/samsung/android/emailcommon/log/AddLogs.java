package com.samsung.android.emailcommon.log;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

public class AddLogs implements AddLogsConst {

    private ContentResolver mContentResolver = null;
    static protected AddLogs INSTANCE = null;
    private Context mContext = null;
    
    
    protected AddLogs(Context context) {
        mContext = context;
        if(context != null)
            mContentResolver = context.getContentResolver();
    }
    
    static public AddLogs createInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new AddLogs(context);
        }
        return  INSTANCE;
    }
    
    public static void logAccountSetup(Context context, String msg) {
        createInstance(context).addLogsProvider(msg, ACCOUNTSETUP);
        release();
    }

    public static void logAccountStats(Context context, String msg) {
        createInstance(context).addLogsProvider(msg, ACCOUNTSTATS);
        release();
    }
    
    public static void logImapDraftsSync(Context context, String msg) {
        createInstance(context).addLogsProvider(msg, IMAPDRAFTSSYNC);
        release();
    }

    public static void logCertErrorDlgInteraction(Context context, String msg) {
        createInstance(context).addLogsProvider(msg, CERT_ERROR_DIALOG);
        release();
    }
    
    
    public void addLogsProvider(String msg, String type) {
        ContentValues cv = new ContentValues();
        cv.put(TYPE, type);
        cv.put(MSG, msg);
        if(mContentResolver != null)
            mContentResolver.update(CONTENT_URI, cv, null, null);
    }
    private static void release()
    {
    	if(INSTANCE!=null)
    	{
    		INSTANCE.mContentResolver = null;
    		INSTANCE.mContext = null;
    		INSTANCE = null;
    	}
    }
}
