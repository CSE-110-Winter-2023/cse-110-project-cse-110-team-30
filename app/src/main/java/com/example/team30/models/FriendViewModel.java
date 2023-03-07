package com.example.team30.models;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FriendViewModel extends AndroidViewModel {
    private LiveData<Friend> friends;
    private final FriendDao dao;
    private final FriendRepository repo;

    public FriendViewModel(@NonNull Application application){
        super(application);
        Context context = getApplication().getApplicationContext();
        FriendDatabase db = FriendDatabase.getSingleton(context);
        dao = db.todoListItemDao();

        API api =  API.provide();
        this.repo = new FriendRepository(api, dao);
    }

    //this one here should the getSynced here and not sure if we should return the list of friends or just a friend, but I do not know how to fix getSynced. My bad
    public LiveData<Friend> getFriends(Friend friend) {
        if(friends == null) {
            friends = repo.getSynced(friend);
        }
        return friends;
    }

    private void loadFriends(){
        //friends = dao.getAll();
    }

    public void createTodo(String UID) {
        Friend newFriend = new Friend(UID);
        dao.insert(newFriend);
    }

}
