/*
 * Copyright (C) 2009 The Android Open Source Project
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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.samsung.android.emailcommon.EmailPackage;
import com.samsung.android.emailcommon.IntentConst;
import com.samsung.android.emailcommon.utility.EmailLog;

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

public class EmailServiceProxy extends ServiceProxy implements IEmailService, ProxyVersion {
//    private static final String TAG = "EmailServiceProxy";

    public static final String AUTO_DISCOVER_BUNDLE_ERROR_CODE = "autodiscover_error_code";
    public static final String AUTO_DISCOVER_BUNDLE_HOST_AUTH = "autodiscover_host_auth";

    public static final String VALIDATE_BUNDLE_RESULT_CODE = "validate_result_code";
    public static final String VALIDATE_BUNDLE_POLICY_SET = "validate_policy_set";
    public static final String VALIDATE_BUNDLE_ERROR_MESSAGE = "validate_error_message";
    public static final String VALIDATE_BUNDLE_PROTOCOL_VERSION = "validate_protocol_version"; // jykim
                                                                                               // send
                                                                                               // protocolVersion
    public static final String VALIDATE_BUNDLE_LEGACY_CAPABILITIES = "legacy_capabilities";

    public static final String VALIDATE_BUNDLE_UNSUPPORTED_POLICIES = "validate_unsupported_policies";
    private final IEmailServiceCallback mCallback;
//    private Object mReturn = null;
    private IEmailService mService;
    
    
    public static EmailServiceProxy getEasProxy(Context _context, IEmailServiceCallback callback) {
    	Intent i = new Intent(IntentConst.EXCHANGE_INTENT);
    	i.setComponent(new ComponentName(EmailPackage.PKG_PROVIDER, EmailPackage.EasService));
    	return new EmailServiceProxy(_context, i, callback);
    }
    
    public static EmailServiceProxy getImapProxy(Context _context, IEmailServiceCallback callback) {
    	Intent i = new Intent(IntentConst.IMAP_INTENT);
    	i.setComponent(new ComponentName(EmailPackage.PKG_PROVIDER, EmailPackage.ImapService));
    	return new EmailServiceProxy(_context, i, callback);
    }
    
    public static EmailServiceProxy getPop3Proxy(Context _context, IEmailServiceCallback callback) {
    	Intent i = new Intent(IntentConst.POP3_INTENT);
    	i.setComponent(new ComponentName(EmailPackage.PKG_PROVIDER, EmailPackage.PopService));
    	return new EmailServiceProxy(_context, i, callback);
    }
    
    // @hide
    protected EmailServiceProxy(Context _context, Intent intent, IEmailServiceCallback callback) {
    	super(_context, intent);
    	mCallback = callback;
    }
    

    @Override
    public void onConnected(IBinder binder) {
        mService = IEmailService.Stub.asInterface(binder);
    }


    public void addCallback() {
    	if (mCallback != null) {
    		setTask(new ProxyTask() {
    			public void run() throws RemoteException {
    				mService.setCallback(mCallback);
    			}
    		}, "setCallback");
    	}
    }
    
    public void removeCallback() {
    	if (mCallback != null) {
    		setTask(new ProxyTask() {
    			public void run() throws RemoteException {
    				mService.removeCallback(mCallback);
    			}
    		}, "setCallback");
    	}
    }
    
    public void setCallback(final IEmailServiceCallback cb) throws RemoteException {
        setTask(new ProxyTask() {
            public void run() throws RemoteException {
                mService.setCallback(cb);
            }
        }, "setCallback");
    }

    public void removeCallback(final IEmailServiceCallback cb) throws RemoteException {
        boolean retVal = setTask(new ProxyTask() {
            public void run() throws RemoteException {
                mService.removeCallback(cb);
            }
        }, "removeCallback");

        if (retVal == true) {
            waitForCompletion();
        }
    }

    @Override
    public Bundle sendMeetingResponse(final String version, final Bundle bundle)
            throws RemoteException {
        setTask(new ProxyTask() {
            public void run() throws RemoteException {
                if (mCallback != null)
                    mService.setCallback(mCallback);
                mService.sendMeetingResponse(version, bundle);
            }
        }, "sendMeetingResponse");
        return null;
    }

    // change@SISO for Edit Response options start
    @Override
    public Bundle sendMeetingEditedResponse(final String version, final Bundle bundle)
    		throws RemoteException {

        setTask(new ProxyTask() {

            public void run() throws RemoteException {

                if (mCallback != null)
                    mService.setCallback(mCallback);

                mService.sendMeetingEditedResponse(version, bundle);

            }

        }, "sendMeetingEditedResponse");
        return null;

    }

    // change@SISO for Edit Response options end
    @Override
    public Bundle loadMore(final String version, final Bundle bundle) throws RemoteException {
        // change@wtl.dtuttle start
        // dr_30: loadMore() modeled after loadAttachment().
        setTask(new ProxyTask() {
            public void run() {
                try {
                    if (mCallback != null)
                        mService.setCallback(mCallback);
                    mService.loadMore(version, bundle);
                } catch (RemoteException e) {
                }
            }
        });
        // chance@wtl.dtuttle end
        return null;
    }

    @Override
    public Bundle loadMoreCancel(final String version, final Bundle bundle) throws RemoteException {
        setTask(new ProxyTask() {
            public void run() {
                try {
                    if (mCallback != null)
                        mService.setCallback(mCallback);
                    mService.loadMoreCancel(version, bundle);
                } catch (RemoteException e) {
                }
            }
        });
        return null;
    }

    @Override
    public Bundle refreshMessageBody(final String version, final Bundle bundle)
            throws RemoteException {
        setTask(new ProxyTask() {
            public void run() {
                try {
                    if (mCallback != null)
                        mService.setCallback(mCallback);
                    mService.refreshMessageBody(version, bundle);
                } catch (RemoteException e) {
                }
            }
        });
        return null;
    }

    public IBinder asBinder() {
        return null;
    }

    @Override
    public Bundle emptyTrash(final String version, final Bundle bundle) throws RemoteException {
        setTask(new ProxyTask() {
            public void run() {
                try {
                    if (mCallback != null)
                        mService.setCallback(mCallback);
                    mService.emptyTrash(version, bundle);
                } catch (RemoteException e) {
                }
            }
        });
        return null;
    }


    @Override
    public Bundle OoOffice(final String version, final Bundle bundle) throws RemoteException {
        setTask(new ProxyTask() {
            public void run() {
                try {
                    if (mCallback != null)
                        mService.setCallback(mCallback);
                    mService.OoOffice(version, bundle);
                } catch (RemoteException e) {
                }
            }
        });
        return null;
    }

    // jykim 0518 start
    @Override
    public Bundle changeSmsSettings(final String version, final Bundle bundle) throws RemoteException {
        setTask(new ProxyTask() {
            public void run() {
                try {
                    if (mCallback != null)
                        mService.setCallback(mCallback);
                    mService.changeSmsSettings(version, bundle);
                } catch (RemoteException e) {
                }
            }
        });
        return null;
    }


    // change@siso.kanchan 14.1 IRM feature start.
    @Override
    public Bundle refreshIRMTemplates(final String version, final Bundle bundle) throws RemoteException {
        setTask(new ProxyTask() {
            public void run() {
                try {
                    if (mCallback != null) {
                        mService.setCallback(mCallback);
                    }
                    mService.refreshIRMTemplates(version, bundle);
                } catch (RemoteException e) {
                }
            }
        });
        return null;
    }

    // change@siso.kanchan 14.1 IRM feature end.

    // huijae.lee
    @Override
    public Bundle searchMessage(final String version, final Bundle bundle) throws RemoteException {
        setTask(new ProxyTask() {
            public void run() {
                try {
                    if (mCallback != null) {
                        mService.setCallback(mCallback);
                    }
                    mService.searchMessage(version, bundle);
                } catch (RemoteException e) {

                }
            }
        },"searchMessage");
        return null;
    }

	@Override
	public Bundle updateFolderList(final String version, final Bundle bundle)
			throws RemoteException {
		setTask(new ProxyTask() {
            public void run() {
                try {
                    if (mCallback != null)
                        mService.setCallback(mCallback);
                    mService.updateFolderList(version, bundle);
                } catch (RemoteException e) {
                }
            }
        });
		return null;
	}


	@Override
	public Bundle loadAttachment(final String version, final Bundle bundle)
			throws RemoteException {
		setTask(new ProxyTask() {
            public void run() throws RemoteException {
                try {
                    if (mCallback != null)
                    	EmailLog.e("[DPTEST]", "[DPTEST]EmailServiceProxy : loadAttachment");
                        mService.setCallback(mCallback);
                    mService.loadAttachment(version, bundle);
                } catch (RemoteException e) {
                    try {
                        // Try to send a callback (if set)
                        if (mCallback != null) {
                            mCallback.loadAttachmentStatus(version, bundle);
                        }
                    } catch (RemoteException e1) {
                    }
                }
            }
        }, "loadAttachment");
		
		return null;
	}

	@Override
	public Bundle moveMessageInterAccount(final String version, final Bundle args)
			throws RemoteException {
		setTask(new ProxyTask() {
            public void run() throws RemoteException {
                try {
                    mService.moveMessageInterAccount(version, args);
                } catch (RemoteException e) {
                }
            }
        }, "moveMessageInterAccount");
		return null;
	}

    public void removeDownloadAttachment(final Bundle args)
            throws RemoteException {
        setTask(new ProxyTask() {
            public void run() throws RemoteException {
                try {
                    mService.removeDownloadAttachment(args);
                } catch (RemoteException e) {
                }
            }
        }, "moveMessageInterAccount");
    }
}
