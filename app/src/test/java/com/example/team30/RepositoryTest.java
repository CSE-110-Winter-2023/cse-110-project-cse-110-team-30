package com.example.team30;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.team30.models.API;
import com.example.team30.models.Friend;
import com.example.team30.models.FriendDao;
import com.example.team30.models.FriendDatabase;
import com.example.team30.models.Location;
import com.example.team30.models.Repository;
import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RepositoryTest {
    API api = API.provide();
    String PRIVATE_CODE = "team-30-repo";
    float LATITUDE =  11, LONGITUDE = 17;
    Repository repo;
    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        FriendDatabase db = Room.inMemoryDatabaseBuilder(context, FriendDatabase.class)
                .allowMainThreadQueries()
                .build();
        FriendDao dao = db.friendDao();
        repo = new Repository(api, dao);
    }


    @Test
    public void testInsertLocation(){
        assertTrue(repo.insertUserLocation("team-30-repo", PRIVATE_CODE, LATITUDE, LONGITUDE));
    }

    @Test
    public void testGetSingleLocation(){
        Location location = repo.getSingleLocation(new Friend("pratham-20"));
        assertEquals("pratham-20", location.getPublic_code());
        assertEquals("Point Nemo", location.getLabel());
        assertEquals(-48.876667, location.getLatitude(), .01);
        assertEquals(-123.39333, location.getLongitude(), .01);
    }

    @Test
    public void testUpdateThenGetLocation(){
        assertTrue(repo.insertUserLocation("team-30-repo", PRIVATE_CODE, 32, 49));
        Location location = repo.getSingleLocation(new Friend("team-30-repo"));
        assertEquals("team-30-repo", location.getPublic_code());
        assertEquals("Team 30", location.getLabel());
        assertEquals(32, location.getLatitude(), .01);
        assertEquals(49, location.getLongitude(), .01);
    }
}
