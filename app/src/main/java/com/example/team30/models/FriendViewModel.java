package com.example.team30.models;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class FriendViewModel extends AndroidViewModel {
    private final FriendDao dao;
    private final Repository repo;

    public FriendViewModel(@NonNull Application application){
        super(application);
        Context context = getApplication().getApplicationContext();
        FriendDatabase db = FriendDatabase.getSingleton(context);
        dao = db.friendDao();

        API api =  API.provide();
        this.repo = new Repository(api, dao);
    }

    /**
     * Save a friend to the database
     * @param UID friend's UID
     */
    public void save(String UID, Location l) {
        Friend newFriend = new Friend(UID);
        newFriend.setLocationAttributes(l);
        if(dao.get(UID) == null) {
            repo.addFriend(newFriend);
        }
    }

    /**
     * Get friend's location when they are first added
     * @param UID
     * @return location object of friend
     */
    public Location getInitialLocation(String UID){
        Friend newFriend = new Friend(UID);
        return repo.getSingleLocation(newFriend);
    }
}
