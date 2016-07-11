package com.samsung.android.emailcommon.utility;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import com.samsung.android.emailcommon.Preferences;
import com.samsung.android.emailcommon.utility.EmailLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
   Function             Permission   Group                                Permissions
  ======================================================================================================
    Calendar      android.permission-group.CALENDAR     ??  android.permission.READ_CALENDAR
                                                        ??  android.permission.WRITE_CALENDAR

    Camera    android.permission-group.CAMERA           ??  android.permission.CAMERA

    Contact    android.permission-group.CONTACTS        ??  android.permission.READ_CONTACTS
                                                        ??  android.permission.WRITE_CONTACTS
                                                        ??  android.permission.GET_ACCOUNTS

    Location     android.permission-group.LOCATION      ??  android.permission.ACCESS_FINE_LOCATION
                                                        ??  android.permission.ACCESS_COARSE_LOCATION

    Recoder     android.permission-group.MICROPHONE     ??  android.permission.RECORD_AUDIO

     Call     android.permission-group.PHONE            ??  android.permission.READ_PHONE_STATE
                                                        ??  android.permission.CALL_PHONE
                                                        ??  android.permission.READ_CALL_LOG
                                                        ??  android.permission.WRITE_CALL_LOG
                                                        ??  com.android.voicemail.permission.ADD_VOICEMAIL
                                                        ??  android.permission.USE_SIP
                                                        ??  android.permission.PROCESS_OUTGOING_CALLS

    Sensor     android.permission-group.SENSORS         ??  android.permission.BODY_SENSORS

    Text     android.permission-group.SMS               ??  android.permission.SEND_SMS
                                                        ??  android.permission.RECEIVE_SMS
                                                        ??  android.permission.READ_SMS
                                                        ??  android.permission.RECEIVE_WAP_PUSH
                                                        ??  android.permission.RECEIVE_MMS
                                                        ??  android.permission.READ_CELL_BROADCASTS

   Storage   android.permission-group.STORAGE           ??  android.permission.READ_EXTERNAL_STORAGE
                                                        ??  android.permission.WRITE_EXTERNAL_STORAGE
*/

public class EmailRuntimePermission {
    private static final String TAG = "RuntimePermission";

    public final static int PERM_REQUEST_TYPE_APP = 0;

    public final static int PERM_REQUEST_TYPE_CONTACTS = 2;

    public final static int PERM_REQUEST_TYPE_SMS = 3;

    public final static int PERM_REQUEST_TYPE_CALENDAR = 4;

    public final static int PERM_REQUEST_TYPE_EAS = 5;

    public final static int PERM_REQUEST_TYPE_TASK = 6;

    public final static int PERM_REQUEST_TYPE_STORAGE = 7;
    
    public final static int PERM_REQUEST_TYPE_CONTACTS_BY_MAILBOX_LIST = 8;
    
    public final static int PERM_REQUEST_TYPE_CONTACTS_BY_VIEW = 10;

    public final static int PERM_REQUEST_TYPE_CALENDAR_BY_VIEW = 11;

    public final static int PERM_REQUEST_TYPE_STORAGE_BY_VIEW = 12;
    
    public final static int PERM_REQUEST_TYPE_CONTACTS_BY_SEARCH_ON_SERVER = 13;

    public final static int PERM_REQUEST_TYPE_INSERT = 14;

    public final static int PERM_REQUEST_TYPE_INSERT_MAP = 15;

    public final static int PERM_REQUEST_TYPE_CONTACTS_FOR_EMAILBOOK = 16;

    public final static int PERM_REQUEST_TYPE_ATTACH = 17;

    public final static int PERM_REQUEST_TYPE_ATTACH_MAP = 18;

    public final static int PERM_REQUEST_TYPE_TAKE_PICTURE = 19;

    public final static int PERM_REQUEST_TYPE_RECORD_VIDEO = 20;

    public final static int PERM_REQUEST_TYPE_EAS_BASIC = 21;

    public final static int PERM_REQUEST_TYPE_EAS_BASIC_WDS = 22;

    public final static int PERM_REQUEST_TYPE_EAS_TYPE = 23;

