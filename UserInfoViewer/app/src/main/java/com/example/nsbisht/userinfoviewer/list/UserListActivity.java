package com.example.nsbisht.userinfoviewer.list;

import android.os.Bundle;

import com.example.nsbisht.userinfoviewer.BR;
import com.example.nsbisht.userinfoviewer.R;
import com.example.nsbisht.userinfoviewer.base.BaseActivity;
import com.example.nsbisht.userinfoviewer.databinding.ActivityMainBinding;

import javax.inject.Inject;

public class UserListActivity extends BaseActivity<ActivityMainBinding, UserViewModel> implements UserNavigator {

    @Inject
    UserViewModel mUserViewModel;

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

    }

}
