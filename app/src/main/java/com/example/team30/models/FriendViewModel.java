package com.example.team30.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.team30.models.Friend;
import com.example.team30.models.FriendDatabase;
import com.example.team30.models.Repository;

public class FriendViewModel extends AndroidViewModel {
    private LiveData<Friend> friend;
    private final Repository repo;

    public FriendViewModel(@NonNull Application application) {
        super(application);
        var context = application.getApplicationContext();
        var db = FriendDatabase.getSingleton(context);
        var dao = db.friendDao();
        this.repo = new Repository(dao);
    }

    public LiveData<Friend> getfriend(String UID) {
        // TODO: use getSynced here instead?
        // The returned live data should update whenever there is a change in
        // the database, or when the server returns a newer version of the note.
        // Polling interval: 3s.
        if (!repo.existsLocal(UID)) {
            Friend newFriend = repo.CheckExist(UID);
            if (newFriend == null){
                return null;
            }
            repo.upsertLocal(newFriend);
        }
        return repo.getLocal(UID);
    }

//    public void save(Friend friend) {
//        // TODO: try to upload the note to the server.
//        repo.upsertSynced(friend);
//        repo.upsertRemote(friend);
//    }

    public void register(String UID, String privateCode, String label, float longitude, float latitude) {
        repo.insertUserLocationRemote(UID, privateCode, label,  longitude, latitude);
    }

    public void updateUserLocation(String UID, String privateCode, float longitude, float latitude){
        repo.updateUserLocationRemote(UID, privateCode, longitude, latitude);
    }
}
