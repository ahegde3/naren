package com.samsung.android.emailcommon;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import com.samsung.android.emailcommon.service.BoundServiceMessenger;
import com.samsung.android.emailcommon.service.PGPKeyConst;
import com.samsung.android.emailcommon.service.PGPKeyInfo;
import com.samsung.android.emailcommon.service.ProxyArgs;
import com.samsung.android.emailcommon.service.ServiceConnectionManager;
import com.samsung.android.emailcommon.utility.Log;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;

public class PGPServiceUtil implements ProxyArgs {

    private static final String TAG = "PGPServiceUtil"; 
    private static PGPServiceUtil sInstance;
    Context mContext;
    private PGPBoundServiceMessenger mMessenger = null;
    static Callback mCallback;
    static private Object [] SYNC_TAG = new Object[0];
    static private int CALL_ID_CREATE_KEY = 11;
    static private int CALL_ID_IMPORT_KEY = 12;
    static private int CALL_ID_EXPORT_KEY = 13;
    static private int CALL_ID_REMOVE_KEY = 14;
    static private int CALL_ID_FINGER = 15;
    static private int CALL_ID_USER_INFO = 16;
    static private int CALL_ID_KEY_INFO = 17;
    public interface Callback {
        public void cmdKeyCallback(int status, String msg);
        public void fingerCallback(String finger, int valid);
        public void userInfoCallback(int status, String userId, String userName, long keyId);
        public void keyInfoCallback(PGPKeyInfo [] info);
    }

    static public class CallbackImpl implements Callback {

        @Override
        public void cmdKeyCallback(int status, String msg) {

        }

        @Override
        public void fingerCallback(String finger, int valid) {

        }

        @Override
        public void userInfoCallback(int status, String userId,
                String userName, long keyId) {

        }

        @Override
        public void keyInfoCallback(PGPKeyInfo [] info) {

        }

    }
    abstract static public class UserInfoCallback implements Callback {
        @Override
        public void cmdKeyCallback(int status, String msg) {
            //do nothing
        }
        @Override
        public void fingerCallback(String finger, int valid) {
            //do nothing
        }
        @Override
        public void keyInfoCallback(PGPKeyInfo [] info) {
            //do nothing
        }
    }

    abstract static public class FingerCallback implements Callback {
        @Override
        public void cmdKeyCallback(int status, String msg) {
            //do nothing
        }

        @Override
        public void userInfoCallback(int status, String userId, String userName, long keyId) {
            //do nothing
        }
        @Override
        public void keyInfoCallback(PGPKeyInfo [] info) {
            //do nothing
        }
    }

    abstract static public class KeyManageCallback implements Callback {
        @Override
        public void fingerCallback(String finger, int valid) {
            //do nothing
        }

        @Override
        public void userInfoCallback(int status, String userId, String userName, long keyId) {
            //do nothing
        }
        @Override
        public void keyInfoCallback(PGPKeyInfo [] info) {
            //do nothing
        }
    }

    abstract static public class KeyInfoCallback implements Callback {
        @Override
        public void cmdKeyCallback(int status, String msg) {

        }

        @Override
        public void fingerCallback(String finger, int valid) {
            //do nothing
        }

        @Override
        public void userInfoCallback(int status, String userId, String userName, long keyId) {
            //do nothing
        }

    }

    public PGPServiceUtil (Context context) {
        mContext = context;
        mMessenger = new PGPBoundServiceMessenger(context);
    }

    public static PGPServiceUtil getInstance(Context context, Callback callback) {
        mCallback = callback;
        if (sInstance == null) {
            sInstance = new PGPServiceUtil(context);
        }
        return sInstance;
    }

