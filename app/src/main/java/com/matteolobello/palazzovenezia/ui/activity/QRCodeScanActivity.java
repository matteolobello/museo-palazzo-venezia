package com.matteolobello.palazzovenezia.ui.activity;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.matteolobello.palazzovenezia.R;
import com.matteolobello.palazzovenezia.data.asset.AssetFileReader;
import com.matteolobello.palazzovenezia.data.model.Painting;
import com.matteolobello.palazzovenezia.data.preference.PreferenceHandler;
import com.matteolobello.palazzovenezia.data.qrcode.QRCodeScanning;
import com.matteolobello.palazzovenezia.util.LanguageUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QRCodeScanActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private static final long AUTO_FOCUS_INTERVAL = 2000;

    @BindView(R.id.qr_decoder_view) protected QRCodeReaderView     mQRCodeReaderView;
    @BindView(R.id.flash_fab)       protected FloatingActionButton mFlashFab;

    private PreferenceHandler mPreferenceHandler;

    private boolean mTorchEnabled = false;

    private int mStartingDescriptionActivityCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scan);

        ButterKnife.bind(this);

        mPreferenceHandler = PreferenceHandler.get();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mFlashFab.setTransitionName("fab");
        }

        mQRCodeReaderView.setAutofocusInterval(AUTO_FOCUS_INTERVAL);
        mQRCodeReaderView.setOnQRCodeReadListener(this);
        mQRCodeReaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQRCodeReaderView.forceAutoFocus();
            }
        });

        mFlashFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTorchEnabled = !mTorchEnabled;

                mQRCodeReaderView.setTorchEnabled(mTorchEnabled);

                mFlashFab.setImageResource(mTorchEnabled
                        ? R.drawable.ic_flash_off
                        : R.drawable.ic_flash_on);
            }
        });
    }

    @Override
    public void onQRCodeRead(String id, PointF[] points) {
        if (!QRCodeScanning.isQRCodeValid(getApplicationContext(), id)) {
            Toast.makeText(this, LanguageUtil.getStringByLocale(this, R.string.qr_code_not_valid,
                    mPreferenceHandler.getValue(this, PreferenceHandler.LANGUAGE_KEY)), Toast.LENGTH_SHORT).show();

            return;
        }

        // As DescriptionActivity is launched twice,
        // force starting it only once
        mStartingDescriptionActivityCounter++;
        if (mStartingDescriptionActivityCounter > 1) {
            mStartingDescriptionActivityCounter = 0;

            return;
        }

        mQRCodeReaderView.stopCamera();

        Painting painting = new Painting();

        try {
            String language = mPreferenceHandler.getValue(getApplicationContext(), PreferenceHandler.LANGUAGE_KEY);
            if (language == null) {
                language = "en";
                mPreferenceHandler.setValue(getApplicationContext(), PreferenceHandler.LANGUAGE_KEY, "en");
            }

            painting.setId(id);

            String descriptionFilePath = getAssets().list(id + "/" + "txt" + "/" + language)[0];
            File descriptionFile = new File(descriptionFilePath);
            String description = AssetFileReader.readAssetsFile(getApplicationContext(),
                    id + "/" + "txt" + "/" + language + "/" + descriptionFilePath);

            painting.setName(descriptionFile.getName());
            painting.setDescription(description);

            String audioFileName = getAssets().list(id + "/" + "mp3" + "/" + language)[0];
            painting.setAudioPath(id + "/" + "mp3" + "/" + language + "/" + audioFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Intent intent = new Intent(getApplicationContext(), PaintingActivity.class);
        intent.putExtra("painting", painting);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(QRCodeScanActivity.this, mFlashFab, "fab");
        startActivity(intent, options.toBundle());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mQRCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mQRCodeReaderView.stopCamera();
    }

    @Override
    public void onBackPressed() {
        mQRCodeReaderView.setVisibility(View.GONE);

        super.onBackPressed();
    }
}
