package com.samsung.android.emailcommon.service;

import com.samsung.android.emailcommon.utility.EmailLog;

import java.util.ArrayList;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import android.os.UserHandle;

/**
 * The Class ServiceConnectionAdapter.
 * Definition of adapter for ServiceConnection class
 */
public class ServiceConnectionAdapter {

    public static final String LOG_TAG = "ServiceConnectionAdapter";

//    private UserHandle mUserHandle;
    private Context mContext;
    private Messenger mIncomingMessenger = null;
    private ServiceConnection mServiceConnection = null;
    protected boolean isBound = false;
    protected Messenger mServiceMessenger = null;
    protected OnServiceConnectedCommand mOnConnectedCommand = null;
    private ArrayList<OnServiceConnectedCommand> mPendingCommandsList = null;
    
    public ServiceConnectionAdapter(Context context, Handler incomingHandler, OnServiceConnectedCommand onConnectedCommand) {
        mContext = context;
        if(incomingHandler != null) {
            mIncomingMessenger = new Messenger(incomingHandler);
        }
        mOnConnectedCommand = onConnectedCommand;
        mPendingCommandsList = new ArrayList<OnServiceConnectedCommand>();
        //        mUserHandle = new UserHandle(UserHandle.USER_CURRENT);
        mServiceConnection = createServiceConnection();
    }

    private ServiceConnection createServiceConnection() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName className, IBinder service) {
                mServiceMessenger = new Messenger(service);
                isBound = true;
                EmailLog.d(LOG_TAG, "onServiceConnected()");
                if(mOnConnectedCommand != null) {
                    mOnConnectedCommand.onPreExecute(mServiceMessenger, mIncomingMessenger);
                    mOnConnectedCommand.execute();
                }
                executePendingCommands();
            }
            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                EmailLog.d(LOG_TAG, "onServiceDisconnected()");
                isBound = false;
            }
        };
        return mServiceConnection;
    }

    public void bind(String className, String pkgName) { 
        //     Intent bind = new Intent(mContext, className);

        Intent i = new Intent(className);
        i.setComponent(new ComponentName(pkgName, className));
        mContext.getApplicationContext().bindService(i, mServiceConnection, Context.BIND_AUTO_CREATE);

        EmailLog.d(LOG_TAG, "connectService(): bind service");
        //        mContext.bindServiceAsUser(bind, mServiceConnection, Context.BIND_AUTO_CREATE, mUserHandle);
        //mContext.bindService(bind, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbind() {
        EmailLog.d(LOG_TAG, "disconnectService(): unbind service");
        mContext.getApplicationContext().unbindService(mServiceConnection);
    }

    public ServiceConnection getServiceConnection() {
        return mServiceConnection;
    }

    public boolean sendPendingCommand(OnServiceConnectedCommand onConnectedCommand) {
        if(mServiceMessenger != null && isBound == true) {
            onConnectedCommand.setCommandPending();
            onConnectedCommand.onPreExecute(mServiceMessenger, mIncomingMessenger);
            onConnectedCommand.execute();
            return true;
        }
        addPendingCommand(onConnectedCommand);
        return false;
    }

    private void addPendingCommand(OnServiceConnectedCommand onConnectedCommand) {
        onConnectedCommand.setCommandPending();
        mPendingCommandsList.add(onConnectedCommand);
    }

    private boolean executePendingCommands() {
        boolean returnFlag = false;
        int processedCnt = 0;
        
        while(processedCnt < mPendingCommandsList.size())   {
            EmailLog.d(LOG_TAG, "mPendingCommandsList ... " + mPendingCommandsList.size());
            OnServiceConnectedCommand command = mPendingCommandsList.get(processedCnt);
            if(command != null) {
                EmailLog.d(LOG_TAG, "Executing ... " + command.getName());
                command.onPreExecute(mServiceMessenger, mIncomingMessenger);
                command.execute();
                returnFlag = true;
            }
            processedCnt++;
        }
        return returnFlag;
    }

    public String getOnServiceConnectedCommandName() {
        return mOnConnectedCommand.getName();
    }
}
