package com.example.nsbisht.userinfoviewer;

import android.app.Application;

import com.example.nsbisht.userinfoviewer.di.component.DaggerAppComponent;

/**
 * Created by nsbisht on 1/31/18.
 */

public class UserInfoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerAppComponent.builder()
                .build()
                .inject(this);
    }
}
