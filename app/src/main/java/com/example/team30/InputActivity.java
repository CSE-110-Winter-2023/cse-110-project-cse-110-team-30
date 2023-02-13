package com.example.team30;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
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
        Spinner SpinnerCategory=findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        SpinnerCategory.setAdapter(adapter);
    }

    public void onSubmit(View view) {
        EditText nameView = findViewById(R.id.name);
        String name = String.valueOf(nameView.getText());

        EditText latitudeView = findViewById(R.id.latitude);
        Float latitude = Float.parseFloat(String.valueOf(latitudeView.getText()));

        EditText longitudeView = findViewById(R.id.longitude);
        Float longitude = Float.parseFloat(String.valueOf(longitudeView.getText()));

        Spinner SpinnerCategory=findViewById(R.id.spinner_category);
        String type = SpinnerCategory.getSelectedItem().toString();

        Coordinates coordinates = new Coordinates(latitude, longitude);
        Location location = new Location(name, type, coordinates);

        SharedPreferences data = getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        this.future = backgroundThreadExecutor.submit(() -> {
            editor.putString(type + "Name", name);
            editor.putFloat(type + "Latitude", latitude);
            editor.putFloat(type + "Longitude", longitude);
            return null;
        });

        Intent oldIntent = getIntent();
        int currInt = oldIntent.getIntExtra("initial", 0);
        // First location being made
        if(currInt == -1) {
            editor.putInt("counter", 1);
        }
        else {
            currInt = data.getInt("counter", -1);
            editor.putInt("counter", currInt + 1);
        }
        editor.putBoolean("newLocation", true);
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("newLocation", location);
        startActivity(intent);
    }
}