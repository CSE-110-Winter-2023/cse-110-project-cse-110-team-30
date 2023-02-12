package com.example.team30;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class InputActivity extends AppCompatActivity {
    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    private Future<Void> future;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
    }

    public void onSubmit(View view) {
        EditText nameView = findViewById(R.id.name);
        String name = String.valueOf(nameView.getText());

        EditText latitudeView = findViewById(R.id.latitude);
        Float latitude = Float.parseFloat(String.valueOf(latitudeView.getText()));
        EditText longitudeView = findViewById(R.id.longitude);

        Float longitude = Float.parseFloat(String.valueOf(longitudeView.getText()));

        //@TODO add dropdown menu for types
        String type = "parent";

        Coordinates coordinates = new Coordinates(latitude, longitude);
        Location location = new Location(name, type, coordinates);

        SharedPreferences data = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        this.future = backgroundThreadExecutor.submit(() -> {
            editor.putString(type + "Name", name);
            editor.putFloat(type + "Latitude", latitude);
            editor.putFloat(type + "Longitude", longitude);
            return null;
        });

        //@TODO we need to increment the counter by one
        //does not work for some reason
        int currInt = data.getInt("counter", 0);
        editor.putInt("counter", currInt+1);
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}