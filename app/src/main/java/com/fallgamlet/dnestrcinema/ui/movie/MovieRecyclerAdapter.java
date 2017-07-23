package com.fallgamlet.dnestrcinema.ui.movie;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.mvp.models.Config;
import com.fallgamlet.dnestrcinema.mvp.models.MovieItem;
import com.fallgamlet.dnestrcinema.ui.holders.MovieViewHolder;
import com.fallgamlet.dnestrcinema.utils.HttpUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fallgamlet on 08.07.16.
 */
public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    //region Sub classes and Interfaces
    public interface OnAdapterListener {
        void onItemPressed(MovieItem item, int pos);
        void onItemSchedulePressed(MovieItem item, int pos);
        void onItemBuyTicketPressed(MovieItem item, int pos);
    }
    //endregion

    //region Fields
    private int itemLayoutId;
    private OnAdapterListener mListener;
    private List<MovieItem> mListData, mListDataFiltered;
    //endregion

    //region Methods
    public MovieRecyclerAdapter(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false);
        final MovieViewHolder holder = new MovieViewHolder(view);

        holder.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    int pos = getPosition(holder.getItem());
                    mListener.onItemPressed(holder.getItem(), pos);
                }
            }
        });

        if (holder.getBuyTicketButton() != null) {
            holder.getBuyTicketButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int pos = getPosition(holder.getItem());
                        mListener.onItemBuyTicketPressed(holder.getItem(), pos);
                    }
                }
            });
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final MovieItem item = getItem(position);
        holder.initData(item);

        //region Load and set Image
        String imgUrl = item.getPosterUrl();

        if (imgUrl != null) {
            String baseUrl = Config.getInstance().getRequestFactory().getBaseUrl();
            imgUrl = HttpUtils.getAbsoluteUrl(baseUrl, imgUrl);
        }

        if (imgUrl != null) {
            holder.getImageView().setImageResource(R.drawable.ic_local_movies_24dp);
            Picasso.with(holder.getImageView().getContext()).load(imgUrl)
                    .into(holder.getImageView());
        }
        //endregion
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position, List<Object> payloads) {
        if (payloads == null || payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
            return;
        }

        Object obj = payloads.get(0);
        // Если изменения не пустые и это пришла загруженная картинка
        if (obj != null && obj instanceof Bitmap) {
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
    //endregion
}
