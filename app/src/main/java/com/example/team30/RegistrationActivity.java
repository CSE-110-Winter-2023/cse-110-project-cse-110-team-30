package com.example.team30;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;

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

//        Pair<Double, Double> coordinates = locationService.getLocation().getValue();
        Pair<Double, Double> coordinates = new Pair<Double, Double>(0.0,0.0);


        if(coordinates != null) {
            compass.setMyLat(coordinates.first.floatValue());
            compass.setMyLong(coordinates.second.floatValue());
        }

        else {
            compass.setMyLat(0);
            compass.setMyLong(0);
        }
        LocationViewModel viewModel = setupViewModel();

        TextView Name = findViewById(R.id.UsernameEntry);
        Button button = findViewById(R.id.UID_generator);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("Start generation", "uniqueID generation");
                String uniqueID = UUID.randomUUID().toString();
                String privateCode = UUID.randomUUID().toString();
                String userName = String.valueOf(Name.getText());
//                String uniqueID = "A17188813";
//                String privateCode = "A17188813";
                System.out.println("UserID"+ uniqueID);

                editor.putBoolean("register", true);
                editor.putString("YourUID", uniqueID);
                editor.putString("privateCode", privateCode);
                editor.putBoolean("newFriend", false);
                editor.apply();

                viewModel.register(uniqueID, privateCode, userName, coordinates.first.floatValue(),
                        coordinates.second.floatValue());
                Intent intent = new Intent(RegistrationActivity.this, UIDGeneration.class);
                intent.putExtra("uniqueID", uniqueID); //pass UID to UIDGeneration
                intent.putExtra("privateCode", privateCode); //pass UID to UIDGeneration
                startActivity(intent);
            }
        });
    }

    private LocationViewModel setupViewModel() {
        return new ViewModelProvider(this).get(LocationViewModel.class);
    }
}