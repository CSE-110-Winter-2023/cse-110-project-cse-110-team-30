package com.example.team30;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Math;

public class Compass {
    private Map<String, Location> locations;
    private Coordinates myCoords;
    public Compass(){
        locations = new HashMap<>();
    }

    public Compass(List<Location> locationsList){
        locations = new HashMap<>();
        for(Location L : locationsList)
            locations.put(L.getType(), L);
    }

    public double calculateAngleWithDistance(String type, Coordinates coords){
        Location l = locations.get(type);
        myCoords = coords;
        double y = Math.abs(l.getLongitude() - myCoords.getLongitude());
        double x = Math.abs(l.getLatitude() - myCoords.getLatitude());
        return Math.atan(y/x) * 180/Math.PI;
    }

    public void addLocation(Location l){
        locations.put(l.getType(), l);
    }

    public void updateLocations(){}
}
