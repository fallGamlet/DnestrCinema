package com.fallgamlet.dnestrcinema;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.fallgamlet.dnestrcinema.network.NetworkImageTask;

public class ImageActivity extends AppCompatActivity {
    //region Static felds
    public static String ARG_IMG_URL = "img_url";
    public static String ARG_IMG = "img";
    //endregion

    //region Fields
    ImageView mImageView;
    String mImgURL;
    Bitmap mImg;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mImageView = (ImageView) findViewById(R.id.imageView);

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
                    getImageTask().requestImage(imgUrl, new NetworkImageTask.NetworkImageCallback() {
                        @Override
                        public void onImageLoaded(NetworkImageTask.UrlImage urlImg) {
                            if (urlImg.img != null && urlImg.url != null && urlImg.url.equalsIgnoreCase(imgUrl)) {
                                showImage(urlImg.img);
                            }
                        }
                    });
                }
            }
        } catch (Exception ignored) {
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


}
