package com.example.team30;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private OrientationService orientationService;
    private LocationService locationService;
    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    private Future<Void> future;
    private long lastLocationTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lastLocationTime = System.currentTimeMillis();

        // Check for and get location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
        locationService = LocationService.singleton(this);
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

        TextView redDot = findViewById(R.id.RecDot);
        TextView greenDot = findViewById(R.id.GreenDot);
        TextView timer = findViewById(R.id.timer);

        //Update the location of user and all relation point
        locationService.getLocation().observe(this, coords ->{
            //Update the GPS connection signal
            lastLocationTime = System.currentTimeMillis();
            redDot.setVisibility(View.INVISIBLE);
            timer.setVisibility(View.INVISIBLE);
            greenDot.setVisibility(View.VISIBLE);
            //TODO:CAll reposition function

        });

        //Update the GPS lost signal - each min
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            long currTime = System.currentTimeMillis();
            float lostTimeInMin = (float)(currTime - lastLocationTime)/60000;
            //lost signal time greater than 1 min
            System.out.println("Lost time (m)" + String.valueOf(lostTimeInMin) + "Last Update: " + String.valueOf(lastLocationTime));
            if (lostTimeInMin >= 1){
                String label = String.valueOf((int)lostTimeInMin) + "m";
                //UIThread of change GPS lost signal UI
                runOnUiThread(()->{
                    timer.setText(label);
                    redDot.setVisibility(View.VISIBLE);
                    timer.setVisibility(View.VISIBLE);
                    greenDot.setVisibility(View.INVISIBLE);
                });
            }
        }, 0, 1, TimeUnit.MINUTES);

    }

    public void addFriend(View view) {
        Intent intent = new Intent(MainActivity.this, AddFriendActivity.class);
        startActivity(intent);
    }
    //Check GPS Status true/false
    public boolean checkGPSStatus(AppCompatActivity context){
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE );
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return statusOfGPS;
    };


}
