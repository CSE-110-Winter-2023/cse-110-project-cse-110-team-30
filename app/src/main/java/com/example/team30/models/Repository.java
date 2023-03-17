package com.example.team30.models;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;

public class Repository {
    private final FriendDao dao;
    private final API api;
    private ScheduledFuture<?> friendFuture;
    private final MutableLiveData<List<Location>> liveLocations;

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
        locations.addSource(getAllRemote(), locations::postValue);
        return locations;
    }

    private LiveData<List<Location>> getAllRemote() {
        var executor = Executors.newSingleThreadScheduledExecutor();
        friendFuture = executor.scheduleAtFixedRate(() -> {
            List<Friend> friends = dao.getAll();
            var locations = api.getMultipleLocations(friends);
            liveLocations.postValue(locations);
        }, 0, 3, TimeUnit.SECONDS);
        return liveLocations;
    }

    public Location getSingleLocation(Friend friend){
        CompletableFuture<Location> l = CompletableFuture.supplyAsync(() ->
                api.getLocation(friend));
        try{
            return l.get();
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean insertUserLocation(String UID, String privateCode, float latitude, float longitude){
        JsonObject json = new JsonObject();
        json.addProperty("private_code", privateCode);
        json.addProperty("label", "Team 30");
        json.addProperty("latitude", latitude);
        json.addProperty("longitude", longitude);
        CompletableFuture<Boolean> success = CompletableFuture.supplyAsync(() ->
                api.putLocation(UID, json.toString()));
        try{
            return success.get();
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUserLocation(String UID, String privateCode, float latitude, float longitude){
        JsonObject json = new JsonObject();
        json.addProperty("private_code", privateCode);
        json.addProperty("latitude", latitude);
        json.addProperty("longitude", longitude);
        CompletableFuture<Boolean> success = CompletableFuture.supplyAsync(() ->
                api.patchLocation(UID, json.toString()));
        try{
            return success.get();
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public LiveData<List<Location>> mockGetActiveLocations(){
        var locations = new MediatorLiveData<List<Location>>();
        Observer<List<Location>> updateFromRemote = newLocations -> {};
        locations.addSource(mockGetAllRemote(), locations::postValue);
        return locations;
    }
    private LiveData<List<Location>> mockGetAllRemote() {
        var executor = Executors.newSingleThreadScheduledExecutor();
        friendFuture = executor.scheduleAtFixedRate(() -> {
            List<Friend> friends = dao.getAll();
            var locations = api.mockGetAllLocations(friends);
            liveLocations.postValue(locations);
        }, 0, 3, TimeUnit.SECONDS);
        return liveLocations;
    }
}
