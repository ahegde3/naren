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

package com.samsung.android.emailcommon.provider;

import android.os.Parcel;
import android.os.Parcelable;

public class FilterItem extends BaseItem implements Parcelable   {
    public int id;
    public int contact_id;
    
    // normal item
    public int address_id;
    public String address;
    
    // contact item image, name
    public String contact_name;
        
    public String subject_name;
    public String folder_name;
    
    public FilterItem() {
        id = -1;
        contact_id = -1;
        address_id = -1;
        address = null;
        contact_name = null;
        subject_name = null;
        folder_name = null;
    }
    
    public FilterItem(Parcel in) {
        id = in.readInt();
        contact_id = in.readInt();
        address_id = in.readInt();
        address = in.readString();
        contact_name = in.readString();
        subject_name = in.readString();
        folder_name = in.readString();
    }
    
    @Override
    public String toString() {
        return address;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeInt(id);
        dest.writeInt(contact_id);
        dest.writeInt(address_id);
        dest.writeString(address);
        dest.writeString(contact_name);
        dest.writeString(subject_name);
        dest.writeString(folder_name);
    }
    
    /**
     * Implements Parcelable
     */
    public static final Parcelable.Creator<FilterItem> CREATOR = new Parcelable.Creator<FilterItem>() {
        @Override
        public FilterItem createFromParcel(Parcel in) {
            return new FilterItem(in);
        }

        @Override
        public FilterItem[] newArray(int size) {
            return new FilterItem[size];
        }
    };
}