package com.fallgamlet.dnestrcinema;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fallgamlet.dnestrcinema.network.DataSettings;
import com.fallgamlet.dnestrcinema.network.NetworkImageTask;
import com.fallgamlet.dnestrcinema.network.RssItem;

public class CinemaDetailActivity extends AppCompatActivity implements View.OnClickListener {

    //region Static fields
    public static String ARG_RSSITEM = "rss_item";
    //endregion

    //region Fields
    private RssItem mRssItem;

    private RssRecyclerAdapter.ViewHolder mRssHolder;
    private TextView mDescriptionView;
    Toolbar mToolbar;
    //endregion

    //region Getters and Setters
    public RssItem getRssItem() { return mRssItem; }
    public void setRssItem(RssItem item){ mRssItem = item; }


    //endregion

    //region Override methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            RssItem item = bundle.getParcelable(ARG_RSSITEM);
            setRssItem(item);
        }

        initViews();
        showData(getRssItem());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean res = false;
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                res = true;
                break;
            default:
                res = super.onOptionsItemSelected(item);
                break;
        }
        return res;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        if (view == null) { return; }

        if (mRssHolder != null && view == mRssHolder.getScheduleView()) {
            navigateToRoomView(getRssItem());
            return;
        }
    }
    //endregion

    //region Methods
    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (getRssItem() != null) {
                actionBar.setTitle(getRssItem().getTitle());
            }
        }


        mRssHolder = new RssRecyclerAdapter.ViewHolder(findViewById(R.id.itemRootView));
        mDescriptionView = (TextView) findViewById(R.id.descriptionView);

        mRssHolder.getScheduleView().setOnClickListener(this);
    }

    private void showData(RssItem rssItem) {
        if (mDescriptionView != null) {
            String desc = rssItem==null? null: rssItem.getDescription();
            mDescriptionView.setText(RssItem.fromHtml(desc));
        }

        if (mRssHolder != null) {
            mRssHolder.initData(rssItem);

            //region Load and set Image
            String imgUrl = rssItem == null? null: rssItem.getImgUrl();
            try {
                mRssHolder.getImageView().setImageResource(R.drawable.ic_local_movies_black_24dp);
                // если ссылка есть
                if (imgUrl != null) {
                    Bitmap img = NetworkImageTask.cachedImages.get(imgUrl);
                    if (img != null) {
                        mRssHolder.mImageView.setImageBitmap(img);
                    } else {
                        getImageTask().requestImage(imgUrl, new NetworkImageTask.NetworkImageCallback() {
                            @Override
                            public void onImageLoaded(NetworkImageTask.UrlImage urlImg) {
                                if (mRssHolder != null && urlImg.img != null && urlImg.url != null && urlImg.url.equalsIgnoreCase(mRssHolder.getItem().getImgUrl())) {
                                    mRssHolder.getImageView().setImageBitmap(urlImg.img);
                                }
                            }
                        });
                    }
                }
            } catch (Exception ignored) {
                mRssHolder.getImageView().setImageResource(R.drawable.ic_local_movies_black_24dp);
            }
            //endregion
        }


    }

    protected  void navigateToRoomView(String roomName) {
        if (roomName == null) {
            return;
        }

        String imgURL = null;
        if (RssItem.ROOM_BLUE.equalsIgnoreCase(roomName)) {
            imgURL = DataSettings.PATH_IMG_ROOM_BLUE;
        } else if (RssItem.ROOM_BORDO.equalsIgnoreCase(roomName)) {
            imgURL = DataSettings.PATH_IMG_ROOM_BORDO;
        } else if (RssItem.ROOM_DVD.equalsIgnoreCase(roomName)) {
            imgURL = DataSettings.PATH_IMG_ROOM_DVD;
        }

        if (imgURL != null) {
            imgURL = DataSettings.BASE_URL + imgURL;
            ImageActivity.showActivity(this, imgURL);
        }

    }

    AlertDialog dialog;
    protected  void navigateToRoomView(RssItem rssItem) {
        String[] rooms = null;
        if (rssItem != null && !rssItem.getSchedules().isEmpty()) {
            int count = rssItem.getSchedules().size();
            rooms = new String[count];
            for (int i=0; i<count; i++) {
                RssItem.Schedule item = rssItem.getSchedules().get(i);
                if (item.room != null) {
                    rooms[i] = item.room;
                }
            }
        }

        if (rooms != null) {
            if (rooms.length == 1) {
                navigateToRoomView(rooms[0]);
            } else {
                final String[] items = rooms;
                dialog = new AlertDialog.Builder(this, R.style.AppTheme_Dialog)
                        .setTitle("Выберите зал")
                        .setCancelable(true)
                        .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (dialog != null) {
                                    dialog.dismiss();
                                    dialog = null;
                                }
                                navigateToRoomView(items[i]);
                            }
                        })
                        .create();
                dialog.show();
            }
        }
    }
    //endregion

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
