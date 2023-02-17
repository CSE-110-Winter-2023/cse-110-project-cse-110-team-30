package com.example.team30;

import org.junit.Test;
import static org.junit.Assert.*;

    public class CoordinateTest {
        @Test
        public void testGetLatitude() {
            Coordinates coordinates = new Coordinates((float) 37.7749, (float) -122.4194);
            assertEquals(37.7749, coordinates.getLatitude(), 0.001);
        }

        @Test
        public void testGetLongitude() {
            Coordinates coordinates = new Coordinates((float) 37.7749, (float) -122.4194);
            assertEquals(-122.4194, coordinates.getLongitude(), 0.001);
        }

        @Test
        public void testSetLatitude() {
            Coordinates coordinates = new Coordinates((float) 37.7749, (float) -122.4194);
            coordinates.setLatitude((float) 47.6062);
            assertEquals(47.6062, coordinates.getLatitude(), 0.001);
        }

        @Test
        public void testSetLongitude() {
            Coordinates coordinates = new Coordinates((float) 37.7749, (float) -122.4194);
            coordinates.setLongitude((float) -122.3321);
            assertEquals(-122.3321, coordinates.getLongitude(), 0.001);
        }
    }


