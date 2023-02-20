package com.example.team30;

import java.util.HashMap;
import java.util.Map;
import java.lang.Math;

public class Compass {
    private Map<String, Location> locations;
    private Coordinates myCoords;

    private static Compass instance;

    public Compass(){
        locations = new HashMap<>();
        myCoords = new Coordinates(0,0);
    }

    public static Compass singleton(){
        if(instance == null){
            instance = new Compass();
        }
        return instance;
    }
    public double calculateAngle(String type, Coordinates coords, float orientation){
        Location l = locations.get(type);
        if(coords != null)
            myCoords = coords;
        double y = l.getLongitude() - myCoords.getLongitude();
        double x = l.getLatitude() - myCoords.getLatitude();
        double angle = Math.atan(y/x) * 180/Math.PI;
        if(x < 0){
            angle = angle + 180;
        }
        if(x > 0 && y < 0){
            angle = angle + 360;
        }
        orientation *= 180/Math.PI;
        return angle - orientation;
    }
    public double calculateAngleWithDistance(String type, Coordinates coords, float rotate) {
        return calculateAngleWithDistance(type, coords) - rotate;
    }


    public boolean hasLocation(String type){
        return locations.containsKey(type);
    }
    public void addLocation(Location l){
        locations.put(l.getType(), l);
    }

    public void updateLocations(){}
}
