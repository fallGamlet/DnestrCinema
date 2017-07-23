package com.fallgamlet.dnestrcinema.ui.movie.detail;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.mergers.MovieMerger;
import com.fallgamlet.dnestrcinema.mvp.models.Config;
import com.fallgamlet.dnestrcinema.mvp.models.MovieDetailItem;
import com.fallgamlet.dnestrcinema.mvp.models.MovieItem;
import com.fallgamlet.dnestrcinema.mvp.models.ScheduleItem;
import com.fallgamlet.dnestrcinema.mvp.presenters.BasePresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpMovieDetailPresenter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpMovieDetailView;
import com.fallgamlet.dnestrcinema.network.KinoTir;
import com.fallgamlet.dnestrcinema.ui.ImageActivity;
import com.fallgamlet.dnestrcinema.ui.movie.ImageRecyclerAdapter;
import com.fallgamlet.dnestrcinema.utils.HttpUtils;
import com.fallgamlet.dnestrcinema.utils.LogUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;

import icepick.Icepick;
import icepick.State;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by fallgamlet on 09.04.17.
 */

public class MvpMovieDetailPresenterImpl
        extends BasePresenter<MvpMovieDetailView>
        implements MvpMovieDetailPresenter
{

    //region Fields
    @State
    MovieItem mMovie;
    private ImageRecyclerAdapter mImagesAdapter;
    private AlertDialog mDialog;
    //endregion

    public MvpMovieDetailPresenterImpl() {

    }

    public MvpMovieDetailPresenterImpl(MvpMovieDetailView view) {
        bindView(view);
        if (view != null) {
            view.setImageAdapter(getImagesAdapter());
        }
    }

    @Override
    public void setData(MovieItem item) {
        mMovie = item;
        showData(item);
        loadData(item);
    }

    @Override
    public void onTrailerButtonPressed() {
        navigateToTrailer();
    }

    @Override
    public void onBuyTicketButtonPressed() {
        if (mMovie == null)
            return;

        String baseUrl = Config.getInstance().getRequestFactory().getBaseUrl();
        String url = HttpUtils.getAbsoluteUrl(baseUrl, mMovie.getBuyTicketLink());
        navigateToUrl(url);
    }

    private void navigateToUrl(String url) {
        if (url == null)
            return;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        getView().getContext().startActivity(intent);
    }

    @Override
    public void onRoomsPressed() {
        navigateToRoomView(mMovie);
    }

    @Override
    public void onPause(Bundle bundle) {
        if (bundle != null) {
            Icepick.saveInstanceState(this, bundle);
        }
    }

    @Override
    public void onResume(Bundle bundle) {
        if (bundle != null) {
            Icepick.restoreInstanceState(this, bundle);
        }
        setData(mMovie);
    }

    @NonNull
    private ArrayList<String> getRooms(MovieItem movieItem) {
        ArrayList<String> rooms = new ArrayList<>();

        if (movieItem == null || movieItem.getSchedules().isEmpty()) {
            return rooms;
        }

        StringBuilder strBuilder = new StringBuilder();

        for (ScheduleItem schedule: movieItem.getSchedules()) {
            strBuilder.append(schedule.room);
            if (schedule.value != null) {
                strBuilder.append(": ").append(schedule.value.trim());
            }
            rooms.add(strBuilder.toString());
            strBuilder.setLength(0);
        }

        return rooms;
    }

    private void showData(MovieItem movieItem) {
        if (getView() == null) {
            return;
        }

        if (movieItem == null) {
            movieItem = new MovieItem();
        }

        MovieDetailItem detail = movieItem.getDetail();

        getView().setTitle(movieItem.getTitle());
        getView().setPubDate(movieItem.getPubDate());
        getView().setRooms(getRooms(movieItem));
        getView().setDuration(movieItem.getDuration());

        getView().setDirector(detail.getDirector());
        getView().setActors(detail.getActors());
        getView().setScenario(detail.getScenario());
        getView().setAgeLimit(detail.getAgeLimit());
        getView().setBudget(detail.getBudget());
        getView().setCountry(detail.getCountry());
        getView().setGenre(detail.getGenre());
        getView().setDescription(HttpUtils.fromHtml(detail.getDescription()));

        getView().showBuyTicketButton(movieItem.getBuyTicketLink() != null && !movieItem.getBuyTicketLink().isEmpty());
        getView().showTrailerButton(!movieItem.getTrailerUrlSet().isEmpty());

        String baseUrl = Config.getInstance().getRequestFactory().getBaseUrl();
        String imgUrl = HttpUtils.getAbsoluteUrl(baseUrl, movieItem.getPosterUrl());
        if (imgUrl != null && getView().getPosterImageView().getDrawable() == null) {
            Picasso.with(getView().getContext()).load(imgUrl).into(getView().getPosterImageView());
        }

        if (getImagesAdapter().getItemCount() == 0) {
            loadImages(detail.getImgUrls());
        }
    }

    public void loadData(MovieItem movieItem) {
        if (movieItem == null || getView() == null || getView().getContext() == null) {
            return;
        }

        String urlStr = movieItem.getLink();

        if (urlStr == null) {
            return;
        }

        Config.getInstance()
                .getNetClient()
                .getDetailMovies(urlStr)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    new Consumer<MovieItem>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull MovieItem movieItem) throws Exception {
                            if (mMovie == null) {
                                mMovie = movieItem;
                            } else {
                                new MovieMerger().merge(mMovie, movieItem);
                            }

                            showData(mMovie);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                            LogUtils.log("DetailMovie", "loaf error", throwable);
                        }
                    }
                );
    }

    protected synchronized void loadImages(Collection<String> urlList) {
        if (urlList == null || urlList.isEmpty()) {
            getView().showImages(false);
            return;
        }

        for (String url: urlList) {
            addImage(url);
        }
    }

    private void addImage(String imgUrl) {
        if (imgUrl != null) {
            String baseUrl = Config.getInstance().getRequestFactory().getBaseUrl();
            imgUrl = HttpUtils.getAbsoluteUrl(baseUrl, imgUrl);
        }

        if (imgUrl != null) {
            LogUtils.log("LoadImage", "Image load by url: "+imgUrl);
            getImagesAdapter().addItem(imgUrl);
            getView().showImages(true);
        }
    }

    protected  void navigateToRoomView(MovieItem movieItem) {
        String[] rooms = null;
        if (movieItem != null && !movieItem.getSchedules().isEmpty()) {
            int count = movieItem.getSchedules().size();
            rooms = new String[count];
            for (int i=0; i<count; i++) {
                ScheduleItem item = movieItem.getSchedules().get(i);
                if (item.room != null) {
                    rooms[i] = item.room;
                }
            }
        }

        if (rooms != null && rooms.length > 0) {
            if (rooms.length == 1) {
                navigateToRoomView(rooms[0]);
            } else {
                showRoomSelectionDialog(rooms);
            }
        }
    }

    protected void showRoomSelectionDialog(final String[] items) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }

        mDialog = new AlertDialog.Builder(getView().getContext(), R.style.AppTheme_Dialog)
                .setTitle("Выберите зал")
                .setCancelable(true)
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mDialog != null) {
                            mDialog.dismiss();
                            mDialog = null;
                        }
                        navigateToRoomView(items[i]);
                    }
                })
                .create();
        mDialog.show();
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
            String baseUrl = Config.getInstance().getRequestFactory().getBaseUrl();
            imgURL = HttpUtils.getAbsoluteUrl(baseUrl, imgURL);
            ImageActivity.showActivity(getView().getContext(), imgURL);
        }
    }

    protected void navigateToTrailer() {
        if (getView() == null || getView().getContext() == null) {
            return;
        }

        if (mMovie == null || mMovie.getTrailerUrlSet().isEmpty()) {
            return;
        }

        final ArrayList<String> trailerUrls = new ArrayList<>(mMovie.getTrailerUrlSet());

        if (trailerUrls.size() == 1) {
            navigateToTrailer(trailerUrls.get(0));
            return;
        }

        String[] items = new String[trailerUrls.size()];
        for (int i=0; i<trailerUrls.size(); i++) {
            items[i] = "Трейлер "+(i+1);
        }

        mDialog = new AlertDialog.Builder(getView().getContext(), R.style.AppTheme_Dialog)
                .setTitle("Выберите")
                .setCancelable(true)
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mDialog != null) {
                            mDialog.dismiss();
                            mDialog = null;
                        }
                        navigateToTrailer(trailerUrls.get(i));
                    }
                })
                .create();
        mDialog.show();
    }

    protected void navigateToTrailer(String url) {
        if (url == null || url.isEmpty()) {
            return;
        }

        if (getView() == null || getView().getContext() == null) {
            return;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            getView().getContext().startActivity(intent);
        } catch (Exception e) {
            LogUtils.log("Intent", "Uri intent error", e);
        }
    }

    //region Images recycler adapter
    @NonNull
    private synchronized ImageRecyclerAdapter getImagesAdapter() {
        if (mImagesAdapter == null) {
            mImagesAdapter = new ImageRecyclerAdapter();
        }
        return mImagesAdapter;
    }
    //endregion
}
