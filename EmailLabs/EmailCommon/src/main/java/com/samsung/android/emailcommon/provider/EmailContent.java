/*
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

package com.samsung.android.emailcommon.provider;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.Data;
import android.text.TextUtils;

import com.samsung.android.emailcommon.AccountManagerTypes;
import com.samsung.android.emailcommon.EasITPolicy;
import com.samsung.android.emailcommon.EmailFeature;
import com.samsung.android.emailcommon.crypto.AESEncryptionUtil;
import com.samsung.android.emailcommon.mail.Snippet;
import com.samsung.android.emailcommon.system.CarrierValues;
import com.samsung.android.emailcommon.utility.AttachmentUtilities;
import com.samsung.android.emailcommon.utility.BodyUtilites;
import com.samsung.android.emailcommon.utility.EmailLog;
import com.samsung.android.emailcommon.utility.Log;
import com.samsung.android.emailcommon.utility.SyncScheduleData;
import com.samsung.android.emailcommon.utility.SyncScheduler;
import com.samsung.android.emailcommon.utility.Utility;
import com.samsung.android.emailcommon.EmailFeature;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * EmailContent is the superclass of the various classes of content stored by
 * EmailProvider. It is intended to include 1) column definitions for use with
 * the Provider, and 2) convenience methods for saving and retrieving content
 * from the Provider. This class will be used by 1) the Email process (which
 * includes the application and EmaiLProvider) as well as 2) the Exchange
 * process (which runs independently). It will necessarily be cloned for use in
 * these two cases. Conventions used in naming columns: RECORD_ID is the primary
 * key for all Email records The SyncColumns interface is used by all classes
 * that are synced to the server directly (Mailbox and Email) <name>_KEY always
 * refers to a foreign key <name>_ID always refers to a unique identifier
 * (whether on client, server, etc.)
 */
 
 /*
 * Jitha.mj
 * if editing the AUTHORITY , AUTHORITY_MULTI,NOTIFIER_AUTHORITY,CONTENT_URI,CONTENT_URI_MULTI,
 * CONTENT_NOTIFIER_URI, please update 
 * EmailConstants.java within EmailSDK.
 * Package name of EmailConstants in EmailSDK is : "com.samsung.android.sdk.emailcommon"
 * 
 */
public abstract class EmailContent {
    public static final String TAG = "EmailContent >>";

    public static final String AUTHORITY = "com.samsung.android.email.provider";
    public static final String AUTHORITY_MULTI = AUTHORITY; //"com.samsung.android.email.multiprovider";

    public static final String NOTIFIER_AUTHORITY = "com.samsung.android.email.notifier";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri CONTENT_URI_MULTI = Uri.parse("content://" + AUTHORITY_MULTI);

    public static final String PARAMETER_LIMIT = "limit";

    public static final Uri CONTENT_NOTIFIER_URI = Uri.parse("content://" + NOTIFIER_AUTHORITY);

    public static final String PROVIDER_PERMISSION = "com.samsung.android.email.permission.ACCESS_PROVIDER";

    // All classes share this
    public static final String RECORD_ID = "_id";

    public static final String ADDRESS = "emailAddress";

    public static final String DISPLAYNAME = "displayName";

    public static final String[] COUNT_COLUMNS = new String[] {
        "count(*)"
    };

    /**
     * This projection can be used with any of the EmailContent classes, when
     * all you need is a list of id's. Use ID_PROJECTION_COLUMN to access the
     * row data.
     */
    public static final String[] ID_PROJECTION = new String[] {
        RECORD_ID
    };

    public static final int ID_PROJECTION_COLUMN = 0;

    public static final String[] EMAILADDRESS_ACCOUNTID_PROJECTION = new String[] {
            RECORD_ID, ADDRESS, DISPLAYNAME
    };

    public static final int EMAILADDRESS_PROJECTION_COLUMN = 1;

    public static final int DISPLAYNAME_PROJECTION_COLUMN = 2;

    // P5_Porting_Email_Block_Start - worked by minsumr.kim
    // change@wtl.kSingh - to control auto syncs from System Account settings -
    // starts
    public static final String SYNC_INTERVAL_COLUMN = "syncInterval";

    public static final String[] CONTROLED_SYNC_PROJECTION = new String[] {
            RECORD_ID, SYNC_INTERVAL_COLUMN, AccountColumns.FLAGS
    };

    private static String[] MESSAGEID_TO_MAILBOXID_PROJECTION = new String[] {
            EmailContent.RECORD_ID, EmailContent.MessageColumns.MAILBOX_KEY
    };
    private static String[] MESSAGEID_TO_ACCOUNTID_PROJECTION = new String[] {
        EmailContent.RECORD_ID, EmailContent.MessageColumns.ACCOUNT_KEY
};

    private static String[] NOTEID_TO_ACCOUNTID_PROJECTION = new String[] {
        EmailContent.RECORD_ID, EmailContent.NoteColumns.ACCOUNT_KEY
    };

    public static String[] DRAFTS_SYNC_PROJECTION = new String[] {
        EmailContent.RECORD_ID, EmailContent.SyncColumns.SERVER_ID, EmailContent.MessageColumns.ACCOUNT_KEY,
        EmailContent.MessageColumns.MAILBOX_KEY, EmailContent.MessageColumns.DIRTY_COMMIT
};

    private static int MESSAGEID_TO_MAILBOXID_COLUMN_MAILBOXID = 1;

    private static int MESSAGEID_TO_ACCOUNTID_COLUMN_ACCOUNTID = 1;

    private static int NOTEID_TO_ACCOUNTID_COLUMN_ACCOUNTID = 1;

    public static final int CONTROLED_SYNC_ID_COLUMN = 0;

    public static final int CONTROLED_SYNC_INTERVAL_COLUMN = 1;

    public static final int CONTROLED_ACCOUNT_FLAGS_COLUMN = 2;

    // change@wtl.kSingh - to control auto syncs from System Account settings -
    // ends
    // P5_Porting_Email_Block_End - worked by minsumr.kim
    public static final String ID_SELECTION = RECORD_ID + " =?";

    public static final String FIELD_COLUMN_NAME = "field";

    public static final String ADD_COLUMN_NAME = "add";

    // Newly created objects get this id
    public static final int NOT_SAVED = -1;
    
    public static final int RECENTLY_COUNT_LIMIT = 30;

    public static final String WHERE_MAILBOX_KEY = Message.MAILBOX_KEY + "=?";

    public static final String WHERE_MESSAGE_ID = MessageColumns.ID + "=?";

    // The base Uri that this piece of content came from
    public Uri mBaseUri;

    // Lazily initialized uri for this Content
    private Uri mUri = null;

    // The id of the Content
    public long mId = NOT_SAVED;

    //change@siso addded constants for LDAPAccount table trustAll column
    public static final int IS_TRUST_ALL_NO = 0;
    public static final int IS_TRUST_ALL_YES = 1;

    // Write the Content into a ContentValues container
    public abstract ContentValues toContentValues();

    // Read the Content from a ContentCursor
    public abstract void restore(Cursor cursor);

    final static public String DEFAULT_SPAM = "Spambox";
    final static private String [] SPAMBOX_NAMES = new String [] {
        "Junk", "Spam", DEFAULT_SPAM
    };
    
    // The Uri is lazily initialized
    public Uri getUri() {
        if (mUri == null) {
            mUri = ContentUris.withAppendedId(mBaseUri, mId);
        }
        return mUri;
    }

    public boolean isSaved() {
        return mId != NOT_SAVED;
    }
    
    static private boolean debugNative = false;
    public static void checkNative() {
        if (debugNative) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Exception("Native checked").printStackTrace();
            }
        }
    }

    // The Content sub class must have a no-arg constructor
    static public <T extends EmailContent> T getContent(Cursor cursor, Class<T> klass) {
        try {
            T content = klass.newInstance();
            content.mId = cursor.getLong(0);
            content.restore(cursor);
            return content;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Uri save(Context context) {
        if (isSaved()) {
            throw new UnsupportedOperationException();
        }
        if (context != null) {
            Uri res = context.getContentResolver().insert(mBaseUri, toContentValues());
            if(res == null) return null;
            mId = Long.parseLong(res.getPathSegments().get(1));
            return res;
        }  else  {
            return null;
        }
    }

    /**
     * Generic count method that can be used for any ContentProvider
     *
     * @param context the calling Context
     * @param state dummy parameter to call overridden method
     * @return the Uri for the provider query
     */


//    public Uri save(Context context, boolean state) {
//        if (context != null) {
//            Uri res = context.getContentResolver().insert(mBaseUri, toContentValues());
//            mId = Long.parseLong(res.getPathSegments().get(1));
//            return res;
//        }  else  {
//            return null;
//        }
//    }
    
    public int update(Context context, ContentValues contentValues) {
        if (!isSaved()) {
            throw new UnsupportedOperationException();
        }
        return context.getContentResolver().update(getUri(), contentValues, null, null);
    }

    static public int update(Context context, Uri baseUri, long id, ContentValues contentValues) {
        return context.getContentResolver().update(ContentUris.withAppendedId(baseUri, id),
                contentValues, null, null);
    }

    static public int delete(Context context, Uri baseUri, long id) {
        return context.getContentResolver().delete(ContentUris.withAppendedId(baseUri, id), null,
                null);
    }

    /**
     * Generic count method that can be used for any ContentProvider
     * 
     * @param context the calling Context
     * @param uri the Uri for the provider query
     * @param selection as with a query call
     * @param selectionArgs as with a query call
     * @return the number of items matching the query (or zero)
     */
    
    
    static public int count(Context context, Uri uri, String selection, String[] selectionArgs) {
        return EmailContentUtils.getFirstRowLong(context, uri, COUNT_COLUMNS, selection, selectionArgs, null,
                0, Long.valueOf(-1)).intValue();
    }
    
    static public int count(Context context, String selection, String[] selectionArgs) {
        return EmailContentUtils.getFirstRowLong(context, Message.CONTENT_URI, COUNT_COLUMNS, selection, selectionArgs, null,
                0, Long.valueOf(-1)).intValue();
    }
    
    
    /**
     * Same as {@link #count(Context, Uri, String, String[])} without selection.
     */
    static public int count(Context context, Uri uri) {
        return count(context, uri, null, null);
    }

    static public int count(Context context) {
        return count(context, Account.CONTENT_URI);
    }

    // SncAdapter Porting
    static public long[] getIdListWhere(Context context, Uri uri, String where) {
        long[] idList;
        Cursor c = null;
        try {
            c = context.getContentResolver().query(uri, ID_PROJECTION, where, null, null);
            if (c.getCount() <= 0) {
                c.close();
                return null;
            }

            idList = new long[c.getCount()];
            int i = 0;
            if (c.moveToFirst()) {
                do {
                    idList[i++] = c.getLong(0); // column 0 is id
                } while (c.moveToNext());
            } else {
                return null;
            }
        } catch (Exception e) {
            EmailLog.e(TAG, "Exception in getIdListWhere", e);
            return null;
        } finally {
            if (c != null && !c.isClosed())
                c.close();
        }
        return idList;
    }

    // SncAdapter Porting
    /**
     * Checked if the passed accountId is SNC.
     * 
     * @param context the id of message
     * @param acctId account which needs to be updated
     * @param syncStatus Sync status for the account
     */
    public static boolean isSNCAccount(Context context, Long acctId) {
        boolean result = false;

        EmailContent.Account localAccount = EmailContent.Account.restoreAccountWithId(context,
                acctId);

        if (localAccount != null) {
            if (localAccount.mHostAuthKeyRecv > 0) {
                HostAuth ha = HostAuth
                        .restoreHostAuthWithId(context, localAccount.mHostAuthKeyRecv);
                if (ha != null && ha.mProtocol.equals("snc")) {
                    EmailLog.v(TAG, "account is SNC ");
                    result = true;
                }
            }
        }
        return result;
    }

    static public Uri uriWithLimit(Uri uri, int limit) {
        return uri.buildUpon()
                .appendQueryParameter(EmailContent.PARAMETER_LIMIT, Integer.toString(limit))
                .build();
    }

    /**
     * no public constructor since this is a utility class
     */
    // sh2209.cho Quick Response
    protected EmailContent() {
    }

    public interface SyncColumns {
        public static final String ID = "_id";

        // source id (string) : the source's name of this item
        public static final String SERVER_ID = "syncServerId";

        // source's timestamp (long) for this item
        public static final String SERVER_TIMESTAMP = "syncServerTimeStamp";
    }

    public interface BodyColumns {
        public static final String ID = "_id";

        // Foreign key to the message corresponding to this body
        public static final String MESSAGE_KEY = "messageKey";

        // The html content itself
        public static final String HTML_CONTENT = "htmlContent";

        // The plain text content itself
        public static final String TEXT_CONTENT = "textContent";

        // Replied-to or forwarded body (in html form)
        public static final String HTML_REPLY = "htmlReply";

        // Replied-to or forwarded body (in text form)
        public static final String TEXT_REPLY = "textReply";

        // A reference to a message's unique id used in reply/forward.
        // Protocol code can be expected to use this column in determining
        // whether a message can be
        // deleted safely (i.e. isn't referenced by other messages)
        public static final String SOURCE_MESSAGE_KEY = "sourceMessageKey";

        // The text to be placed between a reply/forward response and the
        // original message
        public static final String INTRO_TEXT = "introText";
        
        // Replied-to or forwarded body (in text form)
        public static final String FILE_SAVE_FLAGS = "fileSaveFlags";
    }

    public static final class Body extends EmailContent implements BodyColumns {
        public static final String TABLE_NAME = "Body";

        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI + "/body");
        public static final Uri CONTENT_URI_MULTI = Uri.parse(EmailContent.CONTENT_URI_MULTI + "/body");

        public static final int CONTENT_ID_COLUMN = 0;

        public static final int CONTENT_MESSAGE_KEY_COLUMN = 1;

        public static final int CONTENT_HTML_CONTENT_COLUMN = 2;

        public static final int CONTENT_TEXT_CONTENT_COLUMN = 3;

        public static final int CONTENT_HTML_REPLY_COLUMN = 4;

        public static final int CONTENT_TEXT_REPLY_COLUMN = 5;

        public static final int CONTENT_SOURCE_KEY_COLUMN = 6;

        public static final int CONTENT_INTRO_TEXT_COLUMN = 7;
        public static final int CONTENT_FILE_SAVE_FLAGS_COLUMN = 8;

        public static final int FLAGS_HTML_CONTENT_FILE = 1;
        public static final int FLAGS_TEXT_CONTENT_FILE = 2;
        public static final int FLAGS_HTML_REPLY_FILE = 4;
        public static final int FLAGS_TEXT_REPLY_FILE = 8;
        public static final int FLAGS_INTRO_FILE = 16;

        
        // Maximum message body size to store into DB for IMAP/POP3 accounts
        public static final int MESSAGE_BODY_MAX_SIZE = 100 * 1024; // 100 KB
        // Maximum DB transaction size to commit new messages during EAS account sync 
        public final static int MAX_EMAIL_SIZE_PER_REQ = 200 * 1024; // 200 KB
        // Message body part size, which will be stored in DB even if we are storing full body in files 
        public final static int MESSAGE_BODY_PART_SIZE_FOR_DB = 100 * 1024; // 100 KB

        public static final String[] CONTENT_PROJECTION = new String[] {
                RECORD_ID, BodyColumns.MESSAGE_KEY, BodyColumns.HTML_CONTENT,
                BodyColumns.TEXT_CONTENT, BodyColumns.HTML_REPLY, BodyColumns.TEXT_REPLY,
                BodyColumns.SOURCE_MESSAGE_KEY, BodyColumns.INTRO_TEXT, BodyColumns.FILE_SAVE_FLAGS
        };

        public static final String[] COMMON_PROJECTION_TEXT = new String[] {
                RECORD_ID, BodyColumns.TEXT_CONTENT
        };

        public static final String[] COMMON_PROJECTION_HTML = new String[] {
                RECORD_ID, BodyColumns.HTML_CONTENT
        };

        public static final String[] COMMON_PROJECTION_REPLY_TEXT = new String[] {
                RECORD_ID, BodyColumns.TEXT_REPLY
        };

        public static final String[] COMMON_PROJECTION_REPLY_HTML = new String[] {
                RECORD_ID, BodyColumns.HTML_REPLY
        };

        public static final String[] COMMON_PROJECTION_INTRO = new String[] {
                RECORD_ID, BodyColumns.INTRO_TEXT
        };

        public static final String[] COMMON_PROJECTION_SOURCE = new String[] {
                RECORD_ID, BodyColumns.SOURCE_MESSAGE_KEY
        };

        public static final int COMMON_PROJECTION_COLUMN_TEXT = 1;

        private static final String[] PROJECTION_SOURCE_KEY = new String[] {
            BodyColumns.SOURCE_MESSAGE_KEY
        };

        private static final String[] PROJECTION_FILE_SAVE_FLAGS = new String[] {
            BodyColumns.FILE_SAVE_FLAGS
        };
        
        public long mMessageKey;

        public String mHtmlContent;

        public String mTextContent;

        public String mHtmlReply;

        public String mTextReply;

        public long mSourceKey;

        public String mIntroText;
        public int mFileSaveFlags;

        public Body() {
            mBaseUri = CONTENT_URI;
        }

        @Override
        public ContentValues toContentValues() {
            ContentValues values = new ContentValues();

            // Assign values for each row.
            values.put(BodyColumns.MESSAGE_KEY, mMessageKey);
            if (mHtmlContent != null && mHtmlContent.length() > MESSAGE_BODY_MAX_SIZE) {
                values.put(BodyColumns.HTML_CONTENT, EmailContentUtils.cutText(mHtmlContent, 
                        MESSAGE_BODY_PART_SIZE_FOR_DB, BodyUtilites.getCutText(), true));
            } else {
                values.put(BodyColumns.HTML_CONTENT, mHtmlContent);
            }
            if (mTextContent != null && mTextContent.length() > MESSAGE_BODY_MAX_SIZE) {
                values.put(BodyColumns.TEXT_CONTENT, EmailContentUtils.cutText(mTextContent, 
                        MESSAGE_BODY_PART_SIZE_FOR_DB, BodyUtilites.getCutText(), false));
            } else {
                values.put(BodyColumns.TEXT_CONTENT, mTextContent);
            }
            if (mHtmlReply != null && mHtmlReply.length() > MESSAGE_BODY_MAX_SIZE) {
                values.put(BodyColumns.HTML_REPLY, EmailContentUtils.cutText(mHtmlReply, 
                        MESSAGE_BODY_PART_SIZE_FOR_DB, BodyUtilites.getCutText(), true));
            } else {
                values.put(BodyColumns.HTML_REPLY, mHtmlReply);
            }
            if (mTextReply != null && mTextReply.length() > MESSAGE_BODY_MAX_SIZE) {
                values.put(BodyColumns.TEXT_REPLY, EmailContentUtils.cutText(mTextReply, 
                        MESSAGE_BODY_PART_SIZE_FOR_DB, BodyUtilites.getCutText(), false));
            } else {
                values.put(BodyColumns.TEXT_REPLY, mTextReply);
            }
            if (mIntroText != null && mIntroText.length() > MESSAGE_BODY_MAX_SIZE) {
                values.put(BodyColumns.INTRO_TEXT, EmailContentUtils.cutText(mIntroText, 
                        MESSAGE_BODY_PART_SIZE_FOR_DB, BodyUtilites.getCutText(), false));
            } else {
                values.put(BodyColumns.INTRO_TEXT, mIntroText);
            }
            values.put(BodyColumns.SOURCE_MESSAGE_KEY, mSourceKey);
            values.put(BodyColumns.FILE_SAVE_FLAGS, mFileSaveFlags);
            return values;
        }

        private void restoreBodyFromFiles(Context context) {
            long accountId = Account.getAccountIdForMessageId(context, mMessageKey);
            int oldFlags = mFileSaveFlags;
            
            if ((mFileSaveFlags & FLAGS_HTML_CONTENT_FILE) != 0) {
                String fileContent = BodyUtilites.readHtmlContentFromFile(context, accountId, mMessageKey);
                if (fileContent != null) {
                    mHtmlContent = fileContent;
                } else {
                    mFileSaveFlags &= ~FLAGS_HTML_CONTENT_FILE;
                }
            }
            
            if ((mFileSaveFlags & FLAGS_TEXT_CONTENT_FILE) != 0) {
                String fileContent = BodyUtilites.readTextContentFromFile(context, accountId, mMessageKey);
                if (fileContent != null) {
                    mTextContent = fileContent;
                } else {
                    // File with body was removed, clear flags
                    mFileSaveFlags &= ~FLAGS_TEXT_CONTENT_FILE;
                }
            }
            
            if ((mFileSaveFlags & FLAGS_TEXT_REPLY_FILE) != 0) {
                String fileContent = BodyUtilites.readTextReplyFromFile(context, accountId, mMessageKey);
                if (fileContent != null) {
                    mTextReply = fileContent;
                } else {
                    mFileSaveFlags &= ~FLAGS_TEXT_REPLY_FILE;
                }
            }
            
            if ((mFileSaveFlags & FLAGS_HTML_REPLY_FILE) != 0) {
                String fileContent = BodyUtilites.readHtmlReplyFromFile(context, accountId, mMessageKey);
                if (fileContent != null) {
                    mHtmlReply = fileContent;
                } else {
                    mFileSaveFlags &= ~FLAGS_HTML_REPLY_FILE;
                }
            }
            
            if ((mFileSaveFlags & FLAGS_INTRO_FILE) != 0) {
                String fileContent = BodyUtilites.readIntroFromFile(context, accountId, mMessageKey);
                if (fileContent != null) {
                    mIntroText = fileContent;
                } else {
                    mFileSaveFlags &= ~FLAGS_INTRO_FILE;
                }
            }
            
            if (oldFlags != mFileSaveFlags) {
                updateTruncatedFlags(context, accountId, mMessageKey, mFileSaveFlags);
            }
        }
        
        private static Body restoreBodyWithCursor(Context context, Cursor cursor) {
            try {
                if (cursor.moveToFirst()) {
                    Body body = getContent(cursor, Body.class);
                    if (body != null) {
                        body.restoreBodyFromFiles(context);
                    }
                    return body;
                } else {
                    return null;
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in restoreBodyWithCursor", e);
                return null;
            } finally {
                try {
                    cursor.close();
                } catch(Exception e) {}
            }
        }

        public static Body restoreBodyWithId(Context context, long id) {
            Uri u = ContentUris.withAppendedId(Body.CONTENT_URI, id);
            Cursor c = null;
            try {
                c = context.getContentResolver()
                        .query(u, Body.CONTENT_PROJECTION, null, null, null);
            } catch (Exception e) {
            }
            return restoreBodyWithCursor(context, c);
        }

        public static Body restoreBodyWithMessageId(Context context, long messageId) {
            Cursor c = null;
            try {
                c = context.getContentResolver().query(Body.CONTENT_URI, Body.CONTENT_PROJECTION,
                        Body.MESSAGE_KEY + "=?", new String[] {
                            Long.toString(messageId)
                        }, null);
            } catch (Exception e) {
            }
            if (c != null)
                return restoreBodyWithCursor(context, c);
            else
                return null;
        }

        /**
         * Returns the bodyId for the given messageId, or -1 if no body is
         * found.
         */
        public static long lookupBodyIdWithMessageId(Context context, long messageId) {
            return EmailContentUtils.getFirstRowLong(context, Body.CONTENT_URI, ID_PROJECTION,
                    Body.MESSAGE_KEY + "=?", new String[] {
                        Long.toString(messageId)
                    }, null, ID_PROJECTION_COLUMN, Long.valueOf(-1));
        }

        public void saveBodyToFilesIfNecessary(Context context) {
            Log.d(TAG, "saveBodyToFilesIfNecessary start : messageId = " + mMessageKey);
            if (context != null && !EmailContentUtils.isFullMessageBodyLoadDisabled()) {
                long accountId = Account.getAccountIdForMessageId(context, mMessageKey);
                if (mHtmlContent != null && mHtmlContent.length() > MESSAGE_BODY_MAX_SIZE) {
                    EmailLog.d(TAG, "Save HTML content");
                    BodyUtilites.writeHtmlContentToFile(context, accountId, mMessageKey, mHtmlContent);
                    mFileSaveFlags |= FLAGS_HTML_CONTENT_FILE;
                }
                
                if ((mHtmlContent == null || mHtmlContent.length() == 0) && mTextContent != null && mTextContent.length() > MESSAGE_BODY_MAX_SIZE) {
                    EmailLog.d(TAG, "Save text content");
                    BodyUtilites.writeTextContentToFile(context, accountId, mMessageKey, mTextContent);
                    mFileSaveFlags |= FLAGS_TEXT_CONTENT_FILE;
                }
                
                if (mTextReply != null && mTextReply.length() > MESSAGE_BODY_MAX_SIZE) {
                    BodyUtilites.writeTextReplyToFile(context, accountId, mMessageKey, mTextReply);
                    mFileSaveFlags |= FLAGS_TEXT_REPLY_FILE;
                }
                
                if (mHtmlReply != null && mHtmlReply.length() > MESSAGE_BODY_MAX_SIZE) {
                    BodyUtilites.writeHtmlReplyToFile(context, accountId, mMessageKey, mHtmlReply);
                    mFileSaveFlags |= FLAGS_HTML_REPLY_FILE;
                }
                
                if (mIntroText != null && mIntroText.length() > MESSAGE_BODY_MAX_SIZE) {
                    BodyUtilites.writeIntroToFile(context, accountId, mMessageKey, mIntroText);
                    mFileSaveFlags |= FLAGS_INTRO_FILE;
                }
            }
            EmailLog.d(TAG, "saveBodyToFilesIfNecessary end");
        }

        private static void saveBodyToFilesIfNecessary(Context context, long messageId, ContentValues cv) {
            if (context != null && cv != null && messageId > 0 && !EmailContentUtils.isFullMessageBodyLoadDisabled()) {
                long accountId = Account.getAccountIdForMessageId(context, messageId);
                int flags = restoreFileSaveFlags(context, messageId);
                
                String htmlText = null;
                if (cv.containsKey(HTML_CONTENT)) {
                    htmlText = cv.getAsString(HTML_CONTENT);
                    if (htmlText != null && htmlText.length() > MESSAGE_BODY_MAX_SIZE) {
                            BodyUtilites.writeHtmlContentToFile(context, accountId, messageId, htmlText);
                            flags |= FLAGS_HTML_CONTENT_FILE;
                            cv.put(HTML_CONTENT, EmailContentUtils.cutText(htmlText, 
                                    MESSAGE_BODY_PART_SIZE_FOR_DB, BodyUtilites.getCutText(), true));
                    }
                }
                
                if (cv.containsKey(TEXT_CONTENT)) {
                    String text = cv.getAsString(TEXT_CONTENT);
                    if ((htmlText == null || htmlText.length() == 0) && text != null && text.length() > MESSAGE_BODY_MAX_SIZE) {
                            BodyUtilites.writeTextContentToFile(context, accountId, messageId, text);
                            flags |= FLAGS_TEXT_CONTENT_FILE;
                            cv.put(TEXT_CONTENT, EmailContentUtils.cutText(text, 
                                    MESSAGE_BODY_PART_SIZE_FOR_DB, BodyUtilites.getCutText(), false));
                    }
                }
                
                if (cv.containsKey(TEXT_REPLY)) {
                    String text = cv.getAsString(TEXT_REPLY);
                    if (text != null && text.length() > MESSAGE_BODY_MAX_SIZE) {
                            BodyUtilites.writeTextReplyToFile(context, accountId, messageId, text);
                            flags |= FLAGS_TEXT_REPLY_FILE;
                            cv.put(TEXT_REPLY, EmailContentUtils.cutText(text, 
                                    MESSAGE_BODY_PART_SIZE_FOR_DB, BodyUtilites.getCutText(), false));
                    }
                }
                
                if (cv.containsKey(HTML_REPLY)) {
                    String text = cv.getAsString(HTML_REPLY);
                    if (text != null && text.length() > MESSAGE_BODY_MAX_SIZE) {
                            BodyUtilites.writeHtmlReplyToFile(context, accountId, messageId, text);
                            flags |= FLAGS_HTML_REPLY_FILE;
                            cv.put(HTML_REPLY, EmailContentUtils.cutText(text, 
                                    MESSAGE_BODY_PART_SIZE_FOR_DB, BodyUtilites.getCutText(), true));
                    }
                }
                
                if (cv.containsKey(INTRO_TEXT)) {
                    String text = cv.getAsString(INTRO_TEXT);
                    if (text != null && text.length() > MESSAGE_BODY_MAX_SIZE) {
                            BodyUtilites.writeIntroToFile(context, accountId, messageId, text);
                            flags |= FLAGS_INTRO_FILE;
                            cv.put(INTRO_TEXT, EmailContentUtils.cutText(text, 
                                    MESSAGE_BODY_PART_SIZE_FOR_DB, BodyUtilites.getCutText(), false));
                    }
                }
                
                if (flags > 0) {
                    cv.put(FILE_SAVE_FLAGS, flags);
                }
            }
        }
        
        /**
         * Updates the Body for a messageId with the given ContentValues. If the
         * message has no body, a new body is inserted for the message. Warning:
         * the argument "values" is modified by this method, setting
         * MESSAGE_KEY.
         */
        public static void updateBodyWithMessageId(Context context, long messageId,
                ContentValues values) {
            saveBodyToFilesIfNecessary(context, messageId, values);
            ContentResolver resolver = context.getContentResolver();
            long bodyId = lookupBodyIdWithMessageId(context, messageId);
            values.put(BodyColumns.MESSAGE_KEY, messageId);
            if (bodyId == -1) {
                resolver.insert(CONTENT_URI, values);
            } else {
                final Uri uri = ContentUris.withAppendedId(CONTENT_URI, bodyId);
                resolver.update(uri, values, null, null);
            }
        }

        public static long restoreBodySourceKey(Context context, long messageId) {
            return EmailContentUtils.getFirstRowLong(context, Body.CONTENT_URI, Body.PROJECTION_SOURCE_KEY,
                    Body.MESSAGE_KEY + "=?", new String[] {
                        Long.toString(messageId)
                    }, null, 0, Long.valueOf(0));
        }
        
        public static int restoreFileSaveFlags(Context context, long messageId) {
            return EmailContentUtils.getFirstRowInt(context, Body.CONTENT_URI, Body.PROJECTION_FILE_SAVE_FLAGS,
                    Body.MESSAGE_KEY + "=?", new String[] {
                        Long.toString(messageId)
                    }, null, 0, 0);
        }

        public static long[] getMessagesIdsWhere(Context context, String selection, 
                String[] selectionArgs) {
            long[] ids = null;
            Cursor c = null;
            try {
                c = context.getContentResolver().query(Body.CONTENT_URI,
                        new String[]{Body.MESSAGE_KEY}, selection, selectionArgs, null);

                if (c != null) {
                    int count = c.getCount();
                    ids = new long[count];
                    for (int i = 0; i < count; ++i) {
                        c.moveToNext();
                        ids[i] = c.getLong(CONTENT_ID_COLUMN);
                    }
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in restoreMessagesWhere", e);
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
            
            return ids;
        }
        private static String restoreTextWithMessageId(Context context, long messageId,
                String[] projection) {
            Cursor c = null;
            try {
                c = context.getContentResolver().query(Body.CONTENT_URI, projection,
                        Body.MESSAGE_KEY + "=?", new String[] {
                            Long.toString(messageId)
                        }, null);
                if (c != null && c.moveToFirst()) {
                    return c.getString(COMMON_PROJECTION_COLUMN_TEXT);
                } else {
                    return null;
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in restoreTextWithMessageId", e);
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        }

        public static String restoreBodyTextWithMessageId(Context context, long messageId) {
            String resultContent = null;
            int flags = restoreFileSaveFlags(context, messageId);
            if ((flags & FLAGS_TEXT_CONTENT_FILE) != 0) {
                long accountId = Account.getAccountIdForMessageId(context, messageId);
                resultContent = BodyUtilites.readTextContentFromFile(context, accountId, messageId);
                
                if (resultContent == null) {
                    EmailLog.d(TAG, "restoreBodyTextWithMessageId : content is null, mark message as truncated");
                    flags &= ~FLAGS_TEXT_CONTENT_FILE;
                    updateTruncatedFlags(context, accountId, messageId, flags);
                }
            }
            
            if (resultContent == null) {
                resultContent = restoreTextWithMessageId(context, messageId, Body.COMMON_PROJECTION_TEXT);
            }
            return resultContent;
        }

        public static String restoreBodyHtmlWithMessageId(Context context, long messageId) {
            String resultContent = null;
            int flags = restoreFileSaveFlags(context, messageId);
            if ((flags & FLAGS_HTML_CONTENT_FILE) != 0) {
                long accountId = Account.getAccountIdForMessageId(context, messageId);
                resultContent = BodyUtilites.readHtmlContentFromFile(context, accountId, messageId);
                
                if (resultContent == null) {
                    EmailLog.d(TAG, "restoreBodyHtmlWithMessageId : content is null, mark message as truncated");
                    flags &= ~FLAGS_HTML_CONTENT_FILE;
                    updateTruncatedFlags(context, accountId, messageId, flags);
                }
            }
            
            if (resultContent == null) {
                resultContent = restoreTextWithMessageId(context, messageId, Body.COMMON_PROJECTION_HTML);
            }
            return resultContent;
        }

        public static String restoreReplyTextWithMessageId(Context context, long messageId) {
            String resultContent = null;
            int flags = restoreFileSaveFlags(context, messageId);
            if ((flags & FLAGS_TEXT_REPLY_FILE) != 0) {
                long accountId = Account.getAccountIdForMessageId(context, messageId);
                resultContent = BodyUtilites.readTextReplyFromFile(context, accountId, messageId);
                
                if (resultContent == null) {
                    EmailLog.d(TAG, "restoreReplyTextWithMessageId : content is null, mark message as truncated");
                    flags &= ~FLAGS_TEXT_REPLY_FILE;
                    updateTruncatedFlags(context, accountId, messageId, flags);
                }
            }
            
            if (resultContent == null) {
                resultContent = restoreTextWithMessageId(context, messageId, Body.COMMON_PROJECTION_REPLY_TEXT); 
            }
            return resultContent;
        }

        public static String restoreReplyHtmlWithMessageId(Context context, long messageId) {
            String resultContent = null;
            int flags = restoreFileSaveFlags(context, messageId);
            if ((flags & FLAGS_HTML_REPLY_FILE) != 0) {
                long accountId = Account.getAccountIdForMessageId(context, messageId);
                resultContent = BodyUtilites.readHtmlReplyFromFile(context, accountId, messageId);
                
                if (resultContent == null) {
                    EmailLog.d(TAG, "restoreReplyHtmlWithMessageId : content is null, mark message as truncated");
                    flags &= ~FLAGS_HTML_REPLY_FILE;
                    updateTruncatedFlags(context, accountId, messageId, flags);
                }
            }
            
            if (resultContent == null) {
                resultContent = restoreTextWithMessageId(context, messageId, Body.COMMON_PROJECTION_REPLY_HTML);
            }
            return resultContent;
        }

        public static String restoreIntroTextWithMessageId(Context context, long messageId) {
            String resultContent = null;
            int flags = restoreFileSaveFlags(context, messageId);
            if ((flags & FLAGS_INTRO_FILE) != 0) {
                long accountId = Account.getAccountIdForMessageId(context, messageId);
                resultContent = BodyUtilites.readIntroFromFile(context, accountId, messageId);
                
                if (resultContent == null) {
                    EmailLog.d(TAG, "restoreIntroTextWithMessageIdd : content is null, mark message as truncated");
                    flags &= ~FLAGS_INTRO_FILE;
                    updateTruncatedFlags(context, accountId, messageId, flags);
                }
            }
            
            if (resultContent == null) {
                resultContent = restoreTextWithMessageId(context, messageId, Body.COMMON_PROJECTION_INTRO);
            }
            return resultContent;
        }

        @Override
        public void restore(Cursor cursor) {
            mBaseUri = EmailContent.Body.CONTENT_URI;
            mMessageKey = cursor.getLong(CONTENT_MESSAGE_KEY_COLUMN);
            mHtmlContent = cursor.getString(CONTENT_HTML_CONTENT_COLUMN);
            mTextContent = cursor.getString(CONTENT_TEXT_CONTENT_COLUMN);
            mHtmlReply = cursor.getString(CONTENT_HTML_REPLY_COLUMN);
            mTextReply = cursor.getString(CONTENT_TEXT_REPLY_COLUMN);
            mSourceKey = cursor.getLong(CONTENT_SOURCE_KEY_COLUMN);
            mIntroText = cursor.getString(CONTENT_INTRO_TEXT_COLUMN);
            mFileSaveFlags = cursor.getInt(CONTENT_FILE_SAVE_FLAGS_COLUMN);
        }
        
        private static void updateTruncatedFlags(Context context, long accountId, long messageId, int flags) {
            EmailLog.d(TAG, "updateTruncatedFlags start accountId = " + accountId + " messageId = " + messageId + " flags = " + flags);
            if (EmailContentUtils.isFullMessageBodyLoadDisabled()) {
                Log.d(TAG, "updateTruncatedFlags: Saving message body to file is not supported inside container. Skip it.");
                return;
            }
            ContentValues cv = new ContentValues();
            cv.put(BodyColumns.FILE_SAVE_FLAGS, flags);
            context.getContentResolver().update(CONTENT_URI, cv, BodyColumns.MESSAGE_KEY + " = " + Long.toString(messageId), null);
            
            // If message body files were removed mark message as not fully loaded
            Account acnt = Account.restoreAccountWithId(context, accountId);
            cv = new ContentValues();
            if (acnt != null) {
                if (acnt.isEasAccount(context)) {
                    EmailLog.d(TAG, "updateTruncatedFlags : set ISTRUNCATED to YES");
                    cv.put(MessageColumns.ISTRUNCATED, Message.FLAG_TRUNCATED_YES);
                } else {
                    EmailLog.d(TAG, "updateTruncatedFlags : set FLAG_LOADED to PARTIAL");
                    cv.put(MessageColumns.FLAG_LOADED, Message.FLAG_LOADED_PARTIAL);
                }
            } else {
                EmailLog.d(TAG, "updateTruncatedFlags : cannot restore account, set both flags");
                // If we cannot restore account and check its type, update both flags
                cv.put(MessageColumns.ISTRUNCATED, Message.FLAG_TRUNCATED_YES);
                cv.put(MessageColumns.FLAG_LOADED, Message.FLAG_LOADED_PARTIAL);
            }
            context.getContentResolver().update(ContentUris.withAppendedId(Message.CONTENT_URI, messageId), cv, null, null);
            EmailLog.d(TAG, "updateTruncatedFlags finish");
        }

        public static boolean bodyFilesRemoved(Context context, long accountId, long messageId) {
            if (EmailContentUtils.isFullMessageBodyLoadDisabled()) {
                Log.d(TAG, "bodyFilesRemoved: no body files insde container");
                return false;
            }
            
            if (Utility.isSdpActive()) {
                Log.d(TAG, "bodyFilesRemoved: inside locked container with SDP enabled " +
                        "files are not available. Skip this check.");
                return false;
            }
            
            boolean result = false;
            int flags = restoreFileSaveFlags(context, messageId);
            int oldFlags = flags;
            
            if (flags > 0) {
                if ((flags & FLAGS_HTML_CONTENT_FILE) != 0 && 
                    !BodyUtilites.isHtmlContentFileExists(context, accountId, messageId)) {
                    flags &= ~FLAGS_HTML_CONTENT_FILE; 
                    result = true;
                }
                
                if ((flags & FLAGS_TEXT_CONTENT_FILE) != 0 && 
                    !BodyUtilites.isTextContentFileExists(context, accountId, messageId)) {
                    flags &= ~FLAGS_TEXT_CONTENT_FILE;
                    result = true;
                }
                
                if ((flags & FLAGS_HTML_REPLY_FILE) != 0 && 
                    !BodyUtilites.isHtmlReplyFileExists(context, accountId, messageId)) {
                    flags &= ~FLAGS_HTML_REPLY_FILE;
                    result = true;
                }
                
                if ((flags & FLAGS_TEXT_REPLY_FILE) != 0 && 
                    !BodyUtilites.isTextReplyFileExists(context, accountId, messageId)) {
                    flags &= ~FLAGS_TEXT_REPLY_FILE;
                    result = true;
                }
                
                if ((flags & FLAGS_INTRO_FILE) != 0 && 
                    !BodyUtilites.isIntroFileExists(context, accountId, messageId)) {
                    flags &= ~FLAGS_INTRO_FILE;
                    result = true;
                }
            }
            
            if (flags != oldFlags) {
                EmailLog.d(TAG, "Body.bodyFilesRemoved(): some body files were removed. Update flags in DB");
                updateTruncatedFlags(context, accountId, messageId, flags);
            }
                
            return result;
        }
        
        public boolean update() {
            return false;
        }
    }

    public interface MessageColumns {
        public static final String ID = "_id";

        // Basic columns used in message list presentation
        // The name as shown to the user in a message list
        public static final String DISPLAY_NAME = "displayName";

        // The time (millis) as shown to the user in a message list [INDEX]
        public static final String TIMESTAMP = "timeStamp";

        // Message subject
        public static final String SUBJECT = "subject";

        // Boolean, unread = 0, read = 1 [INDEX]
		 //jitha.mj - If updating FLAG_READ, please update EmailConstants.java in Email SDK for the same variable.
        public static final String FLAG_READ = "flagRead";

        // Load state, see constants below (unloaded, partial, complete,
        // deleted)
        public static final String FLAG_LOADED = "flagLoaded";

        // Boolean, unflagged = 0, flagged (favorite) = 1
		//jitha.mj - If updating FLAG_FAVORITE, please update EmailConstants.java in Email SDK for the same variable.
        public static final String FLAG_FAVORITE = "flagFavorite";

        // Boolean, no attachment = 0, attachment = 1
        public static final String FLAG_ATTACHMENT = "flagAttachment";

        public static final String FLAG_REPLY = "flagReply";

        public static final String ORIGINAL_ID = "originalId";

        // Bit field for flags which we'll not be selecting on
        public static final String FLAGS = "flags";

        // Sync related identifiers
        // Any client-required identifier
        public static final String CLIENT_ID = "clientId";

        // The message-id in the message's header
        public static final String MESSAGE_ID = "messageId";

        // References to other Email objects in the database
        // Foreign key to the Mailbox holding this message [INDEX]
        public static final String MAILBOX_KEY = "mailboxKey";

        // Foreign key to the Account holding this message
        public static final String ACCOUNT_KEY = "accountKey";

        // Address lists, packed with Address.pack()
        public static final String FROM_LIST = "fromList";

        public static final String TO_LIST = "toList";

        public static final String CC_LIST = "ccList";

        public static final String BCC_LIST = "bccList";

        public static final String REPLY_TO_LIST = "replyToList";

        // Meeting invitation related information (for now, start time in ms)
        public static final String MEETING_INFO = "meetingInfo";

        public static final String THREAD_ID = "threadId"; // suhyb 0729, for
        // thread mail....

        //This column is also used to store the SMTP failure reason phrase for Legacy protocols.
        public static final String THREAD_NAME = "threadName"; // suhyb 0729,
        // for thread
        // mail....

        // change@wtl.subbu importance
        public static final String IMPORTANCE = "importance";

        public static final String ISTRUNCATED = "istruncated";

        public static final String FLAG_MOVED = "flagMoved";

        public static final String DST_MAILBOX_KEY = "dstMailboxKey";
		
		 //jitha.mj - If updating FLAGSTATUS, please update EmailConstants.java in Email SDK for the same variable.
        public static final String FLAGSTATUS = "flagStatus";

        // change@siso_hq for Load More with Inline Images - start
        public static final String ISMIMELOADED = "isMimeLoaded";

        // change@siso_hq for Load More with Inline Images - end
        // change@wtl.akulik smime support start
        public static final String SMIME_FLAGS = "smimeFlags";

        public static final String ENCRYPTION_ALGORITHM = "encryptionAlgorithm";

        // change@wtl.akulik smime support end
        // change@siso.vpdj Conversation View start
        public static final String CONVERSATION_ID = "conversationId";

        public static final String CONVERSATION_INDEX = "conversationIndex";

        // change@wtl.rprominski voicemail support start
        public static final String UM_CALLER_ID = "umCallerId";

        public static final String UM_USER_NOTES = "umUserNotes";

        // change@wtl.rprominski voicemail suppoer end
        // change@wtl.jrabina EAS 14.0 LastVerbExecution start
        public static final String LAST_VERB = "lastVerb";

        public static final String LAST_VERB_TIME = "lastVerbTime";

        // change@wtl.jrabina EAS 14.0 LastVerbExecution end
        // change@wtl.kSingh - SMS Sync - starts
        public static final String MESSAGE_TYPE = "messageType";

        public static final String MESSAGE_DIRTY = "messageDirty";

        // change@wtl.kSingh - SMS Sync - ends
        public static final String ACCOUNT_SCHEMA = "accountSchema";

        public static final String MAILBOX_TYPE = "mailboxType";

        // A text "snippet" derived from the body of the message
        public static final String SNIPPET = "snippet";

        // change@siso.Spoorti - IRM - starts
        public static final String IRM_TEMPLATE_ID = "IRMTemplateId";

        public static final String IRM_LICENSE_FLAG = "IRMLicenseFlag";

        public static final String IRM_OWNER = "IRMOwner";

        public static final String IRM_CONTENT_EXPIRY_DATE = "IRMContentExpiryDate";

        public static final String IRM_CONTENT_OWNER = "IRMContentOwner";

        public static final String IRM_REMOVAL_FLAG = "IRMRemovalFlag";

        public static final String IRM_TEMPLATE_DESCRIPTION = "IRMTemplateDescription";

        public static final String IRM_TEMPLATE_NAME = "IRMTemplateName";

        public static final String IRM_TEMPLATE_DECSRIPTION = "IRMTemplateDescription";
        // change@siso.Spoorti - IRM - ends
        // huijae.lee
        // This is for delete, read and unread flag that do not synchronize to
        // server.
        public static final String EAS_LOCAL_DELETE_FLAG = "EasLocalDeleteFlag";
        public static final String EAS_LOCAL_READ_FLAG = "EasLocalReadFlag";

        public static final String RETRY_SEND_TIMES = "retrySendTimes";

        public static final String KEY_IDS = "keyIds";  
        // change@siso.gaurav email sort on size start 
        public static final String SIZE = "size";
        //change@siso.gaurav email sort on size end 
        //change@siso madhu.dumpa check message as dirtcommit or not
        public static final String DIRTY_COMMIT = "dirtyCommit";
        public static final String OPEN_TIME = "openTime";
        
        // Boolean, unflagged = 0, flagged (delete hidden) = 1
        public static final String FLAG_DELETEHIDDEN = "flagDeleteHidden";
        
        public static final String PROTOCOL = "protocol";
    }

    public static final class Message extends EmailContent implements SyncColumns, MessageColumns {
        public static final String TABLE_NAME = "Message";

        public static final String UPDATED_TABLE_NAME = "Message_Updates";

        public static final String DELETED_TABLE_NAME = "Message_Deletes";

        // To refer to a specific message, use
        // ContentUris.withAppendedId(CONTENT_URI, id)
        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI + "/message");
        public static final Uri CONTENT_URI_MULTI = Uri.parse(EmailContent.CONTENT_URI_MULTI + "/message");
        public static final Uri CONTENT_URI_MULTI_SEARCH_ALL = Uri.parse(EmailContent.CONTENT_URI_MULTI + "/message_all_search");
        public static final Uri CONTENT_URI_CONV_MULTI = Uri.parse(EmailContent.CONTENT_URI_MULTI + "/message_thread");

        public static final Uri CONTENT_URI_LIMIT_1 = uriWithLimit(CONTENT_URI, 1);
        public static final Uri CONTENT_URI_LIMIT_2 = uriWithLimit(CONTENT_URI, 2);

        public static final Uri SYNCED_CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI
                + "/syncedMessage");
        
        public static final Uri SYNCED_CONTENT_URI_MULTI = Uri.parse(EmailContent.CONTENT_URI_MULTI
                + "/syncedMessage");

        public static final Uri UPDATEDELETE_SYNCED_CONTENT_URI = Uri
                .parse(EmailContent.CONTENT_URI + "/syncedMessageUpdateDelete");

        public static final Uri UPDATE_RECENT_HISTORY_CONTENT_URI = Uri
                .parse(EmailContent.CONTENT_URI + "/updateRecentHistory");

        public static final Uri MOVE_ITEM_CONTENT_URI = Uri
                .parse(EmailContent.CONTENT_URI + "/moveItem");

        public static final Uri DELETED_CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI
                + "/deletedMessage");

        public static final Uri UPDATED_CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI
                + "/updatedMessage");
        
        public static final Uri UPDATED_CONTENT_URI_MULTI = Uri.parse(EmailContent.CONTENT_URI_MULTI
                + "/updatedMessage");
        
        public static final Uri DELETED_CONTENT_URI_MULTI = Uri.parse(EmailContent.CONTENT_URI_MULTI
                + "/deletedMessage");

        public static final Uri NOTIFIER_URI = Uri.parse(EmailContent.CONTENT_NOTIFIER_URI
                + "/message");

        public static final Uri NOTIFIER_URI_FOR_MESSAGELIST = Uri.parse(EmailContent.CONTENT_NOTIFIER_URI
                + "/formessagelist");
        
        public static final Uri NOTIFIER_URI_OUTBOX = Uri.parse(EmailContent.CONTENT_NOTIFIER_URI
                + "/outbox");
        
        public static final String KEY_TIMESTAMP_DESC = MessageColumns.TIMESTAMP + " desc";

        public static final int CONTENT_ID_COLUMN = 0;
        public static final int CONTENT_DISPLAY_NAME_COLUMN = 1;
        public static final int CONTENT_TIMESTAMP_COLUMN = 2;
        public static final int CONTENT_SUBJECT_COLUMN = 3;
        public static final int CONTENT_FLAG_READ_COLUMN = 4;
        public static final int CONTENT_FLAG_LOADED_COLUMN = 5;
        public static final int CONTENT_FLAG_FAVORITE_COLUMN = 6;
        public static final int CONTENT_FLAG_ATTACHMENT_COLUMN = 7;
        public static final int CONTENT_FLAGS_COLUMN = 8;
        public static final int CONTENT_SERVER_ID_COLUMN = 9;
        
        public static final int CONTENT_CLIENT_ID_COLUMN = 10;
        public static final int CONTENT_MESSAGE_ID_COLUMN = 11;
        public static final int CONTENT_MAILBOX_KEY_COLUMN = 12;
        public static final int CONTENT_ACCOUNT_KEY_COLUMN = 13;
        public static final int CONTENT_FROM_LIST_COLUMN = 14;
        public static final int CONTENT_TO_LIST_COLUMN = 15;
        public static final int CONTENT_CC_LIST_COLUMN = 16;
        public static final int CONTENT_BCC_LIST_COLUMN = 17;
        public static final int CONTENT_REPLY_TO_COLUMN = 18;
        public static final int CONTENT_SERVER_TIMESTAMP_COLUMN = 19;

        public static final int CONTENT_MEETING_INFO_COLUMN = 20;
        public static final int CONTENT_THREAD_ID_COLUMN = 21; 
        public static final int CONTENT_THREAD_NAME_COLUMN = 22; 
        public static final int CONTENT_IMPORTANCE_COLUMN = 23;
        public static final int CONTENT_ISTRUNCATED_COLUMN = 24;
        public static final int CONTENT_FLAG_MOVED_COLUMN = 25;
        public static final int CONTENT_DST_MAILBOX_KEY_COLUMN = 26;
        public static final int CONTENT_FLAG_STATUS_COLUMN = 27;
        public static final int CONTENT_ISMIMELOADED_COLUMN = 28;
        public static final int CONTENT_SMIME_FLAGS = 29;
        
        public static final int CONTENT_ENCRYPTION_ALGORITHM = 30;
        public static final int CONTENT_CONVERSATION_ID = 31;
        public static final int CONTENT_CONVERSATION_INDEX = 32;
        public static final int CONTENT_UM_CALLER_ID = 33;
        public static final int CONTENT_UM_USER_NOTES = 34;
        public static final int CONTENT_LAST_VERB_COLUMN = 35;
        public static final int CONTENT_LAST_VERB_TIME_COLUMN = 36;
        public static final int CONTENT_MESSAGE_TYPE = 37;
        public static final int CONTENT_MESSAGE_DIRTY = 38;
        public static final int CONTENT_ACCOUNT_SCHEMA_COLUMN = 39;
        public static final int CONTENT_MAILBOX_TYPE_COLUMN = 40;
        public static final int CONTENT_SNIPPET_COLUMN = 41;
        public static final int CONTENT_FLAG_REPLY = 42;
        public static final int CONTENT_ORIGINAL_ID = 43;
        public static final int CONTENT_IRM_TEMPLATE_ID = 44;
        public static final int CONTENT_IRM_CONTENT_EXPIRY_DATE = 45;
        public static final int CONTENT_IRM_CONTENT_OWNER = 46;
        public static final int CONTENT_IRM_LICENSE_FLAG = 47;
        public static final int CONTENT_IRM_OWNER = 48;

        public static final int CONTENT_IRM_REMOVAL_FLAG = 49;
        public static final int CONTENT_IRM_TEMPLATE_NAME = 50;
        public static final int CONTENT_IRM_TEMPLATE_DESCRIPTION = 51;
        public static final int CONTENT_EAS_LOCAL_DELETE_FLAG = 52;
        public static final int CONTENT_EAS_LOCAL_READ_FLAG = 53;
        public static final int CONTENT_RETRY_SEND_TIMES = 54;
        public static final int CONTENT_KEY_IDS_COLUMN = 55;        
        public static final int CONTENT_SIZE_COLUMN = 56;
        public static final int CONTENT_DIRTY_COMMIT_COLUMN = 57;
        public static final int CONTENT_OPEN_TIME_COLUMN = 58;
        
        public static final int CONTENT_FLAG_DELETEHIDDEN = 59;
        
        public static final int CONTENT_LAST_ORDINARY_INDEX = CONTENT_FLAG_DELETEHIDDEN;
        public static final int CONTENT_SEVEN_BASE_INDEX = CONTENT_LAST_ORDINARY_INDEX + 1;
        
        public static final int CONTENT_TYPE_MESSAGE_COLUMN = CONTENT_SEVEN_BASE_INDEX;
        public static final int CONTENT_SEVEN_MESSAGE_KEY_COLUMN = CONTENT_TYPE_MESSAGE_COLUMN + 1;
        public static final int CONTENT_MISSING_BODY_COLUMN = CONTENT_SEVEN_MESSAGE_KEY_COLUMN + 1;
        public static final int CONTENT_MISSING_HTML_BODY_COLUMN = CONTENT_MISSING_BODY_COLUMN + 1;
        public static final int CONTENT_UNK_ENCODING_COLUMN = CONTENT_MISSING_HTML_BODY_COLUMN + 1;
        public static final int CONTENT_SEVEN_ACCOUNT_KEY_COLUMN = CONTENT_UNK_ENCODING_COLUMN + 1;
        
        public static final String[] CONTENT_PROJECTION = new String[] {
            RECORD_ID,
            MessageColumns.DISPLAY_NAME,
            MessageColumns.TIMESTAMP,
            MessageColumns.SUBJECT,
            MessageColumns.FLAG_READ,
            MessageColumns.FLAG_LOADED,
            MessageColumns.FLAG_FAVORITE,
            MessageColumns.FLAG_ATTACHMENT,
            MessageColumns.FLAGS,
            SyncColumns.SERVER_ID,

            MessageColumns.CLIENT_ID, //10
            MessageColumns.MESSAGE_ID,
            MessageColumns.MAILBOX_KEY,
            MessageColumns.ACCOUNT_KEY,
            MessageColumns.FROM_LIST,
            MessageColumns.TO_LIST,
            MessageColumns.CC_LIST,
            MessageColumns.BCC_LIST,
            MessageColumns.REPLY_TO_LIST,
            SyncColumns.SERVER_TIMESTAMP,

            MessageColumns.MEETING_INFO, //20
            MessageColumns.THREAD_ID,
            MessageColumns.THREAD_NAME,
            MessageColumns.IMPORTANCE,
            MessageColumns.ISTRUNCATED,
            MessageColumns.FLAG_MOVED,
            MessageColumns.DST_MAILBOX_KEY,
            MessageColumns.FLAGSTATUS,
            MessageColumns.ISMIMELOADED,
            MessageColumns.SMIME_FLAGS,

            MessageColumns.ENCRYPTION_ALGORITHM, //30
            MessageColumns.CONVERSATION_ID,
            MessageColumns.CONVERSATION_INDEX,
            MessageColumns.UM_CALLER_ID,
            MessageColumns.UM_USER_NOTES,
            MessageColumns.LAST_VERB,
            MessageColumns.LAST_VERB_TIME,
            MessageColumns.MESSAGE_TYPE,
            MessageColumns.MESSAGE_DIRTY,
            MessageColumns.ACCOUNT_SCHEMA, 
            
            MessageColumns.MAILBOX_TYPE, // 40
            MessageColumns.SNIPPET,
            MessageColumns.FLAG_REPLY,
            MessageColumns.ORIGINAL_ID,
            MessageColumns.IRM_TEMPLATE_ID, 
            MessageColumns.IRM_CONTENT_EXPIRY_DATE,
            MessageColumns.IRM_CONTENT_OWNER, 
            MessageColumns.IRM_LICENSE_FLAG,
            MessageColumns.IRM_OWNER,
            MessageColumns.IRM_REMOVAL_FLAG, 
            MessageColumns.IRM_TEMPLATE_NAME,
            //50
            MessageColumns.IRM_TEMPLATE_DECSRIPTION,
            MessageColumns.EAS_LOCAL_DELETE_FLAG,
            MessageColumns.EAS_LOCAL_READ_FLAG,
            MessageColumns.RETRY_SEND_TIMES,
            MessageColumns.KEY_IDS,
            MessageColumns.SIZE,
            MessageColumns.DIRTY_COMMIT,
            MessageColumns.OPEN_TIME,
            MessageColumns.FLAG_DELETEHIDDEN, 
            //  60

        };

        public static final String[] CONTENT_PROJECTION_UP_DEL = new String[] {
            RECORD_ID,
            MessageColumns.DISPLAY_NAME,
            MessageColumns.TIMESTAMP,
            MessageColumns.SUBJECT,
            MessageColumns.FLAG_READ,
            MessageColumns.FLAG_LOADED,
            MessageColumns.FLAG_FAVORITE,
            MessageColumns.FLAG_ATTACHMENT,
            MessageColumns.FLAGS,
            SyncColumns.SERVER_ID,

            MessageColumns.CLIENT_ID, //10
            MessageColumns.MESSAGE_ID,
            MessageColumns.MAILBOX_KEY,
            MessageColumns.ACCOUNT_KEY,
            MessageColumns.FROM_LIST,
            MessageColumns.TO_LIST,
            MessageColumns.CC_LIST,
            MessageColumns.BCC_LIST,
            MessageColumns.REPLY_TO_LIST,
            SyncColumns.SERVER_TIMESTAMP,

            MessageColumns.MEETING_INFO, //20
            MessageColumns.THREAD_ID,
            MessageColumns.THREAD_NAME,
            MessageColumns.IMPORTANCE,
            MessageColumns.ISTRUNCATED,
            MessageColumns.FLAG_MOVED,
            MessageColumns.DST_MAILBOX_KEY,
            MessageColumns.FLAGSTATUS,
            MessageColumns.ISMIMELOADED,
            MessageColumns.SMIME_FLAGS,

            MessageColumns.ENCRYPTION_ALGORITHM, //30
            MessageColumns.CONVERSATION_ID,
            MessageColumns.CONVERSATION_INDEX,
            MessageColumns.UM_CALLER_ID,
            MessageColumns.UM_USER_NOTES,
            MessageColumns.LAST_VERB,
            MessageColumns.LAST_VERB_TIME,
            MessageColumns.MESSAGE_TYPE,
            MessageColumns.MESSAGE_DIRTY,
            MessageColumns.ACCOUNT_SCHEMA, 
            
            //40
            MessageColumns.MAILBOX_TYPE,
            MessageColumns.SNIPPET,
            MessageColumns.FLAG_REPLY,
            MessageColumns.ORIGINAL_ID,
            MessageColumns.IRM_TEMPLATE_ID, 
            MessageColumns.IRM_CONTENT_EXPIRY_DATE,
            MessageColumns.IRM_CONTENT_OWNER, 
            MessageColumns.IRM_LICENSE_FLAG,
            MessageColumns.IRM_OWNER,
            MessageColumns.IRM_REMOVAL_FLAG, 
            //50
            MessageColumns.IRM_TEMPLATE_NAME, 
            MessageColumns.IRM_TEMPLATE_DECSRIPTION,
            MessageColumns.EAS_LOCAL_DELETE_FLAG, 
            MessageColumns.EAS_LOCAL_READ_FLAG,
            MessageColumns.RETRY_SEND_TIMES,
            MessageColumns.KEY_IDS,
            MessageColumns.SIZE,
            MessageColumns.DIRTY_COMMIT,
            MessageColumns.OPEN_TIME,
            MessageColumns.FLAG_DELETEHIDDEN  
        };

        public static final int LIST_ID_COLUMN = 0;
        public static final int LIST_DISPLAY_NAME_COLUMN = 1;
        public static final int LIST_TIMESTAMP_COLUMN = 2;
        public static final int LIST_SUBJECT_COLUMN = 3;
        public static final int LIST_READ_COLUMN = 4;
        public static final int LIST_LOADED_COLUMN = 5;
        public static final int LIST_FAVORITE_COLUMN = 6;
        public static final int LIST_ATTACHMENT_COLUMN = 7;
        public static final int LIST_FLAGS_COLUMN = 8;
        public static final int LIST_MAILBOX_KEY_COLUMN = 9;

        public static final int LIST_ACCOUNT_KEY_COLUMN = 10;
        public static final int LIST_SERVER_ID_COLUMN = 11;
        public static final int LIST_IMPORTANCE_COLUMN = 12;
        public static final int LIST_ISTRUNCATED_COLUMN = 13;
        public static final int LIST_FLAG_MOVED_COLUMN = 14;
        public static final int LIST_DST_MAILBOX_KEY_COLUMN = 15;
        public static final int LIST_FLAG_STATUS_COLUMN = 16;
        public static final int LIST_ISMIMELOADED_COLUMN = 17;
        public static final int LIST_LAST_VERB = 18;

        public static final int LIST_LAST_VERB_TIME = 19;
        public static final int LIST_SNIPPET_COLUMN = 20;
        public static final int LIST_SIZE_COLUMN = 21;



        // Public projection for common list columns
        public static final String[] LIST_PROJECTION = new String[] {
            RECORD_ID, 
            MessageColumns.DISPLAY_NAME, 
            MessageColumns.TIMESTAMP,
            MessageColumns.SUBJECT, 
            MessageColumns.FLAG_READ, 
            MessageColumns.FLAG_LOADED,
            MessageColumns.FLAG_FAVORITE, 
            MessageColumns.FLAG_ATTACHMENT, 
            MessageColumns.FLAGS,
            MessageColumns.MAILBOX_KEY, 

            MessageColumns.ACCOUNT_KEY, //10 
            SyncColumns.SERVER_ID,
            MessageColumns.IMPORTANCE, 
            MessageColumns.ISTRUNCATED, 
            MessageColumns.FLAG_MOVED,
            MessageColumns.DST_MAILBOX_KEY, 
            MessageColumns.FLAGSTATUS,
            MessageColumns.ISMIMELOADED,
            MessageColumns.LAST_VERB, 
            MessageColumns.LAST_VERB_TIME, 
            //20
            MessageColumns.SNIPPET,
            MessageColumns.SIZE,
        };

        // change@siso.kanchan 14.1 IRM feature starts.
        public static final int MSG_CLASS_IRM_REPLY_ALLOWED = 1;

        public static final int MSG_CLASS_IRM_FORWARD_ALLOWED = 2;

        public static final int MSG_CLASS_IRM_REPLY_ALL_ALLOWED = 4;

        public static final int MSG_CLASS_IRM_EDIT_ALLOWED = 8;

        public static final int MSG_CLASS_IRM_MODIFY_RECEPIENTS_ALLOWED = 16;

        public static final int MSG_CLASS_IRM_EXTRACT_ALLOWED = 32;

        public static final int MSG_CLASS_IRM_EXPORT_ALLOWED = 64;

        public static final int MSG_CLASS_IRM_PRINT_ALLOWED = 128;

        public static final int MSG_CLASS_IRM_PROGRAMATIC_ACCESS_ALLOWED = 256;

        // change@siso.kanchan 14.1 IRM feature ends.

        public static final int EAS_LOCAL_READ = 1;
        public static final int EAS_LOCAL_UNREAD = 2;
        
        public static final int MAXIUM_VISIBLE_LIMIT = 5000;
        
        public static final int MAXIUM_VISIBLE_LIMIT_PREV = 1000;
        
        public static final int MESSAGE_EMAIL = 0x0000;
        
        public static final int MESSAGE_IMAP_SERVER_SEARCH_RESULT = 0x0001;

        public static final int MESSAGE_SMS = 0x0100;

        public static final int MESSAGE_VOICE_MAIL = 0x0200;
        
        public static final int MESSAGE_CALENDAR = 0x0300;

//        public static final int CONTENT_MESSAGE_DIRTY = 39;

        public static final int MESSAGE_DIRTY_FLAG = 0x0001;
        // change@SISO "madhu.dumpa" message move fix start
        public static final int MESSAGE_FLAG_MOVED = 1;

        public static final int MESSAGE_FLAG_NOT_MOVED = 0;
        // change@SISO "madhu.dumpa" message move fix end
        public static final int MESSAGE_FLAG_DELETED = 2; 
        public static final int RECENTLY_NOREAD = 0;

        public static final int ID_COLUMNS_ID_COLUMN = 0;

        public static final int ID_COLUMNS_SYNC_SERVER_ID = 1;

        public static final int FLAG_CLEAR = 0;
        public static final int FLAG_COMPLETE = 1;
        public static final int FLAG_ACTIVIE = 2;

        public static final String[] ID_COLUMNS_PROJECTION = new String[] {
                RECORD_ID, SyncColumns.SERVER_ID
        };

        public static final String[] ID_SUBJECT_COLUMN_PROJECTION = new String[] {
                RECORD_ID, MessageColumns.ORIGINAL_ID, SyncColumns.SERVER_ID,
                MessageColumns.SUBJECT, MessageColumns.RETRY_SEND_TIMES
        };

        public static final String[] ID_CONVERSATION_COLUMN_PROJECTION = new String[] {
                RECORD_ID, MessageColumns.CONVERSATION_ID
        };

        public static final int ID_MAILBOX_COLUMN_ID = 0;

        public static final int ID_MAILBOX_COLUMN_MAILBOX_KEY = 1;

        public static final String[] ID_MAILBOX_PROJECTION = new String[] {
                RECORD_ID, MessageColumns.MAILBOX_KEY
        };

        // Reply Sync for ICS by EJ 111129 ; Add ORIGINAL_ID
        public static final String[] ID_COLUMN_PROJECTION = new String[] {
                RECORD_ID, MessageColumns.ORIGINAL_ID, SyncColumns.SERVER_ID
        };

        public static final String[] ID_MESSAGE_UPDATES_PROJECTION = new String[] {
            MessageColumns.ACCOUNT_KEY, MessageColumns.ID,
            MessageColumns.MAILBOX_KEY, MessageColumns.FLAGS,
            SyncColumns.SERVER_ID, SyncColumns.SERVER_TIMESTAMP,
            MessageColumns.FLAG_FAVORITE, MessageColumns.FLAG_READ,
            MessageColumns.FLAG_MOVED, MessageColumns.FLAG_REPLY,
            MessageColumns.LAST_VERB
        };

        private static final String ACCOUNT_KEY_SELECTION = MessageColumns.ACCOUNT_KEY + "=?";
        
        private static final String MAILBOX_KEY_SELECTION = MessageColumns.MAILBOX_KEY
                + "=?";

        /** Selection for messages that are loaded */
        public static final String FLAG_LOADED_SELECTION = MessageColumns.FLAG_LOADED
                + " IN ("
                + Message.FLAG_LOADED_PARTIAL
                + ","
                + Message.FLAG_LEGACY_LOADING
                + ","
                + Message.FLAG_LOADED_COMPLETE
                + ")";

        public static final String ALL_FAVORITE_SELECTION = MessageColumns.FLAG_FAVORITE
                + "=1 AND " + MessageColumns.MAILBOX_KEY + " NOT IN (" + "SELECT "
                + MailboxColumns.ID + " FROM " + Mailbox.TABLE_NAME + "" + " WHERE "
                + MailboxColumns.TYPE + " = " + Mailbox.TYPE_TRASH +
                " OR "  + MailboxColumns.TYPE + " = " + Mailbox.TYPE_JUNK +" )" + " AND "
                + FLAG_LOADED_SELECTION;
        
        
        
        public static final String ALL_STARRED_AND_FLAGGED_UNREAD_SELECTION = "(" + MessageColumns.FLAG_FAVORITE
                + "=1 OR " + MessageColumns.FLAGSTATUS + "=2) AND " + MessageColumns.MAILBOX_KEY + " NOT IN (" + "SELECT "
                + MailboxColumns.ID + " FROM " + Mailbox.TABLE_NAME + "" + " WHERE "
                + MailboxColumns.TYPE + " = " + Mailbox.TYPE_TRASH  +
                " OR " + MailboxColumns.TYPE + " = " + Mailbox.TYPE_JUNK + " )"  + " AND "
                + MessageColumns.FLAG_READ + "=0 AND "
                + FLAG_LOADED_SELECTION;


        
        public static final String ALL_FLAGGED_UNREAD_SELECTION = MessageColumns.FLAGSTATUS
                + "=2 AND " + MessageColumns.MAILBOX_KEY + " NOT IN (" + "SELECT "
                + MailboxColumns.ID + " FROM " + Mailbox.TABLE_NAME + "" + " WHERE "
                + MailboxColumns.TYPE + " = " + Mailbox.TYPE_TRASH  +
                " OR " + MailboxColumns.TYPE + " = " + Mailbox.TYPE_JUNK + " )"  + " AND "
                + MessageColumns.FLAG_READ + "=0 AND "
                + FLAG_LOADED_SELECTION;
        
        public static final String ALL_FLAGGED_SELECTION = MessageColumns.FLAGSTATUS
                + "=2 AND " + MessageColumns.MAILBOX_KEY + " NOT IN (" + "SELECT "
                + MailboxColumns.ID + " FROM " + Mailbox.TABLE_NAME + "" + " WHERE "
                + MailboxColumns.TYPE + " = " + Mailbox.TYPE_TRASH +
                " OR "  + MailboxColumns.TYPE + " = " + Mailbox.TYPE_JUNK + " )" + " AND "
                + FLAG_LOADED_SELECTION;
        
        public static final String ALL_FAVORITE_OR_FLAGED_SELECTION = "(" + MessageColumns.FLAG_FAVORITE
                + "=1 OR " + MessageColumns.FLAGSTATUS + "=2) AND " + MessageColumns.MAILBOX_KEY + " NOT IN (" + "SELECT "
                + MailboxColumns.ID + " FROM " + Mailbox.TABLE_NAME + "" + " WHERE "
                + MailboxColumns.TYPE + " = " + Mailbox.TYPE_TRASH +
                " OR "  + MailboxColumns.TYPE + " = " + Mailbox.TYPE_JUNK + " )"  + " AND "
                + FLAG_LOADED_SELECTION;
        
        /** Selection to retrieve all messages in "inbox" for any account */
        public static final String INBOX_SELECTION = MessageColumns.MAILBOX_KEY + " IN ("
                + "SELECT " + MailboxColumns.ID + " FROM " + Mailbox.TABLE_NAME + " WHERE "
                + MailboxColumns.TYPE + " = " + Mailbox.TYPE_INBOX + ")" + " AND "
                + FLAG_LOADED_SELECTION;
        
//        public static final String CONVERSATION_SELECTION = MessageColumns.MAILBOX_KEY + " IN ("
//                + "SELECT " + MailboxColumns.ID + " FROM " + Mailbox.TABLE_NAME + " WHERE "
//                + MailboxColumns.TYPE + " = " + Mailbox.TYPE_INBOX + " OR " + MailboxColumns.TYPE + " = " + Mailbox.TYPE_SENT + ")" + " AND "
//                + FLAG_LOADED_SELECTION;
        
        public static final String CONVERSATION_SELECTION = MessageColumns.MAILBOX_KEY + " IN ("
        		+ "SELECT " + MailboxColumns.ID + " FROM " + Mailbox.TABLE_NAME + " WHERE "
        		+ MailboxColumns.TYPE + " = " + Mailbox.TYPE_INBOX +  ")" + " AND "
        		+ FLAG_LOADED_SELECTION + " AND NOT "+ "( "+
        		MessageColumns.MAILBOX_KEY+" NOT IN "+"(SELECT "+MailboxColumns.ID+" FROM "+Mailbox.TABLE_NAME+" WHERE ("+MailboxColumns.TYPE+"="+Mailbox.TYPE_INBOX+") )"+
        		"AND "+MessageColumns.SUBJECT+" IS NULL"+ ")" + 
        		" AND " + MessageColumns.THREAD_ID + " in (Select " +
        		MessageColumns.THREAD_ID + " from " + Message.TABLE_NAME + " where " +
        		MessageColumns.MAILBOX_TYPE + "=0 ) ";    
        
        /** Selection for deleted message in POP3 */
        public static final String DELETED_SELECTION =  MessageColumns.FLAG_LOADED + "=" +Message.FLAG_LOADED_DELETED;
        
        public static final String SEARCH_SELECTION = MessageColumns.MAILBOX_KEY + " IN ("
                + "SELECT " + MailboxColumns.ID + " FROM " + Mailbox.TABLE_NAME + " WHERE "
                + MailboxColumns.TYPE + " = " + Mailbox.TYPE_INBOX + ")";
       
        /** Selection to retrieve unread messages in "inbox" for any account */
        public static final String UNREAD_SELECTION = MessageColumns.FLAG_READ
                + "=0 AND " + INBOX_SELECTION + " AND "
                + MessageColumns.FLAG_DELETEHIDDEN + "=0";
        
        /** Selection to retrieve unread messages in "all mailbox" for any account */
        public static final String ALL_UNREAD_SELECTION = MessageColumns.FLAG_READ + "=0 AND "
                + MessageColumns.FLAG_DELETEHIDDEN + "=0 AND " + MessageColumns.MAILBOX_KEY + " IN (" + "SELECT "
                        + MailboxColumns.ID + " FROM " + Mailbox.TABLE_NAME + "" + " WHERE "
                        + MailboxColumns.TYPE + " = " + Mailbox.TYPE_INBOX + " OR "
                        + MailboxColumns.TYPE +" = " + Mailbox.TYPE_MAIL + " OR "  + MailboxColumns.TYPE +" = " + Mailbox.TYPE_DRAFTS + " OR "
                        + MailboxColumns.TYPE + " = " + Mailbox.TYPE_USER_CREATED_MAIL
                        +")" + " AND "
                        + FLAG_LOADED_SELECTION;

        /** Selection to retrieve all messages in "inbox" for one account */
        public static final String PER_ACCOUNT_INBOX_SELECTION = ACCOUNT_KEY_SELECTION + " AND "
                + INBOX_SELECTION;
        
        /** Selection to retrieve all messages in "inbox" for one account */
        public static final String PER_ACCOUNT_CONVERSATION_SELECTION = ACCOUNT_KEY_SELECTION + " AND "
                + CONVERSATION_SELECTION;


        private static final String ACCOUNT_FAVORITE_SELECTION = ACCOUNT_KEY_SELECTION + " AND "
                + ALL_FAVORITE_SELECTION;
        
        public static final String RECENTLY_SELECTION = MessageColumns.OPEN_TIME
                + "!=" + RECENTLY_NOREAD + " AND " + MessageColumns.MAILBOX_KEY + " NOT IN (" + "SELECT "
                + MailboxColumns.ID + " FROM " + Mailbox.TABLE_NAME + "" + " WHERE "
                + MailboxColumns.TYPE + " = " + Mailbox.TYPE_TRASH + ")" + " AND "
                + FLAG_LOADED_SELECTION;
        
        public static final String RECENTLY_UNREAD_SELECTION = 
                MessageColumns.ID + " IN (select " 
                + MessageColumns.ID + " from " + Message.TABLE_NAME + " where " + MessageColumns.FLAG_READ + "=0 AND "
                + MessageColumns.ID + " IN (select " + MessageColumns.ID + " from " + Message.TABLE_NAME + " where " + MessageColumns.OPEN_TIME + "!= " + RECENTLY_NOREAD
                + " AND " + MessageColumns.MAILBOX_KEY + " NOT IN (select " + MailboxColumns.ID + " FROM " + Mailbox.TABLE_NAME + "" + " WHERE "
                + MailboxColumns.TYPE + " = " + Mailbox.TYPE_TRASH + ")" + " AND "
                + FLAG_LOADED_SELECTION + " ORDER by " + MessageColumns.OPEN_TIME + " DESC limit " + RECENTLY_COUNT_LIMIT +"))";
        
        
        public static final String ALL_FAVORITE_UNREAD_SELECTION = MessageColumns.FLAG_FAVORITE
                + "=1 AND " + MessageColumns.MAILBOX_KEY + " NOT IN (" + "SELECT "
                + MailboxColumns.ID + " FROM " + Mailbox.TABLE_NAME + "" + " WHERE "
                + MailboxColumns.TYPE + " = " + Mailbox.TYPE_TRASH +
                " OR "  + MailboxColumns.TYPE + " = " + Mailbox.TYPE_JUNK + " )"  + " AND "
                + MessageColumns.FLAG_READ + "=0 AND "
                + FLAG_LOADED_SELECTION;

        public static String ATTACHMENT_MESSAGE_PREFIX = "__attachment_message__";


        
        public static String getVipSelection (String[] vipList) {
            final StringBuilder selection = new StringBuilder();
            boolean first = true;
            selection.append('(');
            // if there is no vip list, put dummy_id in selection
            if (vipList == null || vipList.length == 0) {
                selection.append(MessageColumns.FROM_LIST);
                selection.append(" LIKE '");
                selection.append("e@m@a@i@l@d@u@m@my"); //dummy account
                selection.append("%'");
            }else{
                for (String vipaccount : vipList) {
                    if (first) {
                        first = false;
                    } else{
                        selection.append(" OR ");
                    }

                    selection.append(MessageColumns.FROM_LIST);
                    selection.append(" LIKE '");
                    selection.append(getStringWithEscape(vipaccount));
                    selection.append("%'");

                }
            }

//            " NOT IN (" + "SELECT "
//                    + MailboxColumns.ID + " FROM " + Mailbox.TABLE_NAME + "" + " WHERE "
//                    + MailboxColumns.TYPE + " = " + Mailbox.TYPE_TRASH + " OR "  + MailboxColumns.TYPE + " = " + Mailbox.TYPE_JUNK + " )"

            selection.append(") AND " + MessageColumns.MAILBOX_KEY + " NOT IN (" + "SELECT "
                    + MailboxColumns.ID + " FROM " + Mailbox.TABLE_NAME + "" + " WHERE "
                    + MailboxColumns.TYPE + " = " + Mailbox.TYPE_TRASH + " OR " + MailboxColumns.TYPE + " = " + Mailbox.TYPE_JUNK + " OR "
                    + MailboxColumns.TYPE + " = " + Mailbox.TYPE_DRAFTS + " OR " + MailboxColumns.TYPE +" = " + Mailbox.TYPE_SENT + " OR "
                    + MailboxColumns.TYPE + " = " + Mailbox.TYPE_OUTBOX + " OR "
                    + MailboxColumns.TYPE + " = " + Mailbox.TYPE_ATTACHMENT + " OR "
                    + MailboxColumns.TYPE + " = " + Mailbox.TYPE_SCHEDULED_OUTBOX
                    +" )" + " AND "
                    + FLAG_LOADED_SELECTION);


            return selection.toString();
        }
        
        public static String getVipUnreadSelection (String[] vipList) {
            final StringBuilder selection = new StringBuilder();
            boolean first = true;
            selection.append('(');
            // if there is no vip list, put dummy_id in selection
            if (vipList == null || vipList.length == 0) {
                selection.append(MessageColumns.FROM_LIST);
                selection.append(" LIKE '");
                selection.append("e@m@a@i@l@d@u@m@my"); //dummy account
                selection.append("%'");
            } else{
                for (String vipaccount : vipList) {
                    if (first) {
                        first = false;
                    }else{
                        selection.append(" OR ");
                    }

                    selection.append(MessageColumns.FROM_LIST);
                    selection.append(" LIKE '");
                    selection.append(getStringWithEscape(vipaccount));
                    selection.append("%'");

                }
            }

            selection.append(") AND " + MessageColumns.MAILBOX_KEY + " NOT IN (" + "SELECT "
                    + MailboxColumns.ID + " FROM " + Mailbox.TABLE_NAME + "" + " WHERE "
                    + MailboxColumns.TYPE + " = " + Mailbox.TYPE_TRASH + " OR " + MailboxColumns.TYPE + " = " + Mailbox.TYPE_JUNK + " OR "
                    + MailboxColumns.TYPE + " = " + Mailbox.TYPE_DRAFTS + " OR " + MailboxColumns.TYPE +" = " + Mailbox.TYPE_SENT + " OR "
                    + MailboxColumns.TYPE + " = " + Mailbox.TYPE_OUTBOX + " OR "
                    + MailboxColumns.TYPE + " = " + Mailbox.TYPE_SCHEDULED_OUTBOX
                    + " )" + " AND "
                    + MessageColumns.FLAG_READ + "=0 AND "
                    + FLAG_LOADED_SELECTION);


            return selection.toString();
        }
        
        public static String getRecentlySelection (String[] recentList) {
            final StringBuilder selection = new StringBuilder();
            boolean first = true;
            selection.append('(');
            // if there is no vip list, put dummy_id in selection
            if (recentList == null || recentList.length == 0) {
                selection.append(MessageColumns.FROM_LIST);
                selection.append(" LIKE '");
                selection.append("e@m@a@i@l@d@u@m@my"); //dummy account
                selection.append("%'");
            }else{
                for (String recentlymessageId : recentList) {
                    if (first) {
                        first = false;
                    } else{
                        selection.append(" OR ");
                    }

                    selection.append(MessageColumns.ID);
                    selection.append(" = ");
                    selection.append(recentlymessageId);
                    
                }
            }

            selection.append(") AND "+ MessageColumns.MAILBOX_KEY + " IN (" + "SELECT "
                    + MailboxColumns.ID + " FROM " + Mailbox.TABLE_NAME + "" + " WHERE "
                    + MailboxColumns.TYPE + " = " + Mailbox.TYPE_INBOX + ")" + " AND "
                    + FLAG_LOADED_SELECTION);


            return selection.toString();
        }
        
        public static String getStringWithEscape(String str) {
            String patternString = str;
            if (patternString != null) {
                patternString = patternString.replaceAll("\\\\", "\\\\");
                patternString = patternString.replaceAll("%", "\\%");
                patternString = patternString.replaceAll("_", "\\_");
                patternString = patternString.replaceAll("\'", "\\''");
                
            } else {
                patternString = "";
            }
            return patternString;
        }
        
        /**
         * Selection for latest incoming messages. In order to tell whether
         * incoming or not, we need the mailbox type, which is in the mailbox
         * table, not the message table, so use a subquery.
         */
        private static final String LATEST_INCOMING_MESSAGE_SELECTION = MessageColumns.MAILBOX_KEY
                + " IN (SELECT " + RECORD_ID + " FROM " + Mailbox.TABLE_NAME + " WHERE "
                + MailboxColumns.ACCOUNT_KEY + "=? AND " + Mailbox.USER_VISIBLE_MAILBOX_SELECTION
                + " AND " + MailboxColumns.TYPE + " IN (" + Mailbox.TYPE_INBOX + "))";

        public static final String LATEST_INCOMING_UNREAD_MESSAGE_SELECTION_ALL_MAILBOX = MessageColumns.MAILBOX_KEY
                + " IN (SELECT " + RECORD_ID + " FROM " + Mailbox.TABLE_NAME + " WHERE "
                + MailboxColumns.ACCOUNT_KEY + "=? AND " + Mailbox.USER_VISIBLE_MAILBOX_SELECTION
                + " AND " + MailboxColumns.TYPE + " NOT IN (" + Mailbox.TYPE_DRAFTS + ","
                + Mailbox.TYPE_OUTBOX + "," + Mailbox.TYPE_SCHEDULED_OUTBOX + ","
                + Mailbox.TYPE_NOT_EMAIL + "," + Mailbox.TYPE_JUNK + ","
                + Mailbox.TYPE_SENT + "," + Mailbox.TYPE_SEARCH_RESULTS + "))"
                + " AND " + MessageColumns.FLAG_READ + " = 0 AND (" + MessageColumns.FLAGS + " & "
                        + Message.FLAG_NEW_MAIL + ") != 0" ;
        
        private static final String All_LATEST_INCOMING_MESSAGE_SELECTION = MessageColumns.MAILBOX_KEY
                + " IN (SELECT " + RECORD_ID + " FROM " + Mailbox.TABLE_NAME + " WHERE "
                + Mailbox.USER_VISIBLE_MAILBOX_SELECTION
                + " AND " + MailboxColumns.TYPE + " IN (" + Mailbox.TYPE_INBOX + "))";
        
        
        public static final String OUT_OF_SYNC_MESSAGE_SELECTION = MessageColumns.ACCOUNT_KEY
                + "=? AND " + MessageColumns.TIMESTAMP + ">0 AND " + MessageColumns.TIMESTAMP
                + "<=?";

        public static final String OUT_OF_SYNC_MESSAGE_MAILBOX_SELECTION = MessageColumns.MAILBOX_KEY
                + "=? AND "
                + Message.SERVER_TIMESTAMP
                + "<=?";

        // MNO-SVL_B2B START: m.hanifa
        public static final String[] OLDEST_TIMESTAMP_COLUMNS = new String[] {
            "MIN(" + MessageColumns.TIMESTAMP + ")"
        };

        private static final String OLDEST_MESSAGE_TIMESTAMP_SELECTION = MessageColumns.ACCOUNT_KEY
                + "=? AND " + MessageColumns.TIMESTAMP + ">0 AND " + MessageColumns.MAILBOX_KEY
                + "=?";

        public static final String MESSAGES_OLDER_THAN_TIMESTAMP_SELECTION = OLDEST_MESSAGE_TIMESTAMP_SELECTION
                + " AND " + MessageColumns.TIMESTAMP + " <?";

        public static final String MESSAGES_WITH_NO_TIMESTAMP_SELECTION = MessageColumns.ACCOUNT_KEY
                + "=? AND "
                + MessageColumns.TIMESTAMP
                + "=0 AND "
                + MessageColumns.MAILBOX_KEY
                + "=?";

        // MNO-SVL_B2B END: m.hanifa

        public static final String LEAST_SERVER_TIMESTAMP_COLUMNS = EmailContent.Message.SERVER_TIMESTAMP
                + " ASC";

        private static final String NEWEST_MESSAGE_SERVER_TIMESTAMP_SELECTION = EmailContent.Message.SERVER_TIMESTAMP
                + ">0 AND ("
                + EmailContent.Message.SERVER_ID
                + ">=0 OR "
                + EmailContent.Message.SERVER_ID
                + " isnull ) AND "
                + EmailContent.Message.MAILBOX_KEY
                + " IN (SELECT "
                + RECORD_ID
                + " FROM "
                + Mailbox.TABLE_NAME
                + " WHERE "
                + MailboxColumns.TYPE
                + " IN ("
                + Mailbox.TYPE_OUTBOX + "))";

        // _id field is in AbstractContent
        public String mDisplayName;

        public long mTimeStamp;

        public String mSubject;

        public boolean mFlagRead = false;

        public int mFlagLoaded = FLAG_LOADED_UNLOADED;

        public boolean mFlagFavorite = false;

        public boolean mFlagAttachment = false;

        public boolean mFlagReply = false; // Reply Sync for ICS by EJ 111129

        public long mOriginalId = NOT_SAVED;

        public int mFlags = 0;

        public String mServerId;

        public long mServerTimeStamp;

        public String mClientId;

        public String mMessageId;

        public long mMailboxKey;

        public long mAccountKey;

        public String mFrom;

        public String mTo;

        public String mCc;

        public String mBcc;

        public String mReplyTo;

        // For now, just the start time of a meeting invite, in ms
        public String mMeetingInfo;

        // change@wtl.subbu message importance
        // SVL Start - J.sb
        // Initialized to normal priority
        public int mImportance = 1;

        // SVL End - J.sb

        public int mFlagTruncated = FLAG_TRUNCATED_NO;

        public int mFlagMoved = 0;

        public int mDstMailBoxKey = -1;

        public int mFlagStatus = 0;

        // change@siso_hq for Load More with Inline Images - start
        public int mIsMimeLoaded = 0;

        // change@siso_hq for Load More with Inline Images - end
        public long mThreadId = 0; // suhyb 0729, for thread mail....

        public String mThreadName = null; // suhyb 0729, for thread mail....

        // change@siso.vpdj Conversation View start
        public String mConversationId;

        public byte[] mConversationIndex;

        // change@siso.vpdj end
        public String mAccountSchema;

        public int mMailboxType;

        public String mSnippet;

        // The following transient members may be used while building and
        // manipulating messages,
        // but they are NOT persisted directly by EmailProvider
        transient public String mText;

        transient public String mHtml;

        transient public String mTextReply;

        transient public String mHtmlReply;

        public int mEstimatedDataSize;

        transient public long mSourceKey;

        transient public ArrayList<Attachment> mAttachments = null;

        transient public String mIntroText;

        // P5_Porting_Email_Block_Start - worked by minsumr.kim
        // ================================================================================
        // Seven2010.07.20)
        // ================================================================================
        public int mTypeMsg;

        public long mMessageKey;

        public long mSevenAccountKey;

        public long mSevenMessageKey;

        public int mMissingBody;

        public int mMissingHtmlBody;

        public int mUnkEncoding;

        public int mSevenMailboxKey;

        // ================================================================================
        // change@wtl.jrabina EAS 14.0 LastVerbExecution start
        transient public int mLastVerb;

        transient public long mLastVerbTime;

        public static final int LAST_VERB_UNKNOWN = 0;

        public static final int LAST_VERB_REPLYTOSENDER = 1;

        public static final int LAST_VERB_REPLYTOALL = 2;

        public static final int LAST_VERB_FORWARD = 3;
        
        public static final int LAST_VERB_REPLY_FORWARD = 4;

        // change@wtl.jrabina EAS 14.0 LastVerbExecution end
        // change@wtl.cpillers start
//        transient public FollowupFlag mFollowupFlag = null;

        // change@wtl.cpillers end
        // change@wtl.akulik smime support start
        public static final int SIGN_SHIFT = 0;

        public static final int ENCRYPT_SHIFT = 1;

        public static final int PROCESS_SHIFT = 2;

        public static final int VERIFY_SHIFT = 3;

        public static final int PGP_SHIFT = 4;

        transient public boolean mEncrypted;

        transient public boolean mSigned;

        transient public boolean mPGP;

        transient public boolean mOpaqueSigned;

        public String mKeyIds;
        
        transient public boolean mProcessed;

        transient public boolean mVerified;

        public Integer mEncryptionAlgorithm;

        // change@wtl.akulik smime support end
        // change@wtl.rprominski voicemail support start
        public String mUmCallerId;

        public String mUmUserNotes;

        // change@wtl.rprominski voicemail support end
        // change@wtl.kSingh - SMS Sync - starts
        public int mMessageType;

        public int mMessageDirty;

        // change@siso.Spoorti - IRM 14.1 support start
        public String mIRMTemplateId;
        public int mIRMLicenseFlag = -1;
        public int mIRMOwner = 0;
        public String mIRMContentExpiryDate;
        public String mIRMContentOwner;
        public int mIRMRemovalFlag;
        public String mIRMTemplateName;
        public String mIRMTemplateDescription;
        // change@siso.Spoorti - IRM 14.1 support end

        public int mRetrySendTimes = 0;
        public int mDeleteHidden = 0;
        //change@siso madhu.dumpa orphanMessage issue start
        public int mDirtyCommit = 0;
        //change@siso madhu.dumpa orphanMessage issue end
        //change@k1001.kim - recently read mails
        public Long mOpenTime ;

        // change@wtl.kSingh - SMS Sync - ends
        // change@wtl.dtuttle start
        // dr_17: flag values for truncation. These values will be packed in
        // with
        // load flags below in the database
        // Values used in mFlagTruncated
        public static final byte FLAG_TRUNCATED_YES = 0x1;
        public static final byte FLAG_TRUNCATED_NO = 0x0;
        
        public static final byte FLAG_IRM_UPDATE = 0x2;
        public static final byte FLAG_IRM_REMOVAL = 0x1;
        public static final byte FLAG_IRM_NONE = 0x0;
        
        // change@wtl.dtuttle end
        // change@siso_hq for Load More with Inline Images - start
        public static final int ISMIMELOADED_NO = 0;
        public static final int ISMIMELOADED_YES = 1;
        public static final int ISMIMENEEDTOLOAD = 2;

        // change@siso_hq for Load More with Inline Images - end
        // P5_Porting_Email_Block_End - worked by minsumr.kim

        // Values used in mFlagRead
        public static final int UNREAD = 0;
        public static final int READ = 1;

        // Values used in mFlagLoaded
        public static final int FLAG_LOADED_UNLOADED = 0;
        public static final int FLAG_LOADED_COMPLETE = 1;
        public static final int FLAG_LOADED_PARTIAL = 2;
        public static final int FLAG_LOADED_DELETED = 3;
        public static final int FLAG_LEGACY_LOADING = 4;

        // Bits used in mFlags
        // The following three states are mutually exclusive, and indicate
        // whether the message is an
        // original, a reply, or a forward
        public static final int FLAG_TYPE_ORIGINAL = 0;
        public static final int FLAG_TYPE_REPLY = 1 << 0;
        public static final int FLAG_TYPE_FORWARD = 1 << 1;
        public static final int FLAG_TYPE_MASK = FLAG_TYPE_REPLY | FLAG_TYPE_FORWARD;

        // The following flags indicate messages that are determined to be
        // incoming meeting related
        // (e.g. invites from others)
        public static final int FLAG_INCOMING_MEETING_INVITE = 1 << 2;
        public static final int FLAG_INCOMING_MEETING_CANCEL = 1 << 3;
        public static final int FLAG_INCOMING_MEETING_MASK = FLAG_INCOMING_MEETING_INVITE
                | FLAG_INCOMING_MEETING_CANCEL;

        // The following flags indicate messages that are outgoing and meeting
        // related
        // (e.g. invites TO others)
        public static final int FLAG_OUTGOING_MEETING_INVITE = 1 << 4;
        public static final int FLAG_OUTGOING_MEETING_CANCEL = 1 << 5;
        public static final int FLAG_OUTGOING_MEETING_ACCEPT = 1 << 6;
        public static final int FLAG_OUTGOING_MEETING_DECLINE = 1 << 7;
        public static final int FLAG_OUTGOING_MEETING_TENTATIVE = 1 << 8;
        public static final int FLAG_OUTGOING_MEETING_MASK = FLAG_OUTGOING_MEETING_INVITE
                | FLAG_OUTGOING_MEETING_CANCEL | FLAG_OUTGOING_MEETING_ACCEPT
                | FLAG_OUTGOING_MEETING_DECLINE | FLAG_OUTGOING_MEETING_TENTATIVE;
        public static final int FLAG_OUTGOING_MEETING_REQUEST_MASK = FLAG_OUTGOING_MEETING_INVITE
                | FLAG_OUTGOING_MEETING_CANCEL;

        // 8 general purpose flags (bits) that may be used at the discretion of
        // the sync adapter
        public static final int FLAG_SYNC_ADAPTER_SHIFT = 9;

        // All flag type should be defined here. but foolish GED Email code
        // add (1 << 9)-shifted flag "MESSAGE_FLAG_MOVED_MESSAGE" at
        // EasSyncService.java.
        // So be careful not to duplicate (1 << 9)-shifted flag.

        public static final int FLAG_SYNC_ADAPTER_MASK = 255 << FLAG_SYNC_ADAPTER_SHIFT;

        /**
         * Bit used in mFlags indicating that the outgoing message should *not*
         * include quoted original message. ("Not", in order to keep
         * compatibility with old databases)
         */
        public static final int FLAG_NOT_INCLUDE_QUOTED_TEXT_SHIFT = 17;

        public static final int FLAG_NOT_INCLUDE_QUOTED_TEXT = 1 << FLAG_NOT_INCLUDE_QUOTED_TEXT_SHIFT;
        
        public static final int FLAG_NEW_MAIL = 1 << 18;
        public static final int FLAG_DRAFT_MAIL = 1 << 19;
        
        // P5_Porting_Email_Block_Start - worked by minsumr.kim
        public static final int FLAG_ORIGINAL_MESSAGE_EDITED = 1 << 10; // for
        // smart
        // forward

        public static final int FLAG_READ_RECEIPT_REQUESTED = 1 << 11;

        public static final int FLAG_DELIVERY_RECEIPT_REQUESTED = 1 << 12;

        public static final int FLAG_EDIT_INFO_OF_ORIGINAL_MESSAGE = 1 << 13;

        // MIME support for EAS
        public static final int FLAG_NEED_TO_FETCH_MIME = 1 << 14;

        /**
         * Flag used to mark a POP message local only.
         * Used only for POP messages
         * Introduce for new POP sync concept
         */
        //public static final int FLAG_POP_LOCAL_COPY = 1 << 15;
        
        // change@siso A.JULKA Read/Delivery support end
        // change@wtl.akulik smime support start
        public static final int MSG_CLASS_SMIME_SIGNED = 1;

        public static final int MSG_CLASS_SMIME_ENCRYPTED = 2;

        public static final int MSG_SYNC_ID_OF_SEND_FAILED_EXCEED_CAPACITY = -1;
        public static final int MSG_SYNC_ID_OF_SEND_FAILED_SERVER_ERROR = -2;
        public static final int MSG_SYNC_ID_OF_SEND_FAILED_NETWORK_ERROR = -3;
        public static final int MSG_SYNC_ID_OF_SEND_FAILED_INVALID_ADDR = -4;
        public static final int MSG_SYNC_ID_OF_SEND_FAILED_MAILBOXQUOTA_EXCEEDED = -5;
        public static final int MSG_SYNC_ID_OF_SEND_FAILED_SECURITY_ERROR = -6;
        public static final int MSG_SYNC_ID_ZERO = 0;
        /**
         * IMAP-Drafts-Sync
         * Introducing this flag to mark a Drafts message which is set to be uploaded to server,
         */
        public static final int MSG_SYNC_ID_OF_DRAFTS_MSG_TO_UPLOAD = -7;
        /**
         * IMAP-Drafts-Sync
         * This indicates that this message was created when either the IMAP account was not configured
         * properly OR the IMAP account does have a syncd Drafts folder.
         */
        public static final int MSG_SYNC_ID_OF_DRAFTS_MSG_TO_UPLOAD_ERROR = -8;
        
        public static final int MSG_SYNC_ID_OF_SEND_FAILED_EXCEED_DAILY_MESSAGE_LIMIT = -9;


        // change@wtl.akulik smime support end
        // change@wtl.kSingh - SMS Sync
        // public static final int FLAG_SMS_CLASS = 1 << 9;
        public static final String SMS_DUMMY_CLIENT_ID = "SMS";

        // P5_Porting_Email_Block_End - worked by minsumr.kim

        // change@siso.gaurav email sort on size start
        public int mSize;
        // change@siso.gaurav email sort on size end
        //change@siso madhu.dumpa orphanMessage issue start
        public static final int IS_COMMIT_DIRTY_YES = 1;
        public static final int IS_COMMIT_DIRTY_NO = 0;
        //change@siso madhu.dumpa orphanMessage issue end

        public Message() {
            mBaseUri = CONTENT_URI;
        }

        public void delete(Context context, Uri uri) {
            EmailContent.delete(context, uri, mId);
        }
        @Override
        public ContentValues toContentValues() {
            ContentValues values = new ContentValues();

            // Assign values for each row.
            values.put(MessageColumns.DISPLAY_NAME, mDisplayName);
            values.put(MessageColumns.TIMESTAMP, mTimeStamp);
            values.put(MessageColumns.SUBJECT, mSubject);
            values.put(MessageColumns.FLAG_READ, mFlagRead);
            values.put(MessageColumns.FLAG_LOADED, mFlagLoaded);
            values.put(MessageColumns.FLAG_FAVORITE, mFlagFavorite);
            values.put(MessageColumns.FLAG_ATTACHMENT, mFlagAttachment);
            values.put(MessageColumns.FLAGS, mFlags);

            values.put(MessageColumns.FLAG_REPLY, mFlagReply);
            values.put(MessageColumns.ORIGINAL_ID, mOriginalId);

            values.put(SyncColumns.SERVER_ID, mServerId);
            values.put(SyncColumns.SERVER_TIMESTAMP, mServerTimeStamp);
            values.put(MessageColumns.CLIENT_ID, mClientId);
            values.put(MessageColumns.MESSAGE_ID, mMessageId);

            values.put(MessageColumns.MAILBOX_KEY, mMailboxKey);
            values.put(MessageColumns.ACCOUNT_KEY, mAccountKey);

            values.put(MessageColumns.FROM_LIST, mFrom);
            values.put(MessageColumns.TO_LIST, mTo);
            values.put(MessageColumns.CC_LIST, mCc);
            values.put(MessageColumns.BCC_LIST, mBcc);
            values.put(MessageColumns.REPLY_TO_LIST, mReplyTo);

            values.put(MessageColumns.MEETING_INFO, mMeetingInfo);

            // change@wtl.rprominski voicemail support start
            values.put(MessageColumns.UM_CALLER_ID, mUmCallerId);
            values.put(MessageColumns.UM_USER_NOTES, mUmUserNotes);
            // change@wtl.rprominski voicemail support end

            values.put(MessageColumns.ACCOUNT_SCHEMA, mAccountSchema);
            values.put(MessageColumns.MAILBOX_TYPE, mMailboxType);

            // ------------------------------------------------------------------------------------------------
            // set Thread mail parameter
            // by suhyb, 20100729.

            // make thread title
            // remove all reply, forward prefix (Re:, Fwd: or Fw
            // remove all blank character.
            if (mThreadId == 0) {
                mThreadId = getThreadIdFromSubject(mSubject,mAccountKey);
            }
            // ------------------------------------------------------------------------------------------------

            values.put(MessageColumns.THREAD_ID, mThreadId); // suhyb 0729, for
            // thread mail....

            // values.put(MessageColumns.THREAD_NAME, mThreadName); // suhyb
            // 0729, for thread mail....

            // change@wtl.subbu message importance
            values.put(MessageColumns.IMPORTANCE, mImportance);
            values.put(MessageColumns.ISTRUNCATED, mFlagTruncated);
            values.put(MessageColumns.FLAG_MOVED, mFlagMoved);
            values.put(MessageColumns.DST_MAILBOX_KEY, mDstMailBoxKey);
            values.put(MessageColumns.FLAGSTATUS, mFlagStatus);
            // change@siso_hq for Load More with Inline Images - start
            values.put(MessageColumns.ISMIMELOADED, mIsMimeLoaded);
            // change@siso_hq for Load More with Inline Images - end
            // change@siso.vpdj Conversation View start
            values.put(MessageColumns.CONVERSATION_ID, mConversationId);
            values.put(MessageColumns.CONVERSATION_INDEX, mConversationIndex);
            // change@siso.vpdj end

            // change@wtl.akulik smime support start
            values.put(MessageColumns.SMIME_FLAGS, getSmimeFlags());
            values.put(MessageColumns.ENCRYPTION_ALGORITHM, mEncryptionAlgorithm);
            // change@wtl.akulik smime support end
            // change@wtl.jrabina EAS 14.0 LastVerbExecution start
            values.put(MessageColumns.LAST_VERB, mLastVerb);
            values.put(MessageColumns.LAST_VERB_TIME, mLastVerbTime);
            // change@wtl.jrabina EAS 14.0 LastVerbExecution end

            values.put(MessageColumns.MESSAGE_TYPE, mMessageType);
            values.put(MessageColumns.MESSAGE_DIRTY, mMessageDirty);
            // change@wtl.kSingh - SMS Sync - ends

            values.put(MessageColumns.SNIPPET, mSnippet);
            // change@siso.Spoorti - IRM 14.1 support start
            values.put(MessageColumns.IRM_TEMPLATE_ID, mIRMTemplateId);
            values.put(MessageColumns.IRM_CONTENT_OWNER, mIRMContentOwner);
            values.put(MessageColumns.IRM_LICENSE_FLAG, mIRMLicenseFlag);
            values.put(MessageColumns.IRM_OWNER, mIRMOwner);
            values.put(MessageColumns.IRM_CONTENT_EXPIRY_DATE, mIRMContentExpiryDate);
            values.put(MessageColumns.IRM_REMOVAL_FLAG, mIRMRemovalFlag);
            values.put(MessageColumns.IRM_TEMPLATE_NAME, mIRMTemplateName);
            values.put(MessageColumns.IRM_TEMPLATE_DECSRIPTION, mIRMTemplateDescription);
            // change@siso.Spoorti - IRM 14.1 support end

            values.put(MessageColumns.RETRY_SEND_TIMES, mRetrySendTimes);
            
            values.put(MessageColumns.KEY_IDS, mKeyIds);
            // change@siso.gaurav email sort on size start
            values.put(MessageColumns.SIZE, mEstimatedDataSize);
            // change@siso.gaurav email sort on size end
            //change@siso madhu.dumpa orphanMessage issue start
            values.put(MessageColumns.DIRTY_COMMIT, mDirtyCommit);
            //change@siso madhu.dumpa orphanMessage issue end
            values.put(MessageColumns.OPEN_TIME, mOpenTime);
            
            values.put(MessageColumns.FLAG_DELETEHIDDEN, mDeleteHidden);
            return values;
        }

        // MNO-SVL_B2B START: m.hanifa
        public static long getOldestMessageTimestamp(Context context, long accountId, long mailboxId) {
            return EmailContentUtils.getFirstRowLong(context, CONTENT_URI, Message.OLDEST_TIMESTAMP_COLUMNS,
                    Message.OLDEST_MESSAGE_TIMESTAMP_SELECTION, new String[] {
                            Long.toString(accountId), Long.toString(mailboxId)
                    }, null, 0, Long.valueOf(-1));
        }

        // MNO-SVL_B2B END: m.hanifa

        public static long getNewestServerTimestampMessage(Context context) {
            return EmailContentUtils.getFirstRowLong(context, CONTENT_URI, new String[] {
                MessageColumns.ID
            }, Message.NEWEST_MESSAGE_SERVER_TIMESTAMP_SELECTION, null,
                    LEAST_SERVER_TIMESTAMP_COLUMNS, 0, Long.valueOf(-1));
        }

        private void updateBodyFlags(Context context) {
            if (Body.bodyFilesRemoved(context, mAccountKey, mId)) {
                if ("eas".equalsIgnoreCase(mAccountSchema)) {
                    mFlagTruncated = FLAG_TRUNCATED_YES;
                } else {
                    mFlagLoaded = Message.FLAG_LOADED_PARTIAL;
                }
            }
        }
        
        private boolean inlineAttachmentsDeleted(Context context) {
            boolean result = false;
            
            Attachment[] atts = Attachment.restoreAttachmentsWithMessageId(context, mId);
            
            if (atts != null && atts.length > 0) {
                for (int i=0; i<atts.length; i++) {
                    if (atts[i].mIsInline == Attachment.IS_INLINE_ATTACHMENT 
                            && !AttachmentUtilities.isExist(context, mAccountKey, atts[i].mId)) {
                        result = true;
                        break;
                    }
                }
            }
            
            return result;
        }
        
        private void setTruncated(Context context) {
            EmailLog.d(TAG, "setTruncated : set ISTRUNCATED to YES");
            mFlagTruncated = FLAG_TRUNCATED_YES;
            ContentValues cv = new ContentValues();
            cv.put(MessageColumns.ISTRUNCATED, Message.FLAG_TRUNCATED_YES);
            context.getContentResolver().update(ContentUris.withAppendedId(Message.CONTENT_URI, mId), cv, null, null);
        }

        /**
         * This gives back the first N messages from inbox for this account and this mailbox
         * @param cntx
         * @param accountId
         * @param mailboxKey
         * @param limit
         */
        public static ArrayList<String> getFirstNMessagesFromInbox(Context cntx, long accountId,
                                                                    long mailboxKey, int limit) {
            EmailLog.e(TAG, "getFirstNMessagesFromInbox:" + accountId + ":" + mailboxKey +
                    ":" + limit);
            ArrayList<String> firstNMessages = new ArrayList<String>();
            if (cntx == null) {
                return firstNMessages;
            }
            ContentResolver resolver = cntx.getContentResolver();
            if (resolver == null) {
                return firstNMessages;
            }
            Cursor c = null;
            String selectionClause = ACCOUNT_KEY + "=? AND " + MAILBOX_KEY + "=? AND "
                    + FLAG_LOADED + "<>" + FLAG_LOADED_DELETED + " ORDER BY " + SERVER_TIMESTAMP +
                    " DESC LIMIT " + limit;
            try {
                c = resolver.query(Message.CONTENT_URI, new String[]{SERVER_ID}, selectionClause,
                        new String[]{Long.toString(accountId), Long.toString(mailboxKey)}, null);

                if (c != null && c.moveToFirst()) {
                    do {
                        String serverId = c.getString(0);
                        firstNMessages.add(serverId);
                    } while(c.moveToNext());
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in getMessageCountInInbox");
                e.printStackTrace();
                return firstNMessages;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
            return firstNMessages;
        }

        /**
         * This gives back the first N messages from inbox for this account and this mailbox
         * @param cntx
         * @param accountId
         * @param mailboxKey
         * @param limit
         */
        public static long getOldestMessagesTimeStamp(Context cntx, long accountId,
                                                                   long mailboxKey) {
            EmailLog.e(TAG, "getOldestMessages:" + accountId + ":" + mailboxKey);
            long oldestTimeStamp = 0;

            if (cntx == null) {
                return 0;
            }
            ContentResolver resolver = cntx.getContentResolver();
            if (resolver == null) {
                return 0;
            }
            Cursor c = null;
            String selectionClause = ACCOUNT_KEY + "=? AND " + MAILBOX_KEY + "=? AND "
                    + FLAG_LOADED + "<>" + FLAG_LOADED_DELETED + " ORDER BY " + SERVER_TIMESTAMP +
                    " ASC LIMIT " + 1;
            try {
                c = resolver.query(Message.CONTENT_URI, new String[]{SERVER_TIMESTAMP}, selectionClause,
                        new String[]{Long.toString(accountId), Long.toString(mailboxKey)}, null);

                if (c != null && c.moveToFirst()) {
                    do {
                        oldestTimeStamp = c.getLong(0);
                    } while(c.moveToNext());
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in getOldestMessagesTimeStamp");
                e.printStackTrace();
                return oldestTimeStamp;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
            return oldestTimeStamp;
        }

        /**
         * This gives is the number of visisble mails in the inbox of this account
         * @param cntx
         * @param accountId
         * @param mailboxKey
         * @return
         */
        public static int getMessageCountInInbox (Context cntx, long accountId, long mailboxKey) {
            EmailLog.e(TAG, "getMessageCountInInbox:" + accountId + ":" + mailboxKey);
            int count = -1;
            if (cntx == null) {
                return count;
            }
            ContentResolver resolver = cntx.getContentResolver();
            if (resolver == null) {
                return count;
            }
            Cursor c = null;
            String selectionClause = ACCOUNT_KEY + "=? AND " + MAILBOX_KEY + "=? AND "
                    + FLAG_LOADED + "<>" + FLAG_LOADED_DELETED;
            try {
                c = resolver.query(Message.CONTENT_URI, new String[]{"COUNT(*)"}, selectionClause,
                        new String[]{Long.toString(accountId), Long.toString(mailboxKey)}, null);

                if (c != null && c.moveToFirst()) {
                    count = c.getInt(0);
                    EmailLog.e(TAG, "getMessageCountInInbox count:" + count);
                    return count;
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in getMessageCountInInbox");
                e.printStackTrace();
                return count;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
            return count;
        }

        /**
         * This gives is the number of visisble mails in the inbox of this account
         * @param cntx
         * @param accountId
         * @param mailboxKey
         * @return
         */
        public static int getDeletedMessageCount (Context cntx, long accountId, long mailboxKey) {
            EmailLog.e(TAG, "getDeletedMessageCount:" + accountId + ":" + mailboxKey);
            int count = 0;
            if (cntx == null) {
                return count;
            }
            ContentResolver resolver = cntx.getContentResolver();
            if (resolver == null) {
                return count;
            }
            Cursor c = null;
            String selectionClause = ACCOUNT_KEY + "=? AND " + MAILBOX_KEY + "=? AND "
                    + FLAG_LOADED + "=" + FLAG_LOADED_DELETED;
            try {
                c = resolver.query(Message.CONTENT_URI, new String[]{"COUNT(*)"}, selectionClause,
                        new String[]{Long.toString(accountId), Long.toString(mailboxKey)}, null);

                if (c != null && c.moveToFirst()) {
                    count = c.getInt(0);
                    EmailLog.e(TAG, "getDeletedMessageCount count:" + count);
                    return count;
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in getDeletedMessageCount");
                e.printStackTrace();
                return count;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
            return count;
        }

        /**
         * This get all the visible messages in this mailbox for this account,
         * ordered by timestamp ASC
         * @param cntx
         * @param accountId
         * @param mailboxKey
         * @return
         */
        public static ArrayList<String> getAllMessagesFromInbox (Context cntx, long accountId,
                                                                 long mailboxKey) {
            EmailLog.e(TAG, "getAllMessagesFromInbox:" + accountId + ":" + mailboxKey);
            ArrayList<String> firstNMessages = new ArrayList<String>();
            if (cntx == null) {
                return firstNMessages;
            }
            ContentResolver resolver = cntx.getContentResolver();
            if (resolver == null) {
                return firstNMessages;
            }
            Cursor c = null;
            String selectionClause = ACCOUNT_KEY + "=? AND " + MAILBOX_KEY + "=? AND "
                    + FLAG_LOADED + "<>" + FLAG_LOADED_DELETED + " ORDER BY " + SERVER_TIMESTAMP +
                    " ASC";
            try {
                c = resolver.query(Message.CONTENT_URI, new String[]{SERVER_ID}, selectionClause,
                        new String[]{Long.toString(accountId), Long.toString(mailboxKey)}, null);

                if (c != null && c.moveToFirst()) {
                    do {
                        String serverId = c.getString(0);
                        firstNMessages.add(serverId);
                    } while(c.moveToNext());
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in getAllMessagesFromInbox");
                e.printStackTrace();
                return firstNMessages;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
            return firstNMessages;
        }

        public static void deleteMessage (Context cntx, String uid) {
            EmailLog.e(TAG, "deleteMessage:" + uid);
            ContentResolver resolver = cntx.getContentResolver();
            Cursor c = null;
            String selectionClause = SERVER_ID + "=?";
            try {
                resolver.delete(Message.CONTENT_URI, selectionClause, new String[]{uid});
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in deleteMessage");
                e.printStackTrace();
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        }

        public static long getMessageIdByServerId (Context cntx, String serverId) {
            EmailLog.d(TAG, "getMessageIdByServerId:" + serverId);
            int msgId = -1;
            if (cntx == null) {
                return msgId;
            }
            ContentResolver resolver = cntx.getContentResolver();
            if (resolver == null) {
                return msgId;
            }
            Cursor c = null;
            String selectionClause = SERVER_ID + "=?";
            try {
                c = resolver.query(Message.CONTENT_URI, new String[]{RECORD_ID}, selectionClause,
                        new String[]{serverId}, null);

                if (c != null && c.moveToFirst()) {
                    msgId = c.getInt(0);
                    EmailLog.d(TAG, "getMessageIdByServerId msgId:" + msgId);
                    return msgId;
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in getMessageIdByServerId");
                e.printStackTrace();
                return msgId;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
            return msgId;
        }

        /**
         * For updating the flag for messages
         * @param cntx
         * @param uids
         * @param flag
         */
        public static void updateMessageFlag (Context cntx, List<String> uids, int flag) {
            EmailLog.e(TAG, "updateMessageFlag:" + flag);
            ContentResolver resolver = cntx.getContentResolver();
            String selectionClause = SERVER_ID + "=?";

            try {
                for (String uid : uids) {
                    long msgId = getMessageIdByServerId(cntx, uid);
                    Message msg = restoreMessageWithId(cntx, msgId);
                    if (msg == null)
                        continue;
                    int newFlag = msg.mFlags | flag;
                    ContentValues cv = new ContentValues();
                    cv.put(MessageColumns.FLAGS, newFlag);
                    resolver.update(Message.CONTENT_URI, cv, selectionClause, new String[]{uid});
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in updateMessageFlag");
                e.printStackTrace();
            }
        }

        public static Message restoreMessageWithId(Context context, long id) {
            Uri u = ContentUris.withAppendedId(Message.CONTENT_URI, id);
            if (context == null) {
                // throw new NullPointerException("context");
                return null;
            }
            ContentResolver resolver = context.getContentResolver();
            if (resolver == null) {
                // throw new NullPointerException("resolver");
                return null;
            }
            Cursor c = null;
            try {
                c = resolver.query(u, Message.CONTENT_PROJECTION, null, null, null);

                if (c != null && c.moveToFirst()) {
                    Message message = getContent(c, Message.class);
                    if (message != null) {
                        message.updateBodyFlags(context);
                        // This function is required for EAS accounts only
                        if (EmailFeature.useMimeForEas()
                                && "eas".equalsIgnoreCase(message.mAccountSchema)
                                && message.mFlagAttachment
                                && message.inlineAttachmentsDeleted(context)) {
                            message.setTruncated(context);
                        }
                    }
                    return message;
                } else {
                    return null;
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in restoreMessageWithId");
                e.printStackTrace();
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        }

        
        public static Long getMessgeIdWithSyncServerId(Context context, String SyncServerId) {
                        
            return EmailContentUtils.getFirstRowLong(context, Message.CONTENT_URI, new String[] {RECORD_ID}, DELETED_SELECTION +" AND "+Message.SERVER_ID + "=?", new String[] {
                    String.valueOf(SyncServerId)}, null, 0, Long.valueOf(-1));
        }

        public static Long getDeletedMessgeIdWithSyncServerId(Context context, String SyncServerId) {
            
            return EmailContentUtils.getFirstRowLong(context, Message.DELETED_CONTENT_URI, new String[] {RECORD_ID}, Message.SERVER_ID + "=?", new String[] {
                    String.valueOf(SyncServerId)}, null, 0, Long.valueOf(-1));
        }
        
        public static Long getIMAPMessgeIdWithSyncServerId(Context context, String SyncServerId) {
            
            return EmailContentUtils.getFirstRowLong(context, Message.CONTENT_URI, new String[] {RECORD_ID}, SEARCH_SELECTION +" AND "+Message.SERVER_ID + "=?", new String[] {
                    String.valueOf(SyncServerId)}, null, 0, Long.valueOf(-1));
        }
        
        // ----------------------------------------------------------------------------------
        // suhyb 0729, for thread mail....
        // main query method to get all Messages which include given string.
        public static Message[] restoreMessageWithThreadName(Context context, String szQueryName,
                String szStartID, String szLimitCount) {
            Cursor c = null;
            try {
                c = context.getContentResolver().query(
                        Message.CONTENT_URI,
                        Message.CONTENT_PROJECTION,
                        MessageColumns.THREAD_NAME + "=" + szQueryName + " AND "
                                + MessageColumns.ID + " > " + szStartID + " limit " + szLimitCount,
                        null, null);
                if (c != null) {
                    int count = c.getCount();
                    Message[] messages = new Message[count];
                    for (int i = 0; i < count; ++i) {
                        c.moveToNext();

                        messages[i] = getContent(c, Message.class);
                        // messages[i] = new Message().restore(c);
                        if (messages[i] != null)
                            messages[i].updateBodyFlags(context);
                    }
                    return messages;
                } else {
                    return null;
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in restoreMessageWithThreadName", e);
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        }

        public static Message[] restoreMessageAllWithThreadName(Context context, String szQueryName) {
            Cursor c = null;
            try {
                c = context.getContentResolver().query(Message.CONTENT_URI,
                        Message.CONTENT_PROJECTION, MessageColumns.THREAD_NAME + "=" + szQueryName,
                        null, null);
                
                if(c != null) {
                    int count = c.getCount();
                    Message[] messages = new Message[count];
                    for (int i = 0; i < count; ++i) {
                        c.moveToNext();

                        messages[i] = getContent(c, Message.class);
                        // messages[i] = new Message().restore(c);
                        if (messages[i] != null) messages[i].updateBodyFlags(context);
                    }
                    return messages;
                } else {
                    return null;
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in restoreMessageAllWithThreadName", e);
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        }

        public static Message[] restoreMessageWithLimit(Context context, String szStartID,
                String szLimitCount) {
            Cursor c = null;
            try {
                c = context.getContentResolver().query(Message.CONTENT_URI,
                        Message.CONTENT_PROJECTION,
                        MessageColumns.ID + " > " + szStartID + " limit " + szLimitCount, null,
                        null);
                
                if(c != null) {
                    int count = c.getCount();
                    Message[] messages = new Message[count];
                    for (int i = 0; i < count; ++i) {
                        c.moveToNext();

                        messages[i] = getContent(c, Message.class);
                        // messages[i] = new Message().restore(c);
                        if (messages[i] != null) messages[i].updateBodyFlags(context);
                    }
                    return messages;
                } else {
                    return null;
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in restoreMessageWithLimit", e);
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        }
        
        public static long[] getMessagesIdsWhere(Context context, String selection, 
                String[] selectionArgs) {
            long[] ids = null;
            Cursor c = null;
            try {
                c = context.getContentResolver().query(Message.CONTENT_URI,
                        new String[]{RECORD_ID}, selection, selectionArgs, null);

                if (c != null) {
                    int count = c.getCount();
                    ids = new long[count];
                    for (int i = 0; i < count; ++i) {
                        c.moveToNext();
                        ids[i] = c.getLong(CONTENT_ID_COLUMN);
                    }
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in restoreMessagesWhere", e);
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
            
            return ids;
        }
        
        public static Message[] restoreMessageAll(Context context) {
            Cursor c = null;
            try {
                c = context.getContentResolver().query(Message.CONTENT_URI,
                        Message.CONTENT_PROJECTION, null, null, null);

                if(c != null) {
                    int count = c.getCount();
                    Message[] messages = new Message[count];
                    for (int i = 0; i < count; ++i) {
                        c.moveToNext();

                        messages[i] = getContent(c, Message.class);
                        // messages[i] = new Message().restore(c);
                        if (messages[i] != null) messages[i].updateBodyFlags(context);
                    }
                    return messages;
                } else {
                    return null;
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in restoreMessageAll", e);
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        }

        // ----------------------------------------------------------------------------------
        // P5_Porting_Email_Block_End - worked by minsumr.kim

        @Override
        public void restore(Cursor cursor) {
            mBaseUri = CONTENT_URI;
            mId = cursor.getLong(CONTENT_ID_COLUMN);
            mDisplayName = cursor.getString(CONTENT_DISPLAY_NAME_COLUMN);
            mTimeStamp = cursor.getLong(CONTENT_TIMESTAMP_COLUMN);
            mSubject = cursor.getString(CONTENT_SUBJECT_COLUMN);
            mFlagRead = cursor.getInt(CONTENT_FLAG_READ_COLUMN) == 1;
            mFlagLoaded = cursor.getInt(CONTENT_FLAG_LOADED_COLUMN);
            mFlagFavorite = cursor.getInt(CONTENT_FLAG_FAVORITE_COLUMN) == 1;
            mFlagAttachment = cursor.getInt(CONTENT_FLAG_ATTACHMENT_COLUMN) == 1;
            mFlags = cursor.getInt(CONTENT_FLAGS_COLUMN);
            mServerId = cursor.getString(CONTENT_SERVER_ID_COLUMN);
            mServerTimeStamp = cursor.getLong(CONTENT_SERVER_TIMESTAMP_COLUMN);
            mClientId = cursor.getString(CONTENT_CLIENT_ID_COLUMN);
            mMessageId = cursor.getString(CONTENT_MESSAGE_ID_COLUMN);
            mMailboxKey = cursor.getLong(CONTENT_MAILBOX_KEY_COLUMN);
            mAccountKey = cursor.getLong(CONTENT_ACCOUNT_KEY_COLUMN);
            mFrom = cursor.getString(CONTENT_FROM_LIST_COLUMN);
            mTo = cursor.getString(CONTENT_TO_LIST_COLUMN);
            mCc = cursor.getString(CONTENT_CC_LIST_COLUMN);
            mBcc = cursor.getString(CONTENT_BCC_LIST_COLUMN);
            mReplyTo = cursor.getString(CONTENT_REPLY_TO_COLUMN);
            mMeetingInfo = cursor.getString(CONTENT_MEETING_INFO_COLUMN);
            mThreadId = cursor.getLong(CONTENT_THREAD_ID_COLUMN); // suhyb 0729,
            // for
            // thread mail....
            mThreadName = cursor.getString(CONTENT_THREAD_NAME_COLUMN); // reserved
            // field
            // change@wtl.subbu message importance
            mImportance = cursor.getInt(CONTENT_IMPORTANCE_COLUMN);
            mFlagTruncated = cursor.getInt(CONTENT_ISTRUNCATED_COLUMN);
            mFlagMoved = cursor.getInt(CONTENT_FLAG_MOVED_COLUMN);
            mDstMailBoxKey = cursor.getInt(CONTENT_DST_MAILBOX_KEY_COLUMN);
            mFlagStatus = cursor.getInt(CONTENT_FLAG_STATUS_COLUMN);

            // change@siso_hq for Load More with Inline Images - start
            mIsMimeLoaded = cursor.getInt(CONTENT_ISMIMELOADED_COLUMN);
            // change@siso_hq for Load More with Inline Images - end

            int flags = cursor.getInt(CONTENT_SMIME_FLAGS);
            setSmimeFlags(flags);
            mEncryptionAlgorithm = cursor.getInt(CONTENT_ENCRYPTION_ALGORITHM);

            // change@siso.vpdj Conversation View start
            mConversationId = cursor.getString(CONTENT_CONVERSATION_ID);
            mConversationIndex = cursor.getBlob(CONTENT_CONVERSATION_INDEX);
            // change@siso.vpdj end


            // change@wtl.jrabina EAS 14.0 LastVerbExecution start
            mLastVerb = cursor.getInt(CONTENT_LAST_VERB_COLUMN);
            mLastVerbTime = cursor.getLong(CONTENT_LAST_VERB_TIME_COLUMN);
            // change@wtl.jrabina EAS 14.0 LastVerbExecution end
            // change@wtl.kSingh - SMS Sync - starts
            mMessageType = cursor.getInt(CONTENT_MESSAGE_TYPE);
            mMessageDirty = cursor.getInt(CONTENT_MESSAGE_DIRTY);
            // change@wtl.kSingh - SMS Sync - ends

            mFlagReply = cursor.getInt(CONTENT_FLAG_REPLY) == 1;

            if (cursor.getColumnCount() > CONTENT_TYPE_MESSAGE_COLUMN) { // msc,
                // hanklee,
                // 2010.07.28
                try {
                    mTypeMsg = cursor.getInt(CONTENT_TYPE_MESSAGE_COLUMN);
                } catch (Exception e) {
                    e.printStackTrace();
                    mTypeMsg = 0;
                }
                mSevenMessageKey = cursor.getInt(CONTENT_SEVEN_MESSAGE_KEY_COLUMN);
                mMissingBody = cursor.getInt(CONTENT_MISSING_BODY_COLUMN);
                mMissingHtmlBody = cursor.getInt(CONTENT_MISSING_HTML_BODY_COLUMN);
                mUnkEncoding = cursor.getInt(CONTENT_UNK_ENCODING_COLUMN);
                mSevenAccountKey = cursor.getInt(CONTENT_SEVEN_ACCOUNT_KEY_COLUMN);
            }

            mAccountSchema = cursor.getString(CONTENT_ACCOUNT_SCHEMA_COLUMN);
            mMailboxType = cursor.getInt(CONTENT_MAILBOX_TYPE_COLUMN);

            mSnippet = cursor.getString(CONTENT_SNIPPET_COLUMN);

            mMessageKey = mId; // msc, ksshin, 2010.7.30
            // change@siso.Spoorti - IRM 14.1 support starts
            mIRMTemplateId = cursor.getString(CONTENT_IRM_TEMPLATE_ID);
            mIRMContentExpiryDate = cursor.getString(CONTENT_IRM_CONTENT_EXPIRY_DATE);
            mIRMContentOwner = cursor.getString(CONTENT_IRM_CONTENT_OWNER);
            mIRMLicenseFlag = cursor.getInt(CONTENT_IRM_LICENSE_FLAG);
            mIRMOwner = cursor.getInt(CONTENT_IRM_OWNER);
            mIRMRemovalFlag = cursor.getInt(CONTENT_IRM_REMOVAL_FLAG);
            mIRMTemplateName = cursor.getString(CONTENT_IRM_TEMPLATE_NAME);
            mIRMTemplateDescription = cursor.getString(CONTENT_IRM_TEMPLATE_DESCRIPTION);
            // change@siso.Spoorti - IRM 14.1 support Ends

            mRetrySendTimes = cursor.getInt(CONTENT_RETRY_SEND_TIMES);
            
            try {
                int keyIndex = cursor.getColumnIndex("keyIds");
                mKeyIds= cursor.getString(keyIndex);
            } catch(Exception e) {
                EmailLog.v("PGPKEY_EXCEPTION","mKeyIds not found in projection cursor");
            }
            mSize=cursor.getInt(CONTENT_SIZE_COLUMN);
            //change@siso madhu.dumpa orphanMessage issue start
            mDirtyCommit = cursor.getInt(CONTENT_DIRTY_COMMIT_COLUMN);
            //change@siso madhu.dumpa orphanMessage issue end
            mOpenTime = cursor.getLong(CONTENT_OPEN_TIME_COLUMN);
            mDeleteHidden = cursor.getInt(CONTENT_FLAG_DELETEHIDDEN);
        }

        public boolean update() {
            return false;
        }

        // change@wtl.akulik smime support start
        /**
         * Gets the smime flags calculated from signed and encrypted
         * configuration.
         * 
         * @return smime flag
         */
        public int getSmimeFlags() {
            int flags = 0;
            flags = (mSigned ? 1 : 0) << SIGN_SHIFT;
            flags |= (mEncrypted ? 1 : 0) << ENCRYPT_SHIFT;
            flags |= (mProcessed ? 1 : 0) << PROCESS_SHIFT;
            flags |= (mVerified ? 1 : 0) << VERIFY_SHIFT;
            flags |= (mPGP ? 1:0)<< PGP_SHIFT;
            return flags;
        }

        /**
         * Sets the appropriate message's flags depending on the decoded input.
         * 
         * @param flags flags to be decoded and set
         */
        public void setSmimeFlags(int flags) {
            mSigned = (flags & (1 << SIGN_SHIFT)) != 0;
            mEncrypted = (flags & (1 << ENCRYPT_SHIFT)) != 0;
            mProcessed = (flags & (1 << PROCESS_SHIFT)) != 0;
            mVerified = (flags & (1 << VERIFY_SHIFT)) != 0;
            mPGP = (flags & (1<< PGP_SHIFT)) != 0;
        }

        // change@wtl.akulik smime support end
        /*
         * Override this so that we can store the Body first and link it to the
         * Message Also, attachments when we get there... (non-Javadoc)
         * @see
         * com.samsung.android.email.ui.provider.EmailContent#save(android.content.Context)
         */
        @Override
        public Uri save(Context context) {

            boolean doSave = !isSaved();

            // This logic is in place so I can (a) short circuit the expensive
            // stuff when
            // possible, and (b) override (and throw) if anyone tries to call
            // save() or update()
            // directly for Message, which are unsupported.
            if (mText == null && mHtml == null && mTextReply == null && mHtmlReply == null
                    && (mAttachments == null || mAttachments.isEmpty())) {
                if (doSave) {
                    return super.save(context);
                } else {
                    // Call update, rather than super.update in case we ever
                    // override it
                    if (update(context, toContentValues()) == 1) {
                        return getUri();
                    }
                    return null;
                }
            }

            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            addSaveOps(ops);
            try {
                ContentProviderResult[] results = context.getContentResolver().applyBatch(
                        AUTHORITY, ops);
                Uri u = null;
                if (doSave) {
                    u = results[0].uri;
                    mId = Long.parseLong(u.getPathSegments().get(1));
                    EmailLog.d(TAG, "Message.save(): new message ID is " + mId);
                }
                saveBodyToFilesIfNecessary(context);


                // If saving, set the mId's of the various saved objects
                if (doSave) {
                    if (mAttachments != null) {
                        int resultOffset = 2;
                        for (Attachment a : mAttachments) {
                            // Save the id of the attachment record
                            u = results[resultOffset++].uri;
                            if (u != null) {
                                a.mId = Long.parseLong(u.getPathSegments().get(1));
                            }
                            a.mMessageKey = mId;
                        }
                    }
                    return u;
                } else {
                    return null;
                }
            } catch (RemoteException e) {
                EmailLog.e(TAG, e.toString());
                // There is nothing to be done here; fail by returning null
            } catch (OperationApplicationException e) {
                EmailLog.e(TAG, e.toString());
                // There is nothing to be done here; fail by returning null
            } catch (Exception e){
                EmailLog.e(TAG, e.toString());
            }
            return null;
        }

        public void saveBodyToFilesIfNecessary(Context context) {
            if(EmailLog.DEBUG)
                EmailLog.d(TAG, "Message.saveBodyToFilesIfNecessary : start, messageId = " + mId + " Subject = " + mSubject);
            else
                EmailLog.d(TAG, "Message.saveBodyToFilesIfNecessary : start, messageId = " + mId);

            if (EmailContentUtils.isFullMessageBodyLoadDisabled()) {
                Log.d(TAG, "Message.saveBodyToFilesIfNecessary: no body files inside container.Skip it.");
                return;
            }
            int flags = Body.restoreFileSaveFlags(context, mId);

            if (mHtml != null && mHtml.length() > Body.MESSAGE_BODY_MAX_SIZE) {
                BodyUtilites.writeHtmlContentToFile(context, mAccountKey, mId, mHtml);
                flags |= Body.FLAGS_HTML_CONTENT_FILE;
            }
            
            if ((mHtml == null || mHtml.length() == 0) && mText != null && mText.length() > Body.MESSAGE_BODY_MAX_SIZE) {
                BodyUtilites.writeTextContentToFile(context, mAccountKey, mId, mText);
                flags |= Body.FLAGS_TEXT_CONTENT_FILE;
            }
            
            if (mTextReply != null && mTextReply.length() > Body.MESSAGE_BODY_MAX_SIZE) {
                BodyUtilites.writeTextReplyToFile(context, mAccountKey, mId, mTextReply);
                flags |= Body.FLAGS_TEXT_REPLY_FILE;
            }
            
            if (mHtmlReply != null && mHtmlReply.length() > Body.MESSAGE_BODY_MAX_SIZE) {
                BodyUtilites.writeHtmlReplyToFile(context, mAccountKey, mId, mHtmlReply);
                flags |= Body.FLAGS_HTML_REPLY_FILE;
            }
            
            if (mIntroText != null && mIntroText.length() > Body.MESSAGE_BODY_MAX_SIZE) {
                BodyUtilites.writeIntroToFile(context, mAccountKey, mId, mIntroText);
                flags |= Body.FLAGS_INTRO_FILE;
            }
            
            if (flags > 0) {
                ContentValues cv = new ContentValues();
                cv.put(BodyColumns.FILE_SAVE_FLAGS, flags);
                Body.updateBodyWithMessageId(context, mId, cv);
                
            }
            EmailLog.d(TAG, "Message.saveBodyToFilesIfNecessary : finish");
        }
        
        public int addBodyPartsContentValues(ContentValues cv) {
            int flags = 0;
            if (cv != null) {
                if (mText != null) {
                    if (mText.length() > Body.MESSAGE_BODY_MAX_SIZE) {
                        cv.put(Body.TEXT_CONTENT, EmailContentUtils.cutText(mText, 
                                Body.MESSAGE_BODY_PART_SIZE_FOR_DB, BodyUtilites.getCutText(), false));
                        // We are saving text content to file if we have no HTML content only
                        if (mHtml == null || mHtml.length() == 0) 
                            flags |= Body.FLAGS_TEXT_CONTENT_FILE;
                    } else {
                        cv.put(Body.TEXT_CONTENT, mText);
                    }
                }
                if (mHtml != null) {
                    if (mHtml.length() > Body.MESSAGE_BODY_MAX_SIZE) {
                        cv.put(Body.HTML_CONTENT, EmailContentUtils.cutText(mHtml, 
                                Body.MESSAGE_BODY_PART_SIZE_FOR_DB, BodyUtilites.getCutText(), true));
                        flags |= Body.FLAGS_HTML_CONTENT_FILE;
                    } else {
                        cv.put(Body.HTML_CONTENT, mHtml);
                    }
                }
                if (mTextReply != null) {
                    if (mTextReply.length() > Body.MESSAGE_BODY_MAX_SIZE) {
                        cv.put(Body.TEXT_REPLY, EmailContentUtils.cutText(mTextReply, 
                                Body.MESSAGE_BODY_PART_SIZE_FOR_DB, BodyUtilites.getCutText(), false));
                        flags |= Body.FLAGS_TEXT_REPLY_FILE;
                    } else {
                        cv.put(Body.TEXT_REPLY, mTextReply);
                    }
                }
                if (mHtmlReply != null) {
                    if (mHtmlReply.length() > Body.MESSAGE_BODY_MAX_SIZE) {
                        cv.put(Body.HTML_REPLY,  EmailContentUtils.cutText(mHtmlReply, 
                                Body.MESSAGE_BODY_PART_SIZE_FOR_DB, BodyUtilites.getCutText(), true));
                        flags |= Body.FLAGS_HTML_REPLY_FILE;
                    } else {
                        cv.put(Body.HTML_REPLY, mHtmlReply);
                    }
                }
                if (mIntroText != null) {
                    if (mIntroText.length() > Body.MESSAGE_BODY_MAX_SIZE) {
                        cv.put(Body.INTRO_TEXT, EmailContentUtils.cutText(mIntroText, 
                                Body.MESSAGE_BODY_PART_SIZE_FOR_DB, BodyUtilites.getCutText(), false));
                        flags |= Body.FLAGS_INTRO_FILE;
                    } else {
                        cv.put(Body.INTRO_TEXT, mIntroText);
                    }
                }
                if (flags > 0 && !EmailContentUtils.isFullMessageBodyLoadDisabled()) {
                    cv.put(Body.FILE_SAVE_FLAGS, flags);
                }
            }
            
            return flags;
        }
        
        // syoungsu.seo@samsung.com : update logsprovider - start
        // record the first OPS index of each message to applybatch
        private int addSaveOpsInternal(ContentProviderOperation.Builder b, 
                ArrayList<ContentProviderOperation> ops, boolean mp) {
            // syoungsu.seo@samsung.com : update logsprovider - end
            // First, save the message
            int nOPSIndex = ops.size();
            // Generate the snippet here, before we create the CPO for Message
            // jk0112.lee, refer Html text first because sometimes text field
            // has html tag
            if (mHtml != null) {
                mSnippet = Snippet.fromHtmlText(mHtml);
            } else if (mText != null) {
                mSnippet = Snippet.fromPlainText(mText);
            }
            ops.add(b.withValues(toContentValues()).build());

            // Create and save the body
            ContentValues cv = new ContentValues();
            addBodyPartsContentValues(cv);
            
            if (mSourceKey != 0) {
                cv.put(Body.SOURCE_MESSAGE_KEY, mSourceKey);
            }
            b = ContentProviderOperation.newInsert(mp ? Body.CONTENT_URI_MULTI : Body.CONTENT_URI);
            b.withValues(cv);
            ContentValues backValues = new ContentValues();
            int messageBackValue = ops.size() - 1;
            backValues.put(Body.MESSAGE_KEY, messageBackValue);
            ops.add(b.withValueBackReferences(backValues).build());

            // Create the attaachments, if any
            if (mAttachments != null) {
                for (Attachment att : mAttachments) {
                    ops.add(ContentProviderOperation.newInsert(mp ? Attachment.CONTENT_URI_MULTI : Attachment.CONTENT_URI)
                            .withValues(att.toContentValues())
                            .withValueBackReference(Attachment.MESSAGE_KEY, messageBackValue)
                            .build());
                }
            }
            // syoungsu.seo@samsung.com : update logsprovider - start
            return nOPSIndex;
            // syoungsu.seo@samsung.com : update logsprovider - end
        }
        
        public int addSaveOps(ArrayList<ContentProviderOperation> ops) {
            return addSaveOps(ops, false);
        }
        
        public int addSaveOps(ArrayList<ContentProviderOperation> ops, boolean mp) {
            Uri uri = mp ? CONTENT_URI_MULTI : CONTENT_URI;
            ContentProviderOperation.Builder b = ContentProviderOperation.newInsert(uri);
            return addSaveOpsInternal(b, ops, mp);
            
        }

        /*
         * Compute estimated body size bazed on parsed fields.
         * Required for EAS 2003 servers
         * Can be used during message sync only. 
         */
        public int getComputedBodySize() {
            int resultSize = 0;
            
            // Body.MESSAGE_BODY_PART_SIZE_FOR_DB size will be stored in DB even if big message body will be saved to file
            if (mText != null) {
                resultSize += (mText.length() > Body.MESSAGE_BODY_MAX_SIZE) ? Body.MESSAGE_BODY_PART_SIZE_FOR_DB : mText.length();
            }
            if (mHtml != null) {
                resultSize += (mHtml.length() > Body.MESSAGE_BODY_MAX_SIZE) ? Body.MESSAGE_BODY_PART_SIZE_FOR_DB : mHtml.length();
            } 
            if (mHtmlReply != null) {
                resultSize += (mHtmlReply.length() > Body.MESSAGE_BODY_MAX_SIZE) ? Body.MESSAGE_BODY_PART_SIZE_FOR_DB : mHtmlReply.length();
            }
            if (mTextReply != null) {
                resultSize += (mTextReply.length() > Body.MESSAGE_BODY_MAX_SIZE) ? Body.MESSAGE_BODY_PART_SIZE_FOR_DB : mTextReply.length();
            }
            if (mIntroText != null) {
                resultSize += (mIntroText.length() > Body.MESSAGE_BODY_MAX_SIZE) ? Body.MESSAGE_BODY_PART_SIZE_FOR_DB : mIntroText.length();
            }
            
            EmailLog.d(TAG, "getComputedBodySize() resultSize = " + resultSize);
            return resultSize;
        }
        // P5_Porting_Email_Block_Start - worked by minsumr.kim
        public static String getThreadNameFromSubject(String subject) {

            String threadName = null;

            if (TextUtils.isEmpty(subject)) {
                threadName = "";
            } else {
                try {
                    int iStartStr = subject.lastIndexOf(':');
                    if (iStartStr < 0) {
                        threadName = subject.trim();
                    } else {
                        threadName = subject.substring(iStartStr + 1).trim();
                    }
                } catch (Exception e) {
                    threadName = "";
                }
            }

            return threadName;
        }

        public static long getThreadIdFromSubject(String subject, long accountKey) {
            long threadId = 0;
            if (TextUtils.isEmpty(subject)) {
            	//given randomId
                threadId = System.currentTimeMillis();
                
            } else {
                String threadName = getThreadNameFromSubject(subject+ "_" +String.valueOf(accountKey));
                threadId = ((long) threadName.hashCode() << 12) | (threadName.length() & 0xFFF);
            }
            return threadId;
        }

        // change@wtl.rprominski smime support start
        public void addUpdateAndDeleteOps(ArrayList<ContentProviderOperation> ops, Uri p7mAttUri, Context ctx) {
            if(p7mAttUri == null)
                EmailLog.d(TAG, "Uri is null");

            ContentResolver resolver = ctx.getContentResolver();
            String[] prj = new String[] {
                MessageColumns.ID
            };
            String selection = BodyColumns.MESSAGE_KEY + " = " + Long.toString(mId);
            Cursor bodycursor = resolver.query(Body.CONTENT_URI, prj, selection, null, null);
            Uri bodyToUpdateUri = null;
            if (bodycursor != null) {
                if (bodycursor.moveToNext()) {
                    long id = bodycursor.getInt(0);
                    if (id > 0) {
                        bodyToUpdateUri = ContentUris.withAppendedId(Body.CONTENT_URI, id);
                    }
                }
                bodycursor.close();
            }

            // select attachement to be deleted
            prj = new String[] {
                AttachmentColumns.ID
            };
            selection = AttachmentColumns.MESSAGE_KEY + " = " + Long.toString(mId);
            Cursor attachmentCursor = resolver.query(Attachment.CONTENT_URI, prj, selection, null,
                    null);
            
            if (attachmentCursor != null) {
                while (attachmentCursor.moveToNext()) {
                    Uri attachmentToDelete = null;
                    long id = attachmentCursor.getInt(0);
                    if (id > 0) {
                        attachmentToDelete = ContentUris.withAppendedId(Attachment.CONTENT_URI, id);
                    if (attachmentToDelete != null) {
                        ContentProviderOperation.Builder d = ContentProviderOperation
                                .newDelete(attachmentToDelete);
                        ops.add(d.build());
                    }
                    }
                }
                attachmentCursor.close();
            }

            Uri messageUpdateUri = ContentUris.withAppendedId(mBaseUri, mId);
            ContentProviderOperation.Builder u = ContentProviderOperation
                    .newUpdate(messageUpdateUri);

            ops.add(u.withValues(toContentValues()).build());

            // update the body
            ContentValues cv = new ContentValues();
            if (mText != null && mText.length() > Body.MESSAGE_BODY_MAX_SIZE) {
                cv.put(Body.TEXT_CONTENT, EmailContentUtils.cutText(mText, 
                        Body.MESSAGE_BODY_PART_SIZE_FOR_DB, BodyUtilites.getCutText(), false));
            } else {
                cv.put(Body.TEXT_CONTENT, mText);
            }
            
            if (mHtml != null && mHtml.length() > Body.MESSAGE_BODY_MAX_SIZE) {
                cv.put(Body.HTML_CONTENT, EmailContentUtils.cutText(mHtml, 
                        Body.MESSAGE_BODY_PART_SIZE_FOR_DB, BodyUtilites.getCutText(), true));
            } else {
                cv.put(Body.HTML_CONTENT, mHtml);
            }
            
            if (mTextReply != null) {
                if (mTextReply.length() > Body.MESSAGE_BODY_MAX_SIZE) {
                    cv.put(Body.TEXT_REPLY, EmailContentUtils.cutText(mTextReply, 
                            Body.MESSAGE_BODY_PART_SIZE_FOR_DB, BodyUtilites.getCutText(), false));
                } else {
                    cv.put(Body.TEXT_REPLY, mTextReply);
                }
            }
            if (mHtmlReply != null) {
                if (mHtmlReply.length() > Body.MESSAGE_BODY_MAX_SIZE) {
                    cv.put(Body.HTML_REPLY,  EmailContentUtils.cutText(mHtmlReply, 
                            Body.MESSAGE_BODY_PART_SIZE_FOR_DB, BodyUtilites.getCutText(), true));
                } else {
                    cv.put(Body.HTML_REPLY, mHtmlReply);
                }
            }
            if (mIntroText != null) {
                if (mIntroText.length() > Body.MESSAGE_BODY_MAX_SIZE) {
                    cv.put(Body.INTRO_TEXT, EmailContentUtils.cutText(mIntroText, 
                            Body.MESSAGE_BODY_PART_SIZE_FOR_DB, BodyUtilites.getCutText(), false));
                } else {
                    cv.put(Body.INTRO_TEXT, mIntroText);
                }
            }

            if (bodyToUpdateUri != null) {
                u = ContentProviderOperation.newUpdate(bodyToUpdateUri);
            } else {
                u = ContentProviderOperation.newInsert(Body.CONTENT_URI);
            }
            ops.add(u.withValues(cv).build());

            if (mAttachments != null) {
                for (Attachment att : mAttachments) {
                    ContentValues attContentValues = att.toContentValues();
                    attContentValues.put(AttachmentColumns.MESSAGE_KEY, mId);
                    ops.add(ContentProviderOperation.newInsert(Attachment.CONTENT_URI)
                            .withValues(attContentValues).build());
                }
            }
        }

        /**
         * @param context
         * @param messageId the id of message
         * @return the mailbox corresponding to the given messageId, or -1 if
         *         not found.
         */
        public static long getMailboxId(Context context, long messageId) {
            Cursor c = null;
            try {
                c = context.getContentResolver().query(EmailContent.Message.CONTENT_URI,
                        MESSAGEID_TO_MAILBOXID_PROJECTION, EmailContent.RECORD_ID + "=?",
                        new String[] {
                            Long.toString(messageId)
                        }, null);
                if(c != null) {
                    return c.moveToFirst() ? c.getLong(MESSAGEID_TO_MAILBOXID_COLUMN_MAILBOXID) : -1;
                } else {
                    return -1;
                }
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        }

        
        public static long getAccountId(Context context, long messageId) {
            Cursor c = null;
            if ( messageId < 0 ) {
                return -1;
            }
            try {
                c = context.getContentResolver().query(EmailContent.Message.CONTENT_URI,
                        MESSAGEID_TO_ACCOUNTID_PROJECTION, EmailContent.RECORD_ID + "=?",
                        new String[] {
                            Long.toString(messageId)
                        }, null);
                
                if(c != null) {
                    return c.moveToFirst() ? c.getLong(MESSAGEID_TO_ACCOUNTID_COLUMN_ACCOUNTID) : -1;
                } else {
                    return -1;
                }
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        }
        
        public static int getUnreadCountByMailboxType(Context context) {
            return count(context, Message.CONTENT_URI, UNREAD_SELECTION, null);
        }
        
        public static int getAllUnreadCount(Context context) {
            return count(context, Message.CONTENT_URI, UNREAD_SELECTION, null);
        }
        
        public static int getAllMailboxUnreadCount(Context context) {
            return count(context, Message.CONTENT_URI, ALL_UNREAD_SELECTION, null);
        }

        public static int getUnreadCountByAccountAndMailboxType(
                Context context, long accountId) {
            return count(context, Message.CONTENT_URI, ACCOUNT_KEY_SELECTION
                    + " AND " + UNREAD_SELECTION,
                    new String[] { String.valueOf(accountId) });
        }

        public static int getUnreadCountByAccount(Context context, long accountId) {
            return count(context, Message.CONTENT_URI, ACCOUNT_KEY_SELECTION
                    + " AND " + UNREAD_SELECTION,
                    new String[] { String.valueOf(accountId) });
        }
        
        public static int getMessageCountByMailboxId(Context context, long id) {
            return count(context, Message.CONTENT_URI, MAILBOX_KEY_SELECTION,
                    new String[] { String.valueOf(id) });
        }

        /**
         * @return number of favorite (starred) messages throughout all
         *         accounts.
         */
        public static int getFavoriteMessageCount(Context context) {
            return count(context, Message.CONTENT_URI, ALL_FAVORITE_SELECTION, null);
        }
        
        public static int getFavoriteUnreadMessageCount(Context context) {
            return count(context, Message.CONTENT_URI, ALL_FAVORITE_UNREAD_SELECTION, null);
        }
        
        public static int getFravoriteOrFlagedMessageCount(Context context) {
        	return count(context, Message.CONTENT_URI, ALL_FAVORITE_OR_FLAGED_SELECTION, null);
        }
        
        public static int getRecentlyMessageCount(Context context) {
            int count = count(context, Message.CONTENT_URI, RECENTLY_SELECTION, null );
            
                return count;
        }
        public static int getRecentlyUnreadMessageCount(Context context) {
            int count = count(context, Message.CONTENT_URI, RECENTLY_UNREAD_SELECTION, null);
                return count;
        }

        // w9697.lee add Starred and Flagged / Flagged
        public static int getStarredFlaggedUnreadMessageCount(Context context) {
            return count(context, Message.CONTENT_URI, ALL_STARRED_AND_FLAGGED_UNREAD_SELECTION , null);
            
        }
        
        public static int getFlaggedUnreadMessageCount(Context context) {
            return count(context, Message.CONTENT_URI, ALL_FLAGGED_UNREAD_SELECTION , null);
            
        }
        
        public static int getFlaggedMessageCount(Context context) {
            return count(context, Message.CONTENT_URI, ALL_FLAGGED_SELECTION , null);
            
        }

        /**
         * @return number of favorite (starred) messages for an account
         */
        public static int getFavoriteMessageCount(Context context, long accountId) {
            return count(context, Message.CONTENT_URI, ACCOUNT_FAVORITE_SELECTION, new String[] {
                Long.toString(accountId)
            });
        }

      //change@k1001.kim - add vip inbox 
        public static int getVipMessageCount(Context context, String vipSelection) {
            return count(context, Message.CONTENT_URI, vipSelection, null);
        }
        
        
        
        /**
         * @return the latest messages on an account.
         */
        public static Message getLatestAllAccountIncomingMessage(Context context) {
            Cursor c = null;
            try {
                c = context.getContentResolver().query(
                        Message.CONTENT_URI_LIMIT_1,
                        // ++yuseung9.kim, 2011.03.31, Quick Reply
                        // Combined Email - Modify
                        // 20110405 - MASONE
                        Message.CONTENT_PROJECTION, All_LATEST_INCOMING_MESSAGE_SELECTION,
                        null,
                        EmailContent.MessageColumns.TIMESTAMP + " DESC");
                if (c != null && c.moveToFirst()) {
                    Message m = new Message();
                    m.restore(c);
                    return m;
                }
            } catch (Exception e) {
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
            return null; // not found;
        }    

        /**
         * @return the latest messageId with attached file on an account.
         */
        public static Message getLatestIncomingAttachedMessage(Context context, Long accountId) {
            Cursor c = null;
            try {
                c = context.getContentResolver().query(
                        Message.CONTENT_URI_LIMIT_1,
                        Message.CONTENT_PROJECTION, 
                        LATEST_INCOMING_MESSAGE_SELECTION + " AND " +  MessageColumns.FLAG_ATTACHMENT + "= ?",
                        new String[] { Long.toString(accountId), "1"}, 
                        EmailContent.MessageColumns.TIMESTAMP + " DESC");
                if (c != null && c.moveToFirst()) {
                    Message m = new Message();
                    m.restore(c);
                    return m;
                }
            } catch (Exception e) {
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
            return null; // not found;
        }

        public static Message[] getNIncomingMessage(Context context, int count) {
            EmailLog.d(TAG, "getNIncomingMessage is called. count is " + count);
            
            Cursor c = null;
            try {
                c = context.getContentResolver().query(
                        Message.CONTENT_URI_LIMIT_2,
                        Message.CONTENT_PROJECTION, null,
                      null, EmailContent.MessageColumns.TIMESTAMP + " DESC");
                
                if(c != null) {
                    Message[] messages = new Message[count];
                    
                    for (int i = 0; i < count; ++i) {
                        c.moveToNext();
                        
                        messages[i] = getContent(c, Message.class);
                    }
                    return messages;
                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        }
        
        public static long getKeyColumnLong(Context context, long messageId, String column) {
            String[] columns = EmailContentUtils.getRowColumns(context, Message.CONTENT_URI, messageId,
                    column);
            if (columns != null && columns[0] != null) {
                return Long.parseLong(columns[0]);
            }
            return -1;
        }
        
        public static int getCount(Context context, String selection, String [] selectionArgs) {
            int count = 0;
            Cursor c = null;
            try {
                c = context.getContentResolver().query(CONTENT_URI, 
                        new String[] {"COUNT(*)"}, selection, selectionArgs, null);
                if (c != null && c.moveToFirst()) {
                    count = c.getInt(0);
                }
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
            return count;
        }

        // adapter_porting
        public static Message restoreMessageWhere(Context context, String where) {        
            return restoreMessageWhere(context, where, null);
        }
        
        public static Message restoreMessageWhere(Context context, String selection, 
                String[] selectionArgs) {
            return restoreMessageWhere(context, selection, selectionArgs, null);
        }
        
        public static Message restoreMessageWhere(Context context, String selection,
                String[] selectionArgs, String sort) {
             if (context == null) {
                 // throw new NullPointerException("context");
                 return null;
             }
             ContentResolver resolver = context.getContentResolver();
             if (resolver == null) {
                 // throw new NullPointerException("resolver");
                 return null;
             }
             Cursor c = null;
             try {
                 c = resolver.query(Message.CONTENT_URI, Message.CONTENT_PROJECTION, selection, 
                         selectionArgs, sort);
                 if (c != null && c.moveToFirst()) {
                     return getContent(c, Message.class);
                 } else {
                     return null;
                 }
             } catch (Exception e) {
                 return null;
             } finally {
                 if (c != null && !c.isClosed())
                     c.close();
             }
        }

        public static Message[] restoreMessagesWhere(Context context, String where) {        
            return restoreMessagesWhere(context, where, null);
        }

        public static Message[] restoreMessagesWhere(Context context, String selection, 
                String[] selectionArgs) {
            return restoreMessagesWhere(context, selection, selectionArgs, null);
        }

        public static Message[] restoreMessagesWhere(Context context, String selection,
                String[] selectionArgs, String sort) {
             if (context == null) {
                 // throw new NullPointerException("context");
                 return null;
             }
             ContentResolver resolver = context.getContentResolver();
             if (resolver == null) {
                 // throw new NullPointerException("resolver");
                 return null;
             }
             Cursor c = null;
             try {
                 c = resolver.query(Message.CONTENT_URI, Message.CONTENT_PROJECTION, selection, 
                         selectionArgs, sort);
                 
                 if(c != null) {
                     int count = c.getCount();
                     Message[] messages = new Message[count];
                     for (int i = 0; i < count; ++i) {
                         c.moveToNext();
                         messages[i] = getContent(c, Message.class);
                         if (messages[i] != null) 
                             messages[i].updateBodyFlags(context);
                     }
                     return messages;
                 } else {
                     return null;
                 }
             } catch (Exception e) {
                 EmailLog.e(TAG, "Exception in restoreMessagesWhere", e);
                 return null;
             } finally {
                 if (c != null && !c.isClosed())
                     c.close();
             }
        }

        // SncAdapter Porting
        /**
         * Utility api to get the message object from the local Store based on
         * the message Uid.This is different from the normal
         * restoreMessageWithID in the sense that, we get the vanished responses
         * during the QResync where we don't have the details related the
         * account and mailbox.Also there are chances that passes Uid can clash
         * with the Uid of a non-SNC account.
         * 
         * @param uId message Uid.
         */
        public static EmailContent.Message restoreUnifiedMessageWithUid(Context context, long uId,
                boolean isTrash) {
            EmailLog.i(TAG, "restoreUnifiedMessageWithUid " + uId);
            EmailContent.Message localMessage = null;
            Cursor c = null;
            ArrayList<EmailContent.Message> localMsgList = new ArrayList<EmailContent.Message>();

            try {
                c = context.getContentResolver().query(EmailContent.Message.CONTENT_URI,
                        EmailContent.Message.CONTENT_PROJECTION, SyncColumns.SERVER_ID + "=?",
                        new String[] {
                            String.valueOf(uId)
                        }, null);

                if (c == null) {
                    return localMessage;
                }

                while (c.moveToNext()) {
                    EmailContent.Message message = null;
                    EmailLog.v(TAG, "Initializing msg obj for " + String.valueOf(uId));
                    message = EmailContent.getContent(c, EmailContent.Message.class);
                    if(message != null) {
                        Long acctId = message.mAccountKey;
                        if (EmailContent.isSNCAccount(context, acctId)) {
                            localMsgList.add(message);
                        }
                    }
                }
            } catch (Exception e) {
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }

            // After filter all the message table entry for account type SNC the
            // list
            // size will close down to the size-of 2(Unified + Trash)
            for (EmailContent.Message message : localMsgList) {
                EmailContent.Mailbox mailbox = null;
                mailbox = EmailContent.Mailbox.restoreMailboxWithId(context, message.mMailboxKey);
                if (mailbox != null) {
                    if (isTrash && mailbox.mType != Mailbox.TYPE_TRASH) {
                        continue;
                    }
                    localMessage = message;
                }
            }

            return localMessage;
        }

        public static Set<Long> getMessagesForThreads(Context context, Set<Long> threadIds, String selection) {
            HashSet<Long> set = new HashSet<Long>();
            if (context == null || threadIds == null || threadIds.size() == 0) {
                return set;
            }
            StringBuilder threadIdString = new StringBuilder();
            for (long id : threadIds) {
                if (threadIdString.length() > 0) {
                    threadIdString.append(",");
                }
                threadIdString.append(id);
            }
            String where = Message.THREAD_ID + " IN (" + threadIdString + ")";
            if(!TextUtils.isEmpty(selection)){
                where += " AND " + selection; 
            }
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(Message.CONTENT_URI, new String[] {
                        MessageColumns.ID
                }, where, null, null);
                if (cursor == null) {
                    return set;
                }
                while (cursor.moveToNext()) {
                    set.add(cursor.getLong(0));
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return set;
        }

        @Override
        public String toString() {
            return "[" + mId + "] [" + mDisplayName + "," + mTimeStamp + "," + mSubject + "]\n"
                    + "[Flags: " + mFlagRead + "," + mFlagLoaded + "," + mFlagFavorite + ","
                    + mFlagAttachment + "," + mFlags + "]\n" + "[Ids: " + mServerId + ","
                    + mServerTimeStamp + "," + mClientId + "]\n" + "[Key: " + mMessageId + ","
                    + mMailboxKey + "," + mAccountKey + "]\n" + "[Header: " + mFrom + "," + mTo
                    + "," + mCc + "," + mBcc + "," + mReplyTo + "," + mMeetingInfo + "]\n"
                    + "[Thread: " + mThreadId + "," + mThreadName + "]\n" + "[Imp: " + mImportance
                    + "]\n" + "[Mime: " + mEncryptionAlgorithm + "," + mEncrypted + "," + mSigned
                    + "," + mProcessed + "," + mVerified + "]\n" + mConversationId;
        }
    }

    // change@siso.Spoorti - IRM 14.1 support start
    public interface IRMTemplateColumns {
        public static final String IRM_TEMPLATE_ID = "IRMTemplateId";

        public static final String IRM_TEMPLATE_NAME = "IRMTemplateName";

        public static final String IRM_TEMPLATE_DESCRIPTION = "IRMTemplateDescription";

        public static final String IRM_ACCOUNT_KEY = "AccountKey";
    }

    public static final class IRMTemplate extends EmailContent implements IRMTemplateColumns {

        public static final String TABLE_NAME = "IRMTemplate";

        // To refer to a specific message, use
        // ContentUris.withAppendedId(CONTENT_URI, id)
        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI + "/IRMTemplate");

        public static final int CONTENT_IRM_TEMPLATE_ID_COLUMN = 0;

        public static final int CONTENT_IRM_TEMPLATE_NAME_COLUMN = 1;

        public static final int CONTENT_IRM_TEMPLATE_DESCRIPTION_COLUMN = 2;

        public static final int CONTENT_IRM_ACCOUNT_KEY_COLUMN = 3;

        public String mIRMTemplateId;

        public String mIRMTemplateName;

        public String mIRMTemplateDescription;

        public int mIRMAccountKey;

        public static final String[] CONTENT_PROJECTION = new String[] {
                RECORD_ID, IRMTemplateColumns.IRM_TEMPLATE_ID,
                IRMTemplateColumns.IRM_TEMPLATE_NAME, IRMTemplateColumns.IRM_TEMPLATE_DESCRIPTION,
                IRMTemplateColumns.IRM_ACCOUNT_KEY
        };

        public IRMTemplate() {
            super();
            mBaseUri = CONTENT_URI;
        }

        @Override
        public ContentValues toContentValues() {
            ContentValues values = new ContentValues();
            values.put(IRMTemplateColumns.IRM_ACCOUNT_KEY, mIRMAccountKey);
            values.put(IRMTemplateColumns.IRM_TEMPLATE_DESCRIPTION, mIRMTemplateDescription);
            values.put(IRMTemplateColumns.IRM_TEMPLATE_ID, mIRMTemplateId);
            values.put(IRMTemplateColumns.IRM_TEMPLATE_NAME, mIRMTemplateName);
            return values;
        }

        /**
         * Restore a IRMTemplate from the database, given its unique id
         * 
         * @param context
         * @param id
         * @return the instantiated IRMTemplate
         */
        public static IRMTemplate restoreIRMTemplateWithId(Context context, long id) {
            Uri u = ContentUris.withAppendedId(EmailContent.IRMTemplate.CONTENT_URI, id);
            Cursor c = null;
            try {
                c = context.getContentResolver().query(u, IRMTemplate.CONTENT_PROJECTION, null,
                        null, null);

                if (c != null && c.moveToFirst()) {
                    return getContent(c, IRMTemplate.class);
                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        }

        @Override
        public void restore(Cursor cursor) {
            mBaseUri = CONTENT_URI;
            mIRMAccountKey = cursor.getInt(CONTENT_IRM_TEMPLATE_ID_COLUMN);
            mIRMTemplateDescription = cursor.getString(CONTENT_IRM_TEMPLATE_DESCRIPTION_COLUMN);
            mIRMTemplateId = cursor.getString(CONTENT_IRM_TEMPLATE_ID_COLUMN);
            mIRMTemplateName = cursor.getString(CONTENT_IRM_TEMPLATE_NAME_COLUMN);
        }

    }


    // change@wtl.cpillers end
    // P5_Porting_Email_Block_End - worked by minsumr.kim
    // change@siso.saritha Resolverecipient Information start
    public interface RecipientInformationColumns {
        public static final String ID = "_id";

        public static final String ACCOUNTKEY = "accountkey";

        public static final String SERVERID = "server_id";

        public static final String EMAILADDRESS = "email_address";

        public static final String FILEAS = "fileas";

        public static final String ALIAS = "alias";

        public static final String WEIGHTEDRANK = "weightedrank";

    }

    public static final class RecipientInformationCache extends EmailContent implements
            RecipientInformationColumns {

        public static final String TABLE_NAME = "RecipientInformation";

        // To refer to a specific followup flag, use
        // ContentUris.withAppendedId(CONTENT_URI, id)
        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI
                + "/recipientInformation");

        public static final Uri CONTENT_FILTER_URI = Uri.withAppendedPath(CONTENT_URI, "filter");

        public static final int CONTENT_ID_COLUMN = 0;

        public static final int CONTENT_ACCOUNTKEY_COLUMN = 1;

        public static final int CONTENT_SERVER_ID_COLUMN = 2;

        public static final int CONTENT_EMAIL_ADDRESS_COLUMN = 3;

        public static final int CONTENT_FILEAS = 4;

        public static final int CONTENT_ALIAS = 5;

        public static final int CONTENT_WEIGHTEDRANK = 6;
        
        public static final String[] CONTENT_PROJECTION = new String[] {
            RecipientInformationColumns.ID,
            
            RecipientInformationColumns.ACCOUNTKEY,

            RecipientInformationColumns.SERVERID,

            RecipientInformationColumns.EMAILADDRESS,

            RecipientInformationColumns.FILEAS, 
            
            RecipientInformationColumns.ALIAS,
            
            RecipientInformationColumns.WEIGHTEDRANK
    };
        

        public String riServerId;

        public long riAccountKey;

        public long riMailboxKey;

        public String riEmailAddress;

        public String riFileAs;

        public String riAlias;

        public String riWeightedRank;

        public RecipientInformationCache() {
            mBaseUri = CONTENT_URI;
        }

        @Override
        public ContentValues toContentValues() {
            ContentValues cValues = new ContentValues();
            cValues.put(RecipientInformationColumns.SERVERID, riServerId);
            cValues.put(RecipientInformationColumns.ACCOUNTKEY, riAccountKey);
            cValues.put(RecipientInformationColumns.EMAILADDRESS, riEmailAddress);
            cValues.put(RecipientInformationColumns.FILEAS, riFileAs);
            cValues.put(RecipientInformationColumns.ALIAS, riAlias);
            cValues.put(RecipientInformationColumns.WEIGHTEDRANK, riWeightedRank);

            return cValues;
        }

        public void addSaveRiOps(ArrayList<ContentProviderOperation> ops) {
            // First, save the message
            ContentProviderOperation.Builder b = ContentProviderOperation
                    .newInsert(RecipientInformationCache.CONTENT_URI);
            ops.add(b.withValues(toContentValues()).build());
        }

        @Override
        public void restore(Cursor cursor) {
            mBaseUri = CONTENT_URI;
            mId = cursor.getLong(CONTENT_ID_COLUMN);
            riServerId = cursor.getString(CONTENT_SERVER_ID_COLUMN);
            riAccountKey = cursor.getLong(CONTENT_ACCOUNTKEY_COLUMN);
            riEmailAddress = cursor.getString(CONTENT_EMAIL_ADDRESS_COLUMN);
            riFileAs = cursor.getString(CONTENT_FILEAS);
            riAlias = cursor.getString(CONTENT_ALIAS);
            riWeightedRank = cursor.getString(CONTENT_WEIGHTEDRANK);
        }
        
        public static RecipientInformationCache restoreAccountWithId(Context context, long id) {
            Uri u = ContentUris.withAppendedId(RecipientInformationCache.CONTENT_URI, id);
            Cursor c = null;
            try {
                c = context.getContentResolver().query(u, RecipientInformationCache.CONTENT_PROJECTION, null, null,
                        null);

                if (c == null) {
                    EmailLog.e(TAG, "Cursor is NULL. Query failed");
                    return null;
                }
                try {
                    if (c.moveToFirst()) {
                        return getContent(c, RecipientInformationCache.class);
                    } else {
                        EmailLog.e(TAG, "Cursor is empty. Account does not exist id:" + id);
                        return null;
                    }
                } catch (NullPointerException ne) { // Memory Full Exception
                    ne.printStackTrace();
                    return null;
                } finally {
                    if (c != null && !c.isClosed())
                        c.close();
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                EmailLog.e(TAG, "RuntimeException in restoreAccountWithId", e);
                return null;
            }
        }

    }

    // change@siso.saritha Resolverecipient Information end

    public interface AccountColumns {
        public static final String ID = "_id";

        // The display name of the account (user-settable)
        public static final String DISPLAY_NAME = "displayName";

        // The email address corresponding to this account
        public static final String EMAIL_ADDRESS = "emailAddress";

        // A server-based sync key on an account-wide basis (EAS needs this)
        public static final String SYNC_KEY = "syncKey";

        // The default sync lookback period for this account
        public static final String SYNC_LOOKBACK = "syncLookback";

        // The default sync frequency for this account, in minutes
        public static final String SYNC_INTERVAL = "syncInterval";

        // A foreign key into the account manager, having host, login, password,
        // port, and ssl flags
        public static final String HOST_AUTH_KEY_RECV = "hostAuthKeyRecv";

        // (optional) A foreign key into the account manager, having host,
        // login, password, port,
        // and ssl flags
        public static final String HOST_AUTH_KEY_SEND = "hostAuthKeySend";

        // Flags
        public static final String FLAGS = "flags";

        // Default account
        public static final String IS_DEFAULT = "isDefault";

        // Old-Style UUID for compatibility with previous versions
        public static final String COMPATIBILITY_UUID = "compatibilityUuid";

        // User name (for outgoing messages)
        public static final String SENDER_NAME = "senderName";

        // Ringtone
        public static final String RINGTONE_URI = "ringtoneUri";

        // Protocol version (arbitrary string, used by EAS currently)
        public static final String PROTOCOL_VERSION = "protocolVersion";

        // The number of new messages (reported by the sync/download engines
        public static final String NEW_MESSAGE_COUNT = "newMessageCount";

        // Flags defining security (provisioning) requirements of this account
        public static final String SECURITY_FLAGS = "securityFlags";

        // Server-based sync key for the security policies currently enforced
        public static final String SECURITY_SYNC_KEY = "securitySyncKey";

        // Signature to use with this account
        public static final String SIGNATURE = "signature";

        // P5_Porting_Email_Block_Start - worked by minsumr.kim
        // change@siso.vpdj Conversation Text preview
        // Text Preview to use with this account
        public static final String TEXT_PREVIEW_SIZE = "textPreview";

        // change@wtl.jpshu SyncSchedule begin
        public static final String EMAIL_SIZE = "emailsize";
        
        public static final String ROAMING_EMAIL_SIZE = "roamingemailsize";

        public static final String POLICY_KEY = "policyKey";

        public static final String PEAK_DAYS = "peakDays";

        public static final String PEAK_START_MINUTE = "peakStartMinute";

        public static final String PEAK_END_MINUTE = "peakEndMinute";

        public static final String PEAK_SCHEDULE = "peakSchedule";

        public static final String OFF_PEAK_SCHEDULE = "offPeakSchedule";

        public static final String ROAMING_SCHEDULE = "roamingSchedule";

        public static final String IS_PEAK_SCHEDULE_ON = "isPeakScheduleOn";

        // change@wtl.jpshu SyncSchedule end
        // change@wtl.jpshu calendar_sync_lookback
        public static final String CALENDAR_SYNC_LOOKBACK = "calendarSyncLookback";

        // change@siso.vpdj conversation mode setting start
        public static final String CONVERSATION_MODE = "conversationMode";


        // change@wtl.Ksingh Conflict Resolution
        public static final String CONFLICT_RESOLUTION = "conflict";

        // change@wtl.rprominski smime support
        public static final String SMIME_OWN_ENCRYPT_CERT_ALIAS = "smimeOwnCertificateAlias";
        public static final String SMIME_OWN_SIGN_CERT_ALIAS = "smimeOwnSignCertAlias";

        // change@wtl.akulik smime support start
        public static final String SMIME_OPTIONS_FLAGS = "smimeOptionsFlags";

        public static final String SMIME_OPTIONS_SIGN_ALGORITHM = "smimeSignAlgorithm";

        public static final String SMIME_OPTIONS_ENCRYPTION_ALGORITHM = "smimeEncryptionAlgorithm";

        public static final String SMIME_OPTIONS_MESSAGE_SIGN_TYPE = "smimeMsgSignType";

        // change@wtl.akulik smime support end
        // change@wtl.shatekar deviceInformation START
        public static final String DEVICE_INFO_SENT = "deviceInfoSent";

        // change@wtl.shatekar deviceInformation END
        // change@siso.mahsky Allow/Block/Quarantine start
        public static final String DEVICE_BLOCK_TYPE = "deviceBlockedType";

        // change@siso.mahsky Allow/Block/Quarantine end

        // initial sync
        public static final String INIT_SYNC_COMPLETE = "isInitSyncComplete";

        // change@siso.Spoorti IRM support starts
        public static final String IRM_TEMPLATE_TIME_STAMP = "IRMTemplateTimeStamp";

        // change@siso.Spoorti IRM support ends

        // WTL - CBA - Start
        public static final String CBA_CERTIFICATE_ALIAS = "cbaCertificateAlias";

        // WTL - CBA - End

        // SVL start vjeswani
        public static final String MESSAGE_FORMAT = "messageFormat";

        public static final String ACCOUNT_TYPE = "accountType";
        // SVL end vjeswani

        // huijae.lee
        // Settings for local change
        public static final String EAS_LOCAL_CHANGE_SETTING = "easLocalChange";

        // sh2209.cho : add ICS Settings
        public static final String FORWARD_WITH_FILES = "forwardWithFiles";
        public static final String AUTO_DOWNLOAD = "autoDownload";

        public static final String RECENT_MESSAGES = "recentMessages";
        public static final String SHOW_IMAGE = "showImage";

        // Change@siso.vinay Auto retry times start
        public static final String AUTO_RETRY_TIMES = "autoRetryTimes";
        // Change@siso.vinay Auto retry times end
        public static final String DWONLOAD_ON_SCROLL = "downloadOnScroll";
        
        //change@siso.gaurav cancel send.
        public static final String CANCEL_SENDING_MESSAGE_TIMEOUT = "cancelSendingMessageTimeout";

        
        // Change@siso.Naresh Spamfilter Store the Spamfolderif of account
//        public static final String SPAM_FOLDER_KEY  = "spamfolderid";
        // starov.ilya: True if default signature was changed by user. Required to provide default signature translation 
        public static final String IS_SIGNATURE_EDITED = "isSignatureEdited";
        
        public static final String COMPANY_NAME = "companyname";

        //Added to find the IMAP Days based sync support
        public static final String IMAP_DAYS_BASED_SYNC = "imapDaysBasedSync";
        
        public static final String MAILBOXLIST_SYNC_TIME = "mailboxlistSyncTime";
    }

    
    public static final class Account extends EmailContent implements AccountColumns, 
    CarrierValues, AccountValues.SyncTime, AccountValues.SyncSize, Parcelable {
        public static final String TABLE_NAME = "Account";


        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI + "/account");

        public static final Uri ADD_TO_FIELD_URI = Uri.parse(EmailContent.CONTENT_URI
                + "/accountIdAddToField");

        public static final Uri RESET_NEW_MESSAGE_COUNT_URI = Uri.parse(EmailContent.CONTENT_URI
                + "/resetNewMessageCount");

        public static final Uri NOTIFIER_URI = Uri.parse(EmailContent.CONTENT_NOTIFIER_URI + "/account");

        // SVL start vjeswani
        // Type of message format to be downloaded from Server!
        public static final int MESSAGE_FORMAT_TYPE_HTML = 0;

        public static final int MESSAGE_FORMAT_TYPE_TEXT = 1;

        // SVL end vjeswani

        // SncAdapter Porting
        // Email size values!
        public static final int MESSAGE_SIZE_1_5_K = 0;

        public static final int MESSAGE_SIZE_60_K = 1;

        public static final int MESSAGE_SIZE_100_K = 2;
        /* as per VZW july'12 req, 20KB is added */
        public static final int MESSAGE_SIZE_20_K = 3;


        public static final int MESSAGE_ROAMING_SIZE_USE_DEFAULT = 1;
        public static final int MESSAGE_ROAMING_SIZE_1_5_K = 0;
        public static final int MESSAGE_ROAMING_SIZE_20_K = 2;
        public static final int MESSAGE_ROAMING_SIZE_60_K = 3;
        public static final int MESSAGE_ROAMING_SIZE_100_K = 4;

        /* as per VZW Oct 2013 req, "size to retrieve" option is modified */
       

        /**
         * Added to detect the IMAP days based sync support dynamically
         */
        public static final int IMAP_SYNC_INIT = -1;
        public static final int IMAP_COUNT_BASED_SYNC = 0;
        public static final int IMAP_DAYS_BASED_SYNC = 1;

        public static final int IMAP_SYNC_PERIOD_THREE_MONTHS = 90;// 3 months
        public static final int IMAP_SYNC_PERIOD_TWO_WEEKS = 14;// 2 Weeks

        /**
         * Value used by UI to represent "combined view". NOTE: This must be
         * used only by UI, and mustn't be stored in the database. This is
         * defined here to avoid conflict with other pseudo account IDs, if any.
         */
        public static final long ACCOUNT_ID_COMBINED_VIEW = 0x1000000000000000L;
        
        public static final long ACCOUNT_ID_PRIORITY_SENDER_VIEW = 0x1000000000011111L;
        
        public static final long ACCOUNT_ID_RECENTLY_READ_VIEW = 0x1000000001111111L;
        
        public static final long ACCOUNT_ID_FILTER_VIEW = 0x1000000011111111L;
        public static final long ACCOUNT_ID_SINGLE_VIEW = 0x1000000111111111L;
        public static final long ACCOUNT_ID_OTHERS = 0x1000001111111111L;

        public final static int FLAGS_NOTIFY_NEW_MAIL = 1;

        public final static int FLAGS_VIBRATE_ALWAYS = 2;
		
        //public final static int FLAGS_VIBRATE_NEVER = 5;

        public static final int FLAGS_DELETE_POLICY_MASK = 4 + 8;

        public static final int FLAGS_DELETE_POLICY_SHIFT = 2;

        public static final int FLAGS_INCOMPLETE = 1 << 4;

        public static final int FLAGS_SECURITY_HOLD = 1 << 5;

        public static final int FLAGS_VIBRATE_WHEN_SILENT = 1 << 6;

        // public static final int FLAGS_SUPPORTS_SMART_FORWARD = 128;

        public static final int FLAGS_BACKGROUND_ATTACHMENTS = 1 << 8;

        // P5_Porting_Email_Block_Start - worked by minsumr.kim
        // P1TF_BEGIN - HanaTheONE 06/09/10 Added Always Cc/Bcc setting
        public static final int FLAGS_CC_MYSELF = 1 << 9;

        public static final int FLAGS_BCC_MYSELF = 1 << 10;

        // P1TF_END - HanaTheONE 06/09/10 Added Always Cc/Bcc setting
        // change@wtl.kSingh - SMS Sync
        public static final int FLAGS_SMS_SYNC = 1 << 11;

        // Available to sync adapter
        public static final int FLAGS_SYNC_ADAPTER = 1 << 12;

        // temprorary added comment out ; check it ;
        public static final int FLAGS_ADD_SIGNATURE = 1 << 13;

        // Password Expired - Should be cleared after changing Lock Screen PW
        public static final int FLAGS_PASSWORD_EXPIRED = 1 << 14;

        // Recovery Password sent failed
        public static final int FLAGS_RECOVERY_PASSWORD_FAILED = 1 << 15;

        // P5_Porting_Email_Block_End - worked by minsumr.kim
        
        // Check for CBA is enabled by user or not
        public static final int FLAGS_CBAUTHENTICATION_ENABLED = 1 << 16;
        
        // Check for Inbox support or not
        public static final int FLAGS_SUPPORT_INBOX = 1 << 17;

        // This flag is for VZW feature that enables attachement downloads via
        // Wi-Fi only. See function EmailFeature.isDownloadOnlyViaWifiEnabled()
        // for details
        public static final int FLAGS_DOWNLOAD_ONLY_VIA_WIFI = 1 << 18;
        
        public static final int FLAGS_SHOW_IMAGE_INITIAL_SETTING = 1 << 19;
        
        // This flag is for IMAP Forward Flag support. Its stores, whether
        // remote IMAP server supports the "$Forwarded" flag
        public static final int FLAGS_IMAP_FORWARD_FLAG_INIT = 1 << 20;
        public static final int FLAGS_IMAP_FORWARD_FLAG_ENABLED = 1 << 21;
        public static final int FLAGS_IMAP_FORWARD_FLAG_VERIFIED = 1 << 22;
        public static final int FLAGS_IMAP_FORWARD_FLAG_DISABLED = 1 << 23;

        public static final int FLAGS_AUTH_FAILED_HOLD = 1 << 24;

        // This flag is for VZW devices in setupwizard flow.
        // Account security settings will be updated later after finishing the setup wizard mode.
        public static final int FLAGS_UPDATE_SECURITY_POLICIES_LATER = 1 << 25;

        public static final int FLAGS_STICKY_PING_DISABLED = 1 << 26;

        //Flag for the untrusted certificate
        public static final int FLAGS_UNTRUSTED_CERTIFICATE = 1 << 27;

	public static final int DELETE_POLICY_NEVER = 0;
	public static final int DELETE_POLICY_DOWNSYNC = 1;
	public static final int DELETE_POLICY_ON_DELETE = 2;//(UPSYNC_ONLY)
	public static final int DELETE_POLICY_SYNC = 3;




        // P5_Porting_Email_Block_Start - worked by minsumr.kim
        public static final int SYNC_WINDOW_USER = -1;

        public static final int SYNC_WINDOW_ALL_MAILS = -1;

        // change@wtl.jpshu SyncSchedule begin
        
        // Wed Thur Fri xx |

        // 20111104_thanx.kim_Israel
        public static final int DEFAULT_PEAK_DAYS_ISL = 0x1f; // | -- xx Sun Mon
        // |
        // Tue Wed Thur xx |

        public static final int DEFAULT_CONFLICT_RESOLUTION = 1;

        public static final int DEFAULT_CALENDAR_LOOKBACK = 4;

        // change@wtl.jpshu SyncSchedule end
        // change@wtl.fzhang email search begin
        public static final double EX_PROTOCOL_VERSION_2_0 = 2.0;

        public static final double EX_PROTOCOL_VERSION_2_5 = 2.5;

        public static final double EX_PROTOCOL_VERSION_12_0 = 12.0;

        public static final double EX_PROTOCOL_VERSION_12_1 = 12.1;

        // change@wtl.fzhang email search end

        // change@wtl.jpshu calendar_sync_lookback begin
        public static final int CALENDAR_SYNC_WINDOW_ALL = 0;

        public static final int CALENDAR_SYNC_WINDOW_2_WEEKS = 4;

        public static final int CALENDAR_SYNC_WINDOW_1_MONTH = 5;

        public static final int CALENDAR_SYNC_WINDOW_3_MONTH = 6;

        public static final int CALENDAR_SYNC_WINDOW_6_MONTH = 7;

        // SncAdapter Porting
        // Account or Mailbox initial Sync Status
        public static final int INITIAL_SYNC_FAILED = 0;

        public static final int INITIAL_SYNC_SUCCESS = 1;

        public static final int INITIAL_SYNC_IGNORE = 2;

        public static final int INITIAL_SYNC_RESET = 3;
        
        public int mCalendarSyncLookback = 4;

        public int mConflictFlags = 1;

        // change@wtl.shatekar Device Information START
        public static final int FLAG_DEVICE_INFO_SENT = 1;

        public static final int FLAG_EAS_ACTIVATION_SENT_FAIL = 1 << 1;

        // change@wtl.shatekar Device Information END
        // change@wtl.jpshu calendar_sync_lookback end
        // change@wtl.rprominski smime support start
        public String mSmimeOwnEncryptCertAlias;
        public String mSmimeOwnSignCertAlias;

        public static final int CERTTYPE_ENCRYPTION = 1;
        public static final int CERTTYPE_SIGNING = 2;
        // change@wtl.rprominski smime support end

        // WTL - CBA - Start
        public String mCbaCertificateAlias;

        // WTL - CBA - End

        // change@wtl.akulik smime support start
        public boolean mSmimeEncryptAll;

        public boolean mSmimeSignAll;
        
        public boolean mIsDefaultkeyChecked;

        public static final int SMIME_ENCRYPT_ALL_SHIFT = 0;

        public static final int SMIME_SIGN_ALL_SHIFT = 1;

        public int mSmimeSignAlgorithm;

        public int mSmimeEncryptionAlgorithm;
        // change@wtl.akulik smime support end
        public int mSmimeMsgSignType;

        // P5_Porting_Email_Block_End - worked by minsumr.kim
        public String mDisplayName;

        public String mEmailAddress;

        public String mSyncKey;

        public int mSyncLookback;

        public int mSyncInterval;

        public long mHostAuthKeyRecv;

        public long mHostAuthKeySend;

        public int mFlags;

        public boolean mIsDefault; // note: callers should use
        // getDefaultAccountId()

        public String mCompatibilityUuid;

        public String mSenderName;

        public String mRingtoneUri;

        public String mProtocolVersion;

        public int mNewMessageCount;

        public long mSecurityFlags;

        public String mSecuritySyncKey;

        public String mSignature;

        // P5_Porting_Email_Block_Start - worked by minsumr.kim
        public int mTextPreviewSize; // change@siso.vpdj Conversation Text
        // preview

        // change@wtl.jpshu SyncSchedule begin
        public int mEmailSize; // MessageDiff change @siso.gaurav
        
        public int mRoamingEmailSize;

        public int mEmailMessageDiffEnabled; // MessageDiff change @siso.gaurav

        public String[] mEmailRetrieveSizeValues = null;
        
        public String[] mEmailRoamingRetrieveSizeValues = null;

        public String mPolicyKey;

        private SyncScheduleData mSyncScheduleData; 

        public boolean mIsInPeakPeriod;

        // change@wtl.jpshu SyncSchedule end

        // change@wtl.shatekar Device Information START
        public int mDeviceInfoSent;

        // SVL start: vjeswani, sync and connect support
        public int mMessageFormat;

        public int mAccountType;

        // SVL end: vjeswani
        // change@wtl.shatekar Device Information END
        // ===============================================================
        // Seven (2010.07.20)
        // ===============================================================
        public static int DEVICE_IS_ALLOWED = 0;

        public static int DEVICE_IS_BLOCKED = 1;

        public static int DEVICE_IS_QUARANTINED = 2;

        public int mdeviceBlockedType;

        public long mAccountKey;

//        public PolicySet mPolicySet;

        // huijae.lee
        public boolean mEasLocalChangeEnabled;

        // sh2209.cho : add ICS Settings
        public boolean mForwardWithFiles;
        public boolean mAutoDownload;

        public int mRecentMessages;
        public boolean mShowImage;
        public boolean mDownloadOnScroll;

        private boolean mIsSignatureEdited;
        
        // Convenience for creating an account
        public transient HostAuth mHostAuthRecv;

        public transient HostAuth mHostAuthSend;

        // change@siso.Spoorti IRM support starts
        public static String mIRMTemplateTimeStamp;

        // change@siso.Spoorti IRM support ends

        // Might hold the corresponding AccountManager account structure
        public transient android.accounts.Account mAmAccount; // ICS CanAutoSync

        public int mAutoRetryTimes = DEFAULT_AUTORETRYTIMES;// Change@siso.vinay Auto retry times
        
        public long mCancelSendingMessageTimeout = 0; //change@siso.gaurav cancel send.

        private String mCompanyName;

        private int mImapDaysBasedSync = IMAP_SYNC_INIT;
        
        public boolean mInitialSettingShowImage = true;

        public static final int CONTENT_ID_COLUMN = 0;
        public static final int CONTENT_DISPLAY_NAME_COLUMN = 1;
        public static final int CONTENT_EMAIL_ADDRESS_COLUMN = 2;
        public static final int CONTENT_SYNC_KEY_COLUMN = 3;
        public static final int CONTENT_SYNC_LOOKBACK_COLUMN = 4;
        public static final int CONTENT_SYNC_INTERVAL_COLUMN = 5;
        public static final int CONTENT_HOST_AUTH_KEY_RECV_COLUMN = 6;
        public static final int CONTENT_HOST_AUTH_KEY_SEND_COLUMN = 7;
        public static final int CONTENT_FLAGS_COLUMN = 8;
        public static final int CONTENT_IS_DEFAULT_COLUMN = 9;
        public static final int CONTENT_COMPATIBILITY_UUID_COLUMN = 10;
        public static final int CONTENT_SENDER_NAME_COLUMN = 11;
        public static final int CONTENT_RINGTONE_URI_COLUMN = 12;
        public static final int CONTENT_PROTOCOL_VERSION_COLUMN = 13;
        public static final int CONTENT_NEW_MESSAGE_COUNT_COLUMN = 14;
        public static final int CONTENT_SECURITY_FLAGS_COLUMN = 15;
        public static final int CONTENT_SECURITY_SYNC_KEY_COLUMN = 16;
        public static final int CONTENT_SIGNATURE_COLUMN = 17;
        // P5_Porting_Email_Block_Start - worked by minsumr.kim
        // change@wtl.jpshu SyncSchedule begin
        public static final int CONTENT_EMAIL_SIZE_COLUMN = 18;
        public static final int CONTENT_POLICY_KEY_COLUMN = 19;
        public static final int CONTENT_PEAK_DAYS_COLUMN = 20;
        public static final int CONTENT_PEAK_START_MINUTE_COLUMN = 21;
        public static final int CONTENT_PEAK_END_MINUTE_COLUMN = 22;
        public static final int CONTENT_PEAK_SCHEDULE_COLUMN = 23;
        public static final int CONTENT_OFF_PEAK_SCHEDULE_COLUMN = 24;
        public static final int CONTENT_ROAMING_SCHEDULE_COLUMN = 25;
        public static final int CONTENT_CALENDAR_SYNC_LOOKBACK_COLUMN = 26;
        // msc, hanklee, 2010.07.19, type message
        // change@wtl.rprominski smime support start
        public static final int CONTENT_SMIME_OWN_ENCRYPT_CERT_ALIAS_COLUMN = 27;
        // change@wtl.akulik smime support start
        public static final int CONTENT_SMIME_OPTIONS_FLAGS_COLUMN = 28;
        public static final int CONTENT_SMIME_OPTIONS_SIGN_ALGORITHM_COLUMN = 29;
        public static final int CONTENT_SMIME_OPTIONS_ENCRYPTION_ALGORITHM_COLUMN = 30;
        // change@wtl.akulik smime support end
        // change@siso.vpdj conversation mode setting start
        public static final int CONTENT_CONVERSATION_MODE_COLUMN = 31;
        // change@siso.vpdj conversation mode setting end
        // change@siso.vpdj Conversation Text preview
        public static final int CONTENT_TEXTPREVIEW_SIZE_COLUMN = 32;
        // change@wtl.shatekar deviceInformation START
        public static final int DEVICE_INFO_SENT_COLUMN = 33;
        public static final int CONTENT_BLOCKDEVICE_COLUMN = 34;
        public static final int CONTENT_CONFLICT_RESOLUTION_COLUMN = 35;
        // change@siso.Spoorti IRM support starts
        public static final int CONTENT_IRM_TEMPLATE_TIME_STAMP = 36;
        // change@siso.Spoorti IRM support ends
        // WTL - CBA - Start
        public static final int CONTENT_CBA_CERTIFICATE_ALIAS_COLUMN = 37;
        // WTL - CBA - End
        // SVL Start vjeswani
        public static final int CONTENT_MESSAGE_FORMAT = 38;
        public static final int CONTENT_ACCOUNT_TYPE = 39;

        // huijae.lee
        public static final int CONTENT_EAS_LOCAL_CHANGE_SETTING = 40;

        // sh2209.cho : add ICS settings
        public static final int CONTENT_FORWARD_WITH_FILES = 41;
        public static final int CONTENT_AUTO_DOWNLOAD = 42;

        public static final int CONTENT_RECENT_MESSAGES = 43;
        public static final int CONTENT_SHOW_IMAGE = 44;

        // SVL End vjeswani
        // change@wtl.shatekar deviceInformation END
        // P5_Porting_Email_Block_End - worked by minsumr.kim
        public static final int CONTENT_AUTO_RETRY_COLUMN = 45;// Change@siso.vinay
        // Auto retry
        // times
        public static final int CONTENT_DOWNLOAD_ON_SCROLL = 46;// Change@siso.g.manish
        
        public static final int CONTENT_IS_SIGNATURE_EDITED = 47;
        
        public static final int CONTENT_CANCEL_SENDING_MESSAGE_TIMEOUT = 48; //change@siso.gaurav cancel send.

        public static final int CONTENT_IS_PEAK_SCHEDULE_ON_COLUMN = 49;

        public static final int CONTENT_COMPANY_NAME = 50;
        
        public static final int CONTENT_ROAMING_EMAIL_SIZE_COLUMN = 51;
        public static final int CONTENT_SMIME_OWN_SIGN_CERT_ALIAS_COLUMN = 52;

        public static final int CONTENT_IMAP_DAYS_BASED_SYNC = 53;

		public static final int CONTENT_SMIME_OPTIONS_MESSAGE_SIGN_TYPE_COLUMN = 54;

        public static final String[] CONTENT_PROJECTION = new String[] {
                RECORD_ID,
                AccountColumns.DISPLAY_NAME,
                AccountColumns.EMAIL_ADDRESS,
                AccountColumns.SYNC_KEY,
                AccountColumns.SYNC_LOOKBACK,
                AccountColumns.SYNC_INTERVAL,
                AccountColumns.HOST_AUTH_KEY_RECV,
                AccountColumns.HOST_AUTH_KEY_SEND,
                AccountColumns.FLAGS,
                AccountColumns.IS_DEFAULT,
                
                AccountColumns.COMPATIBILITY_UUID,
                AccountColumns.SENDER_NAME,
                AccountColumns.RINGTONE_URI,
                AccountColumns.PROTOCOL_VERSION,
                AccountColumns.NEW_MESSAGE_COUNT,
                AccountColumns.SECURITY_FLAGS,
                AccountColumns.SECURITY_SYNC_KEY,
                AccountColumns.SIGNATURE,
                AccountColumns.EMAIL_SIZE,
                AccountColumns.POLICY_KEY,
                
                AccountColumns.PEAK_DAYS,
                AccountColumns.PEAK_START_MINUTE,
                AccountColumns.PEAK_END_MINUTE,
                AccountColumns.PEAK_SCHEDULE,
                AccountColumns.OFF_PEAK_SCHEDULE,
                AccountColumns.ROAMING_SCHEDULE,
                AccountColumns.CALENDAR_SYNC_LOOKBACK,
                AccountColumns.SMIME_OWN_ENCRYPT_CERT_ALIAS,
                AccountColumns.SMIME_OPTIONS_FLAGS,
                AccountColumns.SMIME_OPTIONS_SIGN_ALGORITHM,

                AccountColumns.SMIME_OPTIONS_ENCRYPTION_ALGORITHM,
                AccountColumns.CONVERSATION_MODE
                , AccountColumns.TEXT_PREVIEW_SIZE
                , AccountColumns.DEVICE_INFO_SENT
                , AccountColumns.DEVICE_BLOCK_TYPE
                , AccountColumns.CONFLICT_RESOLUTION
                , AccountColumns.IRM_TEMPLATE_TIME_STAMP
                , AccountColumns.CBA_CERTIFICATE_ALIAS
                , AccountColumns.MESSAGE_FORMAT,
                AccountColumns.ACCOUNT_TYPE

                , AccountColumns.EAS_LOCAL_CHANGE_SETTING
                , AccountColumns.FORWARD_WITH_FILES, 
                AccountColumns.AUTO_DOWNLOAD
                , AccountColumns.RECENT_MESSAGES, 
                AccountColumns.SHOW_IMAGE
                , AccountColumns.AUTO_RETRY_TIMES
                , AccountColumns.DWONLOAD_ON_SCROLL 
                , AccountColumns.IS_SIGNATURE_EDITED
                , AccountColumns.CANCEL_SENDING_MESSAGE_TIMEOUT //change@siso.gaurav cancel send.
                , AccountColumns.IS_PEAK_SCHEDULE_ON

                , AccountColumns.COMPANY_NAME
                , AccountColumns.ROAMING_EMAIL_SIZE
                , AccountColumns.SMIME_OWN_SIGN_CERT_ALIAS
                , AccountColumns.IMAP_DAYS_BASED_SYNC,
				AccountColumns.SMIME_OPTIONS_MESSAGE_SIGN_TYPE,
        };

        public static final int CONTENT_MAILBOX_TYPE_COLUMN = 1;

        /**
         * This projection is for listing account id's only
         */
        public static final String[] ID_TYPE_PROJECTION = new String[] {
                RECORD_ID, MailboxColumns.TYPE
        };

        public static final int ACCOUNT_FLAGS_COLUMN_ID = 0;

        public static final int ACCOUNT_FLAGS_COLUMN_FLAGS = 1;

        public static final String[] ACCOUNT_FLAGS_PROJECTION = new String[] {
                AccountColumns.ID, AccountColumns.FLAGS
        };

        public static final String MAILBOX_SELECTION = MessageColumns.MAILBOX_KEY + " =?";

        public static final String UNREAD_COUNT_SELECTION = MessageColumns.MAILBOX_KEY + " =? and "
                + MessageColumns.FLAG_READ + "= 0";

        public static final String UUID_SELECTION = AccountColumns.COMPATIBILITY_UUID + " =?";

        public static final String SECURITY_NONZERO_SELECTION = Account.SECURITY_SYNC_KEY
                + " IS NOT NULL AND " + Account.SECURITY_SYNC_KEY + "!=0";

        public static final String SECURITY_FLAG_NONZERO_SELECTION = Account.SECURITY_FLAGS
                + " IS NOT NULL AND " + Account.SECURITY_FLAGS + "!=0";

        private static final String FIND_INBOX_SELECTION = MailboxColumns.TYPE + " = "
                + Mailbox.TYPE_INBOX + " AND " + MailboxColumns.ACCOUNT_KEY + " =?";
        
        private static final String FIND_SENTBOX_SELECTION = MailboxColumns.TYPE + " = "
                + Mailbox.TYPE_SENT + " AND " + MailboxColumns.ACCOUNT_KEY + " =?";

        // SncAdapter Porting
        public static final int MAX_SMALL_MESSAGE_SIZE = (60 * 1024);

        /**
         * This projection is for searching for the default account
         */
        private static final String[] DEFAULT_ID_PROJECTION = new String[] {
                RECORD_ID, IS_DEFAULT
        };
        
        /**
         * no public constructor since this is a utility class
         */
        public Account() {
            mBaseUri = CONTENT_URI;

            // other defaults (policy)
            mRingtoneUri = DEFAULT_RINGTONEURI;
            mSyncInterval = -1;
            mSyncLookback = -1;
            mFlags = FLAGS_NOTIFY_NEW_MAIL | FLAGS_ADD_SIGNATURE | FLAGS_IMAP_FORWARD_FLAG_INIT;

            mCompatibilityUuid = UUID.randomUUID().toString();
            // P5_Porting_Email_Block_Start - worked by minsumr.kim
            // change@wtl.dtuttle start
            // dr_65: email size member init to unassigned
            // Changed to 3 to represent the default value (2KB) in
            // res/values/strings.xml and res/values/arrays.xml
            // for this field in the account setup screen. It is believed that
            // hotmail and/or gmail accounts,
            // which skip AccountSetupOptions.java, were getting -1 put into the
            // DB. See
            // account_setup_options_mail_email_size_default in
            // res/values/strings.xml for more info.
            // Note: this is ASYNC00000938
            mEmailSize = 9; // jykim changing initial value
            
            mRoamingEmailSize = 2048;
            
            mEmailMessageDiffEnabled = 0; // MessageDiff change @siso.gaurav
            // change@wtl.dtuttle end
            mConflictFlags = 1;
            // change@wtl.jpshu SyncSchedule begin
            mPolicyKey = "0";
            mSyncScheduleData = new SyncScheduleData(DEFAULT_PEAK_START_MINUTE, // 8
                    // am
                    // 8hr*60
                    // =
                    // 480
                    DEFAULT_PEAK_END_MINUTE, // 5 pm 17hr*60 = 1020
                    Account.DEFAULT_PEAK_DAYS, // | -- xx Mon Tue | Wed Thur Fri
                    // xx
                    // |
                    CHECK_INTERVAL_PUSH,
                    // Account.CHECK_INTERVAL_NEVER,
                    CHECK_INTERVAL_PUSH, CHECK_ROAMING_DEFAULT);
            mIsInPeakPeriod = false;
            // change@wtl.jpshu SyncSchedule end
            // change@wtl.jpshu calendar_sync_lookback
            mCalendarSyncLookback = -1;
            // msc, hanklee, 2010.07.26, for seven
//            mTypeMsg = 0;
            // change@siso.vpdj Conversation Text preview start
            mTextPreviewSize = 128;
            // change@siso.vpdj Conversation Text preview end
            // change@wtl.shatekar Device Information START
            mDeviceInfoSent = 0;
            // change@wtl.shatekar Device Information END
            // P5_Porting_Email_Block_End - worked by minsumr.kim
            // SVL start: vjeswani, sync and connect support
            mMessageFormat = MESSAGE_FORMAT_TYPE_HTML;
            mAccountType = 0xFF; // invalid type
            // SVL start: vjeswani,

            // sh2209.cho : add ICS Settings
            mForwardWithFiles = true;
            mAutoDownload = false;

            mRecentMessages = 0;
            mShowImage = DEFAULT_SHOWIMAGE;

            // Change@siso.vinay Auto retry times start
            mAutoRetryTimes = DEFAULT_AUTORETRYTIMES;// For Infinete option we using Integer value
            // 1000
            // Change@siso.vinay Auto retry times end
            
            mDownloadOnScroll = true;
            
            //Change@siso.Naresh Spamfilter Store the Spamfolderif of account
            
            mIsSignatureEdited = false;
            mCancelSendingMessageTimeout = 0; //change@siso.gaurav cancel send.
            
            mCompanyName = null;
        }
        
        public static int getSyncInterval (Context context, long accountId) {
    		Account account = Account.restoreAccountWithId(context, accountId);
    		if (account == null) {
    			EmailLog.d (TAG, "Account is NULL for accountId - " + accountId);
    			return -1;
    		}
    		SyncScheduleData syncSchedule = account.getSyncScheduleData();
    		return syncSchedule.getSyncInterval(context);
    	}
        
        public static int count(Context context) {
            int count = 0;
            Uri uri = Uri.parse("content://" + AUTHORITY + "/account_count");
            Cursor c = context.getContentResolver().query(uri, null, null, null, null);
            if(c == null){
				return count;
			}

			try{
				if (c != null && c.moveToFirst()) {
					count = c.getInt(0);
				}
			} finally {
				if (c != null && !c.isClosed()) {
		            c.close();
				}
			}
            return count;
        }
        
        public static long findInbox(Context context, long accountId) {
            long inboxId = -1;
            if (accountId < 0)
                return -1;
            if (isVirtualAccount(accountId)) 
                return -1;
            Uri uri = Uri.parse(Account.CONTENT_URI + "/" + String.valueOf(accountId) + "/inbox");
            Cursor c = context.getContentResolver().query(uri, null, null, null, null);
            if(c == null){
				return inboxId;
             }

			try{
				if (c != null && c.moveToFirst()) {
					inboxId = c.getInt(0);
				}
			} finally {
				if (c != null && !c.isClosed()) {
					c.close();
                 }
			}
            return inboxId;
        }

        public static boolean isVirtualAccount(long id) {
            if(id == Account.ACCOUNT_ID_COMBINED_VIEW
                    || id == Account.ACCOUNT_ID_SINGLE_VIEW
                    || id == Account.ACCOUNT_ID_OTHERS
                    /* start - To support old model */
                    || id == Account.ACCOUNT_ID_PRIORITY_SENDER_VIEW 
                    || id == Account.ACCOUNT_ID_FILTER_VIEW
                    || id == Account.ACCOUNT_ID_RECENTLY_READ_VIEW)
                     /* end - To support old model */
                return true;
            return false;
        }
        
        
        
        public static long getSpamMailboxId(Context context, long accountId) {


        	long spamFolder = -1;
        	spamFolder = Mailbox.findMailboxOfType(context, accountId, Mailbox.TYPE_JUNK);

        	if(spamFolder != Mailbox.NO_MAILBOX){
        		return spamFolder;
        	}

        	StringBuilder sb = new StringBuilder();
        	sb.append(MailboxColumns.ACCOUNT_KEY).append("=").append(accountId);
        	sb.append(" AND (");

        	for (int i = 0; i < SPAMBOX_NAMES.length; i ++) {
                if (i != 0)
                    sb.append(" OR ");
                sb.append(MailboxColumns.DISPLAY_NAME).append("=\"").append(SPAMBOX_NAMES[i]).append("\"");
            }
            sb.append(")");
            
            
            Cursor c = null;
            try {
                c = context.getContentResolver().query(Mailbox.CONTENT_URI, new String [] {MailboxColumns.ID}, 
                        sb.toString(), null, null);
                
                if(c == null)
                    return spamFolder;
                
                if (c.moveToFirst()) {
                    spamFolder = c.getLong(0);
                }
            } catch(Exception e) {
                //e.printStackTrace();
            } finally {
                if (c != null)
                    c.close();
            }
            
            return spamFolder;
        }
        
        /**
         * @return true if an {@code accountId} is assigned to any existing
         *         account.
         */
        public static boolean isValidId(Context context, long accountId) {
            if (isVirtualAccount(accountId)) {
                return true;
            }
            Account account = Account.restoreAccountWithId(context, accountId);
            if(account != null && account.mId > 0) {
                return true;
            }
            return false;
        }
        
        
        public static Account restoreAccountWithId(Context context, long id) {
            checkNative();
            Uri u = ContentUris.withAppendedId(Account.CONTENT_URI, id);
            if(context == null){
                EmailLog.e(TAG, "context is NULL. Query failed");
                return null;
            }
            Cursor c = null;
            try {
                c = context.getContentResolver().query(u, Account.CONTENT_PROJECTION, null, null,
                        null);

                if (c == null) {
                    EmailLog.e(TAG, "Cursor is NULL. Query failed");
                    return null;
                }
                try {
                    if (c.moveToFirst()) {
                        return getContent(c, Account.class);
                    } else {
                        EmailLog.e(TAG, "Cursor is empty. Account does not exist id:" + id);
                        return null;
                    }
                } catch (NullPointerException ne) { // Memory Full Exception
                    ne.printStackTrace();
                    return null;
                } finally {
                    if (c != null && !c.isClosed())
                        c.close();
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                EmailLog.e(TAG, "RuntimeException in restoreAccountWithId", e);
                return null;
            }
        }

        // P5_Porting_Email_Block_Start - worked by minsumr.kim
        // change@wtl.kSingh - Gal Search
        public static Account restoreAccountWithEmailAddress(Context context, String email) {
            Cursor c = null;
            try {
                String [] selectionArgs = new String[1];
                selectionArgs[0] = email;
                c = context.getContentResolver().query(Account.CONTENT_URI,
                        Account.CONTENT_PROJECTION, Account.EMAIL_ADDRESS + "=?"
                        + " COLLATE NOCASE", selectionArgs, null);
                if (c != null && c.moveToFirst()) {
                    return getContent(c, Account.class);
                } else {
                    return null;
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "IllegalStateException in restoreAccountWithId", e);
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        }
        

        // adapter_porting
        public static Account[] restoreAccounts(Context context, String where) {
            checkNative();
            Account[] accounts = null;
            Cursor c = null;
            try {
                c = context.getContentResolver().query(Account.CONTENT_URI,
                        Account.CONTENT_PROJECTION, where, null, null);
                if (c == null || c.isClosed() == true) {
                    return null;
                }

                if (c.getCount() <= 0) {
                    c.close();
                    return null;
                }
                accounts = new Account[c.getCount()];
                int i = 0;

                if (c.moveToFirst()) {
                    do {
                        accounts[i++] = getContent(c, Account.class);
                    } while (c.moveToNext());
                } else {
                    return null;
                }
            } catch (Exception e) {
                EmailLog.e(TAG,
                        "IllegalStateException in restoreAccounts(Context context, String where)",
                        e);
                return null;
            } finally {
                if (c != null && !c.isClosed()) {
                    c.close();
                }
            }
            return accounts;
        }

        /**
         * This always excludes the UnifiedAccount created for SNC.
         * 
         * @param context
         * @return
         */
        public static Account[] restoreAccounts(Context context) {
            // Account[] accounts = EmailContentCacheAccount.getAllAccounts();
            checkNative();
            Account[] accounts;
            Cursor c = null;
            try {
                c = context.getContentResolver().query(Account.CONTENT_URI,
                        Account.CONTENT_PROJECTION, null, null, null);
                if (c == null || c.isClosed() == true) {
                    return null;
                }
                if (c.getCount() <= 0) {
                    c.close();
                    return null;
                }
                accounts = new Account[c.getCount()];
                int i = 0;
                if (c.moveToFirst()) {
                    do {
                        accounts[i++] = getContent(c, Account.class);
                    } while (c.moveToNext());
                } else {
                    return null;
                }
            } catch (Exception e) {
            	e.printStackTrace();
                EmailLog.e(TAG, "IllegalStateException in restoreAccounts(Context context)", e);
                return null;
            } finally {
                if (c != null && !c.isClosed()) {
                    c.close();
                }
            }
            return accounts;
        }

        // P5_Porting_Email_Block_End - worked by minsumr.kim

        /**
         * Refresh an account that has already been loaded. This is slightly
         * less expensive that generating a brand-new account object.
         */
        public void refresh(Context context) {
            Cursor c = null;
            try {
                c = context.getContentResolver().query(this.getUri(), Account.CONTENT_PROJECTION,
                        null, null, null);
                if(c != null) {
                    c.moveToFirst();
                    restore(c);
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in refresh", e);
                return;
            } finally {
                if (c != null && !c.isClosed()) {
                    c.close();
                }
            }
        }

        /**
         * Update the initial Sync status for an account.
         * 
         * @param context the id of message
         * @param acctId account which needs to be updated
         * @param syncStatus Sync status for the account
         */
        public static void setSyncStatus(Context context, Long acctId, int syncStatus) {
            ContentValues cv = new ContentValues();
            cv.put(AccountColumns.INIT_SYNC_COMPLETE, syncStatus);
            context.getContentResolver().update(
                    ContentUris.withAppendedId(EmailContent.Account.CONTENT_URI, acctId), cv, null,
                    null);
        }
        
        @Override
        public void restore(Cursor cursor) {
            if(cursor == null || cursor.getCount() == 0)
                return;
            mId = cursor.getLong(CONTENT_ID_COLUMN);
            mBaseUri = CONTENT_URI;
            mDisplayName = cursor.getString(CONTENT_DISPLAY_NAME_COLUMN);
            mEmailAddress = cursor.getString(CONTENT_EMAIL_ADDRESS_COLUMN);
            mSyncKey = cursor.getString(CONTENT_SYNC_KEY_COLUMN);
            mSyncLookback = cursor.getInt(CONTENT_SYNC_LOOKBACK_COLUMN);
            mSyncInterval = cursor.getInt(CONTENT_SYNC_INTERVAL_COLUMN);
            mHostAuthKeyRecv = cursor.getLong(CONTENT_HOST_AUTH_KEY_RECV_COLUMN);
            mHostAuthKeySend = cursor.getLong(CONTENT_HOST_AUTH_KEY_SEND_COLUMN);
            mFlags = cursor.getInt(CONTENT_FLAGS_COLUMN);
            mIsDefault = cursor.getInt(CONTENT_IS_DEFAULT_COLUMN) == 1;
            mCompatibilityUuid = cursor.getString(CONTENT_COMPATIBILITY_UUID_COLUMN);
            mSenderName = cursor.getString(CONTENT_SENDER_NAME_COLUMN);
            mRingtoneUri = cursor.getString(CONTENT_RINGTONE_URI_COLUMN);
            mProtocolVersion = cursor.getString(CONTENT_PROTOCOL_VERSION_COLUMN);
            mNewMessageCount = cursor.getInt(CONTENT_NEW_MESSAGE_COUNT_COLUMN);
            mSecurityFlags = cursor.getLong(CONTENT_SECURITY_FLAGS_COLUMN);
            mSecuritySyncKey = cursor.getString(CONTENT_SECURITY_SYNC_KEY_COLUMN);
            mSignature = cursor.getString(CONTENT_SIGNATURE_COLUMN);
            // P5_Porting_Email_Block_Start - worked by minsumr.kim
            // change@wtl.jpshu SyncSchedule begin
            mEmailSize = cursor.getInt(CONTENT_EMAIL_SIZE_COLUMN);
            mRoamingEmailSize = cursor.getInt(CONTENT_ROAMING_EMAIL_SIZE_COLUMN);
            mEmailMessageDiffEnabled = (mEmailSize == 11 ? 1 : 0); // MessageDiff
                                                                  // change
                                                                  // @siso.gaurav
            mPolicyKey = cursor.getString(CONTENT_POLICY_KEY_COLUMN);
            mTextPreviewSize = cursor.getInt(CONTENT_TEXTPREVIEW_SIZE_COLUMN);
            // change@siso.vpdj conversation mode setting end
            mSyncScheduleData.setPeakDay(cursor.getInt(CONTENT_PEAK_DAYS_COLUMN)); 
            mSyncScheduleData.setStartMinute(cursor.getInt(CONTENT_PEAK_START_MINUTE_COLUMN));
            mSyncScheduleData.setEndMinute(cursor.getInt(CONTENT_PEAK_END_MINUTE_COLUMN));
            mSyncScheduleData.setPeakSchedule(cursor.getInt(CONTENT_PEAK_SCHEDULE_COLUMN));
            // WTL_EDM_START
            // mSyncScheduleData.setOffPeakSchedule(cursor.getInt(CONTENT_OFF_PEAK_SCHEDULE_COLUMN));
            int value = cursor.getInt(CONTENT_OFF_PEAK_SCHEDULE_COLUMN);
            // change@siso.amit IMAP peak/off-peak schedule start
            switch (value) {
                case -2:
                case -1:
                case 5:
                case 10:
                case 15:
                case 30:
                case 60:
                case 120:
                case 240:
                case 720:
                case 1440:
                    break;

                default:
                    value = -2;
            }
            // change@siso.amit IMAP peak/off-peak schedule end
            mSyncScheduleData.setOffPeakSchedule(value);
            // WTL_EDM_END
            mSyncScheduleData.setRoamingSchedule(cursor.getInt(CONTENT_ROAMING_SCHEDULE_COLUMN));
            mSyncScheduleData.setIsPeakScheduleOn(cursor.getInt(CONTENT_IS_PEAK_SCHEDULE_ON_COLUMN) == 0 ? false
                    : true);
            // change@wtl.jpshu SyncSchedule end
            // change@wtl.jpshu calendar_sync_lookback
            mCalendarSyncLookback = cursor.getInt(CONTENT_CALENDAR_SYNC_LOOKBACK_COLUMN);
            // msc, sunyoung, 2010.08.23

            // change@wtl.rprominski smime support start
            mSmimeOwnEncryptCertAlias = cursor
                    .getString(CONTENT_SMIME_OWN_ENCRYPT_CERT_ALIAS_COLUMN);
            mSmimeOwnSignCertAlias = cursor
                    .getString(CONTENT_SMIME_OWN_SIGN_CERT_ALIAS_COLUMN);
            // change@wtl.rprominski smime support end
            // change@wtl.akulik smime support start
            setSmimeFlags(cursor.getInt(CONTENT_SMIME_OPTIONS_FLAGS_COLUMN));
            mSmimeSignAlgorithm = cursor.getInt(CONTENT_SMIME_OPTIONS_SIGN_ALGORITHM_COLUMN);
            mSmimeEncryptionAlgorithm = cursor
                    .getInt(CONTENT_SMIME_OPTIONS_ENCRYPTION_ALGORITHM_COLUMN);
            // change@wtl.akulik smime support end
            // change@wtl.shatekar Device Information START
            mSmimeMsgSignType = cursor.getInt(CONTENT_SMIME_OPTIONS_MESSAGE_SIGN_TYPE_COLUMN);
            mDeviceInfoSent = cursor.getInt(DEVICE_INFO_SENT_COLUMN);
            // change@wtl.shatekar Device Information END
            // change@siso.mahsky Allow/Block/Quarantined start
            mdeviceBlockedType = cursor.getInt(CONTENT_BLOCKDEVICE_COLUMN);
            // change@siso.mahsky Allow/Block/Quarantined end
            mConflictFlags = cursor.getInt(CONTENT_CONFLICT_RESOLUTION_COLUMN);
            // changes@siso Conflict resolution
            // change@siso.Spoorti IRM support starts
            mIRMTemplateTimeStamp = cursor.getString(CONTENT_IRM_TEMPLATE_TIME_STAMP);
            // change@siso.Spoorti IRM support ends
            // WTL - CBA - Start
            mCbaCertificateAlias = cursor.getString(CONTENT_CBA_CERTIFICATE_ALIAS_COLUMN);
            // WTL - CBA - End

            // SVL Start vjeswani
            mMessageFormat = cursor.getInt(CONTENT_MESSAGE_FORMAT);
            mAccountType = cursor.getInt(CONTENT_ACCOUNT_TYPE);
            // SVL End vjeswani

            // huijae.lee
            mEasLocalChangeEnabled = (cursor.getInt(CONTENT_EAS_LOCAL_CHANGE_SETTING) == 0 ? false
                    : true);

            // sh2209.cho : add ICS settings
            mForwardWithFiles = (cursor.getInt(CONTENT_FORWARD_WITH_FILES) == 0 ? false : true);
            mAutoDownload = (cursor.getInt(CONTENT_AUTO_DOWNLOAD) == 0 ? false : true);

            mRecentMessages = cursor.getInt(CONTENT_RECENT_MESSAGES);
            mShowImage = (cursor.getInt(CONTENT_SHOW_IMAGE) == 0 ? false : true);

            // Change@siso.vinay Auto retry times start
            mAutoRetryTimes = cursor.getInt(CONTENT_AUTO_RETRY_COLUMN);
            // Change@siso.vinay Auto retry times end
            mDownloadOnScroll = (cursor.getInt(CONTENT_DOWNLOAD_ON_SCROLL) == 0 ? false : true);//Change g.manish
            // Change@siso.Naresh Spamfilter Store the Spamfolderif of account
//            mSpamfolderkey = cursor.getInt(CONTENT_SPAM_FOLDER_KEY);
            // starov.ilya: To support default signature translation
            mIsSignatureEdited = (cursor.getInt(CONTENT_IS_SIGNATURE_EDITED) == 0 ? false : true);   
            mCancelSendingMessageTimeout = cursor.getLong(CONTENT_CANCEL_SENDING_MESSAGE_TIMEOUT); //change@siso.gaurav cancel send.
            mCompanyName = cursor.getString(CONTENT_COMPANY_NAME);
            mImapDaysBasedSync = cursor.getInt(CONTENT_IMAP_DAYS_BASED_SYNC);
        }

        private long getId(Uri u) {
            return Long.parseLong(u.getPathSegments().get(1));
        }

        /**
         * @return the user-visible name for the account
         */
        public String getDisplayName() {
            return mDisplayName;
        }

        /**
         * Set the description. Be sure to call save() to commit to database.
         * 
         * @param description the new description
         */
        public void setDisplayName(String description) {
            mDisplayName = description;
        }

        /**
         * @return the email address for this account
         */
        public String getEmailAddress() {
            return mEmailAddress;
        }

        /**
         * Set the Email address for this account. Be sure to call save() to
         * commit to database.
         * 
         * @param emailAddress the new email address for this account
         */
        public void setEmailAddress(String emailAddress) {
            mEmailAddress = emailAddress;
        }

        /**
         * @return the sender's name for this account
         */
        public String getSenderName() {
            if (mSenderName == null)
                return "";

            return mSenderName;
        }

        /**
         * Set the sender's name. Be sure to call save() to commit to database.
         * 
         * @param name the new sender name
         */
        public void setSenderName(String name) {
            mSenderName = name;
        }

        public String getSignature() {
            return mSignature;
        }

        public void setSignature(String signature) {
            mSignature = signature;
        }

        // P5_Porting_Email_Block_Start - worked by minsumr.kim

        // change@wtl.dtuttle start
        // dr_70: get/set email size
        public byte getEmailSize() {
            EmailLog.d(TAG, "EmailSize getEmailSize mEmailSize = " + mEmailSize);
            return (byte) mEmailSize;
        }
        
        public byte getRoamingEmailSize() {
            EmailLog.d(TAG, "EmailSize getRoamingEmailSize mRoamingEmailSize = " + mRoamingEmailSize);
            return (byte) mRoamingEmailSize;
        }
        
        public int getRoamingEmailIntSize() {
            return mRoamingEmailSize;
        }
        
        public byte getRealEmailSize(Context context, boolean isRoaming) {
            if(context == null)
                EmailLog.d(TAG, "context is null");

            if(isRoaming) {
                if(mRoamingEmailSize == -1) {
                    EmailLog.d(TAG, "EmailSize getRealEmailSize mRoamingEmailSize use above settings = " + mEmailSize);
                    return (byte) getEmailSize();
                } else {
                    EmailLog.d(TAG, "EmailSize getRealEmailSize mRoamingEmailSize = " + mRoamingEmailSize);
                    return (byte) getRoamingEmailSize();
                }
            }
            else {
                EmailLog.d(TAG, "EmailSize getRealEmailSize mEmailSize = " + mEmailSize);
                return (byte) getEmailSize();
            }  
        }
        
        // SncAdapter Porting
        public static int getEmailSize(byte size) {
            if (size == MESSAGE_SIZE_1_5_K) {
                return (int) (1.5 * 1024);
            } else if (size == MESSAGE_SIZE_20_K) {
                return 20 * 1024;
            } else if (size == MESSAGE_SIZE_60_K) {
                    return 60 * 1024;
            } else if (size == MESSAGE_SIZE_100_K) {
                return 100 * 1024;
            }

            return MAX_SMALL_MESSAGE_SIZE; // default!
        }

        public static int getRoamingEmailSize(byte size) {
            if (size == MESSAGE_SIZE_1_5_K) {
                return (int) (1.5 * 1024);
            } else if (size == MESSAGE_SIZE_20_K) {
                return 20 * 1024;
            } else if (size == MESSAGE_SIZE_60_K) {
                    return 60 * 1024;
            } else if (size == MESSAGE_SIZE_100_K) {
                return 100 * 1024;
            }

            return MAX_SMALL_MESSAGE_SIZE; // default!
        }

        public void setEmailSize(byte emailSize) {
            mEmailSize = (int) emailSize;
        }
        
        public void setRoamingEmailSize(byte roamingEmailSize) {
            mRoamingEmailSize = (int) roamingEmailSize;
        }
        
        public void setRoamingEmailIntSize(int roamingEmailSize) {
            mRoamingEmailSize = roamingEmailSize;
        }
        
        // change@wtl.dtuttle end
        // MessageDiff change @siso.gaurav start
        public int getEmailMessageDiffEnabled() {
            return mEmailMessageDiffEnabled;
        }

        public void setEmailMessageDiffEnabled(int msgEnabled) {
            mEmailMessageDiffEnabled = msgEnabled;
        }

        // MessageDiff change @siso.gaurav end

        public int getEmailRetrieveSize() {
            if(mEmailSize == 9)
                return mEmailSize = EMAIL_SIZE_51200;
            else
                return mEmailSize;
        }

        public int getEmailIntSize() {
            return mEmailSize;
        }
        
        public void setEmailRetrieveSize(int emailSize) {
            mEmailSize = emailSize;
        }

        public void setEmailRetrieveSizeValue(String[] value) {
            mEmailRetrieveSizeValues = value;
        }

        public int getMinEmailRetrieveSize(Context context) {
            if(context == null)
                EmailLog.d(TAG, "context is null");
            // if (null != mEmailRetrieveSizeValues)
            // return Integer.parseInt(mEmailRetrieveSizeValues[0]);
            // else
            return EMAIL_SIZE_2048;
        }

        public int getDefaultEmailRetrieveSize(Context context) {
            if(context == null)
                EmailLog.d(TAG, "context is null");
            return  DEFAULT_MESSAGESIZE;
        }
        
        public int getEmailRoamingRetrieveSize() {
            if(mRoamingEmailSize == 9)
                return mRoamingEmailSize = EMAIL_SIZE_51200;
            else
                return mRoamingEmailSize;
        }

        public void setEmailRoamingRetrieveSize(int roamingemailSize) {
            mRoamingEmailSize = roamingemailSize;
        }

        public void setEmailRoamingRetrieveSizeValue(String[] value) {
            mEmailRoamingRetrieveSizeValues = value;
        }

        public int getMinEmailRoamingRetrieveSize(Context context) {
            if(context == null)
                EmailLog.d(TAG, "context is null");
            // if (null != mEmailRetrieveSizeValues)
            // return Integer.parseInt(mEmailRetrieveSizeValues[0]);
            // else
            return EMAIL_SIZE_2048;
        }

        public int getDefaultEmailRoamingRetrieveSize(Context context) {
            if(context == null)
                EmailLog.d(TAG, "context is null");
            return  DEFAULT_MESSAGESIZE_ROAMING;
        }

        public int getLargeMsgCriSize(Context context, boolean isRoaming) {
            int criSize;
            if(isRoaming) {
                if(mRoamingEmailSize == -1) {
                    EmailLog.d(TAG, "Current RoamingEmailRetrieveSize use above settings= " + mEmailSize);
                    if (mEmailSize < getMinEmailRetrieveSize(context)) {
                        criSize = getDefaultEmailRetrieveSize(context);
                    } else {
                        criSize = mEmailSize;
                    }
                }
                else {
                    EmailLog.d(TAG, "Current RoamingEmailRetrieveSize = " + mRoamingEmailSize);
                    if (mRoamingEmailSize < getMinEmailRoamingRetrieveSize(context)) {
                        criSize = getDefaultEmailRoamingRetrieveSize(context);
                    } else {
                        criSize = mRoamingEmailSize;
                    }
                }
            }
            else {
                EmailLog.d(TAG, "Current EmailRetrieveSize = " + mEmailSize);
                if (mEmailSize < getMinEmailRetrieveSize(context)) {
                    criSize = getDefaultEmailRetrieveSize(context);
                } else {
                    criSize = mEmailSize;
                }
            }
            EmailLog.d(TAG, "LargeMsg criterion size = " + criSize);
            return criSize;
        }

        // change@siso.saritha Conflictresolution start
        public int getConflictresolution() {
            return mConflictFlags;
        }

        public void setConflictresolution(int conflict) {
            mConflictFlags = conflict;
        }

        // change@siso.saritha Conflictresolution end

        // change@siso.vpdj Conversation Text preview start
        public int getTextPreviewSize() {
            return mTextPreviewSize;
        }

        public void setTextPreviewSize(int textPreview) {
            mTextPreviewSize = textPreview;
        }

        // change@siso.vpdj Conversation Text preview end

        // change@wtl.shatekar Device Information START
        /**
         * @return value of deviceInfoSent variable stored in Account table.
         *         This value indicates whether the Device Information was sent
         *         (1) or not (0).
         */
        public int getDeviceInfoSent() {
            return mDeviceInfoSent;
        }

        /**
         * @param devInfo The deviceInfo value is updated when the Device
         *            Information is sent
         */
        public void setDeviceInfoSent(int devInfo) {
            mDeviceInfoSent = devInfo;
        }

        // change@wtl.shatekar Device Information END
        // P5_Porting_Email_Block_End - worked by minsumr.kim

        /**
         * @return the minutes per check (for polling) 
         */
        public int getSyncInterval() {
            return mSyncInterval;
        }

        /**
         * Set the minutes per check (for polling). Be sure to call save() to
         * commit to database. 
         * 
         * @param minutes the number of minutes between polling checks
         */
        public void setSyncInterval(int minutes) {
            mSyncInterval = minutes;
        }

        // sh2209.cho : add ICS settings
        public boolean getForwardWithFiles() {
            return mForwardWithFiles;
        }

        public void setForwardWithFiles(boolean value) {
            mForwardWithFiles = value;
        }

        public boolean getAutoDownload() {
            return mAutoDownload;
        }

        public void setAutoDownload(boolean value) {
            mAutoDownload = value;
        }

        public int getRecentMessages() {
            return mRecentMessages;
        }

        public void setRecentMessages(int value) {
            mRecentMessages = value;
        }

        public boolean getShowImage() {
            return mShowImage;
        }

        public void setShowImage(boolean value) {
            mShowImage = value;
        }

        // sh2209.cho : add ICS settings

        /**
         * @return One of the {@code Account.SYNC_WINDOW_*} constants that
         *         represents the sync lookback window. 
         */
        public int getSyncLookback() {
            return mSyncLookback;
        }

        /**
         * Set the sync lookback window. Be sure to call save() to commit to
         * database. TODO define sentinel values for "all", "1 month", etc. See
         * Account.java
         * 
         * @param value One of the {@code Account.SYNC_WINDOW_*} constants
         */
        public void setSyncLookback(int value) {
            mSyncLookback = value;
        }

        // P5_Porting_Email_Block_Start - worked by minsumr.kim
        // change@wtl.jpshu calendar_sync_lookback begin
        public int getCalendarSyncLookback() {
            return mCalendarSyncLookback;
        }

        public boolean setCalendarSyncLookback(int days) {
            boolean success;
            if (days == CALENDAR_SYNC_WINDOW_2_WEEKS || days == CALENDAR_SYNC_WINDOW_1_MONTH
                    || days == CALENDAR_SYNC_WINDOW_3_MONTH || days == CALENDAR_SYNC_WINDOW_6_MONTH
                    || days == CALENDAR_SYNC_WINDOW_ALL) {
                success = true;
                mCalendarSyncLookback = days;
            } else {
                mCalendarSyncLookback = CALENDAR_SYNC_WINDOW_2_WEEKS;
                success = false;
            }

            return success;
        }

        // change@wtl.jpshu calendar_sync_lookback end

        // change@wtl.rprominski smime support begin
        public String getSmimeOwnCertificate() {
            return mSmimeOwnEncryptCertAlias;
        }

        public void setSmimeOwnCertificate(String alias) {
            mSmimeOwnEncryptCertAlias = alias;
        }
        
        public String getSmimeOwnSignCertificate() {
            return mSmimeOwnSignCertAlias;
        }

        public void setSmimeOwnSignCertificate(String alias) {
            mSmimeOwnSignCertAlias = alias;
        }    

        // change@wtl.smime support end

        // WTL - CBA - Start
        public String getCbaCertificate() {
            return mCbaCertificateAlias;
        }

        public void setCbaCertificate(String alias) {
            mCbaCertificateAlias = alias;
        }

        // WTL - CBA - End

        // change@wtl.akulik smime support start
        public boolean getSmimeSignAll() {
            return mSmimeSignAll;
        }

        public void setSmimeSignAll(boolean value) {
            mSmimeSignAll = value;
        }
        
        //aditya@siso changes for the new UX change
        public void setIsDefaultKeyChecked(boolean value){
            mIsDefaultkeyChecked = value;
        }
        
        public boolean getIsDefaultKeyChecked()
        {
            return mIsDefaultkeyChecked;
        }
       
        public boolean getSmimeEncryptAll() {
            return mSmimeEncryptAll;
        }

        public void setSmimeEncryptAll(boolean value) {
            mSmimeEncryptAll = value;
        }

        public int getSmimeSignAlgorithm() {
            return mSmimeSignAlgorithm;
        }

        /**
         * Sets the algorithm for signing messages. Possible values are:
         * <ul>
         * <li>0 - SHA1</li>
         * <li>1 - MD5</li>
         * </ul>
         * 
         * @param signAlgorithm algorithm to be set
         */
        public void setSmimeSignAlgorithm(int signAlgorithm) {
            mSmimeSignAlgorithm = signAlgorithm;
        }
        public void setSmimeMsgSignType(int msgSignType) {
            mSmimeMsgSignType = msgSignType;
        }
        public int getSmimeEncryptionAlgorithm() {
            return mSmimeEncryptionAlgorithm;
        }

        /**
         * Sets the algorithm for signing messages. Possible values are:
         * <ul>
         * <li>0 - 3DES</li>
         * <li>1 - DES</li>
         * <li>2 - RC2 128bit</li>
         * <li>3 - RC2 64bit</li>
         * <li>4 - RC2 40bit</li>
         * </ul>
         * 
         * @param signAlgorithm algorithm to be set
         */
        public void setSmimeEncryptionAlgorithm(int encryptionAlgorithm) {
            mSmimeEncryptionAlgorithm = encryptionAlgorithm;
        }

        // change@wtl.akulik smime support end

        // Change@siso.vinay Auto retry times start
        public int getAutoRetryTimes() {
            if(mAutoRetryTimes == 1000) //Defensive code for previous No Limit concept.
                mAutoRetryTimes = DEFAULT_AUTORETRYTIMES;

            return mAutoRetryTimes;
        }

        public void setAutoRetryTimes(int retryTimes) {
            mAutoRetryTimes = retryTimes;
        }

        // Change@siso.vinay Auto retry times end
        
        public boolean getDownloadOnScroll() {
            return mDownloadOnScroll;
           }

        public void setDownloadOnScroll(boolean downloadOnScroll) {
            mDownloadOnScroll = downloadOnScroll;
        }

//        // Change@siso.Naresh Spamfilter Store the Spamfolderif of account        
//        public long getSpamFolderKey() {
//            return mSpamfolderkey;
//        }
//
//        public void setSpamFolderKey(long spamfolderkey) {
//            mSpamfolderkey = spamfolderkey;
//        }
        // change@siso.gaurav cancel send start
        public long getCancelSendingMessageTimeout() {
            return mCancelSendingMessageTimeout;
        }

        public void setCancelSendingMessageTimeout(
                long cancelSendingMessageTimeout) {
            mCancelSendingMessageTimeout = cancelSendingMessageTimeout;
        }
        // change@siso.gaurav cancel send end
        
        // change@siso.vpdj conversation mode setting start

        // change@siso.vpdj conversation mode setting end

        // ============================================================
        // Seven2010.07.20)
        // ============================================================
        public void setAccountKey(long accountKey) {
            this.mAccountKey = accountKey;
        }

        public long getAccountKey() {
            return this.mAccountKey;
        }

//        public void setSevenAccountKey(long sevenAccountKey) {
//            this.mSevenAccountKey = sevenAccountKey;
//        }
//
//        public long getSevenAccountKey() {
//            return this.mSevenAccountKey;
//        }
//
//        public void setTypeMsg(int typeMsg) {
//            this.mTypeMsg = typeMsg;
//        }
//
//        public int getTypeMsg() {
//            return this.mTypeMsg;
//        }
//
//        public void setTimeLimit(long timeLimit) {
//            this.mTimeLimit = timeLimit;
//        }
//
//        public long getTimeLimit() {
//            return this.mTimeLimit;
//        }
//
//        public void setSizeLimit(long sizeLimit) {
//            this.mSizeLimit = sizeLimit;
//        }
//
//        public long getSizeLimit() {
//            return this.mSizeLimit;
//        }

        // ================================================================
        // P5_Porting_Email_Block_End - worked by minsumr.kim

        /**
         * @return the flags for this account
         * @see #FLAGS_NOTIFY_NEW_MAIL
         * @see #FLAGS_VIBRATE_ALWAYS
         * @see #FLAGS_VIBRATE_WHEN_SILENT
         */
        public int getFlags() {
            return mFlags;
        }

        /**
         * Set the flags for this account
         * 
         * @see #FLAGS_NOTIFY_NEW_MAIL
         * @see #FLAGS_VIBRATE_ALWAYS
         * @see #FLAGS_VIBRATE_WHEN_SILENT
         * @param newFlags the new value for the flags
         */
        public void setFlags(int newFlags) {
            mFlags = newFlags;
        }

        /**
         * @return the ringtone Uri for this account
         */
        public String getRingtone() {
            return mRingtoneUri;
        }

        // SVL start vjeswani
        /**
         * @return the message format for this account
         */
        public int getMessageFormat() {
            return mMessageFormat;
        }

        /**
         * Set the message format.
         * 
         * @param message format
         */
        public void setMessageFormat(int messageFormat) {
            mMessageFormat = messageFormat;
        }

        /**
         * @return the message format for this account
         */
        public int getAccountType() {
            return mAccountType;
        }

        /**
         * Set the message format.
         * 
         * @param message format
         */
        public void setAccountType(int accountType) {
            mAccountType = accountType;
        }

        // SVL end vjeswani

        /**
         * Set the ringtone Uri for this account
         * 
         * @param newUri the new URI string for the ringtone for this account
         */
        public void setRingtone(String newUri) {
            mRingtoneUri = newUri;
        }

        // change@wtl.pkijowski SyncSchedule
        public SyncScheduleData getSyncScheduleData() {
            return mSyncScheduleData;
        }

        public void setSyncScheduleData(SyncScheduleData syncScheduleData) {

            mSyncScheduleData.setPeakDay(syncScheduleData.getPeakDay());
            mSyncScheduleData.setStartMinute(syncScheduleData.getStartMinute());
            mSyncScheduleData.setEndMinute(syncScheduleData.getEndMinute());
            mSyncScheduleData.setPeakSchedule(syncScheduleData.getPeakSchedule());
            mSyncScheduleData.setOffPeakSchedule(syncScheduleData.getOffPeakSchedule());
            mSyncScheduleData.setRoamingSchedule(syncScheduleData.getRoamingSchedule());
            mSyncScheduleData.setIsPeakScheduleOn(syncScheduleData.getIsPeakScheduleOn());
            //EmailLog.d(TAG, "Account setSyncScheduleData accId = " + mId + " email = " + mEmailAddress);
            EmailLog.d(TAG, Integer.toString(syncScheduleData.getPeakDay()));
            EmailLog.d(TAG, Integer.toString(syncScheduleData.getStartMinute()));
            EmailLog.d(TAG, Integer.toString(syncScheduleData.getEndMinute()));
            EmailLog.d(TAG, Integer.toString(syncScheduleData.getPeakSchedule()));
            EmailLog.d(TAG, Integer.toString(syncScheduleData.getOffPeakSchedule()));
            EmailLog.d(TAG, Integer.toString(syncScheduleData.getRoamingSchedule()));
            EmailLog.d(TAG, Boolean.toString(syncScheduleData.getIsPeakScheduleOn()));
        }

        // change@wtl.pkijowski SyncSchedule

        /**
         * Set the "delete policy" as a simple 0,1,2,3 value set.
         * 
         * @param newPolicy the new delete policy
         */
        public void setDeletePolicy(int newPolicy) {
            Log.d(TAG, "setDeletePolicy() deletePolicy :"+newPolicy);
            mFlags &= ~FLAGS_DELETE_POLICY_MASK;
            mFlags |= (newPolicy << FLAGS_DELETE_POLICY_SHIFT) & FLAGS_DELETE_POLICY_MASK;
        }

        /**
         * Return the "delete policy" as a simple 0,1,2,3 value set.
         * 
         * @return the current delete policy
         */
        public int getDeletePolicy() {
            int deletePolicy = (mFlags & FLAGS_DELETE_POLICY_MASK) >> FLAGS_DELETE_POLICY_SHIFT;
            Log.d(TAG, "getDeletePolicy() deletePolicy :"+deletePolicy);
            return deletePolicy;
        }


        /**
         * This feature flag is for the new Pop sync concept introduced.
         * According to the new concept wo do not down sync the deleted mails from the server
         */
        public boolean isPopDownSyncConceptDisabled() {
            int deletePolicy = (mFlags & FLAGS_DELETE_POLICY_MASK) >> FLAGS_DELETE_POLICY_SHIFT;
            Log.d(TAG, "isPopDownSyncConceptEnabled() deletePolicy :"+deletePolicy);
            if(deletePolicy == 1 || deletePolicy == 3){
                return false;
            }
            return true;
        }

        public boolean isPopUpSyncConceptDisabled() {
            int deletePolicy = (mFlags & FLAGS_DELETE_POLICY_MASK) >> FLAGS_DELETE_POLICY_SHIFT;
            Log.d(TAG, "isPopDownSyncConceptEnabled() deletePolicy :"+deletePolicy);
            if(deletePolicy == 2 || deletePolicy == 3){
                return false;
            }
            return true;
        }

        /**
         * Return the Uuid associated with this account. This is primarily for
         * compatibility with accounts set up by previous versions, because
         * there are externals references to the Uuid (e.g. desktop shortcuts).
         */
        public String getUuid() {
            return mCompatibilityUuid;
        }

        // MDM intent parameter for EAS Setup >>>>>>
        public long getSecurityFlags() {
            return mSecurityFlags;
        }

        public void setSecurityFlags(long securityflags) {
            mSecurityFlags = securityflags;
        }

        public int getImapDaysBasedSync() {
            return mImapDaysBasedSync;
        }

        public void setImapDaysBasedSync(int value) {
            mImapDaysBasedSync = value;
        }

        // MDM intent parameter for EAS Setup <<<<<<
        /**
         * For compatibility while converting to provider model, generate a
         * "store URI"
         * 
         * @return a string in the form of a Uri, as used by the other parts of
         *         the email app
         */
        public String getStoreUri(Context context) {
            // reconstitute if necessary
            if (mHostAuthRecv == null) {
                mHostAuthRecv = HostAuth.restoreHostAuthWithId(context, mHostAuthKeyRecv);
            }
            // convert if available
            if (mHostAuthRecv != null) {
                String storeUri = mHostAuthRecv.getStoreUri();
                if (storeUri != null) {
                    return getUriWithFullEmailAddress(storeUri);
                }
            }
            return "";
        }

        private String getUriWithFullEmailAddress(String uriInput) {
            try {
                URI uri = new URI(uriInput);
                URI uriOutput = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(),
                        uri.getPort(), uri.getPath(), uri.getQuery(), mEmailAddress.trim());
                return uriOutput.toString();
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return uriInput;
            }
        }

        public String getSenderUri(Context context) {
            // reconstitute if necessary
            if (mHostAuthSend == null) {
                mHostAuthSend = HostAuth.restoreHostAuthWithId(context, mHostAuthKeySend);
            }
            // convert if available
            if (mHostAuthSend != null) {
                String senderUri = mHostAuthSend.getStoreUri();
                if (senderUri != null) {
                    return senderUri;
                }
            }
            return "";
        }

        // upgrade to HC3.1
        /**
         * For compatibility while converting to provider model, set the store
         * URI
         * 
         * @param context
         * @param sotreUri the new value
         */
        @Deprecated
        public void setStoreUri(Context context, String storeUri) {
            // reconstitute or create if necessary
            if (mHostAuthRecv == null) {
                if (mHostAuthKeyRecv != 0) {
                    mHostAuthRecv = HostAuth.restoreHostAuthWithId(context, mHostAuthKeyRecv);
                } else {
                    mHostAuthRecv = new EmailContent.HostAuth();
                }
            }

            if (mHostAuthRecv != null) {
                mHostAuthRecv.setStoreUri(storeUri);
            }
        }

        /**
         * For compatibility while converting to provider model, set the sender
         * URI
         * 
         * @param context
         * @param sotreUri the new value
         */
        @Deprecated
        public void setSenderUri(Context context, String senderUri) {
            // reconstitute or create if necessary
            if (mHostAuthSend == null) {
                if (mHostAuthKeySend != 0) {
                    mHostAuthSend = HostAuth.restoreHostAuthWithId(context, mHostAuthKeySend);
                } else {
                    mHostAuthSend = new EmailContent.HostAuth();
                }
            }

            if (mHostAuthSend != null) {
                mHostAuthSend.setStoreUri(senderUri);
            }
        }

        public HostAuth getOrCreateHostAuthSend(Context context) {
            if (mHostAuthSend == null) {
                if (mHostAuthKeySend != 0) {
                    mHostAuthSend = HostAuth.restoreHostAuthWithId(context, mHostAuthKeySend);
                } else {
                    mHostAuthSend = new EmailContent.HostAuth();
                }
            }
            return mHostAuthSend;
        }

        public HostAuth getOrCreateHostAuthRecv(Context context) {
            if (mHostAuthRecv == null) {
                if (mHostAuthKeyRecv != 0) {
                    mHostAuthRecv = HostAuth.restoreHostAuthWithId(context, mHostAuthKeyRecv);
                } else {
                    mHostAuthRecv = new EmailContent.HostAuth();
                }
            }
            return mHostAuthRecv;
        }

        /**
         * For compatibility while converting to provider model, generate a
         * "local store URI"
         * 
         * @return a string in the form of a Uri, as used by the other parts of
         *         the email app
         */
        public String getLocalStoreUri(Context context) {
            return "local://localhost/" + context.getDatabasePath(getUuid() + ".db");
        }

        /**
         * @return true if the instance is of an EAS account. NOTE This method
         *         accesses the DB if {@link #mHostAuthRecv} hasn't been
         *         restored yet. Use caution when you use this on the main
         *         thread.
         */
        public boolean isEasAccount(Context context) {
            return "eas".equals(getProtocol(context));
        }

        public boolean isImapAccount(Context context) {
            return "imap".equals(getProtocol(context));
        }
        
        /**
         * @return true if the account supports "move messages".
         */
        public static boolean supportsMoveMessages(Context context, long accountId) {
            String protocol = getProtocol(context, accountId);
            return "eas".equals(protocol) || "imap".equals(protocol);
        }

        /**
         * Set the account to be the default account. If this is set to "true",
         * when the account is saved, all other accounts will have the same
         * value set to "false".
         * 
         * @param newDefaultState the new default state - if true, others will
         *            be cleared.
         */
        public void setDefaultAccount(boolean newDefaultState) {
            mIsDefault = newDefaultState;
        }

        /**
         * Helper method for finding the default account.
         */
        static private long getDefaultAccountWhere(Context context, String where) {
            return EmailContentUtils.getFirstRowLong(context, CONTENT_URI, DEFAULT_ID_PROJECTION, where,
                    null, null, 0, Long.valueOf(-1));
        }

        /**
         * @return {@link Uri} to this {@link Account} in the
         *         {@code content://com.samsung.android.email.ui.provider/account/UUID}
         *         format, which is safe to use for desktop shortcuts.
         *         <p>
         *         We don't want to store _id in shortcuts, because
         *         {@link com.samsung.android.email.ui.AccountBackupRestore} won't preserve
         *         it.
         */
        public Uri getShortcutSafeUri() {
            return getShortcutSafeUriFromUuid(mCompatibilityUuid);
        }

        /**
         * @return {@link Uri} to an {@link Account} with a {@code uuid}.
         */
        public static Uri getShortcutSafeUriFromUuid(String uuid) {
            return CONTENT_URI.buildUpon().appendEncodedPath(uuid).build();
        }

        /**
         * Parse {@link Uri} in the
         * {@code content://com.samsung.android.email.ui.provider/account/ID} format where
         * ID = account id (used on Eclair, Android 2.0-2.1) or UUID, and return
         * _id of the {@link Account} associated with it.
         * 
         * @param context context to access DB
         * @param uri URI of interest
         * @return _id of the {@link Account} associated with ID, or -1 if none
         *         found.
         */
        public static long getAccountIdFromShortcutSafeUri(Context context, Uri uri) {
            // Make sure the URI is in the correct format.
            if (!"content".equals(uri.getScheme()) || !AUTHORITY.equals(uri.getAuthority())) {
                return -1;
            }

            final List<String> ps = uri.getPathSegments();
            if (ps.size() != 2 || !"account".equals(ps.get(0))) {
                return -1;
            }

            // Now get the ID part.
            final String id = ps.get(1);

            // First, see if ID can be parsed as long. (Eclair-style)
            // (UUIDs have '-' in them, so they are always non-parsable.)
            try {
                return Long.parseLong(id);
            } catch (NumberFormatException ok) {
                // OK, it's not a long. Continue...
            }

            // Now id is a UUId.
            return EmailContentUtils.getFirstRowLong(context, CONTENT_URI, ID_PROJECTION, UUID_SELECTION,
                    new String[]{
                            id
                    }, null, 0, Long.valueOf(-1));
        }

        /**
         * Return the id of the default account. If one hasn't been explicitly
         * specified, return the first one in the database. For any account
         * saved in the DB, this must be used to check for the default account -
         * the mIsDefault field is set lazily and may be incorrect.
         * 
         * @param context the caller's context
         * @return the id of the default account, or -1 if there are no accounts
         */
        static public long getDefaultAccountId(Context context) {
            long id = getDefaultAccountWhere(context, AccountColumns.IS_DEFAULT + "=1");
            if (id == -1) {
                // SVL Start : j.sb
                // While changing the default account, exclude the unified
                // account
                id = getDefaultAccountWhere(context, null);
                // SVL End : j.sb
            }
            return id;
        }
        
        static public long setDefaultAccountWhenAccountDeleted(Context context) {
            long id = getDefaultAccountWhere(context, AccountColumns.IS_DEFAULT + "=1");
            if (id == -1) {
                id = getDefaultAccountWhere(context, null);
                if (id != -1) {
                    EmailLog.d(TAG, "setDefaultAccountWhenAccountDeleted : " + id);
                    Account acc = restoreAccountWithId(context, id);
                    ContentValues cv = new ContentValues();
                    cv.put(AccountColumns.IS_DEFAULT, true);
                    if(acc != null)
                        acc.update(context, cv);
                }
            }
            return id;
        }

        /**
         * Given an account id, return the account's protocol
         * 
         * @param context the caller's context
         * @param accountId the id of the account to be examined
         * @return the account's protocol (or null if the Account or HostAuth do
         *         not exist)
         */
        public static String getProtocol(Context context, long accountId) {
            Account account = Account.restoreAccountWithId(context, accountId);
            if (account != null) {
                return account.getProtocol(context);
            }
            return null;
        }
        
        public static String getEmailAddressWithId(Context context, long accountId) {
            Account account = Account.restoreAccountWithId(context, accountId);
            if (account != null) {
                return account.getEmailAddress();
            }
            return null;
        }
        
        public static String getAccountNameWithId(Context context, long accountId) {
            Account account = Account.restoreAccountWithId(context, accountId);
            if (account != null) {
                return account.getDisplayName();
            }
            return null;
        }

        /**
         * Return the account's protocol
         * 
         * @param context the caller's context
         * @return the account's protocol (or null if the HostAuth doesn't not
         *         exist)
         */
        public String getProtocol(Context context) {
            HostAuth hostAuth = HostAuth.restoreHostAuthWithId(context, mHostAuthKeyRecv);
            if (hostAuth != null) {
                return hostAuth.mProtocol;
            }
            return null;
        }

        // adapter_porting
        static public long getAccountIdWhere(Context context, String where, String[] selectionArgs) {
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(CONTENT_URI, ID_PROJECTION, where,
                        selectionArgs, null);
                if (cursor != null && cursor.moveToFirst()) {
                    return cursor.getLong(0); // column 0 is id
                }
            } finally {
                if (cursor != null && !cursor.isClosed())
                    cursor.close();
            }
            return -1;
        }

        /**
         * Return the account ID for a message with a given id
         * 
         * @param context the caller's context
         * @param messageId the id of the message
         * @return the account ID, or -1 if the account doesn't exist
         */
        public static long getAccountIdForMessageId(Context context, long messageId) {
            return Message.getKeyColumnLong(context, messageId, MessageColumns.ACCOUNT_KEY);
        }

        /**
         * Return the account for a message with a given id
         * 
         * @param context the caller's context
         * @param messageId the id of the message
         * @return the account, or null if the account doesn't exist
         */
        public static Account getAccountForMessageId(Context context, long messageId) {
            long accountId = getAccountIdForMessageId(context, messageId);
            if (accountId != -1) {
                return Account.restoreAccountWithId(context, accountId);
            }
            return null;
        }


        /**
         * We have two conditions where we need to put the account on hold
         * i) Pocilies need to be updated
         * ii) An untrusted certificate need to be verified
         */
        public static boolean isSecurityHold(Context context, long accountId) {
            Account account = Account.restoreAccountWithId(context, accountId);
            if((account != null) && (((account.mFlags & Account.FLAGS_SECURITY_HOLD) != 0
                    && !EasITPolicy.getInstance(context).isActive()) ||
                    (EmailFeature.isUntrustedCertificateFeatureEnabled(context)
                            && (account.mFlags & Account.FLAGS_UNTRUSTED_CERTIFICATE) != 0))) {
                return true;
            }
            return false;
        }

        /**
         * Checks whether this account is on security hold because of policies
         * @param context
         * @param accountId
         * @return
         */
        public static boolean isOnlySecurityHold(Context context, long accountId) {
            Account account = Account.restoreAccountWithId(context, accountId);
            if((account != null) && (account.mFlags & Account.FLAGS_SECURITY_HOLD) != 0
                    && !EasITPolicy.getInstance(context).isActive()) {
                return true;
            }
            return false;
        }

        /**
         * Checks whether this account is on security hold because of untrusted certificates
         * @param context
         * @param accountId
         * @return
         */
        public static boolean isOnUntrustedCertificateHold(Context context, long accountId) {
            if(!EmailFeature.isUntrustedCertificateFeatureEnabled(context)) {
                return false;
            }
            Account account = Account.restoreAccountWithId(context, accountId);
            if((account != null) && (account.mFlags & Account.FLAGS_UNTRUSTED_CERTIFICATE) != 0) {
                return true;
            }
            return false;
        }

        /**
         * Check a single account is in Authentication failed .
         */
        public static boolean isAuthFailedHold(Context context, long accountId) {
            Account account = Account.restoreAccountWithId(context, accountId);
            if (account != null) {
                return account.isAuthFailedHold();
            }
            return false;
        }

        public boolean isAuthFailedHold() {
            return (mFlags & Account.FLAGS_AUTH_FAILED_HOLD) != 0;
        }

        /**
         * @return id of the "inbox" mailbox, or -1 if not found.
         */
        public static long getInboxId(Context context, long accountId) {
            return EmailContentUtils.getFirstRowLong(context, Mailbox.CONTENT_URI, ID_PROJECTION,
                    FIND_INBOX_SELECTION, new String[] {
                        Long.toString(accountId)
                    }, null, ID_PROJECTION_COLUMN, -1L);
        }
        
        /**
         * @return id of the "inbox" mailbox, or -1 if not found.
         */
        public static long getSentBoxId(Context context, long accountId) {
            return EmailContentUtils.getFirstRowLong(context, Mailbox.CONTENT_URI, ID_PROJECTION,
                    FIND_SENTBOX_SELECTION, new String[] {
                        Long.toString(accountId)
                    }, null, ID_PROJECTION_COLUMN, -1L);
        }

        
        public static long getMailboxId(Context context, long accountId, int mailboxType) {
            return EmailContentUtils.getFirstRowLong(context, Mailbox.CONTENT_URI, ID_PROJECTION,
                    MailboxColumns.TYPE + " =? " + " AND " + MailboxColumns.ACCOUNT_KEY + " =?",
                    new String[] { Integer.toString(mailboxType), Long.toString(accountId)},
                    null, ID_PROJECTION_COLUMN, -1L);
        }
        
        /**
         * Clear all account hold flags that are set. (This will trigger
         * watchers, and in particular will cause EAS to try and resync the
         * account(s).)
         */
        public static void clearSecurityHoldOnAllAccounts(Context context) {
            ContentResolver resolver = context.getContentResolver();
            Cursor c = null;
            try {
             // change@wtl.akulik start IT Policy 12.0
                c = resolver.query(Account.CONTENT_URI, ACCOUNT_FLAGS_PROJECTION,
                                // WHERE_ACCOUNT_SECURITY_NONZERO, null, null);
                                        null, null, null);
                // change@wtl.akulik end
                if(c != null) {
                    while (c.moveToNext()) {
                        // change@wtl.akulik start IT Policy 12.0
                        long accountId = c.getLong(ACCOUNT_FLAGS_COLUMN_ID);
                        // if (Policies.getNumberOfPoliciesForAccount(context,
                        // accountId) > 0) {
                        int flags = c.getInt(ACCOUNT_FLAGS_COLUMN_FLAGS);
                        if (0 != (flags & Account.FLAGS_SECURITY_HOLD)
                                && (0 == (flags & Account.FLAGS_PASSWORD_EXPIRED))) {
                            ContentValues cv = new ContentValues();
                            cv.put(AccountColumns.FLAGS, flags & ~Account.FLAGS_SECURITY_HOLD);
                            Uri uri = ContentUris.withAppendedId(Account.CONTENT_URI, accountId);
                            resolver.update(uri, cv, null, null);
                        }
                        // }
                        // change@wtl.akulik end
                    }
                }
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        }

        public static void setAuthFailed(Context context, long accountId, boolean loginFailed) {
            Account account = Account.restoreAccountWithId(context, accountId);
            if (account != null) {
                account.setAuthFailed(context, loginFailed);
            }
        }

        public void setAuthFailed(Context context, boolean loginFailed) {
            EmailLog.d(TAG, "setAuthFailed accountId "+ mId + " loginFailed =" + loginFailed);
            ContentValues cv = new ContentValues();
            if (loginFailed == isAuthFailedHold()) {
                // already set
                return;
            }
            if (loginFailed) {
                cv.put(AccountColumns.FLAGS, mFlags | Account.FLAGS_AUTH_FAILED_HOLD);
                mFlags = mFlags | Account.FLAGS_AUTH_FAILED_HOLD;
            } else {
                cv.put(AccountColumns.FLAGS, mFlags & ~Account.FLAGS_AUTH_FAILED_HOLD);
                mFlags = mFlags & ~Account.FLAGS_AUTH_FAILED_HOLD;
            }
            Uri uri = ContentUris.withAppendedId(Account.CONTENT_URI, mId);
            context.getContentResolver().update(uri, cv, null, null);
        }

        /**
         * Override update to enforce a single default account, and do it
         * atomically
         */
        @Override
        public int update(Context context, ContentValues cv) {
            // change@wtl.dtuttle start
            // dr_74: more packing/unpacking work. It's more tricky here since
            // if someone has set
            // a default account, this needs to act as a radio button and
            // disable all of the
            // other "isDefault" items. The google implementation was just
            // blindly erasing the
            // the isDefault flag everywhere, then assigining back to this
            // account. We should
            // be able to use other code to lookup the one and only one other
            // default account and then
            // change that one account if it is not the same one we are using
            // (i.e. eliminate reassign).
            if (context == null || cv == null) return 0;
            if (cv.containsKey(AccountColumns.IS_DEFAULT)
                    && cv.getAsBoolean(AccountColumns.IS_DEFAULT)) {
                ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

                // We need to find the current default account and erase the
                // lower 4 bits in this field for it
                long currentDefaultAccountId;
                if (((currentDefaultAccountId = getDefaultAccountId(context)) != -1)
                        && (currentDefaultAccountId != mId)) {
                    // commit the change
                    ContentValues cv1 = new ContentValues();
                    cv1.put(AccountColumns.IS_DEFAULT, false);
                    //Clear the default flag in all accounts
                    ops.add(ContentProviderOperation.newUpdate(CONTENT_URI).withValues(cv1).build());
                }
                // change@wtl.dtuttle end
                // Update this account
                ops.add(ContentProviderOperation
                        .newUpdate(ContentUris.withAppendedId(CONTENT_URI, mId)).withValues(cv)
                        .build());
                try {
                    context.getContentResolver().applyBatch(AUTHORITY, ops);
                    return 1;
                } catch (RemoteException e) {
                    // There is nothing to be done here; fail by returning 0
                } catch (OperationApplicationException e) {
                    // There is nothing to be done here; fail by returning 0
                }
                return 0;
            }
            return super.update(context, cv);
        }

        /*
         * Override this so that we can store the HostAuth's first and link them
         * to the Account (non-Javadoc)
         * @see
         * com.samsung.android.email.ui.provider.EmailContent#save(android.content.Context)
         */
        @Override
        public Uri save(Context context) {
            if (isSaved()) {
                throw new UnsupportedOperationException();
            }
            // This logic is in place so I can (a) short circuit the expensive
            // stuff when
            // possible, and (b) override (and throw) if anyone tries to call
            // save() or update()
            // directly for Account, which are unsupported.
            if (mHostAuthRecv == null && mHostAuthSend == null && mIsDefault == false) {
                return super.save(context);
            }

            int index = 0;
            int recvIndex = -1;
            int recvCredentialsIndex = -1;
            int sendIndex = -1;
            int sendCredentialsIndex = -1;

            // Create operations for saving the send and recv hostAuths
            // Also, remember which operation in the array they represent
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            if (mHostAuthRecv != null) {
                if (mHostAuthRecv.mCredential != null) {
                    recvCredentialsIndex = index++;
                    ops.add(ContentProviderOperation.newInsert(mHostAuthRecv.mCredential.mBaseUri)
                            .withValues(mHostAuthRecv.mCredential.toContentValues())
                        .build());
                }
                recvIndex = index++;
//                ops.add(ContentProviderOperation.newInsert(mHostAuthRecv.mBaseUri)
//                        .withValues(mHostAuthRecv.toContentValues()).build());
                final ContentProviderOperation.Builder b = ContentProviderOperation.newInsert(
                        mHostAuthRecv.mBaseUri);
                b.withValues(mHostAuthRecv.toContentValues());
                if (recvCredentialsIndex >= 0) {
                    final ContentValues cv = new ContentValues();
                    cv.put(HostAuthColumns.CREDENTIAL_KEY, recvCredentialsIndex);
                    b.withValueBackReferences(cv);
                }
                ops.add(b.build());
            }
            if (mHostAuthSend != null) {
                if (mHostAuthSend.mCredential != null) {
                    if (mHostAuthRecv != null && mHostAuthRecv.mCredential != null &&
                            mHostAuthRecv.mCredential.equals(mHostAuthSend.mCredential)) {
                        // These two credentials are identical, use the same row.
                        sendCredentialsIndex = recvCredentialsIndex;
                    } else {
                        sendCredentialsIndex = index++;
                        ops.add(ContentProviderOperation.newInsert(mHostAuthSend.mCredential.mBaseUri)
                                .withValues(mHostAuthSend.mCredential.toContentValues())
                                .build());
                    }
                }
                sendIndex = index++;
                final ContentProviderOperation.Builder b = ContentProviderOperation.newInsert(
                        mHostAuthSend.mBaseUri);
                b.withValues(mHostAuthSend.toContentValues());
                if (sendCredentialsIndex >= 0) {
                    final ContentValues cv = new ContentValues();
                    cv.put(HostAuthColumns.CREDENTIAL_KEY, sendCredentialsIndex);
                    b.withValueBackReferences(cv);
                }
                ops.add(b.build());
//                ops.add(ContentProviderOperation.newInsert(mHostAuthSend.mBaseUri)
//                        .withValues(mHostAuthSend.toContentValues()).build());
            }

            // Create operations for making this the only default account
            // Note, these are always updates because they change existing
            // accounts
            if (mIsDefault) {
                // change@wtl.dtuttle start
                // dr_75: more packing/unpacking code

                // Again, we need a precision strike, rather than Google's
                // carpet bombing (by erasing this field for
                // all acounts).
                // We need to find the current default account and erase the
                // lower 4 bits in this field for it
                long currentDefaultAccountId;
                if (((currentDefaultAccountId = getDefaultAccountId(context)) != -1)
                        && (currentDefaultAccountId != mId)) {
                    index++;
                    // lookup the account
                    Account modifyMe = restoreAccountWithId(context, currentDefaultAccountId);

                    // commit the change
                    ContentValues cv1 = new ContentValues();
                    /*
                     * cv1.put(AccountColumns.IS_DEFAULT, 0);
                     * ops.add(ContentProviderOperation
                     * .newUpdate(CONTENT_URI).withValues(cv1).build());
                     */
                    cv1.put(AccountColumns.IS_DEFAULT, 0);
                    if(modifyMe != null) {
                        cv1.put(AccountColumns.EMAIL_SIZE, modifyMe.mEmailSize);
                        cv1.put(AccountColumns.ROAMING_EMAIL_SIZE, modifyMe.mRoamingEmailSize);
                    }
                    ops.add(ContentProviderOperation
                            .newUpdate(
                                    ContentUris
                                            .withAppendedId(CONTENT_URI, currentDefaultAccountId))
                            .withValues(cv1).build());
                }
                // change@wtl.dtuttle end
            }

            // Now do the Account
            ContentValues cv = null;
            if (recvIndex >= 0 || sendIndex >= 0) {
                cv = new ContentValues();
                if (recvIndex >= 0) {
                    cv.put(Account.HOST_AUTH_KEY_RECV, recvIndex);
                }
                if (sendIndex >= 0) {
                    cv.put(Account.HOST_AUTH_KEY_SEND, sendIndex);
                }
            }

            ContentProviderOperation.Builder b = ContentProviderOperation.newInsert(mBaseUri);
            b.withValues(toContentValues());
            if (cv != null) {
                b.withValueBackReferences(cv);
            }
            ops.add(b.build());

            try {
                ContentProviderResult[] results = context.getContentResolver().applyBatch(
                        AUTHORITY, ops);
                // If saving, set the mId's of the various saved objects
                if (recvIndex >= 0) {
                    long newId = getId(results[recvIndex].uri);
                    mHostAuthKeyRecv = newId;
                    mHostAuthRecv.mId = newId;
                }
                if (sendIndex >= 0) {
                    long newId = getId(results[sendIndex].uri);
                    mHostAuthKeySend = newId;
                    mHostAuthSend.mId = newId;
                }
                Uri u = results[index].uri;
                mId = getId(u);
                // P5_Porting_Email_Block_Start - worked by minsumr.kim
                // ==============================================================
                // Seven (2010.07.20)
                // ==============================================================

//                accountCB = new AccountCB();
//                accountCB.mId = mId;
//                accountCB.mAccountKey = mId;
//                accountCB.mSevenAccountKey = mSevenAccountKey;
//                accountCB.mTimeLimit = mTimeLimit;
//                accountCB.mTypeMsg = mTypeMsg;
//
//                // 2010.09.07.
//                accountCB.mPeakTime = mPeakTime;
//                accountCB.mOffPeakTime = mOffPeakTime;
//                accountCB.mDays = mDays;
//                accountCB.mPeakStartTime = mPeakStartTime;
//                accountCB.mPeakEndTime = mPeakEndTime;
//                accountCB.mWhileroaming = mWhileroaming;
//                accountCB.mAttachmentEnabled = mAttachmentEnabled;
//
//                if (accountCB != null) {
//                    ops = new ArrayList<ContentProviderOperation>();
//                    ops.add(ContentProviderOperation.newInsert(AccountCB.CONTENT_URI)
//                            .withValues(accountCB.toContentValues()).build());
//                    results = context.getContentResolver().applyBatch(AUTHORITY, ops);
//                }
                // ==============================================================
                // syoungsu.seo@samsung.com : add broadcast method to notify
                // socialHub of changing account - start
                String ACTION = "com.samsung.android.email.ui.action.ACCOUNT_UPDATED";
                Intent intent = new Intent(ACTION);
                context.sendBroadcast(intent);
                // syoungsu.seo@samsung.com : add broadcast method to notify
                // socialHub of changing account - end
                // P5_Porting_Email_Block_End - worked by minsumr.kim
                return u;
            } catch (RemoteException e) {
                // There is nothing to be done here; fail by returning null
            } catch (OperationApplicationException e) {
                // There is nothing to be done here; fail by returning null
            }
            return null;
        }

        // P5_Porting_Email_Block_Start - worked by minsumr.kim
        // change@wtl.akulik smime support start
        /**
         * Gets the smime options flags calculated from sign all and encrypt all
         * options.
         * 
         * @return smime flag
         */
        public int getSmimeFlags() {
            int flags = 0;
            flags = (mSmimeEncryptAll ? 1 : 0) << SMIME_ENCRYPT_ALL_SHIFT;
            flags |= (mSmimeSignAll ? 1 : 0) << SMIME_SIGN_ALL_SHIFT;
            return flags;
        }

        /**
         * Sets the appropriate account's smime options flags depending on the
         * decoded input.
         * 
         * @param flags flags to be decoded and set
         */
        public void setSmimeFlags(int flags) {
            mSmimeEncryptAll = (flags & (1 << SMIME_ENCRYPT_ALL_SHIFT)) != 0;
            mSmimeSignAll = (flags & (1 << SMIME_SIGN_ALL_SHIFT)) != 0;
        }

        // change@wtl.akulik smime support end
        // P5_Porting_Email_Block_End - worked by minsumr.kim

        @Override
        public ContentValues toContentValues() {
            ContentValues values = new ContentValues();
            values.put(AccountColumns.DISPLAY_NAME, mDisplayName);
            values.put(AccountColumns.EMAIL_ADDRESS, mEmailAddress);
            values.put(AccountColumns.SYNC_KEY, mSyncKey);
            values.put(AccountColumns.SYNC_LOOKBACK, mSyncLookback);
            values.put(AccountColumns.SYNC_INTERVAL, mSyncInterval);
            values.put(AccountColumns.HOST_AUTH_KEY_RECV, mHostAuthKeyRecv);
            values.put(AccountColumns.HOST_AUTH_KEY_SEND, mHostAuthKeySend);
            values.put(AccountColumns.FLAGS, mFlags);
            values.put(AccountColumns.IS_DEFAULT, mIsDefault);
            values.put(AccountColumns.COMPATIBILITY_UUID, mCompatibilityUuid);
            values.put(AccountColumns.SENDER_NAME, mSenderName);
            values.put(AccountColumns.RINGTONE_URI, mRingtoneUri);
            values.put(AccountColumns.PROTOCOL_VERSION, mProtocolVersion);
            values.put(AccountColumns.NEW_MESSAGE_COUNT, mNewMessageCount);
            values.put(AccountColumns.SECURITY_FLAGS, mSecurityFlags);
            values.put(AccountColumns.SECURITY_SYNC_KEY, mSecuritySyncKey);
            values.put(AccountColumns.SIGNATURE, mSignature);
            // P5_Porting_Email_Block_Start - worked by minsumr.kim
            // change@wtl.jpshu SyncSchedule begin
            values.put(AccountColumns.EMAIL_SIZE, mEmailSize);
            values.put(AccountColumns.ROAMING_EMAIL_SIZE, mRoamingEmailSize);
            values.put(AccountColumns.CONFLICT_RESOLUTION, mConflictFlags);
            values.put(AccountColumns.POLICY_KEY, mPolicyKey);
            values.put(AccountColumns.PEAK_DAYS, mSyncScheduleData.getPeakDay()); 
            values.put(AccountColumns.PEAK_START_MINUTE, mSyncScheduleData.getStartMinute());
            values.put(AccountColumns.PEAK_END_MINUTE, mSyncScheduleData.getEndMinute());
            values.put(AccountColumns.PEAK_SCHEDULE, mSyncScheduleData.getPeakSchedule());
            values.put(AccountColumns.OFF_PEAK_SCHEDULE, mSyncScheduleData.getOffPeakSchedule());
            values.put(AccountColumns.ROAMING_SCHEDULE, mSyncScheduleData.getRoamingSchedule());
            values.put(AccountColumns.IS_PEAK_SCHEDULE_ON, mSyncScheduleData.getIsPeakScheduleOn());
            // change@wtl.jpshu SyncSchedule end
            // change@wtl.jpshu calendar_sync_lookback
            values.put(AccountColumns.CALENDAR_SYNC_LOOKBACK, mCalendarSyncLookback);
            // change@wtl.rprominski smime support
            values.put(AccountColumns.SMIME_OWN_ENCRYPT_CERT_ALIAS, mSmimeOwnEncryptCertAlias);
            values.put(AccountColumns.SMIME_OWN_SIGN_CERT_ALIAS, mSmimeOwnSignCertAlias);
            // change@wtl.akulik smime support start
            values.put(AccountColumns.SMIME_OPTIONS_FLAGS, getSmimeFlags());
            // change@wtl.akulik smime support end
            // change@siso.vpdj Conversation Text preview start
            values.put(AccountColumns.TEXT_PREVIEW_SIZE, mTextPreviewSize);
            // change@siso.vpdj Conversation Text preview end
            // change@wtl.shatekar Device Information START
            values.put(AccountColumns.DEVICE_INFO_SENT, mDeviceInfoSent);
            // change@wtl.shatekar Device Information END
            // P5_Porting_Email_Block_End - worked by minsumr.kim
            // WTL - CBA - Start
            values.put(AccountColumns.CBA_CERTIFICATE_ALIAS, mCbaCertificateAlias);
            // WTL - CBA - End
            // SVL start vjeswani
            values.put(AccountColumns.MESSAGE_FORMAT, mMessageFormat);
            values.put(AccountColumns.ACCOUNT_TYPE, mAccountType);
            // SVL end vjeswani

            // sh2209.cho : add ICS Settings
            values.put(AccountColumns.FORWARD_WITH_FILES, mForwardWithFiles);
            values.put(AccountColumns.AUTO_DOWNLOAD, mAutoDownload);

            values.put(AccountColumns.RECENT_MESSAGES, mRecentMessages);
            values.put(AccountColumns.SHOW_IMAGE, mShowImage);

            // Change@siso.vinay Auto retry times start
            values.put(AccountColumns.AUTO_RETRY_TIMES, mAutoRetryTimes);
            // Change@siso.vinay Auto retry times end
            values.put(AccountColumns.SMIME_OPTIONS_ENCRYPTION_ALGORITHM, mSmimeEncryptionAlgorithm);
            values.put(AccountColumns.SMIME_OPTIONS_SIGN_ALGORITHM, mSmimeSignAlgorithm);
            values.put(AccountColumns.SMIME_OPTIONS_MESSAGE_SIGN_TYPE, mSmimeMsgSignType);
            
            values.put(AccountColumns.DWONLOAD_ON_SCROLL , mDownloadOnScroll);
            // Change@siso.Naresh Spamfilter Store the Spamfolderif of account
//            values.put(AccountColumns.SPAM_FOLDER_KEY  , mSpamfolderkey);
            values.put(AccountColumns.IS_SIGNATURE_EDITED , mIsSignatureEdited);
            
            values.put(AccountColumns.CANCEL_SENDING_MESSAGE_TIMEOUT, mCancelSendingMessageTimeout); //change@siso.gaurav cancel send

            values.put(AccountColumns.COMPANY_NAME , mCompanyName);
            values.put(AccountColumns.IMAP_DAYS_BASED_SYNC , mImapDaysBasedSync);

            return values;
        }

        /**
         * Supports Parcelable
         */
        public int describeContents() {
            return 0;
        }

        /**
         * Supports Parcelable
         */
        public static final Parcelable.Creator<EmailContent.Account> CREATOR = new Parcelable.Creator<EmailContent.Account>() {
            public EmailContent.Account createFromParcel(Parcel in) {
                return new EmailContent.Account(in);
            }

            public EmailContent.Account[] newArray(int size) {
                return new EmailContent.Account[size];
            }
        };

        /**
         * Supports Parcelable
         */
        public void writeToParcel(Parcel dest, int flags) {
            // mBaseUri is not parceled
            dest.writeLong(mId);
            dest.writeString(mDisplayName);
            dest.writeString(mEmailAddress);
            dest.writeString(mSyncKey);
            dest.writeInt(mSyncLookback);
            dest.writeInt(mSyncInterval);
            dest.writeLong(mHostAuthKeyRecv);
            dest.writeLong(mHostAuthKeySend);
            dest.writeInt(mFlags);
            dest.writeByte(mIsDefault ? (byte) 1 : (byte) 0);
            dest.writeString(mCompatibilityUuid);
            dest.writeString(mSenderName);
            dest.writeString(mRingtoneUri);
            dest.writeString(mProtocolVersion);
            dest.writeInt(mNewMessageCount);
            dest.writeLong(mSecurityFlags);
            dest.writeString(mSecuritySyncKey);
            dest.writeString(mSignature);
            // P5_Porting_Email_Block_Start - worked by minsumr.kim
            // change@wtl.jpshu SyncSchedule begin
            dest.writeInt(mEmailSize);
            dest.writeInt(mRoamingEmailSize);
            dest.writeInt(mConflictFlags);
            dest.writeString(mPolicyKey);
            dest.writeParcelable(mSyncScheduleData, 0); 
            // change@wtl.jpshu SyncSchedule end

            // change@wtl.jpshu calendar_sync_lookback
            dest.writeInt(mCalendarSyncLookback);
            // change@siso.vpdj Conversation Text preview start
            dest.writeInt(mTextPreviewSize);
            // change@siso.vpdj Conversation Text preview end
            // change@wtl.shatekar Device Information START
            dest.writeInt(mDeviceInfoSent);
            // change@wtl.shatekar Device Information END
            // P5_Porting_Email_Block_End - worked by minsumr.kim

            if (mHostAuthRecv != null) {
                dest.writeByte((byte) 1);
                mHostAuthRecv.writeToParcel(dest, flags);
            } else {
                dest.writeByte((byte) 0);
            }

            if (mHostAuthSend != null) {
                dest.writeByte((byte) 1);
                mHostAuthSend.writeToParcel(dest, flags);
            } else {
                dest.writeByte((byte) 0);
            }
            // WTL - CBA - Start
            dest.writeString(mCbaCertificateAlias);
            // WTL - CBA - End

            // SVL start vjeswani
            dest.writeInt(mMessageFormat);
            dest.writeInt(mAccountType);
            // SVL end vjeswani

            // sh2209.cho : add ICS Settings
            dest.writeByte(mForwardWithFiles ? (byte) 1 : (byte) 0);
            dest.writeByte(mAutoDownload ? (byte) 1 : (byte) 0);

            dest.writeInt(mRecentMessages);
            dest.writeByte(mShowImage ? (byte) 1 : (byte) 0);

            // Change@siso.vinay Auto retry times start
            dest.writeInt(mAutoRetryTimes);
            // Change@siso.vinay Auto retry times end
            dest.writeByte(mDownloadOnScroll ? (byte) 1 : (byte) 0);
            
            dest.writeByte(mIsSignatureEdited ? (byte) 1 : (byte) 0);
            dest.writeLong(mCancelSendingMessageTimeout); //change@siso.gaurav cancel send
            
            dest.writeString(mCompanyName);
            dest.writeInt(mImapDaysBasedSync);
        }

        /**
         * Supports Parcelable
         */
        public Account(Parcel in) {
            mBaseUri = EmailContent.Account.CONTENT_URI;
            mId = in.readLong();
            mDisplayName = in.readString();
            mEmailAddress = in.readString();
            mSyncKey = in.readString();
            mSyncLookback = in.readInt();
            mSyncInterval = in.readInt();
            mHostAuthKeyRecv = in.readLong();
            mHostAuthKeySend = in.readLong();
            mFlags = in.readInt();
            mIsDefault = in.readByte() == 1;
            mCompatibilityUuid = in.readString();
            mSenderName = in.readString();
            mRingtoneUri = in.readString();
            mProtocolVersion = in.readString();
            mNewMessageCount = in.readInt();
            mSecurityFlags = in.readLong();
            mSecuritySyncKey = in.readString();
            mSignature = in.readString();
            // P5_Porting_Email_Block_Start - worked by minsumr.kim
            mEmailSize = in.readInt();
            mRoamingEmailSize = in.readInt();
            mConflictFlags = in.readInt();
            mPolicyKey = in.readString();
            mSyncScheduleData = in.readParcelable(SyncScheduleData.class.getClassLoader()); 
            // change@wtl.jpshu calendar_sync_lookback
            mCalendarSyncLookback = in.readInt();
            // change@siso.vpdj Conversation Text preview start
            mTextPreviewSize = in.readInt();
            // change@siso.vpdj Conversation Text preview end
            // change@wtl.shatekar Device Information START
            mDeviceInfoSent = in.readInt();
            // change@wtl.shatekar Device Information END
            // P5_Porting_Email_Block_End - worked by minsumr.kim

            mHostAuthRecv = null;
            if (in.readByte() == 1) {
                mHostAuthRecv = new EmailContent.HostAuth(in);
            }

            mHostAuthSend = null;
            if (in.readByte() == 1) {
                mHostAuthSend = new EmailContent.HostAuth(in);
            }
            // WTL - CBA - Start
            mCbaCertificateAlias = in.readString();
            // WTL - CBA - End

            // SVL start vjeswani
            mMessageFormat = in.readInt();
            mAccountType = in.readInt();
            // SVL end vjeswani

            // sh2209.cho : add ICS Settings
            mForwardWithFiles = in.readByte() == 1;
            mAutoDownload = in.readByte() == 1;

            mRecentMessages = in.readInt();
            mShowImage = in.readByte() == 1;

            // Change@siso.vinay Auto retry times start
            mAutoRetryTimes = in.readInt();
            // Change@siso.vinay Auto retry times end
            mEmailMessageDiffEnabled = (mEmailSize == 11 ? 1 : 0); // MessageDiff
                                                                  // change
                                                                  // @siso.gaurav
            mDownloadOnScroll = in.readByte() == 1;
            mIsSignatureEdited = in.readByte() == 1;
            mCancelSendingMessageTimeout = in.readLong(); //change@siso.gaurav cancel send
            mCompanyName = in.readString();
            mImapDaysBasedSync = in.readInt();
        }

        /**
         * For debugger support only - DO NOT use for code.
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder('[');
            if (mHostAuthRecv != null && mHostAuthRecv.mProtocol != null) {
                sb.append(mHostAuthRecv.mProtocol);
                sb.append(':');
            }
            if (mDisplayName != null)
                sb.append(mDisplayName);
            sb.append(':');
            if (mEmailAddress != null)
                sb.append(mEmailAddress);
            sb.append(':');
            if (mSenderName != null)
                sb.append(mSenderName);
            sb.append(']');
            return sb.toString();
        }

        // SVL Start: j.sb
        // Account Stats information
        public String dumpAccountInfo(Context context) {
            StringBuilder sb = new StringBuilder();
            sb.append("\nAccount details: ")
               .append(mEmailAddress);
            sb.append("\n mId=" + mId)
            .append(" mDisplayName=" +mDisplayName)
            .append(" mProtocolVersion=" + mProtocolVersion)
            .append(" mIsDefault=" + mIsDefault);

            sb.append("\n mHostAuthKeyRecv=" + mHostAuthKeyRecv)
            .append(" mHostAuthKeySend=" + mHostAuthKeySend);

            sb.append("\n mSyncKey=" + mSyncKey).append(" mSyncLookback=" + mSyncLookback)
            .append(" mSyncInterval=" + mSyncInterval)
            .append(" DeletePolicy=" + Integer.toString((mFlags & FLAGS_DELETE_POLICY_MASK) >> FLAGS_DELETE_POLICY_SHIFT));

            sb.append("\n mFlags=" + mFlags)
            .append(" mSecurityFlags=" + mSecurityFlags)
            .append(" mSecuritySyncKey=" + mSecuritySyncKey)
            .append(" mCompatibilityUuid=" + mCompatibilityUuid);

            sb.append("\n mSenderName=" + mSenderName).append(" mRingtoneUri=" + mRingtoneUri);
            if(mRingtoneUri == null){
                sb.append("(" + "Silent" + ")");
            }
            else{
                Ringtone mRingtone = RingtoneManager.getRingtone(context, Uri.parse(mRingtoneUri));
                if (mRingtone != null) {
                    sb.append("(" + mRingtone.getTitle(context) + ")");
                }
            }
            
            sb.append("\n mSignature=" + mSignature)
            .append(" mIsSignatureEdited=" + mIsSignatureEdited);

            sb.append("\n mNewMessageCount=" + mNewMessageCount)
            .append(" mPolicyKey=" + mPolicyKey)
            .append(" mCalendarSyncLookback=" + mCalendarSyncLookback);

            // sb.append("[").append(mLastAccessedFolderId).append('/').append(mTypeMsg).append("]\n");
                        
//            sb.append("\n mTypeMsg=" + mTypeMsg);
            sb.append(" mMessageFormat="+ mMessageFormat)
            .append(" mEmailSize=" + mEmailSize)
            .append(" mRoamingEmailSize=" + mRoamingEmailSize)
            .append(" mAccountType="+ mAccountType)
            .append(" mImapDaysBasedSync=" + mImapDaysBasedSync);

            sb.append("\n mForwardWithFiles=" + mForwardWithFiles)
            .append(" mAutoDownload=" + mAutoDownload)
            .append(" mRecentMessages=" +mRecentMessages)
            .append(" mShowImage=" + mShowImage)
            .append(" mAutoRetryTimes=" + mAutoRetryTimes)
            .append(" mCancelSendingMessageTimeout=" + mCancelSendingMessageTimeout)
            .append("\n mCbaCertificateAlias=" + mCbaCertificateAlias);

            sb.append("\n SyncSchedule: ").append(getSyncScheduleData().toString());

            sb.append("\n Smime Details: ");
            sb.append("\n  mSmimeOwnEncryptCertAlias=" + mSmimeOwnEncryptCertAlias)
            .append(" mSmimeOwnSignCertAlias=" + mSmimeOwnSignCertAlias)
            .append(" flags=" + getSmimeFlags())
            .append(" mSmimeSignAlgorithm=" + mSmimeSignAlgorithm)
            .append(" mSmimeEncryptionAlgorithm="+  mSmimeEncryptionAlgorithm );

            return sb.toString();
        }

        public String dumpHostAuthInfo(HostAuth ha) {
            StringBuilder sb = new StringBuilder();
            if (ha != null) {
                sb.append("HostAuth details: \n").append(" mId=" +ha.mId).append(" mProtocol=" +ha.mProtocol)
                .append(" mAddress=" + ha.mAddress).append('/').append(ha.mPort)
                .append(" mDomain=" +ha.mDomain)
                .append(" mLogin=" +ha.mLogin)
                .append(" mPasswordenc=" +ha.mPasswordenc)
                .append("\n mCapabilities=" + ha.mCapabilities)
                .append("\n mCredentialsKey=" + ha.mCredentialKey);
            } else {
                sb.append("HostAuth details: null \n");
            }
            return sb.toString();
        }
        // SVL End: j.sb
        
        public boolean isSyncable (Context context) {
            android.accounts.Account syncAccount = null;
            boolean syncable;
            if (isEasAccount(context)) {
                syncAccount = new android.accounts.Account(
                        mEmailAddress, AccountManagerTypes.TYPE_EXCHANGE);
            } else if(CarrierValues.IS_CARRIER_CUE && mEmailAddress.contains(EmailFeature.getAccountHintDomain())){ //nauta.cu
                syncAccount = new android.accounts.Account(
                        mEmailAddress, AccountManagerTypes.TYPE_NAUTA);
            } else {
                syncAccount = new android.accounts.Account(
                        mEmailAddress, AccountManagerTypes.TYPE_POP_IMAP);
            }
            syncable = ContentResolver.getSyncAutomatically(syncAccount, AUTHORITY);
            EmailLog.d(TAG, "account : " + String.valueOf(mId) + " Syncable + " + syncable);
            return syncable;
        }

        public void setSignatureEdited(boolean isSignatureEdited) {
            mIsSignatureEdited = isSignatureEdited;
        }

        public boolean isSignatureEdited() {
            return mIsSignatureEdited;
        }
        
        public void setCompanyName(String companyname) {
            mCompanyName = companyname;
        }
        
        public String getCompanyName() {
            return mCompanyName;
        }
        
        /**
         * isPushScheduledNow - Check Push is scheduled for this account
         *
         * @param account
         * @return
         */
        public boolean isPushScheduledNow(Context context) {
            SyncScheduleData syncSchedule = getSyncScheduleData();
            boolean isPeak = SyncScheduler.getIsPeakAndNextAlarm(syncSchedule).first;
            boolean ret = true;

            if ((isPeak && syncSchedule.getPeakSchedule() != CHECK_INTERVAL_PUSH)
                    || (!isPeak && syncSchedule.getOffPeakSchedule() != CHECK_INTERVAL_PUSH)) {
                ret = false;
            }
            // get roaming information
            boolean isRoaming = Utility.isRoaming(context);
            
            if (isRoaming) {
                int roamingSchedule = syncSchedule.getRoamingSchedule();
                if (roamingSchedule == CHECK_ROAMING_MANUAL) {
                    return false;
                }
                // Other option to follow the current schedule!
            }
            return ret;
        }

        public boolean isDownloadOnlyViaWifiOn() {
            return (mFlags & FLAGS_DOWNLOAD_ONLY_VIA_WIFI) != 0 ? true : false;
        }
    }

    public interface AttachmentColumns {
        public static final String ID = "_id";

        // The display name of the attachment
        public static final String FILENAME = "fileName";

        // The mime type of the attachment
        public static final String MIME_TYPE = "mimeType";

        // The size of the attachment in bytes
        public static final String SIZE = "size";

        // The (internal) contentId of the attachment (inline attachments will
        // have these)
        public static final String CONTENT_ID = "contentId";

        // The location of the loaded attachment (probably a file)
        public static final String CONTENT_URI = "contentUri";

        // A foreign key into the Message table (the message owning this
        // attachment)
        public static final String MESSAGE_KEY = "messageKey";

        // The location of the attachment on the server side
        // For IMAP, this is a part number (e.g. 2.1); for EAS, it's the
        // internal file name
        public static final String LOCATION = "location";

        // The transfer encoding of the attachment
        public static final String ENCODING = "encoding";

        // Not currently used
        public static final String CONTENT = "content";

        // Flags
        public static final String FLAGS = "flags";

        // Content that is actually contained in the Attachment row
        public static final String CONTENT_BYTES = "content_bytes";

        // A foreign key into the Account table (for the message owning this
        // attachment)
        public static final String ACCOUNT_KEY = "accountKey";

        // Voicemail support
        public static final String VOICEMAIL_ATT_ORDER = "vmAttOrder";

        public static final String VOICEMAIL_ATT_DURATION = "vmAttDuration";

        // For support eas 12.1 IS_INLINE tag
        public static final String ISINLINE = "isInline";

        // The size(in bytes) of the encoded input stream from which the
        // attachment is read
        public static final String ENCODED_SIZE = "encodedSize";
    }

    public static final class Attachment extends EmailContent implements AttachmentColumns,
            Parcelable {

        public static final String TABLE_NAME = "Attachment";

        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI + "/attachment");
        public static final Uri CONTENT_URI_MULTI = Uri.parse(EmailContent.CONTENT_URI_MULTI + "/attachment");

        // This must be used with an appended id:
        // ContentUris.withAppendedId(MESSAGE_ID_URI, id)
        public static final Uri MESSAGE_ID_URI = Uri.parse(EmailContent.CONTENT_URI
                + "/attachment/message");

        public String mFilePath;
        public String mFileName;
        public String mMimeType;

        public long mSize;

        public String mContentId;
        public String mContentUri;

        public long mMessageKey;

        public String mLocation;
        public String mEncoding;
        public String mContent; // Not currently used

        public int mFlags;

        public byte[] mContentBytes;

        public long mAccountKey;

        public int mVoiceMailAttDuration;
        public int mVoiceMailAttOrder;

        // For support eas 12.1 IS_INLINE tag
        public int mIsInline;

        // Save the size(in bytes) of the encoded input stream from which the
        // attachment is read.
        public String mEncodedSize;

        public static final int CONTENT_ID_COLUMN = 0;
        public static final int CONTENT_FILENAME_COLUMN = 1;
        public static final int CONTENT_MIME_TYPE_COLUMN = 2;
        public static final int CONTENT_SIZE_COLUMN = 3;
        public static final int CONTENT_CONTENT_ID_COLUMN = 4;
        public static final int CONTENT_CONTENT_URI_COLUMN = 5;
        public static final int CONTENT_MESSAGE_ID_COLUMN = 6;
        public static final int CONTENT_LOCATION_COLUMN = 7;
        public static final int CONTENT_ENCODING_COLUMN = 8;
        public static final int CONTENT_CONTENT_COLUMN = 9; // Not currently
                                                            // used
        public static final int CONTENT_FLAGS_COLUMN = 10;
        public static final int CONTENT_CONTENT_BYTES_COLUMN = 11;
        public static final int CONTENT_ACCOUNT_KEY_COLUMN = 12;
        public static final int CONTENT_VOICEMAIL_ATT_ORDER = 13;
        public static final int CONTENT_VOICEMAIL_ATT_DURATION = 14;
        public static final int CONTENT_ISINLINE = 15;
        public static final int CONTENT_ENCODED_SIZE_COLUMN = 16;

        public static final String[] CONTENT_PROJECTION = new String[] {
                RECORD_ID, AttachmentColumns.FILENAME, AttachmentColumns.MIME_TYPE,
                AttachmentColumns.SIZE, AttachmentColumns.CONTENT_ID,
                AttachmentColumns.CONTENT_URI, AttachmentColumns.MESSAGE_KEY,
                AttachmentColumns.LOCATION, AttachmentColumns.ENCODING, AttachmentColumns.CONTENT,
                AttachmentColumns.FLAGS, AttachmentColumns.CONTENT_BYTES,
                AttachmentColumns.ACCOUNT_KEY, AttachmentColumns.VOICEMAIL_ATT_ORDER,
                AttachmentColumns.VOICEMAIL_ATT_DURATION, AttachmentColumns.ISINLINE,
                AttachmentColumns.ENCODED_SIZE
        };

        // Bits used in mFlags Instruct Rfc822Output to
        // 1) not use Content-Disposition and
        // 2) use multipart/alternative with this attachment.
        // This is only valid if there is one and only one attachment and that
        // attachment has this flag set

        public static final int FLAG_ICS_ALTERNATIVE_PART = 1 << 0;

        // Indicate that this attachment has been requested for downloading by
        // the user; this is the highest priority for attachment downloading
        public static final int FLAG_DOWNLOAD_USER_REQUEST = 1 << 1;

        // Indicate that this attachment needs to be downloaded as part of an
        // outgoing forwarded message
        public static final int FLAG_DOWNLOAD_FORWARD = 1 << 2;

        // Indicates that the attachment download failed in a non-recoverable
        // manner (Currently used processing HTTP process 413 error)
        public static final int FLAG_DOWNLOAD_FAILED = 1 << 3;

        // add for smartforward, mineng.kwon 20100110 @samsung
        public static final int FLAG_FORWARD_ORIGINAL_ATTACHMENT = 1 << 4;

        // add for SaveAll, 20110719 - MASONE
        public static final int FLAG_DOWNLOAD_AND_SAVE = 1 << 5;

        // add for SaveAll cancel, 20110719 - MASONE
        public static final int FLAG_CANCEL_DOWNLOADALL = 1 << 6;

        // Allow "room" for some additional download-related flags here
        // Indicates that the attachment will be smart-forwarded
        public static final int FLAG_SMART_FORWARD = 1 << 8;

        public static final int FLAG_DOWNLOAD_COMPLETE = 1 << 9;

        public static final int FLAG_DOWNLOAD_FROM_SHOWPICTURE = 1 << 10;

        // For S-Pen Drawing
        public static final int FLAG_INLINE_ATTACHMENT_SDRAWING = 1 << 11;

        // For attachment session recover feature
        public static final int FLAG_ATTACHMENT_SESSION_RECOVERY = 1 << 12;

        // For attachment cancel in sync
        public static final int FLAG_ATTACHMENT_CANCELED_AS_SYNCMODE = 1 << 13;

        // This flag is for VZW feature that enables attachement downloads via
        // Wi-Fi only. See function EmailFeature.isDownloadOnlyViaWifiEnabled()
        // for details
        public static final int FLAG_DOWNLOAD_WIFI_ONLY = 1 << 14;
        
        public static final int FLAG_IMAGE_AUTO_DOWNLOAD = 1 << 15;
        
        public static final int FLAG_DOWNLOAD_FROM_GEAR = 1 << 16;
        
        public static final int FLAG_AVAILABLE_IMAGE_RESIZING = 1 << 17;
        public static final int IS_NORMAL_ATTACHMENT = 0;

        public static final int IS_INLINE_ATTACHMENT = 1;

//        public static final String EMPTY_URI_SELECTION_WITHOUT_INSERT = AttachmentColumns.CONTENT_URI
//                + " isnull AND " + Attachment.FLAGS + "=0";

        public static final String EMPTY_URI_SELECTION = AttachmentColumns.CONTENT_URI
                + " isnull AND " + Attachment.FLAGS + "=0";

        // Attachments with an empty URI that are in an inbox
        public static final String EMPTY_URI_INBOX_SELECTION = EMPTY_URI_SELECTION + " AND "
                + AttachmentColumns.MESSAGE_KEY + " IN (" + "SELECT " + MessageColumns.ID
                + " FROM " + Message.TABLE_NAME + " WHERE " + Message.INBOX_SELECTION + ")";

        /**
         * no public constructor since this is a utility class
         */
        public Attachment() {
            mBaseUri = CONTENT_URI;
        }

        /**
         * Restore an Attachment from the database, given its unique id
         *
         * @param context
         * @param id
         * @return the instantiated Attachment
         */
        public static Attachment restoreAttachmentWithId(Context context, long id) {
            Uri u = ContentUris.withAppendedId(Attachment.CONTENT_URI, id);
            Cursor c = null;
            try {
                c = context.getContentResolver().query(u, Attachment.CONTENT_PROJECTION, null,
                        null, null);
                if (c != null && c.moveToFirst()) {
                    return getContent(c, Attachment.class);
                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        }

        public static Attachment restoreAttachmentWithAccIdAndLocation(Context context, long accId,
                String location) {
            if (accId < 0 || location == null || location.length() <= 0) {
                return null;
            }
            Cursor c = null;
            try {
                c = context.getContentResolver().query(
                        Attachment.CONTENT_URI,
                        Attachment.CONTENT_PROJECTION,
                        AttachmentColumns.ACCOUNT_KEY + "=? and " + AttachmentColumns.LOCATION
                                + "=?", new String[] {
                                Long.toString(accId), location
                        }, null);
                if (c != null && c.moveToFirst()) {
                    return getContent(c, Attachment.class);
                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        }
        
        public static Attachment restoreAttachmentWithMessageIdAndLocation(Context context, long messageId,
                String location) {
            if (messageId < 0 || location == null || location.length() <= 0) {
                return null;
            }
            Cursor c = null;
            try {
                c = context.getContentResolver().query(
                        Attachment.CONTENT_URI,
                        Attachment.CONTENT_PROJECTION,
                        AttachmentColumns.MESSAGE_KEY + "=? and " + AttachmentColumns.LOCATION
                                + "=?", new String[] {
                                Long.toString(messageId), location
                        }, null);
                if (c != null && c.moveToFirst()) {
                    return getContent(c, Attachment.class);
                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        }

        /**
         * Restore all the Attachments of a message given its messageId
         */
        public static Attachment[] restoreAttachmentsWithMessageId(Context context, long messageId) {
            Uri uri = ContentUris.withAppendedId(MESSAGE_ID_URI, messageId);
            Cursor c = null;
            try {
                c = context.getContentResolver().query(uri, CONTENT_PROJECTION, null, null, null);
                
                if(c != null) {
                    int count = c.getCount();
                    Attachment[] attachments = new Attachment[count];
                    for (int i = 0; i < count; ++i) {
                        c.moveToNext();
                        Attachment attach = new Attachment();
                        attach.restore(c);
                        attachments[i] = attach;
                    }
                    return attachments;
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    if (c != null) c.close();
                } catch(Exception e2) {}
            }
        }

        /**
         * Restore all the Attachments of a Account given its accountId
         */
        public static Attachment[] restoreAttachmentsWithAccountId(Context context, long accId) {

            if (accId < 0) {
                return null;
            }
            Cursor c = null;
            try {
                c = context.getContentResolver().query(
                        Attachment.CONTENT_URI, 
                        Attachment.CONTENT_PROJECTION, 
                        AttachmentColumns.ACCOUNT_KEY + "=? ",//AND " + AttachmentColumns.CONTENT_URI +  " IS NOT NULL", 
                        new String[] { Long.toString(accId) }, 
                        null);
                
                if(c != null) {
                    int count = c.getCount();
                    Attachment[] attachments = new Attachment[count];
                    for (int i = 0; i < count; ++i) {
                        c.moveToNext();
                        Attachment attach = new Attachment();
                        attach.restore(c);
                        attachments[i] = attach;
                    }
                    return attachments;
                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        }

        /**
         * Calculates Attachments count for of a message given its messageId
         */
        public static int getAttachmentsCountWithMessageId(Context context, long messageId) {
            int result = 0;
            Uri uri = ContentUris.withAppendedId(MESSAGE_ID_URI, messageId);
            Cursor c = null;
            try {
                c = context.getContentResolver().query(uri, CONTENT_PROJECTION, null, null, null);
                if (c!= null) {
                    result = c.getCount(); 
                }
            } catch (Exception e) {
                Log.dumpException(TAG, e);
            } finally {
                try {
                    if (c != null) c.close();
                } catch(Exception e2) {}
            }
            
            return result;
        }
        
        /**
         * Creates a unique file in the external store by appending a hyphen and
         * a number to the given filename.
         *
         * @param filename
         * @return a new File object, or null if one could not be created
         */
        public static File createUniqueFile(String filename) {
            // TODO Handle internal storage, as required
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                // lgs_100713 modify file path
                String targetDir = Environment.getExternalStorageDirectory().toString()
                        + "/download/";
                File directory = new File(targetDir); // Environment.getExternalStorageDirectory();

                File file = new File(directory, filename);
                if (!file.exists()) {
                    return file;
                }

                // Get the extension of the file, if any.
                int index = filename.lastIndexOf('.');

                String name = filename;
                String extension = "";

                if (index != -1) {
                    name = filename.substring(0, index);
                    extension = filename.substring(index);
                }

                StringBuffer tempFileName = new StringBuffer();

                for (int i = 2; i < Integer.MAX_VALUE; i++) {
                    file = new File(directory, tempFileName.append(name).append('-').append(i)
                            .append(extension).toString());

                    if (!file.exists()) {
                        return file;
                    }
                }
                return null;
            }
            return null;
        }

        @Override
        public void restore(Cursor cursor) {
            mBaseUri = CONTENT_URI;
            mId = cursor.getLong(CONTENT_ID_COLUMN);
            mFileName = cursor.getString(CONTENT_FILENAME_COLUMN);
            mMimeType = cursor.getString(CONTENT_MIME_TYPE_COLUMN);
            mSize = cursor.getLong(CONTENT_SIZE_COLUMN);
            mEncodedSize = cursor.getString(CONTENT_ENCODED_SIZE_COLUMN);
            mContentId = cursor.getString(CONTENT_CONTENT_ID_COLUMN);
            mContentUri = cursor.getString(CONTENT_CONTENT_URI_COLUMN);
            mMessageKey = cursor.getLong(CONTENT_MESSAGE_ID_COLUMN);
            mLocation = cursor.getString(CONTENT_LOCATION_COLUMN);
            mEncoding = cursor.getString(CONTENT_ENCODING_COLUMN);
            mContent = cursor.getString(CONTENT_CONTENT_COLUMN);
            mFlags = cursor.getInt(CONTENT_FLAGS_COLUMN);
            mContentBytes = cursor.getBlob(CONTENT_CONTENT_BYTES_COLUMN);
            mAccountKey = cursor.getLong(CONTENT_ACCOUNT_KEY_COLUMN);
            mVoiceMailAttOrder = cursor.getInt(CONTENT_VOICEMAIL_ATT_ORDER);
            mVoiceMailAttDuration = cursor.getInt(CONTENT_VOICEMAIL_ATT_DURATION);
            mIsInline = cursor.getInt(CONTENT_ISINLINE);
        }

        @Override
        public ContentValues toContentValues() {
            ContentValues values = new ContentValues();
            values.put(AttachmentColumns.FILENAME, mFileName);
            values.put(AttachmentColumns.MIME_TYPE, mMimeType);
            values.put(AttachmentColumns.SIZE, mSize);
            values.put(AttachmentColumns.ENCODED_SIZE, mEncodedSize);
            values.put(AttachmentColumns.CONTENT_ID, mContentId);
            values.put(AttachmentColumns.CONTENT_URI, mContentUri);
            values.put(AttachmentColumns.MESSAGE_KEY, mMessageKey);
            values.put(AttachmentColumns.LOCATION, mLocation);
            values.put(AttachmentColumns.ENCODING, mEncoding);
            values.put(AttachmentColumns.CONTENT, mContent);
            values.put(AttachmentColumns.FLAGS, mFlags);
            values.put(AttachmentColumns.CONTENT_BYTES, mContentBytes);
            values.put(AttachmentColumns.ACCOUNT_KEY, mAccountKey);
            values.put(AttachmentColumns.VOICEMAIL_ATT_ORDER, mVoiceMailAttOrder);
            values.put(AttachmentColumns.VOICEMAIL_ATT_DURATION, mVoiceMailAttDuration);
            values.put(AttachmentColumns.ISINLINE, mIsInline);

            return values;
        }

        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            // mBaseUri is not parceled
            dest.writeLong(mId);
            dest.writeString(mFileName);
            dest.writeString(mMimeType);
            dest.writeLong(mSize);
            dest.writeString(mEncodedSize);
            dest.writeString(mContentId);
            dest.writeString(mContentUri);
            dest.writeLong(mMessageKey);
            dest.writeString(mLocation);
            dest.writeString(mEncoding);
            dest.writeString(mContent);
            dest.writeInt(mFlags);
            dest.writeLong(mAccountKey);
            if (mContentBytes == null) {
                dest.writeInt(-1);
            } else {
                dest.writeInt(mContentBytes.length);
                dest.writeByteArray(mContentBytes);
            }
            dest.writeInt(mVoiceMailAttOrder);
            dest.writeInt(mVoiceMailAttDuration);
            dest.writeInt(mIsInline);
        }

        public Attachment(Parcel in) {
            mBaseUri = EmailContent.Attachment.CONTENT_URI;
            mId = in.readLong();
            mFileName = in.readString();
            mMimeType = in.readString();
            mSize = in.readLong();
            mEncodedSize = in.readString();
            mContentId = in.readString();
            mContentUri = in.readString();
            mMessageKey = in.readLong();
            mLocation = in.readString();
            mEncoding = in.readString();
            mContent = in.readString();
            mFlags = in.readInt();
            mAccountKey = in.readLong();
            final int contentBytesLen = in.readInt();
            if (contentBytesLen == -1) {
                mContentBytes = null;
            } else {
                mContentBytes = new byte[contentBytesLen];
                in.readByteArray(mContentBytes);
            }
            mVoiceMailAttOrder = in.readInt();
            mVoiceMailAttDuration = in.readInt();
            mIsInline = in.readInt();
        }

        public static final Parcelable.Creator<EmailContent.Attachment> CREATOR = new Parcelable.Creator<EmailContent.Attachment>() {
            @Override
            public EmailContent.Attachment createFromParcel(Parcel in) {
                return new EmailContent.Attachment(in);
            }

            @Override
            public EmailContent.Attachment[] newArray(int size) {
                return new EmailContent.Attachment[size];
            }
        };

        public long getEncodedSize() {
            long encodedSize = 0;
            if (mEncodedSize != null) {
                String[] part = mEncodedSize.split("/");
                if (part.length == 2) {
                    encodedSize = Long.valueOf(part[1]);
                }
            }
            return encodedSize;
        }

        public long getDownloadedSize() {
            long downloadedSize = 0;
            if (mEncodedSize != null) {
                String[] part = mEncodedSize.split("/");
                if (part.length == 2) {
                    downloadedSize = Long.valueOf(part[0]);
                }
            }
            return downloadedSize;
        }

        @Override
        public String toString() {
            return "[" + mId + ", " + mFileName + ", " + mMimeType + ", " + mSize + ", "
                    + mContentId + ", " + mContentUri + ", " + mMessageKey + ", " + mLocation
                    + ", " + mEncoding + ", " + mFlags + ", " + mContentBytes + ", " + mAccountKey
                    + ", " + mIsInline + ", " + mEncodedSize + "]";
        }
    }

    // sh2209.cho Quick Response
    public interface QuickResponseColumns {
        // The QuickResponse text
        static final String TEXT = "quickResponse";
        // A foreign key into the Account table owning the QuickResponse
        static final String ACCOUNT_KEY = "accountKey";
        // Predefined quick response number (0 for user created quick responses,
        // 1-10 for predefined)
        static final String PREDEFINED_RESPONSE_NUMBER = "isPredefined";
        // Indicates predefined response edited by user (we will not translate
        // such responses)
        static final String IS_EDITED_PREDEFINED = "isEditedPredefined";
    }

    public interface MailboxColumns {
        // The display name of this mailbox [INDEX]
        public static final String ID = "_id";

        static final String DISPLAY_NAME = "displayName";

        // change@siso.julka folder operations change start
        static final String DISPLAY_NAME_AJ = "displayname";

        // change@siso.julka folder operations change end
        // The server's identifier for this mailbox
        public static final String SERVER_ID = "serverId";

        // The server's identifier for the parent of this mailbox (null =
        // top-level)
        public static final String PARENT_SERVER_ID = "parentServerId";

        // A foreign key for the parent fo this mailbox (-1 = top-level, 0 =
        // uninitialized
        public static final String PARENT_KEY = "parentKey";

        // A foreign key to the Account that owns this mailbox
        public static final String ACCOUNT_KEY = "accountKey";

        // The type (role) of this mailbox
        public static final String TYPE = "type";

        // The hierarchy separator character
        public static final String DELIMITER = "delimiter";

        // Server-based sync key or validity marker (e.g. "SyncKey" for EAS,
        // "uidvalidity" for IMAP)
        public static final String SYNC_KEY = "syncKey";

        // The sync lookback period for this mailbox (or null if using the
        // account default)
        public static final String SYNC_LOOKBACK = "syncLookback";

        // The sync frequency for this mailbox (or null if using the account
        // default)
        public static final String SYNC_INTERVAL = "syncInterval";

        // The time of last successful sync completion (millis)
        public static final String SYNC_TIME = "syncTime";

        // Thre time of last requested sync time (millis)
        public static final String SYNC_REQUESTED_TIME = "syncRequestedTime";
        
        // Cached unread count
        public static final String UNREAD_COUNT = "unreadCount";

        // Visibility of this folder in a list of folders [INDEX]
        public static final String FLAG_VISIBLE = "flagVisible";

        // Other states, as a bit field, e.g. CHILDREN_VISIBLE, HAS_CHILDREN
        public static final String FLAGS = "flags";

        // Backward compatible
        public static final String VISIBLE_LIMIT = "visibleLimit";

        // Sync status (can be used as desired by sync services)
        public static final String SYNC_STATUS = "syncStatus";

        // Number of messages in the mailbox.
        public static final String MESSAGE_COUNT = "messageCount";

        // change@wtl.jrabina Folder Commands start
        public static final String FLAG_CHANGED = "flagChanged";

        public static final String DST_MAILBOX_ID = "dstMailboxId";

        public static final String NEW_DISPLAY_NAME = "newDisplayName";

        // change@wtl.jrabina Folder Commands end

        // by taesoo77.lee adapter_work moved from U1 db field
        public static final String FLAG_NOSELECT = "flagNoSelect";

        public static final String LAST_TOUCHED_TIME = "lastTouchedTime";

        // kanchan@SISO adding for mailbox sync options start
        public static final String OFFPEAK_SYNC_SCHEDULE = "offpeakSyncSchedule";
        public static final String PEAK_SYNC_SCHEDULE = "peakSyncSchedule";
        public static final String REF_SYNC_INTERVAL = "SyncIntervalReference";
        // kanchan@SISO adding for mailbox sync options end
    }

    public static final class Mailbox extends EmailContent implements SyncColumns, MailboxColumns, AccountValues.SyncTime,
            Cloneable {
        public static final String TABLE_NAME = "Mailbox";

        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI + "/mailbox");

        public static final Uri CONTENT_URI_MULTI = Uri.parse(EmailContent.CONTENT_URI_MULTI + "/mailbox");

        public static final Uri CONTENT_URI_COUNT = Uri.parse(EmailContent.CONTENT_URI + "/mailbox/messagecount");

        public static final Uri ADD_TO_FIELD_URI = Uri.parse(EmailContent.CONTENT_URI
                + "/mailboxIdAddToField");
        
        public static final Uri NOTIFIER_URI_INBOX_ADDED = Uri.parse(EmailContent.CONTENT_NOTIFIER_URI
                + "/inboxadded");

        public String mDisplayName;

        public String mServerId;

        public String mParentServerId;

        public long mParentKey;

        public long mAccountKey;

        public int mType;

        public int mDelimiter;

        public String mSyncKey;

        public int mSyncLookback;

        public int mSyncInterval;

        // kanchan@SISO adding for mailbox sync options start
        public int mOffpeakSyncSchedule;
        public int mPeakSyncSchedule;
        // kanchan@SISO adding for mailbox sync options end

        public long mSyncTime;

        public boolean mFlagVisible = true;

        public boolean mFlagNoSelect = false;

        public int mFlags;

        public int mVisibleLimit;

        public String mSyncStatus;

        // change@wtl.jrabina Folder Commands start
        public int mFlagChanged;

        public String mDstServerId;

        public String mNewDisplayName;

        public String mLastTouchedTime;

        // change@wtl.jrabina Folder Commands start
        // msc, hanklee, 2010.07.28
        public int mTypeMsg;

        public long mMailboxKey;

        public long mSevenMailboxKey;

        public int mSyncFlag;

        public long tag;

        public static final int CONTENT_ID_COLUMN = 0;

        public static final int CONTENT_DISPLAY_NAME_COLUMN = 1;

        public static final int CONTENT_SERVER_ID_COLUMN = 2;

        public static final int CONTENT_PARENT_SERVER_ID_COLUMN = 3;

        public static final int CONTENT_ACCOUNT_KEY_COLUMN = 4;

        public static final int CONTENT_TYPE_COLUMN = 5;

        public static final int CONTENT_DELIMITER_COLUMN = 6;

        public static final int CONTENT_SYNC_KEY_COLUMN = 7;

        public static final int CONTENT_SYNC_LOOKBACK_COLUMN = 8;

        public static final int CONTENT_SYNC_INTERVAL_COLUMN = 9;

        public static final int CONTENT_SYNC_TIME_COLUMN = 10;

        public static final int CONTENT_FLAG_VISIBLE_COLUMN = 11;

        public static final int CONTENT_FLAG_NOSELECT_COLUMN = 12;

        public static final int CONTENT_FLAGS_COLUMN = 13;

        public static final int CONTENT_VISIBLE_LIMIT_COLUMN = 14;

        public static final int CONTENT_SYNC_STATUS_COLUMN = 15;

        // change@wtl.jrabina Folder Commands start
        public static final int CONTENT_FLAG_CHANGED = 16;

        public static final int CONTENT_DST_SERVER_ID = 17;

        public static final int CONTENT_NEW_DISPLAY_NAME = 18;

        public static final int CONTENT_PARENT_KEY_COLUMN = 19;

        public static final int CONTENT_NEW_TOUCHED_TIME = 20;

        // kanchan@SISO adding for mailbox sync options start
        public static final int CONTENT_OFFPEAK_SYNC_SCHEDULE_COLUMN = 21;
        public static final int CONTENT_PEAK_SYNC_SCHEDULE_COLUMN = 22;
        // kanchan@SISO adding for mailbox sync options end

        public static final String[] CONTENT_PROJECTION = new String[] {
                RECORD_ID, 
                MailboxColumns.DISPLAY_NAME, 
                MailboxColumns.SERVER_ID,
                MailboxColumns.PARENT_SERVER_ID, 
                MailboxColumns.ACCOUNT_KEY, 
                MailboxColumns.TYPE,
                MailboxColumns.DELIMITER, 
                MailboxColumns.SYNC_KEY, 
                MailboxColumns.SYNC_LOOKBACK,
                MailboxColumns.SYNC_INTERVAL,
                
                MailboxColumns.SYNC_TIME,
                MailboxColumns.FLAG_VISIBLE,
                MailboxColumns.FLAG_NOSELECT, 
                MailboxColumns.FLAGS,
                MailboxColumns.VISIBLE_LIMIT,
                MailboxColumns.SYNC_STATUS,
                MailboxColumns.FLAG_CHANGED, 
                MailboxColumns.DST_MAILBOX_ID, 
                MailboxColumns.NEW_DISPLAY_NAME, 
                MailboxColumns.PARENT_KEY, 
                
                MailboxColumns.LAST_TOUCHED_TIME
                , MailboxColumns.OFFPEAK_SYNC_SCHEDULE, 
                MailboxColumns.PEAK_SYNC_SCHEDULE 
        };

        // P5_Porting_Email_Block_End - worked by minsumr.kim

        private static final String ACCOUNT_AND_MAILBOX_TYPE_SELECTION = MailboxColumns.ACCOUNT_KEY
                + " =? AND " + MailboxColumns.TYPE + " =?";
        private static final String MAILBOX_TYPE_SELECTION = MailboxColumns.TYPE + " =?";
        private static final String MAILBOX_ID_SELECTION = MailboxColumns.ID + " =?";

        private static final String[] MAILBOX_SUM_OF_UNREAD_COUNT_PROJECTION = new String[] {
            "sum(" + MailboxColumns.UNREAD_COUNT + ")"
        };

        private static final int UNREAD_COUNT_COUNT_COLUMN = 0;

        private static final String[] MAILBOX_SUM_OF_MESSAGE_COUNT_PROJECTION = new String[] {
            "sum(" + MailboxColumns.MESSAGE_COUNT + ")"
        };
        
        private static final String[] MAILBOX_MESSAGE_COUNT_PROJECTION = new String[] {
            MailboxColumns.MESSAGE_COUNT
        };
        
        private static final int MESSAGE_COUNT_COUNT_COLUMN = 0;

        public static final long NO_MAILBOX = -1;

        // Sentinels for PARENT_KEY
        public static final long PARENT_KEY_NONE = -1L;

        public static final long PARENT_KEY_UNINITIALIZED = 0L;

        private static final String WHERE_TYPE_AND_ACCOUNT_KEY = MailboxColumns.TYPE + "=? and "
                + MailboxColumns.ACCOUNT_KEY + "=?";

        public static final String MOVE_TO_TARGET_MAILBOX_SELECTION = MailboxColumns.TYPE
                + " NOT IN (" + Mailbox.TYPE_DRAFTS + "," + Mailbox.TYPE_OUTBOX + "," 
                + Mailbox.TYPE_SCHEDULED_OUTBOX + ","
                + Mailbox.TYPE_SENT + "," + Mailbox.TYPE_SEARCH_RESULTS + ")";

        public static final Integer[] INVALID_DROP_TARGETS = new Integer[] {
                Mailbox.TYPE_DRAFTS, Mailbox.TYPE_OUTBOX, Mailbox.TYPE_SENT,
                Mailbox.TYPE_SEARCH_RESULTS, Mailbox.TYPE_SCHEDULED_OUTBOX
        };

        public static final Integer VALID_SMS_DROP_TARGET = Mailbox.TYPE_TRASH; // jykim
        // sms
        // sync

        // MNO_LAB Start : a1.sangiah
        // Change for Search folder is displayed in the folder list upon search
        // completion
        public static final String USER_VISIBLE_MAILBOX_SELECTION = MailboxColumns.TYPE + "<"
                + Mailbox.TYPE_NOT_EMAIL + " AND " + MailboxColumns.TYPE + "!="
                + Mailbox.TYPE_SEARCH_RESULTS + " AND " + MailboxColumns.FLAG_VISIBLE + "=1";
        // MNO_LAB End
        
        public static final String MAILBOX_ORDER_BY = "CASE " + MailboxColumns.TYPE + " WHEN "
                + Mailbox.TYPE_INBOX + " THEN 0" + " WHEN " + Mailbox.TYPE_DRAFTS + " THEN 1"
                + " WHEN " + Mailbox.TYPE_OUTBOX + " THEN 2" + " WHEN " + Mailbox.TYPE_SCHEDULED_OUTBOX + " THEN 3" 
                + " WHEN " + Mailbox.TYPE_SENT + " THEN 4" + " WHEN " + Mailbox.TYPE_TRASH + " THEN 5" 
                + " WHEN " + Mailbox.TYPE_JUNK + " THEN 6" +
                // Other mailboxes (i.e. of Mailbox.TYPE_MAIL) are shown in
                // alphabetical order.
                " ELSE 10 END" + " ," + MailboxColumns.PARENT_SERVER_ID  +" , "+  MailboxColumns.DISPLAY_NAME + " COLLATE NOCASE";

        // Types of mailboxes. The list is ordered to match a typical UI
        // presentation, e.g.
        // placing the inbox at the top.
        // The "main" mailbox for the account, almost always referred to as
        // "Inbox"
        // Arrays of "special_mailbox_display_names" and "special_mailbox_icons"
        // are depends on
        // types Id of mailboxes.
        
        public static final int TYPE_TEMP = -1;
        
        public static final int TYPE_INBOX = 0;
        
        // Types of mailboxes
        // Holds mail (generic)
        public static final int TYPE_MAIL = 1;

        // Parent-only mailbox; holds no mail
        public static final int TYPE_PARENT = 2;

        // Holds drafts
		 /*
         * jitha.mj
         * If updating TYPE_DRAFTS, please update EmailConstants.java within EmailSDK for the same variable.
         */
        public static final int TYPE_DRAFTS = 3;

        // The local outbox associated with the Account
        public static final int TYPE_OUTBOX = 4;

        // Holds sent mail
        public static final int TYPE_SENT = 5;

        // Holds deleted mail
        public static final int TYPE_TRASH = 6;

        // Holds junk mail
        public static final int TYPE_JUNK = 7;

        // change@wtl.pkijowski search
        // Holds search results
        public static final int TYPE_SEARCH_RESULTS = 8;

        // scheduled message
        public static final int TYPE_SCHEDULED_OUTBOX = 9;

        // change@wtl.pkijowski search

        // change@wtl.lohith Folder Commands 14.0 Start
        // change@wtl.jrabina Folder Commands start
        public static final int TYPE_USER_CREATED_MAIL = 12;
        
        public static final int TYPE_FILTER = 13;

        // change@wtl.jrabina Folder Commands end
        // Types after this are used for non-mail mailboxes (as in EAS)
        public static final int TYPE_NOT_EMAIL = 0x40;

        public static final int TYPE_CALENDAR = 0x41;

        public static final int TYPE_CONTACTS = 0x42;

        public static final int TYPE_TASKS = 0x43;

        public static final int TYPE_EAS_ACCOUNT_MAILBOX = 0x44;

        // change@siso.Mahsky start NotesSync
        public static final int TYPE_NOTES = 0x45;

        // change@siso.Mahsky end NotesSync
        public static final int TYPE_JOURNAL = 0x46;

        public static final int TYPE_USER_TASKS = 0x51;

        public static final int TYPE_USER_CALENDAR = 0x52;

        public static final int TYPE_USER_CONTACTS = 0x53;

        public static final int TYPE_USER_NOTES = 0x54;

        public static final int TYPE_USER_JOURNAL = 0x55;

        public static final int TYPE_UNKNOWN_FOLDER = 0x60;

        public static final int TYPE_RECIPIENT_INFORMATION_CACHE = 0x61;
        // change@wtl.jpshu document search begin
        public static final int TYPE_SEARCH_DOCS = 0x62;
        // change@wtl.jpshu document search end

        // Recipient information cache are not supported -start
        // Bit field flags

        public static final int TYPE_NOT_SYNCABLE = 0x100;

        // A mailbox that holds Messages that are attachments
        public static final int TYPE_ATTACHMENT = 0x101;

        // SncAdapter Porting
        public static final int TYPE_UNIFIED_INBOX = 0x102;

        public static final int TYPE_UNIFIED_TRASH = 0x103;

        // For special handling of Bad Sync Key scenario
        public static final int TYPE_FAKE_INBOX = 0x200;
        
        // Bit field flags; each is defined below
        // Warning: Do not read there flags until OP/IMAP/EAS all populate them
        // This mailbox has children in the mailbox hierachy
        public static final int FLAG_HAS_CHILDREN = 1 << 0;

        // This mailbox's children are visible in the UI
        public static final int FLAG_CHILDREN_VISIBLE = 1 << 1;

        // This mailbox cannot receive "pushed" mail
        public static final int FLAG_CANT_PUSH = 1 << 2;

        // This mailbox can hod emails (i.e. some parent mailboxes cannot
        // themselves contain mail)
        public static final int FLAG_HOLDS_MAIL = 1 << 3;

        // This mailbox is a valid target for moving message within the account
        public static final int FLAG_ACCEPTS_MOVED_MAIL = 1 << 4;

        // This mailbox is valid target for appending message
        public static final int FLAG_ACCEPTS_APPENDED_MAIL = 1 << 5;

        // This mailbox established sencond sync. (It mean's it get real data of EAS account)
        public static final int FLAG_EAS_PROCESSING_DUMMY_SYNC_ESTABLISHED = 1 << 6;

        // change@wtl.jrabina Folder Commands start
        public static final int FLAG_FOLDER_DELETED = 1 << 0;

        public static final int FLAG_FOLDER_UPDATED = 1 << 1;

        public static final int FLAG_FOLDER_CREATED = 1 << 2;

        public static final int FLAG_FOLDER_REFRESHED = 1 << 3;

        public static final int FLAG_FOLDER_RENAMED = 1 << 4;

        // change@siso.deepak.gp folder hierarchy start
        public static final int FLAG_FOLDER_OPERATION_UNSUPPORTED = 1 << 5;

        // change@siso.deepak.gp folder hierarchy end

        // change@wtl.jrabina Folder Commands end
        // Magic mailbox ID's
        // NOTE: This is a quick solution for merged mailboxes. I would rather
        // implement this
        // with a more generic way of packaging and sharing queries between
        // activities
        public static final long QUERY_ALL_INBOXES = -2;

        public static final long QUERY_ALL_UNREAD = -3;

        public static final long QUERY_ALL_FAVORITES = -4;

        public static final long QUERY_ALL_DRAFTS = -5;

        public static final long QUERY_ALL_OUTBOX = -6;

        public static final long QUERY_ALL_TRASH = -7;

        public static final long QUERY_ALL_SENT = -8;

        //change@k1001.kim - add vip inbox 
        
        public static final long QUERY_ALL_VIP = -9;
        
        //change@k1001.kim - add recently read inbox
        
        public static final long QUERY_ALL_RECENTLY = -10;
        
        public static final long QUERY_ALL_SAVED_EMAIL = -11;

        //w9697.lee - add Starred and Flagged inbox
        public static final long QUERY_ALL_FAVORITES_FLAGGED = -12;
      //w9697.lee - adds Flagged inbox
        public static final long QUERY_ALL_FLAGGED = -13;
        
        public static final long QUERY_ALL_SCHEDULED_OUTBOX = -14;
        
        public static final long MAILBOX_ID_SINGLE_PENDING = -15;
        
        public static final long MAILBOX_ID_SINGLE_APPROVED = -16;
        
        public static final long MAILBOX_ID_SINGLE_SUBMITTED = -17;
        
        public static final long MAILBOX_ID_SINGLE_CONTACT = -18;
        
        public static final long MAILBOX_ID_SINGLE_CALENDAR = -19;
        
        public static final long QUERY_ALL_REMINDER = -20;
        
        public static final long MAILBOX_ID_TOPSTORY_INBOX = -30;
        
        public static final long QUERY_ALL_FILTER = -51; // filter folder has mailboxId under -50 

        public static final long QUERY_ALL_FILTER_BASE = -50; // start num of filter folder
        
        public static final long QUERY_ALL_EACH_ACCOUNT = -1000;
        
        public static final long QUERY_ALL_SEARCH = -1001;
        
        public static final long MAILBOX_VIRTUAL_BASE = -0x10000;
        
        public static final long MAILBOX_SPINNER = MAILBOX_VIRTUAL_BASE - 1;
        
        public static final long MAILBOX_SEPARATOR_COMBINED_VIEW = MAILBOX_VIRTUAL_BASE - 2;
        
        public static final long MAILBOX_SEPARATOR_ACCOUNT = MAILBOX_VIRTUAL_BASE - 3;
        
        public static final long MAILBOX_MOST_RECENT_BASE = -0x100000;
        
        public static final String MAILBOX_DELIMITER_DEFAULT = "/";
        public static final String FAKE_INBOX_PREFIX = "tmpInbox";

        public static final int MAILBOX_FLAG_SELECTED = 0;

        public static final int MAILBOX_FLAG_NO_SELECTED = 1;
        
        public Mailbox() {
            mBaseUri = CONTENT_URI;
        }

        // SncAdapter Porting
        public Mailbox(long accId, String displayName, int type) {
            super();
            mBaseUri = CONTENT_URI;
            mAccountKey = accId;
            mDisplayName = displayName;
            mType = type;
        }

        public Object clone() {
            try {
                return super.clone();
            } catch (Exception e) {
                // EmailLog.dumpException("clone of mailbox failed", e);
                e.printStackTrace();
                return null;
            }
        }

        /**
         * In support of deleting a mailbox, find all messages and delete their
         * bodies.
         * 
         * @param context
         * @param accountId the account for the mailbox
         * @param mailboxId the mailbox for the messages
         */
        public static void deleteAllMailboxBodyFiles(Context context, long accountId,
                long mailboxId, boolean useUriApi) {
        	EmailLog.d(TAG, "deleteAllMailboxBodyFiles accountId = " + accountId + " mailboxId = " + mailboxId);
            Cursor c = context.getContentResolver().query(Message.CONTENT_URI,
                    Message.ID_COLUMN_PROJECTION, MessageColumns.MAILBOX_KEY + "=?", new String[] {
                        Long.toString(mailboxId)
                    }, null);
            try {
                while (c.moveToNext()) {
                    long messageId = c.getLong(Message.ID_PROJECTION_COLUMN);
                    if (useUriApi) {
                        BodyUtilites.deleteAllMessageBodyFilesUri(context, accountId, messageId);
                    } else {
                    	int flags = Body.restoreFileSaveFlags(context, messageId);
                    	BodyUtilites.deleteAllMessageBodyFiles(context, accountId, messageId, flags);
                    }
                }
            } finally {
                c.close();
            }
        }

        public static void deleteAllMailboxBodyFiles(Context context, long accountId,
                long mailboxId) {
            deleteAllMailboxBodyFiles(context, accountId, mailboxId, true);
        }
        
        public static boolean isPrivateSyncOptionTypeById(Context context, long id) {
            int type = Mailbox.getMailboxType(context, id);
            if (type == Mailbox.TYPE_TRASH || type == Mailbox.TYPE_USER_CREATED_MAIL
                    || type == Mailbox.TYPE_SENT || type == Mailbox.TYPE_DRAFTS) {
                return true;
            }
            return false;
        }

        public static boolean isPrivateSyncOptionTypeByType(Context context, long type) {
            if(context == null)
                EmailLog.d(TAG, "context is null");

            if (type == Mailbox.TYPE_TRASH || type == Mailbox.TYPE_USER_CREATED_MAIL
                    || type == Mailbox.TYPE_SENT || type == Mailbox.TYPE_DRAFTS) {
                return true;
            }
            return false;
        }

        public static boolean isPrivateSyncOptionTypeByType(Context context, int type) {
            if(context == null)
                EmailLog.d(TAG, "context is null");

            if (type == Mailbox.TYPE_TRASH || type == Mailbox.TYPE_USER_CREATED_MAIL
                    || type == Mailbox.TYPE_SENT || type == Mailbox.TYPE_DRAFTS) {
                return true;
            }
            return false;
        }
        

        public boolean isInitialSyncDone() {
            return (getSyncStatus() == Account.INITIAL_SYNC_SUCCESS);
        }
        
        public int getSyncStatus() {
            int status = Account.INITIAL_SYNC_FAILED;
            if (TextUtils.isEmpty(mSyncStatus)) {
                return status;
            }

            try {
                status = Integer.valueOf(mSyncStatus);
            } catch (Exception e) {
                EmailLog.e(TAG,
                        "[getSyncStatus()] exception in mailbox sync status "
                                + mSyncStatus);
                e.printStackTrace();
            }
            return status;
        }
        
        /**
         * Restore a Mailbox from the database, given its unique id
         * 
         * @param context
         * @param id
         * @return the instantiated Mailbox
         */
        public static Mailbox restoreMailboxWithId(Context context, long id) {
            // change@wtl.kSingh - Invalid MailboxId START
            /*
             * If user selects cancel on apply IT Policy screen, and presses
             * menu key, email application crashes. ASYNC00001019
             */
            checkNative();
            Uri u = ContentUris.withAppendedId(Mailbox.CONTENT_URI, id);

            Cursor c = null;

            try {

                c = context.getContentResolver().query(u, Mailbox.CONTENT_PROJECTION, null, null,
                        null);

                if (c != null && c.moveToFirst()) {
                    return getContent(c, Mailbox.class);
                } else {
                    return null;
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "No mailbox found with Id: " + id);
                return null;
            } finally {
                try {
                if (c != null && !c.isClosed())
                    c.close();
                }
                catch(IllegalStateException e) {
                    //nothing to do.
                }
            }
        }
        
        

        public static Mailbox[] restoreMailboxWithServerId(Context context, long accountId,
                String serverId) {

            String where = EmailContent.MailboxColumns.ACCOUNT_KEY + "='" + accountId + "' AND "
                    + EmailContent.MailboxColumns.SERVER_ID + "='" + serverId + "'";
            EmailLog.i(TAG, "restoreMailboxWithTagId" + where);
            return restoreMailboxWhere(context, where);
        }
        // SncAdapter Porting Begin
        public static Mailbox[] restoreMailboxWithTagId(Context context, long accountId,
                String tagId) {

            String where = EmailContent.MailboxColumns.ACCOUNT_KEY + "='" + accountId + "' AND "
                    + EmailContent.MailboxColumns.SYNC_KEY + "='" + tagId + "'";
            EmailLog.i(TAG, "restoreMailboxWithTagId" + where);
            return restoreMailboxWhere(context, where);
        }

        public static Mailbox[] restoreMailboxesWithAccoutId(Context context, long accountId) {
            String where = EmailContent.MailboxColumns.ACCOUNT_KEY + "='" + accountId + "'";
            EmailLog.i(TAG, "restoreMailboxesWithAccoutId" + where);
            return restoreMailboxWhere(context, where);
        }

        public static Mailbox[] restoreMailboxWhere(Context context, String where) {
            return restoreMailboxWhere(context, where, null);
        }
        
        public static Mailbox[] restoreMailboxWhere(Context context, String where, String[] selectionArgs) {
            // ;
            // EmailContent.MailboxColumns.SYNC_KEY + "='" + tagId + "'");
            Mailbox[] mailboxes = null;
            Cursor c = null;
            try {
                c = context.getContentResolver().query(Mailbox.CONTENT_URI,
                        Mailbox.CONTENT_PROJECTION, where, selectionArgs, null);
                if (c == null || c.isClosed() == true) {
                    return null;
                }

                if (c.getCount() <= 0) {
                    c.close();
                    return null;
                }
                mailboxes = new Mailbox[c.getCount()];
                int i = 0;

                if (c.moveToFirst()) {
                    do {
                        mailboxes[i++] = getContent(c, Mailbox.class);
                    } while (c.moveToNext());
                } else {
                    return null;
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in restoreMailboxWhere", e);
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
            return mailboxes;
        }

        /**
         * Convenience method that returns the mailbox found using the method
         * above
         */
        // TODO refactor to restoreUnifiedInbox
        public static Mailbox restoreUnifiedMailbox(Context context) {
            Cursor c = null;
            long mailboxId = Mailbox.NO_MAILBOX;
            try {
                c = context.getContentResolver().query(Mailbox.CONTENT_URI, ID_PROJECTION,
                        MailboxColumns.TYPE + "=" + TYPE_UNIFIED_INBOX, null, null);
                if (c != null && c.moveToFirst()) {
                    mailboxId = c.getLong(ID_PROJECTION_COLUMN);
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in restoreUnifiedMailbox", e);
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }

            if (mailboxId != Mailbox.NO_MAILBOX) {
                Mailbox mb = Mailbox.restoreMailboxWithId(context, mailboxId);
                // EmailContentCacheMailbox.updateMailboxCache(mb); //SncAdapter
                // Porting : EmailContentCacheMailbox remove
                return mb;
            }
            return null;
        }

        /**
         * Convenience method that will update the modSeq value of a mailbox for
         * the QRESYNC.
         */
        public static void updateModSeq(Context ctx, long mbId, String modSeq) {

            Uri mburi = ContentUris.withAppendedId(EmailContent.Mailbox.CONTENT_URI, mbId);
            ContentValues updateModSeqValues = new ContentValues();
            updateModSeqValues.put(EmailContent.Mailbox.SYNC_KEY, modSeq);
            ctx.getContentResolver().update(mburi, updateModSeqValues, null, null);
        }

        /**
         * Convenience method that returns the mailbox found using the method
         * above
         */
        public static Mailbox restoreUnifiedTrashMailbox(Context context) {
            Cursor c = null;
            long mailboxId = Mailbox.NO_MAILBOX;
            try {
                c = context.getContentResolver().query(Mailbox.CONTENT_URI, ID_PROJECTION,
                        MailboxColumns.TYPE + "=" + TYPE_UNIFIED_TRASH, null, null);
                if (c != null && c.moveToFirst()) {
                    mailboxId = c.getLong(ID_PROJECTION_COLUMN);
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in restoreUnifiedTrashMailbox", e);
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }

            if (mailboxId != Mailbox.NO_MAILBOX) {
                Mailbox mb = Mailbox.restoreMailboxWithId(context, mailboxId);
                // EmailContentCacheMailbox.updateMailboxCache(mb); //SncAdapter
                // Porting : EmailContentCacheMailbox remove
                return mb;
            }
            return null;
        }

        // SncAdapter Porting End

        @Override
        public void restore(Cursor cursor) {
            mBaseUri = CONTENT_URI;
            mId = cursor.getLong(CONTENT_ID_COLUMN);
            mDisplayName = cursor.getString(CONTENT_DISPLAY_NAME_COLUMN);
            mServerId = cursor.getString(CONTENT_SERVER_ID_COLUMN);
            mParentServerId = cursor.getString(CONTENT_PARENT_SERVER_ID_COLUMN);
            mParentKey = cursor.getLong(CONTENT_PARENT_KEY_COLUMN);
            mAccountKey = cursor.getLong(CONTENT_ACCOUNT_KEY_COLUMN);
            mType = cursor.getInt(CONTENT_TYPE_COLUMN);
            mDelimiter = cursor.getInt(CONTENT_DELIMITER_COLUMN);
            mSyncKey = cursor.getString(CONTENT_SYNC_KEY_COLUMN);
            mSyncLookback = cursor.getInt(CONTENT_SYNC_LOOKBACK_COLUMN);
            mSyncInterval = cursor.getInt(CONTENT_SYNC_INTERVAL_COLUMN);
            mSyncTime = cursor.getLong(CONTENT_SYNC_TIME_COLUMN);
            mFlagVisible = cursor.getInt(CONTENT_FLAG_VISIBLE_COLUMN) == 1;
            mFlagNoSelect = cursor.getInt(CONTENT_FLAG_NOSELECT_COLUMN) == 1;
            mFlags = cursor.getInt(CONTENT_FLAGS_COLUMN);
            mVisibleLimit = cursor.getInt(CONTENT_VISIBLE_LIMIT_COLUMN);
            mSyncStatus = cursor.getString(CONTENT_SYNC_STATUS_COLUMN);
            // change@wtl.jrabina Folder Commands start
            mFlagChanged = cursor.getInt(CONTENT_FLAG_CHANGED);
            mDstServerId = cursor.getString(CONTENT_DST_SERVER_ID);
            mNewDisplayName = cursor.getString(CONTENT_NEW_DISPLAY_NAME);
            mLastTouchedTime = cursor.getString(CONTENT_NEW_TOUCHED_TIME);
            // change@wtl.jrabina Folder Commands end

            // msc, hanklee, 2010.07.28
//            mMailboxKey = cursor.getLong(CONTENT_MAILBOX_KEY_COLUMN);
//            mTypeMsg = cursor.getInt(CONTENT_TYPE_MSG_COLUMN);
//            mSevenMailboxKey = cursor.getLong(CONTENT_SEVEN_MAILBOX_KEY_COLUMN);
//            mSyncFlag = cursor.getInt(CONTENT_SYNC_FLAG_COLUMN);
            // kanchan@SISO adding for mailbox sync options start
            mOffpeakSyncSchedule = cursor.getInt(CONTENT_OFFPEAK_SYNC_SCHEDULE_COLUMN);
            mPeakSyncSchedule = cursor.getInt(CONTENT_PEAK_SYNC_SCHEDULE_COLUMN);
            // kanchan@SISO adding for mailbox sync options end
        }

        public boolean isSpamFolder(final Context context) {
            if (mType == TYPE_JUNK)
                return true;
            boolean nameMatched = false;
            for (String sn : SPAMBOX_NAMES) {
                sn.equals(mDisplayName);
                nameMatched = true;
                break;
            }
            
            if (nameMatched) {
                long folderId = Account.getSpamMailboxId(context, mAccountKey);
                if (folderId == mId) {
                    if (mType != TYPE_JUNK) {
                        mType = TYPE_JUNK;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                            	Uri uri = ContentUris.withAppendedId(CONTENT_URI, mId);
                            	ContentValues cv = new ContentValues();
                            	cv.put(MailboxColumns.TYPE, TYPE_JUNK);
                                context.getContentResolver().update(uri, cv, null, null);
                            }
                        }).start();
                    }
                    return true;
                }
            }
            return false;
        }
        public static boolean isVirtualFolder(long mailboxId) {
            if(mailboxId == Mailbox.QUERY_ALL_DRAFTS
                    || mailboxId == Mailbox.QUERY_ALL_REMINDER
                    || mailboxId == Mailbox.QUERY_ALL_SAVED_EMAIL
                    || mailboxId == Mailbox.QUERY_ALL_FAVORITES
                    || mailboxId == Mailbox.QUERY_ALL_FLAGGED
                    || mailboxId == Mailbox.QUERY_ALL_FAVORITES_FLAGGED
                    || mailboxId == Mailbox.QUERY_ALL_VIP
                    || mailboxId == Mailbox.QUERY_ALL_UNREAD
                    || mailboxId == Mailbox.QUERY_ALL_INBOXES)
                return true;
            return false;
        }

        @Override
        public ContentValues toContentValues() {
            ContentValues values = new ContentValues();
            values.put(MailboxColumns.DISPLAY_NAME, mDisplayName);
            values.put(MailboxColumns.SERVER_ID, mServerId);
            values.put(MailboxColumns.PARENT_SERVER_ID, mParentServerId);
            values.put(MailboxColumns.PARENT_KEY, mParentKey);
            values.put(MailboxColumns.ACCOUNT_KEY, mAccountKey);
            values.put(MailboxColumns.TYPE, mType);
            values.put(MailboxColumns.DELIMITER, mDelimiter);
            values.put(MailboxColumns.SYNC_KEY, mSyncKey);
            values.put(MailboxColumns.SYNC_LOOKBACK, mSyncLookback);
            values.put(MailboxColumns.SYNC_INTERVAL, mSyncInterval);
            values.put(MailboxColumns.SYNC_TIME, mSyncTime);
            values.put(MailboxColumns.FLAG_VISIBLE, mFlagVisible);
            values.put(MailboxColumns.FLAG_NOSELECT, mFlagNoSelect);
            values.put(MailboxColumns.FLAGS, mFlags);
            values.put(MailboxColumns.VISIBLE_LIMIT, mVisibleLimit);
            values.put(MailboxColumns.SYNC_STATUS, mSyncStatus);
            // change@wtl.jrabina Folder Commands start
            values.put(MailboxColumns.FLAG_CHANGED, mFlagChanged);
            values.put(MailboxColumns.DST_MAILBOX_ID, mDstServerId);
            values.put(MailboxColumns.NEW_DISPLAY_NAME, mNewDisplayName);
            values.put(MailboxColumns.LAST_TOUCHED_TIME, mLastTouchedTime);
            // change@wtl.jrabina Folder Commands end
            // kanchan@SISO adding for mailbox sync options start
            values.put(MailboxColumns.OFFPEAK_SYNC_SCHEDULE, mOffpeakSyncSchedule);
            values.put(MailboxColumns.PEAK_SYNC_SCHEDULE, mPeakSyncSchedule);
            // kanchan@SISO adding for mailbox sync options end
            return values;
        }

        /**
         * Convenience method to return the id of a given type of Mailbox for a
         * given Account
         * 
         * @param context the caller's context, used to get a ContentResolver
         * @param accountId the id of the account to be queried
         * @param type the mailbox type, as defined above
         * @return the id of the mailbox, or -1 if not found
         */
        public static long findMailboxOfType(Context context, long accountId, int type) {
            String[] bindArguments = new String[] {
                    Long.toString(type), Long.toString(accountId)
            };
            return EmailContentUtils.getFirstRowLong(context, Mailbox.CONTENT_URI, ID_PROJECTION,
                    WHERE_TYPE_AND_ACCOUNT_KEY, bindArguments, null, ID_PROJECTION_COLUMN,
                    NO_MAILBOX);
        }

        /**
         * Convenience method that returns the mailbox found using the method
         * above
         */
        public static Mailbox restoreMailboxOfType(Context context, long accountId, int type) {
            checkNative();

            Cursor c = null;

            StringBuilder selection = new StringBuilder();
            selection.append(MailboxColumns.ACCOUNT_KEY).append("=").append(accountId);
            selection.append(" AND ");
            selection.append(MailboxColumns.TYPE).append("=").append(type);
            try {

                c = context.getContentResolver().query(Mailbox.CONTENT_URI, Mailbox.CONTENT_PROJECTION, selection.toString(), null,
                        null);

                if (c != null && c.moveToFirst()) {
                    return getContent(c, Mailbox.class);
                } else {
                    return null;
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "No mailbox found with type: " + type);
                return null;
            } finally {
                try {
                if (c != null && !c.isClosed())
                    c.close();
                }
                catch(IllegalStateException e) {
                    //nothing to do.
                }
            }
        }

        public static int getAllUnreadCountbyInBox(Context context) {

            return EmailContentUtils.getFirstRowInt(context, Mailbox.CONTENT_URI,
                    MAILBOX_SUM_OF_UNREAD_COUNT_PROJECTION, MAILBOX_TYPE_SELECTION, new String[] {
                        String.valueOf(Mailbox.TYPE_INBOX)
                    }, null, UNREAD_COUNT_COUNT_COLUMN, 0);
        }

        public static int getUnreadCountByAccountAndMailboxType(Context context, long accountId,
                int type) {
            return EmailContentUtils.getFirstRowInt(context, Mailbox.CONTENT_URI,
                    MAILBOX_SUM_OF_UNREAD_COUNT_PROJECTION, ACCOUNT_AND_MAILBOX_TYPE_SELECTION,
                    new String[] {
                            String.valueOf(accountId), String.valueOf(type)
                    }, null, UNREAD_COUNT_COUNT_COLUMN, 0);
        }

        public static int getAllCountByAccountAndMailboxType(Context context, long accountId,
                int type) {
            return EmailContentUtils.getFirstRowInt(context, Mailbox.CONTENT_URI,
                    MAILBOX_SUM_OF_MESSAGE_COUNT_PROJECTION, ACCOUNT_AND_MAILBOX_TYPE_SELECTION,
                    new String[] {
                            String.valueOf(accountId), String.valueOf(type)
                    }, null, MESSAGE_COUNT_COUNT_COLUMN, 0);
        }

       
        public static int getMessageCountByMailboxId(Context context, long id) {
            return EmailContentUtils.getFirstRowInt(context, Mailbox.CONTENT_URI,
                    MAILBOX_MESSAGE_COUNT_PROJECTION, MAILBOX_ID_SELECTION, new String[] {
                        String.valueOf(id)
                    }, null, MESSAGE_COUNT_COUNT_COLUMN, 0);
        }
        
        public static int getMessageUnreadCountByMailboxId(Context context, long id) {
            return EmailContentUtils.getFirstRowInt(context, Mailbox.CONTENT_URI,
                    MAILBOX_SUM_OF_UNREAD_COUNT_PROJECTION, MAILBOX_ID_SELECTION, new String[] {
                        String.valueOf(id)
                    }, null, MESSAGE_COUNT_COUNT_COLUMN, 0);
        }
        
        
        public static int getMessageCountByMailboxType(Context context, int type) {
        	int count = 0;
        	Cursor c = null;
        	try {
        		StringBuilder sel = new StringBuilder();
        		sel.append(MessageColumns.MAILBOX_KEY).append(" in ( select ");
        		sel.append(MailboxColumns.ID).append(" from ").append(Mailbox.TABLE_NAME).append(" where ").append(MailboxColumns.TYPE).append("=").append(type);
        		sel.append(")");
        		sel.append(" AND ").append(Message.FLAG_LOADED_SELECTION);
        		sel.append(" AND ").append(Message.FLAG_DELETEHIDDEN).append("=").append(0);
        		c = context.getContentResolver().query(Message.CONTENT_URI, 
        				new String[] {"count(*)"}, sel.toString(), null, null);
        		if (c != null && c.moveToFirst()) {
        			count = c.getInt(0);
        		}
        	} finally {
        		if (c != null)
        			c.close();
        	}
        	return count;
//            return EmailContentUtils.getFirstRowInt(context, Mailbox.CONTENT_URI,
//                    MAILBOX_SUM_OF_MESSAGE_COUNT_PROJECTION, MAILBOX_TYPE_SELECTION, new String[] {
//                        String.valueOf(type)
//                    }, null, MESSAGE_COUNT_COUNT_COLUMN, 0);
        }
        
        public static int getUnreadCountByMailboxType(Context context, int type) {
        	int count = 0;
        	Cursor c = null;
        	try {
        		StringBuilder sel = new StringBuilder();
        		sel.append(MessageColumns.MAILBOX_KEY).append(" in ( select ");
        		sel.append(MailboxColumns.ID).append(" from ").append(Mailbox.TABLE_NAME).append(" where ").append(MailboxColumns.TYPE).append("=").append(type);
        		sel.append(")");
        		sel.append(" AND ").append(Message.FLAG_READ).append("=0");
        		sel.append(" AND ").append(Message.FLAG_LOADED_SELECTION);
        		sel.append(" AND ").append(Message.FLAG_DELETEHIDDEN).append("=").append(0);
        		c = context.getContentResolver().query(Message.CONTENT_URI, 
        				new String[] {"count(*)"}, sel.toString(), null, null);
        		if (c != null && c.moveToFirst()) {
        			count = c.getInt(0);
        		}
        	} finally {
        		if (c != null)
        			c.close();
        	}
        	return count;
//            return EmailContentUtils.getFirstRowInt(context, Mailbox.CONTENT_URI,
//                    MAILBOX_SUM_OF_UNREAD_COUNT_PROJECTION, MAILBOX_TYPE_SELECTION, new String[] {
//                        String.valueOf(type)
//                    }, null, UNREAD_COUNT_COUNT_COLUMN, 0);
        }
        
        public static int getMessageCountById(Context context, long id) {
        	int count = 0;
        	Cursor c = null;
        	try {
        		c = context.getContentResolver().query(Message.CONTENT_URI, new String [] {"count(*)"}, 
            			MessageColumns.MAILBOX_KEY + "=" + String.valueOf(id), null, null);
        		if (c != null && c.moveToFirst())
        			count = c.getInt(0);
        	} finally {
        		if (c != null)
        			c.close();
        	}
        	return count;
        }
        
        public static int getSavedEmailNum() {
            int count = 0;
            try {
                String path;
                if (Utility.isInAFWMode()) {
                    path = Utility.getAttachmentsSavePathForAfw() + "/Saved Email/";
                } else {
                    path = Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/Saved Email";
                }
                File file = new File(path);
                File[] fileArr = file.listFiles();
                String filename;
                if(fileArr != null) {
                    for(int i =0;i<fileArr.length;i++){
                        if(fileArr[i].isFile()){
                            filename = fileArr[i].getName();
                            if(filename != null && filename.toLowerCase().endsWith(".eml")){
                                count++;
                            }   
                        }

                    }
                }
            } catch (Exception e) {
                 e.printStackTrace();
             }
            EmailLog.d(TAG, "save get num " + count);
             return count;
         }

        /**
         * Return the mailbox for a message with a given id
         * 
         * @param context the caller's context
         * @param messageId the id of the message
         * @return the mailbox, or null if the mailbox doesn't exist
         */
        public static Mailbox getMailboxForMessageId(Context context, long messageId) {
            long mailboxId = Message.getKeyColumnLong(context, messageId,
                    MessageColumns.MAILBOX_KEY);
            if (mailboxId != -1) {
                return Mailbox.restoreMailboxWithId(context, mailboxId);
            }
            return null;
        }

        /**
         * @return mailbox type, or -1 if mailbox not found.
         */
        public static int getMailboxType(Context context, long mailboxId) {
            if(mailboxId != -1) {
                Mailbox mailbox =  Mailbox.restoreMailboxWithId(context, mailboxId);
                if(mailbox != null) {
                    return mailbox.mType;
                }
            }
            return -1;
        }

        /**
         * @return mailbox display name, or null if mailbox not found.
         */
        public static String getDisplayName(Context context, long mailboxId) {
            if(mailboxId != -1) {
                Mailbox mailbox =  Mailbox.restoreMailboxWithId(context, mailboxId);
                if(mailbox != null) {
                    return mailbox.mDisplayName;
                }
            }
            return null;
        }

        /**
         * Return a string generated from mailbox ID and mailbox type to use it as name for sync thread. 
         * This method was used to avoid using mailbox display name as thread name, 
         * because display name is downloaded from server and can contain non UTF-8 symbols
         * 
         * @return string generated from mailbox ID and type.
         */
        public String getNameForThread() {
            String mailboxId = Long.toString(mId);
            String mailboxTypeName = "UNKNOW TYPE";
            
            switch (mType) {
                case TYPE_INBOX:
                    mailboxTypeName = "INBOX";
                    break;
                case TYPE_MAIL:
                    mailboxTypeName = "MAIL";
                    break;
                case TYPE_PARENT:
                    mailboxTypeName = "PARENT";
                    break;
                case TYPE_DRAFTS:
                    mailboxTypeName = "DRAFTS";
                    break;
                case TYPE_OUTBOX:
                    mailboxTypeName = "OUTBOX";
                    break;
                case TYPE_SENT:
                    mailboxTypeName = "SENT";
                    break;
                case TYPE_TRASH:
                    mailboxTypeName = "TRASH";
                    break;
                case TYPE_JUNK:
                    mailboxTypeName = "JUNK";
                    break;
                case TYPE_SEARCH_RESULTS:
                    mailboxTypeName = "SEARCH RESULTS";
                    break;
                case TYPE_SCHEDULED_OUTBOX:
                    mailboxTypeName = "SCHEDULED OUTBOX";
                    break;
                case TYPE_USER_CREATED_MAIL:
                    mailboxTypeName = "USER CREATED MAIL";
                    break;
                case TYPE_NOT_EMAIL:
                    mailboxTypeName = "NOT EMAIL";
                    break;
                case TYPE_CALENDAR:
                    mailboxTypeName = "CALENDAR";
                    break;
                case TYPE_CONTACTS:
                    mailboxTypeName = "CONTACTS";
                    break;
                case TYPE_TASKS:
                    mailboxTypeName = "TASKS";
                    break;
                case TYPE_EAS_ACCOUNT_MAILBOX:
                    mailboxTypeName = "EAS MAILBOX";
                    break;
                case TYPE_NOTES:
                    mailboxTypeName = "NOTES";
                    break;
                case TYPE_JOURNAL:
                    mailboxTypeName = "JOURNAL";
                    break;
                case TYPE_USER_TASKS:
                    mailboxTypeName = "USER TASKS";
                    break;
                case TYPE_USER_CALENDAR:
                    mailboxTypeName = "USER CALENDAR";
                    break;
                case TYPE_USER_CONTACTS:
                    mailboxTypeName = "USER CONTACTS";
                    break;
                case TYPE_USER_NOTES:
                    mailboxTypeName = "USER NOTES";
                    break;
                case TYPE_USER_JOURNAL:
                    mailboxTypeName = "USER JOURNAL";
                    break;
                case TYPE_UNKNOWN_FOLDER:
                    mailboxTypeName = "UNKNOWN FOLDER";
                    break;
                case TYPE_RECIPIENT_INFORMATION_CACHE:
                    mailboxTypeName = "RIC";
                    break;
                case TYPE_SEARCH_DOCS:
                    mailboxTypeName = "SEACRH DOCS";
                    break;
                case TYPE_NOT_SYNCABLE:
                    mailboxTypeName = "NOT SYNCABLE";
                    break;
                case TYPE_ATTACHMENT:
                    mailboxTypeName = "ATTACHMENT";
                    break;
                case TYPE_UNIFIED_INBOX:
                    mailboxTypeName = "UNIFIED INBOX";
                    break;
                case TYPE_UNIFIED_TRASH:
                    mailboxTypeName = "UNIFIED TRASH";
                    break;
                default:
                    // Use initial value here
            }
            
            StringBuffer result = new StringBuffer(mailboxId); 
            result.append("(").append(mailboxTypeName).append(")"); 
            return result.toString();
        }
        
        
        
        /**
         * @param mailboxId ID of a mailbox. This method accepts magic mailbox
         *            IDs, such as {@link #QUERY_ALL_INBOXES}. (They're all
         *            non-refreshable.)
         * @return true if a mailbox is refreshable.
         */
        public static boolean isRefreshable(Context context, long mailboxId, boolean isEAS) {
            if (mailboxId < 0) {
                if (mailboxId == Mailbox.QUERY_ALL_INBOXES
                    || mailboxId == Mailbox.QUERY_ALL_TRASH
                    || mailboxId == Mailbox.QUERY_ALL_SENT
                    || mailboxId == Mailbox.QUERY_ALL_VIP
                    || mailboxId == Mailbox.QUERY_ALL_RECENTLY
                    || mailboxId == Mailbox.QUERY_ALL_FAVORITES
                    || mailboxId == Mailbox.QUERY_ALL_FAVORITES_FLAGGED
                    || mailboxId == Mailbox.QUERY_ALL_FLAGGED
                    || mailboxId == Mailbox.QUERY_ALL_UNREAD
                    || mailboxId < Mailbox.QUERY_ALL_FILTER_BASE)
                    return true;
                else
                    return false; // magic mailboxes
            }
            if (isEAS) {
                switch (getMailboxType(context, mailboxId)) {
                    case -1: // not found
                    case TYPE_OUTBOX:
                    case TYPE_SCHEDULED_OUTBOX:
                        return false;
                }
            } else {
                switch (getMailboxType(context, mailboxId)) {
                    case -1: // not found
                    case TYPE_DRAFTS:
                    case TYPE_OUTBOX:
                    case TYPE_SCHEDULED_OUTBOX:
                        return false;
                }
            }
            return true;
        }

        /**
         * @param mailboxId ID of a mailbox. This method DOES NOT accept magic
         *            mailbox IDs, such as {@link #QUERY_ALL_INBOXES} (because
         *            only the actual mailbox ID matters here. e.g.
         *            {@link #QUERY_ALL_FAVORITES} can contain ANY kind of
         *            messages), so don't pass a negative value.
         * @return true if messages in a mailbox can be moved to another
         *         mailbox. This method only checks the mailbox information. It
         *         doesn't check its account/protocol, so it may return true
         *         even for POP3 mailbox.
         */
        public static boolean canMoveFrom(long mailboxId, int mailboxType) {
            if (mailboxId == QUERY_ALL_INBOXES && mailboxType != TYPE_DRAFTS) {
                return true;
            }
            if (mailboxId < 0) {
                return false;
            }
            

            switch (mailboxType) {
                case TYPE_INBOX:
                case TYPE_MAIL:
                case TYPE_TRASH:
                case TYPE_JUNK:
                case TYPE_USER_CREATED_MAIL:
                case TYPE_SENT:
                    return true;
            }
            return false; // TYPE_DRAFTS, TYPE_OUTBOX etc
        }
        public static boolean canMoveFrom(Context context, long mailboxId) {
            if (mailboxId == QUERY_ALL_INBOXES) {
                return true;
            }
            if (mailboxId < 0) {
                return false;
            }
            Mailbox mailbox =  Mailbox.restoreMailboxWithId(context, mailboxId);
            
            if(mailbox == null) {
                return false;
            }

            if ((mailbox.mType == TYPE_TRASH) && isSNCAccount(context, mailbox.mAccountKey)) {
                return false;
            }
            switch (mailbox.mType) {
                case TYPE_INBOX:
                case TYPE_MAIL:
                case TYPE_TRASH:
                case TYPE_JUNK:
                case TYPE_USER_CREATED_MAIL:
                case TYPE_SENT:
                    return true;
            }
            return false; // TYPE_DRAFTS, TYPE_OUTBOX etc
        }

        /**
         * @return true if messages in a mailbox of a type can be
         *         replied/forwarded.
         */
        public static boolean isMailboxTypeReplyAndForwardable(int type) {
            return (type != TYPE_TRASH) && (type != TYPE_DRAFTS) && (type != TYPE_OUTBOX)
                    && (type != QUERY_ALL_DRAFTS) && (type != QUERY_ALL_OUTBOX) && (type != TYPE_SCHEDULED_OUTBOX);
        }

        public static void setSyncStatus(Context context, long mailboxId, int syncStatus) {
            ContentValues cv = new ContentValues();
            cv.put(MailboxColumns.SYNC_STATUS, Integer.toString(syncStatus));
            try {
                context.getContentResolver().update(
                        ContentUris.withAppendedId(EmailContent.Mailbox.CONTENT_URI, mailboxId), cv,
                        null, null);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

        public static void setSyncStatusWithAccountId(Context context, long accountId,
                int syncStatus) {
            ContentValues cv = new ContentValues();
            cv.put(MailboxColumns.SYNC_STATUS, Integer.toString(syncStatus));
            context.getContentResolver().update(
                    EmailContent.Mailbox.CONTENT_URI, cv,
                    ACCOUNT_KEY + " = " + Long.toString(accountId), null);
        }

        public static void setVisibleLimit(Context context, long mailboxId, int visibleLimit) {
            ContentValues cv = new ContentValues();
            cv.put(MailboxColumns.VISIBLE_LIMIT, Integer.toString(visibleLimit));
            context.getContentResolver().update(
                    ContentUris.withAppendedId(EmailContent.Mailbox.CONTENT_URI, mailboxId), cv,
                    null, null);
        }

        // MNO-SVL_B2B START: m.hanifa
        public static int getSyncStatus(Context context, long mailboxId) {
            int ret = Account.INITIAL_SYNC_FAILED;   // change sajid start
            if (mailboxId < 0) {
                EmailLog.e(TAG, "[getSyncStatus] invalid mailbox id " + mailboxId);
                ret = Account.INITIAL_SYNC_FAILED;
            }
            
            Mailbox mailbox = Mailbox.restoreMailboxWithId(context, mailboxId);
            if(mailbox == null || mailbox.mSyncStatus == null) {
                ret = Account.INITIAL_SYNC_FAILED;
            } else {
                try {
                    ret = Integer.valueOf(mailbox.mSyncStatus);
                } catch (NumberFormatException e) {
                    EmailLog.e(TAG, "[getSyncStatus] invalid mailbox sync status " + mailbox.mSyncStatus);
                    e.printStackTrace();
                } catch (Exception e) {
                    EmailLog.e(TAG, "[getSyncStatus] exception in mailbox sync status " + mailbox.mSyncStatus);
                    e.printStackTrace();
                }
            }
            
            return ret;
            // change sajid end
        }

        /*
         * Legacy protocols updates the syncKey as integer whereas EAS stores it
         * as a string with a pre-defined grammer such S5:0:0
         */
        public int getLegacySyncStatus(Context context) {
            int ret = Account.INITIAL_SYNC_FAILED;
            if (mSyncStatus != null) {
                try {
                    ret = Integer.valueOf(mSyncStatus);
                } catch (Exception e) {
                    // ignore and reset
                    EmailLog.e(TAG, "resetting the incorrect value of syncStatus " + mSyncStatus);
                    mSyncStatus = String.valueOf(Account.INITIAL_SYNC_FAILED);
                    setSyncStatus(context, mId, Account.INITIAL_SYNC_FAILED);
                }
            }
            EmailLog.v(TAG, "[getLegacySyncStatus] mSyncStatus: " + mSyncStatus + " syncStatus: "
                    + ret);
            return ret;
        }

        // MNO-SVL_B2B END: m.hanifa

        // kanchan@siso sync enable/disable start
        public static boolean isSyncDisabled(Context context, long mailboxId, boolean isEAS) {
            if (mailboxId < 0) {
                return true; // magic mailboxes
            }
            Boolean isSyncDisabled = true;
            if (isEAS) {
                Mailbox mailbox = Mailbox.restoreMailboxWithId(context, mailboxId);
                if(mailbox != null && mailbox.mSyncInterval != CHECK_INTERVAL_NEVER) {
                    isSyncDisabled = false;
                }
            }
            return isSyncDisabled;
        }

        // kanchan@siso sync enable/disable end

        // MNO-SVL_B2B START: m.hanifa
        public String dump() {
            StringBuilder sb = new StringBuilder();
            sb.append(" Mailbox Information:");
            sb.append("\n  mDisplayName=" + mDisplayName)
              .append(" mId=" +mId);
            
            sb.append("\n  mServerId=" + mServerId)
               .append(" mParentServerId=" +mParentServerId);
            
            sb.append("\n  mAccountKey=" + mAccountKey)
               .append(" mType=" +mType)
               .append(" mDelimiter=" + mDelimiter);
            
            sb.append("\n  mSyncKey=" +mSyncKey)
              .append(" mSyncLookback=" + mSyncLookback)
              .append(" mSyncInterval=" +mSyncInterval)
              .append(" mSyncStatus=" + mSyncStatus);
            
            sb.append("\n  mFlags=" + mFlags)
              .append(" mVisibleLimit=" +mVisibleLimit)
              .append(" mFlagChanged=" + mFlagChanged)
              .append(" mFlagVisible=" +mFlagVisible);
            
            sb.append("\n  mDstServerId=" +mDstServerId)
              .append(" mNewDisplayName=" + mNewDisplayName);
               
            return sb.toString();
        }
        // MNO-SVL_B2B END: m.hanifa
        
        public static boolean isFilterFolder(long mailboxid) {
            boolean isfilter = false;
            if (mailboxid <= QUERY_ALL_FILTER)
                isfilter = true;
            return isfilter;
        }
        
        public void resetOutboxParams(Context context) {
            if (mType != TYPE_OUTBOX) {
                Log.e(TAG, "resetOutboxParams incorrect mailbox type mbId="
                        + mId + " mbType=" + mType);
                return;
            }
            resetOutboxParams(context, WHERE_MAILBOX_KEY, new String[] { Long.toString(mId) });
        }
        
        public void resetOutboxParamsSelecteMessages(Context context , final Set<Long> messagesId){
            if (mType != TYPE_OUTBOX) {
                Log.e(TAG, "resetOutboxParams incorrect mailbox type mbId="
                        + mId + " mbType=" + mType);
                return;
            }
            if(messagesId.size() == 0){
                return;
            }
            int index = 0;
            String[] param = new String[messagesId.size()+1];
            StringBuffer sbWhere = new StringBuffer();
            sbWhere.append(WHERE_MAILBOX_KEY);
            param[index++] =  Long.toString(mId);
            for (long messageId : messagesId) {
                sbWhere.append(" OR ");
                sbWhere.append(WHERE_MESSAGE_ID);
                param[index++] = Long.toString(messageId);
            }
            resetOutboxParams(context, sbWhere.toString(), param);
            
        }
        
        public static void resetOutboxParams(Context context, String where, String[] whereArgs) {
            ContentValues cv = new ContentValues();
            cv.put(SyncColumns.SERVER_ID, 0);
            cv.put(SyncColumns.SERVER_TIMESTAMP, 0);
            cv.put(MessageColumns.RETRY_SEND_TIMES, 0);
            context.getContentResolver().update(Message.CONTENT_URI, cv,
                    where, whereArgs);
        }
    }

    public interface HostAuthColumns {
        public static final String ID = "_id";

        // The protocol (e.g. "imap", "pop3", "eas", "smtp"
        static final String PROTOCOL = "protocol";

        // The host address
        static final String ADDRESS = "address";

        // The port to use for the connection
        static final String PORT = "port";

        // General purpose flags
        static final String FLAGS = "flags";

        // The login (user name)
        static final String LOGIN = "login";

        // Password
        static final String PASSWORD = "password";
        
        // A domain or path, if required (used in IMAP and EAS)
        static final String DOMAIN = "domain";

        // DEPRECATED - Will not be set or stored
        static final String ACCOUNT_KEY = "accountKey";
        
        // Password
        static final String PASSWORDENC = "passwordenc";

        // Capabilities
        static final String CAPABILITIES = "capabilities";

        // Credential key
        static final String CREDENTIAL_KEY = "credentialKey";
    }

    public static final class HostAuth extends EmailContent implements HostAuthColumns, Parcelable {
        public static final String TABLE_NAME = "HostAuth";

        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI + "/hostauth");

        // TODO j.sb modify the query to verify the push status of these accounts
        public static final String WHERE_PROTOCOL_IMAP_WITH_IDLE_SUPPORT = HostAuthColumns.PROTOCOL
                + "=\"imap\" AND "
                + HostAuthColumns.CAPABILITIES
                + " LIKE '%idle%'";

        public static final String WHERE_PROTOCOL_IMAP = HostAuthColumns.PROTOCOL
                + "=\"imap\"";

        public static final int PORT_UNKNOWN = -1;

        public static final int FLAG_NONE = 0x00; // No flags

        public static final int FLAG_SSL = 0x01; // Use SSL

        public static final int FLAG_TLS = 0x02; // Use TLS

        public static final int FLAG_AUTHENTICATE = 0x04; // Use name/password
        // for authentication

        public static final int FLAG_TRUST_ALL = 0x08; // Trust all certificates

        public static final int FLAG_OAUTH = 0x10; // Use OAuth for authentication

        // Mask of settings directly configurable by the user
        public static final int USER_CONFIG_MASK = 0x0b;

        public String mProtocol;

        public String mAddress;

        public int mPort;

        public int mFlags;

        public String mLogin;

        public String mPassword;

        public int mPasswordenc;

        public String mDomain;

        public String mCapabilities;

        public long mCredentialKey;

        public transient Credential mCredential;

        public static final int CONTENT_ID_COLUMN = 0;

        public static final int CONTENT_PROTOCOL_COLUMN = 1;

        public static final int CONTENT_ADDRESS_COLUMN = 2;

        public static final int CONTENT_PORT_COLUMN = 3;

        public static final int CONTENT_FLAGS_COLUMN = 4;

        public static final int CONTENT_LOGIN_COLUMN = 5;

        public static final int CONTENT_PASSWORD_COLUMN = 6;

        public static final int CONTENT_DOMAIN_COLUMN = 7;

        public static final int CONTENT_PASSWORDENC_COLUMN = 8;

        public static final int CONTENT_CAPABILITIES_COLUMN = 9;

        public static final int CONTENT_CREDENTIAL_KEY_COLUMN = 10;

        public static final String[] CONTENT_PROJECTION = new String[] {
                RECORD_ID, HostAuthColumns.PROTOCOL, HostAuthColumns.ADDRESS, HostAuthColumns.PORT,
                HostAuthColumns.FLAGS, HostAuthColumns.LOGIN, HostAuthColumns.PASSWORD,
                HostAuthColumns.DOMAIN, HostAuthColumns.PASSWORDENC, HostAuthColumns.CAPABILITIES,
                HostAuthColumns.CREDENTIAL_KEY
        };

        //using google token symbol
        public static final String USE_GOOGLE_TOKEN = "useGoogleToken";
        public static final String USE_GOOGLE_OAUTH_TOKEN = "useGoogleOAuthToken";

        //password types.
        public static int PASSWORD_TYPE_PLAIN_TEXT = 0;
        public static int PASSWORD_TYPE_ENCRYPTED = 1;
        public static int PASSWORD_TYPE_GOOGLE_TOKEN = 2;
        public static int PASSWORD_TYPE_GOOGLE_OAUTH_TOKEN = 3;

        /**
         * no public constructor since this is a utility class
         */
        public HostAuth() {
            mBaseUri = CONTENT_URI;

            // other defaults policy)
            mPort = PORT_UNKNOWN;

            mPasswordenc = PASSWORD_TYPE_ENCRYPTED;

            mCapabilities = null;

            mCredentialKey = -1;
        }
 
        public String getPassword() {
            return mPassword;
        }

        // change@siso.gaurav Http redirect start
        public static String getPath(Context context, long hostAuthKey) {
            String path = "";
            if (context != null) {
                HostAuth ha = HostAuth.restoreHostAuthWithId(context, hostAuthKey);
                if (ha != null && ha.mDomain != null && !ha.mDomain.isEmpty()) {
                    path = ha.mDomain;
                }
            }
            return path;
        }

        // change@siso.gaurav Http redirect end
        /**
         * Restore a HostAuth from the database, given its unique id
         * 
         * @param context
         * @param id
         * @return the instantiated HostAuth
         */
        public static HostAuth restoreHostAuthWithId(Context context, long id) {
            checkNative();
            Uri u = ContentUris.withAppendedId(EmailContent.HostAuth.CONTENT_URI, id);
            Cursor c = null;
            try {
                c = context.getContentResolver().query(u, HostAuth.CONTENT_PROJECTION, null, null,
                        null);

                if (c == null) {
                    EmailLog.e(TAG, "restoreHostAuthWithId : null cursor");
                    return null;
                }

                if (c != null && c.moveToFirst()) {
                    return getContent(c, HostAuth.class);
                } else {
                    return null;
                }
            } catch (RuntimeException e) {
                EmailLog.e(TAG, "RuntimeException in restoreHostAuthWithId", e);
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
            return null;
        }
        
        public static HostAuth restoreHostAuthWithSenderKey(Context context, long id) {
            String where = HostAuthColumns.ID + " = " + id + " AND "
                    + HostAuthColumns.PROTOCOL + " LIKE 'smtp'";
            Cursor c = null;
            try {
                c = context.getContentResolver().query(EmailContent.HostAuth.CONTENT_URI,
                        HostAuth.CONTENT_PROJECTION, where, null, null);

                if (c == null) {
                    EmailLog.e(TAG, "restoreHostAuthWithSenderSyncId : null cursor");
                    return null;
                }

                if (c != null && c.moveToFirst()) {
                    return getContent(c, HostAuth.class);
                } else {
                    return null;
                }
            } catch (RuntimeException e) {
                EmailLog.e(TAG, "RuntimeException in restoreHostAuthWithSenderSyncId", e);
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
            return null;
        }

        public static HostAuth restoreHostAuthWithAccountId(Context context, long id) {
            checkNative();
            String where = HostAuthColumns.ACCOUNT_KEY + " = " + id;
            Cursor c = null;
            try {
                c = context.getContentResolver().query(EmailContent.HostAuth.CONTENT_URI,
                        HostAuth.CONTENT_PROJECTION, where, null, null);

                if (c == null) {
                    EmailLog.e(TAG, "restoreHostAuthWithAccountId : null cursor");
                    return null;
                }

                if (c != null && c.moveToFirst()) {
                    return getContent(c, HostAuth.class);
                } else {
                    return null;
                }
            } catch (RuntimeException e) {
                EmailLog.e(TAG, "RuntimeException in restoreHostAuthWithAccountId", e);
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
            return null;
        }

        public static HostAuth restoreStoreUriWithAccountId(Context context, long id) {
            String where = HostAuthColumns.ACCOUNT_KEY + " = " + id + " AND "
                    + HostAuthColumns.PROTOCOL + " NOT LIKE 'smtp'";
            Cursor c = null;
            try {
                c = context.getContentResolver().query(EmailContent.HostAuth.CONTENT_URI,
                        HostAuth.CONTENT_PROJECTION, where, null, null);

                if (c == null) {
                    EmailLog.e(TAG, "restoreStoreUriWithAccountId : null cursor");
                    return null;
                }

                if (c != null && c.moveToFirst()) {
                    return getContent(c, HostAuth.class);
                } else {
                    return null;
                }
            } catch (RuntimeException e) {
                EmailLog.e(TAG, "RuntimeException in restoreStoreUriWithAccountId", e);
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
            return null;
        }

        /**
         * This method is deprecated as AccountKey is no longer used as reference for HostAuth
         * to Account Mapping
         * @param context
         * @param id
         * @return
         */
        @Deprecated
        public static HostAuth restoreSenderUriWithAccountId(Context context, long id) {
            String where = HostAuthColumns.ACCOUNT_KEY + " = " + id + " AND "
                    + HostAuthColumns.PROTOCOL + " LIKE 'smtp'";
            Cursor c = null;
            try {
                c = context.getContentResolver().query(EmailContent.HostAuth.CONTENT_URI,
                        HostAuth.CONTENT_PROJECTION, where, null, null);

                if (c == null) {
                    EmailLog.e(TAG, "restoreSenderUriWithAccountId : null cursor");
                    return null;
                }

                if (c != null && c.moveToFirst()) {
                    return getContent(c, HostAuth.class);
                } else {
                    return null;
                }
            } catch (RuntimeException e) {
                EmailLog.e(TAG, "RuntimeException in restoreSenderUriWithAccountId", e);
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
            return null;
        }


        /**
         * Returns the credential object for this HostAuth. This will load from the
         * database if the HosAuth has a valid credential key, or return null if not.
         */
        public Credential getCredential(Context context) {
            if (mCredential == null) {
                if (mCredentialKey >= 0) {
                    mCredential = Credential.restoreCredentialsWithId(context, mCredentialKey);
                }
            }
            return mCredential;
        }

        /**
         * getOrCreateCredential Return the credential object for this HostAuth,
         * creating it if it does not yet exist. This should not be called on the
         * main thread.
         *
         * As a side-effect, it also ensures FLAG_OAUTH is set. Use {@link #removeCredential()} to clear
         *
         * @param context for provider loads
         * @return the credential object for this HostAuth
         */
        public Credential getOrCreateCredential(Context context) {
            mFlags |= FLAG_OAUTH;
            if (mCredential == null) {
                if (mCredentialKey >= 0) {
                    mCredential = Credential.restoreCredentialsWithId(context, mCredentialKey);
                } else {
                    mCredential = new Credential();
                }
            }
            return mCredential;
        }

        /**
         * Clear the credential object.
         */
        public void removeCredential() {
            mCredential = null;
            mCredentialKey = -1;
            mFlags &= ~FLAG_OAUTH;
        }

        // MDM intent parameter for EAS Setup >>>>>>
        /**
         * To get the account key
         * 
         * @param context
         * @param where
         * @return account key
         */
        static public long getAccountIDWhere(Context context, String where) {
            Cursor cursor = context.getContentResolver().query(CONTENT_URI, new String[] {
                ACCOUNT_KEY
            }, where, null, null);
            try {
                if (cursor.moveToFirst()) {
                    return cursor.getLong(0); // column 0 is id
                }
            } finally {
                cursor.close();
            }
            return -1;
        }

        /**
         * getAccountIdListWhere - returns the list of accountId if query matches.
         *
         * @param context
         * @param where
         * @return
         */
        static public long[] getAccountIdListWhere(Context context, String where) {
            long[] idList;
            Cursor c = null;
            try {
                c = context.getContentResolver().query(CONTENT_URI, new String[] {
                    ACCOUNT_KEY }, where, null, null);
                if(c != null) {
                    if (c.getCount() <= 0) {
                        c.close();
                        return null;
                    }

                    idList = new long[c.getCount()];
                    int i = 0;
                    if (c.moveToFirst()) {
                        do {
                            idList[i++] = c.getLong(0); // column 0 is account key
                        } while (c.moveToNext());
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in getAccountIdListWhere", e);
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
            return idList;
        }

        // MDM intent parameter for EAS Setup <<<<<<

        /**
         * Returns the scheme for the specified flags.
         */
        public static String getSchemeString(String protocol, int flags) {
            String security = "";
            switch (flags & USER_CONFIG_MASK) {
                case FLAG_SSL:
                    security = "+ssl+";
                    break;
                case FLAG_SSL | FLAG_TRUST_ALL:
                    security = "+ssl+trustallcerts";
                    break;
                case FLAG_TLS:
                    security = "+tls+";
                    break;
                case FLAG_TLS | FLAG_TRUST_ALL:
                    security = "+tls+trustallcerts";
                    break;
            }
            return protocol + security;
        }

        /**
         * Returns the flags for the specified scheme.
         */
        public static int getSchemeFlags(String scheme) {
            String[] schemeParts = scheme.split("\\+");
            int flags = HostAuth.FLAG_NONE;
            if (schemeParts.length >= 2) {
                String part1 = schemeParts[1];
                if ("ssl".equals(part1)) {
                    flags |= HostAuth.FLAG_SSL;
                } else if ("tls".equals(part1)) {
                    flags |= HostAuth.FLAG_TLS;
                }
                if (schemeParts.length >= 3) {
                    String part2 = schemeParts[2];
                    if ("trustallcerts".equals(part2)) {
                        flags |= HostAuth.FLAG_TRUST_ALL;
                    }
                }
            }
            return flags;
        }

        @Override
        public void restore(Cursor cursor) {
            mBaseUri = CONTENT_URI;
            mId = cursor.getLong(CONTENT_ID_COLUMN);
            mProtocol = cursor.getString(CONTENT_PROTOCOL_COLUMN);
            mAddress = cursor.getString(CONTENT_ADDRESS_COLUMN);
            mPort = cursor.getInt(CONTENT_PORT_COLUMN);
            mFlags = cursor.getInt(CONTENT_FLAGS_COLUMN);
            mLogin = cursor.getString(CONTENT_LOGIN_COLUMN);
            
            //mPassword = cursor.getString(CONTENT_PASSWORD_COLUMN);
            mPassword = AESEncryptionUtil.AESDecryption(cursor.getString(CONTENT_PASSWORD_COLUMN));
            
            mDomain = cursor.getString(CONTENT_DOMAIN_COLUMN);
            mPasswordenc = cursor.getInt(CONTENT_PASSWORDENC_COLUMN);
            mCapabilities = cursor.getString(CONTENT_CAPABILITIES_COLUMN);
            mCredentialKey = cursor.getLong(CONTENT_CREDENTIAL_KEY_COLUMN);
            if (mCredentialKey != -1) {
                mFlags |= FLAG_OAUTH;
            }
        }

        @Override
        public ContentValues toContentValues() {
            // MNO_LAB Start: julio.diaz
            // Save a default port value if it was not defined
            // by the user
            if ("eas".equals(mProtocol) && mPort < 1) {
                boolean useSSL = ((mFlags & FLAG_SSL) != 0);
                mPort = useSSL ? 443 : 80;
            }
            // MNO_LAB End
            
            ContentValues values = new ContentValues();
            values.put(HostAuthColumns.PROTOCOL, mProtocol);
            values.put(HostAuthColumns.ADDRESS, mAddress);
            values.put(HostAuthColumns.PORT, mPort);
            values.put(HostAuthColumns.FLAGS, mFlags);
            values.put(HostAuthColumns.LOGIN, mLogin);
            
            mPassword = AESEncryptionUtil.AESEncryption(mPassword);
            values.put(HostAuthColumns.PASSWORD, mPassword);
            
            values.put(HostAuthColumns.DOMAIN, mDomain);
            values.put(HostAuthColumns.ACCOUNT_KEY, 0);
            values.put(HostAuthColumns.PASSWORDENC, mPasswordenc);
            values.put(HostAuthColumns.CAPABILITIES, mCapabilities);
            values.put(HostAuthColumns.CREDENTIAL_KEY, mCredentialKey);
            return values;
        }

        /**
         * For compatibility while converting to provider model, generate a
         * "store URI" TODO cache this so we don't rebuild every time
         * 
         * @return a string in the form of a Uri, as used by the other parts of
         *         the email app
         */
        public String getStoreUri() {
            String userInfo = null;
            if ((mFlags & FLAG_AUTHENTICATE) != 0) {
                String trimUser = (mLogin != null) ? mLogin.trim() : "";
                String trimPassword = (mPassword != null) ? mPassword : "";
                userInfo = trimUser + ":" + trimPassword;
            }
            String scheme = getSchemeString(mProtocol, mFlags);
            String address = (mAddress != null) ? mAddress.trim() : null;
            String path = (mDomain != null) ? "/" + mDomain : null;
            String query = null;
            if (mPasswordenc == PASSWORD_TYPE_GOOGLE_TOKEN) {
                query =  USE_GOOGLE_TOKEN;
            }
            if (mPasswordenc == PASSWORD_TYPE_GOOGLE_OAUTH_TOKEN) {
                query =  USE_GOOGLE_OAUTH_TOKEN;
            }
            URI uri;
            try {
                uri = new URI(scheme, userInfo, address, mPort, path, query, null);
                return uri.toString();
            } catch (URISyntaxException e) {
                return null;
            }
        }

        /**
         * Sets the user name and password from URI user info string
         */
        public void setLogin(String userInfo) {
            String userName = null;
            String userPassword = null;
            if (!TextUtils.isEmpty(userInfo)) {
                String[] userInfoParts = userInfo.split(":", 2);
                userName = userInfoParts[0];
                if (userInfoParts.length > 1) {
                    userPassword = userInfoParts[1];
                }
            }
            setLogin(userName, userPassword);
        }

        /**
         * Sets the user name and password
         */
        public void setLogin(String userName, String userPassword) {
            mLogin = userName;
            mPassword = userPassword;

            if (mLogin == null) {
                mFlags &= ~FLAG_AUTHENTICATE;
            } else {
                mFlags |= FLAG_AUTHENTICATE;
            }
        }

        /**
         * Sets the user name and password
         */
        public void setLogin(String userName, String userPassword, boolean useToken) {
            setLogin(userName, userPassword);
            if (useToken) {
                mPasswordenc = PASSWORD_TYPE_GOOGLE_TOKEN;
            } else
                mPasswordenc = PASSWORD_TYPE_ENCRYPTED;
        }

        /**
         * Sets the user name and password
         */
        public void setOAuthLogin(String userName, String userPassword) {
            setLogin(userName, userPassword);
            mPasswordenc = PASSWORD_TYPE_GOOGLE_OAUTH_TOKEN;
        }

        /**
         * Sets the connection values of the auth structure per the given
         * scheme, host and port.
         */
        public void setConnection(String scheme, String host, int port) {
            String[] schemeParts = scheme.split("\\+");
            String protocol = schemeParts[0];
            int flags = getSchemeFlags(scheme);

            setConnection(protocol, host, port, flags);
        }

        public void setConnection(String protocol, String address, int port, int flags) {
            // Set protocol, security, and additional flags based on uri scheme
            mProtocol = protocol;

            mFlags &= ~(FLAG_SSL | FLAG_TLS | FLAG_TRUST_ALL | FLAG_OAUTH);
            mFlags |= (flags & USER_CONFIG_MASK);

            mAddress = address;
            mPort = port;
            boolean useSSL = ((mFlags & FLAG_SSL) != 0);
            if (mPort == PORT_UNKNOWN) {
                // infer port# from protocol + security
                // SSL implies a different port - TLS runs in the "regular" port
                // NOTE: Although the port should be setup in the various setup
                // screens, this
                // block cannot easily be moved because we get process URIs from
                // other sources
                // (e.g. for tests, provider templates and account restore) that
                // may or may not
                // have a port specified.
                if ("pop3".equals(mProtocol)) {
                    mPort = useSSL ? 995 : 110;
                } else if ("imap".equals(mProtocol)) {
                    mPort = useSSL ? 993 : 143;
                } else if ("smtp".equals(mProtocol)) {
                    mPort = useSSL ? 465 : 587;
                }
            }

            if ("eas".equals(mProtocol) && mPort < 1) {
                mPort = useSSL ? 443 : 80;
            }
        }

        /**
         * For compatibility while converting to provider model, set fields from
         * a "store URI"
         * 
         * @param uriString a String containing a Uri
         */
        @Deprecated
        public void setStoreUri(String uriString) {
            try {
                URI uri = new URI(uriString);
                mLogin = null;
                mPassword = null;

                String scheme = uri.getScheme();
                String[] schemeParts = scheme.split("\\+");
                mProtocol = (schemeParts.length >= 1) ? schemeParts[0] : null;

                mFlags &= ~(FLAG_SSL | FLAG_TLS | FLAG_TRUST_ALL);
                mFlags |= getSchemeFlags(scheme);
                boolean ssl = ((mFlags & FLAG_SSL) == FLAG_SSL);

                mFlags &= ~FLAG_AUTHENTICATE;
                if (uri.getUserInfo() != null) {
                    String[] userInfoParts = uri.getUserInfo().split(":", 2);
                    mLogin = userInfoParts[0];
                    mFlags |= FLAG_AUTHENTICATE;
                    if (userInfoParts.length > 1) {
                        mPassword = userInfoParts[1];
                    }
                }

                mAddress = uri.getHost();
                mPort = uri.getPort();
                if (mPort == PORT_UNKNOWN) {
                    // infer port# from protocol + security
                    // SSL implies a different port - TLS runs in the "regular"
                    // port
                    // TODO: This really shouldn't be here - it should have been
                    // set up
                    // in the account setup screens.
                    if ("pop3".equals(mProtocol)) {
                        mPort = ssl ? 995 : 110;
                    } else if ("imap".equals(mProtocol)) {
                        mPort = ssl ? 993 : 143;
                    } else if ("smtp".equals(mProtocol)) {
                        mPort = ssl ? 465 : 587;
                    }
                }
                
                if ("eas".equals(mProtocol) && mPort < 1) {
                    mPort = ssl ? 443 : 80;
                }

                if (uri.getPath() != null && uri.getPath().length() > 0) {
                    mDomain = uri.getPath().substring(1);
                } else {
                    mDomain = null;
                }

                mPasswordenc = PASSWORD_TYPE_ENCRYPTED;
                if (uri.getQuery()!= null ){
                    if (uri.getQuery().equals(USE_GOOGLE_TOKEN)) {
                        mPasswordenc = PASSWORD_TYPE_GOOGLE_TOKEN;
                    }
                    if (uri.getQuery().equals(USE_GOOGLE_OAUTH_TOKEN)) {
                        mPasswordenc = PASSWORD_TYPE_GOOGLE_OAUTH_TOKEN;
                    }
                }
            } catch (URISyntaxException use) {
                /*
                 * We should always be able to parse our own settings.
                 */
                throw new Error(use);
            }

        }

        /**
         * Supports Parcelable
         */
        public int describeContents() {
            return 0;
        }

        /**
         * Supports Parcelable
         */
        public static final Parcelable.Creator<EmailContent.HostAuth> CREATOR = new Parcelable.Creator<EmailContent.HostAuth>() {
            public EmailContent.HostAuth createFromParcel(Parcel in) {
                return new EmailContent.HostAuth(in);
            }

            public EmailContent.HostAuth[] newArray(int size) {
                return new EmailContent.HostAuth[size];
            }
        };

        /**
         * Supports Parcelable
         */
        public void writeToParcel(Parcel dest, int flags) {
            // mBaseUri is not parceled
            dest.writeLong(mId);
            dest.writeString(mProtocol);
            dest.writeString(mAddress);
            dest.writeInt(mPort);
            dest.writeInt(mFlags);
            dest.writeString(mLogin);
            dest.writeString(mPassword);
            dest.writeString(mDomain);
            dest.writeString(mCapabilities);
            if ((mFlags & FLAG_OAUTH) != 0) {
                // TODO: This is nasty, but to be compatible with backward Exchange, we can't make any
                // change to the parcelable format. But we need Credential objects to be here.
                // So... only parcel or unparcel Credentials if the OAUTH flag is set. This will never
                // be set on HostAuth going to or coming from Exchange.
                dest.writeLong(mCredentialKey);
                if (mCredential == null) {
                    Credential.EMPTY.writeToParcel(dest, flags);
                } else {
                    mCredential.writeToParcel(dest, flags);
                }
            }

        }

        /**
         * Supports Parcelable
         */
        public HostAuth(Parcel in) {
            mBaseUri = CONTENT_URI;
            mId = in.readLong();
            mProtocol = in.readString();
            mAddress = in.readString();
            mPort = in.readInt();
            mFlags = in.readInt();
            mLogin = in.readString();
            mPassword = in.readString();
            mDomain = in.readString();
            mCapabilities = in.readString();
            if ((mFlags & FLAG_OAUTH) != 0) {
                // TODO: This is nasty, but to be compatible with backward Exchange, we can't make any
                // change to the parcelable format. But we need Credential objects to be here.
                // So... only parcel or unparcel Credentials if the OAUTH flag is set. This will never
                // be set on HostAuth going to or coming from Exchange.
                mCredentialKey = in.readLong();
                mCredential = new Credential(in);
                if (mCredential.equals(Credential.EMPTY)) {
                    mCredential = null;
                }
            } else {
                mCredentialKey = -1;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof HostAuth)) {
                return false;
            }
            HostAuth that = (HostAuth) o;
            return mPort == that.mPort && mFlags == that.mFlags
                    && TextUtils.equals(mProtocol, that.mProtocol)
                    && TextUtils.equals(mAddress, that.mAddress)
                    && TextUtils.equals(mLogin, that.mLogin)
                    && TextUtils.equals(mPassword, that.mPassword)
                    && TextUtils.equals(mDomain, that.mDomain);
        }

        @Override
        public int hashCode(){
            return super.hashCode();
        }

        /**
         * For debugger support only - DO NOT use for code.
         */
        @Override
        public String toString() {
            return getStoreUri();
        }
    }

    // P5_Porting_Email_Block_Start - worked by minsumr.kim
    // change@wtl.akulik start IT Policy 12.0
    public interface PoliciesColumns {
        public static final String ID = "_id";

        // Policy name
        public static final String NAME = "name";

        // Policy type (e.g. boolean, numeric)
        public static final String TYPE = "type";

        // Policy value
        public static final String VALUE = "value";

        // Associated account ID
        public static final String ACCOUNT_ID = "account_id";
    }

    public static class Policies extends EmailContent implements PoliciesColumns, Parcelable {

        public static final String TABLE_NAME = "Policies";

        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI + "/policies");
        public static final Uri CONTENT_URI_FROM_ACCOUNT_ID =
                Uri.parse(EmailContent.CONTENT_URI + "/policiesFromAccountId");
        
        public String mName;

        public String mType;

        public String mValue;

        public long mAccountId;

        public static final int CONTENT_ID_COLUMN = 0;

        public static final int CONTENT_NAME_COLUMN = 1;

        public static final int CONTENT_TYPE_COLUMN = 2;

        public static final int CONTENT_VALUE_COLUMN = 3;

        public static final int CONTENT_ACCOUNT_ID_COLUMN = 4;

        public static final String[] CONTENT_PROJECTION = new String[] {
                RECORD_ID, PoliciesColumns.NAME, PoliciesColumns.TYPE, PoliciesColumns.VALUE,
                PoliciesColumns.ACCOUNT_ID
        };

        public Policies() {
            mBaseUri = CONTENT_URI;
        }

        /**
         * Gets the number of policies defined for this account (stored in
         * database).
         * 
         * @param context
         * @param accountId
         * @return number of policies in the database related to this account
         */
        public static int getNumberOfPoliciesForAccount(Context context, long accountId) {
            int count = 0;
            Uri uri = ContentUris.withAppendedId(Policies.CONTENT_URI_FROM_ACCOUNT_ID, accountId);
            
            Cursor policyCursor = context.getContentResolver().query(uri, EmailContent.COUNT_COLUMNS, null, null, null);
            if (policyCursor != null && policyCursor.moveToFirst()) {
                count = policyCursor.getInt(0);
            }
            if (policyCursor != null) {
                policyCursor.close();
            }

            return count;
        }
        
        /**
         * Gets the policies for an account.
         * 
         * @param context
         * @param accountId
         * @return Cursor with alll policies for the account. The Caller MUST perform cursor.close() after use.
         */
        public static Cursor getPoliciesWithAccountId (Context context, long accountId) {
            Cursor policyCursor = null;

            Uri uri = ContentUris.withAppendedId(Policies.CONTENT_URI_FROM_ACCOUNT_ID, accountId);
            policyCursor = context.getContentResolver().query(uri, Policies.CONTENT_PROJECTION, null, null, null);
            return policyCursor;
        }

        /**
         * Restore a HostAuth from the database, given its unique id
         * 
         * @param context
         * @param id
         * @return the instantiated HostAuth
         */
        public static Policies restoreHostAuthWithId(Context context, long id) {
            checkNative();
            Uri u = ContentUris.withAppendedId(EmailContent.Policies.CONTENT_URI, id);
            Cursor c = null;

            try {
                c = context.getContentResolver().query(u, Policies.CONTENT_PROJECTION, null, null,
                        null);

                if (c != null && c.moveToFirst()) {
                    return getContent(c, Policies.class);
                } else {
                    return null;
                }
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        }

        @Override
        public void restore(Cursor cursor) {
            mBaseUri = CONTENT_URI;
            mId = cursor.getLong(CONTENT_ID_COLUMN);
            mName = cursor.getString(CONTENT_NAME_COLUMN);
            mType = cursor.getString(CONTENT_TYPE_COLUMN);
            mValue = cursor.getString(CONTENT_VALUE_COLUMN);
            mAccountId = cursor.getLong(CONTENT_ACCOUNT_ID_COLUMN);
        }

        @Override
        public ContentValues toContentValues() {
            ContentValues values = new ContentValues();
            values.put(PoliciesColumns.NAME, mName);
            values.put(PoliciesColumns.TYPE, mType);
            values.put(PoliciesColumns.VALUE, mValue);
            values.put(PoliciesColumns.ACCOUNT_ID, mAccountId);
            return values;
        }

        /**
         * Supports Parcelable
         */
        public int describeContents() {
            return 0;
        }

        /**
         * Supports Parcelable
         */
        public static final Parcelable.Creator<EmailContent.Policies> CREATOR = new Parcelable.Creator<EmailContent.Policies>() {
            public EmailContent.Policies createFromParcel(Parcel in) {
                return new EmailContent.Policies(in);
            }

            public EmailContent.Policies[] newArray(int size) {
                return new EmailContent.Policies[size];
            }
        };

        /**
         * Supports Parcelable
         */
        @Override
        public void writeToParcel(Parcel dest, int flags) {
            // mBaseUri is not parceled
            dest.writeLong(mId);
            dest.writeString(mName);
            dest.writeString(mType);
            dest.writeString(mValue);
            dest.writeLong(mAccountId);
        }

        /**
         * Supports Parcelable
         */
        public Policies(Parcel in) {
            mBaseUri = CONTENT_URI;
            mId = in.readLong();
            mName = in.readString();
            mType = in.readString();
            mValue = in.readString();
            mAccountId = in.readLong();
        }

        /**
         * Dump all sync policies for each of the EAS account
         * @param writer
         */
        public static void dump(Context context, PrintWriter writer, List<Account> accountList) {
            writer.println("\nBEGIN EAS sync policies dump");
            if (accountList == null || accountList.size() == 0) {
                writer.println("\nNo EAS account found");
                accountList = new ArrayList<Account>();
            }
            for (Account account : accountList) {
                Long accountId = account.mId;
                String displayName = account.getDisplayName();
                writer.println("\nACCOUNT NAME: " + displayName);
                Cursor c = getPoliciesWithAccountId(context, accountId);
                if (c != null && c.moveToFirst()) {
                    do {
                        writer.println(c.getString(Policies.CONTENT_NAME_COLUMN)
                               + "=" + c.getString(Policies.CONTENT_VALUE_COLUMN));
                    } while (c.moveToNext());
                }

                if (c != null)
                    c.close();
            }

            writer.println("\nEND EAS sync policies dump");
        }
    }

    // change@wtl.akulik end
    // change@wtl.jpshu document search begin
    public interface DocumentColumns {
        public static final String ID = "_id";
        // Basic columns used in document list presentation
        // The linkId
        public static final String LINK_ID = "linkId";
        // The name as shown to the user in a message list
        public static final String DISPLAY_NAME = "displayName";
        // Boolean, nonFolder = 0, folder = 1
        public static final String FLAG_FOLDER = "isFolder";
        // The time (millis) created
        public static final String TIMESTAMP = "creationDate";
        // The time (millis) of last modification
        public static final String LAST_MOD_DATE = "lastModifiedDate";
        // Boolean, nonHidden= 0, hidden = 1
        public static final String FLAG_HIDDEN = "ishidden";
        // The length of the content
        public static final String CONTENT_LENGTH = "contentLength";
        // The type of the content
        public static final String CONTENT_TYPE = "contentType";
        // References to other Email objects in the database
        // Foreign key to the Mailbox holding this message [INDEX]
        public static final String MAILBOX_KEY = "mailboxKey";
        // Foreign key to the Account holding this message
        public static final String ACCOUNT_KEY = "accountKey";
        public static final String PARENT_FOLDER_LINKID = "parentFolderLinkId";
        // public static final String SERVER_ID = "serverId";
        // public static final String SERVER_TIMESTAMP = "serverTimeStamp";
    }

    public static final class Document extends EmailContent implements DocumentColumns {
        public static final String TABLE_NAME = "Document";

        // To refer to a specific message, use
        // ContentUris.withAppendedId(CONTENT_URI, id)
        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI + "/document");
        public static final Uri UPDATED_CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI
                + "/updatedDocument");

        public static final String KEY_TIMESTAMP_DESC = DocumentColumns.TIMESTAMP + " desc";

        public static final int CONTENT_ID_COLUMN = 0;

        public static final int CONTENT_LINK_ID_COLUMN = 1;
        public static final int CONTENT_DISPLAY_NAME_COLUMN = 2;
        public static final int CONTENT_FLAG_FOLDER_COLUMN = 3;
        public static final int CONTENT_TIMESTAMP_COLUMN = 4;
        public static final int CONTENT_LAST_MOD_DATE_COLUMN = 5;
        public static final int CONTENT_FLAG_HIDDEN_COLUMN = 6;
        public static final int CONTENT_CONTENT_LENGTH_COLUMN = 7;
        public static final int CONTENT_CONTENT_TYPE_COLUMN = 8;
        public static final int CONTENT_MAILBOX_KEY_COLUMN = 9;
        public static final int CONTENT_ACCOUNT_KEY_COLUMN = 10;
        public static final int CONTENT_PARENT_FOLDER_LINKID = 11;
        public static final int CONTENT_SERVER_ID_COLUMN = 12;
        public static final int CONTENT_SERVER_TIMESTAMP_COLUMN = 13;

        public static final String[] CONTENT_PROJECTION = new String[] {
                RECORD_ID, DocumentColumns.LINK_ID, DocumentColumns.DISPLAY_NAME,
                DocumentColumns.FLAG_FOLDER, DocumentColumns.TIMESTAMP,
                DocumentColumns.LAST_MOD_DATE, DocumentColumns.FLAG_HIDDEN,
                DocumentColumns.CONTENT_LENGTH, DocumentColumns.CONTENT_TYPE,
                DocumentColumns.MAILBOX_KEY, DocumentColumns.ACCOUNT_KEY,
                DocumentColumns.PARENT_FOLDER_LINKID, SyncColumns.SERVER_ID,
                SyncColumns.SERVER_TIMESTAMP
        };

        public static final int LIST_ID_COLUMN = 0;
        public static final int LIST_DISPLAY_NAME_COLUMN = 1;
        public static final int LIST_TIMESTAMP_COLUMN = 2;
        public static final int LIST_FLAG_FOLDER_COLUMN = 3;
        public static final int LIST_FLAG_HIDDEN_COLUMN = 4;
        public static final int LIST_CONTENT_LENGTH_COLUMN = 5;
        public static final int LIST_CONTENT_TYPE_COLUMN = 6;
        public static final int LIST_MAILBOX_KEY_COLUMN = 7;
        public static final int LIST_ACCOUNT_KEY_COLUMN = 8;
        public static final int LIST_LINK_ID_COLUMN = 9;
        public static final int LIST_PARENT_FOLDER_LINKID = 10;

        // Public projection for common list columns
        public static final String[] LIST_PROJECTION = new String[] {
                RECORD_ID, DocumentColumns.DISPLAY_NAME, DocumentColumns.TIMESTAMP,
                DocumentColumns.FLAG_FOLDER, DocumentColumns.FLAG_HIDDEN,
                DocumentColumns.CONTENT_LENGTH, DocumentColumns.CONTENT_TYPE,
                DocumentColumns.MAILBOX_KEY, DocumentColumns.ACCOUNT_KEY, DocumentColumns.LINK_ID,
                DocumentColumns.PARENT_FOLDER_LINKID
        };

        public static final int ID_MAILBOX_COLUMN_ID = 0;
        public static final int ID_MAILBOX_COLUMN_MAILBOX_KEY = 1;
        public static final String[] ID_MAILBOX_PROJECTION = new String[] {
                RECORD_ID, DocumentColumns.MAILBOX_KEY
        };

        public static final String[] ID_COLUMN_PROJECTION = new String[] {
            RECORD_ID
        };
        // _id field is in AbstractContent

        public String mLinkId;
        public String mDisplayName;
        public long mTimeStamp;
        public long mLastModTime;
        public boolean mFlagFolder = false;
        public boolean mFlagHidden = false;
        public long mContentLength;
        public String mContentType;

        public long mMailboxKey;
        public long mAccountKey;
        public String mParentFolderLinkId;

        // public String mServerId;
        // public long mServerTimeStamp;

        public Document() {
            mBaseUri = CONTENT_URI;
        }

        // The following transient members may be used while building and
        // manipulating messages,
        // but they are NOT persisted directly by EmailProvider
        transient public ArrayList<Attachment> mAttachments = null;

        @Override
        public ContentValues toContentValues() {
            ContentValues values = new ContentValues();

            // Assign values for each row.
            values.put(DocumentColumns.LINK_ID, mLinkId);
            values.put(DocumentColumns.DISPLAY_NAME, mDisplayName);
            values.put(DocumentColumns.FLAG_FOLDER, mFlagFolder);
            values.put(DocumentColumns.TIMESTAMP, mTimeStamp);
            values.put(DocumentColumns.LAST_MOD_DATE, mLastModTime);
            values.put(DocumentColumns.FLAG_HIDDEN, mFlagHidden);
            values.put(DocumentColumns.CONTENT_LENGTH, mContentLength);
            values.put(DocumentColumns.CONTENT_TYPE, mContentType);

            values.put(DocumentColumns.MAILBOX_KEY, mMailboxKey);
            values.put(DocumentColumns.ACCOUNT_KEY, mAccountKey);
            values.put(DocumentColumns.PARENT_FOLDER_LINKID, mParentFolderLinkId);
            // values.put(DocumentColumns.SERVER_ID, mServerId);
            // values.put(DocumentColumns.SERVER_TIMESTAMP, mServerTimeStamp);

            return values;
        }

        public static Document restoreDocumentWithId(Context context, long id) {
            Uri u = ContentUris.withAppendedId(Document.CONTENT_URI, id);
            Cursor c = context.getContentResolver().query(u, Document.CONTENT_PROJECTION, null,
                    null, null);

            try {
                // change@wtl.jrabina Klocwork fix
                if (c != null && c.moveToFirst()) {
                    return getContent(c, Document.class);
                } else {
                    return null;
                }
            } finally {
                if (c != null)
                    c.close();
            }
        }

        public static Document restoreDocumentWithLinkId(Context context, String linkId) {
            Cursor c = context.getContentResolver().query(Document.CONTENT_URI,
                    Document.CONTENT_PROJECTION, DocumentColumns.LINK_ID + "=?", new String[] {
                        linkId
                    }, null);

            try {
                if (c != null && c.moveToFirst()) {
                    return getContent(c, Document.class);
                } else {
                    return null;
                }
            } finally {
                if (c != null)
                    c.close();
            }
        }

        @Override
        public/* EmailContent.Document */void restore(Cursor c) {
            mBaseUri = CONTENT_URI;
            mId = c.getLong(CONTENT_ID_COLUMN);

            mLinkId = c.getString(CONTENT_LINK_ID_COLUMN);
            mDisplayName = c.getString(CONTENT_DISPLAY_NAME_COLUMN);
            mFlagFolder = c.getInt(CONTENT_FLAG_FOLDER_COLUMN) == 1;
            mTimeStamp = c.getLong(CONTENT_TIMESTAMP_COLUMN);
            mLastModTime = c.getLong(CONTENT_LAST_MOD_DATE_COLUMN);
            mFlagHidden = c.getInt(CONTENT_FLAG_HIDDEN_COLUMN) == 1;
            mContentLength = c.getInt(CONTENT_CONTENT_LENGTH_COLUMN);
            mContentType = c.getString(CONTENT_CONTENT_TYPE_COLUMN);
            mMailboxKey = c.getLong(CONTENT_MAILBOX_KEY_COLUMN);
            mAccountKey = c.getLong(CONTENT_ACCOUNT_KEY_COLUMN);
            mParentFolderLinkId = c.getString(CONTENT_PARENT_FOLDER_LINKID);
            // mServerId = c.getString(CONTENT_SERVER_ID_COLUMN);
            // mServerTimeStamp = c.getLong(CONTENT_SERVER_TIMESTAMP_COLUMN);

            // return this;
        }

        public boolean update() {
            // TODO Auto-generated method stub
            return false;
        }

        /*
         * Override this so that we can store the Body first and link it to the
         * Message Also, attachments when we get there... (non-Javadoc)
         * @see
         * com.samsung.android.email.ui.provider.EmailContent#save(android.content.Context)
         */
        @Override
        public Uri save(Context context) {

            boolean doSave = !isSaved();

            // This logic is in place so I can (a) short circuit the expensive
            // stuff when
            // possible, and (b) override (and throw) if anyone tries to call
            // save() or update()
            // directly for Message, which are unsupported.
            if (mAttachments == null || mAttachments.isEmpty()) {
                if (doSave) {
                    return super.save(context);
                } else {
                    // Call update, rather than super.update in case we ever
                    // override it
                    if (update(context, toContentValues()) == 1) {
                        return getUri();
                    }
                    return null;
                }
            }

            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            addSaveOps(ops);
            try {
                ContentProviderResult[] results = context.getContentResolver().applyBatch(
                        AUTHORITY, ops);
                // If saving, set the mId's of the various saved objects
                if (doSave) {
                    Uri u = results[0].uri;
                    mId = Long.parseLong(u.getPathSegments().get(1));
                    if (mAttachments != null) {
                        int resultOffset = 2;
                        for (Attachment a : mAttachments) {
                            // Save the id of the attachment record
                            u = results[resultOffset++].uri;
                            if (u != null) {
                                a.mId = Long.parseLong(u.getPathSegments().get(1));
                            }
                            a.mMessageKey = mId;
                        }
                    }
                    return u;
                } else {
                    return null;
                }
            } catch (RemoteException e) {
                // There is nothing to be done here; fail by returning null
            } catch (OperationApplicationException e) {
                // There is nothing to be done here; fail by returning null
            }
            return null;
        }

        public void addSaveOps(ArrayList<ContentProviderOperation> ops) {
            // First, save the message
            ContentProviderOperation.Builder b = ContentProviderOperation.newInsert(mBaseUri);
            ops.add(b.withValues(toContentValues()).build());

            int messageBackValue = ops.size() - 1;

            // Create the attachments, if any
            if (mAttachments != null) {
                for (Attachment att : mAttachments) {
                    ops.add(ContentProviderOperation.newInsert(Attachment.CONTENT_URI)
                            .withValues(att.toContentValues())
                            .withValueBackReference(Attachment.MESSAGE_KEY, messageBackValue)
                            .build());
                }
            }

        }

    }
    
    // SISO@CRL_Cahce CRL cache columns START
    public interface CRLCacheColumns {

        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI + "/crlCache");

        public static final String TABLE_NAME = "CrlCache";

        public static final String ID = "_id";

        // CRL distribution point - URL
        public static final String DISTR_POINT_URL = "dp";

        // CRL file location on the device.
        public static final String CRL_URI = "crlLocation";

        // CRL next-update
        public static final String NEXT_UPDATE = "nextUpdate";

        // CRL usage timestamp.
        public static final String LAST_USED = "lastUsed";

        // CRL delta-CRL - Feshest CRL location
        public static final String FRESHEST_CRL = "freshestCrl";
    }

    // SISO@CRL_Cahce CRL cache columns END

    // change@wtl.jpshu document search end
    public interface CertificateCacheColumns {

        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI
                + "/certificateCache");

        public static final String TABLE_NAME = "CertificateCache";

        public static final String ID = "_id";

        // The recipients email address
        static final String EMAIL = "email";

        // The recipients certificate
        public static final String CERTIFICATE = "certificate";
        // change@wtl.rprominski smime support end
    }

//    // ============================================================================================
//    // Seven (2010.07.16)
//    // ============================================================================================
//    public interface MailboxCBColumns {
//        // sequence number.
//        public static final String ID = "_id";
//
//        // MailBox _id
//        public static final String MAILBOX_KEY = "mailboxKey";
//
//        // basic mail or seven mail 0(basic), 1(seven)
//        public static final String TYPE_MSG = "typeMsg";
//
//        public static final String SEVEN_MAILBOX_KEY = "sevenMailboxKey";
//
//        // In inbox basic forder, the personal box folder synk ( 0 or 1 )
//        public static final String SYNC_FLAG = "syncFlag";
//    }
//
//    public interface MessageCBColumns {
//        // sequence number.
//        public static final String ID = "_id";
//
//        // MailBox _id
//        public static final String MESSAGE_KEY = "messageKey";
//
//        // basic mail or seven mail 0(basic), 1(seven)
//        public static final String TYPE_MSG = "typeMsg";
//
//        public static final String SEVEN_MESSAGE_KEY = "sevenMessageKey";
//
//        public static final String MISSING_BODY = "missingBody";
//
//        public static final String MISSING_HTML_BODY = "missingHtmlBody";
//
//        public static final String UNK_ENCODING = "unkEncoding";
//
//        // msc, ksshin, 2010.7.29, added
//        public static final String SEVEN_ACCOUNT_KEY = "sevenAccountKey";
//    }
//
//    public interface AccountCBColumns {
//        public static final String ID = "_id";
//
//        // sequence number.
//        public static final String ACCOUNT_KEY = "accountKey";
//
//        // basic email account key
//        public static final String SEVEN_ACCOUNT_KEY = "sevenAccountKey";
//
//        // seven(premium) email account key
//        public static final String TYPE_MSG = "typeMsg";
//
//        // seven(premium) or basic email
//        // seven(premium) : 1, basic : 0
//        public static final String TIME_LIMIT = "timeLimit";
//
//        public static final String SIZE_LIMIT = "sizeLimit";
//
//        // Reads, the message body size(Currently is not using)
//        public static final String RECV_PROTOCOL = "recvProtocol";
//
//        // search basic email host(receive)
//        public static final String SEND_PROTOCOL = "sendProtocol";
//
//        // search basic email host(send)
//        public static final String PEAK_TIME = "peakTime";
//
//        public static final String OFF_PEAK_TIME = "offPeakTime";
//
//        public static final String DAYS = "days";
//
//        // email push days
//        public static final String PEAK_START_TIME = "peakStartTime";
//
//        // email push start time
//        public static final String PEAK_END_TIME = "peakEndTime";
//
//        // email push end time
//        public static final String WHILE_ROAMING = "whileRoaming";
//
//        public static final String ATTACHMENT_ENABLED = "attachmentEnabled";
//        // Attachment file capacity limitation
//
//    }
//
//    public static final class MailboxCB extends EmailContent implements SyncColumns,
//            MailboxCBColumns {
//
//        public static final String TABLE_NAME = "Mailbox_CB";
//
//        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI + "/mailboxcb");
//
//        public int mTypeMsg;
//
//        public long mMailboxKey;
//
//        public long mSevenMailboxKey;
//
//        public int mSyncFlag;
//
//        public static final int TYPE_MSG_BASIC = 0;
//
//        public static final int TYPE_MSG_SEVEN = 1;
//
//        public static final int CONTENT_ID_COLUMN = 0;
//
//        public static final int CONTENT_MAILBOX_KEY_COLUMN = 1;
//
//        public static final int CONTENT_TYPE_MESSAGE_COLUMN = 2;
//
//        public static final int CONTENT_SEVEN_MAILBOX_KEY_COLUMN = 3;
//
//        public static final int CONTENT_SYNC_FLAG = 4;
//
//        public static final String[] CONTENT_PROJECTION = new String[] {
//                RECORD_ID, MailboxCBColumns.MAILBOX_KEY, MailboxCBColumns.TYPE_MSG,
//                MailboxCBColumns.SEVEN_MAILBOX_KEY, MailboxCBColumns.SYNC_FLAG,
//        };
//
//        public static final long NO_MAILBOX = -1;
//
//        // huijae.lee not used
//        // private static final String WHERE_TYPE_AND_ACCOUNT_KEY =
//        // MailboxCBColumns.TYPE_MSG
//        // + "=? and " + MailboxCBColumns.MAILBOX_KEY + "=?";
//
//        public MailboxCB() {
//            super();
//            mBaseUri = CONTENT_URI;
//        }
//
//        @Override
//        public void restore(Cursor cursor) {
//            mBaseUri = CONTENT_URI;
//            mId = cursor.getLong(CONTENT_ID_COLUMN);
//            mMailboxKey = cursor.getLong(CONTENT_MAILBOX_KEY_COLUMN);
//            mTypeMsg = cursor.getInt(CONTENT_TYPE_MESSAGE_COLUMN);
//            mSevenMailboxKey = cursor.getLong(CONTENT_SEVEN_MAILBOX_KEY_COLUMN);
//            mSyncFlag = cursor.getInt(CONTENT_SYNC_FLAG);
//
//        }
//
//        @Override
//        public ContentValues toContentValues() {
//            ContentValues values = new ContentValues();
//            values.put(MailboxCBColumns.MAILBOX_KEY, mMailboxKey);
//            values.put(MailboxCBColumns.TYPE_MSG, mTypeMsg);
//            values.put(MailboxCBColumns.SEVEN_MAILBOX_KEY, mSevenMailboxKey);
//            values.put(MailboxCBColumns.SYNC_FLAG, mSyncFlag);
//            return values;
//        }
//
//        /**
//         * Searches basic mailboxKey with basic accountKey and seven mailboxKey.
//         * 
//         * @param context
//         * @param accountKey
//         * @param sevenMailboxKey
//         * @return
//         */
//        public static long findMailboxKey(Context context, long accountKey, long sevenMailboxKey) {
//            long mailboxId = NO_MAILBOX;
//            String[] bindArguments = new String[] {
//                    Long.toString(accountKey), Long.toString(sevenMailboxKey)
//            };
//
//            Cursor c = null;
//            try {
//                c = context.getContentResolver().query(Mailbox.CONTENT_URI, new String[] {
//                    "mailboxKey"
//                }, "accountKey=? and sevenMailboxKey=?", bindArguments, null);
//
//                if (c.moveToFirst()) {
//                    mailboxId = c.getLong(0);
//                }
//            } catch (Exception e) {
//                EmailLog.e(TAG, "Exception in findMailboxKey", e);
//            } finally {
//                if (c != null && !c.isClosed())
//                    c.close();
//            }
//            return mailboxId;
//        }
//
//        /**
//         * Restore a Mailbox from the database, given its unique id
//         * 
//         * @param context
//         * @param id
//         * @return the instantiated Mailbox
//         */
//        public static MailboxCB restoreMailboxWithId(Context context, long id) {
//            Uri u = ContentUris.withAppendedId(MailboxCB.CONTENT_URI, id);
//            Cursor c = null;
//            try {
//                c = context.getContentResolver().query(u, MailboxCB.CONTENT_PROJECTION, null, null,
//                        null);
//                if (c.moveToFirst()) {
//                    return EmailContent.getContent(c, MailboxCB.class);
//                } else {
//                    return null;
//                }
//            } catch (Exception e) {
//                EmailLog.e(TAG, "Exception in findMailboxKey", e);
//                return null;
//            } finally {
//                if (c != null && !c.isClosed())
//                    c.close();
//            }
//        }
//    }
//
//    /**
//     * Basic Mail Seven(Premium Mail) Combined Message Box => MessageCB
//     * 
//     * @author temp
//     */
//    public static final class MessageCB extends EmailContent implements SyncColumns,
//            MessageCBColumns {
//        public static final String TABLE_NAME = "Message_CB";
//
//        // To refer to a specific message, use
//        // ContentUris.withAppendedId(CONTENT_URI, id)
//        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI + "/messagecb");
//
//        public static final int CONTENT_ID_COLUMN = 0;
//
//        public static final int CONTENT_MESSAGE_KEY_COLUMN = 1;
//
//        public static final int CONTENT_TYPE_MESSAGE_COLUMN = 2;
//
//        public static final int CONTENT_SEVEN_MESSAGE_KEY_COLUMN = 3;
//
//        public static final int CONTENT_MISSING_BODY_COLUMN = 4;
//
//        public static final int CONTENT_MISSING_HTML_BODY_COLUMN = 5;
//
//        public static final int CONTENT_UNK_ENCODING_COLUMN = 6;
//
//        // msc, ksshin, 2010.7.29, added
//        public static final int CONTENT_SEVEN_ACCOUNT_KEY_COLUMN = 7;
//
//        public static final String[] CONTENT_PROJECTION = new String[] {
//                RECORD_ID, MessageCBColumns.MESSAGE_KEY, MessageCBColumns.TYPE_MSG,
//                MessageCBColumns.SEVEN_MESSAGE_KEY, MessageCBColumns.MISSING_BODY,
//                MessageCBColumns.MISSING_HTML_BODY, MessageCBColumns.UNK_ENCODING,
//                MessageCBColumns.SEVEN_ACCOUNT_KEY
//        };
//
//        // _id field is in AbstractContent
//        public int mTypeMsg;
//
//        public long mMessageKey;
//
//        public long mSevenMessageKey;
//
//        public int mMissingBody;
//
//        public int mMissingHtmlBody;
//
//        public int mUnkEncoding;
//
//        // msc, ksshin, 2010.7.29, added
//        public int mSevenAccountKey;
//
//        public MessageCB() {
//            super();
//            mBaseUri = CONTENT_URI;
//        }
//
//        @Override
//        public ContentValues toContentValues() {
//            ContentValues values = new ContentValues();
//            // Assign values for each row.
//            values.put(MessageCBColumns.MESSAGE_KEY, mMessageKey);
//            values.put(MessageCBColumns.TYPE_MSG, mTypeMsg);
//            values.put(MessageCBColumns.SEVEN_MESSAGE_KEY, mSevenMessageKey);
//            values.put(MessageCBColumns.MISSING_BODY, mMissingBody);
//            values.put(MessageCBColumns.MISSING_HTML_BODY, mMissingHtmlBody);
//            values.put(MessageCBColumns.UNK_ENCODING, mUnkEncoding);
//
//            // msc, ksshin, 2010.7.29, added
//            values.put(MessageCBColumns.SEVEN_ACCOUNT_KEY, mSevenAccountKey);
//
//            return values;
//        }
//
//        public static MessageCB restoreMessageCBWithId(Context context, long id) {
//            Uri u = ContentUris.withAppendedId(MessageCB.CONTENT_URI, id);
//            Cursor c = null;
//            try {
//                c = context.getContentResolver().query(u, MessageCB.CONTENT_PROJECTION, null, null,
//                        null);
//                if (c.moveToFirst()) {
//                    return getContent(c, MessageCB.class);
//                } else {
//                    return null;
//                }
//            } finally {
//                if (c != null && !c.isClosed())
//                    c.close();
//            }
//        }
//
//        @Override
//        public void restore(Cursor c) {
//            mBaseUri = CONTENT_URI;
//            mId = c.getLong(CONTENT_ID_COLUMN);
//            mMessageKey = c.getLong(CONTENT_MESSAGE_KEY_COLUMN);
//            mTypeMsg = c.getInt(CONTENT_TYPE_MESSAGE_COLUMN);
//            mSevenMessageKey = c.getLong(CONTENT_SEVEN_MESSAGE_KEY_COLUMN);
//
//            // msc, ksshin, 2010.7.29, added
//            mMissingBody = c.getInt(CONTENT_MISSING_BODY_COLUMN);
//            mMissingHtmlBody = c.getInt(CONTENT_MISSING_HTML_BODY_COLUMN);
//            mUnkEncoding = c.getInt(CONTENT_UNK_ENCODING_COLUMN);
//            mSevenAccountKey = c.getInt(CONTENT_SEVEN_ACCOUNT_KEY_COLUMN);
//
//        }
//
//        public void update(MessageCB message, Context context) {
//            // TODO Auto-generated method stub
//        }
//    }
//
//    public static final class AccountCB extends EmailContent implements AccountCBColumns,
//            Parcelable {
//        public static final String TABLE_NAME = "Account_CB";
//
//        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI + "/accountcb");
//
//        public static final int TYPE_MSG_BASIC = 0;
//
//        public static final int TYPE_MSG_SEVEN = 1;
//
//        public long mAccountKey;
//
//        public long mSevenAccountKey;
//
//        public int mTypeMsg;
//
//        public long mTimeLimit;
//
//        public long mSizeLimit;
//
//        public int mPushSync;
//
//        public String mSyncSchedule;
//
//        public int mKeepConnectionLowBattery;
//
//        public int mPeakTime; // peak time case email push yes or no
//
//        public int mOffPeakTime; // The case email push yes or no where will not
//        // be peak-time
//
//        public String mDays; // push days
//
//        public String mPeakStartTime;// push start time
//
//        public String mPeakEndTime; // push end time
//
//        public int mWhileroaming; // roaming at the time of push yes or no
//
//        public int mAttachmentEnabled;// Attachment file capacity limitation
//
//        public String mRecvProtocol;
//
//        public String mSendProtocol;
//
//        public static final int CONTENT_ID_COLUMN = 0;
//
//        public static final int CONTENT_ACCOUNT_KEY = 1;
//
//        public static final int CONTENT_SEVEN_ACCOUNT_KEY = 2;
//
//        public static final int CONTENT_TYPE_MSG = 3;
//
//        public static final int CONTENT_TIME_LIMIT = 4;
//
//        public static final int CONTENT_SIZE_LIMIT = 5;
//
//        // public static final int CONTENT_PUSH_SYNC = 6;
//        // public static final int CONTENT_SYNC_SCHEDULE = 7;
//        // public static final int CONTENT_KEEP_CONNECTION_LOW_BATTERY = 8;
//
//        // 2010.09.07
//        public static final int CONTENT_PEAK_TIME = 6;
//
//        public static final int CONTENT_OFF_PEAK_TIME = 7;
//
//        public static final int CONTENT_DAYS = 8;
//
//        public static final int CONTENT_PEAK_START_TIME = 9;
//
//        public static final int CONTENT_PEAK_END_TIME = 10;
//
//        public static final int CONTENT_WHILE_ROAMING = 11;
//
//        public static final int CONTENT_ATTACHMENT_ENABLED = 12;
//
//        // SSE_MSC_S, Seokoo.Han, 2010.08.10, For eas.
//        public static final int CONTENT_RECV_PROTOCOL = 6;
//
//        public static final int CONTENT_SEND_PROTOCOL = 7;
//
//        // SSE_MSC_E, Seokoo.Han, 2010.08.10, For eas.
//
//        /*
//         * public static final String[] CONTENT_PROJECTION = new String[] {
//         * RECORD_ID, AccountCBColumns.ACCOUNT_KEY,
//         * AccountCBColumns.SEVEN_ACCOUNT_KEY, AccountCBColumns.TYPE_MSG,
//         * AccountCBColumns.TIME_LIMIT, AccountCBColumns.SIZE_LIMIT,
//         * AccountCBColumns.PUSH_SYNC, AccountCBColumns.SYNC_SCHEDULE,
//         * AccountCBColumns.KEEP_CONNECTION_LOW_BATTERY,
//         * AccountCBColumns.PEAK_TIME, AccountCBColumns.OFF_PEAK_TIME,
//         * AccountCBColumns.DAYS, AccountCBColumns.PEAK_START_TIME,
//         * AccountCBColumns.PEAK_END_TIME, AccountCBColumns.WHILE_ROAMING,
//         * AccountCBColumns.ATTACHMENT_ENABLED };
//         */
//
//        public static final String[] CONTENT_PROJECTION = new String[] {
//                RECORD_ID, AccountCBColumns.ACCOUNT_KEY, AccountCBColumns.SEVEN_ACCOUNT_KEY,
//                AccountCBColumns.TYPE_MSG, AccountCBColumns.TIME_LIMIT,
//                AccountCBColumns.SIZE_LIMIT, AccountCBColumns.PEAK_TIME,
//                AccountCBColumns.OFF_PEAK_TIME, AccountCBColumns.DAYS,
//                AccountCBColumns.PEAK_START_TIME, AccountCBColumns.PEAK_END_TIME,
//                AccountCBColumns.WHILE_ROAMING, AccountCBColumns.ATTACHMENT_ENABLED
//        };
//
//        public static String[] CONTENT_PROJECTION_PROTOCOL = {
//                EmailContent.RECORD_ID, AccountCBColumns.ACCOUNT_KEY,
//                AccountCBColumns.SEVEN_ACCOUNT_KEY, AccountCBColumns.TYPE_MSG,
//                AccountCBColumns.TIME_LIMIT, AccountCBColumns.SIZE_LIMIT,
//                AccountCBColumns.RECV_PROTOCOL, AccountCBColumns.SEND_PROTOCOL
//        };
//
//        public static final String WHERE_ACCOUNT_KEY = AccountCBColumns.ACCOUNT_KEY + "=?";
//
//        public static final int CONTENT_MAILBOX_TYPE_COLUMN = 1;
//
//        /**
//         * no public constructor since this is a utility class
//         */
//        public AccountCB() {
//            super();
//            mBaseUri = CONTENT_URI;
//        }
//
//        // msc, ksshin, 2010.7.29, added
//        public static AccountCB restoreMessageWithId(Context context, long basicAccountId) {
//            Uri u = ContentUris.withAppendedId(AccountCB.CONTENT_URI, basicAccountId);
//            Cursor c = null;
//            try {
//                c = context.getContentResolver().query(u, AccountCB.CONTENT_PROJECTION, null, null,
//                        null);
//                if (c.moveToFirst()) {
//                    return getContent(c, AccountCB.class);
//                } else {
//                    return null;
//                }
//            } finally {
//                if (c != null && !c.isClosed())
//                    c.close();
//            }
//        }
//
//        @Override
//        public void restore(Cursor cursor) {
//            mId = cursor.getLong(CONTENT_ID_COLUMN);
//            mBaseUri = CONTENT_URI;
//            mAccountKey = cursor.getLong(CONTENT_ACCOUNT_KEY);
//            mSevenAccountKey = cursor.getLong(CONTENT_SEVEN_ACCOUNT_KEY);
//            mTypeMsg = cursor.getInt(CONTENT_TYPE_MSG);
//            mTimeLimit = cursor.getLong(CONTENT_TIME_LIMIT);
//            mSizeLimit = cursor.getLong(CONTENT_SIZE_LIMIT);
//            // mPushSync = cursor.getInt(CONTENT_PUSH_SYNC);
//            // mSyncSchedule = cursor.getString(CONTENT_SYNC_SCHEDULE);
//            // mKeepConnectionLowBattery =
//            // cursor.getInt(CONTENT_KEEP_CONNECTION_LOW_BATTERY);
//
//            // 2010.09.07. add
//            mPeakTime = cursor.getInt(CONTENT_PEAK_TIME);
//            mOffPeakTime = cursor.getInt(CONTENT_OFF_PEAK_TIME);
//            mDays = cursor.getString(CONTENT_DAYS);
//            mPeakStartTime = cursor.getString(CONTENT_PEAK_START_TIME);
//            mPeakEndTime = cursor.getString(CONTENT_PEAK_END_TIME);
//            mWhileroaming = cursor.getInt(CONTENT_WHILE_ROAMING);
//            mAttachmentEnabled = cursor.getInt(CONTENT_ATTACHMENT_ENABLED);
//
//        }
//
//        /**
//         * @return the flags for this account
//         */
//        public long getAccountKey() {
//            return mAccountKey;
//        }
//
//        /**
//         * Set the flags for this account
//         */
//        public void setAccountKey(long AccountKey) {
//            mAccountKey = AccountKey;
//        }
//
//        /**
//         * @return the email address for this account
//         */
//        public long getSevenAccountKey() {
//            return mSevenAccountKey;
//        }
//
//        /**
//         * set seven mail account key
//         */
//        public void setSevenAccountKey(int SevenAccountKey) {
//            mSevenAccountKey = SevenAccountKey;
//        }
//
//        /**
//         * mail type (0:basic, 1:seven)
//         */
//        public int getTypeMsg() {
//            return mTypeMsg;
//        }
//
//        /**
//         * set mail type
//         */
//        public void setTypeMsg(int typeMsg) {
//            mTypeMsg = typeMsg;
//        }
//
//        /**
//         * seven mail time limit
//         */
//        public long getTimeLimit() {
//            return mTimeLimit;
//        }
//
//        /**
//         * set seven mail time limit
//         */
//        public void setTimeLimit(long TimeLimit) {
//            mTimeLimit = TimeLimit;
//        }
//
//        /**
//         * seven mail size limit
//         */
//        public long getSizeLimit() {
//            return mSizeLimit;
//        }
//
//        /**
//         * set seven mail size limit
//         */
//        public void setSizeLimit(long SizeLimit) {
//            mSizeLimit = SizeLimit;
//        }
//
//        public static long getAccountKey(Context context, long sevenAccountKey) {
//            Cursor cursor = null;
//
//            try {
//                cursor = context.getContentResolver().query(CONTENT_URI, new String[] {
//                    AccountCBColumns.ACCOUNT_KEY
//                }, AccountCBColumns.SEVEN_ACCOUNT_KEY + "=?", new String[] {
//                    "" + sevenAccountKey
//                }, null);
//
//                if (cursor.moveToFirst()) {
//                    return cursor.getLong(0); // column 0 is id
//                }
//            } catch (Exception e) {
//                EmailLog.e(TAG, "Exception in getAccountKey", e);
//            } finally {
//                if (cursor != null && !cursor.isClosed())
//                    cursor.close();
//            }
//            return -1;
//        }
//
//        @Override
//        public ContentValues toContentValues() {
//            ContentValues values = new ContentValues();
//            values.put(AccountCBColumns.ACCOUNT_KEY, mAccountKey);
//            values.put(AccountCBColumns.SEVEN_ACCOUNT_KEY, mSevenAccountKey);
//            values.put(AccountCBColumns.TIME_LIMIT, mTimeLimit);
//            values.put(AccountCBColumns.SIZE_LIMIT, mSizeLimit);
//            values.put(AccountCBColumns.TYPE_MSG, mTypeMsg);
//
//            // 2010.09.07, add
//            values.put(AccountCBColumns.PEAK_TIME, mPeakTime);
//            values.put(AccountCBColumns.OFF_PEAK_TIME, mOffPeakTime);
//            values.put(AccountCBColumns.DAYS, mDays);
//            values.put(AccountCBColumns.PEAK_START_TIME, mPeakStartTime);
//            values.put(AccountCBColumns.PEAK_END_TIME, mPeakEndTime);
//            values.put(AccountCBColumns.WHILE_ROAMING, mWhileroaming);
//            values.put(AccountCBColumns.ATTACHMENT_ENABLED, mAttachmentEnabled);
//
//            return values;
//        }
//
//        /**
//         * Override update to enforce a single default account, and do it
//         * atomically
//         */
//        @Override
//        public int update(Context context, ContentValues cv) {
//            return context.getContentResolver().update(CONTENT_URI, cv, WHERE_ACCOUNT_KEY,
//                    new String[] {
//                        "" + mId
//                    });
//        }
//
//        /*
//         * Override this so that we can store the HostAuth's first and link them
//         * to the Account (non-Javadoc)
//         * @see
//         * com.samsung.android.email.ui.provider.EmailContent#save(android.content.Context)
//         */
//        @Override
//        public Uri save(Context context) {
//            if (isSaved()) {
//                throw new UnsupportedOperationException();
//            }
//            return null;
//        }
//
//        /**
//         * Supports Parcelable
//         */
//        public AccountCB(Parcel in) {
//            super();
//            mBaseUri = EmailContent.Account.CONTENT_URI;
//            mId = in.readLong();
//            mAccountKey = in.readLong();
//            mSevenAccountKey = in.readLong();
//            mTypeMsg = in.readInt();
//            mTimeLimit = in.readLong();
//            mSizeLimit = in.readLong();
//
//            mPeakTime = in.readInt();
//            mOffPeakTime = in.readInt();
//            mDays = in.readString();
//            mPeakStartTime = in.readString();
//            mPeakEndTime = in.readString();
//            mWhileroaming = in.readInt();
//            mAttachmentEnabled = in.readInt();
//        }
//
//        /**
//         * For debugger support only - DO NOT use for code.
//         */
//        public String toString() {
//            return "";
//        }
//
//        public int describeContents() {
//            // TODO Auto-generated method stub
//            return 0;
//        }
//
//        public void writeToParcel(Parcel arg0, int arg1) {
//            // TODO Auto-generated method stub
//        }
//
//        // msc, sunyoung, 2010.07.26, accountKey, sevenAccountKey,
//        // hostAuthProtocol, search
//        public static AccountCB selectAccountProtocol(Context context, long accountKey) {
//            Cursor c = null;
//            try {
//                c = context.getContentResolver().query(
//                        Uri.parse(EmailContent.CONTENT_URI + "/accountcb/protocol"),
//                        CONTENT_PROJECTION_PROTOCOL, "" + accountKey, null, null);
//                if (c.moveToFirst()) {
//                    AccountCB cb = new AccountCB();
//                    cb.mAccountKey = c.getLong(CONTENT_ACCOUNT_KEY);
//                    cb.mSevenAccountKey = c.getLong(CONTENT_SEVEN_ACCOUNT_KEY);
//                    cb.mTypeMsg = c.getInt(CONTENT_TYPE_MSG);
//                    cb.mRecvProtocol = c.getString(CONTENT_RECV_PROTOCOL);
//                    cb.mSendProtocol = c.getString(CONTENT_SEND_PROTOCOL);
//                    return cb;
//                } else {
//                    return null;
//                }
//            } catch (Exception e) {
//                EmailLog.e(TAG, "Exception in selectAccountProtocol", e);
//                return null;
//            } finally {
//                if (c != null && !c.isClosed())
//                    c.close();
//            }
//        }
//    }

    // sh2209.cho : Account registration history
    public interface HistoryAccountColumns {

        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI

        + "/historyAccount");

        public static final String TABLE_NAME = "historyAccount";

        public static final String ID = "_id";

        public static final String EMAIL = "EmailAddress";

        public static final String TIMEDATE = "TimeDate";
    }

    // sh2209.cho : Add LDAP TABLE
    public interface LDAPAccountColumns {

        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI

        + "/ldapaccount");

        public static final String TABLE_NAME = "ldapaccount";

        public static final String ID = "_id";

        public static final String FULL_ADDRESS = "FullAddress";

        public static final String SERVER = "LDAPServer";

        public static final String BASEDN = "BaseDN";

        public static final String PORT = "Port";

        public static final String USERNAME = "UserName";

        public static final String PASSWORD = "Password";

        public static final String SSL = "SSL";

        public static final String PASSWORDENC = "passwordenc";

        public static final String TRUST_ALL = "trustAll";
    }
    
    // VIP List.
    public interface VIPListColumns {

        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI

        + "/viplist");

        public static final String TABLE_NAME = "viplist";

        public static final String ID = "_id";

        public static final String CONTACT_ID = "Contact_Id";
        
        public static final String EMAIL_ID = "Email_Id";
        
        public static final String EMAIL_ADDRESS = "EmailAddress";
        
        public static final String DISPLAY_NAME = "DisplayName";
        
        public static final String SENDER_ORDER = "Sender_Order";
    }
    
    public static final class VIPList extends EmailContent implements VIPListColumns {
        public static final String TABLE_NAME = "viplist";

        public static final int CONTENT_VIPLIST_ID_COLUMN = 0;
        public static final int CONTENT_VIPLIST_CONTACT_ID_COLUMN = 1;
        public static final int CONTENT_VIPLIST_EMAIL_ID_COLUMN = 2;
        public static final int CONTENT_VIPLIST_EMAIL_ADDRESS_COLUMN = 3;
        public static final int CONTENT_VIPLIST_DISPLAY_NAME_COLUMN = 4;
        public static final int CONTENT_VIPLIST_SENDER_ORDER_COLUMN = 5;

        public int mVipListId;
        public int mVipListContactId;
        public int mVipListEmailId;
        public String mVipListEmailAddress;
        public String mVipListDisplayName;
        public int mVipListSenderOrder;


        public static final String[] CONTENT_PROJECTION = new String[] {
                RECORD_ID, VIPListColumns.CONTACT_ID, VIPListColumns.EMAIL_ID,
                VIPListColumns.EMAIL_ADDRESS, VIPListColumns.DISPLAY_NAME,
                VIPListColumns.SENDER_ORDER
        };
    	
    	public void setContactId(int contactId) {
    		mVipListContactId = contactId;
        }
    	
    	public int getContactId() {
            return mVipListContactId;
        }
    	
    	public void setEmailId(int emailId) {
    		mVipListEmailId = emailId;
        }
    	
    	public int getEmailId() {
            return mVipListEmailId;
        }
    	
    	public void setEmailAddress(String emailAddress) {
    		mVipListEmailAddress = emailAddress;
        }
    	
    	public String getEmailAddress() {
            return mVipListEmailAddress;
        }
    	
    	public void setDisplayName(String displayName) {
    		mVipListDisplayName = displayName;
        }
    	
    	public String getDisplayName() {
            return mVipListDisplayName;
        }
    	
    	public void setSenderOrder(int senderOrder) {
    		mVipListSenderOrder = senderOrder;
        }
    	
    	public int getSenderOrder() {
            return mVipListSenderOrder;
        }
        
        /**
         * no public constructor since this is a utility class
         */
        public VIPList() {
        	mBaseUri = VIPListColumns.CONTENT_URI;
        }

        @Override
        public ContentValues toContentValues() {
            ContentValues values = new ContentValues();
            values.put(VIPListColumns.CONTACT_ID, mVipListContactId);
            values.put(VIPListColumns.EMAIL_ID, mVipListEmailId);
            values.put(VIPListColumns.EMAIL_ADDRESS, mVipListEmailAddress);
            values.put(VIPListColumns.DISPLAY_NAME, mVipListDisplayName);
            values.put(VIPListColumns.SENDER_ORDER, mVipListSenderOrder);
            return values;
        }

        @Override
        public void restore(Cursor cursor) {
        	mBaseUri = VIPListColumns.CONTENT_URI;
        	mVipListId = cursor.getInt(CONTENT_VIPLIST_ID_COLUMN);
        	mVipListContactId = cursor.getInt(CONTENT_VIPLIST_CONTACT_ID_COLUMN);
        	mVipListEmailId = cursor.getInt(CONTENT_VIPLIST_EMAIL_ID_COLUMN);
        	mVipListEmailAddress = cursor.getString(CONTENT_VIPLIST_EMAIL_ADDRESS_COLUMN);
        	mVipListDisplayName = cursor.getString(CONTENT_VIPLIST_DISPLAY_NAME_COLUMN);
        	mVipListSenderOrder = cursor.getInt(CONTENT_VIPLIST_SENDER_ORDER_COLUMN);
        }
    }
    
    
    public interface FilterListColumns {

        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI

        + "/filter");

        public static final String TABLE_NAME = "filter";

        public static final String ID = "_id";

        public static final String CONTACT_ID = "Contact_Id";
        
        public static final String EMAIL_ID = "Email_Id";
        
        public static final String EMAIL_ADDRESS = "EmailAddress";
        
        public static final String DISPLAY_NAME = "DisplayName";
        
        public static final String SUBJECT_NAME = "SubjectName";
        
        public static final String FOLDER_NAME = "FolderName";
        
    }

    //    // RecentHistory List.
//    public interface RecentHistoryListColumns {
//
//        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI
//
//        + "/recentHistory");
//
//        public static final String TABLE_NAME = "recentHistory";
//
//        public static final String ID = "_id";
//
//        public static final String OPEN_TIME = "OpenTime";
//        
//        public static final String ACCOUNT_KEY = "AccountKey";
//        
//        public static final String MESSAGE_ID = "MessageId";
//        
//    }

    // ==============================================================================================
    public interface EmailContextualUsageInfoColumns {

        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI + "/ctxusageinfo");

        public static final Uri CONTENT_URI_CAT1 = Uri.parse(EmailContent.CONTENT_URI
                + "/ctxusageinfo/cat1");

        public static final String TABLE_NAME = "ctxusageinfo";

        public static final String ID = "_id";

        public static final String CATEGORY_1 = "cat_1"; // 0 for composer
                                                         // attachment;

        public static final String CATEGORY_2 = "cat_2";

        public static final String CATEGORY_3 = "cat_3";

        public static final String DATA_TXT_1 = "data_txt_1";

        public static final String DATA_TXT_2 = "data_txt_2";

        public static final String DATA_INT_1 = "data_int_1";// app id for
                                                             // category 0

        public static final String DATA_INT_2 = "data_int_2";// usage count for
                                                             // category 0

        public static final String DATA_INT_3 = "data_int_3";// last access time
                                                             // for category
                                                             // 0

        public static final String DATA_INT_4 = "data_int_4";
    }

    // ============================================================================================

    public interface EmailAddressCacheColumns {
        public static final String ID = "_id";

        public static final String EMAIL_ADDRESS = "accountAddress";

        public static final String NAME = "accountName";

        public static final String SOURCE = "accountSource";

        public static final String PREFIX = "accountPrefix";

        public static final String OPERATION_TYPE = "operationType";

        public static final String TIMESTAMP = "timeStamp";

        public static final String USAGE_COUNT = "usageCount";

        public static final String RANK = "accountRank";
        
        public static final String PHOTO_CONTENT_BYTES = "photocontentbytes";
    }

    public static class EmailAddressCache extends EmailContent implements Parcelable, EmailAddressCacheColumns{

        public static final String TABLE_NAME = "EmailAddressCache";

        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI
                + "/emailaddresscache");

        public static final Uri CONTENT_FILTER_URI = Uri.withAppendedPath(CONTENT_URI, "filter");
        
        public static final int CONTENT_ID_COLUMN = 0;
        public static final int CONTENT_EMAIL_ADDRESS_COLUMN = 1;
        public static final int CONTENT_NAME_COLUMN = 2;
        public static final int CONTENT_OPERATION_TYPE_COLUMN = 3;
        public static final int CONTENT_PREFIX_COLUMN = 4;
        public static final int CONTENT_RANK_COLUMN = 5;
        public static final int CONTENT_SOURCE_COLUMN = 6;
        public static final int CONTENT_TIMESTAMP_COLUMN = 7;
        public static final int CONTENT_USAGE_COUNT_COLUMN = 8;
        
        public static final int CONTENT_PHOTO_CONTENT_BYTES_COLUMN = 9; 
                
        public String mEmailAddress;
        public String mDisplayName;
        public String mSource;
        public String mPrefix;
        public long mTimestamp;
        public int mUsagecount;
        public int mOperationType;
        public int mRank;
        
        public byte[] mPhotoContentBytes;
        
        public static final String[] CONTENT_PROJECTION = new String[] {
            RECORD_ID,
            EmailAddressCacheColumns.EMAIL_ADDRESS,
            EmailAddressCacheColumns.NAME,
            EmailAddressCacheColumns.OPERATION_TYPE,
            EmailAddressCacheColumns.PREFIX,
            EmailAddressCacheColumns.RANK,
            EmailAddressCacheColumns.SOURCE,
            EmailAddressCacheColumns.TIMESTAMP,
            EmailAddressCacheColumns.USAGE_COUNT,
            EmailAddressCacheColumns.PHOTO_CONTENT_BYTES
        };
        
        public EmailAddressCache(String emailAddress, String displayName, String source,
                String prefix, long timestam, int usagecoun, int operationType, int rank,
                byte[] contentBytes) {
            mBaseUri = CONTENT_URI;

            mEmailAddress = emailAddress;
            mDisplayName = displayName;
            mSource = source;
            mPrefix = prefix;
            mTimestamp = timestam;
            mUsagecount = usagecoun;
            mOperationType = operationType;
            mRank = rank;
            
            mPhotoContentBytes = contentBytes;
        }
        
        public EmailAddressCache(Parcel in) {
            mBaseUri = EmailContent.EmailAddressCache.CONTENT_URI;
            mId = in.readLong();
            mEmailAddress = in.readString();
            mDisplayName = in.readString();
            mSource = in.readString();
            mPrefix = in.readString();
            
            mTimestamp = in.readLong();
            
            mUsagecount = in.readInt();
            mOperationType = in.readInt();
            mRank = in.readInt();
            
            final int contentBytesLen = in.readInt();
            if (contentBytesLen == -1) {
                mPhotoContentBytes = null;
            } else {
                mPhotoContentBytes = new byte[contentBytesLen];
                in.readByteArray(mPhotoContentBytes);
            }
        }
        
        public static EmailAddressCache restoreEmailAddressCacheWithId(Context context, long id) {
            Uri u = ContentUris.withAppendedId(EmailAddressCache.CONTENT_URI, id);
            Cursor c = null;
            try {
                c = context.getContentResolver().query(u, EmailAddressCache.CONTENT_PROJECTION, null, null,
                        null);

                if (c == null) {
                    EmailLog.d(TAG, "restoreEmailAddressCacheWithId. Cursor is NULL. Query failed");
                    return null;
                }
                try {
                    if (c.moveToFirst()) {
                        return getContent(c, EmailAddressCache.class);
                    } else {
                        EmailLog.d(TAG, "restoreEmailAddressCacheWithId. Cursor is empty. EmailAddressCache does not exist id:" + id);
                        return null;
                    }
                } catch (NullPointerException ne) { // Memory Full Exception
                    ne.printStackTrace();
                    return null;
                } finally {
                    if (c != null && !c.isClosed())
                        c.close();
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                EmailLog.d(TAG, "restoreEmailAddressCacheWithId. RuntimeException in restoreEmailAddressCacheWithId", e);
                return null;
            }
        }
        
        public static EmailAddressCache restoreEmailAddressCacheWithEmailAddress(Context context, String email) {
            Cursor c = null;
            try {
                String[] selectionArgs = new String[1];
                selectionArgs[0] = email;
                c = context.getContentResolver().query(EmailAddressCache.CONTENT_URI,
                        EmailAddressCache.CONTENT_PROJECTION, EmailAddressCacheColumns.EMAIL_ADDRESS + "=?"
                        + " COLLATE NOCASE", selectionArgs, null);
                if (c != null && c.moveToFirst()) {
                    return getContent(c, EmailAddressCache.class);
                } else {
                    return null;
                }
            } catch (Exception e) {
                EmailLog.d(TAG, "IllegalStateException in restoreEmailAddressCacheWithEmailAddress", e);
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        }

        @Override
        public void restore(Cursor cursor) {
            mId = cursor.getLong(CONTENT_ID_COLUMN);
            mEmailAddress = cursor.getString(CONTENT_EMAIL_ADDRESS_COLUMN);
            mDisplayName = cursor.getString(CONTENT_NAME_COLUMN);
            mSource = cursor.getString(CONTENT_OPERATION_TYPE_COLUMN);
            mPrefix = cursor.getString(CONTENT_PREFIX_COLUMN);
            mTimestamp = cursor.getLong(CONTENT_RANK_COLUMN);
            mUsagecount = cursor.getInt(CONTENT_SOURCE_COLUMN);
            mOperationType = cursor.getInt(CONTENT_TIMESTAMP_COLUMN);
            mRank = cursor.getInt(CONTENT_USAGE_COUNT_COLUMN);
            
            mPhotoContentBytes = cursor.getBlob(CONTENT_PHOTO_CONTENT_BYTES_COLUMN);
  
            return;
        }

        @Override
        public ContentValues toContentValues() {
            ContentValues values = new ContentValues();
            
            values.put(EmailAddressCacheColumns.EMAIL_ADDRESS, mEmailAddress);
            values.put(EmailAddressCacheColumns.NAME, mDisplayName);
            values.put(EmailAddressCacheColumns.OPERATION_TYPE, mOperationType);
            values.put(EmailAddressCacheColumns.PREFIX, mPrefix);
            values.put(EmailAddressCacheColumns.RANK, mRank);
            values.put(EmailAddressCacheColumns.SOURCE, mSource);
            values.put(EmailAddressCacheColumns.TIMESTAMP, mTimestamp);
            values.put(EmailAddressCacheColumns.USAGE_COUNT, mUsagecount);
            
            values.put(EmailAddressCacheColumns.PHOTO_CONTENT_BYTES, mPhotoContentBytes);
            
            return values;
        }

        public int describeContents() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(mId);
            dest.writeString(mEmailAddress);
            dest.writeString(mDisplayName);
            dest.writeString(mSource);
            dest.writeString(mPrefix);
            dest.writeLong(mTimestamp);
            dest.writeInt(mUsagecount);
            dest.writeInt(mOperationType);
            dest.writeInt(mRank);
            
            if (mPhotoContentBytes == null) {
                dest.writeInt(-1);
            } else {
                dest.writeInt(mPhotoContentBytes.length);
                dest.writeByteArray(mPhotoContentBytes);
            }
        }
        
        public void setEmailAddress(String emailAddress) {
            mEmailAddress = emailAddress;
        }
        
        public String getEmailAddress() {
            return mEmailAddress;
        }
        
        public String getDisplayName() {
            return mDisplayName;
        }
        
        public void setDisplayName(String description) {
            mDisplayName = description;
        }
        
        public byte[] getPhotoContentBytes() {
            return mPhotoContentBytes;
        }
        
        public void setPhotoContentBytes(byte[] contentbytes) {
            mPhotoContentBytes = contentbytes;
        }

    }

    // changes@siso.atul BlackList Start
    public interface BlackListColumns {
        public static final String USER_NAME = "userName";
        public static final String EMAIL_ADDRESS = "emailAddress";
        public static final String ACCOUNT_KEY = "accountId";
        public static final String LAST_ACCESSED_TIME_STAMP = "lastAccessedTimeStamp";
        public static final String IS_DOMAIN = "isDomain";
    }

    public static final class BlackList extends EmailContent implements BlackListColumns {
        public static final String TABLE_NAME = "BlackList";
        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI + "/blacklist");

        public static final int CONTENT_BLACKLIST_NAME_COLUMN = 1;
        public static final int CONTENT_BLACKLIST_ADDRESS_COLUMN = 2;
        public static final int CONTENT_BLACKLIST_ACCOUNT_KEY_COLUMN = 3;
        public static final int CONTENT_BLACKLIST_LAST_ACCESSED_TIME_STAMP = 4;
        public static final int CONTENT_BLACKLIST_IS_DOMAIN = 5;

        public String mBlackListName;
        public String mBlackListEmailAddress;
        public long mBlackListAccountKey;
        public long mLastAccessedTimeStamp;
        public int mIsDomain;

        public static final String[] CONTENT_PROJECTION = new String[] {
                RECORD_ID, BlackListColumns.USER_NAME, BlackListColumns.EMAIL_ADDRESS,
                BlackListColumns.ACCOUNT_KEY, BlackListColumns.LAST_ACCESSED_TIME_STAMP,
                BlackListColumns.IS_DOMAIN
        };

        public static final String[] BLACKLIST_EMAIL_PROJECTION = new String[] {
            BlackListColumns.EMAIL_ADDRESS,
        };

        /**
         * no public constructor since this is a utility class
         */
        public BlackList() {
        	mBaseUri = CONTENT_URI;
        }
        
        public void setBlackListName (String blackListName) {
        	mBlackListName = blackListName;
        }
        
        public String getBlackListName () {
        	return mBlackListName;
        }
        
        public void setBlackListEmailAddress(String blackListEmailAddress) {
        	mBlackListEmailAddress = blackListEmailAddress;
        }
        
        public String getBlackListEmailAddress () {
        	return mBlackListEmailAddress;
        }
        
        public void setBlackListAccountKey(long blackListAccountKey) {
        	mBlackListAccountKey = blackListAccountKey;
        }
        
        public long getBlackListAccountKey () {
        	return mBlackListAccountKey;
        }
        
        public void setLastAccessedTimeStamp(long lastAccessedTimeStamp) {
        	mLastAccessedTimeStamp = lastAccessedTimeStamp;
        }
        
        public long getLastAccessedTimeStamp () {
        	return mLastAccessedTimeStamp;
        }
        
        public void setIsDomain(int isDomain) {
        	mIsDomain = isDomain;
        }
        
        public int getIsDomain () {
        	return mIsDomain;
        }

        /**
         * Restore a BlackList from the database, given its unique id
         * 
         * @param context
         * @param id
         * @return the instantiated BlackList
         */
        public static BlackList restoreBlackListWithId(Context context, long id) {
            Uri u = ContentUris.withAppendedId(EmailContent.BlackList.CONTENT_URI, id);
            Cursor c = null;
            try {
                c = context.getContentResolver().query(u, BlackList.CONTENT_PROJECTION, null, null,
                        null);

                if (c != null && c.moveToFirst()) {
                    return getContent(c, BlackList.class);
                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        }

        public static List<String> generateBlacklistList(Context context) {
            ArrayList<String> blacklistList = new ArrayList<String>();
            if (context == null) {
                return blacklistList;
            }
            String emailAddressOrDomain;
            Cursor cursor = context.getContentResolver().query(
                    BlackList.CONTENT_URI, BlackList.BLACKLIST_EMAIL_PROJECTION,
                    null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        emailAddressOrDomain = cursor.getString(0);
                        if (emailAddressOrDomain != null) {
                            blacklistList.add(emailAddressOrDomain);
                        }
                    } while (cursor.moveToNext());
                } else {
                    Log.d(TAG, " There are no blacklist address in table");
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception in generateBlacklist: ", e);
            } finally {
                if (cursor != null && !cursor.isClosed())
                    cursor.close();
            }
            return blacklistList;
        }

        @Override
        public ContentValues toContentValues() {
            ContentValues values = new ContentValues();
            values.put(BlackListColumns.USER_NAME, mBlackListName);
            values.put(BlackListColumns.EMAIL_ADDRESS, mBlackListEmailAddress);
            values.put(BlackListColumns.ACCOUNT_KEY, mBlackListAccountKey);
            values.put(BlackListColumns.LAST_ACCESSED_TIME_STAMP, mLastAccessedTimeStamp);
            values.put(BlackListColumns.IS_DOMAIN, mIsDomain);
            return values;
        }

        @Override
        public void restore(Cursor cursor) {
            mBaseUri = CONTENT_URI;
            mBlackListName = cursor.getString(CONTENT_BLACKLIST_NAME_COLUMN);
            mBlackListEmailAddress = cursor.getString(CONTENT_BLACKLIST_ADDRESS_COLUMN);
            mBlackListAccountKey = cursor.getLong(CONTENT_BLACKLIST_ACCOUNT_KEY_COLUMN);
            mLastAccessedTimeStamp = cursor.getLong(CONTENT_BLACKLIST_LAST_ACCESSED_TIME_STAMP);
            mIsDomain = cursor.getInt(CONTENT_BLACKLIST_IS_DOMAIN);
        }
    }
    
    
    public interface BlackListMessageColumns {
        public static final String MESSAGE_KEY = "messagekey";
        public static final String PROCESS_DIRTY = "processdirty";
    }

    public static final class BlackListMessage extends EmailContent implements
    BlackListMessageColumns {
        public static final String TABLE_NAME = "BlackListMessge";
        public static final Uri CONTENT_URI = Uri
                .parse(EmailContent.CONTENT_URI + "/blacklistmessage");
        
        public static final Uri CONTENT_URI_MULTI = Uri
                .parse(EmailContent.CONTENT_URI_MULTI + "/blacklistmessage");
        
        public static final int IS_PROCESS_DIRTY_YES = 1;
        public static final int IS_PROCESS_DIRTY_NO = 0;

        public static final int CONTENT_BLACKLISTMESSAGE_MESSAGE_KEY_COLUMN = 0;
        public static final int CONTENT_BLACKLISTMESSAGE_PROCESS_DIRTY_COLUMN = 1;

        public String mMessageKey;
        public int mProcessDirty;

        public static final String[] CONTENT_PROJECTION = new String[] {
                RECORD_ID, BlackListMessageColumns.MESSAGE_KEY,
                BlackListMessageColumns.PROCESS_DIRTY};

        /**
         * no public constructor since this is a utility class
         */
        protected BlackListMessage() {
        }

        @Override
        public ContentValues toContentValues() {
            ContentValues values = new ContentValues();
            values.put(BlackListMessageColumns.MESSAGE_KEY, mMessageKey);
            values.put(BlackListMessageColumns.PROCESS_DIRTY, mProcessDirty);
            return values;
        }

        @Override
        public void restore(Cursor cursor) {
            mMessageKey = cursor.getString(CONTENT_BLACKLISTMESSAGE_MESSAGE_KEY_COLUMN);
            mProcessDirty = cursor.getInt(CONTENT_BLACKLISTMESSAGE_PROCESS_DIRTY_COLUMN);
        }
    }
    // changes@siso.atul BlackList End
  // P5_Porting_Email_Block_End - worked by minsumr.kim
    

    public interface FavoriteContactColumns {


        public static final String ID = "_id";
        public static final String EMAIL_ADDRESS = "emailAddress";
        public static final String STATE = "state";
    }

    public static final class FavoriteContact extends EmailContent implements FavoriteContactColumns    {

        public String mEmailAddress;
        public int mState;

        private String mContactName;
        private byte[] mPhoto;

        public static final String TABLE_NAME = "FavoriteContact";
        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI + "/favoritecontact");

        public static final int CONTENT_FAVORITE_CONTACT_ID_COLUMN = 0;
        public static final int CONTENT_FAVORITE_CONTACT_ADDRESS_COLUMN = 1;
        public static final int CONTENT_FAVORITE_CONTACT_IS_REJECTED_COLUMN = 2;

        public static final int STATE_NEW = 0;
        public static final int STATE_DEFAULT= 1;
        public static final int STATE_REJECTED = 2;
       
        public FavoriteContact()    {
            mState = STATE_DEFAULT;
        }

        @Override
        public ContentValues toContentValues() {
            ContentValues values = new ContentValues();
            values.put(FavoriteContactColumns.EMAIL_ADDRESS, mEmailAddress);
            values.put(FavoriteContactColumns.STATE, mState);
            return values;
        }

        @Override
        public void restore(Cursor cursor) {
            // TODO Auto-generated method stub
            mId = cursor.getLong(CONTENT_FAVORITE_CONTACT_ID_COLUMN);
            mEmailAddress = cursor.getString(CONTENT_FAVORITE_CONTACT_ADDRESS_COLUMN);
            mState = cursor.getInt(CONTENT_FAVORITE_CONTACT_IS_REJECTED_COLUMN);
        }

        public void restoreFavoriteContact(Context context, Cursor cursor) {
            this.restore(cursor);

            int contactId = getContactIdAndContactNameFromEmailAddress(context, mEmailAddress);
            if (contactId < 0)
                return;

            final String[] PROJECTION = new String[] {
                    "display_name", // name
                    Photo.PHOTO // Thumnail photo byte
            };

            Uri uri = Uri.parse("content://com.android.contacts/contacts/"
                    + String.valueOf(contactId) + "/data");
            Cursor c = null;
            try {
                c = context.getContentResolver().query(uri, PROJECTION,
                        "photo_id" + "= " + Data._ID, null, null);
                
                if (c == null || c.getCount() < 1) {
                    return;
                }

                c.moveToFirst();
                //mContactName = c.getString(0);
                mPhoto = c.getBlob(1);

            } catch(IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(c != null) c.close();
                } catch(Exception e2) {}
            }
            
            return;

            /*
            Uri uri = Data.CONTENT_URI;
            Cursor c = context.getContentResolver().query(
                    uri,
                    new String[] {
                            Contacts.DISPLAY_NAME,
                            Photo.PHOTO
                    },
                    Email.DATA1 + "=? AND (" + Data.MIMETYPE + "=? OR " + Data.MIMETYPE + "=?)",
                    new String[] {mEmailAddress, Email.CONTENT_ITEM_TYPE}, null, null);
            */
        }

        public Bitmap getContactPhoto() {
            if(mPhoto != null)
                return BitmapFactory.decodeByteArray(mPhoto, 0, mPhoto.length, null);
            else
                return null;
        }
        
        public String getContactName() {
            return mContactName;
        }

        public String getEmailAddress() {
            return mEmailAddress;
        }

        public long getId()    {
            return mId;
        }

        public int getContactIdAndContactNameFromEmailAddress(Context context, String address) {

            if (context == null || address == null)
                return -1;

            Uri uri = Uri.parse("content://com.android.contacts/data/emails");

            int result = -1;
            Cursor c = null;

            final String[] PROJECTION = new String[] {
                    "_id", "contact_id", "display_name"
            };

            try {
                c = context.getContentResolver().query(
                        uri,
                        PROJECTION,
                        android.provider.ContactsContract.CommonDataKinds.Email.DATA
                                + "= ?", new String[] {
                            String.valueOf(address)
                        }, "creation_time desc");

                if( c == null || c.getCount() < 1 )
                    return -1;

                c.moveToFirst();
                result = c.getInt(1);
                mContactName = c.getString(2);
            } catch(SQLException e)    {
                // comment
            } finally    {
                if( c != null && !c.isClosed() )    {
                    c.close();c = null;
                }
            }

            return result;
        }
    }
    
    
    public interface TopStoriesColumns {
        public static final String TABLE_NAME = "TopStories";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_EMAIL_ADDRESS = "email_address";
        public static final String COLUMN_SUGGESTED = "suggested";
        public static final String COLUMN_LASTEST_MESSAGE_TIMESTAMP = "latest_message_timestamp"; 
    }
    
    public static class TopStories extends EmailContent implements TopStoriesColumns {
        
        public String mEmailAddress = null;
        
        public boolean mIsSuggested = false;
        
        public long mTimeStamp = 0;
        
        public static final Uri CONTENT_URI = Uri
                .parse(EmailContent.CONTENT_URI + "/" + TABLE_NAME);
        
        public static final int _ID = 0;
        
        public static final int EMAIL_ADDRESS = 1;
        
        public static final int SUGGESTED = 2;
        
        public static final int TIMESTAMP = 3;
        
        public static final String [] PROJECTION = {COLUMN_ID,  COLUMN_EMAIL_ADDRESS, 
            COLUMN_SUGGESTED, COLUMN_LASTEST_MESSAGE_TIMESTAMP};

        @Override
        public ContentValues toContentValues() {
            ContentValues cvs = new ContentValues();
            if (!TextUtils.isEmpty(mEmailAddress))
                cvs.put(COLUMN_EMAIL_ADDRESS, mEmailAddress);
            cvs.put(COLUMN_SUGGESTED, mIsSuggested ? 1 : 0);
            cvs.put(COLUMN_LASTEST_MESSAGE_TIMESTAMP, mTimeStamp);
            return cvs;
        }

        @Override
        public void restore(Cursor cursor) {
            mId = cursor.getLong(_ID);
            mEmailAddress = cursor.getString(EMAIL_ADDRESS);
            mIsSuggested = (cursor.getInt(SUGGESTED) == 1) ? true : false;
            mTimeStamp = cursor.getLong(TIMESTAMP);
        }
        
        public static void suggested (Context context, String emailAddress) {
            TopStories ts = restoreWithEmailAddress(context, emailAddress);
            if (ts == null) {
                ts = new TopStories();
                ts.mIsSuggested = true;
                ts.mEmailAddress = emailAddress;
                context.getContentResolver().insert(CONTENT_URI, ts.toContentValues());
            } else {
                ContentValues cvs = new ContentValues();
                cvs.put(COLUMN_SUGGESTED, 1);
                context.getContentResolver().update(ContentUris.withAppendedId(CONTENT_URI, ts.mId), cvs, null, null);
            }
        }
        
        public static boolean isSuggested (Context context, String emailAddress) {
            TopStories ts = restoreWithEmailAddress(context, emailAddress);
            if (ts == null)
                return false;
            return ts.mIsSuggested;
        }
        
        public static TopStories restoreWithEmailAddress(Context context, String emailAddress) {
            Cursor c = null;
            try {
                c = context.getContentResolver().query(CONTENT_URI, 
                        PROJECTION, COLUMN_EMAIL_ADDRESS + "=?", new String [] {emailAddress}, null);
                
                if(c != null) {
                    TopStories ts = new TopStories();
                    if (c.moveToNext()) {
                        ts.restore(c);
                        return ts;
                    }
                }
            } finally {
                if (c != null && !c.isClosed()) {
                    c.close();
                }
            }
            return  null;
        }
    }

    public static class MDMCertificates {
        public static final String TABLE_NAME = "MDMCertificates";
        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI + "/mdm_certificate");

        public static InputStream getCertificateData(Context context, String fileName) throws FileNotFoundException{
            InputStream in = null;
            Uri uri = null;

            if(fileName !=null){
            	uri = CONTENT_URI.buildUpon().appendPath("get").appendPath(fileName).build();
            }
            if(context!=null && uri!=null){
            	in =  context.getContentResolver().openInputStream(uri);
            }
            
            return in;
        }

        public static void deleteCertificate(Context context, String fileName){
            Uri uri = null;
            InputStream in = null;
            try {
                if(fileName !=null){
                    uri = CONTENT_URI.buildUpon().appendPath("delete").appendPath(fileName).build();
                }
                if(context!=null && uri!=null){
                    in = context.getContentResolver().openInputStream(uri);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    public interface NoteColumns {
        public static final String ID = "_id";

        // Basic columns used in note list presentation
        // Client Unique id for notes created on device
        public static final String UUID = "uUID";

        // source id (string) : the source's name of this item
        public static final String SERVER_ID = "syncServerId";

        // Note subject as shown to the user in a note list
        public static final String SUBJECT = "subject";

        // The time (millis) as shown to the user in a message list [INDEX]
        public static final String LAST_MODIFIED_DATE = "lastModifiedDate";

        // References to other Email objects in the database
        // Foreign key to the Mailbox holding this note [INDEX]
        public static final String MAILBOX_KEY = "mailboxKey";

        // Foreign key to the Account holding this note
        public static final String ACCOUNT_KEY = "accountKey";

        // Boolean, unread = 0, read = 1 [INDEX]
        public static final String FLAG_READ = "flagRead";

        // categories applied to the note
        public static final String CATEGORIES = "categories";

        // color applied to the note
        public static final String COLOR = "color";

        // Message class of the note
        public static final String MESSAGECLASS = "messageClass";

        // whether the note is truncated or not
        public static final String ISTRUNCATED = "isTruncated";

        // Body type of the note
        public static final String BODY_TYPE = "bodyType";

        // Body of the note
        public static final String BODY = "body";

        // Size of the note
        public static final String SIZE = "size";

        //whether the note is deleted or not
        public static final String IS_DELETED = "isDeleted";

        //whether the note is changed or not
        public static final String IS_DIRTY = "isDirty";
    }

    public static final class Note extends EmailContent implements NoteColumns {
        public static final String TABLE_NAME = "Notes";

        public static final String AUTHORITY = "note";
        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI + "/note");
        public static final Uri CONTENT_URI_MULTI = Uri.parse(EmailContent.CONTENT_URI_MULTI + "/note");
        public static final Uri CONTENT_URI_LIMIT_1 = uriWithLimit(CONTENT_URI, 1);
        public static final Uri CONTENT_URI_LIMIT_2 = uriWithLimit(CONTENT_URI, 2);

        public static final int CONTENT_ID_COLUMN = 0;
        public static final int CONTENT_UUID_COLUMN = 1;
        public static final int CONTENT_SERVER_ID_COLUMN = 2;
        public static final int CONTENT_SUBJECT_COLUMN = 3;
        public static final int CONTENT_LAST_MODIFIED_DATE_COLUMN = 4;
        public static final int CONTENT_MAILBOX_KEY_COLUMN = 5;
        public static final int CONTENT_ACCOUNT_KEY_COLUMN = 6;
        public static final int CONTENT_FLAG_READ_COLUMN = 7;
        public static final int CONTENT_CATEGORIES_COLUMN = 8;
        public static final int CONTENT_COLOR_COLUMN = 9;
        public static final int CONTENT_MESSAGECLASS_COLUMN = 10;
        public static final int CONTENT_ISTRUNCATED_COLUMN = 11;
        public static final int CONTENT_BODY_TYPE_COLUMN = 12;
        public static final int CONTENT_BODY_COLUMN = 13;
        public static final int CONTENT_SIZE_COLUMN = 14;
        public static final int CONTENT_IS_DELETED_COLUMN = 15;
        public static final int CONTENT_IS_DIRTY_COLUMN = 16;
        public static final int CONTENT_LAST_ORDINARY_INDEX = CONTENT_IS_DIRTY_COLUMN;

        public static final String[] CONTENT_PROJECTION = new String[] {
            RECORD_ID,
            NoteColumns.UUID,
            NoteColumns.SERVER_ID,
            NoteColumns.SUBJECT,
            NoteColumns.LAST_MODIFIED_DATE,
            NoteColumns.MAILBOX_KEY,
            NoteColumns.ACCOUNT_KEY,
            NoteColumns.FLAG_READ,
            NoteColumns.CATEGORIES,
            NoteColumns.COLOR,
            NoteColumns.MESSAGECLASS,
            NoteColumns.ISTRUNCATED, //10
            NoteColumns.BODY_TYPE,
            NoteColumns.BODY,
            NoteColumns.SIZE,
            NoteColumns.IS_DELETED,
            NoteColumns.IS_DIRTY
        };

        // _id field is in AbstractContent
        public String muUID;
        public String mSyncServerId;
        public String mSubject;
        public long mLastModifiedTime;
        public long mMailboxKey;
        public long mAccountKey;
        public boolean mFlagRead = false;
        public String mCategories ;
        public int mColor = -1;
        public String mMessageClass;
        public boolean mIsTruncated = false;
        public int mBodyType;
        public String mBody;
        public int mSize;
        public boolean mIsDeleted = false;
        public boolean mIsDirty = false;

        // Values used in mFlagRead
        public static final int UNREAD = 0;
        public static final int READ = 1;

        public Note() {
            mBaseUri = CONTENT_URI;
        }

        public void delete(Context context, Uri uri) {
            EmailContent.delete(context, uri, mId);
        }

        @Override
        public ContentValues toContentValues() {
            ContentValues values = new ContentValues();

            // Assign values for each row.
            values.put(NoteColumns.UUID, muUID);
            values.put(NoteColumns.SERVER_ID, mSyncServerId);
            values.put(NoteColumns.SUBJECT, mSubject);
            values.put(NoteColumns.LAST_MODIFIED_DATE, mLastModifiedTime);
            values.put(NoteColumns.MAILBOX_KEY, mMailboxKey);
            values.put(NoteColumns.ACCOUNT_KEY, mAccountKey);
            values.put(NoteColumns.FLAG_READ, mFlagRead);
            values.put(NoteColumns.CATEGORIES, mCategories);
            values.put(NoteColumns.COLOR, mColor);
            values.put(NoteColumns.MESSAGECLASS, mMessageClass);
            values.put(NoteColumns.ISTRUNCATED, mIsTruncated);
            values.put(NoteColumns.BODY_TYPE, mBodyType);
            values.put(NoteColumns.BODY, mBody);
            values.put(NoteColumns.SIZE, mSize);
            values.put(NoteColumns.IS_DELETED, mIsDeleted);
            values.put(NoteColumns.IS_DIRTY, mIsDirty);
            return values;
        }

        public static Note restoreNoteWithId(Context context, long id) {
            Uri uri = ContentUris.withAppendedId(Note.CONTENT_URI, id);
            if (context == null) {
                return null;
            }
            ContentResolver resolver = context.getContentResolver();
            if (resolver == null) {
                return null;
            }
            Cursor c = null;
            try {
                c = resolver.query(uri, Note.CONTENT_PROJECTION, null, null, null);

                if (c!= null && c.moveToFirst()) {
                    Note note = getContent(c, Note.class);
                    return note;
                } else {
                    return null;
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in restoreNoteWithId");
                e.printStackTrace();
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        }

        public static long[] getNoteIdsWhere(Context context, String selection,
                String[] selectionArgs) {
            long[] ids = null;
            Cursor c = null;
            try {
                c = context.getContentResolver().query(Note.CONTENT_URI,
                        new String[]{RECORD_ID}, selection, selectionArgs, null);

                if (c != null) {
                    int count = c.getCount();
                    ids = new long[count];
                    for (int i = 0; i < count; ++i) {
                        c.moveToNext();
                        ids[i] = c.getLong(CONTENT_ID_COLUMN);
                    }
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in getNoteIdsWhere", e);
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
            return ids;
        }

        public static Note[] restoreNotesAll(Context context) {
            Cursor c = null;
            try {
                c = context.getContentResolver().query(Note.CONTENT_URI,
                        Note.CONTENT_PROJECTION, null, null, null);
                if(c != null) {
                    int count = c.getCount();
                    Note[] notes = new Note[count];
                    for (int i = 0; i < count; ++i) {
                        c.moveToNext();
                        notes[i] = getContent(c, Note.class);
                    }
                    return notes;
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in restoreMessageAll", e);
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
            return null;
        }

        @Override
        public void restore(Cursor cursor) {
            mBaseUri = CONTENT_URI;
            mId = cursor.getLong(CONTENT_ID_COLUMN);
            muUID = cursor.getString(CONTENT_UUID_COLUMN);
            mSyncServerId = cursor.getString(CONTENT_SERVER_ID_COLUMN);
            mSubject = cursor.getString(CONTENT_SUBJECT_COLUMN);
            mLastModifiedTime = cursor.getLong(CONTENT_LAST_MODIFIED_DATE_COLUMN) ;
            mMailboxKey = cursor.getInt(CONTENT_MAILBOX_KEY_COLUMN);
            mAccountKey = cursor.getInt(CONTENT_ACCOUNT_KEY_COLUMN) ;
            mFlagRead = cursor.getInt(CONTENT_FLAG_READ_COLUMN) == 1;
            mCategories = cursor.getString(CONTENT_CATEGORIES_COLUMN);
            mColor = cursor.getInt(CONTENT_COLOR_COLUMN);
            mMessageClass = cursor.getString(CONTENT_MESSAGECLASS_COLUMN);
            mIsTruncated = cursor.getLong(CONTENT_ISTRUNCATED_COLUMN) == 1;
            mBodyType = cursor.getInt(CONTENT_BODY_TYPE_COLUMN);
            mBody = cursor.getString(CONTENT_BODY_COLUMN);
            mSize = cursor.getInt(CONTENT_SIZE_COLUMN);
            mIsDeleted = cursor.getInt(CONTENT_IS_DELETED_COLUMN) == 1;
            mIsDirty = cursor.getInt(CONTENT_IS_DIRTY_COLUMN) == 1;
        }

        public boolean update() {
            return false;
        }

        @Override
        public Uri save(Context context) {

            boolean doSave = !isSaved();

                if (doSave) {
                    return super.save(context);
                } else {
                    // Call update, rather than super.update in case we ever
                    // override it
                    if (update(context, toContentValues()) == 1) {
                        return getUri();
                    }
                    return null;
                }
        }

        public static long getAccountId(Context context, long noteId) {
            Cursor c = null;
            if ( noteId < 0 ) {
                return -1;
            }
            try {
                c = context.getContentResolver().query(EmailContent.Note.CONTENT_URI,
                        NOTEID_TO_ACCOUNTID_PROJECTION, EmailContent.RECORD_ID + "=?",
                        new String[] {
                            Long.toString(noteId)
                        }, null);
                return (c != null && c.moveToFirst()) ? c.getLong(NOTEID_TO_ACCOUNTID_COLUMN_ACCOUNTID) : -1;
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in getAccountId", e);
                return -1;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        }

        public static int getCount(Context context, String selection, String [] selectionArgs) {
            int count = 0;
            Cursor c = null;
            try {
                c = context.getContentResolver().query(CONTENT_URI,
                        new String[] {"COUNT(*)"}, selection, selectionArgs, null);
                if(c != null && c.moveToFirst()){
                    count = c.getInt(0);
                }
            }catch (Exception e) {
                EmailLog.e(TAG, "Exception in getCount", e);
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
            return count;
        }

        public static Note restoreNoteWhere(Context context, String selection,
                String[] selectionArgs, String sort) {
             if (context == null) {
                 return null;
             }
             ContentResolver resolver = context.getContentResolver();
             if (resolver == null) {
                 return null;
             }
             Cursor c = null;
             try {
                 c = resolver.query(Note.CONTENT_URI, Note.CONTENT_PROJECTION, selection,
                         selectionArgs, sort);
                 if (c !=null && c.moveToFirst()) {
                     return getContent(c, Note.class);
                 } else {
                     return null;
                 }
             } catch (Exception e) {
                 EmailLog.e(TAG, "Exception in restoreNoteWhere", e);
                 return null;
             } finally {
                 if (c != null && !c.isClosed())
                     c.close();
             }
        }

        @Override
        public String toString() {
            return "mId:" + mId + " ,muUID:" + muUID + " ,mSyncServerId:" + mSyncServerId + " ,mSubject:" + mSubject + "\n"
                    + ",mFlagRead:" + mFlagRead + " ,mLastModifiedTime:" + mLastModifiedTime + " ,mCategories:" + mCategories + " ,mColor:" + mColor +"\n"
                    + ",mMailboxKey:" + mMailboxKey + " ,mAccountKey:" + mAccountKey + "\n"
                    + ",mBodyType: " + mBodyType + " ,mMessageClass:" + mMessageClass;
        }
    }

    public interface CategoryColumns {
        public static final String ID = "_id";

        // Basic columns used in category list presentation
        // Category name
        public static final String CATEGORY_NAME = "categoryName";

        // Foreign key to the Account holding this note
        public static final String ACCOUNT_ID = "accountId";
    }

    public static final class Category extends EmailContent implements CategoryColumns {
        public static final String TABLE_NAME = "NotesCategory";

        public static final Uri CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI + "/notescategory");
        public static final Uri CONTENT_URI_MULTI = Uri.parse(EmailContent.CONTENT_URI_MULTI + "/notescategory");

        public static final Uri CONTENT_URI_LIMIT_1 = uriWithLimit(CONTENT_URI, 1);
        public static final Uri CONTENT_URI_LIMIT_2 = uriWithLimit(CONTENT_URI, 2);

        public static final int CONTENT_ID_COLUMN = 0;
        public static final int CONTENT_CATEGORY_NAME_COLUMN = 1;
        public static final int CONTENT_ACCOUNT_ID_COLUMN = 2;

        public static final int CONTENT_LAST_ORDINARY_INDEX = CONTENT_ACCOUNT_ID_COLUMN;
        public static final String[] CONTENT_PROJECTION = new String[] {
            RECORD_ID,
            CategoryColumns.CATEGORY_NAME,
            CategoryColumns.ACCOUNT_ID
        };

        // _id field is in AbstractContent
        public String mCategoryName;
        public long mAccountId;
        public Category() {
            mBaseUri = CONTENT_URI;
        }

        public void delete(Context context, Uri uri) {
            EmailContent.delete(context, uri, mId);
        }

        @Override
        public ContentValues toContentValues() {
            ContentValues values = new ContentValues();
            values.put(CategoryColumns.CATEGORY_NAME, mCategoryName);
            values.put(CategoryColumns.ACCOUNT_ID, mAccountId);
            return values;
        }

        public static Category restoreCategoryWithId(Context context, long id) {
            Uri uri = ContentUris.withAppendedId(Category.CONTENT_URI, id);
            if (context == null) {
                return null;
            }
            ContentResolver resolver = context.getContentResolver();
            if (resolver == null) {
                return null;
            }
            Cursor c = null;
            try {
                c = resolver.query(uri, Category.CONTENT_PROJECTION, null, null, null);

                if (c !=null && c.moveToFirst()) {
                    Category category = getContent(c, Category.class);
                    return category;
                } else {
                    return null;
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in restoreCategoryWithId",e);
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        }

        public static long[] getCategoryIdsWhere(Context context, String selection,
                String[] selectionArgs) {
            long[] ids = null;
            Cursor c = null;
            try {
                c = context.getContentResolver().query(Category.CONTENT_URI,
                        new String[]{RECORD_ID}, selection, selectionArgs, null);

                if (c != null) {
                    int count = c.getCount();
                    ids = new long[count];
                    for (int i = 0; i < count; ++i) {
                        c.moveToNext();
                        ids[i] = c.getLong(CONTENT_ID_COLUMN);
                    }
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in getCategoryIdsWhere", e);
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
            return ids;
        }

        public static String[] getCategoryNameWhere(Context context, String selectionArgs) {
            String[] NamesCategory = null;
            Cursor c = null;
            String selection = Category.ID + " in (" + selectionArgs +")" ;

            try {
                c = context.getContentResolver().query(Category.CONTENT_URI,
                        new String[]{ CATEGORY_NAME }, selection, null, null);

                if (c != null) {
                    int count = c.getCount();
                    NamesCategory = new String[count];
                    for (int i = 0; i < count; ++i) {
                        c.moveToNext();
                        NamesCategory[i] = c.getString(0);
                    }
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in getCategoryNameWhere", e);
                return null;
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }

            return NamesCategory;
        }

        public static Category[] restoreCategoryAll(Context context) {
            Cursor c = null;
            try {
                c = context.getContentResolver().query(Category.CONTENT_URI,
                        Category.CONTENT_PROJECTION, null, null, null);
                if(c != null) {
                    int count = c.getCount();
                    Category[] category = new Category[count];
                    for (int i = 0; i < count; ++i) {
                        c.moveToNext();
                        category[i] = getContent(c, Category.class);
                    }
                    return category;
                }
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in restoreCategoryAll", e);
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
            return null;
        }

        @Override
        public void restore(Cursor cursor) {
            mBaseUri = CONTENT_URI;
            mId = cursor.getLong(CONTENT_ID_COLUMN);
            mCategoryName = cursor.getString(CONTENT_CATEGORY_NAME_COLUMN);
            mAccountId = cursor.getInt(CONTENT_ACCOUNT_ID_COLUMN);
        }

        public boolean update() {
            return false;
        }

        @Override
        public Uri save(Context context) {
            boolean doSave = !isSaved();
                if (doSave) {
                    return super.save(context);
                } else {
                    if (update(context, toContentValues()) == 1) {
                        return getUri();
                    }
                    return null;
                }
        }

        public static int getCount(Context context, String selection, String [] selectionArgs) {
            int count = 0;
            Cursor c = null;
            try {
                c = context.getContentResolver().query(CONTENT_URI,
                        new String[] {"COUNT(*)"}, selection, selectionArgs, null);
                if(c!=null && c.moveToFirst()) {
                    count = c.getInt(0);
                }
            }catch (Exception e) {
                EmailLog.e(TAG, "Exception in getCount", e);
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
            return count;
        }

        @Override
        public String toString() {
            return "mId:" + mId + " ,mCategoryName:" + mCategoryName + " ,mAccountId:" + mAccountId + "\n";
        }
    }
    
    public interface SMIMECertificateColumns {
        public static final String ID = "_id";

        // Basic columns used in category list presentation
        // Category name
        public static final String CERTIFICATE_ALIAS = "aliasName";

        // Foreign key to the Account holding this note
        public static final String ACCOUNT_ID = "accountId";
    }
    
    public static final class SMIMECertificate extends EmailContent implements
    SMIMECertificateColumns {
        public static final String TABLE_NAME = "SMIMECertificates";
        public static final Uri CONTENT_URI = Uri
                .parse(EmailContent.CONTENT_URI + "/smimecertificate/");

        public static final int CONTENT_ACCOUNT_ID_COLUMN = 0;
        public static final int CONTENT_CERTIFICATE_ALIAS_COLUMN = 1;

        public String mAliasName;
        public long mAccountKey;

        public static final String[] CONTENT_PROJECTION = new String[] {
                RECORD_ID, SMIMECertificateColumns.ACCOUNT_ID,
                SMIMECertificateColumns.CERTIFICATE_ALIAS};

        /**
         * no public constructor since this is a utility class
         */
        protected SMIMECertificate() {
        }

        @Override
        public ContentValues toContentValues() {
            ContentValues values = new ContentValues();
            values.put(SMIMECertificateColumns.ACCOUNT_ID, mAccountKey);
            values.put(SMIMECertificateColumns.CERTIFICATE_ALIAS, mAliasName);
            return values;
        }

        @Override
        public void restore(Cursor cursor) {
            mAccountKey = cursor.getLong(CONTENT_ACCOUNT_ID_COLUMN);
            mAliasName = cursor.getString(CONTENT_CERTIFICATE_ALIAS_COLUMN);
        }
    }
}

