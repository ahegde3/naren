package com.bishtlabs.cryptowatch.ui.list;

import android.databinding.ObservableField;

import com.bishtlabs.cryptowatch.data.model.local.Coin;

/**
 * Created by nsbisht on 6/11/18.
 */

public class CoinsItemViewModel {
    public ObservableField<String> mName;
    public ObservableField<String> mTickerName;
    public ObservableField<Float> mPrice;

    public CoinsItemViewModel(Coin coin) {
        this.mName = new ObservableField<String>(coin.getName());
        this.mTickerName = new ObservableField<String>(coin.getTicker());
        this.mPrice = new ObservableField<Float>(coin.getPrice());

    }
}
