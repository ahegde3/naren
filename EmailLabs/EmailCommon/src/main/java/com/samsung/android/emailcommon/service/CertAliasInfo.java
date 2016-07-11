package com.samsung.android.emailcommon.service;

import android.os.Parcel;
import android.os.Parcelable;

public class CertAliasInfo implements Parcelable {
	public String mAlias;
	public boolean [] mUsage;
	
	public static final Parcelable.Creator<CertAliasInfo> CREATOR = new Parcelable.Creator<CertAliasInfo>() {
		public CertAliasInfo createFromParcel(Parcel in) {
			return new CertAliasInfo(in);
		}

		public CertAliasInfo[] newArray(int size) {
			return new CertAliasInfo[size];
		}
	};

	private CertAliasInfo(Parcel in) {
		readFromParcel(in);
	}

	public CertAliasInfo(String arg1, boolean [] arg2) {
		mAlias = arg1;
		mUsage = arg2;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
//		Boolean [] oUsage = new Boolean[mUsage.length];
//		for (int i = 0; i < oUsage.length; i ++) {
//			oUsage[i] = mUsage[i];
//		}	
		dest.writeString(mAlias);
		dest.writeBooleanArray(mUsage);
//		dest.writeArray(oUsage);
	}
	
	private void readFromParcel(Parcel in) {
		mAlias = in.readString();
		mUsage = in.createBooleanArray();
//		Object [] oUsage = in.readArray(CertAliasInfo.class.getClassLoader());
//		if (oUsage != null) {
//			mUsage = new boolean[oUsage.length];
//			for (int i = 0; i < mUsage.length; i ++) {
//				mUsage[i] = (Boolean)oUsage[i];
//			}
//		} else {
//			mUsage = new boolean [0];
//		}
	}

}
