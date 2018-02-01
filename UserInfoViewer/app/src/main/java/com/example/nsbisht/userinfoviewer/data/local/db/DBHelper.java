package com.example.nsbisht.userinfoviewer.data.local.db;

import com.example.nsbisht.userinfoviewer.data.local.db.entity.UserInfo;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Created by nsbisht on 1/30/18.
 */

public interface DBHelper {

    Observable<Boolean> insertUserInfo(UserInfo... userInfos);

    Observable<Boolean> deleteUserInfo(UserInfo userInfo);

    Flowable<UserInfo> getUserById(int id);

    Observable<List<UserInfo>> getAllUsers();

    Observable<Boolean> updateUserInfo(UserInfo userInfo);
}
