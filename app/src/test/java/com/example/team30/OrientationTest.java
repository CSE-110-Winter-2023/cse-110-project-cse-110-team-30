package com.example.team30;

import static org.junit.Assert.assertEquals;

import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import com.example.team30.OrientationService;

//@RunWith(RobolectricTestRunner.class)
//public class OrientationTest {
//    @Rule
//    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
//
//    @Test
//    public void test_orientation_service() {
//        var testValue = Constants.SOUTH;
//
//        var scenario = ActivityScenario.launch(MainActivity.class);
//        scenario.moveToState(Lifecycle.State.STARTED);
//        scenario.onActivity(activity -> {
//            var orientationService = OrientationService.singleton(activity);
//
//            var mockOrientation = new MutableLiveData<Float>();
//            orientationService.setMockOrientationData(mockOrientation);
//            // We don't want to have to do this! It's not our job to tell the activity!
//            activity.reobserveOrientation();
//
//            mockOrientation.setValue(testValue);
//            TextView textView = activity.findViewById(R.id.orientationText);
//
//            var expected = Utilities.formatOrientation(testValue);
//            var observed = textView.getText().toString();
//            assertEquals(expected, observed);
//        });
//    }
//}

//import static org.junit.Assert.assertEquals;
//
//import android.widget.TextView;
//import androidx.lifecycle.Lifecycle;
//import androidx.lifecycle.MutableLiveData;
//import androidx.test.core.app.ActivityScenario;
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.robolectric.RobolectricTestRunner;
//
//import com.example.team30.OrientationService;
//
//@RunWith(RobolectricTestRunner.class)
//public class OrientationTest {
//    @Rule
//    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
//
////    @Rule
////    public GrantPermissionRule mGrantPermissionRule =
////            GrantPermissionRule.grant(
////                    "android.permission.ACCESS_FINE_LOCATION");
//    @Test
//    public void test_orientation_service() {
//        var testValue = Constants.SOUTH;
//
//        var scenario = ActivityScenario.launch(MainActivity.class);
//        scenario.moveToState(Lifecycle.State.STARTED);
//        scenario.onActivity(activity -> {
//            var orientationService = OrientationService.singleton(activity);
//
//            var mockOrientation = new MutableLiveData<Float>();
//            orientationService.setMockOrientationData(mockOrientation);
//            // We don't want to have to do this! It's not our job to tell the activity!
////            activity.reobserveOrientation();
//
//            mockOrientation.setValue(testValue);
////            TextView textView = activity.findViewById(R.id.orientationText);
//
//            var expected = Utilities.formatOrientation(testValue);
////            var observed = textView.getText().toString();
//            assertEquals(expected, orientationService.getOrientation());
//        });
//    }
//}

