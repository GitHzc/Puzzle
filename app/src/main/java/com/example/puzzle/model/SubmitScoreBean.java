package com.example.puzzle.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/7/6 0006.
 */

public class SubmitScoreBean {

    /**
     * error :
     * content : [{"rid":1}]
     */

    @SerializedName("error")
    private String error;
    @SerializedName("content")
    private List<ContentBean> content;

    public static SubmitScoreBean objectFromData(String str) {

        return new Gson().fromJson(str, SubmitScoreBean.class);
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * rid : 1
         */

        @SerializedName("rid")
        private int rid;

        public static ContentBean objectFromData(String str) {

            return new Gson().fromJson(str, ContentBean.class);
        }

        public int getRid() {
            return rid;
        }

        public void setRid(int rid) {
            this.rid = rid;
        }
    }
}
