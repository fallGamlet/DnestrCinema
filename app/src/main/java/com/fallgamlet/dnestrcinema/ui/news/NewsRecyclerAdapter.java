package com.fallgamlet.dnestrcinema.ui.news;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.mvp.models.Config;
import com.fallgamlet.dnestrcinema.mvp.models.NewsItem;
import com.fallgamlet.dnestrcinema.ui.adapters.BaseRecyclerAdapter;
import com.fallgamlet.dnestrcinema.ui.holders.NewsViewHolder;
import com.fallgamlet.dnestrcinema.utils.CollectionUtils;
import com.fallgamlet.dnestrcinema.utils.HttpUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by fallgamlet on 16.07.17.
 */

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
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        loadImage(holder);
    }

    private void loadImage(NewsViewHolder holder) {
        NewsItem item = holder.getItem();

        if (item == null) {
            return;
        }

        Iterator<String> iterator = item.getImgUrls().iterator();
        String imgUrl = iterator.hasNext()? iterator.next(): null;

        if (imgUrl != null) {
            String baseUrl = Config.getInstance().getRequestFactory().getBaseUrl();
            imgUrl = HttpUtils.getAbsoluteUrl(baseUrl, imgUrl);
        }

        if (imgUrl != null) {
            Picasso.with(holder.getImageView().getContext()).load(imgUrl).into(holder.getImageView());
        }
    }


    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        if (!CollectionUtils.isEmpty(payloads)) {
            Object obj = payloads.get(0);
            // Если изменения не пустые и это пришла загруженная картинка
            if (obj != null && obj instanceof Bitmap) {
                // устанавливаем картинку
                holder.getImageView().setImageBitmap((Bitmap) obj);
                holder.getImageView().setVisibility(View.VISIBLE);
            }
        }
    }
}
