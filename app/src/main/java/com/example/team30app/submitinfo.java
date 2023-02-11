package com.example.team30app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SubmitActivity extends AppCompatActivity {

    private EditText locationNameEditText;
    private EditText locationLongitudeEditText;
    private EditText locationLatitudeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitinfo);

        locationNameEditText = findViewById(R.id.location_name);
        locationLongitudeEditText = findViewById(R.id.location_longitude);
        locationLatitudeEditText = findViewById(R.id.location_latitude);


        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String locationName = locationNameEditText.getText().toString();
                String locationLongitude = locationLongitudeEditText.getText().toString();
                String locationLatitude = locationLatitudeEditText.getText().toString();

                if (isValidInformation(locationName, locationLongitude, locationLatitude)) {
                    Intent intent = new Intent(SubmitActivity.this, CompassActivity.class);
                    intent.putExtra("location_name", locationName);
                    intent.putExtra("location_longitude", locationLongitude);
                    intent.putExtra("location_latitude", locationLatitude);
                    startActivity(intent);
                } else {
                    Toast.makeText(SubmitActivity.this, "Invalid information. Please enter valid information.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidInformation(String locationName, String locationLongitude, String locationLatitude) {
        // Add validation logic here
        return true;
    }
}
