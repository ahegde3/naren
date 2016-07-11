/*
 * Copyright (C) 2011 The Android Open Source Project
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

package com.samsung.android.emailcommon.utility;

import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.samsung.android.emailcommon.AccountCache;
import com.samsung.android.emailcommon.Logging;
import com.samsung.android.emailcommon.NotificationUtil;

import com.samsung.android.emailcommon.provider.EmailContent;
import com.samsung.android.emailcommon.provider.EmailContent.Account;
import com.sec.android.emergencymode.EmergencyManager;

import java.util.List;

public class AccountReconciler {
    /**
     * Compare our account list (obtained from EmailProvider) with the account
     * list owned by AccountManager. If there are any orphans (an account in one
     * list without a corresponding account in the other list), delete the
     * orphan, as these must remain in sync. Note that the duplication of
     * account information is caused by the Email application's incomplete
     * integration with AccountManager. This function may not be called from the
     * main/UI thread, because it makes blocking calls into the account manager.
     * 
     * @param context The context in which to operate
     * @param emailProviderAccounts the exchange provider accounts to work from
     * @param accountManagerAccounts The account manager accounts to work from
     * @param resolver the content resolver for making provider updates
     *            (injected for testability)
     */
    public static boolean reconcileAccounts(Context context, List<Account> emailProviderAccounts,
            android.accounts.Account[] accountManagerAccounts, ContentResolver resolver) {
        // First, look through our EmailProvider accounts to make sure there's a
        // corresponding
        // AccountManager account
        boolean accountsDeleted = false;
        boolean isUpsm = EmergencyManager.isEmergencyMode(context);

        if(emailProviderAccounts == null)
            return false;

        if (isUpsm && accountManagerAccounts != null && accountManagerAccounts.length == 0) {
            EmailLog.d(Logging.LOG_TAG, "accountManagerAccounts is empty");
            return false;
        }

        for (Account providerAccount : emailProviderAccounts) {
        	if( providerAccount == null){
        		return false;
        	}
            String providerAccountName = providerAccount.mEmailAddress;
            boolean found = false;
            for (android.accounts.Account accountManagerAccount : accountManagerAccounts) {
                if (accountManagerAccount.name.equalsIgnoreCase(providerAccountName)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                if ((providerAccount.mFlags & Account.FLAGS_INCOMPLETE) != 0) {
                	EmailLog.w(Logging.LOG_TAG,
                            "Account reconciler noticed incomplete account; ignoring");
                    continue;
                }
                try {
                    // This account has been deleted in the AccountManager!
                    NotificationUtil.cancelAllNotifications(context, providerAccount.mId);
                    EmailLog.d(Logging.LOG_TAG,
                            "Account deleted in AccountManager; deleting from provider: "
                            /* + providerAccountName */);
                    //KNOX_EAS_START Single
                    if(Utility.isSamsungAccount(context, providerAccount.mId))
                        Utility.sendReportToAgent(Utility.SSO_DEL_ACCOUNT, context, providerAccountName);
                    //KNOX_EAS_END Single
                    // Delete account attachments and message body files
                    AttachmentUtilities.deleteAllAccountAttachmentFilesUri(context,
                            providerAccount.mId);
                    BodyUtilites.deleteAllAccountBodyFilesUri(context, providerAccount.mId);
                    resolver.delete(ContentUris.withAppendedId(
                                    Account.CONTENT_URI, providerAccount.mId),
                            null, null);
                    // AccountCache.sAccountType.remove(providerAccount.mId);
                    AccountCache.removeAccountType(providerAccount.mId);
                    accountsDeleted = true;
                    EmailLog.logToDropBox(Logging.LOG_TAG, "account="
                            + providerAccount.mEmailAddress + " source=reconciler" + " type="
                            + providerAccount.mAccountType + " action=deleted");
                    //delete all alias names for the account in SMIMECertificate table
                    Uri smimeUri = EmailContent.SMIMECertificate.CONTENT_URI.buildUpon().appendPath(Long.toString(providerAccount.mId)).build();
                    context.getContentResolver().delete(smimeUri, null, null);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // Now, look through AccountManager accounts to make sure we have a
        // corresponding cached EAS
        // account from EmailProvider
        for (android.accounts.Account accountManagerAccount : accountManagerAccounts) {
            String accountManagerAccountName = accountManagerAccount.name;
            boolean found = false;
            for (Account cachedEasAccount : emailProviderAccounts) {
                if (cachedEasAccount.mEmailAddress.equalsIgnoreCase(accountManagerAccountName)) {
                    found = true;
                }
            }
            if (!found) {
                // This account has been deleted from the EmailProvider database
            	EmailLog.d(Logging.LOG_TAG,
                        "Account deleted from provider; deleting from AccountManager: "
                        /*+ accountManagerAccountName*/);
                // Delete the account
                try {
                    AccountManager.get(context).removeAccount(accountManagerAccount, null, null);
                } catch (SecurityException se) {
                    se.printStackTrace();
                }

                // try {
                // // Note: All of the potential errors from removeAccount() are
                // simply logged
                // // here, as there is nothing to actually do about them.
                // blockingResult.getResult();
                // } catch (OperationCanceledException e) {
                // Log.w(Logging.LOG_TAG, e.toString());
                // } catch (AuthenticatorException e) {
                // Log.w(Logging.LOG_TAG, e.toString());
                // } catch (IOException e) {
                // Log.w(Logging.LOG_TAG, e.toString());
                // }
                accountsDeleted = true;
            }
        }

        Account.setDefaultAccountWhenAccountDeleted(context);

        if(!EmailRuntimePermission.hasPermissions(context,EmailRuntimePermission.PERMISSION_CONTACTS)){
            NotificationUtil.showRuntimePermissionNoti(context, EmailRuntimePermission.PERM_REQUEST_TYPE_CONTACTS,
                    EmailRuntimePermission.PERMISSION_FUNCTION_CONTACT_INFO);
        } else {
            Cursor c = null;
            try {
                c = context.getContentResolver().query(NotificationUtil.REFRESH_PEOPLE_STRIPE_NOTI_URI, null, null, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (c != null) c.close();
            }
        }

        return accountsDeleted;
    }
    
  //KNOX_EAS_START Single
    public static boolean reconcileSingleAccounts(Context context, List<Account> emailProviderAccounts,
            android.accounts.Account[] accountManagerAccounts, ContentResolver resolver) {
        // First, look through our EmailProvider accounts to make sure there's a
        // corresponding
        // AccountManager account
        boolean accountsDeleted = false;

        android.accounts.Account[] accountMgrMySingleList = AccountManager.get(context).getAccountsByType("com.seven.Z7.work");

        if(emailProviderAccounts == null) 
            return accountsDeleted = false;

        for (Account providerAccount : emailProviderAccounts) {
            if( providerAccount == null){
                return accountsDeleted = false;
            }
            String providerAccountName = providerAccount.mEmailAddress;
            boolean found = false;
            for (android.accounts.Account accountManagerSingleAccount : accountMgrMySingleList) {
                if (accountManagerSingleAccount.name.equalsIgnoreCase(providerAccountName)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                if ((providerAccount.mFlags & Account.FLAGS_INCOMPLETE) != 0) {
                	EmailLog.w(Logging.LOG_TAG,
                            "Account reconciler noticed incomplete account; ignoring");
                    continue;
                }
                /**
                 * For SNC accounts, account deletion from account manager is
                 * done only if the account is deleted from Email Provider,
                 * because SNC accounts need to deleted from the server first
                 */
                try {
                    boolean isSncAccount = (providerAccount.mHostAuthRecv != null
                            && "snc".equals(providerAccount.mHostAuthRecv.mProtocol) ? true
                                    : false);
                    // This account has been deleted in the AccountManager!
                    if (!isSncAccount) {
                        //KNOX_EAS_START Single
                        for (android.accounts.Account accountManagerAccount : accountManagerAccounts) {
                            if (accountManagerAccount.name.equalsIgnoreCase(providerAccountName)) {
                                // Delete the account
                                if(Utility.isSamsungAccount(context, providerAccount.mId)) {
                                    try {
                                        AccountManager.get(context).removeAccount(accountManagerAccount, null, null);
                                        EmailLog.d(Logging.LOG_TAG,
                                                "MySingle account deleted in AccountManager; deleting eas account from AccountManager:  ");
                                    } catch (SecurityException se) {
                                        se.printStackTrace();
                                    }
                                }
                                break;
                            }
                        }
                        //KNOX_EAS_END Single
                        NotificationUtil.cancelAllNotifications(context, providerAccount.mId);
                        if(Utility.isSamsungAccount(context, providerAccount.mId)) {
                        resolver.delete(ContentUris.withAppendedId(
                                Account.CONTENT_URI, providerAccount.mId),
                                null, null);
                        
                        AccountCache.removeAccountType(providerAccount.mId);
                        
//                        AccountCache.sAccountType.remove(providerAccount.mId);
                        
                        EmailLog.d(Logging.LOG_TAG,
                                "MySingle Account deleted in AccountManager; deleting eas account  from provider: ");
                        accountsDeleted = true;
                        Account.setDefaultAccountWhenAccountDeleted(context);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
     
        //No need to send report.  
/*        for (android.accounts.Account accountManagerAccount : accountMgrMySingleList) {
            String accountManagerAccountName = accountManagerAccount.name;
            boolean found = false;
            for (Account cachedEasAccount : emailProviderAccounts) {
                if (cachedEasAccount.mEmailAddress.equalsIgnoreCase(accountManagerAccountName)) {
                    found = true;
                }
            }
            if (!found) {
                Utility.sendReportToAgent(Utility.SSO_DEL_ACCOUNT, context, accountManagerAccountName);
                Log.d(Logging.LOG_TAG,
                        "EAS account deleted from provider; Requesting deleting mysingle account from AccountManager: " + accountManagerAccountName);
                accountsDeleted = true;
            }
        }*/
        return accountsDeleted;
    }
}
