/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.samsung.android.emailcommon.log;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.samsung.android.emailcommon.utility.EmailLog;


/**
 * A class to keep last N of lines sent to the server and responses received
 * from the server. They are sent to logcat when {@link #logLastDiscourse} is
 * called.
 * <p>
 * This class is used to log the recent network activities when a response
 * parser crashes.
 */
public class DiscourseLogger {
    public static final String TAG = "DiscourseLogger";
	
    private final int mBufferSize;

    private String[] mBuffer;

    private int mPos;

    private boolean mPrefixDate = true;

    private final StringBuilder mReceivingLine = new StringBuilder(100);

    public DiscourseLogger(int bufferSize) {
        mBufferSize = bufferSize;
        initBuffer();
    }

    public DiscourseLogger(int bufferSize, boolean prefixDate) {
        mPrefixDate = prefixDate;
        mBufferSize = bufferSize;
        initBuffer();
    }

    private void initBuffer() {
        mBuffer = new String[mBufferSize];
    }

    /** Add a single line to {@link #mBuffer}. */
    synchronized private void addLine(String s) {
        if (mPrefixDate) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                s = "time=" + sdf.format(cal.getTime()) + " " + s;
            } catch (NoSuchMethodError e) {
                EmailLog.d(TAG, "addLine: NoSuchMethodError, cannot get current datetime");
            }
        }

        mBuffer[mPos] = s;
        mPos++;
        if (mPos >= mBufferSize) {
            mPos = 0;
        }
    }

    private void addReceivingLineToBuffer() {
        if (mReceivingLine.length() > 0) {
            addLine(mReceivingLine.toString());
            mReceivingLine.delete(0, Integer.MAX_VALUE);
        }
    }

    /**
     * Store a single byte received from the server in {@link #mReceivingLine}.
     * When LF is received, the content of {@link #mReceivingLine} is added to
     * {@link #mBuffer}.
     */
    public void addReceivedByte(int b) {
        if (0x20 <= b && b <= 0x7e) { // Append only printable ASCII chars.
            mReceivingLine.append((char) b);
        } else if (b == '\n') { // LF
            addReceivingLineToBuffer();
        } else if (b == '\r') { // CR
        } else {
            final String hex = "00" + Integer.toHexString(b);
            mReceivingLine.append("\\x" + hex.substring(hex.length() - 2, hex.length()));
        }
    }

    /** Add a line sent to the server to {@link #mBuffer}. */
    public void addSentCommand(String command) {
        addLine(command);
    }

    public void logString(String s) {
        EmailLog.d(TAG, s);
        addLine(s);
    }

    /** @return the contents of {@link #mBuffer} as a String array. */
    /* package for testing */String[] getLines() {
        addReceivingLineToBuffer();

        ArrayList<String> list = new ArrayList<String>();

        final int start = mPos;
        int pos = mPos;
        do {
            String s = mBuffer[pos];
            if (s != null) {
                list.add(s);
            }
            pos = (pos + 1) % mBufferSize;
        } while (pos != start);

        String[] ret = new String[list.size()];
        list.toArray(ret);
        return ret;
    }

    /**
     * Log the contents of the {@link mBuffer}, and clears it out. (So it's okay
     * to call this method successively more than once. There will be no
     * duplicate log.)
     */
    public void logLastDiscourse() {
        logLastDiscourse(false);
        // initBuffer();
    }

    /**
     * Log the contents of the {@link mBuffer}, and clears it out. (So it's okay
     * to call this method successively more than once. There will be no
     * duplicate log.)
     */
    public void logLastDiscourse(boolean useError) {
    	EmailLog.e(TAG, "Last activities: [Current Position - " + mPos + "/" + mBufferSize + "]");
        String[] lines = getLines();
        if (lines.length == 0) {
            return;
        }
        for (String r : getLines()) {
            if (useError) {
            	EmailLog.e(TAG, r); // Mostly, only err logs r enabled in Prod build
            } else {
            	EmailLog.w(TAG, r);
            }
        }
        // initBuffer();
    }
    
    /**
     * Prints the contents into PrintWrtier, Called as part of service dump.
     */
    public void logLastDiscourse(PrintWriter writer) {
    	writer.println("Statistics: ");
        String[] lines = getLines();
        if (lines.length == 0) {
            return;
        }
        for (String r : getLines()) {
            writer.println("   " + r);
        }
    }
    
    public void logLastDiscourse(PrintWriter writer, String prefix) {
        EmailLog.e(TAG, "Last activities: [Current Position - " + mPos + "/" + mBufferSize + "]");
        for (String r : getLines()) {
            writer.println(prefix + r);
        }
    }
    
    

}
