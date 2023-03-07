import androidx.appcompat.app.AppCompatActivity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.team30.LocationService;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.provider.Settings;

public class LocationServiceTest {
    @Test
    public void testHasGpsSignal() throws InterruptedException {
        // Get the app context
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        // Create a new instance of LocationService
        LocationService locationService = LocationService.singleton((AppCompatActivity) appContext);
        // Wait for the GPS signal to be acquired
        Thread.sleep(5000); // Wait for 5 seconds
        // Check that hasGpsSignal() returns true
        assertTrue(locationService.hasGpsSignal());
        // Turn off the GPS location service
        // Note: This requires permission android.permission.WRITE_SECURE_SETTINGS
        Settings.Secure.putString(appContext.getContentResolver(), Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF);
        // Wait for at least one minute
        Thread.sleep(61000); // Wait for 61 seconds
        // Check that hasGpsSignal() returns false
        assertFalse(locationService.hasGpsSignal());

        // Turn on the GPS location service
        Settings.Secure.putString(appContext.getContentResolver(), Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_HIGH_ACCURACY);
    }
}
