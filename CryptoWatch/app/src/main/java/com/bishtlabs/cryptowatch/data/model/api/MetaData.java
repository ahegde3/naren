package com.bishtlabs.cryptowatch.data.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by naren on 6/10/18.
 */

public class MetaData {

    @SerializedName("timestamp")
    private long timeStamp;

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
