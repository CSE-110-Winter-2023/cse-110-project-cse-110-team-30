package com.example.team30.models;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "Friendlist")
public class Friend {

    @NonNull
    @PrimaryKey
    public String UID;

    private String public_code;
    private String label;
    private float latitude;
    private float longitude;
    private String created_at;
    private String updated_at;

    public Friend(@NonNull String UID){
        this.UID = UID;
    }

    @NonNull
    public String getUID() {
        return UID;
    }

    public void setUID(@NonNull String UID) {
        this.UID = UID;
    }

    public String getPublic_code() {
        return public_code;
    }

    public void setPublic_code(String public_code) {
        this.public_code = public_code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setLocationAttributes(Location l){
        this.public_code = l.getPublic_code();
        this.label = l.getLabel();
        this.latitude = l.getLatitude();
        this.longitude = l.getLongitude();
        this.created_at = l.getCreated_at();
        this.updated_at = l.getUpdated_at();
    }
}
