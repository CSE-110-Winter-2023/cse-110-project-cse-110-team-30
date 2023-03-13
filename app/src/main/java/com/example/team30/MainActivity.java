package com.example.team30;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.CircularFlow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.team30.models.Friend;
import com.example.team30.models.FriendAdapter;
import com.example.team30.models.Location;
import com.example.team30.models.LocationViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private OrientationService orientationService;
    private LocationService locationService;
    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    private Future<Void> future;
    private Compass compass;
    private CircularFlow flow;
    private long lastLocationTime;

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public RecyclerView recyclerView;

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
        orientationService = OrientationService.singleton(this);

        compass = Compass.singleton();
        SharedPreferences data = getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();

        if(data.getBoolean("register", false) == false){
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
        }

        Intent intent = getIntent();
        String friendUID = intent.getStringExtra("friend data");
        String userID = intent.getStringExtra("uniqueID");
        String privateCode = intent.getStringExtra("privateCode");

        var viewModel = setupViewModel();
        var adapter = setupAdapter(viewModel, locationService, orientationService, userID, privateCode);

        setupViews(viewModel, adapter, friendUID);


//        flow = findViewById(R.id.outerCircleFlow);
        Log.i("MainActivity","newFriend: " + data.getBoolean("newFriend", false));

//        if(data.getBoolean("newFriend", false)){
//            editor.putBoolean("newFriend", false);
//            editor.apply();
//            Location location = (Location) getIntent().getSerializableExtra("location");
//            if(location == null) {
//                Log.e("MainActivity", "location is null");
//            }
//            else {
//                Button button = makeButton(location);
//                System.out.println("Make button successfully");
//                flow.addView(button);
//                flow.updateAngle(button, compass.calculateAngle(location.getLatitude(), location.getLongitude()));
//                flow.updateRadius(button, 50);
//            }
//        }

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

    private Button makeButton(Location location){
        Button button = new Button(this);
        button.setText("Click me");
        button.setId(View.generateViewId());
        button.setBackgroundColor(Color.BLUE);
        //button.setPadding(16, 8, 16, 8);

        // Set the size of the button
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        params.width = 100;
        params.height = 100;
        params.dimensionRatio = "1:1";

        //params.circleAngle = compass.calculateAngle(location.getLatitude(), location.getLongitude());
        //params.circleRadius = 200;

        button.setLayoutParams(params);
        button.setTag(location.getPublic_code());
        return button;
    }

    @NonNull
    private FriendAdapter setupAdapter(LocationViewModel viewModel, LocationService locationservice,
                                       OrientationService orientationService, String UID, String Privatecode ) {
        FriendAdapter adapter = new FriendAdapter();
        adapter.setHasStableIds(true);
        viewModel.getLocations().observe(this, adapter::setFriends);
        locationservice.getLocation().observe(this, coords->{
            adapter.setMyLocation(coords);
            viewModel.updateUserLocation(UID,
                    Privatecode,
                    coords.first.floatValue(),
                    coords.second.floatValue());
        });

        orientationService.getOrientation().observe(this, adapter::setOrientation);
        return adapter;
    }

    private LocationViewModel setupViewModel() {
        return new ViewModelProvider(this).get(LocationViewModel.class);
    }

    private void setupViews(LocationViewModel viewModel, FriendAdapter adapter, String friendUID) {
        if (friendUID != null){
            var friend = viewModel.getOrNotExistFriend(friendUID);
        }
        setupRecycler(adapter);
    }

    @SuppressLint("RestrictedApi")
    private void setupRecycler(FriendAdapter adapter) {
        // We store the recycler view in a field _only_ because we will want to access it in tests.
        recyclerView = findViewById(R.id.recycler_main);
        //TODO: Change the layout manager  ConstraintLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }
}
