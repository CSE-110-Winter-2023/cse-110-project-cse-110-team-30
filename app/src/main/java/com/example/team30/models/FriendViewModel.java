package com.example.team30.models;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class FriendViewModel extends AndroidViewModel {
    private LiveData<Friend> friendItem;
    private final FriendDao dao;


}
