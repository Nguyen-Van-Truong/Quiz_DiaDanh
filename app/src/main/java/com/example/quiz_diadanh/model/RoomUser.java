package com.example.quiz_diadanh.model;

public class RoomUser {
    private int id;
    private int roomId;
    private String status;
    private int userId;

    public RoomUser(int id, int roomId, String status, int userId) {
        this.id = id;
        this.roomId = roomId;
        this.status = status;
        this.userId = userId;
    }

    public RoomUser() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "RoomUser{" +
                "id=" + id +
                ", roomId=" + roomId +
                ", status='" + status + '\'' +
                ", userId=" + userId +
                '}';
    }
}
