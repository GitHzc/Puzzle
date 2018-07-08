package com.example.puzzle.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class LoginBean {

    /**
     * error : 密码错误
     */

    @SerializedName("error")
    private String error;

    public static LoginBean objectFromData(String str) {

        return new Gson().fromJson(str, LoginBean.class);
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
