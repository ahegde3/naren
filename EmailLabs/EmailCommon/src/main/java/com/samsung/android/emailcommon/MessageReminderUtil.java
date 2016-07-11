package com.samsung.android.emailcommon;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.samsung.android.emailcommon.provider.EmailContent.Message;
import com.samsung.android.emailcommon.provider.EmailContent.MessageColumns;
import com.samsung.android.emailcommon.utility.AppLogging;
import com.samsung.android.emailcommon.variant.CommonDefs;


public class MessageReminderUtil {

    protected final static String TAG = "MessageReminderUtil";
    public interface ReminderColumns {
        public static final String TABLE_NAME = "Reminders";
        public static final String ID = "_id";
        public static final String ACCOUNT_ID = "accountId";
        public static final String MESSAGE_ID = "messageId";
        public static final String REMINDER_ID = "reminderId";
        public static final String TIMESTAMP = "timeStamp";
        public static final String TRIGGERED = "triggerd";
    }

    final static public Uri CONTENT_URI = Uri.parse("content://com.samsung.android.email.remind.provider");
    final static public String ACTION_REMINDER = "com.samsung.android.email.reminder";

    static public void setReminder(Context context, long accountId, long messageId, long when) {
        setReminder(context, accountId, messageId, when, null);
    }

    static public void setReminder(final Context context, final long accountId, final long messageId, final long when, final Runnable withEndAction) {
        new Thread( new Runnable() {
            @Override
            public void run() {

                boolean saved = false;
                String reminderId = null;
                Cursor c = null;
                try {
                    c = context.getContentResolver().query(CONTENT_URI, new String [] {ReminderColumns.REMINDER_ID}, 
                    		ReminderColumns.MESSAGE_ID + "=?" + " AND " + ReminderColumns.ACCOUNT_ID + "=?"
                    		+ " AND " + ReminderColumns.TRIGGERED + "=0", new String [] {String.valueOf(messageId), String.valueOf(accountId)}, null);
                    if (c != null && c.moveToFirst()) {
                        saved = true;
                        reminderId = c.getString(0);
                    }
                } finally {
                    if (c != null)
                        c.close();
                }
                if (saved) {
                    ContentValues cv = new ContentValues();
                    cv.put(ReminderColumns.TIMESTAMP, when);
                    context.getContentResolver().update(CONTENT_URI, cv, ReminderColumns.REMINDER_ID + "=?", new String [] {reminderId});
                } else {
                    ContentValues cv = new ContentValues();
                    cv.put(ReminderColumns.TIMESTAMP, when);
                    cv.put(ReminderColumns.REMINDER_ID, messageId);
                    cv.put(ReminderColumns.MESSAGE_ID, messageId);
                    cv.put(ReminderColumns.ACCOUNT_ID, accountId);
                    cv.put(ReminderColumns.TRIGGERED, 0);
                    context.getContentResolver().insert(CONTENT_URI, cv);
                }
                if (withEndAction != null) {
                    withEndAction.run();
                }
            }
        }).start();

    }

