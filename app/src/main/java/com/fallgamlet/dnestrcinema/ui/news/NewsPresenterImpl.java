package com.fallgamlet.dnestrcinema.ui.news;

import android.os.Bundle;

import com.fallgamlet.dnestrcinema.app.AppFacade;
import com.fallgamlet.dnestrcinema.domain.models.NewsItem;
import com.fallgamlet.dnestrcinema.mvp.presenters.BasePresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpNewsPresenter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpNewsView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by fallgamlet on 03.07.17.
 */

public class NewsPresenterImpl
        extends
            BasePresenter <MvpNewsView>
        implements
            MvpNewsPresenter
{

    private List<NewsItem> newsItems;

    @Override
    public void bindView(MvpNewsView view) {
        super.bindView(view);

        if (isDataEmpty()) {
            loadData();
        } else {
            showData();
        }
    }

    private boolean isDataEmpty() {
        return newsItems == null || newsItems.isEmpty();
    }

    @Override
    public void loadData() {
        if(isViewBinded()) {
            getView().showLoading();
        }

        AppFacade.getInstance()
                .getNetClient()
                .getNews()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        new Consumer<List<NewsItem>>() {
                            @Override
                            public void accept(@NonNull List<NewsItem> items) throws Exception {
                                updateData(items);
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

    private void updateData(List<NewsItem> items) {
        newsItems = items;
        showData();
    }

    private void showData() {
        if (newsItems == null) {
            newsItems = getEmptyList();
        }

        if (isViewBinded()) {
            boolean empty = newsItems.isEmpty();

            getView().hideLoading();
            getView().showData(newsItems);
            getView().setContentVisible(!empty);
            getView().setNoContentVisible(empty);
        }
    }

    private ArrayList<NewsItem> getEmptyList() {
        return new ArrayList<>();
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void onSave(Bundle bundle) {
        if (bundle == null) {
            return;
        }

        ArrayList<NewsItem> items = new ArrayList<>();
        if (!isDataEmpty()) {
            items.addAll(this.newsItems);
        }

        bundle.putParcelableArrayList("data", items);
    }

    @Override
    public void onRestore(Bundle bundle) {
        if (bundle == null) {
            showData();
        }else {
            ArrayList<NewsItem> items = bundle.getParcelableArrayList("data");
            if (items == null) {
                items = getEmptyList();
            }
            updateData(items);
        }
    }
}
