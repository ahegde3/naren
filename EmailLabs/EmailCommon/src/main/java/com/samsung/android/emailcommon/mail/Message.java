/*
 * Copyright (C) 2008 The Android Open Source Project
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

package com.samsung.android.emailcommon.mail;

import android.content.Context;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Message implements Part, Body {
    public static final Message[] EMPTY_ARRAY = new Message[0];

    public enum RecipientType {
        TO, CC, BCC,
    }

    protected String mUid;

    private HashSet<Flag> mFlags = null;
    // SVL Start: Hanifa
    // stores all the annotations of a message.
    private HashMap<String, String> mTags = null;
    // SVL End

    protected Date mInternalDate;

    protected Folder mFolder;

    // Snc Porting
    private long mMessageId_original;

    // Snc Porting
    public void setMessageId_original(long set) {
        mMessageId_original = set;
    }

    // Snc Porting
    public long getMessageId_original() {
        return mMessageId_original;
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        this.mUid = uid;
    }

    public Folder getFolder() {
        return mFolder;
    }

    public abstract String getSubject() throws MessagingException;

    public abstract void setSubject(String subject) throws MessagingException;

    public Date getInternalDate() {
        return mInternalDate;
    }

    public void setInternalDate(Date internalDate) {
        this.mInternalDate = internalDate;
    }

    public abstract Date getReceivedDate() throws MessagingException;

    public abstract Date getSentDate() throws MessagingException;

    public abstract void setSentDate(Date sentDate) throws MessagingException;

    public abstract Address[] getRecipients(RecipientType type) throws MessagingException;

    public abstract void setRecipients(RecipientType type, Address[] addresses)
            throws MessagingException;

    public void setRecipient(RecipientType type, Address address) throws MessagingException {
        setRecipients(type, new Address[] {
            address
        });
    }

    public abstract Address[] getFrom() throws MessagingException;
    

    public abstract Address[] getFromSMS() throws MessagingException;
    
    public abstract Address[] getToSMS() throws MessagingException;
    
    public abstract Address[] getReplyToSMS() throws MessagingException;
    
    public abstract String getComments() throws MessagingException;

    public abstract String getImportance() throws MessagingException; // yk84.kim@samsung.com
                                                                      // :
                                                                      // 2011-09-21
                                                                      // :
                                                                      // Importance
                                                                      // function
                                                                      // for
                                                                      // IMAP

    public abstract void setFrom(Address from) throws MessagingException;

    public abstract Address[] getReplyTo() throws MessagingException;

    public abstract void setReplyTo(Address[] from) throws MessagingException;

    public abstract Body getBody() throws MessagingException;

    public abstract String getContentType() throws MessagingException;

    public abstract void addHeader(String name, String value) throws MessagingException;

    public abstract void setHeader(String name, String value) throws MessagingException;

    public abstract String[] getHeader(String name) throws MessagingException;
    
    public abstract String[] getHeaders() throws MessagingException;

    public abstract void removeHeader(String name) throws MessagingException;

    public abstract Address[] getReadReceiptTo() throws MessagingException;

    // Snc Porting
    public abstract void writeTo(Context context, long messageId, OutputStream out)
            throws IOException, MessagingException;

    // Always use these instead of getHeader("Message-ID") or
    // setHeader("Message-ID");
    public abstract void setMessageId(String messageId) throws MessagingException;

    public abstract String getMessageId() throws MessagingException;

    public abstract void setBody(Body body) throws MessagingException;

    public boolean isMimeType(String mimeType) throws MessagingException {
        return getContentType().startsWith(mimeType);
    }

    private HashSet<Flag> getFlagSet() {
        if (mFlags == null) {
            mFlags = new HashSet<Flag>();
        }
        return mFlags;
    }

    /*
     * TODO Refactor Flags at some point to be able to store user defined flags.
     */
    public Flag[] getFlags() {
        return getFlagSet().toArray(new Flag[] {});
    }

    /**
     * Set/clear a flag directly, without involving overrides of
     * {@link #setFlag} in subclasses. Only used for testing.
     */
    public final void setFlagDirectlyForTest(Flag flag, boolean set) throws MessagingException {
        if (set) {
            getFlagSet().add(flag);
        } else {
            getFlagSet().remove(flag);
        }
    }

    public void setFlag(Flag flag, boolean set) throws MessagingException {
        setFlagDirectlyForTest(flag, set);
    }

    /**
     * This method calls setFlag(Flag, boolean)
     * 
     * @param flags
     * @param set
     */
    public void setFlags(Flag[] flags, boolean set) throws MessagingException {
        for (Flag flag : flags) {
            setFlag(flag, set);
        }
    }

    // SVL Start: Hanifa
    // Stores the annotation of a message which refers users mailboxes.
    public HashMap<String, String> getTagsMap() {
        if (mTags != null) {
            return mTags;
        } else {
            mTags = new HashMap<String, String>();
            mTags.clear();
            return mTags;
        }
    }

    public void setTag(String tagName, String value) {

        if (mTags == null) {
            mTags = new HashMap<String, String>();
            mTags.clear();
        }

        if (mTags != null) {
            mTags.put(tagName, value);
        }
    }

    private HashSet<Flag> setFlagSet() {
        if (mFlags == null) {
            mFlags = new HashSet<Flag>();
        }
        return mFlags;
    }

    // SVL End

    public boolean isSet(Flag flag) {
        return getFlagSet().contains(flag);
    }

    public abstract void saveChanges() throws MessagingException;

    @Override
    public String toString() {
        return getClass().getSimpleName() + ':' + mUid;
    }

}
