package com.example.quiz_diadanh;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz_diadanh.adapter.ListCardViewAdapter;
import com.example.quiz_diadanh.widgets.UserModel;

import java.util.ArrayList;
import java.util.List;

public class ListPlayerView extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_waitingroom);
        //khoi tảoecycleView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        //Tao danh sach du lieu
        List<UserModel> datalist = new ArrayList<>();

        datalist.add(new UserModel(1,"Truc"));
        datalist.add(new UserModel(2,"Tuyet"));
        datalist.add(new UserModel(3,"Truong"));

        //khoi tao Adapter va thiet lap cho RecycleView
        ListCardViewAdapter adapter = new ListCardViewAdapter(datalist);
        recyclerView.setAdapter(adapter);

        //thiet lap LayoutManager cho RecycleView
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }
}
