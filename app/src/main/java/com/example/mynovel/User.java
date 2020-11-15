package com.example.mynovel;

public class User {
    private int id;
    private String name;
    private String pwd;
    private String tel;

    public  User(){

    }
    public User(int id, String name, String pwd, String tel) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
        this.tel = tel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
