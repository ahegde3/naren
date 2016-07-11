
package com.samsung.android.emailcommon.utility;

import com.samsung.android.emailcommon.AccountManagerTypes;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

public class ExchangeAccountCounter {
    protected static ExchangeAccountCounter INSTANCE = null;
    public static ArrayList<String> mEASAccount = null;
    public ArrayList<String> mEASAccountFromManager = null;
    private static final String TAG = "ExchangeAccountCounter";

    private static final String APP_NAME_MAILWISE = "MailWise";
    private static final String APP_NAME_NINE = "Nine";
    private static final String APP_NAME_AQUAEMAIL = "AquaEmail";
    private static final String APP_NAME_TOUCHDOWN = "TouchDown";
    private static final String APP_NAME_ClOUD_MAGIC = "CloudMagic";
    private static final String APP_NAME_AOSP = "AOSP";
    private static final String APP_NAME_WEMAIL = "WeMail";
    private static final String APP_NAME_DEFAULT_3RDPARTY = "Default 3rd Party";

    public static ExchangeAccountCounter getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new ExchangeAccountCounter();
        return INSTANCE;
    }

    public void onBootCompleted(Context context) {
        android.accounts.Account[] accountManagerAccounts = AccountManager.get(context)
                .getAccounts();
        if (mEASAccount == null)
            mEASAccount = new ArrayList<String>();
        populateEASAccount(accountManagerAccounts, mEASAccount);
        for (String easAcc : mEASAccount) {
            EmailLog.d(TAG, "EAS Acc in AccMgr =" + easAcc);
        }
    }

    private void populateEASAccount(android.accounts.Account[] accountManagerAccounts,
            ArrayList<String> accList) {
        // TODO Auto-generated method stub
        for (android.accounts.Account act : accountManagerAccounts) {
            String accType = act.type;
            if (isGlobalEasAccount(accType)) {
                accList.add(accType);
            }
        }
    }

    private boolean isGlobalEasAccount(String accType) {
        // TODO Auto-generated method stub
        if (!accType.equals(AccountManagerTypes.TYPE_EXCHANGE)) {
            if (accType.equals(AccountManagerTypes.TYPE_EAS_MAILWISE)
                    || accType.equals(AccountManagerTypes.TYPE_EAS_NINE)
                    || accType.equals(AccountManagerTypes.TYPE_EAS_AQUA)
                    || accType.equals(AccountManagerTypes.TYPE_EAS_TOUCHDOWN)
                    || accType.equals(AccountManagerTypes.TYPE_EAS_CLOUDMAGIC)
                    || accType.contains("eas") || accType.contains("exchange")) {
                return true;
            }
        }
        return false;
    }

    public void onSystemAccountChanged(Context context) {

        android.accounts.Account[] accountManagerAccounts = AccountManager.get(context)
                .getAccounts();

        mEASAccountFromManager = new ArrayList<String>();

        if (mEASAccount == null) {
            EmailLog.d(TAG, "mEASAccount == null!!!");
            mEASAccount = new ArrayList<String>();
        }
        populateEASAccount(accountManagerAccounts, mEASAccountFromManager);
        /*EmailLog.d(TAG, "++++mEASAccount size =" + mEASAccount.size()
                + " mEASAccountFromManager size = " + mEASAccountFromManager.size());*/

        compareEASAccount(mEASAccount, mEASAccountFromManager, context);

        mEASAccount = mEASAccountFromManager;
        mEASAccountFromManager = null;

        /*EmailLog.d(TAG, "-----mEASAccount size =" + mEASAccount.size()
                + " mEASAccountFromManager size = " + mEASAccountFromManager.size());*/

    }

    private void compareEASAccount(ArrayList<String> exchangeAccount,
            ArrayList<String> exhcangeAccountFromManager, Context context) {
        // TODO Auto-generated method stub

        String packgeName = "";

        if (exchangeAccount.isEmpty()) {
            if (exhcangeAccountFromManager.isEmpty()) {
                EmailLog.d(TAG, "Both acc is null!!!");
                return;
            }
        }

        for (String accTypeFromManager : exhcangeAccountFromManager) {
            boolean isExist = false;
            EmailLog.d(TAG, "accTypeFromManager = " + accTypeFromManager);
            for (String accTypeInEmail : exchangeAccount) {
                EmailLog.d(TAG, "accTypeInEmail = " + accTypeInEmail);
                if (accTypeFromManager.equals(accTypeInEmail)) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                packgeName = getApplicationName(accTypeFromManager);
                sendBroadcastIntentToKLMS(packgeName, context);
            }
        }
    }

    private void sendBroadcastIntentToKLMS(String packgeName, Context context) {
        Intent intent = new Intent("com.samsung.action.knox.B2B_NOTIFICATION");
        intent.putExtra("packageName", packgeName);
        EmailLog.d(TAG, "sendBroadcastIntentToKLMS packgeName = " + packgeName);
        intent.putExtra("type", "eas");
        context.sendBroadcast(intent, "com.sec.knox.permission.KLMS_AGENT");
    }

    private String getApplicationName(String accType) {
        String packageName = APP_NAME_DEFAULT_3RDPARTY;
        if (accType.equals(AccountManagerTypes.TYPE_EAS_MAILWISE))
            packageName = APP_NAME_MAILWISE;
        else if (accType.equals(AccountManagerTypes.TYPE_EAS_NINE))
            packageName = APP_NAME_NINE;
        else if (accType.equals(AccountManagerTypes.TYPE_EAS_AQUA))
            packageName = APP_NAME_AQUAEMAIL;
        else if (accType.equals(AccountManagerTypes.TYPE_EAS_TOUCHDOWN))
            packageName = APP_NAME_TOUCHDOWN;
        else if (accType.equals(AccountManagerTypes.TYPE_EAS_CLOUDMAGIC))
            packageName = APP_NAME_ClOUD_MAGIC;
        else if (accType.equals(AccountManagerTypes.TYPE_EAS_AOSP))
            packageName = APP_NAME_AOSP;
        else if (accType.equals(AccountManagerTypes.TYPE_EAS_WEMAIL))
            packageName = APP_NAME_WEMAIL;

        return packageName;
    }

}
