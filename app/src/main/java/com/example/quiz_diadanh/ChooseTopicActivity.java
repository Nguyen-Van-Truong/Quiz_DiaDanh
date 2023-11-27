package com.example.quiz_diadanh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ChooseTopicActivity extends AppCompatActivity {
//    NavigationDrawerController drawerController;
    TextView category_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_topic);

//        drawerController = new NavigationDrawerController(this);
//        drawerController.setupDrawer();

        category_1 = findViewById(R.id.category_1);

        category_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseTopicActivity.this, WaitingRoomActivity.class);
                startActivity(intent);
            }
        });

    }
}