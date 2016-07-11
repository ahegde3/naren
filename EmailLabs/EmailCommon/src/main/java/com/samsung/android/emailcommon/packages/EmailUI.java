package com.samsung.android.emailcommon.packages;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.samsung.android.emailcommon.EmailPackage;
import com.samsung.android.emailcommon.IntentConst;
import com.samsung.android.emailcommon.provider.EmailContent;
import com.samsung.android.emailcommon.utility.Log;


public class EmailUI implements IntentConst {

    final static private String TAG = "EmailUI";

    public static Intent createDefaultIntent() {
        Intent i = new Intent();
        ComponentName cn = new ComponentName(EmailPackage.PKG_UI, EmailPackage.Message);
        i.setComponent(cn);
        // i.setAction("android.intent.action.SHROTCUT");
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return i;
    }

    public static Intent createPeopleIntent() {
        Intent i = new Intent();
        ComponentName cn = new ComponentName(EmailPackage.PKG_UI, EmailPackage.Message);
        i.setComponent(cn);
        // i.setAction("android.intent.action.SHROTCUT");
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return i;
    }

    public static Intent createWidgetIntent() {
        Intent i = new Intent();
        ComponentName cn = new ComponentName(EmailPackage.PKG_UI, EmailPackage.Message);
        i.setComponent(cn);
        // i.setAction("android.intent.action.SHROTCUT");
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(IntentConst.ACTION_FROM_WIDGET, true);
        return i;
    }

    public static Intent createReminderHandleIntent() {
        Intent i = new Intent();
        ComponentName cn = new ComponentName(EmailPackage.PKG_UI, EmailPackage.ReminderHandleActivity);
        i.setComponent(cn);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return i;
    }

    public static Intent createReminderIntent() {
        Intent i = new Intent();
        ComponentName cn = new ComponentName(EmailPackage.PKG_UI, EmailPackage.Reminder);
        i.setComponent(cn);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    public static Intent createDeleteIntentForNewMessageNoti(long accountId) {
        Intent i = new Intent();
        i.setAction(IntentConst.ACTION_CLEAR_NOTIFICATION);
        i.putExtra(IntentConst.EXTRA_ACCOUNT_ID, accountId);
        i.putExtra(IntentConst.EXTRA_IS_LAUNCHED, true);
        i.putExtra(IntentConst.EXTRA_TYPE, 0);
        return i;
    }

    public static Intent createOpenAccountMailboxIntent(long accountId,
                                                        long mailboxId) {


        Intent i = createDefaultIntent();

        long nSingTopFlag = 1;
        i.putExtra("IntentSingTop", nSingTopFlag);
        if (accountId != -1) {
            i.putExtra(EXTRA_ACCOUNT_ID, accountId);
            i.putExtra(EXTRA_OPEN_BY_SELF, true);
        }
        if (mailboxId != -1) {
            i.putExtra(EXTRA_MAILBOX_ID, mailboxId);
        }
        return i;
    }

    public static Intent createOpenAccountMailboxIntentByLoadMore(long accountId,
                                                                  long mailboxId) {


        Intent i = createWidgetIntent();

        long nSingTopFlag = 1;
        i.putExtra("IntentSingTop", nSingTopFlag);
        if (accountId != -1) {
            i.putExtra(EXTRA_ACCOUNT_ID, accountId);
            i.putExtra(EXTRA_OPEN_BY_SELF, true);
        }
        if (mailboxId != -1) {
            i.putExtra(EXTRA_MAILBOX_ID, mailboxId);
        }
        return i;
    }

    public static Intent createOpenMessageviewIntent(long accountId, long mailboxId, long messageId) {
        Intent i = createWidgetIntent();
        if (accountId != -1) {
            i.putExtra(EXTRA_ACCOUNT_ID, accountId);
            i.putExtra(EXTRA_MAILBOX_ID, mailboxId);
            i.putExtra(EXTRA_MESSAGE_ID, messageId);
            i.putExtra(EXTRA_OPEN_BY_SELF, true);
            i.putExtra(EXTRA_OPEN_VIEW, true);
        }
        return i;
    }

    public static Intent createOpenMessageviewIntentByReminder(long accountId, long mailboxId, long messageId) {
        Intent i = createWidgetIntent();
        if (accountId != -1) {
            i.putExtra(EXTRA_ACCOUNT_ID, accountId);
            i.putExtra(EXTRA_MAILBOX_ID, mailboxId);
            i.putExtra(EXTRA_MESSAGE_ID, messageId);
            i.putExtra(EXTRA_OPEN_BY_SELF, false);
            i.putExtra(EXTRA_OPEN_VIEW, true);
        }
        return i;
    }

    public static Intent createOpenMessageviewIntentByThread(long accountId, long mailboxId, long threadId) {
        Intent i = createWidgetIntent();
//		i.setAction(Intent.ACTION_VIEW);
        if (accountId != -1) {
            i.putExtra(EXTRA_ACCOUNT_ID, accountId);
            i.putExtra(EXTRA_MAILBOX_ID, mailboxId);
            i.putExtra(EXTRA_THREAD_ID, threadId);
            i.putExtra(EXTRA_OPEN_BY_SELF, true);
            i.putExtra(EXTRA_OPEN_VIEW, true);
        }
        return i;
    }

    public static Intent createShowPasswordChangedIntent(
            long accountId, String emailAddress) {
        Intent i = createDefaultIntent();
        i.putExtra(EXTRA_ACCOUNT_ID, accountId);
        i.putExtra(EXTRA_PASSWORD_EXPIRED, true);
        i.putExtra(EXTRA_EMAIL_ADDRESS, emailAddress);
        return i;
    }

    public static Intent createReplyIntent(long accountId, long mailboxId, long messageId) {
        Intent i = createReminderHandleIntent();
        if (accountId != -1) {
            i.setAction(ACTION_REPLY);
            i.putExtra(EXTRA_ACCOUNT_ID, accountId);
            i.putExtra(EXTRA_MAILBOX_ID, mailboxId);
            i.putExtra(EXTRA_MESSAGE_ID, messageId);
        }
        return i;
    }

    public static Intent createdeleteIntent(long accountId, long mailboxId, long messageId) {
        Intent i = createReminderIntent();
        if (accountId != -1) {
            i.setAction(ACTION_DELETE_MESSAGE);
            i.putExtra(EXTRA_ACCOUNT_ID, accountId);
            i.putExtra(EXTRA_MAILBOX_ID, mailboxId);
            i.putExtra(EXTRA_MESSAGE_ID, messageId);
        }
        return i;
    }


    public static void actionOpenAccountInbox(Context context, long accountId) {
        try {
            context.startActivity(createOpenAccountMailboxIntentByLoadMore(accountId, -1));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "package not installed", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "activity not found");
        }
    }

