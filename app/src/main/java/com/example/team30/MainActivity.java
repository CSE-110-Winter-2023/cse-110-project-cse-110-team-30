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
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.team30.DataCalculators.Compass;
import com.example.team30.DataCalculators.LocationService;
import com.example.team30.DataCalculators.OrientationService;
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
            Button button = makeButton(location);
            flow.addView(button);
            flow.updateAngle(button, compass.calculateAngle(location.getLatitude(), location.getLongitude()));
            flow.updateRadius(button, 50);
        }
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
        button.setTag(location.getPublic_code());
        //button.setPadding(16, 8, 16, 8);

        // Set the size of the button
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        params.width = 100;
        params.height = 100;
        params.dimensionRatio = "1:1";
        button.setLayoutParams(params);

        return button;
    }
}
