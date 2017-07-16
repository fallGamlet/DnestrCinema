package com.fallgamlet.dnestrcinema.ui.news;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.mvp.models.NewsItem;
import com.fallgamlet.dnestrcinema.network.KinoTir;
import com.fallgamlet.dnestrcinema.utils.DateTimeUtils;
import com.fallgamlet.dnestrcinema.utils.HttpUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by fallgamlet on 16.07.17.
 */

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder> {
    //region Sub classes and Interfaces
    public interface OnAdapterListener {
        void onItemPressed(NewsItem item, int pos);
        void onItemSchedulePressed(NewsItem item, int pos);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //region Fields
        View mRootView;
        ImageView mImageView;
        TextView mTitleView;
        TextView mDateView;
        TextView mBodyView;

        NewsItem mItem;
        //endregion

        //region Constructors
        public ViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }
        //endregion

        //region Getters
        public NewsItem getItem() { return mItem; }
        public View getRootView() { return mRootView; }
        public ImageView getImageView() { return mImageView; }
        public TextView getTitleView() { return mTitleView; }
        public TextView getDateeView() { return mDateView; }
        public TextView getBodyView() { return mBodyView; }
        //endregion

        //region Methods
        public void initView(View itemView) {
            mRootView = itemView;
            if (mRootView != null) {
                mImageView = (ImageView) mRootView.findViewById(R.id.imageView);
                mTitleView = (TextView) mRootView.findViewById(R.id.titleView);
                mDateView = (TextView) mRootView.findViewById(R.id.dateView);
                mBodyView = (TextView) mRootView.findViewById(R.id.textView);
            }
        }

        public void initData(NewsItem item) {
            mItem = item;
            if (item != null) {
                setTitle(item.getTitle());
                setDate(item.getDate());
                setBody(item.getBody());
            }
        }


        //endregion

        //region Set visible
        public void setImageVisible(boolean v) {
            if (mImageView != null) {
                mImageView.setVisibility(v? View.VISIBLE: View.GONE);
            }
        }

        public void setTitleVisible(boolean v) {
            if (mTitleView != null) {
                mTitleView.setVisibility(v? View.VISIBLE: View.GONE);
            }
        }

        public void setDateVisible(boolean v) {
            if (mDateView != null) {
                mDateView.setVisibility(v? View.VISIBLE: View.GONE);
            }
        }

        public void setBodyVisible(boolean v) {
            if (mBodyView != null) {
                mBodyView.setVisibility(v? View.VISIBLE: View.GONE);
            }
        }
        //endregion

        //region Set data
        public void setTitle(String val) {
            if (mTitleView != null) {
                mTitleView.setText(val);
            }
            setTitleVisible(!(val == null || val.isEmpty()));
        }

        public void setDate(Date val) {
            if (mDateView != null) {
                mDateView.setText(DateTimeUtils.getDateDotWithoutTime(val));
            }
            setDateVisible(val != null);
        }

        public void setBody(String val) {
            if (mBodyView != null) {
                mBodyView.setText(val);
            }
            setBodyVisible(!(val == null || val.isEmpty()));
        }

        //endregion
    }
    //endregion

    //region Fields
    protected NewsRecyclerAdapter.OnAdapterListener mListener;
    protected List<NewsItem> mListData, mListDataFiltered;
    //endregion

    //region Methods
    public NewsRecyclerAdapter() {
    }

    @Override
    public NewsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        final NewsRecyclerAdapter.ViewHolder holder = new NewsRecyclerAdapter.ViewHolder(view);

        holder.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    int pos = getPosition(holder.getItem());
                    mListener.onItemPressed(holder.getItem(), pos);
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(NewsRecyclerAdapter.ViewHolder holder, int position) {
        final NewsItem item = getItem(position);
        holder.initData(item);

        //region Load and set Image
        Iterator<String> iterator = item.getImgUrls().iterator();
        String imgUrl = iterator.hasNext()? iterator.next(): null;

        if (imgUrl != null) {
            imgUrl = HttpUtils.getAbsoluteUrl(KinoTir.BASE_URL, imgUrl);
        }

        if (imgUrl != null) {
            Picasso.with(holder.getImageView().getContext()).load(imgUrl).into(holder.getImageView());
        }
        //endregion
    }

    @Override
    public void onBindViewHolder(NewsRecyclerAdapter.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads == null || payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
            return;
        }

        Object obj = payloads.get(0);
        // Если изменения не пустые и это пришла загруженная картинка
        if (obj != null && obj instanceof Bitmap) {
            // устанавливаем картинку
            holder.getImageView().setImageBitmap((Bitmap)obj);
            holder.getImageView().setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mListDataFiltered == null? 0 : mListDataFiltered.size();
    }

    public NewsItem getItem(int position) {
        return  (mListDataFiltered == null || position < 0 || position >= mListDataFiltered.size()) ? null : mListDataFiltered.get(position);
    }

    public int getPosition(NewsItem item) {
        if (mListDataFiltered == null) return -1;
        return mListDataFiltered.indexOf(item);
    }

    public void setData(List<NewsItem> list) {
        if (mListData == null) { mListData = new ArrayList<>(); }
        else { mListData.clear(); }

        if (mListDataFiltered == null) { mListDataFiltered = new ArrayList<>(); }
        else { mListDataFiltered.clear(); }

        if (list != null) { mListData.addAll(list); }
        mListDataFiltered.addAll(mListData);
    }

    public void setListener(NewsRecyclerAdapter.OnAdapterListener listener) {
        mListener = listener;
    }
    //endregion
}
