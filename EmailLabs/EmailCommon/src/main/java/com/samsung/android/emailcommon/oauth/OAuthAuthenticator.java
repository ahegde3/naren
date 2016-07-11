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
import android.net.Uri;
import android.text.format.DateUtils;

import com.samsung.android.emailcommon.combined.common.VendorPolicyLoader.OAuthProvider;
import com.samsung.android.emailcommon.mail.AuthenticationFailedException;
import com.samsung.android.emailcommon.mail.MessagingException;
import com.samsung.android.emailcommon.utility.EmailLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class OAuthAuthenticator {
    private static final String TAG = "OAuthAuthenticator";

    public static final String OAUTH_REQUEST_CODE = "code";
    public static final String OAUTH_REQUEST_REFRESH_TOKEN = "refresh_token";
    public static final String OAUTH_REQUEST_CLIENT_ID = "client_id";
    public static final String OAUTH_REQUEST_CLIENT_SECRET = "client_secret";
    public static final String OAUTH_REQUEST_REDIRECT_URI = "redirect_uri";
    public static final String OAUTH_REQUEST_GRANT_TYPE = "grant_type";

    public static final String JSON_ACCESS_TOKEN = "access_token";
    public static final String JSON_REFRESH_TOKEN = "refresh_token";
    public static final String JSON_EXPIRES_IN = "expires_in";
    public static final String JSON_ID_TOKEN = "id_token";
    public static final String JSON_EMAIL_ID_TOKEN = "email";

    /**
     * Web API to fetch the email Id for this account
     */
    private static final String EMAIL_ID_ENDPOINT_URL="https://www.googleapis.com/oauth2/v1/tokeninfo?id_token=";

    private static final long CONNECTION_TIMEOUT = 20 * DateUtils.SECOND_IN_MILLIS;
    private static final long COMMAND_TIMEOUT = 30 * DateUtils.SECOND_IN_MILLIS;

    private String midToken = null;

    public static class AuthenticationResult {
        public AuthenticationResult(String accessToken, String refreshToken,
                int expiresInSeconds) {
            mAccessToken = accessToken;
            mRefreshToken = refreshToken;
            mExpiresInSeconds = expiresInSeconds;
        }

        @Override
        public String toString() {
            return "result access " + (mAccessToken==null?"null":"[REDACTED]") +
                    " refresh " + (mRefreshToken==null?"null":"[REDACTED]") +
                    " expiresInSeconds " + mExpiresInSeconds + 
                    " mEmailId " + (mEmailId==null?"null":"[REDACTED]");
        }

        private String mAccessToken;
        private String mRefreshToken;
        private int mExpiresInSeconds;
        private String mEmailId;

        public String getmAccessToken() {
            return mAccessToken;
        }

        public void setmAccessToken(String mAccessToken) {
            this.mAccessToken = mAccessToken;
        }

        public String getmRefreshToken() {
            return mRefreshToken;
        }

        public void setmRefreshToken(String mRefreshToken) {
            this.mRefreshToken = mRefreshToken;
        }

        public int getmExpiresInSeconds() {
            return mExpiresInSeconds;
        }

        public void setmExpiresInSeconds(int mExpiresInSeconds) {
            this.mExpiresInSeconds = mExpiresInSeconds;
        }

        public String getmEmailId() {
            return mEmailId;
        }

        public void setmEmailId(String mEmailId) {
            this.mEmailId = mEmailId;
        }
    }

    public OAuthAuthenticator() {

    }

    private HttpURLConnection getHttpURLConnection(String urlString) {
        HttpURLConnection httpUrlConnection = null;
        URL url;
        try {
            url = new URL(urlString);
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setConnectTimeout((int) CONNECTION_TIMEOUT);
            httpUrlConnection.setReadTimeout((int) COMMAND_TIMEOUT);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setChunkedStreamingMode(8192);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpUrlConnection;
    }

    public AuthenticationResult requestAccess(/*final Context context, */final String providerId,
            final String code) throws MessagingException, IOException {
        AuthenticationResult result = null;
        final OAuthProvider provider = GoogleOAuthUtil.findOAuthProvider(/*context, */providerId);
        if (provider == null) {
            EmailLog.e(TAG, "invalid provider %s", providerId);
            // This shouldn't happen, but if it does, it's a fatal. Throw an authentication failed
            // exception, this will at least give the user a heads up to set up their account again.
            throw new AuthenticationFailedException("Invalid provider" + providerId);
        }

        HttpURLConnection connection = getHttpURLConnection(provider.tokenEndpoint);
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter(OAUTH_REQUEST_CODE, code)
                .appendQueryParameter(OAUTH_REQUEST_CLIENT_ID, provider.clientId)
                .appendQueryParameter(OAUTH_REQUEST_REDIRECT_URI, provider.redirectUri)
                .appendQueryParameter(OAUTH_REQUEST_GRANT_TYPE, "authorization_code");
        String query = builder.build().getEncodedQuery();
        if (EmailLog.DEBUG) {
            EmailLog.d(TAG, "requestAccess code=" + code + " provider.clientId=" + provider.clientId +
                    " provider.clientSecret=" + provider.clientSecret + " provider.redirectUri=" + provider.redirectUri);
        }

        try {
            result = doRequest(connection, query);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        String emailId = requestEmailAddress();
        result.setmEmailId(emailId);
        return result;
    }

    /**
     * This request the Email id, using the id_token
     * @return
     * @throws MessagingException
     * @throws IOException
     */
    public String requestEmailAddress()
            throws MessagingException, IOException {
        EmailLog.d(TAG, "requestEmailAddress");
        String postUrl = EMAIL_ID_ENDPOINT_URL + midToken;
        String emailAddress = null;
        HttpURLConnection connection = getHttpURLConnection(postUrl);
        try {
            emailAddress = getEmailId(connection);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return emailAddress;
    }

    public AuthenticationResult requestRefresh(/*final Context context, */final String providerId,
            final String refreshToken) throws MessagingException, IOException {
        final OAuthProvider provider = GoogleOAuthUtil.findOAuthProvider(/*context,*/ providerId);
        AuthenticationResult result = null;
        if (provider == null) {
            EmailLog.e(TAG, "invalid provider %s", providerId);
            // This shouldn't happen, but if it does, it's a fatal. Throw an authentication failed
            // exception, this will at least give the user a heads up to set up their account again.
            throw new AuthenticationFailedException("Invalid provider" + providerId);
        }

        HttpURLConnection connection = getHttpURLConnection(provider.refreshEndpoint);
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter(OAUTH_REQUEST_REFRESH_TOKEN, refreshToken)
                .appendQueryParameter(OAUTH_REQUEST_CLIENT_ID, provider.clientId)
                .appendQueryParameter(OAUTH_REQUEST_GRANT_TYPE, "refresh_token");
        String query = builder.build().getEncodedQuery();

        try {
            result = doRequest(connection, query);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    private AuthenticationResult doRequest(HttpURLConnection connection, String query) throws MessagingException,
    IOException {
        EmailLog.d(TAG, "doRequest");
        if(connection == null){
            EmailLog.e(TAG, "HttpURLConnection error NULL");
            throw new AuthenticationFailedException("HttpURLConnection error NULL"); 
        }
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        OutputStream out = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.write(query);
        writer.flush();
        writer.close();
        out.close();
        int response = connection.getResponseCode();
        if (response == HttpURLConnection.HTTP_OK) {
            return parseResponse(connection);
        } else if (response == HttpURLConnection.HTTP_FORBIDDEN || response == HttpURLConnection.HTTP_UNAUTHORIZED ||
                response == HttpURLConnection.HTTP_BAD_REQUEST) {
            EmailLog.e(TAG, "HTTP Authentication error getting oauth tokens " + response);
            // This is fatal, and we probably should clear our tokens after this.
            throw new AuthenticationFailedException("Auth error getting auth token");
        } else {
            EmailLog.e(TAG, "HTTP Error getting oauth tokens" + response);
            // This is probably a transient error, we can try again later.
            throw new MessagingException("HTTPError " + response + " getting oauth token");
        }
    }

    /**
     * This will fetch the Email ID for this account
     * @param post
     * @return
     * @throws MessagingException
     * @throws IOException
     */
    private String getEmailId(HttpURLConnection connection) throws MessagingException,
            IOException {
        EmailLog.d(TAG, "getEmailId");
        if(connection == null){
            EmailLog.e(TAG, "HttpURLConnection error NULL");
            throw new AuthenticationFailedException("HttpURLConnection error NULL"); 
        }
        int response = connection.getResponseCode();
        EmailLog.d(TAG, "response" + response);
        if (response == HttpURLConnection.HTTP_OK) {
            return parseEmailAddressResponse(connection);
        } else if (response == HttpURLConnection.HTTP_FORBIDDEN || response == HttpURLConnection.HTTP_UNAUTHORIZED ||
                response == HttpURLConnection.HTTP_BAD_REQUEST) {
          EmailLog.e(TAG, "HTTP Authentication error getting Email Id " + response);
          // This is fatal, and we probably should clear our tokens after this.
          throw new AuthenticationFailedException("Auth error getting Email Id");
        } else {
          EmailLog.e(TAG, "HTTP Error getting Email Id" + response);
          // This is probably a transient error, we can try again later.
          throw new MessagingException("HTTPError " + response + " getting Email Id");
        }
    }

    private AuthenticationResult parseResponse(HttpURLConnection response) throws IOException,
            MessagingException {
        EmailLog.d(TAG, "parseResponse");
        InputStream is = response.getInputStream();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        final StringBuilder builder = new StringBuilder();
        for (String line = null; (line = reader.readLine()) != null;) {
            builder.append(line).append("\n");
        }
        try {
            EmailLog.d(TAG, "Response=" + builder.toString());
            final JSONObject jsonResult = new JSONObject(builder.toString());
            String accessToken = null;
            String expiresIn = null;
            String refreshToken = null;
            if (jsonResult.has(JSON_ACCESS_TOKEN)) {
                accessToken = jsonResult.getString(JSON_ACCESS_TOKEN);
            }
            if (jsonResult.has(JSON_EXPIRES_IN)) {
                expiresIn = jsonResult.getString(JSON_EXPIRES_IN);
            }
            if (jsonResult.has(JSON_REFRESH_TOKEN)) {
                refreshToken = jsonResult.getString(JSON_REFRESH_TOKEN);
            } else {
                refreshToken = null;
            }
            if (jsonResult.has(JSON_ID_TOKEN)) {
                midToken = jsonResult.getString(JSON_ID_TOKEN);
            }

            try {
                int expiresInSeconds = Integer.valueOf(expiresIn);
                return new AuthenticationResult(accessToken, refreshToken, expiresInSeconds);
            } catch (NumberFormatException e) {
                EmailLog.dumpException(TAG, e, "Invalid expiration %s" + expiresIn);
                // This indicates a server error, we can try again later.
                throw new MessagingException("Invalid number format", e);
            }
        } catch (JSONException e) {
            EmailLog.dumpException(TAG, e, "Invalid JSON");
            // This indicates a server error, we can try again later.
            throw new MessagingException("Invalid JSON", e);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * Parses and get the Email Id from the JSON response
     * Sample JSON response
     * Response={
           "issuer": "accounts.google.com",
           "issued_to": "80780.apps.googleusercontent.com",
           "audience": "8078909.apps.googleusercontent.com",
           "user_id": "1118976557884",
           "expires_in": 3598,
           "issued_at": 1456353,
           "email": "emailId@gmail.com",
           "email_verified": true
       }
     * @param response
     * @return
     * @throws IOException
     * @throws MessagingException
     */
    private String parseEmailAddressResponse(HttpURLConnection response) throws IOException,
    MessagingException {
        EmailLog.d(TAG, "parseEmailAddressResponse");
        InputStream is = response.getInputStream();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        final StringBuilder builder = new StringBuilder();
        for (String line = null; (line = reader.readLine()) != null;) {
            builder.append(line).append("\n");
        }
        try {
            EmailLog.d(TAG, "Response=" + builder.toString());
            final JSONObject jsonResult = new JSONObject(builder.toString());
            String emailId = null;
            if (jsonResult.has(JSON_EMAIL_ID_TOKEN)) {
                emailId = jsonResult.getString(JSON_EMAIL_ID_TOKEN);
            }
            return emailId;
        } catch (JSONException e) {
            EmailLog.dumpException(TAG, e, "Invalid JSON");
            throw new MessagingException("Invalid JSON", e);
        } finally {
            if (is != null) {
                is.close();
            }
        }
}
}

