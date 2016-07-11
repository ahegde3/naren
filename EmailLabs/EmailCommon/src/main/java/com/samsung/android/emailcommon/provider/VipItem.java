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

public class VipItem	{
	public int id;
	public int contact_id;
	
	// normal item
	public int address_id;
	public String address;
	
	// contact item	image, name
	public String contact_name;
	
	public byte[] photo;
	
	public VipItem() {
		id = -1;
		contact_id = -1;
		address_id = -1;
		address = null;
		contact_name = null;
		photo = null;
    }
	
	@Override
    public String toString() {
        return address;
    }
}