
package com.samsung.android.emailcommon.packages;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Parcelable;
import android.widget.Toast;

import com.samsung.android.emailcommon.EmailPackage;
import com.samsung.android.emailcommon.IntentConst;
import com.samsung.android.emailcommon.provider.EmailContent.Attachment;
import com.samsung.android.emailcommon.utility.Log;
import com.samsung.android.emailcommon.utility.Utility;

public class MessageCompose {
    
    final static private String MESSAGELISTXL = "MessageListXL";

    final static private String TAG = "MessageCompose";
    final static private String fabBtnPath = Environment.getExternalStorageDirectory()
            + "/.EmailTempImage/fabImage/fab_btn.png";
    public static final int[] mFontValuePt = new int[] {
            8, 10, 12, 14, 18, 24, 36
    };
    final static public int RESULT_MULTITASKING = 1001;

    static public String getFabBtnPath() {
        return fabBtnPath;
    }

    // static public void actionCompose(Context context, long accountId) {
    // ComponentName cn = new ComponentName(EmailPackage.PKG_COMPOSER,
    // EmailPackage.ComposerActivity);
    // Intent intent = new Intent();
    // intent.setComponent(cn);
    // intent.setAction("android.intent.action.MAIN");
    // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    // intent.putExtra(IntentConst.EXTRA_ACCOUNT_ID, accountId);
    // try {
    // context.startActivity(intent);
    // } catch (ActivityNotFoundException e) {
    // Toast.makeText(context, "package not installed", Toast.LENGTH_SHORT);
    // Log.e(TAG, "activity not found");
    // }
    // }
    static public void actionComposeNewTask(Context context, long accountId) {
        Intent intent = new Intent();
        intent.setClassName(context, EmailPackage.ComposerActivity);
        // ComponentName cn = new ComponentName(EmailPackage.PKG_UI, EmailPackage.ComposerActivity);
        // intent.setComponent(cn);
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(IntentConst.EXTRA_ACCOUNT_ID, accountId);
        intent.putExtra(IntentConst.ACTION_FROM_WIDGET, true);
        if (Utility.isTabletModel()) {
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "package not installed", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "activity not found");
        }
    }

