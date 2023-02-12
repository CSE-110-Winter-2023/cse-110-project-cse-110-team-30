package com.example.team30;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences data = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        //editor.putInt("counter", 0);
        //put 1 for testing
        editor.putInt("counter", 1);
        editor.apply();

        if(data.getInt("counter", 0) == 0)
            initialInput();
    }

    public void initialInput(){
        Intent intent = new Intent(this, InputActivity.class);
        startActivity(intent);
    }

    public void addLocation(View view) {
        Intent intent = new Intent(this, InputActivity.class);
        startActivity(intent);
    }
}