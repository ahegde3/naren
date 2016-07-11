/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.samsung.android.emailcommon.utility;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.NetworkInfo.State;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;

import com.samsung.android.emailcommon.EmailFeature;
import com.samsung.android.emailcommon.Logging;
import com.samsung.android.emailcommon.Preferences;
import com.samsung.android.emailcommon.system.CarrierValues;

import java.lang.reflect.Method;

public class DataConnectionUtil extends Activity {
    public static final String TAG = "DataConnectionUtil";
    public static final int NETWORKERR_INVALID = -1;
    public static final int NO_ACTIVE_NETWORK = -1;
    private static final int NETWORKERR_FLIGHTMODE_ON = 0;
    private static final int NETWORKERR_MOBILEDATA_OFF = 1;
    private static final int NETWORKERR_DATAROAMING_OFF = 2;
    private static final int NETWORKERR_REACHED_DATALIMIT = 3;
    private static final int NETWORKERR_NO_SIGNAL = 4;
    private static final int NETWORKERR_MMS_EMAIL = 5;
    //    private static final int NETWORKERR_WIFI_ONLY = 6;
    private final static String CONNECTIVITY_CHANGE_INTENT = "android.net.conn.CONNECTIVITY_CHANGE";
    public static int sDataConnectionState = -1;
    private static boolean isNeedConnectionPopuped = true;
    private static DataConnectionUtil sInstance;
    private static NetworkInfo mNetworkInfo = null;
    private static MobileDataBroadcastReceiver receiver = new MobileDataBroadcastReceiver();
    Context mContext;
    ConnectivityManager connectivityManager;
    TelephonyManager telephonyManager;

    protected DataConnectionUtil(Context _context) {
        mContext = _context.getApplicationContext();
    }

    public synchronized static DataConnectionUtil getInstance(Context _context) {
        if (sInstance == null) {
            if (_context == null) {
                return null;
            }
            sInstance = new DataConnectionUtil(_context);
        }
        return sInstance;
    }

    public static void changeisNeedConnectionPopuped() {
        isNeedConnectionPopuped = true;
    }

