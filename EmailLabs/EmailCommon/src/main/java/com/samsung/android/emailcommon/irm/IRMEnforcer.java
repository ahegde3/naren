/*
 * Copyright (C) 2009 The Android Open Source Project
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

package com.samsung.android.emailcommon.irm;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;

import com.samsung.android.emailcommon.utility.Log;
import com.samsung.android.emailcommon.mail.MessagingException;
import com.samsung.android.emailcommon.provider.EmailContent;
import com.samsung.android.emailcommon.provider.EmailContent.Message;
import com.samsung.android.emailcommon.utility.EmailLog;

public class IRMEnforcer {
    static IRMEnforcer sInstance;
    private Context mContext;

    /**
     * IRMEnforcer enforces policies applied on the Email Message.
     *
     * @author kanchankumar@SISO
     */
    private IRMEnforcer(Context context) {
        mContext = context;
    }

    /**
     * Gets or creates the singleton instance of IRMEnforcer.
     */
    public synchronized static IRMEnforcer getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new IRMEnforcer(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * To check that whether reply is allowed on the message.
     *
     * @param messageId the id of the message
     * @return boolean
     */
    public boolean isReplyAllowed(long messageId) {
        return isReplyAllowed(getIRMLicenseFlag(messageId));
    }

    public static boolean isReplyAllowed(int IRMLicenseFlag) {
        boolean replyAllowed = true;
        if (IRMLicenseFlag != MessagingException.IRM_DEFAULT_CODE) {
            replyAllowed = (IRMLicenseFlag & Message.MSG_CLASS_IRM_REPLY_ALLOWED) != 0;
        }
        return replyAllowed;
    }

    /**
     * To check that whether forward is allowed on the message.
     *
     * @param messageId the id of the message
     * @return boolean
     */
    public boolean isForwardAllowed(long messageId) {
        return isForwardAllowed(getIRMLicenseFlag(messageId));
    }

    public static boolean isForwardAllowed(int IRMLicenseFlag) {
        boolean forwardAllowed = true;
        if (IRMLicenseFlag != MessagingException.IRM_DEFAULT_CODE) {
            forwardAllowed = (IRMLicenseFlag & Message.MSG_CLASS_IRM_FORWARD_ALLOWED) != 0;
        }
        return forwardAllowed;
    }

    /**
     * To check that whether replyAll is allowed on the message.
     *
     * @param messageId the id of the message
     * @return boolean
     */
    public boolean isReplyAllAllowed(long messageId) {
        return isReplyAllAllowed(getIRMLicenseFlag(messageId));
    }

    public static boolean isReplyAllAllowed(int IRMLicenseFlag) {
        boolean replyAllAllowed = true;
        if (IRMLicenseFlag != MessagingException.IRM_DEFAULT_CODE) {
            replyAllAllowed = (IRMLicenseFlag & Message.MSG_CLASS_IRM_REPLY_ALL_ALLOWED) != 0;
        }
        return replyAllAllowed;
    }

    /**
     * To check that whether edit is allowed on the message.
     *
     * @param messageId the id of the message
     * @return boolean
     */
    public boolean isEditAllowed(long messageId) {
        return isEditAllowed(getIRMLicenseFlag(messageId));
    }

    public static boolean isEditAllowed(int IRMLicenseFlag) {
        boolean editAllowed = true;
        if (IRMLicenseFlag != MessagingException.IRM_DEFAULT_CODE) {
            editAllowed = (IRMLicenseFlag & Message.MSG_CLASS_IRM_EDIT_ALLOWED) != 0;
        }
        return editAllowed;
    }

    /**
     * To check that whether modifying recipients is allowed on the message.
     *
     * @param messageId the id of the message
     * @return boolean
     */
    public boolean isModifyRecipientsAllowed(long messageId) {
        return isModifyRecipientsAllowed(getIRMLicenseFlag(messageId));
    }

    public static boolean isModifyRecipientsAllowed(int IRMLicenseFlag) {
        boolean modifyRecipientsAllowed = true;
        if (IRMLicenseFlag != MessagingException.IRM_DEFAULT_CODE) {
            modifyRecipientsAllowed = (IRMLicenseFlag & Message.MSG_CLASS_IRM_MODIFY_RECEPIENTS_ALLOWED) != 0;
        }
        return modifyRecipientsAllowed;
    }

    /**
     * To check that whether copy is allowed on the message.
     *
     * @param messageId the id of the message
     * @return boolean
     */
    public boolean isExtractAllowed(long messageId) {
        return isExtractAllowed(getIRMLicenseFlag(messageId));
    }

    public static boolean isExtractAllowed(int IRMLicenseFlag) {
        boolean extractAllowed = true;
        if (IRMLicenseFlag != MessagingException.IRM_DEFAULT_CODE) {
            extractAllowed = (IRMLicenseFlag & Message.MSG_CLASS_IRM_EXTRACT_ALLOWED) != 0;
        }
        return extractAllowed;
    }

    /**
     * To check that whether export is allowed on the message.
     *
     * @param messageId the id of the message
     * @return boolean
     */
    public boolean isExportAllowed(long messageId) {
        return isExportAllowed(getIRMLicenseFlag(messageId));
    }

    public static boolean isExportAllowed(int IRMLicenseFlag) {
        boolean exportAllowed = false;
        if (IRMLicenseFlag != MessagingException.IRM_DEFAULT_CODE) {
            exportAllowed = (IRMLicenseFlag & Message.MSG_CLASS_IRM_EXPORT_ALLOWED) != 0;
        }
        return exportAllowed;
    }

    /**
     * To check that whether print is allowed on the message.
     *
     * @param messageId the id of the message
     * @return boolean
     */
    public boolean isPrintAllowed(long messageId) {
        return isPrintAllowed(getIRMLicenseFlag(messageId));
    }

    public static boolean isPrintAllowed(int IRMLicenseFlag) {
        boolean printAllowed = true;
        if (IRMLicenseFlag != MessagingException.IRM_DEFAULT_CODE) {
            printAllowed = (IRMLicenseFlag & Message.MSG_CLASS_IRM_PRINT_ALLOWED) != 0;
        }
        return printAllowed;
    }

    /**
     * To check that whether accessing email data is allowed on the message.
     *
     * @param messageId the id of the message
     * @return boolean
     */
    public boolean isProgrammaticAccessAllowed(long messageId) {
        return isProgrammaticAccessAllowed(getIRMLicenseFlag(messageId));
    }

    public static boolean isProgrammaticAccessAllowed(int IRMLicenseFlag) {
        boolean programmaticAccessAllowed = true;
        if (IRMLicenseFlag != MessagingException.IRM_DEFAULT_CODE) {
            programmaticAccessAllowed = (IRMLicenseFlag & Message.MSG_CLASS_IRM_PROGRAMATIC_ACCESS_ALLOWED) != 0;
        }
        return programmaticAccessAllowed;
    }

    public int getIRMRemovalFlag(long messageId) {
        int removalFlag = -1;
        Cursor c = null;
        try {
            c = mContext.getContentResolver().query(
                    ContentUris.withAppendedId(Message.CONTENT_URI,
                            messageId), new String[] {
                        EmailContent.Message.IRM_REMOVAL_FLAG
                    }, null, null, null);
            if (c != null) {
                c.moveToFirst();
                removalFlag = c.getInt(0);
            }
        } catch (Exception e) {
            if (EmailLog.USER_LOG) {
                Log.e("IRM", "IRMEnforcer: exception in querying db:" + e);
            }
        } finally {
            if (c != null)
                c.close();
        }
        return removalFlag;
    }

    /**
     * To check that whether irm is enabled on the message.
     *
     * @param messageId the id of the message
     * @return boolean
     */
    public boolean isIRMEnabled(long messageId) {
        boolean irmEnabled = false;
        Cursor c = null;
        try {
            //modifying the URI so that it should hit the content cache first
            c = mContext.getContentResolver().query(
                    ContentUris.withAppendedId(EmailContent.Message.CONTENT_URI, messageId),
                    new String[] {
                    Message.IRM_TEMPLATE_ID
                }, null, null, null);
            if (c != null && (c.getCount() > 0)) {
                c.moveToFirst();
                String id = c.getString(0);

                if (id == null)
                    irmEnabled = false;
                else
                    irmEnabled = (id.length() > 0);
            }
        } catch (Exception e) {
            if (EmailLog.USER_LOG) {
                Log.e("IRM", "IRMEnforcer: exception in querying db:" + e);
            }
        } finally {
            if (c != null)
                c.close();
        }
        return irmEnabled;
    }

    /**
     * To return the IRM LIcense flag associated with the supplied message
     *
     * @param messageId id of the message
     * @return
     */
    public int getIRMLicenseFlag(long messageId) {
      //modifying the URI so that it should hit the content cache first
        Cursor c = mContext.getContentResolver().query(
                ContentUris.withAppendedId(EmailContent.Message.CONTENT_URI, messageId),
                new String[] {
                    Message.IRM_LICENSE_FLAG
                }, null, null, null);
        int IRMLicenseFlag = MessagingException.IRM_DEFAULT_CODE;
        if (c != null && (c.getCount() > 0)) {
            c.moveToFirst();
            IRMLicenseFlag = c.getInt(c.getColumnIndex(Message.IRM_LICENSE_FLAG));
        }
        if (c != null)
            c.close();
        return IRMLicenseFlag;
    }
}
