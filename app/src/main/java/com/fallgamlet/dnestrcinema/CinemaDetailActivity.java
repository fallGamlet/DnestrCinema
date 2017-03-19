package com.fallgamlet.dnestrcinema;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fallgamlet.dnestrcinema.network.DataSettings;
import com.fallgamlet.dnestrcinema.network.Network;
import com.fallgamlet.dnestrcinema.network.NetworkImageTask;
import com.fallgamlet.dnestrcinema.network.MovieItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CinemaDetailActivity extends AppCompatActivity implements View.OnClickListener {

    //region Static fields
    public static String ARG_RSSITEM = "rss_item";
    public static String YOUTUBE = "youtube.com";
    //endregion

    //region Fields
    private List<String> mTrailerUrls;
    private MovieItem mMovieItem;
    private RssRecyclerAdapter.ViewHolder mRssHolder;
    private TextView mDescriptionView;
    private RecyclerView mImageListView;
    View mTrailerBtn;
    Toolbar mToolbar;
    //endregion

    //region Getters and Setters
    public MovieItem getRssItem() { return mMovieItem; }
    public void setRssItem(MovieItem item){ mMovieItem = item; }


    //endregion

    //region Override methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            MovieItem item = bundle.getParcelable(ARG_RSSITEM);
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

        if (view == mTrailerBtn) {
            navigateToTrailer();
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
        mRssHolder.getScheduleView().setOnClickListener(this);

        mDescriptionView = (TextView) findViewById(R.id.descriptionView);

        mTrailerBtn = findViewById(R.id.trailerBtn);
        if (mTrailerBtn != null) {
            mTrailerBtn.setOnClickListener(this);
        }

        mImageListView = (RecyclerView) findViewById(R.id.imageList);
        if (mImageListView != null) {
            mImageListView.setAdapter(getImagesAdapter());
            mImageListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            mImageListView.setItemAnimator(new DefaultItemAnimator());
            mImageListView.setHasFixedSize(false);
        }
    }

    private void showData(MovieItem movieItem) {
        if (mDescriptionView != null) {
            String desc = movieItem ==null? null: movieItem.getDescription();
            mDescriptionView.setText(MovieItem.fromHtml(desc));
        }

        if (mRssHolder != null) {
            mRssHolder.initData(movieItem);

            //region Load and set Image
            String imgUrl = movieItem == null? null: movieItem.getImgUrl();
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

        setImagesViewVisible(false);
        loadInfo(movieItem);
    }

    protected void setImagesViewVisible(boolean v) {
        if (mImageListView != null) {
            mImageListView.setVisibility(v? View.VISIBLE: View.GONE);
        }
    }

    protected void setTrailerViewVisible(boolean v) {
        if (mTrailerBtn != null) {
            mTrailerBtn.setVisibility(v? View.VISIBLE: View.GONE);
        }
    }

    protected void loadInfo(MovieItem movieItem) {
        if (movieItem == null) {
            return;
        }

        Set<String> imgUrlSet = movieItem.getImgUrlSet();
        Set<String> moveUrlSet = movieItem.getMoveUrlSet();

        if (!imgUrlSet.isEmpty()) {
            return;
        }

        Network.RequestData request = new Network.RequestData();
        request.url = movieItem.getLink();
        Network.requestDataAsync(request, new Network.ResponseHandle() {
            @Override
            public void finished(Exception exception, Network.StrResult result) {
                if (exception != null) {
                    Log.d("LoadData", "Error: "+exception.toString());
                    return;
                }

                if (result == null) {
                    Log.d("LoadData", "Null data");
                    return;
                }

                if (result.error != null) {
                    Log.d("LoadData", "Server error: "+result.error);
                    return;
                }

                if (result.data == null) {
                    Log.d("LoadData", "Empty data");
                    return;
                }

                try {
                    ArrayList<String> urlList = new ArrayList<String>();
                    String htmlStr = result.data;
                    Document doc = Jsoup.parse(htmlStr);
                    Elements elements = doc == null? null : doc.select("a[rel='photos']");
                    if (elements != null && !elements.isEmpty()) {
                        for (int i = 0; i < elements.size(); i++) {
                            Element item = elements.get(i);
                            String imgUrl = item.attr("href");
                            if (imgUrl != null && !imgUrl.isEmpty()) {
                                urlList.add(DataSettings.BASE_URL + imgUrl);
                            }
                        }
                    }

                    ArrayList<String> trailerUrlList = new ArrayList<String>();
                    elements = doc == null? null: doc.select(".trailer a");
                    if (elements != null) {
                        for (int i = 0; i < elements.size(); i++) {
                            Element item = elements.get(i);
                            String imgUrl = item.attr("href");
                            if (imgUrl != null && !imgUrl.isEmpty() && imgUrl.contains(YOUTUBE)) {
                                trailerUrlList.add(imgUrl);
                            }
                        }
                    }

                    loadImages(urlList);
                    showTrailers(trailerUrlList);
                } catch (Exception e) {
                    Log.d("LoadPage", "Error: "+e.toString());
                }
            }
        });
    }

    protected void loadImages(List<String> urlList) {
        if (urlList != null) {
            for (String url: urlList) {
                addImage(url);
            }
        }
    }

    private void addImage(final String imgUrl) {
        try {
            // если ссылка есть
            if (imgUrl != null) {
                Bitmap img = NetworkImageTask.cachedImages.get(imgUrl);
                if (img != null) {
                    addImage(img);
                } else {
                    final NetworkImageTask imageTask = new NetworkImageTask();
                    imageTask.requestImage(imgUrl, new NetworkImageTask.NetworkImageCallback() {
                        @Override
                        public void onImageLoaded(NetworkImageTask.UrlImage urlImg) {
                            if (urlImg.img != null && urlImg.url != null && urlImg.url.equalsIgnoreCase(imgUrl)) {
                                addImage(urlImg.img);
                            }
                        }
                    });
                }
            }
        } catch (Exception ignored) {}
    }

    private void addImage(Bitmap image) {
        if (image == null) {
            return;
        }
        try {
            Drawable drawable = new BitmapDrawable(null, image);
            getImagesAdapter().addItem(drawable);
            setImagesViewVisible(true);
        } catch (Exception e) {

        }
    }

    protected void showTrailers(List<String> urlList) {
        mTrailerUrls = urlList;
        setTrailerViewVisible(urlList != null && !urlList.isEmpty());
    }

    protected void navigateToTrailer() {
        if (mTrailerUrls == null || mTrailerUrls.isEmpty()) {
            return;
        }

        if (mTrailerUrls.size() == 1) {
            navigateToTrailer(mTrailerUrls.get(0));
            return;
        }

        String[] items = new String[mTrailerUrls.size()];
        for (int i=0; i<mTrailerUrls.size(); i++) {
            items[i] = "Трейлер "+(i+1);
        }

        dialog = new AlertDialog.Builder(this, R.style.AppTheme_Dialog)
                .setTitle("Выберите")
                .setCancelable(true)
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (dialog != null) {
                            dialog.dismiss();
                            dialog = null;
                        }
                        navigateToTrailer(mTrailerUrls.get(i));
                    }
                })
                .create();
        dialog.show();
    }

    protected void navigateToTrailer(String url) {
        if (url == null || url.isEmpty()) {
            return;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Log.e("Intent url", "Error: "+e.toString());
        }
    }

    protected  void navigateToRoomView(String roomName) {
        if (roomName == null) {
            return;
        }

        String imgURL = null;
        if (MovieItem.ROOM_BLUE.equalsIgnoreCase(roomName)) {
            imgURL = DataSettings.PATH_IMG_ROOM_BLUE;
        } else if (MovieItem.ROOM_BORDO.equalsIgnoreCase(roomName)) {
            imgURL = DataSettings.PATH_IMG_ROOM_BORDO;
        } else if (MovieItem.ROOM_DVD.equalsIgnoreCase(roomName)) {
            imgURL = DataSettings.PATH_IMG_ROOM_DVD;
        }

        if (imgURL != null) {
            imgURL = DataSettings.BASE_URL + imgURL;
            ImageActivity.showActivity(this, imgURL);
        }
    }

    AlertDialog dialog;
    protected  void navigateToRoomView(MovieItem movieItem) {
        String[] rooms = null;
        if (movieItem != null && !movieItem.getSchedules().isEmpty()) {
            int count = movieItem.getSchedules().size();
            rooms = new String[count];
            for (int i=0; i<count; i++) {
                MovieItem.Schedule item = movieItem.getSchedules().get(i);
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

    //region Images recycler adapter
    private ImageRecyclerAdapter mImagesAdapter;
    @NonNull
    private ImageRecyclerAdapter getImagesAdapter() {
        if (mImagesAdapter == null) {
            mImagesAdapter = new ImageRecyclerAdapter();
        }
        return mImagesAdapter;
    }
    //endregion

}
