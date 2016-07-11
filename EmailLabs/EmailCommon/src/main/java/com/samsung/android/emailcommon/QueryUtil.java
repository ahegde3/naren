package com.samsung.android.emailcommon;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.samsung.android.emailcommon.system.CarrierValues;
import com.samsung.android.emailcommon.utility.Log2;
import com.samsung.android.emailcommon.utility.Utility;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.MatrixCursor;
import android.net.Uri;

public class QueryUtil {
	
	static protected boolean _debug = CarrierValues.IS_BUILD_TYPE_ENG;
	
	static protected WeakReference<QueryUtil> _inst = null;
	
	
	protected QueryUtil(ContentResolver resolver) {
		mResolver = resolver;
	}

	private ContentResolver mResolver = null;
	final static private String TAG = "QueryUtils";
	static private boolean _trace = true;
	private ArrayList<InnerCursor> mCursors = new ArrayList<InnerCursor> (); 
	
	
	static public QueryUtil createInstance(ContentResolver resolver) {
		
		QueryUtil qu = null;
		if (_inst == null) {
			qu = new QueryUtil(resolver);
			_inst = new WeakReference<QueryUtil> (qu);
		} else {
			qu = _inst.get();
			if (qu == null) {
				qu = new QueryUtil(resolver);
				_inst = new WeakReference<QueryUtil> (qu);
			} 
		}
		
		return qu;
	}
	
	public Cursor query(Uri uri, String [] projection, String selection) {
		return query(uri, projection, selection, null);
	}
	
	public Cursor query(Uri uri, String [] projection, String selection, String [] selectionARgs) {
		return query(uri, projection, selection, selectionARgs, null);
	}
	
	public Cursor query(Uri uri, String [] projection, String selection, String [] selectionArgs, String sortOrder) {
		Cursor c = null;
		if (mResolver != null) {
			c = mResolver.query(uri, projection, selection, selectionArgs, sortOrder);
			if (c == null)
				return  null;
			if (_trace) {
				if (_debug) {
					c = new InnerCursor(c, new Exception("for test"));
				} else {
					c = new InnerCursor(c, projection, selection, selectionArgs, sortOrder);
				}
			}
		} else {
			Log2.d(TAG, "Cursor util is not initialized");
			if(projection != null)
			    c = new MatrixCursor(projection);
		}
		return c;
	}
	
	@Override
	protected void finalize() throws Throwable {
		if (mCursors.size() > 0) {
			Log2.w(TAG, "uninitialized instance remained");
			if (_trace) {
				if (_debug) {
					synchronized (mCursors) {
						for (InnerCursor c: mCursors) {
							Log2.w(TAG, "instance stack : " + c.toString());
						}
					}
				}
			}
		}
        super.finalize();
	}
	
	private class InnerCursor extends CursorWrapper {
		private String trace = null;
		public InnerCursor(Cursor cursor, Exception e) {
			super(cursor);
			trace = Utility.callStack(e);
			mCursors.add(this);
		}
		
		public InnerCursor(Cursor cursor, String [] proj, String selection, String [] args, String sortOrder) {
			super(cursor);
			StringBuilder sb = new StringBuilder();
			sb.append("projection : ");
			if (proj == null) {
				sb.append(" null ");
			} else {
				for (String p : proj) {
					sb.append(p).append(",");
				}
			}
			sb.append(" selection : ");
			sb.append(selection);
			if (args != null) {
				sb.append(" args : ");
				for (String a : args) {
					sb.append(a).append(",");
				}
			}
			if (sortOrder != null) {
				sb.append(" sorts : ").append(sortOrder);
			}
			trace = sb.toString();
			mCursors.add(this);
		}
		
		public void close() {
			super.close();
			mCursors.remove(this);
		}
		
		@Override
		public String toString() {
			if (trace == null)
				trace = "";
			return trace;
		}
		
		
	}
}
