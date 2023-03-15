package com.example.team30.models;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class LocationViewModel extends AndroidViewModel {
    private LiveData<List<Friend>> friends;
    private List<String> UID;
    private final Repository repo;
    private LiveData<Friend> f;

    public LocationViewModel(@NonNull Application application){
        super(application);
        Context context = getApplication().getApplicationContext();
        FriendDatabase db = FriendDatabase.getSingleton(context);
        var dao = db.friendDao();
        this.repo = new Repository(dao);
        UID = new ArrayList<>();
    }


    public LiveData<List<Friend>> getLocations(){
        if (this.friends == null) {
            this.UID = repo.getAllLocalUID();
            List<Friend> old = repo.getAllLocal().getValue();
            Log.i("LocationViewModel", UID + "   Get UID/Old   "+ old);
            if(UID == null){
                return repo.getAllLocal();
            }
            Log.i("LocationViewModel", "Get activeLocation"+ old);
            this.friends = repo.getActiveLocations(UID);
////            Log.i("LocationViewModel", "End get"+UID);
        }
        return friends;
    }

//    public LiveData<List<String>> getUID( ){
//        if (this.UID == null) {
//            this.UID = repo.getAllLocalUID();
//        }
//        return this.UID;
//    }

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
