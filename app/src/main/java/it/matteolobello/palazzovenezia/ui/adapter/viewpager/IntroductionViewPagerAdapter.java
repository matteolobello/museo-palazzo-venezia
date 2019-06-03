package it.matteolobello.palazzovenezia.ui.adapter.viewpager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import it.matteolobello.palazzovenezia.ui.fragment.introduction.EnjoyIntroductionFragment;
import it.matteolobello.palazzovenezia.ui.fragment.introduction.QRCodeIntroductionFragment;
import it.matteolobello.palazzovenezia.ui.fragment.introduction.WelcomeIntroductionFragment;

public class IntroductionViewPagerAdapter extends FragmentPagerAdapter {

    private final WelcomeIntroductionFragment mWelcomeFragment = new WelcomeIntroductionFragment();
    private final QRCodeIntroductionFragment mQRCodeFragment = new QRCodeIntroductionFragment();
    private final EnjoyIntroductionFragment mEnjoyFragment = new EnjoyIntroductionFragment();

    private final Fragment[] FRAGMENTS = {
            mWelcomeFragment,
            mQRCodeFragment,
            mEnjoyFragment
    };

    public IntroductionViewPagerAdapter(FragmentManager fragmentManager) {
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

    public Fragment getFragmentByClass(Class fragmentClass) {
        if (fragmentClass == WelcomeIntroductionFragment.class) {
            return mWelcomeFragment;
        } else if (fragmentClass == QRCodeIntroductionFragment.class) {
            return mQRCodeFragment;
        } else {
            return mEnjoyFragment;
        }
    }
}