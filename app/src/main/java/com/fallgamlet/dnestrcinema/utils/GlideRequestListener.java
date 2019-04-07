package com.fallgamlet.dnestrcinema.utils;

import android.support.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class GlideRequestListener<T> implements RequestListener<T> {

    private OnFailedListener onFailedListener;
    private OnSuccessListener<T> onSuccessListener;
    private Runnable onCompleteListener;


    public GlideRequestListener<T> failure(OnFailedListener listener) {
        this.onFailedListener = listener;
        return this;
    }

    public GlideRequestListener<T> success(OnSuccessListener<T> listener) {
        this.onSuccessListener = listener;
        return this;
    }

    public GlideRequestListener<T> complete(Runnable listener) {
        this.onCompleteListener = listener;
        return this;
    }


    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<T> target, boolean isFirstResource) {
        boolean result = false;
        if (onFailedListener != null) {
            result = onFailedListener.onFailed(e);
        }
        if (onCompleteListener != null) {
            onCompleteListener.run();
        }
        return result;
    }

    @Override
    public boolean onResourceReady(T resource, Object model, Target<T> target, DataSource dataSource, boolean isFirstResource) {
        boolean result = false;
        if (onSuccessListener != null) {
            result = onSuccessListener.onSuccess(resource);
        }
        if (onCompleteListener != null) {
            onCompleteListener.run();
        }
        return result;
    }


    public interface OnFailedListener {
        boolean onFailed(@Nullable GlideException e);
    }

    public interface OnSuccessListener<T> {
        boolean onSuccess(T data);
    }
}
