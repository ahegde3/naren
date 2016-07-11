package com.samsung.android.emailcommon.service;

import com.samsung.android.emailcommon.utility.EmailLog;

import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class OnServiceConnectedCommand {

    private static final String TAG = "OnServiceConnectedCommand";
    
    /** The outgoing messenger. */
    private Messenger mOutMessenger = null;
    
    /** The incoming messenger. */
    private Messenger mInMessenger = null;
    
    /** Bundle for storing data to send to service when bound. */
    protected Bundle mData = null;
    protected int mCmd;
    
    //Flag informs if command has to be send when service is binding (true) or pending = wait until service is already bound (false)
    /** The is pending command. */
    private boolean isPendingCommand = false;
    
    public static final String TEST_STRING = "TestString";
    public static final String COMMAND_PENDING = "PendingCommand";
    /**
     * Instantiates a new on service connected command.
     */
    public OnServiceConnectedCommand() {
    }
    
    /**
     * Instantiates a new on service connected command.
     *
     * @param data the data
     */
    public OnServiceConnectedCommand(Bundle data, int cmd) {
        mData = data;
        mCmd = cmd;
    }
    
    public void onPreExecute(Messenger outMessenger, Messenger inMessenger) {
        mOutMessenger = outMessenger;
        mInMessenger = inMessenger;
    }
    public void execute() {
        sendingMessenger() ;
    }
    
    public void onPostExecute() {
    }
    
    public String[] getNames() {
        return null;
    }
    
    public String getName() {
        return "BoundServiceMessenger";
    }
    
    public void sendingMessenger() {
        Message msg = Message.obtain(null, mCmd);
        
        if(mInMessenger != null) {
            msg.replyTo = mInMessenger;
        }
        msg.setData(mData);
        if(mOutMessenger != null) {
            try {
                mOutMessenger.send(msg);
                EmailLog.d(TAG, "sendingMessenger: " + mCmd);
            } catch (RemoteException e) {
                e.printStackTrace();
            } 
        }
    }
    public void setCommandPending() {
        isPendingCommand = true;
    }
    
}
