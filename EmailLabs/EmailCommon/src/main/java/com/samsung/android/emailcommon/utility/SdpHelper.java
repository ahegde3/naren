package com.samsung.android.emailcommon.utility;

import android.content.Intent;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.sec.enterprise.knox.sdp.SdpDatabase;
import com.sec.enterprise.knox.sdp.SdpFileSystem;
import com.sec.enterprise.knox.sdp.SdpUtil;
import com.sec.enterprise.knox.sdp.engine.SdpEngineConstants;
import com.sec.enterprise.knox.sdp.engine.SdpEngineInfo;
import com.sec.enterprise.knox.sdp.exception.SdpCompromisedException;
import com.sec.enterprise.knox.sdp.exception.SdpEngineNotExistsException;
import com.sec.enterprise.knox.sdp.exception.SdpInternalException;
import com.sec.enterprise.knox.sdp.exception.SdpLicenseRequiredException;

public class SdpHelper {

    private static SdpFileSystem sSdpFileSystem = null;
    private static SdpDatabase sSdpDatabase   = null;
    private static final Object LOCK = new Object();
    private static final String TAG = "SdpHelper.email";
    private static boolean sIsSdpEnabled = false;

    // Make these constants public for MessageListFragment SdpRceiver
    public static final String SDP_ENGINE_ID    = "id";
    public static final String SDP_ENGINE_STATE = "state";
    public static final int SDP_ENGINE_STATE_UNCLOCKED = SdpEngineConstants.State.UNLOCKED;
    
    private SdpHelper() {}

