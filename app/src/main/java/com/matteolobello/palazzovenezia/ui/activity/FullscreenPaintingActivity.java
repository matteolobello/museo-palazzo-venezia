package com.matteolobello.palazzovenezia.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.chrisbanes.photoview.PhotoView;
import com.matteolobello.palazzovenezia.R;
import com.matteolobello.palazzovenezia.data.asset.AssetImageSetter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullscreenPaintingActivity extends AppCompatActivity {

    @BindView(R.id.painting_image_view) protected PhotoView mPaintingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_painting);

        ButterKnife.bind(this);

        if (!getIntent().getBooleanExtra("map", false)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mPaintingView.setTransitionName("painting");
            }

            String paintingPath = getIntent().getExtras().getString("painting_path");
            if (paintingPath != null) {
                mPaintingView.setImageResource(AssetImageSetter.getImageResByName(this, paintingPath));
            }
        } else {
            mPaintingView.setImageResource(R.drawable.map);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }
}
