package com.example.team30.DataCalculators;

import androidx.core.util.Pair;

import com.example.team30.models.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Math;

public class Compass {
    private float myLong;

    private float myLat;
    private float myAngle;
    public static final double EARTH_RADIUS = 6371; // in km
    public static final double MILES_PER_KM = 0.621371; // conversion factor from km to miles


    private static Compass instance;

    public static  Compass singleton(){
        if(instance == null){
            instance = new Compass();
        }
        return instance;
    }

    public float getmyLong(){
        return myLong;
    }

    public float getMyLat(){
        return myLat;
    }

    public void setMyLong(float newLong){
        this.myLong = newLong;
    }

    public void  setMyLat(float newLat){
        this.myLat = newLat;
    }

    public float getMyAngle() {
        return myAngle;
    }

    public void setMyAngle(float myAngle) {
        this.myAngle = myAngle;
    }

    public void setCoords(Pair<Double, Double>coords){
        this.myLat = coords.first.floatValue();
        this.myLong = coords.second.floatValue();
    }

    public float calculateAngle(float longti, float lati){
        float y = longti - myLong;
        float x = lati - myLat;
        double angle = Math.atan(y/x) * 180/Math.PI;
        if(x < 0){
            angle = angle + 180;
        }
        if(x > 0 && y < 0){
            angle = angle + 360;
        }
        float newangle = (float)(angle);
        return newangle - myAngle;
    }

    public double calculateDistance(double friendLat, double friendLong) {

        double dLat = Math.toRadians(friendLat - myLat);
        double dLong = Math.toRadians(friendLong - myLong);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(myLat)) * Math.cos(Math.toRadians(friendLat)) *
                        Math.sin(dLong / 2) * Math.sin(dLong / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = EARTH_RADIUS * c;
        return distance * MILES_PER_KM;
    }

    public double zoom0radius(double distance){
        double radius = 0;

        if(distance <= 1){
            radius = distance/(1.0/40);
        }
        else if( 1< distance && distance <= 10){
            radius = (distance -1.0)/(9.0/40) + 40;
            System.out.println(radius);
        }
        else if(10 < distance && distance<= 500){
            radius =  (distance -10)/(490.0/40) + 80;
        }
        else if(distance > 500){
            if((distance -500)/(490.0/150.0) + 450 <= 600){
                radius = (distance -500)/(490.0/40) + 120;
            }
            else{
                radius = 160;
            }
        }
        return radius;
    }

    public double zoom1radius(double distance){
        double radius = 0;

        if(distance <= 1){
            radius = (distance/(1.0/53.3));
        }
        else if( 1< distance && distance <= 10){
            radius = (distance -1)/(9.0/53.3) + 53.3;
        }
        else if(10 < distance && distance<= 500){
            radius =  ((distance -10)/(490.0/53.4) + 106.6);
        }
        else if(distance > 500){
            radius = 160;
        }
        return radius;
    }


    public double zoom3radius(double distance){
        double radius = 0;

        if(distance <= 1){
            radius = (distance/(1.0/160));
        }
        else if( distance > 1){
            radius = 160;
        }
        return radius;
    }

    public double zoom2radius(double distance){
        double radius = 0;

        if(distance <= 1){
            radius = (distance/(1.0/80));
        }
        else if( 1< distance && distance <= 10){
            radius = (distance -1)/(9.0/80) + 80 ;
        }
        else if(distance > 10){
            radius = 160;
        }
        return radius;
    }


}