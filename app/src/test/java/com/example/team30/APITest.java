package com.example.team30;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.team30.models.API;
import com.example.team30.models.Friend;
import com.example.team30.models.Location;
import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class APITest {
    API api = API.provide();
    JsonObject json = new JsonObject();

    @Before
    public void setUp(){
        String privateCode = "team-30-test", label = "team-30-test";
        float latitude = 11, longitude = 15;
        json.addProperty("private_code", privateCode);
        json.addProperty("label", "Team 30");
        json.addProperty("latitude", latitude);
        json.addProperty("longitude", longitude);
    }
    @Test
    public void testPut(){
        boolean response = api.putLocation("team-30-test", json.toString());
        assertEquals(true, response);
    }

    @Test
    public void testGetPutFromServer(){

        Location location = api.getLocation(new Friend("pratham-20"));
        assertEquals("pratham-20", location.getPublic_code());
        assertEquals("Point Nemo", location.getLabel());
        assertEquals(-48.876667, location.getLatitude(), .01);
        assertEquals(-123.39333, location.getLongitude(), .01);
    }

    @Test
    public void testGetPutFromAPI(){
        Location location = api.getLocation(new Friend("team-30-test"));
        assertEquals("team-30-test", location.getPublic_code());
        assertEquals("Team 30", location.getLabel());
        assertEquals(11, location.getLatitude(), .01);
        assertEquals(15, location.getLongitude(), .01);
    }

    @Test
    public void testGetLocations(){
        List<Friend>friendList = new ArrayList<>();
        friendList.add(new Friend("pratham"));
        friendList.add(new Friend("team-30-test"));
        List<Location>locations = api.getMultipleLocations(friendList);
        assertEquals(2, locations.size());
        assertEquals("pratham", locations.get(0).getPublic_code());
        assertEquals("team-30-test", locations.get(1).getPublic_code());
    }

    @Test
    public void testPutThenPatchThenGet(){
        JsonObject newJson = new JsonObject();
        String privateCode = "team-30-test-patch", label = "team-30-test-patch";
        float latitude = 15, longitude = 11;
        newJson.addProperty("private_code", privateCode);
        newJson.addProperty("label", "Team 30");
        newJson.addProperty("latitude", latitude);
        newJson.addProperty("longitude", longitude);
        assertTrue(api.putLocation("team-30-test-patch", newJson.toString()));

        Location original = api.getLocation(new Friend("team-30-test-patch"));
        assertEquals(11, original.getLongitude(), .01);
        assertEquals(15, original.getLatitude(), .01);

        JsonObject patch = new JsonObject();
        patch.addProperty("private_code", privateCode);
        patch.addProperty("latitude", 38);
        patch.addProperty("longitude", 17);
        assertTrue(api.patchLocation("team-30-test-patch", patch.toString()));

        Location patched = api.getLocation(new Friend("team-30-test-patch"));
        assertEquals(17, patched.getLongitude(), .01);
        assertEquals(38, patched.getLatitude(), .01);
    }

}
