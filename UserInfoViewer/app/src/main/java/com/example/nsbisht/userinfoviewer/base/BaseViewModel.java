package com.example.nsbisht.userinfoviewer.base;

import android.arch.lifecycle.ViewModel;

import com.example.nsbisht.userinfoviewer.data.DataManager;

/**
 * Created by nsbisht on 1/30/18.
 */

public class BaseViewModel <N> extends ViewModel {

    private N mNavigator;
    private final DataManager mDataManager;


    public BaseViewModel(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    public N getNavigator() {
        return mNavigator;
    }

    public void setNavigator(N mNavigator) {
        this.mNavigator = mNavigator;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

}
