package com.example.nsbisht.userinfoviewer.ui.user;

import com.example.nsbisht.userinfoviewer.data.DataManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nsbisht on 2/2/18.
 */

@Module
public class UserInfoModule {

    @Provides
    UserAddViewModel provideUserAddViewModule(DataManager dataManager) {
        return new UserAddViewModel(dataManager);
    }
}
