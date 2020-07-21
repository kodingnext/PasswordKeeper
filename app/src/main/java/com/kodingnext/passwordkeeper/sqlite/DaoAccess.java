package com.kodingnext.passwordkeeper.sqlite;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// defining the function that access the database. All queries are written in DAO only
@Dao
public interface DaoAccess {
    //for SQLite part 1 (Create and Read)
    @Insert
    void createPassword(ModelPassword data);
    @Query("SELECT * FROM password")
    List<ModelPassword> selectAllPassword();

    //for SQLite part 2 (Delete, Update, Search)
    @Delete
    void deletePassword(ModelPassword data);
    @Update
    void updatePassword(ModelPassword data);
    @Query ("SELECT * FROM password WHERE name LIKE '%' || :name || '%' ")
    List<ModelPassword> searchPassword(String name);
}
