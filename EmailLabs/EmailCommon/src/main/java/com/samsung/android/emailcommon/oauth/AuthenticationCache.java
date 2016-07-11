/*
 * Copyright (C) 2015 The Android Open Source Project
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
package com.samsung.android.emailcommon.oauth;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.samsung.android.emailcommon.mail.AuthenticationFailedException;
import com.samsung.android.emailcommon.mail.MessagingException;
import com.samsung.android.emailcommon.oauth.OAuthAuthenticator.AuthenticationResult;
import com.samsung.android.emailcommon.provider.Credential;
import com.samsung.android.emailcommon.provider.EmailContent.Account;
import com.samsung.android.emailcommon.provider.EmailContent.HostAuth;
import com.samsung.android.emailcommon.utility.EmailLog;

public class AuthenticationCache {
    private static final String TAG = "AuthenticationCache";

    // Threshold for refreshing a token. If the token is expected to expire within this amount of
    // time, we won't even bother attempting to use it and will simply force a refresh.
    private static final long EXPIRATION_THRESHOLD = 5 * DateUtils.MINUTE_IN_MILLIS;

    private final Map<Long, CacheEntry> mCache;
    private final OAuthAuthenticator mAuthenticator;

    private static class CacheEntry {
        CacheEntry(long accountId, String providerId, String accessToken, String refreshToken,
                long expirationTime) {
            mAccountId = accountId;
            mProviderId = providerId;
            mAccessToken = accessToken;
            mRefreshToken = refreshToken;
            mExpirationTime = expirationTime;
        }

        final long mAccountId;
        String mProviderId;
        String mAccessToken;
        String mRefreshToken;
        long mExpirationTime;
    }

    private static class AuthenticationCacheHelper {
        private static final AuthenticationCache INSTANCE = new AuthenticationCache();
        }

    public static AuthenticationCache getInstance() {
        return AuthenticationCacheHelper.INSTANCE;
    }

    private AuthenticationCache() {
        mCache = new HashMap<Long, CacheEntry>();
        mAuthenticator = new OAuthAuthenticator();
    }

    // Gets an access token for the given account. This may be whatever is currently cached, or
    // it may query the server to get a new one if the old one is expired or nearly expired.
    public String retrieveAccessToken(Context context, Account account) throws
            MessagingException, IOException {
        EmailLog.d(TAG, "retrieveAccessToken");
        // Currently, we always use the same OAuth info for both sending and receiving.
        // If we start to allow different credential objects for sending and receiving, this
        // will need to be updated.
        CacheEntry entry = null;
        synchronized (mCache) {
            entry = getEntry(context, account);
        }
        synchronized (entry) {
            final long actualExpiration = entry.mExpirationTime - EXPIRATION_THRESHOLD;
            EmailLog.d(TAG, "actualExpiration=" + actualExpiration +
                    " entry.mExpirationTime=" + entry.mExpirationTime +
                    " EXPIRATION_THRESHOLD=" + EXPIRATION_THRESHOLD + 
                    " System.currentTimeMillis()=" + System.currentTimeMillis());
            if (System.currentTimeMillis() > actualExpiration) {
                // This access token is pretty close to end of life. Don't bother trying to use it,
                // it might just time out while we're trying to sync. Go ahead and refresh it
                // immediately.
                refreshEntry(context, entry);
            }
            return entry.mAccessToken;
        }
    }

    // Gets an access token for the given account. This may be whatever is currently cached, or
    // it may query the server to get a new one if the old one is expired or nearly expired.
    public void saveOrUpdateToken(Context context, Account account, Intent data) throws
            MessagingException, IOException {
        EmailLog.d(TAG, "saveOrUpdateToken");
        // Currently, we always use the same OAuth info for both sending and receiving.
        // If we start to allow different credential objects for sending and receiving, this
        // will need to be updated.
        final HostAuth hostAuth = account.getOrCreateHostAuthRecv(context);
        final Credential cred = hostAuth.getOrCreateCredential(context);
        cred.mProviderId = "google";
        cred.mAccessToken = data.getStringExtra(GoogleOAuthUtil.EXTRA_OAUTH_ACCESS_TOKEN);
        cred.mRefreshToken = data.getStringExtra(GoogleOAuthUtil.EXTRA_OAUTH_REFRESH_TOKEN);
        cred.mExpiration = (data.getIntExtra(GoogleOAuthUtil.EXTRA_OAUTH_EXPIRES_IN, 0) *
                DateUtils.SECOND_IN_MILLIS) + System.currentTimeMillis();
        EmailLog.d(TAG, "cred.mExpiration=" + cred.mExpiration);
        //Fallback method
        if ((hostAuth.mPasswordenc == HostAuth.PASSWORD_TYPE_ENCRYPTED ||
                hostAuth.mPasswordenc == HostAuth.PASSWORD_TYPE_GOOGLE_TOKEN) && !cred.isSaved()) {
            EmailLog.d(TAG, "Save credentials!!");
            cred.save(context);
        } else {
            cred.update(context, cred.toContentValues());
        }
        //Update the Hostauth entries like password and password flag
        GoogleOAuthUtil.updateHostAuthPassword(context, account,
                account.mEmailAddress, cred.mAccessToken, cred.mId);
    }

    public String refreshAccessToken(Context context, Account account) throws
            MessagingException, IOException {
        EmailLog.d(TAG, "refreshAccessToken");
        CacheEntry entry = getEntry(context, account);
        synchronized (entry) {
            refreshEntry(context, entry);
            return entry.mAccessToken;
        }
    }

    private CacheEntry getEntry(Context context, Account account) throws IOException {
        EmailLog.d(TAG, "getEntry");
        CacheEntry entry = null;
        if (account.isSaved()) {
            entry = mCache.get(account.mId);
            if (entry == null) {
                EmailLog.d(TAG, "initializing entry from database");
                final HostAuth hostAuth = account.getOrCreateHostAuthRecv(context);
                if (hostAuth == null) {
                    EmailLog.e(TAG, "HostAuth is Null");
                    throw new IOException();
                }
                final Credential credential = hostAuth.getOrCreateCredential(context);
                entry = new CacheEntry(account.mId, credential.mProviderId, credential.mAccessToken,
                        credential.mRefreshToken, credential.mExpiration);
                mCache.put(account.mId, entry);
            }
        } else {
            // This account is temporary, just create a temporary entry. Don't store
            // it in the cache, it won't be findable because we don't yet have an account Id.
            final HostAuth hostAuth = account.getOrCreateHostAuthRecv(context);
            if (hostAuth == null) {
                EmailLog.e(TAG, "HostAuth is Null");
                throw new IOException();
            }
            final Credential credential = hostAuth.getCredential(context);
            if(credential != null)
                entry = new CacheEntry(account.mId, credential.mProviderId, credential.mAccessToken,
                        credential.mRefreshToken, credential.mExpiration);
        }
        return entry;
    }

    private void refreshEntry(Context context, CacheEntry entry) throws
            IOException, MessagingException {
        EmailLog.d(TAG, "AuthenticationCache refreshEntry=" + entry.mAccountId);
        try {
            final AuthenticationResult result = mAuthenticator.requestRefresh(/*context,*/
                    entry.mProviderId, entry.mRefreshToken);
            // Don't set the refresh token here, it's not returned by the refresh response,
            // so setting it here would make it blank.
            if(result != null){
                entry.mAccessToken = result.getmAccessToken();
                entry.mExpirationTime = (result.getmExpiresInSeconds() * DateUtils.SECOND_IN_MILLIS) +
                        System.currentTimeMillis();
            }
            EmailLog.d(TAG, "entry.mExpirationTime=" + entry.mExpirationTime);
            saveEntry(context, entry);
        } catch (AuthenticationFailedException e) {
            // This is fatal. Clear the tokens and rethrow the exception.
            EmailLog.d(TAG, "authentication failed, clearning");
            clearEntry(context, entry);
            throw e;
        } catch (MessagingException e) {
            EmailLog.d(TAG, "messaging exception");
            throw e;
        } catch (IOException e) {
            EmailLog.d(TAG, "IO exception");
            throw e;
        }
    }

    private void saveEntry(Context context, CacheEntry entry) {
        EmailLog.d(TAG, "saveEntry");
        final Account account = Account.restoreAccountWithId(context,  entry.mAccountId);
        if (account == null) {
            EmailLog.e(TAG, "FATAL - No Account found");
            return;
        }
        final HostAuth hostAuth = account.getOrCreateHostAuthRecv(context);
        final Credential cred = hostAuth.getOrCreateCredential(context);
        cred.mProviderId = entry.mProviderId;
        cred.mAccessToken = entry.mAccessToken;
        cred.mRefreshToken = entry.mRefreshToken;
        cred.mExpiration = entry.mExpirationTime;
        if (cred.isSaved()) {
            cred.update(context, cred.toContentValues());
        } else {
            cred.save(context);
        }
        //update the HostAuth with the new Token
        GoogleOAuthUtil.updateHostAuthPassword(context, account, account.mEmailAddress,
                cred.mAccessToken, cred.mId);
    }

    private void clearEntry(Context context, CacheEntry entry) {
        EmailLog.d(TAG, "clearEntry");
        entry.mAccessToken = "";
        entry.mRefreshToken = "";
        entry.mExpirationTime = 0;
        saveEntry(context, entry);
        mCache.remove(entry.mAccountId);
    }
}
