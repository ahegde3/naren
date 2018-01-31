package com.example.nsbisht.userinfoviewer.di.builder;


import com.example.nsbisht.userinfoviewer.ui.list.UserListActivity;
import com.example.nsbisht.userinfoviewer.ui.list.UserListActivityModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by nsbisht on 1/31/18.
 */

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = UserListActivityModule.class)
    abstract UserListActivity bindUserListActivity();
}
