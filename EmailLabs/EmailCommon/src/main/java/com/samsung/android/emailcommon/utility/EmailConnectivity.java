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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import java.util.HashSet;


/**
 * Encapsulates functionality of ConnectivityManager for use in the Email
 * application. In particular, this class provides callbacks for connectivity
 * lost, connectivity restored, and background setting changed, as well as
 * providing a method that waits for connectivity to be available without
 * holding a wake lock To use, EmailConnectivityManager mgr = new
 * EmailConnectivityManager(context, "Name"); When done, mgr.unregister() to
 * unregister the internal receiver TODO: Use this class in ExchangeService
 */
public class EmailConnectivity extends BroadcastReceiver {
    private static final String TAG = "EmailConnectivity";
    // Loop time while waiting (stopgap in case we don't get a broadcast)
    private static final int CONNECTIVITY_WAIT_TIME = 10 * 60 * 1000;
    // Sentinel value for "no active network"
    public static final int NO_ACTIVE_NETWORK = -1;
    // The name of this manager (used for logging)
    protected String mName;
    // The monitor lock we use while waiting for connectivity
    private final Object mLock = new Object();
    // The instantiator's context
    protected Context mContext;
    // The wake lock used while running (so we don't fall asleep during
    // execution/callbacks)
    protected WakeLock mWakeLock;
    protected android.net.ConnectivityManager mConnectivityManager;
    // Set when we abort waitForConnectivity() via stopWait
    private boolean mStop = false;
    // The thread waiting for connectivity
    private Thread mWaitThread;
    // Whether or not we're registered with the system connectivity manager
    private boolean mRegistered = true;
    private static NetworkInfo mNetworkInfo = null;
    private static HashSet<ConnectivityNotifier> mConnectivityListeners = 
        new HashSet<ConnectivityNotifier>();
    private static final String POWERSAVING_DATA_SERVICE_CHANGED = "android.settings.POWERSAVING_DATA_SERVICE_CHANGED";
    
    public interface ConnectivityNotifier {
        public void onAirPlaneModeOn();
        public void onAirPlaneModeOff();
        public void onDataConnectivityEnabled();
        public void onDataConnectivityDisabled();
    }

    public EmailConnectivity(Context context, String name) {
        mContext = context;
        mName = name;
        mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, name);
        
