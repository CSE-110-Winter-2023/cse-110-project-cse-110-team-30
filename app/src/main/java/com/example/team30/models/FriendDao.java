package com.example.team30.models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FriendDao {

    //not sure if we need, since we are doing different things in lab7.
//    @Upsert
//    public abstract long upsert(Friend friend);


    @Insert
    long insert(Friend friend);

    @Query("SELECT * FROM 'Friendlist' WHERE 'UID'=:UID")
    Friend get(String UID);

    @Query("SELECT *  FROM `Friendlist`")
    LiveData<List<Friend>> getAll();

    @Update
    int update(Friend friend);

    @Delete
    int delete(Friend friend);
}
