package com.example.nsbisht.userinfoviewer.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.nsbisht.userinfoviewer.BR;
import com.example.nsbisht.userinfoviewer.R;
import com.example.nsbisht.userinfoviewer.base.BaseActivity;
import com.example.nsbisht.userinfoviewer.databinding.AddUserInfoBinding;

import javax.inject.Inject;

/**
 * Created by nsbisht on 2/2/18.
 */

public class UserInfoActivity extends BaseActivity<AddUserInfoBinding, UserAddViewModel> implements AddUserNavigator {

    @Inject
    UserAddViewModel mUserAddViewModel;

    AddUserInfoBinding mAddUserInfoBinding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAddUserInfoBinding = getViewDataBinding();
        mUserAddViewModel.setNavigator(this);
    }

    @Override
    public UserAddViewModel getViewModel() {
        return mUserAddViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.add_user_info;
    }
}
