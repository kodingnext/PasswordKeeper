package com.kodingnext.passwordkeeper.sqlite;

import android.app.Application;
import androidx.room.Room;
import android.content.Context;

import com.facebook.stetho.Stetho;

public class AppController extends Application {

    //Adding stetho for background data tracker
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }

    //Adding database helper to get the instance of database quickly
    public static RepositoryPassword getInstance(String databaseName, Context context){
        RepositoryPassword databasePassword = Room.databaseBuilder(context, RepositoryPassword.class, databaseName)
                .allowMainThreadQueries()
                .build();

        return databasePassword;
    }
}
