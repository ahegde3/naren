package com.samsung.android.emailcommon.utility;


import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.CalendarContract.Events;
import android.text.TextUtils;

import com.samsung.android.emailcommon.AccountCache;
import com.samsung.android.emailcommon.AccountManagerTypes;
import com.samsung.android.emailcommon.BadgeManager;
import com.samsung.android.emailcommon.IntentConst;
import com.samsung.android.emailcommon.MessageReminderUtil;
import com.samsung.android.emailcommon.MessageReminderUtil.ReminderColumns;
import com.samsung.android.emailcommon.NotificationUtil;
import com.samsung.android.emailcommon.provider.EmailContent;
import com.samsung.android.emailcommon.provider.EmailContent.Account;
import com.samsung.android.emailcommon.provider.EmailContent.Attachment;
import com.samsung.android.emailcommon.provider.EmailContent.Body;
import com.samsung.android.emailcommon.provider.EmailContent.Mailbox;
import com.samsung.android.emailcommon.provider.EmailContent.MailboxColumns;
import com.samsung.android.emailcommon.provider.EmailContent.Message;
import com.samsung.android.emailcommon.provider.EmailContent.MessageColumns;
import com.samsung.android.emailcommon.system.CarrierValues;
import com.samsung.android.emailcommon.variant.CommonDefs;
import com.samsung.android.emailcommon.EmailFeature;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class SyncHelperCommon {

    public static final int BATCH_OPERATION_COUNT = 1000;
    public interface ISyncHelperCallbackInterface {
        void startOperation();
        void endOperation();
    }

    static private String TAG = "SyncHelperCommon";
    private Context mContext = null;
    static protected SyncHelperCommon _inst = null;
    
    protected SyncHelperCommon (Context context) {
        mContext = context;
    }

    static public SyncHelperCommon createInstance(Context context) {
        if (_inst == null) {
            SyncHelperCommon sh = new SyncHelperCommon(context.getApplicationContext());
            _inst = sh;
        }
        return _inst;
    }
    
    
    
    public AsyncTask<Void, Void, Void> setMessageRead(final long[] messageIds,
            final boolean isRead) {
        return setMessageRead(messageIds, isRead, null);
    }

    public AsyncTask<Void, Void, Void> setMessageRead(final long[] messageIds,
            final boolean isRead, ISyncHelperCallbackInterface callback) {
        return setMessageBoolean(messageIds, EmailContent.MessageColumns.FLAG_READ, isRead, callback);
    }
    
    /**
     * Set/clear the favorite status of a message
     * 
     * @param messageId the message to update
     * @param isFavorite the new value for the isFavorite flag
     * @return the AsyncTask that will execute the changes (for testing only)
     */
    public AsyncTask<Void, Void, Void> setMessageFavorite(final long[] messageIds,
            final boolean isFavorite) {
        return setMessageFavorite(messageIds, isFavorite, null);
    }

    public AsyncTask<Void, Void, Void> setMessageFavorite(final long[] messageIds,
            final boolean isFavorite, ISyncHelperCallbackInterface callback) {
        return SyncHelperCommon.createInstance(mContext).setMessageBoolean(messageIds,
                EmailContent.MessageColumns.FLAG_FAVORITE, isFavorite, callback);
    }
    
    /**
     * Set/clear boolean columns of a message
     *
     * @param messageId the message to update
     * @param columnName the column to update
     * @param columnValue the new value for the column
     * @return the AsyncTask that will execute the changes (for testing only)
     */
    public AsyncTask<Void, Void, Void> setMessageBoolean(final long[] messageIds,
            final String columnName, final boolean columnValue) {
        return setMessageBoolean(messageIds, columnName, columnValue, null);
    }

    public AsyncTask<Void, Void, Void> setMessageBoolean(final long[] messageIds,
            final String columnName, final boolean columnValue,
            final ISyncHelperCallbackInterface callback) {
        if (callback != null) {
            callback.startOperation();
        }
        return Utility.runAsync(new Runnable() {
            public void run() {
                ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
                ContentValues cv = new ContentValues();
                cv.put(columnName, columnValue);
                StringBuilder messageIdString = new StringBuilder();
                int count = 0;
                for (long id : messageIds) {
                    if (messageIdString.length() > 0) {
                        messageIdString.append(",");
                    }
                    count++;
                    messageIdString.append(id);
                    if (count > BATCH_OPERATION_COUNT) {
                        String selection = Message.RECORD_ID + " IN (" + messageIdString + ")";
                        ContentProviderOperation.Builder b = ContentProviderOperation
                                .newUpdate(EmailContent.Message.SYNCED_CONTENT_URI);
                        ops.add(b.withSelection(selection, null).withValues(cv).build());
                        count = 0;
                        messageIdString.setLength(0);
                    }
                }
                if (messageIdString.length() > 0) {
                    String selection = Message.RECORD_ID + " IN (" + messageIdString + ")";
                    ContentProviderOperation.Builder b = ContentProviderOperation
                            .newUpdate(EmailContent.Message.SYNCED_CONTENT_URI);
                    ops.add(b.withSelection(selection, null).withValues(cv).build());
                }

                try {
                    mContext.getContentResolver().applyBatch(EmailContent.AUTHORITY, ops);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (OperationApplicationException e) {
                    e.printStackTrace();
                }

                if (columnName.equals(EmailContent.MessageColumns.FLAG_READ) == true) {
                    BadgeManager.updateBadgeProvider(mContext);
                }
                if (callback != null) {
                    callback.endOperation();
                }
            }
        });
    }
    
    /**
     * Set/clear the favorite status of a message
     * 
     * @param messageId the message to update
     * @param isFavorite the new value for the isFavorite flag
     * @return the AsyncTask that will execute the changes (for testing only)
     */
    public AsyncTask<Void, Void, Void> setMessageFollowUpFlag(final long[] messageIds,
            final int newFollowUpFlag) {
    	return setMessageInt(messageIds, EmailContent.MessageColumns.FLAGSTATUS, newFollowUpFlag,
                null);
    }

    private AsyncTask<Void, Void, Void> setMessageInt(final long[] messageIds, final String columnName,
            final int columnValue,
            final ISyncHelperCallbackInterface callback) {
        if (messageIds == null || messageIds.length == 0) {
            return null;
        }
        if (callback != null) {
            callback.startOperation();
        }
        return Utility.runAsync(new Runnable() {
            public void run() {
                ContentValues cv = new ContentValues();
                cv.put(columnName, columnValue);
                ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
                StringBuilder messageIdString = new StringBuilder();
                int count = 0;
                for (long id : messageIds) {
                    if (messageIdString.length() > 0) {
                        messageIdString.append(",");
                    }
                    count++;
                    messageIdString.append(id);
                    if (count > SyncHelperCommon.BATCH_OPERATION_COUNT) {
                        String selection = Message.RECORD_ID + " IN (" + messageIdString + ")";
                        ContentProviderOperation.Builder b = ContentProviderOperation
                                .newUpdate(EmailContent.Message.SYNCED_CONTENT_URI);
                        ops.add(b.withSelection(selection, null).withValues(cv).build());
                        count = 0;
                        messageIdString.setLength(0);
                    }
                }
                if (messageIdString.length() > 0) {
                    String selection = Message.RECORD_ID + " IN (" + messageIdString + ")";
                    ContentProviderOperation.Builder b = ContentProviderOperation
                            .newUpdate(EmailContent.Message.SYNCED_CONTENT_URI);
                    ops.add(b.withSelection(selection, null).withValues(cv).build());
                }
                try {
                    mContext.getContentResolver().applyBatch(EmailContent.AUTHORITY, ops);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (OperationApplicationException e) {
                    e.printStackTrace();
                }
                if (callback != null) {
                    callback.endOperation();
                }
            }

        });
    }


    public void deleteMessage(final long[] messageIds, final Runnable callback) {
        deleteMessage(messageIds, callback, null, false);
    }

    public void deleteMessage(final long[] messageIds, final Runnable callback, final ISyncHelperCallbackInterface cb, final boolean isSearchOpen) {
        Log.d(TAG, "deleteMessage() is called : ");
        if (messageIds == null || messageIds.length == 0)
            return;
        if(cb != null){
            cb.startOperation();
        }
        Utility.runAsync(new Runnable() {
            public void run() {
            	StringBuilder in = new StringBuilder();
            	Cursor c = null;
            	for (long id : messageIds) {
            		if (in.length() > 0) {
            			in.append(",");
            		}
            		in.append(id);
            	}
            	
            	HashSet<Long> outBoxes = new HashSet<Long> ();
            	
            	try {
            		c = mContext.getContentResolver().query(Mailbox.CONTENT_URI, new String[] {MailboxColumns.ID}, 
            				MailboxColumns.TYPE + " in (" + Mailbox.TYPE_OUTBOX + "," + Mailbox.TYPE_SCHEDULED_OUTBOX + ")", null, null);
            		while (c != null && c.moveToNext()) {
            			outBoxes.add(c.getLong(0));
            		}
            	} finally {
            		if (c != null)
            			c.close();
            	}
            	String inSel = MessageColumns.ID + " in (" + in.toString() + ")";
            	String remiderSelection = ReminderColumns.MESSAGE_ID + " in (" + in.toString() + ")";
            	HashSet<Long> accountSet = new HashSet<Long>();
            	MessageReminderUtil.unsetReminderWhere(mContext, remiderSelection);
            	try {
            		c = mContext.getContentResolver().query(Message.CONTENT_URI, 
            				new String[] {MessageColumns.ID, MessageColumns.MAILBOX_KEY, MessageColumns.FLAGS, 
								MessageColumns.ACCOUNT_KEY, MessageColumns.MEETING_INFO, MessageColumns.MAILBOX_TYPE}, inSel, null, null);
            		while (c != null && c.moveToNext()) {
            			long messageId = c.getLong(0);
            			long mailboxKey = c.getLong(1);
            			long accountKey = c.getLong(3);
						int mailboxType = c.getInt(5);

            			accountSet.add(accountKey);
            			if (outBoxes.contains(mailboxKey)) {
            				NotificationUtil.deleteSendFailNotiFromDB(mContext, accountKey, messageId);
            			}
            			int flags = c.getInt(2);
            			String meetingInfo = c.getString(4);
            			if (((flags & Message.FLAG_INCOMING_MEETING_CANCEL) != 0 || 
            					(flags & Message.FLAG_INCOMING_MEETING_INVITE) != 0) && !CarrierValues.IS_CARRIER_WHOLE_SPRINT 
							    && mailboxType == Mailbox.TYPE_INBOX) {
								long calendarEventId = Utility.getCalendarEventId(mContext , meetingInfo, accountKey);
								if(calendarEventId != -1){
									mContext.getContentResolver().delete(ContentUris
										.withAppendedId(Events.CONTENT_URI,calendarEventId),
										null,null);
								}
							}
						}
            	} finally {
            		if (c != null)
            			c.close();
            	}
                if (accountSet.size() == 1) {
                    deleteMessageSync(mContext, accountSet.iterator().next(), messageIds, isSearchOpen);
                } else {
                    deleteMessageSync(mContext, messageIds, isSearchOpen);
                }

                if (callback != null) {
                    callback.run();
                }
                if(cb != null){
                    cb.endOperation();
            }
            }

        });
    }
    
    /**
     * Synchronous version of {@link #deleteMessage} for tests.
     */


    private void deleteMessageSync(final Context context, long[] messageIds, boolean isSearchOpen) {
        // prevent index bound of exception
        if (messageIds == null || messageIds.length == 0) {
            return;
        }
        HashMap<Long, ArrayList<Long>> accountMessageMap = new HashMap<Long, ArrayList<Long>>();

        Cursor c = null;
        try {
            StringBuilder inSel = new StringBuilder();
            for (long messageId : messageIds) {
                if (inSel.length() > 0) {
                    inSel.append(",");
                }
                inSel.append(messageId);
            }
            inSel.insert(0, " IN (");
            inSel.insert(0, MessageColumns.ID);
            inSel.append(")");
            c = context.getContentResolver().query(Message.CONTENT_URI, new String[]{MessageColumns.ID, MessageColumns.ACCOUNT_KEY},
                    inSel.toString(), null, null);

            if (c != null) {
                while (c.moveToNext()) {
                    long acc = c.getLong(1);
                    long msg = c.getLong(0);
                    if (accountMessageMap.containsKey(acc)) {
                        accountMessageMap.get(acc).add(msg);
                    } else {
                        ArrayList<Long> list = new ArrayList<Long>();
                        list.add(msg);
                        accountMessageMap.put(acc, list);
                    }
                }
            }
        } finally {
            if (c != null)
                c.close();
        }
        for (Entry<Long, ArrayList<Long>> e : accountMessageMap.entrySet()) {
            long accountId = e.getKey();
            long[] messageIds2 = Utility.toPrimitiveLongArray(e.getValue());
            deleteMessageSync(context, accountId, messageIds2, isSearchOpen);
        }
    }
    
    private void deleteMessageSync(final Context context, long accountId, long[] messageIds, boolean isSearchOpen) {
    	if(isSearchOpen) {
    		deleteMessageSyncInSearch(context, accountId, messageIds);
    	} else {
    		deleteMessageSync(context, accountId, messageIds);
    	}
    }
    private void deleteMessageSync(final Context context, long accountId, long[] messageIds) {
        
        // 1. Get the message's account
        Account account = Account.restoreAccountWithId(context, accountId);

        if (account == null)
            return;

        Log.d(TAG, "deleteMessageSync() is called : " + account.mId + "," + messageIds[0]);

        // 2. Confirm that there is a trash mailbox available. If not, create
        // one

        long trashMailboxId = findOrCreateMailboxOfType(mContext, account.mId, Mailbox.TYPE_TRASH);
        // 3. Get the message's original mailbox
        Mailbox mailbox = Mailbox.getMailboxForMessageId(context, messageIds[0]);

        if (mailbox == null)
            return;

        ContentResolver resolver = context.getContentResolver();
        // + msc, hanklee, 2010.07.26, for seven
        // msc, hanklee, 2010.07.26, for seven -
        // huijae.lee

        if ((mailbox.mId == trashMailboxId) || (mailbox.mType == Mailbox.TYPE_DRAFTS && !isServerDraftsFolder(mailbox))
                || (mailbox.mType == Mailbox.TYPE_OUTBOX) || (mailbox.mType == Mailbox.TYPE_SCHEDULED_OUTBOX)) {
			
			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
			try{
            for (long messageId : messageIds) {
                Uri deleteUri = null;
                if (AccountCache.isExchange(context, account.mId)
                        && mailbox.mType == Mailbox.TYPE_DRAFTS) {
                    deleteUri = EmailContent.Message.CONTENT_URI;
                } else {
                    deleteUri = EmailContent.Message.SYNCED_CONTENT_URI;
                }
                // 4. Drop non-essential data for the message (e.g.
                // attachment
                // files)
                AttachmentUtilities.deleteAllAttachmentFiles(context, account.mId,
                        messageId);

                // delete attachment temp image files
                Attachment[] atts = Attachment.restoreAttachmentsWithMessageId(context, messageId);
                for (Attachment att : atts) {

                    if (att.mContentUri == null)
                        continue;

                    Uri uri = Uri.parse(att.mContentUri);

                    if (uri != null && "file".equals(uri.getScheme())) {
                        String filePath = uri.getPath();

                        if (filePath != null && filePath.contains(CommonDefs.tempDirectory)
                                && !(filePath.contains(CommonDefs.SAVE_SIGNATURE_TMPSAMMFILE_PATH))
                                && !(filePath.contains("/TempSignature/"))) { //add code temporary
                            File delFile = new File(filePath);

                            if (delFile != null ) delFile.delete();
                        }
                    }
                }

//              if (mailbox.mType == Mailbox.TYPE_OUTBOX) {
//                  Preferences.getPreferences(mContext).removeFailNotiPref(account.mId, messageId);
//              }
                // 5. Perform "delete" as appropriate
                // Delete message body files
                BodyUtilites.deleteAllMessageBodyFilesUri(context, account.mId, messageId);

					Uri uri = ContentUris.withAppendedId(deleteUri, messageId);
					ContentProviderOperation cpo = ContentProviderOperation.newDelete(uri).build();
					ops.add(cpo);

                // 5a. Really delete it
					//if(resolver != null) resolver.delete(uri, null, null);
            }
				resolver.applyBatch(EmailContent.AUTHORITY, ops);
			}catch(Exception e){
				e.printStackTrace();
			}
        } else {
            moveMessageToSameAccount(messageIds, account, trashMailboxId,
                    mailbox.mId, true);
        }


        // + msc, hanklee, 2010.07.26, for seven

        // msc, hanklee, 2010.07.26, for seven -

    }
    
    private void deleteMessageSyncInSearch(final Context context, long accountId, long[] messageIds) {
        
        // 1. Get the message's account
        Account account = Account.restoreAccountWithId(context, accountId);

        if (account == null)
            return;

        Log.d(TAG, "deleteMessageSync() is called : " + account.mId + "," + messageIds[0]);

        // 2. Confirm that there is a trash mailbox available. If not, create
        // one

        long trashMailboxId = findOrCreateMailboxOfType(mContext, account.mId, Mailbox.TYPE_TRASH);
        long prevMailboxId = -1;
        
//        long[] moveMessageIds = new long[messageIds.length];
        ArrayList<Long> moveMessageIdList = new ArrayList<Long> ();
//        int sizeOfMoveMessageIds = 0;
        // 3. Get the message's original mailbox
        ContentResolver resolver = context.getContentResolver();
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        try{
        	for(int i = 0 ; i < messageIds.length ; i++) {
        		Mailbox mailbox = Mailbox.getMailboxForMessageId(context, messageIds[i]);
        		if (mailbox == null) {
        			continue;
        		} else {
        			if ((mailbox.mId == trashMailboxId) || (mailbox.mType == Mailbox.TYPE_DRAFTS && !isServerDraftsFolder(mailbox))
        					|| (mailbox.mType == Mailbox.TYPE_OUTBOX) || (mailbox.mType == Mailbox.TYPE_SCHEDULED_OUTBOX)) {

        				Uri deleteUri = null;
        				if (AccountCache.isExchange(context, account.mId)
        						&& mailbox.mType == Mailbox.TYPE_DRAFTS) {
        					deleteUri = EmailContent.Message.CONTENT_URI;
        				} else {
        					deleteUri = EmailContent.Message.SYNCED_CONTENT_URI;
        				}
        				// 4. Drop non-essential data for the message (e.g.
        				// attachment
        				// files)
        				AttachmentUtilities.deleteAllAttachmentFiles(context, account.mId,
        						messageIds[i]);

        				// delete attachment temp image files
        				Attachment[] atts = Attachment.restoreAttachmentsWithMessageId(context, messageIds[i]);
        				for (Attachment att : atts) {

        					if (att.mContentUri == null)
        						continue;

        					Uri uri = Uri.parse(att.mContentUri);

        					if (uri != null && "file".equals(uri.getScheme())) {
        						String filePath = uri.getPath();

        						if (filePath != null && filePath.contains(CommonDefs.tempDirectory)
        								&& !(filePath.contains(CommonDefs.SAVE_SIGNATURE_TMPSAMMFILE_PATH))
        								&& !(filePath.contains("/TempSignature/"))) { //add code temporary
        							File delFile = new File(filePath);

        							if (delFile != null ) delFile.delete();
        						}
        					}
        				}

        				//              if (mailbox.mType == Mailbox.TYPE_OUTBOX) {
        				//                  Preferences.getPreferences(mContext).removeFailNotiPref(account.mId, messageId);
        				//              }
        				// 5. Perform "delete" as appropriate
        				// Delete message body files
        				BodyUtilites.deleteAllMessageBodyFilesUri(context, account.mId, messageIds[i]);

        				Uri uri = ContentUris.withAppendedId(deleteUri, messageIds[i]);
        				ContentProviderOperation cpo = ContentProviderOperation.newDelete(uri).build();
        				ops.add(cpo);

        			} else {
        				if(moveMessageIdList.size() == 0) {
        					prevMailboxId = mailbox.mId;
        				}
        				moveMessageIdList.add(new Long(messageIds[i]));
        			}
        		}                	
        	}        
        	resolver.applyBatch(EmailContent.AUTHORITY, ops);
        	
        	long[] moveMessageIds = Utility.toPrimitiveLongArray(moveMessageIdList);
        	moveMessageToSameAccount(moveMessageIds, account, trashMailboxId,
        			prevMailboxId, true);
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
    
    
    // change@kw21.kim To distinguish between delete and move message
    public void moveMessageToSameAccount(long[] messageIds, Account account,
            long targetMailboxId, long prevMailboxId, boolean isDelete) {
        long accountId = account.mId;
        EmailLog.d(TAG, "[moveMessageToSameAccount" + " accountId=" + accountId +
                " targetMailboxId=" + targetMailboxId + " prevMailboxId=" + prevMailboxId +
                " messageIds.length=" + messageIds.length + " messageIds=" +
                Arrays.toString(messageIds) + "]");

        Log.d(TAG, "moveMessageToSameAccount() is called : " + accountId + "," + targetMailboxId
                + "," + prevMailboxId );

        Log.d(TAG, "isDelete= " + isDelete);
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ArrayList<ContentProviderOperation> bodyops = new ArrayList<ContentProviderOperation>();
        ContentResolver resolver = mContext.getContentResolver();
        if (isDelete && AccountCache.isExchange(mContext, accountId)) {
            StringBuilder messageIdString = new StringBuilder();
            int count = 0;
            for (long id : messageIds) {
                if (messageIdString.length() > 0) {
                    messageIdString.append(",");
                }
                count++;
                messageIdString.append(id);
                if (count > BATCH_OPERATION_COUNT) {
                    String selection = Message.RECORD_ID + " IN (" + messageIdString + ")";
                    ContentProviderOperation.Builder b = ContentProviderOperation
                            .newDelete(EmailContent.Message.SYNCED_CONTENT_URI);
                    ops.add(b.withSelection(selection, null).build());

                    selection = Body.MESSAGE_KEY + " IN (" + messageIdString + ")";
                    ContentProviderOperation.Builder b1 = ContentProviderOperation
                            .newDelete(BodyUtilites.getFileBodyDeleteUri(accountId));
                    bodyops.add(b1.withSelection(selection, null).build());
                    count = 0;
                    messageIdString.setLength(0);
                }
//                BodyUtilites.deleteAllMessageBodyFilesUri(mContext, account.mId, id);
            }
            if (messageIdString.length() > 0) {
                String selection = Message.RECORD_ID + " IN (" + messageIdString + ")";
                ContentProviderOperation.Builder b = ContentProviderOperation
                        .newDelete(EmailContent.Message.SYNCED_CONTENT_URI);
                ops.add(b.withSelection(selection, null).build());

                selection = Body.MESSAGE_KEY + " IN (" + messageIdString + ")";
                ContentProviderOperation.Builder b1 = ContentProviderOperation
                        .newDelete(BodyUtilites.getFileBodyDeleteUri(accountId));
                bodyops.add(b1.withSelection(selection, null).build());
                count = 0;
                messageIdString.setLength(0);
            }
        } else {
            int targetMailboxType = Mailbox.getMailboxType(mContext,targetMailboxId);
            if (AccountCache.isPop3(mContext, accountId)) {
                //TODO Optimize this loop
                /**
                 * P150128-03120
                 * When we delete from the Inbox of a POP account, we need to handle delete
                 * settings for the POP account
                 */
                EmailLog.d(TAG, "Moving a POP message..");
                ContentValues cv = new ContentValues();
                cv.put(EmailContent.MessageColumns.MAILBOX_KEY, targetMailboxId);
                cv.put(EmailContent.MessageColumns.MAILBOX_TYPE, targetMailboxType);

                HashSet<String> serverIds = new HashSet<String>();
                for (Long id : messageIds) {
                    EmailContent.Message message = EmailContent.Message.restoreMessageWithId(mContext, id);
                    if (message == null)
                        continue;

                    if (message.mServerId == null
                            || (message.mServerId != null && message.mServerId.length() <= 0)) {
                        // this message cannot synced to server. draft or outbox
                        // message.
                        // So move this message just at the local DB.
                        Uri uri = ContentUris.withAppendedId(EmailContent.Message.SYNCED_CONTENT_URI,
                                id);
                        ContentProviderOperation.Builder b = ContentProviderOperation
                                .newUpdate(uri);

                        ops.add(b.withValues(cv).build());
                        //resolver1.update(uri, cv, null, null);
                        continue;
                    }
                    serverIds.add(message.mServerId);

                    if (targetMailboxType == Mailbox.TYPE_TRASH) {
                        /**
                         * In case of POP account, if we delete an email then set the flag to LOADED_COMPLETE
                         * even it is partially downloaded.
                         */
                        cv.put(EmailContent.MessageColumns.FLAG_LOADED, EmailContent.Message.FLAG_LOADED_COMPLETE);
                    }
                    Uri uri = ContentUris.withAppendedId(EmailContent.Message.SYNCED_CONTENT_URI, id);
                    ContentProviderOperation.Builder b = ContentProviderOperation
                            .newUpdate(uri);

                    ops.add(b.withValues(cv).build());
                    // resolver1.update(uri, cv, null, null);

                    // test code for pop3 ==>
                    int mailboxType = -1;
                    if (prevMailboxId > 0) {
                        mailboxType = Mailbox.getMailboxType(mContext, prevMailboxId);
                    }
                    if (account.isPopUpSyncConceptDisabled() && mailboxType == Mailbox.TYPE_INBOX) {
                        EmailContent.Message sentinel = new EmailContent.Message();
                        sentinel.mAccountKey = message.mAccountKey;
                        sentinel.mMailboxKey = message.mMailboxKey;
                        sentinel.mTimeStamp = message.mTimeStamp;
                        sentinel.mFlagLoaded = EmailContent.Message.FLAG_LOADED_DELETED;
                        sentinel.mFlagRead = true;
                        sentinel.mServerId = message.mServerId;
                        sentinel.save(mContext);
                    }
                    else if(account.isPopUpSyncConceptDisabled() && targetMailboxType == Mailbox.TYPE_INBOX){
                        long messageId = Message.getMessgeIdWithSyncServerId(mContext, message.mServerId);
                        if(messageId != -1){
                            EmailContent.Message.delete(mContext, EmailContent.Message.CONTENT_URI, messageId);
                            // Delete message body files
                            BodyUtilites.deleteAllMessageBodyFilesUri(mContext, account.mId, messageId);
                        }
                    }
                }
                if (serverIds.isEmpty()) {
                    return;
                }
            } else {
                ContentValues cv = new ContentValues();
                cv.put(EmailContent.MessageColumns.MAILBOX_KEY, targetMailboxId);
                cv.put(EmailContent.MessageColumns.FLAG_MOVED,
                        EmailContent.Message.MESSAGE_FLAG_MOVED);
                cv.put(EmailContent.MessageColumns.MAILBOX_TYPE, targetMailboxType);

                StringBuilder messageIdString = new StringBuilder();
                int count = 0;
                for (long id : messageIds) {
                    if (messageIdString.length() > 0) {
                        messageIdString.append(",");
                    }
                    count++;
                    messageIdString.append(id);
                    if (count > BATCH_OPERATION_COUNT) {
                        if (targetMailboxType == Mailbox.TYPE_TRASH
                                || targetMailboxType == Mailbox.TYPE_JUNK) {
                            String remiderSelection = ReminderColumns.MESSAGE_ID + " in ("
                                    + messageIdString + ")";
                            MessageReminderUtil.unsetReminderWhere(mContext, remiderSelection);
                        }
                        String selection = Message.RECORD_ID + " IN (" + messageIdString + ")";
                        ContentProviderOperation.Builder b = ContentProviderOperation
                                .newUpdate(EmailContent.Message.SYNCED_CONTENT_URI);
                        ops.add(b.withSelection(selection, null).withValues(cv).build());
                        count = 0;
                        messageIdString.setLength(0);
                    }
                }

                if (messageIdString.length() > 0) {
                    if (targetMailboxType == Mailbox.TYPE_TRASH
                            || targetMailboxType == Mailbox.TYPE_JUNK) {
                        String remiderSelection = ReminderColumns.MESSAGE_ID + " in ("
                                + messageIdString + ")";
                        MessageReminderUtil.unsetReminderWhere(mContext, remiderSelection);
                    }
                    String selection = Message.RECORD_ID + " IN (" + messageIdString + ")";
                    ContentProviderOperation.Builder b = ContentProviderOperation
                            .newUpdate(EmailContent.Message.SYNCED_CONTENT_URI);
                    ops.add(b.withSelection(selection, null).withValues(cv).build());
                }
            }
        }

        try {
            if (bodyops.size() > 0) {
                resolver.applyBatch(EmailContent.AUTHORITY, bodyops);
            }
            resolver.applyBatch(EmailContent.AUTHORITY, ops);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "moveMessageToSameAccount end");
    }

    static public synchronized long findOrCreateMailboxOfType(Context context, long accountId, int mailboxType) {
        if (accountId < 0 || mailboxType < 0) {
            return Mailbox.NO_MAILBOX;
        }
        long mailboxId = Mailbox.findMailboxOfType(context, accountId, mailboxType);
        return mailboxId == Mailbox.NO_MAILBOX ? createMailbox(context, accountId, mailboxType) : mailboxId;
    }
    
    static public long createMailbox(Context context, long accountId, int mailboxType) {
        if (accountId < 0 || mailboxType < 0) {
            String mes = "Invalid arguments " + accountId + ' ' + mailboxType;
            Log.e(TAG, mes);
            throw new RuntimeException(mes);
        }
        Mailbox box = new Mailbox();
        box.mAccountKey = accountId;
        box.mType = mailboxType;
        box.mSyncInterval = EmailContent.Account.CHECK_INTERVAL_NEVER;
        box.mFlagVisible = true;
        box.mDisplayName = getMailboxServerName(mailboxType);
        // jk0112.lee, folder hierarchy, -1 means root folder
        box.mParentKey = -1;
        box.save(context);
        return box.mId;
    }
    
    public static String getMailboxServerName(int mailboxType) {
        String mMailBoxName = null;
        switch (mailboxType) {
            case Mailbox.TYPE_INBOX:
                mMailBoxName = "Inbox";
                break;
            case Mailbox.TYPE_OUTBOX:
                mMailBoxName = "Outbox";
                break;
            case Mailbox.TYPE_DRAFTS:
                mMailBoxName = "Drafts";
                break;
            case Mailbox.TYPE_TRASH:
                mMailBoxName = "Trash";
                break;
            case Mailbox.TYPE_SENT:
                mMailBoxName = "Sent";
                break;
            case Mailbox.TYPE_JUNK:
                mMailBoxName = EmailContent.DEFAULT_SPAM;
                break;
            // jh5997.hyun - 2011.03.03 - Add for EAS server search
            // change@wtl.fzang email search begin
            case Mailbox.TYPE_SEARCH_RESULTS:
                mMailBoxName = "Search";
                break;
        // change@wtl.fzhang search end
            //change@wtl.jpshu document search begin
            case Mailbox.TYPE_SEARCH_DOCS:
                mMailBoxName = "Sharepoint/UNC";
                break;
            //change@wtl.jpshu document search end
            case  Mailbox.TYPE_SCHEDULED_OUTBOX:
                mMailBoxName = "Scheduled outbox";
                break;
        }

        return mMailBoxName != null ? mMailBoxName : "";
    }
    

    public void syncMailboxInternalCommon(final long accountId, final long mailboxId,
            final boolean requestAdditionalMessage, Account acc) {
    String accountType = null;
    if (AccountCache.isExchange(mContext, accountId))
        accountType = AccountManagerTypes.TYPE_EXCHANGE;
    else if(CarrierValues.IS_CARRIER_CUE && acc.mEmailAddress.contains(EmailFeature.getAccountHintDomain()) )
        accountType = AccountManagerTypes.TYPE_NAUTA;
    else
        accountType = AccountManagerTypes.TYPE_POP_IMAP;

    Bundle extras = new Bundle();
    extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
    extras.putBoolean(ContentResolver.SYNC_EXTRAS_DO_NOT_RETRY, true);
    extras.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
    extras.putLong(IntentConst.EXTRA_MAILBOX_ID, mailboxId);

    if (!AccountCache.isExchange(mContext, accountId)) {
        extras.putInt(IntentConst.EXTRA_ADDITIONAL_MESSAGE_FACTOR, requestAdditionalMessage ? 1 : 0);
    }

        if (acc != null) {
            android.accounts.Account acct = new android.accounts.Account(acc.mEmailAddress,
                    accountType);
            
            Log.e(TAG, "requestSync : " + acc.mId + " " + accountType);
            ContentResolver.requestSync(acct, EmailContent.AUTHORITY, extras);
        }
    }

    public AsyncTask<Void, Void, Void> setConversationsRead(final long[] threadIds,
			final boolean isRead, String selection) {
        // Account account = Account.restoreAccountWithId(mContext, accountId);
		return setConversationsBoolean(threadIds, EmailContent.MessageColumns.FLAG_READ, isRead, selection);
    }

    public AsyncTask<Void, Void, Void> setConversationsBoolean(final long[] threadIds,
            final String columnName, final boolean columnValue, final String selection) {
        return Utility.runAsync(new Runnable() {
            public void run() {
                if (threadIds == null || threadIds.length == 0) {
                    return;
                }

                ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
                ContentValues cv = new ContentValues();
                cv.put(columnName, columnValue);
                StringBuilder threadIdString = new StringBuilder();
                int count = 0;
                for (long id : threadIds) {
                    if (threadIdString.length() > 0) {
                        threadIdString.append(",");
                    }
                    count++;
                    threadIdString.append(id);
                    if (count > BATCH_OPERATION_COUNT) {
                        String where = Message.THREAD_ID + " IN (" + threadIdString + ")";
                        if (selection != null) {
                            where += " AND " + selection;
                        }
                        ContentProviderOperation.Builder b = ContentProviderOperation
                                .newUpdate(EmailContent.Message.SYNCED_CONTENT_URI);
                        ops.add(b.withSelection(where, null).withValues(cv).build());
                        count = 0;
                        threadIdString.setLength(0);
                    }
                }
                if (threadIdString.length() > 0) {
                    String where = Message.THREAD_ID + " IN (" + threadIdString + ")";
                    if (selection != null) {
                        where += " AND " + selection;
                    }
                    ContentProviderOperation.Builder b = ContentProviderOperation
                            .newUpdate(EmailContent.Message.SYNCED_CONTENT_URI);
                    ops.add(b.withSelection(where, null).withValues(cv).build());
                }

                try {
                    mContext.getContentResolver().applyBatch(EmailContent.AUTHORITY, ops);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (OperationApplicationException e) {
                    e.printStackTrace();
                }

                if (columnName.equals(EmailContent.MessageColumns.FLAG_READ) == true) {
                    BadgeManager.updateBadgeProvider(mContext);
                }
            }
        });
    }
    
    public static boolean isServerDraftsFolder(Mailbox mailbox) {
        if (mailbox != null && !TextUtils.isEmpty(mailbox.mServerId)
                && mailbox.mType == Mailbox.TYPE_DRAFTS) {
                return true;
        }
        return false;
    }
}
