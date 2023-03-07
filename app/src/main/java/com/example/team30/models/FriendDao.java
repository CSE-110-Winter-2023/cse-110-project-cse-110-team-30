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
    @Insert
    long insert(Friend friend);

    @Insert
    List<Long> insertAll(List<Friend> friends);

    @Query("SELECT * FROM 'Friendlist' WHERE 'id'=:id")
    Friend get(long id);

    @Update
    int update(Friend friend);

    @Delete
    int delete(Friend friend);
}
