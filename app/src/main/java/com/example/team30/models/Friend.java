package com.example.team30.models;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;


@Entity(tableName = "Friendlist")
public class Friend {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String UID;

    Friend(@NonNull String UID){
        this.UID = UID;
    }


    public static List<Friend> loadJSON(Context context, String path) {
        try {
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Friend>>(){}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", UID='" + UID + '\'' +
                '}';
    }

}
