package com.example.quiz_diadanh.model.database;

public class RoomUser {
    public int id;
    public int room_id;
    public int user_id;

    public RoomUser() {
        // Default constructor required for calls to DataSnapshot.getValue(RoomUser.class)
    }

    public RoomUser(int id, int room_id, int user_id) {
        this.id = id;
        this.room_id = room_id;
        this.user_id = user_id;
    }
}