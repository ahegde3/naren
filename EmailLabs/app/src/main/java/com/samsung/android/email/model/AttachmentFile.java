package com.samsung.android.email.model;

/**
 * Created by nsbisht on 7/13/16.
 */
public class AttachmentFile {
    public static final int FILE_TYPE_IMAGE = 0;
    public static final int FILE_TYPE_VIDEO = 1;
    public static final int FILE_TYPE_PLAIN_TEXT = 3;

    private String mFilePath;

    private int mFileType;

    public AttachmentFile(String filePath, int fileType) {
        setmFilePath(filePath);
        setmFileType(fileType);
    }


    public int getmFileType() {
        return mFileType;
    }

    public void setmFileType(int mFileType) {
        this.mFileType = mFileType;
    }

    public String getmFilePath() {
        return mFilePath;
    }

    public void setmFilePath(String mFilePath) {
        this.mFilePath = mFilePath;
    }
}
