package com.example.puzzle.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/7/7 0007.
 */

public class PhotoBean {

    /**
     * content : [{"pid":"1","src":"https://ws4.sinaimg.cn/large/006tKfTcgy1ft15brnwtmj307g07g3yg.jpg"},{"pid":"2","src":"https://ws2.sinaimg.cn/large/006tKfTcgy1ft15c4b1z9j30b40b4mx7.jpg"},{"pid":"3","src":"https://ws3.sinaimg.cn/large/006tKfTcgy1ft15o5t3hij308t08t3yl.jpg"}]
     * error :
     */

    @SerializedName("error")
    private String error;
    @SerializedName("content")
    private List<ContentBean> content;

    public static PhotoBean objectFromData(String str) {

        return new Gson().fromJson(str, PhotoBean.class);
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
         * pid : 1
         * src : https://ws4.sinaimg.cn/large/006tKfTcgy1ft15brnwtmj307g07g3yg.jpg
         */

        @SerializedName("pid")
        private String pid;
        @SerializedName("src")
        private String src;

        public static ContentBean objectFromData(String str) {

            return new Gson().fromJson(str, ContentBean.class);
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }
    }
}
