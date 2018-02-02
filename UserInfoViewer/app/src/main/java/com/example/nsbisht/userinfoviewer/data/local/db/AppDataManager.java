package com.example.nsbisht.userinfoviewer.data.local.db;

import android.content.Context;

import com.example.nsbisht.userinfoviewer.data.local.db.entity.UserInfo;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Created by nsbisht on 1/31/18.
 */

@Singleton
public class AppDataManager implements DataManager {

    private final DBHelper mDBHelper;

    @Inject
    public AppDataManager(DBHelper mDBHelper) {
        this.mDBHelper = mDBHelper;
    }


    @Override
    public Observable<Boolean> insertUserInfo(UserInfo... userInfos) {
        return mDBHelper.insertUserInfo(userInfos);
    }

    @Override
    public Observable<Boolean> deleteUserInfo(UserInfo userInfo) {
        return mDBHelper.deleteUserInfo(userInfo);
    }

    @Override
    public Flowable<UserInfo> getUserById(int id) {
        return mDBHelper.getUserById(id);
    }

    @Override
    public Observable<List<UserInfo>> getAllUsers() {
        return mDBHelper.getAllUsers();
    }

    @Override
    public Observable<Boolean> updateUserInfo(UserInfo userInfo) {
        return updateUserInfo(userInfo);
    }
}