        IntentFilter filter = new IntentFilter(); 
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION); 
        filter.addAction(POWERSAVING_DATA_SERVICE_CHANGED); 
        mContext.registerReceiver(this, filter);   
        
    }
    
    public EmailConnectivity(){
        mContext = null;
        mConnectivityManager = null;
        mWakeLock = null;
        mName = null;
    }

    public void stopWait() {
        mStop = true;
        Thread thread = mWaitThread;
        if (thread != null) {
            thread.interrupt();
        }
    }

    /**
     * Any UI code that wishes for callback results (on async ops) should register their callback
     * here (typically from onResume()).  Unregistered callbacks will never be called, to prevent
     * problems when the command completes and the activity has already paused or finished.
     * @param listener The callback that may be used in action methods
     */
    public void addConnectivityCallback(ConnectivityNotifier listener) {
        synchronized (mConnectivityListeners) {
            mConnectivityListeners.add(listener);
    }
    }

    /**
     * Any UI code that no longer wishes for callback results (on async ops) should unregister
     * their callback here (typically from onPause()).  Unregistered callbacks will never be called,
     * to prevent problems when the command completes and the activity has already paused or
     * finished.
     * @param listener The callback that may no longer be used
     */
    public void removeConnectivityCallback(ConnectivityNotifier listener) {
        synchronized (mConnectivityListeners) {
            mConnectivityListeners.remove(listener);
        }
    }
    
    public void unregister() {
        try {
            mContext.unregisterReceiver(this);
        } catch (RuntimeException e) {
            // Don't crash if we didn't register
        } finally {
            mRegistered = false;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "In ConnectionChangeReceiver.onReceive");

        boolean mIsAirPlaneModeON = false; 
        boolean  misDataConnectivityOn = false;
        DataConnectionUtil.changeisNeedConnectionPopuped();
        mIsAirPlaneModeON = DataConnectionUtil.isAirPlaneModeOn(context);
        misDataConnectivityOn = DataConnectionUtil.isDataConnectivityOn(context);

        if (!mIsAirPlaneModeON) {
            synchronized (mConnectivityListeners) {
                for (ConnectivityNotifier listener : mConnectivityListeners) {
                    listener.onAirPlaneModeOff();
                }
            }
        } else {
            synchronized (mConnectivityListeners) {
                for (ConnectivityNotifier listener : mConnectivityListeners) {
                    Log.i(TAG, "onReceive | AirPlaneMode is on. So, Invoking listeners " + listener);
                    listener.onAirPlaneModeOn();
                }
            }
        }

        if (misDataConnectivityOn == false) {
            synchronized (mConnectivityListeners) {
                for (ConnectivityNotifier listener : mConnectivityListeners) {
                    listener.onDataConnectivityDisabled();
                }
            }
        } else {
            synchronized (mConnectivityListeners) {
                for (ConnectivityNotifier listener : mConnectivityListeners) {
                    listener.onDataConnectivityEnabled();
                }
            }

            // Wake up the thread waiting for connectivity.
            synchronized (mLock) {
                // There is maximum one thread waiting on the lock.
                mLock.notify();
            }
        }
        Log.i(TAG,getDetailedInformation(context));
    }

    
    private static String getDetailedInformation(Context context){
        String information = " ";
        
        ConnectivityManager connectivityManager =(ConnectivityManager) context.getSystemService
        (Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = connectivityManager.getActiveNetworkInfo();
        
        if(null == mNetworkInfo){
            Log.e(TAG, "Error Active Network Information not yet availiable");
            return " Active Network Information not yet availiable";
        }
        
        if(DataConnectionUtil.isAirPlaneModeOn(context)){
            Log.e(TAG, "Error Error Airplane mode ON");
            return "Airplane mode ON";
        }
        
        //State
        information = "state : [" + mNetworkInfo.getState() + "] ";
        //Type Wifi or Mobile
        information = information + " Type : [" + mNetworkInfo.getType() + " ] "; 
        //Type Wifi or Mobile
        information = information + " Type Name : [" + mNetworkInfo.getTypeName() + " ] "; 
        //Subtype Wifi or Mobile
        information = information + " SybType : [" + mNetworkInfo.getSubtype() + " ] "; 
        //Subtype Name
        information = information + " SybType Name: [" + mNetworkInfo.getSubtypeName() + " ] "; 
        //is Connected
        information = information + " Connected : [" + mNetworkInfo.isConnected() + " ] "; 
        //is Available
        information = information + " Available : [" + mNetworkInfo.isAvailable() + " ] "; 
        return information;
    }


    /**
     * Get the type of the currently active data network
     * @return the type of the active network (or NO_ACTIVE_NETWORK)
     */


    public void waitForConnectivity() {
        // If we're unregistered, throw an exception
        if (!mRegistered) {
            throw new IllegalStateException("ConnectivityManager not registered");
        }
        boolean waiting = false;
        mWaitThread = Thread.currentThread();
        // Acquire the wait lock while we work
        mWakeLock.acquire();
        try {
            while (!mStop) {
                NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
                if (info != null) {
                    // We're done if there's an active network
                    if (waiting) {
                        if (EmailLog.DEBUG) {
                            Log.d(TAG, mName + ": Connectivity wait ended");
                        }
                    }
                    return;
                } else {
                    if (!waiting) {
                        if (EmailLog.DEBUG) {
                            Log.d(TAG, mName + ": Connectivity waiting...");
                        }
                        waiting = true;
                    }
                    // Wait until a network is connected (or 10 mins), but let
                    // the device sleep
                    synchronized (mLock) {
                        // Don't hold a lock during our wait
                        mWakeLock.release();
                        try {
                            mLock.wait(CONNECTIVITY_WAIT_TIME);
                        } catch (InterruptedException e) {
                            // This is fine; we just go around the loop again
                        }
                        // Get the lock back and check again for connectivity
                        mWakeLock.acquire();
                    }
                }
            }
        } finally {
            // Make sure we always release the wait lock
            if (mWakeLock.isHeld()) {
                mWakeLock.release();
            }
            mWaitThread = null;
        }
    }
    
}
