package com.example.team30app;
public class Location implements ILocation{
    private String name;
    private Coordinates coordinates;

    public Location(String name, Coordinates coordinates){
        this.name = name;
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
}
