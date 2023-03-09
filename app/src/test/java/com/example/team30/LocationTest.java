//package com.example.team30;
//import static org.junit.Assert.*;
//import org.junit.Test;
//
//public class LocationTest {
//    @Test
//    public void testGetName() {
//        Location location = new Location("Parent", "Golden Gate Bridge",
//                new Coordinates((float) 37.7749, (float) -122.4194));
//        assertEquals("Golden Gate Bridge", location.getName());
//    }
//
//    @Test
//    public void testGetType() {
//        Location location = new Location("Friend", "Golden Gate Bridge",
//                new Coordinates((float) 37.7749, (float) -122.4194));
//        assertEquals("Friend", location.getType());
//    }
//
//    @Test
//    public void testGetCoordinates() {
//        Coordinates coordinates = new Coordinates((float) 37.7749, (float) -122.4194);
//        Location location = new Location("Parent", "Mom's",
//                coordinates);
//        assertEquals(coordinates, location.getCoordinates());
//    }
//
//    @Test
//    public void testGetLatitude() {
//        Location location = new Location("Golden Gate Bridge", "Bridge",
//                new Coordinates((float) 37.7749, (float) -122.4194));
//        assertEquals(37.7749, location.getLatitude(), 0.001);
//    }
//
//    @Test
//    public void testGetLongitude() {
//        Location location = new Location("Golden Gate Bridge", "Bridge",
//                new Coordinates((float) 37.7749, (float) -122.4194));
//        assertEquals(-122.4194, location.getLongitude(), 0.001);
//
//        Location location1 = new Location("Golden Gate Bridge", "Bridge",
//                new Coordinates((float) 37.7749, (float) 122.4194));
//        assertEquals(122.4194, location1.getLongitude(), 0.001);
//
//        Location location2 = new Location("Golden Gate Bridge", "Bridge",
//                new Coordinates((float) 37.7749, (float) 0));
//        assertEquals(0, location2.getLongitude(), 0.001);
//    }
//
//    @Test
//    public void testSetCoordinates() {
//        Location location = new Location("Test location", "Type",
//                new Coordinates(0, 0));
//        location.setCoordinates(new Coordinates(1, 2));
//        assertEquals(1, location.getLatitude(), 0);
//        assertEquals(2, location.getLongitude(), 0);
//        location.setCoordinates(new Coordinates(1000, -20));
//        assertEquals(1000, location.getLatitude(), 0);
//        assertEquals(-20, location.getLongitude(), 0);
//    }
//}
