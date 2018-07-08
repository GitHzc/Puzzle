package com.example.puzzle.model;

public class User {
    String username;
    String password;
    final String cookie;

    public User(String username, String password, String cookie) {
        this.username = username;
        this.password = password;
        this.cookie = cookie;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCookie() {
        return cookie;
    }

//    public void setCookie(String cookie) {
//        this.cookie = cookie;
//    }
}
