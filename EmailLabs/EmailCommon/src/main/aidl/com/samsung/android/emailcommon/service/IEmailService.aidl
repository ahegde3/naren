/*
 * Copyright (C) 2008-2010 Marc Blank
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

import com.samsung.android.emailcommon.service.IEmailServiceCallback;
import android.os.Bundle;

interface IEmailService {
	Bundle loadMore(String version, in Bundle args);
	Bundle loadMoreCancel(String version, in Bundle args);
	
	Bundle refreshMessageBody(String version, in Bundle args);
	Bundle updateFolderList(String version, in Bundle args);
	Bundle sendMeetingResponse(String version, in Bundle args);
	Bundle sendMeetingEditedResponse(String version, in Bundle args);
	
	Bundle emptyTrash(String version, in Bundle args);
	Bundle OoOffice(String version, in Bundle args);
	Bundle refreshIRMTemplates(String version, in Bundle args);
	Bundle searchMessage(String version, in Bundle args);
	Bundle changeSmsSettings(String version, in Bundle args);
	void setCallback(IEmailServiceCallback cb);
    void removeCallback(IEmailServiceCallback cb);
    
    Bundle loadAttachment(String version, in Bundle args);
    
    Bundle moveMessageInterAccount(String version, in Bundle args);
    void removeDownloadAttachment(in Bundle args);
}