package com.samsung.android.emailcommon.log;

import android.net.Uri;

public interface AddLogsConst {

    public static final String TYPE = "type";
    public static final String MSG = "msg";
    final static public String ACCOUNTSETUP = "accountsetup";
    final static public String ACCOUNTSTATS = "accountstats";
    final static public String IMAPDRAFTSSYNC = "ImapDraftsSync";
    final static public String CERT_ERROR_DIALOG = "certErrorDialog";
    public static final Uri CONTENT_URI = Uri.parse("content://com.samsung.android.email.sync.provider.AddLogsProvider");
    
}
