/*
 * Copyright (C) 2013 The Android Open Source Project
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.samsung.android.emailcommon.provider.BodyValues;
import com.samsung.android.emailcommon.provider.EmailContent;
import com.samsung.android.emailcommon.provider.EmailContent.Body;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

public class BodyUtilites {
    
    private static final String TAG = "BodyUtilites";
    private static final int READ_BUFFER_SIZE = 1024 * 256; // 256 KB
    private static final String BODY_CHARSET = "UTF-8";
    
    private static String sCutText = "...";
    
    public static File getBodyDirectory(Context context, long accountId) {
        File directory = null;
        if (context != null) {
            directory = new File(Utility.getSdpCacheDir(context), accountId + ".msgs_body");
        }
        return directory;
    }
    
    private static Uri getFileBodyUri(long accountId, long messageId, String part) {
        return EmailContent.Body.CONTENT_URI.buildUpon().appendPath("account")
                .appendPath(Long.toString(accountId)).appendPath("message")
                .appendPath(Long.toString(messageId)).appendPath(part).build();
    }
    
    public static String getFileBodyNameByPart(Context context, long accountId, long messageId, String part) {
        String fullFileName = null;
        
        if (accountId > 0 && messageId > 0 && !TextUtils.isEmpty(part)) {
            File directory = getBodyDirectory(context, accountId);
            File file = null;
            if (directory != null) {
                file = new File(directory, Long.toString(messageId) + "_" + part);
                if (file != null && file.exists()) {
                    fullFileName = file.getAbsolutePath(); 
                }
            }
        }
        
        Log.d(TAG, "getFileBodyName returns " + fullFileName);
        return fullFileName;
    }
    
    private static Uri getFileBodyDeleteUri(long accountId, long messageId) {
        return EmailContent.Body.CONTENT_URI.buildUpon().appendPath("account")
                .appendPath(Long.toString(accountId)).appendPath("message")
                .appendPath(Long.toString(messageId)).build();
    }

    public static Uri getFileBodyDeleteUri(long accountId) {
        return EmailContent.Body.CONTENT_URI.buildUpon().appendPath("account")
                .appendPath(Long.toString(accountId)).appendPath("message").build();
    }

    private static Uri getAccountFileBodyDeleteUri(long accountId) {
        return EmailContent.Body.CONTENT_URI.buildUpon().appendPath("account")
                .appendPath(Long.toString(accountId)).build();
    }

    public static void deleteAllMessageBodyFiles(Context context, long accountId, long messageId, int saveFlags) {
        EmailLog.d(TAG, "deleteAllMessageBodyFiles accountId = " + accountId + " messageId = " + Long.toString(messageId), " flags = " + saveFlags);
        if (saveFlags > 0) {
            File directory = getBodyDirectory(context, accountId);
            File file = null;
            if (directory != null) {
            	EmailLog.d(TAG, "directory = " + directory.getAbsolutePath());
                // File.delete() returns false in case of fail (for example, if file does not exist)
                file = new File(directory, Long.toString(messageId) + "_" + BodyValues.HTML_CONTENT_FILE_URI);
                if (file != null) file.delete();
                file = new File(directory, Long.toString(messageId) + "_" + BodyValues.TEXT_CONTENT_FILE_URI);
                if (file != null) file.delete();
                file = new File(directory, Long.toString(messageId) + "_" + BodyValues.HTML_REPLY_FILE_URI);
                if (file != null) file.delete();
                file = new File(directory, Long.toString(messageId) + "_" + BodyValues.TEXT_REPLY_FILE_URI);
                if (file != null) file.delete();
                file = new File(directory, Long.toString(messageId) + "_" + BodyValues.INTRO_FILE_URI);
                if (file != null) file.delete();
            } else {
            	EmailLog.d(TAG, "deleteAllMessageBodyFiles directory is null");
            }
        }
    }


    public static void deleteAllAccountBodyFilesForSelection(Context context, long accountId,
            String selection) {
        EmailLog.d(TAG, "deleteAllMessageBodyFiles accountId = " + accountId + " messageId = "
                + selection);
        if (TextUtils.isEmpty(selection)) {
            EmailLog.d(TAG, "deleteAllMessageBodyFiles selection is null");
            return;
        }
        File directory = getBodyDirectory(context, accountId);
        if (directory == null) {
            EmailLog.d(TAG, "deleteAllMessageBodyFiles directory is null");
        }
        else {
            EmailLog.d(TAG, "directory = " + directory.getAbsolutePath());
        }
        selection = selection + " AND " + Body.FILE_SAVE_FLAGS + " > 0";
        long[] messageIds = Body.getMessagesIdsWhere(context, selection, null);
        if (messageIds == null || messageIds.length <= 0) {
            EmailLog.d(TAG, "deleteAllMessageBodyFiles messageIds is null or 0");
            return;
        }

        File file = null;
        for (long messageId : messageIds) {
            EmailLog.d(TAG, "messageId = " + messageId);
            file = new File(directory, Long.toString(messageId) + "_" + BodyValues.HTML_CONTENT_FILE_URI);
            if (file != null)
                file.delete();
            file = new File(directory, Long.toString(messageId) + "_" + BodyValues.TEXT_CONTENT_FILE_URI);
            if (file != null)
                file.delete();
            file = new File(directory, Long.toString(messageId) + "_" + BodyValues.HTML_REPLY_FILE_URI);
            if (file != null)
                file.delete();
            file = new File(directory, Long.toString(messageId) + "_" + BodyValues.TEXT_REPLY_FILE_URI);
            if (file != null)
                file.delete();
            file = new File(directory, Long.toString(messageId) + "_" + BodyValues.INTRO_FILE_URI);
            if (file != null)
                file.delete();
        }
    }

    //    public static void deleteAllMessageBodyFiles(Context context, long messageId) {
//        deleteAllMessageBodyFiles(context, Account.getAccountIdForMessageId(context, messageId), messageId);
//    }
    
    public static void deleteAllMessageBodyFilesUri(Context context, long accountId, long messageId) {
        context.getContentResolver().delete(getFileBodyDeleteUri(accountId, messageId), null, null);
    }
    
//    public static void deleteAllMessageBodyFilesUri(Context context,  long messageId) {
//        context.getContentResolver().delete(getFileBodyDeleteUri(Account.getAccountIdForMessageId(context, messageId), messageId), null, null);
//    }
    
 

    
    public static void deleteAllAccountBodyFiles(Context context, long accountId) {
    	EmailLog.d(TAG, "deleteAllAccountBodyFiles accountId = " + accountId);
        File directory = getBodyDirectory(context, accountId);
        if (directory == null)
            return;

        File[] files = directory.listFiles();
        if (files == null) {
            directory.delete();
            return;
        }
        for (File file : files) {
            boolean result = file.delete();
            if (!result) {
            	EmailLog.e(TAG, "Failed to delete attachment file " + file.getName());
            }
        }
        directory.delete();
    }
    
    public static void deleteAllAccountBodyFilesUri(Context context, long accountId) {
        context.getContentResolver().delete(getAccountFileBodyDeleteUri(accountId), null, null);
    }
    
    private static void writeBodyContentToFile(Context context, Uri uri, String contentToWrite) {
    	EmailLog.d(TAG, "writeBodyContentToFile() : start");
        if (context == null) {
        	EmailLog.d(TAG, "writeBodyContentToFile() : context is null! Do nothing.");
            return;
        }
        
        if (contentToWrite == null) {
        	EmailLog.d(TAG, "writeBodyContentToFile() : contentToWrite is null. Replace by empty string");
            contentToWrite = "";
        } 
        
        OutputStream outputStream = null;
        
        try {
            outputStream = context.getContentResolver().openOutputStream(uri);
        } catch (FileNotFoundException e) {
        	EmailLog.d(TAG, "FileNotFoundException: " + e.toString());
        }
        
        if (outputStream != null) {
            try {
                outputStream.write(contentToWrite.getBytes(BODY_CHARSET));
                outputStream.flush();
            } catch (IOException e) {
                EmailLog.dumpException(TAG, e);
            } finally {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    EmailLog.dumpException(TAG, e);
                }
            }
            
            if (Utility.isSdpEnabled()) {
                ContentValues cv = new ContentValues();
                context.getContentResolver().update(uri, cv, null, null);
            }
        }
        EmailLog.d(TAG, "writeBodyContentToFile() : finish");
    }
    
    private static String readBodyContentFromFile(Context context, Uri uri) {
    	EmailLog.d(TAG, "readBodyContentFromFile() : start");
        String readContent = null;
        if (context != null) {
            InputStream inputStream = null;
            
            try {
                inputStream = context.getContentResolver().openInputStream(uri);                
            } catch (FileNotFoundException e) {
            	EmailLog.d(TAG, "readBodyContentFromFile() FileNotFoundException: " + e.toString());
            }
            
            if (inputStream != null) {
                byte[] buffer = new byte[READ_BUFFER_SIZE];
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(READ_BUFFER_SIZE);
                int n = 0;
                try {
                    while (-1 != (n = inputStream.read(buffer))) {
                        outputStream.write(buffer,0,n);
                    }
                    outputStream.flush();
                    readContent = outputStream.toString(BODY_CHARSET);
                } catch (Exception e) {
                    EmailLog.dumpException(TAG, e);
                } finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        EmailLog.dumpException(TAG, e);
                    }
                    
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        EmailLog.dumpException(TAG, e);
                    }
                }

            }
        } else {
        	EmailLog.d(TAG, "readBodyContentFromFile() : context is null! Return null.");
        }
        
        EmailLog.d(TAG, "readBodyContentFromFile() : finish");
        return readContent;
    }
    
    private static boolean isBodyFileExists(Context context, long accountId, long messageId, String part) {
        boolean result = true;
        if (context != null && accountId > 0 && messageId > 0 && part != null) {
            InputStream inputStream = null;
            try {
                inputStream = context.getContentResolver().openInputStream(getFileBodyUri(accountId, messageId, part));
            } catch (FileNotFoundException e) {
                result = false;
            } finally {
                if (inputStream != null)
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        // Just ignore
                    }
            }
        }
        EmailLog.d(TAG, "isBodyFileExists accountId = " + accountId + " messageId = " + messageId + " part = " + part + " exist = " + result);
        return result;
    }

    public static void writeHtmlContentToFile(Context context, long accountId, long messageId, String contentToWrite) {
        writeBodyContentToFile(context, getFileBodyUri(accountId, messageId, BodyValues.HTML_CONTENT_FILE_URI), contentToWrite);
    }
    
    public static void writeTextContentToFile(Context context, long accountId, long messageId, String contentToWrite) {
        writeBodyContentToFile(context, getFileBodyUri(accountId, messageId, BodyValues.TEXT_CONTENT_FILE_URI), contentToWrite);
    }
    
    public static void writeHtmlReplyToFile(Context context, long accountId, long messageId, String contentToWrite) {
        writeBodyContentToFile(context, getFileBodyUri(accountId, messageId, BodyValues.HTML_REPLY_FILE_URI), contentToWrite);
    }
    
    public static void writeTextReplyToFile(Context context, long accountId, long messageId, String contentToWrite) {
        writeBodyContentToFile(context, getFileBodyUri(accountId, messageId, BodyValues.TEXT_REPLY_FILE_URI), contentToWrite);
    }
    
    public static void writeIntroToFile(Context context, long accountId, long messageId, String contentToWrite) {
        writeBodyContentToFile(context, getFileBodyUri(accountId, messageId, BodyValues.INTRO_FILE_URI), contentToWrite);
    }

    public static String readHtmlContentFromFile(Context context, long accountId, long messageId) {
        return readBodyContentFromFile(context, getFileBodyUri(accountId, messageId, BodyValues.HTML_CONTENT_FILE_URI));
    }

    public static String readTextContentFromFile(Context context, long accountId, long messageId) {
        return readBodyContentFromFile(context, getFileBodyUri(accountId, messageId, BodyValues.TEXT_CONTENT_FILE_URI));
    }

    public static String readHtmlReplyFromFile(Context context, long accountId, long messageId) {
        return readBodyContentFromFile(context, getFileBodyUri(accountId, messageId, BodyValues.HTML_REPLY_FILE_URI));
    }

    public static String readTextReplyFromFile(Context context, long accountId, long messageId) {
        return readBodyContentFromFile(context, getFileBodyUri(accountId, messageId, BodyValues.TEXT_REPLY_FILE_URI));
    }

    public static String readIntroFromFile(Context context, long accountId, long messageId) {
        return readBodyContentFromFile(context, getFileBodyUri(accountId, messageId, BodyValues.INTRO_FILE_URI));
    }

    public static boolean isTextContentFileExists(Context context, long accountId, long messageId) {
        return isBodyFileExists(context, accountId, messageId, BodyValues.TEXT_CONTENT_FILE_URI);
    }

    public static boolean isHtmlContentFileExists(Context context, long accountId, long messageId) {
        return isBodyFileExists(context, accountId, messageId, BodyValues.HTML_CONTENT_FILE_URI);
    }

    public static boolean isTextReplyFileExists(Context context, long accountId, long messageId) {
        return isBodyFileExists(context, accountId, messageId, BodyValues.TEXT_REPLY_FILE_URI);
    }

    public static boolean isHtmlReplyFileExists(Context context, long accountId, long messageId) {
        return isBodyFileExists(context, accountId, messageId, BodyValues.HTML_REPLY_FILE_URI);
    }

    public static boolean isIntroFileExists(Context context, long accountId, long messageId) {
        return isBodyFileExists(context, accountId, messageId, BodyValues.INTRO_FILE_URI);
    }
    
    public static String getCutText() {
        return sCutText;
    }

    public static void setCutText(String cutText) {
        sCutText = cutText;
    }
    
}
