package com.example.nsbisht.userinfoviewer.ui.list;

import com.example.nsbisht.userinfoviewer.data.local.db.DataManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nsbisht on 1/31/18.
 */

@Module
public class UserListActivityModule {

    @Provides
    UserViewModel provideUserViewModel(DataManager mDataManager) {
        return new UserViewModel(mDataManager);
    }
}
