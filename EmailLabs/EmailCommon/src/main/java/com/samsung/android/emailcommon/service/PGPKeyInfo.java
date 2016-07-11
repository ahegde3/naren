package com.samsung.android.emailcommon.service;


import android.os.Parcel;
import android.os.Parcelable;

public class PGPKeyInfo implements Parcelable {
	public long mRID;
	public long mKEYID;
	public String mUSERID;
	public String mEMAILID;
	public int mDefaultKey;
	public long mCreation;
	public long mExpiry;
	public int mCanEncrypt;
	public int mValidate;
	public int mType;
	public long mDBID;
	public static final Parcelable.Creator<PGPKeyInfo> CREATOR = new Parcelable.Creator<PGPKeyInfo>() {
        public PGPKeyInfo createFromParcel(Parcel in) {
            return new PGPKeyInfo(in);
        }

        public PGPKeyInfo[] newArray(int size) {
            return new PGPKeyInfo[size];
        }
    };
    
    private PGPKeyInfo(Parcel in) {
        readFromParcel(in);
    }

    public PGPKeyInfo() {
    }

    
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(mRID);
		dest.writeLong(mKEYID);
		dest.writeString(mUSERID);
		dest.writeString(mEMAILID);
		dest.writeInt(mDefaultKey);
		dest.writeLong(mCreation);
		dest.writeLong(mExpiry);
		dest.writeInt(mCanEncrypt);
		dest.writeInt(mValidate);
		dest.writeInt(mType);
		dest.writeLong(mDBID);
	}

	
	public void readFromParcel(Parcel in) {
		mRID = in.readLong();
		mKEYID = in.readLong();
		mUSERID = in.readString();
		mEMAILID = in.readString();
		mDefaultKey = in.readInt();
		mCreation = in.readLong();
		mExpiry = in.readLong();
		mCanEncrypt = in.readInt();
		mValidate = in.readInt();
		mType = in.readInt();
		mDBID = in.readLong();
    }
}
