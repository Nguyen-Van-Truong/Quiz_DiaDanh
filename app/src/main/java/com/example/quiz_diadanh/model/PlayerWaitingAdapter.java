package com.example.quiz_diadanh.model;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz_diadanh.R;

import java.util.Arrays;
import java.util.List;

public class PlayerWaitingAdapter extends RecyclerView.Adapter<PlayerWaitingAdapter.ViewHolder> {

    private List<Integer> imageList;

    public PlayerWaitingAdapter(List<Integer> imageList) {
        this.imageList = imageList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.player_waiting, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.listPlayerWaiting);
        imageList = Arrays.asList(
                R.drawable.user_image,
                R.drawable.user_image,
                R.drawable.user_image,
                R.drawable.user_image,
                R.drawable.user_image
        );

        PlayerWaitingAdapter playerListAdpater = new PlayerWaitingAdapter(imageList);
        recyclerView.setAdapter(playerListAdpater);

        return view;
    }
}

