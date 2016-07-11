package com.samsung.android.emailcommon.security;

import android.net.Uri;
import android.provider.BaseColumns;

public class KeyRing {

    public static final class KeyRings implements BaseColumns {
    	public static final String AUTHORITY = "com.samsung.android.emailsecurity.pgp.PGPContentProvider";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
                + "/keyrings");

        public static final String KEY_RING_CONTENT_DIR_TYPE = "vnd.android.cursor.dir/pgp.contentprovider.org.keyring";
        public static final String TABLE_NAME = "keyrings";
        public static final String _ID_type = "INTEGER PRIMARY KEY";
        public static final String MASTER_KEY_ID = "c_master_key_id";
        public static final String MASTER_KEY_ID_type = "INT64";
        public static final String TYPE = "c_type";
        public static final String TYPE_type = "INTEGER";
        public static final String KEY_RING_DATA = "c_key_ring_data";
        public static final String KEY_RING_DATA_type = "BLOB";
        public static final String KEY_RID = "c_id";
        public static final String KEY_RID_type = "INTEGER";
    }

}