package com.example.team30app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LocationAddressInputActivity extends AppCompatActivity {

    // Define keys for SharedPreferences
    private final String FAMILY_LOCATION_NAME = "FAMILY_LOCATION_NAME";
    private final String FAMILY_LOCATION_LATITUDE = "FAMILY_LOCATION_LATITUDE";
    private final String FAMILY_LOCATION_LONGITUDE = "FAMILY_LOCATION_LONGITUDE";
    private final String PERSONAL_LOCATION_NAME = "PERSONAL_LOCATION_NAME";
    private final String PERSONAL_LOCATION_LATITUDE = "PERSONAL_LOCATION_LATITUDE";
    private final String PERSONAL_LOCATION_LONGITUDE = "PERSONAL_LOCATION_LONGITUDE";
    private final String FRIEND_LOCATION_NAME = "FRIEND_LOCATION_NAME";
    private final String FRIEND_LOCATION_LATITUDE = "FRIEND_LOCATION_LATITUDE";
    private final String FRIEND_LOCATION_LONGITUDE = "FRIEND_LOCATION_LONGITUDE";

    private SharedPreferences sharedPreferences;
    private EditText locationNameEditText;
    private EditText latitudeEditText;
    private EditText longitudeEditText;

    private int formCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_address_input);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("locationData", Context.MODE_PRIVATE);

        // Initialize Views
        locationNameEditText = findViewById(R.id.editTextType);
        latitudeEditText = findViewById(R.id.editTextLatitude);
        longitudeEditText = findViewById(R.id.editTextLongitude);
        Button nextButton = findViewById(R.id.buttonNext);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the data from the EditTexts
                String locationName = locationNameEditText.getText().toString().trim();
                String latitudeString = latitudeEditText.getText().toString().trim();
                String longitudeString = longitudeEditText.getText().toString().trim();

                // Check if the EditTexts are empty
                if (locationName.isEmpty() || latitudeString.isEmpty() || longitudeString.isEmpty()) {

                    Toast.makeText(LocationAddressInputActivity.this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
                } else {
// Convert the latitude and longitude to double
                    double latitude = Double.parseDouble(latitudeString);
                    double longitude = Double.parseDouble(longitudeString);                // Save the data to SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    switch (formCounter) {
                        case 0:
                            editor.putString(FAMILY_LOCATION_NAME, locationName);
                            editor.putFloat(FAMILY_LOCATION_LATITUDE, (float) latitude);
                            editor.putFloat(FAMILY_LOCATION_LONGITUDE, (float) longitude);
                            break;
                        case 1:
                            editor.putString(PERSONAL_LOCATION_NAME, locationName);
                            editor.putFloat(PERSONAL_LOCATION_LATITUDE, (float) latitude);
                            editor.putFloat(PERSONAL_LOCATION_LONGITUDE, (float) longitude);
                            break;
                        case 2:
                            editor.putString(FRIEND_LOCATION_NAME, locationName);
                            editor.putFloat(FRIEND_LOCATION_LATITUDE, (float) latitude);
                            editor.putFloat(FRIEND_LOCATION_LONGITUDE, (float) longitude);
                            break;
                    }
                    editor.apply();

                    // Increment formCounter
                    formCounter++;

                    // If all the forms have been filled, start the SubmitActivity
                    if (formCounter == 3) {
                        Intent intent = new Intent(LocationAddressInputActivity.this, SubmitActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Clear the EditTexts
                        locationNameEditText.setText("");
                        latitudeEditText.setText("");
                        longitudeEditText.setText("");
                    }
                }
            }
        });
    }

}
