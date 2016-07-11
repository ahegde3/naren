
package com.samsung.android.emailcommon.service;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

import com.samsung.android.emailcommon.utility.EmailLog;

public class OoODataList implements Parcelable {
    private static final String TAG = "OoODataList";

    ArrayList<OoOData> dataList = new ArrayList<OoOData>();

    public static final Parcelable.Creator<OoODataList> CREATOR = new Parcelable.Creator<OoODataList>() {
        public OoODataList createFromParcel(Parcel in) {
            return new OoODataList(in);
        }

        public OoODataList[] newArray(int size) {
            return new OoODataList[size];
        }
    };

    private OoODataList(Parcel in) {
        readFromParcel(in);
    }

    public OoODataList() {

    }

    public int getCount() {
        return dataList.size();
    }

    public OoOData getItem(int i) {
        if (i >= 0 && i < getCount())
            return dataList.get(i);
        return null;
    }

    public int AddOoOData(int atype, int astate, int aenabled, String amsg) {
        dataList.add(new OoOData(atype, astate, aenabled, amsg));
        return 0;
    }

    public int AddOoOData(int atype, int astate, int aenabled, String amsg, long astart, long aend) {
        dataList.add(new OoOData(atype, astate, aenabled, amsg, astart, aend));
        return 0;
    }

    // public int AddOoOData(int atype, int astate, int aenabled, String amsg,
    // Date astart, Date aend) throws InvalidParameterException
    public int AddOoOData(int atype, int astate, int aenabled, String amsg, Date astart, Date aend) {
        dataList.add(new OoOData(atype, astate, aenabled, amsg, astart, aend));
        return 0;
    }

    public void displayOoOData() {
    	EmailLog.d(TAG, "OOF ResultList: Number of results = " + dataList.size());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelableArray(dataList.toArray(new OoOData[] {}), flags);
    }

    public void readFromParcel(Parcel in) {
        Parcelable[] tmp = in.readParcelableArray(getClass().getClassLoader());
        for (Parcelable p : tmp) {
            dataList.add((OoOData) p);
        }
    }
}
