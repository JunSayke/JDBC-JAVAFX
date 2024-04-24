package com.example.jdbcjavafx.datas;

public class FlashcardData {
    private final int id;
    private final int userId;
    private final String front;
    private final String back;

    public FlashcardData(int id, int userId, String question, String answer) {
        this.id = id;
        this.userId = userId;
        this.front = question;
        this.back = answer;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getFront() {
        return front;
    }

    public String getBack() {
        return back;
    }

    @Override
    public String toString() {
        return front;
    }
}
