package com.bishtlabs.cryptowatch.ui.base;

import android.arch.lifecycle.ViewModel;

import com.bishtlabs.cryptowatch.data.DataManager;

/**
 * Created by naren on 6/1/18.
 */

public abstract class BaseViewModel<N> extends ViewModel {
    private N mNavigator;
    private DataManager mDataManager;

    public BaseViewModel(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    public N getNavigator() {
        return mNavigator;
    }

    public void setNavigator(N mNavigator) {
        this.mNavigator = mNavigator;
    }
}
