package com.samsung.android.emailcommon.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;

import com.samsung.android.emailcommon.EmailSyncProviderUtils;
import com.samsung.android.emailcommon.service.EmailServiceProxy;
import com.samsung.android.emailcommon.service.IEmailServiceCallback;
import com.samsung.android.emailcommon.service.OoODataList;
import com.samsung.android.emailcommon.service.ProxyArgs;
import com.samsung.android.emailcommon.utility.EmailLog;

public class ServiceAdapter implements ProxyArgs {
    private static final String TAG = "ServiceAdapter";
    protected EmailServiceProxy svc ;
    protected IEmailServiceCallback.Stub mCallback = null;
    protected Context mContext;

    public void createFolder(final long accountId, final long parentMailboxId, final String folderName) {
        if (svc == null)
            return;

        Bundle b = new Bundle();
        b.putLong(ARG_ACCOUNT_ID, accountId);
        b.putLong(ARG_PARENT_MAILBOX_ID, parentMailboxId);
        b.putString(ARG_TEXT, folderName);

//        try {
//            svc.folderCreate(EmailServiceProxy.API_VER, b);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
    }

    public void deleteFolder(final long accountId, final long [] mailboxIds) {
        if (svc == null)
            return;

        Bundle b = new Bundle();
        b.putLong(ARG_ACCOUNT_ID, accountId);
        b.putLongArray(ARG_MAILBOX_ID_ARRAY, mailboxIds);

//        try {
//            svc.folderDelete(EmailServiceProxy.API_VER, b);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
    }

    public void renameFolder(final long mailboxId, final String text) {
        if (svc == null)
            return;

        Bundle b = new Bundle();
        b.putLong(ARG_MAILBOX_ID, mailboxId);
        b.putString(ARG_TEXT, text);

//        try {
//            svc.folderRename(EmailServiceProxy.API_VER, b);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
    }

    public void moveFolder(final String targetServerMailboxId, final long movingMailboxId) {
        if (svc == null)
            return;

        Bundle b = new Bundle();
        b.putLong(ARG_MAILBOX_ID, movingMailboxId);
        b.putString(ARG_MAILBOX_ID_TO, targetServerMailboxId);

//        try {
//            svc.folderMove(EmailServiceProxy.API_VER, b);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
    }

    public Bundle deleteAccount(final long accountId) {

        Bundle b = new Bundle();
        b.putLong(ProxyArgs.ARG_ACCOUNT_ID, accountId);

        return EmailSyncProviderUtils.emailSyncProviderQuery(mContext, b, EmailSyncProviderUtils.SYNC_INDEX_DELETEACCOUNT);
    }

