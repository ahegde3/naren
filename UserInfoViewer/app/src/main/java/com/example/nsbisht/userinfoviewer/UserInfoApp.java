package com.example.nsbisht.userinfoviewer;

import android.app.Activity;
import android.app.Application;

import com.example.nsbisht.userinfoviewer.di.component.DaggerAppComponent;
import com.example.nsbisht.userinfoviewer.di.module.AppModule;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * Created by nsbisht on 1/31/18.
 */

public class UserInfoApp extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build()
                .inject(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }
}
