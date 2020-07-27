package com.example.portfoliomanagement.service;

import android.content.Context;

import androidx.room.Room;

import com.example.portfoliomanagement.dao.AppDatabase;

public class DBService {
    private static AppDatabase appDatabase;

    public static void init(Context context){
        appDatabase= Room.databaseBuilder(context,
                AppDatabase.class, "portfolio-management").build();
    }
    public static AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
