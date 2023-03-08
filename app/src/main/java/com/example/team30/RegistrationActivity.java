package com.example.team30;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.team30.models.API;
import com.example.team30.models.FriendDao;
import com.example.team30.models.FriendDatabase;
import com.example.team30.models.FriendViewModel;
import com.example.team30.models.Location;
import com.example.team30.models.LocationViewModel;
import com.example.team30.models.Repository;

import java.util.UUID;

public class RegistrationActivity extends AppCompatActivity {
    private LocationService locationService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Button button = findViewById(R.id.UID_generator);
        SharedPreferences data = getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        locationService = LocationService.singleton(this);
        Pair<Double, Double> coordinates = locationService.getLocation().getValue();
        LocationViewModel viewModel = setupViewModel();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uniqueID = UUID.randomUUID().toString();
                String privateCode = UUID.randomUUID().toString();

                editor.putBoolean("register", true);
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