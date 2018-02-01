package com.example.nsbisht.userinfoviewer.data.local.db;

import com.example.nsbisht.userinfoviewer.data.local.db.database.AppDatabase;
import com.example.nsbisht.userinfoviewer.data.local.db.entity.UserInfo;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Created by nsbisht on 1/30/18.
 */

@Singleton
public class AppDBHelper implements DBHelper {

    private final AppDatabase mAppDatabase;

    @Inject
    public AppDBHelper(AppDatabase mAppDatabase) {
        this.mAppDatabase = mAppDatabase;
    }

    @Override
    public Observable<Boolean> insertUserInfo(final UserInfo... userInfos) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mAppDatabase.userInfoDao().insertUserInfo(userInfos);
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> deleteUserInfo(final UserInfo userInfo) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mAppDatabase.userInfoDao().deletedUserInfo(userInfo);
                return true;
            }
        });
    }

    @Override
    public Flowable<UserInfo> getUserById(int id) {
        return mAppDatabase.userInfoDao().getUserById(id);
    }

    @Override
    public Observable<List<UserInfo>> getAllUsers() {
        return Observable.fromCallable(new Callable<List<UserInfo>>() {
            @Override
            public List<UserInfo> call() throws Exception {
                return mAppDatabase.userInfoDao().getAllUsers();
            }
        });
    }

    @Override
    public Observable<Boolean> updateUserInfo(final UserInfo userInfo) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mAppDatabase.userInfoDao().updateUserInfo(userInfo);
                return true;
            }
        });
    }
}
