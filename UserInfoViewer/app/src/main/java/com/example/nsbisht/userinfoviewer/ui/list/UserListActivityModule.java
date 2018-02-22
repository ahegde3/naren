package com.example.nsbisht.userinfoviewer.ui.list;

import android.arch.lifecycle.ViewModelProvider;
import android.support.v7.widget.LinearLayoutManager;

import com.example.nsbisht.userinfoviewer.ViewModelProviderFactory;
import com.example.nsbisht.userinfoviewer.data.DataManager;
import com.example.nsbisht.userinfoviewer.data.remote.UserInfo;

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

    @Provides
    LinearLayoutManager provideLinearLayoutManager(UserListActivity activity) {
        return new LinearLayoutManager(activity);
    }

    @Provides
    ViewModelProvider.Factory provideBlogViewModel(UserViewModel userViewModel) {
        return new ViewModelProviderFactory<>(userViewModel);
    }
}
