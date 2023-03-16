package com.example.team30;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class BddRegister {



    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION");
    @Test
    public void testLaunchRegistrationActivity() {
        // Check if the user has not registered before
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext());
        boolean isRegistered = preferences.getBoolean("register", false);


        String uniqueId = preferences.getString("YourUID", null);
        String privateCode = preferences.getString("privateCode", null);
        boolean newFriend = preferences.getBoolean("newFriend", false);
        // Launch the MainActivity using ActivityScenario



        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);


        if (!isRegistered) {


            // Verify that the RegistrationActivity is launched



            ActivityScenario<RegistrationActivity> registrationScenario = ActivityScenario.launch(RegistrationActivity.class);

            Intents.init();
            registrationScenario.onActivity(activity -> {
                // Check that the UI has been set up correctly
                assert(activity.findViewById(R.id.UID_generator)) != null;
            });

            // Click the "UID_generator" button to register the user
            onView(withId(R.id.UID_generator)).perform(click());

            // Verify that the UIDGeneration activity is launched
            intended(hasComponent(UIDGeneration.class.getName()));

            // Close the RegistrationActivity scenario
            registrationScenario.close();
        }



        // Close the MainActivity scenario
        scenario.close();
    }
}









