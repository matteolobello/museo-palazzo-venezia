package it.matteolobello.palazzovenezia.ui.fragment.home;

import android.app.Activity;
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

import com.matteolobello.palazzovenezia.R;
import it.matteolobello.palazzovenezia.data.bundle.BundleKeys;
import it.matteolobello.palazzovenezia.data.model.Painting;
import it.matteolobello.palazzovenezia.ui.adapter.recyclerview.PaintingsRecyclerViewAdapter;
import it.matteolobello.palazzovenezia.ui.view.recyclerview.PaintingsRecyclerViewItemDecoration;

public class PaintingsFragment extends Fragment {

    private NestedScrollView mNestedScrollView;
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

        final int titleMarginTop = ((LinearLayout.LayoutParams) mTitleTextView.getLayoutParams()).topMargin;
        mNestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = mNestedScrollView.getScrollY();
                float percentage = scrollY == 0 ? 0 : scrollY * 100 / titleMarginTop;

                mTitleTextView.setAlpha(1 - (percentage / 100));
            }
        });

        mRecyclerView.setFocusable(false);
        mTitleTextView.requestFocus();
    }
}
