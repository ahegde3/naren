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

package com.android.common.contacts;

import com.android.common.widget.CompositeCursorAdapter;
import com.samsung.android.emailcommon.AccountManagerTypes;
import com.samsung.android.emailcommon.utility.EmailLog;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Groups;
import android.text.TextUtils;
import android.text.util.Rfc822Token;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * A base class for email address autocomplete adapters. It uses
 * {@link Email#CONTENT_FILTER_URI} to search for data rows by email address
 * and/or contact name. It also searches registered {@link Directory}'s.
 */
public abstract class BaseEmailAddressAdapterSec extends CompositeCursorAdapter implements Filterable {

    private static final String TAG = "BaseEmailAddressAdapter";

    // TODO: revert to references to the Directory class as soon as the
    // issue with the dependency on SDK 8 is resolved

    // This is Directory.LOCAL_INVISIBLE
    private static final long DIRECTORY_LOCAL_INVISIBLE = 1;

    // This is ContactsContract.DIRECTORY_PARAM_KEY
    private static final String DIRECTORY_PARAM_KEY = "directory";

    // This is ContactsContract.LIMIT_PARAM_KEY
    private static final String LIMIT_PARAM_KEY = "limit";

    // This is ContactsContract.PRIMARY_ACCOUNT_NAME
    private static final String PRIMARY_ACCOUNT_NAME = "name_for_primary_account";
    // This is ContactsContract.PRIMARY_ACCOUNT_TYPE
    private static final String PRIMARY_ACCOUNT_TYPE = "type_for_primary_account";

    /**
     * The preferred number of results to be retrieved. This number may be
     * exceeded if there are several directories configured, because we will use
     * the same limit for all directories.
     */
    private static final int DEFAULT_PREFERRED_MAX_RESULT_COUNT = 10;

    /**
     * The number of extra entries requested to allow for duplicates. Duplicates
     * are removed from the overall result.
     */
    private static final int ALLOWANCE_FOR_DUPLICATES = 5;

    /**
     * The "Searching..." message will be displayed if search is not complete
     * within this many milliseconds.
     */
    private static final int MESSAGE_SEARCH_PENDING_DELAY = 1000;

    private static final int MESSAGE_SEARCH_PENDING = 1;
    
    //change@siso.saritha Recipient Information cache 
    public static final String EXCHANGE_GAL_AUTHORITY = "com.samsung.android.exchange.directory.provider";
    public static final Uri RIC_URI = Uri.parse("content://" + EXCHANGE_GAL_AUTHORITY + "/recipientInformation cache/");
    
    private Long Acc_Id=(long)-1;
    public static final int MAX_DIRECTORYPARTITIONFILTER_RESULT = 20;
    public static final String START_PARAM_KEY = "startindex";
    public static final String NEED_PHOTO_DATA = "needPhotoData";
    
    //EAS GAL search projection string
    public static final String GAL_DISPLAY_NAME = "displayName";
    public static final String GAL_EMAIL_ADDRESS = "emailAddress";
    public static final String GAL_WORK_PHONE = "workPhone";
    public static final String GAL_HOME_PHONE = "homePhone";
    public static final String GAL_MOBILE_PHONE = "mobilePhone";
    public static final String GAL_FIRST_NAME = "firstName";
    public static final String GAL_LAST_NAME = "lastName";
    public static final String GAL_COMPANY = "company";
    public static final String GAL_TITLE = "title";
    public static final String GAL_OFFICE = "office";
    public static final String GAL_ALIAS = "alias";
    //change@siso.vinay Gal Photo start
    public static final String GAL_PICTURE_DATA ="pictureData";
    //change@siso.vinay Gal Photo end
    
    public static boolean ExpandResultMaxAndShowMoreMode = false;
    public static boolean isEnableGroupSearch = false;
    
    // Disable online search
    public boolean isOnlineSearchDisabled = false;
    
    protected final Object mSynchronizer = new Object();
    public static final String EXCHANGE_GAL_SHOW_MORE =  "gal_search_show_more";

    /**
     * Model object for a {@link Directory} row. There is a partition in the
     * {@link CompositeCursorAdapter} for every directory (except
     * {@link Directory#LOCAL_INVISIBLE}.
     */
    public final static class DirectoryPartition extends CompositeCursorAdapter.Partition {
        public long directoryId;
        public String directoryType;
        public String displayName;
        public String accountName;
        public String accountType;
        public boolean loading;
        public CharSequence constraint;
        public DirectoryPartitionFilter filter;
        public boolean moreResultIsRemained;

        public DirectoryPartition() {
            super(false, false);
            moreResultIsRemained = false;
        }
    }

    private static class EmailQuery {
        public static final String[] PROJECTION = {
            Contacts.DISPLAY_NAME,  // 0
            Email.DATA              // 1
        };

        public static final int NAME = 0;
        public static final int ADDRESS = 1;
    }
    
    //change@siso.vinay Gal photo start
    private static class EmailQuery1 {
        public static final String[] PROJECTION = {
            Contacts.DISPLAY_NAME,  // 0
            Email.DATA              // 1
            ,GAL_PICTURE_DATA       // 2 
        };

        public static final int NAME = 0;
        public static final int ADDRESS = 1;
        public static final int PICTURE_DATA = 2;
    }
    //change@siso.vinay Gal photo end

    private static class DirectoryListQuery {

        // TODO: revert to references to the Directory class as soon as the
        // issue with the dependency on SDK 8 is resolved
        public static final Uri URI =
                Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "directories");
        private static final String DIRECTORY_ID = "_id";
        private static final String DIRECTORY_ACCOUNT_NAME = "accountName";
        private static final String DIRECTORY_ACCOUNT_TYPE = "accountType";
        private static final String DIRECTORY_DISPLAY_NAME = "displayName";
        private static final String DIRECTORY_PACKAGE_NAME = "packageName";
        private static final String DIRECTORY_TYPE_RESOURCE_ID = "typeResourceId";

        public static final String[] PROJECTION = {
            DIRECTORY_ID,               // 0
            DIRECTORY_ACCOUNT_NAME,     // 1
            DIRECTORY_ACCOUNT_TYPE,     // 2
            DIRECTORY_DISPLAY_NAME,     // 3
            DIRECTORY_PACKAGE_NAME,     // 4
            DIRECTORY_TYPE_RESOURCE_ID, // 5
        };

        public static final int ID = 0;
        public static final int ACCOUNT_NAME = 1;
        public static final int ACCOUNT_TYPE = 2;
        public static final int DISPLAY_NAME = 3;
        public static final int PACKAGE_NAME = 4;
        public static final int TYPE_RESOURCE_ID = 5;
    }

    /**
     * A fake column name that indicates a "Searching..." item in the list.
     */
    private static final String SEARCHING_CURSOR_MARKER = "searching";

    /**
     * An asynchronous filter used for loading two data sets: email rows from the local
     * contact provider and the list of {@link Directory}'s.
     */
    private final class DefaultPartitionFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Cursor directoryCursor = null;
            Cursor recentCursor = null;
            
            if (!mDirectoriesLoaded) {
                directoryCursor = mContentResolver.query(
                        DirectoryListQuery.URI, DirectoryListQuery.PROJECTION, null, null, null);
                mDirectoriesLoaded = true;
            }
            MergeCursor mergeResultCursor =null;
            FilterResults results = new FilterResults();
            Cursor cursor = null;
            Cursor groupCursor = null;
            Cursor rICursor1 = null;
            if (constraint != null && !TextUtils.isEmpty(constraint)) {
                Uri uri = null;
                Uri.Builder builder = null;
                if (ExpandResultMaxAndShowMoreMode) {
                    builder = Email.CONTENT_FILTER_URI.buildUpon()
                        .appendPath(constraint.toString());
                } else {
                    builder = Email.CONTENT_FILTER_URI.buildUpon()
                        .appendPath(constraint.toString())
                        .appendQueryParameter(LIMIT_PARAM_KEY,
                                String.valueOf(mPreferredMaxResultCount));
                }
                if (mAccount != null) {
                    builder.appendQueryParameter(PRIMARY_ACCOUNT_NAME, mAccount.name);
                    builder.appendQueryParameter(PRIMARY_ACCOUNT_TYPE, mAccount.type);
                }
                uri = builder.build();
                if(isEnableGroupSearch != false)
                    groupCursor = getGroupNameCursor(constraint);
                cursor = mContentResolver.query(uri, EmailQuery.PROJECTION, null, null, null);

                // /
                // SQLiteDatabase mDatabase = helper.getWritableDatabase();

                // /

                // change@siso.saritha Recipient Information cache start
                //disable online search
                if(isOnlineSearchDisabled == false) {
                    rICursor1 = RIemailAddress(Acc_Id, constraint);
                }
                
                //taesoo77.lee for adding recent-address-cache
                try{
                    String filter = constraint.toString();
                
                    Uri emailCacheUri =  Uri.withAppendedPath(Uri.parse("content://com.samsung.android.email.provider/emailaddresscache/filter"), Uri.encode(filter));
                    recentCursor = mContentResolver.query(emailCacheUri, new String[] {"accountName as display_name", "accountAddress as data1"}, null, null, "usageCount DESC, accountName");
                }catch(Exception e){
                    recentCursor = null;
                }
                
                if(rICursor1 !=null && recentCursor!=null){
                	if(groupCursor == null || isEnableGroupSearch == false) {
	                    mergeResultCursor = new MergeCursor(new Cursor[] {
	                            cursor, rICursor1, recentCursor
	                    });
                	} else {
                		mergeResultCursor = new MergeCursor(new Cursor[] {
	                            groupCursor, cursor, rICursor1, recentCursor
	                    });
                	}
	                results.count = mergeResultCursor.getCount();
                }else if(rICursor1 !=null){
                	if(groupCursor == null || isEnableGroupSearch == false) {
	                    mergeResultCursor = new MergeCursor(new Cursor[] {
	                            cursor, rICursor1
	                    });
                	} else {
                		mergeResultCursor = new MergeCursor(new Cursor[] {
                				groupCursor, cursor, rICursor1
	                    });
                	}                		
                    results.count = mergeResultCursor.getCount();
                }else if(recentCursor != null){
                	if(groupCursor == null || isEnableGroupSearch == false) {
	                    mergeResultCursor = new MergeCursor(new Cursor[] {
	                            cursor, recentCursor
	                    });
                	} else {
                		mergeResultCursor = new MergeCursor(new Cursor[] {
                				groupCursor, cursor, recentCursor
	                    });
                	}
                    results.count = mergeResultCursor.getCount();
                }else{
                	if(groupCursor == null || isEnableGroupSearch == false) {
                		results.count = cursor.getCount();
                	} else {
                		mergeResultCursor = new MergeCursor(new Cursor[] {
                				groupCursor, cursor
	                    });
                		results.count = mergeResultCursor.getCount();
                	}
                }
                
//                if (rICursor1 != null) {
//                    mergeResultCursor = new MergeCursor(new Cursor[] {
//                            cursor, rICursor1
//                    });
//                    results.count = mergeResultCursor.getCount();
//                } else {
//                    results.count = cursor.getCount();
//                }
            }
            
            if((groupCursor != null && isEnableGroupSearch != false) || rICursor1 != null || recentCursor!=null) {
            	results.values = new Cursor[] { directoryCursor, mergeResultCursor };
            }else {
            	results.values = new Cursor[] { directoryCursor, cursor };
            }
			//change@siso.saritha Recipient Information Cache end
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                Cursor[] cursors = (Cursor[]) results.values;
                onDirectoryLoadFinished(constraint, cursors[0], cursors[1]);
            }
            results.count = getCount();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return makeDisplayString((Cursor) resultValue);
        }
    }

    public Cursor RIemailAddress(long acckey,CharSequence constraint)
    {
    	 String filter = constraint == null ? "" : constraint.toString();    	
    	 Cursor ric =  null;    	 
    	 
    	 try{
	    	 Uri uri = RIC_URI.buildUpon().appendPath(Long.toString(acckey)).appendPath(filter).build();
	    	 ric = mContentResolver.query(uri,EmailQuery.PROJECTION, null, null, null);
    	 }catch(Exception e) {
    		 ric = null;
    	 }
    	 
    	 return ric;    
    }
    /**
     * An asynchronous filter that performs search in a particular directory.
     */
    private final class DirectoryPartitionFilter extends Filter {
        private final int mPartitionIndex;
        private final long mDirectoryId;
        private int mLimit;

        public DirectoryPartitionFilter(int partitionIndex, long directoryId) {
            this.mPartitionIndex = partitionIndex;
            this.mDirectoryId = directoryId;
        }

        public synchronized void setLimit(int limit) {
            this.mLimit = limit;
        }

        public synchronized int getLimit() {
            return this.mLimit;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (!TextUtils.isEmpty(constraint)) {
                Uri uri = null;
                Cursor cursor = null;
                if(ExpandResultMaxAndShowMoreMode)
                {
                    uri = Email.CONTENT_FILTER_URI.buildUpon()
                    .appendPath(constraint.toString())
                    .appendQueryParameter(DIRECTORY_PARAM_KEY, String.valueOf(mDirectoryId))
                    .appendQueryParameter(LIMIT_PARAM_KEY, String.valueOf(getLimit()))
//                    .appendQueryParameter(NEED_PHOTO_DATA, "1")
                    .build();
                    
                    cursor = mContentResolver.query(
                            uri, EmailQuery1.PROJECTION, START_PARAM_KEY, new String[] {
                            String.valueOf(getLimit() - BaseEmailAddressAdapterSec.MAX_DIRECTORYPARTITIONFILTER_RESULT)}, 
                            null);
                }
                else
                {
                    uri = Email.CONTENT_FILTER_URI.buildUpon()
                            .appendPath(constraint.toString())
                            .appendQueryParameter(DIRECTORY_PARAM_KEY, String.valueOf(mDirectoryId))
                            .appendQueryParameter(LIMIT_PARAM_KEY,
                                    String.valueOf(getLimit() + ALLOWANCE_FOR_DUPLICATES))
                            .build();
                    
                    cursor = mContentResolver.query(
                            uri, EmailQuery.PROJECTION, null, null, null);
                }


                results.values = cursor;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Cursor cursor = (Cursor) results.values;
            onPartitionLoadFinished(constraint, mPartitionIndex, cursor);
            results.count = getCount();
        }
    }

    protected final ContentResolver mContentResolver;
    private boolean mDirectoriesLoaded;
    private Account mAccount;
    private int mPreferredMaxResultCount = 0;
    private Handler mHandler;

    public BaseEmailAddressAdapterSec(Context context) {
        this(context, DEFAULT_PREFERRED_MAX_RESULT_COUNT);
    }

    public BaseEmailAddressAdapterSec(Context context, int preferredMaxResultCount) {
        super(context);
        mContentResolver = context.getContentResolver();
        mPreferredMaxResultCount = preferredMaxResultCount;
        
        if(mPreferredMaxResultCount < 0)
        {
            ExpandResultMaxAndShowMoreMode = true;
        }
        else
        {
            ExpandResultMaxAndShowMoreMode = false;
        }

        if(mPreferredMaxResultCount == -2) {
            isEnableGroupSearch = true;
        } else {
            isEnableGroupSearch = false;
        }

        mHandler = new MyHandler(this);
    }


    public static class MyHandler extends Handler {

        WeakReference<BaseEmailAddressAdapterSec> viewHelper;

        public  MyHandler(BaseEmailAddressAdapterSec view) {
            viewHelper = new WeakReference<BaseEmailAddressAdapterSec>(
                    view);
        }
        @Override
        public void handleMessage(Message msg) {
            if(viewHelper != null) {
                BaseEmailAddressAdapterSec service = viewHelper.get();
                if(service != null) {
                    service.showSearchPendingIfNotComplete(msg.arg1);
                }
            }
        }
    }
    
    
    /**
     * Set the account when known. Causes the search to prioritize contacts from
     * that account.
     */
    public void setAccount(Account account) {
        mAccount = account;
    }

    /**
     * Editted version for Recipient Information Cache of Exchange
     */
    public void setAccount(Account account,Long AccId) {
        mAccount = account;
        Acc_Id = AccId;
    }

    //disable online search
    public void disableOnlineSearch(boolean disable) {
        isOnlineSearchDisabled = disable;
    }

    /**
     * Override to create a view for line item in the autocomplete suggestion list UI.
     */
    protected abstract View inflateItemView(ViewGroup parent);

    /**
     * Override to populate the autocomplete suggestion line item UI with data.
     */
//    protected abstract void bindView(View view, String directoryType, String directoryName,
//            String displayName, String emailAddress);
    protected abstract void bindView(View view, String directoryType, String directoryName,
            String displayName, String emailAddress, String pictureData);

    /**
     * Override to create a view for a "Searching directory" line item, which is
     * displayed temporarily while the corresponding filter is running.
     */
    protected abstract View inflateItemViewLoading(ViewGroup parent);

    /**
     * Override to populate the "Searching directory" line item UI with data.
     */
    protected abstract void bindViewLoading(View view, String directoryType, String directoryName);

    @Override
    protected int getItemViewType(int partitionIndex, int position) {
        DirectoryPartition partition = (DirectoryPartition)getPartition(partitionIndex);
        return partition.loading ? 1 : 0;
    }

    @Override
    protected View newView(Context context, int partitionIndex, Cursor cursor,
            int position, ViewGroup parent) {
        DirectoryPartition partition = (DirectoryPartition)getPartition(partitionIndex);
        if (partition.loading) {
            return inflateItemViewLoading(parent);
        } else {
            return inflateItemView(parent);
        }
    }

    @Override
    protected void bindView(View v, int partition, Cursor cursor, int position) {
        DirectoryPartition directoryPartition = (DirectoryPartition)getPartition(partition);
        String directoryType = directoryPartition.directoryType;
        String directoryName = directoryPartition.displayName;
        if (directoryPartition.loading) {
            bindViewLoading(v, directoryType, directoryName);
        } else {
            String displayName = cursor.getString(EmailQuery.NAME);
            String emailAddress = cursor.getString(EmailQuery.ADDRESS);
            
            //change@siso.vinay Gal Photo start
            int imageIndex = cursor.getColumnIndex(EmailQuery1.PROJECTION[EmailQuery1.PICTURE_DATA]);
            if(isEnableGroupSearch != false) {
                int indexSystemId = cursor.getColumnIndex(Groups.SYSTEM_ID);
                
                if( indexSystemId != -1) {
                    emailAddress = "(Group)";
                }
            }
            String pictureData = null;
            //change@siso.vinay Gal Photo end
            
            if (TextUtils.isEmpty(displayName) || TextUtils.equals(displayName, emailAddress)) {
                displayName = emailAddress;
//                emailAddress = emailAddress.trim();
                emailAddress = null;
            }
            
            //change@siso.vinay Gal Photo start
            if( imageIndex != -1){
                pictureData = cursor.getString(imageIndex);
            }
             //change@siso.vinay Gal Photo end
            
//            bindView(v, directoryType, directoryName, displayName, emailAddress);
            bindView(v, directoryType, directoryName, displayName, emailAddress, pictureData);
        }
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    protected boolean isEnabled(int partitionIndex, int position) {
        // The "Searching..." item should not be selectable
        return !isLoading(partitionIndex);
    }

    private boolean isLoading(int partitionIndex) {
        return ((DirectoryPartition)getPartition(partitionIndex)).loading;
    }

    @Override
    public Filter getFilter() {
        return new DefaultPartitionFilter();
    }

    // change@siso.cmouli GAl search show-more start
    private static String BackupOfSearchedString;

    public static String getBackupOfSearchedString() {
        return BackupOfSearchedString;
    }

    public static void setBackupOfSearchedString(String backupOfSearchedString) {
        BackupOfSearchedString = backupOfSearchedString;
    }
    // change@siso.cmouli GAl search show-more end

    /**
     * Handles the result of the initial call, which brings back the list of
     * directories as well as the search results for the local directories.
     */
    protected void onDirectoryLoadFinished(
            CharSequence constraint, Cursor directoryCursor, Cursor defaultPartitionCursor) {
        if (directoryCursor != null) {
            PackageManager packageManager = getContext().getPackageManager();
            DirectoryPartition preferredDirectory = null;
            List<DirectoryPartition> directories = new ArrayList<DirectoryPartition>();
            while (directoryCursor.moveToNext()) {
                long id = directoryCursor.getLong(DirectoryListQuery.ID);

                // Skip the local invisible directory, because the default directory
                // already includes all local results.
                if (id == DIRECTORY_LOCAL_INVISIBLE) {
                    continue;
                }

                DirectoryPartition partition = new DirectoryPartition();
                partition.directoryId = id;
                partition.displayName = directoryCursor.getString(DirectoryListQuery.DISPLAY_NAME);
                partition.accountName = directoryCursor.getString(DirectoryListQuery.ACCOUNT_NAME);
                partition.accountType = directoryCursor.getString(DirectoryListQuery.ACCOUNT_TYPE);
                partition.moreResultIsRemained = false;
                String packageName = directoryCursor.getString(DirectoryListQuery.PACKAGE_NAME);
                int resourceId = directoryCursor.getInt(DirectoryListQuery.TYPE_RESOURCE_ID);
                if (packageName != null && resourceId != 0) {
                    try {
                        Resources resources =
                                packageManager.getResourcesForApplication(packageName);
                        partition.directoryType = resources.getString(resourceId);
                        if (partition.directoryType == null) {
                            EmailLog.e(TAG, "Cannot resolve directory name: "
                                    + resourceId + "@" + packageName);
                        }
                    } catch (NameNotFoundException e) {
                        EmailLog.e(TAG, "Cannot resolve directory name: "
                                + resourceId + "@" + packageName, e);
                    }
                }

                // If an account has been provided and we found a directory that
                // corresponds to that account, place that directory second, directly
                // underneath the local contacts.
                if (mAccount != null && mAccount.name.equals(partition.accountName) &&
                        mAccount.type.equals(partition.accountType)) {
                    preferredDirectory = partition;
                } else {
                    directories.add(partition);
                }
            }

            if (preferredDirectory != null) {
                directories.add(1, preferredDirectory);
            }

            for (DirectoryPartition partition : directories) {
                addPartition(partition);
            }
        }

        int count = getPartitionCount();
        int limit = 0;

        // Since we will be changing several partitions at once, hold the data change
        // notifications
        setNotificationsEnabled(false);
        try {
            // The filter has loaded results for the default partition too.
            if (defaultPartitionCursor != null && getPartitionCount() > 0) {
                changeCursor(0, defaultPartitionCursor);
            }

            int defaultPartitionCount = (defaultPartitionCursor == null ? 0
                    : defaultPartitionCursor.getCount());

            limit = mPreferredMaxResultCount - defaultPartitionCount;

            // Show non-default directories as "loading"
            // Note: skipping the default partition (index 0), which has already been loaded
            for (int i = 1; i < count; i++) {
                DirectoryPartition partition = (DirectoryPartition) getPartition(i);
                partition.constraint = constraint;

                if (limit > 0 || ExpandResultMaxAndShowMoreMode) {
                    if (!partition.loading) {
                        partition.loading = true;
                        changeCursor(i, null);
                    }
                } else {
                    partition.loading = false;
                    changeCursor(i, null);
                }
            }
        } finally {
            setNotificationsEnabled(true);
        }

        // Start search in other directories
        // Note: skipping the default partition (index 0), which has already been loaded
        for (int i = 1; i < count; i++) {
            DirectoryPartition partition = (DirectoryPartition) getPartition(i);
            
            // Disable online search
            if (isOnlineSearchDisabled && partition.accountType != null
                    && partition.accountType.equalsIgnoreCase(AccountManagerTypes.TYPE_EXCHANGE)) {
                continue;
            }

            if (partition.loading) {
                mHandler.removeMessages(MESSAGE_SEARCH_PENDING, partition);
                Message msg = mHandler.obtainMessage(MESSAGE_SEARCH_PENDING, i, 0, partition);
                mHandler.sendMessageDelayed(msg, MESSAGE_SEARCH_PENDING_DELAY);
                if (partition.filter == null) {
                    partition.filter = new DirectoryPartitionFilter(i, partition.directoryId);
                }
                if(ExpandResultMaxAndShowMoreMode)
                {
                    partition.filter.setLimit(MAX_DIRECTORYPARTITIONFILTER_RESULT);
                }
                else
                {
                    partition.filter.setLimit(limit);
                }
                partition.filter.filter(constraint);
            } else {
                if (partition.filter != null) {
                    // Cancel any previous loading request
                    partition.filter.filter(null);
                }
            }
        }
    }

    public void doGalSearch(int partitionIndex) {
        if(partitionIndex > 0)
        {
        	DirectoryPartition partition = (DirectoryPartition) getPartition(partitionIndex);
        	clearPartitions();
        	partition.filter.setLimit(partition.filter.getLimit()+ MAX_DIRECTORYPARTITIONFILTER_RESULT);
        	partition.loading = true;
        	partition.constraint = BackupOfSearchedString;
        	partition.filter.filter(BackupOfSearchedString);
        	showSearchPendingIfNotComplete(partitionIndex);
        }
        else
        {
            clearPartitions();
            
        	int count = getPartitionCount();
        	for (int i = 0; i < count; i++) {
                DirectoryPartition partition = (DirectoryPartition) getPartition(i);
                partition.moreResultIsRemained = false;
            }
        	for (int i = 0; i < count; i++) {
                DirectoryPartition partition = (DirectoryPartition) getPartition(i);
                if(partition.accountType != null && (partition.accountType.equalsIgnoreCase(AccountManagerTypes.TYPE_EXCHANGE) || partition.accountType.equalsIgnoreCase(AccountManagerTypes.TYPE_POP_IMAP) || partition.accountType.equalsIgnoreCase(AccountManagerTypes.TYPE_NAUTA)))
                {
                  partition.filter.setLimit(partition.filter.getLimit()+ MAX_DIRECTORYPARTITIONFILTER_RESULT);
                  partition.loading = true;
                  partition.constraint = BackupOfSearchedString;
                  partition.filter.filter(BackupOfSearchedString);
                  partition.moreResultIsRemained = false;
                  showSearchPendingIfNotComplete(i);
                }           
        	}
        }
    }

    void showSearchPendingIfNotComplete(int partitionIndex) {
        if (partitionIndex < getPartitionCount()) {
            DirectoryPartition partition = (DirectoryPartition) getPartition(partitionIndex);
            if (partition.loading) {
                changeCursor(partitionIndex, createLoadingCursor());
            }
        }
    }

    /**
     * Creates a dummy cursor to represent the "Searching directory..." item.
     */
    private Cursor createLoadingCursor() {
        MatrixCursor cursor = new MatrixCursor(new String[]{SEARCHING_CURSOR_MARKER});
        cursor.addRow(new Object[]{""});
        return cursor;
    }

    public void onPartitionLoadFinished(
            CharSequence constraint, int partitionIndex, Cursor cursor) {
        if (partitionIndex < getPartitionCount()) {
            DirectoryPartition partition = (DirectoryPartition) getPartition(partitionIndex);

            // Check if the received result matches the current constraint
            // If not - the user must have continued typing after the request
            // was issued
            if (partition.loading && TextUtils.equals(constraint, partition.constraint)) {
//                partition.loading = false;
                mHandler.removeMessages(MESSAGE_SEARCH_PENDING, partition);
                if (null != constraint)
                	setBackupOfSearchedString(constraint.toString());
                changeCursor(partitionIndex, removeDuplicatesAndTruncate(partitionIndex, cursor));
                partition.loading = false;
                
                if(ExpandResultMaxAndShowMoreMode)
                {
                    synchronized (mSynchronizer) {
                        if(isAllPartitionLoadFinished() && isAllPartitionEmpty()==false)
                        {
                            addShowMoreButtonAtlastPatition();
                        }
                    }
                }
            } else {
                // We got the result for an unexpected query (the user is still typing)
                // Just ignore this result
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if (cursor != null) {
            cursor.close();
        }
    }

    /**
     * Post-process the cursor to eliminate duplicates.  Closes the original cursor
     * and returns a new one.
     */
    private Cursor removeDuplicatesAndTruncate(int partition, Cursor cursor) {
//        MatrixCursor newCursor = new MatrixCursor(EmailQuery.PROJECTION);
        MatrixCursor newCursor = new MatrixCursor(EmailQuery1.PROJECTION);
        
        DirectoryPartition curpartition = (DirectoryPartition) getPartition(partition);
        curpartition.moreResultIsRemained = false;
        
        if (cursor == null) {
            EmailLog.d(TAG, "addShowMoreButtonAtlastPatition : cursor is null");
            if(newCursor != null && !newCursor.isClosed())
                newCursor.close();
            return null;
        }

        if (ExpandResultMaxAndShowMoreMode == false
                && cursor.getCount() <= DEFAULT_PREFERRED_MAX_RESULT_COUNT
                && !hasDuplicates(cursor, partition)) {
            EmailLog.d(TAG, "addShowMoreButtonAtlastPatition : this partition has no duplication.");
            if(newCursor != null && !newCursor.isClosed())
                newCursor.close();
            return cursor;
        }

        int count = 0;
        cursor.moveToPosition(-1);
        
        while (cursor.moveToNext() && (ExpandResultMaxAndShowMoreMode || count < DEFAULT_PREFERRED_MAX_RESULT_COUNT)) {
            String displayName = cursor.getString(EmailQuery.NAME);
            String emailAddress = cursor.getString(EmailQuery.ADDRESS);
            
            //change@siso.vinay Gal Photo start
            int imageIndex = cursor.getColumnIndex(EmailQuery1.PROJECTION[EmailQuery1.PICTURE_DATA]);
            String pictureData = null;
            //change@siso.vinay Gal Photo end
            
            if(EXCHANGE_GAL_SHOW_MORE.equalsIgnoreCase(displayName)){
                curpartition.moreResultIsRemained = true;
                continue;
            }

            //change@siso.vinay Gal Photo start
            if( imageIndex != -1){
                pictureData = cursor.getString(imageIndex);
            }
            //change@siso.vinay Gal Photo end
            
            if (!isDuplicateAddPictureDate(emailAddress, partition, pictureData)) {
                newCursor.addRow(new Object[]{displayName, emailAddress, pictureData});
                count++;
            }
        }
        
        cursor.close();

        return newCursor;
    }
    
    private void addShowMoreButtonAtlastPatition() { 
        int totalPatitionCount = getPartitionCount();
        
        if(totalPatitionCount <= 1) {
            EmailLog.d(TAG, "addShowMoreButtonAtlastPatition : partition is none");
            return;
        }
        
        int partionindex = totalPatitionCount -1;

        boolean enbleShowMore = false;
        for (int i = 1; i < totalPatitionCount; i++) {
            DirectoryPartition partition = (DirectoryPartition) getPartition(i);
            if(partition != null)
            {
                Cursor curCursor = getCursor(i);
                if(curCursor != null && curCursor.getCount()>=0)
                {
                    EmailLog.d(TAG, "addShowMoreButtonAtlastPatition : cursor is not null. + (" + i + ")");
                    
                    if(partition.moreResultIsRemained == true){
                        EmailLog.d(TAG, "addShowMoreButtonAtlastPatition : showmore does not need. + (" + i + ")");
                        enbleShowMore = true;
                        continue;
                    }
                }                
            }
        }
        
        Cursor lastCursor = getCursor(partionindex);                
//        MatrixCursor newCursor = new MatrixCursor(EmailQuery.PROJECTION);        
        MatrixCursor newCursor = new MatrixCursor(EmailQuery1.PROJECTION);
        if(lastCursor != null)
        {
            lastCursor.moveToPosition(-1);
            while (lastCursor.moveToNext()) {
//                if (lastCursor.getColumnName(0).equals(SEARCHING_CURSOR_MARKER)) {
//                    break;
//                }
                String displayName = lastCursor.getString(EmailQuery.NAME);
                String emailAddress = lastCursor.getString(EmailQuery.ADDRESS);
                
                //change@siso.vinay Gal Photo start
                int imageIndex = lastCursor.getColumnIndex(EmailQuery1.PROJECTION[EmailQuery1.PICTURE_DATA]);
                String pictureData = null;
                //change@siso.vinay Gal Photo end
                
                if(displayName != null && displayName.equalsIgnoreCase(EXCHANGE_GAL_SHOW_MORE)) {
                    continue;
                }
                
                //change@siso.vinay Gal Photo start
                if( imageIndex != -1){
                    pictureData = lastCursor.getString(imageIndex);
                }
                //change@siso.vinay Gal Photo end
                
//                newCursor.addRow(new Object[]{displayName, emailAddress});
                newCursor.addRow(new Object[]{displayName, emailAddress, pictureData});
            }            
        }
        
//        int id = Resources.getSystem().getIdentifier("gal_search_show_more", "string", "android");
//        newCursor.addRow(new Object[]{Resources.getSystem().getString(id), "" + partionindex});
        
        if(enbleShowMore) {
//            newCursor.addRow(new Object[]{EXCHANGE_GAL_SHOW_MORE, "" + partionindex});
//            newCursor.addRow(new Object[]{EXCHANGE_GAL_SHOW_MORE, "" + -1});
            newCursor.addRow(new Object[]{EXCHANGE_GAL_SHOW_MORE, "" + -1, EXCHANGE_GAL_SHOW_MORE});
        }else {
            EmailLog.d(TAG, "addShowMoreButtonAtlastPatition : showmore does not need.");
        }
        
        changeCursor(partionindex, newCursor);
        
        return;
    }
    
    private boolean  isAllPartitionLoadFinished()
    {
        int totalCount = getPartitionCount();
        
        for(int i=1; i<totalCount; i++)
        {
            DirectoryPartition curpartition = (DirectoryPartition)getPartition(i);
            if(curpartition.loading)
            {
                return false;
            }
        }
        
        return true;
    }
    
    private boolean isAllPartitionEmpty()
    {
        int totalCount = getPartitionCount();
        
        for(int i=0; i<totalCount; i++)
        {
            if(isPartitionEmpty(i) == false)
            {
                return false;
            }
        }
        
        return true;
    }

    private boolean hasDuplicates(Cursor cursor, int partition) {
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            String emailAddress = cursor.getString(EmailQuery.ADDRESS);
            if (isDuplicate(emailAddress, partition)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the supplied email address is already present in partitions other
     * than the supplied one.
     */    
    private boolean isDuplicate(String emailAddress, int excludePartition) {
        int partitionCount = getPartitionCount();
        
        for (int partition = 0; partition < partitionCount; partition++) {
            if (partition != excludePartition && !isLoading(partition)) {
                Cursor cursor = getCursor(partition);
                if (cursor != null) {
                    cursor.moveToPosition(-1);
                    while (cursor.moveToNext()) {
                        String address = cursor.getString(EmailQuery.ADDRESS);
                        if (TextUtils.equals(emailAddress, address)) {
                            return true;
                        }
                    }
                }
            }            
        }

        return false;
    }
    
    private boolean isDuplicateAddPictureDate(String emailAddress, int excludePartition, String pictureData) {
        int partitionCount = getPartitionCount();
        MatrixCursor newCursor = new MatrixCursor(EmailQuery1.PROJECTION); //fillout PictureDate from GAL search
        boolean defaultPatitionCursorChanged = false;
        boolean nowFounded = false;
        boolean isduplicated = false;
        String curdisplayName = null;
        String curemailAddress = null;
        int imageIndex = -1;
        String curpictureData = null;
        
        for (int partition = 0; partition < partitionCount; partition++) {
            if (partition != excludePartition && !isLoading(partition)) {
                Cursor cursor = getCursor(partition);
                if (cursor != null) {
                    cursor.moveToPosition(-1);
                    while (cursor.moveToNext()) {
                        String address = cursor.getString(EmailQuery.ADDRESS);
                        if(partition == 0)
                        {
                            curdisplayName = cursor.getString(EmailQuery.NAME);
                            curemailAddress = cursor.getString(EmailQuery.ADDRESS);
                            imageIndex = cursor.getColumnIndex(EmailQuery1.PROJECTION[EmailQuery1.PICTURE_DATA]);                            
                            
                            curpictureData = null;
                            if(imageIndex >= 0) {
                                curpictureData = cursor.getString(imageIndex);
                            }
                        }
                        
                        nowFounded = false;
                        if (TextUtils.equals(emailAddress, address)) {
                            isduplicated = true;                            
                            nowFounded = true;
                            
                            if(partition != 0) {
                                break;
                            }
                        }
                        
                        if(partition == 0) {
                            if(nowFounded && curpictureData == null){                                  
                                newCursor.addRow(new Object[]{curdisplayName, curemailAddress, pictureData});
                                defaultPatitionCursorChanged = true;
                            } else {
                                newCursor.addRow(new Object[]{curdisplayName, curemailAddress, curpictureData});                                
                            }
                        }
                    } //while end
                } // if end
            } // if end
            
            if(isduplicated == true){
                break;
            }
            
        } // for end
        
        if(defaultPatitionCursorChanged) {
            changeCursor(0, newCursor);
        }
        
        if(newCursor != null && !newCursor.isClosed())
            newCursor.close();

        return isduplicated;
    }

    private String makeDisplayString(Cursor cursor) {
        if (cursor.getColumnName(0).equals(SEARCHING_CURSOR_MARKER)) {
            return "";
        }

        String displayName = cursor.getString(EmailQuery.NAME);
        String emailAddress = cursor.getString(EmailQuery.ADDRESS);
        if (TextUtils.isEmpty(displayName) || TextUtils.equals(displayName, emailAddress)) {
             return emailAddress;
        } else {
            return new Rfc822Token(displayName, emailAddress, null).toString();
        }
    }
    
	public Cursor getGroupNameCursor(CharSequence constraint) {
		Cursor groupCursor = null;
		String inputText;
		
		if (constraint != null) {
            inputText = constraint.toString();
        } else 
        	inputText = "";
		
		groupCursor = mContentResolver.query(Groups.CONTENT_URI, new String[] {Groups.TITLE, Groups.ACCOUNT_TYPE, 
																	Groups.SYSTEM_ID}, 
        		"(" + Groups.TITLE + " like '" + inputText + "%%'" + ")"
        		, null, null);
		
		return groupCursor;
	}

}
