package com.fallgamlet.dnestrcinema.data.network.kinotir.mappers;

import com.fallgamlet.dnestrcinema.data.network.Mapper;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fallgamlet on 09.07.17.
 */

public class JsonLoginMapper implements Mapper <String, Boolean> {

    @Override
    public Boolean map(String src) {
        LoginResult loginResult;

        try {
            Gson gson = new Gson();
            loginResult = gson.fromJson(src, LoginResult.class);
        }
        catch (Exception ignored) {
            loginResult = new LoginResult();
        }

        return loginResult.success;
    }

    private class LoginResult {
        @SerializedName("success")
        public boolean success;

        @SerializedName("error")
        public String error;

        public LoginResult() {
            success = false;
            error = null;
        }
    }

}
