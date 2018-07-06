package com.example.puzzle.model;

import java.util.List;

public class HistoryBean {

    /**
     * content : [{"mode":"3","score":"90","time":"2018-01-02 00:10:01"},{"mode":"3","score":"100","time":"2018-01-01 00:00:00"},{"mode":"3","score":"150","time":"2018-01-01 00:30:01"},{"mode":"3","score":"160","time":"2018-01-01 00:10:01"}]
     * error :
     */

    private String error;
    private List<ContentBean> content;

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
         * mode : 3
         * score : 90
         * time : 2018-01-02 00:10:01
         */

        private String mode;
        private String score;
        private String time;

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
