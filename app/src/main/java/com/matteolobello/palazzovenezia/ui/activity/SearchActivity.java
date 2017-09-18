package com.matteolobello.palazzovenezia.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.matteolobello.palazzovenezia.R;
import com.matteolobello.palazzovenezia.data.asset.AssetPaintingsGenerator;
import com.matteolobello.palazzovenezia.data.preference.PreferenceHandler;
import com.matteolobello.palazzovenezia.ui.adapter.SearchRecyclerViewAdapter;
import com.matteolobello.palazzovenezia.util.LanguageUtil;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.search_container) protected View         mSearchContainer;
    @BindView(R.id.search_view)      protected EditText     mSearchEditText;
    @BindView(R.id.search_clear)     protected ImageView    mSearchClearImageView;
    @BindView(R.id.search_rv)        protected RecyclerView mSearchRecyclerView;

    private SearchRecyclerViewAdapter mSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        ButterKnife.bind(this);

        setupSearchView();
        setupRecyclerView();
    }

    @OnClick(R.id.toolbar_back)
    protected void onBackArrowClick() {
        finish();
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

        mSearchEditText.setHint(LanguageUtil.getStringByLocale(this, R.string.search,
                PreferenceHandler.get().getValue(this, PreferenceHandler.LANGUAGE_KEY)));

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