    public static void startReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTIVITY_CHANGE_INTENT);
        context.registerReceiver(receiver, intentFilter);
    }

    public static void stopReceiver(Context context) {
        try {
            context.unregisterReceiver(receiver);
        } catch (IllegalArgumentException e) {
            Log.e(Logging.LOG_TAG, "stopReceiver(): IllegalArgumentException..." + e.toString());
        }

    }

    public static boolean isNetworkConnected(Context context) {
        try {
            NetworkInfo info = ((ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            if (info == null) {
                Log.d(Logging.LOG_TAG, "isNetworkConnected(): info is null");
                return false;
            }
            if (info.getType() == ConnectivityManager.TYPE_WIFI)
                return true;
            else {
                if (info.isConnected()) {
                    DetailedState state = info.getDetailedState();
                    if (state == DetailedState.CONNECTED) {
                        Log.d(Logging.LOG_TAG,
                                "isNetworkConnected(): registerForConnectionStateChanges");
                        if (receiver.getConnectionState()) {
                            return true;
                        } else {
                            Log.d(Logging.LOG_TAG,
                                    "isNetworkConnected(): network is not connected");
                            //DataConnection.ShowDataConnectionPopup(activity, NETWORKERR_MOBILEDATA_OFF);
                            return false;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(Logging.LOG_TAG, "isNetworkConnected(): exception...");
        }
        Log.d(Logging.LOG_TAG, "isNetworkConnected(): network is disconnected");
        return false;
    }

    public static ConnectivityManager connectivityService(Context context) {
        android.net.ConnectivityManager cm;
        if (context != null) {
            cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm;
        } else
            return null;
    }

    @SuppressWarnings("deprecation")
    public static boolean isBackgroundDataAllowed(Context context) {
        if (context != null) {
            android.net.ConnectivityManager cm;
            cm = connectivityService(context);
            if (cm != null) return cm.getBackgroundDataSetting();
            else return false;
        } else
            return false;
    }

    static public int getActiveNetworkType(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return getActiveNetworkType(cm);
    }

    static public int getActiveNetworkType(ConnectivityManager cm) {
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return NO_ACTIVE_NETWORK;
        return info.getType();
    }

    /**
     * Request current connectivity status
     *
     * @return whether there is connectivity at this time
     */
    public static boolean hasConnectivity(Context context) {
        android.net.ConnectivityManager cm = connectivityService(context);
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            return (info != null);
        } else return false;
    }

    public static boolean canConnect(Context context) {
        if (Preferences.getPreferences(context).getIgnoreDataConnection())
            return true;
        if (isWifiOnly(context)) {
            return true;
        }
        if (isAirPlaneModeOn(context) == false && isDataConnectivityOn(context) == true) {
            return true;
        }
        return false;
    }

    public static boolean isWifiOnly(Context context) {
        boolean mIsWifiOnly = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (null == mNetworkInfo) {
            allNetwork(connectivityManager);
        } else if (mNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {

            boolean cellConnected = false;
            NetworkInfo info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (null != info) {
                if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        cellConnected = true;
                    }
                }
            }
            mIsWifiOnly = (mNetworkInfo != null) && (mNetworkInfo
                    .getType() == ConnectivityManager.TYPE_WIFI) && !cellConnected;
        }

        return mIsWifiOnly;
    }

    public static boolean isConnectedWifi(Context context) {
        boolean mIsWLAN = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            mNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (null == mNetworkInfo) {
                allNetwork(connectivityManager);
            } else {
                if (mNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    mIsWLAN = true;
                }
            }
        }
        return mIsWLAN;
    }

    static void allNetwork(ConnectivityManager connectivityManager) {
        NetworkInfo[] allNetwork = connectivityManager.getAllNetworkInfo();
        if (allNetwork != null) {
            for (NetworkInfo info : allNetwork) {
                Log.i(TAG, "All Network Information " + "[ State :" + info.getState() + "]" +
                        "[ Type :" + info.getType() + "]" +
                        "[ Type Name :" + info.getTypeName() + "]" +
                        "[ SubType Name :" + info.getSubtypeName() + "]" +
                        "[ IsAvailable :" + info.isAvailable() + "]" +
                        "[ IsConnected :" + info.isConnected() + "]");

            }
        }
    }

    public static boolean isAirPlaneModeOn(Context context) {
        boolean mIsAirPlaneModeON = false;
        try {
            int airplaneMode = Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON);
            mIsAirPlaneModeON = false;
            if (airplaneMode == 1) {
                mIsAirPlaneModeON = true;
            }
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }
        return mIsAirPlaneModeON;
    }

    //TODO: Modify this API to return data connectivity status based on Data usage type(WIFI/MOBILE)
    public static boolean isDataConnectivityOn(Context context) {

        if (Preferences.getPreferences(context).getIgnoreDataConnection())
            return true;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (null == mNetworkInfo) {
            allNetwork(connectivityManager);
        } else if (null != mNetworkInfo && mNetworkInfo.getState() == State.CONNECTED &&
                mNetworkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
            return true;
        }

        return false;
    }

    public boolean IsDataConnection() {
        return true;
    }

    public boolean IsDataConnection(Activity fromActivity) {
        return IsDataConnection(fromActivity, false);
    }

    public boolean IsDataConnection(Activity fromActivity, boolean needPopup) {
        return IsDataConnection(fromActivity, needPopup, false);
    }

    public boolean IsDataConnectionNeedPopupAlways(Activity fromActivity, boolean needPopup) {
        if (!IsFlightMode()) {
            changeisNeedConnectionPopuped();
        }

        return IsDataConnection(fromActivity, needPopup, false);
    }

    public boolean IsDataConnection(Activity fromActivity, boolean needPopup, boolean isInSetupFlow) {
        if (Preferences.getPreferences(fromActivity) != null && Preferences
                .getPreferences(fromActivity).getIgnoreDataConnection())
            return true;

        if (sDataConnectionState == NETWORKERR_INVALID)
            isNeedConnectionPopuped = true;
        sDataConnectionState = NETWORKERR_INVALID;
        if (!IsWifiConnected()) {
            if (IsWifiOnlyModel()) {
                sDataConnectionState = NETWORKERR_NO_SIGNAL;
            } else if (IsFlightMode()) {
                sDataConnectionState = NETWORKERR_FLIGHTMODE_ON;
            } else if (IsMobileDataOff()) {
                sDataConnectionState = NETWORKERR_MOBILEDATA_OFF;
            } else if (IsRoamingOff()) {
                sDataConnectionState = NETWORKERR_DATAROAMING_OFF;
            } else if (IsReachToDataLimit()) {
                sDataConnectionState = NETWORKERR_REACHED_DATALIMIT;
            } else if (IsNoServiceOrLimitedService() && !isInSetupFlow) {
                sDataConnectionState = NETWORKERR_MMS_EMAIL;
            } else if (!receiver.getConnectionState() && !isInSetupFlow) {
                sDataConnectionState = NETWORKERR_NO_SIGNAL;
                Log.d(Logging.LOG_TAG, "IsDataConnection(): network is not connected");
            } else {
                sDataConnectionState = NETWORKERR_INVALID;
                return true;
            }

            if (needPopup && !isNeedConnectionPopuped)
                return false;
            else if (needPopup)
                isNeedConnectionPopuped = false;

            if (sDataConnectionState != NETWORKERR_INVALID) {
                connectivityManager = (ConnectivityManager) mContext
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo niBluetoothTethering = connectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);
                NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                if (niBluetoothTethering != null && niBluetoothTethering.isConnected()) {
                    sDataConnectionState = NETWORKERR_INVALID;
                    return true;
                } else if ((info != null && info.isConnected())) {
                    sDataConnectionState = NETWORKERR_INVALID;
                    return true;
                } else
                    DataConnection.ShowDataConnectionPopup(fromActivity, sDataConnectionState);
            }
            // DataConnection.actionDataConnection(fromActivity,
            // sDataConnectionState);

            return false;
        } else
            return true;
    }

    public boolean IsWifiConnected() {

        connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo == null) {
            Log.d(TAG, "(IsWifiConnected)mNetworkInfo = null");
            return false;
        }
        if (mNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            Log.d(TAG, "(IsWifiConnected)type is WIFI");
            return true;
        } else {
            Log.d(TAG, "(IsWifiConnected)type is not WIFI");
            return false;
        }
    }

    public boolean IsWifiOnlyModel() {
        boolean isNonPhone = Utility.isNonPhone(mContext.getApplicationContext());
        boolean noDataCapble = !isDataCapable();
        boolean wifi_only_model = "wifi-only".equalsIgnoreCase(CarrierValues.RO_CARRIER);
        Log.d(TAG,
                "(IsWifiOnlyModel)isNonPhone = " + isNonPhone + " noDataCapble " + noDataCapble + " wifi_only_model " + wifi_only_model);
        return (isNonPhone && noDataCapble) || wifi_only_model;
    }

    public boolean IsWifiAndDataModel() {
        boolean isNonPhone = Utility.isNonPhone(mContext.getApplicationContext());
        boolean noVoiceCapable = !isVoiceCapable();
        Log.d(TAG,
                "(IsWifiOnlyModel)isNonPhone = " + isNonPhone + " noVoiceCapable " + noVoiceCapable);
        return isNonPhone && noVoiceCapable;
    }

    public boolean isDataCapable() {
        boolean ret = true;
        ConnectivityManager cm = null;

        if (mContext == null) {
            Log.d(TAG, "(isDataCapable)null context");
            return ret;
        }

        cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) {
            Log.d(TAG, "(isDataCapable)null ConnectivityManager");
            return ret;
        }


        //ret = (cm.isNetworkSupported(ConnectivityManager.TYPE_MOBILE) == true);
        NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkInfo != null) {
            ret = networkInfo.isAvailable();
        }
        Log.d(TAG, "(isDataCapable)ret = " + ret);
        return ret;

    }

    public boolean isVoiceCapable() {

        boolean ret = true;
        TelephonyManager tm = null;
        if (mContext == null) {
            Log.d(TAG, "(isDataCapable)null context");
            return ret;
        }

        tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

        if (tm == null) {
            Log.d(TAG, "(isVoiceCapable)null TelephonyManager");
            return ret;
        }
        ret = (tm.isVoiceCapable() == true);
        Log.d(TAG, "(isVoiceCapable)ret = " + ret);
        return ret;
    }

    public boolean IsFlightMode() {
        boolean mode = Settings.Global
                .getInt(mContext.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        Log.d(TAG, "(IsFlightMode)mode = " + mode);
        return mode;
    }

    private boolean isRoaming() {

        TelephonyManager tm = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);

        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = cm.getActiveNetworkInfo();

        boolean roaming1 = false;

        boolean roaming2 = false;


        if (tm != null) {
            roaming1 = tm.isNetworkRoaming();
        } else {
            Log.d(TAG, "(isRoaming)tm is null ");
        }


        if (ni != null) {
            roaming2 = ni.isRoaming();
        } else {
            Log.d(TAG, "(isRoaming)ni is null ");
        }


        Log.d(TAG, "(isRoaming)roaming1/roaming2 " + roaming1 + "/" + roaming2);
        return (roaming1 || roaming2);

    }


    public boolean isMobileDataEnabled() {
        boolean mobileDataEnabled = false;
        try {
            final Class cmClass = connectivityManager.getClass();
            final Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"
            mobileDataEnabled = (Boolean) method.invoke(connectivityManager);
        } catch (final Exception e) {
            Log.e(TAG, "isMobileDataEnabled: system api not found", e);
        }
        return mobileDataEnabled;
    }


