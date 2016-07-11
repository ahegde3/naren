package com.samsung.android.emailcommon.service;

public interface EASMessengerConst {
//	final static public int RESULT_SUCCESS = 0;
//	final static public int RESULT_UNKONWN_FAIL = 11;
//	final static public int IN_PROGRESS_OOO = 32;
//	final static public int IN_PROGRESS_CHANGE_SMS_SETTINGS = 32;
//	final static public int IN_PROGRESS_EMPTY_TRASH = 32;
//	final static public int IN_PROGRESS_MOVE_CONV_ALWAYS = 32;
	
	final static public int PGP_KEY_ID_INVALID = -1;
	
    public static final int PUBLIC_LIST            = 0x1000;
    public static final int PUBLIC_EDIT_LIST     = 0x1001;
    public static final int PUBLIC_EXPORT_LIST    = 0x1010;
    public static final int PUBLIC_DETAIL         = 0x1011;

    public static final int PRIVATE_LIST         = 0x10000;
    public static final int PRIVATE_EDIT_LIST     = 0x10001;
    public static final int PRIVATE_EXPORT_LIST    = 0x10010;
    public static final int PRIVATE_DETAIL         = 0x10011;

    public static final int DEFAULT_LIST            = 0x10000000;
    public static final int ENCRYPTION_LIST        = 0x10000011;
    public static final int SIGN_LIST            = 0x10000111;
    public static final int DECRYPTION_LIST        = 0x10001111;
}
