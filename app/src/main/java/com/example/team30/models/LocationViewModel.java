package com.example.team30.models;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;


public class LocationViewModel extends AndroidViewModel {
    private LiveData<List<Friend>> friends;
    private List<String> UID;
    private final Repository repo;

    public LocationViewModel(@NonNull Application application){
        super(application);
        Context context = getApplication().getApplicationContext();
        FriendDatabase db = FriendDatabase.getSingleton(context);
        var dao = db.friendDao();
        this.repo = new Repository(dao);
    }


    public LiveData<List<Friend>> getLocations(){
        if (friends == null) {
            friends = repo.getAllLocal();
//            friends = repo.getSynced(friends);
        }
        return friends;
    }

    /**
     * Open a note in the database
     * @param UID the UID of the Friend
     * @return a LiveData object that will be updated when this note changes.
     */
    public LiveData<Friend> getOrNotExistFriend(String UID) {
        if (!repo.existsLocal(UID)) {
            Friend newFriend = repo.CheckExist(UID);
            if (newFriend == null){
                return null;
            }
            this.UID.add(UID);
            repo.upsertLocal(newFriend);
        }
        return repo.getLocal(UID);
    }

//    public Friend setMyLocation(String UID, float longitude, float latitude, long time) {
//        myLocation = new Friend(UID, longitude, latitude, time);
//        return myLocation;
//    }

    public void register(String UID, String privateCode, String userName, float longitude, float latitude) {
        repo.insertUserLocationRemote(UID, privateCode, userName, longitude, latitude);
    }

    public void updateUserLocation(String UID, String privateCode, float longitude, float latitude){
//        Log.i("LocationViewModel", "Update location in web");
        repo.updateUserLocationRemote(UID, privateCode, longitude, latitude);
    }

}
