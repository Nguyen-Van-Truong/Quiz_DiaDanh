package com.example.quiz_diadanh;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.quiz_diadanh.model.RankingItem;

import java.util.ArrayList;
import java.util.List;

public class RankingActivity extends AppCompatActivity {
    private ListView rankingListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        rankingListView = findViewById(R.id.rankingListView);

        List<RankingItem> rankingItemList = new ArrayList<>();

        // Thêm dữ liệu cho bảng xếp hạng
        rankingItemList.add(new RankingItem(R.drawable.ic_rank1, "Người 1", 1000));
        rankingItemList.add(new RankingItem(R.drawable.ic_rank2, "Người 2", 900));
        rankingItemList.add(new RankingItem(R.drawable.ic_rank3, "Người 3", 800));
        rankingItemList.add(new RankingItem(R.drawable.ic_rank4, "Người 4", 700));

        RankingAdapter adapter = new RankingAdapter(this, rankingItemList);
        rankingListView.setAdapter(adapter);
    }
}

