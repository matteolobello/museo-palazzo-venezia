package com.matteolobello.palazzovenezia.ui.scroll;

import android.support.annotation.IntDef;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class ScrollHandler {

    public static final int UP = 0;
    public static final int DOWN = 1;

    @IntDef({UP, DOWN})
    public @interface ScrollDirection {
    }

    public interface OnScrollListener {
        void onScroll(@ScrollDirection int scrollDirection);
    }

    public static void setOnScrollListener(NestedScrollView nestedScrollView, final OnScrollListener onScrollListener) {
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                onScrollListener.onScroll(scrollY > oldScrollY
                        ? DOWN
                        : UP);
            }
        });
    }

    public static void setOnScrollListener(final RecyclerView recyclerView, final OnScrollListener onScrollListener) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (!(layoutManager instanceof GridLayoutManager)) {
            throw new IllegalStateException("Not a grid");
        }

        final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0 || gridLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    onScrollListener.onScroll(DOWN);
                }

                if (dy <= 0) {
                    onScrollListener.onScroll(UP);
                }
            }
        });
    }
}
