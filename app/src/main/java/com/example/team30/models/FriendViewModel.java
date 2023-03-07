package com.example.team30.models;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FriendViewModel extends AndroidViewModel {
    private LiveData<List<Friend>> friends;
    private final FriendDao dao;

    public FriendViewModel(@NonNull Application application){
        super(application);
        Context context = getApplication().getApplicationContext();
        FriendDatabase db = FriendDatabase.getSingleton(context);
        dao = db.todoListItemDao();
    }

    public LiveData<List<Friend>> getFriends() {
        if(friends == null) {
            loadFriends();
        }
        return friends;
    }

    private void loadFriends(){
        friends = dao.getAll();
    }
    public void createTodo(String UID) {
        Friend newFriend = new Friend(UID);
        dao.insert(newFriend);
    }
}
