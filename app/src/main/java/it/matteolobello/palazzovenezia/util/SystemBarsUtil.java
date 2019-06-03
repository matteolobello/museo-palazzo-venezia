package it.matteolobello.palazzovenezia.util;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import it.matteolobello.palazzovenezia.R;

public class SystemBarsUtil {

    public static void setFullyTransparentStatusBar(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(activity.getWindow().getDecorView().getSystemUiVisibility()
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    public static void setFullyTransparentNavigationBar(Activity activity) {
        setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setNavigationBarColor(activity, Color.TRANSPARENT, false);
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

    public static void goImmersive(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    public static boolean hasNavigationBar(Activity activity) {
        int id = activity.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && activity.getResources().getBoolean(id);
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
