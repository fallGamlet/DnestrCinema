package com.fallgamlet.dnestrcinema;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.fallgamlet.dnestrcinema.network.RssItem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CinemaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CinemaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CinemaFragment extends Fragment {
    //region Fields
    private ViewGroup mRootView;
    private RecyclerView mlistView;
    private TextView mPlaceholderView;

    private RssRecyclerAdapter mAdapter;
    private ArrayList<RssItem> dataItems;
    private OnFragmentInteractionListener mListener;

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
        CinemaFragment fragment = new CinemaFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_cinema, container, false);
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

        getAdapter().setData(dataItems);

        return mRootView;
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
    }

    public ArrayList<RssItem> getDataItems() {
        if (dataItems == null) {
            dataItems = new ArrayList<>();
        }
        return dataItems;
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
                public void onItemPressed(RssItem item, int pos) {
                    navigateToDetail(item);
                }

                @Override
                public void onItemSchedulePressed(RssItem item, int pos) {
//                    navigateToRoomView(item);
                }
            });
        }
        return mAdapter;
    }

    protected void navigateToDetail(RssItem rssItem) {
        if (rssItem != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(CinemaDetailActivity.ARG_RSSITEM, rssItem);

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
        if (RssItem.ROOM_BLUE.equalsIgnoreCase(roomName)) {
            imgURL = DataSettings.PATH_IMG_ROOM_BLUE;
        } else if (RssItem.ROOM_BORDO.equalsIgnoreCase(roomName)) {
            imgURL = DataSettings.PATH_IMG_ROOM_BORDO;
        } else if (RssItem.ROOM_DVD.equalsIgnoreCase(roomName)) {
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

    protected  void navigateToRoomView(RssItem rssItem) {
        String[] rooms = null;
        if (rssItem != null && !rssItem.getSchedules().isEmpty()) {
            int count = rssItem.getSchedules().size();
            rooms = new String[count];
            for (int i=0; i<count; i++) {
                RssItem.Schedule item = rssItem.getSchedules().get(i);
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
