package com.example.team30;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Looper;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.security.Provider;
import java.util.Arrays;


public class LocationService implements LocationListener {
    private static final long SIGNAL_LOST_TIME_MS = 60 * 1000;
    final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };


    // This needs to be more specific than just Activity for location permissions requesting.
    private final AppCompatActivity activity;

    private static LocationService instance;

    private MutableLiveData<Pair<Double, Double>> locationValue;

    private final LocationManager locationManager;


    public static LocationService singleton(AppCompatActivity activity) {
        if (instance == null) {
            instance = new LocationService(activity);
        }
        return instance;
    }

    /**
     * Constructor for LocationService
     *
     * @param activity Context needed to initiate LocationManager
     */
    protected LocationService(AppCompatActivity activity) {
        this.locationValue = new MutableLiveData<>();
        this.activity = activity;
        this.locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        // Register sensor listeners
        withLocationPermissions(this::registerLocationListener);
    }

    /**  This will only be called when we for sure have permissions. */
    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    private void registerLocationListener() {
        this.locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0,
                LocationService.this);
    }

    /** Utility method for doing something with location permissions if we have them, and
     *  after asking for them if we don't already.
     * @param action the thing to do that needs permissions.
     */
    private void withLocationPermissions(Runnable action) {
        if (Arrays.stream(REQUIRED_PERMISSIONS).allMatch(perm -> activity.checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED)) {
            // We already have at least one of the location permissions, go ahead!
            action.run();
        } else {
            // We need to ask for permission first.
            // This is the call that requires AppCompatActivity and not just Activity!
            var launcher = activity.registerForActivityResult(new RequestMultiplePermissions(), grants -> {
                // At least one of the values in the Map<String, Boolean> grants needs to be true.
                if (grants.values().stream().noneMatch(isGranted -> isGranted)) {
                    // If you've landed here by denying it, you should grant it manually in settings or wipe data.
                    throw new IllegalStateException("App needs you to grant at least one location permission!");
                }
                // We have permission now, carry on!
                action.run();
            });
            launcher.launch(REQUIRED_PERMISSIONS);
        }
    }


    @Override
    public void onLocationChanged(Location location) {

//        signalLostHandler.removeCallbacks(signalLostRunnable);
//        signalLostHandler.postDelayed(signalLostRunnable, 60 * 1000);
        this.locationValue.postValue(new Pair<>(location.getLatitude(), location.getLongitude()));
    }

    private void unregisterLocationListener() {
        locationManager.removeUpdates(this);
    }

    public LiveData<Pair<Double, Double>> getLocation() {
        return this.locationValue;
    }

    public void setMockOrientationData(MutableLiveData<Pair<Double, Double>> mockData) {
        unregisterLocationListener();
        this.locationValue = mockData;
    }
}