package com.samsung.android.emailcommon.provider;

import com.samsung.android.emailcommon.utility.EmailLog;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class EmailContentUtils {
	
	final static private String TAG = "EmailContentUtils";
    public interface CursorGetter<T> {
        T get(Cursor cursor, int column);
    }
    
    private static final CursorGetter<Integer> INT_GETTER = new CursorGetter<Integer>() {
        public Integer get(Cursor cursor, int column) {
            return cursor.getInt(column);
        }
    };
    
	private static final CursorGetter<Long> LONG_GETTER = new CursorGetter<Long>() {
        public Long get(Cursor cursor, int column) {
            return cursor.getLong(column);
        }
    };
    
    private static final CursorGetter<String> STRING_GETTER = new CursorGetter<String>() {
        public String get(Cursor cursor, int column) {
            return cursor.getString(column);
        }
    };

    private static final CursorGetter<byte[]> BLOB_GETTER = new CursorGetter<byte[]>() {
        public byte[] get(Cursor cursor, int column) {
            return cursor.getBlob(column);
        }
    };
    
    /**
     * @return if {@code original} is to the EmailProvider, add "?limit=1".
     *         Otherwise just returns {@code original}. Other providers don't
     *         support the limit param. Also, changing URI passed from other
     *         apps can cause permission errors.
     */
    /* package */static Uri buildLimitOneUri(Uri original) {
        String uriString = original.toString();
        if ("content".equals(original.getScheme())
                && EmailContent.AUTHORITY.equals(original.getAuthority())
                && !uriString.endsWith("/-1")) {
            return EmailContent.uriWithLimit(original, 1);
        }
        return original;
    }
    
    /**
     * @return a generic in column {@code column} of the first result row, if
     *         the query returns at least 1 row. Otherwise returns
     *         {@code defaultValue}.
     */
    public static <T extends Object> T getFirstRowColumn(Context context, Uri uri,
            String[] projection, String selection, String[] selectionArgs, String sortOrder,
            int column, T defaultValue, CursorGetter<T> getter) {
        // Use PARAMETER_LIMIT to restrict the query to the single row we need
        Cursor c = null;
        try {
            uri = buildLimitOneUri(uri);
            c = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    sortOrder);
            if (c != null) {
                if (c.moveToFirst()) {
                    return getter.get(c, column);
                }
            }
        } catch (Exception e) {
            EmailLog.e(TAG, "getFirstRowColumn Exception!!");
        } finally {
            try {
                if (c != null)
                    c.close();
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }
    
	/**
     * {@link #getFirstRowColumn} for a Long with null as a default value.
     */
    public static Long getFirstRowLong(Context context, Uri uri, String[] projection,
            String selection, String[] selectionArgs, String sortOrder, int column) {
        return getFirstRowColumn(context, uri, projection, selection, selectionArgs, sortOrder,
                column, null, LONG_GETTER);
    }

    /**
     * {@link #getFirstRowColumn} for a Long with a provided default value.
     */
    public static Long getFirstRowLong(Context context, Uri uri, String[] projection,
            String selection, String[] selectionArgs, String sortOrder, int column,
            Long defaultValue) {
        return getFirstRowColumn(context, uri, projection, selection, selectionArgs, sortOrder,
                column, defaultValue, LONG_GETTER);
    }
    
    /**
     * {@link #getFirstRowColumn} for an Integer with null as a default value.
     */
    public static Integer getFirstRowInt(Context context, Uri uri, String[] projection,
            String selection, String[] selectionArgs, String sortOrder, int column) {
        return getFirstRowColumn(context, uri, projection, selection, selectionArgs, sortOrder,
                column, null, INT_GETTER);
    }

    /**
     * {@link #getFirstRowColumn} for an Integer with a provided default value.
     */
    public static Integer getFirstRowInt(Context context, Uri uri, String[] projection,
            String selection, String[] selectionArgs, String sortOrder, int column,
            Integer defaultValue) {
        return getFirstRowColumn(context, uri, projection, selection, selectionArgs, sortOrder,
                column, defaultValue, INT_GETTER);
    }

    /**
     * {@link #getFirstRowColumn} for a String with null as a default value.
     */
    public static String getFirstRowString(Context context, Uri uri, String[] projection,
            String selection, String[] selectionArgs, String sortOrder, int column) {
        return getFirstRowString(context, uri, projection, selection, selectionArgs, sortOrder,
                column, null);
    }

    /**
     * {@link #getFirstRowColumn} for a String with a provided default value.
     */
    public static String getFirstRowString(Context context, Uri uri, String[] projection,
            String selection, String[] selectionArgs, String sortOrder, int column,
            String defaultValue) {
        return getFirstRowColumn(context, uri, projection, selection, selectionArgs, sortOrder,
                column, defaultValue, STRING_GETTER);
    }

    /**
     * {@link #getFirstRowColumn} for a byte array with a provided default
     * value.
     */
    public static byte[] getFirstRowBlob(Context context, Uri uri, String[] projection,
            String selection, String[] selectionArgs, String sortOrder, int column,
            byte[] defaultValue) {
        return getFirstRowColumn(context, uri, projection, selection, selectionArgs, sortOrder,
                column, defaultValue, BLOB_GETTER);
    }
    
    /**
     * Convenience method wrapping calls to retrieve columns from a single row,
     * via EmailProvider. The arguments are exactly the same as to
     * contentResolver.query(). Results are returned in an array of Strings
     * corresponding to the columns in the projection. If the cursor has no
     * rows, null is returned.
     */
    public static String[] getRowColumns(Context context, Uri contentUri, String[] projection,
            String selection, String[] selectionArgs) {
        String[] values = new String[projection.length];
        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(contentUri, projection, selection, selectionArgs, null);
        try {
            if (c.moveToFirst()) {
                for (int i = 0; i < projection.length; i++) {
                    values[i] = c.getString(i);
                }
            } else {
                return null;
            }
        } finally {
            c.close();
        }
        return values;
    }

    /**
     * Convenience method for retrieving columns from a particular row in
     * EmailProvider. Passed in here are a base uri (e.g. Message.CONTENT_URI),
     * the unique id of a row, and a projection. This method calls the previous
     * one with the appropriate URI.
     */
    public static String[] getRowColumns(Context context, Uri baseUri, long id,
            String... projection) {
        return getRowColumns(context, ContentUris.withAppendedId(baseUri, id), projection, null,
                null);
    }
    
    public static boolean isFullMessageBodyLoadDisabled() {
    	// for SDL
//        return SecProductFeature_COMMON.SEC_PRODUCT_FEATURE_COMMON_KNOX_SDP_ENABLE && mIsSdpEnabled;
    	return false;
    }
    
    /**
     * Cut text to specified length and add specified ending.
     * Return text unchanged, if it is not longer than specified length
     * or if input parameters are invalid
     */
    public static String cutText(String text, int length, String overflowMsg, boolean isHtml) {
        if (text == null || length < 0 || text.length() <= length)
            return text;
        
        String resultText = null;
        if (isHtml)
            resultText=text.substring(0, length) + "<BR><BR>...<BR><BR>" + overflowMsg + "<BR>";
        else
            resultText=text.substring(0, length) + "\n\r\n\r...\n\r\n\r" + overflowMsg + "\n\r";
        
        return resultText;
    }
}
