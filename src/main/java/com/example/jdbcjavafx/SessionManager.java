package com.example.jdbcjavafx;

import com.example.jdbcjavafx.datas.UserData;

public class SessionManager {
    private static SessionManager sessionManager = null;
    private UserData userData;
    public synchronized static SessionManager getInstance() {
        if (sessionManager == null)
            sessionManager = new SessionManager();
        return sessionManager;
    }
    private SessionManager() {
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }
}