//    static public void actionCompose(Activity activity, int code, long accountId) {
//        actionCompose(activity, code, accountId, "none");
//    }

    static public void actionCompose(Activity activity, int code, long accountId /*,String currentScreen*/) {
        if (activity == null)
            return;
        ComponentName cn = new ComponentName(EmailPackage.PKG_UI, EmailPackage.ComposerActivity);
        Intent intent = new Intent();
        intent.setComponent(cn);
        intent.setAction(Intent.ACTION_SEND);
        // intent.putExtra(IntentConst.EXTRA_CURRENT_SCREEN, currentScreen);
        intent.putExtra(IntentConst.EXTRA_FROM_WITHIN_APP, true);
        intent.putExtra(IntentConst.EXTRA_ACCOUNT_ID, accountId);
        try {
            activity.startActivityForResult(intent, code);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "package not installed", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "activity not found");
        }
    }

    static public void actionCompose(Activity activity, Intent intent, int code) {
        try {
            activity.startActivityForResult(intent, code);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "package not installed", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "activity not found");
        }
    }

    static public void actionEditTemp(Activity activity, int code, int x, int y) {
        actionEditTemp(activity, code, x, y, "none");
    }

    static public void actionEditTemp(Activity activity, int code, int x, int y,
            String currentScreen) {
        if (activity == null)
            return;
        ComponentName cn = new ComponentName(EmailPackage.PKG_UI, EmailPackage.ComposerActivity);
        Intent intent = new Intent();
        intent.setComponent(cn);
        intent.setAction(IntentConst.ACTION_EDIT_DRAFT);
        intent.putExtra(IntentConst.EXTRA_ACCOUNT_ID, (long) -1);
        intent.putExtra(IntentConst.EXTRA_MESSAGE_ID, (long) 0);
        intent.putExtra(IntentConst.EXTRA_FAB_CENTER_X, x);
        intent.putExtra(IntentConst.EXTRA_FAB_CENTER_Y, y);
        intent.putExtra(IntentConst.EXTRA_CURRENT_SCREEN, currentScreen);
        intent.putExtra(IntentConst.EXTRA_FROM_WITHIN_APP, true);
        try {
            activity.startActivityForResult(intent, code);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "package not installed", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "activity not found");
        }
    }

    static public void actionEditDraft(Activity activity, int code, long accountId, long messageId,
            boolean withInApp) {
        if (activity == null)
            return;
        ComponentName cn = new ComponentName(EmailPackage.PKG_UI, EmailPackage.ComposerActivity);
        Intent intent = new Intent();
        intent.setComponent(cn);
        intent.setAction(IntentConst.ACTION_EDIT_DRAFT);
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(IntentConst.EXTRA_ACCOUNT_ID, accountId);
        intent.putExtra(IntentConst.EXTRA_MESSAGE_ID, messageId);
        if (withInApp) {
            intent.putExtra(IntentConst.EXTRA_FROM_WITHIN_APP, true);
        }
        try {
            activity.startActivityForResult(intent, code);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "package not installed", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "activity not found");
        }
    }

    static public void actionReply(Activity activity, int code, long messageId, boolean replyAll,
            ClipData data) {
        if (activity == null)
            return;
        ComponentName cn = new ComponentName(EmailPackage.PKG_UI, EmailPackage.ComposerActivity);
        Intent intent = new Intent();
        intent.setComponent(cn);
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(replyAll ? IntentConst.ACTION_REPLY_ALL : IntentConst.ACTION_REPLY);
        intent.putExtra(IntentConst.EXTRA_MESSAGE_ID, messageId);
        intent.putExtra(IntentConst.COMPOSE_EXTRA_REPLYALL, replyAll);
        intent.setClipData(data);
        try {
            activity.startActivityForResult(intent, code);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "package not installed", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "activity not found");
        }
    }

    public static void actionReply(Context context, long messageId, boolean replyAll,
            boolean isMultiTask) {
        ComponentName cn = new ComponentName(EmailPackage.PKG_UI, EmailPackage.ComposerActivity);
        Intent intent = new Intent();
        intent.setComponent(cn);
        if (isMultiTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
        intent.setAction(replyAll ? IntentConst.ACTION_REPLY_ALL : IntentConst.ACTION_REPLY);
        intent.putExtra(IntentConst.EXTRA_MESSAGE_ID, messageId);
        intent.putExtra(IntentConst.COMPOSE_EXTRA_REPLYALL, replyAll);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "package not installed", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "activity not found");
        }
    }

    public static void actionReply(Activity activity, int code, long messageId, boolean replyAll) {
        actionReply(activity, code, messageId, replyAll, (String) null);
    }

    public static void actionReply(Activity activity, int code, long messageId, boolean replyAll,
            String bodyText, Attachment[] parcelAttachment, boolean isOpaqueSigned) {
        if (activity == null)
            return;
        ComponentName cn = new ComponentName(EmailPackage.PKG_UI, EmailPackage.ComposerActivity);
        Intent intent = new Intent();
        intent.setComponent(cn);
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(replyAll ? IntentConst.ACTION_REPLY_ALL : IntentConst.ACTION_REPLY);
        intent.putExtra(IntentConst.EXTRA_MESSAGE_ID, messageId);
        intent.putExtra(IntentConst.COMPOSE_EXTRA_REPLYALL, replyAll);
        if (bodyText != null)
            intent.putExtra(IntentConst.EXTRA_OPAQUE_SIGN_BODY, bodyText);
        if (parcelAttachment != null) {
            intent.putExtra(IntentConst.EXTRA_OPAQUE_SIGN_ATTACHMENTS, parcelAttachment);
        }
        intent.putExtra(IntentConst.EXTRA_OPAQUE_SIGN_FLAG, isOpaqueSigned);
        try {
            activity.startActivityForResult(intent, code);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "package not installed", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "activity not found");
        }
    }

    public static void actionReply(Activity activity, int code, long messageId, boolean replyAll,
            String bodyText) {
        if (activity == null)
            return;
        ComponentName cn = new ComponentName(EmailPackage.PKG_UI, EmailPackage.ComposerActivity);
        Intent intent = new Intent();
        intent.setComponent(cn);
        
        if(MESSAGELISTXL.equals(activity.getClass().getSimpleName())){
            intent.putExtra(IntentConst.EXTRA_FROM_WITHIN_APP, true);
        }
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(replyAll ? IntentConst.ACTION_REPLY_ALL : IntentConst.ACTION_REPLY);
        intent.putExtra(IntentConst.EXTRA_MESSAGE_ID, messageId);
        intent.putExtra(IntentConst.COMPOSE_EXTRA_REPLYALL, replyAll);
        if (bodyText != null)
            intent.putExtra(Intent.EXTRA_TEXT, bodyText);
        try {
            activity.startActivityForResult(intent, code);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "package not installed", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "activity not found");
        }
    }

    public static void actionMeetingResponse(Activity activity, int code, long messageId,
            int response) {
        if (activity == null)
            return;
        ComponentName cn = new ComponentName(EmailPackage.PKG_UI, EmailPackage.ComposerActivity);
        Intent intent = new Intent();
        intent.setComponent(cn);
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(IntentConst.ACTION_MEETING_RESPONSE);
        intent.putExtra(IntentConst.EXTRA_MESSAGE_ID, messageId);
        intent.putExtra(IntentConst.EXTRA_MEETING_RESPONSE, response);
        try {
            activity.startActivityForResult(intent, code);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "package not installed", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "activity not found");
        }
    }

    public static void actionForward(Activity activity, int code, long messageId) {
        actionForward(activity, code, messageId, false, false);
    }

    public static void actionForward(Activity activity, int code, long messageId,
            boolean addRecipients, boolean addAttachment) {
        actionForward(activity, code, messageId, addRecipients, addAttachment, null, null, false);
    }

    public static void actionForward(Activity activity, int code, long messageId,
            boolean addRecipients, boolean addAttachment, String bodyText,
            Parcelable[] parcelAttachment, boolean isOpaqueSigned) {
        if (activity == null)
            return;

        ComponentName cn = new ComponentName(EmailPackage.PKG_UI, EmailPackage.ComposerActivity);
        Intent intent = new Intent();
        intent.setComponent(cn);
        intent.setAction(IntentConst.ACTION_FORWARD);
        intent.putExtra(IntentConst.EXTRA_MESSAGE_ID, messageId);
        intent.putExtra(IntentConst.COMPOSE_EXTRA_FORWARD_RECIPIENTS, addRecipients);
        intent.putExtra(IntentConst.COMPOSE_EXTRA_ADD_ATTACHMENTS, addAttachment);
        if (bodyText != null) {
            intent.putExtra(IntentConst.EXTRA_OPAQUE_SIGN_BODY, bodyText);
        }
        if (parcelAttachment != null) {
            intent.putExtra(IntentConst.EXTRA_OPAQUE_SIGN_ATTACHMENTS, parcelAttachment);
        }
        intent.putExtra(IntentConst.EXTRA_OPAQUE_SIGN_FLAG, isOpaqueSigned);
        try {
            activity.startActivityForResult(intent, code);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "package not installed", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "activity not found");
        }
    }

    public static void actionProposeNewTime(Activity activity, int code, long messageId) {
        if (activity == null)
            return;
        ComponentName cn = new ComponentName(EmailPackage.PKG_UI, EmailPackage.ComposerActivity);
        Intent intent = new Intent();
        intent.setComponent(cn);
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(IntentConst.ACTION_PROPOSE_NEW_TIME);
        intent.putExtra(IntentConst.EXTRA_MESSAGE_ID, messageId);
        try {
            activity.startActivityForResult(intent, code);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "package not installed", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "activity not found");
        }
    }

    public static void actionQuickReply(Activity activity, String data, String signature) {
        if (activity == null)
            return;
        ComponentName cn = new ComponentName(EmailPackage.PKG_UI, EmailPackage.QuickReplyService);
        Intent intent = new Intent();
        intent.setComponent(cn);
        intent.setAction(IntentConst.ACTION_QUICK_REPLY_BACKGROURND);
        intent.putExtra("data", data);
        intent.putExtra("signature", signature);
        try {
            activity.startService(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "package not installed", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "activity not found");
        }
    }

    public static void actionBackgroundSend(Context context, Intent intent) {
        ComponentName cn = new ComponentName(EmailPackage.PKG_UI,
                EmailPackage.BackgroundSendService);
        intent.setComponent(cn);
        intent.setAction("broadcast_receiver");
        intent.putExtra(Intent.EXTRA_INTENT, intent);
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startService(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "package not installed", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "activity not found");
        }
    }
}
