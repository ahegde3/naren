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

package com.samsung.android.emailcommon.adapter;

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
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Directory;
import android.provider.ContactsContract.Groups;
import android.provider.ContactsContract.RawContacts;
import android.text.TextUtils;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.android.common.widget.CompositeCursorAdapter;
import com.samsung.android.emailcommon.AccountManagerTypes;
import com.samsung.android.emailcommon.EmailFeature;
import com.samsung.android.emailcommon.EmailPackage;
import com.samsung.android.emailcommon.mail.Address;
import com.samsung.android.emailcommon.provider.EmailContent;
import com.samsung.android.emailcommon.utility.EmailLog;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * A base class for email address autocomplete adapters. It uses
 * {@link Email#CONTENT_FILTER_URI} to search for data rows by email address
 * and/or contact name. It also searches registered {@link Directory}'s.
 */
public abstract class SecBaseEmailAddressAdapter extends CompositeCursorAdapter implements Filterable {

    private static final String TAG = "SecBaseEmailAddressAdapter";

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

    private Long Acc_Id = (long) -1;
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
    public static final String GAL_PICTURE_DATA = "pictureData";
    //change@siso.vinay Gal Photo end

    public static final String CACHE_PICTURE_DATA = "cachedpictureData";

    public static boolean ExpandResultMaxAndShowMoreMode = false;
    public static boolean isEnableGroupSearch = false;

    // Disable online search
    public boolean isOnlineSearchDisabled = false;

    protected final Object mSynchronizer = new Object();
    public static final String EXCHANGE_GAL_SHOW_MORE = "gal_search_show_more";
    public static final String RESULT_NOMATCHES = "NoMatches";

    /* Black list function */
    private boolean mIsEnableFilteringBlackList = false;
    private OnFilteringBlackListInterfaceListener mOnFilteringBlackListInterfaceListener = null;
    protected class TagForBlackList {
        public int mPartition;
        public int mPosition;
        public String mEmailAddress;

        public TagForBlackList(int partition, int position, String emailAddress) {
            mPartition = partition;
            mPosition = position;
            mEmailAddress = emailAddress;
        }
    }
    private HashSet<String> mHashBlackList = new HashSet<String>() {
        @Override
        public String toString() {
            StringBuilder buffer = new StringBuilder();
            Iterator<?> it = this.iterator();
            while (it.hasNext()) {
                Object next = it.next();
                if (next != this)
                    buffer.append(next);
                if (it.hasNext()) {
                    buffer.append(", ");
                }
            }

            return buffer.toString();
        }

        @Override
        public boolean add(String object) {
            if (object == null || object.isEmpty())
                return false;

            boolean temp = super.add(object);
            EmailLog.logd(TAG, "Hiden hash set add data : " + object);
            return temp;
        }
    };
    private String mNoMatches = null;

    public boolean addBlackList(Address address) {
        if (address == null) return false;
        boolean temp = true;
        String addr = address.getAddress();
        if (addr != null && !addr.isEmpty()) {
            if (!mHashBlackList.contains(addr)) {
                temp &= mHashBlackList.add(addr);
            }
        }
        EmailLog.logd(TAG, "addBlackList() black list : " + mHashBlackList.toString());
        return temp;
    }

    public boolean addBlackList(Address[] addresses) {
        if (addresses == null) return false;
        boolean temp = true;
        for (Address addr : addresses) {
            temp &= addBlackList(addr);
        }
        return temp;
    }

    public boolean removeBlackList(Address address) {
        if (address == null) return false;
        boolean temp = true;
        String addr = address.getAddress();
        if (mHashBlackList.contains(addr)) {
            temp &= mHashBlackList.remove(addr);
        }
        return temp;
    }

    public boolean removeBlackList(Address[] addresses) {
        if (addresses == null) return false;
        boolean temp = true;
        for (Address addr : addresses) {
            temp &= removeBlackList(addr);
        }
        return temp;
    }

    protected boolean addBlackListByClickEvent(Address address) {
        if (mOnFilteringBlackListInterfaceListener != null) {
            mOnFilteringBlackListInterfaceListener.onNotifyAddBlackList(address);
        }
        return addBlackList(address);
    }

    public String getBlackList() {
        ArrayList<Address> arrayList = new ArrayList<>();
        for (String address : mHashBlackList.toArray(new String[0])) {
            arrayList.add(new Address(address));
        }
        return Address.pack(arrayList.toArray(new Address[0]));
    }

    public void changeCursorExceptAddress(int partition, Address[] exceptAddresses) {
        Cursor cursor = getCursor(partition);

        if (cursor == null || exceptAddresses == null || exceptAddresses.length <= 0) return;

        cursor = getCursorExceptAddresses(cursor, exceptAddresses);

        changeCursor(partition, cursor);
    }

    public Cursor getCursorExceptAddresses(Cursor cursor, Address[] exceptAddresses) {
        EmailLog.d(TAG, "getCursorExceptAddresses start");
        if (cursor == null || exceptAddresses == null) return cursor;

        if (exceptAddresses.length <= 0) {
            return cursor;
        } else {
            HashSet<String> exceptHashSet = new HashSet<>();

            for (Address addr : exceptAddresses) {
                if (!TextUtils.isEmpty(addr.getAddress()))
                    exceptHashSet.add(addr.getAddress());
            }

            boolean isContainExceptAddress = false;


            MatrixCursor groupCursor = new MatrixCursor(EmailQuery_Group.PROJECTION);
            MatrixCursor emailQueryCursor = new MatrixCursor(EmailQuery.PROJECTION);
            MatrixCursor emailQuery1Cursor = new MatrixCursor(EmailQuery1.PROJECTION);
            MatrixCursor emailQuery2Cursor = new MatrixCursor(EmailQuery2.PROJECTION);
            MatrixCursor emailQuery3Cursor = new MatrixCursor(EmailQuery2.PROJECTION);

            try {
                cursor.moveToPosition(-1);
                while (cursor.moveToNext()) {
                    int cursorType =
                            cursor.getColumnIndex(EmailQuery_Group.PROJECTION[EmailQuery_Group.ACCOUNT_TYPE]) >= 0 ? 1 :
                                    cursor.getColumnIndex(EmailQuery1.PROJECTION[EmailQuery1.PICTURE_DATA]) >= 0 ? 2 :
                                            cursor.getColumnIndex(EmailQuery2.PROJECTION[EmailQuery2.PHOTO_THUMBNAIL_URI]) >= 0 ? 3 :
                                                    cursor.getColumnIndex(EmailQuery3.PROJECTION[EmailQuery3.CACHED_PICTURE_DATA]) >= 0 ? 4 : 0;
                    if (exceptHashSet.contains(cursor.getString(EmailQuery.ADDRESS))) {
                        isContainExceptAddress = true;
                        continue;
                    }
                    MatrixCursor.RowBuilder b = null;
                    String[] columns = null;
                    switch (cursorType) {
                        case 0:
                            b = emailQueryCursor.newRow();
                            columns = EmailQuery.PROJECTION;
                            break;
                        case 1:
                            b = groupCursor.newRow();
                            columns = EmailQuery_Group.PROJECTION;
                            break;
                        case 2:
                            b = emailQuery1Cursor.newRow();
                            columns = EmailQuery1.PROJECTION;
                            break;
                        case 3:
                            b = emailQuery2Cursor.newRow();
                            columns = EmailQuery2.PROJECTION;
                            break;
                        case 4:
                            b = emailQuery3Cursor.newRow();
                            columns = EmailQuery3.PROJECTION;
                            break;
                    }
                    for (String col : columns) {
                        int index = cursor.getColumnIndex(col);
                        if (index >= 0) {
                            int type = cursor.getType(index);
                            switch (type) {
                                case Cursor.FIELD_TYPE_NULL:
                                    b.add(null);
                                    break;
                                case Cursor.FIELD_TYPE_INTEGER:
                                    b.add(cursor.getInt(index));
                                    break;
                                case Cursor.FIELD_TYPE_FLOAT:
                                    b.add(cursor.getFloat(index));
                                    break;
                                case Cursor.FIELD_TYPE_STRING:
                                    b.add(cursor.getString(index));
                                    break;
                                case Cursor.FIELD_TYPE_BLOB:
                                    b.add(cursor.getBlob(index));
                                    break;
                            }
                        } else {
                            b.add(null);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                EmailLog.e(TAG, "changeCursorForRemoveAddress isContainExceptAddress : " + isContainExceptAddress);
                if (!isContainExceptAddress) {
                    groupCursor.close();
                    emailQueryCursor.close();
                    emailQuery1Cursor.close();
                    emailQuery2Cursor.close();
                    emailQuery3Cursor.close();
                    groupCursor = null;
                    emailQueryCursor = null;
                    emailQuery1Cursor = null;
                    emailQuery2Cursor = null;
                    emailQuery3Cursor = null;
                } else {
                    cursor.close();
                    cursor = null;
                }
            }

            if (cursor != null) {
                return cursor;
            } else {
                return new MergeCursor(new Cursor[]{groupCursor,
                        emailQueryCursor,
                        emailQuery1Cursor,
                        emailQuery2Cursor,
                        emailQuery3Cursor});
            }
        }
    }

    public void setOntFilteringBlackLisInterfaceListener(OnFilteringBlackListInterfaceListener listener) {
        if (listener == null)
            return;

        mIsEnableFilteringBlackList = true;
        mOnFilteringBlackListInterfaceListener = listener;
        return;
    }

    public interface OnFilteringBlackListInterfaceListener {
        public void onNotifyAddBlackList(Address address);
    }
    /* Black list function */

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
        public boolean hasHiddenItem;

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

    private static class EmailQuery1 {
        public static final String[] PROJECTION = {
                Contacts.DISPLAY_NAME,  // 0
                Email.DATA              // 1
                , GAL_PICTURE_DATA       // 2
        };

        public static final int NAME = 0;
        public static final int ADDRESS = 1;
        public static final int PICTURE_DATA = 2;
    }

    private static class EmailQuery1_1 {
        public static final String[] PROJECTION = {
                Contacts.DISPLAY_NAME,
                Email.DATA,
                GAL_PICTURE_DATA,
                GAL_DISPLAY_NAME,
                GAL_EMAIL_ADDRESS,
                GAL_WORK_PHONE,
                GAL_HOME_PHONE,
                GAL_MOBILE_PHONE,
                GAL_FIRST_NAME,
                GAL_LAST_NAME,
                GAL_COMPANY,
                GAL_TITLE,
                GAL_OFFICE,
                GAL_ALIAS
        };

        public static final int NAME = 0;
        public static final int ADDRESS = 1;
        public static final int PICTURE_DATA = 2;
        public static final int DISPLAY_NAME = 3;
        public static final int EMAIL_ADDRESS = 4;
        public static final int WORK_PHONE = 5;
        public static final int HOME_PHONE = 6;
        public static final int MOBILE_PHONE = 7;
        public static final int FIRST_NAME = 8;
        public static final int LAST_NAME = 9;
        public static final int COMPANY = 10;
        public static final int TITLE = 11;
        public static final int OFFICE = 12;
        public static final int ALIAS = 13;
    }

    private static class EmailQuery2 {
        public static final String[] PROJECTION = {
                Contacts.DISPLAY_NAME,  // 0
                Email.DATA,             // 1
                Contacts.PHOTO_THUMBNAIL_URI //2
        };

        public static final int NAME = 0;
        public static final int ADDRESS = 1;
        public static final int PHOTO_THUMBNAIL_URI = 2;
    }

    private static class EmailQuery2_1 {
        public static final String[] PROJECTION = {
                Contacts.DISPLAY_NAME,
                Email.DATA,
                Contacts.PHOTO_THUMBNAIL_URI,
                GAL_PICTURE_DATA,
                GAL_DISPLAY_NAME,
                GAL_EMAIL_ADDRESS,
                GAL_WORK_PHONE,
                GAL_HOME_PHONE,
                GAL_MOBILE_PHONE,
                GAL_FIRST_NAME,
                GAL_LAST_NAME,
                GAL_COMPANY,
                GAL_TITLE,
                GAL_OFFICE,
                GAL_ALIAS
        };

        public static final int NAME = 0;
        public static final int ADDRESS = 1;
        public static final int PHOTO_THUMBNAIL_URI = 2;
        public static final int PICTURE_DATA = 3;
        public static final int DISPLAY_NAME = 4;
        public static final int EMAIL_ADDRESS = 5;
        public static final int WORK_PHONE = 6;
        public static final int HOME_PHONE = 7;
        public static final int MOBILE_PHONE = 8;
        public static final int FIRST_NAME = 9;
        public static final int LAST_NAME = 10;
        public static final int COMPANY = 11;
        public static final int TITLE = 12;
        public static final int OFFICE = 13;
        public static final int ALIAS = 14;
    }

    private static class EmailQuery3 {
        public static final String[] PROJECTION = {
                Contacts.DISPLAY_NAME,  // 0
                Email.DATA              // 1
                , CACHE_PICTURE_DATA       // 2
        };

        public static final int NAME = 0;
        public static final int ADDRESS = 1;
        public static final int CACHED_PICTURE_DATA = 2;
    }

    private static class EmailQuery3_1 {
        public static final String[] PROJECTION = {
                Contacts.DISPLAY_NAME,
                Email.DATA,
                CACHE_PICTURE_DATA,
                GAL_PICTURE_DATA,
                GAL_DISPLAY_NAME,
                GAL_EMAIL_ADDRESS,
                GAL_WORK_PHONE,
                GAL_HOME_PHONE,
                GAL_MOBILE_PHONE,
                GAL_FIRST_NAME,
                GAL_LAST_NAME,
                GAL_COMPANY,
                GAL_TITLE,
                GAL_OFFICE,
                GAL_ALIAS
        };

        public static final int NAME = 0;
        public static final int ADDRESS = 1;
        public static final int CACHED_PICTURE_DATA = 2;
        public static final int PICTURE_DATA = 3;
        public static final int DISPLAY_NAME = 4;
        public static final int EMAIL_ADDRESS = 5;
        public static final int WORK_PHONE = 6;
        public static final int HOME_PHONE = 7;
        public static final int MOBILE_PHONE = 8;
        public static final int FIRST_NAME = 9;
        public static final int LAST_NAME = 10;
        public static final int COMPANY = 11;
        public static final int TITLE = 12;
        public static final int OFFICE = 13;
        public static final int ALIAS = 14;
    }

    private static class EmailQuery_Group {
        public static final String[] PROJECTION = {
                Groups.TITLE,  // 0
                Groups.ACCOUNT_TYPE              // 1
                , Groups.SYSTEM_ID       // 2
        };

        public static final int TITLE = 0;
        public static final int ACCOUNT_TYPE = 1;
        public static final int SYSTEM_ID = 2;
    }

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

    public static class SearchResult {
        public String displayName = null;
        public String emailAddress = null;

        public String pictureData = null;
        public String thumbnailUriAsString = null;

        public String displayname = null;
        public String emailaddress = null;
        public String workphone = null;
        public String homephone = null;
        public String mobilephone = null;
        public String firstname = null;
        public String lastname = null;
        public String company = null;
        public String title = null;
        public String office = null;
        public String alias = null;
    }

//    protected ArrayList<SearchResult> mSearchResultCacheArray = new ArrayList<SearchResult>();

    /**
     * A fake column name that indicates a "Searching..." item in the list.
     */
    private static final String SEARCHING_CURSOR_MARKER = "searching";

    private String mInputText = "";

    public static boolean removeEmailaddressCache(Context ctx, String addr) {
        if (ctx == null || addr == null || addr.length() <= 0) {
            return false;
        }

        final ContentResolver resolver = ctx.getContentResolver();
        try {
            resolver.delete(EmailContent.RecipientInformationCache.CONTENT_URI,
                    EmailContent.RecipientInformationCache.EMAILADDRESS + " LIKE ?", new String[]{
                            addr
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            resolver.delete(EmailContent.EmailAddressCache.CONTENT_URI,
                    EmailContent.EmailAddressCache.EMAIL_ADDRESS + " LIKE ?", new String[]{
                            addr
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * An asynchronous filter used for loading two data sets: email rows from the local
     * contact provider and the list of {@link Directory}'s.
     */
    private final class DefaultPartitionFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Cursor recentCursor = null;
            MatrixCursor reducedRICCursor = null;
            MatrixCursor reducedRecentEmailCursor = null;

            MergeCursor mergeResultCursor = null;
            FilterResults results = new FilterResults();
            Cursor cursor = null;
            Cursor groupCursor = null;
            Cursor rICursor1 = null;
            if (!TextUtils.isEmpty(constraint)) {
                mInputText = constraint.toString();
                Uri uri = null;
                Uri.Builder builder = null;
                String endoceInputText = "";
                try {
                    endoceInputText = URLEncoder.encode(mInputText, "utf-8");
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }

                if (ExpandResultMaxAndShowMoreMode) {
                    builder = Email.CONTENT_FILTER_URI.buildUpon()
                            .appendEncodedPath(endoceInputText);
                } else {
                    builder = Email.CONTENT_FILTER_URI.buildUpon()
                            .appendEncodedPath(endoceInputText)
                            .appendQueryParameter(LIMIT_PARAM_KEY,
                                    String.valueOf(mPreferredMaxResultCount));
                }
                if (mAccount != null) {
                    builder.appendQueryParameter(PRIMARY_ACCOUNT_NAME, mAccount.name);
                    builder.appendQueryParameter(PRIMARY_ACCOUNT_TYPE, mAccount.type);
                }
                uri = builder.build();
                if (isEnableGroupSearch != false)
                    groupCursor = getGroupNameCursor(constraint);

                if (ExpandResultMaxAndShowMoreMode) {
                    cursor = mContentResolver.query(uri, EmailQuery2.PROJECTION, null, null, null);
                    if (mIsEnableFilteringBlackList == true)
                        cursor = getCursorExceptAddresses(cursor, Address.unpack(getBlackList()));
                    EmailLog.logd(TAG, "EmailQuery2 uri = " + uri.toString());
                } else {
                    cursor = mContentResolver.query(uri, EmailQuery.PROJECTION, null, null, null);
                    if (mIsEnableFilteringBlackList == true)
                        cursor = getCursorExceptAddresses(cursor, Address.unpack(getBlackList()));
                    EmailLog.logd(TAG, "EmailQuery uri = " + uri.toString());
                }

                // change@siso.saritha Recipient Information cache start
                //disable online search
                if (isOnlineSearchDisabled == false) {
                    rICursor1 = RIemailAddress(Acc_Id, constraint);
                    if (rICursor1 != null && rICursor1.getCount() > 0) {
                        reducedRICCursor = removeDuplicatesEmailQueryCursor(cursor, rICursor1);
                    }
                }

                EmailLog.logd(TAG, "recent-address-cache");
                //taesoo77.lee for adding recent-address-cache
                try {
                    String filter = (TextUtils.isEmpty(constraint)) ? "" : constraint.toString();

                    Uri emailCacheUri = Uri
                            .withAppendedPath(
                                    Uri.parse("content://com.samsung.android.email.provider/emailaddresscache/filter"),
                                    Uri.encode(filter));
                    recentCursor = mContentResolver.query(emailCacheUri, new String[]{
                            "accountName as display_name", "accountAddress as data1",
                            "photocontentbytes as " + CACHE_PICTURE_DATA
                    }, null, null, "usageCount DESC, accountName");
                    EmailLog.logd(TAG, "emailCacheUri uri = " + emailCacheUri.toString());
                } catch (Exception e) {
                    recentCursor = null;
                }
                if (recentCursor != null && recentCursor.getCount() > 0) {
                    reducedRecentEmailCursor = removeDuplicatesEmailQueryCursor(cursor, recentCursor);
                    if (reducedRecentEmailCursor != null && reducedRecentEmailCursor.getCount() > 0) {
                        reducedRecentEmailCursor = removeDuplicatesEmailQueryCursor(reducedRICCursor, reducedRecentEmailCursor);
                    }
                }

//                mSearchResultCacheArray.clear();
            } else {
                mInputText = "";
            }

            mergeResultCursor = new MergeCursor(new Cursor[]{
                    groupCursor, cursor, reducedRICCursor, reducedRecentEmailCursor});

            results.values = new Cursor[]{mergeResultCursor};
            results.count = mergeResultCursor.getCount();
            EmailLog.logd(TAG, "mergeResults count = " + results.count);


            if (recentCursor != null) {
                recentCursor.close();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            String sConstraint = constraint == null ? "" : constraint.toString();
            if (!mInputText.equals(sConstraint)) {
                EmailLog.logd(TAG, "current constraint is different to InputText" + ", constraint : " + sConstraint + ", InputText : " + mInputText);
                if (results.values != null) {
                    Cursor[] cursors = (Cursor[]) results.values;
                    if (cursors[0] != null && !cursors[0].isClosed())
                        cursors[0].close();
                }
                return;
            }

            if (results.values != null) {
                Cursor[] cursors = (Cursor[]) results.values;
                onDirectoryLoadFinished(constraint, cursors[0]);
            }
            results.count = getCount();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return makeDisplayString((Cursor) resultValue);
        }
    }

    private MatrixCursor removeDuplicatesEmailQueryCursor(Cursor baseCursor, Cursor refCursor) {
        if (refCursor != null && refCursor.getCount() <= 0) {
            try {
                refCursor.close();
                refCursor = null;
            } catch (Exception e) {
            }

            return null;
        } else if (refCursor == null) {
            return null;
        }

        MatrixCursor newCursor = null;

        int cacheImageIndex = refCursor.getColumnIndex(EmailQuery3.PROJECTION[EmailQuery3.CACHED_PICTURE_DATA]);

        if (cacheImageIndex < 0) {
            newCursor = new MatrixCursor(EmailQuery.PROJECTION);
        } else {
            newCursor = new MatrixCursor(EmailQuery3.PROJECTION);
        }

        try {
            boolean baseExist = (baseCursor != null && baseCursor.getCount() > 0);

            refCursor.moveToPosition(-1);
            while (refCursor.moveToNext()) {
                String curdisplayName = refCursor.getString(EmailQuery.NAME);
                String curaddress = refCursor.getString(EmailQuery.ADDRESS);
                /* Black list function */
                if (mIsEnableFilteringBlackList && mHashBlackList.contains(curaddress)) {
                    continue;
                }
                /* Black list function */
                byte[] curcontentbytes = null;

                try {
                    if (cacheImageIndex > 0) {
                        curcontentbytes = refCursor.getBlob(EmailQuery3.CACHED_PICTURE_DATA);
                    }
                } catch (Exception e) {
                    curcontentbytes = null;
                }

                boolean remove = false;

                if (curaddress != null && curaddress.length() > 0) {
                    if (baseExist) {
                        baseCursor.moveToPosition(-1);
                        while (baseCursor.moveToNext()) {
                            String baseaddress = baseCursor.getString(EmailQuery.ADDRESS);
                            if (baseaddress != null && baseaddress.equalsIgnoreCase(curaddress)) {
                                remove = true;
                                break;
                            }
                        }
                    }

                    if (remove) {
                        continue;
                    }

                    if (cacheImageIndex < 0) {
                        newCursor.addRow(new Object[]{
                                curdisplayName, curaddress
                        });
                    } else {
                        newCursor.addRow(new Object[]{
                                curdisplayName, curaddress, curcontentbytes
                        });
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (newCursor == null || newCursor.getCount() <= 0) {
                if (newCursor != null) {
                    try {
                        newCursor.close();
                        newCursor = null;
                    } catch (Exception e) {
                    }
                }
            }

            if (refCursor != null) {
                try {
                    refCursor.close();
                    refCursor = null;
                } catch (Exception e) {
                }
            }
        }

        return newCursor;
    }

    public Cursor RIemailAddress(long acckey, CharSequence constraint) {
        String filter = constraint == null ? "" : constraint.toString();
        Cursor ric = null;

        try {
            String inputtext = URLEncoder.encode(filter, "utf-8");
            inputtext = inputtext.replace("-", "%2D");
            Uri uri = RIC_URI.buildUpon().appendPath(Long.toString(acckey)).appendEncodedPath(inputtext).build();
            ric = mContentResolver.query(uri, EmailQuery.PROJECTION, null, null, null);
            EmailLog.logd(TAG, "RIemailAddress constraint = " + constraint + ", uri = " + uri.toString());
        } catch (Exception e) {
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
            boolean hasBlackListItem = false;
            if (!TextUtils.isEmpty(constraint)) {
                Uri uri = null;
                Cursor cursor = null;
                if (ExpandResultMaxAndShowMoreMode) {
                    uri = Email.CONTENT_FILTER_URI.buildUpon()
                            .appendPath(constraint.toString())
                            .appendQueryParameter(DIRECTORY_PARAM_KEY, String.valueOf(mDirectoryId))
                            .appendQueryParameter(LIMIT_PARAM_KEY, String.valueOf(getLimit()))
//                    .appendQueryParameter(NEED_PHOTO_DATA, "1")
                            .build();

                    cursor = mContentResolver.query(
                            uri, EmailQuery1.PROJECTION, START_PARAM_KEY, new String[]{
                                    String.valueOf(getLimit() - SecBaseEmailAddressAdapter.MAX_DIRECTORYPARTITIONFILTER_RESULT)},
                            null);
                    if (cursor != null && mIsEnableFilteringBlackList == true) {
                        int count = cursor.getCount();
                        cursor = getCursorExceptAddresses(cursor, Address.unpack(getBlackList()));

                        hasBlackListItem = (count > cursor.getCount());
                    }

                    EmailLog.logd(TAG, "expandMode = " + ExpandResultMaxAndShowMoreMode
                            + ", uri = " + uri);
                } else {
                    uri = Email.CONTENT_FILTER_URI.buildUpon()
                            .appendPath(constraint.toString())
                            .appendQueryParameter(DIRECTORY_PARAM_KEY, String.valueOf(mDirectoryId))
                            .appendQueryParameter(LIMIT_PARAM_KEY,
                                    String.valueOf(getLimit() + ALLOWANCE_FOR_DUPLICATES))
                            .build();

                    cursor = mContentResolver.query(
                            uri, EmailQuery.PROJECTION, null, null, null);
                    if (cursor != null && mIsEnableFilteringBlackList == true) {
                        int count = cursor.getCount();
                        cursor = getCursorExceptAddresses(cursor, Address.unpack(getBlackList()));

                        hasBlackListItem = (count > cursor.getCount());
                    }

                    EmailLog.logd(TAG, "expandMode = " + ExpandResultMaxAndShowMoreMode
                            + ", uri = " + uri);
                }

                ((DirectoryPartition) getPartition(mPartitionIndex)).hasHiddenItem = hasBlackListItem;
                results.values = cursor;
                EmailLog.logd(TAG, "constraint = " + constraint);
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Cursor cursor = (Cursor) results.values;
            if (!TextUtils.isEmpty(constraint)) {
                onPartitionLoadFinished(constraint, mPartitionIndex, cursor);
                results.count = getCount();
            }
        }
    }

    protected final ContentResolver mContentResolver;
    private Account mAccount;
    private int mPreferredMaxResultCount = 0;
    private Handler mHandler;


    public SecBaseEmailAddressAdapter(Context context, int preferredMaxResultCount) {
        this(context, preferredMaxResultCount, true);
    }

    public SecBaseEmailAddressAdapter(Context context, int preferredMaxResultCount, boolean setDirPartition) {
        super(context);
        mContentResolver = context.getContentResolver();
        mPreferredMaxResultCount = preferredMaxResultCount;

        if (mPreferredMaxResultCount < 0) {
            ExpandResultMaxAndShowMoreMode = true;
        } else {
            ExpandResultMaxAndShowMoreMode = false;
        }

        if (mPreferredMaxResultCount == -2) {
            isEnableGroupSearch = true;
        } else {
            isEnableGroupSearch = false;
        }

        mHandler = new MyHandler(this);

        mPhotoCacheMap = new LruCache<Uri, byte[]>(PHOTO_CACHE_SIZE);

        if (true == setDirPartition) {
            Cursor directoryCursor = null;
            try {
                directoryCursor = mContentResolver.query(
                        DirectoryListQuery.URI, DirectoryListQuery.PROJECTION, null, null, null);
                setDirectoryPartitions(directoryCursor);
            }catch(Exception e) {
                e.printStackTrace();
            } finally {
                if(directoryCursor != null) {
                    directoryCursor.close();
                }
            }
        }
    }

    public void setDirPartitions() {
        Cursor directoryCursor =  null;
        try {
            directoryCursor = mContentResolver.query(
                    DirectoryListQuery.URI, DirectoryListQuery.PROJECTION, null, null, null);
            setDirectoryPartitions(directoryCursor);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(directoryCursor != null) {
                directoryCursor.close();
            }
        }
    }

    public static class MyHandler extends Handler {

        WeakReference<SecBaseEmailAddressAdapter> viewHelper;

        public MyHandler(SecBaseEmailAddressAdapter view) {
            viewHelper = new WeakReference<SecBaseEmailAddressAdapter>(
                    view);
        }

        @Override
        public void handleMessage(Message msg) {
            if (viewHelper != null) {
                SecBaseEmailAddressAdapter service = viewHelper.get();
                if (service != null) {
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
    public void setAccount(Account account, Long AccId) {
        mAccount = account;
        Acc_Id = AccId;
    }

    //disable online search
    public void disableOnlineSearch(boolean disable) {
        isOnlineSearchDisabled = disable;
    }

    private void setDirectoryPartitions(Cursor directoryCursor) {
        try {
            if (directoryCursor != null) {
                PackageManager packageManager = getContext().getPackageManager();
                DirectoryPartition preferredDirectory = null;
                List<DirectoryPartition> directories = new ArrayList<DirectoryPartition>();

                ArrayList<Long> arrayDirectoryId = new ArrayList<>();

                for (int i = 0; i < getPartitionCount(); i++) {
                    arrayDirectoryId.add(((DirectoryPartition) getPartition(i)).directoryId);
                }
                while (directoryCursor.moveToNext()) {
                    long id = directoryCursor.getLong(DirectoryListQuery.ID);

                    // Skip the local invisible directory, because the default directory
                    // already includes all local results.
                    if (id == DIRECTORY_LOCAL_INVISIBLE || arrayDirectoryId.contains(id)) {
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
                    if (1 <= directories.size())
                        directories.add(1, preferredDirectory);
                    else
                        directories.add(preferredDirectory);
                }

                for (DirectoryPartition partition : directories) {
                    addPartition(partition);
                }
            }
        } finally {
            if (directoryCursor != null) {
                directoryCursor.close();
            }
        }
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
                                     String displayName, String emailAddress, String inputText, String pictureData);

    protected abstract void bindView(View view, String directoryType, String directoryName,
                                String displayName, String emailAddress, String inputText, byte[] pictureData);

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
        DirectoryPartition partition = (DirectoryPartition) getPartition(partitionIndex);
        return partition.loading ? 1 : 0;
    }

    @Override
    protected View newView(Context context, int partitionIndex, Cursor cursor,
                           int position, ViewGroup parent) {
        DirectoryPartition partition = (DirectoryPartition) getPartition(partitionIndex);
        if (partition.loading) {
            return inflateItemViewLoading(parent);
        } else {
            return inflateItemView(parent);
        }
    }

    @Override
    protected void bindView(View v, int partition, Cursor cursor, int position) {
        DirectoryPartition directoryPartition = (DirectoryPartition) getPartition(partition);
        String directoryType = directoryPartition.directoryType;
        String directoryName = directoryPartition.displayName;
        boolean isSearchingCursor = SEARCHING_CURSOR_MARKER.equals(cursor.getColumnName(0));
        if (directoryPartition.loading || isSearchingCursor) {
            bindViewLoading(v, directoryType, directoryName);
        } else {
            String displayName = cursor.getString(EmailQuery.NAME);
            String emailAddress = cursor.getString(EmailQuery.ADDRESS);
            EmailLog.d(TAG, "bindView view display name : " + displayName + ", emailAddress : " + emailAddress);

            //change@siso.vinay Gal Photo start
            int imageIndex = cursor.getColumnIndex(EmailQuery1.PROJECTION[EmailQuery1.PICTURE_DATA]);
            int thumnaleIndex = cursor.getColumnIndex(EmailQuery2.PROJECTION[EmailQuery2.PHOTO_THUMBNAIL_URI]);
            int cacheImageIndex = cursor.getColumnIndex(EmailQuery3.PROJECTION[EmailQuery3.CACHED_PICTURE_DATA]);
            if (isEnableGroupSearch != false) {
                int indexAccountType = cursor.getColumnIndex(EmailQuery_Group.PROJECTION[EmailQuery_Group.ACCOUNT_TYPE]);

                if (indexAccountType != -1 && cursor.getString(indexAccountType) != null) {
                    displayName = cursor.getString(cursor.getColumnIndex(EmailQuery_Group.PROJECTION[EmailQuery_Group.TITLE]));
                    emailAddress = "(Group)";
                }
            }
            String pictureData = null;
            byte[] pictureDataByte = null;
            //change@siso.vinay Gal Photo end

            if (TextUtils.isEmpty(displayName)) {
                displayName = emailAddress;
            }

            if (imageIndex > 0) {
                pictureData = cursor.getString(imageIndex);
            }

            if (thumnaleIndex > 0) {
                String thumbnailUriAsString = cursor.getString(thumnaleIndex);
                Uri photoThumbnailUri = null;
                try {
                    photoThumbnailUri = (thumbnailUriAsString != null ? Uri.parse(thumbnailUriAsString) : null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                pictureDataByte = tryFetchPhoto(photoThumbnailUri);

            }

            if ((pictureDataByte == null || pictureDataByte.length <= 0) && cacheImageIndex > 0) {
                pictureDataByte = cursor.getBlob(cacheImageIndex);
            }

            TagForBlackList tag = new TagForBlackList(partition, position, emailAddress);
            v.setTag(tag);
            if (imageIndex > 0 && pictureData != null && pictureData.length() > 0) {
                bindView(v, directoryType, directoryName, displayName, emailAddress, mInputText, pictureData);
            } else {
                bindView(v, directoryType, directoryName, displayName, emailAddress, mInputText, pictureDataByte);
            }

            //save temp
//            int displaynameIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.DISPLAY_NAME]);
//            int emailaddressIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.EMAIL_ADDRESS]);
//            int workphoneIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.WORK_PHONE]);
//            int homephoneIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.HOME_PHONE]);
//            int mobilephoneIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.MOBILE_PHONE]);
//            int firstnameIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.FIRST_NAME]);
//            int lastnameIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.LAST_NAME]);
//            int companyIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.COMPANY]);
//            int titleIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.TITLE]);
//            int officeIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.OFFICE]);
//            int aliasIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.ALIAS]);
//
//            SearchResult newResult = new SearchResult();
//            newResult.displayname = displayName;
//            newResult.emailaddress = emailAddress;
//
//            if( workphoneIndex != -1){
//                newResult.workphone = cursor.getString(workphoneIndex);
//            }
//            if( homephoneIndex != -1){
//                newResult.homephone = cursor.getString(homephoneIndex);
//            }
//            if( mobilephoneIndex != -1){
//                newResult.mobilephone = cursor.getString(mobilephoneIndex);
//            }
//            if( firstnameIndex != -1){
//                newResult.firstname = cursor.getString(firstnameIndex);
//            }
//            if( lastnameIndex != -1){
//                newResult.lastname = cursor.getString(lastnameIndex);
//            }
//            if( companyIndex != -1){
//                newResult.company = cursor.getString(companyIndex);
//            }
//            if( titleIndex != -1){
//                newResult.title = cursor.getString(titleIndex);
//            }
//            if( officeIndex != -1){
//                newResult.office = cursor.getString(officeIndex);
//            }
//            if( aliasIndex != -1){
//                newResult.alias = cursor.getString(aliasIndex);
//            }

//            mSearchResultCacheArray.add(newResult);
        }
    }

//    public SearchResult getSearchResultMatch(String emailaddr) {
//        SearchResult result = null;
//
//        if(emailaddr == null || emailaddr.length() <= 0 || mSearchResultCacheArray == null || mSearchResultCacheArray.isEmpty()) {
//            return null;
//        }
//
//        int listSize = mSearchResultCacheArray.size();
//
//        for(int index = 0; index < listSize; index++) {
//            SearchResult resultcache = mSearchResultCacheArray.get(index);
//
//            if(resultcache == null || resultcache.emailaddress == null) {
//                continue;
//            }
//
//            String cacheaddr = resultcache.emailaddress.trim();
//            String queryaddr = emailaddr.trim();
//
//            if (cacheaddr != null && queryaddr != null && cacheaddr.equalsIgnoreCase(queryaddr)) {
//                EmailLog.d(TAG, "getSearchResultMatch() find information detail in cache. emailaddr = "
//                        + emailaddr);
//                result = resultcache;
//            }
//        }
//
//        return result;
//    }

    public SearchResult getSearchResultFromCursor(int position, Cursor csr) {
        SearchResult result = new SearchResult();

        try {
            if (position < 0) {
                return null;
            }

            Cursor cursor = (Cursor) getItem(position);

            if (csr != null) {
                cursor = csr;
            }

            if (cursor == null) {
                return null;
            }

            int displaynameIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.DISPLAY_NAME]);
            int emailaddressIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.EMAIL_ADDRESS]);
            int workphoneIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.WORK_PHONE]);
            int homephoneIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.HOME_PHONE]);
            int mobilephoneIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.MOBILE_PHONE]);
            int firstnameIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.FIRST_NAME]);
            int lastnameIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.LAST_NAME]);
            int companyIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.COMPANY]);
            int titleIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.TITLE]);
            int officeIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.OFFICE]);
            int aliasIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.ALIAS]);

            if (displaynameIndex != -1) {
                result.displayname = cursor.getString(displaynameIndex);
            } else {
                return null;
            }
            if (emailaddressIndex != -1) {
                result.emailaddress = cursor.getString(emailaddressIndex);
            } else {
                return null;
            }
            if (workphoneIndex != -1) {
                result.workphone = cursor.getString(workphoneIndex);
            }
            if (homephoneIndex != -1) {
                result.homephone = cursor.getString(homephoneIndex);
            }
            if (mobilephoneIndex != -1) {
                result.mobilephone = cursor.getString(mobilephoneIndex);
            }
            if (firstnameIndex != -1) {
                result.firstname = cursor.getString(firstnameIndex);
            }
            if (lastnameIndex != -1) {
                result.lastname = cursor.getString(lastnameIndex);
            }
            if (companyIndex != -1) {
                result.company = cursor.getString(companyIndex);
            }
            if (titleIndex != -1) {
                result.title = cursor.getString(titleIndex);
            }
            if (officeIndex != -1) {
                result.office = cursor.getString(officeIndex);
            }
            if (aliasIndex != -1) {
                result.alias = cursor.getString(aliasIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }

        return result;
    }

    private static class PhotoQuery {
        public static final String[] PROJECTION = {
                Photo.PHOTO
        };

        public static final int PHOTO = 0;
    }

    /**
     * The number of photos cached in this Adapter.
     */
    private static final int PHOTO_CACHE_SIZE = 30;

    private LruCache<Uri, byte[]> mPhotoCacheMap;

    private byte[] tryFetchPhoto(final Uri photoThumbnailUri) {
        byte[] photoBytes = null;
        if (photoThumbnailUri != null) {
            try {
                photoBytes = mPhotoCacheMap.get(photoThumbnailUri);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (photoBytes == null) {
                fetchPhotoAsync(photoThumbnailUri);
                photoBytes = mPhotoCacheMap.get(photoThumbnailUri);
            }
        }
        return photoBytes;
    }

    private void fetchPhotoAsync(final Uri photoThumbnailUri) {
        final Cursor photoCursor = mContentResolver.query(
                photoThumbnailUri, PhotoQuery.PROJECTION, null, null, null);
        if (photoCursor != null) {
            try {
                if (photoCursor.moveToFirst()) {
                    final byte[] photoBytes = photoCursor.getBlob(PhotoQuery.PHOTO);
                    try {
                        mPhotoCacheMap.put(photoThumbnailUri, photoBytes);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    notifyDataSetChanged();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                photoCursor.close();
            }
        }


//        final AsyncTask<Void, Void, Void> photoLoadTask = new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
//                final Cursor photoCursor = mContentResolver.query(
//                        photoThumbnailUri, PhotoQuery.PROJECTION, null, null, null);
//                if (photoCursor != null) {
//                    try {
//                        if (photoCursor.moveToFirst()) {
//                            final byte[] photoBytes = photoCursor.getBlob(PhotoQuery.PHOTO);
//
//                            mHandler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    try {
//                                        if(mPhotoCacheMap != null) {
//                                            mPhotoCacheMap.put(photoThumbnailUri, photoBytes);
//                                        }
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    notifyDataSetChanged();
//                                }
//                            });
//                        }
//                    } finally {
//                        photoCursor.close();
//                    }
//                }
//                return null;
//            }
//        };
//        photoLoadTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
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
        return ((DirectoryPartition) getPartition(partitionIndex)).loading;
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
            CharSequence constraint, Cursor defaultPartitionCursor) {

        int count = getPartitionCount();
        int limit = 0;
        EmailLog.logd(TAG, "onDirectoryLoadFinished() constraint : " + constraint + ", partition count : " + count);
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
                if (EmailFeature.blockGalSearch()
                        && (null != mAccount && !mAccount.name.equals(partition.accountName) && partition.accountType
                        .equals(AccountManagerTypes.TYPE_EXCHANGE))) {
                    if (partition.filter != null) {
                        partition.filter.filter(null);
                    }
                    partition.loading = false;
                } else {
                    mHandler.removeMessages(MESSAGE_SEARCH_PENDING, partition);
                    Message msg = mHandler.obtainMessage(MESSAGE_SEARCH_PENDING, i, 0, partition);
                    mHandler.sendMessageDelayed(msg, MESSAGE_SEARCH_PENDING_DELAY);
                    if (partition.filter == null) {
                        partition.filter = new DirectoryPartitionFilter(i, partition.directoryId);
                    }
                    if (ExpandResultMaxAndShowMoreMode) {
                        partition.filter.setLimit(MAX_DIRECTORYPARTITIONFILTER_RESULT);
                    } else {
                        partition.filter.setLimit(limit);
                    }
                    partition.filter.filter(constraint);
                }
            } else {
                if (partition.filter != null) {
                    // Cancel any previous loading request
                    partition.filter.filter(null);
                }
            }
        }
    }

    public void doGalSearch(int partitionIndex) {
        if (partitionIndex > 0) {
            DirectoryPartition partition = (DirectoryPartition) getPartition(partitionIndex);
            clearPartitions();
            partition.filter.setLimit(partition.filter.getLimit() + MAX_DIRECTORYPARTITIONFILTER_RESULT);
            partition.loading = true;
            partition.constraint = BackupOfSearchedString;
            partition.filter.filter(BackupOfSearchedString);
            showSearchPendingIfNotComplete(partitionIndex);
        } else {
            clearPartitions();

            int count = getPartitionCount();
            for (int i = 0; i < count; i++) {
                DirectoryPartition partition = (DirectoryPartition) getPartition(i);
                partition.moreResultIsRemained = false;
            }
            for (int i = 0; i < count; i++) {
                DirectoryPartition partition = (DirectoryPartition) getPartition(i);
                if (partition.accountType != null &&
                        ((partition.accountType.equalsIgnoreCase(AccountManagerTypes.TYPE_EXCHANGE)
                                && (!EmailFeature.blockGalSearch()?true:(mAccount == null || partition.accountName.equals(mAccount.name))))
                                || partition.accountType.equalsIgnoreCase(AccountManagerTypes.TYPE_POP_IMAP)
                                || partition.accountType.equalsIgnoreCase(AccountManagerTypes.TYPE_LDAP)
                                || partition.accountType.equalsIgnoreCase(AccountManagerTypes.TYPE_NAUTA))) {
                    partition.filter.setLimit(partition.filter.getLimit() + MAX_DIRECTORYPARTITIONFILTER_RESULT);
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
            EmailLog.logd(TAG, "onPartitionLoadFinished(" + partitionIndex + "), "
                    + (cursor != null ? "cursor count is " + cursor.getCount() : "cursor is null"));
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

                if (ExpandResultMaxAndShowMoreMode) {
                    synchronized (mSynchronizer) {
                        if (isAllPartitionLoadFinished() && isAllPartitionEmpty() == false) {
                            addShowMoreButtonAtlastPatition();
                        } else if (isAllPartitionLoadFinished() && isAllPartitionEmpty() == true) {
                            if (isExistPartitionHasHiddenItems() && isNeedShowMoreItem()) {
                                addShowMoreButtonAtlastPatition();
                            } else {
                                addNoMatchesAtlastPatition();
                            }
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

    private void addNoMatchesAtlastPatition() {
        int totalPatitionCount = getPartitionCount();
        PackageManager packageManager = getContext().getPackageManager();
        String packageName = getContext().getPackageName();
//        String noMatches = null;

        try {
            Resources resources = null;
            int resourceId = 0;

            if (EmailPackage.PKG_UI.equalsIgnoreCase(packageName)) {
                resources = packageManager.getResourcesForApplication(EmailPackage.PKG_UI);
                resourceId = getContext().getResources().getIdentifier(
                        "com.samsung.android.email.provider:string/search_empty", null, null);
            } else if (EmailPackage.PKG_COMPOSER.equalsIgnoreCase(packageName)) {
                resources = packageManager.getResourcesForApplication(EmailPackage.PKG_COMPOSER);
                resourceId = getContext().getResources().getIdentifier(
                        "com.samsung.android.email.provider:string/search_empty", null, null);
            }
            if (resources != null) mNoMatches = resources.getString(resourceId);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        if (totalPatitionCount < 1) {
            EmailLog.d(TAG, "addNoMatchesAtlastPatition : partition is none");
            return;
        }

        int partionindex = totalPatitionCount - 1;
        Cursor lastCursor = getCursor(partionindex);

        if (lastCursor != null && lastCursor.getCount() == 1) {
            String displayName = lastCursor.getString(lastCursor.getColumnIndex(EmailQuery1.PROJECTION[EmailQuery1.NAME]));

            if (TextUtils.equals(displayName, mNoMatches)) return;
        }

        MatrixCursor newCursor = new MatrixCursor(EmailQuery1.PROJECTION);
        newCursor.addRow(new Object[]{
                mNoMatches, "", null
        });

        changeCursor(partionindex, newCursor);

        return;
    }

    public boolean isNoMatchItem() {
        if (getCount() == 1) {
            Cursor cursor = getCursor(getPartitionCount() - 1);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(EmailQuery1.PROJECTION[EmailQuery1.NAME]);
                if (columnIndex >= 0) {
                    String displayName = cursor.getString(columnIndex);

                    if (TextUtils.equals(displayName, mNoMatches)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Post-process the cursor to eliminate duplicates.  Closes the original cursor
     * and returns a new one.
     */
    private Cursor removeDuplicatesAndTruncate(int partition, Cursor cursor) {
        DirectoryPartition curpartition = (DirectoryPartition) getPartition(partition);
        curpartition.moreResultIsRemained = false;

        if (cursor == null) {
            EmailLog.d(TAG, "addShowMoreButtonAtlastPatition : cursor is null");
            return null;
        }

        if (ExpandResultMaxAndShowMoreMode == false
                && cursor.getCount() <= DEFAULT_PREFERRED_MAX_RESULT_COUNT
                && !hasDuplicates(cursor, partition)) {
            EmailLog.d(TAG, "addShowMoreButtonAtlastPatition : this partition has no duplication.");
            return cursor;
        }

        int count = 0;
        // MatrixCursor newCursor = new MatrixCursor(EmailQuery.PROJECTION);
//        MatrixCursor newCursor = new MatrixCursor(EmailQuery1.PROJECTION);
        MatrixCursor newCursor = new MatrixCursor(EmailQuery1_1.PROJECTION);

        cursor.moveToPosition(-1);

        while (cursor.moveToNext() && (ExpandResultMaxAndShowMoreMode || count < DEFAULT_PREFERRED_MAX_RESULT_COUNT)) {
            String displayName = cursor.getString(EmailQuery1_1.NAME);
            String emailAddress = cursor.getString(EmailQuery1_1.ADDRESS);

            /* Black list function */
            if (mIsEnableFilteringBlackList && mHashBlackList.contains(emailAddress)) {
                continue;
            }
            /* Black list function */
            String pictureData = null;
            String displayname = null;
            String emailaddress = null;
            String workphone = null;
            String homephone = null;
            String mobilephone = null;
            String firstname = null;
            String lastname = null;
            String company = null;
            String title = null;
            String office = null;
            String alias = null;

            int imageIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.PICTURE_DATA]);
            int displaynameIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.DISPLAY_NAME]);
            int emailaddressIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.EMAIL_ADDRESS]);
            int workphoneIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.WORK_PHONE]);
            int homephoneIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.HOME_PHONE]);
            int mobilephoneIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.MOBILE_PHONE]);
            int firstnameIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.FIRST_NAME]);
            int lastnameIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.LAST_NAME]);
            int companyIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.COMPANY]);
            int titleIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.TITLE]);
            int officeIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.OFFICE]);
            int aliasIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.ALIAS]);

            if (EXCHANGE_GAL_SHOW_MORE.equalsIgnoreCase(displayName)) {
                curpartition.moreResultIsRemained = true;
                continue;
            }

            if (imageIndex != -1) {
                pictureData = cursor.getString(imageIndex);
            }
            if (displaynameIndex != -1) {
                displayname = cursor.getString(displaynameIndex);
            }
            if (emailaddressIndex != -1) {
                emailaddress = cursor.getString(emailaddressIndex);
            }
            if (workphoneIndex != -1) {
                workphone = cursor.getString(workphoneIndex);
            }
            if (homephoneIndex != -1) {
                homephone = cursor.getString(homephoneIndex);
            }
            if (mobilephoneIndex != -1) {
                mobilephone = cursor.getString(mobilephoneIndex);
            }
            if (firstnameIndex != -1) {
                firstname = cursor.getString(firstnameIndex);
            }
            if (lastnameIndex != -1) {
                lastname = cursor.getString(lastnameIndex);
            }
            if (companyIndex != -1) {
                company = cursor.getString(companyIndex);
            }
            if (titleIndex != -1) {
                title = cursor.getString(titleIndex);
            }
            if (officeIndex != -1) {
                office = cursor.getString(officeIndex);
            }
            if (aliasIndex != -1) {
                alias = cursor.getString(aliasIndex);
            }

            if (!isDuplicateAddPictureDate(partition, displayName, emailAddress, pictureData,
                    displayname, emailaddress, workphone, homephone, mobilephone, firstname,
                    lastname, company, title, office, alias)) {
                newCursor.addRow(new Object[]{
                        displayName, emailAddress, pictureData, displayname, emailaddress,
                        workphone, homephone, mobilephone, firstname, lastname, company, title,
                        office, alias
                });
                count++;
            }
        }

        cursor.close();

        return newCursor;
    }

    private void addShowMoreButtonAtlastPatition() {
        int totalPatitionCount = getPartitionCount();

        if (totalPatitionCount <= 1) {
            EmailLog.d(TAG, "addShowMoreButtonAtlastPatition : partition is none");
            return;
        }

        int partionindex = totalPatitionCount - 1;

        Cursor lastCursor = getCursor(partionindex);
//        MatrixCursor newCursor = new MatrixCursor(EmailQuery.PROJECTION);
        MatrixCursor newCursor = new MatrixCursor(EmailQuery1.PROJECTION);
        if (lastCursor != null) {
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

                if (displayName != null && displayName.equalsIgnoreCase(EXCHANGE_GAL_SHOW_MORE)) {
                    continue;
                }

                //change@siso.vinay Gal Photo start
                if (imageIndex != -1) {
                    pictureData = lastCursor.getString(imageIndex);
                }
                //change@siso.vinay Gal Photo end

//                newCursor.addRow(new Object[]{displayName, emailAddress});
                newCursor.addRow(new Object[]{displayName, emailAddress, pictureData});
            }
        }

