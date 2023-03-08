package com.example.team30.models;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.gson.JsonObject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;

public class Repository {
    private final FriendDao dao;
    private final API api;
    private ScheduledFuture<?> friendFuture;
    private final MutableLiveData<List<Location>> liveLocations;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");


    public Repository(API api, FriendDao dao) {
        this.api = api;
        this.dao = dao;
        liveLocations = new MutableLiveData<>();
    }

    public void addFriend(Friend friend) {
        dao.insert(friend);
    }

    public LiveData<List<Location>> getActiveLocations(){
        var locations = new MediatorLiveData<List<Location>>();
        Observer<List<Location>> updateFromRemote = newLocations -> {};
        locations.addSource(getRemote(), updateFromRemote);
        return locations;
    }

    private LiveData<List<Location>> getRemote() {
        var executor = Executors.newSingleThreadScheduledExecutor();
        friendFuture = executor.scheduleAtFixedRate(() -> {
            List<Friend> friends = dao.getAll().getValue();
            var locations = api.getMultipleLocations(friends);
            liveLocations.postValue(locations);
        }, 0, 3, TimeUnit.SECONDS);
        return liveLocations;
    }

    public void insertUserLocation(String UID, String privateCode, float latitude, float longitude){
        JsonObject json = new JsonObject();
        json.addProperty("private_code", privateCode);
        json.addProperty("label", "Team 30");
        json.addProperty("latitude", latitude);
        json.addProperty("longitude", longitude);
        api.putLocation(UID, json.toString());
    }

    public void updateUserLocation(String UID, String privateCode, float latitude, float longitude){
        JsonObject json = new JsonObject();
        json.addProperty("private_code", privateCode);
        json.addProperty("latitude", latitude);
        json.addProperty("longitude", longitude);
        api.patchLocation(UID, json.toString());
    }
}
