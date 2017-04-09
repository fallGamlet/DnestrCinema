package com.fallgamlet.dnestrcinema;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fallgamlet.dnestrcinema.mvp.presenters.CinemaDetailPresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.ICinemaDetailPresenter;
import com.fallgamlet.dnestrcinema.mvp.views.ICinemaDetailView;
import com.fallgamlet.dnestrcinema.network.HttpUtils;
import com.fallgamlet.dnestrcinema.network.KinoTir;
import com.fallgamlet.dnestrcinema.network.MovieItem;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CinemaDetailActivity
        extends BaseActivity
        implements View.OnClickListener, ICinemaDetailView
{
    //region Static fields
    public static String ARG_MOVIE = "movie_item";
    public static String ARG_MOVIE_DETAIL = "movie_detail";
    public static String YOUTUBE = "youtube.com";
    //endregion

    //region Fields
    private MovieRecyclerAdapter.ViewHolder mMovieHolder;
    private FieldHolder directorHolder;
    private FieldHolder scenarioHolder;
    private FieldHolder actorsHolder;
    private FieldHolder ageLimitHolder;
    private FieldHolder durationHolder;
    private FieldHolder budgetHolder;
    private FieldHolder genreHolder;
    private FieldHolder countryHolder;

    @BindView(R.id.posterImageView) ImageView mPosterImageView;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.shortInfoContainer) View mShortInfoContainer;
    @BindView(R.id.directorView) View mDirectorView;
    @BindView(R.id.scenarioView) View mScenarioView;
    @BindView(R.id.actorsView) View mActorsView;
    @BindView(R.id.ageLimitView) View mAgeLimitView;
    @BindView(R.id.durationView) View mDurationView;
    @BindView(R.id.budgetView) View mBudgetView;
    @BindView(R.id.genreView) View mgenreView;
    @BindView(R.id.countryView) View mCountryView;
    @BindView(R.id.descriptionView) TextView mDescriptionView;
    @BindView(R.id.imageList) RecyclerView mImageListView;
    @BindView(R.id.trailerBtn) View mTrailerBtn;

    ICinemaDetailPresenter<ICinemaDetailView> mPresenter;
    //endregion

    //region Override methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema_detail);

        ButterKnife.bind(this);
        initViews();

        Bundle bundle = getIntent().getExtras();
        MovieItem movieItem = null;
        if (bundle != null) {
            movieItem = bundle.getParcelable(ARG_MOVIE);
        }

        mPresenter = new CinemaDetailPresenter(this);
        mPresenter.setData(movieItem);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.onPause(outState);
//        outState.putParcelable(ARG_MOVIE, mMovie);
//        outState.putParcelable(ARG_MOVIE_DETAIL, mMovieDetail);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPresenter.onResume(savedInstanceState);
//        if (savedInstanceState != null) {
//            mMovie = savedInstanceState.getParcelable(ARG_MOVIE);
//            mMovieDetail = savedInstanceState.getParcelable(ARG_MOVIE_DETAIL);
//        }
//        showData();
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
    public Context getContext() {
        return this;
    }

    @Override
    public void onClick(View view) {
        if (view == null) { return; }

        if (mMovieHolder != null && view == mMovieHolder.getScheduleView()) {
//            navigateToRoomView(getMovie());
            mPresenter.onRoomsPressed();
            return;
        }

        if (view == mTrailerBtn) {
//            navigateToTrailer();
            mPresenter.onTrailerButtonPressed();
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
            actionBar.setTitle(null);
        }

        mMovieHolder = new MovieRecyclerAdapter.ViewHolder(mShortInfoContainer);
        mMovieHolder.getScheduleView().setOnClickListener(this);

        if (mTrailerBtn != null) {
            mTrailerBtn.setOnClickListener(this);
        }

        mImageListView = (RecyclerView) findViewById(R.id.imageList);
        if (mImageListView != null) {
//            mImageListView.setAdapter(getImagesAdapter());
            mImageListView.setHasFixedSize(false);
            mImageListView.setItemAnimator(new DefaultItemAnimator());
            mImageListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            mImageListView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    if (parent.getChildAdapterPosition(view) != 0) {
                        outRect.left = 16;
                    }
                }
            });
        }

        directorHolder = new FieldHolder(mDirectorView);
        scenarioHolder = new FieldHolder(mScenarioView);
        actorsHolder = new FieldHolder(mActorsView);
        ageLimitHolder = new FieldHolder(mAgeLimitView);
        durationHolder = new FieldHolder(mDurationView);
        budgetHolder = new FieldHolder(mBudgetView);
        genreHolder = new FieldHolder(mgenreView);
        countryHolder = new FieldHolder(mCountryView);

