package com.example.team30.models;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class LocationViewModel extends AndroidViewModel {
    private LiveData<List<Location>> friends;
    private final FriendDao dao;
    private final Repository repo;

    public LocationViewModel(@NonNull Application application){
        super(application);
        Context context = getApplication().getApplicationContext();
        FriendDatabase db = FriendDatabase.getSingleton(context);
        dao = db.friendDao();

        API api =  API.provide();
        this.repo = new Repository(api, dao);
    }

    public void register(String UID, String privateCode, Pair<Double, Double> coords) {
        repo.insertUserLocation(UID, privateCode, coords.first.floatValue(), coords.second.floatValue());
    }

    public LiveData<List<Location>> getLocations(){
        return repo.getActiveLocations();
    }

    public void updateUserLocation(String UID, String privateCode, Pair<Double, Double> coords){
        repo.updateUserLocation(UID, privateCode, coords.first.floatValue(), coords.second.floatValue());
    }

}
