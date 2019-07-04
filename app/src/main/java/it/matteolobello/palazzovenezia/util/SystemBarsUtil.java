package it.matteolobello.palazzovenezia.util;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;
import androidx.core.os.BuildCompat;

import it.matteolobello.palazzovenezia.R;

public class SystemBarsUtil {

    public static void setFullyTransparentStatusBar(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(activity.getWindow().getDecorView().getSystemUiVisibility()
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    public static void setFullyTransparentNavigationBar(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(activity.getWindow().getDecorView().getSystemUiVisibility()
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
    }

    public static void setStatusBarColor(Activity activity, int color) {
        if (color == Color.WHITE) {
            if (!BuildCompat.isAtLeastN()) {
                color = ContextCompat.getColor(activity, R.color.systemBarsWhitePreM);
            }
        }
        activity.getWindow().setStatusBarColor(color);
    }

    public static void setStatusBarCompletelyTransparent(Activity activity, boolean on) {
        if (on) {
            activity.getWindow().getDecorView().setSystemUiVisibility(activity.getWindow().getDecorView().getSystemUiVisibility()
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            int flags = activity.getWindow().getDecorView().getSystemUiVisibility();
            flags &= ~View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            flags &= ~View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(Color.BLACK);
        }
    }

    public static void setDarkStatusBarIcons(Activity activity, boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (on) {
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        activity.getWindow().getDecorView().getSystemUiVisibility()
                                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                int flags = activity.getWindow().getDecorView().getSystemUiVisibility();
                flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                activity.getWindow().getDecorView().setSystemUiVisibility(flags);
            }
        }
    }

    public static void setDarkNavigationBarIcons(Activity activity, boolean on) {
        if (BuildCompat.isAtLeastO()) {
            if (on) {
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        activity.getWindow().getDecorView().getSystemUiVisibility()
                                | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                activity.getWindow().setNavigationBarColor(Color.WHITE);
            } else {
                int flags = activity.getWindow().getDecorView().getSystemUiVisibility();
                flags &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                activity.getWindow().getDecorView().setSystemUiVisibility(flags);
                activity.getWindow().setNavigationBarColor(ContextCompat.getColor(activity, android.R.color.black));
            }
        }
    }

    public static boolean hasNavigationBar(Activity activity) {
        int id = activity.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && activity.getResources().getBoolean(id);
    }

    public static void setNavigationBarColor(final Activity activity, int color, boolean animated) {
        if (color == Color.WHITE) {
            if (!BuildCompat.isAtLeastO()) {
                color = ContextCompat.getColor(activity, R.color.systemBarsWhitePreM);
            }
        }

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), activity.getWindow().getNavigationBarColor(), color);
        colorAnimation.setDuration(animated ? 200 : 0);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                activity.getWindow().setNavigationBarColor((int) animator.getAnimatedValue());
            }
        });
        colorAnimation.start();
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static void setNoLimitsSystemBars(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public static void goImmersive(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
