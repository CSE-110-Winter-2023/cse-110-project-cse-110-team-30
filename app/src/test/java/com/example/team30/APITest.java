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
    }

    @Test
    public void testPut(){

    }
}
