package com.example.team30.models;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private LiveData<List<Location>> friends;
    private final FriendDao dao;
    private final Repository repo;

    public MainViewModel(@NonNull Application application){
        super(application);
        Context context = getApplication().getApplicationContext();
        FriendDatabase db = FriendDatabase.getSingleton(context);
        dao = db.friendDao();

        API api =  API.provide();
        this.repo = new Repository(api, dao);
    }


    /**
     * Gets all active locations of current friends
     * @return
     */
    public LiveData<List<Location>> getLocations(){
        return repo.getActiveLocations();
    }

    /**
     * Updates the user's location
     * @param UID user's UID
     * @param privateCode device's private code
     * @param coords initial coordinates
     */
    public void updateUserLocation(String UID, String privateCode, Pair<Double, Double> coords){
        repo.updateUserLocation(UID, privateCode, coords.first.floatValue(), coords.second.floatValue());
    }

    /**
     * Gets list of all current friends
     * @return list of all current friends
     */
    public List<Friend> getFriends(){
        return dao.getAll().getValue();
    }

}
