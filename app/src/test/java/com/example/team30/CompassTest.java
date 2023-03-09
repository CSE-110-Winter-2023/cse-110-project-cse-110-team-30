package com.example.team30;

import static org.junit.Assert.assertEquals;

import android.util.Pair;

import com.example.team30.models.Location;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CompassTest {

//        @Test
//        public void addLocationTest() {
//            Compass compass_test = new Compass();
//            Map<String, Pair<Float, Integer>> position = new HashMap<>();
//            Pair<Float, Integer> testob = new Pair<>(0.001f,2);
//
//            compass_test.setPosition("hantian", testob);
//
//
//            compass_test.addPosition("tianhao", new Pair<>((float)200,-110));
//
//
//            assertEquals(testob, compass_test.getPosition("hantian"));
//
//        }

        @Test
        public void setLocationTest() {
            Compass compass_test = new Compass();
            Location location = new Location("hantian1", "hantian",
                    2, 200, "2023-3-8", "2023-3-9");
            compass_test.setMyLat(-2);
            compass_test.setMyLong(-200);
            assertEquals(-2, compass_test.getMyLat(),0.01);
            assertEquals(-200, compass_test.getmyLong(),0.01);

        }


    }