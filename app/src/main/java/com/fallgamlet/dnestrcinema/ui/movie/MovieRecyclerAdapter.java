package com.fallgamlet.dnestrcinema.ui.movie;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.app.AppFacade;
import com.fallgamlet.dnestrcinema.app.GlideApp;
import com.fallgamlet.dnestrcinema.domain.models.MovieItem;
import com.fallgamlet.dnestrcinema.ui.holders.MovieViewHolder;
import com.fallgamlet.dnestrcinema.utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;


public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private int itemLayoutId;
    private OnAdapterListener mListener;
    private List<MovieItem> mListData, mListDataFiltered;

    public MovieRecyclerAdapter(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false);
        final MovieViewHolder holder = new MovieViewHolder(view);

        holder.getRootView().setOnClickListener(view1 -> {
            if (mListener != null) {
                int pos = getPosition(holder.getItem());
                mListener.onItemPressed(holder.getItem(), pos);
            }
        });

        if (holder.getBuyTicketButton() != null) {
            holder.getBuyTicketButton().setOnClickListener(view12 -> {
                if (mListener != null) {
                    int pos = getPosition(holder.getItem());
                    mListener.onItemBuyTicketPressed(holder.getItem(), pos);
                }
            });
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final MovieItem item = getItem(position);
        holder.initData(item);

        String imgUrl = item.getPosterUrl();
        if (imgUrl != null && !imgUrl.isEmpty()) {
            String baseUrl = AppFacade.Companion.getInstance().getRequestFactory().getBaseUrl();
            imgUrl = HttpUtils.INSTANCE.getAbsoluteUrl(baseUrl, imgUrl);
        }

        GlideApp.with(holder.getImageView())
                .load(imgUrl)
                .placeholder(R.drawable.ic_local_movies_24dp)
                .fallback(R.drawable.ic_local_movies_24dp)
                .error(R.drawable.ic_local_movies_24dp)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.getImageView());
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
            return;
        }

        Object obj = payloads.get(0);
        // Если изменения не пустые и это пришла загруженная картинка
        if (obj instanceof Bitmap) {
            // устанавливаем картинку
            holder.getImageView().setImageBitmap((Bitmap)obj);
        }
    }

    @Override
    public int getItemCount() {
        return mListDataFiltered == null? 0 : mListDataFiltered.size();
    }

    public MovieItem getItem(int position) {
        return  (mListDataFiltered == null || position < 0 || position >= mListDataFiltered.size()) ? null : mListDataFiltered.get(position);
    }

    public int getPosition(MovieItem item) {
        if (mListDataFiltered == null) return -1;
        return mListDataFiltered.indexOf(item);
    }

    public void setData(List<MovieItem> list) {
        if (mListData == null) { mListData = new ArrayList<>(); }
        else { mListData.clear(); }

        if (mListDataFiltered == null) { mListDataFiltered = new ArrayList<>(); }
        else { mListDataFiltered.clear(); }

        if (list != null) { mListData.addAll(list); }
        mListDataFiltered.addAll(mListData);
    }

    public void setListener(OnAdapterListener listener) {
        mListener = listener;
    }


    public interface OnAdapterListener {
        void onItemPressed(MovieItem item, int pos);
        void onItemSchedulePressed(MovieItem item, int pos);
        void onItemBuyTicketPressed(MovieItem item, int pos);
    }
}
