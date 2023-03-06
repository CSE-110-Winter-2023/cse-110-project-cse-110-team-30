package com.example.team30;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UIDGeneration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uidgeneration);

        //get the UID passed in by RegistrationActivity
        Intent intent = getIntent();
        String uniqueID = intent.getStringExtra("uniqueID");

        TextView newUID = findViewById(R.id.NewUIDDisplay);
        newUID.setText(uniqueID);

        Button button = findViewById(R.id.GoToCompass);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UIDGeneration.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}