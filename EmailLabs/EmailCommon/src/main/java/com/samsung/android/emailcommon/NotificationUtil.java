package com.samsung.android.emailcommon;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.samsung.android.emailcommon.mail.Address;
import com.samsung.android.emailcommon.provider.EmailContent;
import com.samsung.android.emailcommon.provider.EmailContent.Account;
import com.samsung.android.emailcommon.provider.EmailContent.Message;
import com.samsung.android.emailcommon.provider.EmailContent.MessageColumns;
import com.samsung.android.emailcommon.utility.EmailLog;
import com.samsung.android.emailcommon.utility.Log;

import java.util.List;

public class NotificationUtil {

    public static final String TAG = "NotificationUtil";
    public Context mContext;

    public interface NotiColumns {
        String TABLE_NAME_NEW_MESSAGE = "NewMessage";
        String TABLE_NAME_SEND_FAIL = "SendFail";
        String TABLE_NAME_PREV_PEOPLE_COUNT = "PrevPeopleCount";
        String ID = "_id";
        String ACCOUNT_ID = "accountId";
        String MESSAGE_ID = "messageId";
        String TIMESTAMP = "timeStamp";
        String TYPE = "type";
        String ALARM = "alarm";
        String SENDER_ADDRESS = "senderAddress";
        String MESSAGE_COUNT = "messageCount";
        String SUBJECT = "subject";
    }

    public static final String BACKGROUND_RUNTIME_PERMISSION_TYPE = "permission_type";
    public static final String BACKGROUND_RUNTIME_PERMISSION_FUNCTION = "permission_function";
    
    final static private int COL_FROM = 0;
    
    final static private int COL_VIP_ADDRESS = 0;
    
    final static public int NOTI_ALARM_NO = 0;
    final static public int NOTI_ALARM_GENERAL = 1;
    final static public int NOTI_ALARM_VIP = 2;

    final static public String AUTHORITY = "com.samsung.android.email.notification.provider";

    final static public Uri NEW_MESSAGE_URI = Uri.parse("content://" + AUTHORITY + "/newMessage");
    final static public Uri NEW_MESSAGE_NOTIFIER = Uri.parse("content://" + AUTHORITY + "/newMessage");
    final static public Uri SEND_FAIL_URI = Uri.parse("content://" + AUTHORITY + "/sendFail");
    final static public Uri LOGIN_FAIL_URI = Uri.parse("content://" + AUTHORITY + "/loginFail");
    final static public Uri ACCOUNT_FAIL_URI = Uri.parse("content://" + AUTHORITY + "/accountFail");
    final static public Uri ACCOUNT_DELETE_URI = Uri.parse("content://" + AUTHORITY + "/accountDelete");
    final static public Uri POLICY_REQUIRED_URI = Uri.parse("content://" + AUTHORITY + "/policyRequired");
    final static public Uri PASSWORD_EXPIRED_URI = Uri.parse("content://" + AUTHORITY + "/passwordExpired");
    final static public Uri REMOVE_POLICY_NOTI_URI = Uri.parse("content://" + AUTHORITY + "/removePolicyNoti");
    final static public Uri REMOVE_MDM_NOTI_URI = Uri.parse("content://" + AUTHORITY + "/removeMDMNoti");
    final static public Uri REFRESH_PEOPLE_STRIPE_NOTI_URI = Uri.parse("content://" + AUTHORITY + "/refreshPeopleStripe");
    final static public Uri HOTMAIL_POP_15_MIN_ERROR_URI = Uri.parse("content://" + AUTHORITY + "/hotmailPopFifteenMinError");
    final static public Uri SENDING_URI = Uri.parse("content://" + AUTHORITY + "/sendingNoti");
    final static public Uri SENDING_FINISH_URI = Uri.parse("content://" + AUTHORITY + "/sendingFinishNoti");
    final static public Uri REMOVE_SENDING_URI = Uri.parse("content://" + AUTHORITY + "/removesendingNoti");
    final static public Uri RUNTIME_PERMISSION_NOTI_URI = Uri.parse("content://" + AUTHORITY + "/runtimePermissionNoti");
    final static public Uri UNTRUSTED_CERTIFICATE_ERROR_NOTI_URI = Uri.parse("content://" + AUTHORITY + "/untrustedCertificateErrorNoti");

    final static public int TYPE_SENDING_FAIL_NORMAL = 1;
    final static public int TYPE_SENDING_FAIL_OUT_OF_MEMORY = 2;

