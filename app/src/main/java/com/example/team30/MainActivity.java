package com.example.team30;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.CircularFlow;
import androidx.constraintlayout.widget.ConstraintLayout;
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    private OrientationService orientationService;
    private LocationService locationService;
    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    private Future<Void> future;
    private Compass compass;
    private CircularFlow flow;
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
        flow = findViewById(R.id.outerCircleFlow);
        if(data.getBoolean("newFriend", false)){
            editor.putBoolean("newFriend", false);
            editor.apply();
            Location location = (Location) getIntent().getSerializableExtra("location");
            System.out.println(location.getLatitude()+location.getLongitude()+location.getPublic_code());
            ImageView dot = makeDot(location);
            ConstraintLayout constraint = findViewById(R.id.compass);
            constraint.addView(dot);
            addDotToView(dot, location);
        }

    }

    public void addDotToView(ImageView dot, Location l){
        int radius = 50;
        float angle = 100;
        if (flow != null) {
            if (flow.isLayoutRequested()) {
                flow.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        flow.addViewToCircularFlow(dot, radius, compass.calculateAngle(l.getLatitude(), l.getLongitude()));
                        flow.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            } else {
                flow.addViewToCircularFlow(dot, radius, angle);
            }
        } else {
            Log.e("TAG", "CircularFlow view is null!");
        }
    }
    public void addFriend(View view) {
        Intent intent = new Intent(MainActivity.this, AddFriendActivity.class);
        startActivity(intent);
    }

    private ImageView makeDot(Location location){
        ImageView dot = new ImageView(this);
        dot.setImageResource(R.drawable.dot);
        dot.setId(View.generateViewId());
        dot.setTag(location.getPublic_code());
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        params.dimensionRatio = "1:1";
        params.height = 25;
        params.width = 25;
        dot.setLayoutParams(params);
        return dot;
    }
}
