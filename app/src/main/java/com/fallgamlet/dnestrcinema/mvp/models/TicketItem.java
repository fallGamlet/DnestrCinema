package com.fallgamlet.dnestrcinema.mvp.models;

import androidx.collection.ArraySet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by fallgamlet on 24.03.17.
 */

public class TicketItem {

    private String id;
    private String tag;
    private String title;
    private String status;
    private String room;
    private String date;
    private String time;
    private String url;
    private Set<TicketPlace> ticketPlaceSet;


    public TicketItem() {
        this.id = null;
        this.tag = null;
        this.title = null;
        this.status = null;
        this.room = null;
        this.date = null;
        this.time = null;
        this.url = null;
        this.ticketPlaceSet = new HashSet<>(6);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public synchronized Set<TicketPlace> getTicketPlaceSet() {
        if (ticketPlaceSet == null) {
            ticketPlaceSet = new ArraySet<>();
        }

        return ticketPlaceSet;
    }

    public void setTicketPlaceSet(Collection<TicketPlace> ticketPlaceSet) {
        this.ticketPlaceSet.clear();

        if (ticketPlaceSet != null) {
            this.ticketPlaceSet.addAll(ticketPlaceSet);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
