package it.matteolobello.palazzovenezia.ui.fragment.home;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import it.matteolobello.palazzovenezia.R;
import it.matteolobello.palazzovenezia.data.bundle.BundleKeys;
import it.matteolobello.palazzovenezia.data.model.Painting;
import it.matteolobello.palazzovenezia.ui.activity.HomeActivity;
import it.matteolobello.palazzovenezia.ui.adapter.recyclerview.PaintingsRecyclerViewAdapter;
import it.matteolobello.palazzovenezia.ui.view.recyclerview.PaintingsRecyclerViewItemDecoration;
import it.matteolobello.palazzovenezia.util.DpPxUtil;
import it.matteolobello.palazzovenezia.util.SystemBarsUtil;

public class PaintingsFragment extends Fragment {

    private NestedScrollView mNestedScrollView;
    private BlurView mToolbarView;
    private TextView mTitleTextView;
    private RecyclerView mRecyclerView;
    private View mPaddingBottomView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_paintings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNestedScrollView = view.findViewById(R.id.nested_scroll_view);
        mToolbarView = view.findViewById(R.id.toolbar);
        mTitleTextView = view.findViewById(R.id.paintings_title_text_view);
        mRecyclerView = view.findViewById(R.id.paintings_recycler_view);
        mPaddingBottomView = view.findViewById(R.id.scroll_view_padding_bottom_view);

        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        Drawable windowBackground = view.getBackground();
        mToolbarView.setPadding(0, SystemBarsUtil.getStatusBarHeight(activity), 0, 0);
        mToolbarView.getLayoutParams().height = DpPxUtil.convertDpToPixel(56) + SystemBarsUtil.getStatusBarHeight(activity);
        mToolbarView.setupWith((ViewGroup) view)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(getContext()))
                .setBlurRadius(20f)
                .setOverlayColor(HomeActivity.BLUR_OVERLAY_COLOR)
                .setHasFixedTransformationMatrix(true);
        mToolbarView.animate()
                .translationY(-mToolbarView.getHeight())
                .alpha(0.0f)
                .setDuration(0)
                .start();

        ((LinearLayout.LayoutParams) mTitleTextView.getLayoutParams()).topMargin =
                SystemBarsUtil.getStatusBarHeight(activity);

        ((LinearLayout.LayoutParams) mPaddingBottomView.getLayoutParams()).height =
                SystemBarsUtil.getNavigationBarHeight(activity)
                        + DpPxUtil.convertDpToPixel(56);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(new PaintingsRecyclerViewAdapter(view,
                activity.getIntent().<Painting>getParcelableArrayListExtra(BundleKeys.EXTRA_ALL_PAINTINGS)));
        mRecyclerView.addItemDecoration(new PaintingsRecyclerViewItemDecoration());

        mToolbarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNestedScrollView.smoothScrollTo(0, 0);
            }
        });

        mNestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            boolean isToolbarVisible = false;

            @Override
            public void onScrollChanged() {
                float scrollY = mNestedScrollView.getScrollY();
                float scrollProgressPercentage = scrollY == 0 ? 0 :
                        scrollY * 100 / DpPxUtil.convertDpToPixel(20);

                mTitleTextView.setAlpha(1 - (scrollProgressPercentage / 100));

                if (scrollProgressPercentage >= 100) {
                    if (!isToolbarVisible) {
                        mToolbarView.animate()
                                .translationY(0)
                                .alpha(1.0f)
                                .setDuration(200)
                                .start();

                        isToolbarVisible = true;
                    }
                } else {
                    if (isToolbarVisible) {
                        mToolbarView.animate()
                                .translationY(-mToolbarView.getHeight())
                                .alpha(0.0f)
                                .setDuration(200)
                                .start();

                        isToolbarVisible = false;
                    }
                }
            }
        });

        mRecyclerView.setFocusable(false);
        mTitleTextView.requestFocus();
    }
}
