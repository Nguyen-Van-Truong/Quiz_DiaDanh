package com.example.quiz_diadanh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quiz_diadanh.model.FirebaseService;
import com.example.quiz_diadanh.model.Room;
import com.example.quiz_diadanh.model.RoomUser;
import com.example.quiz_diadanh.model.Topic;
import com.example.quiz_diadanh.model.UserPreferences;

import java.util.ArrayList;

public class ChooseTopicActivity extends AppCompatActivity {
    private static final int TOTAL_COLUMNS = 2;
    private static final int COLUMN_SPACING = 22;

    private GridLayout categoryContainer;
    private FirebaseService firebaseService;
    private UserPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_topic);

        initViews();
        initFirebaseService();
        loadActiveTopics();
    }

    private void initViews() {
        categoryContainer = findViewById(R.id.category_container);
        userPreferences = new UserPreferences(this);
    }

    private void initFirebaseService() {
        firebaseService = new FirebaseService();
    }

    private void loadActiveTopics() {
        firebaseService.getAllActiveTopics(new FirebaseService.OnActiveTopicsReceivedListener() {
            @Override
            public void onActiveTopicsReceived(ArrayList<Topic> activeTopics) {
                displayTopics(activeTopics);
            }

            @Override
            public void onError(Exception exception) {
                // Handle error
            }
        });
    }

    private void displayTopics(ArrayList<Topic> topics) {
        int row = 0, col = 0;

        categoryContainer.setRowCount(GridLayout.UNDEFINED);

        for (Topic topic : topics) {
            TextView textView = createTopicTextView(topic);
            GridLayout.LayoutParams params = createGridLayoutParameters(row, col);
            textView.setLayoutParams(params);

            textView.setOnClickListener(view -> handleTopicSelection(topic));

            categoryContainer.addView(textView);

            col = (col + 1) % TOTAL_COLUMNS;
            if (col == 0) row++;
        }
    }

    private TextView createTopicTextView(Topic topic) {
        TextView textView = new TextView(this);
        textView.setText(topic.getName());
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.category_button_background);
        textView.setPadding(10, 10, 10, 10);
        textView.setId(topic.getId());
        return textView;
    }

    private GridLayout.LayoutParams createGridLayoutParameters(int row, int col) {
        GridLayout.Spec rowSpec = GridLayout.spec(row);
        GridLayout.Spec colSpec = GridLayout.spec(col, GridLayout.FILL, 1f);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, colSpec);
        params.setMargins(0, 0, COLUMN_SPACING, 22);
        return params;
    }

    private void handleTopicSelection(Topic topic) {
        Integer creatorId = Integer.parseInt(userPreferences.getValueForKey(UserPreferences.KEY_USER_ID));
        createAndAddRoom(topic, creatorId);
    }

    private void createAndAddRoom(Topic topic, int creatorId) {
        Room newRoom = new Room();
        newRoom.setCreatorId(creatorId);
        newRoom.setPassword("");
        newRoom.setStatus("open");
        newRoom.setTimeLimit(10);
        newRoom.setTopicId(topic.getId());

        firebaseService.generateUniqueRoomCode(generatedCode -> {
            newRoom.setCode(generatedCode);
            addRoomAndNavigate(topic, creatorId, newRoom);
        });
    }

    private void addRoomAndNavigate(Topic topic, int creatorId, Room newRoom) {
        firebaseService.getMaxIdFromTable("rooms", maxIdRooms -> {
            int nextRoomId = maxIdRooms + 1;
            newRoom.setId(nextRoomId);  // Set the next ID to the new room

            RoomUser newRoomUser = new RoomUser();
            newRoomUser.setStatus("creator");
            newRoomUser.setUserId(creatorId);
            newRoomUser.setRoomId(nextRoomId); // Set the correct room ID for the new room user

            firebaseService.addRoomUser(newRoomUser);
            firebaseService.addRoom(newRoom); // Now addRoom has the correct room ID

            navigateToWaitingRoomActivity(topic, newRoom);
        });
    }


    private void navigateToWaitingRoomActivity(Topic topic, Room newRoom) {
        Intent intent = new Intent(ChooseTopicActivity.this, WaitingRoomActivity.class);
        intent.putExtra("topic_id", topic.getId());
        intent.putExtra("room", newRoom); // Put the Room object
        startActivity(intent);
    }

}

