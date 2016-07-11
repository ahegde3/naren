package com.samsung.android.emailcommon;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;

import com.samsung.android.emailcommon.mail.MessagingException;
import com.samsung.android.emailcommon.service.BoundServiceMessenger;
import com.samsung.android.emailcommon.service.OoODataList;
import com.samsung.android.emailcommon.service.PGPKeyInfo;
import com.samsung.android.emailcommon.service.ProxyArgs;
import com.samsung.android.emailcommon.service.ServiceConnectionManager;
import com.samsung.android.emailcommon.utility.OoOConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessengerServiceUtil implements ProxyArgs {


    private static MessengerServiceUtil sInstance;
    Context mContext;
    private EASBoundServiceMessenger mMessenger = null;
    static Callback mCallback;
    static private Object [] SYNC_TAG = new Object[0];
    static private int CALL_ID_SET_OOO = 11;
    static private int CALL_ID_GET_OOO = 12;
    static private int CALL_ID_EMPTY_TRASH = 13;
    static private int CALL_ID_CHANGE_SMS_SETTINGS = 14;
//    static private int CALL_ID_MOVE_CONV_ALWAYS = 15;

    public interface Callback {
        void outOfOfficeCallback(MessagingException result, long accountId, int progress,
				Bundle oooResults);
        void emptyTrashCallback(MessagingException result, long accountId,
				int progress);
        void changeSmsCallback(int status);
        void moveConvAlwaysCallback(PGPKeyInfo [] info);
    }

    static public class CallbackImpl implements Callback {

		@Override
		public void outOfOfficeCallback(MessagingException result, long accountId, int progress,
				Bundle oooResults) {
		}

		@Override
		public void emptyTrashCallback(MessagingException result,
				long accountId, int progress) {
		}

		@Override
		public void changeSmsCallback(int status) {
		}

		@Override
		public void moveConvAlwaysCallback(PGPKeyInfo[] info) {
		}


    }
    abstract static public class oooCallback implements Callback {

    }

    abstract static public class emptyTrashCallback implements Callback {

    }
    
    abstract static public class changeSmsCallback implements Callback {

    }
    
    abstract static public class moveConvAlwaysCallback implements Callback {

    }


    public MessengerServiceUtil (Context context) {
        mContext = context;
        mMessenger = new EASBoundServiceMessenger(context);
    }

    public static MessengerServiceUtil getInstance(Context context, Callback callback) {
        if (sInstance == null) {
            sInstance = new MessengerServiceUtil(context);
        }
        mCallback = callback;
        return sInstance;
    }


    public void changeSmsSettings(long accountId) {
        Bundle b = new Bundle();
        b.putLong(ARG_ACCOUNT_ID, accountId);
        b.putString(ARG_TYPE, EmailPackage.MessengerService);

        ServiceCallback callback = getServiceCallback(mContext);
        callback.addCallback(CALL_ID_CHANGE_SMS_SETTINGS, mCallback);

        mMessenger.startMessenger(b, BoundServiceMessenger.MSG_CHANGE_SMS);
    }
    
    public void emptyTrash(long accountId) {
        Bundle b = new Bundle();
        b.putLong(ARG_ACCOUNT_ID, accountId);
        b.putString(ARG_TYPE, EmailPackage.MessengerService);

        ServiceCallback callback = getServiceCallback(mContext);
        callback.addCallback(CALL_ID_EMPTY_TRASH, mCallback);

        mMessenger.startMessenger(b, BoundServiceMessenger.MSG_EMPTY_TRASH);
    }
    
    public void getOutOfOffice(long accountId) {
        Bundle b = new Bundle();
        b.putLong(ARG_ACCOUNT_ID, accountId);
        b.putString(ARG_TYPE, EmailPackage.MessengerService);

        ServiceCallback callback = getServiceCallback(mContext);
        callback.addCallback(CALL_ID_GET_OOO, mCallback);

        mMessenger.startMessenger(b, BoundServiceMessenger.MSG_GET_OOO);
    }
    
    public void setOutOfOffice(long accountId, OoODataList data) {
        Bundle b = new Bundle();
        b.putLong(ARG_ACCOUNT_ID, accountId);
        b.putParcelable(ARG_OOO, data);
        b.putString(ARG_TYPE, EmailPackage.MessengerService);

        ServiceCallback callback = getServiceCallback(mContext);
        callback.addCallback(CALL_ID_SET_OOO, mCallback);

        mMessenger.startMessenger(b, BoundServiceMessenger.MSG_SET_OOO);
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

    static public void release() {
        clearCallback();
    }

    static private ServiceCallback sServiceCallback;
    static HashMap<Integer, List<Callback>> sCallbacks = new HashMap<Integer, List<Callback>> ();
    static private class ServiceCallback{

        Context mContext;

        public ServiceCallback(Context context) {
            mContext = context.getApplicationContext();
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

        public void changeSmsCallback(Bundle args) {
            int statusCode = args.getInt(ARG_STATUS_CODE);
            List<Callback> callbacks = null;
            if (statusCode == MessagingException.IN_PROGRESS) {
                callbacks = getCallback(CALL_ID_CHANGE_SMS_SETTINGS, false);
            } else {
                callbacks = getCallback(CALL_ID_CHANGE_SMS_SETTINGS, true);
            }
            for (Callback callback : callbacks) {
                //String msg = args.getString(ARG_TEXT);

                callback.changeSmsCallback(statusCode);
            }
        }
        
        public void emptyTrashCallback(Bundle args) {
        	
        	final long accountId = args.getLong(ARG_ACCOUNT_ID); 
			final int statusCode = args.getInt(ARG_STATUS_CODE);
			int progress = args.getInt(ARG_PROGRESS);
			
			MessagingException result = mapStatusToException(statusCode);
			switch (statusCode) {
			case MessagingException.SUCCESS:
				progress = 100;
				break;
			case MessagingException.IN_PROGRESS:
				// discard progress reports that look like sentinels
				if (progress < 0 || progress >= 100) {
					return;
				}
				break;
			}
			
            List<Callback> callbacks = null;
            if (statusCode == MessagingException.IN_PROGRESS) {
                callbacks = getCallback(CALL_ID_EMPTY_TRASH, false);
            } else {
                callbacks = getCallback(CALL_ID_EMPTY_TRASH, true);
            }
            for (Callback callback : callbacks) {
                //String msg = args.getString(ARG_TEXT);

                callback.emptyTrashCallback(result, accountId, progress);
            }
        }
        
        public void outOfOfficeCallback(Bundle args) {
        	long accountId = args.getLong(ARG_ACCOUNT_ID);
			Bundle oooResults = args.getBundle(ARG_BUNDLE);
			int statusCode = args.getInt(ARG_STATUS_CODE);
			int progress = args.getInt(ARG_PROGRESS);

			MessagingException result = mapStatusToException(MessagingException.OOO_EXCEPTION, statusCode);
			switch (statusCode) {
			case MessagingException.SUCCESS:
				progress = 100;
				break;
			case MessagingException.IN_PROGRESS:
				// discard progress reports that look like sentinels
				if (progress < 0 || progress >= 100) {
					return;
				}
				break;
			}
			
			int id = -1;
			if(oooResults != null) {
				oooResults.setClassLoader(mContext.getClassLoader());
				if(oooResults.getBoolean(OoOConstants.OOO_TYPE_SET, false))
					id = CALL_ID_SET_OOO;
				else
					id = CALL_ID_GET_OOO;
			} else 
				id = CALL_ID_GET_OOO;

            List<Callback> callbacks = null;
            if (statusCode == MessagingException.IN_PROGRESS) {
                callbacks = getCallback(id, false);
            } else {
                callbacks = getCallback(id, true);
            }
            for (Callback callback : callbacks) {
                //String msg = args.getString(ARG_TEXT);

                callback.outOfOfficeCallback(result, accountId, progress, oooResults);
            }
        }

    }

    public class EASBoundServiceMessenger extends BoundServiceMessenger{

        
        public EASBoundServiceMessenger(Context _context) {
            super(_context);
            // TODO Auto-generated constructor stub
        }

        @Override
        public boolean onIncomingMessage(Message msg) {
            Bundle b = msg.getData();
            switch ( msg.what ){
                case MSG_CHANGE_SMS_CALLBACK:
                    getServiceCallback(mContext).changeSmsCallback(b);
                    break;

                case MSG_EMPTY_TRASH_CALLBACK:
                    getServiceCallback(mContext).emptyTrashCallback(b);
                    break;
                    
                case MSG_OOO_CALBACK:
                    getServiceCallback(mContext).outOfOfficeCallback(b);
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

    private static MessagingException mapStatusToException(int statusCode) {
        return mapStatusToException(-1, statusCode);
    }

    private static MessagingException mapStatusToException(int exceptionType, int statusCode) {
        if (statusCode == MessagingException.IN_PROGRESS)
            return null;
        if (statusCode == MessagingException.SUCCESS)
            return null;

        if (exceptionType != -1) {
            return new MessagingException(exceptionType, statusCode);
        } else {
            return new MessagingException(statusCode);
        }
    }
    
    public EASBoundServiceMessenger getBoundServiceMessenger() {
    	return mMessenger;
    }
    
    public void destroy() {
    	sInstance = null;
    	mCallback = null;
    }
}