    public static void setSdpEnabled(Context context) {
        synchronized(LOCK) {
            try {
                SdpUtil sdpUtil = SdpUtil.getInstance();
                if( sdpUtil != null && sdpUtil.isSdpSupported() && !isSdpEnabled() ) {
                    sSdpDatabase = new SdpDatabase(null);
                    sSdpFileSystem = new SdpFileSystem(context, null);
                    sIsSdpEnabled = true;
                }
            } catch (SQLiteException e) {
                Log.d(TAG, "setSdpEnabled :: SQLiteException occured...");
                Log.dumpException(TAG, e);
            } catch (SdpEngineNotExistsException e) {
                Log.d(TAG, "setSdpEnabled :: SdpEngineNotExistsException occured...");
                Log.dumpException(TAG, e);
            } catch (Throwable e) {
                Log.d(TAG, "setSdpEnabled :: Unknown exception" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static boolean isSdpEnabled() {
        synchronized(LOCK) {
            return sIsSdpEnabled;
        }
    }

    public static synchronized SdpDatabase DB() throws SdpCompromisedException {
        if(!isSdpEnabled())
            throw new SdpCompromisedException();
        return sSdpDatabase;
    }

    public static synchronized SdpFileSystem FILE() throws SdpCompromisedException {
        if(!isSdpEnabled())
            throw new SdpCompromisedException();
        return sSdpFileSystem;
    }

    public static void onSdpStateChanged(Intent intent, SQLiteDatabase db, String dbAlias) throws SdpCompromisedException {
        boolean result = false;
        if (isSdpEnabled()) {
            SdpEngineInfo sdpEngineInfo = null;
            SdpUtil sdpUtil = SdpUtil.getInstance();

            if( intent != null ) {
                int engineId    = intent.getIntExtra(SDP_ENGINE_ID, -1);
                int engineState = intent.getIntExtra(SDP_ENGINE_STATE, -1);
                try {
                    if( sdpUtil != null &&
                            (sdpEngineInfo = sdpUtil.getEngineInfo(null)) != null &&
                            sdpEngineInfo.getId() == engineId) {
                        result = DB().updateStateToDB(db, dbAlias, engineState);
                        Log.d(TAG, "onSdpStateChanged ::" +
                                " Target Engine Id : " + engineId +
                                ", Engine State : " + engineState +
                                ", DB Alias : " + dbAlias);
                    }
                } catch (SdpLicenseRequiredException e) {
                    Log.dumpException(TAG, e);
                } catch (SdpEngineNotExistsException e) {
                    Log.dumpException(TAG, e);
                } catch (SdpInternalException e) {
                    Log.dumpException(TAG, e);
                } catch (Exception e) {
                    Log.dumpException(TAG, e);
                } catch (Throwable e) {
                    Log.d(TAG, "onSdpStateChanged :: Unknown exception" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        Log.d(TAG, "onSdpStateChanged :: Result : " + result);
    }

    public static void setSdpState(SQLiteDatabase db, String dbAlias) throws SdpCompromisedException {
        boolean result = false;

        if(isSdpEnabled()) {
            try {
                SdpEngineInfo sdpEngineInfo = null;
                SdpUtil sdpUtil = SdpUtil.getInstance();
                if( sdpUtil != null &&
                        (sdpEngineInfo = sdpUtil.getEngineInfo(null)) != null ) {
                    int engineId    = sdpEngineInfo.getId();
                    int engineState = sdpEngineInfo.getState();
                    result = DB().updateStateToDB(db, dbAlias, engineState);
                    Log.d(TAG, "setSdpState ::" +
                                " Target Engine Id : " + engineId +
                                ", Engine State : " + engineState +
                                ", DB Alias : " + dbAlias);
                }
            } catch (SdpLicenseRequiredException e) {
                Log.dumpException(TAG, e);
            } catch (SdpEngineNotExistsException e) {
                Log.dumpException(TAG, e);
            } catch (SdpInternalException e) {
                Log.dumpException(TAG, e);
            } catch (Exception e) {
                Log.dumpException(TAG, e);
            } catch (Throwable e) {
                Log.d(TAG, "setSdpState :: Unknown exception" + e.getMessage());
                e.printStackTrace();
            }
        }
        Log.d(TAG, "setSdpState :: Result : " + result);
    }
    public static boolean isSdpActive() {
        boolean ret = false;
        if (isSdpEnabled()) {
            SdpEngineInfo sdpEngineInfo = null;
            SdpUtil sdpUtil = SdpUtil.getInstance();

            try {
                if( sdpUtil != null &&
                        (sdpEngineInfo = sdpUtil.getEngineInfo(null)) != null )
                {
                    ret = (sdpEngineInfo.getState() == SdpEngineConstants.State.LOCKED);
                } else
                    Log.e(TAG, "isSdpActive :: SdpEngineInfo not exists");
            } catch (SdpLicenseRequiredException e) {
                Log.dumpException(TAG, e);
            } catch (SdpEngineNotExistsException e) {
                Log.dumpException(TAG, e);
            } catch (SdpInternalException e) {
                Log.dumpException(TAG, e);
            } catch (Exception e) {
                Log.dumpException(TAG, e);
            } catch (Throwable e) {
                Log.d(TAG, "isSdpActive :: Unknown exception" + e.getMessage());
                e.printStackTrace();
            }
        }
        return ret;
    }

    public static int getSdpEngineId() {
        int id = 0;
        if (isSdpEnabled()) {
            try {
                SdpUtil sdpUtil = SdpUtil.getInstance();
                if (sdpUtil != null) {
                    SdpEngineInfo sdpEngineInfo = sdpUtil.getEngineInfo(null);
                    id = sdpEngineInfo.getId();
                    Log.d(TAG, "getSdpEngineId() : sdpEngineInfo.getId() returned " + id);
                }
            } catch (SdpLicenseRequiredException e) {
                Log.dumpException(TAG, e);
            } catch (SdpEngineNotExistsException e) {
                Log.dumpException(TAG, e);
            } catch (SdpInternalException e) {
                Log.dumpException(TAG, e);
            } catch (Throwable e) {
                Log.d(TAG, "getSdpEngineId() : " + e.getMessage());
                e.printStackTrace();
            }
        }

        return id;
    }
}
