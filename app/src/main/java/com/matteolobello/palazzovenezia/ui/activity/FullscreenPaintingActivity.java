package com.matteolobello.palazzovenezia.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;
import com.matteolobello.palazzovenezia.R;
import com.matteolobello.palazzovenezia.data.asset.AssetImageSetter;
import com.matteolobello.palazzovenezia.data.bundle.BundleKeys;
import com.matteolobello.palazzovenezia.util.SystemBarsUtil;

public class FullscreenPaintingActivity extends AppCompatActivity {

    private PhotoView mPaintingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_painting);

        SystemBarsUtil.goImmersive(this);

        mPaintingView = findViewById(R.id.painting_image_view);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        String paintingPath = extras.getString(BundleKeys.EXTRA_PAINTING_PATH);
        if (paintingPath != null) {
            mPaintingView.setImageResource(AssetImageSetter.getImageResByName(this, paintingPath));
        }
    }
}
