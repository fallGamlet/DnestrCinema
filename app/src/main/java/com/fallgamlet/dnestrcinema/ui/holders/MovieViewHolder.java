package com.fallgamlet.dnestrcinema.ui.holders;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.domain.models.MovieItem;
import com.fallgamlet.dnestrcinema.domain.models.ScheduleItem;
import com.fallgamlet.dnestrcinema.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by fallgamlet on 23.07.17.
 */

public class MovieViewHolder extends RecyclerView.ViewHolder {

    private View mRootView;
    private ImageView mImageView;
    private TextView mTitleView;
    private TextView mPubdateView;
    private TextView mScheduleView;
    private View mBuyTicketButton;
    private MovieItem mItem;



    public MovieViewHolder(View itemView) {
        super(itemView);
        initView(itemView);
    }


    public MovieItem getItem() { return mItem; }

    public View getRootView() { return mRootView; }

    public ImageView getImageView() { return mImageView; }

    public TextView getTitleView() { return mTitleView; }

    public TextView getPubdateView() { return mPubdateView; }

    public TextView getScheduleView() { return mScheduleView; }

    public View getBuyTicketButton() {
        return mBuyTicketButton;
    }



    public void initView(View itemView) {
        mRootView = itemView;
        if (mRootView != null) {
            mImageView = mRootView.findViewById(R.id.imageView);
            mTitleView = mRootView.findViewById(R.id.titleView);
            mPubdateView = mRootView.findViewById(R.id.pubdateView);
            mScheduleView = mRootView.findViewById(R.id.roomView);
            mBuyTicketButton = mRootView.findViewById(R.id.buyTicketButton);
        }
    }

    public void initData(MovieItem item) {
        mItem = item;

        if (item == null) { return; }

        ArrayList<String> arr = new ArrayList<>();
        StringBuilder strBuilder = new StringBuilder();
        for (ScheduleItem schedule: mItem.getSchedules()) {
            strBuilder.append(schedule.room);
            if (schedule.value != null) {
                strBuilder.append(": ").append(schedule.value.trim());
            }
            arr.add(strBuilder.toString());
            strBuilder.setLength(0);
        }

        String roomsStr = TextUtils.join("\n", arr);
        String titleStr = item.getTitle() == null? null: item.getTitle().replaceAll("[ ]/[ ]", "\n").trim();

        setTitle(titleStr);
        setPubDate(item.getPubDate());
        setSchedule(roomsStr);
    }

    public void setTitle(CharSequence title) {
        if (mTitleView != null) {
            mTitleView.setText(title);
        }
    }

    public void setPubDate(Date date) {
        if (mPubdateView != null) {
            mPubdateView.setText(DateTimeUtils.INSTANCE.getDateNamed(date));
        }
    }

    public void setSchedule(CharSequence schedule) {
        if (mScheduleView != null) {
            mScheduleView.setText(schedule);
        }
    }

}
