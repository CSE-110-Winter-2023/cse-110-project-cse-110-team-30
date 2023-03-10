//package com.example.team30;
//
//import android.content.Context;
//
//import androidx.room.Room;
//import androidx.test.core.app.ApplicationProvider;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import com.example.team30.models.API;
//import com.example.team30.models.FriendDao;
//import com.example.team30.models.FriendDatabase;
//import com.example.team30.models.Repository;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//@RunWith(AndroidJUnit4.class)
//public class ClearDatabase {
//    FriendDatabase db;
//    @Before
//    public void createDb() {
//        Context context = ApplicationProvider.getApplicationContext();
//        db = Room.inMemoryDatabaseBuilder(context, FriendDatabase.class)
//                .allowMainThreadQueries()
//                .build();
//        FriendDao dao = db.friendDao();
//    }
//    @Test
//    public void clearDb() {
//        db.clearAllTables();
//    }
//}
