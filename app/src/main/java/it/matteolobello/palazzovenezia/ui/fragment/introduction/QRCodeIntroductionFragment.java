package it.matteolobello.palazzovenezia.ui.fragment.introduction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import it.matteolobello.palazzovenezia.R;
import it.matteolobello.palazzovenezia.ui.activity.IntroductionActivity;
import it.matteolobello.palazzovenezia.util.PermissionUtil;
import it.matteolobello.palazzovenezia.util.SystemBarsUtil;

public class QRCodeIntroductionFragment extends Fragment {

    private FloatingActionButton mIntroButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_intro_qrcode, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final IntroductionActivity introductionActivity = (IntroductionActivity) getActivity();
        if (introductionActivity == null) {
            return;
        }

        mIntroButton = view.findViewById(R.id.intro_button);

        if (PermissionUtil.hasPermissions(getContext())) {
            mIntroButton.setImageResource(R.drawable.ic_next);
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mIntroButton.getLayoutParams();
        layoutParams.setMargins(0, 0, 0,
                layoutParams.bottomMargin + SystemBarsUtil.getNavigationBarHeight(getActivity()));
        mIntroButton.setLayoutParams(layoutParams);
        mIntroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                introductionActivity.onIntroButtonClick();
            }
        });
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