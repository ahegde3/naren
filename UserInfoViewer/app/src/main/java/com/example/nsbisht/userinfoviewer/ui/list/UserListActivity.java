package com.example.nsbisht.userinfoviewer.ui.list;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;

import com.example.nsbisht.userinfoviewer.BR;
import com.example.nsbisht.userinfoviewer.R;
import com.example.nsbisht.userinfoviewer.base.BaseActivity;
import com.example.nsbisht.userinfoviewer.data.local.db.entity.UserInfo;
import com.example.nsbisht.userinfoviewer.databinding.ActivityMainBinding;
import com.example.nsbisht.userinfoviewer.ui.user.UserInfoActivity;

import java.util.List;

import javax.inject.Inject;

public class UserListActivity extends BaseActivity<ActivityMainBinding, UserViewModel> implements UserNavigator {

    @Inject
    UserViewModel mUserViewModel;

    @Inject
    UserInfoAdapter mAdapter;

    ActivityMainBinding mActivityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = getViewDataBinding();
        mUserViewModel.setNavigator(this);
    }

    @Override
    public UserViewModel getViewModel() {
        return mUserViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    public void openDetailView() {
        Intent intent = new Intent();
        intent.setClass(this, UserInfoActivity.class);
        startActivity(intent);
        finish();
    }

    private void setUp() {
        mActivityMainBinding.userInfoListView.recycleView.setItemAnimator(new DefaultItemAnimator());
        mActivityMainBinding.userInfoListView.recycleView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUp();
        subscribeToLiveData();
    }

    private void subscribeToLiveData() {
        mUserViewModel.getUserInfoListLiveData().observe(this, new Observer<List<UserInfo>>() {
            @Override
            public void onChanged(@Nullable List<UserInfo> userInfo) {
                mUserViewModel.addUserInfoItemsToList(userInfo);
            }
        });
    }
}
