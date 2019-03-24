package com.matteolobello.palazzovenezia.ui.view.recyclerview;

import android.graphics.Rect;
import android.view.View;

import com.matteolobello.palazzovenezia.util.DpPxUtil;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PaintingsRecyclerViewItemDecoration extends RecyclerView.ItemDecoration {

    private static final int COLUMNS = 2;
    private static final int MARGIN = DpPxUtil.convertDpToPixel(2);

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        final int position = parent.getChildLayoutPosition(view);

        if (position % COLUMNS == 0) {
            outRect.right = MARGIN;
        } else {
            outRect.left = MARGIN;
        }
    }
}