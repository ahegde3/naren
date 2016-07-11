package com.samsung.android.emailcommon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class VerifiedDomain {

//	private static final String TAG = "VerifiedDomain";
	
	final static public Uri URI = Preferences.CONTENT_URI;
	
	public static final String GET_VERIFIED_DOMAINS = "getVerifiedDomains";
    public static final String PUT_VERIFIED_DOMAIN = "putVerifiedDomain";
    public static final String CLEAR_VERIFIED_DOMAINS = "clearVerifiedDomains";
    public static final String DOMAIN_NAME = "domainName";
    
    private static VerifiedDomain _inst = null;
	
	protected Context mContext = null;
	
	public static VerifiedDomain getInstance(Context context) {
		if (_inst == null) {
			_inst = new VerifiedDomain(context);
		}
		return _inst;
	}
	
	protected VerifiedDomain(Context context) {
		mContext = context;
	}
	
	static public void release() {
		if (_inst == null)
			return;
		_inst.mContext = null;
		_inst = null;
	}
	
    public boolean isVerified(String domain) {
        Boolean isVerified = false;
        if (mContext != null) {
            QueryUtil qu = QueryUtil.createInstance(mContext.getContentResolver());
            Cursor c = qu.query(URI, null, GET_VERIFIED_DOMAINS);
            if (c != null && !c.isClosed()) {
                if (c.moveToFirst()) {
                    do {
                        if (c.getString(0).equals(domain)) {
                            isVerified = true;
                            break;
                        }
                    } while (c.moveToNext());
                }
                c.close();
            }
        }
        return isVerified;
    }
	
	public void putVerifiedDomain (Context context, String domain) {
		ContentValues cv = new ContentValues();
	    cv.put(DOMAIN_NAME, domain);
	    context.getContentResolver().update(URI, cv, PUT_VERIFIED_DOMAIN, null);
	}
	
	public void clearVerifiedDomains () {

        if (mContext != null)
            mContext.getContentResolver().delete(URI, CLEAR_VERIFIED_DOMAINS, null);
	}
}
