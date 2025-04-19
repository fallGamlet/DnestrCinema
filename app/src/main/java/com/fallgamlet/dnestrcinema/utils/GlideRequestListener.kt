package com.fallgamlet.dnestrcinema.utils

import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class GlideRequestListener<T> : RequestListener<T> {

    private var onFailedListener: OnFailedListener? = null
    private var onSuccessListener: OnSuccessListener<T>? = null
    private var onCompleteListener: Runnable? = null


    fun failure(listener: OnFailedListener): GlideRequestListener<T> {
        this.onFailedListener = listener
        return this
    }

    fun success(listener: OnSuccessListener<T>): GlideRequestListener<T> {
        this.onSuccessListener = listener
        return this
    }

    fun complete(listener: Runnable): GlideRequestListener<T> {
        this.onCompleteListener = listener
        return this
    }

    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<T>,
        isFirstResource: Boolean
    ): Boolean {
        var result = false
        if (onFailedListener != null) {
            result = onFailedListener!!.onFailed(e)
        }
        if (onCompleteListener != null) {
            onCompleteListener!!.run()
        }
        return result
    }

    override fun onResourceReady(
        resource: T & Any,
        model: Any,
        target: Target<T>?,
        dataSource: DataSource,
        isFirstResource: Boolean
    ): Boolean {
        var result = false
        if (onSuccessListener != null) {
            result = onSuccessListener!!.onSuccess(resource)
        }
        if (onCompleteListener != null) {
            onCompleteListener!!.run()
        }
        return result
    }


    interface OnFailedListener {
        fun onFailed(e: GlideException?): Boolean
    }

    interface OnSuccessListener<T> {
        fun onSuccess(data: T): Boolean
    }
}
