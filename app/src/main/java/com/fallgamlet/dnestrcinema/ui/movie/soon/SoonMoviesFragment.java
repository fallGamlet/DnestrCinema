package com.fallgamlet.dnestrcinema.ui.movie.soon;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.mvp.models.Config;
import com.fallgamlet.dnestrcinema.mvp.models.MovieItem;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpSoonPresenter;
import com.fallgamlet.dnestrcinema.mvp.views.Fragments;
import com.fallgamlet.dnestrcinema.ui.movie.DividerItemDecoration;
import com.fallgamlet.dnestrcinema.ui.movie.MovieRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SoonMoviesFragment
    extends
        Fragments.MvpSoonViewFragment
{
    //region Fields
    private View rootView;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.listView)
    RecyclerView listView;
    @BindView(R.id.placeholderView)
    TextView placeholderView;

    private MovieRecyclerAdapter mAdapter;
    SwipeRefreshLayout.OnRefreshListener refreshListener;
    //endregion

    public SoonMoviesFragment() {
        MvpSoonPresenter presenter = Config.getInstance()
                                            .getPresenterFactory()
                                            .createSoonPresenter();
        setPresenter(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;
        ButterKnife.bind(this, view);
        initViews();
    }

    private void initViews() {
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        setContentVisible(true);
        setNoContentVisible(false);

        initSwipeLayout();
        initListView();
    }

    private void initSwipeLayout() {
        swipeLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        swipeLayout.setOnRefreshListener(getOnRefreshListener());
    }

    private void initListView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        DefaultItemAnimator itemAnimator = new DefaultItemAnimator() {
            @Override
            public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
                return true;
            }
        };

        int left = (int) getResources().getDimension(R.dimen.DividerLeft);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        decoration.setPaddingStart(left);

        listView.setLayoutManager(layoutManager);
        listView.setItemAnimator(itemAnimator);
        listView.addItemDecoration(decoration);
        listView.setAdapter(getAdapter());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootView = null;
        swipeLayout = null;
        listView = null;
        placeholderView = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getPresenter().onSave(outState);

//        if(outState != null) {
//            outState.putParcelableArrayList(ARG_DATA, getDataItems());
//            outState.putString(ARG_URL, mUrl);
//        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        getPresenter().onRestore(savedInstanceState);

//        if (savedInstanceState != null) {
//            String url = savedInstanceState.getString(ARG_URL);
//            ArrayList<MovieItem> items = savedInstanceState.getParcelableArrayList(ARG_DATA);
//
//            if (url != null) {
//                mUrl = url;
//            }
//            if (items != null) {
//                setDataItems(items);
//            }
//        }
    }

    @Override
    public void onPause() {
        super.onPause();

        getPresenter().unbindView();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().bindView(this);
    }


    private SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        if (refreshListener == null) {
            refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getPresenter().onRefresh();
                }
            };
        }
        return refreshListener;
    }


    private MovieRecyclerAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new MovieRecyclerAdapter(R.layout.movie_item_soon);
            mAdapter.setListener(new MovieRecyclerAdapter.OnAdapterListener() {
                @Override
                public void onItemPressed(MovieItem item, int pos) {
                    getPresenter().onMovieSelected(item);
                }

                @Override
                public void onItemSchedulePressed(MovieItem item, int pos) {
                }

                @Override
                public void onItemBuyTicketPressed(MovieItem item, int pos) {
                }
            });
        }
        return mAdapter;
    }


    @Override
    public void showLoading() {
        if (this.swipeLayout != null) {
            this.swipeLayout.setRefreshing(true);
        }
    }

    @Override
    public void hideLoading() {
        if (this.swipeLayout != null) {
            this.swipeLayout.setRefreshing(false);
        }
    }

    @Override
    public void showData(List<MovieItem> items) {
        getAdapter().setData(items);
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public void setContentVisible(boolean v) {
        if (this.swipeLayout != null) {
            this.swipeLayout.setVisibility(v? View.VISIBLE: View.GONE);
        }
    }

    @Override
    public void setNoContentVisible(boolean v) {
        if (this.placeholderView!= null) {
            this.placeholderView.setVisibility(v? View.VISIBLE: View.GONE);
        }
    }
}
