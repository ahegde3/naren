/*
 * Copyright (C) 2008-2009 Marc Blank
 * Licensed to The Android Open Source Project.
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
import android.os.Bundle;

oneway interface IEmailServiceCallback {

    void oOOfStatus(String version, in Bundle args);
    void emptyTrashStatus(String version, in Bundle args);
//	void closeSSLVerificationDialog(String version, in Bundle args);
	void moveConvAlwaysStatus(String version, in Bundle args);
	void loadAttachmentStatus(String version, in Bundle args);
	void loadMoreStatus(String version, in Bundle args);
	//Email SDK changes@prathima.s move msg status callback --- 05/29/14 --- start
	void moveMessageStatus(String version, in Bundle args);
	//Email SDK changes@prathima.s move msg status callback --- 05/29/14 --- end
	void sendMeetingEditedResponseCallback(String version, in Bundle args);
  	void deviceInfoStatus(String version, in Bundle args);
  	void searchMessageStatus(String version, in Bundle args);
  	void syncMessageStatus(String version, in Bundle args);
  	void syncAccountStatus(String version, in Bundle args);
  	void refreshIRMTemplatesStatus(String version, in Bundle args);
}
