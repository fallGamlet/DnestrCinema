package com.fallgamlet.dnestrcinema;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fallgamlet.dnestrcinema.network.DataSettings;
import com.fallgamlet.dnestrcinema.network.KinoTir;
import com.fallgamlet.dnestrcinema.network.MovieItem;
import com.fallgamlet.dnestrcinema.network.Network;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CinemaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CinemaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CinemaFragment extends Fragment {
    //region Constants
    public static final String ARG_URL = "arg_url";
    public static final String ARG_PARSER = "arg_parser";
    public static final String ARG_DATA = "arg_data";
    //endregion

    //region Fields
    private ViewGroup mRootView;
    private RecyclerView mlistView;
    private TextView mPlaceholderView;

    private RssRecyclerAdapter mAdapter;
    private String mUrl;
    private ArrayList<MovieItem> dataItems;
    private OnFragmentInteractionListener mListener;
    AsyncTask mTask;

    AlertDialog dialog;
    //endregion

    public CinemaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CinemaFragment.
     */
    public static CinemaFragment newInstance() {
        return newInstance(null, null);
    }

    public static CinemaFragment newInstance(String url) {
        return newInstance(url, null);
    }

    public static CinemaFragment newInstance(ArrayList<MovieItem> data) {
        return newInstance(null, data);
    }

    public static CinemaFragment newInstance(String url, ArrayList<MovieItem> data) {
        CinemaFragment fragment = new CinemaFragment();
        Bundle args = new Bundle();
        if (url != null) { args.putString(ARG_URL, url); }
        if (data != null) { args.putParcelableArrayList(ARG_DATA, data); }
        fragment.setArguments(args);
        return fragment;
    }

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
            ArrayList<MovieItem> items = savedInstanceState.getParcelableArrayList(ARG_DATA);

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

    protected void initViews(ViewGroup view) {
        mRootView = view;
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
            mlistView.setLayoutManager(layoutManager);
            mlistView.setItemAnimator(itemAnimator);
            mlistView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
            mlistView.setAdapter(getAdapter());
        }
    }

    protected void initData() {
        getAdapter().setData(getDataItems());
    }

    protected void loadData(String url) {
        if (url == null || url.isEmpty()) {
            return;
        }

        Network.RequestData requestData = new Network.RequestData();
        requestData.options = Network.Options.Default();
        requestData.options.contentType = Network.CONTENT_TYPE_HTML;
        requestData.url = url;

        mTask = Network.requestDataAsync(requestData, new Network.ResponseHandle() {
            @Override
            public void finished(Exception exception, Network.StrResult result) {
                mTask = null;
                if (exception != null) {
                    String msg = null;
                    if (exception instanceof UnknownHostException) {
                        msg = "Веб сервис недоступен. Возможно нет соединение с сетью Интернет";
                    } else {
                        msg = exception.toString();
                    }

                    System.err.println(exception);
                    return;
                }

                if (result.error != null) {
                    System.err.println(exception);
                    return;
                }

                List<MovieItem> items = new KinoTir.MovieListParser().parse(result.data);
                setDataItems(items);
            }
        });
    }

    public ArrayList<MovieItem> getDataItems() {
        if (dataItems == null) {
            dataItems = new ArrayList<>();
        }
        return dataItems;
    }

    protected void setDataItems(List<MovieItem> items) {
        getDataItems().clear();
        if (items != null && !items.isEmpty()) {
            getDataItems().addAll(items);
        }
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        getAdapter().setData(getDataItems());
        getAdapter().notifyDataSetChanged();

        if (mPlaceholderView != null) {
            mPlaceholderView.setVisibility(getDataItems().isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    protected RssRecyclerAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new RssRecyclerAdapter();
            mAdapter.setListener(new RssRecyclerAdapter.OnAdapterListener() {
                @Override
                public void onItemPressed(MovieItem item, int pos) {
                    navigateToDetail(item);
                }

                @Override
                public void onItemSchedulePressed(MovieItem item, int pos) {
//                    navigateToRoomView(item);
                }
            });
        }
        return mAdapter;
    }

    protected void navigateToDetail(MovieItem movieItem) {
        if (movieItem != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(CinemaDetailActivity.ARG_RSSITEM, movieItem);

            Intent intent = new Intent(getContext(), CinemaDetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    protected  void navigateToRoomView(String roomName) {
        if (roomName == null) {
            return;
        }

        String imgURL = null;
        if (MovieItem.ROOM_BLUE.equalsIgnoreCase(roomName)) {
            imgURL = DataSettings.PATH_IMG_ROOM_BLUE;
        } else if (MovieItem.ROOM_BORDO.equalsIgnoreCase(roomName)) {
            imgURL = DataSettings.PATH_IMG_ROOM_BORDO;
        } else if (MovieItem.ROOM_DVD.equalsIgnoreCase(roomName)) {
            imgURL = DataSettings.PATH_IMG_ROOM_DVD;
        }

        if (imgURL != null) {
            imgURL = DataSettings.BASE_URL + imgURL;
            Bundle bundle = new Bundle();
            bundle.putString(ImageActivity.ARG_IMG_URL, imgURL);

            Intent intent = new Intent(getContext(), ImageActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
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

        if (rooms != null) {
            if (rooms.length == 1) {
                navigateToRoomView(rooms[0]);
            } else {
                final String[] items = rooms;
                dialog = new AlertDialog.Builder(getContext(), R.style.AppTheme_Dialog)
                        .setTitle("Выберите зал")
                        .setCancelable(true)
                        .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (dialog != null) {
                                    dialog.dismiss();
                                    dialog = null;
                                }
                                navigateToRoomView(items[i]);
                            }
                        })
                        .create();
                dialog.show();
            }
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
