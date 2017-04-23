package com.fallgamlet.dnestrcinema;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
//        int count = state.getItemCount();

        if (pos == 0) {
            outRect.set(0, space, 0, space);
        } else {
            outRect.set(0, 0, 0, space);
        }
    }
}