/*
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

package com.samsung.android.emailcommon;


import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.samsung.android.emailcommon.system.CarrierValues;
import com.samsung.android.emailcommon.utility.*;

public class Device {
    private static String sDeviceId = null;

//    private static String sPairedDeviceType = null;

    private static String TAG = "Device";

    private static enum REQUEST {
        DEVICE_ID, DEVICE_TYPE
    }

    /**
     * EAS requires a unique device id, so that sync is possible from a variety
     * of different devices (e.g. the syncKey is specific to a device) If we're
     * on an emulator or some other device that doesn't provide one, we can
     * create it as android<n> where <n> is system time. This would work on a
     * real device as well, but it would be better to use the "real" id if it's
     * available
     */

    public static  synchronized String getDeviceId(Context context) throws IOException {
        if (sDeviceId == null) {
        	
            sDeviceId = getDeviceInternal(context, REQUEST.DEVICE_ID);
        }
        return sDeviceId;
    }

    public static  synchronized String getPairedDeviceType(Context context) {
        String deviceType = null;
        try {
            deviceType =  getDeviceInternal(context, REQUEST.DEVICE_TYPE);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return deviceType;
    }

    public static  synchronized void setPairedDeviceType(Context context, String deviceType) {
        if (deviceType != null) {
            File f = null;
            f = context.getFileStreamPath("deviceType");
            BufferedWriter w = null;
            try {
                w = new BufferedWriter(new FileWriter(f), 128);
                w.write(deviceType);
                w.flush();
                EmailLog.d(TAG, "Successfully written deviceType: " + deviceType);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                try {
                    if (w != null) {
                        w.close();
                    }
                } catch ( IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }

    private static String getDeviceInternal(Context context, REQUEST request) throws IOException {
        if (context == null) {
            throw new IllegalStateException("getDeviceId requires a Context");
        }
        // 1. find deviceId in file
        File f = null;
        if (request == REQUEST.DEVICE_ID) {
            f = context.getFileStreamPath("deviceName");
        } else if (request == REQUEST.DEVICE_TYPE) {
            f = context.getFileStreamPath("deviceType");
        }
        String id = getIdFromFile(f);
        if (request == REQUEST.DEVICE_ID && (TextUtils.isEmpty(id))) {
            // 2. we cannot find it, so create and save it in file.
            BufferedWriter w = new BufferedWriter(new FileWriter(f), 128);
            try {
                id = getConsistentDeviceId(context);
                if (id != null) {
                    w.write(id);
                    w.flush();
                }
            } finally {
                w.close();
            }
        }

        return id;
    }

    /**
     * @return Device's unique ID if available. null if the device has no unique
     *         ID.
     */
    private static String getConsistentDeviceId(Context context) {
    	
    	final String consistentDeviceId;
    	String deviceId;
    	if (Utility.isNonPhone(context)) {
    		EmailLog.d(TAG, "get deviceId for wifi only model");
    		deviceId = getHardwareId(context);
            if (deviceId == null) {
                Log.e(TAG, "hardwareId is null in getConsistentDeviceId");
                return null;
            }
            consistentDeviceId = Utility.getSmallHashForDeviceId(deviceId);
            int deviceType = 0; // WIFI : 0
        	if(Utility.isInContainer(context)) {
        		EmailLog.d(TAG,"containerized, set deviceIdNum for KNOX");
        		deviceType = 5;
        	}
            Log.d(TAG, "return unique deviceID : " + "SEC" + deviceType + consistentDeviceId);
            return "SEC" + deviceType + consistentDeviceId;
    	} else {
    		EmailLog.d(TAG, "get deviceId for phone model");

            int deviceType = 0; // WIFI : 0, IMEI : 1, ESN : 2, MEID : 3
            try {
                TelephonyManager tm = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                if (tm == null) {
                    EmailLog.e(TAG, "TelephonyManager is null in getConsistentDeviceId");
                    return null; // error case 1
                }
                deviceId = tm.getDeviceId();
                if (deviceId == null) {
                    EmailLog.e(TAG, "tm.getDeviceId() is null in getConsistentDeviceId");
                    return null; // error case 2
                }
                deviceType = getDeviceType(context, tm.getPhoneType(), deviceId.length());
            } catch (Exception e) {
                EmailLog.e(TAG, "Exception in getConsistentDeviceId" + e.getMessage());
                return null; // error case 3
            }
            consistentDeviceId = Utility.getSmallHashForDeviceId(deviceId);
            EmailLog.d(TAG, "return unique deviceID : " + "SEC" + deviceType + consistentDeviceId);
            
            return "SEC" + deviceType + consistentDeviceId;
    	}
    }
    
    private static String getHardwareId(Context context) {
    	
    	String hardwareId = null;
        if (Utility.isNonPhone(context)) {
            // Wifi Model
            hardwareId = Settings.Secure.getString(context.getContentResolver(), "hardware_id");
            if (hardwareId == null) {
                hardwareId = CarrierValues.RO_SERIALNO;
                if (hardwareId.equals("unknown"))
                    hardwareId = null;
            }
        }
        return hardwareId;
    }
    
    private static int getDeviceType(Context context, int phoneType, int lengthOfDeviceId) {
    	
    	int deviceType = 0;
    	if(Utility.isInContainer(context)) {
    		EmailLog.d(TAG,"containerized, set deviceIdNum for KNOX");
    		deviceType = 5;
    	} else {
    		EmailLog.d(TAG,"non-containerized, set deviceIdNum for normal");
            if (phoneType == TelephonyManager.PHONE_TYPE_GSM) {
            	deviceType = 1; // IMEI
            } else if (phoneType == TelephonyManager.PHONE_TYPE_CDMA) {
                if (lengthOfDeviceId == 14)
                	deviceType = 3; // MEID
                else
                	deviceType = 2; // ESN
            } else {
                // Unknown case
                if (lengthOfDeviceId == 15)
                	deviceType = 1; // IMEI
                else if (lengthOfDeviceId == 14)
                	deviceType = 3; // MEID
                else
                	deviceType = 2; // ESN
            }
    	}
        return deviceType;
    }
    
    private static String getIdFromFile(File f) throws IOException {
    	
        BufferedReader rdr = null;
        String id = null;
        if (f != null && f.exists()) {
            if (f.canRead()) {
            	try{
                    rdr = new BufferedReader(new FileReader(f), 128);
                    id = rdr.readLine();
            	} finally {
            	    if(rdr != null) {
                        rdr.close();
                    }
            	}
                if (id == null) {
                    // It's very bad if we read a null device id; let's delete
                    // that file
                    if (!f.delete()) {
                        EmailLog.e(TAG, "Can't delete null deviceName file; try overwrite.");
                    }
                }
            } else {
                EmailLog.w(TAG, f.getAbsolutePath() + ": File exists, but can't read?"
                        + "  Trying to remove.");
                if (!f.delete()) {
                    EmailLog.w(TAG, "Remove failed. Tring to overwrite.");
                }
            }
        }
    	return id;
    }
}
