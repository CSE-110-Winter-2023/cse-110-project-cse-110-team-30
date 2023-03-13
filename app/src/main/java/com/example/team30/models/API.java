package com.example.team30.models;

import android.util.Log;

import androidx.annotation.WorkerThread;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class API {
    private final String BASE_URL = "https://socialcompass.goto.ucsd.edu/";
    private volatile static API instance = null;
    MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private OkHttpClient client;

    private final Gson gson;

    public API() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    public static API provide() {
        if (instance == null) {
            instance = new API();
        }
        return instance;
    }

    @WorkerThread
    public Friend getLocation(String UID) {
//        String UID = friend.getUID();
        UID = UID.replace(" ", "%20");

        var request = new Request.Builder()
                .url(BASE_URL + "location/" + UID)
                .method("GET", null)
                .build();

        try (var response = client.newCall(request).execute()) {
            int code = response.code();
            if (code != 200) {
                System.out.println("Received error response with status code " + code);
                return null;
            }

            assert response.body() != null;
            var body = response.body().string();
            Log.i("GET Location", body);
//            Location location = Location.fromJSON(body);
            return gson.fromJson(body, Friend.class);//added
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Get nothing");
            return null;
        }
    }

//    @WorkerThread
//    public List<Location> getMultipleLocations(List<Friend>friends){
//        List<Location> locations = new ArrayList<>();
//        for(Friend f : friends){
//            Location l = getLocation(f);
//            locations.add(l);
//        }
//        return locations;
//    }

    @WorkerThread
    public boolean putLocation(String UID, String json) {
        UID = UID.replace(" ", "%20");
        var body = RequestBody.create(json, JSON);
        var request = new Request.Builder()
                .header("Content-Type", "application/json")
                .url(BASE_URL + "location/" + UID)//URLEncoder.encode(temp, "UTF-8"))
                .method("PUT", body)
                .build();

        try (var response = client.newCall(request).execute()) {
            assert response.body() != null;
            var answer = response.body().string();
            Log.i("PUT Location", answer);
            return response.isSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Future<Boolean> putAsync(String UID, String json) {
        var executor = Executors.newSingleThreadExecutor();
        return executor.submit(() -> putLocation(UID, json));
    }

    @WorkerThread
    public boolean patchLocation(String UID, String json) {
        UID = UID.replace(" ", "%20");
        RequestBody body = RequestBody.create(json, JSON);
        var request = new Request.Builder()
                .url(BASE_URL + "location/" + UID)
                .method("PATCH", body)
                .build();

        try (var response = client.newCall(request).execute()) {
            assert response.body() != null;
            var answer = response.body().string();
            Log.i("PUT Location", answer);
            return response.isSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Future<Boolean> patchAsync(String UID, String json) {
        var executor = Executors.newSingleThreadExecutor();
        return executor.submit(() -> patchLocation(UID, json));
    }
}
