package com.example.jdbcjavafx.datas;

public class UserData {
    private int id;
    private String username, email;
    public UserData(int id, String username) {
        this(id, username, null);
    }

    public UserData(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
