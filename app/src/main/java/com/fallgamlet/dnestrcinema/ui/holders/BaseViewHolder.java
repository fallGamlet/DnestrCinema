package com.fallgamlet.dnestrcinema.ui.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by fallgamlet on 23.07.17.
 */

public class BaseViewHolder <T> extends RecyclerView.ViewHolder {

    private View rootView;
    private T item;


    public BaseViewHolder(View itemView) {
        super(itemView);
        this.rootView = itemView;

    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public View getRootView() {
        return this.rootView;
    }

    public void setRootView(View view) {
        this.rootView = view;
    }
}
