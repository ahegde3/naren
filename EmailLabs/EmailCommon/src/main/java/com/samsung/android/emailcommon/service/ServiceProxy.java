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

package com.samsung.android.emailcommon.service;

import com.samsung.android.emailcommon.utility.EmailLog;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Debug;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * The EmailServiceProxy class provides a simple interface for the UI to call
 * into the various EmailService classes (e.g. ExchangeService for EAS). It
 * wraps the service connect/disconnect process so that the caller need not be
 * concerned with it. Use the class like this: new EmailServiceClass(context,
 * class).loadAttachment(attachmentId, callback) Methods without a return value
 * return immediately (i.e. are asynchronous); methods with a return value wait
 * for a result from the Service (i.e. they should not be called from the UI
 * thread) with a default timeout of 30 seconds (settable) An EmailServiceProxy
 * object cannot be reused (trying to do so generates a RemoteException)
 */

public abstract class ServiceProxy {
    private static final boolean DEBUG_PROXY = false; // DO NOT CHECK THIS IN
                                                      // SET TO TRUE
    private final String mTag;

    private final Context mContext;
    protected final Intent mIntent;
    private Runnable mRunnable = new ProxyRunnable();
    private ProxyTask mTask;
    private String mName = " unnamed";
    private final ServiceConnection mConnection = new ProxyConnection();
    // Service call timeout (in seconds)
    private int mTimeout = 15;
    private int mAutoDiscoverTimeout = 300; // // 5 minutes for autodiscovery to
                                            // be given.
    private long mStartTime;
    private boolean mDead = false;
    public boolean mBindable = true;

    public abstract void onConnected(IBinder binder);

    public ServiceProxy(Context _context, Intent _intent) {
        mContext = _context;
        mIntent = _intent;
        mTag = getClass().getSimpleName();
        if (Debug.isDebuggerConnected()) {
            mTimeout <<= 2;
        }
    }

    private class ProxyConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName name, IBinder binder) {
            onConnected(binder);
            if (DEBUG_PROXY) {
                EmailLog.v(mTag, "Connected: " + name.getShortClassName());
            }
            // Run our task on a new thread
            new Thread(new Runnable() {
                public void run() {
                    runTask();
                }
            }).start();
        }

        public void onServiceDisconnected(ComponentName name) {
            if (DEBUG_PROXY) {
                EmailLog.v(mTag, "Disconnected: " + name.getShortClassName());
            }
        }
    }

    public interface ProxyTask {
        public void run() throws RemoteException;
    }

    private class ProxyRunnable implements Runnable {
        @Override
        public void run() {
            try {
                mTask.run();
            } catch (RemoteException e) {
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ServiceProxy setTimeout(int secs) {
        mTimeout = secs;
        return this;
    }

    public int getTimeout() {
        return mTimeout;
    }

    public void endTask() {
        try {
            mContext.unbindService(mConnection);
        } catch (IllegalArgumentException e) {
            // This can happen if the user ended the activity that was using the
            // service
            // This is harmless, but we've got to catch it
        } catch (Exception e) {
            e.printStackTrace();
        }

        synchronized (mConnection) {
            if (DEBUG_PROXY) {
                EmailLog.v(mTag, "Task " + mName + " completed; disconnecting");
            }
            mConnection.notify();
        }
    }

    private void runTask() {
        Thread thread = new Thread(mRunnable);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        endTask();
    }

    public boolean setTask(ProxyTask task, String name) {
        mName = name;
        return setTask(task);
    }

    public boolean setTask(ProxyTask task) throws IllegalStateException {
        if (mDead) {
            throw new IllegalStateException();
        }
        try {
            mTask = task;
            mStartTime = System.currentTimeMillis();
            if (DEBUG_PROXY) {
                EmailLog.v(mTag, "Bind requested for task " + mName);
            }

            if (mContext == null)
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            if (mBindable == true) {
                return mContext.bindService(mIntent, mConnection, Context.BIND_AUTO_CREATE);
            } else {
                mBindable = true;
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void waitForCompletion() {
        synchronized (mConnection) {
            long time = System.currentTimeMillis();
            try {
                if (DEBUG_PROXY) {
                    EmailLog.v(mTag, "Waiting for task " + mName + " to complete...");
                }
                mConnection.wait(mTimeout * 1000L);
            } catch (InterruptedException e) {
                // Can be ignored safely
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (DEBUG_PROXY) {
                EmailLog.v(mTag,
                        "Wait for " + mName + " finished in " + (System.currentTimeMillis() - time)
                                + "ms");
            }
        }
    }

    public void waitForCompletionForAutoDiscover() {
        synchronized (mConnection) {
            long time = System.currentTimeMillis();
            try {
                if (DEBUG_PROXY) {
                    EmailLog.v(mTag, "Waiting for task " + mName + " to complete...");
                }
                mConnection.wait(mAutoDiscoverTimeout * 1000L);
            } catch (InterruptedException e) {
                // Can be ignored safely
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (DEBUG_PROXY) {
                EmailLog.v(mTag,
                        "Wait for " + mName + " finished in " + (System.currentTimeMillis() - time)
                                + "ms");
            }
        }
    }

    public void close() throws RemoteException {
        if (mDead) {
            throw new RemoteException();
        }
        endTask();
        mDead = true;
    }

    /**
     * Connection test; return indicates whether the remote service can be
     * connected to
     * 
     * @return the result of trying to connect to the remote service
     */
    public boolean test() {
        try {
            return setTask(new ProxyTask() {
                public void run() throws RemoteException {
                    if (DEBUG_PROXY) {
                        EmailLog.v(mTag,
                                "Connection test succeeded in "
                                        + (System.currentTimeMillis() - mStartTime) + "ms");
                    }
                }
            }, "test");
        } catch (Exception e) {
            // For any failure, return false.
            return false;
        }
    }
}
