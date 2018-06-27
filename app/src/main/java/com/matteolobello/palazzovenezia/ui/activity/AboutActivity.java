package com.matteolobello.palazzovenezia.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.matteolobello.palazzovenezia.R;
import com.matteolobello.palazzovenezia.data.preference.PreferenceHandler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String MATTEO_LINK = "https://www.matteolobello.com";
    private static final String ANDREA_LINK = "https://www.instagram.com/alpaca_furioso";
    private static final String ALESSANDRO_LINK = "https://www.facebook.com/alessandro.currenti.1";
    private static final String FEDERICO_LINK = "https://www.facebook.com/federico.fiorio.9";
    private static final String ROLANDO_LINK = "https://www.facebook.com/RoyanOfficial";
    private static final String LEONARDO_LINK = "https://www.instagram.com/leolance";

    private TextView mAboutTextView;

    private PreferenceHandler mPreferenceHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mAboutTextView = findViewById(R.id.about_text_view);

        mPreferenceHandler = PreferenceHandler.get();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        findViewById(R.id.matteo_wrapper).setOnClickListener(this);
        findViewById(R.id.andrea_wrapper).setOnClickListener(this);
        findViewById(R.id.alessandro_wrapper).setOnClickListener(this);
        findViewById(R.id.federico_wrapper).setOnClickListener(this);
        findViewById(R.id.rolando_wrapper).setOnClickListener(this);
        findViewById(R.id.leonardo_wrapper).setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        String url = null;

        switch (view.getId()) {
            case R.id.matteo_wrapper:
                url = MATTEO_LINK;
                break;
            case R.id.andrea_wrapper:
                url = ANDREA_LINK;
                break;
            case R.id.alessandro_wrapper:
                url = ALESSANDRO_LINK;
                break;
            case R.id.federico_wrapper:
                url = FEDERICO_LINK;
                break;
            case R.id.rolando_wrapper:
                url = ROLANDO_LINK;
                break;
            case R.id.leonardo_wrapper:
                url = LEONARDO_LINK;
                break;
        }

        if (url != null) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }
}
