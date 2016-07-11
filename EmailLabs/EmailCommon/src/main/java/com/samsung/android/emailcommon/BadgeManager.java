/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.samsung.android.emailcommon;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDiskIOException;

import com.samsung.android.emailcommon.provider.EmailContent.Message;
import com.samsung.android.emailcommon.utility.EmailLog;
import com.samsung.android.emailcommon.utility.Log;
import com.samsung.android.emailcommon.utility.Utility;
import com.sec.android.touchwiz.app.BadgeNotification;
import com.samsung.android.emailcommon.utility.EmailRuntimePermission;

public class BadgeManager {

    private static final String TAG = "BadgeManager";
    
    private static int getUnreadTotalCount(Context context) {

        int unreadMessageCount = Message.getAllUnreadCount(context);
        if (unreadMessageCount < 0) {
            unreadMessageCount = 0;
        }
        return unreadMessageCount;
    }
    
    public static void updateBadgeProvider(final Context context) {
    	Utility.runAsync(new Runnable() {
			@Override
			public void run() {
		        int cnt = getUnreadTotalCount(context);
		        updateBadgeProvider(context, cnt);
			}
    	});
    }
    
    private static void updateBadgeProvider(Context context, int cnt) {

        try {
            BadgeNotification.Apps.setBadgeCount(context.getContentResolver(), EmailPackage.PKG_UI, EmailPackage.Message, cnt);
        }catch (IllegalArgumentException e) {
            EmailLog.d(TAG, "BadgeProvider wasn't installed ");
        }catch (SQLiteDiskIOException e) {
            EmailLog.d(TAG, "SQLiteDiskIOException while setting badge count ");
        }catch (NoClassDefFoundError e) {
            EmailLog.d(TAG, "no class");
        } catch (Exception e) {
            Log.dumpException(TAG, e);
        }
        EmailLog.d(TAG, "[updateBadgeProvider] - cnt : " + cnt);

        if(Utility.numOfAccount(context) > 0) {

            if(!EmailRuntimePermission.hasPermissions(context,EmailRuntimePermission.PERMISSION_CONTACTS)){
                NotificationUtil.showRuntimePermissionNoti(context, EmailRuntimePermission.PERM_REQUEST_TYPE_CONTACTS,
                        EmailRuntimePermission.PERMISSION_FUNCTION_CONTACT_INFO);
                return;
            }

            Cursor c = null;
            try {
                c = context.getContentResolver().query(NotificationUtil.REFRESH_PEOPLE_STRIPE_NOTI_URI, null, null, null, null);
            } catch (Exception e) {
                Log.dumpException(TAG, e);
            } finally {
                if (c != null) c.close();
            }
        }
    }

}
