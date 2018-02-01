package com.example.nsbisht.userinfoviewer.di.component;

import com.example.nsbisht.userinfoviewer.UserInfoApp;
import com.example.nsbisht.userinfoviewer.di.builder.ActivityBuilder;
import com.example.nsbisht.userinfoviewer.di.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Created by nsbisht on 1/31/18.
 */

@Singleton
@Component(modules = {AndroidInjectionModule.class, AppModule.class, ActivityBuilder.class})
public interface AppComponent {

    void inject(UserInfoApp app);

}
