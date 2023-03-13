package com.example.team30.models;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.time.Instant;

class TimestampAdapter extends TypeAdapter<Long> {
    @Override
    public void write(JsonWriter out, Long value) throws java.io.IOException {
        var instant = Instant.ofEpochSecond(value);
        out.value(instant.toString());
    }

    @Override
    public Long read(JsonReader in) throws java.io.IOException {
        var instant = Instant.parse(in.nextString());
        return instant.getEpochSecond();
    }
}

@Entity(tableName = "Friendlist")
public class Friend {
    @NonNull
    @PrimaryKey
    @SerializedName("UID")
    public String UID;

    /** The content of the longitude. */
    @SerializedName("label")
    @NonNull
    public float label;

    /** The content of the longitude. */
    @SerializedName("longitude")
    @NonNull
    public float longitude;

    /** The content of the latitude. */
    @SerializedName("latitude")
    @NonNull
    public float latitude;

    /** The content of the time. */
    @SerializedName("updateTime")
    @NonNull
    public long updateTime;


    @JsonAdapter(TimestampAdapter.class)
    @SerializedName(value = "updated_at", alternate = "updatedAt")
    public long updatedAt = 0;

//    public Friend(@NonNull String UID){
//        this.UID = UID;
//    }

//    @NonNull
//    public String getUID() {
//        return UID;
//    }

//    public void setUID(@NonNull String UID) {
//        this.UID = UID;
//    }

    /** General constructor for a note. */
    public Friend(@NonNull String UID, @NonNull float longitude, @NonNull float latitude, @NonNull long updateTime) {
        this.UID = UID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.updateTime = updateTime;
        this.updatedAt = 0;
    }

    @Ignore
    public Friend(@NonNull String UID, @NonNull float longitude, @NonNull float latitude, @NonNull long updateTime, long updatedAt) {
        this.UID = UID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.updateTime = updateTime;
        this.updatedAt = updatedAt;
    }



    public static Friend fromJSON(String json) {
        return new Gson().fromJson(json, Friend.class);
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }
}