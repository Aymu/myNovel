package com.example.mynovel;

public class collection {
    private String user;
    private String name;
    private String author;
    private String url;
    private String type;

    public collection() {
    }

    public collection(String user,String name, String author, String url, String type) {
        this.user = user;
        this.name = name;
        this.author = author;
        this.url = url;
        this.type = type;
    }
    public void setUser(String u){
        this.user = u;
    }
    public String getUser(){
        return this.user;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }
}
