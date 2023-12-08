package com.example.quiz_diadanh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.quiz_diadanh.adapter.RankingAdapter;
import com.example.quiz_diadanh.model.FirebaseService;
import com.example.quiz_diadanh.model.Result;
import com.example.quiz_diadanh.model.Room;
import com.example.quiz_diadanh.model.User;
import com.example.quiz_diadanh.model.UserPreferences;
import com.example.quiz_diadanh.widgets.RankingItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankingActivity extends AppCompatActivity {
    private ListView rankingListView;
    private FirebaseService firebaseService;
    private int roomId; // Assume this is the room ID for which you want to show the rankings
    private Room room;
    Button btnBackToWaitingRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        rankingListView = findViewById(R.id.rankingListView);
        firebaseService = new FirebaseService();
        retrieveIntentData();
        loadRankings();
        btnBackToWaitingRoom = findViewById(R.id.btnBackToWaitingRoom);

        btnBackToWaitingRoom.setOnClickListener(v -> {
            Intent intent = new Intent(RankingActivity.this, WaitingRoomActivity.class);
            intent.putExtra("userReturnedToRoom", true);
            intent.putExtra("room", room);
            startActivity(intent);
            finish();
        });

        UserPreferences userPreferences = new UserPreferences(this);
        User user = userPreferences.getUser();
        int userId = user.getId();
        if (userId == room.getCreatorId())
            firebaseService.resetRoomUsersStatus(roomId, userId);


    }

    private void retrieveIntentData() {
        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            room = (Room) receivedIntent.getSerializableExtra("room");

            if (room != null) {
                roomId = room.getId();
            }
        }
    }

    private void loadRankings() {
        firebaseService.getResultsForRoom(roomId, new FirebaseService.OnResultsReceivedListener() {
            @Override
            public void onResultsReceived(List<Result> results) {
                calculateRankings(results);
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(RankingActivity.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculateRankings(List<Result> results) {
        Map<Integer, Integer> userScores = new HashMap<>();
        for (Result result : results) {
            int userId = result.getUserId();
            int score = result.getScore();
            userScores.put(userId, userScores.getOrDefault(userId, 0) + score);
        }

        // Convert the map to a list of RankingItems
        List<RankingItem> rankingItems = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : userScores.entrySet()) {
            int userId = entry.getKey();
            int totalScore = entry.getValue();

            // Retrieve user name asynchronously
            firebaseService.getFullNameById(userId, new FirebaseService.OnFullNameReceivedListener() {
                @Override
                public void onFullNameReceived(String fullName) {
                    // Determine the icon based on current size of rankingItems (which represents rank)
                    int rank = rankingItems.size() + 1;
                    int iconResId;
                    switch (rank) {
                        case 1:
                            iconResId = R.drawable.ic_rank1;
                            break;
                        case 2:
                            iconResId = R.drawable.ic_rank2;
                            break;
                        case 3:
                            iconResId = R.drawable.ic_rank3;
                            break;
                        default:
                            iconResId = 0; // No icon for ranks 4 and onwards
                            break;
                    }
                    rankingItems.add(new RankingItem(iconResId, fullName, totalScore));

                    // Check if all names have been retrieved
                    if (rankingItems.size() == userScores.size()) {
                        // Sort the list based on score
                        Collections.sort(rankingItems, (item1, item2) -> Integer.compare(item2.getScore(), item1.getScore()));
                        // Set the adapter after retrieving all names
                        RankingAdapter adapter = new RankingAdapter(RankingActivity.this, rankingItems);
                        rankingListView.setAdapter(adapter);
                    }
                }

                @Override
                public void onError(Exception exception) {
                    Toast.makeText(RankingActivity.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void demo() {
        List<RankingItem> rankingItemList = new ArrayList<>();

        // Thêm dữ liệu cho bảng xếp hạng
        rankingItemList.add(new RankingItem(R.drawable.ic_rank1, "Người 1", 1000));
        rankingItemList.add(new RankingItem(R.drawable.ic_rank2, "Người 2", 900));
        rankingItemList.add(new RankingItem(R.drawable.ic_rank3, "Người 3", 800));
        rankingItemList.add(new RankingItem(0, "Người 4", 700));
        rankingItemList.add(new RankingItem(0, "Người 4", 700));
        rankingItemList.add(new RankingItem(0, "Người 4", 700));

        RankingAdapter adapter = new RankingAdapter(this, rankingItemList);
        rankingListView.setAdapter(adapter);
    }
}