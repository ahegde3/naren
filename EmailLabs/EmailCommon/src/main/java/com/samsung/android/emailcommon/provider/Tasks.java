/*
 * Copyright (C) 2006 The Android Open Source Project
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

//package android.provider;  //Move tasks.java

package com.samsung.android.emailcommon.provider;


import com.samsung.android.emailcommon.utility.EmailLog;

import android.accounts.Account;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * @hide
 */
public final class Tasks {

    public static final String TAG = "Tasks";

    public static final String AUTHORITY = "tasks";

    public static final String TABLE_NAME = "syncTasks";
    // public static final String COMPLETED_TABLE_NAME = "CompletedTasks";
    public static final String DELETED_TABLE_NAME = "DeletedTasks";
    public static final String UPDATED_TABLE_NAME = "UpdatedTasks";
    public static final String SYNC_TASKS = "syncTasks";
    // change@siso.stella for TasksAccounts Table
    public static final String TASKS_ACCOUNTS_TABLE_NAME = "TasksAccounts";
    public static final String REMINDER_TASKS = "TasksReminders";

    public static final String TASK_REMINDER_ACTION = "android.intent.action.TASK_REMINDER";

    /**
     * The content:// style URL for the top-level tasks authority
     */
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri TASK_CONTENT_URI = Uri.parse("content://com.android.calendar/"
            + TABLE_NAME);
    // public static final Uri COMPLETED_CONTENT_URI =
    // Uri.parse("content://tasks/" + COMPLETED_TABLE_NAME);

    public static final Uri UPDATED_CONTENT_URI = Uri.parse("content://com.android.calendar/"
            + UPDATED_TABLE_NAME);

    public static final Uri DELETED_CONTENT_URI = Uri.parse("content://com.android.calendar/"
            + DELETED_TABLE_NAME);

    public static final Uri SYNCHED_TASKS_CONTENT_URI = Uri.parse("content://com.android.calendar/"
            + SYNC_TASKS);
    // change@siso.stella for TasksAccounts Table
    public static final Uri TASKS_ACCOUNTS_CONTENT_URI = Uri
            .parse("content://com.android.calendar/" + TASKS_ACCOUNTS_TABLE_NAME);

    public static final String CALLER_IS_SYNCADAPTER = "caller_is_syncadapter";

    // TODO
    // public static final String SYNC1 = "sync1";
    /**
     * Columns from the Calendars table that other tables join into themselves.
     */
    public interface TasksColumns {
        /**
         * A string that uniquely identifies this task to its source
         */
        public static final String SOURCE_ID = "sourceid";

        public static final String BODY_TYPE = "bodyType";
        public static final String BODY = "body";
        public static final String BODY_SIZE = "body_size";
        public static final String BODY_TRUNCATED = "body_truncated";
        public static final String CATEGORY1 = "category1";
        public static final String CATEGORY2 = "category2";
        public static final String CATEGORY3 = "category3";
        public static final String COMPLETE = "complete";
        public static final String DATE_COMPLETED = "date_completed";
        public static final String DUE_DATE = "due_date";
        public static final String UTC_DUE_DATE = "utc_due_date";
        public static final String IMPORTANCE = "importance";
        public static final String RECURRENCE_TYPE = "recurrence_type";
        public static final String RECURRENCE_START = "recurrence_start";
        public static final String RECURRENCE_UNTIL = "recurrence_until";
        public static final String RECURRENCE_OCCURRENCES = "recurrence_occurrences";
        public static final String RECURRENCE_INTERVAL = "recurrence_interval";
        public static final String RECURRENCE_DAY_OF_MONTH = "recurrence_day_of_month";
        public static final String RECURRENCE_DAY_OF_WEEK = "recurrence_day_of_week";
        public static final String RECURRENCE_WEEK_OF_MONTH = "recurrence_week_of_month";
        public static final String RECURRENCE_MONTH_OF_YEAR = "recurrence_month_of_year";
        public static final String RECURRENCE_REGENERATE = "recurrence_regenerate";
        public static final String RECURRENCE_DEAD_OCCUR = "recurrence_dead_occur";
        public static final String REMINDER_SET = "reminder_set";
        public static final String REMINDER_TIME = "reminder_time";
        public static final String SENSITIVITY = "sensitivity";
        public static final String START_DATE = "start_date";
        public static final String UTC_START_DATE = "utc_start_date";
        public static final String SUBJECT = "subject";

        // for recurrence exceptions, the following is non null
        public static final String PARENT_TASKID = "parentId";
        public static final String MAILBOX_KEY = "mailboxKey";
        // Foreign key to the Account holding this message
        public static final String ACCOUNT_KEY = "accountKey";
        public static final String ACCOUNT_NAME = "accountName";
        public static final String REMINDER_TYPE = "reminder_type";
        public static final String _SYNC_DIRTY = "_sync_dirty";
    }

    /**
     * Contains a list of tasks.
     */
    public static class TasksTbl implements BaseColumns, TasksColumns {
        public static final Cursor query(ContentResolver cr, String[] projection, String where,
                String orderBy) {
            return cr.query(CONTENT_URI, projection, where, null,
                    orderBy == null ? DEFAULT_SORT_ORDER : orderBy);
        }

        /**
         * Convenience method perform a delete on the Calendar provider
         * 
         * @param cr the ContentResolver
         * @param selection the rows to delete
         * @return the count of rows that were deleted
         */
        public static int delete(ContentResolver cr, String selection, String[] selectionArgs) {
            return cr.delete(CONTENT_URI, selection, selectionArgs);
        }

        /**
         * Convenience method to delete all tasks that match the account.
         * 
         * @param cr the ContentResolver
         * @param account the account whose rows should be deleted
         * @return the count of rows that were deleted
         */

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = Uri.parse("content://com.android.calendar/syncTasks");

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = DUE_DATE;

