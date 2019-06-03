package it.matteolobello.palazzovenezia.util;

import android.graphics.Color;

public class ColorUtil {

    public static double getBrightness(int color) {
        return Math.sqrt(
                Color.red(color) * Color.red(color) * .241 +
                        Color.green(color) * Color.green(color) * .691 +
                        Color.blue(color) * Color.blue(color) * .068);
    }

    public static boolean isAlmostWhite(int color) {
        return getBrightness(color) > 250;
    }
}
