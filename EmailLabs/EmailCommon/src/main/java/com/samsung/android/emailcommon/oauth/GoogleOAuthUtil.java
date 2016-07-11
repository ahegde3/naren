/*
 * Copyright (C) 2014 Samsung Telecommunications America
 *
 */

package com.samsung.android.emailcommon.oauth;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;

import com.samsung.android.emailcommon.Logging;
import com.samsung.android.emailcommon.combined.common.VendorPolicyLoader.OAuthProvider;
import com.samsung.android.emailcommon.mail.AuthenticationFailedException;
import com.samsung.android.emailcommon.provider.EmailContent;
import com.samsung.android.emailcommon.provider.EmailContent.Account;
import com.samsung.android.emailcommon.provider.EmailContent.HostAuth;
import com.samsung.android.emailcommon.provider.EmailContent.HostAuthColumns;
import com.samsung.android.emailcommon.provider.ImapConstants;
import com.samsung.android.emailcommon.utility.EmailLog;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class GoogleOAuthUtil {

    private static final String TAG = "GoogleOAuthUtil";
    private static final String SCOPE = "oauth2:https://mail.google.com/";
    /** 
    id: An identifier that is unique within this file. It can be referenced from providers.xml
      if a email provider is known to allow OAuth authentication.
    label: The text that will be displayed to the user when selecting a provider to access
      for OAuth authentication.

    The rest of these values are determined when registering your app with a provider.
    auth_endpoint: The uri to access when making the authentication request.
    token_endpoint: The uri to access when getting the initial access and refresh tokens.
    refresh_endpoint: The uri to access when refreshing the access token.
    response_type: Value sent as "response_type" when making the authentication request.
    redirect_uri: Value sent as "redirect_uri" when making the authentication request.
    scope: Value(s) sent as "scope" when making the authentication request.
    state: Value sent as "state" when making the authentication request.
    client_id: Value sent as "client_id" when making the authentication request, and when
      getting and refreshing the access token.

     //MNO B2B: To generate client_id go to 
     1. https://console.developers.google.com/project
     2. Create the Client id by entering the system platform key
     3. Enter the generated client key here.

    client_secret: Value sent as "client_secret" when getting and refreshing the access token.
     **/
    private static final String PROVIDER_LABEL="Google";
    private static final String AUTH_ENDPOINT="https://accounts.google.com/o/oauth2/auth";
    private static final String TOKEN_ENDPOINT="https://accounts.google.com/o/oauth2/token";
    private static final String REFRESH_ENDPOINT="https://accounts.google.com/o/oauth2/token";
    private static final String RESPONSE_TYPE="code";
    private static final String REDIRECT_URI="http://localhost/";
    private static final String OAUTH_SCOPE="https://mail.google.com https://www.googleapis.com/auth/userinfo.email";
    private static final String STATE="state";
    private static final String CLIENT_ID="807804364716-d0sp56n7j4dpoo2rklobadf6u8f736hf.apps.googleusercontent.com";
    private static final String CLIENT_SECRET="REDACTED";

    public final static int REQUEST_GET_OAUTH_TOKEN = 2020;

    public static final String EXTRA_EMAIL_ADDRESS = "email_address";
    public static final String EXTRA_PROVIDER = "provider";
    public static final String EXTRA_PROVIDER_ID = "provider_id";
    public static final String EXTRA_AUTHENTICATION_CODE = "authentication_code";

    public static final int LOADER_ID_OAUTH_TOKEN = 1;

    public static final String EXTRA_OAUTH_ACCESS_TOKEN = "accessToken";
    public static final String EXTRA_OAUTH_REFRESH_TOKEN = "refreshToken";
    public static final String EXTRA_OAUTH_EXPIRES_IN = "expiresIn";
    public static final String EXTRA_OAUTH_EMAIL_ID = "emailId";

    public static final int RESULT_OAUTH_SUCCESS = Activity.RESULT_FIRST_USER + 0;
    public static final int RESULT_OAUTH_USER_CANCELED = Activity.RESULT_FIRST_USER + 1;
    public static final int RESULT_OAUTH_FAILURE = Activity.RESULT_FIRST_USER + 2;

    public GoogleOAuthUtil() {
    }

    /**
     * getIMAPLoginCommand -Get IMAP command to connect server.
     *
     * @param username
     * @param token
     */
    public static String getIMAPLoginCommand(String username, String token) {
        String cmd = "user=" + username + '\001' + "auth=Bearer " + token + '\001' + '\001';
        byte[] res = Base64.encode(cmd.getBytes(), Base64.DEFAULT);
        String newCmd = null;
        try {
            newCmd = new String(res, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ImapConstants.AUTHENTICATE + " " + "XOAUTH2" + " " + newCmd;
    }

    /**
     * getSMTPLoginCommand
     * get SMTP command to connect server.
     *
     * @param username
     * @param token
     */
    public static String getSMTPLoginCommand(String username, String token) {
        String cmd = "user=" + username + '\001' + "auth=Bearer " + token + '\001' + '\001';
        byte[] res = Base64.encode(cmd.getBytes(), Base64.NO_WRAP);
        String newCmd = null;
        try {
            newCmd = new String(res, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "AUTH XOAUTH2 " + newCmd;
    }

    /**
     * getPOP3LoginCommand - Get POP command to connect server.
     *
     * @param username
     * @param token
     */
    public static String getPOP3LoginCommand(String username, String token) {
        String cmd = "USER=" + username + '\001' + "auth=Bearer " + token + '\001' + '\001';
        byte[] res = Base64.encode(cmd.getBytes(), Base64.NO_WRAP);
        String newCmd = null;
        try {
            newCmd = new String(res, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "AUTH XOAUTH2 " + newCmd;
    }

    /**
     * getGoogleAccounts - Get all of Google accounts on AccountManager
     *
     * @param context
     */
    public static ArrayList<android.accounts.Account> getGoogleAccounts(Context context) {
        AccountManager am = AccountManager.get(context);
        android.accounts.Account[] accounts = am.getAccountsByType("com.google");
        ArrayList<android.accounts.Account> res = new ArrayList<android.accounts.Account>();
        for (android.accounts.Account acc : accounts) {
            if (EmailContent.Account.restoreAccountWithEmailAddress(context, acc.name) == null ) {
                res.add(acc);
            }
        }
        return res;
    }

    /**
     * getGoogleAccount - Get Google account on AccountManager
     *
     * @param context
     * @param username
     */
    private static android.accounts.Account getGoogleAccount(Context context, String username) {
        AccountManager am = AccountManager.get(context);
        android.accounts.Account[] accounts = am.getAccountsByType("com.google");
        android.accounts.Account resAccount = null;
        for (android.accounts.Account acc : accounts) {
            if (acc.name.equals(username)) {
                resAccount = acc;
                break;
            }
        }
        if (resAccount == null) {
            return null;
        }
        return resAccount;
    }


    /**
     * getGoogleAccounts - Get all configured Google accounts name from AccountManager
     *
     * @param context
     * @param emailAddress
     */
    public static boolean getGoogleAccountNameFromAccountManager(Context ctx, String emailAddress) {
        String GOOGLE_TYPE = "com.google";
        AccountManager am = AccountManager.get(ctx);
        android.accounts.Account[] accounts = am.getAccountsByType(GOOGLE_TYPE);
        if (accounts != null) {
            EmailLog.d(TAG, "Number of accounts in AccountManager DB: " + accounts.length);
            for (android.accounts.Account acc : accounts) {
                if(emailAddress.equalsIgnoreCase(acc.name)) {
                    EmailLog.d(TAG, "acc name" + acc.name);
                    return true;
                }
            }
        } else {
            EmailLog.e(TAG, "account is null");
            return false;
        }
        return false;
    }

    /**
     * getToken - get token from google.
     *
     * @param context
     * @param activity
     * @param username
     */
    public static String getToken(Context context, Activity activity, String username)
            throws AuthenticationFailedException {
        EmailLog.d(TAG, "getToken(Context, Activity, String)");
        android.accounts.Account account = getGoogleAccount(context, username);
        if (account == null) {
            EmailLog.e(TAG, "There is no account on AccountManager");
            throw new AuthenticationFailedException("There is no account on AccountManager");
        }
        AccountManager am = AccountManager.get(context);
        AccountManagerFuture<Bundle> accountManagerFuture = am.getAuthToken(account, SCOPE, null,
                activity, null, null);
        Bundle authTokenBundle = null;
        try {
            authTokenBundle = accountManagerFuture.getResult();
        } catch (OperationCanceledException e) {
            e.printStackTrace();
        } catch (AuthenticatorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (authTokenBundle == null) {
            EmailLog.e(TAG, "Failed to get token.");
            throw new AuthenticationFailedException("Failed to get token.");
        }
        return authTokenBundle.get(AccountManager.KEY_AUTHTOKEN).toString();
    }

    /**
     * getToken - get token from google.
     *
     * @param context
     * @param activity
     * @param username
     * @param callback
     */
    public static void getToken(Context context, Activity activity, String username,
            AccountManagerCallback<Bundle> callback) throws AuthenticationFailedException {
        EmailLog.d(TAG, "getToken(Context, Activity, String, AccountManagerCallback<Bundle>)");
        android.accounts.Account account = getGoogleAccount(context, username);
        if (account == null) {
            EmailLog.e(TAG, "There is no account on AccountManager");
            throw new AuthenticationFailedException("There is no account on AccountManager");
        }
        AccountManager am = AccountManager.get(context);
        am.getAuthToken(account, SCOPE, null, activity, callback, null);
    }

    /**
     * getToken - get token from google.
     *
     * @param context
     * @param username
     */
    public static String getToken(Context context, String username)
            throws AuthenticationFailedException {
        EmailLog.d(TAG, "getToken(Context, String)");
        android.accounts.Account account = getGoogleAccount(context, username);
        if (account == null) {
            EmailLog.e(TAG, "There is no account on AccountManager");
            throw new AuthenticationFailedException("There is no account on AccountManager");
        }
        AccountManager am = AccountManager.get(context);
        AccountManagerFuture<Bundle> accountManagerFuture = am.getAuthToken(account, SCOPE, null,
                false, null, null);
        Bundle authTokenBundle = null;
        try {
            authTokenBundle = accountManagerFuture.getResult();
        } catch (OperationCanceledException e) {
            e.printStackTrace();
        } catch (AuthenticatorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (authTokenBundle != null) {
            Object token = authTokenBundle.get(AccountManager.KEY_AUTHTOKEN);
            if (token != null) {
                return token.toString();
            } else {
                EmailLog.e(TAG, "Failed to get token, token is null");
                throw new AuthenticationFailedException("Failed to get token.");
            }
        } else {
            EmailLog.e(TAG, "Failed to get token, authTokenBundle is null");
            throw new AuthenticationFailedException("Failed to get token.");
        }
    }

    /**
     * getRefreshToken - after existing token is removed, get new token from google.
     *
     * @param context
     * @param username
     */
    public static String getRefreshToken(Context context, String username, String prevToken)
            throws AuthenticationFailedException {
        if(username == null)
            return null;
        AccountManager am = AccountManager.get(context);
        am.invalidateAuthToken(username, prevToken);
        return getToken(context, username);
    }

    /**
     * updateHostAuthEntries - Update the HostAuth entries with the latest token values
     *
     * @param context
     * @param account
     * @param userName
     * @param password
     */
    public static void updateHostAuthEntries(Context context, Account account, String userName,
            String password, boolean oauth2) {
        if (account == null) {
            EmailLog.e(TAG, "FATAL - No account found");
            return;
        }
        account.mHostAuthRecv = HostAuth.restoreHostAuthWithId(context, account.mHostAuthKeyRecv);
        account.mHostAuthSend = HostAuth.restoreHostAuthWithId(context, account.mHostAuthKeySend);
        if(account.mHostAuthRecv != null && account.mHostAuthSend!= null ) {
            if (oauth2) {
                account.mHostAuthRecv.setOAuthLogin(userName, password);
                account.mHostAuthSend.setOAuthLogin(userName, password);
            } else {
                account.mHostAuthRecv.setLogin(userName, password, true);
                account.mHostAuthSend.setLogin(userName, password, true);
            }
            ContentValues cv = account.mHostAuthRecv.toContentValues();
            cv.put(HostAuthColumns.ACCOUNT_KEY, account.mId);
            context.getContentResolver().update(HostAuth.CONTENT_URI, cv,
                    HostAuthColumns.ID + "=" + account.mHostAuthRecv.mId, null);
            cv = account.mHostAuthSend.toContentValues();
            cv.put(HostAuthColumns.ACCOUNT_KEY, account.mId);
            context.getContentResolver().update(HostAuth.CONTENT_URI, cv,
                    HostAuthColumns.ID + "=" + account.mHostAuthSend.mId, null);
        }
    }

    /**
     * updateHostAuthPassword - Update the HostAuth entries with the latest token values
     *
     * @param context
     * @param account
     * @param userName
     * @param password
     */
    public static void updateHostAuthPassword(Context context, Account account, String userName,
            String password, long credKey) {
        if (account == null) {
            EmailLog.e(TAG, "FATAL - No account found");
            return;
        }
        account.mHostAuthRecv = HostAuth.restoreHostAuthWithId(context, account.mHostAuthKeyRecv);
        account.mHostAuthSend = HostAuth.restoreHostAuthWithId(context, account.mHostAuthKeySend);
        account.mHostAuthRecv.setOAuthLogin(userName, password);
        account.mHostAuthSend.setOAuthLogin(userName, password);
        if(account.mHostAuthRecv != null && account.mHostAuthSend!= null ) {
            ContentValues cv = account.mHostAuthRecv.toContentValues();
            cv.put(HostAuthColumns.ACCOUNT_KEY, account.mId);
            cv.put(HostAuthColumns.CREDENTIAL_KEY, credKey);
            cv.put(HostAuthColumns.PASSWORDENC, EmailContent.HostAuth.PASSWORD_TYPE_GOOGLE_OAUTH_TOKEN);
            context.getContentResolver().update(HostAuth.CONTENT_URI, cv,
                    HostAuthColumns.ID + "=" + account.mHostAuthRecv.mId, null);
            cv = account.mHostAuthSend.toContentValues();
            cv.put(HostAuthColumns.ACCOUNT_KEY, account.mId);
            cv.put(HostAuthColumns.CREDENTIAL_KEY, credKey);
            cv.put(HostAuthColumns.CREDENTIAL_KEY, credKey);
            cv.put(HostAuthColumns.PASSWORDENC, EmailContent.HostAuth.PASSWORD_TYPE_GOOGLE_OAUTH_TOKEN);

            try {
                context.getContentResolver().update(HostAuth.CONTENT_URI, cv,
                        HostAuthColumns.ID + "=" + account.mHostAuthSend.mId, null);
            } catch (IllegalArgumentException e) {
                EmailLog.e(TAG, e.getMessage());
            }
        }

    }

    /**
     * getAllOAuthProviders - Get all the OAUTH providers(Right now google only)
     * @param context
     * @param resId
     * @return
     */
/*    public static List<OAuthProvider> getAllOAuthProviders(final Context context, final int resId) {
        try {
            List<OAuthProvider> providers = new ArrayList<OAuthProvider>();
            OAuthProvider provider = null;
            try {
                provider = new OAuthProvider();
                provider.id = PROVIDER_ID;
                provider.label = PROVIDER_LABEL;
                provider.authEndpoint = AUTH_ENDPOINT;
                provider.tokenEndpoint = TOKEN_ENDPOINT;
                provider.refreshEndpoint = REFRESH_ENDPOINT;
                provider.responseType = RESPONSE_TYPE;
                provider.redirectUri = REDIRECT_URI;
                provider.scope = OAUTH_SCOPE;
                provider.state = STATE;
                provider.clientId = CLIENT_ID;
                provider.clientSecret = CLIENT_SECRET;
                providers.add(provider);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            return providers;
        } catch (Exception e) {
            EmailLog.e(Logging.LOG_TAG, "Error while trying to load provider settings.", e);
        }
        return null;
    }*/

    /**
     * Search for a single resource containing known oauth provider definitions.
     *
     * @param context
     * @param id String Id of the oauth provider.
     * @param resourceId ResourceId of the xml file to search.
     * @return The OAuthProvider if found, null if not.
     */
    public static OAuthProvider findOAuthProvider(/*final Context context, */final String id) {
        // TODO: Consider adding a way to cache this file during new account setup, so that we
        // don't need to keep loading the file over and over.
        // TODO: need a mechanism to get a list of all supported OAuth providers so that we can
        // offer the user a choice of who to authenticate with.
        try {
            OAuthProvider provider = null;
            try {
                provider = new OAuthProvider();
                provider.id = id;
                provider.label = PROVIDER_LABEL;
                provider.authEndpoint = AUTH_ENDPOINT;
                provider.tokenEndpoint = TOKEN_ENDPOINT;
                provider.refreshEndpoint = REFRESH_ENDPOINT;
                provider.responseType = RESPONSE_TYPE;
                provider.redirectUri = REDIRECT_URI;
                provider.scope = OAUTH_SCOPE;
                provider.state = STATE;
                provider.clientId = CLIENT_ID;
                provider.clientSecret = CLIENT_SECRET;
                return provider;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            EmailLog.e(Logging.LOG_TAG, "Error while trying to load provider settings.", e);
        }
        return null;
    }

    /**
     * Create the request to get the authorization code.
     *
     * @param context
     * @param provider The OAuth provider to register with
     * @param emailAddress Email address to send as a hint to the oauth service.
     * @return
     */
    public static Uri createOAuthRegistrationRequest(/*final Context context,*/
            final OAuthProvider provider, final String emailAddress) {
        final Uri.Builder b = Uri.parse(provider.authEndpoint).buildUpon();
        b.appendQueryParameter("response_type", provider.responseType);
        b.appendQueryParameter("client_id", provider.clientId);
        b.appendQueryParameter("redirect_uri", provider.redirectUri);
        b.appendQueryParameter("scope", provider.scope);
        b.appendQueryParameter("state", provider.state);
        b.appendQueryParameter("login_hint", emailAddress);
        return b.build();
    }
}