    public boolean createKey(String keyId, String desc, String userName, String password, int algo,
            int keyLength, GregorianCalendar expiry, boolean brev) {//int keyLength, long expiry, boolean brev) {
        Bundle b = new Bundle();
        b.putString(ARG_PGP_KEY_ID, keyId);
        b.putString(ARG_PGP_KEY_DESC, desc);
        b.putString(ARG_USERNAME, userName);
        b.putString(ARG_PASSWORD, password);
        b.putInt(ARG_PGP_KEY_ALGO, algo);
        b.putInt(ARG_PGP_KEY_LENGTH, keyLength);
        b.putSerializable(ARG_PGP_KEY_EXPIRY, expiry);//b.putLong(ARG_PGP_KEY_EXPIRY, expiry);
        b.putBoolean(ARG_PGP_KEY_BREV, brev);

        ServiceCallback callback = getServiceCallback(mContext);
        callback.addCallback(CALL_ID_CREATE_KEY, mCallback);

        mMessenger.startMessenger(b, BoundServiceMessenger.MSG_PGPCREATEKEY);

        return true;
    }

    public boolean importKey(String filePath) {
        Bundle b = new Bundle();
        b.putString(ARG_URI, filePath);

        ServiceCallback callback = getServiceCallback(mContext);
        callback.addCallback(CALL_ID_IMPORT_KEY, mCallback);
        mMessenger.startMessenger(b, BoundServiceMessenger.MSG_PGPIMPORTKEY);

        return true;
    }

    public boolean exportKey(long [] keyId, String path, String name, boolean isPublic){
        Bundle b = new Bundle();
        b.putLongArray(ARG_PGP_KEY_ID_ARRAY, keyId);
        b.putString(ARG_URL, path);
        b.putString(ARG_URI, name);
        b.putBoolean(ARG_PGP_KEY, isPublic);

        ServiceCallback callback = getServiceCallback(mContext);
        callback.addCallback(CALL_ID_EXPORT_KEY, mCallback);
        mMessenger.startMessenger(b, BoundServiceMessenger.MSG_PGPEXPORTKEY);

        return true;
    }


    public boolean exportKeyWithRingId(long [] ringId, String path, String name, boolean isPublic) {
        Bundle b = new Bundle();
        b.putLongArray(ARG_PGP_KEY_ID_ARRAY, null);
        b.putLongArray(ARG_PGP_RING_KEY_ID_ARRAY, ringId);

        b.putString(ARG_URL, path);
        b.putString(ARG_URI, name);
        b.putBoolean(ARG_PGP_KEY, isPublic);

        ServiceCallback callback = getServiceCallback(mContext);
        callback.addCallback(CALL_ID_EXPORT_KEY, mCallback);
        mMessenger.startMessenger(b, BoundServiceMessenger.MSG_PGPEXPORTKEY);
        return true;
    }

    public boolean deleteKey(long [] keyId){
        Bundle b = new Bundle();
        b.putLongArray(ARG_PGP_KEY_ID_ARRAY, keyId);

        ServiceCallback callback = getServiceCallback(mContext);
        callback.addCallback(CALL_ID_REMOVE_KEY, mCallback);
        mMessenger.startMessenger(b, BoundServiceMessenger.MSG_PGPDELETEKEY);

        return true;
    }

    public void checkFingerprint(long keyId, boolean isPublicKey) {
        Bundle b = new Bundle();
        b.putLong(ARG_PGP_KEY_ID, keyId);
        b.putBoolean(ARG_PGP_KEY, isPublicKey);

        ServiceCallback callback = getServiceCallback(mContext);
        callback.addCallback(CALL_ID_FINGER, mCallback);
        mMessenger.startMessenger(b, BoundServiceMessenger.MSG_PGPGETFINGERPRINT);

    }

    public void getUserInfo(long keyId, boolean isPublicKey) {
        Bundle b = new Bundle();
        b.putLong(ARG_PGP_KEY_ID, keyId);
        b.putBoolean(ARG_PGP_KEY, isPublicKey);
        ServiceCallback callback = getServiceCallback(mContext);
        callback.addCallback(CALL_ID_USER_INFO, mCallback);

        mMessenger.startMessenger(b, BoundServiceMessenger.MSG_PGPGETUSERINFO);
    }

    public void getUserInfoWithRingId(long ringId, boolean isPublicKey) {
        Bundle b = new Bundle();
        b.putLong(ARG_PGP_KEY_ID, PGPKeyConst.PGP_KEY_ID_INVALID);
        b.putLong(ARG_PGP_RING_KEY_ID, ringId);
        b.putBoolean(ARG_PGP_KEY, isPublicKey);
        ServiceCallback callback = getServiceCallback(mContext);
        callback.addCallback(CALL_ID_USER_INFO, mCallback);

        mMessenger.startMessenger(b, BoundServiceMessenger.MSG_PGPGETUSERINFO);
    }

