package com.bishtlabs.cryptowatch.data.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by naren on 6/10/18.
 */

public class TickerResponse {
    /**
     * Sample response
     {
     "data": {
     "1": {
     "id": 1,
     "name": "Bitcoin",
     "symbol": "BTC",
     "website_slug": "bitcoin",
     "rank": 1,
     "circulating_supply": 17008162.0,
     "total_supply": 17008162.0,
     "max_supply": 21000000.0,
     "quotes": {
     "USD": {
     "price": 9024.09,
     "volume_24h": 8765400000.0,
     "market_cap": 153483184623.0,
     "percent_change_1h": -2.31,
     "percent_change_24h": -4.18,
     "percent_change_7d": -0.47
     }
     },
     "last_updated": 1525137271
     },
     "1027": {
     "id": 1027,
     "name": "Ethereum",
     "symbol": "ETH",
     "website_slug": "ethereum",
     "rank": 2,
     "circulating_supply": 99151888.0,
     "total_supply": 99151888.0,
     "max_supply": null,
     "quotes": {
     "USD": {
     "price": 642.399,
     "volume_24h": 2871290000.0,
     "market_cap": 63695073558.0,
     "percent_change_1h": -3.75,
     "percent_change_24h": -7.01,
     "percent_change_7d": -2.32
     }
     },
     "last_updated": 1525137260
     }
     ...
     },
     "metadata": {
     "timestamp": 1525137187,
     "num_cryptocurrencies": 1602,
     "error": null
     }
     ]
     */

    @SerializedName("data")
    private Data data;

    @SerializedName("metadata")
    private MetaData metaData;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }
}
