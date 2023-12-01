package com.example.quiz_diadanh.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseService {

    private DatabaseReference databaseReference;

    public FirebaseService() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void addQuiz(Quiz quiz) {
        databaseReference.child("quizzes").child(String.valueOf(quiz.getId())).setValue(quiz);
    }

    public void updateQuiz(String quizId, Quiz quiz) {
        databaseReference.child("quizzes").child(quizId).setValue(quiz);
    }

    public void deleteQuiz(String quizId) {
        databaseReference.child("quizzes").child(quizId).removeValue();
    }

    public void addUser(User user) {
        databaseReference.child("users").child(String.valueOf(user.getId())).setValue(user);
    }

    public void updateUser(String userId, User user) {
        databaseReference.child("users").child(userId).setValue(user);
    }

    public void deleteUser(String userId) {
        databaseReference.child("users").child(userId).removeValue();
    }

    public void addRoom(Room room) {
        databaseReference.child("rooms").child(String.valueOf(room.getId())).setValue(room);
    }

    public void updateRoom(String roomId, Room room) {
        databaseReference.child("rooms").child(roomId).setValue(room);
    }

    public void deleteRoom(String roomId) {
        databaseReference.child("rooms").child(roomId).removeValue();
    }

    public void addTopic(Topic topic) {
        databaseReference.child("topics").child(String.valueOf(topic.getId())).setValue(topic);
    }

    public void updateTopic(String topicId, Topic topic) {
        databaseReference.child("topics").child(topicId).setValue(topic);
    }

    public void deleteTopic(String topicId) {
        databaseReference.child("topics").child(topicId).removeValue();
    }

    public void addResult(Result result) {
        databaseReference.child("results").child(String.valueOf(result.getId())).setValue(result);
    }

    public void updateResult(String resultId, Result result) {
        databaseReference.child("results").child(resultId).setValue(result);
    }

    public void deleteResult(String resultId) {
        databaseReference.child("results").child(resultId).removeValue();
    }

    public void addRoomUser(RoomUser roomUser) {
        databaseReference.child("room_users").child(String.valueOf(roomUser.getId())).setValue(roomUser);
    }

    public void updateRoomUser(String roomUserId, RoomUser roomUser) {
        databaseReference.child("room_users").child(roomUserId).setValue(roomUser);
    }

    public void deleteRoomUser(String roomUserId) {
        databaseReference.child("room_users").child(roomUserId).removeValue();
    }
}
