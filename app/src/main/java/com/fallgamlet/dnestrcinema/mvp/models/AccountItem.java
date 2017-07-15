package com.fallgamlet.dnestrcinema.mvp.models;

/**
 * Created by fallgamlet on 15.07.17.
 */

public class AccountItem {

    private String login;
    private String password;
    private int cinemaId;


    public AccountItem() {
        login = null;
        password = null;
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
