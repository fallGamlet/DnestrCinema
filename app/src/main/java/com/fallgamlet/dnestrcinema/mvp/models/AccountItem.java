package com.fallgamlet.dnestrcinema.mvp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fallgamlet on 15.07.17.
 */

public class AccountItem {

    @SerializedName("login")
    private String login;
    @SerializedName("password")
    private String password;
    @SerializedName("cinemaId")
    private long cinemaId;


    public AccountItem() {
        login = null;
        password = null;
        cinemaId = 0;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(long cinemaId) {
        this.cinemaId = cinemaId;
    }
}