    static public void showNewMessage(Context context, long accountId, List<Long> ids, boolean alarm) {

        boolean isVip = false;
        int newAlarm = 0;
        EmailLog.d(TAG, "New Synced Email Size = " + ids.size());
        if (ids.size() == 0) {
            return;
        }
        EmailLog.d(TAG, "showNewMessage with Ids, account ID : " + accountId);
        try {
            int count = ids.size();
            for (int i = 0; i < count - 1; i++) {
                ContentValues cv = new ContentValues();
                cv.put(NotiColumns.ACCOUNT_ID, accountId);
                cv.put(NotiColumns.MESSAGE_ID, ids.get(i));
                cv.put(NotiColumns.TYPE, 1);
                
                if (!isVip) {
                    Address sendersAddress = getSendersAddressByMessagId(context, ids.get(i));
                    if (sendersAddress != null && isVip(sendersAddress.getAddress(), context)) {
                        EmailLog.d(TAG, "Priority Sender is existed in New Synced Email !!!");
                        isVip = true;
                    }
                }

                cv.put(NotiColumns.ALARM, NOTI_ALARM_NO);
                context.getContentResolver().insert(NEW_MESSAGE_URI, cv);
            }
            ContentValues cv = new ContentValues();
            cv.put(NotiColumns.ACCOUNT_ID, accountId);
            cv.put(NotiColumns.MESSAGE_ID, ids.get(count - 1));
            cv.put(NotiColumns.TYPE, 1);
            if (!isVip) {
                Address sendersAddress = getSendersAddressByMessagId(context, ids.get(count - 1));
                if (sendersAddress != null && isVip(sendersAddress.getAddress(), context)) {
                    EmailLog.d(TAG, "Priority Sender is existed in New Synced Email !!!");
                    isVip = true;
                }
            }

            if (alarm) {
                if (isVip)
                    newAlarm = NOTI_ALARM_VIP;
                else
                    newAlarm = NOTI_ALARM_GENERAL;
            }
            cv.put(NotiColumns.ALARM, newAlarm);
            context.getContentResolver().insert(NEW_MESSAGE_URI, cv);
        } catch (IllegalArgumentException e) {
            Log.dumpException(TAG, e);
        }
    }

    static public void showNewMessageUpdate(Context context, long accountId, int type) {

        EmailLog.d(TAG, "showNewMessageUpdate = " + accountId);
        try {

            ContentValues cv = new ContentValues();
            cv.put(NotiColumns.ACCOUNT_ID, accountId);
            cv.put(NotiColumns.TYPE, type);

            context.getContentResolver().update(ContentUris.withAppendedId(NEW_MESSAGE_URI, accountId), cv, null, null);
        } catch (IllegalArgumentException e) {
            Log.dumpException(TAG, e);
        }
    }


    static private final String[] SENDER_PROJECTION = new String[] {
        MessageColumns.FROM_LIST,
        };
    
    private static Address getSendersAddressByMessagId(Context context, long messageId) {
        // TODO Auto-generated method stub
        String mSender = "";
        Cursor c = null;
        Address sender = null;
        try {
            c = context.getContentResolver().query(Message.CONTENT_URI, SENDER_PROJECTION,
                    NotiColumns.ID + "=" + String.valueOf(messageId), null, null);

            if (c != null && c.moveToFirst())
                mSender = c.getString(COL_FROM);
            else {
                EmailLog.w(TAG, "NULL response for senders email address query");
                return null;
            }
            if (mSender != null)
                sender = Address.unpackFirst(mSender);
            return sender;
        } catch(Exception e){
            Log.dumpException(TAG, e);
            return null;
        } finally {
        
            if (c != null && !c.isClosed())
                c.close();
        }
    }



    static public void cancelNewMessage(Context context, long accountId, List<Long> ids) {
        if (ids == null) {
            return;
        }
        EmailLog.d(TAG, "cancelNewMessage with Ids, account ID : " + accountId);
        try {
            Uri preUri = ContentUris.withAppendedId(NEW_MESSAGE_URI, accountId);
            for (long id : ids) {
                EmailLog.d(TAG, "id :  " + id);
                Uri uri = ContentUris.withAppendedId(preUri, id);
                context.getContentResolver().delete(uri, null, null);
            }
        } catch (IllegalArgumentException ie) {
            Log.dumpException(TAG, ie);
        }
    }

