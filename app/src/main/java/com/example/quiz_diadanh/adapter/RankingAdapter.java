package com.example.quiz_diadanh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quiz_diadanh.R;
import com.example.quiz_diadanh.widgets.RankingItem;

import java.util.List;

public class RankingAdapter extends ArrayAdapter<RankingItem> {
    private Context context;
    private List<RankingItem> rankingItemList;

    public RankingAdapter(Context context, List<RankingItem> rankingItemList) {
        super(context, 0, rankingItemList);
        this.context = context;
        this.rankingItemList = rankingItemList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.ranking_item, parent, false);
        }

        RankingItem item = rankingItemList.get(position);

        ImageView iconImageView = convertView.findViewById(R.id.iconImageView);
        TextView rankTextView = convertView.findViewById(R.id.rankTextView); // New TextView for rank
        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        TextView scoreTextView = convertView.findViewById(R.id.scoreTextView);

        if (item.getIconResId() != 0) {
            iconImageView.setVisibility(View.VISIBLE);
            rankTextView.setVisibility(View.GONE);
            iconImageView.setImageResource(item.getIconResId());
        } else {
            iconImageView.setVisibility(View.GONE);
            rankTextView.setVisibility(View.VISIBLE);
            rankTextView.setText(String.valueOf(position + 1)); // Displaying the rank number
        }
        nameTextView.setText(item.getName());
        scoreTextView.setText(String.valueOf(item.getScore()));

        return convertView;
    }
}