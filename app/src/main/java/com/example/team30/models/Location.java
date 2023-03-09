package com.example.team30.models;

import com.google.gson.Gson;

import java.io.Serializable;

public class Location implements Serializable {
    private String public_code;
    private String label;
    private float latitude;
    private float longitude;
    private String created_at;
    private String updated_at;

    public Location(String public_code, String label, float latitude, float longitude, String created_at, String updated_at) {
        this.public_code = public_code;
        this.label = label;
        this.latitude = latitude;
        this.longitude = longitude;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public static Location fromJSON(String json) {
        return new Gson().fromJson(json, Location.class);
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getPublic_code(){return public_code;}

    public String getLabel() {
        return label;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return "Location{" +
                "public_code='" + public_code + '\'' +
                ", label='" + label + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