    static public void cancelNewMessage(Context context, long accountId, boolean isLaunched) {
        EmailLog.d(TAG,"cancelNewMessage with account ID : " + accountId);
        try {
            Uri uri = ContentUris.withAppendedId(NEW_MESSAGE_URI, accountId);
            context.getContentResolver().delete(uri, null, null);
        } catch (IllegalArgumentException ie) {
            Log.dumpException(TAG, ie);
        }
            Intent broadcast = new Intent();
            broadcast.setAction("com.samsung.android.email.intent.action.CLEAR_NOTIFICATION");
            broadcast.putExtra(IntentConst.EXTRA_ACCOUNT_ID, accountId);
            broadcast.putExtra(IntentConst.EXTRA_IS_LAUNCHED, isLaunched);
            context.sendBroadcast(broadcast);
    }

    static public void cancelNewMessage(Context context) {
        EmailLog.d(TAG, "cancelNewMessage with all accounts");
        Uri uri = NEW_MESSAGE_URI.buildUpon().appendEncodedPath("all").build();
        try {
            context.getContentResolver().delete(uri, null, null);
        } catch (NullPointerException npe) {
            if (context == null) {
                EmailLog.e(TAG, "Context is null.");
            } else {
                EmailLog.e(TAG, "context.getContentResolver() is null.");
            }
            npe.printStackTrace();
        } catch (IllegalArgumentException ie) {
            Log.dumpException(TAG, ie);
        }
    }

    static public void showLoginFailedNotification(Context context, long accountId) {
        showLoginFailedNotification(context, accountId, null);
    }

    static public void showLoginFailedNotification(Context context, long accountId, Exception e) {
        EmailLog.d(TAG, "showLoginFailedNotification");
        Account.setAuthFailed(context, accountId, true);
        String alert = "";
        if (e != null) {
            alert = e.getMessage();
        }
        try {
            ContentValues cv = new ContentValues();
            cv.put(NotiColumns.ACCOUNT_ID, accountId);
            cv.put(NotiColumns.ALARM, alert);
            context.getContentResolver().insert(LOGIN_FAIL_URI, cv);
        } catch (IllegalArgumentException ie) {
            Log.dumpException(TAG, ie);
        }
    }

    /**
     * This is to show the error notification for Hotmail POP account.
     * @param context
     * @param accountId
     * @param e
     */
    static public void show15MinErrorNotification(Context context, long accountId, Exception e) {
        EmailLog.d(TAG, "show15MinErrorNotification");
        String alert = "";
        if (e != null) {
            alert = e.getMessage();
        }
        try {
            ContentValues cv = new ContentValues();
            cv.put(NotiColumns.ACCOUNT_ID, accountId);
            cv.put(NotiColumns.ALARM, alert);
            context.getContentResolver().insert(HOTMAIL_POP_15_MIN_ERROR_URI, cv);
        } catch (IllegalArgumentException ie) {
            Log.dumpException(TAG, ie);
        }
    }

    static public void cancelLoginFailedNotification(Context context, long accountId) {
        try {
            context.getContentResolver().delete(ContentUris.withAppendedId(LOGIN_FAIL_URI, accountId), null, null);
        } catch (Exception e) {
            EmailLog.dumpException(TAG, e);
        }
    }

    static public void showAccountConfigutaionFailureNotification(Context context, long accountId,
            boolean incoming) {
        EmailLog.d(TAG, "showAccountConfigutaionFailureNotification");
        try {
            ContentValues cv = new ContentValues();
            cv.put(NotiColumns.ACCOUNT_ID, accountId);
            cv.put(NotiColumns.TYPE, incoming);
            context.getContentResolver().insert(ACCOUNT_FAIL_URI, cv);
        } catch (IllegalArgumentException ie) {
            Log.dumpException(TAG, ie);
        }
    }

    static public void cancelAccountConfigutaionFailureNotification(Context context, long accountId) {
        try {
            context.getContentResolver().delete(
                    ContentUris.withAppendedId(ACCOUNT_FAIL_URI, accountId), null, null);
        } catch (IllegalArgumentException ie) {
            Log.dumpException(TAG, ie);
        }
    }

