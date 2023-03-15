package com.example.team30.models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;

public class Repository {
    private final FriendDao dao;
    private final FriendAPI api;

    private ScheduledFuture<?> poller;

    private Future<?> noteFuture;
    private final MutableLiveData<List<Friend>> realNoteData;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");


    public Repository(FriendDao dao) {
        this.api = FriendAPI.provide();
        this.dao = dao;
        realNoteData = new MutableLiveData<>();
    }

//    public void addFriend(Friend friend) {
//        dao.insert(friend);
//    }

    public LiveData<List<Friend>> getActiveLocations(List<String> UID){
        var locations = new MediatorLiveData<List<Friend>>();
        Observer<List<Friend>> updateFromRemote = newLocations -> {
//            Log.i("Repository", "observe track");
            if(newLocations == null) return;
            for(Friend f:newLocations){
                upsertLocal(f);
            }
        };
//        note::postValue
        locations.addSource(getAllLocal(), locations::postValue);
        locations.addSource(getAllRemote(UID), updateFromRemote);
//        locations.addSource(getAllRemote(UID), updateFromRemote);
        Log.i("Repository", "Return the friendlist" + locations);
        return locations;
    }

    public LiveData<List<Friend>> getSynced(){
        List<String> UIDList = getAllLocalUID();
        var friends = new MediatorLiveData<List<Friend>>();
        Observer<List<Friend>> updateFromRemote = theirFriend -> {
            var ourFriend = friends.getValue();
            int lenFriends = theirFriend.size();
            for(int i = 0; i < lenFriends; i++){
                upsertLocal(theirFriend.get(i));
            }
        };
        friends.addSource(getRemote(UIDList), updateFromRemote);
        Log.i("Repository", "Return the friendlist");
        return friends;
    }

//    public void upsertSynced(Friend friend) {
//        upsertLocal(friend);
//        upsertRemote(friend);
//    }

    public LiveData<Friend> getLocal(String UID) {
        return dao.get(UID);
    }

    public LiveData<List<Friend>> getAllLocal() {return dao.getAll();}

    public List<String> getAllLocalUID() {return dao.getAllUID();}

    public void upsertLocal(Friend friend) {
//        for(Friend f:friend){
            dao.upsert(friend);
//        }
    }

    public void deleteLocal(Friend friend) {
        dao.delete(friend);
    }

    public boolean existsLocal(String UID) {
        return dao.exists(UID);
    }


    private LiveData<List<Friend>> getAllRemote(List<String> UID) {
        if (this.poller != null && !this.poller.isCancelled()) {
            poller.cancel(true);
        }
        var executor = Executors.newSingleThreadScheduledExecutor();
        noteFuture = executor.scheduleAtFixedRate(() -> {
            var note = api.getMultipleLocations(UID);
            realNoteData.postValue(note);
        }, 0, 3000, TimeUnit.MILLISECONDS);

        return realNoteData;
    }

    public Friend CheckExist(String UID){
        Friend friend = api.getFriend(UID);
        if (friend == null){
            Log.e("Repository", "Not data get");
            return null;
        }
        return friend;
    }

    public LiveData<List<Friend>> getRemote(List<String> oldfriends) {

        MutableLiveData<List<Friend>> liveLocations = new MutableLiveData<>();
        //3 seconds.
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            List<Friend> newFriends = new ArrayList<>();
            for (String f:oldfriends){
                Friend updatedFriend = api.getFriend(f);
                newFriends.add(updatedFriend);
            }
            liveLocations.postValue(newFriends);
        }, 0, 3, TimeUnit.SECONDS);
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
    public void insertUserLocationRemote(String UID, String privateCode, String label, float latitude, float longitude){
        JsonObject json = new JsonObject();
        json.addProperty("private_code", privateCode);
        json.addProperty("label", label);
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
//        Log.i("Repository", UID + privateCode + latitude + longitude);
        api.patchAsync(UID, json.toString());
    }
}
