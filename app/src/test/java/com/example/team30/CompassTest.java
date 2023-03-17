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
}
