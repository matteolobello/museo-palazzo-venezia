package com.matteolobello.palazzovenezia.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.matteolobello.palazzovenezia.data.asset.AssetPaintingsGenerator;
import com.matteolobello.palazzovenezia.data.bundle.BundleKeys;
import com.matteolobello.palazzovenezia.data.preference.PreferenceHandler;

import androidx.appcompat.app.AppCompatActivity;

public class LaunchScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Class classToLaunch = HomeActivity.class;

        PreferenceHandler preferenceHandler = PreferenceHandler.get();

        String booleanStringValue = preferenceHandler.getValue(this, PreferenceHandler.LAUNCH_INTRODUCTION_KEY);
        if (booleanStringValue == null) {
            preferenceHandler.setValue(this, PreferenceHandler.LAUNCH_INTRODUCTION_KEY, "false");

            classToLaunch = IntroductionActivity.class;
        }

        Intent intent = new Intent(this, classToLaunch);
        if (classToLaunch.getName().equals(HomeActivity.class.getName())) {
            String action = getIntent().getStringExtra(BundleKeys.EXTRA_SHORTCUT_ACTION);
            if (action != null) {
                intent.putExtra(BundleKeys.EXTRA_SHORTCUT_ACTION, action);
            }
            intent.putParcelableArrayListExtra(BundleKeys.EXTRA_ALL_PAINTINGS,
                    AssetPaintingsGenerator.generatePaintings(getApplicationContext()));
        }

        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }
}
