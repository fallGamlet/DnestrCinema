package com.fallgamlet.dnestrcinema.ui.news;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fallgamlet.dnestrcinema.mvp.presenters.MvpNewsPresenter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpNewsView;
import com.fallgamlet.dnestrcinema.utils.DateTimeUtils;
import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.mvp.views.MvpBaseFragment;
import com.fallgamlet.dnestrcinema.utils.HttpUtils;
import com.fallgamlet.dnestrcinema.network.KinoTir;
import com.fallgamlet.dnestrcinema.mvp.models.MovieItem;
import com.fallgamlet.dnestrcinema.network.Network;
import com.fallgamlet.dnestrcinema.mvp.models.NewsItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment
    extends MvpBaseFragment <MvpNewsPresenter>
    implements MvpNewsView
{
    //region Constants
    public static final String ARG_URL = "arg_url";
    public static final String ARG_PARSER = "arg_parser";
    public static final String ARG_DATA = "arg_data";
    //endregion

    //region Fields
    private ViewGroup mRootView;
    private SwipeRefreshLayout mSwipeLayout;
    private RecyclerView mlistView;
    private TextView mPlaceholderView;

    private NewsRecyclerAdapter mAdapter;
    private String mUrl;
    private ArrayList<NewsItem> dataItems;
    private OnFragmentInteractionListener mListener;
    AsyncTask mTask;

    AlertDialog dialog;
    //endregion

    public NewsFragment() {
        // Required empty public constructor
    }

    //region newInstance methos
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CinemaFragment.
     */
    public static NewsFragment newInstance() {
        return newInstance(null, null);
    }

    public static NewsFragment newInstance(String url) {
        return newInstance(url, null);
    }

    public static NewsFragment newInstance(ArrayList<MovieItem> data) {
        return newInstance(null, data);
    }

    public static NewsFragment newInstance(String url, ArrayList<MovieItem> data) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        if (url != null) { args.putString(ARG_URL, url); }
        if (data != null) { args.putParcelableArrayList(ARG_DATA, data); }
        fragment.setArguments(args);
        return fragment;
    }
    //endregion

    //region Override Fragment lifecycle methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if (args != null) {
            mUrl = args.getString(ARG_URL);
            dataItems = args.getParcelableArrayList(ARG_DATA);
        } else {
            mUrl = null;
            dataItems = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cinema, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (view == null) {
            return;
        }

        initViews((ViewGroup) view);
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRootView = null;
        mSwipeLayout = null;
        mlistView = null;
        mPlaceholderView = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter = null;
        dataItems = null;
        mUrl = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            mListener = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (mTask != null && mTask.getStatus() == AsyncTask.Status.RUNNING) {
            mTask.cancel(true);
        }
        mTask = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState != null) {
            outState.putParcelableArrayList(ARG_DATA, getDataItems());
            outState.putString(ARG_URL, mUrl);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            String url = savedInstanceState.getString(ARG_URL);
            ArrayList<NewsItem> items = savedInstanceState.getParcelableArrayList(ARG_DATA);

            if (url != null) {
                mUrl = url;
            }
            if (items != null) {
                setDataItems(items);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDataItems().isEmpty()) {
            loadData(mUrl);
        }
    }
    //endregion

    //region OnRefreshListener and methods
    SwipeRefreshLayout.OnRefreshListener mOnRefreshListener;
    private SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        if (mOnRefreshListener == null) {
            mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadData(mUrl);
                }
            };
        }
        return mOnRefreshListener;
    }

    private void setRefreshVisible(boolean v) {
        if (mSwipeLayout != null) {
            mSwipeLayout.setRefreshing(v);
        }
    }
    //endregion

    //region Methods
    protected void initViews(ViewGroup view) {
        mRootView = view;

        if (view == null) { return; }

        mSwipeLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipeLayout);
        mSwipeLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        mSwipeLayout.setOnRefreshListener(getOnRefreshListener());

        mlistView = (RecyclerView) mRootView.findViewById(R.id.listView);
        mPlaceholderView = (TextView) mRootView.findViewById(R.id.placeholderView);

        if (mPlaceholderView != null) { mPlaceholderView.setVisibility(View.GONE); }

        if (mlistView != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            DefaultItemAnimator itemAnimator = new DefaultItemAnimator() {
                @Override
                public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
                    return true;
                }
            };

            int space = (int) getResources().getDimension(R.dimen.SpaceSmall);

            SpacerItemDecoration decoration = new SpacerItemDecoration();
            decoration.setSpace(space);

            mlistView.setLayoutManager(layoutManager);
            mlistView.setItemAnimator(itemAnimator);
            mlistView.setAdapter(getAdapter());
            mlistView.addItemDecoration(decoration);
        }
    }

    protected void initData() {
        getAdapter().setData(getDataItems());
    }

    protected void loadData(String url) {
        if (url == null || url.isEmpty()) {
            return;
        }

        setRefreshVisible(true);

        Network.get(url)
                .observeOn(Schedulers.computation())
                .subscribeOn(Schedulers.io())
                .map(new Function<String, List<NewsItem>>() {
                    @Override
                    public List<NewsItem> apply(@io.reactivex.annotations.NonNull String s) throws Exception {
                        return new KinoTir.NewsParser().parse(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<NewsItem>>() {
                               @Override
                               public void accept(@io.reactivex.annotations.NonNull List<NewsItem> newsItems) throws Exception {
                                   setDataItems(newsItems);
                                   setRefreshVisible(false);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                                setRefreshVisible(false);
                            }
                        });
    }

    public ArrayList<NewsItem> getDataItems() {
        if (dataItems == null) {
            dataItems = new ArrayList<>();
        }
        return dataItems;
    }

    protected void setDataItems(List<NewsItem> items) {
        getDataItems().clear();
        if (items != null && !items.isEmpty()) {
            getDataItems().addAll(items);
        }
        notifyDataSetChanged();

        showPlaceholder(items.isEmpty());
    }

    public void notifyDataSetChanged() {
        getAdapter().setData(getDataItems());
        getAdapter().notifyDataSetChanged();

        if (mPlaceholderView != null) {
            mPlaceholderView.setVisibility(getDataItems().isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    protected NewsRecyclerAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new NewsRecyclerAdapter();
            mAdapter.setListener(new NewsRecyclerAdapter.OnAdapterListener() {
                @Override
                public void onItemPressed(NewsItem item, int pos) {

                }

                @Override
                public void onItemSchedulePressed(NewsItem item, int pos) {

                }
            });
        }
        return mAdapter;
    }

    protected void showPlaceholder(boolean v) {
        if (mPlaceholderView != null) {
            mPlaceholderView.setVisibility(v? View.VISIBLE: View.GONE);
        }
    }
    //endregion

    //region Sub Classes and interfaces
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public static class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder> {
        //region Sub classes and Interfaces
        public interface OnAdapterListener {
            void onItemPressed(NewsItem item, int pos);
            void onItemSchedulePressed(NewsItem item, int pos);
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            //region Fields
            View mRootView;
            ImageView mImageView;
            TextView mTitleView;
            TextView mDateView;
            TextView mBodyView;

            NewsItem mItem;
            //endregion

            //region Constructors
            public ViewHolder(View itemView) {
                super(itemView);
                initView(itemView);
            }
            //endregion

            //region Getters
            public NewsItem getItem() { return mItem; }
            public View getRootView() { return mRootView; }
            public ImageView getImageView() { return mImageView; }
            public TextView getTitleView() { return mTitleView; }
            public TextView getDateeView() { return mDateView; }
            public TextView getBodyView() { return mBodyView; }
            //endregion

            //region Methods
            public void initView(View itemView) {
                mRootView = itemView;
                if (mRootView != null) {
                    mImageView = (ImageView) mRootView.findViewById(R.id.imageView);
                    mTitleView = (TextView) mRootView.findViewById(R.id.titleView);
                    mDateView = (TextView) mRootView.findViewById(R.id.dateView);
                    mBodyView = (TextView) mRootView.findViewById(R.id.textView);
                }
            }

            public void initData(NewsItem item) {
                mItem = item;
                if (item != null) {
                    setTitle(item.getTitle());
                    setDate(item.getDate());
                    setBody(item.getBody());
                }
            }


            //endregion

            //region Set visible
            public void setImageVisible(boolean v) {
                if (mImageView != null) {
                    mImageView.setVisibility(v? View.VISIBLE: View.GONE);
                }
            }

            public void setTitleVisible(boolean v) {
                if (mTitleView != null) {
                    mTitleView.setVisibility(v? View.VISIBLE: View.GONE);
                }
            }

            public void setDateVisible(boolean v) {
                if (mDateView != null) {
                    mDateView.setVisibility(v? View.VISIBLE: View.GONE);
                }
            }

            public void setBodyVisible(boolean v) {
                if (mBodyView != null) {
                    mBodyView.setVisibility(v? View.VISIBLE: View.GONE);
                }
            }
            //endregion

            //region Set data
            public void setTitle(String val) {
                if (mTitleView != null) {
                    mTitleView.setText(val);
                }
                setTitleVisible(!(val == null || val.isEmpty()));
            }

            public void setDate(Date val) {
                if (mDateView != null) {
                    mDateView.setText(DateTimeUtils.getDateDotWithoutTime(val));
                }
                setDateVisible(val != null);
            }

            public void setBody(String val) {
                if (mBodyView != null) {
                    mBodyView.setText(val);
                }
                setBodyVisible(!(val == null || val.isEmpty()));
            }

            //endregion
        }
        //endregion

        //region Fields
        protected NewsRecyclerAdapter.OnAdapterListener mListener;
        protected List<NewsItem> mListData, mListDataFiltered;
        //endregion

        //region Methods
        public NewsRecyclerAdapter() {
        }

        @Override
        public NewsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
            final NewsRecyclerAdapter.ViewHolder holder = new NewsRecyclerAdapter.ViewHolder(view);

            holder.getRootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int pos = getPosition(holder.getItem());
                        mListener.onItemPressed(holder.getItem(), pos);
                    }
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(NewsRecyclerAdapter.ViewHolder holder, int position) {
            final NewsItem item = getItem(position);
            holder.initData(item);

            //region Load and set Image
            Iterator<String> iterator = item.getImgUrls().iterator();
            String imgUrl = iterator.hasNext()? iterator.next(): null;

            if (imgUrl != null) {
                imgUrl = HttpUtils.getAbsoluteUrl(KinoTir.BASE_URL, imgUrl);
            }

            if (imgUrl != null) {
                Picasso.with(holder.getImageView().getContext()).load(imgUrl).into(holder.getImageView());
            }
            //endregion
        }

        @Override
        public void onBindViewHolder(NewsRecyclerAdapter.ViewHolder holder, int position, List<Object> payloads) {
            if (payloads == null || payloads.isEmpty()) {
                super.onBindViewHolder(holder, position, payloads);
                return;
            }

            Object obj = payloads.get(0);
            // Если изменения не пустые и это пришла загруженная картинка
            if (obj != null && obj instanceof Bitmap) {
                // устанавливаем картинку
                holder.getImageView().setImageBitmap((Bitmap)obj);
                holder.getImageView().setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return mListDataFiltered == null? 0 : mListDataFiltered.size();
        }

        public NewsItem getItem(int position) {
            return  (mListDataFiltered == null || position < 0 || position >= mListDataFiltered.size()) ? null : mListDataFiltered.get(position);
        }

        public int getPosition(NewsItem item) {
            if (mListDataFiltered == null) return -1;
            return mListDataFiltered.indexOf(item);
        }

        public void setData(List<NewsItem> list) {
            if (mListData == null) { mListData = new ArrayList<>(); }
            else { mListData.clear(); }

            if (mListDataFiltered == null) { mListDataFiltered = new ArrayList<>(); }
            else { mListDataFiltered.clear(); }

            if (list != null) { mListData.addAll(list); }
            mListDataFiltered.addAll(mListData);
        }

        public void setListener(NewsRecyclerAdapter.OnAdapterListener listener) {
            mListener = listener;
        }
        //endregion
    }
    //endregion
}
