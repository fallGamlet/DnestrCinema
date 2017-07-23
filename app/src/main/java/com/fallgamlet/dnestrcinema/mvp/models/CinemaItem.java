package com.fallgamlet.dnestrcinema.mvp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fallgamlet on 06.07.17.
 */

public class CinemaItem {

    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
