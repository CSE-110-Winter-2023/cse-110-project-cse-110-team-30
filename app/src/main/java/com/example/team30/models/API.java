package com.example.team30.models;

import android.util.Log;

import androidx.annotation.WorkerThread;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class API {
    private final String BASE_URL = "https://socialcompass.goto.ucsd.edu/";
    private volatile static API instance = null;

    private OkHttpClient client;

    public API() {
        this.client = new OkHttpClient();
    }

    public static API provide() {
        if (instance == null) {
            instance = new API();
        }
        return instance;
    }

    @WorkerThread
    public Location getLocation(Friend friend) {
        String UID = friend.getUID();
        UID = UID.replace(" ", "%20");

        var request = new Request.Builder()
                .url(BASE_URL + "location/" + UID)
                .method("GET", null)
                .build();

        try (var response = client.newCall(request).execute()) {
            assert response.body() != null;
            var body = response.body().string();
            Log.i("GET Location", body);
            Location location = Location.fromJSON(body);
            return location;//added
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @WorkerThread
    public List<Location> getMultipleLocations(List<Friend>friends){
        List<Location> locations = new ArrayList<>();
        for(Friend f : friends){
            Location l = getLocation(f);
            locations.add(l);
        }
        return locations;
    }

    @WorkerThread
    public void putLocation(Friend friend, RequestBody requestBody) {
        String UID = friend.getUID();
        UID = UID.replace(" ", "%20");

        var request = new Request.Builder()
                .url(BASE_URL + "location/" + UID)
                .method("PUT", requestBody)
                .build();

        try (var response = client.newCall(request).execute()) {
            assert response.body() != null;
            var body = response.body().string();
            Log.i("PUT Location", body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @WorkerThread
    public void patchLocation(Friend friend, RequestBody requestBody) {
        String UID = friend.getUID();
        UID = UID.replace(" ", "%20");

        var request = new Request.Builder()
                .url(BASE_URL + "location/" + UID)
                .method("PATCH", requestBody)
                .build();

        try (var response = client.newCall(request).execute()) {
            assert response.body() != null;
            var body = response.body().string();
            Log.i("PUT Location", body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
