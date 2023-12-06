package com.example.quiz_diadanh.model;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.quiz_diadanh.WaitingRoomActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class FirebaseService {

    private DatabaseReference databaseReference;
    private FirebaseStorage storage;

    public FirebaseService() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
    }

    public void getUserByEmail(String email, OnUserReceivedListener listener) {
        DatabaseReference usersRef = databaseReference.child("users");
        Query query = usersRef.orderByChild("email").equalTo(email);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            listener.onUserReceived(user);
                            return;
                        }
                    }
                } else {
                    listener.onError(new Exception("User not found"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onError(databaseError.toException());
            }
        });
    }
    public void getRoomUsersByStatus(int roomId, OnRoomUsersReceivedListener listener) {
        DatabaseReference roomUsersRef = databaseReference.child("roomUsers");
        Query query = roomUsersRef.orderByChild("roomId").equalTo(roomId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<RoomUser> roomUsers = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RoomUser roomUser = snapshot.getValue(RoomUser.class);
                    if (roomUser != null && ("creator".equals(roomUser.getStatus()) || "joined".equals(roomUser.getStatus()))) {
                        roomUsers.add(roomUser);
                    }
                }
                listener.onRoomUsersReceived(roomUsers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onError(databaseError.toException());
            }
        });
    }

    // Functional interface for callback
    public interface OnRoomUsersReceivedListener {
        void onRoomUsersReceived(ArrayList<RoomUser> roomUsers);
        void onError(Exception exception);
    }

    public void getRoomByCode(String code, OnRoomReceivedListener listener) {
        databaseReference.child("rooms").orderByChild("code").equalTo(code)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Room room = snapshot.getValue(Room.class);
                                if (room != null) {
                                    listener.onRoomReceived(room);
                                    return;
                                }
                            }
                        }
                        listener.onError(new Exception("Room not found"));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        listener.onError(databaseError.toException());
                    }
                });
    }

    // Functional interface for callback
    public interface OnRoomReceivedListener {
        void onRoomReceived(Room room);
        void onError(Exception exception);
    }

    public void getAllUsersInRoom(String roomId, OnUsersInRoomReceivedListener listener) {
        DatabaseReference roomUsersRef = databaseReference.child("roomUsers");

        // Query the roomUsers node to get all users in the specified room
        Query roomQuery = roomUsersRef.orderByChild("roomId").equalTo(roomId);

        roomQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<User> usersInRoom = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RoomUser roomUser = snapshot.getValue(RoomUser.class);
                    if (roomUser != null) {
                        // Retrieve the user information based on the userId in the roomUser
                        String userId = roomUser.getUserId() + "";
                        getUserById(userId, new OnUserReceivedListener() {
                            @Override
                            public void onUserReceived(User user) {
                                if (user != null) {
                                    usersInRoom.add(user);
                                    // Check if this is the last user in the room
                                    if (usersInRoom.size() == dataSnapshot.getChildrenCount()) {
                                        listener.onUsersInRoomReceived(usersInRoom);
                                    }
                                }
                            }

                            @Override
                            public void onError(Exception exception) {
                                // Handle the error if user retrieval fails
                                // You can choose to ignore the error or take appropriate action
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onError(databaseError.toException());
            }
        });
    }

    // Functional interface for callback
    public interface OnUsersInRoomReceivedListener {
        void onUsersInRoomReceived(ArrayList<User> users);

        void onError(Exception exception);
    }

    public void getUserById(String userId, OnUserReceivedListener listener) {
        databaseReference.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    listener.onUserReceived(user);
                } else {
                    listener.onError(new Exception("User not found"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onError(databaseError.toException());
            }
        });
    }

    // Functional interface for callback
    public interface OnUserReceivedListener {
        void onUserReceived(User user);

        void onError(Exception exception);
    }

    public void getAllActiveTopics(OnActiveTopicsReceivedListener listener) {
        Query activeTopicsQuery = databaseReference.child("topics").orderByChild("status").equalTo("active");

        activeTopicsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Topic> activeTopics = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Topic topic = snapshot.getValue(Topic.class);
                    if (topic != null) {
                        activeTopics.add(topic);
                    }
                }
                listener.onActiveTopicsReceived(activeTopics);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onError(databaseError.toException());
            }
        });
    }

    // Functional interface for callback
    public interface OnActiveTopicsReceivedListener {
        void onActiveTopicsReceived(ArrayList<Topic> activeTopics);

        void onError(Exception exception);
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

                // Kiểm tra xem Activity còn tồn tại hay không
                if (context instanceof Activity && !((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                    // Load hình ảnh từ đường dẫn public bằng Glide
                    Glide.with(context)
                            .load(downloadUrl)
                            .into(imageView);
                }
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
        findMaxIdAndPerformAction("roomUsers", currentMaxId -> {
            int nextId = currentMaxId + 1;
            roomUser.setId(nextId);
            databaseReference.child("roomUsers").child(String.valueOf(nextId)).setValue(roomUser);
        });
    }

    public void updateRoomUser(String roomUserId, RoomUser roomUser) {
        databaseReference.child("room_users").child(roomUserId).setValue(roomUser);
    }

    public void deleteRoomUser(String roomUserId) {
        databaseReference.child("room_users").child(roomUserId).removeValue();
    }

    private void getMaxIdFromNode(String node, OnMaxIdReceivedListener listener) {
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
                listener.onMaxIdReceived(maxId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    // Functional interface để xử lý hành động sau khi ID lớn nhất được tìm thấy
    public interface OnMaxIdReceivedListener {
        void onMaxIdReceived(int maxId);
    }

    // Phương thức để lấy ID lớn nhất từ một bảng cụ thể
    public void getMaxIdFromTable(String tableName, OnMaxIdReceivedListener listener) {
        getMaxIdFromNode(tableName, listener);
    }

    public void generateUniqueRoomCode(OnUniqueRoomCodeGeneratedListener listener) {
        generateCodeRecursively(listener, new StringBuilder());
    }

    private void generateCodeRecursively(OnUniqueRoomCodeGeneratedListener listener, StringBuilder generatedCode) {
        if (generatedCode.length() == 6) {
            checkCodeUniqueness(generatedCode.toString(), isUnique -> {
                if (isUnique) {
                    listener.onUniqueRoomCodeGenerated(generatedCode.toString());
                } else {
                    generateCodeRecursively(listener, new StringBuilder()); // Gọi đệ quy để tạo mã mới nếu mã không duy nhất
                }
            });
        } else {
            Random random = new Random();
            generatedCode.append(random.nextInt(10)); // Sinh số ngẫu nhiên từ 0 đến 9
            generateCodeRecursively(listener, generatedCode); // Tiếp tục sinh mã
        }
    }


    private void checkCodeUniqueness(String generatedCode, OnCodeUniquenessCheckedListener listener) {
        databaseReference.child("rooms").orderByChild("name").equalTo(generatedCode)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listener.onCodeUniquenessChecked(!dataSnapshot.exists());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý lỗi nếu cần
                    }
                });
    }

    public interface OnUniqueRoomCodeGeneratedListener {
        void onUniqueRoomCodeGenerated(String generatedCode);
    }

    private interface OnCodeUniquenessCheckedListener {
        void onCodeUniquenessChecked(boolean isUnique);
    }
}
