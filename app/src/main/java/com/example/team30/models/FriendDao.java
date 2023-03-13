package com.example.team30.models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import java.util.List;
/** Data access object for the {@link Friend} class. */
@Dao
public interface FriendDao {

    //not sure if we need, since we are doing different things in lab7.
//    @Upsert
//    public abstract long upsert(Friend friend);

    @Upsert
    public abstract long upsert(Friend friend);

    @Query("SELECT EXISTS(SELECT 1 FROM Friendlist WHERE UID = :UID)")
    public abstract boolean exists(String UID);

    @Query("SELECT * FROM Friendlist WHERE UID = :UID")
    public abstract LiveData<Friend> get(String UID);

    @Query("SELECT * FROM Friendlist ORDER BY UID")
    public abstract LiveData<List<Friend>> getAll();

    @Delete
    public abstract int delete(Friend friend);

//    @Insert
//    long insert(Friend friend);
//
//    @Query("SELECT * FROM 'Friendlist' WHERE 'UID'=:UID")
//    Friend get(String UID);
//
//    @Query("SELECT *  FROM `Friendlist`")
//    LiveData<List<Friend>> getAll();
//
//    @Update
//    int update(Friend friend);
//
//    @Delete
//    int delete(Friend friend);
}
