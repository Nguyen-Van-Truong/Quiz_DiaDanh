package com.example.quiz_diadanh.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz_diadanh.R;
import com.example.quiz_diadanh.widgets.UserModel;

import java.util.List;

public class ListCardViewAdapter extends RecyclerView.Adapter<ListCardViewAdapter.ViewHolder> {

    private List<UserModel> dataList;

    // Constructor
    public ListCardViewAdapter(List<UserModel> dataList) {
        this.dataList = dataList;
    }

    // ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    @Override
    public ListCardViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // Create a new card view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserModel data = dataList.get(position);
        Log.d("UserModel", "Username: " + data.getUsername() + ", SrcImage: " + data.getSrcImage());

        TextView itemNameTextView = holder.cardView.findViewById(R.id.txtName);
        itemNameTextView.setText(data.getUsername());

        ImageView itemImageView = holder.cardView.findViewById(R.id.avatar);
        itemImageView.setImageResource(data.getSrcImage());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}