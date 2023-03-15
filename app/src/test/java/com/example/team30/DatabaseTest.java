package com.example.team30;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.team30.models.Friend;
import com.example.team30.models.FriendDao;
import com.example.team30.models.FriendDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private FriendDao dao;
    private FriendDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, FriendDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.friendDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testInsert() {
        Friend item1  = new Friend("unit-test-1");
        Friend item2  = new Friend("unit-test-2");

        long id1 = dao.insert(item1);
        long id2 = dao.insert(item2);
        Friend f = dao.get("unit-test-1");
        System.out.println(f.getUID());
        assertNotEquals(id1, id2);
    }

    @Test
    public void testGet(){
        Friend item1  = new Friend("unit-test-get");
        long id1 = dao.insert(item1);
        Friend friend = dao.get("unit-test-get");
        assertEquals("unit-test-get", friend.getUID());
    }

}
