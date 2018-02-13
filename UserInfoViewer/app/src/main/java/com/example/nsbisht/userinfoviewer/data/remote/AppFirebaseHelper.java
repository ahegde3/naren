package com.example.nsbisht.userinfoviewer.data.remote;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Cancellable;

/**
 * Created by naren on 2/4/18.
 */

public class AppFirebaseHelper implements FirebaseHelper {

    private FirebaseDatabase mFirebaseDatabase;


    @Inject
    public AppFirebaseHelper(FirebaseDatabase firebaseDatabase) {
        this.mFirebaseDatabase = firebaseDatabase;
    }


    @Override
    public Single<UserInfo> getUserInfo(String user) {
        mFirebaseDatabase.getReference(user).child("firstName");
        return null;
    }

    @Override
    public Single<Void> createUserInfo(UserInfo userInfo, String userId) {
        mFirebaseDatabase.getReference().child("users").child(userId).setValue(userInfo);

        return null;
    }

    @Override
    public Observable<UserInfo> valueEventListener() {
        return Observable.fromEmitter(new Action1<Emitter<UserInfo>>() {
            @Override
            public void call(final Emitter<UserInfo> userInfoEmitter) {
                final ValueEventListener listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                        userInfoEmitter.onNext(userInfo);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        userInfoEmitter.onError(new Throwable(databaseError.getMessage()));
                        mFirebaseDatabase.getReference().removeEventListener(this);
                    }
                };
                mFirebaseDatabase.getReference().addValueEventListener(listener);
                userInfoEmitter.setCancellation(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        mFirebaseDatabase.getReference().removeEventListener(listener);
                    }
                });
            }
        }, Emitter.BackpressureMode.BUFFER);
    }
}
