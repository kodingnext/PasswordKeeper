package com.kodingnext.passwordkeeper.sqlite;

import androidx.room.Database;
import androidx.room.RoomDatabase;

//Setup the database
@Database(entities = {ModelPassword.class}, version = 1, exportSchema = false)
public abstract class RepositoryPassword extends RoomDatabase {
    public abstract DaoAccess daoAccess();
}
