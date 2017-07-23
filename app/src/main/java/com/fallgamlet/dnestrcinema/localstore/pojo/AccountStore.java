package com.fallgamlet.dnestrcinema.localstore.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fallgamlet on 23.07.17.
 */

public class AccountStore {

    @SerializedName("login")
    private String login;
    @SerializedName("password")
    private String password;
    @SerializedName("cinemaId")
    private int cinemaId;


    public AccountStore() {
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

    public int getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(int cinemaId) {
        this.cinemaId = cinemaId;
    }

}
