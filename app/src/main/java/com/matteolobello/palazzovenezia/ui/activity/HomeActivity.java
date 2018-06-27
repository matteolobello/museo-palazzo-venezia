package com.matteolobello.palazzovenezia.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.matteolobello.palazzovenezia.R;
import com.matteolobello.palazzovenezia.data.bundle.BundleKeys;
import com.matteolobello.palazzovenezia.data.model.Painting;
import com.matteolobello.palazzovenezia.data.preference.PreferenceHandler;
import com.matteolobello.palazzovenezia.data.transition.TransitionNames;
import com.matteolobello.palazzovenezia.ui.adapter.recyclerview.PaintingsRecyclerViewAdapter;
import com.matteolobello.palazzovenezia.util.ScrollUtil;
import com.matteolobello.palazzovenezia.util.PermissionUtil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mQRCodeScanFab;

    private PreferenceHandler mPreferenceHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mToolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.paintings_recycler_view);
        mQRCodeScanFab = findViewById(R.id.qr_code_scan_fab);

        mPreferenceHandler = PreferenceHandler.get();

        setSupportActionBar(mToolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mQRCodeScanFab.setTransitionName(TransitionNames.FAB);
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
                        .makeSceneTransitionAnimation(HomeActivity.this, mQRCodeScanFab, TransitionNames.FAB);
                startActivity(intent, options.toBundle());
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        mRecyclerView.setAdapter(new PaintingsRecyclerViewAdapter(
                getIntent().<Painting>getParcelableArrayListExtra(BundleKeys.EXTRA_ALL_PAINTINGS)));

        ScrollUtil.setOnScrollListener(mRecyclerView, new ScrollUtil.OnScrollListener() {
            @Override
            public void onScroll(@ScrollUtil.ScrollDirection int scrollDirection) {
                if (scrollDirection == ScrollUtil.UP) {
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
                        .makeSceneTransitionAnimation(HomeActivity.this, mQRCodeScanFab, TransitionNames.FAB);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_map:
                startActivity(new Intent(getApplicationContext(), FullscreenPaintingActivity.class).putExtra(BundleKeys.EXTRA_MAP, true));
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
