package com.kodingnext.passwordkeeper.sqlite;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

//Adding entity for store and get the database attribute
//set table name "password"
@Entity(tableName = "password")
public class ModelPassword {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    int id;
    String name;
    String password;
    String emailoruser;

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String nama) {
        this.name = nama;
    }

    public String getEmailoruser() {
        return emailoruser;
    }

    public void setEmailoruser(String emailoruser) {
        this.emailoruser = emailoruser;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