    public void getPGPKeyInfo(String userId, int type) {
        Bundle b = new Bundle();
        b.putString(ARG_USERID, userId);
        b.putInt(ARG_PGP_KEY_INFO_TYPE, type);

        ServiceCallback callback = getServiceCallback(mContext);
        callback.addCallback(CALL_ID_KEY_INFO, mCallback);

        mMessenger.startMessenger(b, BoundServiceMessenger.MSG_PGPKEYINFO);
    }

    public static ServiceCallback getServiceCallback (Context context) {
        synchronized (SYNC_TAG) {
            if (sServiceCallback == null) {
                sServiceCallback = new ServiceCallback(context);
            }
            return sServiceCallback;
        }
    }
    static void clearCallback () {
        synchronized (SYNC_TAG) {
            if (sServiceCallback != null) {
                sCallbacks.clear();
                sServiceCallback.mContext = null;
                sServiceCallback = null;
            }
        }
    }

    public void destroy() {
        Log.d(TAG, "PGPServiceUtil.destroy()");
        if (mMessenger != null) {
            ServiceConnectionManager scm =  mMessenger.getServiceConnectionManager();
            if (scm != null) {
                scm.finishConnections();
            }
        }
    }
    
    static public void release() {
        clearCallback();
        if (sInstance != null) {
            sInstance.destroy();
            sInstance = null;
        }
    }

    static private ServiceCallback sServiceCallback;
    static HashMap<Integer, List<Callback>> sCallbacks = new HashMap<Integer, List<Callback>> ();
    static private class ServiceCallback{

        Context mContext;

        public ServiceCallback(Context context) {
            mContext = context;
        }

        public void addCallback(int id, Callback callback) {
            synchronized(sCallbacks) {
                if (sCallbacks.containsKey(id)) {
                    sCallbacks.get(id).add(callback);
                } else {
                    ArrayList<Callback> list = new ArrayList<Callback> ();
                    list.add(callback);
                    sCallbacks.put(id, list);
                }
            }
        }

        public List<Callback> getCallback(int id, boolean autoRemove) {
            synchronized (sCallbacks) {
                if (sCallbacks.containsKey(id)) {
                    List<Callback> list = sCallbacks.get(id);
                    if (autoRemove)
                        sCallbacks.remove(id);
                    return list;
                }
                return new ArrayList<Callback> ();
            }
        }

        public void removeCallback(int id, Callback callback) {
            synchronized (sCallbacks) {
                if (sCallbacks.containsKey(id)) {
                    List<Callback> list = sCallbacks.get(id);
                    list.remove(callback);

                }
            }
        }

        public void createKeyCallback(Bundle args) {
            int statusCode = args.getInt(ARG_STATUS_CODE);
            List<Callback> callbacks = null;
            if (statusCode == PGPKeyConst.IN_PROGRESS_STORE_KEY) {
                callbacks = getCallback(CALL_ID_CREATE_KEY, false);
            } else {
                callbacks = getCallback(CALL_ID_CREATE_KEY, true);
            }
            for (Callback callback : callbacks) {
                String msg = args.getString(ARG_TEXT);

                callback.cmdKeyCallback(statusCode, msg);
            }
        }

        public void importKeyCallback(Bundle args)  {
            int statusCode = args.getInt(ARG_STATUS_CODE);
            List<Callback> callbacks = null;
            if (statusCode == PGPKeyConst.IN_PROGRESS_IMPORT_KEY) {
                callbacks = getCallback(CALL_ID_IMPORT_KEY, false);
            } else {
                callbacks = getCallback(CALL_ID_IMPORT_KEY, true);
            }
            for (Callback callback : callbacks) {
                String msg = args.getString(ARG_TEXT);

                callback.cmdKeyCallback(statusCode, msg);
            }

        }