    static public void unsetReminder(Context context, long accountId, long messageId) {
        unsetReminder(context, accountId, messageId, null);
    }
    static public void unsetReminder(final Context context, final long accountId, final long messageId, final Runnable withEndAction) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                if (context == null) return;
                context.getContentResolver().delete(CONTENT_URI, ReminderColumns.MESSAGE_ID + "=?" + " AND " + ReminderColumns.ACCOUNT_ID + "=?", new String [] {String.valueOf(messageId), String.valueOf(accountId)});
                if (withEndAction != null) {
                    withEndAction.run();
                }
            }
        }).start();

        AppLogging.insertLog(context, AppLogging.APPLOGGING_APP_ID ,AppLogging.APPLOGGING_FEATURE_REMINDER_OFF, null);
    }

    static public long getReminder(Context context, final long accountId, final long messageId) {
        long val = 0;
        Cursor c = null;
        if( context == null) return val;
        try {
            c = context.getContentResolver().query(CONTENT_URI, new String [] {ReminderColumns.TIMESTAMP}, 
            		ReminderColumns.TRIGGERED + "=0" +" AND " + ReminderColumns.MESSAGE_ID + "=?" + " AND " + ReminderColumns.ACCOUNT_ID + "=?", new String [] {String.valueOf(messageId), String.valueOf(accountId)}, null);
            if (c != null && c.moveToFirst()) {
                val = c.getLong(0);
            }
        } finally {
            if (c != null)
                c.close();
        }
        return val;
    }
    
    static public HashMap<Long, Boolean> getReminders(Context context, final String selectionArg) {
        HashMap<Long, Boolean> hasReminders = new HashMap<Long, Boolean> ();
        Cursor c = null;
        if( context == null) return hasReminders;
        try {
            c = context.getContentResolver().query(CONTENT_URI, new String [] {ReminderColumns.MESSAGE_ID, ReminderColumns.TIMESTAMP}, 
            		ReminderColumns.TRIGGERED + "=0" + " AND " + ReminderColumns.MESSAGE_ID +" IN (" + selectionArg +")", null, null);
            if (c != null && c.getCount() > 0) {
                c.moveToPosition(-1);
                long messageId = -1;
                long timeStamp = -1;
                boolean hasReminder = false;
                while(c.moveToNext()) {
                    messageId = c.getLong(0);
                    timeStamp = c.getLong(1);
                    hasReminder = timeStamp > System.currentTimeMillis();
                    hasReminders.put(messageId, hasReminder);
                }
            }
        } finally {
            if (c != null)
                c.close();
        }
        return hasReminders;
    }

    
    static public HashMap<Long, Boolean> getReminders(Context context, final long accountId, final String selectionArg) {
        HashMap<Long, Boolean> hasReminders = new HashMap<Long, Boolean> ();
        Cursor c = null;
        if( context == null) return hasReminders;
        try {
            c = context.getContentResolver().query(CONTENT_URI, new String [] {ReminderColumns.MESSAGE_ID, ReminderColumns.TIMESTAMP}, 
                    ReminderColumns.ACCOUNT_ID + " = " + accountId + " AND " + ReminderColumns.MESSAGE_ID +" IN (" + selectionArg +")", null, null);
            if (c != null && c.getCount() > 0) {
                c.moveToPosition(-1);
                long messageId = -1;
                long timeStamp = -1;
                boolean hasReminder = false;
                while(c.moveToNext()) {
                	messageId = c.getLong(0);
                	timeStamp = c.getLong(1);
                	hasReminder = timeStamp > System.currentTimeMillis();
                	hasReminders.put(messageId, hasReminder);
                }
            }
        } finally {
            if (c != null)
                c.close();
        }
        return hasReminders;
    }


    static public boolean hasReminder(Context context, long accountId, long mesageId) {
        return getReminder(context, accountId, mesageId) != 0;
    }

    static public int getReminderCount(Context context) {
    	final StringBuilder selection = new StringBuilder();
    	int count = 0;
    	List<Long> listIds = getRemindersWithMessageId(context);
        if (listIds.size() > 0) {
            selection.append(splitSelection(listIds, 3));
        } else {
            selection.append(MessageColumns.ID).append("= -1");
        }
        if (CommonDefs.EAS_LOCAL_DB_OPERATION) {
            selection.append(" AND ").append(MessageColumns.EAS_LOCAL_DELETE_FLAG).append("=0");
        }
        selection.append(" AND ").append(Message.FLAG_LOADED_SELECTION);
        selection.append(" AND ").append(MessageColumns.FLAG_DELETEHIDDEN + "=0");
        Cursor cursor = context.getContentResolver().query(Message.CONTENT_URI,  new String [] {Message.MESSAGE_ID}, selection.toString(), null, null);
        if (cursor != null) {
        	count = cursor.getCount();
        	cursor.close();
        }
    	return count;
    }
    
    static private String splitSelection(List<Long> whole, int number) {
        if (whole.size() == 0)
            return "";
        int i = 0;
        StringBuilder sb = new StringBuilder();
        boolean start = true;
        int part = 0;
        sb.append("(");
        for (Long data : whole) {
            if (start) {
                if (part > 0) {
                    sb.append(" OR ");
                }
                sb.append(MessageColumns.ID).append(" in (");
            } else {
                sb.append(",");
            }
            sb.append(data);
            start = false;
            i ++;
            if (i % number == 0) {
                sb.append(") ");
                part ++;
                start = true;
            }
        }
        if (!start) {
            sb.append(") ");
        }
        sb.append(") ");
        return sb.toString();
    }
    
    static public List<Long> getRemindersWithMessageId(Context context) {
        Cursor c = null;
        ArrayList<Long> alarmSet = new ArrayList<Long> ();
        try {
            c = context.getContentResolver().query(CONTENT_URI, new String [] {ReminderColumns.MESSAGE_ID, ReminderColumns.TRIGGERED}, 
                    null, null, null);
            while (c != null && c.moveToNext()) {
            	if (c.getInt(1) == 0) {
                    alarmSet.add(Long.parseLong(c.getString(0)));
            	}
            }
        } finally {
            if (c != null)
                c.close();
        }

        return alarmSet;
    }

    static public void unsetReminderWhere(Context context, String where) {
        unsetReminderWhere(context, where, null);
    }

    static public void unsetReminderWhere(final Context context, final String where,
            final Runnable withEndAction) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                context.getContentResolver().delete(CONTENT_URI, where, null);
                if (withEndAction != null) {
                    withEndAction.run();
                }
            }
        }).start();

    }
}

