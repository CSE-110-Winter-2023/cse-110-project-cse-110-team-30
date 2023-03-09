//package com.example.team30;
//
//import org.junit.Test;
//import static org.junit.Assert.*;
//
//    public class CoordinateTest {
//        @Test
//        public void testGetLatitude() {
//            Coordinates coordinates1 = new Coordinates((float) 37.7749, (float) -122.4194);
//            Coordinates coordinates2 = new Coordinates((float) -37.7749, (float) -122.4194);
//            Coordinates coordinates3 = new Coordinates((float) 0, (float) 0);
//            Coordinates coordinates4 = new Coordinates((float) -37.7749, (float) 122.4194);
//            assertEquals(37.7749, coordinates1.getLatitude(), 0.001);
//            assertEquals(-37.7749, coordinates2.getLatitude(), 0.001);
//            assertEquals(0, coordinates3.getLatitude(), 0.001);
//            assertEquals(-37.7749, coordinates4.getLatitude(), 0.001);
//        }
//
//        @Test
//        public void testGetLongitude() {
//            Coordinates coordinates1 = new Coordinates((float) 37.7749, (float) -122.4194);
//            Coordinates coordinates2 = new Coordinates((float) -37.7749, (float) -122.4194);
//            Coordinates coordinates3 = new Coordinates((float) 0, (float) 0);
//            Coordinates coordinates4 = new Coordinates((float) -37.7749, (float) 122.4194);
//            assertEquals(-122.4194, coordinates1.getLongitude(), 0.001);
//            assertEquals(-122.4194, coordinates2.getLongitude(), 0.001);
//            assertEquals(0, coordinates3.getLongitude(), 0.001);
//            assertEquals(122.4194, coordinates4.getLongitude(), 0.001);
//        }
//
//        @Test
//        public void testSetLatitude() {
//            Coordinates coordinates = new Coordinates((float) 37.7749, (float) -122.4194);
//            coordinates.setLatitude((float) 47.6062);
//            assertEquals(47.6062, coordinates.getLatitude(), 0.001);
//            coordinates.setLatitude((float) 0);
//            assertEquals(0, coordinates.getLatitude(), 0.001);
//            coordinates.setLatitude((float) -47.6062);
//            assertEquals(-47.6062, coordinates.getLatitude(), 0.001);
//        }
//
//        @Test
//        public void testSetLongitude() {
//            Coordinates coordinates = new Coordinates((float) 37.7749, (float) -122.4194);
//            coordinates.setLongitude((float) -122.3321);
//            assertEquals(-122.3321, coordinates.getLongitude(), 0.001);
//            coordinates.setLongitude((float) 0);
//            assertEquals(0, coordinates.getLongitude(), 0.001);
//            coordinates.setLongitude((float) 122.3321);
//            assertEquals(122.3321, coordinates.getLongitude(), 0.001);
//        }
//    }


