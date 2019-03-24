package com.matteolobello.palazzovenezia.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.matteolobello.palazzovenezia.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class MaterialDesignViewPager extends ViewPager {

    private static final boolean SWIPE_ENABLE = false;

    private Animation mCurrentAnimation;

    private boolean mIsSwitchingFragment;

    public MaterialDesignViewPager(@NonNull Context context) {
        super(context);
    }

    public MaterialDesignViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return SWIPE_ENABLE;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return SWIPE_ENABLE;
    }

    @Override
    public void setCurrentItem(int item) {
        setCurrentItem(item, true);
    }

    @Override
    public void setCurrentItem(final int item, final boolean withAnimation) {
        setCurrentItem(item, withAnimation, null);
    }

    public void setCurrentItem(final int item, final boolean withAnimation, final OnFragmentSetListener onFragmentSetListener) {
        if (getCurrentItem() == item) {
            return;
        }

        if (mIsSwitchingFragment) {
            return;
        }

        if (!withAnimation) {
            mIsSwitchingFragment = true;

            super.setCurrentItem(item, false);

            mIsSwitchingFragment = false;

            return;
        }

        mIsSwitchingFragment = true;

        if (mCurrentAnimation != null) {
            mCurrentAnimation.cancel();
        }

        Animation fadeOutAnimation = mCurrentAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                MaterialDesignViewPager.super.setCurrentItem(item, false);

                Animation slideInUpAnimation = mCurrentAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_up_fade_in);
                slideInUpAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mIsSwitchingFragment = false;

                        if (onFragmentSetListener != null) {
                            onFragmentSetListener.onFragmentSet();
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                startAnimation(slideInUpAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        startAnimation(fadeOutAnimation);
    }

    public boolean isSwitchingFragment() {
        return mIsSwitchingFragment;
    }

    public interface OnFragmentSetListener {
        void onFragmentSet();
    }
}