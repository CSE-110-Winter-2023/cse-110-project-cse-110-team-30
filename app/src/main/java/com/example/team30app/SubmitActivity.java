package com.example.team30app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SubmitActivity extends AppCompatActivity {
    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    private Future<Void> future;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        Bundle extras = getIntent().getExtras();
        SharedPreferences data = getPreferences(MODE_PRIVATE);
        this.future = backgroundThreadExecutor.submit(() -> {
            if(extras.getBoolean("parent")){
                final TextView name, longitude, latitude;
                name = findViewById(R.id.parentName);
                longitude = findViewById(R.id.parentLongitude);
                latitude = findViewById(R.id.parentLatitude);
                runOnUiThread(() -> {
                    name.setText(data.getString("parentName", ""));
                    longitude.setText(String.valueOf(data.getFloat("parentLongitude", 0)));
                    latitude.setText(String.valueOf(data.getFloat("parentLatitude", 0)));
                });
            }
            if(extras.getBoolean("home")){
                final TextView name, longitude, latitude;
                name = findViewById(R.id.homeName);
                longitude = findViewById(R.id.homeLongitude);
                latitude = findViewById(R.id.homeLatitude);
                runOnUiThread(() -> {
                    name.setText(data.getString("homeName", ""));
                    longitude.setText(String.valueOf(data.getFloat("homeLongitude", 0)));
                    latitude.setText(String.valueOf(data.getFloat("homeLatitude", 0)));
                });
            }
            if(extras.getBoolean("friend")){
                final TextView name, longitude, latitude;
                name = findViewById(R.id.friendName);
                longitude = findViewById(R.id.friendLongitude);
                latitude = findViewById(R.id.friendLatitude);
                runOnUiThread(() -> {
                    name.setText(data.getString("friendName", ""));
                    longitude.setText(String.valueOf(data.getFloat("friendLongitude", 0)));
                    latitude.setText(String.valueOf(data.getFloat("friendLatitude", 0)));
                });
            }
            return null;
        });
    }

    public void onSubmit(View view) {
        Intent intent = new Intent(this, CompassActivity.class);
        finish();
        startActivity(intent);
    }
}
