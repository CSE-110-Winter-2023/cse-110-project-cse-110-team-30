package com.example.team30;



import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class BddRegister {

    @Test
    public void testLaunchRegistrationActivity() {
        // Check if the user has not registered before
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext());
        boolean isRegistered = preferences.getBoolean("register", false);

        // Launch the MainActivity using ActivityScenario
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);


        if (!isRegistered) {
            

            // Verify that the RegistrationActivity is launched
            intended(hasComponent(RegistrationActivity.class.getName()));
        }

        // Close the MainActivity scenario
        scenario.close();
    }
}






