package com.example.team30.models;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "Friendlist")
public class Friend {
    @NonNull
    @PrimaryKey
    public String UID;
    Friend(@NonNull String UID){
        this.UID = UID;
    }

    @NonNull
    public String getUID() {
        return UID;
    }

    public void setUID(@NonNull String UID) {
        this.UID = UID;
    }
}
