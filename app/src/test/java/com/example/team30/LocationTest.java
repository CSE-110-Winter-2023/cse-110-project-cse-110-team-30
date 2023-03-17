package com.example.team30;

import static org.junit.Assert.*;

import com.example.team30.models.Location;

import org.junit.Test;


public class LocationTest {
    @Test
    public void testPublicode(){
        Location location = new Location("tianhao", "cth", (float) 100.0, (float) 99.0, "2022", "2023");
        assertEquals("tianhao", location.getPublic_code());
    }
    @Test
    public void testLabel(){
        Location location = new Location("tianhao", "cth", (float) 100.0, (float) 99.0, "2022", "2023");
        assertEquals("cth", location.getLabel());
    }
    @Test
    public void testLati(){
        Location location = new Location("tianhao", "cth", (float) 100.0, (float) 99.0, "2022", "2023");
        assertEquals( 100, location.getLatitude(),0.01);
    }
    @Test
    public void testLongti(){
        Location location = new Location("tianhao", "cth", (float) 100.0, (float) 99.0, "2022", "2023");
        assertEquals( 99.0, location.getLongitude(),0.01);
    }
    @Test
    public void testGetcreate(){
        Location location = new Location("tianhao", "cth", (float) 100.0, (float) 99.0, "2022", "2023");
        assertEquals( "2022", location.getCreated_at());
    }
    @Test
    public void testGetupdate(){
        Location location = new Location("tianhao", "cth", (float) 100.0, (float) 99.0, "2022", "2023");
        assertEquals( "2023", location.getUpdated_at());
    }

}
