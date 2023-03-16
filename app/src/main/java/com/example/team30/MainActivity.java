package com.example.team30;

import static com.example.team30.R.id.compressPage;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.CircularFlow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.team30.models.Friend;
import com.example.team30.models.Location;
import com.example.team30.models.LocationViewModel;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private OrientationService orientationService;
    private LocationService locationService;
    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    private Future<Void> future;
    private Compass compass;
    private long lastLocationTime;
    private Map<String, Integer> dotList = new HashMap<>();
    private Map<String, Integer> labelList = new HashMap<>();
    private Map<Integer, Integer> CircleList = new HashMap<>();
    private ConstraintLayout map;
    private CircularFlow flow;
    private final Integer[] CircleScal = {0, 1, 10, 500, 1000};
    private final int CompassWidth = 1000;
    private Set<String> friendsList;

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Check for and get location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

        lastLocationTime = System.currentTimeMillis();
        map = findViewById(compressPage);
        compass = Compass.singleton();
        locationService = LocationService.singleton(this);
        orientationService = OrientationService.singleton(this);
        SharedPreferences data = getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        TextView redDot = findViewById(R.id.RecDot);
        TextView greenDot = findViewById(R.id.GreenDot);
        TextView timer = findViewById(R.id.timer);
        Button Zoom_in = findViewById(R.id.zoom_in);
        Button Zoom_out = findViewById(R.id.zoom_out);

        var viewModel = setupViewModel();

        if(data.getBoolean("register", false) == false){
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
        }

        Intent intent = getIntent();
        String userID = data.getString("YourUID","");
        String privateCode = data.getString("privateCode","");

        //Update the location of user and all relation point
        int LargesSize = map.getWidth();
        System.out.println(LargesSize);

        editor.putInt("CircleSize", 2);
        editor.apply();
        int currCircleNum = data.getInt("CircleSize", 2);
        CircleList.put(4, findViewById(R.id.innerCircle1).getId());
        CircleList.put(3, findViewById(R.id.innerCircle2).getId());
        CircleList.put(2, findViewById(R.id.innerCircle3).getId());
        CircleList.put(1, findViewById(R.id.innerCircle4).getId());
        for(int i = 4; i > 0; i--){
            ConstraintSet ConSet = new ConstraintSet();
            ConSet.clone(map);
            int currCircle = CircleList.get(i);
            if(currCircleNum < i){
                Log.i("Invisible the circle", currCircleNum + "No show"+i);
                ConSet.setVisibility(currCircle,ConstraintSet.INVISIBLE);
            }else{
                Log.i("Add new circle", String.valueOf(CompassWidth*i/currCircleNum));
                ConSet.setVisibility(currCircle,ConstraintSet.VISIBLE);
                ConSet.constrainWidth(currCircle, CompassWidth*i/currCircleNum);
                ConSet.constrainHeight(currCircle, CompassWidth*i/currCircleNum);
            }
            ConSet.applyTo(map);
//            Log.i("CircleCreat", i + String.valueOf(firstCircle.getWidth()));
        }

//        viewModel.getUID().observe(this, UIDlist->{
            locationService.getLocation().observe(this, coords ->{
//                Log.i("MainActivity", "Observe location");
                    //Update the GPS connection signal
                lastLocationTime = System.currentTimeMillis();
                redDot.setVisibility(View.INVISIBLE);
                timer.setVisibility(View.INVISIBLE);
                greenDot.setVisibility(View.VISIBLE);

                viewModel.updateUserLocation(userID, privateCode, coords.first.floatValue(),coords.second.floatValue());
//                Log.i("MainActivity", "add dot start");
//                Log.i("NewFriendList", String.valueOf(friendsList));
                viewModel.getLocations().observe(this, friends->{
//                    Log.i("MainActivity", "ViewModel update"+friends);
                    orientationService.getOrientation().observe(this, orientation ->{
                        createDot(friends, coords, orientation);

                    });
                });
            });
