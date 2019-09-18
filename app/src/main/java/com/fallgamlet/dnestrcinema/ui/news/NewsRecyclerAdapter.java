package com.fallgamlet.dnestrcinema.ui.news;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import android.view.View;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.app.GlideApp;
import com.fallgamlet.dnestrcinema.app.AppFacade;
import com.fallgamlet.dnestrcinema.domain.models.NewsItem;
import com.fallgamlet.dnestrcinema.ui.adapters.BaseRecyclerAdapter;
import com.fallgamlet.dnestrcinema.ui.holders.NewsViewHolder;
import com.fallgamlet.dnestrcinema.utils.CollectionUtils;
import com.fallgamlet.dnestrcinema.utils.HttpUtils;
import com.fallgamlet.dnestrcinema.utils.ViewUtils;

import java.util.Iterator;
import java.util.List;


public class NewsRecyclerAdapter extends BaseRecyclerAdapter<NewsItem, NewsViewHolder> {


    public NewsRecyclerAdapter() {
    }


    @Override
    protected int getLayoutId() {
        return R.layout.news_item;
    }

    @Override
    protected NewsViewHolder createViewHolder(View view) {
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        loadImage(holder);
    }

    private void loadImage(NewsViewHolder holder) {
        NewsItem item = holder.getItem();
        if (item == null) {
            return;
        }

        Iterator<String> iterator = item.getImgUrls().iterator();
        String imgUrl =  iterator.hasNext()? iterator.next(): null;

        if (imgUrl != null && !imgUrl.isEmpty()) {
            String baseUrl = AppFacade.Companion.getInstance().getRequestFactory().getBaseUrl();
            imgUrl = HttpUtils.INSTANCE.getAbsoluteUrl(baseUrl, imgUrl);
        }

        boolean hasImage = imgUrl != null && !imgUrl.isEmpty();
        ViewUtils.INSTANCE.setVisible(holder.getImageView(), hasImage);

        if (hasImage) {
            GlideApp.with(holder.getImageView())
                    .load(imgUrl)
                    .placeholder(R.drawable.ic_photo_empty_240dp)
                    .error(R.drawable.ic_photo_empty_240dp)
                    .fallback(R.drawable.ic_photo_empty_240dp)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.getImageView());
        }
    }


    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        if (!CollectionUtils.INSTANCE.isEmpty(payloads)) {
            Object obj = payloads.get(0);
            // Если изменения не пустые и это пришла загруженная картинка
            if (obj instanceof Bitmap) {
                // устанавливаем картинку
                holder.getImageView().setImageBitmap((Bitmap) obj);
                holder.getImageView().setVisibility(View.VISIBLE);
            } else if (obj instanceof Drawable) {
                holder.getImageView().setImageDrawable((Drawable) obj);
                holder.getImageView().setVisibility(View.VISIBLE);
            }
        }
    }
}
