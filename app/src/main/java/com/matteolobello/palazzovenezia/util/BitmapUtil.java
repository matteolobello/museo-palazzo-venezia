package com.matteolobello.palazzovenezia.util;

import android.graphics.Bitmap;
import android.graphics.Color;

public class BitmapUtil {

    public static int calculateBrightnessEstimate(Bitmap bitmap) {
        int R = 0;
        int G = 0;
        int B = 0;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int n = 0;
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int color : pixels) {
            R += Color.red(color);
            G += Color.green(color);
            B += Color.blue(color);
            n++;
        }
        return (R + B + G) / (n * 3);
    }
}
