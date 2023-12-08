package com.example.quiz_diadanh.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz_diadanh.R;
import com.example.quiz_diadanh.model.RoomUser;
import com.example.quiz_diadanh.model.User;

import java.util.ArrayList;

public class RoomUserAdapter extends RecyclerView.Adapter<RoomUserAdapter.ViewHolder> {
    private ArrayList<User> users; // Change to store User objects

    public RoomUserAdapter(ArrayList<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.userNameTextView.setText((position + 1) + ". " + user.getFullName());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView;

        ViewHolder(View view) {
            super(view);
            userNameTextView = view.findViewById(R.id.userNameTextView);
        }
    }
}
