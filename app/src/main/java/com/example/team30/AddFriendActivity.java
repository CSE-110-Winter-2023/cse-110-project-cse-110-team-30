package com.example.team30;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.UUID;


public class AddFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);

        //add friend page would display your UID
        SharedPreferences data = getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        String yourUID = data.getString("YourUID", null);
        TextView yourUIDDisplay = findViewById(R.id.YourUIDDisplay);
        yourUIDDisplay.setText(yourUID);

        Button button = findViewById(R.id.submitBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Load the image from a file or URL
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dotme);

                Intent intent = new Intent(AddFriendActivity.this, MainActivity.class);
                // Pass the information about the image as an extra
                intent.putExtra("imageResourceId", R.drawable.dotme);
                startActivity(intent);

            }

        });
    }
}