//    private boolean isMobileDataEnabled() {
//
//        TelephonyManager tm = (TelephonyManager) mContext
//                .getSystemService(Context.TELEPHONY_SERVICE);
//        int status = tm.getDataState();
//        return (TelephonyManager.DATA_DISCONNECTED < status); //Connecting & Connected && Suspend
//       /* ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
//        boolean enabled = cm.getMobileDataEnabled();
//        Log.d(TAG, "(isMobileDataEnabled)enabled = " + enabled);
//        return enabled;*/
//    }

    private boolean isRoamingEnabled() {

//    	TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
//
//    	try {
//
//    		val = tm.getDataRoamingEnabled();
//
//    	} catch (NoSuchMethodError e) {
//
//    		e.printStackTrace();
//
//    	}

        int val = Settings.Global
                .getInt(mContext.getContentResolver(), Settings.Global.DATA_ROAMING, 0);
        Log.d(TAG, "(isRoamingEnabled)val = " + val);
        return (val == 1) ? true : false;

    }

    public boolean IsMobileDataOff() {

        if (EmailFeature.isKoreaIspAccountsetup() != null) {  // Korea model Only

            if (isRoaming()) {  // In case of roaming state

                try {

                    NetworkInfo niWimax = connectivityManager
                            .getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
                    if (niWimax == null) {
                        Log.d(TAG, "(IsMobileDataOff)1/niWimax is null");
                    } else {
                        Log.d(TAG, "(IsMobileDataOff)1/connected = " + niWimax.isConnected());
                    }

                    return !(isRoamingEnabled() ||

                            (niWimax != null && niWimax.isConnected()));

                } catch (Exception e) {

                }

                return true;

            }

        }


        connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        try {

            if (connectivityManager != null) {

                NetworkInfo niWimax = connectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
                if (niWimax == null) {
                    Log.d(TAG, "(IsMobileDataOff)2/niWimax is null");
                } else {
                    Log.d(TAG, "(IsMobileDataOff)2/connected = " + niWimax.isConnected());
                }
                return !(isMobileDataEnabled() ||

                        (niWimax != null && niWimax.isConnected()));

            }

        } catch (Exception e) {

        }

        return true;

    }

    public boolean IsRoamingOff() {

        telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

        return isRoaming() && !isRoamingEnabled();
    }

    public boolean IsNoServiceOrLimitedService() {
        //for SDL
        TelephonyManager tm = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        int state = ServiceState.STATE_IN_SERVICE;
        try {
            state = tm.getDataServiceState();
        } catch (NoSuchMethodError e) {
            e.printStackTrace();
        }
//        try {
//            if(SecProductFeature_COMMON.SEC_PRODUCT_FEATURE_COMMON_USE_MULTISIM == true) {
//                int mDataSimId = SystemProperties.getInt(TelephonyProperties.PROPERTY_DATA_PREFER_SIM_ID, 0);
//
//                return MultiSimTelephonyManager.getDefault(mDataSimId).getDataServiceState() == ServiceState.STATE_OUT_OF_SERVICE
//                            || MultiSimTelephonyManager.getDefault(mDataSimId).getDataServiceState() == ServiceState.STATE_EMERGENCY_ONLY;
//            }
//            else {
//                telephonyManager = (TelephonyManager) mContext
//                            .getSystemService(Context.TELEPHONY_SERVICE);
        Log.d(TAG, "(IsNoServiceOrLimitedService)state = " + state);
        return state == ServiceState.STATE_OUT_OF_SERVICE
                || state == ServiceState.STATE_EMERGENCY_ONLY;
//            }
//        } catch (Exception e) {
//            return false;
//        }
    }

    public static boolean IsRoamingMenuSupport(Context context) {
        int roamingMenuDisplayForSprint = Utility.getRoamingPreference(context);
        if (!DataConnectionUtil.getInstance(context).IsWifiOnlyModel()
                && DataConnectionUtil.getInstance(context).isDataCapable()
                && roamingMenuDisplayForSprint == Utility.DEFAULT_ROAMING_PREFERENCE_SPRINT)
            return true;

        return false;

    }

    public boolean IsReachToDataLimit() {

        return false;
        //connectivityManager =(ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        //return connectivityManager.isMobilePolicyDataEnable();
    }

    public static class MobileDataBroadcastReceiver extends BroadcastReceiver {
        boolean isConnected = true;

        @Override
        public void onReceive(Context context, Intent intent) {

            final ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                final NetworkInfo mobileNetworkInfo = cm
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                if (mobileNetworkInfo != null && mobileNetworkInfo.isAvailable())
                    isConnected = true;
                else
                    isConnected = false;
            }
        }

        public boolean getConnectionState() {
            return isConnected;
        }

    	/*

        boolean isConnected = true;

        @Override

        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if(TelephonyIntents.ACTION_ANY_DATA_CONNECTION_STATE_CHANGED.equals(action)) {

                String state = intent.getStringExtra("state");
                String apnType = intent.getStringExtra("apnType");
                Log.d(Logging.LOG_TAG, "MobileDataBroadcastReceiver: state is "+state + ", apnType = " + apnType);

                if (!"default".equalsIgnoreCase(apnType))
                	return;
                if(state == null || "DISCONNECTED".equals(state)) {

                    isConnected  = false;

                } else {

                    isConnected  = true;

                }

            }

        }



        public boolean getConnectionState() {
            return isConnected;
        }

    */
    }
    
    
