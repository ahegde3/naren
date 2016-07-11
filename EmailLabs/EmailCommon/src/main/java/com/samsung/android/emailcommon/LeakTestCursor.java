package com.samsung.android.emailcommon;


import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.HashSet;

import com.samsung.android.emailcommon.utility.EmailLog;

public class LeakTestCursor extends CursorWrapper {
	
	static private HashSet<LeakTestCursor> sOpenedCursorList = new HashSet<LeakTestCursor>();
	static public boolean ENABLE_LEAK_MONITOR = false;
	
	private String callStack;
	
	public LeakTestCursor(Cursor cursor) {
		super(cursor);
		if (ENABLE_LEAK_MONITOR) {
			callStack = CallStack();
			sOpenedCursorList.add(this);
		}
	}
	
	private String CallStack() {
    	StringBuffer stacktrace = new StringBuffer();
    	StackTraceElement[] stacks = new Exception().getStackTrace();
    	int maxDepth = 7;
    	if (stacks.length < maxDepth)
    		maxDepth = stacks.length;
    	for (int i = 2; i < maxDepth; i ++) {
    		stacktrace.append(stacks[i].toString() + "\n");
    	}
    	return stacktrace.toString();
    }
	@Override
	public void close() {
		if (ENABLE_LEAK_MONITOR)
			sOpenedCursorList.remove(this);
	}
	
	static public void printOutOpenCursor () {
		for (LeakTestCursor each : sOpenedCursorList) {
			EmailLog.e("OpenCursor", each.callStack + "\n<-><-><-><-><->\n");
		}
	}
}
