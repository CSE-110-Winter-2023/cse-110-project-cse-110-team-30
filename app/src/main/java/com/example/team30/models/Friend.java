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
    @SerializedName("public_code")
    public String public_code;

    /** The content of the longitude. */
    @SerializedName("label")
    @NonNull
    public String label;

    /** The content of the longitude. */
    @SerializedName("longitude")
    @NonNull
    public float longitude;

    /** The content of the latitude. */
    @SerializedName("latitude")
    @NonNull
    public float latitude;

    /** The content of the latitude. */
    @SerializedName("is_listed_publicly")
    @NonNull
    public boolean is_listed_publicly;

    @JsonAdapter(TimestampAdapter.class)
    @SerializedName(value = "updated_at")
    public long updated_at = 0;

    @JsonAdapter(TimestampAdapter.class)
    @SerializedName(value = "created_at")
    public long created_at = 0;

//    public Friend(@NonNull String UID){
//        this.UID = UID;
//    }

    @NonNull
    public String getUID() {
        return public_code;
    }

    /** General constructor for a note. */
    public Friend(@NonNull String public_code,@NonNull String label, @NonNull float longitude, @NonNull float latitude, @NonNull long updated_at,@NonNull long created_at, @NonNull boolean is_listed_publicly ) {
        this.public_code = public_code;
        this.label = label;
        this.longitude = longitude;
        this.latitude = latitude;
        this.updated_at = updated_at;
        this.is_listed_publicly = is_listed_publicly;
        this.created_at = created_at;
    }



    public static Friend fromJSON(String json) {
        return new Gson().fromJson(json, Friend.class);
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }
}