package com.example.quiz_diadanh.model;

public class Result {
    private int id;
    private int quizId;
    private int score;
    private String selectedOption;
    private String status;
    private String timestamp;
    private int userId;
    private int roomId;

    public Result(int id, int quizId, int score, String selectedOption, String status, String timestamp, int userId, int roomId) {
        this.id = id;
        this.quizId = quizId;
        this.score = score;
        this.selectedOption = selectedOption;
        this.status = status;
        this.timestamp = timestamp;
        this.userId = userId;
        this.roomId = roomId;
    }

    public Result() {
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", quizId=" + quizId +
                ", score=" + score +
                ", selectedOption='" + selectedOption + '\'' +
                ", status='" + status + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", userId=" + userId +
                '}';
    }
}
