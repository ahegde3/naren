
package com.samsung.android.emailcommon.combined.common;

/**
 * CombinedEmail constant
 * 
 * @author hanklee
 */
public class MailConst {

    /** basic or combined email type value */
    public static final int TYPE_MESSAGE_FOR_BASIC = 0;
    public static final int TYPE_MESSAGE_FOR_PREMIUM = 1;
    // huijae.lee
    public static final int TYPE_MESSAGE_FOR_EXCHANGE = 2;

    /** callback process waiting time when called seven interface */
    public static final long MAX_CALLBACK_WAIT_TIME = 30000;

    /** sync folder for seven */
    public static final int SYNC_FOLDER_OFF = 0;
    public static final int SYNC_FOLDER_ON = 1;

    /** status for premium account */
    public static final int STATUS_PREMIUM_ACCOUNT_CHANGE_PWD = 4;
}
