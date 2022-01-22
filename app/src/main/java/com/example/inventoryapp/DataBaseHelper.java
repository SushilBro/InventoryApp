package com.example.inventoryapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "LoginForm";
    public static final int Version = 1;


    public DataBaseHelper(Context context) {

        super(context, DATABASE_NAME, null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table UserName(id integer primary key autoincrement,fullname text not null,phoneNumber text not null,email text not null,password text not null,confirmPassword text not null)");

        Log.d("---------", "table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists UserName");

        Log.d("---------", "table dropped");
    }
}
