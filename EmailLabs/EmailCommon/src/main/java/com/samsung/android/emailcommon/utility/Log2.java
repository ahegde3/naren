package com.samsung.android.emailcommon.utility;

public class Log2{
	
	public static void v(String tag, String message) {
		Log.v(tag, message);
		EmailLog.v(tag, message);
	}

	public static void i(String tag, String message) {
		Log.i(tag, message);
		EmailLog.i(tag, message);
	}
	
	public static void d(String tag, String message) {
		Log.d(tag, message);
		EmailLog.d(tag, message);
	}

	public static void w(String tag, String message) {
		Log.w(tag, message);
		EmailLog.w(tag, message);
	}

	public static void e(String tag, String message) {
		Log.e(tag, message);
		EmailLog.e(tag, message);
	}
}
