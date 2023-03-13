package com.example.team30.models;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.gson.JsonObject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;

public class Repository {
    private final FriendDao dao;
    private final API api;

    private ScheduledFuture<?> friendFuture;
    private final MutableLiveData<List<Location>> liveLocations;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");


    public Repository(FriendDao dao) {
        this.api = API.provide();
        this.dao = dao;
        liveLocations = new MutableLiveData<>();
    }

//    public void addFriend(Friend friend) {
//        dao.insert(friend);
//    }

    public LiveData<Friend> getSynced(String UID){
//        var locations = new MediatorLiveData<List<Location>>();
//        Observer<List<Location>> updateFromRemote = newLocations -> {};
//        locations.addSource(getAllRemote(), updateFromRemote);
//        return locations;
        var friend = new MediatorLiveData<Friend>();

        Observer<Friend> updateFromRemote = theirNote -> {
            var ourNote = friend.getValue();
            if (theirNote == null) return; // do nothing
            if (ourNote == null || ourNote.updatedAt < theirNote.updatedAt) {
                upsertLocal(theirNote);
            }
        };

        friend.addSource(getRemote(UID), updateFromRemote);
        return friend;
    }

//    public void upsertSynced(Friend friend) {
//        upsertLocal(friend);
//        upsertRemote(friend);
//    }

    public LiveData<Friend> getLocal(String UID) {
        return dao.get(UID);
    }

    public LiveData<List<Friend>> getAllLocal() {
//        LiveData<List<Friend>> friendlist = dao.getAll();
        return dao.getAll();
    }

    public void upsertLocal(Friend friend) {
        friend.updatedAt = System.currentTimeMillis();
        dao.upsert(friend);
    }

    public void deleteLocal(Friend friend) {
        dao.delete(friend);
    }

    public boolean existsLocal(String UID) {
        return dao.exists(UID);
    }


//    private LiveData<List<Location>> getAllRemote() {
//        var executor = Executors.newSingleThreadScheduledExecutor();
//        friendFuture = executor.scheduleAtFixedRate(() -> {
//            List<Friend> friends = dao.getAll().getValue();
//            var locations = api.getMultipleLocations(friends);
//            liveLocations.postValue(locations);
//        }, 0, 3, TimeUnit.SECONDS);
//        return liveLocations;
//    }
    public Friend CheckExist(String UID){
        Friend friend = api.getLocation(UID);
//        if (friend != null) {
//            return null;
//        }
        return friend;
    }

    public LiveData<Friend> getRemote(String UID) {
        MutableLiveData<Friend> liveLocations = new MutableLiveData<>();

        // Retrieve from the server and update the local database.
        Friend friend = api.getLocation(UID);
        if (friend != null) {
            upsertLocal(friend);
            liveLocations.setValue(friend);
        }

        //3 seconds.
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            Friend updatedNote = api.getLocation(UID);
            if (updatedNote != null) {
                upsertLocal(updatedNote);
                liveLocations.postValue(updatedNote);
            }
        }, 0, 3, TimeUnit.SECONDS);

        // Return the LiveData object.
        return liveLocations;
    }
//    public void upsertRemote() {
//        // TODO: Implement upsertRemote!
//        api.putAsync(friend);
//        //throw new UnsupportedOperationException("Not implemented yet");
//    }


//    public Location getInitialLocation(Friend friend){
//        return api.getLocation(friend);
//    }
//
    public void insertUserLocationRemote(String UID, String privateCode, float latitude, float longitude){
        JsonObject json = new JsonObject();
        json.addProperty("private_code", privateCode);
        json.addProperty("label", "Team 30");
        json.addProperty("latitude", latitude);
        json.addProperty("longitude", longitude);
        json.addProperty("updated_at", System.currentTimeMillis());
        api.putAsync(UID, json.toString());
    }

    public void updateUserLocationRemote(String UID, String privateCode, float latitude, float longitude){
        JsonObject json = new JsonObject();
        json.addProperty("private_code", privateCode);
        json.addProperty("latitude", latitude);
        json.addProperty("longitude", longitude);
        api.patchAsync(UID, json.toString());
    }
}
