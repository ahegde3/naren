/*
 * Copyright (C) 2009 The Android Open Source Project
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

import com.samsung.android.emailcommon.mail.MessagingException;
import com.samsung.android.emailcommon.provider.EmailContent;
import com.samsung.android.emailcommon.provider.EmailContent.Account;
import com.samsung.android.emailcommon.provider.EmailContent.Mailbox;

import android.content.Context;
import java.util.ArrayList;

/**
 * This interface allows a store to define a completely different synchronizer
 * algorithm, as necessary.
 */
public interface StoreSynchronizer {

    /**
     * An object of this class is returned by SynchronizeMessagesSynchronous to
     * report the results of the sync run.
     */
    public static class SyncResults {
        /**
         * The total # of messages in the folder
         */
        public int mTotalMessages;

        /**
         * The total # of messages in the folder
         */
        public int mSyncableMessages = -1;
        
        /**
         * The # of new messages in the folder
         */
        public ArrayList<String> mNewMessages;

        /**
         * Parameter shows message count already download.
         */
        public int mMessageFetched;

        /**
         * Used during Qresync if the changed messages are more in number!
         */
        public boolean mReSync;

        /**
         * Used to trigger the SNC module to update the account list from Server
         * since a mail-without-account is received while sync'ing, which shows
         * some inconsistency between the client and server.
         */
        public boolean mUpdateAccountList;

        /**
         * no of messages deleted during the sync
         */
        public int mDeletedMessages;

        /**
         * no of messages updated during the sync
         */
        public int mUpdatedMessages;

        /**
         * sync key. currently, UIDNEXT value is used as a sync key to effectively identify
         * the changes in the mailbox. This is primarily used in legacy push support using IDLE
         */
        public String mSyncKey;

        /**
         * time taken for the sync
         */
        public long mSyncTime;

        /**
         * load more count for IMAP days based sync
         */
        public long mloadMoreCount;

        /**
         * Sync ignored due to IMAP Smart sync
         */
        public boolean mSyncIgnored;

        /**
         * The # of SPAM messages in the folder
         */
        public ArrayList<String> mSpamMessages;

        public SyncResults() {
        }

        /**
         * @param totalMessages
         * @param newMessages
         */
        public SyncResults(int totalMessages, ArrayList<String> newMessages) {
            mTotalMessages = totalMessages;
            mNewMessages = newMessages;
        }

        public SyncResults(int totalMessages, ArrayList<String> newMessages, String syncKey) {
            mTotalMessages = totalMessages;
            mNewMessages = newMessages;
            mSyncKey = syncKey;
        }

        public SyncResults(int totalMessages, ArrayList<String> newMessages, boolean reSync) {
            mTotalMessages = totalMessages;
            mNewMessages = newMessages;
            mReSync = reSync;
        }

        public SyncResults(int totalMessages, ArrayList<String> newMessages, boolean reSync,
                boolean updateAccountList) {
            mTotalMessages = totalMessages;
            mNewMessages = newMessages;
            mReSync = reSync;
            mUpdateAccountList = updateAccountList;
        }

        public SyncResults(int totalMessages, ArrayList<String> newMessages, int fetchedMessages) {
            mTotalMessages = totalMessages;
            mNewMessages = newMessages;
            mMessageFetched = fetchedMessages;
        }

        public SyncResults(int totalMessages, ArrayList<String> newMessages, int fetchedMessages, long syncTime) {
            mTotalMessages = totalMessages;
            mNewMessages = newMessages;
            mMessageFetched = fetchedMessages;
            mSyncTime = syncTime;
        }

        public SyncResults(int totalMessages, ArrayList<String> newMessages, boolean reSync,
                boolean updateAccountList, long syncTime) {
            mTotalMessages = totalMessages;
            mNewMessages = newMessages;
            mReSync = reSync;
            mUpdateAccountList = updateAccountList;
            mSyncTime = syncTime;
        }

        public SyncResults(int totalMessages, ArrayList<String> newMessages, int syncableMessages, int fetchedMessages, long syncTime) {
            mTotalMessages = totalMessages;
            mNewMessages = newMessages;
            mSyncableMessages = syncableMessages;
            mMessageFetched = fetchedMessages;
            mSyncTime = syncTime;
        }


        @Override
        public String toString() {
            return "mTotal=" + mTotalMessages + " mFetched=" + mMessageFetched +
                    " mNew=" + mNewMessages + " mSpam=" + mSpamMessages + " mUpdated=" + mUpdatedMessages +
                    " mDeleted=" + mDeletedMessages + " mSyncableMessages=" + mSyncableMessages +
                    " mloadMoreCount=" + mloadMoreCount + " mSynckey=" + mSyncKey +
                    " mSyncTime=" + mSyncTime;
        }
    }

    /**
     * The job of this method is to synchronize messages between a remote folder
     * and the corresponding local folder. The following callbacks should be
     * called during this operation:
     * {@link MessagingListener#synchronizeMailboxNewMessage(Account, String, Message)}
     * {@link MessagingListener#synchronizeMailboxRemovedMessage(Account, String, Message)}
     * Callbacks (through listeners) *must* be synchronized on the listeners
     * object, e.g. synchronized (listeners) { for(MessagingListener listener :
     * listeners) { listener.synchronizeMailboxNewMessage(account, folder,
     * message); } }
     * 
     * @param account The account to synchronize
     * @param folder The folder to synchronize
     * @param listeners callbacks to make during sync operation
     * @param context if needed for making system calls
     * @return an object describing the sync results
     */
    public SyncResults SynchronizeMessagesSynchronous(EmailContent.Account account,
            EmailContent.Mailbox folder, Context context)
            throws MessagingException;

    public SyncResults SynchronizeMailBox(EmailContent.Account account,
            EmailContent.Mailbox folder, boolean remoteSync) throws MessagingException;

    public SyncResults qresyncMailBox(final Account account, final Mailbox folder,
            final boolean remoteSync) throws MessagingException;



}



