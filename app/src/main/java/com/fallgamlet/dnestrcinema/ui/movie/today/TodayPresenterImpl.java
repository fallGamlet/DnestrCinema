package com.fallgamlet.dnestrcinema.ui.movie.today;

import android.os.Bundle;

import com.fallgamlet.dnestrcinema.app.AppFacade;
import com.fallgamlet.dnestrcinema.domain.models.MovieItem;
import com.fallgamlet.dnestrcinema.mvp.presenters.BasePresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpTodayPresenter;
import com.fallgamlet.dnestrcinema.mvp.routers.NavigationRouter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpTodayView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by fallgamlet on 03.07.17.
 */

public class TodayPresenterImpl
    extends
        BasePresenter <MvpTodayView>
    implements
        MvpTodayPresenter
{

    private List<MovieItem> movies;

    @Override
    public void bindView(MvpTodayView view) {
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
                .getTodayMovies()
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

    @Override
    public void onTicketBuySelected(MovieItem movieItem) {
        if (isRouterExist()) {
            getRouter().showBuyTicket(movieItem);
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
