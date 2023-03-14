package com.example.team30;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.team30.DataCalculators.Compass;
import com.example.team30.DataCalculators.LocationService;
import com.example.team30.DataCalculators.OrientationService;
import com.example.team30.models.API;
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
        SharedPreferences data = getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();

        //zoom level 1 is the most un-zoomed
        //zoom level 3 is the most zoomed
        if(data.getInt("zoom level", -1) == 1) {
            setContentView(R.layout.activity_level1);
            Button zoomOut = findViewById(R.id.zoom_out);
            zoomOut.setClickable(false);
        }
        else if (data.getInt("zoom level", -1) == 2) {
            setContentView(R.layout.activity_main);
        }
        else if (data.getInt("zoom level", -1) == 3) {
            setContentView(R.layout.activity_level3);
            Button zoomIn = findViewById(R.id.zoom_in);
            zoomIn.setClickable(false);
        }

        // Check for and get location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

        compass = Compass.singleton();
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

        Button zoomIn = findViewById(R.id.zoom_in);
        zoomIn.setOnClickListener(v -> {
            Log.d("MainActivity", "zoom in clicked");
            int currZoom = data.getInt("zoom level", -1);
            editor.putInt("zoom level", currZoom + 1);
            editor.apply();
            recreate();
        });

        Button zoomOut = findViewById(R.id.zoom_out);
        zoomOut.setOnClickListener(v -> {
            Log.d("MainActivity", "zoom out clicked");
            int currZoom = data.getInt("zoom level", -1);
            editor.putInt("zoom level", currZoom - 1);
            editor.apply();
            recreate();
        });
    }

    public void addFriend(View view) {
        Intent intent = new Intent(MainActivity.this, AddFriendActivity.class);
        startActivity(intent);
        finish();
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
        params.circleConstraint = R.id.compass;
        params.circleAngle = compass.calculateAngle(location.getLongitude(), location.getLatitude());
        params.circleRadius = 200;
        dot.setLayoutParams(params);

        layout.addView(dot);
    }
}
