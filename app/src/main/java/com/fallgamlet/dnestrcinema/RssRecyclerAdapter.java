package com.fallgamlet.dnestrcinema;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fallgamlet.dnestrcinema.network.NetworkImageTask;
import com.fallgamlet.dnestrcinema.network.RssItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fallgamlet on 08.07.16.
 */
public class RssRecyclerAdapter extends RecyclerView.Adapter<RssRecyclerAdapter.ViewHolder> {
    //region Sub classes and Interfaces
    public interface OnAdapterListener {
        void onItemPressed(RssItem item, int pos);
        void onItemSchedulePressed(RssItem item, int pos);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //region Fields
        View mRootView;
        ImageView mImageView;
        TextView mTitleView;
        TextView mPubdateView;
        TextView mScheduleView;
//        TextView mDescriptionView;

        RssItem mItem;
        //endregion

        //region Constructors
        public ViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }
        //endregion

        //region Getters
        public RssItem getItem() { return mItem; }
        public View getRootView() { return mRootView; }
        public ImageView getImageView() { return mImageView; }
        public TextView getTitleView() { return mTitleView; }
        public TextView getPubdateView() { return mPubdateView; }
        public TextView getScheduleView() { return mScheduleView; }
        //endregion

        //region Methods
        public void initView(View itemView) {
            mRootView = itemView;
            if (mRootView != null) {
                mImageView = (ImageView) mRootView.findViewById(R.id.imageView);
                mTitleView = (TextView) mRootView.findViewById(R.id.titleView);
                mPubdateView = (TextView) mRootView.findViewById(R.id.pubdateView);
                mScheduleView = (TextView) mRootView.findViewById(R.id.roomView);
//                mDescriptionView = (TextView) mRootView.findViewById(R.id.descriptionView);
            }
        }

        public void initData(RssItem item) {
            mItem = item;
            if (item == null) { return; }
            Spanned desc = fromHtml(item.getDescription());

            ArrayList<String> arr = new ArrayList<>();
            StringBuilder strBuilder = new StringBuilder();
            for (RssItem.Schedule schedule: mItem.getSchedules()) {
                strBuilder.append(schedule.room);
                if (schedule.value != null) {
                    strBuilder.append(": ").append(schedule.value.trim());
                }
                arr.add(strBuilder.toString());
                strBuilder.setLength(0);
            }

            String roomsStr = TextUtils.join("\n", arr);

            String titleStr = item.getTitle() == null? null: item.getTitle().replaceAll("[ ]/[ ]", "\n").trim();
//            String[] titleArr = item.getTitle()==null? null: item.getTitle().split("/");
//            if (titleArr != null) {
//                for (int i=0; i<titleArr.length; i++) {
//                    if (titleArr[i] != null) { titleArr[i] = titleArr[i].trim(); }
//                }
//                titleStr = TextUtils.join("\n", titleArr);
//            }

            if (mTitleView != null) { mTitleView.setText(titleStr); }
            if (mPubdateView != null) { mPubdateView.setText(DateTimeFormatter.getDateDotWithoutTime(item.getPubDate())); }
            if (mScheduleView != null) { mScheduleView.setText(roomsStr); }
//            if (mDescriptionView != null) { mDescriptionView.setText(desc); }
        }
        //endregion
    }
    //endregion

    //region Fields
    protected OnAdapterListener mListener;
    protected List<RssItem> mListData, mListDataFiltered;

    protected NetworkImageTask imageTask;
    //endregion

    //region Methods
    public RssRecyclerAdapter() {
        imageTask = new NetworkImageTask();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_item, parent, false);
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

//        holder.getScheduleView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mListener != null) {
//                    int pos = getPosition(holder.getItem());
//                    mListener.onItemSchedulePressed(holder.getItem(), pos);
//                }
//            }
//        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RssItem item = getItem(position);
        holder.initData(item);

        //region Load and set Image
        String imgUrl = item.getImgUrl();
        try {
            holder.mImageView.setImageResource(R.drawable.ic_local_movies_black_24dp);
            // если ссылка есть
            if (imgUrl != null) {
                Bitmap img = NetworkImageTask.cachedImages.get(imgUrl);
                if (img != null) {
                    holder.mImageView.setImageBitmap(img);
                } else {
                    imageTask.requestImage(imgUrl, new NetworkImageTask.NetworkImageCallback() {
                        @Override
                        public void onImageLoaded(NetworkImageTask.UrlImage urlImg) {
                            if (urlImg.img != null && urlImg.url != null && urlImg.url.equalsIgnoreCase(item.getImgUrl())) {
                                int pos = getPosition(item);
                                // посылаем сигнал, что элемент нужно обновить - установить картинку
                                notifyItemChanged(pos, urlImg.img);
                            }
                        }
                    });
                }
            }
        } catch (Exception ignored) {
            holder.mImageView.setImageResource(R.drawable.ic_local_movies_black_24dp);
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

    public RssItem getItem(int position) {
        return  (mListDataFiltered == null || position < 0 || position >= mListDataFiltered.size()) ? null : mListDataFiltered.get(position);
    }

    public int getPosition(RssItem item) {
        if (mListDataFiltered == null) return -1;
        return mListDataFiltered.indexOf(item);
    }

    public void setData(List<RssItem> list) {
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

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
    //endregion
}
