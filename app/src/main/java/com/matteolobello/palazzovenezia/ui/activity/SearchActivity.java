package com.matteolobello.palazzovenezia.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.matteolobello.palazzovenezia.R;
import com.matteolobello.palazzovenezia.data.asset.AssetPaintingsGenerator;
import com.matteolobello.palazzovenezia.ui.adapter.recyclerview.SearchRecyclerViewAdapter;

import java.lang.reflect.Field;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchActivity extends AppCompatActivity {

    private View mToolbarBackImageView;
    private View mSearchContainer;
    private EditText mSearchEditText;
    private ImageView mSearchClearImageView;
    private RecyclerView mSearchRecyclerView;

    private SearchRecyclerViewAdapter mSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mToolbarBackImageView = findViewById(R.id.toolbar_back);
        mSearchContainer = findViewById(R.id.search_container);
        mSearchEditText = findViewById(R.id.search_view);
        mSearchClearImageView = findViewById(R.id.search_clear);
        mSearchRecyclerView = findViewById(R.id.search_rv);

        mToolbarBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        setupSearchView();
        setupRecyclerView();
    }

    private void setupSearchView() {
        try {
            // Set cursor colour to white
            // http://stackoverflow.com/a/26544231/1692770
            // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(mSearchEditText, R.drawable.white_cursor);
        } catch (Exception ignored) {
        }

        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSearchAdapter.searchPainting(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mSearchClearImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchEditText.setText("");
            }
        });
    }

    private void setupRecyclerView() {
        mSearchRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mSearchAdapter = new SearchRecyclerViewAdapter(AssetPaintingsGenerator.generatePaintings(this));
        mSearchRecyclerView.setAdapter(mSearchAdapter);
    }
}
