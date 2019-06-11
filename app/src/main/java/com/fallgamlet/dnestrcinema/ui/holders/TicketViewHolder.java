package com.fallgamlet.dnestrcinema.ui.holders;

import androidx.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.domain.models.TicketItem;

/**
 * Created by fallgamlet on 23.07.17.
 */

public class TicketViewHolder extends BaseViewHolder<TicketItem> {

    private TextView titleView;
    private FieldHolder orderHolder;
    private FieldHolder roomHolder;
    private FieldHolder statusHolder;
    private FieldHolder dateHolder;
    private FieldHolder timeHolder;


    public TicketViewHolder(View itemView) {
        super(itemView);
        initView(itemView);
    }

    private void initView(View view) {
        titleView = (TextView) view.findViewById(R.id.titleView);
        orderHolder = new FieldHolder(view.findViewById(R.id.order));
        statusHolder = new FieldHolder(view.findViewById(R.id.status));
        roomHolder = new FieldHolder(view.findViewById(R.id.room));
        dateHolder = new FieldHolder(view.findViewById(R.id.date));
        timeHolder = new FieldHolder(view.findViewById(R.id.time));
    }

    @Override
    public void setItem(TicketItem item) {
        super.setItem(item);
        updateData();
    }

    public void updateData() {
        TicketItem ticketItem = getItem();
        if (ticketItem == null) {
            ticketItem = new TicketItem();
        }

        setTitle(ticketItem.getTitle());
        setOrder(ticketItem.getId());
        setRoom(ticketItem.getRoom());
        setStatus(ticketItem.getStatus());
        setDate(ticketItem.getDate());
        setTime(ticketItem.getTime());
    }

    public void setTitle(CharSequence value) {
        if (titleView != null) {
            titleView.setText(value);
        }
    }

    public void setOrder(String value) {
        orderHolder.setKey(getString(R.string.label_order));
        orderHolder.setValue(value);
    }

    public void setRoom(String value) {
        roomHolder.setKey(getString(R.string.label_room));
        roomHolder.setValue(value);
    }

    public void setStatus(String value) {
        statusHolder.setKey(getString(R.string.label_status));
        statusHolder.setValue(value);
    }

    public void setDate(String value) {
        dateHolder.setKey(getString(R.string.label_session_date));
        dateHolder.setValue(value);
    }

    public void setTime(String value) {
        timeHolder.setKey(getString(R.string.label_session_time));
        timeHolder.setValue(value);
    }

    private String getString(@StringRes int stringId) {
        return getRootView().getContext().getString(stringId);
    }

}