    public final static int PERM_REQUEST_TYPE_EAS_EXCHANGE = 24;

    public final static int PERM_REQUEST_TYPE_ATTACH_DROP_ITEMS = 25;

    public final static int PERM_REQUEST_TYPE_CALENDAR_BY_MESSAGE_LIST = 26;

    public final static int PERM_REQUEST_TYPE_MY_FILES = 27;
    
    public final static int PERM_REQUEST_TYPE_CALENDAR_BY_MOVE_ITEM_IN_MESSAGE_LIST = 28;

    public final static int PERM_REQUEST_TYPE_CONTACTS_IN_SETUP_OPTION = 29;

    public final static int PERM_REQUEST_TYPE_CALENDAR_TASK_IN_SETUP_OPTION = 30;

    public final static int PERM_REQUEST_TYPE_CONTACTS_FOR_GAL_SEARCH = 31;

    public final static int PERM_REQUEST_TYPE_STORAGE_RINGTONE = 32;

    public final static int PERM_REQUEST_TYPE_STORAGE_RINGTONEVIP = 33;

    public final static int PERM_REQUEST_TYPE_CONTACTS_IN_SETUP_OPTION_NEXT = 34;

    public final static int PERM_REQUEST_TYPE_CALENDAR_TASK_IN_SETUP_OPTION_NEXT = 35;

    public final static int PERM_REQUEST_TYPE_SMS_IN_SETUP_OPTION_NEXT = 36;

    public final static int PERM_REQUEST_TYPE_STORAGE_RINGTONE_EXTERNAL = 37;

    public final static int PERM_REQUEST_TYPE_SMS_SETUP = 38;

    public final static int PERM_REQUEST_TYPE_STORAGE_VOICE_RECORDER = 39;

    public final static int PERM_REQUEST_TYPE_CONTACT_CALENDAR = 40;

    public final static int PERM_REQUEST_TYPE_CALENDAR_IN_THREAD_LIST = 41;

    public final static int PERMISSION_FUNCTION_CONTACT_INFO = 0;

    public final static int PERMISSION_FUNCTION_EAS_ACCOUNT = 1;

    //The following five constants for Calendar event (PERMISSION_CONTACT_CALENDAR)
    public static final int PERMISSION_READ_CONTACTS = 0;

    public static final int PERMISSION_WRITE_CONTACTS = 1;

    public static final int PERMISSION_GET_ACCOUNTS = 2;

    public static final int PERMISSION_READ_CALENDAR = 3;

    public static final int PERMISSION_WRITE_CALENDAR = 4;

    public static HashMap<String, Integer> isRationalePermission = new HashMap();

