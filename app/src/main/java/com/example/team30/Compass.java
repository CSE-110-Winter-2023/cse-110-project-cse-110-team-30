package com.example.team30;

import android.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.lang.Math;

public class Compass {
    private float myLong;

    private float myLat;

    private Map<String, String> IDlist;

    private Map<String, Pair<Float, Integer>> position;

    private static Compass instance;

    public static  Compass singleton(){
        if(instance == null){
            instance = new Compass();
        }
        return instance;
    }

    public Compass(){
        IDlist = new HashMap<>();
        position = new HashMap<>();
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

    public void calculateAngles(){

    }

    public void addPosition(String objectId, Pair<Float,Integer> newPair){
        position.put(objectId, newPair);
    }


    public Pair<Float, Integer> getPosition(String objectId){
        return position.get(objectId);
    }

    public void setPosition(String objectId, Pair<Float, Integer> newPair){
        position.put(objectId, newPair);
    }


    public void addIDObject(String UID, String objectId){

    }
}