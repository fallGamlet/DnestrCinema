package com.fallgamlet.dnestrcinema;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fallgamlet.dnestrcinema.network.NetworkImageTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fallgamlet on 08.07.16.
 */
public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.ViewHolder> {
    //region Sub classes and Interfaces
    public interface OnAdapterListener {
        void onItemPressed(Drawable item, int pos);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //region Fields
        private View mRootView;
        private ImageView mImageView;
        private Drawable mDrawable;
        //endregion

        //region Constructors
        public ViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }
        //endregion

        //region Getters
        public Drawable getItem() { return mDrawable; }
        public View getRootView() { return mRootView; }
        public ImageView getImageView() { return mImageView; }
        //endregion

        //region Setters
        public void setItem(Drawable drawable) {
            mDrawable = drawable;
            if (mImageView != null) {
                mImageView.setImageDrawable(drawable);
            }
        }
        public void setImageView(ImageView imageView) {
            mImageView = imageView;
            if (mImageView != null) {
                mImageView.setImageDrawable(mDrawable);
            }
        }
        //endregion

        //region Methods
        public void initView(View itemView) {
            mRootView = itemView;
            mImageView = ( mRootView instanceof ImageView)? (ImageView) mRootView: null;
            if (mRootView != null && mImageView == null) {
                mImageView = (ImageView) mRootView.findViewById(R.id.imageView);
            }
        }

        public void initData(Drawable drawable) {
            setItem(drawable);
        }
        //endregion
    }
    //endregion

    //region Fields
    protected OnAdapterListener mListener;
    protected ArrayList<Drawable> mListData;

    protected NetworkImageTask imageTask;
    //endregion

    //region Methods
    public ImageRecyclerAdapter() {
        imageTask = new NetworkImageTask();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getListener() != null) {
                    int pos = getPosition(holder.getItem());
                    getListener().onItemPressed(holder.getItem(), pos);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.initData(getItem(position));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        if (payloads == null || payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
            return;
        }

        Object obj = payloads.get(0);
        // Если изменения не пустые и это пришла загруженная картинка
        if (obj instanceof Drawable) {
            // устанавливаем картинку
            if (holder.getImageView() != null){
                holder.getImageView().setImageDrawable((Drawable) obj);
            }
        } else if (obj instanceof Bitmap) {
            if (holder.getImageView() != null){
                holder.getImageView().setImageBitmap((Bitmap) obj);
            }
        }
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }

    public Drawable getItem(int position) {
        ArrayList<Drawable> data = getData();
        return  (position < 0 || position >= data.size())? null: data.get(position);
    }

    public int getPosition(Drawable item) {
        return getData().indexOf(item);
    }

    public void setData(List<Drawable> list) {
        if (list != null && !list.isEmpty()) {
            ArrayList<Drawable> data = getData();
            data.clear();
            data.addAll(list);
        }
    }

    public ArrayList<Drawable> getData() {
        if (mListData == null) {
            mListData = new ArrayList<>(10);
        }
        return mListData;
    }

    public void addItem(Drawable item) {
        if (item != null) {
            int pos = getPosition(item);
            if (pos == -1) {
                getData().add(item);
                notifyItemInserted(getData().size()-1);
            }
        }
    }

    public void setListener(OnAdapterListener listener) {
        mListener = listener;
    }

    public OnAdapterListener getListener() {
        return mListener;
    }
    //endregion
}
