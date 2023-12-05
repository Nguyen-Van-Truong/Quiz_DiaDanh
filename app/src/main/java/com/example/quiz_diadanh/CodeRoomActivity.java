package com.example.quiz_diadanh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quiz_diadanh.model.FirebaseService;
import com.example.quiz_diadanh.model.Room;
import com.example.quiz_diadanh.model.RoomUser;
import com.example.quiz_diadanh.model.User;
import com.example.quiz_diadanh.model.UserPreferences;

public class CodeRoomActivity extends AppCompatActivity {

    Button button;
    EditText editTextCode;
    FirebaseService firebaseService;
    int currentUserId; // You need to set this based on the logged-in user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coderoom);

        button = findViewById(R.id.button);
        editTextCode = findViewById(R.id.editText);
        firebaseService = new FirebaseService();

        UserPreferences userPreferences = new UserPreferences(this);
        User currentUser = userPreferences.getUser();
        currentUserId = currentUser.getId(); // Fetch and set the current user's ID

        button.setOnClickListener(v -> {
            String roomCode = editTextCode.getText().toString();
            if (!roomCode.isEmpty()) {
                checkRoomAndJoin(roomCode);
            } else {
                Toast.makeText(CodeRoomActivity.this, "Please enter a room code", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkRoomAndJoin(String code) {
        firebaseService.getRoomByCode(code, new FirebaseService.OnRoomReceivedListener() {
            @Override
            public void onRoomReceived(Room room) {
                // Use a ternary conditional expression to check the room status
                boolean isRoomOpen = room.getStatus().equals("open");
                if (isRoomOpen) joinRoom(room);
                else
                    Toast.makeText(CodeRoomActivity.this, "Room is no longer valid", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(CodeRoomActivity.this, "Room not found: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void joinRoom(Room room) {
        RoomUser roomUser = new RoomUser(0, room.getId(), "joined", currentUserId); // '0' for ID, to be set in FirebaseService
        firebaseService.addRoomUser(roomUser);

        Intent intent = new Intent(CodeRoomActivity.this, WaitingRoomActivity.class);
        intent.putExtra("room", room); // Pass the room object to the next activity
        startActivity(intent);
    }
}
