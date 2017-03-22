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
import com.fallgamlet.dnestrcinema.network.HttpUtils;
import com.fallgamlet.dnestrcinema.network.KinoTir;
import com.fallgamlet.dnestrcinema.network.Network;
import com.fallgamlet.dnestrcinema.network.NetworkImageTask;
import com.fallgamlet.dnestrcinema.network.MovieItem;

import java.util.ArrayList;
import java.util.List;

public class CinemaDetailActivity extends AppCompatActivity implements View.OnClickListener {

    //region Static fields
    public static String ARG_MOVIE = "movie_item";
    public static String ARG_MOVIE_DETAIL = "movie_detail";
    public static String YOUTUBE = "youtube.com";
    //endregion

    //region Fields
    private List<String> mTrailerUrls;
    private MovieItem mMovie;
    private MovieItem mMovieDetail;
    private MovieRecyclerAdapter.ViewHolder mMovieHolder;
    private FieldHolder directorHolder;
    private FieldHolder scenarioHolder;
    private FieldHolder actorsHolder;
    private FieldHolder ageLimitHolder;
    private FieldHolder durationHolder;
    private FieldHolder budgetHolder;
    private FieldHolder genreHolder;
    private FieldHolder countryHolder;
    private TextView mDescriptionView;
    private RecyclerView mImageListView;
    View mTrailerBtn;
    Toolbar mToolbar;
    //endregion

    //region Getters and Setters
    public MovieItem getMovie() { return mMovie; }
    public void setMovie(MovieItem item){ mMovie = item; }
    //endregion

