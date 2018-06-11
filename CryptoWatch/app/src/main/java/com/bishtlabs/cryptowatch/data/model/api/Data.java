package com.bishtlabs.cryptowatch.data.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by naren on 6/10/18.
 */

public class Data {

    @SerializedName("data")
    private CoinData[] data;

    public CoinData[] getData() {
        return data;
    }

    public void setData(CoinData[] data) {
        this.data = data;
    }
}
