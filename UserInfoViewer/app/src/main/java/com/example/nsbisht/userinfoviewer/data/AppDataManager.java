package com.example.nsbisht.userinfoviewer.data;

import com.example.nsbisht.userinfoviewer.data.local.db.DBHelper;
import com.example.nsbisht.userinfoviewer.data.local.db.entity.UserInfo;
import com.example.nsbisht.userinfoviewer.data.remote.FirebaseHelper;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by nsbisht on 1/31/18.
 */

@Singleton
public class AppDataManager implements DataManager {

    private final DBHelper mDBHelper;
    private final FirebaseHelper mFirebaseHelper;

    @Inject
    public AppDataManager(DBHelper mDBHelper, FirebaseHelper mFirebaseHelper) {
        this.mDBHelper = mDBHelper;
        this.mFirebaseHelper = mFirebaseHelper;
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

    @Override
    public Single<com.example.nsbisht.userinfoviewer.data.remote.UserInfo> getUserInfo(String user) {
        return mFirebaseHelper.getUserInfo(user);
    }

    @Override
    public Single<Void> createUserInfo(com.example.nsbisht.userinfoviewer.data.remote.UserInfo userInfo) {
        return mFirebaseHelper.createUserInfo(userInfo);
    }

    @Override
    public PublishSubject<com.example.nsbisht.userinfoviewer.data.remote.UserInfo> valueEventListener() {
        return mFirebaseHelper.valueEventListener();
    }
}
