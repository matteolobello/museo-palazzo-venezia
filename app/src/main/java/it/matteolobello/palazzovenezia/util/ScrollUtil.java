package it.matteolobello.palazzovenezia.util;

import androidx.annotation.IntDef;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ScrollUtil {

    public static final int UP = 0;
    public static final int DOWN = 1;

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

    @IntDef({UP, DOWN})
    public @interface ScrollDirection {
    }

    public interface OnScrollListener {
        void onScroll(@ScrollDirection int scrollDirection);
    }
}