//        });
//        viewModel.getLocations(friendsList).observe(this, friends->{
//            locationService.getLocation().observe(this, coords ->{
//                //Update the GPS connection signal
//                lastLocationTime = System.currentTimeMillis();
//                redDot.setVisibility(View.INVISIBLE);
//                timer.setVisibility(View.INVISIBLE);
//                greenDot.setVisibility(View.VISIBLE);
//                viewModel.updateUserLocation(userID, privateCode, coords.first.floatValue(),coords.second.floatValue());
//                orientationService.getOrientation().observe(this, orientation ->{
//                    creatDot(friends, coords, orientation);
//                });
//            });
//        });

        //Update the GPS lost signal - each min
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            long currTime = System.currentTimeMillis();
            float lostTimeInMin = (float)(currTime - lastLocationTime)/60000;
            //lost signal time greater than 1 min
            System.out.println("Lost time (m)" + String.valueOf(lostTimeInMin) + "Last Update: " + String.valueOf(lastLocationTime));
            if (lostTimeInMin >= 1){
                String label = String.valueOf((int)lostTimeInMin) + "m";
                //UIThread of change GPS lost signal UI
                runOnUiThread(()->{
                    timer.setText(label);
                    redDot.setVisibility(View.VISIBLE);
                    timer.setVisibility(View.VISIBLE);
                    greenDot.setVisibility(View.INVISIBLE);
                });
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    public void addFriend(View view) {
        Intent intent = new Intent(MainActivity.this, AddFriendActivity.class);
        startActivity(intent);
    }

    public void ZoomIn(View view){
        SharedPreferences data = getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        int currCircleNum = data.getInt("CircleSize", 2) - 1;
        if(currCircleNum <= 1){
            view.setBackgroundColor(Color.DKGRAY);
            if(currCircleNum < 1){
                return;
            }
        }else{
            Button zoomout = findViewById(R.id.zoom_out);
            zoomout.setBackgroundColor(Color.blue(20));
            view.setBackgroundColor(Color.blue(20));
        }
        editor.putInt("CircleSize", currCircleNum);
        editor.apply();
        ConstraintSet ConSet = new ConstraintSet();
        ConSet.clone(map);
        Log.i("ZOOMIN", "Change Circle to " + currCircleNum);
        for (Map.Entry<Integer, Integer> m : CircleList.entrySet()){
            if(m.getKey() > currCircleNum){
                Log.i("Invisible the circle", currCircleNum + ": No show" + m.getKey());
                ConSet.setVisibility(m.getValue(),ConstraintSet.INVISIBLE);
            }else{
                Log.i("Add new circle", currCircleNum + ": Reposition circle" + m.getKey());
                ConSet.setVisibility(m.getValue(),ConstraintSet.VISIBLE);
                ConSet.constrainWidth(m.getValue(), CompassWidth*m.getKey()/currCircleNum);
                ConSet.constrainHeight(m.getValue(), CompassWidth*m.getKey()/currCircleNum);
            }
        }
        ConSet.applyTo(map);
    }

    public void ZoomOut(View view){
        SharedPreferences data = getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        int currCircleNum = data.getInt("CircleSize", 2) + 1;
        if(currCircleNum >= 4){
            view.setBackgroundColor(Color.DKGRAY);
            if(currCircleNum > 4){
                return;
            }
        }else{
            Button zoomin = findViewById(R.id.zoom_in);
            zoomin.setBackgroundColor(Color.blue(20));
            view.setBackgroundColor(Color.blue(20));
        }
        editor.putInt("CircleSize", currCircleNum);
        editor.apply();
        System.out.println(data.getInt("CircleSize", 2));

        ConstraintSet ConSet = new ConstraintSet();
        ConSet.clone(map);
        Log.i("ZOOMOUT", "Zoom out to "+currCircleNum);
        for (Map.Entry<Integer, Integer> m : CircleList.entrySet()){
            if(m.getKey() > currCircleNum){
                Log.i("Invisible the circle", currCircleNum + ": No show" + m.getKey());
                ConSet.setVisibility(m.getValue(),ConstraintSet.INVISIBLE);
            }else{
                Log.i("Add new circle", currCircleNum + ": Reposition circle" + m.getKey());
                ConSet.setVisibility(m.getValue(),ConstraintSet.VISIBLE);
                ConSet.constrainWidth(m.getValue(), CompassWidth*m.getKey()/currCircleNum);
                ConSet.constrainHeight(m.getValue(), CompassWidth*m.getKey()/currCircleNum);
            }
        }
        ConSet.applyTo(map);
    }

    private Button makeButton(Location location){
        Button button = new Button(this);
        button.setText("Click me");
        button.setId(View.generateViewId());
        button.setBackgroundColor(Color.BLUE);
        //button.setPadding(16, 8, 16, 8);

        // Set the size of the button
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        params.width = 100;
        params.height = 100;
        params.dimensionRatio = "1:1";

        //params.circleAngle = compass.calculateAngle(location.getLatitude(), location.getLongitude());
        //params.circleRadius = 200;

        button.setLayoutParams(params);
        button.setTag(location.getPublic_code());
        return button;
    }

    private LocationViewModel setupViewModel() {
        return new ViewModelProvider(this).get(LocationViewModel.class);
    }


    private ImageView setCircle(int radius, int visible){
        ImageView innerCircle = new ImageView(this);
        innerCircle.setImageResource(R.drawable.circle);
        innerCircle.setId(View.generateViewId());
        innerCircle.setVisibility(visible);
        ConstraintLayout.LayoutParams layout = new ConstraintLayout.LayoutParams(radius,radius);
        layout.topMargin = -100;
        layout.topToTop = ConstraintSet.PARENT_ID;
        layout.bottomToBottom = ConstraintSet.PARENT_ID;
        layout.startToStart = ConstraintSet.PARENT_ID;
        layout.endToEnd = ConstraintSet.PARENT_ID;
        innerCircle.setLayoutParams(layout);
//        System.out.println(innerCircle.getWidth() + radius);
        return innerCircle;
    }

    private TextView addDot(float angle, int radius, String text, int TextSize){
        TextView textView = new TextView(this);
        textView.setId(View.generateViewId());
        textView.setText(text); // Set the text of the TextView to a dot
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TextSize); // Set the text size to 40sp
        textView.setTextColor(Color.BLACK); // Set the text color to white
        textView.setGravity(Gravity.CENTER); // Center the dot horizontally and vertically
        textView.setElevation(100);
        ConstraintLayout.LayoutParams layout = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        layout.circleRadius = radius;
        layout.circleConstraint = R.id.triangle;
        layout.circleAngle = angle;
        layout.topMargin = -100; // adjust the value to position the dot as desired
        layout.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layout.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        layout.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        layout.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        textView.setLayoutParams(layout);
        return textView;
    }

    private void createDot(List<Friend> friends, Pair<Double,Double> coords,float orientation) {
        if(friends == null) return;
        int lenFriends = friends.size();
        for (int i = 0; i < lenFriends; i++) {
            Friend friend = friends.get(i);
            Pair<Float,Integer> AngleRadius = AngleRadius(friend, coords, orientation);
//            Log.i("CreatDot", friend.public_code + AngleRadius + friend.longitude + friend.latitude);
            int LabelVisible = 0;
            if(AngleRadius.second ==  CompassWidth/2){
                LabelVisible = 4;
            }
            if (dotList.get(friend.public_code) == null){
                TextView friendDot = addDot(AngleRadius.first, AngleRadius.second, "•", 40);
                TextView friendLabel = addDot(AngleRadius.first, AngleRadius.second-10, friend.label, 10);
                dotList.put(friend.public_code, friendDot.getId());
                labelList.put(friend.public_code, friendLabel.getId());
//                friendLabel.setVisibility(LabelVisible);
                map.addView(friendDot);
                map.addView(friendLabel);
                map.requestLayout();
                Log.i("Add Dot", "Add successful");
            }else{
//                Log.i("Observe data", "RePosition");
                ConstraintSet ConSet = new ConstraintSet();
                ConSet.clone(map);
                ConSet.setVisibility(labelList.get(friend.public_code), LabelVisible);
                ConSet.constrainCircle(labelList.get(friend.public_code), R.id.triangle,
                        AngleRadius.second, AngleRadius.first-10);
                ConSet.setElevation(labelList.get(friend.public_code), -1000);

                ConSet.constrainCircle(dotList.get(friend.public_code), R.id.triangle,
                        AngleRadius.second, AngleRadius.first);
                ConSet.setElevation(dotList.get(friend.public_code), -1000);

                ConSet.applyTo(map);
            }
        }
    }

    private Pair<Float,Integer> AngleRadius(Friend friend, Pair<Double,Double> myLoc, float orientation){
        //float orientation
        float circleRadius = 100;
        float longti = friend.longitude;
        float lati = friend.latitude;

        float y = (float) (longti - myLoc.second);
        float x = (float) (lati - myLoc.first);

        double angle = Math.atan(y/x) * 180/Math.PI;
        if(x < 0){
            angle = angle + 180;
        }
        if(x > 0 && y < 0){
            angle = angle + 360;
        }

        float newangle = (float)(angle - (float) (orientation * 180/Math.PI));
        int newRadius = (int) (Math.sqrt(x*x + y*y));

        Pair<Float, Integer> newPair = new Pair<>(newangle, RadiusByCircle(newRadius));
        return newPair;
    }

    private Integer RadiusByCircle(int initalRadius){
        SharedPreferences data = getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        int currCircle = data.getInt("CircleSize", 2);
        if(initalRadius > CircleScal[currCircle]){
//            Log.i("OverBound", "Return upBound" + CompassWidth/2);
            return CompassWidth/2;
        }
        int unitCirclewidth = CompassWidth/currCircle/2;
        for(int i = currCircle-1; i >= 0; i--){
//            Log.i("Radius recalculator", initalRadius + "Change");
            if(initalRadius > CircleScal[i] && initalRadius < CircleScal[i+1]){
//                Log.i("Radius recalculator", initalRadius + "Change");
                int radius = (initalRadius - CircleScal[i])*(unitCirclewidth)/(CircleScal[i+1] - CircleScal[i]);

                radius = radius + i*unitCirclewidth;
//                Log.i("Radius recalculator", initalRadius + " Change to " + radius);
//                Log.i("Radius recalculator", unitCirclewidth + " has to " + radius);
//                Log.i("Radius recalculator",  CircleScal[i] + " Change to " + CircleScal[i+1]);

                return radius;
            }
        }
        return null;
    }
}
