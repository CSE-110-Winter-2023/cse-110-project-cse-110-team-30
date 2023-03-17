package com.example.team30;

import static org.junit.Assert.assertEquals;

import androidx.core.util.Pair;

import com.example.team30.DataCalculators.Compass;

import org.junit.Test;

public class CompassTest {
    public Compass compass = Compass.singleton();

    @Test
    public void testSetCoords(){
        Pair<Double, Double> coords = new Pair<>(11.5, 31.6);
        compass.setCoords(coords);
        assertEquals(11.5, compass.getMyLat(), .001);
        assertEquals(31.6, compass.getMyLong(), .001);
    }

    @Test
    public void testZoom0Radius(){
        assertEquals(20, compass.zoom0radius(.5), .01);
        assertEquals(82.376, compass.zoom0radius(39.11), .01);
    }

    @Test
    public void testZoom1Radius(){
        assertEquals(26.65, compass.zoom1radius(.5), .01);
        assertEquals(109.77, compass.zoom1radius(39.11), .01);
    }

    @Test
    public void testZoom2Radius(){
        assertEquals(40, compass.zoom2radius(.5), .01);
        assertEquals(160, compass.zoom2radius(39.11), .01);
    }
}