    static public void showSendFailNotification(Context context, long messageId, long accountId,
            int type) {
        EmailLog.d(TAG, "showSendFailNotification");
        try {
            ContentValues cv = new ContentValues();
            cv.put(NotiColumns.ACCOUNT_ID, accountId);
            cv.put(NotiColumns.MESSAGE_ID, messageId);
            cv.put(NotiColumns.TYPE, type);
            context.getContentResolver().insert(SEND_FAIL_URI, cv);
//            Intent broadcast = new Intent(IntentConst.ACTION_REPLY_BACKGROUND_SENT_INTERNAL);
//            Account account = Account.restoreAccountWithId(context, accountId);
//            if (account != null)
//                broadcast.putExtra("account-id", account.mEmailAddress);
//            broadcast.addCategory(Intent.CATEGORY_APP_EMAIL);
//            broadcast.putExtra("msg-id", messageId);
//            broadcast.putExtra("sent-result", false);
//            context.sendBroadcast(broadcast);
//
//            broadcast = new Intent(IntentConst.ACTION_SEND_BACKGROUND_SENT_INTERNAL);
//            if (account != null)
//                broadcast.putExtra("accountAddress", account.mEmailAddress);
//            broadcast.putExtra("originalMsgId", messageId);
//            String action = ""; // TODO
//            broadcast.putExtra("action", action);
//            if (action.equals("newemail"))
//                broadcast.removeExtra("originalMsgId");
//            broadcast.putExtra("result", false);
//            context.sendBroadcast(broadcast);
        } catch (IllegalArgumentException ie) {
            Log.dumpException(TAG, ie);
        }
    }

    static public void deleteSendFailNotiFromDB(Context context, long accountId, long messageId) {
        EmailLog.d(TAG, "deleteSendFailNotiFromDB");
        try {
            context.getContentResolver().delete(
                    NotificationUtil.SEND_FAIL_URI,
                    NotificationUtil.NotiColumns.ACCOUNT_ID + "=? AND "
                            + NotificationUtil.NotiColumns.MESSAGE_ID + "=?", new String[] {
                            String.valueOf(accountId), String.valueOf(messageId)
                    });
        } catch (IllegalArgumentException ie) {
            Log.dumpException(TAG, ie);
        }
    }

