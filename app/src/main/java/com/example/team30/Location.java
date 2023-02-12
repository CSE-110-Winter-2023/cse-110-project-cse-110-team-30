package com.example.team30;
public class Location implements ILocation{
    private String name;
    private Coordinates coordinates;
    private String type;

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
}