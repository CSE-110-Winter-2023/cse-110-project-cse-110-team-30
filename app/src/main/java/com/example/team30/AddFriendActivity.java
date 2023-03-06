package com.example.team30;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);

        Button button = findViewById(R.id.submitBtn);

        // Set a click listener for the button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to launch the new activity
                Intent intent = new Intent(AddFriendActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}