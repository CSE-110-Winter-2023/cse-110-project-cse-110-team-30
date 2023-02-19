package com.example.team30;

import java.util.Locale;

public class Utilities {
    static String formatOrientation(float azimuth) {
        float degrees = (float) Math.toDegrees(azimuth);
        return String.format(Locale.US, "%.0f degrees from North", degrees);
    }

    static String formatLocation(double latitude, double longitude) {
        return String.format(Locale.US, "%.0f° %.0f' %.0f\" N, %.0f° %.0f' %.0f\" W",
                Math.abs(latitude), Math.abs(latitude % 1) * 60, Math.abs(latitude % 1 % 1) * 60,
                Math.abs(longitude), Math.abs(longitude % 1) * 60, Math.abs(longitude % 1 % 1) * 60);
    }

    static String formatTime(long time) {
        return String.format(Locale.US, "%tT %tZ", time, time);
    }
}
