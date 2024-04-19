package com.example.jdbcjavafx.datas;

public class UserData {
    private final int id;
    private final String username;

    public UserData(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
