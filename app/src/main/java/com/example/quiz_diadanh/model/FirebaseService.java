package com.example.quiz_diadanh.model;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FirebaseService {

    private DatabaseReference databaseReference;
    private FirebaseStorage storage;

    public FirebaseService() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
    }

    public void loadQuizImage(String imageUrl, ImageView imageView, Context context) {
        if (imageUrl.startsWith("gs://")) {
            // Nếu là đường dẫn gs:// thì chuyển đổi sang đường dẫn public
            convertGsUrlToPublicUrl(imageUrl, imageView, context);
        } else {
            // Nếu là đường dẫn public thì load trực tiếp
            Glide.with(context)
                    .load(imageUrl)
                    .into(imageView);
        }
    }

    private void convertGsUrlToPublicUrl(String gsUrl, ImageView imageView, Context context) {
        StorageReference storageRef = storage.getReferenceFromUrl(gsUrl);

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Lấy đường dẫn public
                String downloadUrl = uri.toString();

                // Load hình ảnh từ đường dẫn public bằng Glide
                Glide.with(context)
                        .load(downloadUrl)
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Xử lý lỗi nếu không thể lấy được đường dẫn public
                Log.e("FirebaseStorage", "Error getting download URL", exception);
            }
        });
    }

    // Phương thức để lấy tất cả câu hỏi của một topic cụ thể
    public void getAllQuizzesForTopic(int topicId, OnAllQuizzesReceivedListener listener) {
        databaseReference.child("quizzes").orderByChild("topicId").equalTo(topicId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Quiz> quizzes = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Quiz quiz = snapshot.getValue(Quiz.class);
                            if (quiz != null) {
                                quizzes.add(quiz);
                            }
                        }
                        listener.onAllQuizzesReceived(quizzes);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listener.onError(databaseError.toException());
                    }
                });
    }

    // Functional interface for callback
    public interface OnAllQuizzesReceivedListener {
        void onAllQuizzesReceived(ArrayList<Quiz> quizzes);

        void onError(Exception exception);
    }

    // Phương thức trợ giúp để tìm ID lớn nhất trong một node cụ thể và thực hiện hành động
    private void findMaxIdAndPerformAction(String node, OnMaxIdFoundAction action) {
        databaseReference.child(node).orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int maxId = 0;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Integer id = child.child("id").getValue(Integer.class);
                        if (id != null && id > maxId) {
                            maxId = id;
                        }
                    }
                }
                action.performAction(maxId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    // Functional interface để xử lý hành động sau khi ID lớn nhất được tìm thấy
    private interface OnMaxIdFoundAction {
        void performAction(int maxId);
    }

    public void addQuiz(Quiz quiz) {
        findMaxIdAndPerformAction("quizzes", currentMaxId -> {
            int nextId = currentMaxId + 1;
            quiz.setId(nextId);
            databaseReference.child("quizzes").child(String.valueOf(nextId)).setValue(quiz);
        });
    }

    public void updateQuiz(String quizId, Quiz quiz) {
        databaseReference.child("quizzes").child(quizId).setValue(quiz);
    }

    public void deleteQuiz(String quizId) {
        databaseReference.child("quizzes").child(quizId).removeValue();
    }

    public void addUser(User user) {
        findMaxIdAndPerformAction("users", currentMaxId -> {
            int nextId = currentMaxId + 1;
            user.setId(nextId);
            databaseReference.child("users").child(String.valueOf(nextId)).setValue(user);
        });
    }

    public void updateUser(String userId, User user) {
        databaseReference.child("users").child(userId).setValue(user);
    }

    public void deleteUser(String userId) {
        databaseReference.child("users").child(userId).removeValue();
    }

    public void addRoom(Room room) {
        findMaxIdAndPerformAction("rooms", currentMaxId -> {
            int nextId = currentMaxId + 1;
            room.setId(nextId);
            databaseReference.child("rooms").child(String.valueOf(nextId)).setValue(room);
        });
    }

    public void updateRoom(String roomId, Room room) {
        databaseReference.child("rooms").child(roomId).setValue(room);
    }

    public void deleteRoom(String roomId) {
        databaseReference.child("rooms").child(roomId).removeValue();
    }

    public void addTopic(Topic topic) {
        findMaxIdAndPerformAction("topics", currentMaxId -> {
            int nextId = currentMaxId + 1;
            topic.setId(nextId);
            databaseReference.child("topics").child(String.valueOf(nextId)).setValue(topic);
        });
    }

    public void updateTopic(String topicId, Topic topic) {
        databaseReference.child("topics").child(topicId).setValue(topic);
    }

    public void deleteTopic(String topicId) {
        databaseReference.child("topics").child(topicId).removeValue();
    }

    public void addResult(Result result) {
        findMaxIdAndPerformAction("results", currentMaxId -> {
            int nextId = currentMaxId + 1;
            result.setId(nextId);
            databaseReference.child("results").child(String.valueOf(nextId)).setValue(result);
        });
    }

    public void updateResult(String resultId, Result result) {
        databaseReference.child("results").child(resultId).setValue(result);
    }

    public void deleteResult(String resultId) {
        databaseReference.child("results").child(resultId).removeValue();
    }

    public void addRoomUser(RoomUser roomUser) {
        findMaxIdAndPerformAction("room_users", currentMaxId -> {
            int nextId = currentMaxId + 1;
            roomUser.setId(nextId);
            databaseReference.child("room_users").child(String.valueOf(nextId)).setValue(roomUser);
        });
    }

    public void updateRoomUser(String roomUserId, RoomUser roomUser) {
        databaseReference.child("room_users").child(roomUserId).setValue(roomUser);
    }

    public void deleteRoomUser(String roomUserId) {
        databaseReference.child("room_users").child(roomUserId).removeValue();
    }
}
