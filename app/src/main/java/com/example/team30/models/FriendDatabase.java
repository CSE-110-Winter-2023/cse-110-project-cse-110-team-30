package com.example.team30.models;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {Friend.class}, version = 1, exportSchema = false)
public abstract class FriendDatabase extends RoomDatabase {
    private volatile static FriendDatabase singleton = null;
//    private volatile static FriendDatabase instance = null;

    public abstract FriendDao friendDao();


    public synchronized static FriendDatabase getSingleton(Context context) {
        if (singleton == null) {
            singleton = FriendDatabase.makeDatabase(context);
        }
        return singleton;
    }

    private static FriendDatabase makeDatabase(Context context) {
        return Room.databaseBuilder(context, FriendDatabase.class, "friend.db")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                    }
                })
                .build();
    }

    @VisibleForTesting
    public static void injectTestDatabase(FriendDatabase testDatabase) {
        if (singleton != null) {
            singleton.close();
        }
        singleton = testDatabase;
    }
}