package com.example.nsbisht.userinfoviewer.data.local.db.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.nsbisht.userinfoviewer.data.local.db.dao.UserInfoDao;
import com.example.nsbisht.userinfoviewer.data.local.db.entity.UserInfo;

/**
 * Created by nsbisht on 1/30/18.
 */

@Database(entities = {UserInfo.class}, version = AppDatabase.VERSION, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    static final int VERSION = 1;

    public abstract UserInfoDao userInfoDao();

}
