package com.example.quiz_diadanh.model.database;

public class Room {
    public int id;
    public int creator_id;
    public int topic_id;
    public String name;
    public String password;
    public int time_limit;

    public Room() {
        // Default constructor required for calls to DataSnapshot.getValue(Room.class)
    }

    public Room(int id, int creator_id, int topic_id, String name, String password, int time_limit) {
        this.id = id;
        this.creator_id = creator_id;
        this.topic_id = topic_id;
        this.name = name;
        this.password = password;
        this.time_limit = time_limit;
    }
}