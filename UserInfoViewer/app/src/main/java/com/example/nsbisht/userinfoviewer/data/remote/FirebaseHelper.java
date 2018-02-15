package com.example.nsbisht.userinfoviewer.data.remote;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by naren on 2/4/18.
 */

public interface FirebaseHelper {

    Single<UserInfo> getUserInfo(String user);

    Single<Void> createUserInfo(UserInfo userInfo);

    PublishSubject<List<UserInfo>> valueEventListener();

}
