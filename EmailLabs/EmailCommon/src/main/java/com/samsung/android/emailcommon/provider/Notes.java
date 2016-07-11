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

//package android.provider;  //Move notes.java

package com.samsung.android.emailcommon.provider;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public final class Notes {

    public static final String TAG = "Notes";

    public static final String AUTHORITY = "com.android.notes";

    public static final String MESSAGE_TABLE = "message";
    public static final String UPDATE_MESSAGE_TABLE = "update_message";
    public static final String DELETED_MESSAGE_TABLE = "deleted_message";
    public static final String ACCOUNT_TABLE = "deleted_message";
    public static final String BODY_TABLE = "body";

    public static final int SYNC_DONE = 0;
    public static final int SYNC_REQUIRED_NEW_CLIENT_MSG = SYNC_DONE + 1;
    public static final int SYNC_REQUIRED_UPDATE_CLIENT_MSG = SYNC_DONE + 2;
    public static final int SYNC_REQUIRED_DELETE_CLIENT_MSG = SYNC_DONE + 3;
    public static final int SYNC_REQUIRED = SYNC_DONE + 4;

    /**
     * The content:// style URL for the top-level tasks authority
     */
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final Uri CONTENT_URI_SYNC = Uri.parse("content://" + AUTHORITY + "/syncNote");

    public static final Uri CONTENT_URI_LOCAL = Uri.parse("content://" + AUTHORITY + "/localNote");

    public static final String CALLER_IS_SYNCADAPTER = "caller_is_syncadapter";

    // TODO
    // public static final String SYNC1 = "sync1";
    /**
     * Columns from the Calendars table that other tables join into themselves.
     */

    public static final int CLIENT_OP_TYPE = 0;
    public static final int CLIENT_OP_ADD_NOTE = CLIENT_OP_TYPE + 1;
    public static final int CLIENT_OP_DEL_NOTE = CLIENT_OP_TYPE + 2;
    public static final int CLIENT_OP_MODIFY_NOTE = CLIENT_OP_TYPE + 3;

    public interface Columns {
        public static final String ID = MessageColumns.ID;
        public static final String OP_TYPE = "opType";
        public static final String EMAIL_ADDRESS = AccountColumns.EMAIL_ADDRESS;
        public static final String SUBJECT = MessageColumns.SUBJECT;
        public static final String CATEGORIES = MessageColumns.CATEGORIES;
        public static final String LAST_MODIFIED_DATE = MessageColumns.LAST_MODIFIED_DATE;
        public static final String TEXT = BodyColumns.TEXT_CONTENT;
        public static final String TEXT_TYPE = BodyColumns.TYPE;
    }

    public static final String[] PROJECTION = new String[] {
            Columns.ID, Columns.EMAIL_ADDRESS, Columns.SUBJECT, Columns.CATEGORIES,
            Columns.LAST_MODIFIED_DATE, Columns.TEXT, Columns.TEXT_TYPE
    };

    public interface MessageColumns {
        public static final String ID = "_id";
        // Basic columns used in message list presentation
        // The name as shown to the user in a message list
        public static final String DISPLAY_NAME = "displayName";
        // The time (millis) as shown to the user in a message list [INDEX]

        // when last time note was modified "ON" the server
        public static final String LAST_MODIFIED_DATE = "last_modified_date";

        // It can holds multiple categories in delimiters seperated strings.
        public static final String CATEGORIES = "categories";

        public static final String TIMESTAMP = "timeStamp";
        // Message subject
        public static final String SUBJECT = "subject";
        // Boolean, unread = 0, read = 1 [INDEX]
        public static final String FLAG_READ = "flagRead";
        // Load state, see constants below (unloaded, partial, complete,
        // deleted)
        public static final String FLAG_LOADED = "flagLoaded";

        // Sync related identifiers
        // Any client-required identifier
        public static final String CLIENT_ID = "clientId";
        // The message-id in the message's header
        public static final String NOTE_ID = "noteId";

        // Foreign key to the Account holding this message
        public static final String ACCOUNT_KEY = "accountKey";

        // Foreign key to the Account holding this message
        public static final String ACCOUNT_TYPE = "accountType";

        public static final String FLAG_MOVED = "flag_moved";

        public static final String SERVER_ID = "server_id";

        public static final String SERVER_TIMESTAMP = "server_timestamp";

        public static final String FLAG_SYNC_NEEDED = "flag_sync_needed";

    }

    public interface BodyColumns {
        public static final String ID = "_id";

        public static final String TYPE = "type";

        public static final String SIZE = "size";

        public static final String TRUNCATED = "truncated";

        // Foreign key to the Note corresponding to this body
        public static final String NOTE_KEY = "noteKey";
        // The html content itself
        public static final String HTML_CONTENT = "htmlContent";
        // The plain text content itself
        public static final String TEXT_CONTENT = "textContent";
        public static final String ACCOUNT_KEY = "accountKey";

    }

    public interface AccountColumns {
        public static final String ID = "_id";
        // The display name of the account (user-settable)
        public static final String DISPLAY_NAME = "displayName";
        // The email address corresponding to this account
        public static final String EMAIL_ADDRESS = "emailAddress";
        // Foreign key to the Account holding this message
        public static final String ACCOUNT_KEY = "accountKey";

        // Foreign key to the Account holding this message
        public static final String ACCOUNT_TYPE = "accountType";

        // The default sync lookback period for this account
        public static final String NEW_MESSAGE_COUNT = "newMessageCount";

        // The default sync lookback period for this account
        public static final String MAILBOXID = "mailBoxId";

        public static final String SYNC_NEEDED = "syncNeeded";

    }

    /**
     * Contains a list of tasks.
     */
    public static class Message implements BaseColumns, MessageColumns {

        public static final String[] MESSAGE_PROJECTION = new String[] {
                ID, ACCOUNT_KEY, SERVER_ID, SUBJECT, DISPLAY_NAME, LAST_MODIFIED_DATE, CATEGORIES,
                FLAG_SYNC_NEEDED
        };

//        private static final String WHERE_DELETE_FOR_ACCOUNT = Message.ACCOUNT_KEY + "=?" + " AND "
//                + Message.ACCOUNT_TYPE + "=?";

        // To refer to a specific message, use
        // ContentUris.withAppendedId(CONTENT_URI, id)
        public static final Uri CONTENT_URI = Uri.parse(Notes.CONTENT_URI + "/message");

        public static final Uri SYNCED_CONTENT_URI = Uri
                .parse(Notes.CONTENT_URI + "/syncedMessage");
        public static final Uri DELETED_CONTENT_URI = Uri.parse(Notes.CONTENT_URI
                + "/deletedMessage");
        public static final Uri UPDATED_CONTENT_URI = Uri.parse(Notes.CONTENT_URI
                + "/updatedMessage");

        public static final String TABLE_NAME = "Message";
        public static final String UPDATED_TABLE_NAME = "Message_Updates";
        public static final String DELETED_TABLE_NAME = "Message_Deletes";

        /*
         * Implement it later public static int
         * deleteMessageForAccount(ContentResolver cr, Account account) { //
         * delete all calendars that match this account return
         * Calendar.Calendars.delete(cr, WHERE_DELETE_FOR_ACCOUNT, new String[]
         * { account.name, account.type }); }
         */

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
        // public static final Uri CONTENT_URI = Uri.parse("content://"+
        // AUTHORITY);

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = LAST_MODIFIED_DATE;

        /**
         * The URL to the notes
         * <P>
         * Type: TEXT (URL)
         * </P>
         */
        public static final String URL = "url";

        /**
         * The name of the notes
         * <P>
         * Type: TEXT
         * </P>
         */
        public static final String NAME = "name";

        public static final String SYNC_DIRTY = "_sync_dirty";

        // change@wtl.jane added
        public static final String DELETED = "deleted";

        public static final String _CLIENT_ID = "clientId";
    }

    public static class Body implements BaseColumns, BodyColumns {
        public static final String[] BODY_PROJECTION = new String[] {
                ID, NOTE_KEY, TEXT_CONTENT, HTML_CONTENT, TYPE, SIZE, TRUNCATED
        };

        public static final String TABLE_NAME = "Body";
        public static final Uri CONTENT_URI = Uri.parse(Notes.CONTENT_URI + "/body");

        public static final Cursor query(ContentResolver cr, String[] projection, String where,
                String orderBy) {
            return cr.query(CONTENT_URI, projection, where, null, orderBy);
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
        // public static final Uri CONTENT_URI = Uri.parse("content://"+
        // AUTHORITY);

        /**
         * The default sort order for this table
         */

        /**
         * The URL to the notes
         * <P>
         * Type: TEXT (URL)
         * </P>
         */
        public static final String URL = "url";

        /**
         * The name of the notes
         * <P>
         * Type: TEXT
         * </P>
         */
        public static final String NAME = "name";

        public static final String SYNC_DIRTY = "_sync_dirty";

        // change@wtl.jane added
        public static final String DELETED = "deleted";

        public static final String _CLIENT_ID = "clientId";
    }

    public static class Account implements BaseColumns, AccountColumns {

        public static final String[] ACCOUNT_PROJECTION = new String[] {
                ID, ACCOUNT_KEY, ACCOUNT_TYPE, DISPLAY_NAME, EMAIL_ADDRESS, NEW_MESSAGE_COUNT
        };

        public static final String TABLE_NAME = "Account";
        public static final Uri CONTENT_URI = Uri.parse(Notes.CONTENT_URI + "/account");

        // public static final Uri CONTENT_URI = Notes.CONTENT_URI ;

        public static final Cursor query(ContentResolver cr, String[] projection, String where,
                String orderBy) {
            return cr.query(CONTENT_URI, projection, where, null, orderBy);
        }

        public static int delete(ContentResolver cr, String selection, String[] selectionArgs) {
            return cr.delete(CONTENT_URI, selection, selectionArgs);
        }

        public static final String URL = "url";
        public static final String NAME = "name";

        public static final String SYNC_DIRTY = "_sync_dirty";

        public static final String DELETED = "deleted";

        public static final String _CLIENT_ID = "clientId";
    }
}
