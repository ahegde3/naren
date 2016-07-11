
package com.samsung.android.emailcommon;

public class AccountRefs {
    public static final int DELETE_POLICY_NEVER = 0;
    public static final int DELETE_POLICY_DOWNSYNC = 1;
    public static final int DELETE_POLICY_ON_DELETE = 2;//(UPSYNC_ONLY)
	public static final int DELETE_POLICY_SYNC = 3;
    public static final int CHECK_INTERVAL_NEVER = -1;
    public static final int CHECK_INTERVAL_PUSH = -2;

    // These flags will never be seen in a "real" (legacy) account
    public static final int BACKUP_FLAGS_IS_BACKUP = 1;
    public static final int BACKUP_FLAGS_SYNC_CONTACTS = 2;
    public static final int BACKUP_FLAGS_IS_DEFAULT = 4;
    public static final int BACKUP_FLAGS_SYNC_CALENDAR = 8;

    // Since email sync has always been "on" prior to the creation of this flag,
    // it's sense is
    // reversed to avoid legacy issues.
    // change@siso.subbu for tasks
    public static final int BACKUP_FLAGS_SYNC_TASK = 64;

    // change@siso.saritha for Notes
    public static final int BACKUP_FLAGS_SYNC_NOTES = 128;
    // public static final int BACKUP_FLAGS_DONT_SYNC_EMAIL = 16;
    public static final int BACKUP_FLAGS_SYNC_EMAIL = 16; // SYNC EMAIL ICS
    public static final int BACKUP_FLAGS_BACKGROUND_ATTACHMENTS = 32;
}
