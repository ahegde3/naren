/*
* Copyright 2015 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.samsung.android.emailcommon.utility;

import android.content.Context;
import android.content.pm.PackageManager;

import java.util.ArrayList;

/**
 * Created by jaesung.yi on 2015-10-09.
 */
public class PermissionUtil {
    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if(grantResults.length < 1){
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static ArrayList<String> getRequiredPermission(Context context, String[] permissions ) {
        if( null == permissions || permissions.length == 0 ) return null;

        ArrayList<String> result = new ArrayList<String>();

        for( String permission: permissions ) {
            if (PackageManager.PERMISSION_DENIED == context.checkSelfPermission(permission)) {
                result.add(permission);
            }
        }

        return result;
    }
}
