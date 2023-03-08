package com.example.team30.models;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class FriendViewModel extends AndroidViewModel {
    private LiveData<Friend> friends;
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

    public void save(String UID) {
        Friend newFriend = new Friend(UID);
        repo.addFriend(newFriend);
    }

}
