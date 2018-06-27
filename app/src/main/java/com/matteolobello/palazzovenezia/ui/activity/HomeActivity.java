package com.matteolobello.palazzovenezia.ui.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.matteolobello.palazzovenezia.R;
import com.matteolobello.palazzovenezia.ui.adapter.viewpager.HomeViewPagerAdapter;
import com.matteolobello.palazzovenezia.ui.fragment.home.QRCodeFragment;
import com.matteolobello.palazzovenezia.util.PermissionUtil;
import com.matteolobello.palazzovenezia.util.SystemBarsUtil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

public class HomeActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;

    private HomeViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SystemBarsUtil.setNavigationBarColor(this, ContextCompat.getColor(this, android.R.color.white), false);
        SystemBarsUtil.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.white), false);

        if (!SystemBarsUtil.hasNavigationBar(this)) {
            findViewById(R.id.navigation_bar_divider).setVisibility(View.GONE);
        }

        mViewPager = findViewById(R.id.home_view_pager);
        mBottomNavigationView = findViewById(R.id.bottom_navigation_view);

        mViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(mViewPagerAdapter.getCount());

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int index = 0;
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        index = 0;
                        break;
                    case R.id.action_search:
                        index = 1;
                        break;
                    case R.id.action_scan:
                        index = 2;
                        break;
                    case R.id.action_map:
                        index = 3;
                        break;
                    case R.id.action_about:
                        index = 4;
                        break;
                }

                mViewPager.setCurrentItem(index, false);

                if (index == 2) {
                    if (!PermissionUtil.hasPermissions(getApplicationContext())) {
                        ActivityCompat.requestPermissions(HomeActivity.this, PermissionUtil.PERMISSIONS, PermissionUtil.CAMERA_PERMISSIONS_REQUEST_CODE);
                        return false;
                    }

                    ((QRCodeFragment) mViewPagerAdapter.getFragmentByClass(QRCodeFragment.class)).startCamera();
                } else {
                    ((QRCodeFragment) mViewPagerAdapter.getFragmentByClass(QRCodeFragment.class)).stopCamera();
                }

                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        ((QRCodeFragment) mViewPagerAdapter.getFragmentByClass(QRCodeFragment.class)).stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mViewPager.getCurrentItem() == 2) {
            ((QRCodeFragment) mViewPagerAdapter.getFragmentByClass(QRCodeFragment.class)).startCamera();
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

            if (permissionsGranted) {
                changeTabSelection(2);
            } else {
                changeTabSelection(0);
                Toast.makeText(this, R.string.permission_not_granted, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void changeTabSelection(int index) {
        mViewPager.setCurrentItem(index, false);

        int tabItemId = R.id.action_home;
        switch (index) {
            case 0:
                tabItemId = R.id.action_home;
                break;
            case 1:
                tabItemId = R.id.action_search;
                break;
            case 2:
                tabItemId = R.id.action_scan;
                break;
            case 3:
                tabItemId = R.id.action_map;
                break;
            case 4:
                tabItemId = R.id.action_about;
                break;
        }

        mBottomNavigationView.setSelectedItemId(tabItemId);
    }
}
