package com.matteolobello.palazzovenezia.util;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.matteolobello.palazzovenezia.R;

import androidx.core.content.ContextCompat;

public class SystemBarsUtil {

    public static void setFullyTransparentStatusBar(Activity activity) {
        setFullyTransparentSystemBars(activity, false);
    }

    public static void setFullyTransparentSystemBars(Activity activity, boolean navBarToo) {
        setStatusBarColor(activity, Color.TRANSPARENT, false);
        setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        if (navBarToo) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public static void setNavigationBarColor(final Activity activity, int color, boolean withFade) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), activity.getWindow().getNavigationBarColor(), color);
        colorAnimation.setDuration(withFade ? 250 : 0);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int color = (int) animator.getAnimatedValue();
                if (ColorUtil.isAlmostWhite(color)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        setLightNavigationBar(activity);
                    } else {
                        color = ContextCompat.getColor(activity, R.color.systemBarsWhitePreM);
                    }
                } else {
                    clearLightNavigationBar(activity);
                }

                activity.getWindow().setNavigationBarColor(color);
            }
        });
        colorAnimation.start();
    }

    public static void setStatusBarColor(final Activity activity, int color, boolean withFade) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), activity.getWindow().getStatusBarColor(), color);
        colorAnimation.setDuration(withFade ? 250 : 0);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int color = (int) animator.getAnimatedValue();
                if (ColorUtil.isAlmostWhite(color)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setLightStatusBar(activity);
                    } else {
                        color = ContextCompat.getColor(activity, R.color.systemBarsWhitePreM);
                    }
                } else {
                    clearLightStatusBar(activity);
                }

                activity.getWindow().setStatusBarColor(color);
            }
        });
        colorAnimation.start();
    }

    public static boolean hasNavigationBar(Activity activity) {
        int id = activity.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && activity.getResources().getBoolean(id);
    }

    public static int getNavigationBarHeight(Activity activity) {
        int resourceId = activity.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return activity.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private static void setLightNavigationBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int flags = activity.getWindow().getDecorView().getSystemUiVisibility();
            flags = flags | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    }

    private static void setLightStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = activity.getWindow().getDecorView().getSystemUiVisibility();
            flags = flags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    }

    private static void clearLightNavigationBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int flags = activity.getWindow().getDecorView().getSystemUiVisibility();
            flags &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    }

    private static void clearLightStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = activity.getWindow().getDecorView().getSystemUiVisibility();
            flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    }

    private static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
