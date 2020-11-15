package com.example.mynovel;

public class novelAndAuthor {
    private int id;
    private String novel_name;
    private String author_name;
    private String novel_type;

    public novelAndAuthor(){

    }
    public novelAndAuthor(int id, String novel_name, String author_name, String novel_type) {
        this.id = id;
        this.novel_name = novel_name;
        this.author_name = author_name;
        this.novel_type = novel_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNovel_name() {
        return novel_name;
    }

    public void setNovel_name(String novel_name) {
        this.novel_name = novel_name;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getNovel_type() {
        return novel_type;
    }

    public void setNovel_type(String novel_type) {
        this.novel_type = novel_type;
    }
}
