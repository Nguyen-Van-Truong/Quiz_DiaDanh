package com.example.quiz_diadanh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WaitingRoomActivity extends AppCompatActivity {
    Button buttonbegin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitingroom);

        buttonbegin = findViewById(R.id.buttonbegin);

        buttonbegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WaitingRoomActivity.this, QuizAnswerActivity.class);
                startActivity(intent);
            }
        });

    }
}