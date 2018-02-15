package com.example.nsbisht.userinfoviewer.data.remote;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by naren on 2/4/18.
 */

public class AppFirebaseHelper implements FirebaseHelper {

    private FirebaseDatabase mFirebaseDatabase;

    @Inject
    public AppFirebaseHelper() {
        this.mFirebaseDatabase = FirebaseDatabase.getInstance();
    }


    @Override
    public Single<UserInfo> getUserInfo(String user) {
        mFirebaseDatabase.getReference(user).child("firstName");
        return null;
    }

    @Override
    public Single<Void> createUserInfo(UserInfo userInfo) {
        String userId = mFirebaseDatabase.getReference().push().getKey();
        mFirebaseDatabase.getReference().child(userId).setValue(userInfo);

        return null;
    }

    @Override
    public PublishSubject<UserInfo> valueEventListener() {
        final PublishSubject<UserInfo> latestUserInfo = PublishSubject.create();

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                latestUserInfo.onNext(userInfo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                latestUserInfo.onError(new Throwable(databaseError.getMessage()));
            }
        };

        mFirebaseDatabase.getReference().addValueEventListener(listener);
        return latestUserInfo;
    }
}
