package com.example.mynovel;

public class userMessage {
    private String username;
    private String message;
    private String touser;
    private String tomsg;
    public userMessage(){

    }

    public String getTouser() {
        return touser;
    }

    public userMessage(String username, String message, String touser,String tomsg) {
        this.username = username;
        this.message = message;
        this.touser = touser;
        this.tomsg = tomsg;
    }

    public String getTomsg() {
        return tomsg;
    }

    public void setTomsg(String tomsg) {
        this.tomsg = tomsg;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }
}
