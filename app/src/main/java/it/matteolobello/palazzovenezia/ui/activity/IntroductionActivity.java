package it.matteolobello.palazzovenezia.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.rd.PageIndicatorView;

import it.matteolobello.palazzovenezia.R;
import it.matteolobello.palazzovenezia.data.asset.AssetPaintingsGenerator;
import it.matteolobello.palazzovenezia.data.bundle.BundleKeys;
import it.matteolobello.palazzovenezia.ui.adapter.viewpager.IntroductionViewPagerAdapter;
import it.matteolobello.palazzovenezia.ui.fragment.introduction.QRCodeIntroductionFragment;
import it.matteolobello.palazzovenezia.util.PermissionUtil;
import it.matteolobello.palazzovenezia.util.SystemBarsUtil;

public class IntroductionActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private PageIndicatorView mPageIndicatorView;

    private IntroductionViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        SystemBarsUtil.setFullyTransparentStatusBar(this);
        SystemBarsUtil.setFullyTransparentNavigationBar(this);

        mViewPager = findViewById(R.id.introduction_view_pager);
        mPageIndicatorView = findViewById(R.id.viewpager_indicator);

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mPageIndicatorView.getLayoutParams();
        layoutParams.setMargins(0, 0, 0,
                layoutParams.bottomMargin + SystemBarsUtil.getNavigationBarHeight(this));
        mPageIndicatorView.setLayoutParams(layoutParams);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mViewPagerAdapter = new IntroductionViewPagerAdapter(getSupportFragmentManager());

        mViewPager.setPageTransformer(true, new ParallaxPageTransformer());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(mViewPagerAdapter.getCount());
    }

    @Override
    protected void onResume() {
        super.onResume();

        QRCodeIntroductionFragment qrCodeIntroductionFragment =
                (QRCodeIntroductionFragment) mViewPagerAdapter.getFragmentByClass(QRCodeIntroductionFragment.class);
        if (!PermissionUtil.hasPermissions(this)) {
            qrCodeIntroductionFragment.setGrantPermissionButton();
        } else {
            qrCodeIntroductionFragment.setNextButton();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionUtil.CAMERA_PERMISSIONS_REQUEST_CODE) {
            boolean permissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    permissionsGranted = false;
                    break;
                }
            }

            QRCodeIntroductionFragment qrCodeIntroductionFragment =
                    (QRCodeIntroductionFragment) mViewPagerAdapter.getFragmentByClass(QRCodeIntroductionFragment.class);
            if (!permissionsGranted) {
                qrCodeIntroductionFragment.setGrantPermissionButton();
            } else {
                qrCodeIntroductionFragment.setNextButton();
            }
        }
    }

    public void onIntroButtonClick() {
        int currentItem = mViewPager.getCurrentItem();
        switch (currentItem) {
            case 0:
                mViewPager.setCurrentItem(currentItem + 1);
                break;
            case 1:
                if (PermissionUtil.hasPermissions(this)) {
                    mViewPager.setCurrentItem(currentItem + 1);
                    break;
                }

                ActivityCompat.requestPermissions(this, PermissionUtil.PERMISSIONS, PermissionUtil.CAMERA_PERMISSIONS_REQUEST_CODE);
                break;
            case 2:
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putParcelableArrayListExtra(BundleKeys.EXTRA_ALL_PAINTINGS,
                        AssetPaintingsGenerator.generatePaintings(getApplicationContext()));
                startActivity(intent);
                finish();
                break;
        }
    }

    private class ParallaxPageTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(@NonNull View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) {
                view.setAlpha(1);
            } else if (position <= 1) {
                ((ViewGroup) view).getChildAt(0).setTranslationX(-position * (pageWidth / 2));
            } else {
                view.setAlpha(1);
            }
        }
    }
}