    public static final String[] PERMISSION_APP = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS,
    };
    
    //Contact
    public static final String[] PERMISSION_CONTACTS = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS,
    };

    //SMS
    public static final String[] PERMISSION_SMS = {
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_WAP_PUSH,
            Manifest.permission.RECEIVE_MMS,
    };

    //Calendar
    public static final String[] PERMISSION_CALENDAR = {
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
    };

    public static final String[] PERMISSION_EAS ={
//            Manifest.permission.READ_CALL_LOG,
//            Manifest.permission.WRITE_CALL_LOG,

            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR,

            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS
    };

    //contact & calendar
    public static final String[] PERMISSION_CONTACT_CALENDAR = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR,
    };

    //Storage
    public static final String[] PERMISSION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //Camera Provider
    public static String[] PERMISSION_RCL_CAMERA_PROVIDER = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    // Map
    public static String[] PERMISSION_LOCATION = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE
    };

    // Record Video
    public static String[] PERMISSION_RECORD_VIDEO = {
            Manifest.permission.CAMERA
    };

    private static String mPermission;

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

    public static boolean hasPermissions(Context context, final String[] permissions) {
        for (final String permission : permissions) {
            if (!hasPermission(context, permission)) {
                return false;
            }
            isRationalePermission.remove(permission);
            setSamsungPermissionState(context);
        }
        return true;
    }

    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if(grantResults.length < 1){
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /*public static String getPermission(int requestType, String[] permissons) {
        return permissons[requestType];
    }*/

    public static ArrayList<String> requestPermission(int requestType, Activity activity) {
        try {
            if (activity != null) {
                String[] permissions = getPermissionsByType(requestType);

                Log.d(TAG, "requestType:"+requestType);

	        if(permissions == null) return null;

                ArrayList<String> unsatisfiedPermissions = new ArrayList<>();
                ArrayList<String> neverShowAgainPermissions = new ArrayList<>();

                if(isRationalePermission.size() == 0){
                    getSamsungPermissionState(activity);
                }
                for (String permission : permissions) {
                    if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "rationale - " + activity.shouldShowRequestPermissionRationale(permission));
                        if(isRationalePermission.get(permission) == null && activity.shouldShowRequestPermissionRationale(permission)==false){//first request
                            unsatisfiedPermissions.add(permission);
                            Log.d(TAG, "rationale - 1 :"+permission.toString());
                        }
                        else if (isRationalePermission.get(permission) == null && activity.shouldShowRequestPermissionRationale(permission)==true) {//Never ask popup
                            unsatisfiedPermissions.add(permission);
                            isRationalePermission.put(permission, 1);
                            Log.d(TAG, "rationale - 2 :"+permission.toString());
                        }
                        else if ((int)isRationalePermission.get(permission) == 1 && activity.shouldShowRequestPermissionRationale(permission)==false) {
                            neverShowAgainPermissions.add(permission);
                            Log.d(TAG, "rationale - 3:"+permission.toString());
                        }
                        else{
                            unsatisfiedPermissions.add(permission);
                            isRationalePermission.put(permission, 1);
                            Log.d(TAG, "rationale - 4 : error case");
                        }

                        setSamsungPermissionState(activity);
                    }
                    else{

                    }
                }
                if (unsatisfiedPermissions.size() != 0) {
                    EmailLog.d(TAG, activity.getClass().getSimpleName() + " requests permissions for " + mPermission + ", requestCode: " + requestType);
                    for (String permission : permissions) {
                        EmailLog.d(TAG, "permission: " + permission);
                    }
                    activity.requestPermissions(
                            unsatisfiedPermissions.toArray(new String[unsatisfiedPermissions.size()]), requestType);
                }
                //In case of calendar event, one  permission (Calendar/Contact) might be unsatisfied and other permission
                // might be "never show"
                if (neverShowAgainPermissions.size() != 0) {
                    EmailLog.d(TAG, "never ask again for permission: " + mPermission);
                    return neverShowAgainPermissions;
                }
            }
        }catch(Exception e){
            Log.e(TAG, "rationale - Exception",e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    private static void getSamsungPermissionState(Context context){
        isRationalePermission.clear();
        SharedPreferences pref = context.getSharedPreferences(Preferences.PREPERENCE_SAMSUNG_PERMISSION_STATE, Context.MODE_PRIVATE);
        HashMap<String, Integer> map= (HashMap<String, Integer>) pref.getAll();
        for (Map.Entry entry : map.entrySet()) {
            String s = (String)entry.getKey();
            Integer value=(Integer)entry.getValue();
            //Use Value
            isRationalePermission.put(s, value);
        }
    }

    private static void setSamsungPermissionState(Context context){
        SharedPreferences pref = context.getSharedPreferences(Preferences.PREPERENCE_SAMSUNG_PERMISSION_STATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        for (String s : isRationalePermission.keySet()) {
            editor.putInt(s, isRationalePermission.get(s));
        }
        editor.commit();
    }

    public static void requestPermission(int requestType, Fragment fragment) {
        if (fragment != null) {
            String[] permissions = getPermissionsByType(requestType);
            if (permissions != null)
                fragment.requestPermissions(permissions, requestType);
        }
    }

    public static String[] getPermissionsByType(int requestType) {
        String[] permissions = null;
        switch (requestType) {
            case PERM_REQUEST_TYPE_APP:
                permissions = PERMISSION_APP;
                mPermission = "APP";
                break;
            case PERM_REQUEST_TYPE_CONTACTS:
            case PERM_REQUEST_TYPE_CONTACTS_IN_SETUP_OPTION:
            case PERM_REQUEST_TYPE_CONTACTS_BY_MAILBOX_LIST:
            case PERM_REQUEST_TYPE_CONTACTS_BY_VIEW:
            case PERM_REQUEST_TYPE_CONTACTS_BY_SEARCH_ON_SERVER:
            case PERM_REQUEST_TYPE_CONTACTS_FOR_EMAILBOOK:
            case PERM_REQUEST_TYPE_CONTACTS_FOR_GAL_SEARCH:
            case PERM_REQUEST_TYPE_CONTACTS_IN_SETUP_OPTION_NEXT:
                permissions = PERMISSION_CONTACTS;
                mPermission = "CONTACTS";
                break;
            case PERM_REQUEST_TYPE_SMS:
            case PERM_REQUEST_TYPE_SMS_IN_SETUP_OPTION_NEXT:
            case PERM_REQUEST_TYPE_SMS_SETUP:
                permissions = PERMISSION_SMS;
                mPermission = "SMS";
                break;
            case PERM_REQUEST_TYPE_CALENDAR:
            case PERM_REQUEST_TYPE_TASK:
            case PERM_REQUEST_TYPE_CALENDAR_TASK_IN_SETUP_OPTION:
            case PERM_REQUEST_TYPE_CALENDAR_BY_VIEW:
            case PERM_REQUEST_TYPE_CALENDAR_BY_MESSAGE_LIST:
            case PERM_REQUEST_TYPE_CALENDAR_BY_MOVE_ITEM_IN_MESSAGE_LIST:
            case PERM_REQUEST_TYPE_CALENDAR_TASK_IN_SETUP_OPTION_NEXT:
                permissions = PERMISSION_CALENDAR;
                mPermission = "CALENDAR";
                break;
            case PERM_REQUEST_TYPE_CONTACT_CALENDAR:
                permissions = PERMISSION_CONTACT_CALENDAR;
                mPermission = "CALENDAR";
                break;
            case PERM_REQUEST_TYPE_EAS:
                permissions = PERMISSION_EAS;
                mPermission = "EAS";
                break;
            case PERM_REQUEST_TYPE_STORAGE:
            case PERM_REQUEST_TYPE_STORAGE_BY_VIEW:
            case PERM_REQUEST_TYPE_ATTACH:
            case PERM_REQUEST_TYPE_ATTACH_DROP_ITEMS:
            case PERM_REQUEST_TYPE_MY_FILES:
            case PERM_REQUEST_TYPE_STORAGE_RINGTONE:
            case PERM_REQUEST_TYPE_STORAGE_RINGTONEVIP:
            case PERM_REQUEST_TYPE_STORAGE_RINGTONE_EXTERNAL:
            case PERM_REQUEST_TYPE_STORAGE_VOICE_RECORDER:
                permissions = PERMISSION_STORAGE;
                mPermission = "STORAGE";
                break;

            case PERM_REQUEST_TYPE_INSERT:
                permissions = PERMISSION_RCL_CAMERA_PROVIDER;
                mPermission = "RCL_CAMERA_PROVIDER";
                break;

            case PERM_REQUEST_TYPE_INSERT_MAP:
            case PERM_REQUEST_TYPE_ATTACH_MAP:
                permissions = PERMISSION_LOCATION;
                mPermission = "LOCATION";
                break;

            case PERM_REQUEST_TYPE_TAKE_PICTURE:
            case PERM_REQUEST_TYPE_RECORD_VIDEO:
                permissions = PERMISSION_RECORD_VIDEO;
                mPermission = "RECORD_VIDEO";
                break;

            default:
                Log.d(TAG, "requestPermission - switch default");
                break;
        }

        return permissions;
    }

    public static ArrayList<String> getRequiredPermissionList(int requestType, Activity activity) {
        ArrayList<String> requiredPermissionLists = new ArrayList<String>();

        String[] permissions = getPermissionsByType(requestType);

        for (String permission : permissions) {
            if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                requiredPermissionLists.add(permission);
            }
        }

        return requiredPermissionLists;
    }
}
