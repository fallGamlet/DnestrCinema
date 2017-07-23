package com.fallgamlet.dnestrcinema.ui.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.mvp.models.NewsItem;
import com.fallgamlet.dnestrcinema.utils.DateTimeUtils;

import java.util.Date;

/**
 * Created by fallgamlet on 23.07.17.
 */

public class NewsViewHolder extends BaseViewHolder<NewsItem> {

    private ImageView mImageView;
    private TextView mTitleView;
    private TextView mDateView;
    private TextView mBodyView;


    public NewsViewHolder(View itemView) {
        super(itemView);
        initView(itemView);
    }


    public ImageView getImageView() {
        return mImageView;
    }

    public TextView getTitleView() {
        return mTitleView;
    }

    public TextView getDateView() {
        return mDateView;
    }

    public TextView getBodyView() {
        return mBodyView;
    }


    public void initView(View itemView) {
        if (itemView != null) {
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
            mTitleView = (TextView) itemView.findViewById(R.id.titleView);
            mDateView = (TextView) itemView.findViewById(R.id.dateView);
            mBodyView = (TextView) itemView.findViewById(R.id.textView);
        }
    }

    @Override
    public void setItem(NewsItem item) {
        super.setItem(item);
        updateData();
    }

    private void updateData() {
        NewsItem item = getItem();
        if (item == null) {
            item = new NewsItem();
        }

        setTitle(item.getTitle());
        setDate(item.getDate());
        setBody(item.getBody());
    }


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
}
