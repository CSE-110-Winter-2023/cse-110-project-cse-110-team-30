package com.example.team30;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UIDGeneration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uidgeneration);
        Button button = findViewById(R.id.button);

        // Set a click listener for the button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to launch the new activity
                Intent intent = new Intent(UIDGeneration.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}