    //region Override methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mMovie = bundle.getParcelable(ARG_MOVIE);
            mMovieDetail = bundle.getParcelable(ARG_MOVIE_DETAIL);
        }

        initViews();
        showData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_MOVIE, mMovie);
        outState.putParcelable(ARG_MOVIE_DETAIL, mMovieDetail);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mMovie = savedInstanceState.getParcelable(ARG_MOVIE);
            mMovieDetail = savedInstanceState.getParcelable(ARG_MOVIE_DETAIL);
        }
        showData();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

        if (mMovieHolder != null && view == mMovieHolder.getScheduleView()) {
            navigateToRoomView(getMovie());
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
            if (getMovie() != null) {
                actionBar.setTitle(getMovie().getTitle());
            }
        }

        mMovieHolder = new MovieRecyclerAdapter.ViewHolder(findViewById(R.id.itemRootView));
        mMovieHolder.getScheduleView().setOnClickListener(this);

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

        directorHolder = new FieldHolder();
        scenarioHolder = new FieldHolder();
        actorsHolder = new FieldHolder();
        ageLimitHolder = new FieldHolder();
        durationHolder = new FieldHolder();
        budgetHolder = new FieldHolder();
        genreHolder = new FieldHolder();
        countryHolder = new FieldHolder();

        directorHolder.initViews(findViewById(R.id.directorView));
        scenarioHolder.initViews(findViewById(R.id.scenarioView));
        actorsHolder.initViews(findViewById(R.id.actorsView));
        ageLimitHolder.initViews(findViewById(R.id.ageLimitView));
        durationHolder.initViews(findViewById(R.id.durationView));
        budgetHolder.initViews(findViewById(R.id.budgetView));
        genreHolder.initViews(findViewById(R.id.genreView));
        countryHolder.initViews(findViewById(R.id.countryView));

        directorHolder.setVisible(false);
        scenarioHolder.setVisible(false);
        actorsHolder.setVisible(false);
        ageLimitHolder.setVisible(false);
        durationHolder.setVisible(false);
        budgetHolder.setVisible(false);
        genreHolder.setVisible(false);
        countryHolder.setVisible(false);
    }

    private void showData() {
        if (mMovie == null) { mMovie = mMovieDetail; }
        MovieItem movieItem = mMovie;

        if (mDescriptionView != null) {
            String desc = movieItem ==null? null: movieItem.getDescription();
            showDescription(HttpUtils.fromHtml(desc));
        }

        if (mMovieHolder != null) {
            mMovieHolder.initData(movieItem);

            //region Load and set Image
            String imgUrl = movieItem == null? null: movieItem.getImgUrl();
            try {
                mMovieHolder.getImageView().setImageResource(R.drawable.ic_local_movies_black_24dp);
                // если ссылка есть
                if (imgUrl != null) {
                    Bitmap img = NetworkImageTask.cachedImages.get(imgUrl);
                    if (img != null) {
                        mMovieHolder.mImageView.setImageBitmap(img);
                    } else {
                        getImageTask().requestImage(imgUrl, new NetworkImageTask.NetworkImageCallback() {
                            @Override
                            public void onImageLoaded(NetworkImageTask.UrlImage urlImg) {
                                if (mMovieHolder != null && urlImg.img != null && urlImg.url != null && urlImg.url.equalsIgnoreCase(mMovieHolder.getItem().getImgUrl())) {
                                    mMovieHolder.getImageView().setImageBitmap(urlImg.img);
                                }
                            }
                        });
                    }
                }
            } catch (Exception ignored) {
                mMovieHolder.getImageView().setImageResource(R.drawable.ic_local_movies_black_24dp);
            }
            //endregion
        }

        setImagesViewVisible(false);

        if (mMovieDetail == null){
            loadInfo(movieItem);
        } else {
            showDetailData(mMovieDetail);
        }
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

    protected void showDescription(CharSequence text) {
        if (mDescriptionView != null) {
            mDescriptionView.setText(text);
            mDescriptionView.setVisibility(text==null || text.length()==0? View.GONE: View.VISIBLE);
        }
    }

    protected void loadInfo(MovieItem movieItem) {
        if (movieItem == null) {
            return;
        }

        showDetailData(movieItem);

        Network.RequestData request = new Network.RequestData();
        request.url = HttpUtils.getAbsoluteUrl(KinoTir.BASE_URL, movieItem.getLink());
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
                    String htmlStr = result.data;
                    mMovieDetail = new KinoTir.MovieDetailParser().parse(htmlStr);
                    showDetailData(mMovieDetail);
                } catch (Exception e) {
                    Log.d("LoadPage", "Error: "+e.toString());
                }
            }
        });
    }

    protected void showDetailData(MovieItem movieItem) {
        if (movieItem != null) {
            ArrayList<String> urlList = new ArrayList<>(movieItem.getImgUrlSet());
            ArrayList<String> trailerUrlList = new ArrayList<>(movieItem.getTrailerUrlSet());

            loadImages(urlList);
            showTrailers(trailerUrlList);
            showDescription(HttpUtils.fromHtml(movieItem.getDescription()));

            if (durationHolder != null) {
                durationHolder.setDataAndVisible(getString(R.string.label_duration), movieItem.getDuration());
            }

            if (ageLimitHolder != null) {
                ageLimitHolder.setDataAndVisible(getString(R.string.label_agelimit), movieItem.getAgeLimit());
            }

            if (genreHolder != null) {
                genreHolder.setDataAndVisible(getString(R.string.label_genre), movieItem.getGenre());
            }

            if (countryHolder != null) {
                countryHolder.setDataAndVisible(getString(R.string.label_country), movieItem.getCountry());
            }

            if (directorHolder != null) {
                directorHolder.setDataAndVisible(getString(R.string.label_director), movieItem.getDirector());
            }

            if (scenarioHolder != null) {
                scenarioHolder.setDataAndVisible(getString(R.string.label_scenario), movieItem.getScenario());
            }

            if (actorsHolder != null) {
                actorsHolder.setDataAndVisible(getString(R.string.label_actors), movieItem.getActors());
            }
            if (budgetHolder != null) {
                budgetHolder.setDataAndVisible(getString(R.string.label_budget), movieItem.getBudget());
            }

        }
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
            imgURL = KinoTir.PATH_IMG_ROOM_BLUE;
        } else if (MovieItem.ROOM_BORDO.equalsIgnoreCase(roomName)) {
            imgURL = KinoTir.PATH_IMG_ROOM_BORDO;
        } else if (MovieItem.ROOM_DVD.equalsIgnoreCase(roomName)) {
            imgURL = KinoTir.PATH_IMG_ROOM_DVD;
        }

        if (imgURL != null) {
            imgURL = HttpUtils.getAbsoluteUrl(KinoTir.BASE_URL, imgURL);
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
