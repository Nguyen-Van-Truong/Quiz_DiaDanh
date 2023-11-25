package com.example.quiz_diadanh.model.database;

public class Result {
    public int id;
    public int user_id;
    public int quiz_id;
    public String selected_option;
    public String timestamp;
    public int score;

    public Result() {
        // Default constructor required for calls to DataSnapshot.getValue(Result.class)
    }

    public Result(int id, int user_id, int quiz_id, String selected_option, String timestamp, int score) {
        this.id = id;
        this.user_id = user_id;
        this.quiz_id = quiz_id;
        this.selected_option = selected_option;
        this.timestamp = timestamp;
        this.score = score;
    }
}