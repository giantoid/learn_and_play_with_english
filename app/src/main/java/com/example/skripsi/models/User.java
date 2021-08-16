package com.example.skripsi.models;

public class User {
    private int id_user;
    private String username;

    public User(int id_user, String username) {
        this.id_user = id_user;
        this.username = username;
    }

    public int getId() {
        return id_user;
    }

    public String getUsername() {
        return username;
    }

}
