package com.matteolobello.palazzovenezia.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.github.chrisbanes.photoview.PhotoView;
import com.matteolobello.palazzovenezia.R;
import com.matteolobello.palazzovenezia.data.asset.AssetImageSetter;
import com.matteolobello.palazzovenezia.data.bundle.BundleKeys;
import com.matteolobello.palazzovenezia.data.transition.TransitionNames;

import androidx.appcompat.app.AppCompatActivity;

public class FullscreenPaintingActivity extends AppCompatActivity {

    private PhotoView mPaintingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_painting);

        mPaintingView = findViewById(R.id.painting_image_view);

        if (!getIntent().getBooleanExtra(BundleKeys.EXTRA_MAP, false)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mPaintingView.setTransitionName(TransitionNames.PAINTING);
            }

            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                return;
            }

            String paintingPath = extras.getString(BundleKeys.EXTRA_PAINTING_PATH);
            if (paintingPath != null) {
                mPaintingView.setImageResource(AssetImageSetter.getImageResByName(this, paintingPath));
            }
        } else {
            mPaintingView.setImageResource(R.drawable.map);
        }

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}
