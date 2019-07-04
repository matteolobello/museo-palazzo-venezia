package it.matteolobello.palazzovenezia.ui.fragment.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import it.matteolobello.palazzovenezia.R;
import it.matteolobello.palazzovenezia.data.bundle.BundleKeys;
import it.matteolobello.palazzovenezia.data.model.Painting;
import it.matteolobello.palazzovenezia.ui.adapter.recyclerview.PaintingsRecyclerViewAdapter;
import it.matteolobello.palazzovenezia.ui.view.recyclerview.PaintingsRecyclerViewItemDecoration;
import it.matteolobello.palazzovenezia.util.DpPxUtil;

public class PaintingsFragment extends Fragment {

    private NestedScrollView mNestedScrollView;
    private View mToolbarView;
    private TextView mTitleTextView;
    private RecyclerView mRecyclerView;

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

        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

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
                        scrollY * 100 / (mTitleTextView.getHeight() - DpPxUtil.convertDpToPixel(20));

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
