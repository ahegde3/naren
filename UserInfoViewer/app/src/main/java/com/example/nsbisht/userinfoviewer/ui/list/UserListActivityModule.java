package com.example.nsbisht.userinfoviewer.ui.list;

import com.example.nsbisht.userinfoviewer.data.DataManager;
import com.example.nsbisht.userinfoviewer.data.local.db.entity.UserInfo;

import java.util.ArrayList;

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

    @Provides
    UserInfoAdapter provideUserInfoAdapter() {
        return new UserInfoAdapter(new ArrayList<UserInfo>());
    }
}
