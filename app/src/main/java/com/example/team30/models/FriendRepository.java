package com.example.team30.models;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.gson.JsonObject;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class FriendRepository {
    private final FriendDao dao;

    private final API api;
    private ScheduledFuture<?> friendFuture;
    private final MutableLiveData<Friend> realFriendData;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");


    public FriendRepository(API api, FriendDao dao) {
        this.api = api;
        this.dao = dao;
        realFriendData = new MutableLiveData<>();
    }


    // Synced Methods
    // ==============

    /**
     * This is where the magic happens. This method will return a LiveData object that will be
     * updated when the note is updated either locally or remotely on the server. Our activities
     * however will only need to observe this one LiveData object, and don't need to care where
     * it comes from!
     *
     * This method will always prefer the newest version of the note.
     *
     * @param friend the title of the note
     * @return a LiveData object that will be updated when the note is updated locally or remotely.
     */
    public LiveData<Friend> getSynced(Friend friend) {
        var newfriend = new MediatorLiveData<Friend>();

        Observer<Friend> updateFromRemote = theirFriend -> {
           // var ourFriend = friend.getUID();
            if (theirFriend == null) return; // do nothing
            if (friend == null || friend.updatedAt < theirFriend.updatedAt) {
                upsertLocal(theirFriend);
            }
        };

//        // If we get a local update, pass it on.
//        friend.addSource(getLocal(title), friend::postValue);


        // If we get a remote update, update the local version (triggering the above observer)
        friend.addSource(getRemote(friend), updateFromRemote);

        return newfriend;
    }



    // Local Methods
    // =============

    public Friend get(String UID) {
        return dao.get(UID);
    }

    public LiveData<List<Friend>> getAllLocal() {
        return dao.getAll();
    }

    public void upsertLocal(Friend friend) {
        friend.updatedAt = Instant.now().getEpochSecond();
        dao.insert(friend);
    }



    //this fuction is to get location from the remote.

    public LiveData<Friend> getRemote(Friend friend) {
        if (friendFuture != null && !friendFuture.isCancelled()) {
            friendFuture.cancel(true);
        }
        var executor = Executors.newSingleThreadScheduledExecutor();
        friendFuture = executor.scheduleAtFixedRate(() -> {
            var location = api.getLocation(friend);
            realFriendData.postValue(friend);
        }, 0, 3000, TimeUnit.MILLISECONDS);

        return realFriendData;
    }




    //this method is to update our location to the server, and updates every 3 second,
    // but I do not know how to get out own location.
    public void upsertRemote(Friend friend) {
        // TODO: Implement upsertRemote!
        var executor = Executors.newSingleThreadScheduledExecutor();
        friendFuture = executor.scheduleAtFixedRate(() -> {
            JsonObject json = new JsonObject();
            json.addProperty("content",friend.UID);

            api.putLocation(friend, RequestBody.create(JSON,json.toString()));
            realFriendData.postValue(friend);
        }, 0, 3000, TimeUnit.MILLISECONDS);    }
}
