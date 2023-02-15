package com.example.team30;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Math;

public class Compass {
    private Map<String, Location> locations;
    private Coordinates myCoords;

    private static Compass instance;

    public Compass(){
        locations = new HashMap<>();
    }

    public static Compass singleton(){
        if(instance == null){
            instance = new Compass();
        }
        return instance;
    }
    public double calculateAngleWithDistance(String type, Coordinates coords){
        Location l = locations.get(type);
        if (l == null) {
            return 360;
        }
        myCoords = coords;
        double y = l.getLongitude() - myCoords.getLongitude();
        double x = l.getLatitude() - myCoords.getLatitude();
        return Math.atan(y/x) * 180/Math.PI;
    }

    public boolean hasLocation(String type){
        return locations.containsKey(type);
    }
    public void addLocation(Location l){
        locations.put(l.getType(), l);
    }

    public void updateLocations(){}
}
