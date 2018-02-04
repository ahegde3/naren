package com.example.nsbisht.userinfoviewer.ui.list;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableArrayList;

import com.example.nsbisht.userinfoviewer.base.BaseViewModel;
import com.example.nsbisht.userinfoviewer.data.local.db.DataManager;
import com.example.nsbisht.userinfoviewer.data.local.db.entity.UserInfo;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nsbisht on 1/31/18.
 */

public class UserViewModel extends BaseViewModel<UserNavigator> {

    private final ObservableArrayList<UserInfo> userInfoObservableArrayList = new ObservableArrayList<>();
    private final MutableLiveData<List<UserInfo>> userInfoListLiveData;



    public UserViewModel(DataManager mDataManager) {
        super(mDataManager);
        userInfoListLiveData = new MutableLiveData<>();
        fetchUserInfoList();
    }

    public void onFabButtonClicked() {
        getNavigator().openDetailView();
    }

    public MutableLiveData<List<UserInfo>> getUserInfoListLiveData() {
        return userInfoListLiveData;
    }

    public void addUserInfoItemsToList(List<UserInfo> blogs) {
        userInfoObservableArrayList.clear();
        userInfoObservableArrayList.addAll(blogs);
    }

    public ObservableArrayList<UserInfo> getUserInfoObservableArrayList() {
        return userInfoObservableArrayList;
    }

    public void fetchUserInfoList() {
        getDataManager()
                .getAllUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<UserInfo>>() {
                    @Override
                    public void accept(List<UserInfo> userInfos) throws Exception {
                        if (userInfos != null) {
                            userInfoListLiveData.setValue(userInfos);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }
}
