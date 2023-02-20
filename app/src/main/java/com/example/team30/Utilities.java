package com.example.team30;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;

import java.util.Optional;

public class Utilities{
    public static void showAlert(Activity activity, String message){
        AlertDialog.Builder alerBuilder = new AlertDialog.Builder(activity);

        alerBuilder
                .setTitle("Alert!")
                .setMessage(message)
                .setPositiveButton("Ok", (dialog,id) -> {
                    dialog.cancel();
                })
                .setCancelable(true);

        AlertDialog alertDialog = alerBuilder.create();
        alertDialog.show();
    }
    public static Optional<Integer> parseCount(String str){
        try{
            int maxCount = Integer.parseInt(str);
            return Optional.of(maxCount);
        } catch (NumberFormatException e){
            return Optional.empty();
        }
    }

}
