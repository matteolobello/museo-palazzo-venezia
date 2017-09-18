package com.matteolobello.palazzovenezia.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.matteolobello.palazzovenezia.R;
import com.matteolobello.palazzovenezia.data.preference.PreferenceHandler;
import com.matteolobello.palazzovenezia.util.LanguageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity {

    private static final String MATTEO_LINK     = "https://www.matteolobello.com";
    private static final String ANDREA_LINK     = "https://www.instagram.com/alpaca_furioso";
    private static final String ALESSANDRO_LINK = "https://www.facebook.com/alessandro.currenti.1";
    private static final String FEDERICO_LINK   = "https://www.facebook.com/federico.fiorio.9";
    private static final String ROLANDO_LINK    = "https://www.facebook.com/RoyanOfficial";
    private static final String LEONARDO_LINK   = "https://www.instagram.com/leolance";

    @BindView(R.id.about_text_view) protected TextView mAboutTextView;

    private PreferenceHandler mPreferenceHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ButterKnife.bind(this);

        mPreferenceHandler = PreferenceHandler.get();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(LanguageUtil.getStringByLocale(this, R.string.about,
                    mPreferenceHandler.getValue(this, PreferenceHandler.LANGUAGE_KEY)));
        }

        mAboutTextView.setText(LanguageUtil.getStringByLocale(this, R.string.about_text,
                mPreferenceHandler.getValue(this, PreferenceHandler.LANGUAGE_KEY)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.matteo_wrapper)
    protected void onMatteoClick() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MATTEO_LINK)));
    }

    @OnClick(R.id.andrea_wrapper)
    protected void onAndreaClick() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ANDREA_LINK)));
    }

    @OnClick(R.id.alessandro_wrapper)
    protected void onAlessandroClick() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ALESSANDRO_LINK)));
    }

    @OnClick(R.id.federico_wrapper)
    protected void onFedericoClick() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(FEDERICO_LINK)));
    }

    @OnClick(R.id.rolando_wrapper)
    protected void onRolandoClick() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ROLANDO_LINK)));
    }

    @OnClick(R.id.leonardo_wrapper)
    protected void onLeonardoClick() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(LEONARDO_LINK)));
    }
}
