
package com.samsung.android.emailcommon.service;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.TimeZone;

public class OoOData implements Parcelable {
    private static final int OOO_CONST_BASE = 0;

    // please do not change the next three values,
    // they are taken from the protocol documentation.
    public static final int OOO_STATE_DISABLE = OOO_CONST_BASE;

    public static final int OOO_STATE_GLOBAL = OOO_CONST_BASE + 1;

    public static final int OOO_STATE_TIME_BASED = OOO_CONST_BASE + 2;

    public static final int OOO_TYPE_GENERIC = OOO_CONST_BASE + 3;

    public static final int OOO_TYPE_INTERNAL = OOO_CONST_BASE + 4;

    public static final int OOO_TYPE_EXTERNAL_KNOWN = OOO_CONST_BASE + 5;

    public static final int OOO_TYPE_EXTERNAL_UNKNOWN = OOO_CONST_BASE + 6;

    public int type;

    public int state;

    public int enabled;

    public Date start;

    public Date end;

    public String msg;

//    private static final String TAG = "OoOData";

    public static final Parcelable.Creator<OoOData> CREATOR = new Parcelable.Creator<OoOData>() {
        public OoOData createFromParcel(Parcel in) {
            return new OoOData(in);
        }

        public OoOData[] newArray(int size) {
            return new OoOData[size];
        }
    };

    private OoOData(Parcel in) {
        readFromParcel(in);
    }

    public OoOData() {
    }

    // public OoOData (int atype, int astate, int aenabled, String amsg, Date
    // astart, Date aend) throws InvalidParameterException {
    public OoOData(int atype, int astate, int aenabled, String amsg, Date astart, Date aend) {
        type = atype;
        state = astate;
        enabled = aenabled;
        start = astart;
        end = aend;
        msg = amsg;
        /* Validate the arguments */
    }

    // public OoOData (int atype, int astate, int aenabled, String amsg, long
    // astart, long aend) throws InvalidParameterException {
    public OoOData(int atype, int astate, int aenabled, String amsg, long astart, long aend) {
        type = atype;
        state = astate;
        enabled = aenabled;
        start = new Date(astart);
        end = new Date(aend);
        msg = amsg;
        /* Validate the arguments */
    }

    // public OoOData (int atype, int astate, int aenabled, String amsg) throws
    // InvalidParameterException {
    public OoOData(int atype, int astate, int aenabled, String amsg) {
        type = atype;
        state = astate;
        enabled = aenabled;
        msg = amsg;
        start = null;
        end = null;

    }

    public long getStart() {
        if (start != null)
            return start.getTime();
        return 0;
    }

    public long getEnd() {
        if (end != null)
            return end.getTime();
        return 0;
    }

    public boolean isStartDST() {
        if (start != null) {
            TimeZone tz = TimeZone.getDefault();
            return tz.inDaylightTime(start);
        }
        return false;
    }

    public boolean isEndDST() {
        if (end != null) {
            TimeZone tz = TimeZone.getDefault();
            return tz.inDaylightTime(end);
        }
        return false;
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeInt(state);
        dest.writeInt(enabled);
        dest.writeString(msg);
        if (start != null)
            dest.writeLong(start.getTime());
        else
            dest.writeLong(0);
        if (end != null)
            dest.writeLong(end.getTime());
        else
            dest.writeLong(0);
    }

    public void readFromParcel(Parcel in) {
        type = in.readInt();
        state = in.readInt();
        enabled = in.readInt();
        msg = in.readString();
        long s = in.readLong();
        if (s != 0)
            start = new Date(s);
        else
            start = null;
        s = in.readLong();
        if (s != 0)
            end = new Date(s);
        else
            end = null;
    }
}
