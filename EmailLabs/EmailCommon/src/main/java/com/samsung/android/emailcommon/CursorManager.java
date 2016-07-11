package com.samsung.android.emailcommon;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class CursorManager {
	
	static private CursorManager _inst;
	static private Context _context;
	static public CursorManager inst(Context context) {
		if (_inst == null) {
			_inst = new CursorManager();
		}
		_context = context.getApplicationContext();
		return _inst;
	}
	public Cursor query(Uri uri, String[] proj, String sel, String[] args, String aa) {
		Cursor c = _context.getContentResolver().query(uri, proj, sel, args, aa);
		if (LeakTestCursor.ENABLE_LEAK_MONITOR)
			return new LeakTestCursor(c);
		return c;
	}
}