//        directorHolder.setVisible(false);
//        scenarioHolder.setVisible(false);
//        actorsHolder.setVisible(false);
//        ageLimitHolder.setVisible(false);
//        durationHolder.setVisible(false);
//        budgetHolder.setVisible(false);
//        genreHolder.setVisible(false);
//        countryHolder.setVisible(false);
    }
    //endregion

    //region ICinemaDetailView implementation
    @Override
    public void setImageAdapter(RecyclerView.Adapter adapter) {
        mImageListView.setAdapter(adapter);
    }

    @Override
    public ImageView getPosterImageView() {
        return mPosterImageView;
    }

    @Override
    public void setPosterImage(Drawable drawable) {
        mPosterImageView.setImageDrawable(drawable);
    }

    @Override
    public void setPubDate(Date date) {
        mMovieHolder.setPubDate(date);
    }

    @Override
    public void setRooms(Collection<? extends CharSequence> rooms) {
        String roomsStr = TextUtils.join("\n", rooms);
        mMovieHolder.setSchedule(roomsStr);
    }

    @Override
    public void setTitle(CharSequence title) {
//        super.setTitle(title);
        mMovieHolder.setTitle(title);
    }

    @Override
    public void setDuration(CharSequence v) {
        String val = v==null? null: v.toString();
        durationHolder.setDataAndVisible(getString(R.string.label_duration), val);
    }

    @Override
    public void setGenre(CharSequence v) {
        String val = v==null? null: v.toString();
        genreHolder.setDataAndVisible(getString(R.string.label_genre), val);
    }

    @Override
    public void setAgeLimit(CharSequence v) {
        String val = v==null? null: v.toString();
        ageLimitHolder.setDataAndVisible(getString(R.string.label_agelimit), val);
    }

    @Override
    public void setCountry(CharSequence v) {
        String val = v==null? null: v.toString();
        countryHolder.setDataAndVisible(getString(R.string.label_country), val);
    }

    @Override
    public void setDirector(CharSequence v) {
        String val = v==null? null: v.toString();
        directorHolder.setDataAndVisible(getString(R.string.label_director), val);
    }

    @Override
    public void setScenario(CharSequence v) {
        String val = v==null? null: v.toString();
        scenarioHolder.setDataAndVisible(getString(R.string.label_scenario), val);
    }

    @Override
    public void setActors(CharSequence v) {
        String val = v==null? null: v.toString();
        actorsHolder.setDataAndVisible(getString(R.string.label_actors), val);
    }

    @Override
    public void setBudget(CharSequence v) {
        String val = v==null? null: v.toString();
        budgetHolder.setDataAndVisible(getString(R.string.label_budget), val);
    }

    @Override
    public void setDescription(CharSequence v) {
        if (mDescriptionView != null) {
            mDescriptionView.setText(v);
            mDescriptionView.setVisibility(v==null || v.length()==0? View.GONE: View.VISIBLE);
        }
    }

    //region Set visible
    @Override
    public void showImages(boolean v) {
        mImageListView.setVisibility(v? View.VISIBLE: View.GONE);
    }

    @Override
    public void showTrailerButton(boolean v) {
        if (mTrailerBtn != null) {
            mTrailerBtn.setVisibility(v? View.VISIBLE: View.GONE);
        }
    }

    @Override
    public void showDuration(boolean v) {
        durationHolder.setVisible(v);
    }

    @Override
    public void showGenre(boolean v) {
        genreHolder.setVisible(v);
    }

    @Override
    public void showAgeLimit(boolean v) {
        ageLimitHolder.setVisible(v);
    }

    @Override
    public void showCountry(boolean v) {
        countryHolder.setVisible(v);
    }

    @Override
    public void showDirector(boolean v) {
        directorHolder.setVisible(v);
    }

    @Override
    public void showScenario(boolean v) {
        scenarioHolder.setVisible(v);
    }

    @Override
    public void showActors(boolean v) {
        actorsHolder.setVisible(v);
    }

    @Override
    public void showBudget(boolean v) {
        budgetHolder.setVisible(v);
    }

    @Override
    public void showDescription(boolean v) {
        mDescriptionView.setVisibility(v? View.VISIBLE: View.GONE);
    }
    //endregion

    //endregion
}
