package com.samsung.android.email.model;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * This represent an Email object.
 * Here we will list all the attributes that defines an EMail
 * Created by nsbisht on 5/31/16.
 */
public class Email {
    private String mFromAddress;
    /**
     * Used to represent fav/replied/forwarded/read etc.
     */
    private int mFlag;
    private boolean mHasAttachment;
    private String mSubject;
    private String mBody;
    private Date mDate;
    private Bitmap[] mAttachmentthumbnails;

    public Bitmap[] getmAttachmentthumbnails() {
        return mAttachmentthumbnails;
    }

    public void setmAttachmentthumbnails(Bitmap[] mAttachmentthumbnails) {
        this.mAttachmentthumbnails = mAttachmentthumbnails;
    }

    public String getmFromAddress() {
        return mFromAddress;
    }

    public void setmFromAddress(String mFromAddress) {
        this.mFromAddress = mFromAddress;
    }

    public int getmFlag() {
        return mFlag;
    }

    public void setmFlag(int mFlag) {
        this.mFlag = mFlag;
    }

    public boolean ismHasAttachment() {
        return mHasAttachment;
    }

    public void setmHasAttachment(boolean mHasAttachment) {
        this.mHasAttachment = mHasAttachment;
    }

    public String getmSubject() {
        return mSubject;
    }

    public void setmSubject(String mSubject) {
        this.mSubject = mSubject;
    }

    public String getmBody() {
        return mBody;
    }

    public void setmBody(String mBody) {
        this.mBody = mBody;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }
}
