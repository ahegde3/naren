package com.example.nsbisht.userinfoviewer.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.example.nsbisht.userinfoviewer.data.AppDataManager;
import com.example.nsbisht.userinfoviewer.data.DataManager;
import com.example.nsbisht.userinfoviewer.data.local.db.AppDBHelper;
import com.example.nsbisht.userinfoviewer.data.local.db.DBHelper;
import com.example.nsbisht.userinfoviewer.data.local.db.database.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nsbisht on 1/30/18.
 */

@Module
public class AppModule {

    private final Application mApplication;

    public AppModule(Application mApplication) {
        this.mApplication = mApplication;
    }


    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    AppDatabase provideRoomDatabase() {
        return Room.databaseBuilder(mApplication, AppDatabase.class, "UserInfo.db")
                .build();
    }

    @Provides
    @Singleton
    DBHelper provideDbHelper(AppDBHelper dbHelper) {
        return dbHelper;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager dataManager) {
        return dataManager;
    }


}
