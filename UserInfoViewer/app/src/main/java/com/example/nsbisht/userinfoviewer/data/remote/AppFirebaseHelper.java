package com.example.nsbisht.userinfoviewer.data.remote;

import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

/**
 * Created by naren on 2/4/18.
 */

public class AppFirebaseHelper implements FirebaseHelper {

    private FirebaseDatabase mFirebaseInstance;


    @Override
    public Single<UserInfo> getUserIfo(String user) {
        mFirebaseInstance.getReference(user).child("firstName");
        return null;
    }
}
