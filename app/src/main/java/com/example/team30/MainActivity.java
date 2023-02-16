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
    private Compass compass;
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
        compass = Compass.singleton();
        SharedPreferences data = getSharedPreferences("test", MODE_PRIVATE);

        if(data.getInt("counter", -1) == -1)
            initialInput();
        populateCompass(data);
        locationService.getLocation().observe(this, coords ->{
           reposition(coords);
        });
    }

    public void populateCompass(SharedPreferences data){
        String[] types = {"Parent", "Friend", "Home"};
        for(String type : types){
            if(data.getBoolean(type, false)){
                if(!compass.hasLocation(type)){
                    Coordinates c = new Coordinates(data.getFloat(type + "Latitude", 0), data.getFloat(type + "Longitude", 0));
                    Location l = new Location(type, data.getString(type + "Name", ""), c);
                    compass.addLocation(l);
                }
                setVisibility(type, data.getString(type + "Name", ""));
            }
        }
    }

    private void reposition(Coordinates coords){
        if(compass.hasLocation("Parent")){
            float ParentAngle = (float) compass.calculateAngleWithDistance("Parent", coords);

            ImageView pic = findViewById(R.id.house);
            ConstraintLayout.LayoutParams layout = (ConstraintLayout.LayoutParams) pic.getLayoutParams();
            layout.circleAngle = ParentAngle;
            pic.setLayoutParams(layout);

            TextView text = findViewById(R.id.parentLabel);
            ConstraintLayout.LayoutParams layoutText = (ConstraintLayout.LayoutParams) text.getLayoutParams();
            layoutText.circleAngle = ParentAngle;
            text.setLayoutParams(layoutText);

        }
        if(compass.hasLocation("Friend")){
            float FriendAngle = (float) compass.calculateAngleWithDistance("Friend", coords);

            ImageView pic = findViewById(R.id.Friendhome);
            ConstraintLayout.LayoutParams layout = (ConstraintLayout.LayoutParams) pic.getLayoutParams();
            layout.circleAngle = FriendAngle;
            pic.setLayoutParams(layout);

            TextView text = findViewById(R.id.friendLabel);
            ConstraintLayout.LayoutParams layoutText = (ConstraintLayout.LayoutParams) text.getLayoutParams();
            layoutText.circleAngle = FriendAngle;
            text.setLayoutParams(layoutText);
        }
        if(compass.hasLocation("Home")){
            float MyAngle = (float) compass.calculateAngleWithDistance("Home", coords);

            ImageView pic = findViewById(R.id.Myhome);
            ConstraintLayout.LayoutParams layout = (ConstraintLayout.LayoutParams) pic.getLayoutParams();
            layout.circleAngle = MyAngle;
            pic.setLayoutParams(layout);

            TextView text = findViewById(R.id.myLabel);
            ConstraintLayout.LayoutParams layoutText = (ConstraintLayout.LayoutParams) text.getLayoutParams();
            layoutText.circleAngle = MyAngle;
            text.setLayoutParams(layoutText);
        }
    }

    private void setVisibility(String type, String name){
        if(type.equals("Parent")){
            ImageView pic = findViewById(R.id.house);
            pic.setVisibility(View.VISIBLE);
            TextView text = findViewById(R.id.parentLabel);
            text.setVisibility(View.VISIBLE);
            text.setText(name);
        }
        else if(type.equals("Friend")){
            ImageView pic = findViewById(R.id.Friendhome);
            pic.setVisibility(View.VISIBLE);
            TextView text = findViewById(R.id.friendLabel);
            text.setVisibility(View.VISIBLE);
            text.setText(name);
        }
        else{
            ImageView pic = findViewById(R.id.Myhome);
            pic.setVisibility(View.VISIBLE);
            TextView text = findViewById(R.id.myLabel);
            text.setVisibility(View.VISIBLE);
            text.setText(name);
        }
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