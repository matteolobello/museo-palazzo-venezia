package com.matteolobello.palazzovenezia.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.matteolobello.palazzovenezia.R;
import com.matteolobello.palazzovenezia.data.model.Painting;
import com.matteolobello.palazzovenezia.data.preference.PreferenceHandler;
import com.matteolobello.palazzovenezia.ui.adapter.PaintingsRecyclerViewAdapter;
import com.matteolobello.palazzovenezia.ui.scroll.ScrollHandler;
import com.matteolobello.palazzovenezia.util.LanguageUtil;
import com.matteolobello.palazzovenezia.util.PermissionUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)                 protected Toolbar              mToolbar;
    @BindView(R.id.paintings_recycler_view) protected RecyclerView         mRecyclerView;
    @BindView(R.id.qr_code_scan_fab)        protected FloatingActionButton mQRCodeScanFab;

    private PreferenceHandler mPreferenceHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        mPreferenceHandler = PreferenceHandler.get();

        mToolbar.setTitle(LanguageUtil.getStringByLocale(this, R.string.app_name,
                mPreferenceHandler.getValue(this, PreferenceHandler.LANGUAGE_KEY)));
        setSupportActionBar(mToolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mQRCodeScanFab.setTransitionName("fab");
        }

        mQRCodeScanFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PermissionUtil.hasPermissions(getApplicationContext())) {
                    ActivityCompat.requestPermissions(HomeActivity.this, PermissionUtil.PERMISSIONS, PermissionUtil.CAMERA_PERMISSIONS_REQUEST_CODE);

                    return;
                }

                Intent intent = new Intent(getApplicationContext(), QRCodeScanActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(HomeActivity.this, mQRCodeScanFab, "fab");
                startActivity(intent, options.toBundle());
            }
        });

        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        mRecyclerView.setAdapter(new PaintingsRecyclerViewAdapter(
                getIntent().<Painting>getParcelableArrayListExtra("all_paintings")));

        ScrollHandler.setOnScrollListener(mRecyclerView, new ScrollHandler.OnScrollListener() {
            @Override
            public void onScroll(@ScrollHandler.ScrollDirection int scrollDirection) {
                if (scrollDirection == ScrollHandler.UP) {
                    mQRCodeScanFab.show();
                } else {
                    mQRCodeScanFab.hide();
                }
            }
        });

        invalidateOptionsMenu();
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
                Intent intent = new Intent(getApplicationContext(), QRCodeScanActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(HomeActivity.this, mQRCodeScanFab, "fab");
                startActivity(intent, options.toBundle());
            } else {
                Toast.makeText(this, R.string.permission_not_granted, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        menu.findItem(R.id.action_map).setTitle(
                LanguageUtil.getStringByLocale(this, R.string.map, mPreferenceHandler.getValue(this, PreferenceHandler.LANGUAGE_KEY)));
        menu.findItem(R.id.action_about).setTitle(
                LanguageUtil.getStringByLocale(this, R.string.about, mPreferenceHandler.getValue(this, PreferenceHandler.LANGUAGE_KEY)));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_map:
                startActivity(new Intent(getApplicationContext(), FullscreenPaintingActivity.class).putExtra("map", true));
                break;
            case R.id.action_about:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                break;
            case R.id.action_search:
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
