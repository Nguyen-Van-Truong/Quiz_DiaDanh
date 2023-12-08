package com.example.quiz_diadanh.model;

import java.io.Serializable;

public class Room implements Serializable {
    private int id;
    private int creatorId;
    private String code;
    private String password;
    private String status;
    private int timeLimit;
    private int topicId;

    public Room(int id, int creatorId, String code, String password, String status, int timeLimit, int topicId) {
        this.id = id;
        this.creatorId = creatorId;
        this.code = code;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
                ", code='" + code + '\'' +
                ", password='" + password + '\'' +
                ", status='" + status + '\'' +
                ", timeLimit=" + timeLimit +
                ", topicId=" + topicId +
                '}';
    }
}