        /**
         * The URL to the tasks
         * <P>
         * Type: TEXT (URL)
         * </P>
         */
        public static final String URL = "url";

        /**
         * The name of the tasks
         * <P>
         * Type: TEXT
         * </P>
         */
        public static final String NAME = "name";

        

        // change@wtl.jane added
        public static final String DELETED = "deleted";

        public static final String _CLIENT_ID = "clientId";
    }

    public interface TaskReminderColumns {

        public static final String TASK_ID = "task_id";
        public static final String TASK_STATE = "state";
        public static final String TASK_REMINDER_TIME = TasksTbl.REMINDER_TIME;
        public static final String TASK_SUBJECT = TasksTbl.SUBJECT;
        public static final String TASK_START_DATE = TasksTbl.START_DATE;
        public static final String TASK_DUE_DATE = TasksTbl.DUE_DATE;
        public static final String TASK_ACCOUNT_KEY = "accountkey";
        public static final String TASK_REMINDER_TYPE = TasksTbl.REMINDER_TYPE;
    }

    public static class TaskReminderAlerts implements BaseColumns, TaskReminderColumns {

        public static final Uri REMINDER_CONTENT_URI = Uri.parse(Tasks.CONTENT_URI + "/"
                + REMINDER_TASKS);

        public static final Uri SPLANNER_REMINDER_CONTENT_URI = Uri.parse("content://com.android.calendar/" + "/"
                + REMINDER_TASKS);

        /*
         * Schedules a task Reminder
         */

        public static void scheduleReminder(Context context, AlarmManager manager, long alarmTime,
                long task_id) {

        	EmailLog.v(TAG, " inside schedule reminder to set a reminder for a task ");

            if (manager == null)
                manager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

            Intent intent = new Intent(TASK_REMINDER_ACTION);
            intent.setData(ContentUris.withAppendedId(Tasks.TASK_CONTENT_URI, task_id));
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
            manager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pi);

            Time time = new Time();
            time.set(alarmTime);
            String schedTime = time.format(" %a, %b %d, %Y %I:%M%P");
            EmailLog.d(TAG, "Scheduled reminder at " + alarmTime + " " + schedTime);
        }

        public static void removeReminder(Context context, long task_id) {
            ContentResolver cr = context.getContentResolver();
            String selection = TaskReminderColumns.TASK_ID + "=?";
            String[] projectionIn = {
                TaskReminderAlerts._ID
            };
            // String selection = TasksTbl._ID;
            String[] selectionArgs = {
                Long.toString(task_id)
            };
            Uri reminderUri = TaskReminderAlerts.REMINDER_CONTENT_URI;
            // Log.i("inside updateInternal of task provider for reminder update id is "
            // , ""+ id);
            Cursor c1 = null;
            c1 = cr.query(reminderUri, projectionIn, selection, selectionArgs, null);

            if (c1 == null)
                return;
            try {
                while (c1.moveToNext()) {
                    long id = c1.getLong(0);
                    reminderUri = ContentUris.withAppendedId(
                            TaskReminderAlerts.REMINDER_CONTENT_URI, id);
                    EmailLog.i("inside removereminder of task provider for reminder delete id is ", ""
                            + id);
                    cr.delete(reminderUri, null, null);
                }
                c1.close();
            } catch (Exception e) {

            } finally {
                if (!c1.isClosed())
                    c1.close();
            }
        }
    }

    public static class TasksAccounts implements BaseColumns, TasksColumns {

        public static final Cursor query(ContentResolver cr, String[] projection, String where,
                String orderBy) {
            return cr.query(CONTENT_URI, projection, where, null,
                    orderBy == null ? DEFAULT_SORT_ORDER : orderBy);
        }

        /**
         * Convenience method perform a delete on the Tasks provider
         * 
         * @param cr the ContentResolver
         * @param selection the rows to delete
         * @return the count of rows that were deleted
         */
        public static int delete(ContentResolver cr, String selection, String[] selectionArgs) {
            return cr.delete(CONTENT_URI, selection, selectionArgs);
        }

        /**
         * Convenience method to delete all tasks that match the account.
         * 
         * @param cr the ContentResolver
         * @param account the account whose rows should be deleted
         * @return the count of rows that were deleted
         */
        public static int deleteTasksForAccount(ContentResolver cr, Account account) {
            // delete all tasks that match this account
            return Tasks.TasksAccounts.delete(cr, WHERE_DELETE_FOR_ACCOUNT, new String[] {
                    account.name, account.type
            });
        }

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = Uri
                .parse("content://com.android.calendar/TasksAccounts");

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = "displayName";

        /**
         * The URL to the tasks of the account
         * <P>
         * Type: TEXT (URL)
         * </P>
         */
        public static final String URL = "url";

        /**
         * The display name of the Tasks account
         * <P>
         * Type: TEXT
         * </P>
         */
        public static final String DISPLAY_NAME = "displayName";

        public static final String SELECTED = "selected";

        /**
         * The account that was used to sync the entry to the device.
         * <P>
         * Type: TEXT
         * </P>
         */
        public static final String _SYNC_ACCOUNT = "_sync_account";

        /**
         * The type of the account that was used to sync the entry to the
         * device.
         * <P>
         * Type: TEXT
         * </P>
         */
        public static final String _SYNC_ACCOUNT_KEY = "_sync_account_key";

        /**
         * The type of the account that was used to sync the entry to the
         * device.
         * <P>
         * Type: TEXT
         * </P>
         */
        public static final String _SYNC_ACCOUNT_TYPE = "_sync_account_type";
        
        private static final String WHERE_DELETE_FOR_ACCOUNT = _SYNC_ACCOUNT + "=?";

    }
}
