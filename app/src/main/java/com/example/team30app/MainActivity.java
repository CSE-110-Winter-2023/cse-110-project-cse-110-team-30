package com.example.team30app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // This is the shared preferences instance that will be used to store the location data.
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the shared preferences instance.
        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences = this.getSharedPreferences("locationData", Context.MODE_PRIVATE);

        // Check if there is any saved location data in the shared preferences.
        if (sharedPreferences.getString("parentName", null) != null &&
                sharedPreferences.getString("homeName", null) != null &&
                sharedPreferences.getString("friendName", null) != null) {
            // If there is saved data, launch the CompassActivity.
            Intent intent = new Intent(this, CompassActivity.class);
            startActivity(intent);
        } else {
            // If there is no saved data, launch the LocationAddressInputActivity.
            Intent intent = new Intent(this, LocationAddressInputActivity.class);
            startActivity(intent);
        }
    }
}
