package com.example.quiz_diadanh.model;

public class Room {
    private int id;
    private int creatorId;
    private String name;
    private String password;
    private String status;
    private int timeLimit;
    private int topicId;

    public Room(int id, int creatorId, String name, String password, String status, int timeLimit, int topicId) {
        this.id = id;
        this.creatorId = creatorId;
        this.name = name;
        this.password = password;
        this.status = status;
        this.timeLimit = timeLimit;
        this.topicId = topicId;
    }

    public Room() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", creatorId=" + creatorId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", status='" + status + '\'' +
                ", timeLimit=" + timeLimit +
                ", topicId=" + topicId +
                '}';
    }
}
