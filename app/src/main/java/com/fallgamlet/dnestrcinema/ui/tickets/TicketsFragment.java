package com.fallgamlet.dnestrcinema.ui.tickets;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.app.AppFacade;
import com.fallgamlet.dnestrcinema.domain.models.TicketItem;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpTicketsPresenter;
import com.fallgamlet.dnestrcinema.mvp.routers.LoginRouter;
import com.fallgamlet.dnestrcinema.mvp.views.Fragments;
import com.fallgamlet.dnestrcinema.mvp.views.MvpTicketsView;
import com.fallgamlet.dnestrcinema.ui.adapters.BaseRecyclerAdapter;
import com.fallgamlet.dnestrcinema.ui.movie.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TicketsFragment
    extends
        Fragments.MvpTicketsViewFragment
    implements
        MvpTicketsView
{

    //region Fields
    private View rootView;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.listView)
    RecyclerView listView;
    @BindView(R.id.placeholderView)
    TextView placeholderView;

    private TicketRecyclerAdapter mAdapter;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    //endregion


    public static TicketsFragment newInstance() {
        TicketsFragment fragment = new TicketsFragment();
        return fragment;
    }

    public TicketsFragment() {
        MvpTicketsPresenter presenter = AppFacade.Companion.getInstance().getPresenterFactory().createTicketPresenter();
        setPresenter(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tickets, container, false);
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

        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        decoration.setPaddingStart(0);

        listView.setLayoutManager(layoutManager);
        listView.setItemAnimator(itemAnimator);
        listView.setAdapter(getAdapter());
        listView.addItemDecoration(decoration);
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

    private TicketRecyclerAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new TicketRecyclerAdapter();
            mAdapter.setListener(new BaseRecyclerAdapter.OnAdapterListener() {
                @Override
                public void onItemPressed(Object item, int pos) {

                }
            });
        }
        return mAdapter;
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
    public void onPause() {
        super.onPause();
        getPresenter().setRouter(null);
        getPresenter().unbindView();
    }

    @Override
    public void onResume() {
        super.onResume();

        FragmentManager fm1 = getFragmentManager();
        LoginRouter loginRouter = new LoginRouterImpl(fm1, R.id.rootView);

        getPresenter().setRouter(loginRouter);
        getPresenter().bindView(this);
    }


    @OnClick(R.id.logoutButton)
    void onLogoutPressed(View view) {
        getPresenter().onLogout();
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
    public void showData(List<TicketItem> items) {
        getAdapter().setData(items);
    }

    @Override
    public void setContentVisible(boolean v) {
        if (this.swipeLayout != null) {
            this.swipeLayout.setVisibility(v? View.VISIBLE: View.GONE);
        }
    }

    @Override
    public void setNoContentVisible(boolean v) {
        if (placeholderView != null) {
            placeholderView.setVisibility(v? View.VISIBLE: View.GONE);
        }
    }
}
