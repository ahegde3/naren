package com.samsung.android.emailcommon.security;

import android.net.Uri;
import android.provider.BaseColumns;

public class PrivateKey {

    public static final class PrivateKeys implements BaseColumns {
    	public static final String AUTHORITY = "com.samsung.android.emailsecurity.pgp.PGPContentProvider";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
                + "/privatekeys");

        public static final String PRIVATE_KEY_CONTENT_DIR_TYPE = "vnd.android.cursor.dir/pgp.contentprovider.org.private.key";

        // define table fields
        public static final String TABLE_NAME = "privatekeys";
        public static final String _ID_type = "INTEGER PRIMARY KEY";
        public static final String KEY_ID = "c_key_id";
        public static final String KEY_ID_type = "INT64";
        public static final String ALGORITHM = "c_algorithm";
        public static final String ALGORITHM_type = "INTEGER";
        public static final String CREATION = "c_creation";
        public static final String CREATION_type = "INTEGER";
        public static final String EXPIRY = "c_expiry";
        public static final String EXPIRY_type = "INTEGER";
        public static final String EMAIL_ID = "c_email_id";
        public static final String EMAIL_ID_type = "TEXT";
        public static final String USER_ID = "c_user_name";
        public static final String USER_ID_type = "TEXT";
        public static final String COMMENT = "c_comment";
        public static final String COMMENT_type = "TEXT";

        public static final String OLD_USER_ID = "c_user_id";
        public static final String OLD_USER_ID_type = "TEXT";
        public static final String OLD_NAME = "c_name";
        public static final String OLD_NAME_type = "TEXT";
        
        // public static final String KEY_SIZE = "c_key_size";
        // public static final String KEY_SIZE_type = "INTEGER";

        public static final String CAN_SIGN = "c_can_sign";
        public static final String CAN_SIGN_type = "INTEGER";
        public static final String KEY_RING_DATA = "c_key_ring";
        public static final String KEY_RING_TYPE = "BLOB";
        public static final String IS_DEFAULT_KEY = "c_is_default";
        public static final String IS_DEFAULT_type = "INTEGER";
        public static final String PASS_PHRASE = "c_pass_phrase";
        public static final String PASS_PHRASE_type = "TEXT";
    }
}