package com.example.team30;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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

import com.example.team30.models.FriendViewModel;
import com.example.team30.models.Location;

import java.util.UUID;


public class AddFriendActivity extends AppCompatActivity {

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
        Button button = findViewById(R.id.submitBtn);
        button.setOnClickListener(v -> {
            editor.apply();
            EditText uidView = findViewById(R.id.FriendsUIDEntry);
            String uid = String.valueOf(uidView.getText());
            viewModel.save(uid);
            Location location = viewModel.getInitialLocation(uid);
            Intent intent = new Intent(AddFriendActivity.this, MainActivity.class);
            intent.putExtra("location", location);
            Log.i("Added friend", "Added friend with UID: " + uid);
            startActivity(intent);
        });
    }
    private FriendViewModel setupViewModel() {
        return new ViewModelProvider(this).get(FriendViewModel.class);
    }
}