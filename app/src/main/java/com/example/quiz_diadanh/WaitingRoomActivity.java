package com.example.quiz_diadanh;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.quiz_diadanh.model.Room;
import com.example.quiz_diadanh.model.RoomUser;
import com.example.quiz_diadanh.model.User;
import com.example.quiz_diadanh.model.UserPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WaitingRoomActivity extends AppCompatActivity {
    Button buttonBegin;
    int roomId;
    int userId;
    int topicId;
    private Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitingroom);

        buttonBegin = findViewById(R.id.buttonbegin);
        retrieveIntentData();

        buttonBegin.setOnClickListener(v -> navigateToQuizAnswerActivity());
    }

    private void retrieveIntentData() {
        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            // Retrieve the Room object from the Intent
            room = (Room) receivedIntent.getSerializableExtra("room");

            // Use UserPreferences to get the stored user ID
            UserPreferences userPreferences = new UserPreferences(this);
            User user = userPreferences.getUser();
            userId = user.getId(); // Update to use the ID from UserPreferences

            // Debugging: Display the user information
            if (room != null) {
                roomId = room.getId();
            }

            if (receivedIntent.hasExtra("topic_id")) {
                topicId = receivedIntent.getIntExtra("topic_id", -1);
            }
        }
    }


    private void navigateToQuizAnswerActivity() {
        // Check if the current user is the room creator
        if (room != null && userId == room.getCreatorId()) {
            // If the user is the room creator, start QuizAnswerActivity
            Intent intent = new Intent(this, QuizAnswerActivity.class);
            intent.putExtra("topic_id", topicId);
            startActivity(intent);
        } else {
            // If the user is not the room creator, show a toast message
            Toast.makeText(this, "Chỉ chủ phòng có thể bắt đầu", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onBackPressed() {
        showExitConfirmationDialog();
    }

    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit Room").setMessage("Do you want to leave the room?").setPositiveButton("Yes", (dialog, which) -> handleExit()).setNegativeButton("No", (dialog, which) -> dialog.dismiss()).create().show();
    }

    private void handleExit() {
        if (room != null) {
            if (room.getCreatorId() == userId) {
                exitRoomAsCreator(roomId);
            } else {
                exitRoomAsParticipant(roomId, userId);
            }
        } else {
            // Handle the case where the room object is null
            Toast.makeText(WaitingRoomActivity.this, "Room not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void exitRoomAsCreator(final int roomId) {
        // Reference to the 'rooms' node
        DatabaseReference roomRef = FirebaseDatabase.getInstance().getReference("rooms").child(room.getId() + "");
        roomRef.child("status").setValue("closed") // Update the room status to 'closed'
                .addOnSuccessListener(aVoid -> Toast.makeText(WaitingRoomActivity.this, "Room status updated to closed", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(WaitingRoomActivity.this, "Failed to update room status", Toast.LENGTH_SHORT).show());

        // Reference to the 'roomUsers' node
        DatabaseReference roomUsersRef = FirebaseDatabase.getInstance().getReference("roomUsers");
        roomUsersRef.orderByChild("roomId").equalTo(roomId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Toast.makeText(WaitingRoomActivity.this, "No users found in room", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            RoomUser roomUser = snapshot.getValue(RoomUser.class);
                            if (roomUser != null && roomUser.getRoomId() == roomId) {
                                snapshot.getRef().child("status").setValue("invalid");
                                Toast.makeText(WaitingRoomActivity.this, "User status set to invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                        finish(); // Close the activity
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Firebase", "loadRoomUsers:onCancelled", databaseError.toException());
                        Toast.makeText(WaitingRoomActivity.this, "Error loading room users: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void exitRoomAsParticipant(int roomId, int userId) {
        // Reference to the 'roomUsers' node
        DatabaseReference roomUsersRef = FirebaseDatabase.getInstance().getReference("roomUsers");
        roomUsersRef.orderByChild("roomId").equalTo(roomId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Toast.makeText(WaitingRoomActivity.this, "No users found in room", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            RoomUser roomUser = snapshot.getValue(RoomUser.class);
                            if (roomUser != null && roomUser.getUserId() == userId) {
                                snapshot.getRef().child("status").setValue("invalid"); // Cập nhật trạng thái của người dùng thành 'invalid'
                                Toast.makeText(WaitingRoomActivity.this, "User status set to invalid", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                        finish(); // Close the activity
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Firebase", "loadRoomUsers:onCancelled", databaseError.toException());
                        Toast.makeText(WaitingRoomActivity.this, "Error loading room users: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
