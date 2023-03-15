package com.example.team30.models;

import android.util.Log;

import androidx.annotation.WorkerThread;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class API {
    private final String BASE_URL = "https://socialcompass.goto.ucsd.edu/";
    private volatile static API instance = null;
    MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private OkHttpClient client;
    Logger logger = Logger.getLogger("API Logger");

    public API() {
        this.client = new OkHttpClient();
    }

    public static API provide() {
        if (instance == null) {
            instance = new API();
        }
        return instance;
    }

    /**
     * Returns a singular location object corresponding to the given UID/friend
     * @param friend friend object wrapping a UID
     * @return return location object for the friend
     */
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
            Location location = Location.fromJSON(body);
            return location;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns multiple locations for the given friends
     * @param friends list of friends/UIDS
     * @return corresponding list of locations
     */
    @WorkerThread
    public List<Location> getMultipleLocations(List<Friend>friends){
        List<Location> locations = new ArrayList<>();
        for(Friend f : friends){
            Location l = getLocation(f);
            locations.add(l);
        }
        logger.info("Got " + friends.size() + " locations from the server.");
        return locations;
    }

    /**
     * Inserts a location in the server
     * @param UID uid to associate with location
     * @param json request json
     * @return boolean for success
     */
    @WorkerThread
    public boolean putLocation(String UID, String json) {
        UID = UID.replace(" ", "%20");
        RequestBody body = RequestBody.create(json, JSON);
        var request = new Request.Builder()
                .url(BASE_URL + "location/" + UID)
                .method("PUT", body)
                .build();

        try (var response = client.newCall(request).execute()) {
            assert response.body() != null;
            var answer = response.body().string();
            logger.info("Put location with UID: " + UID);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Patches a location in the server
     * @param UID uid associated with location
     * @param json request json
     * @return boolean for success
     */
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
            logger.info("Patched location with UID: " + UID);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
