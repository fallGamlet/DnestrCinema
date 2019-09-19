package com.fallgamlet.dnestrcinema.ui.news;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by fallgamlet on 16.02.16.
 */
public class SpacerItemDecoration extends RecyclerView.ItemDecoration {

    private int space = 0;

    public SpacerItemDecoration() {
    }

    public SpacerItemDecoration setSpace(int val) {
        this.space = val;
        return this;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int pos = parent.getChildAdapterPosition(view);

        if (pos == 0) {
            outRect.set(0, space, 0, space);
        } else {
            outRect.set(0, 0, 0, space);
        }
    }
}
