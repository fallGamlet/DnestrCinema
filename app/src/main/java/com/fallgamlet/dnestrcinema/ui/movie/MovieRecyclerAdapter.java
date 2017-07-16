package com.fallgamlet.dnestrcinema.ui.movie;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.mvp.models.ScheduleItem;
import com.fallgamlet.dnestrcinema.utils.DateTimeUtils;
import com.fallgamlet.dnestrcinema.utils.HttpUtils;
import com.fallgamlet.dnestrcinema.network.KinoTir;
import com.fallgamlet.dnestrcinema.mvp.models.MovieItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fallgamlet on 08.07.16.
 */
public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.ViewHolder> {

    //region Sub classes and Interfaces
    public interface OnAdapterListener {
        void onItemPressed(MovieItem item, int pos);
        void onItemSchedulePressed(MovieItem item, int pos);
        void onItemBuyTicketPressed(MovieItem item, int pos);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //region Fields
        View mRootView;
        ImageView mImageView;
        TextView mTitleView;
        TextView mPubdateView;
        TextView mScheduleView;
        View mBuyTicketButton;

        MovieItem mItem;
        //endregion

        //region Constructors
        public ViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }
        //endregion

        //region Getters
        public MovieItem getItem() { return mItem; }

        public View getRootView() { return mRootView; }

        public ImageView getImageView() { return mImageView; }

        public TextView getTitleView() { return mTitleView; }

        public TextView getPubdateView() { return mPubdateView; }

        public TextView getScheduleView() { return mScheduleView; }

        public View getBuyTicketButton() {
            return mBuyTicketButton;
        }
        //endregion

        //region Methods
        public void initView(View itemView) {
            mRootView = itemView;
            if (mRootView != null) {
                mImageView = (ImageView) mRootView.findViewById(R.id.imageView);
                mTitleView = (TextView) mRootView.findViewById(R.id.titleView);
                mPubdateView = (TextView) mRootView.findViewById(R.id.pubdateView);
                mScheduleView = (TextView) mRootView.findViewById(R.id.roomView);
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
                mPubdateView.setText(DateTimeUtils.getDateNamed(date));
            }
        }

        public void setSchedule(CharSequence schedule) {
            if (mScheduleView != null) {
                mScheduleView.setText(schedule);
            }
        }
        //endregion
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false);
        final ViewHolder holder = new ViewHolder(view);

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MovieItem item = getItem(position);
        holder.initData(item);

        //region Load and set Image
        String imgUrl = item.getPosterUrl();

        if (imgUrl != null) {
            imgUrl = HttpUtils.getAbsoluteUrl(KinoTir.BASE_URL, imgUrl);
        }

        if (imgUrl != null) {
            holder.getImageView().setImageResource(R.drawable.ic_local_movies_24dp);
            Picasso.with(holder.getImageView().getContext()).load(imgUrl)
                    .into(holder.getImageView());
        }
        //endregion
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        if (payloads == null || payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
            return;
        }

        Object obj = payloads.get(0);
        // Если изменения не пустые и это пришла загруженная картинка
        if (obj != null && obj instanceof Bitmap) {
            // устанавливаем картинку
            holder.mImageView.setImageBitmap((Bitmap)obj);
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
