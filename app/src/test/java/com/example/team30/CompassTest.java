package com.example.team30;
import static org.junit.Assert.*;
import org.junit.Test;

    public class CompassTest {
        @Test
        public void hasLocation(){
            Compass compass_test = new Compass();
            assertEquals(false,compass_test.hasLocation("Parent"));
        }
        @Test
        public void addLocationTest(){
            Compass compass_test = new Compass();
            Location location = new Location("Parent", "Golden Gate Bridge",
                    new Coordinates((float) 37.7749, (float) -122.4194));
            compass_test.addLocation(location);
            assertEquals( true ,compass_test.hasLocation("Parent"));
            assertEquals( false ,compass_test.hasLocation("Friend"));
        }
        @Test
        public void calculateAngleWithDistanceTest(){
            Compass compass_test = new Compass();
            Location locationParent = new Location("Parent", "Golden Gate Bridge",
                    new Coordinates((float) 40, (float) -100));
            Location locationFriend = new Location("Friend", "Golden Gate Bridge",
                    new Coordinates((float) -40, (float) 100));
            Location locationHome = new Location("Home", "Golden Gate Bridge",
                    new Coordinates((float) -40, (float) -200));
            compass_test.addLocation(locationParent);
            compass_test.addLocation(locationFriend);
            compass_test.addLocation(locationHome);

            Coordinates coordinates = new Coordinates((float) 37.7749, (float) -122.4194);
            assertEquals(84.332020791674,compass_test.calculateAngle("Parent", coordinates, 0),0.01);
            assertEquals(109.27354652632,compass_test.calculateAngle("Friend", coordinates, 0),0.01);
            assertEquals(224.92834135802,compass_test.calculateAngle("Home", coordinates, 0),0.01);
        }
        @Test
        public void calculateAngleWithDistanceEdgeTest(){
            Compass compass_test = new Compass();
            Location locationParent = new Location("Parent", "Golden Gate Bridge",
                    new Coordinates((float) 40, (float) -200));
            Location locationFriend = new Location("Friend", "Golden Gate Bridge",
                    new Coordinates((float) 0, (float) 0));
//            Location locationHome = new Location("Home", "Golden Gate Bridge",
//                    new Coordinates((float) -40, (float) -200));
            compass_test.addLocation(locationParent);
            compass_test.addLocation(locationFriend);
//            compass_test.addLocation(locationHome);

            Coordinates coordinates = new Coordinates((float) 37.7749, (float) -122.4194);
            assertEquals(271.64285785969,compass_test.calculateAngle("Parent", coordinates, 0),0.01);
            assertEquals(107.1486332690,compass_test.calculateAngle("Friend", coordinates,0 ),0.01);
//            assertEquals(224.92834135802,compass_test.calculateAngleWithDistance("Home", coordinates),0.01);
        }
        @Test
        public void singletonTest(){
            Compass compass_test = new Compass();
    //        @Todo, not Understand the function
        }


}
