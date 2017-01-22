package com.fallgamlet.dnestrcinema;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.fallgamlet.dnestrcinema.network.DataSettings;
import com.fallgamlet.dnestrcinema.network.NetworkImageTask;

public class ImageActivity extends AppCompatActivity {
    //region Static felds
    public static String ARG_IMG_URL = "img_url";
    public static String ARG_IMG = "img";
    //endregion

    //region Fields
    ImageView mImageView;
    ContentLoadingProgressBar mProgressBar;
    String mImgURL;
    Bitmap mImg;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mProgressBar = (ContentLoadingProgressBar) findViewById(R.id.progressBar);

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

    private void showImage(final String imgUrl) {
        try {
            // если ссылка есть
            if (imgUrl != null) {
                Bitmap img = NetworkImageTask.cachedImages.get(imgUrl);
                if (img != null) {
                    mImageView.setImageBitmap(img);
                } else {
                    showProgressBar(true);
                    getImageTask().requestImage(imgUrl, new NetworkImageTask.NetworkImageCallback() {
                        @Override
                        public void onImageLoaded(NetworkImageTask.UrlImage urlImg) {
                            showProgressBar(false);
                            if (urlImg.img != null && urlImg.url != null && urlImg.url.equalsIgnoreCase(imgUrl)) {
                                showImage(urlImg.img);
                            } else {
                                showImageEmpty();
                            }
                        }
                    });
                }
            }
        } catch (Exception ignored) {
            showProgressBar(false);
            showImageEmpty();
        }
    }

    //region Imagetask singleton
    private NetworkImageTask imageTask;
    private NetworkImageTask getImageTask() {
        if (imageTask == null) {
            imageTask = new NetworkImageTask();
        }
        return imageTask;
    }
    //endregion

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