    public static void actionOpenMailbox(Context context, long accountId,
                                         long mailboxId) {
        try {
            context.startActivity(createOpenAccountMailboxIntent(accountId, mailboxId));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "package not installed", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "activity not found");
        }
    }

    public static void actionOpenMessage(Context context, long accountId,
                                         long mailboxId, long messageId) {
        try {
            context.startActivity(createOpenMessageviewIntent(accountId, mailboxId, messageId));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "package not installed", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "activity not found");
        }
    }

    public static void actionOpenMessageByThread(Context context, long accountId,
                                                 long mailboxId, long threadId) {
        try {
            context.startActivity(createOpenMessageviewIntentByThread(accountId, mailboxId, threadId));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "package not installed", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "activity not found");
        }
    }

    /**
     * createAccountMoreSettingsIntent -
     *
     * @param context
     * @param accountId
     * @param loginWarningAccountName
     * @return
     */

    public static Intent createAccountMoreSettingsIntent(long accountId,
                                                         String address) {
        Intent i = new Intent();
        ComponentName cn = new ComponentName(EmailPackage.PKG_UI, EmailPackage.AccountSettingsXL);
        i.setComponent(cn);
        i.setAction(ACTION_ENTER_EACH_ACCOUNT_SETTINGS_MORE);
        i.putExtra(EXTRA_EACH_SETTINGS_ACCOUNT_ID, accountId);
        i.putExtra(EXTRA_EACH_SETTINGS_ADDRESS, address);
        return i;

    }

    public static Intent actionUpdateSecurityIntent(Context context, long accountId,
                                                    boolean showDialog) {
        Log.e(TAG, "actionUpdateSecurityIntent");

        boolean isUntrustedCertificateHold = EmailContent.Account.isOnUntrustedCertificateHold(context, accountId);
        //Lets see why we got this
        Intent i = new Intent();
        ComponentName cn = null;
        if (isUntrustedCertificateHold) {
            cn = new ComponentName(EmailPackage.PKG_UI, EmailPackage.Message);
            i.putExtra(EXTRA_OPEN_BY_SELF, true);
        } else {
            cn = new ComponentName(EmailPackage.PKG_UI, EmailPackage.AccountSecurity);
        }
        i.setComponent(cn);
        i.putExtra(EXTRA_ACCOUNT_ID, accountId);
        i.putExtra(EXTRA_SHOW_DIALOG, showDialog);
        i.putExtra(EXTRA_UNTRUSTED_CERTIFICATE_HOLD, isUntrustedCertificateHold);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }

    public static Intent actionDevicePasswordExpirationIntent(long accountId,
                                                              boolean expired) {
        Intent intent = new Intent();
        ComponentName cn = new ComponentName(EmailPackage.PKG_UI, EmailPackage.AccountSecurity);
        intent.setComponent(cn);
        intent.putExtra(EXTRA_ACCOUNT_ID, accountId);
        intent.putExtra(expired ? EXTRA_LOCK_PASSWORD_EXPIRED : EXTRA_LOCK_PASSWORD_EXPIRING, true);
        return intent;
    }

//    public static Intent createOpenManageAppPermissionIntent() {
//        Intent i = new Intent().setAction(IntentConst.ACTION_APPLICATION_DETAILS_SETTINGS)
//                .setData(Uri.fromParts("package", EmailPackage.PKG_BASE, null));
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        return i;
//    }
}
