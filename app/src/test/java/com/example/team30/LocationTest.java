package com.example.team30;
import static org.junit.Assert.*;

import com.example.team30.models.Location;

import org.junit.Test;

public class LocationTest {

    @Test
    public void Test1(){
        Location location = new Location("UTest", "LTest", (float) 20.1, 100.5f,
                "2022-2-1", "2023-2-2");
        assertEquals("2023-2-2", location.getUpdated_at());
        assertEquals("UTest", location.getPublic_code());
        assertEquals("LTest", location.getLabel());
        assertEquals(20.1f, location.getLatitude(), 0.001);
        assertEquals(100.5f, location.getLongitude(), 0.001);
    }
    @Test
    public void Test2(){
        Location location = new Location("Uxxxxx", "213884", 0, 0,
                "2021-2-1", "2021-2-2");
        assertEquals("2021-2-2", location.getUpdated_at());
        assertEquals("Uxxxxx", location.getPublic_code());
        assertEquals("213884", location.getLabel());
        assertEquals(0, location.getLatitude(), 0.001);
        assertEquals(0, location.getLongitude(), 0.001);
    }
    @Test
    public void setUpdated_atTest(){
        Location location = new Location("UTest", "LTest", 20, 100,
                "2022-2-1", "2023-2-2");
        assertEquals("2023-2-2", location.getUpdated_at());
        location.setUpdated_at("2023-3-5");
        assertEquals("2023-3-5", location.getUpdated_at());
    }

}
