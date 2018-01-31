package com.example.nsbisht.userinfoviewer.data.local.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.nsbisht.userinfoviewer.data.local.db.entity.UserInfo;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by nsbisht on 1/30/18.
 */

@Dao
public interface UserInfoDao {

    @Insert
    void insertUserInfo(UserInfo... users);

    @Delete
    void deletedUserInfo(UserInfo userInfo);

    @Query("SELECT * FROM user_info")
    List<UserInfo> getAllUsers();

    @Update
    void updateUserInfo(UserInfo userInfo);

    @Query("SELECT * FROM user_info WHERE id=:userId")
    Flowable<UserInfo> getUserById(int userId);
}