/*    public  void SetDataConnection(Context mContext, int requestCode,boolean enable)
    {

        switch (requestCode) {
            case NETWORKERR_FLIGHTMODE_ON:
                SetFlightMode(mContext, false);
                break;
            case NETWORKERR_MOBILEDATA_OFF:
                SetMobileDataOn(mContext, enable);
                break;
            case NETWORKERR_DATAROAMING_OFF:
                SetRoamingOn(mContext, true);
                break;
            case NETWORKERR_REACHED_DATALIMIT:
                SetReachToDataLimit(mContext, enable);
                break;
            case NETWORKERR_NO_SIGNAL:
            case NETWORKERR_MMS_EMAIL:
            case NETWORKERR_INVALID:
            default:
                break;
        } 
    }*/
/*
    public void SetFlightMode(Context mContext, boolean enabled){

        Settings.System.putInt(mContext.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, enabled ? 1 : 0);

        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        intent.putExtra("state", enabled);
        mContext.sendBroadcast(intent);
    }

    public void SetMobileDataOn(Context mContext, boolean enabled){

        connectivityManager =(ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.setMobileDataEnabled(enabled);

    }   

    public void SetRoamingOn(Context mContext, boolean enabled){

        telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.setDataRoamingEnabled(enabled);
    }

    public void SetNoServiceOrLimitedService(Context mContext, boolean enabled){
        //NA
    }

    public void SetReachToDataLimit(Context mContext, boolean enabled){

    }*/

}