//        int id = Resources.getSystem().getIdentifier("gal_search_show_more", "string", "android");
//        newCursor.addRow(new Object[]{Resources.getSystem().getString(id), "" + partionindex});

        if (isNeedShowMoreItem()) {
//            newCursor.addRow(new Object[]{EXCHANGE_GAL_SHOW_MORE, "" + partionindex});
//            newCursor.addRow(new Object[]{EXCHANGE_GAL_SHOW_MORE, "" + -1});
            newCursor.addRow(new Object[]{EXCHANGE_GAL_SHOW_MORE, "" + -1, EXCHANGE_GAL_SHOW_MORE});
        } else {
            EmailLog.d(TAG, "addShowMoreButtonAtlastPatition : showmore does not need.");
        }

        try {
            changeCursor(partionindex, newCursor);
        } catch (Exception e) {
            EmailLog.d(TAG, "addShowMoreButtonAtlastPatition : partionindex : " + partionindex + ", newCursor.getCount() : " + newCursor.getCount());
            e.printStackTrace();
        }

        return;
    }

    private boolean isAllPartitionLoadFinished() {
        int totalCount = getPartitionCount();

        for (int i = 1; i < totalCount; i++) {
            DirectoryPartition curpartition = (DirectoryPartition) getPartition(i);
            if (curpartition.loading) {
                return false;
            }
        }

        return true;
    }

    private boolean isAllPartitionEmpty() {
        int totalCount = getPartitionCount();

        for (int i = 0; i < totalCount; i++) {
            if (isPartitionEmpty(i) == false) {
                return false;
            }
        }

        return true;
    }

    private boolean isExistPartitionHasHiddenItems() {
        int totalCount = getPartitionCount();

        for (int i = 1; i < totalCount; i++) {
            DirectoryPartition curpartition = (DirectoryPartition) getPartition(i);
            if (curpartition.hasHiddenItem) {
                return true;
            }
        }

        return false;
    }

    private boolean isNeedShowMoreItem() {
        int totalPatitionCount = getPartitionCount();

        for (int i = 1; i < totalPatitionCount; i++) {
            DirectoryPartition partition = (DirectoryPartition) getPartition(i);
            if (partition != null) {
                Cursor curCursor = getCursor(i);
                if (curCursor != null && curCursor.getCount() >= 0) {
                    EmailLog.d(TAG, "addShowMoreButtonAtlastPatition : cursor is not null. + (" + i + ")");

                    if (partition.moreResultIsRemained == true) {
                        EmailLog.d(TAG, "addShowMoreButtonAtlastPatition : showmore does not need. + (" + i + ")");
                        return true;
                    }
                }
            }
        }

        return false;
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

    private boolean isDuplicateAddPictureDate(int excludePartition, String displayName,
                                              String emailAddress, String newpictureData, String displayname, String emailaddress,
                                              String workphone, String homephone, String mobilephone, String firstname,
                                              String lastname, String company, String title, String office, String alias) {

        boolean isduplicated = false;
        int partitionCount = getPartitionCount();

        MergeCursor mergeResultCursor = null;
        MatrixCursor newCursor1 = null;
        MatrixCursor newCursor2 = null;
        MatrixCursor newCursor3 = null;

        try {
            newCursor1 = new MatrixCursor(EmailQuery1_1.PROJECTION);
            newCursor2 = new MatrixCursor(EmailQuery2_1.PROJECTION);
            newCursor3 = new MatrixCursor(EmailQuery3_1.PROJECTION);

            boolean defaultPatitionCursorChanged = false;
            boolean nowFounded = false;

            int imageIndex1 = -1;
            int imageIndex2 = -1;
            int imageIndex3 = -1;
            int imageIndex = -1;
            int displaynameIndex = -1;
            int emailaddressIndex = -1;
            int workphoneIndex = -1;
            int homephoneIndex = -1;
            int mobilephoneIndex = -1;
            int firstnameIndex = -1;
            int lastnameIndex = -1;
            int companyIndex = -1;
            int titleIndex = -1;
            int officeIndex = -1;
            int aliasIndex = -1;

            String curdisplayName = null;
            String curemailAddress = null;

            String curpictureData = null;
            String curthumbnailUriAsString = null;
            byte[] curpictureBytes = null;

            String curdisplayname = null;
            String curemailaddress = null;
            String curworkphone = null;
            String curhomephone = null;
            String curmobilephone = null;
            String curfirstname = null;
            String curlastname = null;
            String curcompany = null;
            String curtitle = null;
            String curoffice = null;
            String curalias = null;

            for (int partition = 0; partition < partitionCount; partition++) {
                if (partition != excludePartition && !isLoading(partition)) {
                    Cursor cursor = getCursor(partition);
                    if (cursor != null) {
                        cursor.moveToPosition(-1);
                        while (cursor.moveToNext()) {
                            curpictureData = null;
                            curthumbnailUriAsString = null;
                            curpictureBytes = null;

                            String name = cursor.getString(EmailQuery1_1.NAME);
                            String address = cursor.getString(EmailQuery1_1.ADDRESS);
                            if (partition == 0) {
                                curdisplayName = cursor.getString(EmailQuery1_1.NAME);
                                curemailAddress = cursor.getString(EmailQuery1_1.ADDRESS);
                                imageIndex1 = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1.PICTURE_DATA]);
                                imageIndex2 = cursor.getColumnIndex(EmailQuery2_1.PROJECTION[EmailQuery2.PHOTO_THUMBNAIL_URI]);
                                imageIndex3 = cursor.getColumnIndex(EmailQuery3_1.PROJECTION[EmailQuery3.CACHED_PICTURE_DATA]);

                                imageIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.PICTURE_DATA]);
                                displaynameIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.DISPLAY_NAME]);
                                emailaddressIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.EMAIL_ADDRESS]);
                                workphoneIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.WORK_PHONE]);
                                homephoneIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.HOME_PHONE]);
                                mobilephoneIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.MOBILE_PHONE]);
                                firstnameIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.FIRST_NAME]);
                                lastnameIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.LAST_NAME]);
                                companyIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.COMPANY]);
                                titleIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.TITLE]);
                                officeIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.OFFICE]);
                                aliasIndex = cursor.getColumnIndex(EmailQuery1_1.PROJECTION[EmailQuery1_1.ALIAS]);

                                if (imageIndex1 >= 0) {
                                    curpictureData = cursor.getString(imageIndex1);
                                }
                                if (imageIndex2 >= 0) {
                                    curthumbnailUriAsString = cursor.getString(imageIndex2);
                                }
                                if (imageIndex3 > 0) {
                                    curpictureBytes = cursor.getBlob(imageIndex3);
                                }

                                if (displaynameIndex > 0) {
                                    curdisplayname = cursor.getString(displaynameIndex);
                                }
                                if (curdisplayname == null || curdisplayname.length() <= 0) {
                                    curdisplayname = displayName;
                                }

                                if (emailaddressIndex > 0) {
                                    curemailaddress = cursor.getString(emailaddressIndex);
                                }
                                if (curemailaddress == null || curemailaddress.length() <= 0) {
                                    curemailaddress = emailaddress;
                                }

                                if (workphoneIndex > 0) {
                                    curworkphone = cursor.getString(workphoneIndex);

                                }
                                if (curworkphone == null || curworkphone.length() <= 0) {
                                    curworkphone = workphone;
                                }

                                if (homephoneIndex > 0) {
                                    curhomephone = cursor.getString(homephoneIndex);
                                }
                                if (curhomephone == null || curhomephone.length() <= 0) {
                                    curhomephone = homephone;
                                }

                                if (mobilephoneIndex > 0) {
                                    curmobilephone = cursor.getString(mobilephoneIndex);
                                }
                                if (curmobilephone == null || curmobilephone.length() <= 0) {
                                    curmobilephone = mobilephone;
                                }

                                if (firstnameIndex > 0) {
                                    curfirstname = cursor.getString(firstnameIndex);
                                }
                                if (curfirstname == null || curfirstname.length() <= 0) {
                                    curfirstname = firstname;
                                }

                                if (lastnameIndex > 0) {
                                    curlastname = cursor.getString(lastnameIndex);
                                }
                                if (curlastname == null || curlastname.length() <= 0) {
                                    curlastname = lastname;
                                }

                                if (companyIndex > 0) {
                                    curcompany = cursor.getString(companyIndex);
                                }
                                if (curcompany == null || curcompany.length() <= 0) {
                                    curcompany = company;
                                }

                                if (titleIndex > 0) {
                                    curtitle = cursor.getString(titleIndex);
                                }
                                if (curtitle == null || curtitle.length() <= 0) {
                                    curtitle = title;
                                }

                                if (officeIndex > 0) {
                                    curoffice = cursor.getString(officeIndex);
                                }
                                if (curoffice == null || curoffice.length() <= 0) {
                                    curoffice = office;
                                }

                                if (aliasIndex > 0) {
                                    curalias = cursor.getString(aliasIndex);
                                }
                                if (curalias == null || curalias.length() <= 0) {
                                    curalias = alias;
                                }
                            }

                            nowFounded = false;
                            if (TextUtils.equals(emailAddress, address)) {
                                if ((name == null || TextUtils.equals(displayName, name) || TextUtils.equals(address, name))) {
                                    isduplicated = true;
                                    nowFounded = true;

                                    if (TextUtils.equals(address, name) && displayName != null && displayName.length() > 0) {
                                        curdisplayName = displayName;
                                    }

                                    if (partition != 0) {
                                        break;
                                    }
                                }
                            }

                            if (partition == 0) {
                                if (nowFounded) {
                                    if (curdisplayName == null || curdisplayName.length() <= 0) {
                                        curdisplayName = displayName;
                                    }
                                    if (curpictureData != null) {
                                        newCursor1.addRow(new Object[]{
                                                curdisplayName, curemailAddress, curpictureData,
                                                curdisplayname, curemailaddress, curworkphone,
                                                curhomephone, curmobilephone, curfirstname,
                                                curlastname, curcompany, curtitle, curoffice, curalias
                                        });
                                    } else if (curthumbnailUriAsString != null) {
                                        newCursor2.addRow(new Object[]{
                                                curdisplayName, curemailAddress,
                                                curthumbnailUriAsString, newpictureData,
                                                curdisplayname, curemailaddress, curworkphone,
                                                curhomephone, curmobilephone, curfirstname,
                                                curlastname, curcompany, curtitle, curoffice, curalias
                                        });
                                    } else if (curpictureBytes != null
                                            && (newpictureData == null || newpictureData.length() <= 0)) {
                                        newCursor3.addRow(new Object[]{
                                                curdisplayName, curemailAddress, curpictureBytes,
                                                newpictureData, curdisplayname, curemailaddress,
                                                curworkphone, curhomephone, curmobilephone,
                                                curfirstname, curlastname, curcompany, curtitle,
                                                curoffice, curalias
                                        });
                                    } else {
                                        newCursor1.addRow(new Object[]{
                                                curdisplayName, curemailAddress, newpictureData,
                                                curdisplayname, curemailaddress, curworkphone,
                                                curhomephone, curmobilephone, curfirstname,
                                                curlastname, curcompany, curtitle, curoffice, curalias
                                        });
                                    }
                                    defaultPatitionCursorChanged = true;
                                } else {
                                    if (curpictureData != null) {
                                        newCursor1.addRow(new Object[]{
                                                curdisplayName, curemailAddress, curpictureData,
                                                curdisplayname, curemailaddress, curworkphone,
                                                curhomephone, curmobilephone, curfirstname,
                                                curlastname, curcompany, curtitle, curoffice, curalias
                                        });
                                    } else if (curthumbnailUriAsString != null) {
                                        newCursor2.addRow(new Object[]{
                                                curdisplayName, curemailAddress,
                                                curthumbnailUriAsString, null,
                                                curdisplayname, curemailaddress, curworkphone,
                                                curhomephone, curmobilephone, curfirstname,
                                                curlastname, curcompany, curtitle, curoffice, curalias
                                        });
                                    } else if (curpictureBytes != null) {
                                        newCursor3.addRow(new Object[]{
                                                curdisplayName, curemailAddress, curpictureBytes,
                                                null, curdisplayname, curemailaddress,
                                                curworkphone, curhomephone, curmobilephone,
                                                curfirstname, curlastname, curcompany, curtitle,
                                                curoffice, curalias
                                        });
                                    } else {
                                        newCursor1.addRow(new Object[]{
                                                curdisplayName, curemailAddress, curpictureData,
                                                curdisplayname, curemailaddress, curworkphone,
                                                curhomephone, curmobilephone, curfirstname,
                                                curlastname, curcompany, curtitle, curoffice, curalias
                                        });
                                    }
                                }
                            }
                        } //while end
                    } // if end
                } // if end

                if (isduplicated == true) {
                    break;
                }

            } // for end

            if (defaultPatitionCursorChanged) {
                mergeResultCursor = new MergeCursor(new Cursor[]{
                        newCursor2, newCursor1, newCursor3});
                changeCursor(0, mergeResultCursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (newCursor1 != null) newCursor1.close();
            } catch (Exception e1) {
            }
            try {
                if (newCursor2 != null) newCursor2.close();
            } catch (Exception e2) {
            }
            try {
                if (newCursor3 != null) newCursor3.close();
            } catch (Exception e3) {
            }
        }

        return isduplicated;
    }

    private final String makeDisplayString(Cursor cursor) {
        if (cursor.getColumnName(0).equals(SEARCHING_CURSOR_MARKER)) {
            return "";
        }

        String displayName = cursor.getString(EmailQuery.NAME);
        String emailAddress = cursor.getString(EmailQuery.ADDRESS);
        if (TextUtils.isEmpty(displayName) || TextUtils.equals(displayName, emailAddress)) {
            return emailAddress;
        } else {
            return new Address(emailAddress, displayName).toString();
        }
    }

    public Cursor getGroupNameCursor(CharSequence constraint) {
        MatrixCursor newCursor = new MatrixCursor(EmailQuery_Group.PROJECTION);
        Cursor groupCursor = null;
        String inputText;

        try {
            if (constraint != null) {
                inputText = constraint.toString();
            } else
                inputText = "";

            String str = "";

            if (inputText.contains("'")) {
                str = URLEncoder.encode(inputText, "utf-8");
                str = str.replace("-", "%2D");
            } else
                str = inputText;

            groupCursor = mContentResolver.query(Groups.CONTENT_URI, EmailQuery_Group.PROJECTION,
                    "(" + Groups.TITLE + " like '" + str + "%%'" + ")", null, null);

            if (groupCursor != null) {
                ArrayList<String> titleList = new ArrayList<String>();
                groupCursor.moveToPosition(-1);
                while (groupCursor.moveToNext()) {
                    String title = groupCursor.getString(EmailQuery_Group.TITLE);
                    String accType = groupCursor.getString(EmailQuery_Group.ACCOUNT_TYPE);
                    String systemId = groupCursor.getString(EmailQuery_Group.SYSTEM_ID);

                    // check if the group includes available email address.
                    Uri groupUri = Groups.CONTENT_URI.buildUpon().appendEncodedPath("title")
                            .appendPath(title).appendEncodedPath("primary_emails").build();

                    Cursor tempcur = null;
                    try {
                        tempcur = mContentResolver
                                .query(groupUri, new String[]{
                                        Data._ID, Data.DATA1, RawContacts.DISPLAY_NAME_PRIMARY,
                                }, null, null, null);

                        if (tempcur == null || tempcur.getCount() <= 0) {
                            continue;
                        }

                        if (!titleList.contains(title)) {
                            newCursor.addRow(new Object[]{
                                    title + " (" + tempcur.getCount() + ")", accType, systemId
                            });
                            titleList.add(title);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (tempcur != null) {
                            tempcur.close();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            newCursor = null;
        } finally {
            if (groupCursor != null) {
                groupCursor.close();
            }
        }

        if (newCursor != null && newCursor.getCount() <= 0) {
            newCursor.close();
            return null;
        }

        return newCursor;
    }
}
