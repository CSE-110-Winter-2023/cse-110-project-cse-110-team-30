package com.example.team30;

import static org.junit.Assert.assertEquals;

import com.example.team30.models.API;
import com.example.team30.models.Friend;
import com.example.team30.models.Location;

import org.junit.Before;
import org.junit.Test;

public class APITest {
    API api = API.provide();

    @Test
    public void testGet(){
        Friend friend = new Friend("pratham");
        Location location = api.getLocation(friend);
        assertEquals(location.getPublic_code(), "pratham");

        Friend friend1 = new Friend("hantian1");
        Location location1 = api.getLocation(friend1);
        assertEquals(location1.getPublic_code(), "hantian1");

    }

    @Test
    public void testPut() {
        Friend friend2 = new Friend("hantian2");
        Location location2 = new Location(friend2.getUID(), "hantian", 0, 0, "2023", "2023");

        // Call the API to update the location
        api.putLocation(friend2.getUID(), location2.toJSON());

        // Retrieve the updated location using another API call or by querying the database
        Location updatedLocation = api.getLocation(friend2);

        // Check the properties of the updated location
        assertEquals(updatedLocation.getPublic_code(), "hantian2");
        assertEquals(updatedLocation.getLatitude(), 0, 0.01);
        assertEquals(updatedLocation.getLongitude(), 0, 0.01);
        assertEquals(updatedLocation.getLabel(), "hantian");
        // ... and so on
    }

}
