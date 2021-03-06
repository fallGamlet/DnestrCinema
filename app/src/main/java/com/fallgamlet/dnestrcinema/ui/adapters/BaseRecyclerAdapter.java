package com.fallgamlet.dnestrcinema.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fallgamlet.dnestrcinema.ui.holders.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fallgamlet on 16.07.17.
 */

public abstract class BaseRecyclerAdapter <T, VH extends BaseViewHolder<T>>
        extends RecyclerView.Adapter<VH>
{

    public interface OnAdapterListener<T> {
        void onItemPressed(T item, int pos);
    }


    private OnAdapterListener<T> mListener;
    private List<T> mListData;


    public BaseRecyclerAdapter() {
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
        final VH holder = createViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null && holder != null) {
                    int pos = getPosition(holder.getItem());
                    mListener.onItemPressed(holder.getItem(), pos);
                }
            }
        });

        return holder;
    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract VH createViewHolder(View view);

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final T item = getItem(position);
        holder.setItem(item);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }

    public T getItem(int position) {
        return  (position < 0 || position >= getItemCount()) ? null : getData().get(position);
    }

    public int getPosition(T item) {
        return getData().indexOf(item);
    }

    public void setData(List<T> list) {
        clear();
        add(list);
    }

    public void clear() {
        List<T> items = getData();
        int count = items.size();
        items.clear();

        if (count > 0) {
            notifyItemRangeRemoved(0, count);
        }
    }

    public void add(List<T> list) {
        if (list != null) {
            List<T> items = getData();
            int start = items.size();
            int count = list.size();

            items.addAll(list);
            notifyItemRangeInserted(start, count);
        }
    }

    private synchronized List<T> getData() {
        if (mListData == null) {
            mListData = new ArrayList<>(30);
        }

        return mListData;
    }

    public void setListener(OnAdapterListener<T> listener) {
        this.mListener = listener;
    }

    public OnAdapterListener<T> getListener() {
        return this.mListener;
    }
}
