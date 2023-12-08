package com.example.quiz_diadanh;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quiz_diadanh.adapter.RoomUserAdapter;
import com.example.quiz_diadanh.model.FirebaseService;
import com.example.quiz_diadanh.model.Room;
import com.example.quiz_diadanh.model.RoomUser;
import com.example.quiz_diadanh.model.User;
import com.example.quiz_diadanh.model.UserPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WaitingRoomActivity extends AppCompatActivity {
    Button buttonBegin;
    int roomId;
    int userId;
    private Room room;
    private FirebaseService firebaseService;
    private RecyclerView recyclerView;
    private RoomUserAdapter adapter;
    private ValueEventListener roomUsersListener;
    private Query roomUsersQuery;
    private ValueEventListener roomStatusListener;
    private DatabaseReference roomRef;
    EditText roomCodeEditText;
    private boolean isExiting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitingroom);

        buttonBegin = findViewById(R.id.buttonbegin);
        retrieveIntentData();

        buttonBegin.setOnClickListener(v -> navigateToQuizAnswerActivity());
        roomCodeEditText = findViewById(R.id.codeWaitingRoom);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RoomUserAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        firebaseService = new FirebaseService();

        setupRoomUsersListener(); // Set up the listener
        if (!isExiting) {
            attachListeners();
        }

        roomCodeEditText.setText(room.getCode());

        Intent incomingIntent = getIntent();
        handleIncomingIntent(incomingIntent);

        boolean userReturnedToRoom = getIntent().getBooleanExtra("userReturnedToRoom", false);
        if (userReturnedToRoom) {
            boolean a = isCreator(room.getCreatorId(), userId);
            if (isCreator(room.getCreatorId(), userId)) {
                firebaseService.updateStatusRoomUser(roomId + "", userId, "creator");
            } else {
                firebaseService.updateStatusRoomUser(roomId + "", userId, "joined");
            }
            checkAllParticipantsBackAndUpdateRoomStatus();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        detachListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) { // Use the appropriate request code
            if (resultCode == RESULT_OK && data != null) {
                String exitType = data.getStringExtra("exitType");
                if ("participantExit".equals(exitType)) {
                    exitRoomAsParticipant(roomId, userId);
                    isExiting = true;
                }
            }
        }
    }

    private void handleIncomingIntent(Intent intent) {
        if (intent != null && intent.hasExtra("room")) {
            room = (Room) intent.getSerializableExtra("room");
            roomId = room.getId();
            // Perform any other necessary updates based on the new intent
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIncomingIntent(intent);
    }

    private void checkAllParticipantsBackAndUpdateRoomStatus() {
        firebaseService.checkAllParticipantsBack(roomId + "", allBack -> {
            if (allBack) {
                firebaseService.updateRoomStatus(roomId + "", "open");
            }
        });
    }


    private void attachListeners() {
        if (roomUsersQuery != null) {
            roomUsersQuery.addValueEventListener(roomUsersListener);
        }

        roomRef = FirebaseDatabase.getInstance().getReference("rooms").child(String.valueOf(roomId));
        roomStatusListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Room updatedRoom = dataSnapshot.getValue(Room.class);
                if (updatedRoom != null && "playing".equals(updatedRoom.getStatus()) && userId != updatedRoom.getCreatorId()) {
                    // Navigate to QuizAnswerActivity for other users
                    Intent intent = new Intent(WaitingRoomActivity.this, QuizAnswerActivity.class);
                    intent.putExtra("room", room);
                    startActivityForResult(intent, 1);

                    if (room.getCreatorId() != userId)
                        roomRef.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "roomStatusListener:onCancelled", databaseError.toException());
            }
        };
        roomRef.addValueEventListener(roomStatusListener);
    }

    private void detachListeners() {
        if (roomUsersQuery != null && roomUsersListener != null) {
            roomUsersQuery.removeEventListener(roomUsersListener);
        }
        if (roomRef != null && roomStatusListener != null) {
            roomRef.removeEventListener(roomStatusListener);
        }
    }

    private void setupRoomUsersListener() {
        roomUsersQuery = FirebaseDatabase.getInstance().getReference("roomUsers").orderByChild("roomId").equalTo(roomId);

        roomUsersListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> userIds = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RoomUser roomUser = snapshot.getValue(RoomUser.class);
                    if (roomUser != null) {
                        userIds.add(roomUser.getUserId() + "");
                    }
                }
                fetchUserDetails(userIds);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "loadRoomUsers:onCancelled", databaseError.toException());
                Toast.makeText(WaitingRoomActivity.this, "Error loading room users: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        roomUsersQuery.addValueEventListener(roomUsersListener);
    }

    private void fetchUserDetails(ArrayList<String> userIds) {
        ArrayList<User> users = new ArrayList<>();
        for (String userId : userIds) {
            firebaseService.getUserById(userId, new FirebaseService.OnUserReceivedListener() {
                @Override
                public void onUserReceived(User user) {
                    if (user != null) {
                        users.add(user);
                        if (users.size() == userIds.size()) {
                            adapter.setUsers(users); // Update adapter with user details
                        }
                    }
                }

                @Override
                public void onError(Exception exception) {
                    // Handle error
                }
            });
        }
    }

    private void retrieveIntentData() {
        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            // Retrieve the Room object from the Intent
            room = (Room) receivedIntent.getSerializableExtra("room");

            // Use UserPreferences to get the stored user ID
            UserPreferences userPreferences = new UserPreferences(this);
            User user = userPreferences.getUser();
            userId = user.getId();

            // Debugging: Display the user information
            if (room != null) {
                roomId = room.getId();
            }
        }
    }

    private void navigateToQuizAnswerActivity() {
        if (room != null && isCreator(userId, room.getCreatorId())) {
            updateRoomStatusAndStartGame();
        } else {
            Toast.makeText(this, "Chỉ chủ phòng có thể bắt đầu", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isCreator(int userId, int room) {
        return userId == room;
    }

    private void updateRoomStatusAndStartGame() {
        DatabaseReference roomRef = FirebaseDatabase.getInstance().getReference("rooms").child(String.valueOf(roomId));
        roomRef.child("status").setValue("playing")
                .addOnSuccessListener(aVoid -> {
                    updateParticipantsStatus("playing");
                    // Start QuizAnswerActivity
                    Intent intent = new Intent(this, QuizAnswerActivity.class);
                    intent.putExtra("room", room);
                    startActivityForResult(intent, 1);
                })
                .addOnFailureListener(e -> Toast.makeText(WaitingRoomActivity.this, "Failed to start game", Toast.LENGTH_SHORT).show());
    }

    private void updateParticipantsStatus(String status) {
        DatabaseReference roomUsersRef = FirebaseDatabase.getInstance().getReference("roomUsers");
        Query query = roomUsersRef.orderByChild("roomId").equalTo(roomId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RoomUser roomUser = snapshot.getValue(RoomUser.class);
                    if (roomUser != null) {
                        snapshot.getRef().child("status").setValue(status);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "updateParticipantsStatus:onCancelled", databaseError.toException());
            }
        });
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
            if (isCreator(room.getCreatorId(), userId)) {
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
                            // Delete each room user entry
                            snapshot.getRef().removeValue()
                                    .addOnSuccessListener(aVoid -> Toast.makeText(WaitingRoomActivity.this, "Room user removed", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(WaitingRoomActivity.this, "Failed to remove room user", Toast.LENGTH_SHORT).show());
                        }
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Firebase", "loadRoomUsers:onCancelled", databaseError.toException());
                        Toast.makeText(WaitingRoomActivity.this, "Error loading room users: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void exitRoomAsParticipant(int roomId, int userId) {

        DatabaseReference roomUsersRef = FirebaseDatabase.getInstance().getReference("roomUsers");
        roomUsersRef.orderByChild("roomId").equalTo(roomId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Toast.makeText(WaitingRoomActivity.this, "No users found in room", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        boolean userFound = false;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            RoomUser roomUser = snapshot.getValue(RoomUser.class);
                            if (roomUser != null && roomUser.getUserId() == userId) {
                                // If you want to delete the entry:
                                snapshot.getRef().removeValue();

                                Toast.makeText(WaitingRoomActivity.this, "You have left the room", Toast.LENGTH_SHORT).show();
                                userFound = true;
                                break;
                            }
                        }
                        if (!userFound) {
                            Toast.makeText(WaitingRoomActivity.this, "User not found in room", Toast.LENGTH_SHORT).show();
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