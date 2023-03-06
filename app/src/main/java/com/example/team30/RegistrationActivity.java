package com.example.team30;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.UUID;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Button button = findViewById(R.id.UID_generator);
        SharedPreferences data = getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uniqueID = UUID.randomUUID().toString();

                editor.putBoolean("register", true);
                editor.putString("YourUID", uniqueID);
                editor.apply();

                Intent intent = new Intent(RegistrationActivity.this, UIDGeneration.class);
                intent.putExtra("uniqueID", uniqueID); //pass UID to UIDGeneration
                startActivity(intent);
            }
        });
    }
}