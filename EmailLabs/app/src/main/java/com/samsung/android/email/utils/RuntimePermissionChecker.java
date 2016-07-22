package com.samsung.android.email.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.samsung.android.emailcommon.utility.Log;

/**
 * Created by nsbisht on 7/22/16.
 */
public class RuntimePermissionChecker {
    private static final String TAG = RuntimePermissionChecker.class.getSimpleName();

    public final static int PERM_REQUEST_TYPE_STORAGE = 1;
    //Storage
    public static final String[] PERMISSION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static boolean hasPermissions(Context context, final String[] permissions) {
        for (final String permission : permissions) {
            if (!hasPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    public static void requestPermission(int requestType, Activity fragment) {
        if (fragment != null) {
            String[] permissions = getPermissionsByType(requestType);
            if (permissions != null)
                fragment.requestPermissions(permissions, requestType);
        }
    }
    private static boolean hasPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        if (context != null) {
            android.util.Log.i(TAG, "" + context.checkSelfPermission(permission));
            return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        } else {
            android.util.Log.i(TAG, " context is null in hasPermission");
            return false;
        }
    }

    public static String[] getPermissionsByType(int requestType) {
        String[] permissions = null;
        switch (requestType) {
            case PERM_REQUEST_TYPE_STORAGE:
                permissions = PERMISSION_STORAGE;
                break;
            default:
                Log.d(TAG, "requestPermission - switch default");
                break;
        }

        return permissions;
    }
}
