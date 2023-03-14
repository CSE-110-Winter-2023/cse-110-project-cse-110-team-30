package com.example.team30.models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;

public class Repository {
    private final FriendDao dao;
    private final FriendAPI api;

//    private ScheduledFuture<?> friendFuture;
//    private final MutableLiveData<List<Location>> liveLocations;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");


    public Repository(FriendDao dao) {
        this.api = FriendAPI.provide();
        this.dao = dao;
    }

//    public void addFriend(Friend friend) {
//        dao.insert(friend);
//    }

    public LiveData<List<Friend>> getSynced(List<Friend> oldfriends){

        var friends = new MediatorLiveData<List<Friend>>();
        Observer<List<Friend>> updateFromRemote = theirFriend -> {
            var ourFriend = friends.getValue();
            int lenFriends = theirFriend.size();

            for(int i = 0; i < lenFriends; i++){
                if(ourFriend.get(i).updated_at < theirFriend.get(i).updated_at){
                    upsertLocal(theirFriend.get(i));
                }
            }
        };
        friends.addSource(getRemote(oldfriends), updateFromRemote);
        return friends;
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
        dao.upsert(friend);
    }

    public void deleteLocal(Friend friend) {
        dao.delete(friend);
    }

    public boolean existsLocal(String UID) {
        return dao.exists(UID);
    }


//    private LiveData<List<Location>> getAllRemote(List<Friend> friends) {
//        MutableLiveData<List<Friend>> noteLiveData = new MutableLiveData<>();
//        var executor = Executors.newSingleThreadScheduledExecutor();
//        executor.scheduleAtFixedRate(() -> {
//            List<Friend> newFriends = new ArrayList<>();
//            for (Friend f : friends){
//                Friend newFriend = api.getLocation(f.public_code);
//                upsertLocal(newFriend);
//                newFriends.add(newFriend);
//            }
//            noteLiveData.postValue(newFriends);
//        }, 0, 3, TimeUnit.SECONDS);
//        return noteLiveData;
//    }

    public Friend CheckExist(String UID){
        Friend friend = api.getFriend(UID);
        if (friend == null){
            Log.e("Repository", "Not data get");
            return null;
        }
        return friend;
    }

    public LiveData<List<Friend>> getRemote(List<Friend> oldfriends) {
        MutableLiveData<List<Friend>> liveLocations = new MutableLiveData<>();
        //3 seconds.
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            List<Friend> newFriends = new ArrayList<>();
            for (Friend f:oldfriends){
                Friend updatedFriend = api.getFriend(f.public_code);
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
