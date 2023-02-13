package com.example.team30;

import java.io.Serializable;

public class Location implements ILocation, Serializable {
    private String name;
    private Coordinates coordinates;
    private String type;
    private double angle;

    public Location(String name, String type, Coordinates coordinates){
        this.name = name;
        this.type = type;
        this.coordinates = coordinates;
    }

    @Override
    public Coordinates getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public float getLongitude() {
        return this.coordinates.getLongitude();
    }

    @Override
    public float getLatitude() {
        return this.coordinates.getLatitude();
    }

    @Override
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}