package com.example.team30;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.team30.DataCalculators.Compass;
import com.example.team30.DataCalculators.LocationService;
import com.example.team30.models.LocationViewModel;

import java.util.UUID;

public class RegistrationActivity extends AppCompatActivity {
    private LocationService locationService;
    private Compass compass = Compass.singleton();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        SharedPreferences data = getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        locationService = LocationService.singleton(this);
        Pair<Double, Double> coordinates = new Pair<>(0.0,0.0);
        //Pair<Double, Double> coordinates = locationService.getLocation().getValue();
        if(coordinates != null) {
            compass.setMyLat(coordinates.first.floatValue());
            compass.setMyLong(coordinates.second.floatValue());
        }
        else {
            compass.setMyLat(0);
            compass.setMyLong(0);
        }
        LocationViewModel viewModel = setupViewModel();

        Button button = findViewById(R.id.UID_generator);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uniqueID = UUID.randomUUID().toString();
                String privateCode = UUID.randomUUID().toString();

                editor.putBoolean("register", true);
                editor.putInt("zoom level", 2);
                editor.putString("YourUID", uniqueID);
                editor.putString("privateCode", privateCode);
                editor.putBoolean("newFriend", false);
                editor.apply();

                viewModel.register(uniqueID, privateCode, coordinates);
                Intent intent = new Intent(RegistrationActivity.this, UIDGeneration.class);
                intent.putExtra("uniqueID", uniqueID); //pass UID to UIDGeneration
                startActivity(intent);
            }
        });
    }

    private LocationViewModel setupViewModel() {
        return new ViewModelProvider(this).get(LocationViewModel.class);
    }
}