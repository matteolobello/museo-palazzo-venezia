package com.matteolobello.palazzovenezia.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.matteolobello.palazzovenezia.R;
import com.matteolobello.palazzovenezia.data.asset.AssetPaintingsGenerator;
import com.matteolobello.palazzovenezia.data.preference.PreferenceHandler;
import com.matteolobello.palazzovenezia.ui.systembar.SystemBars;
import com.matteolobello.palazzovenezia.util.LanguageUtil;
import com.matteolobello.palazzovenezia.util.PermissionUtil;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntroductionActivity extends AppCompatActivity {

    private final WelcomeIntroductionFragment mWelcomeFragment = new WelcomeIntroductionFragment();
    private final QRCodeIntroductionFragment mQRCodeFragment = new QRCodeIntroductionFragment();
    private final EnjoyIntroductionFragment mEnjoyFragment = new EnjoyIntroductionFragment();

    private final Fragment[] FRAGMENTS = {
            mWelcomeFragment,
            mQRCodeFragment,
            mEnjoyFragment
    };

    @BindView(R.id.introduction_view_pager) protected ViewPager        mViewPager;
    @BindView(R.id.viewpager_indicator)     protected InkPageIndicator mPageIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mViewPager.setPageTransformer(true, new ParallaxPageTransformer());
        mViewPager.setAdapter(new IntroductionViewPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(FRAGMENTS.length);

        mPageIndicator.setViewPager(mViewPager);

        SystemBars.setFullyTransparentStatusBar(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!PermissionUtil.hasPermissions(this)) {
            mQRCodeFragment.setGrantPermissionButton();
        } else {
            mQRCodeFragment.setNextButton();
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

            if (!permissionsGranted) {
                mQRCodeFragment.setGrantPermissionButton();
            } else {
                mQRCodeFragment.setNextButton();
            }
        }
    }

    void overrideFonts(View view) {
        try {
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    View child = viewGroup.getChildAt(i);
                    overrideFonts(child);
                }
            } else if (view instanceof TextView && !(view instanceof Button)) {
                ((TextView) view).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void onIntroButtonClick() {
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
                intent.putParcelableArrayListExtra("all_paintings",
                        AssetPaintingsGenerator.generatePaintings(getApplicationContext()));
                startActivity(intent);
                finish();
                break;
        }
    }

    private class IntroductionViewPagerAdapter extends FragmentPagerAdapter {

        IntroductionViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return FRAGMENTS[position];
        }

        @Override
        public int getCount() {
            return FRAGMENTS.length;
        }
    }

    public static class WelcomeIntroductionFragment extends Fragment {

        @BindView(R.id.welcome_intro_spinner_language) protected AppCompatSpinner mSelectLanguageSpinner;

        private boolean mInitSpinner = true;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_intro_welcome, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            ButterKnife.bind(this, view);

            ((IntroductionActivity) getActivity()).overrideFonts(view);

            String[] items = {
                    getString(R.string.english),
                    getString(R.string.italiano),
                    getString(R.string.espanol)
            };

            mSelectLanguageSpinner.setAdapter(new ArrayAdapter<String>(getContext(),
                    R.layout.select_language_spinner_item, items) {

                @NonNull
                @Override
                public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);

                    Typeface externalFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lobster-Regular.ttf");
                    ((TextView) v).setTypeface(externalFont);

                    return v;
                }

                @Override
                public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                    View v = super.getDropDownView(position, convertView, parent);

                    Typeface externalFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lobster-Regular.ttf");
                    ((TextView) v).setTypeface(externalFont);

                    return v;
                }
            });

            mSelectLanguageSpinner.setSelection(LanguageUtil.getSpinnerIndex(getContext()));
            mSelectLanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!mInitSpinner) {
                        LanguageUtil.updateAppLanguage(getActivity(), LanguageUtil.LANGUAGES[position], IntroductionActivity.class);
                    } else {
                        TextView textView = (TextView) parent.getChildAt(0);
                        if (textView != null) {
                            textView.setTextColor(Color.WHITE);
                            textView.setTextSize(22);
                        }

                        PreferenceHandler.get().setValue(getContext(), PreferenceHandler.LANGUAGE_KEY, LanguageUtil.getLanguage(getContext()));

                        mInitSpinner = false;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        @OnClick(R.id.intro_button)
        protected void onIntroButtonClick() {
            ((IntroductionActivity) getActivity()).onIntroButtonClick();
        }
    }

    public static class QRCodeIntroductionFragment extends Fragment {

        @BindView(R.id.intro_button) protected FloatingActionButton mIntroButton;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_intro_qrcode, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            ButterKnife.bind(this, view);

            ((IntroductionActivity) getActivity()).overrideFonts(view);

            if (PermissionUtil.hasPermissions(getContext())) {
                mIntroButton.setImageResource(R.drawable.ic_next);
            }
        }

        @OnClick(R.id.intro_button)
        protected void onIntroButtonClick() {
            ((IntroductionActivity) getActivity()).onIntroButtonClick();
        }

        public void setNextButton() {
            if (mIntroButton != null) {
                mIntroButton.setImageResource(R.drawable.ic_next);
            }
        }

        public void setGrantPermissionButton() {
            if (mIntroButton != null) {
                mIntroButton.setImageResource(R.drawable.ic_warning);
            }
        }
    }

    public static class EnjoyIntroductionFragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_intro_enjoy, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            ButterKnife.bind(this, view);

            ((IntroductionActivity) getActivity()).overrideFonts(view);
        }

        @OnClick(R.id.intro_button)
        protected void onIntroButtonClick() {
            ((IntroductionActivity) getActivity()).onIntroButtonClick();
        }
    }

    private class ParallaxPageTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View view, float position) {
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
