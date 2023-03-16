package com.example.team30;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.team30.DataCalculators.Compass;
import com.example.team30.DataCalculators.LocationService;
import com.example.team30.DataCalculators.OrientationService;
import com.example.team30.models.API;

import com.example.team30.models.Friend;
import com.example.team30.models.FriendDao;

import com.example.team30.models.Location;
import com.example.team30.models.MainViewModel;
import com.example.team30.models.Repository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    private OrientationService orientationService;
    private LocationService locationService;
    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    private Future<Void> future;
    private Compass compass;
    private ConstraintLayout circular_constraint;
    private List<Friend> friendList;
    private LiveData<List<Location>> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences data = getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();

        if(data.getBoolean("register", false) == false){
            setContentView(R.layout.activity_main);
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
        }

        //zoom level 1 is the most zoomed-out
        //zoom level 3 is the most zoomed-in
        if(data.getInt("zoom level", -1) == 1) {
            setContentView(R.layout.activity_level1);
            Button zoomOut = findViewById(R.id.zoom_out);
            //zoomOut.setClickable(false);
            //zoomOut.setAlpha(0.5f);
        }
        else if (data.getInt("zoom level", -1) == 2) {
            setContentView(R.layout.activity_main);
        }
        else if (data.getInt("zoom level", -1) == 3) {
            setContentView(R.layout.activity_level3);
            Button zoomIn = findViewById(R.id.zoom_in);
            zoomIn.setClickable(false);
            zoomIn.setAlpha(0.5f);
        }
        else if (data.getInt("zoom level", -1) == 0) {
            setContentView(R.layout.activity_level0);
            Button zoomIn = findViewById(R.id.zoom_in);
            Button zoomOut = findViewById(R.id.zoom_out);
            zoomOut.setAlpha(0.5f);
            zoomIn.setClickable(false);
        }

        if(data.getInt("zoom level", -1) < 3) {
            Button zoomIn = findViewById(R.id.zoom_in);
            zoomIn.setOnClickListener(v -> {
                Log.d("MainActivity", "zoom in clicked");
                int currZoom = data.getInt("zoom level", -1);
                editor.putInt("zoom level", currZoom + 1);
                editor.apply();
                recreate();
            });
        }

        if(data.getInt("zoom level", -1) > 0) {
            Button zoomOut = findViewById(R.id.zoom_out);
            zoomOut.setOnClickListener(v -> {
                Log.d("MainActivity", "zoom out clicked");
                int currZoom = data.getInt("zoom level", -1);
                editor.putInt("zoom level", currZoom - 1);
                editor.apply();
                recreate();
            });
        }

        // Check for and get location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 20-1);
        }

        compass = Compass.singleton();
        locationService = LocationService.singleton(this);
        orientationService = OrientationService.singleton(this);
        circular_constraint = findViewById(R.id.compass1);

        MainViewModel viewModel = setupViewModel();

        locationService.getLocation().observe(this, coords ->{
            viewModel.updateUserLocation(data.getString("YourUID", ""), data.getString("privateCode", ""), coords);
            compass.setCoords(coords);
            System.out.println(coords);
        });

        orientationService.getOrientation().observe(this, angle->{
            float degrees = (float) (angle * 180/Math.PI);
            compass.setMyAngle(degrees);
        });

        locations = viewModel.getLocations();
        locations.observe(this, this::onChanged);
    }

    public void onChanged(List<Location> locations) {
        System.out.println("updated locations");
        SharedPreferences data = getSharedPreferences("test", MODE_PRIVATE);
        if(locations != null){
            for(Location location: locations){
                addDotToLayout(location, circular_constraint, data);
            }
        }
    }

    public void addFriend(View view) {
        Intent intent = new Intent(MainActivity.this, AddFriendActivity.class);
        startActivity(intent);
        finish();
    }

    private void addDotToLayout(Location location, ConstraintLayout layout, SharedPreferences data) {
        ImageView dot = layout.findViewWithTag(location.getPublic_code());
        boolean newDot = false;
        if(dot == null){
            newDot = true;
            dot = new ImageView(this);
        }
        dot.setImageResource(R.drawable.dot);
        dot.setTag(location.getPublic_code());
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );

        float density = getResources().getDisplayMetrics().density;

        params.dimensionRatio = "1:1";
        params.height = 50;
        params.width = 50;
        params.circleConstraint = R.id.compass1;
        params.circleAngle = compass.calculateAngle(location.getLatitude(), location.getLongitude());
        var distance = compass.calculateDistance(location.getLongitude(), location.getLatitude());

        if(data.getInt("zoom level", -1) == 2 ) {
            params.circleRadius = (int)Math.round(compass.zoom2radius(distance));
            System.out.println(compass.zoom2radius(distance));
        }
        else if(data.getInt("zoom level", -1) == 1 ) {
            params.circleRadius = (int)Math.round(compass.zoom1radius(distance));
        }
        else if(data.getInt("zoom level", -1) == 3 ){
            params.circleRadius = (int)Math.round(compass.zoom3radius(distance));
        }
        else if(data.getInt("zoom level", -1) == 0 ){
            params.circleRadius = (int)Math.round(compass.zoom0radius(distance));
        }

        System.out.println(distance);
        //System.out.println(density);
        System.out.println(params.circleRadius);

        dot.setLayoutParams(params);
        if(newDot){
            dot.setId(View.generateViewId());
            layout.addView(dot);
            addLabelToLayout(location.getLabel(), layout, dot);
        }
    }

    private void addLabelToLayout(String label, ConstraintLayout layout, ImageView dot) {
        TextView textView = new TextView(this);
        textView.setId(View.generateViewId());
        textView.setText(label);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );

        params.topToBottom = dot.getId();
        params.startToStart = dot.getId();
        params.endToEnd = dot.getId();
        textView.setLayoutParams(params);

        layout.addView(textView);
    }

    private MainViewModel setupViewModel() {
        return new ViewModelProvider(this).get(MainViewModel.class);
    }
}
