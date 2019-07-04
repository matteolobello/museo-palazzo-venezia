package it.matteolobello.palazzovenezia.ui.fragment.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import it.matteolobello.palazzovenezia.R;
import it.matteolobello.palazzovenezia.util.SystemBarsUtil;

public class MapFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final BlurView blurView = view.findViewById(R.id.blur_view);
        blurView.getLayoutParams().height = SystemBarsUtil.getStatusBarHeight(getContext());
        blurView.setupWith((ViewGroup) view)
                .setFrameClearDrawable(view.getBackground())
                .setBlurAlgorithm(new RenderScriptBlur(getContext()))
                .setBlurRadius(20f)
                .setOverlayColor(Color.argb(60, 255, 255, 255))
                .setHasFixedTransformationMatrix(true);

    }
}
