package com.bishtlabs.cryptowatch.utils;

import android.databinding.BindingAdapter;
import android.widget.TextView;

/**
 * Created by nsbisht on 6/11/18.
 */

public class BindingUtils {

    @BindingAdapter("coin_price")
    public static void setPriceField(TextView textView, float price) {
        textView.setText("$" + price);
    }
}
