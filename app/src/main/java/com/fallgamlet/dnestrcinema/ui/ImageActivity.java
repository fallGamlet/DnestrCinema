package com.fallgamlet.dnestrcinema.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.app.GlideApp;
import com.fallgamlet.dnestrcinema.mvp.models.Config;
import com.fallgamlet.dnestrcinema.utils.GlideRequestListener;
import com.fallgamlet.dnestrcinema.utils.HttpUtils;

public class ImageActivity extends AppCompatActivity {
    public static String ARG_IMG_URL = "img_url";
    public static String ARG_IMG = "img";

    private ImageView mImageView;
    private ContentLoadingProgressBar mProgressBar;
    private String mImgURL;
    private Bitmap mImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mImageView = findViewById(R.id.imageView);
        mProgressBar = findViewById(R.id.progressBar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mImgURL = bundle.getString(ARG_IMG_URL);
            mImg = bundle.getParcelable(ARG_IMG);
        }

        if (mImg != null) {
            showImage(mImg);
        } else {
            showImage(mImgURL);
        }
    }

    private void showProgressBar(boolean val) {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(val? View.VISIBLE: View.GONE);
        }
    }

    private void showImage(Bitmap image) {
        try {
            mImageView.setImageBitmap(image);
        } catch (Exception ignored) {
            showImageEmpty();
        }
    }

    private void showImageEmpty() {
        mImageView.setImageResource(R.drawable.ic_photo_empty_240dp);
    }

    private void showImage(String imgUrl) {
        String baseUrl = Config.getInstance().getRequestFactory().getBaseUrl();
        imgUrl = HttpUtils.getAbsoluteUrl(baseUrl, imgUrl);

        if (imgUrl == null) {
            showImageEmpty();
        } else {
            showProgressBar(true);
            GlideApp.with(mImageView)
                    .load(imgUrl)
                    .placeholder(R.drawable.ic_photo_empty_240dp)
                    .error(R.drawable.ic_photo_empty_240dp)
                    .fitCenter()
                    .addListener(new GlideRequestListener<Drawable>().complete(() -> showProgressBar(false)))
                    .into(mImageView);
        }
    }

    //region Start activity
    public static void showActivity(Context context, String imgUrl) {
        Bundle bundle = new Bundle();
        bundle.putString(ImageActivity.ARG_IMG_URL, imgUrl);

        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    //endregion
}
