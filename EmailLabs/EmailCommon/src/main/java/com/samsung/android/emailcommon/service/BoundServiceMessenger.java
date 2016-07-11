package com.samsung.android.emailcommon.service;

import com.samsung.android.emailcommon.EmailPackage;
import com.samsung.android.emailcommon.utility.EmailLog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

abstract public class BoundServiceMessenger implements ProxyVersion {

    private static final String TAG = "BoundServiceMessenger";

    private Context mContext;

    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = MSG_REGISTER_CLIENT + 1;
    
    public static final int MSG_PGPCREATEKEY = MSG_UNREGISTER_CLIENT +1 ;
    public static final int MSG_PGPKEYINFO = MSG_PGPCREATEKEY +1 ;
    public final static int MSG_PGPIMPORTKEY = MSG_PGPKEYINFO + 1;
    public final static int MSG_PGPEXPORTKEY = MSG_PGPIMPORTKEY + 1;
    public final static int MSG_PGPDELETEKEY = MSG_PGPEXPORTKEY + 1;
    public final static int MSG_PGPGETFINGERPRINT = MSG_PGPDELETEKEY + 1;
    public final static int MSG_PGPGETUSERINFO = MSG_PGPGETFINGERPRINT + 1;
    
    public static final int MSG_PGPCREATEKEYCALLBACK = MSG_PGPGETUSERINFO +1 ;
    public static final int MSG_PGPKEYINFOCALLBACK = MSG_PGPCREATEKEYCALLBACK +1 ;
    public final static int MSG_PGPIMPORTKEYCALLBACK = MSG_PGPKEYINFOCALLBACK + 1;
    public final static int MSG_PGPEXPORTKEYCALLBACK = MSG_PGPIMPORTKEYCALLBACK + 1;
    public final static int MSG_PGPDELETEKEYCALLBACK = MSG_PGPEXPORTKEYCALLBACK + 1;
    public final static int MSG_PGPGETFINGERPRINTCALLBACK = MSG_PGPDELETEKEYCALLBACK + 1;
    public final static int MSG_PGPGETUSERINFOCALLBACK = MSG_PGPGETFINGERPRINTCALLBACK + 1;
    public final static int MSG_PGPGETKEYINFOCALLBACK = MSG_PGPGETUSERINFOCALLBACK + 1;        public final static int MSG_SET_OOO = MSG_PGPGETKEYINFOCALLBACK + 1;    public final static int MSG_GET_OOO = MSG_SET_OOO + 1;    public final static int MSG_EMPTY_TRASH = MSG_GET_OOO + 1;    public final static int MSG_CHANGE_SMS = MSG_EMPTY_TRASH + 1;    public final static int MSG_MOVE_CONV_ALWAYS = MSG_CHANGE_SMS + 1;        public final static int MSG_OOO_CALBACK = MSG_MOVE_CONV_ALWAYS + 1;    public final static int MSG_EMPTY_TRASH_CALLBACK = MSG_OOO_CALBACK + 1;    public final static int MSG_CHANGE_SMS_CALLBACK = MSG_EMPTY_TRASH_CALLBACK + 1;    public final static int MSG_MOVE_CONV_ALWAYS_CALLBACK = MSG_CHANGE_SMS_CALLBACK + 1;
    
    
    public BoundServiceMessenger(Context _context) {
        mContext = _context;
        mIncomingHandler = new IncomingHandler(this);
    }


    private Context getContext() {
        return mContext;
    }
    

    public static class IncomingHandler extends Handler {

        WeakReference<BoundServiceMessenger> viewHelper;

        public  IncomingHandler(BoundServiceMessenger view) {
             viewHelper = new WeakReference<BoundServiceMessenger>(
                     view);
         }
        
        @Override                                                                                                                                    
        public void handleMessage( Message msg ){
            if(viewHelper != null) {
                BoundServiceMessenger service = viewHelper.get();
                if(service != null) {
                    if( service.onIncomingMessage(msg) == false)
                        super.handleMessage(msg);
                }   
            }
        }
   }
       
    abstract public boolean onIncomingMessage(Message msg);
    
    protected ServiceConnectionAdapter mPhoneServiceConnectionAdapter = null;
    protected ServiceConnectionManager mServicConnectionManager = null;
    protected IncomingHandler mIncomingHandler = null;
    
    
    protected void sendPhoneServiceCommand(OnServiceConnectedCommand command) {        if(mPhoneServiceConnectionAdapter != null && command != null) {
            EmailLog.d(TAG, "resendPhoneServiceCommand: " + command.mCmd);
            mPhoneServiceConnectionAdapter.sendPendingCommand(command);
        }
        else if(command != null) {
            String connectionCls = command.mData.getString(ProxyArgs.ARG_TYPE, "");                        if("".equalsIgnoreCase(connectionCls)) {            	connectionCls = EmailPackage.PGPKeyService;            }                        String connectionPkg = EmailPackage.PKG_PROVIDER;
            mServicConnectionManager = ServiceConnectionManager.getInstance(getContext());
            EmailLog.d(TAG, "sendPhoneServiceCommand : " + command.mCmd);
            mPhoneServiceConnectionAdapter = mServicConnectionManager.addBind(
                    connectionCls, connectionPkg, getContext(), mIncomingHandler, command);
            }
        }
    
    protected void requestMessengerService(Bundle args, int cmd){        OnServiceConnectedCommand command = new OnServiceConnectedCommand(args, cmd);
        sendPhoneServiceCommand(command);
    }

    public void startMessenger(final Bundle args, int cmd){        requestMessengerService(args, cmd);
    }

}
