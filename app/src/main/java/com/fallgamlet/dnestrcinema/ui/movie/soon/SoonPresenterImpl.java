package com.fallgamlet.dnestrcinema.ui.movie.soon;

import android.os.Bundle;

import com.fallgamlet.dnestrcinema.mvp.models.Config;
import com.fallgamlet.dnestrcinema.mvp.models.MovieItem;
import com.fallgamlet.dnestrcinema.mvp.presenters.BasePresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpSoonPresenter;
import com.fallgamlet.dnestrcinema.mvp.routers.NavigationRouter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpSoonView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by fallgamlet on 03.07.17.
 */

public class SoonPresenterImpl
        extends BasePresenter <MvpSoonView>
        implements MvpSoonPresenter
{


    private List<MovieItem> movies;

    @Override
    public void bindView(MvpSoonView view) {
        super.bindView(view);

        if (isDataEmpty()) {
            loadData();
        } else {
            showData();
        }
    }

    private boolean isDataEmpty() {
        return movies == null || movies.isEmpty();
    }

    @Override
    public void loadData() {

        if(isViewBinded()) {
            getView().showLoading();
        }

        Config.getInstance()
                .getNetClient()
                .getSoonMovies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        new Consumer<List<MovieItem>>() {
                            @Override
                            public void accept(@NonNull List<MovieItem> movieItems) throws Exception {
                                updateData(movieItems);
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                updateData(getEmptyList());
                            }
                        }
                );
    }

    private void updateData(List<MovieItem> items) {
        movies = items;
        showData();
    }

    private void showData() {
        if (movies == null) {
            movies = getEmptyList();
        }

        if (isViewBinded()) {
            boolean empty = movies.isEmpty();

            getView().hideLoading();
            getView().showData(movies);
            getView().setContentVisible(!empty);
            getView().setNoContentVisible(empty);
        }
    }

    private ArrayList<MovieItem> getEmptyList() {
        return new ArrayList<>();
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void onMovieSelected(MovieItem movieItem) {
        if (isRouterExist()) {
            getRouter().showMovieDetail(movieItem);
        }
    }

    private boolean isRouterExist() {
        return getRouter() != null;
    }

    private NavigationRouter getRouter() {
        return Config.getInstance().getNavigationRouter();
    }


    @Override
    public void onPause(Bundle bundle) {
        if (bundle == null) {
            return;
        }

        ArrayList<MovieItem> items = new ArrayList<>();
        if (!isDataEmpty()) {
            items.addAll(this.movies);
        }

        bundle.putParcelableArrayList("data", items);
    }

    @Override
    public void onResume(Bundle bundle) {
        if (bundle == null) {
            showData();
        }else {
            ArrayList<MovieItem> items = bundle.getParcelableArrayList("data");
            if (items == null) {
                items = getEmptyList();
            }
            updateData(items);
        }
    }
}