        public void exportKeyCallback(Bundle args)   {
            int statusCode = args.getInt(ARG_STATUS_CODE);
            List<Callback> callbacks = null;
            callbacks = getCallback(CALL_ID_EXPORT_KEY, true);
            for (Callback callback : callbacks) {
                String msg = args.getString(ARG_TEXT);

                callback.cmdKeyCallback(statusCode, msg);
            }

        }

        public void deleteKeyCallback(Bundle args)   {
            int statusCode = args.getInt(ARG_STATUS_CODE);
            List<Callback> callbacks = null;
            if (statusCode == PGPKeyConst.IN_PROGRESS_DELETE_KEY) {
                callbacks = getCallback(CALL_ID_REMOVE_KEY, false);
            } else {
                callbacks = getCallback(CALL_ID_REMOVE_KEY, true);
            }
            for (Callback callback : callbacks) {
                String msg = args.getString(ARG_TEXT);

                callback.cmdKeyCallback(statusCode, msg);
            }

        }
        public void getFingerprintCallback(Bundle args)   {

            List<Callback> callbacks = null;
            callbacks = getCallback(CALL_ID_FINGER, true);
            for (Callback callback : callbacks) {
                String finger = args.getString(ARG_PGP_KEY_PRINT);
                int valid = args.getInt(ARG_PGP_KEY_VALID);

                callback.fingerCallback(finger, valid);
            }
        }

        public void getUserInfoCallback(Bundle args)   {
            List<Callback> callbacks = null;
            callbacks = getCallback(CALL_ID_USER_INFO, true);
            for (Callback callback : callbacks) {
                int status = args.getInt(ARG_STATUS_CODE);
                String userId = args.getString(ARG_USERID);
                String userName = args.getString(ARG_USERNAME);
                long keyId = args.getLong(ARG_PGP_KEY_ID);

                callback.userInfoCallback(status, userId, userName, keyId);
            }

        }

        public void getKeyInfoCallback(Bundle args)   {
            List<Callback> callbacks = null;
            callbacks = getCallback(CALL_ID_KEY_INFO, true);
            args.setClassLoader(PGPKeyInfo.class.getClassLoader());

            Parcelable[] datas = (Parcelable[] ) args.getParcelableArray(ARG_PGP_KEY);
            if (datas == null) return;

            PGPKeyInfo[] info = new PGPKeyInfo[datas.length];
            for (Callback callback : callbacks) {
                for (int i = 0; i < datas.length; i++) {
                    info[i] = (PGPKeyInfo) datas[i];
                }
                callback.keyInfoCallback(info);
            }
        }
    }

    public class PGPBoundServiceMessenger extends BoundServiceMessenger{


        public PGPBoundServiceMessenger(Context _context) {
            super(_context);
            // TODO Auto-generated constructor stub
        }

        @Override
        public boolean onIncomingMessage(Message msg) {
            Bundle b = msg.getData();
            switch ( msg.what ){
                case MSG_PGPCREATEKEYCALLBACK:
                    getServiceCallback(mContext).createKeyCallback(b);
                    break;
                case MSG_PGPKEYINFOCALLBACK:
                    getServiceCallback(mContext).getKeyInfoCallback(b);
                    break;
                case MSG_PGPIMPORTKEYCALLBACK:
                    getServiceCallback(mContext).importKeyCallback(b);
                    break;
                case MSG_PGPEXPORTKEYCALLBACK:
                    getServiceCallback(mContext).exportKeyCallback(b);
                    break;
                case MSG_PGPDELETEKEYCALLBACK:
                    getServiceCallback(mContext).deleteKeyCallback(b);
                    break;
                case MSG_PGPGETFINGERPRINTCALLBACK:
                    getServiceCallback(mContext).getFingerprintCallback(b);
                    break;
                case MSG_PGPGETUSERINFOCALLBACK:
                    getServiceCallback(mContext).getUserInfoCallback(b);
                    break;
                case MSG_PGPGETKEYINFOCALLBACK:
                    getServiceCallback(mContext).getKeyInfoCallback(b);
                    break;

                default:
                    return false;
            }
            return true;
        }
        
        public ServiceConnectionManager getServiceConnectionManager() {
            return mServicConnectionManager;
        }
    }
}
