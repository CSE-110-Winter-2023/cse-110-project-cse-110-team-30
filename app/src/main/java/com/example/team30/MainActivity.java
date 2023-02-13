package com.example.team30;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Compass compass = new Compass();
    private LocationService locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check for and get location permissions
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
        locationService = LocationService.singleton(this);
        SharedPreferences data = getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        //data.edit().remove("counter").apply();

        if(data.getInt("counter", -1) == -1)
            initialInput();
        boolean newLocation = data.getBoolean("newLocation", false);
        if(newLocation){
            Intent inputResultIntent = getIntent();
            editor.putBoolean("newLocation", false);
            editor.apply();
            Location location = (Location) inputResultIntent.getSerializableExtra("newLocation");
            newLocationUpdate(location);
        }

        locationService.getLocation().observe(this, coords ->{
            ImageView pic = findViewById(R.id.house);
            ConstraintLayout.LayoutParams layout = (ConstraintLayout.LayoutParams) pic.getLayoutParams();
            layout.circleAngle = (float) compass.calculateAngleWithDistance("Parent", coords);
            System.out.println(coords.getLatitude());
            System.out.println(coords.getLongitude());
            System.out.println("angle");
            System.out.println(layout.circleAngle);
            pic.setLayoutParams(layout);
        });
    }

    public void newLocationUpdate(Location location){
        ImageView pic = findViewById(R.id.house);
        compass.addLocation(location);
        pic.setVisibility(View.VISIBLE);
    }

    public void initialInput(){
        Intent intent = new Intent(this, InputActivity.class);
        intent.putExtra("initial", -1);
        startActivity(intent);
    }

    public void addLocation(View view) {
        Intent intent = new Intent(this, InputActivity.class);
        startActivity(intent);
    }
}