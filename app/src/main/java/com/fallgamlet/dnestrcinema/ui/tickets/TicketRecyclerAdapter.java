package com.fallgamlet.dnestrcinema.ui.tickets;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.mvp.models.TicketItem;
import com.fallgamlet.dnestrcinema.ui.adapters.BaseRecyclerAdapter;
import com.fallgamlet.dnestrcinema.ui.holders.TicketViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fallgamlet on 16.07.17.
 */

public class TicketRecyclerAdapter extends BaseRecyclerAdapter<TicketItem, TicketViewHolder> {


    public TicketRecyclerAdapter() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ticket_item;
    }

    @Override
    protected TicketViewHolder createViewHolder(View view) {
        return new TicketViewHolder(view);
    }

}
