package com.example.team30;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.CircularFlow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

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
import com.example.team30.models.FriendDao;
import com.example.team30.models.FriendViewModel;
import com.example.team30.models.Location;
import com.example.team30.models.LocationViewModel;
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
    private API api;
    private FriendDao friendDao;
    private Repository repo;
    private ConstraintLayout circular_constraint;
    private List<Friend> friendList;

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
        MainViewModel viewModel = setupViewModel();
        List<Friend> friends = viewModel.getFriends();
        if(friends != null){
            for(Friend f : friends){
                addDotToLayout(f.getLocation(), circular_constraint);
            }
        }
//        if(data.getBoolean("newFriend", false)){
//            editor.putBoolean("newFriend", false);
//            editor.apply();
//            Location location = (Location) getIntent().getSerializableExtra("location");
//            System.out.println(location.getLatitude()+location.getLongitude()+location.getPublic_code());
//            addDotToLayout(location, circular_constraint);
//        }

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
        params.circleConstraint = R.id.compass;
        params.circleAngle = compass.calculateAngle(location.getLongitude(), location.getLatitude());
        params.circleRadius = 100;
        dot.setLayoutParams(params);

        layout.addView(dot);
        System.out.println("Adding friend");
    }
    private MainViewModel setupViewModel() {
        return new ViewModelProvider(this).get(MainViewModel.class);
    }
}
