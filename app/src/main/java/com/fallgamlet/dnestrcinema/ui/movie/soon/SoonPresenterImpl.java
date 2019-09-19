package com.fallgamlet.dnestrcinema.ui.movie.soon;

import android.os.Bundle;

import com.fallgamlet.dnestrcinema.app.AppFacade;
import com.fallgamlet.dnestrcinema.domain.models.MovieItem;
import com.fallgamlet.dnestrcinema.mvp.presenters.BasePresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpSoonPresenter;
import com.fallgamlet.dnestrcinema.mvp.routers.NavigationRouter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpSoonView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by fallgamlet on 03.07.17.
 */
@Deprecated
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

        AppFacade.Companion.getInstance()
                .getNetClient()
                .getSoonMovies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        movieItems -> updateData(movieItems),
                        throwable -> updateData(getEmptyList())
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
        return AppFacade.Companion.getInstance().getNavigationRouter();
    }


    @Override
    public void onSave(Bundle bundle) {
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
    public void onRestore(Bundle bundle) {
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
