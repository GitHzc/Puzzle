package com.example.puzzle.model;

import java.util.List;

public class UserInfoBean {

    /**
     * content : [{"rank":"1","username":"928849385"}]
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
         * rank : 1
         * username : 928849385
         */

        private String rank;
        private String username;

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
