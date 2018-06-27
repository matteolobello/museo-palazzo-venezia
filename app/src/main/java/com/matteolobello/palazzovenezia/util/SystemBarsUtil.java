package com.matteolobello.palazzovenezia.util;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SystemBarsUtil {

    public static void setFullyTransparentStatusBar(Activity activity) {
        setFullyTransparentSystemBars(activity, false);
    }

    public static void setFullyTransparentSystemBars(Activity activity, boolean navBarToo) {
        setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        if (navBarToo) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public static void setNavigationBarColor(final Activity activity, int color, boolean withFade) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!withFade) {
                activity.getWindow().setNavigationBarColor(color);

                return;
            }

            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), activity.getWindow().getNavigationBarColor(), color);
            colorAnimation.setDuration(250);
            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @SuppressLint("NewApi")
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    activity.getWindow().setNavigationBarColor(((int) animator.getAnimatedValue()));
                }

            });
            colorAnimation.start();
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

    public static void setStatusBarColor(final Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), activity.getWindow().getStatusBarColor(), color);
            colorAnimation.setDuration(250);
            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @SuppressLint("NewApi")
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    activity.getWindow().setStatusBarColor(((int) animator.getAnimatedValue()));
                }

            });
            colorAnimation.start();
        }
    }

    public static int getStatusBarHeight(Activity activity) {
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return activity.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getNavigationBarHeight(Activity activity) {
        int resourceId = activity.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return activity.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
