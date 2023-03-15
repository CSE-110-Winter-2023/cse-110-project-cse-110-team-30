package com.example.team30.models;


import android.util.Log;

import androidx.annotation.WorkerThread;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class FriendAPI {
    private final String BASE_URL = "https://socialcompass.goto.ucsd.edu/";
    private volatile static FriendAPI instance = null;
    private OkHttpClient client;

    MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final Gson gson;

    public FriendAPI() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    public static FriendAPI provide() {
        if (instance == null) {
            instance = new FriendAPI();
        }
        return instance;
    }
    @WorkerThread
    public List<Friend> getMultipleLocations(List<String> UID){
        List<Friend> locations = new ArrayList<>();
//        Log.i("FriendAPI", "Get All UID update" + UID);
//        for(Friend f : locations){
//            allLocation.put(f.public_code,f);
////            Log.i("FriendAPI", "Get update" + f);
//        }
        for (String s:UID){
            Friend f = getLocation(s);
            if(f != null){
                locations.add(f);
            }
        }
//        Log.i("FriendAPI", "return All location update");
        return locations;
    }
    @WorkerThread
    public List<Friend> getAll() {
        var request = new Request.Builder()
                .url(BASE_URL + "locatios")
                .method("GET", null)
                .build();
        Log.i("FriendAPI", "Get ALLLLL activeLocation" );
        try (var response = client.newCall(request).execute()) {
            int code = response.code();
            if (code != 200) {
                System.out.println("Received error response with status code " + code);
                return null;
            }
            assert response.body() != null;
            var body = response.body().string();
//            Log.i("GET Location", body);
//            Location location = Location.fromJSON(body);
            Type type = new TypeToken<List<Friend>>(){}.getType();
            return gson.fromJson(body, type);//added
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("API","Get nothing");
            return null;
        }
    }

    @WorkerThread
    public Friend getLocation(String UID) {
//        String UID = friend.getUID();
        UID = UID.replace(" ", "%20");

        var request = new Request.Builder()
                .url(BASE_URL + "location/" + UID)
                .method("GET", null)
                .build();
//        Log.i("FriendAPI", "Get activeLocation" + UID);
        try (var response = client.newCall(request).execute()) {
            int code = response.code();
            if (code != 200) {
                System.out.println("Received error response with status code " + code);
                return null;
            }
            assert response.body() != null;
            var body = response.body().string();
//            Log.i("GET Location", body);
//            Location location = Location.fromJSON(body);
            return gson.fromJson(body, Friend.class);//added
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("API","Get nothing");
            return null;
        }
    }

    public Future<Friend> getAsync(String UID) {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> getLocation(UID));
        executor.shutdown();
        return future;
    }

    public Friend getFriend(String UID){
        Future<Friend> futureFriend = getAsync(UID);
        try {
            Friend friend = futureFriend.get(); // this will block until the result is available
            if (friend == null){
                Log.e("Repository", "No data retrieved");
                return null;
            }
            return friend;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Log.e("Repository", "Error retrieving data");
            return null;
        }
    }


    @WorkerThread
    public boolean putLocation(String UID, String json) {
        UID = UID.replace(" ", "%20");
        var body = RequestBody.create(json, JSON);
        var request = new Request.Builder()
                .url(BASE_URL + "location/" + UID)
                .method("PUT", body)
                .build();

        try (var response = client.newCall(request).execute()) {
            assert response.body() != null;
            var answer = response.body().string();
            Log.i("PUT Location", answer);
            return response.isSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("PUT Location", "Fail to push in the data");
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
//            Log.i("Patch Location", answer);
            return response.isSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Patch Location", "Fail to patch in the data");
            return false;
        }
    }

    public Future<Boolean> patchAsync(String UID, String json) {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> patchLocation(UID, json));
        return future;
    }
}