package com.example.puzzle.model;

import java.util.List;

public class RankBean {
    /**
     * content : [{"mode":"3","score":"50","username":"dingjy"},{"mode":"4","score":"100","username":"12"},{"mode":"4","score":"140","username":"dingjy"},{"mode":"4","score":"200","username":"dingjy"},{"mode":"4","score":"300","username":"dingjy"}]
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
         * score : 50
         * username : dingjy
         */

        private String mode;
        private String score;
        private String username;

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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