    public void loadAttachment(final long accountId, final long mailboxId, final long messageId,
                               final long attachmentId, final boolean prune, final boolean reconnect, final boolean background) {
        if (svc == null)
            return;

        Bundle b = new Bundle();
        b.putLong(ARG_ACCOUNT_ID, accountId);
        b.putLong(ARG_MAILBOX_ID, mailboxId);
        b.putLong(ARG_MESSAGE_ID, messageId);
        b.putLong(ARG_ATTACHMENT_ID, attachmentId);
        b.putBoolean(ARG_PRUNE, prune);
        b.putBoolean(ARG_RECONNECT, reconnect);
        b.putBoolean(ARG_BACKGROUND, background);

        try {
            svc.loadAttachment(EmailServiceProxy.API_VER, b);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void loadMore(final long messageId) {
        if (svc == null)
            return;

        Bundle b = new Bundle();
        b.putLong(ARG_MESSAGE_ID, messageId);

        try {
            svc.loadMore(EmailServiceProxy.API_VER, b);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public Bundle hostChanged(long accountId) {
        Bundle b = new Bundle();
        b.putLong(ProxyArgs.ARG_ACCOUNT_ID, accountId);

        return EmailSyncProviderUtils.emailSyncProviderQuery(mContext, b, EmailSyncProviderUtils.SYNC_INDEX_HOSTCHANGED);
    }

    public void syncMailboxList(final long accountId) {

        if (svc == null)
            return;
        Bundle b = new Bundle();
        b.putLong(ARG_ACCOUNT_ID, accountId);

        try {
            svc.updateFolderList(EmailServiceProxy.API_VER, b);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void synchronizeMailbox(final long accountId, final long mailboxId) {
        EmailLog.d(TAG, "synchronizeMailbox" + accountId + mailboxId);
//		if (svc == null)
//			return;
//		  //change.srib@sawrav.roy -- start
//		    Bundle b = new Bundle();
//	        b.putLong(ARG_ACCOUNT_ID, accountId);
//	        b.putLong(ARG_MAILBOX_ID, mailboxId);
//
//	        try {
//	            svc.startSync(EmailServiceProxy.API_VER, b);
//	        } catch (RemoteException e) {
//	            e.printStackTrace();
//	        }
//	        //change.srib@sawrav.roy -- end
    }

    public void searchMessage(long accountId, long mailboxId, String searchText) {
        if(svc == null)
            return;
        Bundle b = new Bundle();
        b.putLong(ARG_ACCOUNT_ID, accountId);
        b.putLong(ARG_MAILBOX_ID, mailboxId);
        b.putString(ARG_SEARCH_TEXT_STRING, searchText);

        try{
            svc.searchMessage(EmailServiceProxy.API_VER, b);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void removeDownloadAttachment(long messageId) {
        if(svc == null)
            return;
        Bundle b = new Bundle();
        b.putLong(ARG_MESSAGE_ID, messageId);

        try{
            svc.removeDownloadAttachment(b);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void addCallback() {
        if(svc == null)
            return;
        svc.addCallback();
    }

    public void removeCallback() {
        if(svc == null)
            return;
        svc.removeCallback();
    }

    /* ************* Only For ImapPush *****************/
    public void startPush(long accountId) {
        EmailLog.d(TAG, "startPush" + accountId);
    }

    public void stopPush(long accountId) {
        EmailLog.d(TAG, "stopPush" + accountId);
    }
    /* ************* Only For ImapPush *****************/

    /* ************* Only For EAS **********************/
    public void moveConversationAlways(long messageId, long toMailboxId, byte[] conversationId, int ignore) {
        EmailLog.d("moveConversationAlways", "" + messageId + toMailboxId + conversationId[0] + ignore);
    }

    public void cancelAutoDiscover(String username) {
        EmailLog.d("cancelAutoDiscover", username);
    }

    public void sslCertValidationFinished(String username, String url, int message) {
        EmailLog.d("sslCertValidationFinished", username + url + message);
    }

    public void emptyTrash(long accountId) {
        EmailLog.d("emptyTrash", "" + accountId);
    }

    public void getOutOfOffice(long accountId) {
        EmailLog.d("getOutOfOffice", "" + accountId);
    }

    public void setOutOfOffice(long accountId,  OoODataList data) {
        EmailLog.d("setOutOfOffice", "" + accountId + data);
    }

    public void changeSmsSettings(long accountId) {
        EmailLog.d("changeSmsSettings", "" + accountId);
    }

    public void sendMeetingResponse(final long messageId, final int response) {
        EmailLog.d("sendMeetingResponse", "" + messageId + response);
    }

    public void sendMeetingEditedResponse(final long messageId, final long draftMessageId, final int response) {
        EmailLog.d("sendMeetingEditedResponse", "" + messageId + draftMessageId + response);
    }

    public Bundle autoDiscover(final String userName, final String password, final String domain, final boolean bTrustCert) {
        EmailLog.d("autoDiscover", userName + password + domain + bTrustCert);
        return null;
    }

    public void refreshIRMTemplates(long accountId) {
        EmailLog.d("refreshIRMTemplates", "" + accountId);
    }

    public void updateNetworkInfo() {
        EmailLog.d("updateNetworkInfo","");
    }
    /* ************* Only For EAS **********************/
}