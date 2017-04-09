package com.fallgamlet.dnestrcinema.mvp.presenters;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;

import com.fallgamlet.dnestrcinema.ImageActivity;
import com.fallgamlet.dnestrcinema.ImageRecyclerAdapter;
import com.fallgamlet.dnestrcinema.LogUtils;
import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.mvp.views.ICinemaDetailView;
import com.fallgamlet.dnestrcinema.network.HttpUtils;
import com.fallgamlet.dnestrcinema.network.KinoTir;
import com.fallgamlet.dnestrcinema.network.MovieItem;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import icepick.Icepick;
import icepick.State;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by fallgamlet on 09.04.17.
 */

public class CinemaDetailPresenter
        extends BasePresenter<ICinemaDetailView>
        implements ICinemaDetailPresenter <ICinemaDetailView>
{

    //region Fields
    @State
    MovieItem mMovie;
    OkHttpClient httpClient = new OkHttpClient();
//    @State
//    MovieItem mMovieDetail;
    private ImageRecyclerAdapter mImagesAdapter;
    private AlertDialog mDialog;
    //endregion

    public CinemaDetailPresenter(ICinemaDetailView view) {
        setView(view);
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

        for (MovieItem.Schedule schedule: movieItem.getSchedules()) {
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

        getView().setTitle(movieItem.getTitle());
        getView().setPubDate(movieItem.getPubDate());
        getView().setRooms(getRooms(movieItem));
        getView().setDirector(movieItem.getDirector());
        getView().setActors(movieItem.getActors());
        getView().setScenario(movieItem.getScenario());
        getView().setAgeLimit(movieItem.getAgeLimit());
        getView().setBudget(movieItem.getBudget());
        getView().setCountry(movieItem.getCountry());
        getView().setGenre(movieItem.getGenre());
        getView().setDuration(movieItem.getDuration());
        getView().setDescription(HttpUtils.fromHtml(movieItem.getDescription()));

        String imgUrl = HttpUtils.getAbsoluteUrl(KinoTir.BASE_URL, movieItem.getPosterUrl());
        if (imgUrl != null && getView().getPosterImageView().getDrawable() == null) {
            Picasso.with(getView().getContext()).load(imgUrl).into(getView().getPosterImageView());
        }

        if (getImagesAdapter().getItemCount() == 0) {
            loadImages(movieItem.getImgUrlSet());
        }
    }

    public void loadData(MovieItem movieItem) {
        if (movieItem == null || getView() == null || getView().getContext() == null) {
            return;
        }

//        showDetailData(movieItem);

        String urlStr = HttpUtils.getAbsoluteUrl(KinoTir.BASE_URL, movieItem.getLink());

        if (urlStr == null) {
            return;
        }

        Request request = new Request.Builder().url(urlStr).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.log("Http", "Fail to load cinema detail", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    LogUtils.log("Http", "Detail cinema info loaded ok");
                    String htmlStr = response.body().string();

                    MovieItem loadedMovie = new KinoTir.MovieDetailParser().parse(htmlStr);
                    mMovie.mergeLeft(loadedMovie);

                    Handler mainHandler = new Handler(getView().getContext().getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            showData(mMovie);
                        }
                    });


                } else {
                    LogUtils.log("Http", response.message());
                }
            }
        });
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
            imgUrl = HttpUtils.getAbsoluteUrl(KinoTir.BASE_URL, imgUrl);
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
                MovieItem.Schedule item = movieItem.getSchedules().get(i);
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
            imgURL = HttpUtils.getAbsoluteUrl(KinoTir.BASE_URL, imgURL);
            ImageActivity.showActivity(getView().getContext(), imgURL);
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
