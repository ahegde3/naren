package com.samsung.android.emailcommon.security;

public class PGPUserInfo {

    public String mUserName;
    public String mUserId;

    public PGPUserInfo(String userName, String userId) {
        mUserName = userName;
        mUserId = userId;
    }

    public void setUserInfo(String userName, String userId) {
        mUserName = userName;
        mUserId = userId;
    }

}
