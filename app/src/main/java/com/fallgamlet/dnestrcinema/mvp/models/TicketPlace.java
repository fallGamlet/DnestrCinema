package com.fallgamlet.dnestrcinema.mvp.models;

/**
 * Created by fallgamlet on 09.07.17.
 */

public class TicketPlace {

    private int row;
    private int place;
    private String url;

    public TicketPlace() {
        this.row = -1;
        this.place = -1;
        this.url = null;
    }

    public TicketPlace(int row, int place, String url) {
        this.row = row;
        this.place = place;
        this.url = url;
    }


    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
