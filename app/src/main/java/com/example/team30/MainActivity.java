package com.example.team30;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    private OrientationService orientationService;
    private LocationService locationService;
    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    private Future<Void> future;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check for and get location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }


        SharedPreferences data = getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();


        if(data.getBoolean("register", false) == false){
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
        }

        ConstraintLayout container = findViewById(R.id.compass);
        // Check for image resource ID in intent extras
        int imageResourceId = getIntent().getIntExtra("imageResourceId", 0);

        if(imageResourceId != 0){
            // Create a new ImageView object
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT));
            imageView.setImageResource(imageResourceId);

            // Add the ImageView to the container layout
            container.addView(imageView);
        }

    }

    public void addFriend(View view) {
        Intent intent = new Intent(MainActivity.this, AddFriendActivity.class);
        startActivity(intent);
    }
}