    static public long[] getAccountNMessageID(Context context) {
        Cursor cursor = null;
        long[] mID = null;
        try {
            String[] mProjection = new String[2];
            mProjection[0] = NotiColumns.ACCOUNT_ID;
            mProjection[1] = NotiColumns.MESSAGE_ID;
            cursor = context.getContentResolver().query(NEW_MESSAGE_URI, mProjection, null, null,
                    null);

            if (cursor == null) {
                return mID;
            }

            if (1 == cursor.getCount() && cursor != null) {
                cursor.moveToFirst();
                mID = new long[2];
                mID[0] = cursor.getLong(0);
                mID[1] = cursor.getLong(1);
            }
        } catch (IllegalArgumentException ie) {
            Log.dumpException(TAG, ie);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return mID;
    }

    static public void cancelSendFailNotification(Context context, long accountId) {
        EmailLog.d(TAG, "cancelSendFailNotification");
        try {
            context.getContentResolver().delete(NotificationUtil.SEND_FAIL_URI,
                    NotificationUtil.NotiColumns.ACCOUNT_ID + " = ?", new String[]{
                            String.valueOf(accountId)
                    });
        } catch (IllegalArgumentException ie) {
            Log.dumpException(TAG, ie);
        }
    }

    public static void cancelAllNotifications(Context context, long accountId) {
        EmailLog.d(TAG,"cancelAllNotifications");
        try {
            cancelNewMessage(context, accountId, false);
            cancelLoginFailedNotification(context, accountId);
            cancelAccountConfigutaionFailureNotification(context, accountId);
            cancelSendFailNotification(context, accountId);
            cancelOtherNotifications(context, accountId);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    static public void showPoliciesRequiredNotification(Context context, long accountId) {
        try {
            ContentValues cv = new ContentValues();
            context.getContentResolver().insert(
                    ContentUris.withAppendedId(POLICY_REQUIRED_URI, accountId), cv);
        } catch (IllegalArgumentException ie) {
            Log.dumpException(TAG, ie);
        }
    }

    static public void cancelOtherNotifications(Context context, long accountId) {
        try {
            context.getContentResolver().delete(
                    ContentUris.withAppendedId(ACCOUNT_DELETE_URI, accountId), null, null);
        } catch (IllegalArgumentException ie) {
            Log.dumpException(TAG, ie);
        }
    }

    static public void cancelPolicyNotification(Context context, long accountId) {
        try {
            context.getContentResolver().delete(
                    ContentUris.withAppendedId(REMOVE_POLICY_NOTI_URI, accountId), null, null);
        } catch (IllegalArgumentException ie) {
            Log.dumpException(TAG, ie);
        }
    }

    static public void cancelUntrustedCertificateNotification(Context context, long accountId) {
        try {
            context.getContentResolver().delete(
                    ContentUris.withAppendedId(UNTRUSTED_CERTIFICATE_ERROR_NOTI_URI, accountId), null, null);
        } catch (IllegalArgumentException ie) {
            Log.dumpException(TAG, ie);
        }
    }

    static public void showUntrustedCertificateNotification(Context context, long accountId) {
        if(!EmailFeature.isUntrustedCertificateFeatureEnabled(context)) {
            return;
        }
        try {
            ContentValues cv = new ContentValues();
            cv.put(NotiColumns.ACCOUNT_ID, accountId);
            context.getContentResolver().insert(UNTRUSTED_CERTIFICATE_ERROR_NOTI_URI, cv);
        } catch (IllegalArgumentException ie) {
            Log.dumpException(TAG, ie);
        }
    }

    static public void cancelMDMNoti(Context context) {
        try {
            context.getContentResolver().delete(REMOVE_MDM_NOTI_URI, null, null);
        } catch (IllegalArgumentException ie) {
            Log.dumpException(TAG, ie);
        }
    }

    public static void showPasswordExpiredNotification(Context context, long accountId,
            boolean isExpired) {
        EmailLog.d(TAG, "showPasswordExpiredNotification");
        try {
            ContentValues cv = new ContentValues();
            cv.put(IntentConst.EXTRA_LOCK_PASSWORD_EXPIRED, isExpired);
            context.getContentResolver().insert(
                    ContentUris.withAppendedId(PASSWORD_EXPIRED_URI, accountId), cv);
        } catch (IllegalArgumentException ie) {
            Log.dumpException(TAG, ie);
        }
    }

    public static void showSendingNotification(Context context) {
        try {
            ContentValues cv = new ContentValues();
            context.getContentResolver().insert(SENDING_URI, cv);
        } catch (NullPointerException npe) {
            if (context == null) {
                EmailLog.e(TAG, "Context is null.");
            } else {
                EmailLog.e(TAG, "context.getContentResolver() is null.");
            }
            npe.printStackTrace();
        } catch (IllegalArgumentException ie) {
            Log.dumpException(TAG, ie);
        }
    }
    
    public static void showSendingNotificationFinish(Context context, final long messageId,
            long accountId, String title) {

        deleteSendFailNotiFromDB(context, accountId, messageId);
        try {
            ContentValues cv = new ContentValues();
            cv.put(NotiColumns.ACCOUNT_ID, accountId);
            cv.put(NotiColumns.MESSAGE_ID, messageId);
            cv.put(NotiColumns.SUBJECT, title);
            context.getContentResolver().insert(SENDING_FINISH_URI, cv);
        } catch (IllegalArgumentException ie) {
            Log.dumpException(TAG, ie);
        }
    }

    public static void cancelSendingNotification(Context context) {
        try {
            context.getContentResolver().delete(REMOVE_SENDING_URI, null, null);
        } catch (NullPointerException npe) {
            if (context == null) {
                EmailLog.e(TAG, "Context is null.");
            } else {
                EmailLog.e(TAG, "context.getContentResolver() is null.");
            }
            npe.printStackTrace();
        } catch (IllegalArgumentException ie) {
            Log.dumpException(TAG, ie);
        }
    }
    public static boolean isVip(String address, Context context) {

        Cursor c = context.getContentResolver().query(EmailContent.VIPListColumns.CONTENT_URI,
                new String[] {
                        EmailContent.VIPListColumns.EMAIL_ADDRESS
                }, EmailContent.VIPListColumns.EMAIL_ADDRESS + "= ?" + " COLLATE NOCASE",
                new String[] {
                        address
                }, null);


        if (c == null) {
            Log.d(TAG, "isDuplicate : Cursor is null.");
            return false;
        }


        boolean res;

        if (c.getCount() == 0)
            res = false;
        else
            res = true;

        if (c != null) {
            c.close();
        }
        return res;
    }
    public static void showRuntimePermissionNoti(Context context, int permission_type, int permission_function) {
        ContentValues cv = new ContentValues();
        cv.put(BACKGROUND_RUNTIME_PERMISSION_TYPE, permission_type);
        cv.put(BACKGROUND_RUNTIME_PERMISSION_FUNCTION, permission_function);
        context.getContentResolver().insert(RUNTIME_PERMISSION_NOTI_URI, cv);
    }

    
}
