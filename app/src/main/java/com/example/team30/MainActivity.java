package com.example.team30;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.CircularFlow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;

import com.example.team30.DataCalculators.Compass;
import com.example.team30.DataCalculators.LocationService;
import com.example.team30.DataCalculators.OrientationService;
import com.example.team30.models.API;
import com.example.team30.models.Friend;
import com.example.team30.models.Location;
import com.example.team30.models.Repository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    private OrientationService orientationService;
    private LocationService locationService;
    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    private Future<Void> future;
    private Compass compass;
    private API api;
    private Repository repo;
    private ConstraintLayout circular_constraint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check for and get location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

        compass = Compass.singleton();
        SharedPreferences data = getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        if(data.getBoolean("register", false) == false){
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
        }
        circular_constraint = findViewById(R.id.compass);
        if(data.getBoolean("newFriend", false)){
            editor.putBoolean("newFriend", false);
            editor.apply();
            Location location = (Location) getIntent().getSerializableExtra("location");
            System.out.println(location.getLatitude()+location.getLongitude()+location.getPublic_code());
            addDotToLayout(location, circular_constraint);
        }

    }


    public void addFriend(View view) {
        Intent intent = new Intent(MainActivity.this, AddFriendActivity.class);
        startActivity(intent);
    }

    private void addDotToLayout(Location location, ConstraintLayout layout) {
        ImageView dot = new ImageView(this);
        dot.setImageResource(R.drawable.dot);
        dot.setId(View.generateViewId());
        dot.setTag(location.getPublic_code());
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        params.dimensionRatio = "1:1";
        params.height = 50;
        params.width = 50;
        dot.setLayoutParams(params);

        // Add the dot ImageView to the ConstraintLayout
        layout.addView(dot);

        // Create a ConstraintSet to set the constraints for the dot ImageView
//        ConstraintSet constraintSet = new ConstraintSet();
//        constraintSet.clone(layout);
//        constraintSet.connect(dot.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP);
//        constraintSet.connect(dot.getId(), ConstraintSet.START, layout.getId(), ConstraintSet.START);
//        constraintSet.applyTo(layout);
    }

}
