/**
 * 
 */

package com.samsung.android.emailcommon.utility;

import android.database.Cursor;

import com.samsung.android.emailcommon.provider.MailboxData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author nj6
 *
 */
/**
 * Class for all the mail box list related data
 */
public class MailBoxListData {
    Cursor mCursor;

    public ArrayList<String> mParentList; // To store the Id's of the parent
                                          // mailbox
    public ArrayList<String> mChildList; // To store the Id's of the child
                                         // mailbox
    public ArrayList<String> mSortedList; // To store the Id's of the mailbox's
                                          // based on the type
    public HashMap<String, MailboxData> mHashParentMailbox;
    public HashMap<String, List<MailboxData>> mHashChildMailbox;
    public HashMap<String, MailboxData> mHashMailboxList;
    private int mMailboxMode = 1; // MessageListFragmentManager.MAILBOX_MODE_NORMAL;
                                  // //adapter_porting

    public MailBoxListData() {
        this(null);
    }

    public MailBoxListData(Cursor cursor) {
        mCursor = cursor;
        mParentList = new ArrayList<String>();
        mChildList = new ArrayList<String>();
        mSortedList = new ArrayList<String>();
        mHashParentMailbox = new HashMap<String, MailboxData>();
        mHashChildMailbox = new HashMap<String, List<MailboxData>>();
        mHashMailboxList = new HashMap<String, MailboxData>();
    }

    public void setCursor(Cursor cursor) {
        mCursor = cursor;
    }

    public Cursor getCursor() {
        return mCursor;
    }

    public void setMode(int mailBoxMode) {
        mMailboxMode = mailBoxMode;

    }

    public int getMailboxMode() {
        return mMailboxMode;
    }

    public void clearMailboxHashMaps() {
        mParentList.clear();
        mChildList.clear();
        mSortedList.clear();
        mHashParentMailbox.clear();
        mHashChildMailbox.clear();
        mHashMailboxList.clear();
    }

}
