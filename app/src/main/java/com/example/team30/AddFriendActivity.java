package com.example.team30;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.team30.models.Friend;
import com.example.team30.models.FriendDao;
import com.example.team30.models.FriendViewModel;
import com.example.team30.models.Location;
import com.example.team30.models.LocationViewModel;

import java.util.UUID;


public class AddFriendActivity extends AppCompatActivity {
    private LiveData<Friend> friend;
    private FriendDao dao;
    private EditText contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);

        //add friend page would display your UID
        SharedPreferences data = getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putBoolean("newFriend", true);
        String yourUID = data.getString("YourUID", null);
        TextView yourUIDDisplay = findViewById(R.id.YourUIDDisplay);
        yourUIDDisplay.setText(yourUID);

        FriendViewModel viewModel = setupViewModel();
        LocationViewModel listFriendViewModel = setupLocationViewModel();

        Log.e("UserID", yourUID);

        Button button = findViewById(R.id.submitBtn);
        button.setOnClickListener(v -> {
            EditText uidView = findViewById(R.id.FriendsUIDEntry);
            String UID = String.valueOf(uidView.getText());
            editor.putString("newFriendUID", UID);
            editor.apply();
            Log.i("AddFriendActivity", "uid: " + UID);

            listFriendViewModel.getOrNotExistFriend(UID);
            friend = viewModel.getfriend(UID);
            if(friend == null) {
                Log.e("AddFriendActivity", "location is null");
            }
            Intent intent = new Intent(AddFriendActivity.this, MainActivity.class);
            Log.i("AddFriendActivity", "Added friend with UID: " + UID);
            startActivity(intent);
        });
    }
    private FriendViewModel setupViewModel() {
        return new ViewModelProvider(this).get(FriendViewModel.class);
    }
    private LocationViewModel setupLocationViewModel() {
        return new ViewModelProvider(this).get(LocationViewModel.class);
    }

}